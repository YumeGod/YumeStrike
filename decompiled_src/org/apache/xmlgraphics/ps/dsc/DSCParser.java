package org.apache.xmlgraphics.ps.dsc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.xmlgraphics.ps.DSCConstants;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.apache.xmlgraphics.ps.dsc.events.DSCAtend;
import org.apache.xmlgraphics.ps.dsc.events.DSCComment;
import org.apache.xmlgraphics.ps.dsc.events.DSCEvent;
import org.apache.xmlgraphics.ps.dsc.events.DSCHeaderComment;
import org.apache.xmlgraphics.ps.dsc.events.PostScriptComment;
import org.apache.xmlgraphics.ps.dsc.events.PostScriptLine;
import org.apache.xmlgraphics.ps.dsc.events.UnparsedDSCComment;
import org.apache.xmlgraphics.ps.dsc.tools.DSCTools;

public class DSCParser implements DSCParserConstants {
   private InputStream in;
   private BufferedReader reader;
   private boolean eofFound = false;
   private boolean checkEOF = true;
   private DSCEvent currentEvent;
   private DSCEvent nextEvent;
   private DSCListener nestedDocumentHandler;
   private DSCListener filterListener;
   private List listeners;
   private boolean listenersDisabled = false;

   public DSCParser(InputStream in) throws IOException, DSCException {
      if (in.markSupported()) {
         this.in = in;
      } else {
         this.in = new BufferedInputStream(this.in);
      }

      String encoding = "US-ASCII";

      try {
         this.reader = new BufferedReader(new InputStreamReader(this.in, encoding));
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException("Incompatible VM! " + var4.getMessage());
      }

      this.parseNext();
   }

   public InputStream getInputStream() {
      return this.in;
   }

   protected void warn(String msg) {
      System.err.println(msg);
   }

   protected String readLine() throws IOException, DSCException {
      String line = this.reader.readLine();
      this.checkLine(line);
      return line;
   }

   private void checkLine(String line) throws DSCException {
      if (line == null) {
         if (!this.eofFound) {
            throw new DSCException("%%EOF not found. File is not well-formed.");
         }
      } else if (line.length() > 255) {
         this.warn("Line longer than 255 characters. This file is not fully PostScript conforming.");
      }

   }

   private final boolean isWhitespace(char c) {
      return c == ' ' || c == '\t';
   }

   private DSCComment parseDSCLine(String line) throws IOException, DSCException {
      int colon = line.indexOf(58);
      String value = "";
      String name;
      if (colon > 0) {
         name = line.substring(2, colon);
         int startOfValue = colon + 1;
         if (startOfValue < line.length()) {
            if (this.isWhitespace(line.charAt(startOfValue))) {
               ++startOfValue;
            }

            value = line.substring(startOfValue).trim();
            if (value.equals(DSCConstants.ATEND.toString())) {
               return new DSCAtend(name);
            }
         }

         while(true) {
            this.reader.mark(512);
            String nextLine = this.readLine();
            if (nextLine == null || !nextLine.startsWith("%%+")) {
               this.reader.reset();
               break;
            }

            value = value + nextLine.substring(3);
         }
      } else {
         name = line.substring(2);
         value = null;
      }

      return this.parseDSCComment(name, value);
   }

   private DSCComment parseDSCComment(String name, String value) {
      DSCComment parsed = DSCCommentFactory.createDSCCommentFor(name);
      if (parsed != null) {
         try {
            parsed.parseValue(value);
            return parsed;
         } catch (Exception var5) {
         }
      }

      UnparsedDSCComment unparsed = new UnparsedDSCComment(name);
      unparsed.parseValue(value);
      return unparsed;
   }

   public void parse(DSCHandler handler) throws IOException, DSCException {
      DSCHeaderComment header = DSCTools.checkAndSkipDSC30Header(this);
      handler.startDocument("%!" + header.getComment());

      while(this.hasNext()) {
         DSCEvent event = this.nextEvent();
         switch (event.getEventType()) {
            case 0:
               handler.startDocument("%!" + ((DSCHeaderComment)event).getComment());
               break;
            case 1:
               handler.handleDSCComment(event.asDSCComment());
               break;
            case 2:
               handler.comment(((PostScriptComment)event).getComment());
               break;
            case 3:
               handler.line(this.getLine());
               break;
            case 4:
               if (this.isCheckEOF()) {
                  this.eofFound = true;
               }

               handler.endDocument();
               break;
            default:
               throw new IllegalStateException("Illegal event type: " + event.getEventType());
         }
      }

   }

   public boolean hasNext() {
      return this.nextEvent != null;
   }

   public int next() throws IOException, DSCException {
      if (this.hasNext()) {
         this.currentEvent = this.nextEvent;
         this.parseNext();
         this.processListeners();
         return this.currentEvent.getEventType();
      } else {
         throw new NoSuchElementException("There are no more events");
      }
   }

   private void processListeners() throws IOException, DSCException {
      if (!this.isListenersDisabled()) {
         if (this.filterListener != null) {
            this.filterListener.processEvent(this.currentEvent, this);
         }

         if (this.listeners != null) {
            Iterator iter = this.listeners.iterator();

            while(iter.hasNext()) {
               ((DSCListener)iter.next()).processEvent(this.currentEvent, this);
            }
         }

      }
   }

   public DSCEvent nextEvent() throws IOException, DSCException {
      this.next();
      return this.getCurrentEvent();
   }

   public DSCEvent getCurrentEvent() {
      return this.currentEvent;
   }

   public DSCEvent peek() {
      return this.nextEvent;
   }

   protected void parseNext() throws IOException, DSCException {
      String line = this.readLine();
      if (line != null) {
         if (this.eofFound && line.length() > 0) {
            throw new DSCException("Content found after EOF");
         }

         if (line.startsWith("%%")) {
            DSCComment comment = this.parseDSCLine(line);
            if (comment.getEventType() == 4 && this.isCheckEOF()) {
               this.eofFound = true;
            }

            this.nextEvent = comment;
         } else if (line.startsWith("%!")) {
            this.nextEvent = new DSCHeaderComment(line.substring(2));
         } else if (line.startsWith("%")) {
            this.nextEvent = new PostScriptComment(line.substring(1));
         } else {
            this.nextEvent = new PostScriptLine(line);
         }
      } else {
         this.nextEvent = null;
      }

   }

   public String getLine() {
      if (this.currentEvent.getEventType() == 3) {
         return ((PostScriptLine)this.currentEvent).getLine();
      } else {
         throw new IllegalStateException("Current event is not a PostScript line");
      }
   }

   public DSCComment nextDSCComment(String name) throws IOException, DSCException {
      return this.nextDSCComment(name, (PSGenerator)null);
   }

   public DSCComment nextDSCComment(String name, PSGenerator gen) throws IOException, DSCException {
      while(this.hasNext()) {
         DSCEvent event = this.nextEvent();
         if (event.isDSCComment()) {
            DSCComment comment = event.asDSCComment();
            if (name.equals(comment.getName())) {
               return comment;
            }
         }

         if (gen != null) {
            event.generate(gen);
         }
      }

      return null;
   }

   public PostScriptComment nextPSComment(String prefix, PSGenerator gen) throws IOException, DSCException {
      while(this.hasNext()) {
         DSCEvent event = this.nextEvent();
         if (event.isComment()) {
            PostScriptComment comment = (PostScriptComment)event;
            if (comment.getComment().startsWith(prefix)) {
               return comment;
            }
         }

         if (gen != null) {
            event.generate(gen);
         }
      }

      return null;
   }

   public void setFilter(DSCFilter filter) {
      if (filter != null) {
         this.filterListener = new FilteringEventListener(filter);
      } else {
         this.filterListener = null;
      }

   }

   public void addListener(DSCListener listener) {
      if (listener == null) {
         throw new NullPointerException("listener must not be null");
      } else {
         if (this.listeners == null) {
            this.listeners = new ArrayList();
         }

         this.listeners.add(listener);
      }
   }

   public void removeListener(DSCListener listener) {
      if (this.listeners != null) {
         this.listeners.remove(listener);
      }

   }

   public void setListenersDisabled(boolean value) {
      this.listenersDisabled = value;
   }

   public boolean isListenersDisabled() {
      return this.listenersDisabled;
   }

   public void setNestedDocumentHandler(final NestedDocumentHandler handler) {
      if (handler == null) {
         this.removeListener(this.nestedDocumentHandler);
      } else {
         this.addListener(new DSCListener() {
            public void processEvent(DSCEvent event, DSCParser parser) throws IOException, DSCException {
               handler.handle(event, parser);
            }
         });
      }

   }

   public void setCheckEOF(boolean value) {
      this.checkEOF = value;
   }

   public boolean isCheckEOF() {
      return this.checkEOF;
   }
}

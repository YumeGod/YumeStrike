package org.apache.xmlgraphics.ps.dsc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.xmlgraphics.ps.dsc.events.DSCComment;

public class EventRecorder implements DSCHandler {
   private List events = new ArrayList();

   public void replay(DSCHandler handler) throws IOException {
      Iterator iter = this.events.iterator();

      while(iter.hasNext()) {
         Object obj = iter.next();
         if (obj instanceof PSLine) {
            handler.line(((PSLine)obj).getLine());
         } else if (obj instanceof PSComment) {
            handler.comment(((PSComment)obj).getComment());
         } else {
            if (!(obj instanceof DSCComment)) {
               throw new IllegalStateException("Unsupported class type");
            }

            handler.handleDSCComment((DSCComment)obj);
         }
      }

   }

   public void comment(String comment) throws IOException {
      this.events.add(new PSComment(comment));
   }

   public void handleDSCComment(DSCComment comment) throws IOException {
      this.events.add(comment);
   }

   public void line(String line) throws IOException {
      this.events.add(new PSLine(line));
   }

   public void startDocument(String header) throws IOException {
      throw new UnsupportedOperationException(this.getClass().getName() + " is only used to handle parts of a document");
   }

   public void endDocument() throws IOException {
      throw new UnsupportedOperationException(this.getClass().getName() + " is only used to handle parts of a document");
   }

   private static class PSLine {
      private String line;

      public PSLine(String line) {
         this.line = line;
      }

      public String getLine() {
         return this.line;
      }
   }

   private static class PSComment {
      private String comment;

      public PSComment(String comment) {
         this.comment = comment;
      }

      public String getComment() {
         return this.comment;
      }
   }
}

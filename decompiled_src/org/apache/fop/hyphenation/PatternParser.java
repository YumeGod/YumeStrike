package org.apache.fop.hyphenation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class PatternParser extends DefaultHandler implements PatternConsumer {
   private XMLReader parser;
   private int currElement;
   private PatternConsumer consumer;
   private StringBuffer token;
   private ArrayList exception;
   private char hyphenChar;
   private String errMsg;
   private boolean hasClasses;
   static final int ELEM_CLASSES = 1;
   static final int ELEM_EXCEPTIONS = 2;
   static final int ELEM_PATTERNS = 3;
   static final int ELEM_HYPHEN = 4;
   private PrintStream testOut;

   public PatternParser() throws HyphenationException {
      this.hasClasses = false;
      this.testOut = System.out;
      this.consumer = this;
      this.token = new StringBuffer();
      this.parser = createParser();
      this.parser.setContentHandler(this);
      this.parser.setErrorHandler(this);
      this.hyphenChar = '-';
   }

   public PatternParser(PatternConsumer consumer) throws HyphenationException {
      this();
      this.consumer = consumer;
   }

   public void parse(String filename) throws HyphenationException {
      this.parse(new File(filename));
   }

   public void parse(File file) throws HyphenationException {
      try {
         InputSource src = new InputSource(file.toURI().toURL().toExternalForm());
         this.parse(src);
      } catch (MalformedURLException var3) {
         throw new HyphenationException("Error converting the File '" + file + "' to a URL: " + var3.getMessage());
      }
   }

   public void parse(InputSource source) throws HyphenationException {
      try {
         this.parser.parse(source);
      } catch (FileNotFoundException var3) {
         throw new HyphenationException("File not found: " + var3.getMessage());
      } catch (IOException var4) {
         throw new HyphenationException(var4.getMessage());
      } catch (SAXException var5) {
         throw new HyphenationException(this.errMsg);
      }
   }

   static XMLReader createParser() {
      try {
         SAXParserFactory factory = SAXParserFactory.newInstance();
         factory.setNamespaceAware(true);
         return factory.newSAXParser().getXMLReader();
      } catch (Exception var1) {
         throw new RuntimeException("Couldn't create XMLReader: " + var1.getMessage());
      }
   }

   protected String readToken(StringBuffer chars) {
      boolean space = false;

      int i;
      for(i = 0; i < chars.length() && Character.isWhitespace(chars.charAt(i)); ++i) {
         space = true;
      }

      String word;
      int countr;
      if (space) {
         for(countr = i; countr < chars.length(); ++countr) {
            chars.setCharAt(countr - i, chars.charAt(countr));
         }

         chars.setLength(chars.length() - i);
         if (this.token.length() > 0) {
            word = this.token.toString();
            this.token.setLength(0);
            return word;
         }
      }

      space = false;

      for(i = 0; i < chars.length(); ++i) {
         if (Character.isWhitespace(chars.charAt(i))) {
            space = true;
            break;
         }
      }

      this.token.append(chars.toString().substring(0, i));

      for(countr = i; countr < chars.length(); ++countr) {
         chars.setCharAt(countr - i, chars.charAt(countr));
      }

      chars.setLength(chars.length() - i);
      if (space) {
         word = this.token.toString();
         this.token.setLength(0);
         return word;
      } else {
         this.token.append(chars);
         return null;
      }
   }

   protected static String getPattern(String word) {
      StringBuffer pat = new StringBuffer();
      int len = word.length();

      for(int i = 0; i < len; ++i) {
         if (!Character.isDigit(word.charAt(i))) {
            pat.append(word.charAt(i));
         }
      }

      return pat.toString();
   }

   protected ArrayList normalizeException(ArrayList ex) {
      ArrayList res = new ArrayList();

      for(int i = 0; i < ex.size(); ++i) {
         Object item = ex.get(i);
         if (!(item instanceof String)) {
            res.add(item);
         } else {
            String str = (String)item;
            StringBuffer buf = new StringBuffer();

            for(int j = 0; j < str.length(); ++j) {
               char c = str.charAt(j);
               if (c != this.hyphenChar) {
                  buf.append(c);
               } else {
                  res.add(buf.toString());
                  buf.setLength(0);
                  char[] h = new char[]{this.hyphenChar};
                  res.add(new Hyphen(new String(h), (String)null, (String)null));
               }
            }

            if (buf.length() > 0) {
               res.add(buf.toString());
            }
         }
      }

      return res;
   }

   protected String getExceptionWord(ArrayList ex) {
      StringBuffer res = new StringBuffer();

      for(int i = 0; i < ex.size(); ++i) {
         Object item = ex.get(i);
         if (item instanceof String) {
            res.append((String)item);
         } else if (((Hyphen)item).noBreak != null) {
            res.append(((Hyphen)item).noBreak);
         }
      }

      return res.toString();
   }

   protected static String getInterletterValues(String pat) {
      StringBuffer il = new StringBuffer();
      String word = pat + "a";
      int len = word.length();

      for(int i = 0; i < len; ++i) {
         char c = word.charAt(i);
         if (Character.isDigit(c)) {
            il.append(c);
            ++i;
         } else {
            il.append('0');
         }
      }

      return il.toString();
   }

   protected void getExternalClasses() throws SAXException {
      XMLReader mainParser = this.parser;
      this.parser = createParser();
      this.parser.setContentHandler(this);
      this.parser.setErrorHandler(this);
      InputStream stream = this.getClass().getResourceAsStream("classes.xml");
      InputSource source = new InputSource(stream);

      try {
         this.parser.parse(source);
      } catch (IOException var8) {
         throw new SAXException(var8.getMessage());
      } finally {
         this.parser = mainParser;
      }

   }

   public void startElement(String uri, String local, String raw, Attributes attrs) throws SAXException {
      if (local.equals("hyphen-char")) {
         String h = attrs.getValue("value");
         if (h != null && h.length() == 1) {
            this.hyphenChar = h.charAt(0);
         }
      } else if (local.equals("classes")) {
         this.currElement = 1;
      } else if (local.equals("patterns")) {
         if (!this.hasClasses) {
            this.getExternalClasses();
         }

         this.currElement = 3;
      } else if (local.equals("exceptions")) {
         if (!this.hasClasses) {
            this.getExternalClasses();
         }

         this.currElement = 2;
         this.exception = new ArrayList();
      } else if (local.equals("hyphen")) {
         if (this.token.length() > 0) {
            this.exception.add(this.token.toString());
         }

         this.exception.add(new Hyphen(attrs.getValue("pre"), attrs.getValue("no"), attrs.getValue("post")));
         this.currElement = 4;
      }

      this.token.setLength(0);
   }

   public void endElement(String uri, String local, String raw) {
      if (this.token.length() > 0) {
         String word = this.token.toString();
         switch (this.currElement) {
            case 1:
               this.consumer.addClass(word);
               break;
            case 2:
               this.exception.add(word);
               this.exception = this.normalizeException(this.exception);
               this.consumer.addException(this.getExceptionWord(this.exception), (ArrayList)this.exception.clone());
               break;
            case 3:
               this.consumer.addPattern(getPattern(word), getInterletterValues(word));
            case 4:
         }

         if (this.currElement != 4) {
            this.token.setLength(0);
         }
      }

      if (this.currElement == 1) {
         this.hasClasses = true;
      }

      if (this.currElement == 4) {
         this.currElement = 2;
      } else {
         this.currElement = 0;
      }

   }

   public void characters(char[] ch, int start, int length) {
      StringBuffer chars = new StringBuffer(length);
      chars.append(ch, start, length);

      for(String word = this.readToken(chars); word != null; word = this.readToken(chars)) {
         switch (this.currElement) {
            case 1:
               this.consumer.addClass(word);
               break;
            case 2:
               this.exception.add(word);
               this.exception = this.normalizeException(this.exception);
               this.consumer.addException(this.getExceptionWord(this.exception), (ArrayList)this.exception.clone());
               this.exception.clear();
               break;
            case 3:
               this.consumer.addPattern(getPattern(word), getInterletterValues(word));
         }
      }

   }

   public void warning(SAXParseException ex) {
      this.errMsg = "[Warning] " + this.getLocationString(ex) + ": " + ex.getMessage();
   }

   public void error(SAXParseException ex) {
      this.errMsg = "[Error] " + this.getLocationString(ex) + ": " + ex.getMessage();
   }

   public void fatalError(SAXParseException ex) throws SAXException {
      this.errMsg = "[Fatal Error] " + this.getLocationString(ex) + ": " + ex.getMessage();
      throw ex;
   }

   private String getLocationString(SAXParseException ex) {
      StringBuffer str = new StringBuffer();
      String systemId = ex.getSystemId();
      if (systemId != null) {
         int index = systemId.lastIndexOf(47);
         if (index != -1) {
            systemId = systemId.substring(index + 1);
         }

         str.append(systemId);
      }

      str.append(':');
      str.append(ex.getLineNumber());
      str.append(':');
      str.append(ex.getColumnNumber());
      return str.toString();
   }

   public void addClass(String c) {
      this.testOut.println("class: " + c);
   }

   public void addException(String w, ArrayList e) {
      this.testOut.println("exception: " + w + " : " + e.toString());
   }

   public void addPattern(String p, String v) {
      this.testOut.println("pattern: " + p + " : " + v);
   }

   public void setTestOut(PrintStream testOut) {
      this.testOut = testOut;
   }

   public void closeTestOut() {
      this.testOut.flush();
      this.testOut.close();
   }

   public static void main(String[] args) throws Exception {
      if (args.length > 0) {
         PatternParser pp = new PatternParser();
         PrintStream p = null;
         if (args.length > 1) {
            FileOutputStream f = new FileOutputStream(args[1]);
            p = new PrintStream(f, false, "utf-8");
            pp.setTestOut(p);
         }

         pp.parse(args[0]);
         if (pp != null) {
            pp.closeTestOut();
         }
      }

   }
}

package javax.xml.parsers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.validation.Schema;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public abstract class SAXParser {
   private static final boolean DEBUG = false;

   protected SAXParser() {
   }

   public void reset() {
      throw new UnsupportedOperationException("This SAXParser, \"" + this.getClass().getName() + "\", does not support the reset functionality." + "  Specification \"" + this.getClass().getPackage().getSpecificationTitle() + "\"" + " version \"" + this.getClass().getPackage().getSpecificationVersion() + "\"");
   }

   public void parse(InputStream var1, HandlerBase var2) throws SAXException, IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("InputStream cannot be null");
      } else {
         InputSource var3 = new InputSource(var1);
         this.parse(var3, var2);
      }
   }

   public void parse(InputStream var1, HandlerBase var2, String var3) throws SAXException, IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("InputStream cannot be null");
      } else {
         InputSource var4 = new InputSource(var1);
         var4.setSystemId(var3);
         this.parse(var4, var2);
      }
   }

   public void parse(InputStream var1, DefaultHandler var2) throws SAXException, IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("InputStream cannot be null");
      } else {
         InputSource var3 = new InputSource(var1);
         this.parse(var3, var2);
      }
   }

   public void parse(InputStream var1, DefaultHandler var2, String var3) throws SAXException, IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("InputStream cannot be null");
      } else {
         InputSource var4 = new InputSource(var1);
         var4.setSystemId(var3);
         this.parse(var4, var2);
      }
   }

   public void parse(String var1, HandlerBase var2) throws SAXException, IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("uri cannot be null");
      } else {
         InputSource var3 = new InputSource(var1);
         this.parse(var3, var2);
      }
   }

   public void parse(String var1, DefaultHandler var2) throws SAXException, IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("uri cannot be null");
      } else {
         InputSource var3 = new InputSource(var1);
         this.parse(var3, var2);
      }
   }

   public void parse(File var1, HandlerBase var2) throws SAXException, IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("File cannot be null");
      } else {
         String var3 = FilePathToURI.filepath2URI(var1.getAbsolutePath());
         InputSource var4 = new InputSource(var3);
         this.parse(var4, var2);
      }
   }

   public void parse(File var1, DefaultHandler var2) throws SAXException, IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("File cannot be null");
      } else {
         String var3 = FilePathToURI.filepath2URI(var1.getAbsolutePath());
         InputSource var4 = new InputSource(var3);
         this.parse(var4, var2);
      }
   }

   public void parse(InputSource var1, HandlerBase var2) throws SAXException, IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("InputSource cannot be null");
      } else {
         Parser var3 = this.getParser();
         if (var2 != null) {
            var3.setDocumentHandler(var2);
            var3.setEntityResolver(var2);
            var3.setErrorHandler(var2);
            var3.setDTDHandler(var2);
         }

         var3.parse(var1);
      }
   }

   public void parse(InputSource var1, DefaultHandler var2) throws SAXException, IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("InputSource cannot be null");
      } else {
         XMLReader var3 = this.getXMLReader();
         if (var2 != null) {
            var3.setContentHandler(var2);
            var3.setEntityResolver(var2);
            var3.setErrorHandler(var2);
            var3.setDTDHandler(var2);
         }

         var3.parse(var1);
      }
   }

   public abstract Parser getParser() throws SAXException;

   public abstract XMLReader getXMLReader() throws SAXException;

   public abstract boolean isNamespaceAware();

   public abstract boolean isValidating();

   public abstract void setProperty(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException;

   public abstract Object getProperty(String var1) throws SAXNotRecognizedException, SAXNotSupportedException;

   public Schema getSchema() {
      throw new UnsupportedOperationException("This parser does not support specification \"" + this.getClass().getPackage().getSpecificationTitle() + "\" version \"" + this.getClass().getPackage().getSpecificationVersion() + "\"");
   }

   public boolean isXIncludeAware() {
      throw new UnsupportedOperationException("This parser does not support specification \"" + this.getClass().getPackage().getSpecificationTitle() + "\" version \"" + this.getClass().getPackage().getSpecificationVersion() + "\"");
   }
}

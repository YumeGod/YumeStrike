package javax.xml.parsers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.validation.Schema;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class DocumentBuilder {
   private static final boolean DEBUG = false;

   protected DocumentBuilder() {
   }

   public void reset() {
      throw new UnsupportedOperationException("This DocumentBuilder, \"" + this.getClass().getName() + "\", does not support the reset functionality." + "  Specification \"" + this.getClass().getPackage().getSpecificationTitle() + "\"" + " version \"" + this.getClass().getPackage().getSpecificationVersion() + "\"");
   }

   public Document parse(InputStream var1) throws SAXException, IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("InputStream cannot be null");
      } else {
         InputSource var2 = new InputSource(var1);
         return this.parse(var2);
      }
   }

   public Document parse(InputStream var1, String var2) throws SAXException, IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("InputStream cannot be null");
      } else {
         InputSource var3 = new InputSource(var1);
         var3.setSystemId(var2);
         return this.parse(var3);
      }
   }

   public Document parse(String var1) throws SAXException, IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("URI cannot be null");
      } else {
         InputSource var2 = new InputSource(var1);
         return this.parse(var2);
      }
   }

   public Document parse(File var1) throws SAXException, IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("File cannot be null");
      } else {
         String var2 = FilePathToURI.filepath2URI(var1.getAbsolutePath());
         InputSource var3 = new InputSource(var2);
         return this.parse(var3);
      }
   }

   public abstract Document parse(InputSource var1) throws SAXException, IOException;

   public abstract boolean isNamespaceAware();

   public abstract boolean isValidating();

   public abstract void setEntityResolver(EntityResolver var1);

   public abstract void setErrorHandler(ErrorHandler var1);

   public abstract Document newDocument();

   public abstract DOMImplementation getDOMImplementation();

   public Schema getSchema() {
      throw new UnsupportedOperationException("This parser does not support specification \"" + this.getClass().getPackage().getSpecificationTitle() + "\" version \"" + this.getClass().getPackage().getSpecificationVersion() + "\"");
   }

   public boolean isXIncludeAware() {
      throw new UnsupportedOperationException("This parser does not support specification \"" + this.getClass().getPackage().getSpecificationTitle() + "\" version \"" + this.getClass().getPackage().getSpecificationVersion() + "\"");
   }
}

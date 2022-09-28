package javax.xml.validation;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public abstract class ValidatorHandler implements ContentHandler {
   protected ValidatorHandler() {
   }

   public abstract void setContentHandler(ContentHandler var1);

   public abstract ContentHandler getContentHandler();

   public abstract void setErrorHandler(ErrorHandler var1);

   public abstract ErrorHandler getErrorHandler();

   public abstract void setResourceResolver(LSResourceResolver var1);

   public abstract LSResourceResolver getResourceResolver();

   public abstract TypeInfoProvider getTypeInfoProvider();

   public boolean getFeature(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         throw new SAXNotRecognizedException(var1);
      }
   }

   public void setFeature(String var1, boolean var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         throw new SAXNotRecognizedException(var1);
      }
   }

   public void setProperty(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         throw new SAXNotRecognizedException(var1);
      }
   }

   public Object getProperty(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         throw new SAXNotRecognizedException(var1);
      }
   }

   public abstract void skippedEntity(String var1) throws SAXException;

   public abstract void processingInstruction(String var1, String var2) throws SAXException;

   public abstract void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException;

   public abstract void characters(char[] var1, int var2, int var3) throws SAXException;

   public abstract void endElement(String var1, String var2, String var3) throws SAXException;

   public abstract void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException;

   public abstract void endPrefixMapping(String var1) throws SAXException;

   public abstract void startPrefixMapping(String var1, String var2) throws SAXException;

   public abstract void endDocument() throws SAXException;

   public abstract void startDocument() throws SAXException;

   public abstract void setDocumentLocator(Locator var1);
}

package org.apache.xerces.util;

import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLParseException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ErrorHandlerWrapper implements XMLErrorHandler {
   protected ErrorHandler fErrorHandler;

   public ErrorHandlerWrapper() {
   }

   public ErrorHandlerWrapper(ErrorHandler var1) {
      this.setErrorHandler(var1);
   }

   public void setErrorHandler(ErrorHandler var1) {
      this.fErrorHandler = var1;
   }

   public ErrorHandler getErrorHandler() {
      return this.fErrorHandler;
   }

   public void warning(String var1, String var2, XMLParseException var3) throws XNIException {
      if (this.fErrorHandler != null) {
         SAXParseException var4 = createSAXParseException(var3);

         try {
            this.fErrorHandler.warning(var4);
         } catch (SAXParseException var7) {
            throw createXMLParseException(var7);
         } catch (SAXException var8) {
            throw createXNIException(var8);
         }
      }

   }

   public void error(String var1, String var2, XMLParseException var3) throws XNIException {
      if (this.fErrorHandler != null) {
         SAXParseException var4 = createSAXParseException(var3);

         try {
            this.fErrorHandler.error(var4);
         } catch (SAXParseException var7) {
            throw createXMLParseException(var7);
         } catch (SAXException var8) {
            throw createXNIException(var8);
         }
      }

   }

   public void fatalError(String var1, String var2, XMLParseException var3) throws XNIException {
      if (this.fErrorHandler != null) {
         SAXParseException var4 = createSAXParseException(var3);

         try {
            this.fErrorHandler.fatalError(var4);
         } catch (SAXParseException var7) {
            throw createXMLParseException(var7);
         } catch (SAXException var8) {
            throw createXNIException(var8);
         }
      }

   }

   protected static SAXParseException createSAXParseException(XMLParseException var0) {
      return new SAXParseException(var0.getMessage(), var0.getPublicId(), var0.getExpandedSystemId(), var0.getLineNumber(), var0.getColumnNumber(), var0.getException());
   }

   protected static XMLParseException createXMLParseException(SAXParseException var0) {
      final String var1 = var0.getPublicId();
      final String var2 = var0.getSystemId();
      final int var3 = var0.getLineNumber();
      final int var4 = var0.getColumnNumber();
      XMLLocator var5 = new XMLLocator() {
         public String getPublicId() {
            return var1;
         }

         public String getExpandedSystemId() {
            return var2;
         }

         public String getBaseSystemId() {
            return null;
         }

         public String getLiteralSystemId() {
            return null;
         }

         public int getColumnNumber() {
            return var4;
         }

         public int getLineNumber() {
            return var3;
         }

         public int getCharacterOffset() {
            return -1;
         }

         public String getEncoding() {
            return null;
         }

         public String getXMLVersion() {
            return null;
         }
      };
      return new XMLParseException(var5, var0.getMessage(), var0);
   }

   protected static XNIException createXNIException(SAXException var0) {
      return new XNIException(var0.getMessage(), var0);
   }
}

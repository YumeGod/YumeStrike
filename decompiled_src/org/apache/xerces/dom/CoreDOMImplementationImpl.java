package org.apache.xerces.dom;

import org.apache.xerces.impl.RevalidationHandler;
import org.apache.xerces.parsers.DOMParserImpl;
import org.apache.xerces.util.XMLChar;
import org.apache.xml.serialize.DOMSerializerImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSSerializer;

public class CoreDOMImplementationImpl implements DOMImplementation, DOMImplementationLS {
   private static final int SIZE = 2;
   private RevalidationHandler[] validators = new RevalidationHandler[2];
   private RevalidationHandler[] dtdValidators = new RevalidationHandler[2];
   private int freeValidatorIndex = -1;
   private int freeDTDValidatorIndex = -1;
   private int currentSize = 2;
   private int docAndDoctypeCounter = 0;
   static CoreDOMImplementationImpl singleton = new CoreDOMImplementationImpl();

   public static DOMImplementation getDOMImplementation() {
      return singleton;
   }

   public boolean hasFeature(String var1, String var2) {
      boolean var3 = var2 == null || var2.length() == 0;
      if (!var1.equalsIgnoreCase("+XPath") || !var3 && !var2.equals("3.0")) {
         if (var1.startsWith("+")) {
            var1 = var1.substring(1);
         }

         return var1.equalsIgnoreCase("Core") && (var3 || var2.equals("1.0") || var2.equals("2.0") || var2.equals("3.0")) || var1.equalsIgnoreCase("XML") && (var3 || var2.equals("1.0") || var2.equals("2.0") || var2.equals("3.0")) || var1.equalsIgnoreCase("LS") && (var3 || var2.equals("3.0"));
      } else {
         try {
            Class var4 = ObjectFactory.findProviderClass("org.apache.xpath.domapi.XPathEvaluatorImpl", ObjectFactory.findClassLoader(), true);
            Class[] var5 = var4.getInterfaces();

            for(int var6 = 0; var6 < var5.length; ++var6) {
               if (var5[var6].getName().equals("org.w3c.dom.xpath.XPathEvaluator")) {
                  return true;
               }
            }

            return true;
         } catch (Exception var7) {
            return false;
         }
      }
   }

   public DocumentType createDocumentType(String var1, String var2, String var3) {
      this.checkQName(var1);
      return new DocumentTypeImpl((CoreDocumentImpl)null, var1, var2, var3);
   }

   final void checkQName(String var1) {
      int var2 = var1.indexOf(58);
      int var3 = var1.lastIndexOf(58);
      int var4 = var1.length();
      if (var2 != 0 && var2 != var4 - 1 && var3 == var2) {
         int var8 = 0;
         int var6;
         String var7;
         String var9;
         if (var2 > 0) {
            if (!XMLChar.isNCNameStart(var1.charAt(var8))) {
               var9 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", (Object[])null);
               throw new DOMException((short)5, var9);
            }

            for(var6 = 1; var6 < var2; ++var6) {
               if (!XMLChar.isNCName(var1.charAt(var6))) {
                  var7 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", (Object[])null);
                  throw new DOMException((short)5, var7);
               }
            }

            var8 = var2 + 1;
         }

         if (!XMLChar.isNCNameStart(var1.charAt(var8))) {
            var9 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", (Object[])null);
            throw new DOMException((short)5, var9);
         } else {
            for(var6 = var8 + 1; var6 < var4; ++var6) {
               if (!XMLChar.isNCName(var1.charAt(var6))) {
                  var7 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", (Object[])null);
                  throw new DOMException((short)5, var7);
               }
            }

         }
      } else {
         String var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", (Object[])null);
         throw new DOMException((short)14, var5);
      }
   }

   public Document createDocument(String var1, String var2, DocumentType var3) throws DOMException {
      if (var3 != null && var3.getOwnerDocument() != null) {
         String var6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null);
         throw new DOMException((short)4, var6);
      } else {
         CoreDocumentImpl var4 = new CoreDocumentImpl(var3);
         Element var5 = var4.createElementNS(var1, var2);
         var4.appendChild(var5);
         return var4;
      }
   }

   public Object getFeature(String var1, String var2) {
      if (singleton.hasFeature(var1, var2)) {
         if (!var1.equalsIgnoreCase("+XPath")) {
            return singleton;
         }

         try {
            Class var3 = ObjectFactory.findProviderClass("org.apache.xpath.domapi.XPathEvaluatorImpl", ObjectFactory.findClassLoader(), true);
            Class[] var4 = var3.getInterfaces();

            for(int var5 = 0; var5 < var4.length; ++var5) {
               if (var4[var5].getName().equals("org.w3c.dom.xpath.XPathEvaluator")) {
                  return var3.newInstance();
               }
            }
         } catch (Exception var6) {
            return null;
         }
      }

      return null;
   }

   public LSParser createLSParser(short var1, String var2) throws DOMException {
      if (var1 == 1 && (var2 == null || "http://www.w3.org/2001/XMLSchema".equals(var2) || "http://www.w3.org/TR/REC-xml".equals(var2))) {
         return var2 != null && var2.equals("http://www.w3.org/TR/REC-xml") ? new DOMParserImpl("org.apache.xerces.parsers.DTDConfiguration", var2) : new DOMParserImpl("org.apache.xerces.parsers.XIncludeAwareParserConfiguration", var2);
      } else {
         String var3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
         throw new DOMException((short)9, var3);
      }
   }

   public LSSerializer createLSSerializer() {
      return new DOMSerializerImpl();
   }

   public LSInput createLSInput() {
      return new DOMInputImpl();
   }

   synchronized RevalidationHandler getValidator(String var1) {
      RevalidationHandler var2;
      if (var1 == "http://www.w3.org/2001/XMLSchema") {
         if (this.freeValidatorIndex < 0) {
            return (RevalidationHandler)ObjectFactory.newInstance("org.apache.xerces.impl.xs.XMLSchemaValidator", ObjectFactory.findClassLoader(), true);
         } else {
            var2 = this.validators[this.freeValidatorIndex];
            this.validators[this.freeValidatorIndex--] = null;
            return var2;
         }
      } else if (var1 == "http://www.w3.org/TR/REC-xml") {
         if (this.freeDTDValidatorIndex < 0) {
            return (RevalidationHandler)ObjectFactory.newInstance("org.apache.xerces.impl.dtd.XMLDTDValidator", ObjectFactory.findClassLoader(), true);
         } else {
            var2 = this.dtdValidators[this.freeDTDValidatorIndex];
            this.dtdValidators[this.freeDTDValidatorIndex--] = null;
            return var2;
         }
      } else {
         return null;
      }
   }

   synchronized void releaseValidator(String var1, RevalidationHandler var2) {
      RevalidationHandler[] var3;
      if (var1 == "http://www.w3.org/2001/XMLSchema") {
         ++this.freeValidatorIndex;
         if (this.validators.length == this.freeValidatorIndex) {
            this.currentSize += 2;
            var3 = new RevalidationHandler[this.currentSize];
            System.arraycopy(this.validators, 0, var3, 0, this.validators.length);
            this.validators = var3;
         }

         this.validators[this.freeValidatorIndex] = var2;
      } else if (var1 == "http://www.w3.org/TR/REC-xml") {
         ++this.freeDTDValidatorIndex;
         if (this.dtdValidators.length == this.freeDTDValidatorIndex) {
            this.currentSize += 2;
            var3 = new RevalidationHandler[this.currentSize];
            System.arraycopy(this.dtdValidators, 0, var3, 0, this.dtdValidators.length);
            this.dtdValidators = var3;
         }

         this.dtdValidators[this.freeDTDValidatorIndex] = var2;
      }

   }

   protected synchronized int assignDocumentNumber() {
      return ++this.docAndDoctypeCounter;
   }

   protected synchronized int assignDocTypeNumber() {
      return ++this.docAndDoctypeCounter;
   }

   public LSOutput createLSOutput() {
      return new DOMOutputImpl();
   }
}

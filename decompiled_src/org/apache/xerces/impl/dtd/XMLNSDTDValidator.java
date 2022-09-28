package org.apache.xerces.impl.dtd;

import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;

public class XMLNSDTDValidator extends XMLDTDValidator {
   private QName fAttributeQName = new QName();

   protected final void startNamespaceScope(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      super.fNamespaceContext.pushContext();
      if (var1.prefix == XMLSymbols.PREFIX_XMLNS) {
         super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[]{var1.rawname}, (short)2);
      }

      int var4 = var2.getLength();

      String var6;
      String var8;
      for(int var5 = 0; var5 < var4; ++var5) {
         var6 = var2.getLocalName(var5);
         String var7 = var2.getPrefix(var5);
         if (var7 == XMLSymbols.PREFIX_XMLNS || var7 == XMLSymbols.EMPTY_STRING && var6 == XMLSymbols.PREFIX_XMLNS) {
            var8 = super.fSymbolTable.addSymbol(var2.getValue(var5));
            if (var7 == XMLSymbols.PREFIX_XMLNS && var6 == XMLSymbols.PREFIX_XMLNS) {
               super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[]{var2.getQName(var5)}, (short)2);
            }

            if (var8 == NamespaceContext.XMLNS_URI) {
               super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[]{var2.getQName(var5)}, (short)2);
            }

            if (var6 == XMLSymbols.PREFIX_XML) {
               if (var8 != NamespaceContext.XML_URI) {
                  super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[]{var2.getQName(var5)}, (short)2);
               }
            } else if (var8 == NamespaceContext.XML_URI) {
               super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[]{var2.getQName(var5)}, (short)2);
            }

            var7 = var6 != XMLSymbols.PREFIX_XMLNS ? var6 : XMLSymbols.EMPTY_STRING;
            if (var8 == XMLSymbols.EMPTY_STRING && var6 != XMLSymbols.PREFIX_XMLNS) {
               super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "EmptyPrefixedAttName", new Object[]{var2.getQName(var5)}, (short)2);
            } else {
               super.fNamespaceContext.declarePrefix(var7, var8.length() != 0 ? var8 : null);
            }
         }
      }

      var6 = var1.prefix != null ? var1.prefix : XMLSymbols.EMPTY_STRING;
      var1.uri = super.fNamespaceContext.getURI(var6);
      if (var1.prefix == null && var1.uri != null) {
         var1.prefix = XMLSymbols.EMPTY_STRING;
      }

      if (var1.prefix != null && var1.uri == null) {
         super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[]{var1.prefix, var1.rawname}, (short)2);
      }

      for(int var15 = 0; var15 < var4; ++var15) {
         var2.getName(var15, this.fAttributeQName);
         var8 = this.fAttributeQName.prefix != null ? this.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
         String var9 = this.fAttributeQName.rawname;
         if (var9 == XMLSymbols.PREFIX_XMLNS) {
            this.fAttributeQName.uri = super.fNamespaceContext.getURI(XMLSymbols.PREFIX_XMLNS);
            var2.setName(var15, this.fAttributeQName);
         } else if (var8 != XMLSymbols.EMPTY_STRING) {
            this.fAttributeQName.uri = super.fNamespaceContext.getURI(var8);
            if (this.fAttributeQName.uri == null) {
               super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[]{var1.rawname, var9, var8}, (short)2);
            }

            var2.setName(var15, this.fAttributeQName);
         }
      }

      int var16 = var2.getLength();

      for(int var17 = 0; var17 < var16 - 1; ++var17) {
         String var10 = var2.getURI(var17);
         if (var10 != null && var10 != NamespaceContext.XMLNS_URI) {
            String var11 = var2.getLocalName(var17);

            for(int var12 = var17 + 1; var12 < var16; ++var12) {
               String var13 = var2.getLocalName(var12);
               String var14 = var2.getURI(var12);
               if (var11 == var13 && var10 == var14) {
                  super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[]{var1.rawname, var11, var10}, (short)2);
               }
            }
         }
      }

   }

   protected void endNamespaceScope(QName var1, Augmentations var2, boolean var3) throws XNIException {
      String var4 = var1.prefix != null ? var1.prefix : XMLSymbols.EMPTY_STRING;
      var1.uri = super.fNamespaceContext.getURI(var4);
      if (var1.uri != null) {
         var1.prefix = var4;
      }

      if (super.fDocumentHandler != null && !var3) {
         super.fDocumentHandler.endElement(var1, var2);
      }

      super.fNamespaceContext.popContext();
   }
}

package org.apache.xerces.dom;

import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.w3c.dom.DOMException;

public class AttrNSImpl extends AttrImpl {
   static final long serialVersionUID = -781906615369795414L;
   static final String xmlnsURI = "http://www.w3.org/2000/xmlns/";
   static final String xmlURI = "http://www.w3.org/XML/1998/namespace";
   protected String namespaceURI;
   protected String localName;

   public AttrNSImpl() {
   }

   protected AttrNSImpl(CoreDocumentImpl var1, String var2, String var3) {
      super(var1, var3);
      this.setName(var2, var3);
   }

   private void setName(String var1, String var2) {
      CoreDocumentImpl var3 = this.ownerDocument();
      this.namespaceURI = var1;
      if (var1 != null) {
         this.namespaceURI = var1.length() == 0 ? null : var1;
      }

      int var5 = var2.indexOf(58);
      int var6 = var2.lastIndexOf(58);
      var3.checkNamespaceWF(var2, var5, var6);
      if (var5 < 0) {
         this.localName = var2;
         if (var3.errorChecking) {
            var3.checkQName((String)null, this.localName);
            if (var2.equals("xmlns") && (var1 == null || !var1.equals(NamespaceContext.XMLNS_URI)) || var1 != null && var1.equals(NamespaceContext.XMLNS_URI) && !var2.equals("xmlns")) {
               String var7 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", (Object[])null);
               throw new DOMException((short)14, var7);
            }
         }
      } else {
         String var4 = var2.substring(0, var5);
         this.localName = var2.substring(var6 + 1);
         var3.checkQName(var4, this.localName);
         var3.checkDOMNSErr(var4, var1);
      }

   }

   public AttrNSImpl(CoreDocumentImpl var1, String var2, String var3, String var4) {
      super(var1, var3);
      this.localName = var4;
      this.namespaceURI = var2;
   }

   protected AttrNSImpl(CoreDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   void rename(String var1, String var2) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      super.name = var2;
      this.setName(var1, var2);
   }

   public void setValues(CoreDocumentImpl var1, String var2, String var3, String var4) {
      AttrImpl.textNode = null;
      super.flags = 0;
      this.isSpecified(true);
      this.hasStringValue(true);
      super.setOwnerDocument(var1);
      this.localName = var4;
      this.namespaceURI = var2;
      super.name = var3;
      super.value = null;
   }

   public String getNamespaceURI() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.namespaceURI;
   }

   public String getPrefix() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      int var1 = super.name.indexOf(58);
      return var1 < 0 ? null : super.name.substring(0, var1);
   }

   public void setPrefix(String var1) throws DOMException {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      if (this.ownerDocument().errorChecking) {
         String var2;
         if (this.isReadOnly()) {
            var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
            throw new DOMException((short)7, var2);
         }

         if (var1 != null && var1.length() != 0) {
            if (!CoreDocumentImpl.isXMLName(var1, this.ownerDocument().isXML11Version())) {
               var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", (Object[])null);
               throw new DOMException((short)5, var2);
            }

            if (this.namespaceURI == null || var1.indexOf(58) >= 0) {
               var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", (Object[])null);
               throw new DOMException((short)14, var2);
            }

            if (var1.equals("xmlns")) {
               if (!this.namespaceURI.equals("http://www.w3.org/2000/xmlns/")) {
                  var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", (Object[])null);
                  throw new DOMException((short)14, var2);
               }
            } else if (var1.equals("xml")) {
               if (!this.namespaceURI.equals("http://www.w3.org/XML/1998/namespace")) {
                  var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", (Object[])null);
                  throw new DOMException((short)14, var2);
               }
            } else if (super.name.equals("xmlns")) {
               var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", (Object[])null);
               throw new DOMException((short)14, var2);
            }
         }
      }

      if (var1 != null && var1.length() != 0) {
         super.name = var1 + ":" + this.localName;
      } else {
         super.name = this.localName;
      }

   }

   public String getLocalName() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.localName;
   }

   public String getTypeName() {
      if (super.type != null) {
         return super.type instanceof XSSimpleTypeDecl ? ((XSSimpleTypeDecl)super.type).getName() : (String)super.type;
      } else {
         return null;
      }
   }

   public boolean isDerivedFrom(String var1, String var2, int var3) {
      return super.type != null && super.type instanceof XSSimpleTypeDefinition ? ((XSSimpleTypeDecl)super.type).isDOMDerivedFrom(var1, var2, var3) : false;
   }

   public String getTypeNamespace() {
      if (super.type != null) {
         return super.type instanceof XSSimpleTypeDecl ? ((XSSimpleTypeDecl)super.type).getNamespace() : "http://www.w3.org/TR/REC-xml";
      } else {
         return null;
      }
   }
}

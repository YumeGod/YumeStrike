package org.apache.xerces.dom;

import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.util.URI;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;

public class ElementNSImpl extends ElementImpl {
   static final long serialVersionUID = -9142310625494392642L;
   static final String xmlURI = "http://www.w3.org/XML/1998/namespace";
   protected String namespaceURI;
   protected String localName;
   transient XSTypeDefinition type;

   protected ElementNSImpl() {
   }

   protected ElementNSImpl(CoreDocumentImpl var1, String var2, String var3) throws DOMException {
      super(var1, var3);
      this.setName(var2, var3);
   }

   private void setName(String var1, String var2) {
      this.namespaceURI = var1;
      if (var1 != null) {
         this.namespaceURI = var1.length() == 0 ? null : var1;
      }

      String var6;
      if (var2 == null) {
         var6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", (Object[])null);
         throw new DOMException((short)14, var6);
      } else {
         int var4 = var2.indexOf(58);
         int var5 = var2.lastIndexOf(58);
         super.ownerDocument.checkNamespaceWF(var2, var4, var5);
         if (var4 < 0) {
            this.localName = var2;
            if (super.ownerDocument.errorChecking) {
               super.ownerDocument.checkQName((String)null, this.localName);
               if (var2.equals("xmlns") && (var1 == null || !var1.equals(NamespaceContext.XMLNS_URI)) || var1 != null && var1.equals(NamespaceContext.XMLNS_URI) && !var2.equals("xmlns")) {
                  var6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", (Object[])null);
                  throw new DOMException((short)14, var6);
               }
            }
         } else {
            String var3 = var2.substring(0, var4);
            this.localName = var2.substring(var5 + 1);
            if (super.ownerDocument.errorChecking) {
               if (var1 == null || var3.equals("xml") && !var1.equals(NamespaceContext.XML_URI)) {
                  var6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", (Object[])null);
                  throw new DOMException((short)14, var6);
               }

               super.ownerDocument.checkQName(var3, this.localName);
               super.ownerDocument.checkDOMNSErr(var3, var1);
            }
         }

      }
   }

   protected ElementNSImpl(CoreDocumentImpl var1, String var2, String var3, String var4) throws DOMException {
      super(var1, var3);
      this.localName = var4;
      this.namespaceURI = var2;
   }

   protected ElementNSImpl(CoreDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   void rename(String var1, String var2) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      super.name = var2;
      this.setName(var1, var2);
      this.reconcileDefaultAttributes();
   }

   protected void setValues(CoreDocumentImpl var1, String var2, String var3, String var4) {
      super.firstChild = null;
      super.previousSibling = null;
      super.nextSibling = null;
      super.fNodeListCache = null;
      super.attributes = null;
      super.flags = 0;
      this.setOwnerDocument(var1);
      this.needsSyncData(true);
      super.name = var3;
      this.localName = var4;
      this.namespaceURI = var2;
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

      if (super.ownerDocument.errorChecking) {
         String var2;
         if (this.isReadOnly()) {
            var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
            throw new DOMException((short)7, var2);
         }

         if (var1 != null && var1.length() != 0) {
            if (!CoreDocumentImpl.isXMLName(var1, super.ownerDocument.isXML11Version())) {
               var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", (Object[])null);
               throw new DOMException((short)5, var2);
            }

            if (this.namespaceURI == null || var1.indexOf(58) >= 0) {
               var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", (Object[])null);
               throw new DOMException((short)14, var2);
            }

            if (var1.equals("xml") && !this.namespaceURI.equals("http://www.w3.org/XML/1998/namespace")) {
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

   public String getBaseURI() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      String var2;
      if (super.attributes != null) {
         Attr var1 = (Attr)super.attributes.getNamedItemNS("http://www.w3.org/XML/1998/namespace", "base");
         if (var1 != null) {
            var2 = var1.getNodeValue();
            if (var2.length() != 0) {
               try {
                  var2 = (new URI(var2)).toString();
                  return var2;
               } catch (URI.MalformedURIException var10) {
                  NodeImpl var4 = this.parentNode() != null ? this.parentNode() : super.ownerNode;
                  String var5 = var4 != null ? var4.getBaseURI() : null;
                  if (var5 != null) {
                     try {
                        var2 = (new URI(new URI(var5), var2)).toString();
                        return var2;
                     } catch (URI.MalformedURIException var7) {
                        return null;
                     }
                  }

                  return null;
               }
            }
         }
      }

      String var11 = this.parentNode() != null ? this.parentNode().getBaseURI() : null;
      if (var11 != null) {
         try {
            return (new URI(var11)).toString();
         } catch (URI.MalformedURIException var8) {
            return null;
         }
      } else {
         var2 = super.ownerNode != null ? super.ownerNode.getBaseURI() : null;
         if (var2 != null) {
            try {
               return (new URI(var2)).toString();
            } catch (URI.MalformedURIException var9) {
               return null;
            }
         } else {
            return null;
         }
      }
   }

   public String getTypeName() {
      if (this.type != null) {
         return this.type instanceof XSSimpleTypeDefinition ? ((XSSimpleTypeDecl)this.type).getTypeName() : ((XSComplexTypeDecl)this.type).getTypeName();
      } else {
         return null;
      }
   }

   public String getTypeNamespace() {
      return this.type != null ? this.type.getNamespace() : null;
   }

   public boolean isDerivedFrom(String var1, String var2, int var3) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      if (this.type != null) {
         return this.type instanceof XSSimpleTypeDefinition ? ((XSSimpleTypeDecl)this.type).isDOMDerivedFrom(var1, var2, var3) : ((XSComplexTypeDecl)this.type).isDOMDerivedFrom(var1, var2, var3);
      } else {
         return false;
      }
   }

   public void setType(XSTypeDefinition var1) {
      this.type = var1;
   }
}

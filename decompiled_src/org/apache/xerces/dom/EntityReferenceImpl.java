package org.apache.xerces.dom;

import org.apache.xerces.util.URI;
import org.w3c.dom.DocumentType;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class EntityReferenceImpl extends ParentNode implements EntityReference {
   static final long serialVersionUID = -7381452955687102062L;
   protected String name;
   protected String baseURI;

   public EntityReferenceImpl(CoreDocumentImpl var1, String var2) {
      super(var1);
      this.name = var2;
      this.isReadOnly(true);
      this.needsSyncChildren(true);
   }

   public short getNodeType() {
      return 5;
   }

   public String getNodeName() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.name;
   }

   public Node cloneNode(boolean var1) {
      EntityReferenceImpl var2 = (EntityReferenceImpl)super.cloneNode(var1);
      var2.setReadOnly(true, var1);
      return var2;
   }

   public String getBaseURI() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      if (this.baseURI == null) {
         DocumentType var1;
         NamedNodeMap var2;
         if (null != (var1 = this.getOwnerDocument().getDoctype()) && null != (var2 = var1.getEntities())) {
            EntityImpl var3 = (EntityImpl)var2.getNamedItem(this.getNodeName());
            if (var3 != null) {
               return var3.getBaseURI();
            }
         }
      } else if (this.baseURI != null && this.baseURI.length() != 0) {
         try {
            return (new URI(this.baseURI)).toString();
         } catch (URI.MalformedURIException var4) {
            return null;
         }
      }

      return this.baseURI;
   }

   public void setBaseURI(String var1) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      this.baseURI = var1;
   }

   protected String getEntityRefValue() {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      String var1 = "";
      if (super.firstChild != null) {
         if (super.firstChild.getNodeType() == 5) {
            var1 = ((EntityReferenceImpl)super.firstChild).getEntityRefValue();
         } else {
            if (super.firstChild.getNodeType() != 3) {
               return null;
            }

            var1 = super.firstChild.getNodeValue();
         }

         if (super.firstChild.nextSibling == null) {
            return var1;
         } else {
            StringBuffer var2 = new StringBuffer(var1);

            for(ChildNode var3 = super.firstChild.nextSibling; var3 != null; var3 = var3.nextSibling) {
               if (var3.getNodeType() == 5) {
                  var1 = ((EntityReferenceImpl)var3).getEntityRefValue();
               } else {
                  if (var3.getNodeType() != 3) {
                     return null;
                  }

                  var1 = var3.getNodeValue();
               }

               var2.append(var1);
            }

            return var2.toString();
         }
      } else {
         return "";
      }
   }

   protected void synchronizeChildren() {
      this.needsSyncChildren(false);
      DocumentType var1;
      NamedNodeMap var2;
      if (null != (var1 = this.getOwnerDocument().getDoctype()) && null != (var2 = var1.getEntities())) {
         EntityImpl var3 = (EntityImpl)var2.getNamedItem(this.getNodeName());
         if (var3 == null) {
            return;
         }

         this.isReadOnly(false);

         for(Node var4 = var3.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
            Node var5 = var4.cloneNode(true);
            this.insertBefore(var5, (Node)null);
         }

         this.setReadOnly(true, true);
      }

   }

   public void setReadOnly(boolean var1, boolean var2) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      if (var2) {
         if (this.needsSyncChildren()) {
            this.synchronizeChildren();
         }

         for(ChildNode var3 = super.firstChild; var3 != null; var3 = var3.nextSibling) {
            var3.setReadOnly(var1, true);
         }
      }

      this.isReadOnly(var1);
   }
}

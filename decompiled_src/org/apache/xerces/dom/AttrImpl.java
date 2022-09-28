package org.apache.xerces.dom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.TypeInfo;

public class AttrImpl extends NodeImpl implements Attr, TypeInfo {
   static final long serialVersionUID = 7277707688218972102L;
   static final String DTD_URI = "http://www.w3.org/TR/REC-xml";
   protected Object value = null;
   protected String name;
   transient Object type;
   protected static TextImpl textNode = null;

   protected AttrImpl(CoreDocumentImpl var1, String var2) {
      super(var1);
      this.name = var2;
      this.isSpecified(true);
      this.hasStringValue(true);
   }

   protected AttrImpl() {
   }

   void rename(String var1) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      this.name = var1;
   }

   protected void makeChildNode() {
      if (this.hasStringValue()) {
         if (this.value != null) {
            TextImpl var1 = (TextImpl)this.ownerDocument().createTextNode((String)this.value);
            this.value = var1;
            var1.isFirstChild(true);
            var1.previousSibling = var1;
            var1.ownerNode = this;
            var1.isOwned(true);
         }

         this.hasStringValue(false);
      }

   }

   void setOwnerDocument(CoreDocumentImpl var1) {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      super.setOwnerDocument(var1);
      if (!this.hasStringValue()) {
         for(ChildNode var2 = (ChildNode)this.value; var2 != null; var2 = var2.nextSibling) {
            var2.setOwnerDocument(var1);
         }
      }

   }

   public void setIdAttribute(boolean var1) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      this.isIdAttribute(var1);
   }

   public boolean isId() {
      return this.isIdAttribute();
   }

   public Node cloneNode(boolean var1) {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      AttrImpl var2 = (AttrImpl)super.cloneNode(var1);
      if (!var2.hasStringValue()) {
         var2.value = null;

         for(Node var3 = (Node)this.value; var3 != null; var3 = var3.getNextSibling()) {
            var2.appendChild(var3.cloneNode(true));
         }
      }

      var2.isSpecified(true);
      return var2;
   }

   public short getNodeType() {
      return 2;
   }

   public String getNodeName() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.name;
   }

   public void setNodeValue(String var1) throws DOMException {
      this.setValue(var1);
   }

   public String getTypeName() {
      return (String)this.type;
   }

   public String getTypeNamespace() {
      return this.type != null ? "http://www.w3.org/TR/REC-xml" : null;
   }

   public TypeInfo getSchemaTypeInfo() {
      return this;
   }

   public String getNodeValue() {
      return this.getValue();
   }

   public String getName() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.name;
   }

   public void setValue(String var1) {
      CoreDocumentImpl var2 = this.ownerDocument();
      if (var2.errorChecking && this.isReadOnly()) {
         String var6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
         throw new DOMException((short)7, var6);
      } else {
         Element var3 = this.getOwnerElement();
         String var4 = "";
         if (this.needsSyncData()) {
            this.synchronizeData();
         }

         if (this.needsSyncChildren()) {
            this.synchronizeChildren();
         }

         if (this.value != null) {
            if (var2.getMutationEvents()) {
               if (this.hasStringValue()) {
                  var4 = (String)this.value;
                  if (textNode == null) {
                     textNode = (TextImpl)var2.createTextNode((String)this.value);
                  } else {
                     textNode.data = (String)this.value;
                  }

                  this.value = textNode;
                  textNode.isFirstChild(true);
                  textNode.previousSibling = textNode;
                  textNode.ownerNode = this;
                  textNode.isOwned(true);
                  this.hasStringValue(false);
                  this.internalRemoveChild(textNode, true);
               } else {
                  var4 = this.getValue();

                  while(this.value != null) {
                     this.internalRemoveChild((Node)this.value, true);
                  }
               }
            } else {
               if (this.hasStringValue()) {
                  var4 = (String)this.value;
               } else {
                  var4 = this.getValue();
                  ChildNode var5 = (ChildNode)this.value;
                  var5.previousSibling = null;
                  var5.isFirstChild(false);
                  var5.ownerNode = var2;
               }

               this.value = null;
               this.needsSyncChildren(false);
            }

            if (this.isIdAttribute() && var3 != null) {
               var2.removeIdentifier(var4);
            }
         }

         this.isSpecified(true);
         if (var2.getMutationEvents()) {
            this.internalInsertBefore(var2.createTextNode(var1), (Node)null, true);
            this.hasStringValue(false);
            var2.modifiedAttrValue(this, var4);
         } else {
            this.value = var1;
            this.hasStringValue(true);
            this.changed();
         }

         if (this.isIdAttribute() && var3 != null) {
            var2.putIdentifier(var1, var3);
         }

      }
   }

   public String getValue() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      if (this.value == null) {
         return "";
      } else if (this.hasStringValue()) {
         return (String)this.value;
      } else {
         ChildNode var1 = (ChildNode)this.value;
         String var2 = null;
         if (var1.getNodeType() == 5) {
            var2 = ((EntityReferenceImpl)var1).getEntityRefValue();
         } else {
            var2 = var1.getNodeValue();
         }

         ChildNode var3 = var1.nextSibling;
         if (var3 != null && var2 != null) {
            StringBuffer var4;
            for(var4 = new StringBuffer(var2); var3 != null; var3 = var3.nextSibling) {
               if (var3.getNodeType() == 5) {
                  var2 = ((EntityReferenceImpl)var3).getEntityRefValue();
                  if (var2 == null) {
                     return "";
                  }

                  var4.append(var2);
               } else {
                  var4.append(var3.getNodeValue());
               }
            }

            return var4.toString();
         } else {
            return var2 == null ? "" : var2;
         }
      }
   }

   public boolean getSpecified() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.isSpecified();
   }

   /** @deprecated */
   public Element getElement() {
      return (Element)(this.isOwned() ? super.ownerNode : null);
   }

   public Element getOwnerElement() {
      return (Element)(this.isOwned() ? super.ownerNode : null);
   }

   public void normalize() {
      if (!this.isNormalized() && !this.hasStringValue()) {
         ChildNode var3 = (ChildNode)this.value;

         Object var2;
         for(Object var1 = var3; var1 != null; var1 = var2) {
            var2 = ((Node)var1).getNextSibling();
            if (((Node)var1).getNodeType() == 3) {
               if (var2 != null && ((Node)var2).getNodeType() == 3) {
                  ((Text)var1).appendData(((Node)var2).getNodeValue());
                  this.removeChild((Node)var2);
                  var2 = var1;
               } else if (((Node)var1).getNodeValue() == null || ((Node)var1).getNodeValue().length() == 0) {
                  this.removeChild((Node)var1);
               }
            }
         }

         this.isNormalized(true);
      }
   }

   public void setSpecified(boolean var1) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      this.isSpecified(var1);
   }

   public void setType(Object var1) {
      this.type = var1;
   }

   public String toString() {
      return this.getName() + "=" + "\"" + this.getValue() + "\"";
   }

   public boolean hasChildNodes() {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      return this.value != null;
   }

   public NodeList getChildNodes() {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      return this;
   }

   public Node getFirstChild() {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      this.makeChildNode();
      return (Node)this.value;
   }

   public Node getLastChild() {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      return this.lastChild();
   }

   final ChildNode lastChild() {
      this.makeChildNode();
      return this.value != null ? ((ChildNode)this.value).previousSibling : null;
   }

   final void lastChild(ChildNode var1) {
      if (this.value != null) {
         ((ChildNode)this.value).previousSibling = var1;
      }

   }

   public Node insertBefore(Node var1, Node var2) throws DOMException {
      return this.internalInsertBefore(var1, var2, false);
   }

   Node internalInsertBefore(Node var1, Node var2, boolean var3) throws DOMException {
      CoreDocumentImpl var4 = this.ownerDocument();
      boolean var5 = var4.errorChecking;
      if (var1.getNodeType() == 11) {
         if (var5) {
            for(Node var15 = var1.getFirstChild(); var15 != null; var15 = var15.getNextSibling()) {
               if (!var4.isKidOK(this, var15)) {
                  String var14 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", (Object[])null);
                  throw new DOMException((short)3, var14);
               }
            }
         }

         while(var1.hasChildNodes()) {
            this.insertBefore(var1.getFirstChild(), var2);
         }

         return var1;
      } else if (var1 == var2) {
         var2 = var2.getNextSibling();
         this.removeChild(var1);
         this.insertBefore(var1, var2);
         return var1;
      } else {
         if (this.needsSyncChildren()) {
            this.synchronizeChildren();
         }

         if (var5) {
            String var12;
            if (this.isReadOnly()) {
               var12 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
               throw new DOMException((short)7, var12);
            }

            if (var1.getOwnerDocument() != var4) {
               var12 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null);
               throw new DOMException((short)4, var12);
            }

            if (!var4.isKidOK(this, var1)) {
               var12 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", (Object[])null);
               throw new DOMException((short)3, var12);
            }

            if (var2 != null && var2.getParentNode() != this) {
               var12 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", (Object[])null);
               throw new DOMException((short)8, var12);
            }

            boolean var6 = true;

            for(Object var7 = this; var6 && var7 != null; var7 = ((NodeImpl)var7).parentNode()) {
               var6 = var1 != var7;
            }

            if (!var6) {
               String var16 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", (Object[])null);
               throw new DOMException((short)3, var16);
            }
         }

         this.makeChildNode();
         var4.insertingNode(this, var3);
         ChildNode var11 = (ChildNode)var1;
         NodeImpl var13 = var11.parentNode();
         if (var13 != null) {
            var13.removeChild(var11);
         }

         ChildNode var8 = (ChildNode)var2;
         var11.ownerNode = this;
         var11.isOwned(true);
         ChildNode var9 = (ChildNode)this.value;
         if (var9 == null) {
            this.value = var11;
            var11.isFirstChild(true);
            var11.previousSibling = var11;
         } else {
            ChildNode var10;
            if (var8 == null) {
               var10 = var9.previousSibling;
               var10.nextSibling = var11;
               var11.previousSibling = var10;
               var9.previousSibling = var11;
            } else if (var2 == var9) {
               var9.isFirstChild(false);
               var11.nextSibling = var9;
               var11.previousSibling = var9.previousSibling;
               var9.previousSibling = var11;
               this.value = var11;
               var11.isFirstChild(true);
            } else {
               var10 = var8.previousSibling;
               var11.nextSibling = var8;
               var10.nextSibling = var11;
               var8.previousSibling = var11;
               var11.previousSibling = var10;
            }
         }

         this.changed();
         var4.insertedNode(this, var11, var3);
         this.checkNormalizationAfterInsert(var11);
         return var1;
      }
   }

   public Node removeChild(Node var1) throws DOMException {
      if (this.hasStringValue()) {
         String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", (Object[])null);
         throw new DOMException((short)8, var2);
      } else {
         return this.internalRemoveChild(var1, false);
      }
   }

   Node internalRemoveChild(Node var1, boolean var2) throws DOMException {
      CoreDocumentImpl var3 = this.ownerDocument();
      if (var3.errorChecking) {
         String var8;
         if (this.isReadOnly()) {
            var8 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
            throw new DOMException((short)7, var8);
         }

         if (var1 != null && var1.getParentNode() != this) {
            var8 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", (Object[])null);
            throw new DOMException((short)8, var8);
         }
      }

      ChildNode var4 = (ChildNode)var1;
      var3.removingNode(this, var4, var2);
      ChildNode var5;
      if (var4 == this.value) {
         var4.isFirstChild(false);
         this.value = var4.nextSibling;
         var5 = (ChildNode)this.value;
         if (var5 != null) {
            var5.isFirstChild(true);
            var5.previousSibling = var4.previousSibling;
         }
      } else {
         var5 = var4.previousSibling;
         ChildNode var6 = var4.nextSibling;
         var5.nextSibling = var6;
         if (var6 == null) {
            ChildNode var7 = (ChildNode)this.value;
            var7.previousSibling = var5;
         } else {
            var6.previousSibling = var5;
         }
      }

      var5 = var4.previousSibling();
      var4.ownerNode = var3;
      var4.isOwned(false);
      var4.nextSibling = null;
      var4.previousSibling = null;
      this.changed();
      var3.removedNode(this, var2);
      this.checkNormalizationAfterRemove(var5);
      return var4;
   }

   public Node replaceChild(Node var1, Node var2) throws DOMException {
      this.makeChildNode();
      CoreDocumentImpl var3 = this.ownerDocument();
      var3.replacingNode(this);
      this.internalInsertBefore(var1, var2, true);
      if (var1 != var2) {
         this.internalRemoveChild(var2, true);
      }

      var3.replacedNode(this);
      return var2;
   }

   public int getLength() {
      if (this.hasStringValue()) {
         return 1;
      } else {
         ChildNode var1 = (ChildNode)this.value;

         int var2;
         for(var2 = 0; var1 != null; var1 = var1.nextSibling) {
            ++var2;
         }

         return var2;
      }
   }

   public Node item(int var1) {
      if (this.hasStringValue()) {
         if (var1 == 0 && this.value != null) {
            this.makeChildNode();
            return (Node)this.value;
         } else {
            return null;
         }
      } else if (var1 < 0) {
         return null;
      } else {
         ChildNode var2 = (ChildNode)this.value;

         for(int var3 = 0; var3 < var1 && var2 != null; ++var3) {
            var2 = var2.nextSibling;
         }

         return var2;
      }
   }

   public boolean isEqualNode(Node var1) {
      return super.isEqualNode(var1);
   }

   public boolean isDerivedFrom(String var1, String var2, int var3) {
      return false;
   }

   public void setReadOnly(boolean var1, boolean var2) {
      super.setReadOnly(var1, var2);
      if (var2) {
         if (this.needsSyncChildren()) {
            this.synchronizeChildren();
         }

         if (this.hasStringValue()) {
            return;
         }

         for(ChildNode var3 = (ChildNode)this.value; var3 != null; var3 = var3.nextSibling) {
            if (var3.getNodeType() != 5) {
               var3.setReadOnly(var1, true);
            }
         }
      }

   }

   protected void synchronizeChildren() {
      this.needsSyncChildren(false);
   }

   void checkNormalizationAfterInsert(ChildNode var1) {
      if (var1.getNodeType() == 3) {
         ChildNode var2 = var1.previousSibling();
         ChildNode var3 = var1.nextSibling;
         if (var2 != null && var2.getNodeType() == 3 || var3 != null && var3.getNodeType() == 3) {
            this.isNormalized(false);
         }
      } else if (!var1.isNormalized()) {
         this.isNormalized(false);
      }

   }

   void checkNormalizationAfterRemove(ChildNode var1) {
      if (var1 != null && var1.getNodeType() == 3) {
         ChildNode var2 = var1.nextSibling;
         if (var2 != null && var2.getNodeType() == 3) {
            this.isNormalized(false);
         }
      }

   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      var1.defaultWriteObject();
   }

   private void readObject(ObjectInputStream var1) throws ClassNotFoundException, IOException {
      var1.defaultReadObject();
      this.needsSyncChildren(false);
   }
}

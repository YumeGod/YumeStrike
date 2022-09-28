package org.apache.xerces.dom;

import java.util.Hashtable;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

public class DocumentTypeImpl extends ParentNode implements DocumentType {
   static final long serialVersionUID = 7751299192316526485L;
   protected String name;
   protected NamedNodeMapImpl entities;
   protected NamedNodeMapImpl notations;
   protected NamedNodeMapImpl elements;
   protected String publicID;
   protected String systemID;
   protected String internalSubset;
   private int doctypeNumber;
   private Hashtable userData;

   public DocumentTypeImpl(CoreDocumentImpl var1, String var2) {
      super(var1);
      this.doctypeNumber = 0;
      this.userData = null;
      this.name = var2;
      this.entities = new NamedNodeMapImpl(this);
      this.notations = new NamedNodeMapImpl(this);
      this.elements = new NamedNodeMapImpl(this);
   }

   public DocumentTypeImpl(CoreDocumentImpl var1, String var2, String var3, String var4) {
      this(var1, var2);
      this.publicID = var3;
      this.systemID = var4;
   }

   public String getPublicId() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.publicID;
   }

   public String getSystemId() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.systemID;
   }

   public void setInternalSubset(String var1) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      this.internalSubset = var1;
   }

   public String getInternalSubset() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.internalSubset;
   }

   public short getNodeType() {
      return 10;
   }

   public String getNodeName() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.name;
   }

   public Node cloneNode(boolean var1) {
      DocumentTypeImpl var2 = (DocumentTypeImpl)super.cloneNode(var1);
      var2.entities = this.entities.cloneMap((NodeImpl)var2);
      var2.notations = this.notations.cloneMap((NodeImpl)var2);
      var2.elements = this.elements.cloneMap((NodeImpl)var2);
      return var2;
   }

   public String getTextContent() throws DOMException {
      return null;
   }

   public void setTextContent(String var1) throws DOMException {
   }

   public boolean isEqualNode(Node var1) {
      if (!super.isEqualNode(var1)) {
         return false;
      } else {
         if (this.needsSyncData()) {
            this.synchronizeData();
         }

         DocumentTypeImpl var2 = (DocumentTypeImpl)var1;
         if ((this.getPublicId() != null || var2.getPublicId() == null) && (this.getPublicId() == null || var2.getPublicId() != null) && (this.getSystemId() != null || var2.getSystemId() == null) && (this.getSystemId() == null || var2.getSystemId() != null) && (this.getInternalSubset() != null || var2.getInternalSubset() == null) && (this.getInternalSubset() == null || var2.getInternalSubset() != null)) {
            if (this.getPublicId() != null && !this.getPublicId().equals(var2.getPublicId())) {
               return false;
            } else if (this.getSystemId() != null && !this.getSystemId().equals(var2.getSystemId())) {
               return false;
            } else if (this.getInternalSubset() != null && !this.getInternalSubset().equals(var2.getInternalSubset())) {
               return false;
            } else {
               NamedNodeMapImpl var3 = var2.entities;
               if (this.entities == null && var3 != null || this.entities != null && var3 == null) {
                  return false;
               } else {
                  Node var6;
                  if (this.entities != null && var3 != null) {
                     if (this.entities.getLength() != var3.getLength()) {
                        return false;
                     }

                     for(int var4 = 0; this.entities.item(var4) != null; ++var4) {
                        Node var5 = this.entities.item(var4);
                        var6 = var3.getNamedItem(var5.getNodeName());
                        if (!((NodeImpl)var5).isEqualNode((NodeImpl)var6)) {
                           return false;
                        }
                     }
                  }

                  NamedNodeMapImpl var8 = var2.notations;
                  if (this.notations == null && var8 != null || this.notations != null && var8 == null) {
                     return false;
                  } else {
                     if (this.notations != null && var8 != null) {
                        if (this.notations.getLength() != var8.getLength()) {
                           return false;
                        }

                        for(int var9 = 0; this.notations.item(var9) != null; ++var9) {
                           var6 = this.notations.item(var9);
                           Node var7 = var8.getNamedItem(var6.getNodeName());
                           if (!((NodeImpl)var6).isEqualNode((NodeImpl)var7)) {
                              return false;
                           }
                        }
                     }

                     return true;
                  }
               }
            }
         } else {
            return false;
         }
      }
   }

   void setOwnerDocument(CoreDocumentImpl var1) {
      super.setOwnerDocument(var1);
      this.entities.setOwnerDocument(var1);
      this.notations.setOwnerDocument(var1);
      this.elements.setOwnerDocument(var1);
   }

   protected int getNodeNumber() {
      if (this.getOwnerDocument() != null) {
         return super.getNodeNumber();
      } else {
         if (this.doctypeNumber == 0) {
            CoreDOMImplementationImpl var1 = (CoreDOMImplementationImpl)CoreDOMImplementationImpl.getDOMImplementation();
            this.doctypeNumber = var1.assignDocTypeNumber();
         }

         return this.doctypeNumber;
      }
   }

   public String getName() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.name;
   }

   public NamedNodeMap getEntities() {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      return this.entities;
   }

   public NamedNodeMap getNotations() {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      return this.notations;
   }

   public void setReadOnly(boolean var1, boolean var2) {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      super.setReadOnly(var1, var2);
      this.elements.setReadOnly(var1, true);
      this.entities.setReadOnly(var1, true);
      this.notations.setReadOnly(var1, true);
   }

   public NamedNodeMap getElements() {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      return this.elements;
   }

   public Object setUserData(String var1, Object var2, UserDataHandler var3) {
      if (this.userData == null) {
         this.userData = new Hashtable();
      }

      Object var4;
      ParentNode.UserDataRecord var5;
      if (var2 == null) {
         if (this.userData != null) {
            var4 = this.userData.remove(var1);
            if (var4 != null) {
               var5 = (ParentNode.UserDataRecord)var4;
               return var5.fData;
            }
         }

         return null;
      } else {
         var4 = this.userData.put(var1, new ParentNode.UserDataRecord(var2, var3));
         if (var4 != null) {
            var5 = (ParentNode.UserDataRecord)var4;
            return var5.fData;
         } else {
            return null;
         }
      }
   }

   public Object getUserData(String var1) {
      if (this.userData == null) {
         return null;
      } else {
         Object var2 = this.userData.get(var1);
         if (var2 != null) {
            ParentNode.UserDataRecord var3 = (ParentNode.UserDataRecord)var2;
            return var3.fData;
         } else {
            return null;
         }
      }
   }

   protected Hashtable getUserDataRecord() {
      return this.userData;
   }
}

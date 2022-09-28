package org.apache.xerces.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class CharacterDataImpl extends ChildNode {
   static final long serialVersionUID = 7931170150428474230L;
   protected String data;
   private static transient NodeList singletonNodeList = new NodeList() {
      public Node item(int var1) {
         return null;
      }

      public int getLength() {
         return 0;
      }
   };

   public CharacterDataImpl() {
   }

   protected CharacterDataImpl(CoreDocumentImpl var1, String var2) {
      super(var1);
      this.data = var2;
   }

   public NodeList getChildNodes() {
      return singletonNodeList;
   }

   public String getNodeValue() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.data;
   }

   protected void setNodeValueInternal(String var1) {
      this.setNodeValueInternal(var1, false);
   }

   protected void setNodeValueInternal(String var1, boolean var2) {
      CoreDocumentImpl var3 = this.ownerDocument();
      String var4;
      if (var3.errorChecking && this.isReadOnly()) {
         var4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
         throw new DOMException((short)7, var4);
      } else {
         if (this.needsSyncData()) {
            this.synchronizeData();
         }

         var4 = this.data;
         var3.modifyingCharacterData(this, var2);
         this.data = var1;
         var3.modifiedCharacterData(this, var4, var1, var2);
      }
   }

   public void setNodeValue(String var1) {
      this.setNodeValueInternal(var1);
      this.ownerDocument().replacedText(this);
   }

   public String getData() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.data;
   }

   public int getLength() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.data.length();
   }

   public void appendData(String var1) {
      if (this.isReadOnly()) {
         String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
         throw new DOMException((short)7, var2);
      } else if (var1 != null) {
         if (this.needsSyncData()) {
            this.synchronizeData();
         }

         this.setNodeValue(this.data + var1);
      }
   }

   public void deleteData(int var1, int var2) throws DOMException {
      this.internalDeleteData(var1, var2, false);
   }

   void internalDeleteData(int var1, int var2, boolean var3) throws DOMException {
      CoreDocumentImpl var4 = this.ownerDocument();
      if (var4.errorChecking) {
         String var9;
         if (this.isReadOnly()) {
            var9 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
            throw new DOMException((short)7, var9);
         }

         if (var2 < 0) {
            var9 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", (Object[])null);
            throw new DOMException((short)1, var9);
         }
      }

      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      int var5 = Math.max(this.data.length() - var2 - var1, 0);

      try {
         String var6 = this.data.substring(0, var1) + (var5 > 0 ? this.data.substring(var1 + var2, var1 + var2 + var5) : "");
         this.setNodeValueInternal(var6, var3);
         var4.deletedText(this, var1, var2);
      } catch (StringIndexOutOfBoundsException var8) {
         String var7 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", (Object[])null);
         throw new DOMException((short)1, var7);
      }
   }

   public void insertData(int var1, String var2) throws DOMException {
      this.internalInsertData(var1, var2, false);
   }

   void internalInsertData(int var1, String var2, boolean var3) throws DOMException {
      CoreDocumentImpl var4 = this.ownerDocument();
      String var5;
      if (var4.errorChecking && this.isReadOnly()) {
         var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
         throw new DOMException((short)7, var5);
      } else {
         if (this.needsSyncData()) {
            this.synchronizeData();
         }

         try {
            var5 = (new StringBuffer(this.data)).insert(var1, var2).toString();
            this.setNodeValueInternal(var5, var3);
            var4.insertedText(this, var1, var2.length());
         } catch (StringIndexOutOfBoundsException var7) {
            String var6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", (Object[])null);
            throw new DOMException((short)1, var6);
         }
      }
   }

   public void replaceData(int var1, int var2, String var3) throws DOMException {
      CoreDocumentImpl var4 = this.ownerDocument();
      String var5;
      if (var4.errorChecking && this.isReadOnly()) {
         var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
         throw new DOMException((short)7, var5);
      } else {
         if (this.needsSyncData()) {
            this.synchronizeData();
         }

         var4.replacingData(this);
         var5 = this.data;
         this.internalDeleteData(var1, var2, true);
         this.internalInsertData(var1, var3, true);
         var4.replacedCharacterData(this, var5, this.data);
      }
   }

   public void setData(String var1) throws DOMException {
      this.setNodeValue(var1);
   }

   public String substringData(int var1, int var2) throws DOMException {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      int var3 = this.data.length();
      if (var2 >= 0 && var1 >= 0 && var1 <= var3 - 1) {
         int var5 = Math.min(var1 + var2, var3);
         return this.data.substring(var1, var5);
      } else {
         String var4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", (Object[])null);
         throw new DOMException((short)1, var4);
      }
   }
}

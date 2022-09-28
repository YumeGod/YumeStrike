package org.apache.batik.dom;

import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public abstract class AbstractCharacterData extends AbstractChildNode implements CharacterData {
   protected String nodeValue = "";

   public String getNodeValue() throws DOMException {
      return this.nodeValue;
   }

   public void setNodeValue(String var1) throws DOMException {
      if (this.isReadonly()) {
         throw this.createDOMException((short)7, "readonly.node", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
      } else {
         String var2 = this.nodeValue;
         this.nodeValue = var1 == null ? "" : var1;
         this.fireDOMCharacterDataModifiedEvent(var2, this.nodeValue);
         if (this.getParentNode() != null) {
            ((AbstractParentNode)this.getParentNode()).fireDOMSubtreeModifiedEvent();
         }

      }
   }

   public String getData() throws DOMException {
      return this.getNodeValue();
   }

   public void setData(String var1) throws DOMException {
      this.setNodeValue(var1);
   }

   public int getLength() {
      return this.nodeValue.length();
   }

   public String substringData(int var1, int var2) throws DOMException {
      this.checkOffsetCount(var1, var2);
      String var3 = this.getNodeValue();
      return var3.substring(var1, Math.min(var3.length(), var1 + var2));
   }

   public void appendData(String var1) throws DOMException {
      if (this.isReadonly()) {
         throw this.createDOMException((short)7, "readonly.node", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
      } else {
         this.setNodeValue(this.getNodeValue() + (var1 == null ? "" : var1));
      }
   }

   public void insertData(int var1, String var2) throws DOMException {
      if (this.isReadonly()) {
         throw this.createDOMException((short)7, "readonly.node", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
      } else if (var1 >= 0 && var1 <= this.getLength()) {
         String var3 = this.getNodeValue();
         this.setNodeValue(var3.substring(0, var1) + var2 + var3.substring(var1, var3.length()));
      } else {
         throw this.createDOMException((short)1, "offset", new Object[]{new Integer(var1)});
      }
   }

   public void deleteData(int var1, int var2) throws DOMException {
      if (this.isReadonly()) {
         throw this.createDOMException((short)7, "readonly.node", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
      } else {
         this.checkOffsetCount(var1, var2);
         String var3 = this.getNodeValue();
         this.setNodeValue(var3.substring(0, var1) + var3.substring(Math.min(var3.length(), var1 + var2), var3.length()));
      }
   }

   public void replaceData(int var1, int var2, String var3) throws DOMException {
      if (this.isReadonly()) {
         throw this.createDOMException((short)7, "readonly.node", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
      } else {
         this.checkOffsetCount(var1, var2);
         String var4 = this.getNodeValue();
         this.setNodeValue(var4.substring(0, var1) + var3 + var4.substring(Math.min(var4.length(), var1 + var2), var4.length()));
      }
   }

   protected void checkOffsetCount(int var1, int var2) throws DOMException {
      if (var1 >= 0 && var1 < this.getLength()) {
         if (var2 < 0) {
            throw this.createDOMException((short)1, "negative.count", new Object[]{new Integer(var2)});
         }
      } else {
         throw this.createDOMException((short)1, "offset", new Object[]{new Integer(var1)});
      }
   }

   protected Node export(Node var1, AbstractDocument var2) {
      super.export(var1, var2);
      AbstractCharacterData var3 = (AbstractCharacterData)var1;
      var3.nodeValue = this.nodeValue;
      return var1;
   }

   protected Node deepExport(Node var1, AbstractDocument var2) {
      super.deepExport(var1, var2);
      AbstractCharacterData var3 = (AbstractCharacterData)var1;
      var3.nodeValue = this.nodeValue;
      return var1;
   }

   protected Node copyInto(Node var1) {
      super.copyInto(var1);
      AbstractCharacterData var2 = (AbstractCharacterData)var1;
      var2.nodeValue = this.nodeValue;
      return var1;
   }

   protected Node deepCopyInto(Node var1) {
      super.deepCopyInto(var1);
      AbstractCharacterData var2 = (AbstractCharacterData)var1;
      var2.nodeValue = this.nodeValue;
      return var1;
   }
}

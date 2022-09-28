package org.apache.batik.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

public abstract class AbstractProcessingInstruction extends AbstractChildNode implements ProcessingInstruction {
   protected String data;

   public String getNodeName() {
      return this.getTarget();
   }

   public short getNodeType() {
      return 7;
   }

   public String getNodeValue() throws DOMException {
      return this.getData();
   }

   public void setNodeValue(String var1) throws DOMException {
      this.setData(var1);
   }

   public String getData() {
      return this.data;
   }

   public void setData(String var1) throws DOMException {
      if (this.isReadonly()) {
         throw this.createDOMException((short)7, "readonly.node", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
      } else {
         String var2 = this.data;
         this.data = var1;
         this.fireDOMCharacterDataModifiedEvent(var2, this.data);
         if (this.getParentNode() != null) {
            ((AbstractParentNode)this.getParentNode()).fireDOMSubtreeModifiedEvent();
         }

      }
   }

   public String getTextContent() {
      return this.getNodeValue();
   }

   protected Node export(Node var1, AbstractDocument var2) {
      AbstractProcessingInstruction var3 = (AbstractProcessingInstruction)super.export(var1, var2);
      var3.data = this.data;
      return var3;
   }

   protected Node deepExport(Node var1, AbstractDocument var2) {
      AbstractProcessingInstruction var3 = (AbstractProcessingInstruction)super.deepExport(var1, var2);
      var3.data = this.data;
      return var3;
   }

   protected Node copyInto(Node var1) {
      AbstractProcessingInstruction var2 = (AbstractProcessingInstruction)super.copyInto(var1);
      var2.data = this.data;
      return var2;
   }

   protected Node deepCopyInto(Node var1) {
      AbstractProcessingInstruction var2 = (AbstractProcessingInstruction)super.deepCopyInto(var1);
      var2.data = this.data;
      return var2;
   }
}

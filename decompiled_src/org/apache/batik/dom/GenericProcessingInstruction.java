package org.apache.batik.dom;

import org.w3c.dom.Node;

public class GenericProcessingInstruction extends AbstractProcessingInstruction {
   protected String target;
   protected boolean readonly;

   protected GenericProcessingInstruction() {
   }

   public GenericProcessingInstruction(String var1, String var2, AbstractDocument var3) {
      this.ownerDocument = var3;
      this.setTarget(var1);
      this.setData(var2);
   }

   public void setNodeName(String var1) {
      this.setTarget(var1);
   }

   public boolean isReadonly() {
      return this.readonly;
   }

   public void setReadonly(boolean var1) {
      this.readonly = var1;
   }

   public String getTarget() {
      return this.target;
   }

   public void setTarget(String var1) {
      this.target = var1;
   }

   protected Node export(Node var1, AbstractDocument var2) {
      GenericProcessingInstruction var3 = (GenericProcessingInstruction)super.export(var1, var2);
      var3.setTarget(this.getTarget());
      return var3;
   }

   protected Node deepExport(Node var1, AbstractDocument var2) {
      GenericProcessingInstruction var3 = (GenericProcessingInstruction)super.deepExport(var1, var2);
      var3.setTarget(this.getTarget());
      return var3;
   }

   protected Node copyInto(Node var1) {
      GenericProcessingInstruction var2 = (GenericProcessingInstruction)super.copyInto(var1);
      var2.setTarget(this.getTarget());
      return var2;
   }

   protected Node deepCopyInto(Node var1) {
      GenericProcessingInstruction var2 = (GenericProcessingInstruction)super.deepCopyInto(var1);
      var2.setTarget(this.getTarget());
      return var2;
   }

   protected Node newNode() {
      return new GenericProcessingInstruction();
   }
}

package org.apache.batik.dom;

import org.w3c.dom.Node;

public class GenericElementNS extends AbstractElementNS {
   protected String nodeName;
   protected boolean readonly;

   protected GenericElementNS() {
   }

   public GenericElementNS(String var1, String var2, AbstractDocument var3) {
      super(var1, var2, var3);
      this.nodeName = var2;
   }

   public void setNodeName(String var1) {
      this.nodeName = var1;
   }

   public String getNodeName() {
      return this.nodeName;
   }

   public boolean isReadonly() {
      return this.readonly;
   }

   public void setReadonly(boolean var1) {
      this.readonly = var1;
   }

   protected Node export(Node var1, AbstractDocument var2) {
      GenericElementNS var3 = (GenericElementNS)super.export(var1, var2);
      var3.nodeName = this.nodeName;
      return var1;
   }

   protected Node deepExport(Node var1, AbstractDocument var2) {
      GenericElementNS var3 = (GenericElementNS)super.deepExport(var1, var2);
      var3.nodeName = this.nodeName;
      return var1;
   }

   protected Node copyInto(Node var1) {
      GenericElementNS var2 = (GenericElementNS)super.copyInto(var1);
      var2.nodeName = this.nodeName;
      return var1;
   }

   protected Node deepCopyInto(Node var1) {
      GenericElementNS var2 = (GenericElementNS)super.deepCopyInto(var1);
      var2.nodeName = this.nodeName;
      return var1;
   }

   protected Node newNode() {
      return new GenericElementNS();
   }
}

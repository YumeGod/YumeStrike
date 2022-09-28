package org.apache.batik.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class GenericElement extends AbstractElement {
   protected String nodeName;
   protected boolean readonly;

   protected GenericElement() {
   }

   public GenericElement(String var1, AbstractDocument var2) throws DOMException {
      super(var1, var2);
      this.nodeName = var1;
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
      super.export(var1, var2);
      GenericElement var3 = (GenericElement)var1;
      var3.nodeName = this.nodeName;
      return var1;
   }

   protected Node deepExport(Node var1, AbstractDocument var2) {
      super.deepExport(var1, var2);
      GenericElement var3 = (GenericElement)var1;
      var3.nodeName = this.nodeName;
      return var1;
   }

   protected Node copyInto(Node var1) {
      GenericElement var2 = (GenericElement)super.copyInto(var1);
      var2.nodeName = this.nodeName;
      return var1;
   }

   protected Node deepCopyInto(Node var1) {
      GenericElement var2 = (GenericElement)super.deepCopyInto(var1);
      var2.nodeName = this.nodeName;
      return var1;
   }

   protected Node newNode() {
      return new GenericElement();
   }
}

package org.apache.batik.dom;

import org.apache.batik.dom.util.DOMUtilities;
import org.w3c.dom.DOMException;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;

public abstract class AbstractEntityReference extends AbstractParentChildNode implements EntityReference {
   protected String nodeName;

   protected AbstractEntityReference() {
   }

   protected AbstractEntityReference(String var1, AbstractDocument var2) throws DOMException {
      this.ownerDocument = var2;
      if (var2.getStrictErrorChecking() && !DOMUtilities.isValidName(var1)) {
         throw this.createDOMException((short)5, "xml.name", new Object[]{var1});
      } else {
         this.nodeName = var1;
      }
   }

   public short getNodeType() {
      return 5;
   }

   public void setNodeName(String var1) {
      this.nodeName = var1;
   }

   public String getNodeName() {
      return this.nodeName;
   }

   protected Node export(Node var1, AbstractDocument var2) {
      super.export(var1, var2);
      AbstractEntityReference var3 = (AbstractEntityReference)var1;
      var3.nodeName = this.nodeName;
      return var1;
   }

   protected Node deepExport(Node var1, AbstractDocument var2) {
      super.deepExport(var1, var2);
      AbstractEntityReference var3 = (AbstractEntityReference)var1;
      var3.nodeName = this.nodeName;
      return var1;
   }

   protected Node copyInto(Node var1) {
      super.copyInto(var1);
      AbstractEntityReference var2 = (AbstractEntityReference)var1;
      var2.nodeName = this.nodeName;
      return var1;
   }

   protected Node deepCopyInto(Node var1) {
      super.deepCopyInto(var1);
      AbstractEntityReference var2 = (AbstractEntityReference)var1;
      var2.nodeName = this.nodeName;
      return var1;
   }

   protected void checkChildType(Node var1, boolean var2) {
      switch (var1.getNodeType()) {
         case 1:
         case 3:
         case 4:
         case 5:
         case 7:
         case 8:
         case 11:
            return;
         case 2:
         case 6:
         case 9:
         case 10:
         default:
            throw this.createDOMException((short)3, "child.type", new Object[]{new Integer(this.getNodeType()), this.getNodeName(), new Integer(var1.getNodeType()), var1.getNodeName()});
      }
   }
}

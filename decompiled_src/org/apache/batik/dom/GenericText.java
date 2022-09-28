package org.apache.batik.dom;

import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class GenericText extends AbstractText {
   protected boolean readonly;

   protected GenericText() {
   }

   public GenericText(String var1, AbstractDocument var2) {
      this.ownerDocument = var2;
      this.setNodeValue(var1);
   }

   public String getNodeName() {
      return "#text";
   }

   public short getNodeType() {
      return 3;
   }

   public boolean isReadonly() {
      return this.readonly;
   }

   public void setReadonly(boolean var1) {
      this.readonly = var1;
   }

   protected Text createTextNode(String var1) {
      return this.getOwnerDocument().createTextNode(var1);
   }

   protected Node newNode() {
      return new GenericText();
   }
}

package org.apache.batik.dom;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class GenericCDATASection extends AbstractText implements CDATASection {
   protected boolean readonly;

   protected GenericCDATASection() {
   }

   public GenericCDATASection(String var1, AbstractDocument var2) {
      this.ownerDocument = var2;
      this.setNodeValue(var1);
   }

   public String getNodeName() {
      return "#cdata-section";
   }

   public short getNodeType() {
      return 4;
   }

   public boolean isReadonly() {
      return this.readonly;
   }

   public void setReadonly(boolean var1) {
      this.readonly = var1;
   }

   protected Text createTextNode(String var1) {
      return this.getOwnerDocument().createCDATASection(var1);
   }

   protected Node newNode() {
      return new GenericCDATASection();
   }
}

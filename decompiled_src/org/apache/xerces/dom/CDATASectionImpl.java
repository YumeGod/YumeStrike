package org.apache.xerces.dom;

import org.w3c.dom.CDATASection;

public class CDATASectionImpl extends TextImpl implements CDATASection {
   static final long serialVersionUID = 2372071297878177780L;

   public CDATASectionImpl(CoreDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   public short getNodeType() {
      return 4;
   }

   public String getNodeName() {
      return "#cdata-section";
   }
}

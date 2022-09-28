package org.apache.fop.layoutmgr.inline;

import org.apache.fop.area.Area;
import org.apache.fop.area.inline.ForeignObject;
import org.apache.fop.fo.XMLObj;
import org.apache.fop.fo.flow.InstreamForeignObject;
import org.w3c.dom.Document;

public class InstreamForeignObjectLM extends AbstractGraphicsLayoutManager {
   public InstreamForeignObjectLM(InstreamForeignObject node) {
      super(node);
   }

   protected Area getChildArea() {
      XMLObj child = ((InstreamForeignObject)this.fobj).getChildXMLObj();
      Document doc = child.getDOMDocument();
      String ns = child.getNamespaceURI();
      return new ForeignObject(doc, ns);
   }
}

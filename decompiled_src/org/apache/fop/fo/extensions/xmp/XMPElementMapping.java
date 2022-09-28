package org.apache.fop.fo.extensions.xmp;

import java.util.HashMap;
import org.apache.fop.fo.ElementMapping;
import org.apache.fop.fo.FONode;
import org.w3c.dom.DOMImplementation;

public class XMPElementMapping extends ElementMapping {
   public XMPElementMapping() {
      this.namespaceURI = "adobe:ns:meta/";
   }

   public DOMImplementation getDOMImplementation() {
      return getDefaultDOMImplementation();
   }

   protected void initialize() {
      if (this.foObjs == null) {
         this.foObjs = new HashMap();
         this.foObjs.put("xmpmeta", new XMPMetaElementMaker());
      }

   }

   static class XMPMetaElementMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new XMPMetaElement(parent);
      }
   }
}

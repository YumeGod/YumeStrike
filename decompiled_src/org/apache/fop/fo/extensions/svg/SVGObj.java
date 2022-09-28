package org.apache.fop.fo.extensions.svg;

import org.apache.fop.fo.FONode;
import org.apache.fop.fo.XMLObj;

public class SVGObj extends XMLObj {
   public SVGObj(FONode parent) {
      super(parent);
   }

   public String getNamespaceURI() {
      return "http://www.w3.org/2000/svg";
   }

   public String getNormalNamespacePrefix() {
      return "svg";
   }
}

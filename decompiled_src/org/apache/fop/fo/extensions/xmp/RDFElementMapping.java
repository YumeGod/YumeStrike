package org.apache.fop.fo.extensions.xmp;

import java.util.HashMap;
import org.apache.fop.fo.ElementMapping;
import org.apache.fop.fo.FONode;
import org.w3c.dom.DOMImplementation;

public class RDFElementMapping extends ElementMapping {
   public RDFElementMapping() {
      this.namespaceURI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
   }

   public DOMImplementation getDOMImplementation() {
      return getDefaultDOMImplementation();
   }

   protected void initialize() {
      if (this.foObjs == null) {
         this.foObjs = new HashMap();
         this.foObjs.put("RDF", new RDFElementMaker());
      }

   }

   static class RDFElementMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new RDFElement(parent);
      }
   }
}

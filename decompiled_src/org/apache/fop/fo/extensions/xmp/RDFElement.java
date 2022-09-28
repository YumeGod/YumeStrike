package org.apache.fop.fo.extensions.xmp;

import org.apache.fop.fo.FONode;

public class RDFElement extends AbstractMetadataElement {
   public RDFElement(FONode parent) {
      super(parent);
   }

   public String getLocalName() {
      return "RDF";
   }

   public String getNormalNamespacePrefix() {
      return "rdf";
   }

   public String getNamespaceURI() {
      return "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
   }
}

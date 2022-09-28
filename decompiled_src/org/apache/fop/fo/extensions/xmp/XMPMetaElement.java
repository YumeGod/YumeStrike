package org.apache.fop.fo.extensions.xmp;

import org.apache.fop.fo.FONode;

public class XMPMetaElement extends AbstractMetadataElement {
   public XMPMetaElement(FONode parent) {
      super(parent);
   }

   public String getLocalName() {
      return "xmpmeta";
   }

   public String getNormalNamespacePrefix() {
      return "x";
   }

   public String getNamespaceURI() {
      return "adobe:ns:meta/";
   }
}

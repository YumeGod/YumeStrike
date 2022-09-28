package org.apache.batik.swing.svg;

import java.util.EventObject;
import org.w3c.dom.svg.SVGDocument;

public class SVGDocumentLoaderEvent extends EventObject {
   protected SVGDocument svgDocument;

   public SVGDocumentLoaderEvent(Object var1, SVGDocument var2) {
      super(var1);
      this.svgDocument = var2;
   }

   public SVGDocument getSVGDocument() {
      return this.svgDocument;
   }
}

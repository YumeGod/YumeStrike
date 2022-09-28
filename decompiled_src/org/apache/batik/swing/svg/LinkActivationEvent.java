package org.apache.batik.swing.svg;

import java.util.EventObject;
import org.w3c.dom.svg.SVGAElement;

public class LinkActivationEvent extends EventObject {
   protected String referencedURI;

   public LinkActivationEvent(Object var1, SVGAElement var2, String var3) {
      super(var1);
      this.referencedURI = var3;
   }

   public String getReferencedURI() {
      return this.referencedURI;
   }
}

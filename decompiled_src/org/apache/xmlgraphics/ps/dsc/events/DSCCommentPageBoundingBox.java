package org.apache.xmlgraphics.ps.dsc.events;

import java.awt.geom.Rectangle2D;

public class DSCCommentPageBoundingBox extends DSCCommentBoundingBox {
   public DSCCommentPageBoundingBox() {
   }

   public DSCCommentPageBoundingBox(Rectangle2D bbox) {
      super(bbox);
   }

   public String getName() {
      return "PageBoundingBox";
   }
}

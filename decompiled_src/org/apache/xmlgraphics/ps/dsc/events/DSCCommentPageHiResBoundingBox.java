package org.apache.xmlgraphics.ps.dsc.events;

import java.awt.geom.Rectangle2D;

public class DSCCommentPageHiResBoundingBox extends DSCCommentHiResBoundingBox {
   public DSCCommentPageHiResBoundingBox() {
   }

   public DSCCommentPageHiResBoundingBox(Rectangle2D bbox) {
      super(bbox);
   }

   public String getName() {
      return "PageHiResBoundingBox";
   }
}

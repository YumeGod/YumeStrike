package org.apache.fop.fo.pagination;

import java.awt.Rectangle;
import org.apache.fop.datatypes.FODimension;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.FONode;

public class RegionEnd extends RegionSE {
   public RegionEnd(FONode parent) {
      super(parent);
   }

   public Rectangle getViewportRectangle(FODimension reldims, SimplePageMaster spm) {
      PercentBaseContext pageWidthContext = this.getPageWidthContext(0);
      PercentBaseContext pageHeightContext = this.getPageHeightContext(0);
      PercentBaseContext neighbourContext;
      Rectangle vpRect;
      if (spm.getWritingMode() != 79 && spm.getWritingMode() != 121) {
         neighbourContext = pageWidthContext;
         vpRect = new Rectangle(reldims.ipd - this.getExtent().getValue(pageHeightContext), 0, reldims.bpd, this.getExtent().getValue(pageHeightContext));
      } else {
         neighbourContext = pageHeightContext;
         vpRect = new Rectangle(reldims.ipd - this.getExtent().getValue(pageWidthContext), 0, this.getExtent().getValue(pageWidthContext), reldims.bpd);
      }

      this.adjustIPD(vpRect, spm.getWritingMode(), neighbourContext);
      return vpRect;
   }

   protected String getDefaultRegionName() {
      return "xsl-region-end";
   }

   public String getLocalName() {
      return "region-end";
   }

   public int getNameId() {
      return 59;
   }
}

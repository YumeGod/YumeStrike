package org.apache.fop.fo.pagination;

import java.awt.Rectangle;
import org.apache.fop.datatypes.FODimension;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.FONode;

public class RegionAfter extends RegionBA {
   public RegionAfter(FONode parent) {
      super(parent);
   }

   public Rectangle getViewportRectangle(FODimension reldims, SimplePageMaster spm) {
      PercentBaseContext pageWidthContext = this.getPageWidthContext(0);
      PercentBaseContext pageHeightContext = this.getPageHeightContext(0);
      PercentBaseContext neighbourContext;
      Rectangle vpRect;
      if (spm.getWritingMode() != 79 && spm.getWritingMode() != 121) {
         neighbourContext = pageHeightContext;
         vpRect = new Rectangle(0, reldims.bpd - this.getExtent().getValue(pageWidthContext), this.getExtent().getValue(pageWidthContext), reldims.ipd);
      } else {
         neighbourContext = pageWidthContext;
         vpRect = new Rectangle(0, reldims.bpd - this.getExtent().getValue(pageHeightContext), reldims.ipd, this.getExtent().getValue(pageHeightContext));
      }

      if (this.getPrecedence() == 48) {
         this.adjustIPD(vpRect, spm.getWritingMode(), neighbourContext);
      }

      return vpRect;
   }

   protected String getDefaultRegionName() {
      return "xsl-region-after";
   }

   public String getLocalName() {
      return "region-after";
   }

   public int getNameId() {
      return 56;
   }
}

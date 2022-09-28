package org.apache.fop.fo.pagination;

import java.awt.Rectangle;
import org.apache.fop.datatypes.FODimension;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.datatypes.SimplePercentBaseContext;
import org.apache.fop.fo.FONode;

public class RegionBefore extends RegionBA {
   public RegionBefore(FONode parent) {
      super(parent);
   }

   protected String getDefaultRegionName() {
      return "xsl-region-before";
   }

   public Rectangle getViewportRectangle(FODimension reldims, SimplePageMaster spm) {
      SimplePercentBaseContext pageWidthContext;
      SimplePercentBaseContext pageHeightContext;
      if (spm.getReferenceOrientation() % 180 == 0) {
         pageWidthContext = new SimplePercentBaseContext((PercentBaseContext)null, 0, spm.getPageWidth().getValue());
         pageHeightContext = new SimplePercentBaseContext((PercentBaseContext)null, 0, spm.getPageHeight().getValue());
      } else {
         pageWidthContext = new SimplePercentBaseContext((PercentBaseContext)null, 0, spm.getPageHeight().getValue());
         pageHeightContext = new SimplePercentBaseContext((PercentBaseContext)null, 0, spm.getPageWidth().getValue());
      }

      SimplePercentBaseContext neighbourContext;
      Rectangle vpRect;
      if (spm.getWritingMode() != 79 && spm.getWritingMode() != 121) {
         neighbourContext = pageHeightContext;
         vpRect = new Rectangle(0, 0, this.getExtent().getValue(pageWidthContext), reldims.ipd);
      } else {
         neighbourContext = pageWidthContext;
         vpRect = new Rectangle(0, 0, reldims.ipd, this.getExtent().getValue(pageHeightContext));
      }

      if (this.getPrecedence() == 48) {
         this.adjustIPD(vpRect, spm.getWritingMode(), neighbourContext);
      }

      return vpRect;
   }

   public String getLocalName() {
      return "region-before";
   }

   public int getNameId() {
      return 57;
   }
}

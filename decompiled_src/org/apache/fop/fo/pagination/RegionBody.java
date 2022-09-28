package org.apache.fop.fo.pagination;

import java.awt.Rectangle;
import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.FODimension;
import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.Numeric;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.properties.CommonMarginBlock;

public class RegionBody extends Region {
   private CommonMarginBlock commonMarginBlock;
   private Numeric columnCount;
   private Length columnGap;

   public RegionBody(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.commonMarginBlock = pList.getMarginBlockProps();
      this.columnCount = pList.get(74).getNumeric();
      this.columnGap = pList.get(75).getLength();
      if (this.getColumnCount() > 1 && this.getOverflow() == 126) {
         this.getFOValidationEventProducer().columnCountErrorOnRegionBodyOverflowScroll(this, this.getName(), this.getLocator());
      }

   }

   public CommonMarginBlock getCommonMarginBlock() {
      return this.commonMarginBlock;
   }

   public int getColumnCount() {
      return this.columnCount.getValue();
   }

   public int getColumnGap() {
      return this.columnGap.getValue();
   }

   public Rectangle getViewportRectangle(FODimension reldims, SimplePageMaster spm) {
      PercentBaseContext pageWidthContext = this.getPageWidthContext(5);
      PercentBaseContext pageHeightContext = this.getPageHeightContext(5);
      int start;
      int end;
      if (spm.getWritingMode() == 79) {
         start = this.commonMarginBlock.marginLeft.getValue(pageWidthContext);
         end = this.commonMarginBlock.marginRight.getValue(pageWidthContext);
      } else {
         start = this.commonMarginBlock.marginRight.getValue(pageWidthContext);
         end = this.commonMarginBlock.marginLeft.getValue(pageWidthContext);
      }

      int before = this.commonMarginBlock.spaceBefore.getOptimum(pageHeightContext).getLength().getValue(pageHeightContext);
      int after = this.commonMarginBlock.spaceAfter.getOptimum(pageHeightContext).getLength().getValue(pageHeightContext);
      return new Rectangle(start, before, reldims.ipd - start - end, reldims.bpd - before - after);
   }

   protected String getDefaultRegionName() {
      return "xsl-region-body";
   }

   public String getLocalName() {
      return "region-body";
   }

   public int getNameId() {
      return 58;
   }
}

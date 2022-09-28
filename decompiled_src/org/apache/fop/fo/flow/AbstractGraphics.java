package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.GraphicsProperties;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.KeepProperty;
import org.apache.fop.fo.properties.LengthRangeProperty;
import org.apache.fop.fo.properties.SpaceProperty;
import org.apache.fop.fo.properties.StructurePointerPropertySet;

public abstract class AbstractGraphics extends FObj implements GraphicsProperties, StructurePointerPropertySet {
   private CommonBorderPaddingBackground commonBorderPaddingBackground;
   private Length alignmentAdjust;
   private int alignmentBaseline;
   private Length baselineShift;
   private LengthRangeProperty blockProgressionDimension;
   private Length contentHeight;
   private Length contentWidth;
   private int displayAlign;
   private int dominantBaseline;
   private Length height;
   private String id;
   private LengthRangeProperty inlineProgressionDimension;
   private KeepProperty keepWithNext;
   private KeepProperty keepWithPrevious;
   private SpaceProperty lineHeight;
   private int overflow;
   private int scaling;
   private int textAlign;
   private Length width;
   private String ptr;

   public AbstractGraphics(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      this.commonBorderPaddingBackground = pList.getBorderPaddingBackgroundProps();
      this.alignmentAdjust = pList.get(3).getLength();
      this.alignmentBaseline = pList.get(4).getEnum();
      this.baselineShift = pList.get(15).getLength();
      this.blockProgressionDimension = pList.get(17).getLengthRange();
      this.contentHeight = pList.get(78).getLength();
      this.contentWidth = pList.get(80).getLength();
      this.displayAlign = pList.get(87).getEnum();
      this.dominantBaseline = pList.get(88).getEnum();
      this.height = pList.get(115).getLength();
      this.id = pList.get(122).getString();
      this.ptr = pList.get(274).getString();
      this.inlineProgressionDimension = pList.get(127).getLengthRange();
      this.keepWithNext = pList.get(132).getKeep();
      this.keepWithPrevious = pList.get(133).getKeep();
      this.lineHeight = pList.get(144).getSpace();
      this.overflow = pList.get(169).getEnum();
      this.scaling = pList.get(215).getEnum();
      this.textAlign = pList.get(245).getEnum();
      this.width = pList.get(264).getLength();
      if (this.getUserAgent().isAccessibilityEnabled()) {
         String altText = pList.get(275).getString();
         if (altText.equals("")) {
            this.getFOValidationEventProducer().altTextMissing(this, this.getLocalName(), this.getLocator());
         }
      }

   }

   public String getId() {
      return this.id;
   }

   public CommonBorderPaddingBackground getCommonBorderPaddingBackground() {
      return this.commonBorderPaddingBackground;
   }

   public SpaceProperty getLineHeight() {
      return this.lineHeight;
   }

   public LengthRangeProperty getInlineProgressionDimension() {
      return this.inlineProgressionDimension;
   }

   public LengthRangeProperty getBlockProgressionDimension() {
      return this.blockProgressionDimension;
   }

   public Length getHeight() {
      return this.height;
   }

   public Length getWidth() {
      return this.width;
   }

   public Length getContentHeight() {
      return this.contentHeight;
   }

   public Length getContentWidth() {
      return this.contentWidth;
   }

   public int getScaling() {
      return this.scaling;
   }

   public int getOverflow() {
      return this.overflow;
   }

   public int getDisplayAlign() {
      return this.displayAlign;
   }

   public int getTextAlign() {
      return this.textAlign;
   }

   public Length getAlignmentAdjust() {
      if (this.alignmentAdjust.getEnum() == 9) {
         Length intrinsicAlignmentAdjust = this.getIntrinsicAlignmentAdjust();
         if (intrinsicAlignmentAdjust != null) {
            return intrinsicAlignmentAdjust;
         }
      }

      return this.alignmentAdjust;
   }

   public int getAlignmentBaseline() {
      return this.alignmentBaseline;
   }

   public Length getBaselineShift() {
      return this.baselineShift;
   }

   public int getDominantBaseline() {
      return this.dominantBaseline;
   }

   public KeepProperty getKeepWithNext() {
      return this.keepWithNext;
   }

   public KeepProperty getKeepWithPrevious() {
      return this.keepWithPrevious;
   }

   public String getPtr() {
      return this.ptr;
   }

   public abstract int getIntrinsicWidth();

   public abstract int getIntrinsicHeight();

   public abstract Length getIntrinsicAlignmentAdjust();
}

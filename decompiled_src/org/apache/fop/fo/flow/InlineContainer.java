package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.Numeric;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.CommonMarginInline;
import org.apache.fop.fo.properties.KeepProperty;
import org.apache.fop.fo.properties.LengthRangeProperty;
import org.apache.fop.fo.properties.SpaceProperty;
import org.xml.sax.Locator;

public class InlineContainer extends FObj {
   private Length alignmentAdjust;
   private int alignmentBaseline;
   private Length baselineShift;
   private LengthRangeProperty blockProgressionDimension;
   private CommonBorderPaddingBackground commonBorderPaddingBackground;
   private CommonMarginInline commonMarginInline;
   private int clip;
   private int dominantBaseline;
   private LengthRangeProperty inlineProgressionDimension;
   private KeepProperty keepTogether;
   private SpaceProperty lineHeight;
   private int overflow;
   private Numeric referenceOrientation;
   private int writingMode;
   private boolean blockItemFound = false;

   public InlineContainer(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.alignmentAdjust = pList.get(3).getLength();
      this.alignmentBaseline = pList.get(4).getEnum();
      this.baselineShift = pList.get(15).getLength();
      this.blockProgressionDimension = pList.get(17).getLengthRange();
      this.commonBorderPaddingBackground = pList.getBorderPaddingBackgroundProps();
      this.commonMarginInline = pList.getMarginInlineProps();
      this.clip = pList.get(71).getEnum();
      this.dominantBaseline = pList.get(88).getEnum();
      this.inlineProgressionDimension = pList.get(127).getLengthRange();
      this.keepTogether = pList.get(131).getKeep();
      this.lineHeight = pList.get(144).getSpace();
      this.overflow = pList.get(169).getEnum();
      this.referenceOrientation = pList.get(197).getNumeric();
      this.writingMode = pList.get(267).getEnum();
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         if (localName.equals("marker")) {
            if (this.blockItemFound) {
               this.nodesOutOfOrderError(loc, "fo:marker", "(%block;)");
            }
         } else if (!this.isBlockItem(nsURI, localName)) {
            this.invalidChildError(loc, nsURI, localName);
         } else {
            this.blockItemFound = true;
         }
      }

   }

   protected void endOfNode() throws FOPException {
      if (!this.blockItemFound) {
         this.missingChildElementError("marker* (%block;)+");
      }

   }

   public Length getAlignmentAdjust() {
      return this.alignmentAdjust;
   }

   public int getAlignmentBaseline() {
      return this.alignmentBaseline;
   }

   public Length getBaselineShift() {
      return this.baselineShift;
   }

   public LengthRangeProperty getBlockProgressionDimension() {
      return this.blockProgressionDimension;
   }

   public int getClip() {
      return this.clip;
   }

   public CommonBorderPaddingBackground getCommonBorderPaddingBackground() {
      return this.commonBorderPaddingBackground;
   }

   public CommonMarginInline getCommonMarginInline() {
      return this.commonMarginInline;
   }

   public int getDominantBaseline() {
      return this.dominantBaseline;
   }

   public KeepProperty getKeepTogether() {
      return this.keepTogether;
   }

   public LengthRangeProperty getInlineProgressionDimension() {
      return this.inlineProgressionDimension;
   }

   public SpaceProperty getLineHeight() {
      return this.lineHeight;
   }

   public int getOverflow() {
      return this.overflow;
   }

   public int getReferenceOrientation() {
      return this.referenceOrientation.getValue();
   }

   public int getWritingMode() {
      return this.writingMode;
   }

   public String getLocalName() {
      return "inline-container";
   }

   public int getNameId() {
      return 36;
   }
}

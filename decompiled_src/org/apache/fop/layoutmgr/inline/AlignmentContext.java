package org.apache.fop.layoutmgr.inline;

import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.datatypes.SimplePercentBaseContext;
import org.apache.fop.fo.Constants;
import org.apache.fop.fonts.Font;

public class AlignmentContext implements Constants {
   private int areaHeight;
   private int lineHeight;
   private int alignmentPoint;
   private int baselineShiftValue;
   private int alignmentBaselineIdentifier;
   private int xHeight;
   private ScaledBaselineTable scaledBaselineTable = null;
   private ScaledBaselineTable actualBaselineTable = null;
   private AlignmentContext parentAlignmentContext = null;

   public AlignmentContext(int height, Length alignmentAdjust, int alignmentBaseline, Length baselineShift, int dominantBaseline, AlignmentContext parentAlignmentContext) {
      this.areaHeight = height;
      this.lineHeight = height;
      this.xHeight = height;
      this.parentAlignmentContext = parentAlignmentContext;
      this.scaledBaselineTable = parentAlignmentContext.getScaledBaselineTable();
      this.setAlignmentBaselineIdentifier(alignmentBaseline, parentAlignmentContext.getDominantBaselineIdentifier());
      this.setBaselineShift(baselineShift);
      int dominantBaselineIdentifier = parentAlignmentContext.getDominantBaselineIdentifier();
      boolean newScaledBaselineTableRequired = false;
      if (this.baselineShiftValue != 0) {
         newScaledBaselineTableRequired = true;
      }

      switch (dominantBaseline) {
         case 9:
            newScaledBaselineTableRequired = this.baselineShiftValue != 0;
         case 87:
         case 157:
            break;
         case 116:
            newScaledBaselineTableRequired = true;
            break;
         default:
            newScaledBaselineTableRequired = true;
            dominantBaselineIdentifier = dominantBaseline;
      }

      this.actualBaselineTable = ScaledBaselineTableFactory.makeGraphicsScaledBaselineTable(height, dominantBaselineIdentifier, this.scaledBaselineTable.getWritingMode());
      if (newScaledBaselineTableRequired) {
         this.scaledBaselineTable = ScaledBaselineTableFactory.makeGraphicsScaledBaselineTable(height, dominantBaselineIdentifier, this.scaledBaselineTable.getWritingMode());
      }

      this.setAlignmentAdjust(alignmentAdjust);
   }

   public AlignmentContext(Font font, int lineHeight, Length alignmentAdjust, int alignmentBaseline, Length baselineShift, int dominantBaseline, AlignmentContext parentAlignmentContext) {
      this.areaHeight = font.getAscender() - font.getDescender();
      this.lineHeight = lineHeight;
      this.parentAlignmentContext = parentAlignmentContext;
      this.scaledBaselineTable = parentAlignmentContext.getScaledBaselineTable();
      this.xHeight = font.getXHeight();
      this.setAlignmentBaselineIdentifier(alignmentBaseline, parentAlignmentContext.getDominantBaselineIdentifier());
      this.setBaselineShift(baselineShift);
      int dominantBaselineIdentifier = parentAlignmentContext.getDominantBaselineIdentifier();
      boolean newScaledBaselineTableRequired = false;
      if (this.baselineShiftValue != 0) {
         newScaledBaselineTableRequired = true;
      }

      switch (dominantBaseline) {
         case 9:
            newScaledBaselineTableRequired = this.baselineShiftValue != 0;
         case 87:
         case 157:
            break;
         case 116:
            newScaledBaselineTableRequired = true;
            break;
         default:
            newScaledBaselineTableRequired = true;
            dominantBaselineIdentifier = dominantBaseline;
      }

      this.actualBaselineTable = ScaledBaselineTableFactory.makeFontScaledBaselineTable(font, dominantBaselineIdentifier, this.scaledBaselineTable.getWritingMode());
      if (newScaledBaselineTableRequired) {
         this.scaledBaselineTable = ScaledBaselineTableFactory.makeFontScaledBaselineTable(font, dominantBaselineIdentifier, this.scaledBaselineTable.getWritingMode());
      }

      this.setAlignmentAdjust(alignmentAdjust);
   }

   public AlignmentContext(Font font, int lineHeight, int writingMode) {
      this.areaHeight = font.getAscender() - font.getDescender();
      this.lineHeight = lineHeight;
      this.xHeight = font.getXHeight();
      this.parentAlignmentContext = null;
      this.scaledBaselineTable = ScaledBaselineTableFactory.makeFontScaledBaselineTable(font, writingMode);
      this.actualBaselineTable = this.scaledBaselineTable;
      this.alignmentBaselineIdentifier = this.getDominantBaselineIdentifier();
      this.alignmentPoint = font.getAscender();
      this.baselineShiftValue = 0;
   }

   public int getAlignmentPoint() {
      return this.alignmentPoint;
   }

   public int getBaselineShiftValue() {
      return this.baselineShiftValue;
   }

   public int getAlignmentBaselineIdentifier() {
      return this.alignmentBaselineIdentifier;
   }

   private void setAlignmentBaselineIdentifier(int alignmentBaseline, int parentDominantBaselineIdentifier) {
      switch (alignmentBaseline) {
         case 4:
         case 6:
         case 14:
         case 24:
         case 56:
         case 59:
         case 82:
         case 84:
         case 141:
         case 142:
            this.alignmentBaselineIdentifier = alignmentBaseline;
            break;
         case 9:
         case 12:
            this.alignmentBaselineIdentifier = parentDominantBaselineIdentifier;
      }

   }

   private void setAlignmentAdjust(Length alignmentAdjust) {
      int beforeEdge = this.actualBaselineTable.getBaseline(14);
      switch (alignmentAdjust.getEnum()) {
         case 4:
         case 6:
         case 14:
         case 24:
         case 56:
         case 59:
         case 82:
         case 84:
         case 141:
         case 142:
            this.alignmentPoint = beforeEdge - this.actualBaselineTable.getBaseline(alignmentAdjust.getEnum());
            break;
         case 9:
            this.alignmentPoint = beforeEdge - this.actualBaselineTable.getBaseline(this.alignmentBaselineIdentifier);
            break;
         case 12:
            this.alignmentPoint = beforeEdge;
            break;
         default:
            this.alignmentPoint = beforeEdge + alignmentAdjust.getValue(new SimplePercentBaseContext((PercentBaseContext)null, 12, this.lineHeight));
      }

   }

   public ScaledBaselineTable getScaledBaselineTable() {
      return this.scaledBaselineTable;
   }

   public int getDominantBaselineIdentifier() {
      return this.scaledBaselineTable.getDominantBaselineIdentifier();
   }

   public int getWritingMode() {
      return this.scaledBaselineTable.getWritingMode();
   }

   private void setBaselineShift(Length baselineShift) {
      this.baselineShiftValue = 0;
      ScaledBaselineTable sbt = null;
      switch (baselineShift.getEnum()) {
         case 0:
            this.baselineShiftValue = baselineShift.getValue(new SimplePercentBaseContext((PercentBaseContext)null, 0, this.parentAlignmentContext.getLineHeight()));
         case 12:
         default:
            break;
         case 137:
            this.baselineShiftValue = Math.round((float)(-(this.xHeight / 2) + this.parentAlignmentContext.getActualBaselineOffset(6)));
            break;
         case 138:
            this.baselineShiftValue = Math.round((float)(this.parentAlignmentContext.getXHeight() + this.parentAlignmentContext.getActualBaselineOffset(6)));
      }

   }

   public AlignmentContext getParentAlignmentContext() {
      return this.parentAlignmentContext;
   }

   public int getBaselineOffset() {
      return this.parentAlignmentContext == null ? 0 : this.parentAlignmentContext.getScaledBaselineTable().getBaseline(this.alignmentBaselineIdentifier) - this.scaledBaselineTable.deriveScaledBaselineTable(this.parentAlignmentContext.getDominantBaselineIdentifier()).getBaseline(this.alignmentBaselineIdentifier) - this.scaledBaselineTable.getBaseline(this.parentAlignmentContext.getDominantBaselineIdentifier()) + this.baselineShiftValue;
   }

   public int getTotalBaselineOffset() {
      int offset = 0;
      if (this.parentAlignmentContext != null) {
         offset = this.getBaselineOffset() + this.parentAlignmentContext.getTotalBaselineOffset();
      }

      return offset;
   }

   public int getTotalAlignmentBaselineOffset() {
      return this.getTotalAlignmentBaselineOffset(this.alignmentBaselineIdentifier);
   }

   public int getTotalAlignmentBaselineOffset(int alignmentBaselineId) {
      int offset = this.baselineShiftValue;
      if (this.parentAlignmentContext != null) {
         offset = this.parentAlignmentContext.getTotalBaselineOffset() + this.parentAlignmentContext.getScaledBaselineTable().getBaseline(alignmentBaselineId) + this.baselineShiftValue;
      }

      return offset;
   }

   public int getActualBaselineOffset(int baselineIdentifier) {
      int offset = this.getTotalAlignmentBaselineOffset() - this.getTotalBaselineOffset();
      offset += this.actualBaselineTable.deriveScaledBaselineTable(this.alignmentBaselineIdentifier).getBaseline(baselineIdentifier);
      return offset;
   }

   private int getTotalTopOffset() {
      int offset = this.getTotalAlignmentBaselineOffset() + this.getAltitude();
      return offset;
   }

   public int getHeight() {
      return this.areaHeight;
   }

   public int getLineHeight() {
      return this.lineHeight;
   }

   public int getAltitude() {
      return this.alignmentPoint;
   }

   public int getDepth() {
      return this.getHeight() - this.alignmentPoint;
   }

   public int getXHeight() {
      return this.xHeight;
   }

   public void resizeLine(int newLineHeight, int newAlignmentPoint) {
      this.areaHeight = newLineHeight;
      this.alignmentPoint = newAlignmentPoint;
      this.scaledBaselineTable.setBeforeAndAfterBaselines(this.alignmentPoint, this.alignmentPoint - this.areaHeight);
   }

   public int getOffset() {
      int offset = false;
      int offset;
      if (this.parentAlignmentContext != null) {
         offset = this.parentAlignmentContext.getTotalTopOffset() - this.getTotalTopOffset();
      } else {
         offset = this.getAltitude() - this.scaledBaselineTable.getBaseline(142);
      }

      return offset;
   }

   public boolean usesInitialBaselineTable() {
      return this.parentAlignmentContext == null || this.scaledBaselineTable == this.parentAlignmentContext.getScaledBaselineTable() && this.parentAlignmentContext.usesInitialBaselineTable();
   }

   private boolean isHorizontalWritingMode() {
      return this.getWritingMode() == 79 || this.getWritingMode() == 121;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer(64);
      sb.append("ah=" + this.areaHeight);
      sb.append(" lp=" + this.lineHeight);
      sb.append(" ap=" + this.alignmentPoint);
      sb.append(" ab=" + this.alignmentBaselineIdentifier);
      sb.append(" bs=" + this.baselineShiftValue);
      return sb.toString();
   }
}

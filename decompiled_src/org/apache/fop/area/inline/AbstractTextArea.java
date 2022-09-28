package org.apache.fop.area.inline;

public abstract class AbstractTextArea extends InlineParent {
   private int textWordSpaceAdjust = 0;
   private int textLetterSpaceAdjust = 0;
   private TextAdjustingInfo textAdjustingInfo = null;
   private int baselineOffset = 0;

   public AbstractTextArea() {
   }

   public AbstractTextArea(int stretch, int shrink, int adj) {
      this.textAdjustingInfo = new TextAdjustingInfo(stretch, shrink, adj);
   }

   public int getTextWordSpaceAdjust() {
      return this.textWordSpaceAdjust;
   }

   public void setTextWordSpaceAdjust(int textWordSpaceAdjust) {
      this.textWordSpaceAdjust = textWordSpaceAdjust;
   }

   public int getTextLetterSpaceAdjust() {
      return this.textLetterSpaceAdjust;
   }

   public void setTextLetterSpaceAdjust(int textLetterSpaceAdjust) {
      this.textLetterSpaceAdjust = textLetterSpaceAdjust;
   }

   public void setSpaceDifference(int spaceDiff) {
      this.textAdjustingInfo.spaceDifference = spaceDiff;
   }

   public boolean applyVariationFactor(double variationFactor, int lineStretch, int lineShrink) {
      if (this.textAdjustingInfo != null) {
         double balancingFactor = 1.0;
         if (variationFactor < 0.0) {
            if (this.textWordSpaceAdjust < 0) {
               balancingFactor = (double)this.textAdjustingInfo.availableStretch / (double)this.textAdjustingInfo.availableShrink * ((double)lineShrink / (double)lineStretch);
            } else {
               balancingFactor = (double)this.textAdjustingInfo.availableShrink / (double)this.textAdjustingInfo.availableStretch * ((double)lineStretch / (double)lineShrink);
            }
         }

         this.textWordSpaceAdjust = (int)((double)(this.textWordSpaceAdjust - this.textAdjustingInfo.spaceDifference) * variationFactor * balancingFactor) + this.textAdjustingInfo.spaceDifference;
         this.textLetterSpaceAdjust = (int)((double)this.textLetterSpaceAdjust * variationFactor);
         int oldAdjustment = this.textAdjustingInfo.adjustment;
         TextAdjustingInfo var10000 = this.textAdjustingInfo;
         var10000.adjustment = (int)((double)var10000.adjustment * balancingFactor * variationFactor);
         this.ipd += this.textAdjustingInfo.adjustment - oldAdjustment;
      }

      return false;
   }

   public int getBaselineOffset() {
      return this.baselineOffset;
   }

   public void setBaselineOffset(int baselineOffset) {
      this.baselineOffset = baselineOffset;
   }

   protected class TextAdjustingInfo extends InlineArea.InlineAdjustingInfo {
      protected int spaceDifference = 0;

      protected TextAdjustingInfo(int stretch, int shrink, int adj) {
         super(stretch, shrink, adj);
      }
   }
}

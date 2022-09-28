package org.apache.fop.area.inline;

import java.io.Serializable;
import org.apache.fop.area.Area;
import org.apache.fop.area.LineArea;
import org.apache.fop.area.Trait;

public class InlineArea extends Area {
   protected int offset = 0;
   private Area parentArea = null;
   private int storedIPDVariation = 0;
   protected InlineAdjustingInfo adjustingInfo = null;

   public InlineAdjustingInfo getAdjustingInfo() {
      return this.adjustingInfo;
   }

   public void setAdjustingInfo(int stretch, int shrink, int adjustment) {
      this.adjustingInfo = new InlineAdjustingInfo(stretch, shrink, adjustment);
   }

   public void setAdjustment(int adjustment) {
      if (this.adjustingInfo != null) {
         this.adjustingInfo.adjustment = adjustment;
      }

   }

   public void increaseIPD(int ipd) {
      this.ipd += ipd;
   }

   public void setOffset(int offset) {
      this.offset = offset;
   }

   public int getOffset() {
      return this.offset;
   }

   public void setParentArea(Area parentArea) {
      this.parentArea = parentArea;
   }

   public Area getParentArea() {
      return this.parentArea;
   }

   public void addChildArea(Area childArea) {
      super.addChildArea(childArea);
      if (childArea instanceof InlineArea) {
         ((InlineArea)childArea).setParentArea(this);
      }

   }

   public boolean hasUnderline() {
      return this.getTraitAsBoolean(Trait.UNDERLINE);
   }

   public boolean hasOverline() {
      return this.getTraitAsBoolean(Trait.OVERLINE);
   }

   public boolean hasLineThrough() {
      return this.getTraitAsBoolean(Trait.LINETHROUGH);
   }

   public boolean isBlinking() {
      return this.getTraitAsBoolean(Trait.BLINK);
   }

   public boolean applyVariationFactor(double variationFactor, int lineStretch, int lineShrink) {
      if (this.adjustingInfo != null) {
         this.setIPD(this.getIPD() + this.adjustingInfo.applyVariationFactor(variationFactor));
      }

      return false;
   }

   public void handleIPDVariation(int ipdVariation) {
      this.increaseIPD(ipdVariation);
      this.notifyIPDVariation(ipdVariation);
   }

   protected void notifyIPDVariation(int ipdVariation) {
      if (this.getParentArea() instanceof InlineArea) {
         ((InlineArea)this.getParentArea()).handleIPDVariation(ipdVariation);
      } else if (this.getParentArea() instanceof LineArea) {
         ((LineArea)this.getParentArea()).handleIPDVariation(ipdVariation);
      } else if (this.getParentArea() == null) {
         this.storedIPDVariation += ipdVariation;
      }

   }

   protected class InlineAdjustingInfo implements Serializable {
      protected int availableStretch;
      protected int availableShrink;
      protected int adjustment;

      protected InlineAdjustingInfo(int stretch, int shrink, int adj) {
         this.availableStretch = stretch;
         this.availableShrink = shrink;
         this.adjustment = adj;
      }

      protected int applyVariationFactor(double variationFactor) {
         int oldAdjustment = this.adjustment;
         this.adjustment = (int)((double)this.adjustment * variationFactor);
         return this.adjustment - oldAdjustment;
      }
   }
}

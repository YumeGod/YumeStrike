package org.apache.fop.area;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.fop.area.inline.InlineArea;

public class LineArea extends Area {
   private LineAdjustingInfo adjustingInfo = null;
   private List inlineAreas = new ArrayList();

   public LineArea() {
   }

   public LineArea(int alignment, int diff, int stretch, int shrink) {
      this.adjustingInfo = new LineAdjustingInfo(alignment, diff, stretch, shrink);
   }

   public void addChildArea(Area childArea) {
      if (childArea instanceof InlineArea) {
         this.addInlineArea((InlineArea)childArea);
         ((InlineArea)childArea).setParentArea(this);
      }

   }

   public void addInlineArea(InlineArea area) {
      this.inlineAreas.add(area);
   }

   public List getInlineAreas() {
      return this.inlineAreas;
   }

   public int getStartIndent() {
      return this.hasTrait(Trait.START_INDENT) ? this.getTraitAsInteger(Trait.START_INDENT) : 0;
   }

   public void updateExtentsFromChildren() {
      int ipd = 0;
      int bpd = 0;
      int i = 0;

      for(int len = this.inlineAreas.size(); i < len; ++i) {
         ipd = Math.max(ipd, ((InlineArea)this.inlineAreas.get(i)).getAllocIPD());
         bpd += ((InlineArea)this.inlineAreas.get(i)).getAllocBPD();
      }

      this.setIPD(ipd);
      this.setBPD(bpd);
   }

   public void handleIPDVariation(int var1) {
      // $FF: Couldn't be decompiled
   }

   public void finalise() {
      if (this.adjustingInfo.lineAlignment == 70) {
         boolean bUnresolvedAreasPresent = false;
         int i = 0;

         for(int len = this.inlineAreas.size(); i < len; ++i) {
            bUnresolvedAreasPresent |= ((InlineArea)this.inlineAreas.get(i)).applyVariationFactor(this.adjustingInfo.variationFactor, this.adjustingInfo.availableStretch, this.adjustingInfo.availableShrink);
         }

         if (!bUnresolvedAreasPresent) {
            this.adjustingInfo = null;
         } else {
            if (!this.adjustingInfo.bAddedToAreaTree) {
               this.adjustingInfo.bAddedToAreaTree = true;
            }

            this.adjustingInfo.variationFactor = 1.0;
         }
      }

   }

   private class LineAdjustingInfo implements Serializable {
      private int lineAlignment;
      private int difference;
      private int availableStretch;
      private int availableShrink;
      private double variationFactor;
      private boolean bAddedToAreaTree;

      private LineAdjustingInfo(int alignment, int diff, int stretch, int shrink) {
         this.lineAlignment = alignment;
         this.difference = diff;
         this.availableStretch = stretch;
         this.availableShrink = shrink;
         this.variationFactor = 1.0;
         this.bAddedToAreaTree = false;
      }

      // $FF: synthetic method
      LineAdjustingInfo(int x1, int x2, int x3, int x4, Object x5) {
         this(x1, x2, x3, x4);
      }

      // $FF: synthetic method
      static double access$234(LineAdjustingInfo x0, double x1) {
         return x0.variationFactor *= x1;
      }

      // $FF: synthetic method
      static int access$300(LineAdjustingInfo x0) {
         return x0.difference;
      }

      // $FF: synthetic method
      static int access$320(LineAdjustingInfo x0, int x1) {
         return x0.difference -= x1;
      }
   }
}

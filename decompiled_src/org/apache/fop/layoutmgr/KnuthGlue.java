package org.apache.fop.layoutmgr;

import org.apache.fop.traits.MinOptMax;

public class KnuthGlue extends KnuthElement {
   private final int stretch;
   private final int shrink;
   private final Adjustment adjustmentClass;

   public KnuthGlue(MinOptMax minOptMax, Position pos, boolean auxiliary) {
      super(minOptMax.getOpt(), pos, auxiliary);
      this.stretch = minOptMax.getStretch();
      this.shrink = minOptMax.getShrink();
      this.adjustmentClass = Adjustment.NO_ADJUSTMENT;
   }

   public KnuthGlue(int width, int stretch, int shrink, Position pos, boolean auxiliary) {
      super(width, pos, auxiliary);
      this.stretch = stretch;
      this.shrink = shrink;
      this.adjustmentClass = Adjustment.NO_ADJUSTMENT;
   }

   public KnuthGlue(int width, int stretch, int shrink, Adjustment adjustmentClass, Position pos, boolean auxiliary) {
      super(width, pos, auxiliary);
      this.stretch = stretch;
      this.shrink = shrink;
      this.adjustmentClass = adjustmentClass;
   }

   public boolean isGlue() {
      return true;
   }

   public int getStretch() {
      return this.stretch;
   }

   public int getShrink() {
      return this.shrink;
   }

   public Adjustment getAdjustmentClass() {
      return this.adjustmentClass;
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer(64);
      if (this.isAuxiliary()) {
         buffer.append("aux. ");
      }

      buffer.append("glue");
      buffer.append(" w=").append(this.getWidth());
      buffer.append(" stretch=").append(this.getStretch());
      buffer.append(" shrink=").append(this.getShrink());
      if (!this.getAdjustmentClass().equals(Adjustment.NO_ADJUSTMENT)) {
         buffer.append(" adj-class=").append(this.getAdjustmentClass());
      }

      return buffer.toString();
   }
}

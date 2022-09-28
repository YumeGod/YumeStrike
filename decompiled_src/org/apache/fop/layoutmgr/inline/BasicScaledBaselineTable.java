package org.apache.fop.layoutmgr.inline;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fo.Constants;

public class BasicScaledBaselineTable implements ScaledBaselineTable, Constants {
   protected Log log;
   private int altitude;
   private int depth;
   private int xHeight;
   private int dominantBaselineIdentifier;
   private int writingMode;
   private int dominantBaselineOffset;
   private int beforeEdgeOffset;
   private int afterEdgeOffset;
   private static final float HANGING_BASELINE_FACTOR = 0.8F;
   private static final float MATHEMATICAL_BASELINE_FACTOR = 0.5F;

   public BasicScaledBaselineTable(int altitude, int depth, int xHeight, int dominantBaselineIdentifier, int writingMode) {
      this.log = LogFactory.getLog(BasicScaledBaselineTable.class);
      this.altitude = altitude;
      this.depth = depth;
      this.xHeight = xHeight;
      this.dominantBaselineIdentifier = dominantBaselineIdentifier;
      this.writingMode = writingMode;
      this.dominantBaselineOffset = this.getBaselineDefaultOffset(this.dominantBaselineIdentifier);
      this.beforeEdgeOffset = altitude - this.dominantBaselineOffset;
      this.afterEdgeOffset = depth - this.dominantBaselineOffset;
   }

   public int getDominantBaselineIdentifier() {
      return this.dominantBaselineIdentifier;
   }

   public int getWritingMode() {
      return this.writingMode;
   }

   public int getBaseline(int baselineIdentifier) {
      int offset = 0;
      if (!this.isHorizontalWritingMode()) {
         switch (baselineIdentifier) {
            case 20:
            case 143:
            case 144:
            case 145:
               this.log.warn("The given baseline is only supported for horizontal writing modes");
               return 0;
         }
      }

      switch (baselineIdentifier) {
         case 4:
         case 20:
            offset = this.afterEdgeOffset;
            break;
         case 6:
         case 24:
         case 56:
         case 59:
         case 82:
         case 84:
         case 141:
         case 142:
         case 143:
         case 144:
            offset = this.getBaselineDefaultOffset(baselineIdentifier) - this.dominantBaselineOffset;
            break;
         case 14:
         case 145:
            offset = this.beforeEdgeOffset;
      }

      return offset;
   }

   private boolean isHorizontalWritingMode() {
      return this.writingMode == 79 || this.writingMode == 121;
   }

   private int getBaselineDefaultOffset(int baselineIdentifier) {
      int offset = 0;
      switch (baselineIdentifier) {
         case 6:
            offset = 0;
            break;
         case 24:
            offset = (this.altitude - this.depth) / 2 + this.depth;
            break;
         case 56:
            offset = Math.round((float)this.altitude * 0.8F);
            break;
         case 59:
         case 141:
            offset = this.depth;
            break;
         case 82:
            offset = Math.round((float)this.altitude * 0.5F);
            break;
         case 84:
            offset = this.xHeight / 2;
            break;
         case 142:
            offset = this.altitude;
      }

      return offset;
   }

   public void setBeforeAndAfterBaselines(int beforeBaseline, int afterBaseline) {
      this.beforeEdgeOffset = beforeBaseline;
      this.afterEdgeOffset = afterBaseline;
   }

   public ScaledBaselineTable deriveScaledBaselineTable(int baselineIdentifier) {
      BasicScaledBaselineTable bac = new BasicScaledBaselineTable(this.altitude, this.depth, this.xHeight, baselineIdentifier, this.writingMode);
      return bac;
   }
}

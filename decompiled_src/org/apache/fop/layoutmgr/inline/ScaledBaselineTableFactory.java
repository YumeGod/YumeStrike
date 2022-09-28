package org.apache.fop.layoutmgr.inline;

import org.apache.fop.fo.Constants;
import org.apache.fop.fonts.Font;

public class ScaledBaselineTableFactory implements Constants {
   public static ScaledBaselineTable makeFontScaledBaselineTable(Font font, int dominantBaselineIdentifier, int writingMode) {
      return new BasicScaledBaselineTable(font.getAscender(), font.getDescender(), font.getXHeight(), dominantBaselineIdentifier, writingMode);
   }

   public static ScaledBaselineTable makeFontScaledBaselineTable(Font font, int writingMode) {
      return makeFontScaledBaselineTable(font, 6, writingMode);
   }

   public static ScaledBaselineTable makeGraphicsScaledBaselineTable(int height, int dominantBaselineIdentifier, int writingMode) {
      return new BasicScaledBaselineTable(height, 0, height, dominantBaselineIdentifier, writingMode);
   }
}

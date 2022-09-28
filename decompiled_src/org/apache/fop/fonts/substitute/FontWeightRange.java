package org.apache.fop.fonts.substitute;

import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FontWeightRange {
   protected static Log log = LogFactory.getLog("org.apache.fop.render.fonts");
   private int start;
   private int end;

   public static FontWeightRange valueOf(String weightRangeString) {
      StringTokenizer rangeToken = new StringTokenizer(weightRangeString, "..");
      FontWeightRange weightRange = null;
      if (rangeToken.countTokens() == 2) {
         String weightString = rangeToken.nextToken().trim();

         try {
            int start = Integer.parseInt(weightString);
            if (start % 100 != 0) {
               log.error("font-weight start range is not a multiple of 100");
            }

            int end = Integer.parseInt(rangeToken.nextToken());
            if (end % 100 != 0) {
               log.error("font-weight end range is not a multiple of 100");
            }

            if (start <= end) {
               weightRange = new FontWeightRange(start, end);
            } else {
               log.error("font-weight start range is greater than end range");
            }
         } catch (NumberFormatException var6) {
            log.error("invalid font-weight value " + weightString);
         }
      }

      return weightRange;
   }

   public FontWeightRange(int start, int end) {
      this.start = start;
      this.end = end;
   }

   public boolean isWithinRange(int value) {
      return value >= this.start && value <= this.end;
   }

   public String toString() {
      return this.start + ".." + this.end;
   }

   public int[] toArray() {
      int cnt = 0;

      for(int i = this.start; i <= this.end; i += 100) {
         ++cnt;
      }

      int[] range = new int[cnt];

      for(int i = 0; i < cnt; ++i) {
         range[i] = this.start + i * 100;
      }

      return range;
   }
}

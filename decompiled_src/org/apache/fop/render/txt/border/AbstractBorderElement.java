package org.apache.fop.render.txt.border;

import java.awt.Point;
import java.util.Arrays;
import org.apache.fop.area.CTM;
import org.apache.fop.fo.Constants;
import org.apache.fop.render.txt.TXTState;

public abstract class AbstractBorderElement implements Constants {
   public static final int UP = 0;
   public static final int RIGHT = 1;
   public static final int DOWN = 2;
   public static final int LEFT = 3;
   protected int[] data = new int[]{0, 0, 0, 0};

   public AbstractBorderElement() {
   }

   public AbstractBorderElement(int type) {
      for(int i = 0; i < 4; ++i) {
         this.data[i] = type >> i & 1;
      }

   }

   public int getData(int side) {
      return this.data[side];
   }

   public void setData(int side, int value) {
      this.data[side] = value;
   }

   public void transformElement(TXTState state) {
      double[] da = state.getResultCTM().toArray();
      CTM ctm = new CTM(da[0], -da[1], -da[2], da[3], 0.0, 0.0);
      Point[] pa = new Point[]{new Point(0, this.data[0]), new Point(this.data[1], 0), new Point(0, -this.data[2]), new Point(-this.data[3], 0)};
      Arrays.fill(this.data, 0);

      for(int i = 0; i < 4; ++i) {
         Point p = state.transformPoint(pa[i], ctm);
         int length = (int)p.distance(0.0, 0.0);
         if (p.x == 0 && p.y > 0) {
            this.data[0] = length;
         } else if (p.x == 0 && p.y < 0) {
            this.data[2] = length;
         } else if (p.x > 0 && p.y == 0) {
            this.data[1] = length;
         } else if (p.x < 0 && p.y == 0) {
            this.data[3] = length;
         }
      }

   }

   public abstract AbstractBorderElement merge(AbstractBorderElement var1);

   public abstract char convert2Char();
}

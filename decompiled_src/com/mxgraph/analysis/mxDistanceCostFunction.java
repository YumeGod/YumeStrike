package com.mxgraph.analysis;

import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;

public class mxDistanceCostFunction implements mxICostFunction {
   public double getCost(mxCellState var1) {
      double var2 = 0.0;
      int var4 = var1.getAbsolutePointCount();
      if (var4 > 0) {
         mxPoint var5 = var1.getAbsolutePoint(0);

         for(int var6 = 1; var6 < var4; ++var6) {
            mxPoint var7 = var1.getAbsolutePoint(var6);
            var2 += var7.getPoint().distance(var5.getPoint());
            var5 = var7;
         }
      }

      return var2;
   }
}

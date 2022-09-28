package com.mxgraph.view;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxRectangle;
import java.util.Hashtable;

public class mxTemporaryCellStates {
   protected mxGraphView view;
   protected Hashtable oldStates;
   protected mxRectangle oldBounds;
   protected double oldScale;

   public mxTemporaryCellStates(mxGraphView var1) {
      this(var1, 1.0, (Object[])null);
   }

   public mxTemporaryCellStates(mxGraphView var1, double var2) {
      this(var1, var2, (Object[])null);
   }

   public mxTemporaryCellStates(mxGraphView var1, double var2, Object[] var4) {
      this.view = var1;
      this.oldBounds = var1.getGraphBounds();
      this.oldStates = var1.getStates();
      this.oldScale = var1.getScale();
      var1.setStates(new Hashtable());
      var1.setScale(var2);
      if (var4 != null) {
         mxCellState var5 = var1.createState(new mxCell());

         for(int var6 = 0; var6 < var4.length; ++var6) {
            var1.validateBounds(var5, var4[var6]);
         }

         mxRectangle var9 = null;

         for(int var7 = 0; var7 < var4.length; ++var7) {
            mxRectangle var8 = var1.validatePoints(var5, var4[var7]);
            if (var8 != null) {
               if (var9 == null) {
                  var9 = var8;
               } else {
                  var9.add(var8);
               }
            }
         }

         if (var9 == null) {
            var9 = new mxRectangle();
         }

         var1.setGraphBounds(var9);
      }

   }

   public void destroy() {
      this.view.setScale(this.oldScale);
      this.view.setStates(this.oldStates);
      this.view.setGraphBounds(this.oldBounds);
   }
}

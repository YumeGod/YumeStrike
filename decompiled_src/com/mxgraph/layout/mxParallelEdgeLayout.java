package com.mxgraph.layout;

import com.mxgraph.model.mxCellPath;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class mxParallelEdgeLayout extends mxGraphLayout {
   protected int spacing;

   public mxParallelEdgeLayout(mxGraph var1) {
      this(var1, 20);
   }

   public mxParallelEdgeLayout(mxGraph var1, int var2) {
      super(var1);
      this.spacing = var2;
   }

   public void execute(Object var1) {
      Map var2 = this.findParallels(var1);
      this.graph.getModel().beginUpdate();

      try {
         Iterator var3 = var2.values().iterator();

         while(var3.hasNext()) {
            List var4 = (List)var3.next();
            if (var4.size() > 1) {
               this.layout(var4);
            }
         }
      } finally {
         this.graph.getModel().endUpdate();
      }

   }

   protected Map findParallels(Object var1) {
      Hashtable var2 = new Hashtable();
      mxIGraphModel var3 = this.graph.getModel();
      int var4 = var3.getChildCount(var1);

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = var3.getChildAt(var1, var5);
         if (!this.isEdgeIgnored(var6)) {
            String var7 = this.getEdgeId(var6);
            if (var7 != null) {
               if (!var2.containsKey(var7)) {
                  var2.put(var7, new ArrayList());
               }

               ((List)var2.get(var7)).add(var6);
            }
         }
      }

      return var2;
   }

   protected String getEdgeId(Object var1) {
      mxGraphView var2 = this.graph.getView();
      Object var3 = var2.getVisibleTerminal(var1, true);
      Object var4 = var2.getVisibleTerminal(var1, false);
      if (var3 instanceof mxICell && var4 instanceof mxICell) {
         String var5 = mxCellPath.create((mxICell)var3);
         String var6 = mxCellPath.create((mxICell)var4);
         return var5.compareTo(var6) > 0 ? var6 + "-" + var5 : var5 + "-" + var6;
      } else {
         return null;
      }
   }

   protected void layout(List var1) {
      Object var2 = var1.get(0);
      mxIGraphModel var3 = this.graph.getModel();
      mxGeometry var4 = var3.getGeometry(var3.getTerminal(var2, true));
      mxGeometry var5 = var3.getGeometry(var3.getTerminal(var2, false));
      double var6;
      double var8;
      if (var4 == var5) {
         var6 = var4.getX() + var4.getWidth() + (double)this.spacing;
         var8 = var4.getY() + var4.getHeight() / 2.0;

         for(int var10 = 0; var10 < var1.size(); ++var10) {
            this.route(var1.get(var10), var6, var8);
            var6 += (double)this.spacing;
         }
      } else if (var4 != null && var5 != null) {
         var6 = var4.getX() + var4.getWidth() / 2.0;
         var8 = var4.getY() + var4.getHeight() / 2.0;
         double var29 = var5.getX() + var5.getWidth() / 2.0;
         double var12 = var5.getY() + var5.getHeight() / 2.0;
         double var14 = var29 - var6;
         double var16 = var12 - var8;
         double var18 = Math.sqrt(var14 * var14 + var16 * var16);
         double var20 = var6 + var14 / 2.0;
         double var22 = var8 + var16 / 2.0;
         double var24 = var16 * (double)this.spacing / var18;
         double var26 = var14 * (double)this.spacing / var18;
         var20 += var24 * (double)(var1.size() - 1) / 2.0;
         var22 -= var26 * (double)(var1.size() - 1) / 2.0;

         for(int var28 = 0; var28 < var1.size(); ++var28) {
            this.route(var1.get(var28), var20, var22);
            var20 -= var24;
            var22 += var26;
         }
      }

   }

   protected void route(Object var1, double var2, double var4) {
      if (this.graph.isCellMovable(var1)) {
         this.setEdgePoints(var1, Arrays.asList(new mxPoint(var2, var4)));
      }

   }
}

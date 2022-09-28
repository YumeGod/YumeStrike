package graph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import java.util.ArrayList;

public class CircleLayout extends mxCircleLayout {
   public CircleLayout(mxGraph var1, double var2) {
      super(var1, var2);
   }

   public void execute(Object var1, int var2, int var3, double var4) {
      mxIGraphModel var6 = this.graph.getModel();
      var6.beginUpdate();

      try {
         double var7 = 0.0;
         Double var9 = null;
         Double var10 = null;
         ArrayList var11 = new ArrayList();
         int var12 = var6.getChildCount(var1);

         int var13;
         for(var13 = 0; var13 < var12; ++var13) {
            Object var14 = var6.getChildAt(var1, var13);
            if (!this.isVertexIgnored(var14)) {
               var11.add(var14);
               mxRectangle var15 = this.getVertexBounds(var14);
               if (var9 == null) {
                  var9 = var15.getY();
               } else {
                  var9 = Math.min(var9, var15.getY());
               }

               if (var10 == null) {
                  var10 = var15.getX();
               } else {
                  var10 = Math.min(var10, var15.getX());
               }

               var7 = Math.min(var7, Math.max(var15.getWidth(), var15.getHeight()));
            } else if (!this.isEdgeIgnored(var14)) {
               if (this.isResetEdges()) {
                  this.graph.resetEdge(var14);
               }

               if (this.isDisableEdgeStyle()) {
                  this.setEdgeStyleEnabled(var14, false);
               }
            }
         }

         var13 = var11.size();
         double var19 = (double)(var2 > var3 ? var3 : var2) / (2.8 * var4);
         if (this.moveCircle) {
            var9 = this.x0;
            var10 = this.y0;
         }

         this.circle(var11.toArray(), var19, var10, var9);
      } finally {
         var6.endUpdate();
      }
   }
}

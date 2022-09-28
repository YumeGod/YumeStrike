package com.mxgraph.swing.handler;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraphView;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.List;

public class mxElbowEdgeHandler extends mxEdgeHandler {
   public mxElbowEdgeHandler(mxGraphComponent var1, mxCellState var2) {
      super(var1, var2);
   }

   public String getToolTipText(MouseEvent var1) {
      int var2 = this.getIndexAt(var1.getX(), var1.getY());
      return var2 == 1 ? mxResources.get("doubleClickOrientation") : null;
   }

   protected boolean isFlipEvent(MouseEvent var1) {
      return var1.getClickCount() == 2 && this.index == 1;
   }

   public boolean isLabel(int var1) {
      return var1 == 3;
   }

   protected Rectangle[] createHandles() {
      this.p = this.createPoints(this.state);
      Rectangle[] var1 = new Rectangle[4];
      mxPoint var2 = this.state.getAbsolutePoint(0);
      mxPoint var3 = this.state.getAbsolutePoint(this.state.getAbsolutePointCount() - 1);
      var1[0] = this.createHandle(var2.getPoint());
      var1[2] = this.createHandle(var3.getPoint());
      mxGeometry var4 = this.graphComponent.getGraph().getModel().getGeometry(this.state.getCell());
      List var5 = var4.getPoints();
      Point var6 = null;
      if (var5 != null && !var5.isEmpty()) {
         mxGraphView var7 = this.graphComponent.getGraph().getView();
         var6 = var7.transformControlPoint(this.state, (mxPoint)var5.get(0)).getPoint();
      } else {
         var6 = new Point((int)(Math.round(var2.getX()) + Math.round((var3.getX() - var2.getX()) / 2.0)), (int)(Math.round(var2.getY()) + Math.round((var3.getY() - var2.getY()) / 2.0)));
      }

      var1[1] = this.createHandle(var6);
      var1[3] = this.createHandle(this.state.getAbsoluteOffset().getPoint(), mxConstants.LABEL_HANDLE_SIZE);
      if (this.isHandleVisible(3) && var1[1].intersects(var1[3])) {
         var1[1] = this.createHandle(var6, mxConstants.HANDLE_SIZE + 3);
      }

      return var1;
   }
}

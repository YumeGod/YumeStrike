package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxCurve;
import com.mxgraph.util.mxLine;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import java.awt.RenderingHints;
import java.util.List;
import java.util.Map;

public class mxCurveShape extends mxConnectorShape {
   protected mxCurve curve = new mxCurve();

   public mxCurve getCurve() {
      return this.curve;
   }

   public void paintShape(mxGraphics2DCanvas var1, mxCellState var2) {
      Object var3 = var1.getGraphics().getRenderingHint(RenderingHints.KEY_STROKE_CONTROL);
      var1.getGraphics().setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
      super.paintShape(var1, var2);
      var1.getGraphics().setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, var3);
   }

   protected void paintPolyline(mxGraphics2DCanvas var1, List var2, Map var3) {
      double var4 = var1.getScale();
      this.validateCurve(var2, var4, var3);
      var1.paintPolyline(this.curve.getCurvePoints(mxCurve.CORE_CURVE), false);
   }

   public void validateCurve(List var1, double var2, Map var4) {
      if (this.curve == null) {
         this.curve = new mxCurve(var1);
      } else {
         this.curve.updateCurve(var1);
      }

      this.curve.setLabelBuffer(var2 * mxConstants.DEFAULT_LABEL_BUFFER);
   }

   protected mxLine getMarkerVector(List var1, boolean var2, double var3) {
      double var5 = this.curve.getCurveLength(mxCurve.CORE_CURVE);
      double var7 = var3 / var5;
      if (var7 >= 1.0) {
         var7 = 1.0;
      }

      mxLine var9;
      if (var2) {
         var9 = this.curve.getCurveParallel(mxCurve.CORE_CURVE, var7);
         return new mxLine(new mxPoint(var9.getX(), var9.getY()), (mxPoint)var1.get(0));
      } else {
         var9 = this.curve.getCurveParallel(mxCurve.CORE_CURVE, 1.0 - var7);
         int var10 = var1.size();
         return new mxLine(new mxPoint(var9.getX(), var9.getY()), (mxPoint)var1.get(var10 - 1));
      }
   }
}

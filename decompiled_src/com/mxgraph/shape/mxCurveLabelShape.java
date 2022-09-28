package com.mxgraph.shape;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxCurve;
import com.mxgraph.util.mxLine;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Map;

public class mxCurveLabelShape implements mxITextShape {
   protected String lastValue;
   protected Font lastFont;
   protected List lastPoints;
   protected mxCurve curve;
   protected mxCellState state;
   protected LabelGlyphCache[] labelGlyphs;
   protected double labelSize;
   protected mxRectangle labelBounds;
   protected LabelPosition labelPosition = new LabelPosition();
   public static double LABEL_BUFFER = 30.0;
   protected boolean rotationEnabled = true;

   public mxCurveLabelShape(mxCellState var1, mxCurve var2) {
      this.state = var1;
      this.curve = var2;
   }

   public boolean getRotationEnabled() {
      return this.rotationEnabled;
   }

   public void setRotationEnabled(boolean var1) {
      this.rotationEnabled = var1;
   }

   public void paintShape(mxGraphics2DCanvas var1, String var2, mxCellState var3, Map var4) {
      Rectangle var5 = var3.getLabelBounds().getRectangle();
      Graphics2D var6 = var1.getGraphics();
      if (this.labelGlyphs == null) {
         this.updateLabelBounds(var2, var4);
      }

      if (this.labelGlyphs != null && (var6.getClipBounds() == null || var6.getClipBounds().intersects(var5))) {
         float var7 = mxUtils.getFloat(var4, mxConstants.STYLE_OPACITY, 100.0F);
         var6 = var1.createTemporaryGraphics(var4, var7, var3);
         Font var9 = mxUtils.getFont(var4, var1.getScale());
         var6.setFont(var9);
         Color var10 = mxUtils.getColor(var4, mxConstants.STYLE_FONTCOLOR, Color.black);
         var6.setColor(var10);
         var6.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

         for(int var11 = 0; var11 < var2.length(); ++var11) {
            mxLine var12 = this.labelGlyphs[var11].glyphVector;
            double var13 = Math.atan(var12.getEndPoint().getY() / var12.getEndPoint().getX());
            AffineTransform var15 = var6.getTransform();
            var6.translate(var12.getX(), var12.getY());
            var6.rotate(var13);
            var6.drawString(var2.substring(var11, var11 + 1), 0.0F, 0.0F);
            var6.setTransform(var15);
         }

         var6.dispose();
      }

   }

   public mxRectangle updateLabelBounds(String var1, Map var2) {
      double var3 = this.state.getView().getScale();
      Font var5 = mxUtils.getFont(var2, var3);
      if (this.labelGlyphs == null || var1.length() != this.labelGlyphs.length) {
         this.labelGlyphs = new LabelGlyphCache[var1.length()];
      }

      this.labelSize = 0.0;
      FontRenderContext var6 = new FontRenderContext((AffineTransform)null, false, false);

      for(int var7 = 0; var7 < var1.length(); ++var7) {
         String var8 = var1.substring(var7, var7 + 1);
         mxRectangle var9 = new mxRectangle(var5.getStringBounds(var8, var6));
         if (this.labelGlyphs[var7] == null) {
            this.labelGlyphs[var7] = new LabelGlyphCache();
         }

         this.labelGlyphs[var7].labelGlyphBounds = var9;
         this.labelSize += var9.getWidth();
         this.labelGlyphs[var7].glyph = var8;
      }

      this.lastValue = var1;
      this.lastFont = var5;
      this.lastPoints = this.curve.getGuidePoints();
      this.labelPosition.startBuffer = LABEL_BUFFER * var3;
      this.labelPosition.endBuffer = LABEL_BUFFER * var3;
      this.calculationLabelPosition(var2, var1);
      double var14;
      if (this.curve.isLabelReversed()) {
         var14 = this.labelPosition.startBuffer;
         this.labelPosition.startBuffer = this.labelPosition.endBuffer;
         this.labelPosition.endBuffer = var14;
      }

      var14 = this.curve.getCurveLength(mxCurve.LABEL_CURVE);
      double var15 = this.labelPosition.startBuffer / var14;
      mxRectangle var10 = null;

      for(int var11 = 0; var11 < var1.length(); ++var11) {
         mxLine var12 = this.curve.getCurveParallel(mxCurve.LABEL_CURVE, var15);
         this.labelGlyphs[var11].glyphVector = var12;
         this.labelGlyphs[var11].labelGlyphBounds.setX(var12.getX());
         this.labelGlyphs[var11].labelGlyphBounds.setY(var12.getY());
         this.postprocessGlyph(this.curve, var11, var15);
         var15 += (this.labelGlyphs[var11].labelGlyphBounds.getWidth() + this.labelPosition.defaultInterGlyphSpace) / var14;
         if (var10 == null) {
            var10 = (mxRectangle)this.labelGlyphs[var11].labelGlyphBounds.clone();
         } else {
            var10.add(this.labelGlyphs[var11].labelGlyphBounds);
         }
      }

      if (var10 == null) {
         mxLine var13 = this.curve.getCurveParallel(mxCurve.LABEL_CURVE, 0.5);
         var10 = new mxRectangle(var13.getX(), var13.getY(), 1.0, 1.0);
      }

      this.labelBounds = var10;
      return var10;
   }

   protected void postprocessGlyph(mxCurve var1, int var2, double var3) {
   }

   public boolean intersectsRect(mxRectangle var1) {
      if (this.labelBounds != null && !this.labelBounds.getRectangle().intersects(var1.getRectangle())) {
         return false;
      } else {
         for(int var2 = 0; var2 < this.labelGlyphs.length; ++var2) {
            if (var1.getRectangle().intersects(this.labelGlyphs[var2].labelGlyphBounds.getRectangle())) {
               return true;
            }
         }

         return false;
      }
   }

   protected void calculationLabelPosition(Map var1, String var2) {
      double var3 = this.curve.getCurveLength(mxCurve.LABEL_CURVE);
      double var5 = var3 - this.labelPosition.startBuffer - this.labelPosition.endBuffer;
      this.labelPosition.startBuffer = Math.max(this.labelPosition.startBuffer, this.labelPosition.startBuffer + var5 / 2.0 - this.labelSize / 2.0);
      this.labelPosition.endBuffer = Math.max(this.labelPosition.endBuffer, this.labelPosition.endBuffer + var5 / 2.0 - this.labelSize / 2.0);
   }

   public mxCurve getCurve() {
      return this.curve;
   }

   public void setCurve(mxCurve var1) {
      this.curve = var1;
   }

   public class LabelPosition {
      public double startBuffer;
      public double endBuffer;
      public double defaultInterGlyphSpace;

      public LabelPosition() {
         this.startBuffer = mxCurveLabelShape.LABEL_BUFFER;
         this.endBuffer = mxCurveLabelShape.LABEL_BUFFER;
         this.defaultInterGlyphSpace = 0.0;
      }
   }

   public class LabelGlyphCache {
      public mxRectangle labelGlyphBounds;
      public String glyph;
      public mxLine glyphVector;
   }
}

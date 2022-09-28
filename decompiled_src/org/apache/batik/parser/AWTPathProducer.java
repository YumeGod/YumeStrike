package org.apache.batik.parser;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.Reader;
import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;

public class AWTPathProducer implements PathHandler, ShapeProducer {
   protected ExtendedGeneralPath path;
   protected float currentX;
   protected float currentY;
   protected float xCenter;
   protected float yCenter;
   protected int windingRule;

   public static Shape createShape(Reader var0, int var1) throws IOException, ParseException {
      PathParser var2 = new PathParser();
      AWTPathProducer var3 = new AWTPathProducer();
      var3.setWindingRule(var1);
      var2.setPathHandler(var3);
      var2.parse(var0);
      return var3.getShape();
   }

   public void setWindingRule(int var1) {
      this.windingRule = var1;
   }

   public int getWindingRule() {
      return this.windingRule;
   }

   public Shape getShape() {
      return this.path;
   }

   public void startPath() throws ParseException {
      this.currentX = 0.0F;
      this.currentY = 0.0F;
      this.xCenter = 0.0F;
      this.yCenter = 0.0F;
      this.path = new ExtendedGeneralPath(this.windingRule);
   }

   public void endPath() throws ParseException {
   }

   public void movetoRel(float var1, float var2) throws ParseException {
      this.path.moveTo(this.xCenter = this.currentX += var1, this.yCenter = this.currentY += var2);
   }

   public void movetoAbs(float var1, float var2) throws ParseException {
      this.path.moveTo(this.xCenter = this.currentX = var1, this.yCenter = this.currentY = var2);
   }

   public void closePath() throws ParseException {
      this.path.closePath();
      Point2D var1 = this.path.getCurrentPoint();
      this.currentX = (float)var1.getX();
      this.currentY = (float)var1.getY();
   }

   public void linetoRel(float var1, float var2) throws ParseException {
      this.path.lineTo(this.xCenter = this.currentX += var1, this.yCenter = this.currentY += var2);
   }

   public void linetoAbs(float var1, float var2) throws ParseException {
      this.path.lineTo(this.xCenter = this.currentX = var1, this.yCenter = this.currentY = var2);
   }

   public void linetoHorizontalRel(float var1) throws ParseException {
      this.path.lineTo(this.xCenter = this.currentX += var1, this.yCenter = this.currentY);
   }

   public void linetoHorizontalAbs(float var1) throws ParseException {
      this.path.lineTo(this.xCenter = this.currentX = var1, this.yCenter = this.currentY);
   }

   public void linetoVerticalRel(float var1) throws ParseException {
      this.path.lineTo(this.xCenter = this.currentX, this.yCenter = this.currentY += var1);
   }

   public void linetoVerticalAbs(float var1) throws ParseException {
      this.path.lineTo(this.xCenter = this.currentX, this.yCenter = this.currentY = var1);
   }

   public void curvetoCubicRel(float var1, float var2, float var3, float var4, float var5, float var6) throws ParseException {
      this.path.curveTo(this.currentX + var1, this.currentY + var2, this.xCenter = this.currentX + var3, this.yCenter = this.currentY + var4, this.currentX += var5, this.currentY += var6);
   }

   public void curvetoCubicAbs(float var1, float var2, float var3, float var4, float var5, float var6) throws ParseException {
      this.path.curveTo(var1, var2, this.xCenter = var3, this.yCenter = var4, this.currentX = var5, this.currentY = var6);
   }

   public void curvetoCubicSmoothRel(float var1, float var2, float var3, float var4) throws ParseException {
      this.path.curveTo(this.currentX * 2.0F - this.xCenter, this.currentY * 2.0F - this.yCenter, this.xCenter = this.currentX + var1, this.yCenter = this.currentY + var2, this.currentX += var3, this.currentY += var4);
   }

   public void curvetoCubicSmoothAbs(float var1, float var2, float var3, float var4) throws ParseException {
      this.path.curveTo(this.currentX * 2.0F - this.xCenter, this.currentY * 2.0F - this.yCenter, this.xCenter = var1, this.yCenter = var2, this.currentX = var3, this.currentY = var4);
   }

   public void curvetoQuadraticRel(float var1, float var2, float var3, float var4) throws ParseException {
      this.path.quadTo(this.xCenter = this.currentX + var1, this.yCenter = this.currentY + var2, this.currentX += var3, this.currentY += var4);
   }

   public void curvetoQuadraticAbs(float var1, float var2, float var3, float var4) throws ParseException {
      this.path.quadTo(this.xCenter = var1, this.yCenter = var2, this.currentX = var3, this.currentY = var4);
   }

   public void curvetoQuadraticSmoothRel(float var1, float var2) throws ParseException {
      this.path.quadTo(this.xCenter = this.currentX * 2.0F - this.xCenter, this.yCenter = this.currentY * 2.0F - this.yCenter, this.currentX += var1, this.currentY += var2);
   }

   public void curvetoQuadraticSmoothAbs(float var1, float var2) throws ParseException {
      this.path.quadTo(this.xCenter = this.currentX * 2.0F - this.xCenter, this.yCenter = this.currentY * 2.0F - this.yCenter, this.currentX = var1, this.currentY = var2);
   }

   public void arcRel(float var1, float var2, float var3, boolean var4, boolean var5, float var6, float var7) throws ParseException {
      this.path.arcTo(var1, var2, var3, var4, var5, this.xCenter = this.currentX += var6, this.yCenter = this.currentY += var7);
   }

   public void arcAbs(float var1, float var2, float var3, boolean var4, boolean var5, float var6, float var7) throws ParseException {
      this.path.arcTo(var1, var2, var3, var4, var5, this.xCenter = this.currentX = var6, this.yCenter = this.currentY = var7);
   }
}

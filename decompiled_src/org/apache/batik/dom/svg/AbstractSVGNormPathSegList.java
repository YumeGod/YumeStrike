package org.apache.batik.dom.svg;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.PathIterator;
import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.apache.batik.parser.DefaultPathHandler;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.PathParser;

public abstract class AbstractSVGNormPathSegList extends AbstractSVGPathSegList {
   protected AbstractSVGNormPathSegList() {
   }

   protected void doParse(String var1, ListHandler var2) throws ParseException {
      PathParser var3 = new PathParser();
      NormalizedPathSegListBuilder var4 = new NormalizedPathSegListBuilder(var2);
      var3.setPathHandler(var4);
      var3.parse(var1);
   }

   protected class SVGPathSegGenericItem extends AbstractSVGPathSegList.SVGPathSegItem {
      public SVGPathSegGenericItem(short var2, String var3, float var4, float var5, float var6, float var7, float var8, float var9) {
         super(var2, var3);
         this.x1 = var6;
         this.y1 = var7;
         this.x2 = var6;
         this.y2 = var7;
         this.x = var8;
         this.y = var9;
      }

      public void setValue(float var1, float var2, float var3, float var4, float var5, float var6) {
         this.x1 = var3;
         this.y1 = var4;
         this.x2 = var3;
         this.y2 = var4;
         this.x = var5;
         this.y = var6;
      }

      public void setValue(float var1, float var2) {
         this.x = var1;
         this.y = var2;
      }

      public void setPathSegType(short var1) {
         this.type = var1;
      }

      public float getX() {
         return this.x;
      }

      public float getY() {
         return this.y;
      }

      public void setX(float var1) {
         this.x = var1;
      }

      public void setY(float var1) {
         this.y = var1;
      }

      public float getX1() {
         return this.x1;
      }

      public float getY1() {
         return this.y1;
      }

      public void setX1(float var1) {
         this.x1 = var1;
      }

      public void setY1(float var1) {
         this.y1 = var1;
      }

      public float getX2() {
         return this.x2;
      }

      public float getY2() {
         return this.y2;
      }

      public void setX2(float var1) {
         this.x2 = var1;
      }

      public void setY2(float var1) {
         this.y2 = var1;
      }
   }

   protected class NormalizedPathSegListBuilder extends DefaultPathHandler {
      protected ListHandler listHandler;
      protected SVGPathSegGenericItem lastAbs;

      public NormalizedPathSegListBuilder(ListHandler var2) {
         this.listHandler = var2;
      }

      public void startPath() throws ParseException {
         this.listHandler.startList();
         this.lastAbs = AbstractSVGNormPathSegList.this.new SVGPathSegGenericItem((short)2, "M", 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      }

      public void endPath() throws ParseException {
         this.listHandler.endList();
      }

      public void movetoRel(float var1, float var2) throws ParseException {
         this.movetoAbs(this.lastAbs.getX() + var1, this.lastAbs.getY() + var2);
      }

      public void movetoAbs(float var1, float var2) throws ParseException {
         this.listHandler.item(AbstractSVGNormPathSegList.this.new SVGPathSegMovetoLinetoItem((short)2, "M", var1, var2));
         this.lastAbs.setX(var1);
         this.lastAbs.setY(var2);
         this.lastAbs.setPathSegType((short)2);
      }

      public void closePath() throws ParseException {
         this.listHandler.item(AbstractSVGNormPathSegList.this.new SVGPathSegItem((short)1, "z"));
      }

      public void linetoRel(float var1, float var2) throws ParseException {
         this.linetoAbs(this.lastAbs.getX() + var1, this.lastAbs.getY() + var2);
      }

      public void linetoAbs(float var1, float var2) throws ParseException {
         this.listHandler.item(AbstractSVGNormPathSegList.this.new SVGPathSegMovetoLinetoItem((short)4, "L", var1, var2));
         this.lastAbs.setX(var1);
         this.lastAbs.setY(var2);
         this.lastAbs.setPathSegType((short)4);
      }

      public void linetoHorizontalRel(float var1) throws ParseException {
         this.linetoAbs(this.lastAbs.getX() + var1, this.lastAbs.getY());
      }

      public void linetoHorizontalAbs(float var1) throws ParseException {
         this.linetoAbs(var1, this.lastAbs.getY());
      }

      public void linetoVerticalRel(float var1) throws ParseException {
         this.linetoAbs(this.lastAbs.getX(), this.lastAbs.getY() + var1);
      }

      public void linetoVerticalAbs(float var1) throws ParseException {
         this.linetoAbs(this.lastAbs.getX(), var1);
      }

      public void curvetoCubicRel(float var1, float var2, float var3, float var4, float var5, float var6) throws ParseException {
         this.curvetoCubicAbs(this.lastAbs.getX() + var1, this.lastAbs.getY() + var2, this.lastAbs.getX() + var3, this.lastAbs.getY() + var4, this.lastAbs.getX() + var5, this.lastAbs.getY() + var6);
      }

      public void curvetoCubicAbs(float var1, float var2, float var3, float var4, float var5, float var6) throws ParseException {
         this.listHandler.item(AbstractSVGNormPathSegList.this.new SVGPathSegCurvetoCubicItem((short)6, "C", var1, var2, var3, var4, var5, var6));
         this.lastAbs.setValue(var1, var2, var3, var4, var5, var6);
         this.lastAbs.setPathSegType((short)6);
      }

      public void curvetoCubicSmoothRel(float var1, float var2, float var3, float var4) throws ParseException {
         this.curvetoCubicSmoothAbs(this.lastAbs.getX() + var1, this.lastAbs.getY() + var2, this.lastAbs.getX() + var3, this.lastAbs.getY() + var4);
      }

      public void curvetoCubicSmoothAbs(float var1, float var2, float var3, float var4) throws ParseException {
         if (this.lastAbs.getPathSegType() == 6) {
            this.curvetoCubicAbs(this.lastAbs.getX() + (this.lastAbs.getX() - this.lastAbs.getX2()), this.lastAbs.getY() + (this.lastAbs.getY() - this.lastAbs.getY2()), var1, var2, var3, var4);
         } else {
            this.curvetoCubicAbs(this.lastAbs.getX(), this.lastAbs.getY(), var1, var2, var3, var4);
         }

      }

      public void curvetoQuadraticRel(float var1, float var2, float var3, float var4) throws ParseException {
         this.curvetoQuadraticAbs(this.lastAbs.getX() + var1, this.lastAbs.getY() + var2, this.lastAbs.getX() + var3, this.lastAbs.getY() + var4);
      }

      public void curvetoQuadraticAbs(float var1, float var2, float var3, float var4) throws ParseException {
         this.curvetoCubicAbs(this.lastAbs.getX() + 2.0F * (var1 - this.lastAbs.getX()) / 3.0F, this.lastAbs.getY() + 2.0F * (var2 - this.lastAbs.getY()) / 3.0F, var3 + 2.0F * (var1 - var3) / 3.0F, var4 + 2.0F * (var2 - var4) / 3.0F, var3, var4);
         this.lastAbs.setX1(var1);
         this.lastAbs.setY1(var2);
         this.lastAbs.setPathSegType((short)8);
      }

      public void curvetoQuadraticSmoothRel(float var1, float var2) throws ParseException {
         this.curvetoQuadraticSmoothAbs(this.lastAbs.getX() + var1, this.lastAbs.getY() + var2);
      }

      public void curvetoQuadraticSmoothAbs(float var1, float var2) throws ParseException {
         if (this.lastAbs.getPathSegType() == 8) {
            this.curvetoQuadraticAbs(this.lastAbs.getX() + (this.lastAbs.getX() - this.lastAbs.getX1()), this.lastAbs.getY() + (this.lastAbs.getY() - this.lastAbs.getY1()), var1, var2);
         } else {
            this.curvetoQuadraticAbs(this.lastAbs.getX(), this.lastAbs.getY(), var1, var2);
         }

      }

      public void arcRel(float var1, float var2, float var3, boolean var4, boolean var5, float var6, float var7) throws ParseException {
         this.arcAbs(var1, var2, var3, var4, var5, this.lastAbs.getX() + var6, this.lastAbs.getY() + var7);
      }

      public void arcAbs(float var1, float var2, float var3, boolean var4, boolean var5, float var6, float var7) throws ParseException {
         if (var1 != 0.0F && var2 != 0.0F) {
            double var8 = (double)this.lastAbs.getX();
            double var10 = (double)this.lastAbs.getY();
            if (var8 != (double)var6 || var10 != (double)var7) {
               Arc2D var12 = ExtendedGeneralPath.computeArc(var8, var10, (double)var1, (double)var2, (double)var3, var4, var5, (double)var6, (double)var7);
               if (var12 != null) {
                  AffineTransform var13 = AffineTransform.getRotateInstance(Math.toRadians((double)var3), var12.getCenterX(), var12.getCenterY());
                  Shape var14 = var13.createTransformedShape(var12);
                  PathIterator var15 = var14.getPathIterator(new AffineTransform());
                  float[] var16 = new float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F};
                  boolean var17 = true;

                  while(!var15.isDone()) {
                     int var18 = var15.currentSegment(var16);
                     switch (var18) {
                        case 3:
                           this.curvetoCubicAbs(var16[0], var16[1], var16[2], var16[3], var16[4], var16[5]);
                        default:
                           var15.next();
                     }
                  }

                  this.lastAbs.setPathSegType((short)10);
               }
            }
         } else {
            this.linetoAbs(var6, var7);
         }
      }
   }
}

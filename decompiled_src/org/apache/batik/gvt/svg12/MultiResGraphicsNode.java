package org.apache.batik.gvt.svg12;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.lang.ref.SoftReference;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.gvt.AbstractGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Element;

public class MultiResGraphicsNode extends AbstractGraphicsNode implements SVGConstants {
   SoftReference[] srcs;
   Element[] srcElems;
   Dimension[] minSz;
   Dimension[] maxSz;
   Rectangle2D bounds;
   BridgeContext ctx;
   Element multiImgElem;

   public MultiResGraphicsNode(Element var1, Rectangle2D var2, Element[] var3, Dimension[] var4, Dimension[] var5, BridgeContext var6) {
      this.multiImgElem = var1;
      this.srcElems = new Element[var3.length];
      this.minSz = new Dimension[var3.length];
      this.maxSz = new Dimension[var3.length];
      this.ctx = var6;

      for(int var7 = 0; var7 < var3.length; ++var7) {
         this.srcElems[var7] = var3[var7];
         this.minSz[var7] = var4[var7];
         this.maxSz[var7] = var5[var7];
      }

      this.srcs = new SoftReference[var3.length];
      this.bounds = var2;
   }

   public void primitivePaint(Graphics2D var1) {
      AffineTransform var2 = var1.getTransform();
      double var3 = Math.sqrt(var2.getShearY() * var2.getShearY() + var2.getScaleX() * var2.getScaleX());
      double var5 = Math.sqrt(var2.getShearX() * var2.getShearX() + var2.getScaleY() * var2.getScaleY());
      GraphicsNode var7 = null;
      int var8 = -1;
      double var9 = this.bounds.getWidth() * var3;
      double var11 = this.calcDist(var9, this.minSz[0], this.maxSz[0]);
      int var13 = 0;

      double var15;
      for(int var14 = 0; var14 < this.minSz.length; ++var14) {
         var15 = this.calcDist(var9, this.minSz[var14], this.maxSz[var14]);
         if (var15 < var11) {
            var11 = var15;
            var13 = var14;
         }

         if ((this.minSz[var14] == null || var9 >= (double)this.minSz[var14].width) && (this.maxSz[var14] == null || var9 <= (double)this.maxSz[var14].width) && (var8 == -1 || var13 == var14)) {
            var8 = var14;
         }
      }

      if (var8 == -1) {
         var8 = var13;
      }

      var7 = this.getGraphicsNode(var8);
      if (var7 != null) {
         Rectangle2D var32 = var7.getBounds();
         if (var32 != null) {
            var15 = var32.getWidth() * var3;
            double var17 = var32.getHeight() * var5;
            double var19 = var32.getX() * var3;
            double var21 = var32.getY() * var5;
            double var23;
            double var25;
            if (var15 < 0.0) {
               var23 = var19 + var15;
               var25 = var19;
            } else {
               var23 = var19;
               var25 = var19 + var15;
            }

            double var27;
            double var29;
            if (var17 < 0.0) {
               var27 = var21 + var17;
               var29 = var21;
            } else {
               var27 = var21;
               var29 = var21 + var17;
            }

            var15 = (double)((int)(Math.ceil(var25) - Math.floor(var23)));
            var17 = (double)((int)(Math.ceil(var29) - Math.floor(var27)));
            var3 = var15 / var32.getWidth() / var3;
            var5 = var17 / var32.getHeight() / var5;
            AffineTransform var31 = var1.getTransform();
            var31 = new AffineTransform(var31.getScaleX() * var3, var31.getShearY() * var3, var31.getShearX() * var5, var31.getScaleY() * var5, var31.getTranslateX(), var31.getTranslateY());
            var1.setTransform(var31);
            var7.paint(var1);
         }
      }
   }

   public double calcDist(double var1, Dimension var3, Dimension var4) {
      if (var3 == null) {
         return var4 == null ? 1.0E11 : Math.abs(var1 - (double)var4.width);
      } else if (var4 == null) {
         return Math.abs(var1 - (double)var3.width);
      } else {
         double var5 = (double)(var4.width + var3.width) / 2.0;
         return Math.abs(var1 - var5);
      }
   }

   public Rectangle2D getPrimitiveBounds() {
      return this.bounds;
   }

   public Rectangle2D getGeometryBounds() {
      return this.bounds;
   }

   public Rectangle2D getSensitiveBounds() {
      return this.bounds;
   }

   public Shape getOutline() {
      return this.bounds;
   }

   public GraphicsNode getGraphicsNode(int var1) {
      if (this.srcs[var1] != null) {
         Object var2 = this.srcs[var1].get();
         if (var2 != null) {
            return (GraphicsNode)var2;
         }
      }

      try {
         GVTBuilder var5 = this.ctx.getGVTBuilder();
         GraphicsNode var3 = var5.build(this.ctx, this.srcElems[var1]);
         this.srcs[var1] = new SoftReference(var3);
         return var3;
      } catch (Exception var4) {
         var4.printStackTrace();
         return null;
      }
   }
}

package org.apache.batik.ext.awt.image.renderable;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.util.Map;
import org.apache.batik.ext.awt.RenderingHintsKeyExt;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.rendered.AffineRed;
import org.apache.batik.ext.awt.image.rendered.BufferedImageCachableRed;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.TileRed;

public class TileRable8Bit extends AbstractColorInterpolationRable implements TileRable {
   private Rectangle2D tileRegion;
   private Rectangle2D tiledRegion;
   private boolean overflow;

   public Rectangle2D getTileRegion() {
      return this.tileRegion;
   }

   public void setTileRegion(Rectangle2D var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.touch();
         this.tileRegion = var1;
      }
   }

   public Rectangle2D getTiledRegion() {
      return this.tiledRegion;
   }

   public void setTiledRegion(Rectangle2D var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.touch();
         this.tiledRegion = var1;
      }
   }

   public boolean isOverflow() {
      return this.overflow;
   }

   public void setOverflow(boolean var1) {
      this.touch();
      this.overflow = var1;
   }

   public TileRable8Bit(Filter var1, Rectangle2D var2, Rectangle2D var3, boolean var4) {
      super(var1);
      this.setTileRegion(var3);
      this.setTiledRegion(var2);
      this.setOverflow(var4);
   }

   public void setSource(Filter var1) {
      this.init(var1);
   }

   public Filter getSource() {
      return (Filter)this.srcs.get(0);
   }

   public Rectangle2D getBounds2D() {
      return (Rectangle2D)this.tiledRegion.clone();
   }

   public RenderedImage createRendering(RenderContext var1) {
      RenderingHints var2 = var1.getRenderingHints();
      if (var2 == null) {
         var2 = new RenderingHints((Map)null);
      }

      AffineTransform var3 = var1.getTransform();
      double var4 = var3.getScaleX();
      double var6 = var3.getScaleY();
      double var8 = var3.getShearX();
      double var10 = var3.getShearY();
      double var12 = var3.getTranslateX();
      double var14 = var3.getTranslateY();
      double var16 = Math.sqrt(var4 * var4 + var10 * var10);
      double var18 = Math.sqrt(var6 * var6 + var8 * var8);
      Rectangle2D var20 = this.getBounds2D();
      Shape var22 = var1.getAreaOfInterest();
      Rectangle2D var21;
      if (var22 == null) {
         var21 = var20;
      } else {
         var21 = var22.getBounds2D();
         if (!var20.intersects(var21)) {
            return null;
         }

         Rectangle2D.intersect(var20, var21, var20);
      }

      Rectangle2D var23 = this.tileRegion;
      int var24 = (int)Math.ceil(var23.getWidth() * var16);
      int var25 = (int)Math.ceil(var23.getHeight() * var18);
      double var26 = (double)var24 / var23.getWidth();
      double var28 = (double)var25 / var23.getHeight();
      int var30 = (int)Math.floor(var23.getX() * var26);
      int var31 = (int)Math.floor(var23.getY() * var28);
      double var32 = (double)var30 - var23.getX() * var26;
      double var34 = (double)var31 - var23.getY() * var28;
      AffineTransform var36 = AffineTransform.getTranslateInstance(var32, var34);
      var36.scale(var26, var28);
      Filter var37 = this.getSource();
      Rectangle2D var38;
      if (this.overflow) {
         var38 = var37.getBounds2D();
      } else {
         var38 = var23;
      }

      RenderContext var39 = new RenderContext(var36, var38, var2);
      RenderedImage var40 = var37.createRendering(var39);
      if (var40 == null) {
         return null;
      } else {
         Rectangle var41 = var36.createTransformedShape(var21).getBounds();
         if (var41.width == Integer.MAX_VALUE || var41.height == Integer.MAX_VALUE) {
            var41 = new Rectangle(-536870912, -536870912, 1073741823, 1073741823);
         }

         CachableRed var45 = this.convertSourceCS(var40);
         TileRed var42 = new TileRed(var45, var41, var24, var25);
         AffineTransform var43 = new AffineTransform(var4 / var16, var10 / var16, var8 / var18, var6 / var18, var12, var14);
         var43.scale(var16 / var26, var18 / var28);
         var43.translate(-var32, -var34);
         Object var44 = var42;
         if (!var43.isIdentity()) {
            var44 = new AffineRed(var42, var43, var2);
         }

         return (RenderedImage)var44;
      }
   }

   public Rectangle2D getActualTileBounds(Rectangle2D var1) {
      Rectangle2D var2 = (Rectangle2D)this.tileRegion.clone();
      if (!(var2.getWidth() <= 0.0) && !(var2.getHeight() <= 0.0) && !(var1.getWidth() <= 0.0) && !(var1.getHeight() <= 0.0)) {
         double var3 = var2.getWidth();
         double var5 = var2.getHeight();
         double var7 = var1.getWidth();
         double var9 = var1.getHeight();
         double var11 = Math.min(var3, var7);
         double var13 = Math.min(var5, var9);
         Rectangle2D.Double var15 = new Rectangle2D.Double(var2.getX(), var2.getY(), var11, var13);
         return var15;
      } else {
         return null;
      }
   }

   public RenderedImage createTile(RenderContext var1) {
      AffineTransform var2 = var1.getTransform();
      RenderingHints var3 = var1.getRenderingHints();
      RenderingHints var4 = new RenderingHints((Map)null);
      if (var3 != null) {
         var4.add(var3);
      }

      Rectangle2D var5 = this.getBounds2D();
      Shape var6 = var1.getAreaOfInterest();
      Rectangle2D var7 = var6.getBounds2D();
      if (!var5.intersects(var7)) {
         return null;
      } else {
         Rectangle2D.intersect(var5, var7, var5);
         Rectangle2D var8 = (Rectangle2D)this.tileRegion.clone();
         if (!(var8.getWidth() <= 0.0) && !(var8.getHeight() <= 0.0) && !(var5.getWidth() <= 0.0) && !(var5.getHeight() <= 0.0)) {
            double var9 = var8.getX();
            double var11 = var8.getY();
            double var13 = var8.getWidth();
            double var15 = var8.getHeight();
            double var17 = var5.getX();
            double var19 = var5.getY();
            double var21 = var5.getWidth();
            double var23 = var5.getHeight();
            double var25 = Math.min(var13, var21);
            double var27 = Math.min(var15, var23);
            double var29 = (var17 - var9) % var13;
            double var31 = (var19 - var11) % var15;
            if (var29 > 0.0) {
               var29 = var13 - var29;
            } else {
               var29 *= -1.0;
            }

            if (var31 > 0.0) {
               var31 = var15 - var31;
            } else {
               var31 *= -1.0;
            }

            double var33 = var2.getScaleX();
            double var35 = var2.getScaleY();
            double var37 = Math.floor(var33 * var29);
            double var39 = Math.floor(var35 * var31);
            var29 = var37 / var33;
            var31 = var39 / var35;
            Rectangle2D.Double var41 = new Rectangle2D.Double(var9 + var13 - var29, var11 + var15 - var31, var29, var31);
            Rectangle2D.Double var42 = new Rectangle2D.Double(var9, var11 + var15 - var31, var25 - var29, var31);
            Rectangle2D.Double var43 = new Rectangle2D.Double(var9 + var13 - var29, var11, var29, var27 - var31);
            Rectangle2D.Double var44 = new Rectangle2D.Double(var9, var11, var25 - var29, var27 - var31);
            Rectangle2D.Double var45 = new Rectangle2D.Double(var5.getX(), var5.getY(), var25, var27);
            RenderedImage var46 = null;
            RenderedImage var47 = null;
            RenderedImage var48 = null;
            RenderedImage var49 = null;
            Filter var50 = this.getSource();
            Rectangle var51;
            AffineTransform var52;
            Rectangle2D.Double var53;
            RenderContext var54;
            if (var41.getWidth() > 0.0 && var41.getHeight() > 0.0) {
               var51 = var2.createTransformedShape(var41).getBounds();
               if (var51.width > 0 && var51.height > 0) {
                  var52 = new AffineTransform(var2);
                  var52.translate(-var41.x + var17, -var41.y + var19);
                  var53 = var41;
                  if (this.overflow) {
                     var53 = new Rectangle2D.Double(var41.x, var41.y, var21, var23);
                  }

                  var4.put(RenderingHintsKeyExt.KEY_AREA_OF_INTEREST, var53);
                  var54 = new RenderContext(var52, var53, var4);
                  var46 = var50.createRendering(var54);
               }
            }

            if (var42.getWidth() > 0.0 && var42.getHeight() > 0.0) {
               var51 = var2.createTransformedShape(var42).getBounds();
               if (var51.width > 0 && var51.height > 0) {
                  var52 = new AffineTransform(var2);
                  var52.translate(-var42.x + var17 + var29, -var42.y + var19);
                  var53 = var42;
                  if (this.overflow) {
                     var53 = new Rectangle2D.Double(var42.x - var21 + var25 - var29, var42.y, var21, var23);
                  }

                  var4.put(RenderingHintsKeyExt.KEY_AREA_OF_INTEREST, var53);
                  var54 = new RenderContext(var52, var53, var4);
                  var47 = var50.createRendering(var54);
               }
            }

            if (var43.getWidth() > 0.0 && var43.getHeight() > 0.0) {
               var51 = var2.createTransformedShape(var43).getBounds();
               if (var51.width > 0 && var51.height > 0) {
                  var52 = new AffineTransform(var2);
                  var52.translate(-var43.x + var17, -var43.y + var19 + var31);
                  var53 = var43;
                  if (this.overflow) {
                     var53 = new Rectangle2D.Double(var43.x, var43.y - var15 + var27 - var31, var21, var23);
                  }

                  var4.put(RenderingHintsKeyExt.KEY_AREA_OF_INTEREST, var53);
                  var54 = new RenderContext(var52, var53, var4);
                  var48 = var50.createRendering(var54);
               }
            }

            if (var44.getWidth() > 0.0 && var44.getHeight() > 0.0) {
               var51 = var2.createTransformedShape(var44).getBounds();
               if (var51.width > 0 && var51.height > 0) {
                  var52 = new AffineTransform(var2);
                  var52.translate(-var44.x + var17 + var29, -var44.y + var19 + var31);
                  var53 = var44;
                  if (this.overflow) {
                     var53 = new Rectangle2D.Double(var44.x - var13 + var25 - var29, var44.y - var15 + var27 - var31, var21, var23);
                  }

                  var4.put(RenderingHintsKeyExt.KEY_AREA_OF_INTEREST, var53);
                  var54 = new RenderContext(var52, var53, var4);
                  var49 = var50.createRendering(var54);
               }
            }

            var51 = var2.createTransformedShape(var45).getBounds();
            if (var51.width != 0 && var51.height != 0) {
               BufferedImage var58 = new BufferedImage(var51.width, var51.height, 2);
               Graphics2D var60 = GraphicsUtil.createGraphics(var58, var1.getRenderingHints());
               var60.translate(-var51.x, -var51.y);
               AffineTransform var59 = new AffineTransform();
               Point2D.Double var55 = new Point2D.Double();
               RenderedImage var56 = null;
               if (var46 != null) {
                  var60.drawRenderedImage(var46, var59);
                  var56 = var46;
               }

               if (var47 != null) {
                  if (var56 == null) {
                     var56 = var47;
                  }

                  var55.x = var29;
                  var55.y = 0.0;
                  var2.deltaTransform(var55, var55);
                  var55.x = Math.floor(var55.x) - (double)(var47.getMinX() - var56.getMinX());
                  var55.y = Math.floor(var55.y) - (double)(var47.getMinY() - var56.getMinY());
                  var60.drawRenderedImage(var47, var59);
               }

               if (var48 != null) {
                  if (var56 == null) {
                     var56 = var48;
                  }

                  var55.x = 0.0;
                  var55.y = var31;
                  var2.deltaTransform(var55, var55);
                  var55.x = Math.floor(var55.x) - (double)(var48.getMinX() - var56.getMinX());
                  var55.y = Math.floor(var55.y) - (double)(var48.getMinY() - var56.getMinY());
                  var60.drawRenderedImage(var48, var59);
               }

               if (var49 != null) {
                  if (var56 == null) {
                     var56 = var49;
                  }

                  var55.x = var29;
                  var55.y = var31;
                  var2.deltaTransform(var55, var55);
                  var55.x = Math.floor(var55.x) - (double)(var49.getMinX() - var56.getMinX());
                  var55.y = Math.floor(var55.y) - (double)(var49.getMinY() - var56.getMinY());
                  var60.drawRenderedImage(var49, var59);
               }

               BufferedImageCachableRed var57 = new BufferedImageCachableRed(var58, var51.x, var51.y);
               return var57;
            } else {
               return null;
            }
         } else {
            return null;
         }
      }
   }
}

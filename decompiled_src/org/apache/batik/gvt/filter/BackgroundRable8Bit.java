package org.apache.batik.gvt.filter;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.batik.ext.awt.image.CompositeRule;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.renderable.AbstractRable;
import org.apache.batik.ext.awt.image.renderable.AffineRable8Bit;
import org.apache.batik.ext.awt.image.renderable.CompositeRable8Bit;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.PadRable8Bit;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;

public class BackgroundRable8Bit extends AbstractRable {
   private GraphicsNode node;

   public GraphicsNode getGraphicsNode() {
      return this.node;
   }

   public void setGraphicsNode(GraphicsNode var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.node = var1;
      }
   }

   public BackgroundRable8Bit(GraphicsNode var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.node = var1;
      }
   }

   static Rectangle2D addBounds(CompositeGraphicsNode var0, GraphicsNode var1, Rectangle2D var2) {
      List var3 = var0.getChildren();
      Iterator var4 = var3.iterator();
      Rectangle2D var5 = null;

      while(var4.hasNext()) {
         GraphicsNode var6 = (GraphicsNode)var4.next();
         if (var6 == var1) {
            break;
         }

         Rectangle2D var7 = var6.getBounds();
         AffineTransform var8 = var6.getTransform();
         if (var8 != null) {
            var7 = var8.createTransformedShape(var7).getBounds2D();
         }

         if (var5 == null) {
            var5 = (Rectangle2D)var7.clone();
         } else {
            var5.add(var7);
         }
      }

      if (var5 == null) {
         return var2 == null ? CompositeGraphicsNode.VIEWPORT : var2;
      } else if (var2 == null) {
         return var5;
      } else {
         var2.add(var5);
         return var2;
      }
   }

   static Rectangle2D getViewportBounds(GraphicsNode var0, GraphicsNode var1) {
      Rectangle2D var2 = null;
      CompositeGraphicsNode var3;
      if (var0 instanceof CompositeGraphicsNode) {
         var3 = (CompositeGraphicsNode)var0;
         var2 = var3.getBackgroundEnable();
      }

      if (var2 == null) {
         var2 = getViewportBounds(var0.getParent(), var0);
      }

      if (var2 == null) {
         return null;
      } else if (var2 == CompositeGraphicsNode.VIEWPORT) {
         if (var1 == null) {
            return (Rectangle2D)var0.getPrimitiveBounds().clone();
         } else {
            var3 = (CompositeGraphicsNode)var0;
            return addBounds(var3, var1, (Rectangle2D)null);
         }
      } else {
         AffineTransform var6 = var0.getTransform();
         if (var6 != null) {
            try {
               var6 = var6.createInverse();
               var2 = var6.createTransformedShape(var2).getBounds2D();
            } catch (NoninvertibleTransformException var5) {
               var2 = null;
            }
         }

         if (var1 != null) {
            CompositeGraphicsNode var4 = (CompositeGraphicsNode)var0;
            var2 = addBounds(var4, var1, var2);
         } else {
            Rectangle2D var7 = var0.getPrimitiveBounds();
            if (var7 != null) {
               var2.add(var7);
            }
         }

         return var2;
      }
   }

   static Rectangle2D getBoundsRecursive(GraphicsNode var0, GraphicsNode var1) {
      Rectangle2D var2 = null;
      if (var0 == null) {
         return null;
      } else {
         if (var0 instanceof CompositeGraphicsNode) {
            CompositeGraphicsNode var3 = (CompositeGraphicsNode)var0;
            var2 = var3.getBackgroundEnable();
         }

         if (var2 != null) {
            return var2;
         } else {
            var2 = getBoundsRecursive(var0.getParent(), var0);
            if (var2 == null) {
               return new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
            } else if (var2 == CompositeGraphicsNode.VIEWPORT) {
               return var2;
            } else {
               AffineTransform var6 = var0.getTransform();
               if (var6 != null) {
                  try {
                     var6 = var6.createInverse();
                     var2 = var6.createTransformedShape(var2).getBounds2D();
                  } catch (NoninvertibleTransformException var5) {
                     var2 = null;
                  }
               }

               return var2;
            }
         }
      }
   }

   public Rectangle2D getBounds2D() {
      Rectangle2D var1 = getBoundsRecursive(this.node, (GraphicsNode)null);
      if (var1 == CompositeGraphicsNode.VIEWPORT) {
         var1 = getViewportBounds(this.node, (GraphicsNode)null);
      }

      return var1;
   }

   public Filter getBackground(GraphicsNode var1, GraphicsNode var2, Rectangle2D var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("BackgroundImage requested yet no parent has 'enable-background:new'");
      } else {
         Rectangle2D var4 = null;
         if (var1 instanceof CompositeGraphicsNode) {
            CompositeGraphicsNode var5 = (CompositeGraphicsNode)var1;
            var4 = var5.getBackgroundEnable();
         }

         ArrayList var13 = new ArrayList();
         Rectangle2D var6;
         AffineTransform var7;
         if (var4 == null) {
            var6 = var3;
            var7 = var1.getTransform();
            if (var7 != null) {
               var6 = var7.createTransformedShape(var3).getBounds2D();
            }

            Filter var8 = this.getBackground(var1.getParent(), var1, var6);
            if (var8 != null && var8.getBounds2D().intersects(var3)) {
               var13.add(var8);
            }
         }

         if (var2 != null) {
            CompositeGraphicsNode var14 = (CompositeGraphicsNode)var1;
            List var15 = var14.getChildren();
            Iterator var17 = var15.iterator();

            while(var17.hasNext()) {
               GraphicsNode var9 = (GraphicsNode)var17.next();
               if (var9 == var2) {
                  break;
               }

               Rectangle2D var10 = var9.getBounds();
               AffineTransform var11 = var9.getTransform();
               if (var11 != null) {
                  var10 = var11.createTransformedShape(var10).getBounds2D();
               }

               if (var3.intersects(var10)) {
                  var13.add(var9.getEnableBackgroundGraphicsNodeRable(true));
               }
            }
         }

         if (var13.size() == 0) {
            return null;
         } else {
            var6 = null;
            Object var16;
            if (var13.size() == 1) {
               var16 = (Filter)var13.get(0);
            } else {
               var16 = new CompositeRable8Bit(var13, CompositeRule.OVER, false);
            }

            if (var2 != null) {
               var7 = var2.getTransform();
               if (var7 != null) {
                  try {
                     var7 = var7.createInverse();
                     var16 = new AffineRable8Bit((Filter)var16, var7);
                  } catch (NoninvertibleTransformException var12) {
                     var16 = null;
                  }
               }
            }

            return (Filter)var16;
         }
      }
   }

   public boolean isDynamic() {
      return false;
   }

   public RenderedImage createRendering(RenderContext var1) {
      Rectangle2D var2 = this.getBounds2D();
      Shape var3 = var1.getAreaOfInterest();
      if (var3 != null) {
         Rectangle2D var4 = var3.getBounds2D();
         if (!var2.intersects(var4)) {
            return null;
         }

         Rectangle2D.intersect(var2, var4, var2);
      }

      Filter var6 = this.getBackground(this.node, (GraphicsNode)null, var2);
      if (var6 == null) {
         return null;
      } else {
         PadRable8Bit var7 = new PadRable8Bit(var6, var2, PadMode.ZERO_PAD);
         RenderedImage var5 = var7.createRendering(new RenderContext(var1.getTransform(), var2, var1.getRenderingHints()));
         return var5;
      }
   }
}

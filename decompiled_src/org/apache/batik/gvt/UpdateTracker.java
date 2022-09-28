package org.apache.batik.gvt;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.gvt.event.GraphicsNodeChangeAdapter;
import org.apache.batik.gvt.event.GraphicsNodeChangeEvent;

public class UpdateTracker extends GraphicsNodeChangeAdapter {
   Map dirtyNodes = null;
   Map fromBounds = new HashMap();
   protected static Rectangle2D NULL_RECT = new Rectangle();

   public boolean hasChanged() {
      return this.dirtyNodes != null;
   }

   public List getDirtyAreas() {
      if (this.dirtyNodes == null) {
         return null;
      } else {
         LinkedList var1 = new LinkedList();
         Set var2 = this.dirtyNodes.keySet();
         Iterator var3 = var2.iterator();

         while(true) {
            WeakReference var4;
            Object var5;
            do {
               if (!var3.hasNext()) {
                  this.fromBounds.clear();
                  this.dirtyNodes.clear();
                  return var1;
               }

               var4 = (WeakReference)var3.next();
               var5 = (GraphicsNode)var4.get();
            } while(var5 == null);

            AffineTransform var6 = (AffineTransform)this.dirtyNodes.get(var4);
            if (var6 != null) {
               var6 = new AffineTransform(var6);
            }

            Rectangle2D var7 = (Rectangle2D)this.fromBounds.remove(var4);
            Rectangle2D var8 = null;
            AffineTransform var9 = null;
            if (!(var7 instanceof ChngSrcRect)) {
               var8 = ((GraphicsNode)var5).getBounds();
               var9 = ((GraphicsNode)var5).getTransform();
               if (var9 != null) {
                  var9 = new AffineTransform(var9);
               }
            }

            while(true) {
               var5 = ((GraphicsNode)var5).getParent();
               if (var5 == null) {
                  if (var5 == null) {
                     Object var13 = var7;
                     if (var7 != null && var7 != NULL_RECT) {
                        if (var6 != null) {
                           var13 = var6.createTransformedShape(var7);
                        }

                        var1.add(var13);
                     }

                     if (var8 != null) {
                        Object var14 = var8;
                        if (var9 != null) {
                           var14 = var9.createTransformedShape(var8);
                        }

                        if (var14 != null) {
                           var1.add(var14);
                        }
                     }
                  }
                  break;
               }

               Filter var10 = ((GraphicsNode)var5).getFilter();
               if (var10 != null) {
                  var8 = var10.getBounds2D();
                  var9 = null;
               }

               AffineTransform var11 = ((GraphicsNode)var5).getTransform();
               var4 = ((GraphicsNode)var5).getWeakReference();
               AffineTransform var12 = (AffineTransform)this.dirtyNodes.get(var4);
               if (var12 == null) {
                  var12 = var11;
               }

               if (var12 != null) {
                  if (var6 != null) {
                     var6.preConcatenate(var12);
                  } else {
                     var6 = new AffineTransform(var12);
                  }
               }

               if (var11 != null) {
                  if (var9 != null) {
                     var9.preConcatenate(var11);
                  } else {
                     var9 = new AffineTransform(var11);
                  }
               }
            }
         }
      }
   }

   public Rectangle2D getNodeDirtyRegion(GraphicsNode var1, AffineTransform var2) {
      WeakReference var3 = var1.getWeakReference();
      AffineTransform var4 = (AffineTransform)this.dirtyNodes.get(var3);
      if (var4 == null) {
         var4 = var1.getTransform();
      }

      if (var4 != null) {
         var2 = new AffineTransform(var2);
         var2.concatenate(var4);
      }

      Filter var5 = var1.getFilter();
      Rectangle2D var6 = null;
      if (var1 instanceof CompositeGraphicsNode) {
         CompositeGraphicsNode var7 = (CompositeGraphicsNode)var1;
         Iterator var8 = var7.iterator();

         while(true) {
            while(true) {
               Rectangle2D var10;
               do {
                  if (!var8.hasNext()) {
                     return var6;
                  }

                  GraphicsNode var9 = (GraphicsNode)var8.next();
                  var10 = this.getNodeDirtyRegion(var9, var2);
               } while(var10 == null);

               if (var5 != null) {
                  Shape var11 = var2.createTransformedShape(var5.getBounds2D());
                  var6 = var11.getBounds2D();
                  return var6;
               }

               if (var6 != null && var6 != NULL_RECT) {
                  var6.add(var10);
               } else {
                  var6 = var10;
               }
            }
         }
      } else {
         var6 = (Rectangle2D)this.fromBounds.remove(var3);
         if (var6 == null) {
            if (var5 != null) {
               var6 = var5.getBounds2D();
            } else {
               var6 = var1.getBounds();
            }
         } else if (var6 == NULL_RECT) {
            var6 = null;
         }

         if (var6 != null) {
            var6 = var2.createTransformedShape(var6).getBounds2D();
         }
      }

      return var6;
   }

   public Rectangle2D getNodeDirtyRegion(GraphicsNode var1) {
      return this.getNodeDirtyRegion(var1, new AffineTransform());
   }

   public void changeStarted(GraphicsNodeChangeEvent var1) {
      GraphicsNode var2 = var1.getGraphicsNode();
      WeakReference var3 = var2.getWeakReference();
      boolean var4 = false;
      if (this.dirtyNodes == null) {
         this.dirtyNodes = new HashMap();
         var4 = true;
      } else if (!this.dirtyNodes.containsKey(var3)) {
         var4 = true;
      }

      if (var4) {
         AffineTransform var5 = var2.getTransform();
         if (var5 != null) {
            var5 = (AffineTransform)var5.clone();
         } else {
            var5 = new AffineTransform();
         }

         this.dirtyNodes.put(var3, var5);
      }

      GraphicsNode var8 = var1.getChangeSrc();
      Object var6 = null;
      if (var8 != null) {
         Rectangle2D var7 = this.getNodeDirtyRegion(var8);
         if (var7 != null) {
            var6 = new ChngSrcRect(var7);
         }
      } else {
         var6 = var2.getBounds();
      }

      Object var9 = (Rectangle2D)this.fromBounds.remove(var3);
      if (var6 != null) {
         if (var9 != null && var9 != NULL_RECT) {
            ((Rectangle2D)var9).add((Rectangle2D)var6);
         } else {
            var9 = var6;
         }
      }

      if (var9 == null) {
         var9 = NULL_RECT;
      }

      this.fromBounds.put(var3, var9);
   }

   public void clear() {
      this.dirtyNodes = null;
   }

   class ChngSrcRect extends Rectangle2D.Float {
      ChngSrcRect(Rectangle2D var2) {
         super((float)var2.getX(), (float)var2.getY(), (float)var2.getWidth(), (float)var2.getHeight());
      }
   }
}

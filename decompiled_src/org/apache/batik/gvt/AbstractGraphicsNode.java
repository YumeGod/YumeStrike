package org.apache.batik.gvt;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.renderable.RenderableImage;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.event.EventListenerList;
import org.apache.batik.ext.awt.RenderingHintsKeyExt;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.renderable.ClipRable;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.gvt.event.GraphicsNodeChangeEvent;
import org.apache.batik.gvt.event.GraphicsNodeChangeListener;
import org.apache.batik.gvt.filter.GraphicsNodeRable;
import org.apache.batik.gvt.filter.GraphicsNodeRable8Bit;
import org.apache.batik.util.HaltingThread;

public abstract class AbstractGraphicsNode implements GraphicsNode {
   protected EventListenerList listeners;
   protected AffineTransform transform;
   protected AffineTransform inverseTransform;
   protected Composite composite;
   protected boolean isVisible = true;
   protected ClipRable clip;
   protected RenderingHints hints;
   protected CompositeGraphicsNode parent;
   protected RootGraphicsNode root;
   protected org.apache.batik.gvt.filter.Mask mask;
   protected Filter filter;
   protected int pointerEventType = 0;
   protected WeakReference graphicsNodeRable;
   protected WeakReference enableBackgroundGraphicsNodeRable;
   protected WeakReference weakRef;
   private Rectangle2D bounds;
   protected GraphicsNodeChangeEvent changeStartedEvent = null;
   protected GraphicsNodeChangeEvent changeCompletedEvent = null;
   static double EPSILON = 1.0E-6;

   protected AbstractGraphicsNode() {
   }

   public WeakReference getWeakReference() {
      if (this.weakRef == null) {
         this.weakRef = new WeakReference(this);
      }

      return this.weakRef;
   }

   public int getPointerEventType() {
      return this.pointerEventType;
   }

   public void setPointerEventType(int var1) {
      this.pointerEventType = var1;
   }

   public void setTransform(AffineTransform var1) {
      this.fireGraphicsNodeChangeStarted();
      this.transform = var1;
      if (this.transform.getDeterminant() != 0.0) {
         try {
            this.inverseTransform = this.transform.createInverse();
         } catch (NoninvertibleTransformException var3) {
            throw new Error(var3.getMessage());
         }
      } else {
         this.inverseTransform = this.transform;
      }

      if (this.parent != null) {
         this.parent.invalidateGeometryCache();
      }

      this.fireGraphicsNodeChangeCompleted();
   }

   public AffineTransform getTransform() {
      return this.transform;
   }

   public AffineTransform getInverseTransform() {
      return this.inverseTransform;
   }

   public AffineTransform getGlobalTransform() {
      AffineTransform var1 = new AffineTransform();

      for(Object var2 = this; var2 != null; var2 = ((GraphicsNode)var2).getParent()) {
         if (((GraphicsNode)var2).getTransform() != null) {
            var1.preConcatenate(((GraphicsNode)var2).getTransform());
         }
      }

      return var1;
   }

   public void setComposite(Composite var1) {
      this.fireGraphicsNodeChangeStarted();
      this.invalidateGeometryCache();
      this.composite = var1;
      this.fireGraphicsNodeChangeCompleted();
   }

   public Composite getComposite() {
      return this.composite;
   }

   public void setVisible(boolean var1) {
      this.fireGraphicsNodeChangeStarted();
      this.isVisible = var1;
      this.invalidateGeometryCache();
      this.fireGraphicsNodeChangeCompleted();
   }

   public boolean isVisible() {
      return this.isVisible;
   }

   public void setClip(ClipRable var1) {
      if (var1 != null || this.clip != null) {
         this.fireGraphicsNodeChangeStarted();
         this.invalidateGeometryCache();
         this.clip = var1;
         this.fireGraphicsNodeChangeCompleted();
      }
   }

   public ClipRable getClip() {
      return this.clip;
   }

   public void setRenderingHint(RenderingHints.Key var1, Object var2) {
      this.fireGraphicsNodeChangeStarted();
      if (this.hints == null) {
         this.hints = new RenderingHints(var1, var2);
      } else {
         this.hints.put(var1, var2);
      }

      this.fireGraphicsNodeChangeCompleted();
   }

   public void setRenderingHints(Map var1) {
      this.fireGraphicsNodeChangeStarted();
      if (this.hints == null) {
         this.hints = new RenderingHints(var1);
      } else {
         this.hints.putAll(var1);
      }

      this.fireGraphicsNodeChangeCompleted();
   }

   public void setRenderingHints(RenderingHints var1) {
      this.fireGraphicsNodeChangeStarted();
      this.hints = var1;
      this.fireGraphicsNodeChangeCompleted();
   }

   public RenderingHints getRenderingHints() {
      return this.hints;
   }

   public void setMask(org.apache.batik.gvt.filter.Mask var1) {
      if (var1 != null || this.mask != null) {
         this.fireGraphicsNodeChangeStarted();
         this.invalidateGeometryCache();
         this.mask = var1;
         this.fireGraphicsNodeChangeCompleted();
      }
   }

   public org.apache.batik.gvt.filter.Mask getMask() {
      return this.mask;
   }

   public void setFilter(Filter var1) {
      if (var1 != null || this.filter != null) {
         this.fireGraphicsNodeChangeStarted();
         this.invalidateGeometryCache();
         this.filter = var1;
         this.fireGraphicsNodeChangeCompleted();
      }
   }

   public Filter getFilter() {
      return this.filter;
   }

   public Filter getGraphicsNodeRable(boolean var1) {
      Object var2 = null;
      if (this.graphicsNodeRable != null) {
         var2 = (GraphicsNodeRable)this.graphicsNodeRable.get();
         if (var2 != null) {
            return (Filter)var2;
         }
      }

      if (var1) {
         var2 = new GraphicsNodeRable8Bit(this);
         this.graphicsNodeRable = new WeakReference(var2);
      }

      return (Filter)var2;
   }

   public Filter getEnableBackgroundGraphicsNodeRable(boolean var1) {
      Object var2 = null;
      if (this.enableBackgroundGraphicsNodeRable != null) {
         var2 = (GraphicsNodeRable)this.enableBackgroundGraphicsNodeRable.get();
         if (var2 != null) {
            return (Filter)var2;
         }
      }

      if (var1) {
         var2 = new GraphicsNodeRable8Bit(this);
         ((GraphicsNodeRable)var2).setUsePrimitivePaint(false);
         this.enableBackgroundGraphicsNodeRable = new WeakReference(var2);
      }

      return (Filter)var2;
   }

   public void paint(Graphics2D var1) {
      if (this.composite != null && this.composite instanceof AlphaComposite) {
         AlphaComposite var2 = (AlphaComposite)this.composite;
         if ((double)var2.getAlpha() < 0.001) {
            return;
         }
      }

      Rectangle2D var14 = this.getBounds();
      if (var14 != null) {
         Composite var3 = null;
         AffineTransform var4 = null;
         RenderingHints var5 = null;
         Graphics2D var6 = null;
         if (this.clip != null) {
            var6 = var1;
            var1 = (Graphics2D)var1.create();
            if (this.hints != null) {
               var1.addRenderingHints(this.hints);
            }

            if (this.transform != null) {
               var1.transform(this.transform);
            }

            if (this.composite != null) {
               var1.setComposite(this.composite);
            }

            var1.clip(this.clip.getClipPath());
         } else {
            if (this.hints != null) {
               var5 = var1.getRenderingHints();
               var1.addRenderingHints(this.hints);
            }

            if (this.transform != null) {
               var4 = var1.getTransform();
               var1.transform(this.transform);
            }

            if (this.composite != null) {
               var3 = var1.getComposite();
               var1.setComposite(this.composite);
            }
         }

         Shape var7 = var1.getClip();
         var1.setRenderingHint(RenderingHintsKeyExt.KEY_AREA_OF_INTEREST, var7);
         boolean var8 = true;
         if (var7 != null) {
            Rectangle2D var10 = var7.getBounds2D();
            if (!var14.intersects(var10.getX(), var10.getY(), var10.getWidth(), var10.getHeight())) {
               var8 = false;
            }
         }

         if (var8) {
            boolean var15 = false;
            if (this.clip != null && this.clip.getUseAntialiasedClip()) {
               var15 = this.isAntialiasedClip(var1.getTransform(), var1.getRenderingHints(), this.clip.getClipPath());
            }

            boolean var11 = this.isOffscreenBufferNeeded();
            var11 |= var15;
            if (!var11) {
               this.primitivePaint(var1);
            } else {
               Object var12 = null;
               if (this.filter == null) {
                  var12 = this.getGraphicsNodeRable(true);
               } else {
                  var12 = this.filter;
               }

               if (this.mask != null) {
                  if (this.mask.getSource() != var12) {
                     this.mask.setSource((Filter)var12);
                  }

                  var12 = this.mask;
               }

               if (this.clip != null && var15) {
                  if (this.clip.getSource() != var12) {
                     this.clip.setSource((Filter)var12);
                  }

                  var12 = this.clip;
               }

               var6 = var1;
               var1 = (Graphics2D)var1.create();
               if (var15) {
                  var1.setClip((Shape)null);
               }

               Rectangle2D var13 = ((Filter)var12).getBounds2D();
               var1.clip(var13);
               GraphicsUtil.drawImage(var1, (RenderableImage)var12);
               var1.dispose();
               var1 = var6;
               var6 = null;
            }
         }

         if (var6 != null) {
            var1.dispose();
         } else {
            if (var5 != null) {
               var1.setRenderingHints(var5);
            }

            if (var4 != null) {
               var1.setTransform(var4);
            }

            if (var3 != null) {
               var1.setComposite(var3);
            }
         }

      }
   }

   private void traceFilter(Filter var1, String var2) {
      System.out.println(var2 + var1.getClass().getName());
      System.out.println(var2 + var1.getBounds2D());
      Vector var3 = var1.getSources();
      int var4 = var3 != null ? var3.size() : 0;
      var2 = var2 + "\t";

      for(int var5 = 0; var5 < var4; ++var5) {
         Filter var6 = (Filter)var3.get(var5);
         this.traceFilter(var6, var2);
      }

      System.out.flush();
   }

   protected boolean isOffscreenBufferNeeded() {
      return this.filter != null || this.mask != null || this.composite != null && !AlphaComposite.SrcOver.equals(this.composite);
   }

   protected boolean isAntialiasedClip(AffineTransform var1, RenderingHints var2, Shape var3) {
      if (var3 == null) {
         return false;
      } else {
         Object var4 = var2.get(RenderingHintsKeyExt.KEY_TRANSCODING);
         if (var4 != "Printing" && var4 != "Vector") {
            return !(var3 instanceof Rectangle2D) || var1.getShearX() != 0.0 || var1.getShearY() != 0.0;
         } else {
            return false;
         }
      }
   }

   public void fireGraphicsNodeChangeStarted(GraphicsNode var1) {
      if (this.changeStartedEvent == null) {
         this.changeStartedEvent = new GraphicsNodeChangeEvent(this, 9800);
      }

      this.changeStartedEvent.setChangeSrc(var1);
      this.fireGraphicsNodeChangeStarted(this.changeStartedEvent);
      this.changeStartedEvent.setChangeSrc((GraphicsNode)null);
   }

   public void fireGraphicsNodeChangeStarted() {
      if (this.changeStartedEvent == null) {
         this.changeStartedEvent = new GraphicsNodeChangeEvent(this, 9800);
      } else {
         this.changeStartedEvent.setChangeSrc((GraphicsNode)null);
      }

      this.fireGraphicsNodeChangeStarted(this.changeStartedEvent);
   }

   public void fireGraphicsNodeChangeStarted(GraphicsNodeChangeEvent var1) {
      RootGraphicsNode var2 = this.getRoot();
      if (var2 != null) {
         List var3 = var2.getTreeGraphicsNodeChangeListeners();
         if (var3 != null) {
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               GraphicsNodeChangeListener var5 = (GraphicsNodeChangeListener)var4.next();
               var5.changeStarted(var1);
            }

         }
      }
   }

   public void fireGraphicsNodeChangeCompleted() {
      if (this.changeCompletedEvent == null) {
         this.changeCompletedEvent = new GraphicsNodeChangeEvent(this, 9801);
      }

      RootGraphicsNode var1 = this.getRoot();
      if (var1 != null) {
         List var2 = var1.getTreeGraphicsNodeChangeListeners();
         if (var2 != null) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               GraphicsNodeChangeListener var4 = (GraphicsNodeChangeListener)var3.next();
               var4.changeCompleted(this.changeCompletedEvent);
            }

         }
      }
   }

   public CompositeGraphicsNode getParent() {
      return this.parent;
   }

   public RootGraphicsNode getRoot() {
      return this.root;
   }

   protected void setRoot(RootGraphicsNode var1) {
      this.root = var1;
   }

   protected void setParent(CompositeGraphicsNode var1) {
      this.parent = var1;
   }

   protected void invalidateGeometryCache() {
      if (this.parent != null) {
         this.parent.invalidateGeometryCache();
      }

      this.bounds = null;
   }

   public Rectangle2D getBounds() {
      if (this.bounds == null) {
         if (this.filter == null) {
            this.bounds = this.getPrimitiveBounds();
         } else {
            this.bounds = this.filter.getBounds2D();
         }

         if (this.bounds != null) {
            Rectangle2D var1;
            if (this.clip != null) {
               var1 = this.clip.getClipPath().getBounds2D();
               if (var1.intersects(this.bounds)) {
                  Rectangle2D.intersect(this.bounds, var1, this.bounds);
               }
            }

            if (this.mask != null) {
               var1 = this.mask.getBounds2D();
               if (var1.intersects(this.bounds)) {
                  Rectangle2D.intersect(this.bounds, var1, this.bounds);
               }
            }
         }

         this.bounds = this.normalizeRectangle(this.bounds);
         if (HaltingThread.hasBeenHalted()) {
            this.invalidateGeometryCache();
         }
      }

      return this.bounds;
   }

   public Rectangle2D getTransformedBounds(AffineTransform var1) {
      AffineTransform var2 = var1;
      if (this.transform != null) {
         var2 = new AffineTransform(var1);
         var2.concatenate(this.transform);
      }

      Rectangle2D var3 = null;
      if (this.filter == null) {
         var3 = this.getTransformedPrimitiveBounds(var1);
      } else {
         var3 = var2.createTransformedShape(this.filter.getBounds2D()).getBounds2D();
      }

      if (var3 != null) {
         if (this.clip != null) {
            Rectangle2D.intersect(var3, var2.createTransformedShape(this.clip.getClipPath()).getBounds2D(), var3);
         }

         if (this.mask != null) {
            Rectangle2D.intersect(var3, var2.createTransformedShape(this.mask.getBounds2D()).getBounds2D(), var3);
         }
      }

      return var3;
   }

   public Rectangle2D getTransformedPrimitiveBounds(AffineTransform var1) {
      Rectangle2D var2 = this.getPrimitiveBounds();
      if (var2 == null) {
         return null;
      } else {
         AffineTransform var3 = var1;
         if (this.transform != null) {
            var3 = new AffineTransform(var1);
            var3.concatenate(this.transform);
         }

         return var3.createTransformedShape(var2).getBounds2D();
      }
   }

   public Rectangle2D getTransformedGeometryBounds(AffineTransform var1) {
      Rectangle2D var2 = this.getGeometryBounds();
      if (var2 == null) {
         return null;
      } else {
         AffineTransform var3 = var1;
         if (this.transform != null) {
            var3 = new AffineTransform(var1);
            var3.concatenate(this.transform);
         }

         return var3.createTransformedShape(var2).getBounds2D();
      }
   }

   public Rectangle2D getTransformedSensitiveBounds(AffineTransform var1) {
      Rectangle2D var2 = this.getSensitiveBounds();
      if (var2 == null) {
         return null;
      } else {
         AffineTransform var3 = var1;
         if (this.transform != null) {
            var3 = new AffineTransform(var1);
            var3.concatenate(this.transform);
         }

         return var3.createTransformedShape(var2).getBounds2D();
      }
   }

   public boolean contains(Point2D var1) {
      Rectangle2D var2 = this.getSensitiveBounds();
      if (var2 != null && var2.contains(var1)) {
         switch (this.pointerEventType) {
            case 0:
            case 1:
            case 2:
            case 3:
               return this.isVisible;
            case 4:
            case 5:
            case 6:
            case 7:
               return true;
            case 8:
            default:
               return false;
         }
      } else {
         return false;
      }
   }

   public boolean intersects(Rectangle2D var1) {
      Rectangle2D var2 = this.getBounds();
      return var2 == null ? false : var2.intersects(var1);
   }

   public GraphicsNode nodeHitAt(Point2D var1) {
      return this.contains(var1) ? this : null;
   }

   protected Rectangle2D normalizeRectangle(Rectangle2D var1) {
      if (var1 == null) {
         return null;
      } else {
         double var2;
         if (var1.getWidth() < EPSILON) {
            if (var1.getHeight() < EPSILON) {
               AffineTransform var5 = this.getGlobalTransform();
               double var3 = Math.sqrt(var5.getDeterminant());
               return new Rectangle2D.Double(var1.getX(), var1.getY(), EPSILON / var3, EPSILON / var3);
            } else {
               var2 = var1.getHeight() * EPSILON;
               if (var2 < var1.getWidth()) {
                  var2 = var1.getWidth();
               }

               return new Rectangle2D.Double(var1.getX(), var1.getY(), var2, var1.getHeight());
            }
         } else if (var1.getHeight() < EPSILON) {
            var2 = var1.getWidth() * EPSILON;
            if (var2 < var1.getHeight()) {
               var2 = var1.getHeight();
            }

            return new Rectangle2D.Double(var1.getX(), var1.getY(), var1.getWidth(), var2);
         } else {
            return var1;
         }
      }
   }
}

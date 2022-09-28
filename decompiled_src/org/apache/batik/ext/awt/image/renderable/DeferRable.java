package org.apache.batik.ext.awt.image.renderable;

import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.util.Map;
import java.util.Vector;

public class DeferRable implements Filter {
   Filter src;
   Rectangle2D bounds;
   Map props;

   public synchronized Filter getSource() {
      while(this.src == null) {
         try {
            this.wait();
         } catch (InterruptedException var2) {
         }
      }

      return this.src;
   }

   public synchronized void setSource(Filter var1) {
      if (this.src == null) {
         this.src = var1;
         this.bounds = var1.getBounds2D();
         this.notifyAll();
      }
   }

   public synchronized void setBounds(Rectangle2D var1) {
      if (this.bounds == null) {
         this.bounds = var1;
         this.notifyAll();
      }
   }

   public synchronized void setProperties(Map var1) {
      this.props = var1;
      this.notifyAll();
   }

   public long getTimeStamp() {
      return this.getSource().getTimeStamp();
   }

   public Vector getSources() {
      return this.getSource().getSources();
   }

   public boolean isDynamic() {
      return this.getSource().isDynamic();
   }

   public Rectangle2D getBounds2D() {
      synchronized(this) {
         while(this.src == null && this.bounds == null) {
            try {
               this.wait();
            } catch (InterruptedException var4) {
            }
         }
      }

      return this.src != null ? this.src.getBounds2D() : this.bounds;
   }

   public float getMinX() {
      return (float)this.getBounds2D().getX();
   }

   public float getMinY() {
      return (float)this.getBounds2D().getY();
   }

   public float getWidth() {
      return (float)this.getBounds2D().getWidth();
   }

   public float getHeight() {
      return (float)this.getBounds2D().getHeight();
   }

   public Object getProperty(String var1) {
      synchronized(this) {
         while(this.src == null && this.props == null) {
            try {
               this.wait();
            } catch (InterruptedException var5) {
            }
         }
      }

      return this.src != null ? this.src.getProperty(var1) : this.props.get(var1);
   }

   public String[] getPropertyNames() {
      synchronized(this) {
         while(this.src == null && this.props == null) {
            try {
               this.wait();
            } catch (InterruptedException var4) {
            }
         }
      }

      if (this.src != null) {
         return this.src.getPropertyNames();
      } else {
         String[] var1 = new String[this.props.size()];
         this.props.keySet().toArray(var1);
         return var1;
      }
   }

   public RenderedImage createDefaultRendering() {
      return this.getSource().createDefaultRendering();
   }

   public RenderedImage createScaledRendering(int var1, int var2, RenderingHints var3) {
      return this.getSource().createScaledRendering(var1, var2, var3);
   }

   public RenderedImage createRendering(RenderContext var1) {
      return this.getSource().createRendering(var1);
   }

   public Shape getDependencyRegion(int var1, Rectangle2D var2) {
      return this.getSource().getDependencyRegion(var1, var2);
   }

   public Shape getDirtyRegion(int var1, Rectangle2D var2) {
      return this.getSource().getDirtyRegion(var1, var2);
   }
}

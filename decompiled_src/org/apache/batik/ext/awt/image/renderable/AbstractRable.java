package org.apache.batik.ext.awt.image.renderable;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.awt.image.renderable.RenderableImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.PadRed;
import org.apache.batik.ext.awt.image.rendered.RenderedImageCachableRed;

public abstract class AbstractRable implements Filter {
   protected Vector srcs;
   protected Map props;
   protected long stamp;

   protected AbstractRable() {
      this.props = new HashMap();
      this.stamp = 0L;
      this.srcs = new Vector();
   }

   protected AbstractRable(Filter var1) {
      this.props = new HashMap();
      this.stamp = 0L;
      this.init((Filter)var1, (Map)null);
   }

   protected AbstractRable(Filter var1, Map var2) {
      this.props = new HashMap();
      this.stamp = 0L;
      this.init(var1, var2);
   }

   protected AbstractRable(List var1) {
      this((List)var1, (Map)null);
   }

   protected AbstractRable(List var1, Map var2) {
      this.props = new HashMap();
      this.stamp = 0L;
      this.init(var1, var2);
   }

   public final void touch() {
      ++this.stamp;
   }

   public long getTimeStamp() {
      return this.stamp;
   }

   protected void init(Filter var1) {
      this.touch();
      this.srcs = new Vector(1);
      if (var1 != null) {
         this.srcs.add(var1);
      }

   }

   protected void init(Filter var1, Map var2) {
      this.init(var1);
      if (var2 != null) {
         this.props.putAll(var2);
      }

   }

   protected void init(List var1) {
      this.touch();
      this.srcs = new Vector(var1);
   }

   protected void init(List var1, Map var2) {
      this.init(var1);
      if (var2 != null) {
         this.props.putAll(var2);
      }

   }

   public Rectangle2D getBounds2D() {
      Rectangle2D var1 = null;
      if (this.srcs.size() != 0) {
         Iterator var2 = this.srcs.iterator();
         Filter var3 = (Filter)var2.next();
         var1 = (Rectangle2D)var3.getBounds2D().clone();

         while(var2.hasNext()) {
            var3 = (Filter)var2.next();
            Rectangle2D var4 = var3.getBounds2D();
            Rectangle2D.union(var1, var4, var1);
         }
      }

      return var1;
   }

   public Vector getSources() {
      return this.srcs;
   }

   public RenderedImage createDefaultRendering() {
      return this.createScaledRendering(100, 100, (RenderingHints)null);
   }

   public RenderedImage createScaledRendering(int var1, int var2, RenderingHints var3) {
      float var4 = (float)var1 / this.getWidth();
      float var5 = (float)var2 / this.getHeight();
      float var6 = Math.min(var4, var5);
      AffineTransform var7 = AffineTransform.getScaleInstance((double)var6, (double)var6);
      RenderContext var8 = new RenderContext(var7, var3);
      float var9 = this.getWidth() * var6 - (float)var1;
      float var10 = this.getHeight() * var6 - (float)var2;
      RenderedImage var11 = this.createRendering(var8);
      CachableRed var12 = RenderedImageCachableRed.wrap(var11);
      return new PadRed(var12, new Rectangle((int)(var9 / 2.0F), (int)(var10 / 2.0F), var1, var2), PadMode.ZERO_PAD, (RenderingHints)null);
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
      Object var2 = this.props.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         Iterator var3 = this.srcs.iterator();

         do {
            if (!var3.hasNext()) {
               return null;
            }

            RenderableImage var4 = (RenderableImage)var3.next();
            var2 = var4.getProperty(var1);
         } while(var2 == null);

         return var2;
      }
   }

   public String[] getPropertyNames() {
      Set var1 = this.props.keySet();
      Iterator var2 = var1.iterator();
      String[] var3 = new String[var1.size()];

      for(int var4 = 0; var2.hasNext(); var3[var4++] = (String)var2.next()) {
      }

      var2 = this.srcs.iterator();

      while(var2.hasNext()) {
         RenderableImage var5 = (RenderableImage)var2.next();
         String[] var6 = var5.getPropertyNames();
         if (var6.length != 0) {
            String[] var7 = new String[var3.length + var6.length];
            System.arraycopy(var3, 0, var7, 0, var3.length);
            System.arraycopy(var7, var3.length, var6, 0, var6.length);
            var3 = var7;
         }
      }

      return var3;
   }

   public boolean isDynamic() {
      return false;
   }

   public Shape getDependencyRegion(int var1, Rectangle2D var2) {
      if (var1 >= 0 && var1 <= this.srcs.size()) {
         Rectangle2D var3 = (Rectangle2D)var2.clone();
         Rectangle2D var4 = this.getBounds2D();
         if (!var4.intersects(var3)) {
            return new Rectangle2D.Float();
         } else {
            Rectangle2D.intersect(var3, var4, var3);
            return var3;
         }
      } else {
         throw new IndexOutOfBoundsException("Nonexistant source requested.");
      }
   }

   public Shape getDirtyRegion(int var1, Rectangle2D var2) {
      if (var1 >= 0 && var1 <= this.srcs.size()) {
         Rectangle2D var3 = (Rectangle2D)var2.clone();
         Rectangle2D var4 = this.getBounds2D();
         if (!var4.intersects(var3)) {
            return new Rectangle2D.Float();
         } else {
            Rectangle2D.intersect(var3, var4, var3);
            return var3;
         }
      } else {
         throw new IndexOutOfBoundsException("Nonexistant source requested.");
      }
   }
}

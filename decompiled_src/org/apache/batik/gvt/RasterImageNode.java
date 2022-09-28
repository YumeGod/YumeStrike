package org.apache.batik.gvt;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.renderable.RenderableImage;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.renderable.Filter;

public class RasterImageNode extends AbstractGraphicsNode {
   protected Filter image;

   public void setImage(Filter var1) {
      this.fireGraphicsNodeChangeStarted();
      this.invalidateGeometryCache();
      this.image = var1;
      this.fireGraphicsNodeChangeCompleted();
   }

   public Filter getImage() {
      return this.image;
   }

   public Rectangle2D getImageBounds() {
      return this.image == null ? null : (Rectangle2D)this.image.getBounds2D().clone();
   }

   public Filter getGraphicsNodeRable() {
      return this.image;
   }

   public void primitivePaint(Graphics2D var1) {
      if (this.image != null) {
         GraphicsUtil.drawImage(var1, (RenderableImage)this.image);
      }
   }

   public Rectangle2D getPrimitiveBounds() {
      return this.image == null ? null : this.image.getBounds2D();
   }

   public Rectangle2D getGeometryBounds() {
      return this.image == null ? null : this.image.getBounds2D();
   }

   public Rectangle2D getSensitiveBounds() {
      return this.image == null ? null : this.image.getBounds2D();
   }

   public Shape getOutline() {
      return this.image == null ? null : this.image.getBounds2D();
   }
}

package org.apache.batik.gvt;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class ProxyGraphicsNode extends AbstractGraphicsNode {
   protected GraphicsNode source;

   public void setSource(GraphicsNode var1) {
      this.source = var1;
   }

   public GraphicsNode getSource() {
      return this.source;
   }

   public void primitivePaint(Graphics2D var1) {
      if (this.source != null) {
         this.source.paint(var1);
      }

   }

   public Rectangle2D getPrimitiveBounds() {
      return this.source == null ? null : this.source.getBounds();
   }

   public Rectangle2D getTransformedPrimitiveBounds(AffineTransform var1) {
      if (this.source == null) {
         return null;
      } else {
         AffineTransform var2 = var1;
         if (this.transform != null) {
            var2 = new AffineTransform(var1);
            var2.concatenate(this.transform);
         }

         return this.source.getTransformedPrimitiveBounds(var2);
      }
   }

   public Rectangle2D getGeometryBounds() {
      return this.source == null ? null : this.source.getGeometryBounds();
   }

   public Rectangle2D getTransformedGeometryBounds(AffineTransform var1) {
      if (this.source == null) {
         return null;
      } else {
         AffineTransform var2 = var1;
         if (this.transform != null) {
            var2 = new AffineTransform(var1);
            var2.concatenate(this.transform);
         }

         return this.source.getTransformedGeometryBounds(var2);
      }
   }

   public Rectangle2D getSensitiveBounds() {
      return this.source == null ? null : this.source.getSensitiveBounds();
   }

   public Shape getOutline() {
      return this.source == null ? null : this.source.getOutline();
   }
}

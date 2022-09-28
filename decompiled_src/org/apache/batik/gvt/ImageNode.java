package org.apache.batik.gvt;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class ImageNode extends CompositeGraphicsNode {
   protected boolean hitCheckChildren = false;

   public void setVisible(boolean var1) {
      this.fireGraphicsNodeChangeStarted();
      this.isVisible = var1;
      this.invalidateGeometryCache();
      this.fireGraphicsNodeChangeCompleted();
   }

   public Rectangle2D getPrimitiveBounds() {
      return !this.isVisible ? null : super.getPrimitiveBounds();
   }

   public void setHitCheckChildren(boolean var1) {
      this.hitCheckChildren = var1;
   }

   public boolean getHitCheckChildren() {
      return this.hitCheckChildren;
   }

   public void paint(Graphics2D var1) {
      if (this.isVisible) {
         super.paint(var1);
      }

   }

   public boolean contains(Point2D var1) {
      switch (this.pointerEventType) {
         case 0:
         case 1:
         case 2:
         case 3:
            return this.isVisible && super.contains(var1);
         case 4:
         case 5:
         case 6:
         case 7:
            return super.contains(var1);
         case 8:
            return false;
         default:
            return false;
      }
   }

   public GraphicsNode nodeHitAt(Point2D var1) {
      if (this.hitCheckChildren) {
         return super.nodeHitAt(var1);
      } else {
         return this.contains(var1) ? this : null;
      }
   }

   public void setImage(GraphicsNode var1) {
      this.fireGraphicsNodeChangeStarted();
      this.invalidateGeometryCache();
      if (this.count == 0) {
         this.ensureCapacity(1);
      }

      this.children[0] = var1;
      ((AbstractGraphicsNode)var1).setParent(this);
      ((AbstractGraphicsNode)var1).setRoot(this.getRoot());
      this.count = 1;
      this.fireGraphicsNodeChangeCompleted();
   }

   public GraphicsNode getImage() {
      return this.count > 0 ? this.children[0] : null;
   }
}

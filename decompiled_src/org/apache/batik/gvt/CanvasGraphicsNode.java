package org.apache.batik.gvt;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

public class CanvasGraphicsNode extends CompositeGraphicsNode {
   protected AffineTransform positionTransform;
   protected AffineTransform viewingTransform;
   protected Paint backgroundPaint;

   public void setBackgroundPaint(Paint var1) {
      this.backgroundPaint = var1;
   }

   public Paint getBackgroundPaint() {
      return this.backgroundPaint;
   }

   public void setPositionTransform(AffineTransform var1) {
      this.fireGraphicsNodeChangeStarted();
      this.invalidateGeometryCache();
      this.positionTransform = var1;
      if (this.positionTransform != null) {
         this.transform = new AffineTransform(this.positionTransform);
         if (this.viewingTransform != null) {
            this.transform.concatenate(this.viewingTransform);
         }
      } else if (this.viewingTransform != null) {
         this.transform = new AffineTransform(this.viewingTransform);
      } else {
         this.transform = new AffineTransform();
      }

      if (this.transform.getDeterminant() != 0.0) {
         try {
            this.inverseTransform = this.transform.createInverse();
         } catch (NoninvertibleTransformException var3) {
            throw new Error(var3.getMessage());
         }
      } else {
         this.inverseTransform = this.transform;
      }

      this.fireGraphicsNodeChangeCompleted();
   }

   public AffineTransform getPositionTransform() {
      return this.positionTransform;
   }

   public void setViewingTransform(AffineTransform var1) {
      this.fireGraphicsNodeChangeStarted();
      this.invalidateGeometryCache();
      this.viewingTransform = var1;
      if (this.positionTransform != null) {
         this.transform = new AffineTransform(this.positionTransform);
         if (this.viewingTransform != null) {
            this.transform.concatenate(this.viewingTransform);
         }
      } else if (this.viewingTransform != null) {
         this.transform = new AffineTransform(this.viewingTransform);
      } else {
         this.transform = new AffineTransform();
      }

      if (this.transform.getDeterminant() != 0.0) {
         try {
            this.inverseTransform = this.transform.createInverse();
         } catch (NoninvertibleTransformException var3) {
            throw new Error(var3.getMessage());
         }
      } else {
         this.inverseTransform = this.transform;
      }

      this.fireGraphicsNodeChangeCompleted();
   }

   public AffineTransform getViewingTransform() {
      return this.viewingTransform;
   }

   public void primitivePaint(Graphics2D var1) {
      if (this.backgroundPaint != null) {
         var1.setPaint(this.backgroundPaint);
         var1.fill(var1.getClip());
      }

      super.primitivePaint(var1);
   }
}

package org.apache.batik.bridge;

import java.awt.Shape;
import org.apache.batik.gvt.CompositeShapePainter;
import org.apache.batik.gvt.ShapeNode;
import org.apache.batik.gvt.ShapePainter;
import org.w3c.dom.Element;

public abstract class SVGDecoratedShapeElementBridge extends SVGShapeElementBridge {
   protected SVGDecoratedShapeElementBridge() {
   }

   ShapePainter createFillStrokePainter(BridgeContext var1, Element var2, ShapeNode var3) {
      return super.createShapePainter(var1, var2, var3);
   }

   ShapePainter createMarkerPainter(BridgeContext var1, Element var2, ShapeNode var3) {
      return PaintServer.convertMarkers(var2, var3, var1);
   }

   protected ShapePainter createShapePainter(BridgeContext var1, Element var2, ShapeNode var3) {
      ShapePainter var4 = this.createFillStrokePainter(var1, var2, var3);
      ShapePainter var5 = this.createMarkerPainter(var1, var2, var3);
      Shape var6 = var3.getShape();
      Object var7;
      if (var5 != null) {
         if (var4 != null) {
            CompositeShapePainter var8 = new CompositeShapePainter(var6);
            var8.addShapePainter(var4);
            var8.addShapePainter(var5);
            var7 = var8;
         } else {
            var7 = var5;
         }
      } else {
         var7 = var4;
      }

      return (ShapePainter)var7;
   }

   protected void handleCSSPropertyChanged(int var1) {
      switch (var1) {
         case 34:
         case 35:
         case 36:
            if (!this.hasNewShapePainter) {
               this.hasNewShapePainter = true;
               ShapeNode var2 = (ShapeNode)this.node;
               var2.setShapePainter(this.createShapePainter(this.ctx, this.e, var2));
            }
            break;
         default:
            super.handleCSSPropertyChanged(var1);
      }

   }
}

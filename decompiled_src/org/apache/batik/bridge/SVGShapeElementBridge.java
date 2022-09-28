package org.apache.batik.bridge;

import java.awt.RenderingHints;
import org.apache.batik.css.engine.CSSEngineEvent;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.ShapeNode;
import org.apache.batik.gvt.ShapePainter;
import org.w3c.dom.Element;

public abstract class SVGShapeElementBridge extends AbstractGraphicsNodeBridge {
   protected boolean hasNewShapePainter;

   protected SVGShapeElementBridge() {
   }

   public GraphicsNode createGraphicsNode(BridgeContext var1, Element var2) {
      ShapeNode var3 = (ShapeNode)super.createGraphicsNode(var1, var2);
      if (var3 == null) {
         return null;
      } else {
         this.associateSVGContext(var1, var2, var3);
         this.buildShape(var1, var2, var3);
         RenderingHints var4 = null;
         var4 = CSSUtilities.convertColorRendering(var2, var4);
         var4 = CSSUtilities.convertShapeRendering(var2, var4);
         if (var4 != null) {
            var3.setRenderingHints(var4);
         }

         return var3;
      }
   }

   protected GraphicsNode instantiateGraphicsNode() {
      return new ShapeNode();
   }

   public void buildGraphicsNode(BridgeContext var1, Element var2, GraphicsNode var3) {
      ShapeNode var4 = (ShapeNode)var3;
      var4.setShapePainter(this.createShapePainter(var1, var2, var4));
      super.buildGraphicsNode(var1, var2, var3);
   }

   protected ShapePainter createShapePainter(BridgeContext var1, Element var2, ShapeNode var3) {
      return PaintServer.convertFillAndStroke(var2, var3, var1);
   }

   protected abstract void buildShape(BridgeContext var1, Element var2, ShapeNode var3);

   public boolean isComposite() {
      return false;
   }

   protected void handleGeometryChanged() {
      super.handleGeometryChanged();
      ShapeNode var1 = (ShapeNode)this.node;
      var1.setShapePainter(this.createShapePainter(this.ctx, this.e, var1));
   }

   public void handleCSSEngineEvent(CSSEngineEvent var1) {
      this.hasNewShapePainter = false;
      super.handleCSSEngineEvent(var1);
   }

   protected void handleCSSPropertyChanged(int var1) {
      RenderingHints var2;
      switch (var1) {
         case 9:
            var2 = this.node.getRenderingHints();
            var2 = CSSUtilities.convertColorRendering(this.e, var2);
            if (var2 != null) {
               this.node.setRenderingHints(var2);
            }
            break;
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 43:
         case 44:
         default:
            super.handleCSSPropertyChanged(var1);
            break;
         case 15:
         case 16:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
            if (!this.hasNewShapePainter) {
               this.hasNewShapePainter = true;
               ShapeNode var3 = (ShapeNode)this.node;
               var3.setShapePainter(this.createShapePainter(this.ctx, this.e, var3));
            }
            break;
         case 42:
            var2 = this.node.getRenderingHints();
            var2 = CSSUtilities.convertShapeRendering(this.e, var2);
            if (var2 != null) {
               this.node.setRenderingHints(var2);
            }
      }

   }
}

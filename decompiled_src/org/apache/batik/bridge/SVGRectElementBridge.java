package org.apache.batik.bridge;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import org.apache.batik.dom.svg.AbstractSVGAnimatedLength;
import org.apache.batik.dom.svg.AnimatedLiveAttributeValue;
import org.apache.batik.dom.svg.LiveAttributeException;
import org.apache.batik.dom.svg.SVGOMRectElement;
import org.apache.batik.gvt.ShapeNode;
import org.apache.batik.gvt.ShapePainter;
import org.w3c.dom.Element;

public class SVGRectElementBridge extends SVGShapeElementBridge {
   public String getLocalName() {
      return "rect";
   }

   public Bridge getInstance() {
      return new SVGRectElementBridge();
   }

   protected void buildShape(BridgeContext var1, Element var2, ShapeNode var3) {
      try {
         SVGOMRectElement var4 = (SVGOMRectElement)var2;
         AbstractSVGAnimatedLength var5 = (AbstractSVGAnimatedLength)var4.getX();
         float var6 = var5.getCheckedValue();
         AbstractSVGAnimatedLength var7 = (AbstractSVGAnimatedLength)var4.getY();
         float var8 = var7.getCheckedValue();
         AbstractSVGAnimatedLength var9 = (AbstractSVGAnimatedLength)var4.getWidth();
         float var10 = var9.getCheckedValue();
         AbstractSVGAnimatedLength var11 = (AbstractSVGAnimatedLength)var4.getHeight();
         float var12 = var11.getCheckedValue();
         AbstractSVGAnimatedLength var13 = (AbstractSVGAnimatedLength)var4.getRx();
         float var14 = var13.getCheckedValue();
         if (var14 > var10 / 2.0F) {
            var14 = var10 / 2.0F;
         }

         AbstractSVGAnimatedLength var15 = (AbstractSVGAnimatedLength)var4.getRy();
         float var16 = var15.getCheckedValue();
         if (var16 > var12 / 2.0F) {
            var16 = var12 / 2.0F;
         }

         Object var17;
         if (var14 != 0.0F && var16 != 0.0F) {
            var17 = new RoundRectangle2D.Float(var6, var8, var10, var12, var14 * 2.0F, var16 * 2.0F);
         } else {
            var17 = new Rectangle2D.Float(var6, var8, var10, var12);
         }

         var3.setShape((Shape)var17);
      } catch (LiveAttributeException var18) {
         throw new BridgeException(var1, var18);
      }
   }

   public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue var1) {
      if (var1.getNamespaceURI() == null) {
         String var2 = var1.getLocalName();
         if (var2.equals("x") || var2.equals("y") || var2.equals("width") || var2.equals("height") || var2.equals("rx") || var2.equals("ry")) {
            this.buildShape(this.ctx, this.e, (ShapeNode)this.node);
            this.handleGeometryChanged();
            return;
         }
      }

      super.handleAnimatedAttributeChanged(var1);
   }

   protected ShapePainter createShapePainter(BridgeContext var1, Element var2, ShapeNode var3) {
      Shape var4 = var3.getShape();
      Rectangle2D var5 = var4.getBounds2D();
      return var5.getWidth() != 0.0 && var5.getHeight() != 0.0 ? super.createShapePainter(var1, var2, var3) : null;
   }
}

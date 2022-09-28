package org.apache.batik.bridge;

import java.awt.geom.Line2D;
import org.apache.batik.dom.svg.AbstractSVGAnimatedLength;
import org.apache.batik.dom.svg.AnimatedLiveAttributeValue;
import org.apache.batik.dom.svg.LiveAttributeException;
import org.apache.batik.dom.svg.SVGOMLineElement;
import org.apache.batik.gvt.ShapeNode;
import org.apache.batik.gvt.ShapePainter;
import org.w3c.dom.Element;

public class SVGLineElementBridge extends SVGDecoratedShapeElementBridge {
   public String getLocalName() {
      return "line";
   }

   public Bridge getInstance() {
      return new SVGLineElementBridge();
   }

   protected ShapePainter createFillStrokePainter(BridgeContext var1, Element var2, ShapeNode var3) {
      return PaintServer.convertStrokePainter(var2, var3, var1);
   }

   protected void buildShape(BridgeContext var1, Element var2, ShapeNode var3) {
      try {
         SVGOMLineElement var4 = (SVGOMLineElement)var2;
         AbstractSVGAnimatedLength var5 = (AbstractSVGAnimatedLength)var4.getX1();
         float var6 = var5.getCheckedValue();
         AbstractSVGAnimatedLength var7 = (AbstractSVGAnimatedLength)var4.getY1();
         float var8 = var7.getCheckedValue();
         AbstractSVGAnimatedLength var9 = (AbstractSVGAnimatedLength)var4.getX2();
         float var10 = var9.getCheckedValue();
         AbstractSVGAnimatedLength var11 = (AbstractSVGAnimatedLength)var4.getY2();
         float var12 = var11.getCheckedValue();
         var3.setShape(new Line2D.Float(var6, var8, var10, var12));
      } catch (LiveAttributeException var13) {
         throw new BridgeException(var1, var13);
      }
   }

   public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue var1) {
      if (var1.getNamespaceURI() == null) {
         String var2 = var1.getLocalName();
         if (var2.equals("x1") || var2.equals("y1") || var2.equals("x2") || var2.equals("y2")) {
            this.buildShape(this.ctx, this.e, (ShapeNode)this.node);
            this.handleGeometryChanged();
            return;
         }
      }

      super.handleAnimatedAttributeChanged(var1);
   }
}

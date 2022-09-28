package org.apache.batik.bridge;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import org.apache.batik.dom.svg.AbstractSVGAnimatedLength;
import org.apache.batik.dom.svg.AnimatedLiveAttributeValue;
import org.apache.batik.dom.svg.LiveAttributeException;
import org.apache.batik.dom.svg.SVGOMEllipseElement;
import org.apache.batik.gvt.ShapeNode;
import org.apache.batik.gvt.ShapePainter;
import org.w3c.dom.Element;

public class SVGEllipseElementBridge extends SVGShapeElementBridge {
   public String getLocalName() {
      return "ellipse";
   }

   public Bridge getInstance() {
      return new SVGEllipseElementBridge();
   }

   protected void buildShape(BridgeContext var1, Element var2, ShapeNode var3) {
      try {
         SVGOMEllipseElement var4 = (SVGOMEllipseElement)var2;
         AbstractSVGAnimatedLength var5 = (AbstractSVGAnimatedLength)var4.getCx();
         float var6 = var5.getCheckedValue();
         AbstractSVGAnimatedLength var7 = (AbstractSVGAnimatedLength)var4.getCy();
         float var8 = var7.getCheckedValue();
         AbstractSVGAnimatedLength var9 = (AbstractSVGAnimatedLength)var4.getRx();
         float var10 = var9.getCheckedValue();
         AbstractSVGAnimatedLength var11 = (AbstractSVGAnimatedLength)var4.getRy();
         float var12 = var11.getCheckedValue();
         var3.setShape(new Ellipse2D.Float(var6 - var10, var8 - var12, var10 * 2.0F, var12 * 2.0F));
      } catch (LiveAttributeException var13) {
         throw new BridgeException(var1, var13);
      }
   }

   public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue var1) {
      if (var1.getNamespaceURI() == null) {
         String var2 = var1.getLocalName();
         if (var2.equals("cx") || var2.equals("cy") || var2.equals("rx") || var2.equals("ry")) {
            this.buildShape(this.ctx, this.e, (ShapeNode)this.node);
            this.handleGeometryChanged();
            return;
         }
      }

      super.handleAnimatedAttributeChanged(var1);
   }

   protected ShapePainter createShapePainter(BridgeContext var1, Element var2, ShapeNode var3) {
      Rectangle2D var4 = var3.getShape().getBounds2D();
      return var4.getWidth() != 0.0 && var4.getHeight() != 0.0 ? super.createShapePainter(var1, var2, var3) : null;
   }
}

package org.apache.batik.bridge;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import org.apache.batik.dom.svg.AnimatedLiveAttributeValue;
import org.apache.batik.dom.svg.LiveAttributeException;
import org.apache.batik.dom.svg.SVGOMAnimatedPoints;
import org.apache.batik.dom.svg.SVGOMPolylineElement;
import org.apache.batik.gvt.ShapeNode;
import org.apache.batik.parser.AWTPolylineProducer;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGPoint;
import org.w3c.dom.svg.SVGPointList;

public class SVGPolylineElementBridge extends SVGDecoratedShapeElementBridge {
   protected static final Shape DEFAULT_SHAPE = new GeneralPath();

   public String getLocalName() {
      return "polyline";
   }

   public Bridge getInstance() {
      return new SVGPolylineElementBridge();
   }

   protected void buildShape(BridgeContext var1, Element var2, ShapeNode var3) {
      SVGOMPolylineElement var4 = (SVGOMPolylineElement)var2;

      try {
         SVGOMAnimatedPoints var5 = var4.getSVGOMAnimatedPoints();
         var5.check();
         SVGPointList var6 = var5.getAnimatedPoints();
         int var7 = var6.getNumberOfItems();
         if (var7 == 0) {
            var3.setShape(DEFAULT_SHAPE);
         } else {
            AWTPolylineProducer var8 = new AWTPolylineProducer();
            var8.setWindingRule(CSSUtilities.convertFillRule(var2));
            var8.startPoints();

            for(int var9 = 0; var9 < var7; ++var9) {
               SVGPoint var10 = var6.getItem(var9);
               var8.point(var10.getX(), var10.getY());
            }

            var8.endPoints();
            var3.setShape(var8.getShape());
         }

      } catch (LiveAttributeException var11) {
         throw new BridgeException(var1, var11);
      }
   }

   public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue var1) {
      if (var1.getNamespaceURI() == null) {
         String var2 = var1.getLocalName();
         if (var2.equals("points")) {
            this.buildShape(this.ctx, this.e, (ShapeNode)this.node);
            this.handleGeometryChanged();
            return;
         }
      }

      super.handleAnimatedAttributeChanged(var1);
   }

   protected void handleCSSPropertyChanged(int var1) {
      switch (var1) {
         case 17:
            this.buildShape(this.ctx, this.e, (ShapeNode)this.node);
            this.handleGeometryChanged();
            break;
         default:
            super.handleCSSPropertyChanged(var1);
      }

   }
}

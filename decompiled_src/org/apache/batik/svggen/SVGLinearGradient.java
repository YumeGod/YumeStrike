package org.apache.batik.svggen;

import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.geom.Point2D;
import org.apache.batik.ext.awt.g2d.GraphicContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SVGLinearGradient extends AbstractSVGConverter {
   public SVGLinearGradient(SVGGeneratorContext var1) {
      super(var1);
   }

   public SVGDescriptor toSVG(GraphicContext var1) {
      Paint var2 = var1.getPaint();
      return this.toSVG((GradientPaint)var2);
   }

   public SVGPaintDescriptor toSVG(GradientPaint var1) {
      SVGPaintDescriptor var2 = (SVGPaintDescriptor)this.descMap.get(var1);
      Document var3 = this.generatorContext.domFactory;
      if (var2 == null) {
         Element var4 = var3.createElementNS("http://www.w3.org/2000/svg", "linearGradient");
         var4.setAttributeNS((String)null, "gradientUnits", "userSpaceOnUse");
         Point2D var5 = var1.getPoint1();
         Point2D var6 = var1.getPoint2();
         var4.setAttributeNS((String)null, "x1", this.doubleString(var5.getX()));
         var4.setAttributeNS((String)null, "y1", this.doubleString(var5.getY()));
         var4.setAttributeNS((String)null, "x2", this.doubleString(var6.getX()));
         var4.setAttributeNS((String)null, "y2", this.doubleString(var6.getY()));
         String var7 = "pad";
         if (var1.isCyclic()) {
            var7 = "reflect";
         }

         var4.setAttributeNS((String)null, "spreadMethod", var7);
         Element var8 = var3.createElementNS("http://www.w3.org/2000/svg", "stop");
         var8.setAttributeNS((String)null, "offset", "0%");
         SVGPaintDescriptor var9 = SVGColor.toSVG(var1.getColor1(), this.generatorContext);
         var8.setAttributeNS((String)null, "stop-color", var9.getPaintValue());
         var8.setAttributeNS((String)null, "stop-opacity", var9.getOpacityValue());
         var4.appendChild(var8);
         var8 = var3.createElementNS("http://www.w3.org/2000/svg", "stop");
         var8.setAttributeNS((String)null, "offset", "100%");
         var9 = SVGColor.toSVG(var1.getColor2(), this.generatorContext);
         var8.setAttributeNS((String)null, "stop-color", var9.getPaintValue());
         var8.setAttributeNS((String)null, "stop-opacity", var9.getOpacityValue());
         var4.appendChild(var8);
         var4.setAttributeNS((String)null, "id", this.generatorContext.idGenerator.generateID("linearGradient"));
         StringBuffer var10 = new StringBuffer("url(");
         var10.append("#");
         var10.append(var4.getAttributeNS((String)null, "id"));
         var10.append(")");
         var2 = new SVGPaintDescriptor(var10.toString(), "1", var4);
         this.descMap.put(var1, var2);
         this.defSet.add(var4);
      }

      return var2;
   }
}

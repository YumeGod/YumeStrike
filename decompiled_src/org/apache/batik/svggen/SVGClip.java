package org.apache.batik.svggen;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import org.apache.batik.ext.awt.g2d.GraphicContext;
import org.w3c.dom.Element;

public class SVGClip extends AbstractSVGConverter {
   public static final Shape ORIGIN = new Line2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
   public static final SVGClipDescriptor NO_CLIP = new SVGClipDescriptor("none", (Element)null);
   private SVGShape shapeConverter;

   public SVGClip(SVGGeneratorContext var1) {
      super(var1);
      this.shapeConverter = new SVGShape(var1);
   }

   public SVGDescriptor toSVG(GraphicContext var1) {
      Shape var2 = var1.getClip();
      SVGClipDescriptor var3 = null;
      if (var2 != null) {
         StringBuffer var4 = new StringBuffer("url(");
         GeneralPath var5 = new GeneralPath(var2);
         ClipKey var6 = new ClipKey(var5, this.generatorContext);
         var3 = (SVGClipDescriptor)this.descMap.get(var6);
         if (var3 == null) {
            Element var7 = this.clipToSVG(var2);
            if (var7 == null) {
               var3 = NO_CLIP;
            } else {
               var4.append("#");
               var4.append(var7.getAttributeNS((String)null, "id"));
               var4.append(")");
               var3 = new SVGClipDescriptor(var4.toString(), var7);
               this.descMap.put(var6, var3);
               this.defSet.add(var7);
            }
         }
      } else {
         var3 = NO_CLIP;
      }

      return var3;
   }

   private Element clipToSVG(Shape var1) {
      Element var2 = this.generatorContext.domFactory.createElementNS("http://www.w3.org/2000/svg", "clipPath");
      var2.setAttributeNS((String)null, "clipPathUnits", "userSpaceOnUse");
      var2.setAttributeNS((String)null, "id", this.generatorContext.idGenerator.generateID("clipPath"));
      Element var3 = this.shapeConverter.toSVG(var1);
      if (var3 != null) {
         var2.appendChild(var3);
         return var2;
      } else {
         var2.appendChild(this.shapeConverter.toSVG(ORIGIN));
         return var2;
      }
   }
}

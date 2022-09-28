package org.apache.batik.bridge;

import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import org.apache.batik.dom.svg.SVGOMUseElement;
import org.apache.batik.ext.awt.image.renderable.ClipRable;
import org.apache.batik.ext.awt.image.renderable.ClipRable8Bit;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.ShapeNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SVGClipPathElementBridge extends AnimatableGenericSVGBridge implements ClipBridge {
   public String getLocalName() {
      return "clipPath";
   }

   public ClipRable createClip(BridgeContext var1, Element var2, Element var3, GraphicsNode var4) {
      String var5 = var2.getAttributeNS((String)null, "transform");
      AffineTransform var6;
      if (var5.length() != 0) {
         var6 = SVGUtilities.convertTransform(var2, "transform", var5, var1);
      } else {
         var6 = new AffineTransform();
      }

      var5 = var2.getAttributeNS((String)null, "clipPathUnits");
      short var7;
      if (var5.length() == 0) {
         var7 = 1;
      } else {
         var7 = SVGUtilities.parseCoordinateSystem(var2, "clipPathUnits", var5, var1);
      }

      if (var7 == 2) {
         var6 = SVGUtilities.toObjectBBox(var6, var4);
      }

      Area var8 = new Area();
      GVTBuilder var9 = var1.getGVTBuilder();
      boolean var10 = false;

      for(Node var11 = var2.getFirstChild(); var11 != null; var11 = var11.getNextSibling()) {
         if (var11.getNodeType() == 1) {
            Element var12 = (Element)var11;
            GraphicsNode var13 = var9.build(var1, var12);
            if (var13 != null) {
               var10 = true;
               if (var12 instanceof SVGOMUseElement) {
                  Node var14 = ((SVGOMUseElement)var12).getCSSFirstChild();
                  if (var14 != null && var14.getNodeType() == 1) {
                     var12 = (Element)var14;
                  }
               }

               int var24 = CSSUtilities.convertClipRule(var12);
               GeneralPath var15 = new GeneralPath(var13.getOutline());
               var15.setWindingRule(var24);
               AffineTransform var16 = var13.getTransform();
               if (var16 == null) {
                  var16 = var6;
               } else {
                  var16.preConcatenate(var6);
               }

               Object var17 = var16.createTransformedShape(var15);
               ShapeNode var18 = new ShapeNode();
               var18.setShape((Shape)var17);
               ClipRable var19 = CSSUtilities.convertClipPath(var12, var18, var1);
               if (var19 != null) {
                  Area var20 = new Area((Shape)var17);
                  var20.subtract(new Area(var19.getClipPath()));
                  var17 = var20;
               }

               var8.add(new Area((Shape)var17));
            }
         }
      }

      if (!var10) {
         return null;
      } else {
         ShapeNode var21 = new ShapeNode();
         var21.setShape(var8);
         ClipRable var22 = CSSUtilities.convertClipPath(var2, var21, var1);
         if (var22 != null) {
            var8.subtract(new Area(var22.getClipPath()));
         }

         Filter var23 = var4.getFilter();
         if (var23 == null) {
            var23 = var4.getGraphicsNodeRable(true);
         }

         boolean var25 = false;
         RenderingHints var26 = CSSUtilities.convertShapeRendering(var2, (RenderingHints)null);
         if (var26 != null) {
            Object var27 = var26.get(RenderingHints.KEY_ANTIALIASING);
            var25 = var27 == RenderingHints.VALUE_ANTIALIAS_ON;
         }

         return new ClipRable8Bit(var23, var8, var25);
      }
   }
}

package org.apache.batik.bridge;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.ext.awt.image.renderable.ClipRable8Bit;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.Marker;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SVGMarkerElementBridge extends AnimatableGenericSVGBridge implements MarkerBridge, ErrorConstants {
   protected SVGMarkerElementBridge() {
   }

   public String getLocalName() {
      return "marker";
   }

   public Marker createMarker(BridgeContext var1, Element var2, Element var3) {
      GVTBuilder var4 = var1.getGVTBuilder();
      CompositeGraphicsNode var5 = new CompositeGraphicsNode();
      boolean var6 = false;

      for(Node var7 = var2.getFirstChild(); var7 != null; var7 = var7.getNextSibling()) {
         if (var7.getNodeType() == 1) {
            Element var8 = (Element)var7;
            GraphicsNode var9 = var4.build(var1, var8);
            if (var9 != null) {
               var6 = true;
               var5.getChildren().add(var9);
            }
         }
      }

      if (!var6) {
         return null;
      } else {
         org.apache.batik.parser.UnitProcessor.Context var24 = UnitProcessor.createContext(var1, var3);
         float var25 = 3.0F;
         String var23 = var2.getAttributeNS((String)null, "markerWidth");
         if (var23.length() != 0) {
            var25 = UnitProcessor.svgHorizontalLengthToUserSpace(var23, "markerWidth", var24);
         }

         if (var25 == 0.0F) {
            return null;
         } else {
            float var10 = 3.0F;
            var23 = var2.getAttributeNS((String)null, "markerHeight");
            if (var23.length() != 0) {
               var10 = UnitProcessor.svgVerticalLengthToUserSpace(var23, "markerHeight", var24);
            }

            if (var10 == 0.0F) {
               return null;
            } else {
               var23 = var2.getAttributeNS((String)null, "orient");
               double var11;
               if (var23.length() == 0) {
                  var11 = 0.0;
               } else if ("auto".equals(var23)) {
                  var11 = Double.NaN;
               } else {
                  try {
                     var11 = (double)SVGUtilities.convertSVGNumber(var23);
                  } catch (NumberFormatException var22) {
                     throw new BridgeException(var1, var2, var22, "attribute.malformed", new Object[]{"orient", var23});
                  }
               }

               Value var13 = CSSUtilities.getComputedStyle(var3, 52);
               float var14 = var13.getFloatValue();
               var23 = var2.getAttributeNS((String)null, "markerUnits");
               short var15;
               if (var23.length() == 0) {
                  var15 = 3;
               } else {
                  var15 = SVGUtilities.parseMarkerCoordinateSystem(var2, "markerUnits", var23, var1);
               }

               AffineTransform var16;
               if (var15 == 3) {
                  var16 = new AffineTransform();
                  var16.scale((double)var14, (double)var14);
               } else {
                  var16 = new AffineTransform();
               }

               AffineTransform var17 = ViewBox.getPreserveAspectRatioTransform(var2, var25, var10, var1);
               if (var17 == null) {
                  return null;
               } else {
                  var16.concatenate(var17);
                  var5.setTransform(var16);
                  if (CSSUtilities.convertOverflow(var2)) {
                     float[] var19 = CSSUtilities.convertClip(var2);
                     Rectangle2D.Float var18;
                     if (var19 == null) {
                        var18 = new Rectangle2D.Float(0.0F, 0.0F, var14 * var25, var14 * var10);
                     } else {
                        var18 = new Rectangle2D.Float(var19[3], var19[0], var14 * var25 - var19[1] - var19[3], var14 * var10 - var19[2] - var19[0]);
                     }

                     CompositeGraphicsNode var20 = new CompositeGraphicsNode();
                     var20.getChildren().add(var5);
                     Filter var21 = var20.getGraphicsNodeRable(true);
                     var20.setClip(new ClipRable8Bit(var21, var18));
                     var5 = var20;
                  }

                  float var26 = 0.0F;
                  var23 = var2.getAttributeNS((String)null, "refX");
                  if (var23.length() != 0) {
                     var26 = UnitProcessor.svgHorizontalCoordinateToUserSpace(var23, "refX", var24);
                  }

                  float var27 = 0.0F;
                  var23 = var2.getAttributeNS((String)null, "refY");
                  if (var23.length() != 0) {
                     var27 = UnitProcessor.svgVerticalCoordinateToUserSpace(var23, "refY", var24);
                  }

                  float[] var28 = new float[]{var26, var27};
                  var16.transform(var28, 0, var28, 0, 1);
                  Marker var29 = new Marker(var5, new Point2D.Float(var28[0], var28[1]), var11);
                  return var29;
               }
            }
         }
      }
   }
}

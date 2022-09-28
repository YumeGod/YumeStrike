package org.apache.batik.bridge;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.font.GVTFontFace;
import org.apache.batik.gvt.font.Glyph;
import org.apache.batik.gvt.text.TextPaintInfo;
import org.apache.batik.parser.AWTPathProducer;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.PathParser;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SVGGlyphElementBridge extends AbstractSVGBridge implements ErrorConstants {
   protected SVGGlyphElementBridge() {
   }

   public String getLocalName() {
      return "glyph";
   }

   public Glyph createGlyph(BridgeContext var1, Element var2, Element var3, int var4, float var5, GVTFontFace var6, TextPaintInfo var7) {
      float var8 = var6.getUnitsPerEm();
      float var9 = var5 / var8;
      AffineTransform var10 = AffineTransform.getScaleInstance((double)var9, (double)(-var9));
      String var11 = var2.getAttributeNS((String)null, "d");
      Shape var12 = null;
      if (var11.length() != 0) {
         AWTPathProducer var13 = new AWTPathProducer();
         var13.setWindingRule(CSSUtilities.convertFillRule(var3));
         boolean var42 = false;

         try {
            var42 = true;
            PathParser var14 = new PathParser();
            var14.setPathHandler(var13);
            var14.parse(var11);
            var42 = false;
         } catch (ParseException var49) {
            throw new BridgeException(var1, var2, var49, "attribute.malformed", new Object[]{"d"});
         } finally {
            if (var42) {
               Shape var17 = var13.getShape();
               Shape var18 = var10.createTransformedShape(var17);
            }
         }

         Shape var52 = var13.getShape();
         Shape var15 = var10.createTransformedShape(var52);
         var12 = var15;
      }

      NodeList var51 = var2.getChildNodes();
      int var53 = var51.getLength();
      int var54 = 0;

      for(int var16 = 0; var16 < var53; ++var16) {
         Node var56 = var51.item(var16);
         if (var56.getNodeType() == 1) {
            ++var54;
         }
      }

      CompositeGraphicsNode var55 = null;
      if (var54 > 0) {
         GVTBuilder var57 = var1.getGVTBuilder();
         var55 = new CompositeGraphicsNode();
         Element var59 = (Element)var2.getParentNode().cloneNode(false);
         NamedNodeMap var19 = var2.getParentNode().getAttributes();
         int var20 = var19.getLength();

         for(int var21 = 0; var21 < var20; ++var21) {
            var59.setAttributeNode((Attr)var19.item(var21));
         }

         Element var63 = (Element)var2.cloneNode(true);
         var59.appendChild(var63);
         var3.appendChild(var59);
         CompositeGraphicsNode var22 = new CompositeGraphicsNode();
         var22.setTransform(var10);
         NodeList var23 = var63.getChildNodes();
         int var24 = var23.getLength();

         for(int var25 = 0; var25 < var24; ++var25) {
            Node var26 = var23.item(var25);
            if (var26.getNodeType() == 1) {
               Element var27 = (Element)var26;
               GraphicsNode var28 = var57.build(var1, var27);
               var22.add(var28);
            }
         }

         var55.add(var22);
         var3.removeChild(var59);
      }

      String var58 = var2.getAttributeNS((String)null, "unicode");
      String var60 = var2.getAttributeNS((String)null, "glyph-name");
      ArrayList var61 = new ArrayList();
      StringTokenizer var62 = new StringTokenizer(var60, " ,");

      while(var62.hasMoreTokens()) {
         var61.add(var62.nextToken());
      }

      String var64 = var2.getAttributeNS((String)null, "orientation");
      String var65 = var2.getAttributeNS((String)null, "arabic-form");
      String var66 = var2.getAttributeNS((String)null, "lang");
      Element var67 = (Element)var2.getParentNode();
      String var68 = var2.getAttributeNS((String)null, "horiz-adv-x");
      if (var68.length() == 0) {
         var68 = var67.getAttributeNS((String)null, "horiz-adv-x");
         if (var68.length() == 0) {
            throw new BridgeException(var1, var67, "attribute.missing", new Object[]{"horiz-adv-x"});
         }
      }

      float var69;
      try {
         var69 = SVGUtilities.convertSVGNumber(var68) * var9;
      } catch (NumberFormatException var48) {
         throw new BridgeException(var1, var2, var48, "attribute.malformed", new Object[]{"horiz-adv-x", var68});
      }

      var68 = var2.getAttributeNS((String)null, "vert-adv-y");
      if (var68.length() == 0) {
         var68 = var67.getAttributeNS((String)null, "vert-adv-y");
         if (var68.length() == 0) {
            var68 = String.valueOf(var6.getUnitsPerEm());
         }
      }

      float var70;
      try {
         var70 = SVGUtilities.convertSVGNumber(var68) * var9;
      } catch (NumberFormatException var47) {
         throw new BridgeException(var1, var2, var47, "attribute.malformed", new Object[]{"vert-adv-y", var68});
      }

      var68 = var2.getAttributeNS((String)null, "vert-origin-x");
      if (var68.length() == 0) {
         var68 = var67.getAttributeNS((String)null, "vert-origin-x");
         if (var68.length() == 0) {
            var68 = Float.toString(var69 / 2.0F);
         }
      }

      float var71;
      try {
         var71 = SVGUtilities.convertSVGNumber(var68) * var9;
      } catch (NumberFormatException var46) {
         throw new BridgeException(var1, var2, var46, "attribute.malformed", new Object[]{"vert-origin-x", var68});
      }

      var68 = var2.getAttributeNS((String)null, "vert-origin-y");
      if (var68.length() == 0) {
         var68 = var67.getAttributeNS((String)null, "vert-origin-y");
         if (var68.length() == 0) {
            var68 = String.valueOf(var6.getAscent());
         }
      }

      float var29;
      try {
         var29 = SVGUtilities.convertSVGNumber(var68) * -var9;
      } catch (NumberFormatException var45) {
         throw new BridgeException(var1, var2, var45, "attribute.malformed", new Object[]{"vert-origin-y", var68});
      }

      Point2D.Float var30 = new Point2D.Float(var71, var29);
      var68 = var67.getAttributeNS((String)null, "horiz-origin-x");
      if (var68.length() == 0) {
         var68 = "0";
      }

      float var31;
      try {
         var31 = SVGUtilities.convertSVGNumber(var68) * var9;
      } catch (NumberFormatException var44) {
         throw new BridgeException(var1, var67, var44, "attribute.malformed", new Object[]{"horiz-origin-x", var68});
      }

      var68 = var67.getAttributeNS((String)null, "horiz-origin-y");
      if (var68.length() == 0) {
         var68 = "0";
      }

      float var32;
      try {
         var32 = SVGUtilities.convertSVGNumber(var68) * -var9;
      } catch (NumberFormatException var43) {
         throw new BridgeException(var1, var2, var43, "attribute.malformed", new Object[]{"horiz-origin-y", var68});
      }

      Point2D.Float var33 = new Point2D.Float(var31, var32);
      return new Glyph(var58, var61, var64, var65, var66, var33, var30, var69, var70, var4, var7, var12, var55);
   }
}

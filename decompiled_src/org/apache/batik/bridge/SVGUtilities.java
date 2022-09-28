package org.apache.batik.bridge;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.dom.util.XMLSupport;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.parser.AWTTransformProducer;
import org.apache.batik.parser.ClockHandler;
import org.apache.batik.parser.ClockParser;
import org.apache.batik.parser.ParseException;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGElement;
import org.w3c.dom.svg.SVGLangSpace;
import org.w3c.dom.svg.SVGNumberList;

public abstract class SVGUtilities implements SVGConstants, ErrorConstants {
   public static final short USER_SPACE_ON_USE = 1;
   public static final short OBJECT_BOUNDING_BOX = 2;
   public static final short STROKE_WIDTH = 3;

   protected SVGUtilities() {
   }

   public static Element getParentElement(Element var0) {
      Node var1;
      for(var1 = CSSEngine.getCSSParentNode(var0); var1 != null && var1.getNodeType() != 1; var1 = CSSEngine.getCSSParentNode(var1)) {
      }

      return (Element)var1;
   }

   public static float[] convertSVGNumberList(SVGNumberList var0) {
      int var1 = var0.getNumberOfItems();
      if (var1 == 0) {
         return null;
      } else {
         float[] var2 = new float[var1];

         for(int var3 = 0; var3 < var1; ++var3) {
            var2[var3] = var0.getItem(var3).getValue();
         }

         return var2;
      }
   }

   public static float convertSVGNumber(String var0) {
      return Float.parseFloat(var0);
   }

   public static int convertSVGInteger(String var0) {
      return Integer.parseInt(var0);
   }

   public static float convertRatio(String var0) {
      float var1 = 1.0F;
      if (var0.endsWith("%")) {
         var0 = var0.substring(0, var0.length() - 1);
         var1 = 100.0F;
      }

      float var2 = Float.parseFloat(var0) / var1;
      if (var2 < 0.0F) {
         var2 = 0.0F;
      } else if (var2 > 1.0F) {
         var2 = 1.0F;
      }

      return var2;
   }

   public static String getDescription(SVGElement var0) {
      String var1 = "";
      boolean var2 = false;
      Node var3 = var0.getFirstChild();
      if (var3 != null && var3.getNodeType() == 1) {
         String var4 = var3.getPrefix() == null ? var3.getNodeName() : var3.getLocalName();
         if (var4.equals("desc")) {
            var2 = ((SVGLangSpace)var3).getXMLspace().equals("preserve");

            for(var3 = var3.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
               if (var3.getNodeType() == 3) {
                  var1 = var1 + var3.getNodeValue();
               }
            }
         }
      }

      return var2 ? XMLSupport.preserveXMLSpace(var1) : XMLSupport.defaultXMLSpace(var1);
   }

   public static boolean matchUserAgent(Element var0, UserAgent var1) {
      String var2;
      StringTokenizer var3;
      String var4;
      if (var0.hasAttributeNS((String)null, "systemLanguage")) {
         var2 = var0.getAttributeNS((String)null, "systemLanguage");
         if (var2.length() == 0) {
            return false;
         }

         var3 = new StringTokenizer(var2, ", ");

         do {
            if (!var3.hasMoreTokens()) {
               return false;
            }

            var4 = var3.nextToken();
         } while(!matchUserLanguage(var4, var1.getLanguages()));
      }

      if (var0.hasAttributeNS((String)null, "requiredFeatures")) {
         var2 = var0.getAttributeNS((String)null, "requiredFeatures");
         if (var2.length() == 0) {
            return false;
         }

         var3 = new StringTokenizer(var2, " ");

         while(var3.hasMoreTokens()) {
            var4 = var3.nextToken();
            if (!var1.hasFeature(var4)) {
               return false;
            }
         }
      }

      if (var0.hasAttributeNS((String)null, "requiredExtensions")) {
         var2 = var0.getAttributeNS((String)null, "requiredExtensions");
         if (var2.length() == 0) {
            return false;
         }

         var3 = new StringTokenizer(var2, " ");

         while(var3.hasMoreTokens()) {
            var4 = var3.nextToken();
            if (!var1.supportExtension(var4)) {
               return false;
            }
         }
      }

      return true;
   }

   protected static boolean matchUserLanguage(String var0, String var1) {
      StringTokenizer var2 = new StringTokenizer(var1, ", ");

      String var3;
      do {
         if (!var2.hasMoreTokens()) {
            return false;
         }

         var3 = var2.nextToken();
      } while(!var0.startsWith(var3));

      if (var0.length() > var3.length()) {
         return var0.charAt(var3.length()) == '-';
      } else {
         return true;
      }
   }

   public static String getChainableAttributeNS(Element var0, String var1, String var2, BridgeContext var3) {
      DocumentLoader var4 = var3.getDocumentLoader();
      Element var5 = var0;
      LinkedList var6 = new LinkedList();

      while(true) {
         String var7 = var5.getAttributeNS(var1, var2);
         if (var7.length() > 0) {
            return var7;
         }

         String var8 = XLinkSupport.getXLinkHref(var5);
         if (var8.length() == 0) {
            return "";
         }

         String var9 = ((AbstractNode)var5).getBaseURI();
         ParsedURL var10 = new ParsedURL(var9, var8);
         Iterator var11 = var6.iterator();

         while(var11.hasNext()) {
            if (var10.equals(var11.next())) {
               throw new BridgeException(var3, var5, "xlink.href.circularDependencies", new Object[]{var8});
            }
         }

         try {
            SVGDocument var12 = (SVGDocument)var5.getOwnerDocument();
            URIResolver var13 = var3.createURIResolver(var12, var4);
            var5 = var13.getElement(var10.toString(), var5);
            var6.add(var10);
         } catch (IOException var14) {
            throw new BridgeException(var3, var5, var14, "uri.io", new Object[]{var8});
         } catch (SecurityException var15) {
            throw new BridgeException(var3, var5, var15, "uri.unsecure", new Object[]{var8});
         }
      }
   }

   public static Point2D convertPoint(String var0, String var1, String var2, String var3, short var4, org.apache.batik.parser.UnitProcessor.Context var5) {
      float var6;
      float var7;
      switch (var4) {
         case 1:
            var6 = UnitProcessor.svgHorizontalCoordinateToUserSpace(var0, var1, var5);
            var7 = UnitProcessor.svgVerticalCoordinateToUserSpace(var2, var3, var5);
            break;
         case 2:
            var6 = UnitProcessor.svgHorizontalCoordinateToObjectBoundingBox(var0, var1, var5);
            var7 = UnitProcessor.svgVerticalCoordinateToObjectBoundingBox(var2, var3, var5);
            break;
         default:
            throw new IllegalArgumentException("Invalid unit type");
      }

      return new Point2D.Float(var6, var7);
   }

   public static float convertLength(String var0, String var1, short var2, org.apache.batik.parser.UnitProcessor.Context var3) {
      switch (var2) {
         case 1:
            return UnitProcessor.svgOtherLengthToUserSpace(var0, var1, var3);
         case 2:
            return UnitProcessor.svgOtherLengthToObjectBoundingBox(var0, var1, var3);
         default:
            throw new IllegalArgumentException("Invalid unit type");
      }
   }

   public static Rectangle2D convertMaskRegion(Element var0, Element var1, GraphicsNode var2, BridgeContext var3) {
      String var4 = var0.getAttributeNS((String)null, "x");
      if (var4.length() == 0) {
         var4 = "-10%";
      }

      String var5 = var0.getAttributeNS((String)null, "y");
      if (var5.length() == 0) {
         var5 = "-10%";
      }

      String var6 = var0.getAttributeNS((String)null, "width");
      if (var6.length() == 0) {
         var6 = "120%";
      }

      String var7 = var0.getAttributeNS((String)null, "height");
      if (var7.length() == 0) {
         var7 = "120%";
      }

      String var9 = var0.getAttributeNS((String)null, "maskUnits");
      short var8;
      if (var9.length() == 0) {
         var8 = 2;
      } else {
         var8 = parseCoordinateSystem(var0, "maskUnits", var9, var3);
      }

      org.apache.batik.parser.UnitProcessor.Context var10 = UnitProcessor.createContext(var3, var1);
      return convertRegion(var4, var5, var6, var7, var8, var2, var10);
   }

   public static Rectangle2D convertPatternRegion(Element var0, Element var1, GraphicsNode var2, BridgeContext var3) {
      String var4 = getChainableAttributeNS(var0, (String)null, "x", var3);
      if (var4.length() == 0) {
         var4 = "0";
      }

      String var5 = getChainableAttributeNS(var0, (String)null, "y", var3);
      if (var5.length() == 0) {
         var5 = "0";
      }

      String var6 = getChainableAttributeNS(var0, (String)null, "width", var3);
      if (var6.length() == 0) {
         throw new BridgeException(var3, var0, "attribute.missing", new Object[]{"width"});
      } else {
         String var7 = getChainableAttributeNS(var0, (String)null, "height", var3);
         if (var7.length() == 0) {
            throw new BridgeException(var3, var0, "attribute.missing", new Object[]{"height"});
         } else {
            String var9 = getChainableAttributeNS(var0, (String)null, "patternUnits", var3);
            short var8;
            if (var9.length() == 0) {
               var8 = 2;
            } else {
               var8 = parseCoordinateSystem(var0, "patternUnits", var9, var3);
            }

            org.apache.batik.parser.UnitProcessor.Context var10 = UnitProcessor.createContext(var3, var1);
            return convertRegion(var4, var5, var6, var7, var8, var2, var10);
         }
      }
   }

   public static float[] convertFilterRes(Element var0, BridgeContext var1) {
      float[] var2 = new float[2];
      String var3 = getChainableAttributeNS(var0, (String)null, "filterRes", var1);
      Float[] var4 = convertSVGNumberOptionalNumber(var0, "filterRes", var3, var1);
      if (!(var2[0] < 0.0F) && !(var2[1] < 0.0F)) {
         if (var4[0] == null) {
            var2[0] = -1.0F;
         } else {
            var2[0] = var4[0];
            if (var2[0] < 0.0F) {
               throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"filterRes", var3});
            }
         }

         if (var4[1] == null) {
            var2[1] = var2[0];
         } else {
            var2[1] = var4[1];
            if (var2[1] < 0.0F) {
               throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"filterRes", var3});
            }
         }

         return var2;
      } else {
         throw new BridgeException(var1, var0, "attribute.malformed", new Object[]{"filterRes", var3});
      }
   }

   public static Float[] convertSVGNumberOptionalNumber(Element var0, String var1, String var2, BridgeContext var3) {
      Float[] var4 = new Float[2];
      if (var2.length() == 0) {
         return var4;
      } else {
         try {
            StringTokenizer var5 = new StringTokenizer(var2, " ");
            var4[0] = new Float(Float.parseFloat(var5.nextToken()));
            if (var5.hasMoreTokens()) {
               var4[1] = new Float(Float.parseFloat(var5.nextToken()));
            }

            if (var5.hasMoreTokens()) {
               throw new BridgeException(var3, var0, "attribute.malformed", new Object[]{var1, var2});
            } else {
               return var4;
            }
         } catch (NumberFormatException var6) {
            throw new BridgeException(var3, var0, var6, "attribute.malformed", new Object[]{var1, var2, var6});
         }
      }
   }

   public static Rectangle2D convertFilterChainRegion(Element var0, Element var1, GraphicsNode var2, BridgeContext var3) {
      String var4 = getChainableAttributeNS(var0, (String)null, "x", var3);
      if (var4.length() == 0) {
         var4 = "-10%";
      }

      String var5 = getChainableAttributeNS(var0, (String)null, "y", var3);
      if (var5.length() == 0) {
         var5 = "-10%";
      }

      String var6 = getChainableAttributeNS(var0, (String)null, "width", var3);
      if (var6.length() == 0) {
         var6 = "120%";
      }

      String var7 = getChainableAttributeNS(var0, (String)null, "height", var3);
      if (var7.length() == 0) {
         var7 = "120%";
      }

      String var9 = getChainableAttributeNS(var0, (String)null, "filterUnits", var3);
      short var8;
      if (var9.length() == 0) {
         var8 = 2;
      } else {
         var8 = parseCoordinateSystem(var0, "filterUnits", var9, var3);
      }

      org.apache.batik.parser.UnitProcessor.Context var10 = UnitProcessor.createContext(var3, var1);
      Rectangle2D var11 = convertRegion(var4, var5, var6, var7, var8, var2, var10);
      var9 = getChainableAttributeNS(var0, (String)null, "filterMarginsUnits", var3);
      if (var9.length() == 0) {
         var8 = 1;
      } else {
         var8 = parseCoordinateSystem(var0, "filterMarginsUnits", var9, var3);
      }

      String var12 = var0.getAttributeNS((String)null, "mx");
      if (var12.length() == 0) {
         var12 = "0";
      }

      String var13 = var0.getAttributeNS((String)null, "my");
      if (var13.length() == 0) {
         var13 = "0";
      }

      String var14 = var0.getAttributeNS((String)null, "mw");
      if (var14.length() == 0) {
         var14 = "0";
      }

      String var15 = var0.getAttributeNS((String)null, "mh");
      if (var15.length() == 0) {
         var15 = "0";
      }

      return extendRegion(var12, var13, var14, var15, var8, var2, var11, var10);
   }

   protected static Rectangle2D extendRegion(String var0, String var1, String var2, String var3, short var4, GraphicsNode var5, Rectangle2D var6, org.apache.batik.parser.UnitProcessor.Context var7) {
      float var8;
      float var9;
      float var10;
      float var11;
      switch (var4) {
         case 1:
            var8 = UnitProcessor.svgHorizontalCoordinateToUserSpace(var0, "mx", var7);
            var9 = UnitProcessor.svgVerticalCoordinateToUserSpace(var1, "my", var7);
            var10 = UnitProcessor.svgHorizontalCoordinateToUserSpace(var2, "mw", var7);
            var11 = UnitProcessor.svgVerticalCoordinateToUserSpace(var3, "mh", var7);
            break;
         case 2:
            Rectangle2D var12 = var5.getGeometryBounds();
            if (var12 == null) {
               var11 = 0.0F;
               var10 = 0.0F;
               var9 = 0.0F;
               var8 = 0.0F;
            } else {
               var8 = UnitProcessor.svgHorizontalCoordinateToObjectBoundingBox(var0, "mx", var7);
               var8 = (float)((double)var8 * var12.getWidth());
               var9 = UnitProcessor.svgVerticalCoordinateToObjectBoundingBox(var1, "my", var7);
               var9 = (float)((double)var9 * var12.getHeight());
               var10 = UnitProcessor.svgHorizontalCoordinateToObjectBoundingBox(var2, "mw", var7);
               var10 = (float)((double)var10 * var12.getWidth());
               var11 = UnitProcessor.svgVerticalCoordinateToObjectBoundingBox(var3, "mh", var7);
               var11 = (float)((double)var11 * var12.getHeight());
            }
            break;
         default:
            throw new IllegalArgumentException("Invalid unit type");
      }

      var6.setRect(var6.getX() + (double)var8, var6.getY() + (double)var9, var6.getWidth() + (double)var10, var6.getHeight() + (double)var11);
      return var6;
   }

   public static Rectangle2D getBaseFilterPrimitiveRegion(Element var0, Element var1, GraphicsNode var2, Rectangle2D var3, BridgeContext var4) {
      org.apache.batik.parser.UnitProcessor.Context var6 = UnitProcessor.createContext(var4, var1);
      double var7 = var3.getX();
      String var5 = var0.getAttributeNS((String)null, "x");
      if (var5.length() != 0) {
         var7 = (double)UnitProcessor.svgHorizontalCoordinateToUserSpace(var5, "x", var6);
      }

      double var9 = var3.getY();
      var5 = var0.getAttributeNS((String)null, "y");
      if (var5.length() != 0) {
         var9 = (double)UnitProcessor.svgVerticalCoordinateToUserSpace(var5, "y", var6);
      }

      double var11 = var3.getWidth();
      var5 = var0.getAttributeNS((String)null, "width");
      if (var5.length() != 0) {
         var11 = (double)UnitProcessor.svgHorizontalLengthToUserSpace(var5, "width", var6);
      }

      double var13 = var3.getHeight();
      var5 = var0.getAttributeNS((String)null, "height");
      if (var5.length() != 0) {
         var13 = (double)UnitProcessor.svgVerticalLengthToUserSpace(var5, "height", var6);
      }

      return new Rectangle2D.Double(var7, var9, var11, var13);
   }

   public static Rectangle2D convertFilterPrimitiveRegion(Element var0, Element var1, Element var2, GraphicsNode var3, Rectangle2D var4, Rectangle2D var5, BridgeContext var6) {
      String var7 = "";
      if (var1 != null) {
         var7 = getChainableAttributeNS(var1, (String)null, "primitiveUnits", var6);
      }

      short var8;
      if (var7.length() == 0) {
         var8 = 1;
      } else {
         var8 = parseCoordinateSystem(var1, "filterUnits", var7, var6);
      }

      String var9 = "";
      String var10 = "";
      String var11 = "";
      String var12 = "";
      if (var0 != null) {
         var9 = var0.getAttributeNS((String)null, "x");
         var10 = var0.getAttributeNS((String)null, "y");
         var11 = var0.getAttributeNS((String)null, "width");
         var12 = var0.getAttributeNS((String)null, "height");
      }

      double var13 = var4.getX();
      double var15 = var4.getY();
      double var17 = var4.getWidth();
      double var19 = var4.getHeight();
      org.apache.batik.parser.UnitProcessor.Context var21 = UnitProcessor.createContext(var6, var2);
      Rectangle2D var22;
      switch (var8) {
         case 1:
            if (var9.length() != 0) {
               var13 = (double)UnitProcessor.svgHorizontalCoordinateToUserSpace(var9, "x", var21);
            }

            if (var10.length() != 0) {
               var15 = (double)UnitProcessor.svgVerticalCoordinateToUserSpace(var10, "y", var21);
            }

            if (var11.length() != 0) {
               var17 = (double)UnitProcessor.svgHorizontalLengthToUserSpace(var11, "width", var21);
            }

            if (var12.length() != 0) {
               var19 = (double)UnitProcessor.svgVerticalLengthToUserSpace(var12, "height", var21);
            }
            break;
         case 2:
            var22 = var3.getGeometryBounds();
            if (var22 != null) {
               if (var9.length() != 0) {
                  var13 = (double)UnitProcessor.svgHorizontalCoordinateToObjectBoundingBox(var9, "x", var21);
                  var13 = var22.getX() + var13 * var22.getWidth();
               }

               if (var10.length() != 0) {
                  var15 = (double)UnitProcessor.svgVerticalCoordinateToObjectBoundingBox(var10, "y", var21);
                  var15 = var22.getY() + var15 * var22.getHeight();
               }

               if (var11.length() != 0) {
                  var17 = (double)UnitProcessor.svgHorizontalLengthToObjectBoundingBox(var11, "width", var21);
                  var17 *= var22.getWidth();
               }

               if (var12.length() != 0) {
                  var19 = (double)UnitProcessor.svgVerticalLengthToObjectBoundingBox(var12, "height", var21);
                  var19 *= var22.getHeight();
               }
            }
            break;
         default:
            throw new Error("invalid unitsType:" + var8);
      }

      Rectangle2D.Double var27 = new Rectangle2D.Double(var13, var15, var17, var19);
      var7 = "";
      if (var1 != null) {
         var7 = getChainableAttributeNS(var1, (String)null, "filterPrimitiveMarginsUnits", var6);
      }

      if (var7.length() == 0) {
         var8 = 1;
      } else {
         var8 = parseCoordinateSystem(var1, "filterPrimitiveMarginsUnits", var7, var6);
      }

      String var23 = "";
      String var24 = "";
      String var25 = "";
      String var26 = "";
      if (var0 != null) {
         var23 = var0.getAttributeNS((String)null, "mx");
         var24 = var0.getAttributeNS((String)null, "my");
         var25 = var0.getAttributeNS((String)null, "mw");
         var26 = var0.getAttributeNS((String)null, "mh");
      }

      if (var23.length() == 0) {
         var23 = "0";
      }

      if (var24.length() == 0) {
         var24 = "0";
      }

      if (var25.length() == 0) {
         var25 = "0";
      }

      if (var26.length() == 0) {
         var26 = "0";
      }

      var22 = extendRegion(var23, var24, var25, var26, var8, var3, var27, var21);
      Rectangle2D.intersect(var22, var5, var22);
      return var22;
   }

   public static Rectangle2D convertFilterPrimitiveRegion(Element var0, Element var1, GraphicsNode var2, Rectangle2D var3, Rectangle2D var4, BridgeContext var5) {
      Node var6 = var0.getParentNode();
      Element var7 = null;
      if (var6 != null && var6.getNodeType() == 1) {
         var7 = (Element)var6;
      }

      return convertFilterPrimitiveRegion(var0, var7, var1, var2, var3, var4, var5);
   }

   public static short parseCoordinateSystem(Element var0, String var1, String var2, BridgeContext var3) {
      if ("userSpaceOnUse".equals(var2)) {
         return 1;
      } else if ("objectBoundingBox".equals(var2)) {
         return 2;
      } else {
         throw new BridgeException(var3, var0, "attribute.malformed", new Object[]{var1, var2});
      }
   }

   public static short parseMarkerCoordinateSystem(Element var0, String var1, String var2, BridgeContext var3) {
      if ("userSpaceOnUse".equals(var2)) {
         return 1;
      } else if ("strokeWidth".equals(var2)) {
         return 3;
      } else {
         throw new BridgeException(var3, var0, "attribute.malformed", new Object[]{var1, var2});
      }
   }

   protected static Rectangle2D convertRegion(String var0, String var1, String var2, String var3, short var4, GraphicsNode var5, org.apache.batik.parser.UnitProcessor.Context var6) {
      double var7;
      double var9;
      double var11;
      double var13;
      switch (var4) {
         case 1:
            var7 = (double)UnitProcessor.svgHorizontalCoordinateToUserSpace(var0, "x", var6);
            var9 = (double)UnitProcessor.svgVerticalCoordinateToUserSpace(var1, "y", var6);
            var11 = (double)UnitProcessor.svgHorizontalLengthToUserSpace(var2, "width", var6);
            var13 = (double)UnitProcessor.svgVerticalLengthToUserSpace(var3, "height", var6);
            break;
         case 2:
            var7 = (double)UnitProcessor.svgHorizontalCoordinateToObjectBoundingBox(var0, "x", var6);
            var9 = (double)UnitProcessor.svgVerticalCoordinateToObjectBoundingBox(var1, "y", var6);
            var11 = (double)UnitProcessor.svgHorizontalLengthToObjectBoundingBox(var2, "width", var6);
            var13 = (double)UnitProcessor.svgVerticalLengthToObjectBoundingBox(var3, "height", var6);
            Rectangle2D var15 = var5.getGeometryBounds();
            if (var15 != null) {
               var7 = var15.getX() + var7 * var15.getWidth();
               var9 = var15.getY() + var9 * var15.getHeight();
               var11 *= var15.getWidth();
               var13 *= var15.getHeight();
            } else {
               var13 = 0.0;
               var11 = 0.0;
               var9 = 0.0;
               var7 = 0.0;
            }
            break;
         default:
            throw new Error("invalid unitsType:" + var4);
      }

      return new Rectangle2D.Double(var7, var9, var11, var13);
   }

   public static AffineTransform convertTransform(Element var0, String var1, String var2, BridgeContext var3) {
      try {
         return AWTTransformProducer.createAffineTransform(var2);
      } catch (ParseException var5) {
         throw new BridgeException(var3, var0, var5, "attribute.malformed", new Object[]{var1, var2, var5});
      }
   }

   public static AffineTransform toObjectBBox(AffineTransform var0, GraphicsNode var1) {
      AffineTransform var2 = new AffineTransform();
      Rectangle2D var3 = var1.getGeometryBounds();
      if (var3 != null) {
         var2.translate(var3.getX(), var3.getY());
         var2.scale(var3.getWidth(), var3.getHeight());
      }

      var2.concatenate(var0);
      return var2;
   }

   public static Rectangle2D toObjectBBox(Rectangle2D var0, GraphicsNode var1) {
      Rectangle2D var2 = var1.getGeometryBounds();
      return var2 != null ? new Rectangle2D.Double(var2.getX() + var0.getX() * var2.getWidth(), var2.getY() + var0.getY() * var2.getHeight(), var0.getWidth() * var2.getWidth(), var0.getHeight() * var2.getHeight()) : new Rectangle2D.Double();
   }

   public static float convertSnapshotTime(Element var0, BridgeContext var1) {
      if (!var0.hasAttributeNS((String)null, "snapshotTime")) {
         return 0.0F;
      } else {
         String var2 = var0.getAttributeNS((String)null, "snapshotTime");
         if (var2.equals("none")) {
            return 0.0F;
         } else {
            ClockParser var3 = new ClockParser(false);

            class Handler implements ClockHandler {
               float time;

               public void clockValue(float var1) {
                  this.time = var1;
               }
            }

            Handler var4 = new Handler();
            var3.setClockHandler(var4);

            try {
               var3.parse(var2);
            } catch (ParseException var6) {
               throw new BridgeException((BridgeContext)null, var0, var6, "attribute.malformed", new Object[]{"snapshotTime", var2, var6});
            }

            return var4.time;
         }
      }
   }
}

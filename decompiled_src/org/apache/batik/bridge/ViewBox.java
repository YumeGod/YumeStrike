package org.apache.batik.bridge;

import java.awt.geom.AffineTransform;
import java.util.StringTokenizer;
import org.apache.batik.dom.svg.LiveAttributeException;
import org.apache.batik.dom.svg.SVGOMAnimatedRect;
import org.apache.batik.parser.AWTTransformProducer;
import org.apache.batik.parser.FragmentIdentifierHandler;
import org.apache.batik.parser.FragmentIdentifierParser;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.PreserveAspectRatioParser;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedPreserveAspectRatio;
import org.w3c.dom.svg.SVGAnimatedRect;
import org.w3c.dom.svg.SVGPreserveAspectRatio;
import org.w3c.dom.svg.SVGRect;

public abstract class ViewBox implements SVGConstants, ErrorConstants {
   protected ViewBox() {
   }

   public static AffineTransform getViewTransform(String var0, Element var1, float var2, float var3, BridgeContext var4) {
      if (var0 != null && var0.length() != 0) {
         ViewHandler var5 = new ViewHandler();
         FragmentIdentifierParser var6 = new FragmentIdentifierParser();
         var6.setFragmentIdentifierHandler(var5);
         var6.parse(var0);
         Element var7 = var1;
         if (var5.hasId) {
            Document var8 = var1.getOwnerDocument();
            var7 = var8.getElementById(var5.id);
         }

         if (var7 == null) {
            throw new BridgeException(var4, var1, "uri.malformed", new Object[]{var0});
         } else {
            if (!var7.getNamespaceURI().equals("http://www.w3.org/2000/svg") || !var7.getLocalName().equals("view")) {
               var7 = getClosestAncestorSVGElement(var1);
            }

            float[] var16;
            if (var5.hasViewBox) {
               var16 = var5.viewBox;
            } else {
               String var9 = var7.getAttributeNS((String)null, "viewBox");
               var16 = parseViewBoxAttribute(var7, var9, var4);
            }

            boolean var10;
            short var17;
            if (var5.hasPreserveAspectRatio) {
               var17 = var5.align;
               var10 = var5.meet;
            } else {
               String var11 = var7.getAttributeNS((String)null, "preserveAspectRatio");
               PreserveAspectRatioParser var12 = new PreserveAspectRatioParser();
               ViewHandler var13 = new ViewHandler();
               var12.setPreserveAspectRatioHandler(var13);

               try {
                  var12.parse(var11);
               } catch (ParseException var15) {
                  throw new BridgeException(var4, var7, var15, "attribute.malformed", new Object[]{"preserveAspectRatio", var11, var15});
               }

               var17 = var13.align;
               var10 = var13.meet;
            }

            AffineTransform var18 = getPreserveAspectRatioTransform(var16, var17, var10, var2, var3);
            if (var5.hasTransform) {
               var18.concatenate(var5.getAffineTransform());
            }

            return var18;
         }
      } else {
         return getPreserveAspectRatioTransform(var1, var2, var3, var4);
      }
   }

   private static Element getClosestAncestorSVGElement(Element var0) {
      for(Object var1 = var0; var1 != null && ((Node)var1).getNodeType() == 1; var1 = ((Node)var1).getParentNode()) {
         Element var2 = (Element)var1;
         if (var2.getNamespaceURI().equals("http://www.w3.org/2000/svg") && var2.getLocalName().equals("svg")) {
            return var2;
         }
      }

      return null;
   }

   /** @deprecated */
   public static AffineTransform getPreserveAspectRatioTransform(Element var0, float var1, float var2) {
      return getPreserveAspectRatioTransform(var0, var1, var2, (BridgeContext)null);
   }

   public static AffineTransform getPreserveAspectRatioTransform(Element var0, float var1, float var2, BridgeContext var3) {
      String var4 = var0.getAttributeNS((String)null, "viewBox");
      String var5 = var0.getAttributeNS((String)null, "preserveAspectRatio");
      return getPreserveAspectRatioTransform(var0, var4, var5, var1, var2, var3);
   }

   public static AffineTransform getPreserveAspectRatioTransform(Element var0, String var1, String var2, float var3, float var4, BridgeContext var5) {
      if (var1.length() == 0) {
         return new AffineTransform();
      } else {
         float[] var6 = parseViewBoxAttribute(var0, var1, var5);
         PreserveAspectRatioParser var7 = new PreserveAspectRatioParser();
         ViewHandler var8 = new ViewHandler();
         var7.setPreserveAspectRatioHandler(var8);

         try {
            var7.parse(var2);
         } catch (ParseException var10) {
            throw new BridgeException(var5, var0, var10, "attribute.malformed", new Object[]{"preserveAspectRatio", var2, var10});
         }

         return getPreserveAspectRatioTransform(var6, var8.align, var8.meet, var3, var4);
      }
   }

   public static AffineTransform getPreserveAspectRatioTransform(Element var0, float[] var1, float var2, float var3, BridgeContext var4) {
      String var5 = var0.getAttributeNS((String)null, "preserveAspectRatio");
      PreserveAspectRatioParser var6 = new PreserveAspectRatioParser();
      ViewHandler var7 = new ViewHandler();
      var6.setPreserveAspectRatioHandler(var7);

      try {
         var6.parse(var5);
      } catch (ParseException var9) {
         throw new BridgeException(var4, var0, var9, "attribute.malformed", new Object[]{"preserveAspectRatio", var5, var9});
      }

      return getPreserveAspectRatioTransform(var1, var7.align, var7.meet, var2, var3);
   }

   public static AffineTransform getPreserveAspectRatioTransform(Element var0, float[] var1, float var2, float var3, SVGAnimatedPreserveAspectRatio var4, BridgeContext var5) {
      try {
         SVGPreserveAspectRatio var6 = var4.getAnimVal();
         short var7 = var6.getAlign();
         boolean var8 = var6.getMeetOrSlice() == 1;
         return getPreserveAspectRatioTransform(var1, var7, var8, var2, var3);
      } catch (LiveAttributeException var9) {
         throw new BridgeException(var5, var9);
      }
   }

   public static AffineTransform getPreserveAspectRatioTransform(Element var0, SVGAnimatedRect var1, SVGAnimatedPreserveAspectRatio var2, float var3, float var4, BridgeContext var5) {
      if (!((SVGOMAnimatedRect)var1).isSpecified()) {
         return new AffineTransform();
      } else {
         SVGRect var6 = var1.getAnimVal();
         float[] var7 = new float[]{var6.getX(), var6.getY(), var6.getWidth(), var6.getHeight()};
         return getPreserveAspectRatioTransform(var0, var7, var3, var4, var2, var5);
      }
   }

   public static float[] parseViewBoxAttribute(Element var0, String var1, BridgeContext var2) {
      if (var1.length() == 0) {
         return null;
      } else {
         int var3 = 0;
         float[] var4 = new float[4];
         StringTokenizer var5 = new StringTokenizer(var1, " ,");

         try {
            while(var3 < 4 && var5.hasMoreTokens()) {
               var4[var3] = Float.parseFloat(var5.nextToken());
               ++var3;
            }
         } catch (NumberFormatException var7) {
            throw new BridgeException(var2, var0, var7, "attribute.malformed", new Object[]{"viewBox", var1, var7});
         }

         if (var3 != 4) {
            throw new BridgeException(var2, var0, "attribute.malformed", new Object[]{"viewBox", var1});
         } else if (!(var4[2] < 0.0F) && !(var4[3] < 0.0F)) {
            return var4[2] != 0.0F && var4[3] != 0.0F ? var4 : null;
         } else {
            throw new BridgeException(var2, var0, "attribute.malformed", new Object[]{"viewBox", var1});
         }
      }
   }

   public static AffineTransform getPreserveAspectRatioTransform(float[] var0, short var1, boolean var2, float var3, float var4) {
      if (var0 == null) {
         return new AffineTransform();
      } else {
         AffineTransform var5 = new AffineTransform();
         float var6 = var0[2] / var0[3];
         float var7 = var3 / var4;
         if (var1 == 1) {
            var5.scale((double)(var3 / var0[2]), (double)(var4 / var0[3]));
            var5.translate((double)(-var0[0]), (double)(-var0[1]));
         } else {
            float var8;
            if (var6 < var7 && var2 || var6 >= var7 && !var2) {
               var8 = var4 / var0[3];
               var5.scale((double)var8, (double)var8);
               switch (var1) {
                  case 2:
                  case 5:
                  case 8:
                     var5.translate((double)(-var0[0]), (double)(-var0[1]));
                     break;
                  case 3:
                  case 6:
                  case 9:
                     var5.translate((double)(-var0[0] - (var0[2] - var3 * var0[3] / var4) / 2.0F), (double)(-var0[1]));
                     break;
                  case 4:
                  case 7:
                  default:
                     var5.translate((double)(-var0[0] - (var0[2] - var3 * var0[3] / var4)), (double)(-var0[1]));
               }
            } else {
               var8 = var3 / var0[2];
               var5.scale((double)var8, (double)var8);
               switch (var1) {
                  case 2:
                  case 3:
                  case 4:
                     var5.translate((double)(-var0[0]), (double)(-var0[1]));
                     break;
                  case 5:
                  case 6:
                  case 7:
                     var5.translate((double)(-var0[0]), (double)(-var0[1] - (var0[3] - var4 * var0[2] / var3) / 2.0F));
                     break;
                  default:
                     var5.translate((double)(-var0[0]), (double)(-var0[1] - (var0[3] - var4 * var0[2] / var3)));
               }
            }
         }

         return var5;
      }
   }

   protected static class ViewHandler extends AWTTransformProducer implements FragmentIdentifierHandler {
      public boolean hasTransform;
      public boolean hasId;
      public boolean hasViewBox;
      public boolean hasViewTargetParams;
      public boolean hasZoomAndPanParams;
      public String id;
      public float[] viewBox;
      public String viewTargetParams;
      public boolean isMagnify;
      public boolean hasPreserveAspectRatio;
      public short align;
      public boolean meet = true;

      public void endTransformList() throws ParseException {
         super.endTransformList();
         this.hasTransform = true;
      }

      public void startFragmentIdentifier() throws ParseException {
      }

      public void idReference(String var1) throws ParseException {
         this.id = var1;
         this.hasId = true;
      }

      public void viewBox(float var1, float var2, float var3, float var4) throws ParseException {
         this.hasViewBox = true;
         this.viewBox = new float[4];
         this.viewBox[0] = var1;
         this.viewBox[1] = var2;
         this.viewBox[2] = var3;
         this.viewBox[3] = var4;
      }

      public void startViewTarget() throws ParseException {
      }

      public void viewTarget(String var1) throws ParseException {
         this.viewTargetParams = var1;
         this.hasViewTargetParams = true;
      }

      public void endViewTarget() throws ParseException {
      }

      public void zoomAndPan(boolean var1) {
         this.isMagnify = var1;
         this.hasZoomAndPanParams = true;
      }

      public void endFragmentIdentifier() throws ParseException {
      }

      public void startPreserveAspectRatio() throws ParseException {
      }

      public void none() throws ParseException {
         this.align = 1;
      }

      public void xMaxYMax() throws ParseException {
         this.align = 10;
      }

      public void xMaxYMid() throws ParseException {
         this.align = 7;
      }

      public void xMaxYMin() throws ParseException {
         this.align = 4;
      }

      public void xMidYMax() throws ParseException {
         this.align = 9;
      }

      public void xMidYMid() throws ParseException {
         this.align = 6;
      }

      public void xMidYMin() throws ParseException {
         this.align = 3;
      }

      public void xMinYMax() throws ParseException {
         this.align = 8;
      }

      public void xMinYMid() throws ParseException {
         this.align = 5;
      }

      public void xMinYMin() throws ParseException {
         this.align = 2;
      }

      public void meet() throws ParseException {
         this.meet = true;
      }

      public void slice() throws ParseException {
         this.meet = false;
      }

      public void endPreserveAspectRatio() throws ParseException {
         this.hasPreserveAspectRatio = true;
      }
   }
}

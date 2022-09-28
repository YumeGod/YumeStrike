package org.apache.batik.bridge;

import java.util.LinkedList;
import java.util.List;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SVGFontFaceElementBridge extends AbstractSVGBridge implements ErrorConstants {
   public String getLocalName() {
      return "font-face";
   }

   public SVGFontFace createFontFace(BridgeContext var1, Element var2) {
      String var3 = var2.getAttributeNS((String)null, "font-family");
      String var4 = var2.getAttributeNS((String)null, "units-per-em");
      if (var4.length() == 0) {
         var4 = "1000";
      }

      float var5;
      try {
         var5 = SVGUtilities.convertSVGNumber(var4);
      } catch (NumberFormatException var40) {
         throw new BridgeException(var1, var2, var40, "attribute.malformed", new Object[]{"units-per-em", var4});
      }

      String var6 = var2.getAttributeNS((String)null, "font-weight");
      if (var6.length() == 0) {
         var6 = "all";
      }

      String var7 = var2.getAttributeNS((String)null, "font-style");
      if (var7.length() == 0) {
         var7 = "all";
      }

      String var8 = var2.getAttributeNS((String)null, "font-variant");
      if (var8.length() == 0) {
         var8 = "normal";
      }

      String var9 = var2.getAttributeNS((String)null, "font-stretch");
      if (var9.length() == 0) {
         var9 = "normal";
      }

      String var10 = var2.getAttributeNS((String)null, "slope");
      if (var10.length() == 0) {
         var10 = "0";
      }

      float var11;
      try {
         var11 = SVGUtilities.convertSVGNumber(var10);
      } catch (NumberFormatException var39) {
         throw new BridgeException(var1, var2, var39, "attribute.malformed", new Object[]{"0", var10});
      }

      String var12 = var2.getAttributeNS((String)null, "panose-1");
      if (var12.length() == 0) {
         var12 = "0 0 0 0 0 0 0 0 0 0";
      }

      String var13 = var2.getAttributeNS((String)null, "ascent");
      if (var13.length() == 0) {
         var13 = String.valueOf((double)var5 * 0.8);
      }

      float var14;
      try {
         var14 = SVGUtilities.convertSVGNumber(var13);
      } catch (NumberFormatException var38) {
         throw new BridgeException(var1, var2, var38, "attribute.malformed", new Object[]{"0", var13});
      }

      String var15 = var2.getAttributeNS((String)null, "descent");
      if (var15.length() == 0) {
         var15 = String.valueOf((double)var5 * 0.2);
      }

      float var16;
      try {
         var16 = SVGUtilities.convertSVGNumber(var15);
      } catch (NumberFormatException var37) {
         throw new BridgeException(var1, var2, var37, "attribute.malformed", new Object[]{"0", var15});
      }

      String var17 = var2.getAttributeNS((String)null, "underline-position");
      if (var17.length() == 0) {
         var17 = String.valueOf(-3.0F * var5 / 40.0F);
      }

      float var18;
      try {
         var18 = SVGUtilities.convertSVGNumber(var17);
      } catch (NumberFormatException var36) {
         throw new BridgeException(var1, var2, var36, "attribute.malformed", new Object[]{"0", var17});
      }

      String var19 = var2.getAttributeNS((String)null, "underline-thickness");
      if (var19.length() == 0) {
         var19 = String.valueOf(var5 / 20.0F);
      }

      float var20;
      try {
         var20 = SVGUtilities.convertSVGNumber(var19);
      } catch (NumberFormatException var35) {
         throw new BridgeException(var1, var2, var35, "attribute.malformed", new Object[]{"0", var19});
      }

      String var21 = var2.getAttributeNS((String)null, "strikethrough-position");
      if (var21.length() == 0) {
         var21 = String.valueOf(3.0F * var14 / 8.0F);
      }

      float var22;
      try {
         var22 = SVGUtilities.convertSVGNumber(var21);
      } catch (NumberFormatException var34) {
         throw new BridgeException(var1, var2, var34, "attribute.malformed", new Object[]{"0", var21});
      }

      String var23 = var2.getAttributeNS((String)null, "strikethrough-thickness");
      if (var23.length() == 0) {
         var23 = String.valueOf(var5 / 20.0F);
      }

      float var24;
      try {
         var24 = SVGUtilities.convertSVGNumber(var23);
      } catch (NumberFormatException var33) {
         throw new BridgeException(var1, var2, var33, "attribute.malformed", new Object[]{"0", var23});
      }

      String var25 = var2.getAttributeNS((String)null, "overline-position");
      if (var25.length() == 0) {
         var25 = String.valueOf(var14);
      }

      float var26;
      try {
         var26 = SVGUtilities.convertSVGNumber(var25);
      } catch (NumberFormatException var32) {
         throw new BridgeException(var1, var2, var32, "attribute.malformed", new Object[]{"0", var25});
      }

      String var27 = var2.getAttributeNS((String)null, "overline-thickness");
      if (var27.length() == 0) {
         var27 = String.valueOf(var5 / 20.0F);
      }

      float var28;
      try {
         var28 = SVGUtilities.convertSVGNumber(var27);
      } catch (NumberFormatException var31) {
         throw new BridgeException(var1, var2, var31, "attribute.malformed", new Object[]{"0", var27});
      }

      List var29 = null;
      Element var30 = SVGUtilities.getParentElement(var2);
      if (!var30.getNamespaceURI().equals("http://www.w3.org/2000/svg") || !var30.getLocalName().equals("font")) {
         var29 = this.getFontFaceSrcs(var2);
      }

      return new SVGFontFace(var2, var29, var3, var5, var6, var7, var8, var9, var11, var12, var14, var16, var22, var24, var18, var20, var26, var28);
   }

   public List getFontFaceSrcs(Element var1) {
      Element var2 = null;

      for(Node var3 = var1.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
         if (var3.getNodeType() == 1 && var3.getNamespaceURI().equals("http://www.w3.org/2000/svg") && var3.getLocalName().equals("font-face-src")) {
            var2 = (Element)var3;
            break;
         }
      }

      if (var2 == null) {
         return null;
      } else {
         LinkedList var9 = new LinkedList();

         for(Node var4 = var2.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
            if (var4.getNodeType() == 1 && var4.getNamespaceURI().equals("http://www.w3.org/2000/svg")) {
               Element var5;
               String var6;
               if (var4.getLocalName().equals("font-face-uri")) {
                  var5 = (Element)var4;
                  var6 = XLinkSupport.getXLinkHref(var5);
                  String var7 = AbstractNode.getBaseURI(var5);
                  ParsedURL var8;
                  if (var7 != null) {
                     var8 = new ParsedURL(var7, var6);
                  } else {
                     var8 = new ParsedURL(var6);
                  }

                  var9.add(var8);
               } else if (var4.getLocalName().equals("font-face-name")) {
                  var5 = (Element)var4;
                  var6 = var5.getAttribute("name");
                  if (var6.length() != 0) {
                     var9.add(var6);
                  }
               }
            }
         }

         return var9;
      }
   }
}

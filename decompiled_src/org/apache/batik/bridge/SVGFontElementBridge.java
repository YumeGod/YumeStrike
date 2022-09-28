package org.apache.batik.bridge;

import org.apache.batik.gvt.font.GVTFontFace;
import org.apache.batik.gvt.text.ArabicTextHandler;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SVGFontElementBridge extends AbstractSVGBridge {
   public String getLocalName() {
      return "font";
   }

   public SVGGVTFont createFont(BridgeContext var1, Element var2, Element var3, float var4, GVTFontFace var5) {
      NodeList var6 = var2.getElementsByTagNameNS("http://www.w3.org/2000/svg", "glyph");
      int var7 = var6.getLength();
      String[] var8 = new String[var7];
      String[] var9 = new String[var7];
      String[] var10 = new String[var7];
      String[] var11 = new String[var7];
      String[] var12 = new String[var7];
      Element[] var13 = new Element[var7];

      Element var15;
      for(int var14 = 0; var14 < var7; ++var14) {
         var15 = (Element)var6.item(var14);
         var8[var14] = var15.getAttributeNS((String)null, "unicode");
         if (var8[var14].length() > 1 && ArabicTextHandler.arabicChar(var8[var14].charAt(0))) {
            var8[var14] = (new StringBuffer(var8[var14])).reverse().toString();
         }

         var9[var14] = var15.getAttributeNS((String)null, "glyph-name");
         var10[var14] = var15.getAttributeNS((String)null, "lang");
         var11[var14] = var15.getAttributeNS((String)null, "orientation");
         var12[var14] = var15.getAttributeNS((String)null, "arabic-form");
         var13[var14] = var15;
      }

      NodeList var22 = var2.getElementsByTagNameNS("http://www.w3.org/2000/svg", "missing-glyph");
      var15 = null;
      if (var22.getLength() > 0) {
         var15 = (Element)var22.item(0);
      }

      NodeList var16 = var2.getElementsByTagNameNS("http://www.w3.org/2000/svg", "hkern");
      Element[] var17 = new Element[var16.getLength()];

      for(int var18 = 0; var18 < var17.length; ++var18) {
         Element var19 = (Element)var16.item(var18);
         var17[var18] = var19;
      }

      NodeList var23 = var2.getElementsByTagNameNS("http://www.w3.org/2000/svg", "vkern");
      Element[] var24 = new Element[var23.getLength()];

      for(int var20 = 0; var20 < var24.length; ++var20) {
         Element var21 = (Element)var23.item(var20);
         var24[var20] = var21;
      }

      return new SVGGVTFont(var4, var5, var8, var9, var10, var11, var12, var1, var13, var15, var17, var24, var3);
   }
}

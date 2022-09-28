package org.apache.batik.bridge;

import java.util.LinkedList;
import java.util.List;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.FontFaceRule;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.apache.batik.css.engine.value.ValueManager;
import org.apache.batik.gvt.font.GVTFontFamily;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.SVGConstants;

public class CSSFontFace extends FontFace implements SVGConstants {
   GVTFontFamily fontFamily = null;

   public CSSFontFace(List var1, String var2, float var3, String var4, String var5, String var6, String var7, float var8, String var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17) {
      super(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17);
   }

   protected CSSFontFace(String var1) {
      super(var1);
   }

   public static CSSFontFace createCSSFontFace(CSSEngine var0, FontFaceRule var1) {
      StyleMap var2 = var1.getStyleMap();
      String var3 = getStringProp(var2, var0, 21);
      CSSFontFace var4 = new CSSFontFace(var3);
      Value var5 = var2.getValue(27);
      if (var5 != null) {
         var4.fontWeight = var5.getCssText();
      }

      var5 = var2.getValue(25);
      if (var5 != null) {
         var4.fontStyle = var5.getCssText();
      }

      var5 = var2.getValue(26);
      if (var5 != null) {
         var4.fontVariant = var5.getCssText();
      }

      var5 = var2.getValue(24);
      if (var5 != null) {
         var4.fontStretch = var5.getCssText();
      }

      var5 = var2.getValue(41);
      ParsedURL var6 = var1.getURL();
      if (var5 != null && var5 != ValueConstants.NONE_VALUE) {
         if (var5.getCssValueType() == 1) {
            var4.srcs = new LinkedList();
            var4.srcs.add(getSrcValue(var5, var6));
         } else if (var5.getCssValueType() == 2) {
            var4.srcs = new LinkedList();

            for(int var7 = 0; var7 < var5.getLength(); ++var7) {
               var4.srcs.add(getSrcValue(var5.item(var7), var6));
            }
         }
      }

      return var4;
   }

   public static Object getSrcValue(Value var0, ParsedURL var1) {
      if (var0.getCssValueType() != 1) {
         return null;
      } else if (var0.getPrimitiveType() == 20) {
         return var1 != null ? new ParsedURL(var1, var0.getStringValue()) : new ParsedURL(var0.getStringValue());
      } else {
         return var0.getPrimitiveType() == 19 ? var0.getStringValue() : null;
      }
   }

   public static String getStringProp(StyleMap var0, CSSEngine var1, int var2) {
      Value var3 = var0.getValue(var2);
      ValueManager[] var4 = var1.getValueManagers();
      if (var3 == null) {
         ValueManager var5 = var4[var2];
         var3 = var5.getDefaultValue();
      }

      while(var3.getCssValueType() == 2) {
         var3 = var3.item(0);
      }

      return var3.getStringValue();
   }

   public static float getFloatProp(StyleMap var0, CSSEngine var1, int var2) {
      Value var3 = var0.getValue(var2);
      ValueManager[] var4 = var1.getValueManagers();
      if (var3 == null) {
         ValueManager var5 = var4[var2];
         var3 = var5.getDefaultValue();
      }

      while(var3.getCssValueType() == 2) {
         var3 = var3.item(0);
      }

      return var3.getFloatValue();
   }

   public GVTFontFamily getFontFamily(BridgeContext var1) {
      if (this.fontFamily != null) {
         return this.fontFamily;
      } else {
         this.fontFamily = super.getFontFamily(var1);
         return this.fontFamily;
      }
   }
}

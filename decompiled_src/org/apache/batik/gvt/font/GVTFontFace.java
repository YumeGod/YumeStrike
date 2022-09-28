package org.apache.batik.gvt.font;

import org.apache.batik.util.SVGConstants;

public class GVTFontFace implements SVGConstants {
   protected String familyName;
   protected float unitsPerEm;
   protected String fontWeight;
   protected String fontStyle;
   protected String fontVariant;
   protected String fontStretch;
   protected float slope;
   protected String panose1;
   protected float ascent;
   protected float descent;
   protected float strikethroughPosition;
   protected float strikethroughThickness;
   protected float underlinePosition;
   protected float underlineThickness;
   protected float overlinePosition;
   protected float overlineThickness;

   public GVTFontFace(String var1, float var2, String var3, String var4, String var5, String var6, float var7, String var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16) {
      this.familyName = var1;
      this.unitsPerEm = var2;
      this.fontWeight = var3;
      this.fontStyle = var4;
      this.fontVariant = var5;
      this.fontStretch = var6;
      this.slope = var7;
      this.panose1 = var8;
      this.ascent = var9;
      this.descent = var10;
      this.strikethroughPosition = var11;
      this.strikethroughThickness = var12;
      this.underlinePosition = var13;
      this.underlineThickness = var14;
      this.overlinePosition = var15;
      this.overlineThickness = var16;
   }

   public GVTFontFace(String var1) {
      this(var1, 1000.0F, "all", "all", "normal", "normal", 0.0F, "0 0 0 0 0 0 0 0 0 0", 800.0F, 200.0F, 300.0F, 50.0F, -75.0F, 50.0F, 800.0F, 50.0F);
   }

   public String getFamilyName() {
      return this.familyName;
   }

   public boolean hasFamilyName(String var1) {
      String var2 = this.familyName;
      if (var2.length() < var1.length()) {
         return false;
      } else {
         var2 = var2.toLowerCase();
         int var3 = var2.indexOf(var1.toLowerCase());
         if (var3 == -1) {
            return false;
         } else {
            if (var2.length() > var1.length()) {
               boolean var4 = false;
               char var5;
               int var6;
               if (var3 > 0) {
                  var5 = var2.charAt(var3 - 1);
                  label67:
                  switch (var5) {
                     case ' ':
                        var6 = var3 - 2;

                        while(true) {
                           if (var6 < 0) {
                              break label67;
                           }

                           switch (var2.charAt(var6)) {
                              case ' ':
                                 --var6;
                                 break;
                              case '"':
                              case '\'':
                                 var4 = true;
                                 break label67;
                              default:
                                 return false;
                           }
                        }
                     case '"':
                     case '\'':
                        var4 = true;
                     case ',':
                        break;
                     default:
                        return false;
                  }
               }

               if (var3 + var1.length() < var2.length()) {
                  var5 = var2.charAt(var3 + var1.length());
                  switch (var5) {
                     case ' ':
                        var6 = var3 + var1.length() + 1;

                        while(var6 < var2.length()) {
                           switch (var2.charAt(var6)) {
                              case ' ':
                                 ++var6;
                                 break;
                              case '"':
                              case '\'':
                                 if (!var4) {
                                    return false;
                                 }

                                 return true;
                              default:
                                 return false;
                           }
                        }

                        return true;
                     case '"':
                     case '\'':
                        if (!var4) {
                           return false;
                        }
                     case ',':
                        break;
                     default:
                        return false;
                  }
               }
            }

            return true;
         }
      }
   }

   public String getFontWeight() {
      return this.fontWeight;
   }

   public String getFontStyle() {
      return this.fontStyle;
   }

   public float getUnitsPerEm() {
      return this.unitsPerEm;
   }

   public float getAscent() {
      return this.ascent;
   }

   public float getDescent() {
      return this.descent;
   }

   public float getStrikethroughPosition() {
      return this.strikethroughPosition;
   }

   public float getStrikethroughThickness() {
      return this.strikethroughThickness;
   }

   public float getUnderlinePosition() {
      return this.underlinePosition;
   }

   public float getUnderlineThickness() {
      return this.underlineThickness;
   }

   public float getOverlinePosition() {
      return this.overlinePosition;
   }

   public float getOverlineThickness() {
      return this.overlineThickness;
   }
}

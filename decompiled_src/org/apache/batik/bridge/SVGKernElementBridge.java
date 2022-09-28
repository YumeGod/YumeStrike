package org.apache.batik.bridge;

import java.util.ArrayList;
import java.util.StringTokenizer;
import org.apache.batik.gvt.font.Kern;
import org.apache.batik.gvt.font.UnicodeRange;
import org.w3c.dom.Element;

public abstract class SVGKernElementBridge extends AbstractSVGBridge {
   public Kern createKern(BridgeContext var1, Element var2, SVGGVTFont var3) {
      String var4 = var2.getAttributeNS((String)null, "u1");
      String var5 = var2.getAttributeNS((String)null, "u2");
      String var6 = var2.getAttributeNS((String)null, "g1");
      String var7 = var2.getAttributeNS((String)null, "g2");
      String var8 = var2.getAttributeNS((String)null, "k");
      if (var8.length() == 0) {
         var8 = "0";
      }

      float var9 = Float.parseFloat(var8);
      int var10 = 0;
      int var11 = 0;
      int[] var12 = null;
      int[] var13 = null;
      ArrayList var14 = new ArrayList();
      ArrayList var15 = new ArrayList();
      StringTokenizer var16 = new StringTokenizer(var4, ",");

      while(true) {
         String var17;
         int[] var18;
         int var19;
         int[] var20;
         while(var16.hasMoreTokens()) {
            var17 = var16.nextToken();
            if (var17.startsWith("U+")) {
               var14.add(new UnicodeRange(var17));
            } else {
               var18 = var3.getGlyphCodesForUnicode(var17);
               if (var12 == null) {
                  var12 = var18;
                  var10 = var18.length;
               } else {
                  if (var10 + var18.length > var12.length) {
                     var19 = var12.length * 2;
                     if (var19 < var10 + var18.length) {
                        var19 = var10 + var18.length;
                     }

                     var20 = new int[var19];
                     System.arraycopy(var12, 0, var20, 0, var10);
                     var12 = var20;
                  }

                  for(var19 = 0; var19 < var18.length; ++var19) {
                     var12[var10++] = var18[var19];
                  }
               }
            }
         }

         var16 = new StringTokenizer(var5, ",");

         while(true) {
            while(var16.hasMoreTokens()) {
               var17 = var16.nextToken();
               if (var17.startsWith("U+")) {
                  var15.add(new UnicodeRange(var17));
               } else {
                  var18 = var3.getGlyphCodesForUnicode(var17);
                  if (var13 == null) {
                     var13 = var18;
                     var11 = var18.length;
                  } else {
                     if (var11 + var18.length > var13.length) {
                        var19 = var13.length * 2;
                        if (var19 < var11 + var18.length) {
                           var19 = var11 + var18.length;
                        }

                        var20 = new int[var19];
                        System.arraycopy(var13, 0, var20, 0, var11);
                        var13 = var20;
                     }

                     for(var19 = 0; var19 < var18.length; ++var19) {
                        var13[var11++] = var18[var19];
                     }
                  }
               }
            }

            var16 = new StringTokenizer(var6, ",");

            while(true) {
               while(var16.hasMoreTokens()) {
                  var17 = var16.nextToken();
                  var18 = var3.getGlyphCodesForName(var17);
                  if (var12 == null) {
                     var12 = var18;
                     var10 = var18.length;
                  } else {
                     if (var10 + var18.length > var12.length) {
                        var19 = var12.length * 2;
                        if (var19 < var10 + var18.length) {
                           var19 = var10 + var18.length;
                        }

                        var20 = new int[var19];
                        System.arraycopy(var12, 0, var20, 0, var10);
                        var12 = var20;
                     }

                     for(var19 = 0; var19 < var18.length; ++var19) {
                        var12[var10++] = var18[var19];
                     }
                  }
               }

               var16 = new StringTokenizer(var7, ",");

               while(true) {
                  while(var16.hasMoreTokens()) {
                     var17 = var16.nextToken();
                     var18 = var3.getGlyphCodesForName(var17);
                     if (var13 == null) {
                        var13 = var18;
                        var11 = var18.length;
                     } else {
                        if (var11 + var18.length > var13.length) {
                           var19 = var13.length * 2;
                           if (var19 < var11 + var18.length) {
                              var19 = var11 + var18.length;
                           }

                           var20 = new int[var19];
                           System.arraycopy(var13, 0, var20, 0, var11);
                           var13 = var20;
                        }

                        for(var19 = 0; var19 < var18.length; ++var19) {
                           var13[var11++] = var18[var19];
                        }
                     }
                  }

                  int[] var21;
                  if (var10 != 0 && var10 != var12.length) {
                     var21 = new int[var10];
                     System.arraycopy(var12, 0, var21, 0, var10);
                  } else {
                     var21 = var12;
                  }

                  if (var11 != 0 && var11 != var13.length) {
                     var18 = new int[var11];
                     System.arraycopy(var13, 0, var18, 0, var11);
                  } else {
                     var18 = var13;
                  }

                  UnicodeRange[] var23 = new UnicodeRange[var14.size()];
                  var14.toArray(var23);
                  UnicodeRange[] var22 = new UnicodeRange[var15.size()];
                  var15.toArray(var22);
                  return new Kern(var21, var18, var23, var22, var9);
               }
            }
         }
      }
   }
}

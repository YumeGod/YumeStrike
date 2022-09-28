package org.apache.batik.bridge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.FontFaceRule;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.gvt.font.GVTFontFace;
import org.apache.batik.gvt.font.GVTFontFamily;
import org.apache.batik.gvt.font.UnresolvedFontFamily;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class SVGFontUtilities implements SVGConstants {
   public static List getFontFaces(Document var0, BridgeContext var1) {
      Map var2 = var1.getFontFamilyMap();
      List var3 = (List)var2.get(var0);
      if (var3 != null) {
         return var3;
      } else {
         LinkedList var10 = new LinkedList();
         NodeList var4 = var0.getElementsByTagNameNS("http://www.w3.org/2000/svg", "font-face");
         SVGFontFaceElementBridge var5 = (SVGFontFaceElementBridge)var1.getBridge("http://www.w3.org/2000/svg", "font-face");

         for(int var6 = 0; var6 < var4.getLength(); ++var6) {
            Element var7 = (Element)var4.item(var6);
            var10.add(var5.createFontFace(var1, var7));
         }

         CSSEngine var11 = ((SVGOMDocument)var0).getCSSEngine();
         List var12 = var11.getFontFaces();
         Iterator var8 = var12.iterator();

         while(var8.hasNext()) {
            FontFaceRule var9 = (FontFaceRule)var8.next();
            var10.add(CSSFontFace.createCSSFontFace(var11, var9));
         }

         return var10;
      }
   }

   public static GVTFontFamily getFontFamily(Element var0, BridgeContext var1, String var2, String var3, String var4) {
      String var5 = var2.toLowerCase() + " " + var3 + " " + var4;
      Map var6 = var1.getFontFamilyMap();
      GVTFontFamily var7 = (GVTFontFamily)var6.get(var5);
      if (var7 != null) {
         return var7;
      } else {
         Document var8 = var0.getOwnerDocument();
         List var9 = (List)var6.get(var8);
         if (var9 == null) {
            var9 = getFontFaces(var8, var1);
            var6.put(var8, var9);
         }

         Iterator var10 = var9.iterator();
         LinkedList var11 = new LinkedList();

         while(true) {
            FontFace var12;
            String var13;
            do {
               do {
                  if (!var10.hasNext()) {
                     if (var11.size() == 1) {
                        var6.put(var5, var11.get(0));
                        return (GVTFontFamily)var11.get(0);
                     }

                     if (var11.size() <= 1) {
                        UnresolvedFontFamily var27 = new UnresolvedFontFamily(var2);
                        var6.put(var5, var27);
                        return var27;
                     }

                     String var26 = getFontWeightNumberString(var3);
                     ArrayList var28 = new ArrayList(var11.size());
                     Iterator var29 = var11.iterator();

                     while(var29.hasNext()) {
                        GVTFontFace var15 = ((GVTFontFamily)var29.next()).getFontFace();
                        String var16 = var15.getFontWeight();
                        var16 = getFontWeightNumberString(var16);
                        var28.add(var16);
                     }

                     ArrayList var30 = new ArrayList(var28);

                     String var17;
                     int var31;
                     for(var31 = 100; var31 <= 900; var31 += 100) {
                        var17 = String.valueOf(var31);
                        boolean var18 = false;
                        int var19 = 1000;
                        int var20 = 0;

                        for(int var21 = 0; var21 < var28.size(); ++var21) {
                           String var22 = (String)var28.get(var21);
                           if (var22.indexOf(var17) > -1) {
                              var18 = true;
                              break;
                           }

                           StringTokenizer var23 = new StringTokenizer(var22, " ,");

                           while(var23.hasMoreTokens()) {
                              int var24 = Integer.parseInt(var23.nextToken());
                              int var25 = Math.abs(var24 - var31);
                              if (var25 < var19) {
                                 var19 = var25;
                                 var20 = var21;
                              }
                           }
                        }

                        if (!var18) {
                           String var32 = var30.get(var20) + ", " + var17;
                           var30.set(var20, var32);
                        }
                     }

                     for(var31 = 0; var31 < var11.size(); ++var31) {
                        var17 = (String)var30.get(var31);
                        if (var17.indexOf(var26) > -1) {
                           var6.put(var5, var11.get(var31));
                           return (GVTFontFamily)var11.get(var31);
                        }
                     }

                     var6.put(var5, var11.get(0));
                     return (GVTFontFamily)var11.get(0);
                  }

                  var12 = (FontFace)var10.next();
               } while(!var12.hasFamilyName(var2));

               var13 = var12.getFontStyle();
            } while(!var13.equals("all") && var13.indexOf(var4) == -1);

            GVTFontFamily var14 = var12.getFontFamily(var1);
            if (var14 != null) {
               var11.add(var14);
            }
         }
      }
   }

   protected static String getFontWeightNumberString(String var0) {
      if (var0.equals("normal")) {
         return "400";
      } else if (var0.equals("bold")) {
         return "700";
      } else {
         return var0.equals("all") ? "100, 200, 300, 400, 500, 600, 700, 800, 900" : var0;
      }
   }
}

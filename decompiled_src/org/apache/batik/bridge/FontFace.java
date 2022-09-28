package org.apache.batik.bridge;

import java.awt.Font;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.gvt.font.AWTFontFamily;
import org.apache.batik.gvt.font.FontFamilyResolver;
import org.apache.batik.gvt.font.GVTFontFace;
import org.apache.batik.gvt.font.GVTFontFamily;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;

public abstract class FontFace extends GVTFontFace implements ErrorConstants {
   List srcs;

   public FontFace(List var1, String var2, float var3, String var4, String var5, String var6, String var7, float var8, String var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17) {
      super(var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17);
      this.srcs = var1;
   }

   protected FontFace(String var1) {
      super(var1);
   }

   public static CSSFontFace createFontFace(String var0, FontFace var1) {
      return new CSSFontFace(new LinkedList(var1.srcs), var0, var1.unitsPerEm, var1.fontWeight, var1.fontStyle, var1.fontVariant, var1.fontStretch, var1.slope, var1.panose1, var1.ascent, var1.descent, var1.strikethroughPosition, var1.strikethroughThickness, var1.underlinePosition, var1.underlineThickness, var1.overlinePosition, var1.overlineThickness);
   }

   public GVTFontFamily getFontFamily(BridgeContext var1) {
      String var2 = FontFamilyResolver.lookup(this.familyName);
      if (var2 != null) {
         CSSFontFace var10 = createFontFace(var2, this);
         return new AWTFontFamily(var10);
      } else {
         Iterator var3 = this.srcs.iterator();

         String var11;
         label42:
         do {
            while(var3.hasNext()) {
               Object var4 = var3.next();
               if (var4 instanceof String) {
                  var11 = (String)var4;
                  var2 = FontFamilyResolver.lookup(var11);
                  continue label42;
               }

               if (var4 instanceof ParsedURL) {
                  try {
                     GVTFontFamily var5 = this.getFontFamily(var1, (ParsedURL)var4);
                     if (var5 != null) {
                        return var5;
                     }
                  } catch (SecurityException var7) {
                     var1.getUserAgent().displayError(var7);
                  } catch (BridgeException var8) {
                     if ("uri.unsecure".equals(var8.getCode())) {
                        var1.getUserAgent().displayError(var8);
                     }
                  } catch (Exception var9) {
                  }
               }
            }

            return new AWTFontFamily(this);
         } while(var2 == null);

         CSSFontFace var6 = createFontFace(var11, this);
         return new AWTFontFamily(var6);
      }
   }

   protected GVTFontFamily getFontFamily(BridgeContext var1, ParsedURL var2) {
      String var3 = var2.toString();
      Element var4 = this.getBaseElement(var1);
      SVGDocument var5 = (SVGDocument)var4.getOwnerDocument();
      String var6 = var5.getURL();
      ParsedURL var7 = null;
      if (var6 != null) {
         var7 = new ParsedURL(var6);
      }

      String var8 = AbstractNode.getBaseURI(var4);
      var2 = new ParsedURL(var8, var3);
      UserAgent var9 = var1.getUserAgent();

      try {
         var9.checkLoadExternalResource(var2, var7);
      } catch (SecurityException var18) {
         var9.displayError(var18);
         return null;
      }

      if (var2.getRef() == null) {
         try {
            Font var19 = Font.createFont(0, var2.openStream());
            return new AWTFontFamily(this, var19);
         } catch (Exception var17) {
            return null;
         }
      } else {
         Element var10 = var1.getReferencedElement(var4, var3);
         if (var10.getNamespaceURI().equals("http://www.w3.org/2000/svg") && var10.getLocalName().equals("font")) {
            SVGDocument var11 = (SVGDocument)var4.getOwnerDocument();
            SVGDocument var12 = (SVGDocument)var10.getOwnerDocument();
            Element var13 = var10;
            if (var11 != var12) {
               var13 = (Element)var11.importNode(var10, true);
               String var14 = AbstractNode.getBaseURI(var10);
               Element var15 = var11.createElementNS("http://www.w3.org/2000/svg", "g");
               var15.appendChild(var13);
               var15.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:base", var14);
               CSSUtilities.computeStyleAndURIs(var10, var13, var3);
            }

            Element var20 = null;

            for(Node var21 = var13.getFirstChild(); var21 != null; var21 = var21.getNextSibling()) {
               if (var21.getNodeType() == 1 && var21.getNamespaceURI().equals("http://www.w3.org/2000/svg") && var21.getLocalName().equals("font-face")) {
                  var20 = (Element)var21;
                  break;
               }
            }

            SVGFontFaceElementBridge var22 = (SVGFontFaceElementBridge)var1.getBridge("http://www.w3.org/2000/svg", "font-face");
            SVGFontFace var16 = var22.createFontFace(var1, var20);
            return new SVGFontFamily(var16, var13, var1);
         } else {
            return null;
         }
      }
   }

   protected Element getBaseElement(BridgeContext var1) {
      SVGDocument var2 = (SVGDocument)var1.getDocument();
      return var2.getRootElement();
   }
}

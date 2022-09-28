package org.apache.batik.bridge;

import java.text.AttributedCharacterIterator;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.gvt.font.Glyph;
import org.apache.batik.gvt.text.GVTAttributedCharacterIterator;
import org.apache.batik.gvt.text.TextPaintInfo;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SVGAltGlyphElementBridge extends AbstractSVGBridge implements ErrorConstants {
   public static final AttributedCharacterIterator.Attribute PAINT_INFO;

   public String getLocalName() {
      return "altGlyph";
   }

   public Glyph[] createAltGlyphArray(BridgeContext var1, Element var2, float var3, AttributedCharacterIterator var4) {
      String var5 = XLinkSupport.getXLinkHref(var2);
      Element var6 = null;

      try {
         var6 = var1.getReferencedElement(var2, var5);
      } catch (BridgeException var26) {
         if ("uri.unsecure".equals(var26.getCode())) {
            var1.getUserAgent().displayError(var26);
         }
      }

      if (var6 == null) {
         return null;
      } else if (!"http://www.w3.org/2000/svg".equals(var6.getNamespaceURI())) {
         return null;
      } else if (var6.getLocalName().equals("glyph")) {
         Glyph var27 = this.getGlyph(var1, var5, var2, var3, var4);
         if (var27 == null) {
            return null;
         } else {
            Glyph[] var28 = new Glyph[]{var27};
            return var28;
         }
      } else {
         if (var6.getLocalName().equals("altGlyphDef")) {
            SVGOMDocument var7 = (SVGOMDocument)var2.getOwnerDocument();
            SVGOMDocument var8 = (SVGOMDocument)var6.getOwnerDocument();
            boolean var9 = var8 == var7;
            Element var10 = var9 ? var6 : (Element)var7.importNode(var6, true);
            if (!var9) {
               String var11 = AbstractNode.getBaseURI(var2);
               Element var12 = var7.createElementNS("http://www.w3.org/2000/svg", "g");
               var12.appendChild(var10);
               var12.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:base", var11);
               CSSUtilities.computeStyleAndURIs(var6, var10, var5);
            }

            NodeList var29 = var10.getChildNodes();
            boolean var30 = false;
            int var13 = var29.getLength();

            for(int var14 = 0; var14 < var13; ++var14) {
               Node var15 = var29.item(var14);
               if (var15.getNodeType() == 1) {
                  Element var16 = (Element)var15;
                  if ("http://www.w3.org/2000/svg".equals(var16.getNamespaceURI()) && "glyphRef".equals(var16.getLocalName())) {
                     var30 = true;
                     break;
                  }
               }
            }

            NodeList var31;
            int var32;
            if (var30) {
               var31 = var10.getElementsByTagNameNS("http://www.w3.org/2000/svg", "glyphRef");
               var32 = var31.getLength();
               Glyph[] var34 = new Glyph[var32];

               for(int var35 = 0; var35 < var32; ++var35) {
                  Element var36 = (Element)var31.item(var35);
                  String var37 = XLinkSupport.getXLinkHref(var36);
                  Glyph var38 = this.getGlyph(var1, var37, var36, var3, var4);
                  if (var38 == null) {
                     return null;
                  }

                  var34[var35] = var38;
               }

               return var34;
            }

            var31 = var10.getElementsByTagNameNS("http://www.w3.org/2000/svg", "altGlyphItem");
            var32 = var31.getLength();
            if (var32 > 0) {
               boolean var33 = false;
               Glyph[] var17 = null;

               for(int var18 = 0; var18 < var32 && !var33; ++var18) {
                  Element var19 = (Element)var31.item(var18);
                  NodeList var20 = var19.getElementsByTagNameNS("http://www.w3.org/2000/svg", "glyphRef");
                  int var21 = var20.getLength();
                  var17 = new Glyph[var21];
                  var33 = true;

                  for(int var22 = 0; var22 < var21; ++var22) {
                     Element var23 = (Element)var20.item(var22);
                     String var24 = XLinkSupport.getXLinkHref(var23);
                     Glyph var25 = this.getGlyph(var1, var24, var23, var3, var4);
                     if (var25 == null) {
                        var33 = false;
                        break;
                     }

                     var17[var22] = var25;
                  }
               }

               if (!var33) {
                  return null;
               }

               return var17;
            }
         }

         return null;
      }
   }

   private Glyph getGlyph(BridgeContext var1, String var2, Element var3, float var4, AttributedCharacterIterator var5) {
      Element var6 = null;

      try {
         var6 = var1.getReferencedElement(var3, var2);
      } catch (BridgeException var19) {
         if ("uri.unsecure".equals(var19.getCode())) {
            var1.getUserAgent().displayError(var19);
         }
      }

      if (var6 != null && "http://www.w3.org/2000/svg".equals(var6.getNamespaceURI()) && "glyph".equals(var6.getLocalName())) {
         SVGOMDocument var7 = (SVGOMDocument)var3.getOwnerDocument();
         SVGOMDocument var8 = (SVGOMDocument)var6.getOwnerDocument();
         boolean var9 = var8 == var7;
         Element var10 = null;
         Element var11 = null;
         Element var12 = null;
         if (var9) {
            var10 = var6;
            var12 = (Element)var6.getParentNode();
            NodeList var13 = var12.getElementsByTagNameNS("http://www.w3.org/2000/svg", "font-face");
            if (var13.getLength() > 0) {
               var11 = (Element)var13.item(0);
            }
         } else {
            var12 = (Element)var7.importNode(var6.getParentNode(), true);
            String var20 = AbstractNode.getBaseURI(var3);
            Element var14 = var7.createElementNS("http://www.w3.org/2000/svg", "g");
            var14.appendChild(var12);
            var14.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:base", var20);
            CSSUtilities.computeStyleAndURIs((Element)var6.getParentNode(), var12, var2);
            String var15 = var6.getAttributeNS((String)null, "id");
            NodeList var16 = var12.getElementsByTagNameNS("http://www.w3.org/2000/svg", "glyph");

            for(int var17 = 0; var17 < var16.getLength(); ++var17) {
               Element var18 = (Element)var16.item(var17);
               if (var18.getAttributeNS((String)null, "id").equals(var15)) {
                  var10 = var18;
                  break;
               }
            }

            NodeList var25 = var12.getElementsByTagNameNS("http://www.w3.org/2000/svg", "font-face");
            if (var25.getLength() > 0) {
               var11 = (Element)var25.item(0);
            }
         }

         if (var10 != null && var11 != null) {
            SVGFontFaceElementBridge var21 = (SVGFontFaceElementBridge)var1.getBridge(var11);
            SVGFontFace var22 = var21.createFontFace(var1, var11);
            SVGGlyphElementBridge var23 = (SVGGlyphElementBridge)var1.getBridge(var10);
            var5.first();
            TextPaintInfo var24 = (TextPaintInfo)var5.getAttribute(PAINT_INFO);
            return var23.createGlyph(var1, var10, var3, -1, var4, var22, var24);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   static {
      PAINT_INFO = GVTAttributedCharacterIterator.TextAttribute.PAINT_INFO;
   }
}

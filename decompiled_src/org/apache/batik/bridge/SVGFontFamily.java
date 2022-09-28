package org.apache.batik.bridge;

import java.lang.ref.SoftReference;
import java.text.AttributedCharacterIterator;
import java.util.Map;
import org.apache.batik.gvt.font.GVTFont;
import org.apache.batik.gvt.font.GVTFontFace;
import org.apache.batik.gvt.font.GVTFontFamily;
import org.apache.batik.gvt.text.GVTAttributedCharacterIterator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SVGFontFamily implements GVTFontFamily {
   public static final AttributedCharacterIterator.Attribute TEXT_COMPOUND_ID;
   protected GVTFontFace fontFace;
   protected Element fontElement;
   protected BridgeContext ctx;
   protected Boolean complex = null;

   public SVGFontFamily(GVTFontFace var1, Element var2, BridgeContext var3) {
      this.fontFace = var1;
      this.fontElement = var2;
      this.ctx = var3;
   }

   public String getFamilyName() {
      return this.fontFace.getFamilyName();
   }

   public GVTFontFace getFontFace() {
      return this.fontFace;
   }

   public GVTFont deriveFont(float var1, AttributedCharacterIterator var2) {
      return this.deriveFont(var1, var2.getAttributes());
   }

   public GVTFont deriveFont(float var1, Map var2) {
      SVGFontElementBridge var3 = (SVGFontElementBridge)this.ctx.getBridge(this.fontElement);
      SoftReference var4 = (SoftReference)var2.get(TEXT_COMPOUND_ID);
      Element var5 = (Element)var4.get();
      return var3.createFont(this.ctx, this.fontElement, var5, var1, this.fontFace);
   }

   public boolean isComplex() {
      if (this.complex != null) {
         return this.complex;
      } else {
         boolean var1 = isComplex(this.fontElement, this.ctx);
         this.complex = var1 ? Boolean.TRUE : Boolean.FALSE;
         return var1;
      }
   }

   public static boolean isComplex(Element var0, BridgeContext var1) {
      NodeList var2 = var0.getElementsByTagNameNS("http://www.w3.org/2000/svg", "glyph");
      int var3 = var2.getLength();

      for(int var4 = 0; var4 < var3; ++var4) {
         Element var5 = (Element)var2.item(var4);

         for(Node var6 = var5.getFirstChild(); var6 != null; var6 = var6.getNextSibling()) {
            if (var6.getNodeType() == 1) {
               Element var7 = (Element)var6;
               Bridge var8 = var1.getBridge(var7);
               if (var8 != null && var8 instanceof GraphicsNodeBridge) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   static {
      TEXT_COMPOUND_ID = GVTAttributedCharacterIterator.TextAttribute.TEXT_COMPOUND_ID;
   }
}

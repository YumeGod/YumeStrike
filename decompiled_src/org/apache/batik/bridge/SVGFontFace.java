package org.apache.batik.bridge;

import java.util.List;
import org.apache.batik.gvt.font.GVTFontFamily;
import org.w3c.dom.Element;

public class SVGFontFace extends FontFace {
   Element fontFaceElement;
   GVTFontFamily fontFamily = null;

   public SVGFontFace(Element var1, List var2, String var3, float var4, String var5, String var6, String var7, String var8, float var9, String var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18) {
      super(var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18);
      this.fontFaceElement = var1;
   }

   public GVTFontFamily getFontFamily(BridgeContext var1) {
      if (this.fontFamily != null) {
         return this.fontFamily;
      } else {
         Element var2 = SVGUtilities.getParentElement(this.fontFaceElement);
         if (var2.getNamespaceURI().equals("http://www.w3.org/2000/svg") && var2.getLocalName().equals("font")) {
            return new SVGFontFamily(this, var2, var1);
         } else {
            this.fontFamily = super.getFontFamily(var1);
            return this.fontFamily;
         }
      }
   }

   public Element getFontFaceElement() {
      return this.fontFaceElement;
   }

   protected Element getBaseElement(BridgeContext var1) {
      return this.fontFaceElement != null ? this.fontFaceElement : super.getBaseElement(var1);
   }
}

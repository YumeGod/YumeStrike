package org.apache.batik.bridge;

import java.awt.font.FontRenderContext;
import java.text.AttributedCharacterIterator;
import org.apache.batik.gvt.font.AltGlyphHandler;
import org.apache.batik.gvt.font.GVTFont;
import org.apache.batik.gvt.font.GVTGlyphVector;
import org.apache.batik.gvt.font.Glyph;
import org.apache.batik.gvt.font.SVGGVTGlyphVector;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Element;

public class SVGAltGlyphHandler implements AltGlyphHandler, SVGConstants {
   private BridgeContext ctx;
   private Element textElement;

   public SVGAltGlyphHandler(BridgeContext var1, Element var2) {
      this.ctx = var1;
      this.textElement = var2;
   }

   public GVTGlyphVector createGlyphVector(FontRenderContext var1, float var2, AttributedCharacterIterator var3) {
      try {
         if ("http://www.w3.org/2000/svg".equals(this.textElement.getNamespaceURI()) && "altGlyph".equals(this.textElement.getLocalName())) {
            SVGAltGlyphElementBridge var4 = (SVGAltGlyphElementBridge)this.ctx.getBridge(this.textElement);
            Glyph[] var5 = var4.createAltGlyphArray(this.ctx, this.textElement, var2, var3);
            if (var5 != null) {
               return new SVGGVTGlyphVector((GVTFont)null, var5, var1);
            }
         }

         return null;
      } catch (SecurityException var6) {
         this.ctx.getUserAgent().displayError(var6);
         throw var6;
      }
   }
}

package org.apache.fop.render;

import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;
import org.apache.fop.apps.FOPException;
import org.apache.fop.area.Area;
import org.apache.fop.area.Trait;
import org.apache.fop.fonts.CustomFontCollection;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontCollection;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontManager;
import org.apache.fop.fonts.FontResolver;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.fonts.base14.Base14FontCollection;
import org.w3c.dom.Document;

public abstract class PrintRenderer extends AbstractRenderer {
   protected FontInfo fontInfo;
   protected FontResolver fontResolver = null;
   protected List embedFontInfoList = null;

   public void addFontList(List fontList) {
      if (this.embedFontInfoList == null) {
         this.setFontList(fontList);
      } else {
         this.embedFontInfoList.addAll(fontList);
      }

   }

   public void setFontList(List embedFontInfoList) {
      this.embedFontInfoList = embedFontInfoList;
   }

   public List getFontList() {
      return this.embedFontInfoList;
   }

   public void setupFontInfo(FontInfo inFontInfo) throws FOPException {
      this.fontInfo = inFontInfo;
      FontManager fontManager = this.userAgent.getFactory().getFontManager();
      FontCollection[] fontCollections = new FontCollection[]{new Base14FontCollection(fontManager.isBase14KerningEnabled()), new CustomFontCollection(this.getFontResolver(), this.getFontList())};
      fontManager.setup(this.getFontInfo(), fontCollections);
   }

   protected String getInternalFontNameForArea(Area area) {
      FontTriplet triplet = (FontTriplet)area.getTrait(Trait.FONT);
      String key = this.fontInfo.getInternalFontKey(triplet);
      if (key == null) {
         triplet = new FontTriplet("any", "normal", 400);
         key = this.fontInfo.getInternalFontKey(triplet);
      }

      return key;
   }

   protected Font getFontFromArea(Area area) {
      FontTriplet triplet = (FontTriplet)area.getTrait(Trait.FONT);
      int size = (Integer)area.getTrait(Trait.FONT_SIZE);
      return this.fontInfo.getFontInstance(triplet, size);
   }

   protected RendererContext instantiateRendererContext() {
      return new RendererContext(this, this.getMimeType());
   }

   protected RendererContext createRendererContext(int x, int y, int width, int height, Map foreignAttributes) {
      RendererContext context = this.instantiateRendererContext();
      context.setUserAgent(this.userAgent);
      context.setProperty("width", new Integer(width));
      context.setProperty("height", new Integer(height));
      context.setProperty("xpos", new Integer(x));
      context.setProperty("ypos", new Integer(y));
      context.setProperty("pageViewport", this.getCurrentPageViewport());
      if (foreignAttributes != null) {
         context.setProperty("foreign-attributes", foreignAttributes);
      }

      return context;
   }

   public void renderDocument(Document doc, String ns, Rectangle2D pos, Map foreignAttributes) {
      int x = this.currentIPPosition + (int)pos.getX();
      int y = this.currentBPPosition + (int)pos.getY();
      int width = (int)pos.getWidth();
      int height = (int)pos.getHeight();
      RendererContext context = this.createRendererContext(x, y, width, height, foreignAttributes);
      this.renderXML(context, doc, ns);
   }

   public FontResolver getFontResolver() {
      if (this.fontResolver == null) {
         this.fontResolver = new DefaultFontResolver(super.userAgent);
      }

      return this.fontResolver;
   }

   public FontInfo getFontInfo() {
      return this.fontInfo;
   }
}

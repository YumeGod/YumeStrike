package org.apache.fop.fonts;

import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.fonts.substitute.FontSubstitutions;

public class FontManager {
   public static final boolean DEFAULT_USE_CACHE = true;
   private String fontBase = null;
   private FontCache fontCache = null;
   private FontSubstitutions fontSubstitutions = null;
   private boolean enableBase14Kerning = false;
   private FontTriplet.Matcher referencedFontsMatcher;

   public FontManager() {
      this.setUseCache(true);
   }

   public void setFontBaseURL(String fontBase) throws MalformedURLException {
      this.fontBase = fontBase;
   }

   public String getFontBaseURL() {
      return this.fontBase;
   }

   public boolean isBase14KerningEnabled() {
      return this.enableBase14Kerning;
   }

   public void setBase14KerningEnabled(boolean value) {
      this.enableBase14Kerning = value;
   }

   public void setFontSubstitutions(FontSubstitutions substitutions) {
      this.fontSubstitutions = substitutions;
   }

   protected FontSubstitutions getFontSubstitutions() {
      if (this.fontSubstitutions == null) {
         this.fontSubstitutions = new FontSubstitutions();
      }

      return this.fontSubstitutions;
   }

   public void setUseCache(boolean useCache) {
      if (useCache) {
         this.fontCache = FontCache.load();
         if (this.fontCache == null) {
            this.fontCache = new FontCache();
         }
      } else {
         this.fontCache = null;
      }

   }

   public boolean useCache() {
      return this.fontCache != null;
   }

   public FontCache getFontCache() {
      return this.fontCache;
   }

   public void setup(FontInfo fontInfo, FontCollection[] fontCollections) {
      int startNum = 1;
      int i = 0;

      for(int c = fontCollections.length; i < c; ++i) {
         startNum = fontCollections[i].setup(startNum, fontInfo);
      }

      this.getFontSubstitutions().adjustFontInfo(fontInfo);
   }

   public static FontResolver createMinimalFontResolver() {
      return new FontResolver() {
         public Source resolve(String href) {
            return new StreamSource(href);
         }
      };
   }

   public void setReferencedFontsMatcher(FontTriplet.Matcher matcher) {
      this.referencedFontsMatcher = matcher;
   }

   public FontTriplet.Matcher getReferencedFontsMatcher() {
      return this.referencedFontsMatcher;
   }

   public void updateReferencedFonts(List fontInfoList) {
      FontTriplet.Matcher matcher = this.getReferencedFontsMatcher();
      this.updateReferencedFonts(fontInfoList, matcher);
   }

   public void updateReferencedFonts(List fontInfoList, FontTriplet.Matcher matcher) {
      if (matcher != null) {
         Iterator iter = fontInfoList.iterator();

         while(true) {
            while(iter.hasNext()) {
               EmbedFontInfo fontInfo = (EmbedFontInfo)iter.next();
               Iterator triplets = fontInfo.getFontTriplets().iterator();

               while(triplets.hasNext()) {
                  FontTriplet triplet = (FontTriplet)triplets.next();
                  if (matcher.matches(triplet)) {
                     fontInfo.setEmbedded(false);
                     break;
                  }
               }
            }

            return;
         }
      }
   }
}

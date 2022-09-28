package org.apache.fop.render.java2d;

import java.util.List;
import javax.xml.transform.Source;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fonts.CustomFont;
import org.apache.fop.fonts.EmbedFontInfo;
import org.apache.fop.fonts.EncodingMode;
import org.apache.fop.fonts.FontCollection;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontLoader;
import org.apache.fop.fonts.FontManager;
import org.apache.fop.fonts.FontResolver;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.fonts.LazyFont;

public class ConfiguredFontCollection implements FontCollection {
   private static Log log;
   private FontResolver fontResolver;
   private List embedFontInfoList;

   public ConfiguredFontCollection(FontResolver fontResolver, List customFonts) {
      this.fontResolver = fontResolver;
      if (this.fontResolver == null) {
         this.fontResolver = FontManager.createMinimalFontResolver();
      }

      this.embedFontInfoList = customFonts;
   }

   public int setup(int start, FontInfo fontInfo) {
      int num = start;
      if (this.embedFontInfoList != null && this.embedFontInfoList.size() >= 1) {
         String internalName = null;

         for(int i = 0; i < this.embedFontInfoList.size(); ++i) {
            EmbedFontInfo configFontInfo = (EmbedFontInfo)this.embedFontInfoList.get(i);
            String fontFile = configFontInfo.getEmbedFile();
            internalName = "F" + num;
            ++num;

            try {
               FontMetricsMapper font = null;
               String metricsUrl = configFontInfo.getMetricsFile();
               if (metricsUrl != null) {
                  LazyFont fontMetrics = new LazyFont(configFontInfo, this.fontResolver);
                  Source fontSource = this.fontResolver.resolve(configFontInfo.getEmbedFile());
                  font = new CustomFontMetricsMapper(fontMetrics, fontSource);
               } else {
                  CustomFont fontMetrics = FontLoader.loadFont(fontFile, (String)null, true, EncodingMode.AUTO, configFontInfo.getKerning(), this.fontResolver);
                  font = new CustomFontMetricsMapper(fontMetrics);
               }

               fontInfo.addMetrics(internalName, font);
               List triplets = configFontInfo.getFontTriplets();

               for(int c = 0; c < triplets.size(); ++c) {
                  FontTriplet triplet = (FontTriplet)triplets.get(c);
                  if (log.isDebugEnabled()) {
                     log.debug("Registering: " + triplet + " under " + internalName);
                  }

                  fontInfo.addFontProperties(internalName, triplet);
               }
            } catch (Exception var13) {
               log.warn("Unable to load custom font from file '" + fontFile + "'", var13);
            }
         }

         return num;
      } else {
         log.debug("No user configured fonts found.");
         return start;
      }
   }

   static {
      log = LogFactory.getLog(ConfiguredFontCollection.class);
   }
}

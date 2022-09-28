package org.apache.fop.fonts;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fonts.autodetect.FontFileFinder;
import org.apache.fop.fonts.autodetect.FontInfoFinder;
import org.apache.fop.util.LogUtil;

public class FontInfoConfigurator {
   protected static Log log;
   private Configuration cfg;
   private FontManager fontManager;
   private FontResolver fontResolver;
   private FontEventListener listener;
   private boolean strict;

   public FontInfoConfigurator(Configuration cfg, FontManager fontManager, FontResolver fontResolver, FontEventListener listener, boolean strict) {
      this.cfg = cfg;
      this.fontManager = fontManager;
      this.fontResolver = fontResolver;
      this.listener = listener;
      this.strict = strict;
   }

   public void configure(List fontInfoList) throws FOPException {
      Configuration fonts = this.cfg.getChild("fonts", false);
      if (fonts != null) {
         long start = 0L;
         if (log.isDebugEnabled()) {
            log.debug("Starting font configuration...");
            start = System.currentTimeMillis();
         }

         FontAdder fontAdder = new FontAdder(this.fontManager, this.fontResolver, this.listener);
         boolean autodetectFonts = fonts.getChild("auto-detect", false) != null;
         if (autodetectFonts) {
            FontDetector fontDetector = new FontDetector(this.fontManager, fontAdder, this.strict);
            fontDetector.detect(fontInfoList);
         }

         this.addDirectories(fonts, fontAdder, fontInfoList);
         FontCache fontCache = this.fontManager.getFontCache();
         this.addFonts(fonts, fontCache, fontInfoList);
         this.fontManager.updateReferencedFonts(fontInfoList);
         Configuration referencedFontsCfg = fonts.getChild("referenced-fonts", false);
         if (referencedFontsCfg != null) {
            FontTriplet.Matcher matcher = FontManagerConfigurator.createFontsMatcher(referencedFontsCfg, this.strict);
            this.fontManager.updateReferencedFonts(fontInfoList, matcher);
         }

         if (fontCache != null && fontCache.hasChanged()) {
            fontCache.save();
         }

         if (log.isDebugEnabled()) {
            log.debug("Finished font configuration in " + (System.currentTimeMillis() - start) + "ms");
         }
      }

   }

   private void addDirectories(Configuration fontsCfg, FontAdder fontAdder, List fontInfoList) throws FOPException {
      Configuration[] directories = fontsCfg.getChildren("directory");

      for(int i = 0; i < directories.length; ++i) {
         boolean recursive = directories[i].getAttributeAsBoolean("recursive", false);
         String directory = null;

         try {
            directory = directories[i].getValue();
         } catch (ConfigurationException var12) {
            LogUtil.handleException(log, var12, this.strict);
            continue;
         }

         if (directory == null) {
            LogUtil.handleException(log, new FOPException("directory defined without value"), this.strict);
         } else {
            FontFileFinder fontFileFinder = new FontFileFinder(recursive ? -1 : 1);

            try {
               List fontURLList = fontFileFinder.find(directory);
               fontAdder.add(fontURLList, fontInfoList);
            } catch (IOException var11) {
               LogUtil.handleException(log, var11, this.strict);
            }
         }
      }

   }

   protected void addFonts(Configuration fontsCfg, FontCache fontCache, List fontInfoList) throws FOPException {
      Configuration[] font = fontsCfg.getChildren("font");

      for(int i = 0; i < font.length; ++i) {
         EmbedFontInfo embedFontInfo = this.getFontInfo(font[i], fontCache);
         if (embedFontInfo != null) {
            fontInfoList.add(embedFontInfo);
         }
      }

   }

   private static void closeSource(Source src) {
      if (src instanceof StreamSource) {
         StreamSource streamSource = (StreamSource)src;
         IOUtils.closeQuietly(streamSource.getInputStream());
         IOUtils.closeQuietly(streamSource.getReader());
      }

   }

   protected EmbedFontInfo getFontInfo(Configuration fontCfg, FontCache fontCache) throws FOPException {
      String metricsUrl = fontCfg.getAttribute("metrics-url", (String)null);
      String embedUrl = fontCfg.getAttribute("embed-url", (String)null);
      String subFont = fontCfg.getAttribute("sub-font", (String)null);
      if (metricsUrl == null && embedUrl == null) {
         LogUtil.handleError(log, "Font configuration without metric-url or embed-url attribute", this.strict);
         return null;
      } else {
         if (this.strict) {
            Source source;
            if (embedUrl != null) {
               source = this.fontResolver.resolve(embedUrl);
               closeSource(source);
               if (source == null) {
                  LogUtil.handleError(log, "Failed to resolve font with embed-url '" + embedUrl + "'", this.strict);
                  return null;
               }
            }

            if (metricsUrl != null) {
               source = this.fontResolver.resolve(metricsUrl);
               closeSource(source);
               if (source == null) {
                  LogUtil.handleError(log, "Failed to resolve font with metric-url '" + metricsUrl + "'", this.strict);
                  return null;
               }
            }
         }

         Configuration[] tripletCfg = fontCfg.getChildren("font-triplet");
         if (tripletCfg.length == 0) {
            LogUtil.handleError(log, "font without font-triplet", this.strict);
            File fontFile = FontCache.getFileFromUrls(new String[]{embedUrl, metricsUrl});

            URL fontUrl;
            try {
               fontUrl = fontFile.toURI().toURL();
            } catch (MalformedURLException var14) {
               log.debug("Malformed Url: " + var14.getMessage());
               return null;
            }

            if (fontFile != null) {
               FontInfoFinder finder = new FontInfoFinder();
               finder.setEventListener(this.listener);
               EmbedFontInfo[] infos = finder.find(fontUrl, this.fontResolver, fontCache);
               return infos[0];
            } else {
               return null;
            }
         } else {
            List tripletList = new ArrayList();

            for(int j = 0; j < tripletCfg.length; ++j) {
               FontTriplet fontTriplet = this.getFontTriplet(tripletCfg[j]);
               tripletList.add(fontTriplet);
            }

            boolean useKerning = fontCfg.getAttributeAsBoolean("kerning", true);
            EncodingMode encodingMode = EncodingMode.valueOf(fontCfg.getAttribute("encoding-mode", EncodingMode.AUTO.getName()));
            EmbedFontInfo embedFontInfo = new EmbedFontInfo(metricsUrl, useKerning, tripletList, embedUrl, subFont);
            embedFontInfo.setEncodingMode(encodingMode);
            if (fontCache != null && !fontCache.containsFont(embedFontInfo)) {
               fontCache.addFont(embedFontInfo);
            }

            if (log.isDebugEnabled()) {
               String embedFile = embedFontInfo.getEmbedFile();
               log.debug("Adding font " + (embedFile != null ? embedFile + ", " : "") + "metric file " + embedFontInfo.getMetricsFile());

               for(int j = 0; j < tripletList.size(); ++j) {
                  FontTriplet triplet = (FontTriplet)tripletList.get(j);
                  log.debug("  Font triplet " + triplet.getName() + ", " + triplet.getStyle() + ", " + triplet.getWeight());
               }
            }

            return embedFontInfo;
         }
      }
   }

   private FontTriplet getFontTriplet(Configuration tripletCfg) throws FOPException {
      try {
         String name = tripletCfg.getAttribute("name");
         if (name == null) {
            LogUtil.handleError(log, "font-triplet without name", this.strict);
            return null;
         } else {
            String weightStr = tripletCfg.getAttribute("weight");
            if (weightStr == null) {
               LogUtil.handleError(log, "font-triplet without weight", this.strict);
               return null;
            } else {
               int weight = FontUtil.parseCSS2FontWeight(FontUtil.stripWhiteSpace(weightStr));
               String style = tripletCfg.getAttribute("style");
               if (style == null) {
                  LogUtil.handleError(log, "font-triplet without style", this.strict);
                  return null;
               } else {
                  style = FontUtil.stripWhiteSpace(style);
                  return FontInfo.createFontKey(name, style, weight);
               }
            }
         }
      } catch (ConfigurationException var6) {
         LogUtil.handleException(log, var6, this.strict);
         return null;
      }
   }

   static {
      log = LogFactory.getLog(FontInfoConfigurator.class);
   }
}

package org.apache.fop.fonts.autodetect;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fonts.CustomFont;
import org.apache.fop.fonts.EmbedFontInfo;
import org.apache.fop.fonts.EncodingMode;
import org.apache.fop.fonts.FontCache;
import org.apache.fop.fonts.FontEventListener;
import org.apache.fop.fonts.FontLoader;
import org.apache.fop.fonts.FontResolver;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.fonts.FontUtil;
import org.apache.fop.fonts.MultiByteFont;
import org.apache.fop.fonts.truetype.FontFileReader;
import org.apache.fop.fonts.truetype.TTFFile;
import org.apache.fop.fonts.truetype.TTFFontLoader;

public class FontInfoFinder {
   private Log log;
   private FontEventListener eventListener;
   private final Pattern quotePattern;

   public FontInfoFinder() {
      this.log = LogFactory.getLog(FontInfoFinder.class);
      this.quotePattern = Pattern.compile("'");
   }

   public void setEventListener(FontEventListener listener) {
      this.eventListener = listener;
   }

   private void generateTripletsFromFont(CustomFont customFont, Collection triplets) {
      if (this.log.isTraceEnabled()) {
         this.log.trace("Font: " + customFont.getFullName() + ", family: " + customFont.getFamilyNames() + ", PS: " + customFont.getFontName() + ", EmbedName: " + customFont.getEmbedFontName());
      }

      String strippedName = this.stripQuotes(customFont.getStrippedFontName());
      String fullName = this.stripQuotes(customFont.getFullName());
      String searchName = fullName.toLowerCase();
      String style = this.guessStyle(customFont, searchName);
      int guessedWeight = FontUtil.guessWeight(searchName);
      int weight = guessedWeight;
      triplets.add(new FontTriplet(fullName, "normal", 400));
      if (!fullName.equals(strippedName)) {
         triplets.add(new FontTriplet(strippedName, "normal", 400));
      }

      Set familyNames = customFont.getFamilyNames();
      Iterator iter = familyNames.iterator();

      while(iter.hasNext()) {
         String familyName = this.stripQuotes((String)iter.next());
         if (!fullName.equals(familyName)) {
            int priority = fullName.startsWith(familyName) ? fullName.length() - familyName.length() : fullName.length();
            triplets.add(new FontTriplet(familyName, style, weight, priority));
         }
      }

   }

   private String stripQuotes(String name) {
      return this.quotePattern.matcher(name).replaceAll("");
   }

   private String guessStyle(CustomFont customFont, String fontName) {
      String style = "normal";
      if (customFont.getItalicAngle() > 0) {
         style = "italic";
      } else {
         style = FontUtil.guessStyle(fontName);
      }

      return style;
   }

   private EmbedFontInfo getFontInfoFromCustomFont(URL fontUrl, CustomFont customFont, FontCache fontCache) {
      List fontTripletList = new ArrayList();
      this.generateTripletsFromFont(customFont, fontTripletList);
      String embedUrl = fontUrl.toExternalForm();
      String subFontName = null;
      if (customFont instanceof MultiByteFont) {
         subFontName = ((MultiByteFont)customFont).getTTCName();
      }

      EmbedFontInfo fontInfo = new EmbedFontInfo((String)null, customFont.isKerningEnabled(), fontTripletList, embedUrl, subFontName);
      fontInfo.setPostScriptName(customFont.getFontName());
      if (fontCache != null) {
         fontCache.addFont(fontInfo);
      }

      return fontInfo;
   }

   public EmbedFontInfo[] find(URL fontUrl, FontResolver resolver, FontCache fontCache) {
      String embedUrl = null;
      embedUrl = fontUrl.toExternalForm();
      long fileLastModified = -1L;
      EmbedFontInfo[] fontInfos;
      if (fontCache != null) {
         fileLastModified = FontCache.getLastModified(fontUrl);
         if (fontCache.containsFont(embedUrl)) {
            fontInfos = fontCache.getFontInfos(embedUrl, fileLastModified);
            if (fontInfos != null) {
               return fontInfos;
            }
         } else if (fontCache.isFailedFont(embedUrl, fileLastModified)) {
            if (this.log.isDebugEnabled()) {
               this.log.debug("Skipping font file that failed to load previously: " + embedUrl);
            }

            return null;
         }
      }

      fontInfos = null;
      EmbedFontInfo fi;
      CustomFont customFont;
      if (fontUrl.toExternalForm().endsWith(".ttc")) {
         fi = null;
         String fontFileURI = fontUrl.toExternalForm().trim();
         InputStream in = null;

         List ttcNames;
         label189: {
            FontFileReader reader;
            try {
               try {
                  in = FontLoader.openFontUri(resolver, fontFileURI);
                  TTFFile ttf = new TTFFile();
                  reader = new FontFileReader(in);
                  ttcNames = ttf.getTTCnames(reader);
                  break label189;
               } catch (Exception var20) {
                  if (this.eventListener != null) {
                     this.eventListener.fontLoadingErrorAtAutoDetection(this, fontFileURI, var20);
                  }
               }

               reader = null;
            } finally {
               IOUtils.closeQuietly(in);
            }

            return reader;
         }

         ArrayList embedFontInfoList = new ArrayList();
         Iterator ttcNamesIterator = ttcNames.iterator();

         while(true) {
            while(true) {
               if (!ttcNamesIterator.hasNext()) {
                  return (EmbedFontInfo[])embedFontInfoList.toArray(new EmbedFontInfo[embedFontInfoList.size()]);
               }

               String fontName = (String)ttcNamesIterator.next();
               if (this.log.isDebugEnabled()) {
                  this.log.debug("Loading " + fontName);
               }

               try {
                  TTFFontLoader ttfLoader = new TTFFontLoader(fontFileURI, fontName, true, EncodingMode.AUTO, true, resolver);
                  customFont = ttfLoader.getFont();
                  if (this.eventListener != null) {
                     customFont.setEventListener(this.eventListener);
                  }
                  break;
               } catch (Exception var19) {
                  if (fontCache != null) {
                     fontCache.registerFailedFont(embedUrl, fileLastModified);
                  }

                  if (this.eventListener != null) {
                     this.eventListener.fontLoadingErrorAtAutoDetection(this, embedUrl, var19);
                  }
               }
            }

            EmbedFontInfo fi = this.getFontInfoFromCustomFont(fontUrl, customFont, fontCache);
            if (fi != null) {
               embedFontInfoList.add(fi);
            }
         }
      } else {
         try {
            customFont = FontLoader.loadFont((URL)fontUrl, (String)null, true, EncodingMode.AUTO, resolver);
            if (this.eventListener != null) {
               customFont.setEventListener(this.eventListener);
            }
         } catch (Exception var22) {
            if (fontCache != null) {
               fontCache.registerFailedFont(embedUrl, fileLastModified);
            }

            if (this.eventListener != null) {
               this.eventListener.fontLoadingErrorAtAutoDetection(this, embedUrl, var22);
            }

            return null;
         }

         fi = this.getFontInfoFromCustomFont(fontUrl, customFont, fontCache);
         return fi != null ? new EmbedFontInfo[]{fi} : null;
      }
   }
}

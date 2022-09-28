package org.apache.fop.fonts;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.xml.sax.InputSource;

public class LazyFont extends Typeface implements FontDescriptor {
   private static Log log;
   private String metricsFileName = null;
   private String fontEmbedPath = null;
   private boolean useKerning = false;
   private EncodingMode encodingMode;
   private boolean embedded;
   private String subFontName;
   private boolean isMetricsLoaded;
   private Typeface realFont;
   private FontDescriptor realFontDescriptor;
   private FontResolver resolver;

   public LazyFont(EmbedFontInfo fontInfo, FontResolver resolver) {
      this.encodingMode = EncodingMode.AUTO;
      this.embedded = true;
      this.subFontName = null;
      this.isMetricsLoaded = false;
      this.realFont = null;
      this.realFontDescriptor = null;
      this.resolver = null;
      this.metricsFileName = fontInfo.getMetricsFile();
      this.fontEmbedPath = fontInfo.getEmbedFile();
      this.useKerning = fontInfo.getKerning();
      this.encodingMode = fontInfo.getEncodingMode();
      this.subFontName = fontInfo.getSubFontName();
      this.embedded = fontInfo.isEmbedded();
      this.resolver = resolver;
   }

   public String toString() {
      return "metrics-url=" + this.metricsFileName + ", embed-url=" + this.fontEmbedPath + ", kerning=" + this.useKerning;
   }

   private void load(boolean fail) {
      if (!this.isMetricsLoaded) {
         try {
            if (this.metricsFileName != null) {
               FontReader reader = null;
               if (this.resolver != null) {
                  Source source = this.resolver.resolve(this.metricsFileName);
                  if (source == null) {
                     String err = "Cannot load font: failed to create Source from metrics file " + this.metricsFileName;
                     if (fail) {
                        throw new RuntimeException(err);
                     }

                     log.error(err);
                     return;
                  }

                  InputStream in = null;
                  if (source instanceof StreamSource) {
                     in = ((StreamSource)source).getInputStream();
                  }

                  if (in == null && source.getSystemId() != null) {
                     in = (new URL(source.getSystemId())).openStream();
                  }

                  if (in == null) {
                     String err = "Cannot load font: After URI resolution, the returned Source object does not contain an InputStream or a valid URL (system identifier) for metrics file: " + this.metricsFileName;
                     if (fail) {
                        throw new RuntimeException(err);
                     }

                     log.error(err);
                     return;
                  }

                  InputSource src = new InputSource(in);
                  src.setSystemId(source.getSystemId());
                  reader = new FontReader(src);
               } else {
                  reader = new FontReader(new InputSource((new URL(this.metricsFileName)).openStream()));
               }

               reader.setKerningEnabled(this.useKerning);
               if (this.embedded) {
                  reader.setFontEmbedPath(this.fontEmbedPath);
               }

               reader.setResolver(this.resolver);
               this.realFont = reader.getFont();
            } else {
               if (this.fontEmbedPath == null) {
                  throw new RuntimeException("Cannot load font. No font URIs available.");
               }

               this.realFont = FontLoader.loadFont(this.fontEmbedPath, this.subFontName, this.embedded, this.encodingMode, this.useKerning, this.resolver);
            }

            if (this.realFont instanceof FontDescriptor) {
               this.realFontDescriptor = (FontDescriptor)this.realFont;
            }
         } catch (FOPException var6) {
            log.error("Failed to read font metrics file " + this.metricsFileName, var6);
            if (fail) {
               throw new RuntimeException(var6.getMessage());
            }
         } catch (IOException var7) {
            log.error("Failed to read font metrics file " + this.metricsFileName, var7);
            if (fail) {
               throw new RuntimeException(var7.getMessage());
            }
         }

         this.realFont.setEventListener(this.eventListener);
         this.isMetricsLoaded = true;
      }

   }

   public Typeface getRealFont() {
      this.load(false);
      return this.realFont;
   }

   public String getEncodingName() {
      this.load(true);
      return this.realFont.getEncodingName();
   }

   public char mapChar(char c) {
      this.load(true);
      return this.realFont.mapChar(c);
   }

   public boolean hadMappingOperations() {
      this.load(true);
      return this.realFont.hadMappingOperations();
   }

   public boolean hasChar(char c) {
      this.load(true);
      return this.realFont.hasChar(c);
   }

   public boolean isMultiByte() {
      this.load(true);
      return this.realFont.isMultiByte();
   }

   public String getFontName() {
      this.load(true);
      return this.realFont.getFontName();
   }

   public String getEmbedFontName() {
      this.load(true);
      return this.realFont.getEmbedFontName();
   }

   public String getFullName() {
      this.load(true);
      return this.realFont.getFullName();
   }

   public Set getFamilyNames() {
      this.load(true);
      return this.realFont.getFamilyNames();
   }

   public int getMaxAscent(int size) {
      this.load(true);
      return this.realFont.getMaxAscent(size);
   }

   public int getAscender(int size) {
      this.load(true);
      return this.realFont.getAscender(size);
   }

   public int getCapHeight(int size) {
      this.load(true);
      return this.realFont.getCapHeight(size);
   }

   public int getDescender(int size) {
      this.load(true);
      return this.realFont.getDescender(size);
   }

   public int getXHeight(int size) {
      this.load(true);
      return this.realFont.getXHeight(size);
   }

   public int getWidth(int i, int size) {
      this.load(true);
      return this.realFont.getWidth(i, size);
   }

   public int[] getWidths() {
      this.load(true);
      return this.realFont.getWidths();
   }

   public boolean hasKerningInfo() {
      this.load(true);
      return this.realFont.hasKerningInfo();
   }

   public Map getKerningInfo() {
      this.load(true);
      return this.realFont.getKerningInfo();
   }

   public int getCapHeight() {
      this.load(true);
      return this.realFontDescriptor.getCapHeight();
   }

   public int getDescender() {
      this.load(true);
      return this.realFontDescriptor.getDescender();
   }

   public int getAscender() {
      this.load(true);
      return this.realFontDescriptor.getAscender();
   }

   public int getFlags() {
      this.load(true);
      return this.realFontDescriptor.getFlags();
   }

   public boolean isSymbolicFont() {
      this.load(true);
      return this.realFontDescriptor.isSymbolicFont();
   }

   public int[] getFontBBox() {
      this.load(true);
      return this.realFontDescriptor.getFontBBox();
   }

   public int getItalicAngle() {
      this.load(true);
      return this.realFontDescriptor.getItalicAngle();
   }

   public int getStemV() {
      this.load(true);
      return this.realFontDescriptor.getStemV();
   }

   public FontType getFontType() {
      this.load(true);
      return this.realFontDescriptor.getFontType();
   }

   public boolean isEmbeddable() {
      this.load(true);
      return this.realFontDescriptor.isEmbeddable();
   }

   static {
      log = LogFactory.getLog(LazyFont.class);
   }
}

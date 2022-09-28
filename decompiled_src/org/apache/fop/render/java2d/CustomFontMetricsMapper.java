package org.apache.fop.render.java2d;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.fonts.CustomFont;
import org.apache.fop.fonts.FontType;
import org.apache.fop.fonts.LazyFont;
import org.apache.fop.fonts.Typeface;

public class CustomFontMetricsMapper extends Typeface implements FontMetricsMapper {
   private Typeface typeface;
   private Font font;
   private float size = 1.0F;
   private static final int TYPE1_FONT = 1;

   public CustomFontMetricsMapper(CustomFont fontMetrics) throws FontFormatException, IOException {
      this.typeface = fontMetrics;
      this.initialize(fontMetrics.getEmbedFileSource());
   }

   public CustomFontMetricsMapper(LazyFont fontMetrics, Source fontSource) throws FontFormatException, IOException {
      this.typeface = fontMetrics;
      this.initialize(fontSource);
   }

   private void initialize(Source source) throws FontFormatException, IOException {
      int type = 0;
      if (FontType.TYPE1.equals(this.typeface.getFontType())) {
         type = 1;
      }

      InputStream is = null;
      if (source instanceof StreamSource) {
         is = ((StreamSource)source).getInputStream();
      } else {
         if (source.getSystemId() == null) {
            throw new IllegalArgumentException("No font source provided.");
         }

         is = (new URL(source.getSystemId())).openStream();
      }

      this.font = Font.createFont(type, is);
      is.close();
   }

   public final String getEncodingName() {
      return null;
   }

   public final boolean hasChar(char c) {
      return this.font.canDisplay(c);
   }

   public final char mapChar(char c) {
      return this.typeface.mapChar(c);
   }

   public final Font getFont(int size) {
      if (this.size == (float)size) {
         return this.font;
      } else {
         this.size = (float)size / 1000.0F;
         this.font = this.font.deriveFont(this.size);
         return this.font;
      }
   }

   public final int getAscender(int size) {
      return this.typeface.getAscender(size);
   }

   public final int getCapHeight(int size) {
      return this.typeface.getCapHeight(size);
   }

   public final int getDescender(int size) {
      return this.typeface.getDescender(size);
   }

   public final String getEmbedFontName() {
      return this.typeface.getEmbedFontName();
   }

   public final Set getFamilyNames() {
      return this.typeface.getFamilyNames();
   }

   public final String getFontName() {
      return this.typeface.getFontName();
   }

   public final FontType getFontType() {
      return this.typeface.getFontType();
   }

   public final String getFullName() {
      return this.typeface.getFullName();
   }

   public final Map getKerningInfo() {
      return this.typeface.getKerningInfo();
   }

   public final int getWidth(int i, int size) {
      return this.typeface.getWidth(i, size);
   }

   public final int[] getWidths() {
      return this.typeface.getWidths();
   }

   public final int getXHeight(int size) {
      return this.typeface.getXHeight(size);
   }

   public final boolean hasKerningInfo() {
      return this.typeface.hasKerningInfo();
   }
}

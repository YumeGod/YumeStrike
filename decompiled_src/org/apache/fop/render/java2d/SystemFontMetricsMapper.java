package org.apache.fop.render.java2d;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.fop.fonts.FontType;
import org.apache.fop.fonts.Typeface;

public class SystemFontMetricsMapper extends Typeface implements FontMetricsMapper {
   private static Java2DFontMetrics metric = null;
   private final String family;
   private final int style;

   public SystemFontMetricsMapper(String family, int style, Graphics2D graphics) {
      this.family = family;
      this.style = style;
      if (metric == null) {
         metric = new Java2DFontMetrics(graphics);
      }

   }

   public String getFontName() {
      return this.family;
   }

   public String getEmbedFontName() {
      return this.getFontName();
   }

   public String getFullName() {
      return this.getFontName();
   }

   public Set getFamilyNames() {
      Set s = new HashSet();
      s.add(this.family);
      return s;
   }

   public FontType getFontType() {
      return FontType.OTHER;
   }

   public int getMaxAscent(int size) {
      return metric.getMaxAscent(this.family, this.style, size);
   }

   public int getAscender(int size) {
      return metric.getAscender(this.family, this.style, size);
   }

   public int getCapHeight(int size) {
      return metric.getCapHeight(this.family, this.style, size);
   }

   public int getDescender(int size) {
      return metric.getDescender(this.family, this.style, size);
   }

   public int getXHeight(int size) {
      return metric.getXHeight(this.family, this.style, size);
   }

   public int getWidth(int i, int size) {
      return metric.width(i, this.family, this.style, size);
   }

   public int[] getWidths() {
      return metric.getWidths(this.family, this.style, 1);
   }

   public Font getFont(int size) {
      return metric.getFont(this.family, this.style, size);
   }

   public Map getKerningInfo() {
      return Collections.EMPTY_MAP;
   }

   public boolean hasKerningInfo() {
      return false;
   }

   public String getEncodingName() {
      return null;
   }

   public char mapChar(char c) {
      return c;
   }

   public boolean hasChar(char c) {
      return metric.hasChar(this.family, this.style, 1, c);
   }
}

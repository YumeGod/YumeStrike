package org.apache.fop.fonts;

import java.util.Map;
import java.util.Set;

public interface FontMetrics {
   String getFontName();

   String getFullName();

   Set getFamilyNames();

   String getEmbedFontName();

   FontType getFontType();

   int getMaxAscent(int var1);

   int getAscender(int var1);

   int getCapHeight(int var1);

   int getDescender(int var1);

   int getXHeight(int var1);

   int getWidth(int var1, int var2);

   int[] getWidths();

   boolean hasKerningInfo();

   Map getKerningInfo();
}

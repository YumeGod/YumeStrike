package org.apache.fop.fonts;

import java.util.Map;
import java.util.Set;

public interface MutableFont {
   void setFontName(String var1);

   void setFullName(String var1);

   void setFamilyNames(Set var1);

   void setEmbedFileName(String var1);

   void setEmbedResourceName(String var1);

   void setCapHeight(int var1);

   void setAscender(int var1);

   void setDescender(int var1);

   void setFontBBox(int[] var1);

   void setFlags(int var1);

   void setStemV(int var1);

   void setItalicAngle(int var1);

   void setMissingWidth(int var1);

   void setFontType(FontType var1);

   void setFirstChar(int var1);

   void setLastChar(int var1);

   void setKerningEnabled(boolean var1);

   void putKerningEntry(Integer var1, Map var2);
}

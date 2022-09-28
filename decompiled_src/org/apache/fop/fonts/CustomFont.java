package org.apache.fop.fonts;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.transform.Source;

public abstract class CustomFont extends Typeface implements FontDescriptor, MutableFont {
   private String fontName = null;
   private String fullName = null;
   private Set familyNames = null;
   private String fontSubName = null;
   private String embedFileName = null;
   private String embedResourceName = null;
   private FontResolver resolver = null;
   private int capHeight = 0;
   private int xHeight = 0;
   private int ascender = 0;
   private int descender = 0;
   private int[] fontBBox = new int[]{0, 0, 0, 0};
   private int flags = 4;
   private int weight = 0;
   private int stemV = 0;
   private int italicAngle = 0;
   private int missingWidth = 0;
   private FontType fontType;
   private int firstChar;
   private int lastChar;
   private Map kerning;
   private boolean useKerning;

   public CustomFont() {
      this.fontType = FontType.TYPE1;
      this.firstChar = 0;
      this.lastChar = 255;
      this.useKerning = true;
   }

   public String getFontName() {
      return this.fontName;
   }

   public String getEmbedFontName() {
      return this.getFontName();
   }

   public String getFullName() {
      return this.fullName;
   }

   public Set getFamilyNames() {
      return Collections.unmodifiableSet(this.familyNames);
   }

   public String getStrippedFontName() {
      return FontUtil.stripWhiteSpace(this.getFontName());
   }

   public String getFontSubName() {
      return this.fontSubName;
   }

   public String getEmbedFileName() {
      return this.embedFileName;
   }

   public Source getEmbedFileSource() throws IOException {
      Source result = null;
      if (this.resolver != null && this.embedFileName != null) {
         result = this.resolver.resolve(this.embedFileName);
         if (result == null) {
            throw new IOException("Unable to resolve Source '" + this.embedFileName + "' for embedded font");
         }
      }

      return result;
   }

   public String getEmbedResourceName() {
      return this.embedResourceName;
   }

   public int getAscender() {
      return this.ascender;
   }

   public int getDescender() {
      return this.descender;
   }

   public int getCapHeight() {
      return this.capHeight;
   }

   public int getAscender(int size) {
      return size * this.ascender;
   }

   public int getDescender(int size) {
      return size * this.descender;
   }

   public int getCapHeight(int size) {
      return size * this.capHeight;
   }

   public int getXHeight(int size) {
      return size * this.xHeight;
   }

   public int[] getFontBBox() {
      return this.fontBBox;
   }

   public int getFlags() {
      return this.flags;
   }

   public boolean isSymbolicFont() {
      return (this.getFlags() & 4) != 0 || "ZapfDingbatsEncoding".equals(this.getEncodingName());
   }

   public int getWeight() {
      return this.weight;
   }

   public int getStemV() {
      return this.stemV;
   }

   public int getItalicAngle() {
      return this.italicAngle;
   }

   public int getMissingWidth() {
      return this.missingWidth;
   }

   public FontType getFontType() {
      return this.fontType;
   }

   public int getFirstChar() {
      return this.firstChar;
   }

   public int getLastChar() {
      return this.lastChar;
   }

   public boolean isKerningEnabled() {
      return this.useKerning;
   }

   public final boolean hasKerningInfo() {
      return this.isKerningEnabled() && this.kerning != null && !this.kerning.isEmpty();
   }

   public final Map getKerningInfo() {
      return this.hasKerningInfo() ? this.kerning : Collections.EMPTY_MAP;
   }

   public void setFontName(String name) {
      this.fontName = name;
   }

   public void setFullName(String name) {
      this.fullName = name;
   }

   public void setFamilyNames(Set names) {
      this.familyNames = new HashSet(names);
   }

   public void setFontSubFamilyName(String subFamilyName) {
      this.fontSubName = subFamilyName;
   }

   public void setEmbedFileName(String path) {
      this.embedFileName = path;
   }

   public void setEmbedResourceName(String name) {
      this.embedResourceName = name;
   }

   public void setCapHeight(int capHeight) {
      this.capHeight = capHeight;
   }

   public void setXHeight(int xHeight) {
      this.xHeight = xHeight;
   }

   public void setAscender(int ascender) {
      this.ascender = ascender;
   }

   public void setDescender(int descender) {
      this.descender = descender;
   }

   public void setFontBBox(int[] bbox) {
      this.fontBBox = bbox;
   }

   public void setFlags(int flags) {
      this.flags = flags;
   }

   public void setWeight(int weight) {
      weight = weight / 100 * 100;
      weight = Math.max(100, weight);
      weight = Math.min(900, weight);
      this.weight = weight;
   }

   public void setStemV(int stemV) {
      this.stemV = stemV;
   }

   public void setItalicAngle(int italicAngle) {
      this.italicAngle = italicAngle;
   }

   public void setMissingWidth(int width) {
      this.missingWidth = width;
   }

   public void setFontType(FontType fontType) {
      this.fontType = fontType;
   }

   public void setFirstChar(int index) {
      this.firstChar = index;
   }

   public void setLastChar(int index) {
      this.lastChar = index;
   }

   public void setKerningEnabled(boolean enabled) {
      this.useKerning = enabled;
   }

   public void setResolver(FontResolver resolver) {
      this.resolver = resolver;
   }

   public void putKerningEntry(Integer key, Map value) {
      if (this.kerning == null) {
         this.kerning = new HashMap();
      }

      this.kerning.put(key, value);
   }

   public void replaceKerningMap(Map kerningMap) {
      if (kerningMap == null) {
         this.kerning = Collections.EMPTY_MAP;
      } else {
         this.kerning = kerningMap;
      }

   }
}

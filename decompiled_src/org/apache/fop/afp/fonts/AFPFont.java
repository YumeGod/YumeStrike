package org.apache.fop.afp.fonts;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.fop.fonts.FontType;
import org.apache.fop.fonts.Typeface;

public abstract class AFPFont extends Typeface {
   protected String name;
   private boolean embeddable = true;

   public AFPFont(String name) {
      this.name = name;
   }

   public String getFontName() {
      return this.name;
   }

   public String getEmbedFontName() {
      return this.name;
   }

   public String getFullName() {
      return this.getFontName();
   }

   public Set getFamilyNames() {
      Set s = new HashSet();
      s.add(this.name);
      return s;
   }

   public FontType getFontType() {
      return FontType.OTHER;
   }

   public boolean hasKerningInfo() {
      return false;
   }

   public Map getKerningInfo() {
      return null;
   }

   public abstract CharacterSet getCharacterSet(int var1);

   public void setEmbeddable(boolean value) {
      this.embeddable = value;
   }

   public boolean isEmbeddable() {
      return this.embeddable;
   }

   protected static final char toUnicodeCodepoint(int character) {
      return (char)character;
   }

   public String toString() {
      return "name=" + this.name;
   }
}

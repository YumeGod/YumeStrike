package org.apache.fop.afp.fonts;

public class AFPFontAttributes {
   private int fontReference;
   private final String fontKey;
   private final AFPFont font;
   private final int pointSize;

   public AFPFontAttributes(String fontKey, AFPFont font, int pointSize) {
      this.fontKey = fontKey;
      this.font = font;
      this.pointSize = pointSize;
   }

   public AFPFont getFont() {
      return this.font;
   }

   public String getFontKey() {
      return this.fontKey + this.pointSize;
   }

   public int getPointSize() {
      return this.pointSize;
   }

   public int getFontReference() {
      return this.fontReference;
   }

   public void setFontReference(int fontReference) {
      this.fontReference = fontReference;
   }

   public String toString() {
      return "fontReference=" + this.fontReference + ", fontKey=" + this.fontKey + ", font=" + this.font + ", pointSize=" + this.pointSize;
   }
}

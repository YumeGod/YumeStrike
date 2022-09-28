package org.apache.fop.fonts.substitute;

public class FontSubstitution {
   private FontQualifier fromQualifier;
   private FontQualifier toQualifier;

   public FontSubstitution(FontQualifier fromQualifier, FontQualifier toQualifier) {
      this.fromQualifier = fromQualifier;
      this.toQualifier = toQualifier;
   }

   public FontQualifier getFromQualifier() {
      return this.fromQualifier;
   }

   public FontQualifier getToQualifier() {
      return this.toQualifier;
   }

   public String toString() {
      return "from=" + this.fromQualifier + ", to=" + this.toQualifier;
   }
}

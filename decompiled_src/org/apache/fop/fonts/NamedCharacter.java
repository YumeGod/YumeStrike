package org.apache.fop.fonts;

public class NamedCharacter {
   private String charName;
   private String unicodeSequence;

   public NamedCharacter(String charName, String unicodeSequence) {
      if (charName == null) {
         throw new NullPointerException("charName must not be null");
      } else {
         this.charName = charName;
         if (unicodeSequence != null) {
            this.unicodeSequence = unicodeSequence;
         } else {
            this.unicodeSequence = org.apache.xmlgraphics.fonts.Glyphs.getUnicodeSequenceForGlyphName(charName);
         }

      }
   }

   public NamedCharacter(String charName) {
      this(charName, (String)null);
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      result = 31 * result + (this.charName == null ? 0 : this.charName.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         NamedCharacter other = (NamedCharacter)obj;
         return this.charName.equals(other.charName);
      }
   }

   public String getName() {
      return this.charName;
   }

   public String getUnicodeSequence() {
      return this.unicodeSequence;
   }

   public boolean hasSingleUnicodeValue() {
      return this.unicodeSequence != null && this.unicodeSequence.length() == 1;
   }

   public char getSingleUnicodeValue() throws IllegalStateException {
      if (this.unicodeSequence == null) {
         return '\uffff';
      } else if (this.unicodeSequence.length() > 1) {
         throw new IllegalStateException("getSingleUnicodeValue() may not be called for a named character that has more than one Unicode value (a sequence) associated with the named character!");
      } else {
         return this.unicodeSequence.charAt(0);
      }
   }

   public String toString() {
      StringBuffer sb = new StringBuffer(this.unicodeSequence);
      sb.append(" (");
      if (this.unicodeSequence != null) {
         int i = 0;

         for(int c = this.unicodeSequence.length(); i < c; ++i) {
            sb.append("0x").append(Integer.toHexString(this.unicodeSequence.charAt(0)));
         }

         sb.append(", ");
      }

      sb.append(this.getName()).append(')');
      return sb.toString();
   }
}

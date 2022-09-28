package org.apache.fop.fonts.type1;

import java.awt.geom.RectangularShape;
import org.apache.fop.fonts.NamedCharacter;

public class AFMCharMetrics {
   private int charCode = -1;
   private NamedCharacter character;
   private double widthX;
   private double widthY;
   private RectangularShape bBox;

   public int getCharCode() {
      return this.charCode;
   }

   public boolean hasCharCode() {
      return this.charCode >= 0;
   }

   public void setCharCode(int charCode) {
      this.charCode = charCode;
   }

   public NamedCharacter getCharacter() {
      return this.character;
   }

   public void setCharacter(NamedCharacter ch) {
      this.character = ch;
   }

   public void setCharacter(String charName, String unicodeSequence) {
      this.setCharacter(new NamedCharacter(charName, unicodeSequence));
   }

   public String getUnicodeSequence() {
      return this.getCharacter() != null ? this.getCharacter().getUnicodeSequence() : null;
   }

   public String getCharName() {
      return this.getCharacter() != null ? this.getCharacter().getName() : null;
   }

   public double getWidthX() {
      return this.widthX;
   }

   public void setWidthX(double widthX) {
      this.widthX = widthX;
   }

   public double getWidthY() {
      return this.widthY;
   }

   public void setWidthY(double widthY) {
      this.widthY = widthY;
   }

   public RectangularShape getBBox() {
      return this.bBox;
   }

   public void setBBox(RectangularShape box) {
      this.bBox = box;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer("AFM Char: ");
      sb.append(this.getCharCode());
      sb.append(" (");
      if (this.getUnicodeSequence() != null) {
         int i = 0;

         for(int c = this.getUnicodeSequence().length(); i < c; ++i) {
            sb.append("0x").append(Integer.toHexString(this.getUnicodeSequence().charAt(i)));
            sb.append(", ");
         }
      }

      sb.append(this.getCharName()).append(')');
      return sb.toString();
   }
}

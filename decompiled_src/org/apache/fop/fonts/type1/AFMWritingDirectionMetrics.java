package org.apache.fop.fonts.type1;

public class AFMWritingDirectionMetrics {
   private Number underlinePosition;
   private Number underlineThickness;
   private double italicAngle;
   private boolean isFixedPitch;

   public Number getUnderlinePosition() {
      return this.underlinePosition;
   }

   public void setUnderlinePosition(Number underlinePosition) {
      this.underlinePosition = underlinePosition;
   }

   public Number getUnderlineThickness() {
      return this.underlineThickness;
   }

   public void setUnderlineThickness(Number underlineThickness) {
      this.underlineThickness = underlineThickness;
   }

   public double getItalicAngle() {
      return this.italicAngle;
   }

   public void setItalicAngle(double italicAngle) {
      this.italicAngle = italicAngle;
   }

   public boolean isFixedPitch() {
      return this.isFixedPitch;
   }

   public void setFixedPitch(boolean value) {
      this.isFixedPitch = value;
   }
}

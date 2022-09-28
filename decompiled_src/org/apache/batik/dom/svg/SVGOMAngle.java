package org.apache.batik.dom.svg;

import org.apache.batik.parser.AngleParser;
import org.apache.batik.parser.DefaultAngleHandler;
import org.apache.batik.parser.ParseException;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAngle;

public class SVGOMAngle implements SVGAngle {
   protected short unitType;
   protected float value;
   protected static final String[] UNITS = new String[]{"", "", "deg", "rad", "grad"};
   protected static double[][] K = new double[][]{{1.0, 0.017453292519943295, 0.015707963267948967}, {57.29577951308232, 1.0, 63.66197723675813}, {0.9, 0.015707963267948967, 1.0}};

   public short getUnitType() {
      this.revalidate();
      return this.unitType;
   }

   public float getValue() {
      this.revalidate();
      return toUnit(this.unitType, this.value, (short)2);
   }

   public void setValue(float var1) throws DOMException {
      this.revalidate();
      this.unitType = 2;
      this.value = var1;
      this.reset();
   }

   public float getValueInSpecifiedUnits() {
      this.revalidate();
      return this.value;
   }

   public void setValueInSpecifiedUnits(float var1) throws DOMException {
      this.revalidate();
      this.value = var1;
      this.reset();
   }

   public String getValueAsString() {
      this.revalidate();
      return Float.toString(this.value) + UNITS[this.unitType];
   }

   public void setValueAsString(String var1) throws DOMException {
      this.parse(var1);
      this.reset();
   }

   public void newValueSpecifiedUnits(short var1, float var2) {
      this.unitType = var1;
      this.value = var2;
      this.reset();
   }

   public void convertToSpecifiedUnits(short var1) {
      this.value = toUnit(this.unitType, this.value, var1);
      this.unitType = var1;
   }

   protected void reset() {
   }

   protected void revalidate() {
   }

   protected void parse(String var1) {
      try {
         AngleParser var2 = new AngleParser();
         var2.setAngleHandler(new DefaultAngleHandler() {
            public void angleValue(float var1) throws ParseException {
               SVGOMAngle.this.value = var1;
            }

            public void deg() throws ParseException {
               SVGOMAngle.this.unitType = 2;
            }

            public void rad() throws ParseException {
               SVGOMAngle.this.unitType = 3;
            }

            public void grad() throws ParseException {
               SVGOMAngle.this.unitType = 4;
            }
         });
         this.unitType = 1;
         var2.parse(var1);
      } catch (ParseException var3) {
         this.unitType = 0;
         this.value = 0.0F;
      }

   }

   public static float toUnit(short var0, float var1, short var2) {
      if (var0 == 1) {
         var0 = 2;
      }

      if (var2 == 1) {
         var2 = 2;
      }

      return (float)(K[var0 - 2][var2 - 2] * (double)var1);
   }
}

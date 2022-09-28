package org.apache.batik.css.engine.value;

import org.w3c.dom.DOMException;

public class FloatValue extends AbstractValue {
   protected static final String[] UNITS = new String[]{"", "%", "em", "ex", "px", "cm", "mm", "in", "pt", "pc", "deg", "rad", "grad", "ms", "s", "Hz", "kHz", ""};
   protected float floatValue;
   protected short unitType;

   public static String getCssText(short var0, float var1) {
      if (var0 >= 0 && var0 < UNITS.length) {
         String var2 = String.valueOf(var1);
         if (var2.endsWith(".0")) {
            var2 = var2.substring(0, var2.length() - 2);
         }

         return var2 + UNITS[var0 - 1];
      } else {
         throw new DOMException((short)12, "");
      }
   }

   public FloatValue(short var1, float var2) {
      this.unitType = var1;
      this.floatValue = var2;
   }

   public short getPrimitiveType() {
      return this.unitType;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public String getCssText() {
      return getCssText(this.unitType, this.floatValue);
   }

   public String toString() {
      return this.getCssText();
   }
}

package org.apache.batik.css.engine.value;

import org.w3c.dom.DOMException;

public class StringValue extends AbstractValue {
   protected String value;
   protected short unitType;

   public static String getCssText(short var0, String var1) {
      switch (var0) {
         case 19:
            int var2 = var1.indexOf(34) != -1 ? 39 : 34;
            return var2 + var1 + var2;
         case 20:
            return "url(" + var1 + ')';
         default:
            return var1;
      }
   }

   public StringValue(short var1, String var2) {
      this.unitType = var1;
      this.value = var2;
   }

   public short getPrimitiveType() {
      return this.unitType;
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof StringValue) {
         StringValue var2 = (StringValue)var1;
         return this.unitType != var2.unitType ? false : this.value.equals(var2.value);
      } else {
         return false;
      }
   }

   public String getCssText() {
      return getCssText(this.unitType, this.value);
   }

   public String getStringValue() throws DOMException {
      return this.value;
   }

   public String toString() {
      return this.getCssText();
   }
}

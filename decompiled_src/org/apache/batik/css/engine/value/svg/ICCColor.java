package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.value.AbstractValue;
import org.w3c.dom.DOMException;

public class ICCColor extends AbstractValue {
   protected String colorProfile;
   protected int count;
   protected float[] colors = new float[5];

   public ICCColor(String var1) {
      this.colorProfile = var1;
   }

   public short getCssValueType() {
      return 3;
   }

   public String getColorProfile() throws DOMException {
      return this.colorProfile;
   }

   public int getNumberOfColors() throws DOMException {
      return this.count;
   }

   public float getColor(int var1) throws DOMException {
      return this.colors[var1];
   }

   public String getCssText() {
      StringBuffer var1 = new StringBuffer(this.count * 8);
      var1.append("icc-color(");
      var1.append(this.colorProfile);

      for(int var2 = 0; var2 < this.count; ++var2) {
         var1.append(", ");
         var1.append(this.colors[var2]);
      }

      var1.append(')');
      return var1.toString();
   }

   public void append(float var1) {
      if (this.count == this.colors.length) {
         float[] var2 = new float[this.count * 2];
         System.arraycopy(this.colors, 0, var2, 0, this.count);
         this.colors = var2;
      }

      this.colors[this.count++] = var1;
   }
}

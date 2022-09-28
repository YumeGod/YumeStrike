package org.apache.batik.ext.awt.image;

import java.io.ObjectStreamException;
import java.io.Serializable;

public final class CompositeRule implements Serializable {
   public static final int RULE_OVER = 1;
   public static final int RULE_IN = 2;
   public static final int RULE_OUT = 3;
   public static final int RULE_ATOP = 4;
   public static final int RULE_XOR = 5;
   public static final int RULE_ARITHMETIC = 6;
   public static final int RULE_MULTIPLY = 7;
   public static final int RULE_SCREEN = 8;
   public static final int RULE_DARKEN = 9;
   public static final int RULE_LIGHTEN = 10;
   public static final CompositeRule OVER = new CompositeRule(1);
   public static final CompositeRule IN = new CompositeRule(2);
   public static final CompositeRule OUT = new CompositeRule(3);
   public static final CompositeRule ATOP = new CompositeRule(4);
   public static final CompositeRule XOR = new CompositeRule(5);
   public static final CompositeRule MULTIPLY = new CompositeRule(7);
   public static final CompositeRule SCREEN = new CompositeRule(8);
   public static final CompositeRule DARKEN = new CompositeRule(9);
   public static final CompositeRule LIGHTEN = new CompositeRule(10);
   private int rule;
   private float k1;
   private float k2;
   private float k3;
   private float k4;

   public static CompositeRule ARITHMETIC(float var0, float var1, float var2, float var3) {
      return new CompositeRule(var0, var1, var2, var3);
   }

   public int getRule() {
      return this.rule;
   }

   private CompositeRule(int var1) {
      this.rule = var1;
   }

   private CompositeRule(float var1, float var2, float var3, float var4) {
      this.rule = 6;
      this.k1 = var1;
      this.k2 = var2;
      this.k3 = var3;
      this.k4 = var4;
   }

   public float[] getCoefficients() {
      return this.rule != 6 ? null : new float[]{this.k1, this.k2, this.k3, this.k4};
   }

   private Object readResolve() throws ObjectStreamException {
      switch (this.rule) {
         case 1:
            return OVER;
         case 2:
            return IN;
         case 3:
            return OUT;
         case 4:
            return ATOP;
         case 5:
            return XOR;
         case 6:
            return this;
         case 7:
            return MULTIPLY;
         case 8:
            return SCREEN;
         case 9:
            return DARKEN;
         case 10:
            return LIGHTEN;
         default:
            throw new Error("Unknown Composite Rule type");
      }
   }

   public String toString() {
      switch (this.rule) {
         case 1:
            return "[CompositeRule: OVER]";
         case 2:
            return "[CompositeRule: IN]";
         case 3:
            return "[CompositeRule: OUT]";
         case 4:
            return "[CompositeRule: ATOP]";
         case 5:
            return "[CompositeRule: XOR]";
         case 6:
            return "[CompositeRule: ARITHMATIC k1:" + this.k1 + " k2: " + this.k2 + " k3: " + this.k3 + " k4: " + this.k4 + ']';
         case 7:
            return "[CompositeRule: MULTIPLY]";
         case 8:
            return "[CompositeRule: SCREEN]";
         case 9:
            return "[CompositeRule: DARKEN]";
         case 10:
            return "[CompositeRule: LIGHTEN]";
         default:
            throw new Error("Unknown Composite Rule type");
      }
   }
}

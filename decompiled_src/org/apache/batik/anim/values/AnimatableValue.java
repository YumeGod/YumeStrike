package org.apache.batik.anim.values;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import org.apache.batik.dom.anim.AnimationTarget;

public abstract class AnimatableValue {
   protected static DecimalFormat decimalFormat;
   protected AnimationTarget target;
   protected boolean hasChanged = true;

   protected AnimatableValue(AnimationTarget var1) {
      this.target = var1;
   }

   public static String formatNumber(float var0) {
      return decimalFormat.format((double)var0);
   }

   public abstract AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5);

   public abstract boolean canPace();

   public abstract float distanceTo(AnimatableValue var1);

   public abstract AnimatableValue getZeroValue();

   public String getCssText() {
      return null;
   }

   public boolean hasChanged() {
      boolean var1 = this.hasChanged;
      this.hasChanged = false;
      return var1;
   }

   public String toStringRep() {
      return this.getCssText();
   }

   public String toString() {
      return this.getClass().getName() + "[" + this.toStringRep() + "]";
   }

   static {
      decimalFormat = new DecimalFormat("0.0###########################################################", new DecimalFormatSymbols(Locale.ENGLISH));
   }
}

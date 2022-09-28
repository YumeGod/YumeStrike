package org.apache.fop.layoutmgr;

import org.apache.fop.fo.properties.KeepProperty;
import org.apache.fop.fo.properties.Property;

public class Keep {
   private static final int STRENGTH_AUTO = Integer.MIN_VALUE;
   private static final int STRENGTH_ALWAYS = Integer.MAX_VALUE;
   public static final Keep KEEP_AUTO = new Keep(Integer.MIN_VALUE, 9);
   public static final Keep KEEP_ALWAYS = new Keep(Integer.MAX_VALUE, 75);
   private int strength;
   private int context;

   private Keep(int strength, int context) {
      this.strength = strength;
      this.context = context;
   }

   private static int getKeepStrength(Property keep) {
      if (keep.isAuto()) {
         return Integer.MIN_VALUE;
      } else {
         return keep.getEnum() == 7 ? Integer.MAX_VALUE : keep.getNumber().intValue();
      }
   }

   public static Keep getKeep(KeepProperty keepProperty) {
      Keep keep = new Keep(Integer.MIN_VALUE, 9);
      keep.update(keepProperty.getWithinPage(), 104);
      keep.update(keepProperty.getWithinColumn(), 28);
      keep.update(keepProperty.getWithinLine(), 75);
      return keep;
   }

   private void update(Property keep, int context) {
      if (!keep.isAuto()) {
         this.strength = getKeepStrength(keep);
         this.context = context;
      }

   }

   public boolean isAuto() {
      return this.strength == Integer.MIN_VALUE;
   }

   public int getContext() {
      return this.context;
   }

   public int getPenalty() {
      if (this.strength == Integer.MIN_VALUE) {
         return 0;
      } else {
         return this.strength == Integer.MAX_VALUE ? 1000 : 999;
      }
   }

   private static int getKeepContextPriority(int context) {
      switch (context) {
         case 9:
            return 3;
         case 28:
            return 1;
         case 75:
            return 0;
         case 104:
            return 2;
         default:
            throw new IllegalArgumentException();
      }
   }

   public Keep compare(Keep other) {
      if (this.strength == Integer.MAX_VALUE && this.strength > other.strength) {
         return this;
      } else if (other.strength == Integer.MAX_VALUE && other.strength > this.strength) {
         return other;
      } else {
         int pThis = getKeepContextPriority(this.context);
         int pOther = getKeepContextPriority(other.context);
         if (pThis == pOther) {
            return this.strength >= other.strength ? this : other;
         } else {
            return pThis < pOther ? this : other;
         }
      }
   }

   public String toString() {
      return this.strength == Integer.MIN_VALUE ? "auto" : (this.strength == Integer.MAX_VALUE ? "always" : Integer.toString(this.strength));
   }
}

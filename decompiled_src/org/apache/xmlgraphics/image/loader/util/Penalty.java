package org.apache.xmlgraphics.image.loader.util;

public class Penalty {
   public static final Penalty ZERO_PENALTY = new Penalty(0);
   public static final Penalty INFINITE_PENALTY = new Penalty(Integer.MAX_VALUE);
   private final int value;

   public static Penalty toPenalty(int value) {
      switch (value) {
         case 0:
            return ZERO_PENALTY;
         case Integer.MAX_VALUE:
            return INFINITE_PENALTY;
         default:
            return new Penalty(value);
      }
   }

   private Penalty(int value) {
      this.value = value;
   }

   public Penalty add(Penalty value) {
      return this.add(value.getValue());
   }

   public Penalty add(int value) {
      long p = (long)this.getValue() + (long)value;
      return toPenalty(truncate(p));
   }

   public int getValue() {
      return this.value;
   }

   public boolean isInfinitePenalty() {
      return this.value == Integer.MAX_VALUE;
   }

   public String toString() {
      return "Penalty: " + (this.isInfinitePenalty() ? "INF" : Integer.toString(this.getValue()));
   }

   public static int truncate(long penalty) {
      penalty = Math.min(2147483647L, penalty);
      penalty = Math.max(-2147483648L, penalty);
      return (int)penalty;
   }
}

package org.apache.fop.layoutmgr;

public final class Adjustment {
   public static final Adjustment NO_ADJUSTMENT = new Adjustment("none");
   public static final Adjustment SPACE_BEFORE_ADJUSTMENT = new Adjustment("space-before");
   public static final Adjustment SPACE_AFTER_ADJUSTMENT = new Adjustment("space-after");
   public static final Adjustment LINE_NUMBER_ADJUSTMENT = new Adjustment("line-number");
   public static final Adjustment LINE_HEIGHT_ADJUSTMENT = new Adjustment("line-height");
   private final String name;

   private Adjustment(String name) {
      this.name = name;
   }

   public boolean equals(Object obj) {
      return this == obj;
   }

   public int hashCode() {
      return super.hashCode();
   }

   public String toString() {
      return this.name;
   }
}

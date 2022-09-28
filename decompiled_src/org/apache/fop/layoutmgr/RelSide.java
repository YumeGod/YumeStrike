package org.apache.fop.layoutmgr;

public final class RelSide {
   public static final RelSide BEFORE = new RelSide("before");
   public static final RelSide AFTER = new RelSide("after");
   public static final RelSide START = new RelSide("start");
   public static final RelSide END = new RelSide("end");
   private String name;

   private RelSide(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public String toString() {
      return "RelSide:" + this.name;
   }
}

package org.apache.fop.render.print;

public final class PagesMode {
   public static final PagesMode ALL = new PagesMode("all");
   public static final PagesMode EVEN = new PagesMode("even");
   public static final PagesMode ODD = new PagesMode("odd");
   private String name;

   private PagesMode(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public static PagesMode byName(String name) {
      if (ALL.getName().equalsIgnoreCase(name)) {
         return ALL;
      } else if (EVEN.getName().equalsIgnoreCase(name)) {
         return EVEN;
      } else if (ODD.getName().equalsIgnoreCase(name)) {
         return ODD;
      } else {
         throw new IllegalArgumentException("Invalid value for PagesMode: " + name);
      }
   }

   public String toString() {
      return "PagesMode:" + this.name;
   }
}

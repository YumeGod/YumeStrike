package org.apache.fop.render.txt;

public final class Helper {
   private Helper() {
   }

   public static int round(int x, int quantum) {
      int ceil = ceil(x, quantum);
      int floor = floor(x, quantum);
      return ceil - x < x - floor ? ceil : floor;
   }

   public static int ceil(int x, int quantum) {
      int dx = x >= 0 && x % quantum != 0 ? 1 : 0;
      return (x / quantum + dx) * quantum;
   }

   public static int floor(int x, int quantum) {
      int dx = x <= 0 && x % quantum != 0 ? -1 : 0;
      return (x / quantum + dx) * quantum;
   }

   public static int roundPosition(int x, int y) {
      return round(x, y) / y;
   }

   public static int ceilPosition(int x, int y) {
      return ceil(x, y) / y;
   }

   public static int floorPosition(int x, int y) {
      return floor(x, y) / y;
   }
}

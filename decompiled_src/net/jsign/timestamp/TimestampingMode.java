package net.jsign.timestamp;

public enum TimestampingMode {
   AUTHENTICODE,
   RFC3161;

   public static TimestampingMode of(String s) {
      TimestampingMode[] arr$ = values();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         TimestampingMode mode = arr$[i$];
         if (mode.name().equalsIgnoreCase(s)) {
            return mode;
         }
      }

      if ("tsp".equalsIgnoreCase(s)) {
         return RFC3161;
      } else {
         throw new IllegalArgumentException("Unknown timestamping mode: " + s);
      }
   }
}

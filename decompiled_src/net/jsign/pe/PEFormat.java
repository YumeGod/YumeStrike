package net.jsign.pe;

public enum PEFormat {
   PE32(267, "PE32"),
   PE32plus(523, "PE32+"),
   ROM(263, "ROM");

   final int value;
   final String label;

   private PEFormat(int value, String label) {
      this.value = value;
      this.label = label;
   }

   static PEFormat valueOf(int value) {
      PEFormat[] arr$ = values();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         PEFormat format = arr$[i$];
         if (format.value == value) {
            return format;
         }
      }

      return null;
   }
}

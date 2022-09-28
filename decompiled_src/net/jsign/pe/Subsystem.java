package net.jsign.pe;

public enum Subsystem {
   UNKNOWN(0),
   NATIVE(1),
   WINDOWS_GUI(2),
   WINDOWS_CUI(3),
   POSIX_CUI(7),
   WINDOWS_CE_GUI(9),
   EFI_APPLICATION(10),
   EFI_BOOT_SERVICE_DRIVER(11),
   EFI_RUNTIME_DRIVER(12),
   EFI_ROM(13),
   XBOX(14);

   final int value;

   private Subsystem(int value) {
      this.value = value;
   }

   static Subsystem valueOf(int value) {
      Subsystem[] arr$ = values();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Subsystem format = arr$[i$];
         if (format.value == value) {
            return format;
         }
      }

      return null;
   }
}

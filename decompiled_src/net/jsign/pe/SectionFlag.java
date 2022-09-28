package net.jsign.pe;

import java.util.ArrayList;
import java.util.List;

public enum SectionFlag {
   TYPE_NO_PAD(8),
   CODE(32),
   INITIALIZED_DATA(64),
   UNINITIALIZED_DATA(128),
   LNK_OTHER(256),
   LNK_INFO(512),
   LNK_REMOVE(2048),
   LNK_COMDAT(4096),
   GPREL(32768),
   MEM_PURGEABLE(131072),
   MEM_16BIT(131072),
   MEM_LOCKED(262144),
   MEM_PRELOAD(524288),
   ALIGN_1BYTES(1048576),
   ALIGN_2BYTES(2097152),
   ALIGN_4BYTES(3145728),
   ALIGN_8BYTES(4194304),
   ALIGN_16BYTES(5242880),
   ALIGN_32BYTES(6291456),
   ALIGN_64BYTES(7340032),
   ALIGN_128BYTES(8388608),
   ALIGN_256BYTES(9437184),
   ALIGN_512BYTES(10485760),
   ALIGN_1024BYTES(11534336),
   ALIGN_2048BYTES(12582912),
   ALIGN_4096BYTES(13631488),
   ALIGN_8192BYTES(14680064),
   LNK_NRELOC_OVFL(16777216),
   MEM_DISCARDABLE(33554432),
   MEM_NOT_CACHED(67108864),
   MEM_NOT_PAGED(134217728),
   MEM_SHARED(268435456),
   EXECUTE(536870912),
   READ(1073741824),
   WRITE(Integer.MIN_VALUE);

   private final int mask;

   private SectionFlag(int mask) {
      this.mask = mask;
   }

   static List getFlags(int flags) {
      List result = new ArrayList();
      SectionFlag[] arr$ = values();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         SectionFlag flag = arr$[i$];
         if ((flag.mask & flags) != 0) {
            result.add(flag);
         }
      }

      return result;
   }
}

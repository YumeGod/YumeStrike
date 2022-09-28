package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CmapIndexEntry {
   private int platformId;
   private int encodingId;
   private int offset;

   protected CmapIndexEntry(RandomAccessFile var1) throws IOException {
      this.platformId = var1.readUnsignedShort();
      this.encodingId = var1.readUnsignedShort();
      this.offset = var1.readInt();
   }

   public int getEncodingId() {
      return this.encodingId;
   }

   public int getOffset() {
      return this.offset;
   }

   public int getPlatformId() {
      return this.platformId;
   }

   public String toString() {
      String var2 = "";
      String var1;
      switch (this.platformId) {
         case 1:
            var1 = " (Macintosh)";
            break;
         case 3:
            var1 = " (Windows)";
            break;
         default:
            var1 = "";
      }

      if (this.platformId == 3) {
         switch (this.encodingId) {
            case 0:
               var2 = " (Symbol)";
               break;
            case 1:
               var2 = " (Unicode)";
               break;
            case 2:
               var2 = " (ShiftJIS)";
               break;
            case 3:
               var2 = " (Big5)";
               break;
            case 4:
               var2 = " (PRC)";
               break;
            case 5:
               var2 = " (Wansung)";
               break;
            case 6:
               var2 = " (Johab)";
               break;
            default:
               var2 = "";
         }
      }

      return "platform id: " + this.platformId + var1 + ", encoding id: " + this.encodingId + var2 + ", offset: " + this.offset;
   }
}

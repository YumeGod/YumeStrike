package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Script {
   private int defaultLangSysOffset;
   private int langSysCount;
   private LangSysRecord[] langSysRecords;
   private LangSys defaultLangSys;
   private LangSys[] langSys;

   protected Script(RandomAccessFile var1, int var2) throws IOException {
      var1.seek((long)var2);
      this.defaultLangSysOffset = var1.readUnsignedShort();
      this.langSysCount = var1.readUnsignedShort();
      int var3;
      if (this.langSysCount > 0) {
         this.langSysRecords = new LangSysRecord[this.langSysCount];

         for(var3 = 0; var3 < this.langSysCount; ++var3) {
            this.langSysRecords[var3] = new LangSysRecord(var1);
         }
      }

      if (this.langSysCount > 0) {
         this.langSys = new LangSys[this.langSysCount];

         for(var3 = 0; var3 < this.langSysCount; ++var3) {
            var1.seek((long)(var2 + this.langSysRecords[var3].getOffset()));
            this.langSys[var3] = new LangSys(var1);
         }
      }

      if (this.defaultLangSysOffset > 0) {
         var1.seek((long)(var2 + this.defaultLangSysOffset));
         this.defaultLangSys = new LangSys(var1);
      }

   }

   public LangSys getDefaultLangSys() {
      return this.defaultLangSys;
   }
}

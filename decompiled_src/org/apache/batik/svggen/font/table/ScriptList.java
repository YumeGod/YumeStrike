package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ScriptList {
   private int scriptCount = 0;
   private ScriptRecord[] scriptRecords;
   private Script[] scripts;

   protected ScriptList(RandomAccessFile var1, int var2) throws IOException {
      var1.seek((long)var2);
      this.scriptCount = var1.readUnsignedShort();
      this.scriptRecords = new ScriptRecord[this.scriptCount];
      this.scripts = new Script[this.scriptCount];

      int var3;
      for(var3 = 0; var3 < this.scriptCount; ++var3) {
         this.scriptRecords[var3] = new ScriptRecord(var1);
      }

      for(var3 = 0; var3 < this.scriptCount; ++var3) {
         this.scripts[var3] = new Script(var1, var2 + this.scriptRecords[var3].getOffset());
      }

   }

   public int getScriptCount() {
      return this.scriptCount;
   }

   public ScriptRecord getScriptRecord(int var1) {
      return this.scriptRecords[var1];
   }

   public Script findScript(String var1) {
      if (var1.length() != 4) {
         return null;
      } else {
         int var2 = var1.charAt(0) << 24 | var1.charAt(1) << 16 | var1.charAt(2) << 8 | var1.charAt(3);

         for(int var3 = 0; var3 < this.scriptCount; ++var3) {
            if (this.scriptRecords[var3].getTag() == var2) {
               return this.scripts[var3];
            }
         }

         return null;
      }
   }
}

package org.apache.batik.gvt.font;

public class KerningTable {
   private Kern[] entries;

   public KerningTable(Kern[] var1) {
      this.entries = var1;
   }

   public float getKerningValue(int var1, int var2, String var3, String var4) {
      for(int var5 = 0; var5 < this.entries.length; ++var5) {
         if (this.entries[var5].matchesFirstGlyph(var1, var3) && this.entries[var5].matchesSecondGlyph(var2, var4)) {
            return this.entries[var5].getAdjustValue();
         }
      }

      return 0.0F;
   }
}

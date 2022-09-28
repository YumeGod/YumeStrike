package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class GsubTable implements Table, LookupSubtableFactory {
   private ScriptList scriptList;
   private FeatureList featureList;
   private LookupList lookupList;

   protected GsubTable(DirectoryEntry var1, RandomAccessFile var2) throws IOException {
      var2.seek((long)var1.getOffset());
      var2.readInt();
      int var3 = var2.readUnsignedShort();
      int var4 = var2.readUnsignedShort();
      int var5 = var2.readUnsignedShort();
      this.scriptList = new ScriptList(var2, var1.getOffset() + var3);
      this.featureList = new FeatureList(var2, var1.getOffset() + var4);
      this.lookupList = new LookupList(var2, var1.getOffset() + var5, this);
   }

   public LookupSubtable read(int var1, RandomAccessFile var2, int var3) throws IOException {
      Object var4 = null;
      switch (var1) {
         case 1:
            var4 = SingleSubst.read(var2, var3);
         case 2:
         case 3:
         case 5:
         case 6:
         default:
            break;
         case 4:
            var4 = LigatureSubst.read(var2, var3);
      }

      return (LookupSubtable)var4;
   }

   public int getType() {
      return 1196643650;
   }

   public ScriptList getScriptList() {
      return this.scriptList;
   }

   public FeatureList getFeatureList() {
      return this.featureList;
   }

   public LookupList getLookupList() {
      return this.lookupList;
   }

   public String toString() {
      return "GSUB";
   }
}

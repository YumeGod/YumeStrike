package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Lookup {
   public static final int IGNORE_BASE_GLYPHS = 2;
   public static final int IGNORE_BASE_LIGATURES = 4;
   public static final int IGNORE_BASE_MARKS = 8;
   public static final int MARK_ATTACHMENT_TYPE = 65280;
   private int type;
   private int flag;
   private int subTableCount;
   private int[] subTableOffsets;
   private LookupSubtable[] subTables;

   public Lookup(LookupSubtableFactory var1, RandomAccessFile var2, int var3) throws IOException {
      var2.seek((long)var3);
      this.type = var2.readUnsignedShort();
      this.flag = var2.readUnsignedShort();
      this.subTableCount = var2.readUnsignedShort();
      this.subTableOffsets = new int[this.subTableCount];
      this.subTables = new LookupSubtable[this.subTableCount];

      int var4;
      for(var4 = 0; var4 < this.subTableCount; ++var4) {
         this.subTableOffsets[var4] = var2.readUnsignedShort();
      }

      for(var4 = 0; var4 < this.subTableCount; ++var4) {
         this.subTables[var4] = var1.read(this.type, var2, var3 + this.subTableOffsets[var4]);
      }

   }

   public int getType() {
      return this.type;
   }

   public int getSubtableCount() {
      return this.subTableCount;
   }

   public LookupSubtable getSubtable(int var1) {
      return this.subTables[var1];
   }
}

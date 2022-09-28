package org.apache.batik.svggen.font.table;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class Program {
   private short[] instructions;

   public short[] getInstructions() {
      return this.instructions;
   }

   protected void readInstructions(RandomAccessFile var1, int var2) throws IOException {
      this.instructions = new short[var2];

      for(int var3 = 0; var3 < var2; ++var3) {
         this.instructions[var3] = (short)var1.readUnsignedByte();
      }

   }

   protected void readInstructions(ByteArrayInputStream var1, int var2) {
      this.instructions = new short[var2];

      for(int var3 = 0; var3 < var2; ++var3) {
         this.instructions[var3] = (short)var1.read();
      }

   }
}

package org.apache.batik.svggen.font.table;

import java.io.ByteArrayInputStream;

public class GlyfSimpleDescript extends GlyfDescript {
   private int[] endPtsOfContours;
   private byte[] flags;
   private short[] xCoordinates;
   private short[] yCoordinates;
   private int count;

   public GlyfSimpleDescript(GlyfTable var1, short var2, ByteArrayInputStream var3) {
      super(var1, var2, var3);
      this.endPtsOfContours = new int[var2];

      int var4;
      for(var4 = 0; var4 < var2; ++var4) {
         this.endPtsOfContours[var4] = var3.read() << 8 | var3.read();
      }

      this.count = this.endPtsOfContours[var2 - 1] + 1;
      this.flags = new byte[this.count];
      this.xCoordinates = new short[this.count];
      this.yCoordinates = new short[this.count];
      var4 = var3.read() << 8 | var3.read();
      this.readInstructions(var3, var4);
      this.readFlags(this.count, var3);
      this.readCoords(this.count, var3);
   }

   public int getEndPtOfContours(int var1) {
      return this.endPtsOfContours[var1];
   }

   public byte getFlags(int var1) {
      return this.flags[var1];
   }

   public short getXCoordinate(int var1) {
      return this.xCoordinates[var1];
   }

   public short getYCoordinate(int var1) {
      return this.yCoordinates[var1];
   }

   public boolean isComposite() {
      return false;
   }

   public int getPointCount() {
      return this.count;
   }

   public int getContourCount() {
      return this.getNumberOfContours();
   }

   private void readCoords(int var1, ByteArrayInputStream var2) {
      short var3 = 0;
      short var4 = 0;

      int var5;
      for(var5 = 0; var5 < var1; ++var5) {
         if ((this.flags[var5] & 16) != 0) {
            if ((this.flags[var5] & 2) != 0) {
               var3 += (short)var2.read();
            }
         } else if ((this.flags[var5] & 2) != 0) {
            var3 += (short)(-((short)var2.read()));
         } else {
            var3 += (short)(var2.read() << 8 | var2.read());
         }

         this.xCoordinates[var5] = var3;
      }

      for(var5 = 0; var5 < var1; ++var5) {
         if ((this.flags[var5] & 32) != 0) {
            if ((this.flags[var5] & 4) != 0) {
               var4 += (short)var2.read();
            }
         } else if ((this.flags[var5] & 4) != 0) {
            var4 += (short)(-((short)var2.read()));
         } else {
            var4 += (short)(var2.read() << 8 | var2.read());
         }

         this.yCoordinates[var5] = var4;
      }

   }

   private void readFlags(int var1, ByteArrayInputStream var2) {
      try {
         for(int var3 = 0; var3 < var1; ++var3) {
            this.flags[var3] = (byte)var2.read();
            if ((this.flags[var3] & 8) != 0) {
               int var4 = var2.read();

               for(int var5 = 1; var5 <= var4; ++var5) {
                  this.flags[var3 + var5] = this.flags[var3];
               }

               var3 += var4;
            }
         }
      } catch (ArrayIndexOutOfBoundsException var6) {
         System.out.println("error: array index out of bounds");
      }

   }
}

package org.apache.batik.svggen.font.table;

import java.io.ByteArrayInputStream;

public abstract class GlyfDescript extends Program implements GlyphDescription {
   public static final byte onCurve = 1;
   public static final byte xShortVector = 2;
   public static final byte yShortVector = 4;
   public static final byte repeat = 8;
   public static final byte xDual = 16;
   public static final byte yDual = 32;
   protected GlyfTable parentTable;
   private int numberOfContours;
   private short xMin;
   private short yMin;
   private short xMax;
   private short yMax;

   protected GlyfDescript(GlyfTable var1, short var2, ByteArrayInputStream var3) {
      this.parentTable = var1;
      this.numberOfContours = var2;
      this.xMin = (short)(var3.read() << 8 | var3.read());
      this.yMin = (short)(var3.read() << 8 | var3.read());
      this.xMax = (short)(var3.read() << 8 | var3.read());
      this.yMax = (short)(var3.read() << 8 | var3.read());
   }

   public void resolve() {
   }

   public int getNumberOfContours() {
      return this.numberOfContours;
   }

   public short getXMaximum() {
      return this.xMax;
   }

   public short getXMinimum() {
      return this.xMin;
   }

   public short getYMaximum() {
      return this.yMax;
   }

   public short getYMinimum() {
      return this.yMin;
   }
}

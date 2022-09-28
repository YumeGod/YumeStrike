package org.apache.batik.svggen.font.table;

import java.io.ByteArrayInputStream;

public class GlyfCompositeComp {
   public static final short ARG_1_AND_2_ARE_WORDS = 1;
   public static final short ARGS_ARE_XY_VALUES = 2;
   public static final short ROUND_XY_TO_GRID = 4;
   public static final short WE_HAVE_A_SCALE = 8;
   public static final short MORE_COMPONENTS = 32;
   public static final short WE_HAVE_AN_X_AND_Y_SCALE = 64;
   public static final short WE_HAVE_A_TWO_BY_TWO = 128;
   public static final short WE_HAVE_INSTRUCTIONS = 256;
   public static final short USE_MY_METRICS = 512;
   private int firstIndex;
   private int firstContour;
   private short argument1;
   private short argument2;
   private short flags;
   private int glyphIndex;
   private double xscale = 1.0;
   private double yscale = 1.0;
   private double scale01 = 0.0;
   private double scale10 = 0.0;
   private int xtranslate = 0;
   private int ytranslate = 0;
   private int point1 = 0;
   private int point2 = 0;

   protected GlyfCompositeComp(ByteArrayInputStream var1) {
      this.flags = (short)(var1.read() << 8 | var1.read());
      this.glyphIndex = var1.read() << 8 | var1.read();
      if ((this.flags & 1) != 0) {
         this.argument1 = (short)(var1.read() << 8 | var1.read());
         this.argument2 = (short)(var1.read() << 8 | var1.read());
      } else {
         this.argument1 = (short)var1.read();
         this.argument2 = (short)var1.read();
      }

      if ((this.flags & 2) != 0) {
         this.xtranslate = this.argument1;
         this.ytranslate = this.argument2;
      } else {
         this.point1 = this.argument1;
         this.point2 = this.argument2;
      }

      short var2;
      if ((this.flags & 8) != 0) {
         var2 = (short)(var1.read() << 8 | var1.read());
         this.xscale = this.yscale = (double)var2 / 16384.0;
      } else if ((this.flags & 64) != 0) {
         var2 = (short)(var1.read() << 8 | var1.read());
         this.xscale = (double)var2 / 16384.0;
         var2 = (short)(var1.read() << 8 | var1.read());
         this.yscale = (double)var2 / 16384.0;
      } else if ((this.flags & 128) != 0) {
         var2 = (short)(var1.read() << 8 | var1.read());
         this.xscale = (double)var2 / 16384.0;
         var2 = (short)(var1.read() << 8 | var1.read());
         this.scale01 = (double)var2 / 16384.0;
         var2 = (short)(var1.read() << 8 | var1.read());
         this.scale10 = (double)var2 / 16384.0;
         var2 = (short)(var1.read() << 8 | var1.read());
         this.yscale = (double)var2 / 16384.0;
      }

   }

   public void setFirstIndex(int var1) {
      this.firstIndex = var1;
   }

   public int getFirstIndex() {
      return this.firstIndex;
   }

   public void setFirstContour(int var1) {
      this.firstContour = var1;
   }

   public int getFirstContour() {
      return this.firstContour;
   }

   public short getArgument1() {
      return this.argument1;
   }

   public short getArgument2() {
      return this.argument2;
   }

   public short getFlags() {
      return this.flags;
   }

   public int getGlyphIndex() {
      return this.glyphIndex;
   }

   public double getScale01() {
      return this.scale01;
   }

   public double getScale10() {
      return this.scale10;
   }

   public double getXScale() {
      return this.xscale;
   }

   public double getYScale() {
      return this.yscale;
   }

   public int getXTranslate() {
      return this.xtranslate;
   }

   public int getYTranslate() {
      return this.ytranslate;
   }

   public int scaleX(int var1, int var2) {
      return Math.round((float)((double)var1 * this.xscale + (double)var2 * this.scale10));
   }

   public int scaleY(int var1, int var2) {
      return Math.round((float)((double)var1 * this.scale01 + (double)var2 * this.yscale));
   }
}

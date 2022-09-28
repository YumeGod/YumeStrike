package org.apache.batik.gvt.flow;

import java.awt.geom.Point2D;
import org.apache.batik.gvt.font.GVTGlyphVector;

public class LineInfo {
   FlowRegions fr;
   double lineHeight = -1.0;
   double ascent = -1.0;
   double descent = -1.0;
   double hLeading = -1.0;
   double baseline;
   int numGlyphs;
   int words = 0;
   int size = 0;
   GlyphGroupInfo[] ggis = null;
   int newSize = 0;
   GlyphGroupInfo[] newGGIS = null;
   int numRanges;
   double[] ranges;
   double[] rangeAdv;
   BlockInfo bi = null;
   boolean paraStart;
   boolean paraEnd;
   protected static final int FULL_WORD = 0;
   protected static final int FULL_ADV = 1;
   static final float MAX_COMPRESS = 0.1F;
   static final float COMRESS_SCALE = 3.0F;

   public LineInfo(FlowRegions var1, BlockInfo var2, boolean var3) {
      this.fr = var1;
      this.bi = var2;
      this.lineHeight = (double)var2.getLineHeight();
      this.ascent = (double)var2.getAscent();
      this.descent = (double)var2.getDescent();
      this.hLeading = (this.lineHeight - (this.ascent + this.descent)) / 2.0;
      this.baseline = (double)((float)(var1.getCurrentY() + this.hLeading + this.ascent));
      this.paraStart = var3;
      this.paraEnd = false;
      if (this.lineHeight > 0.0) {
         var1.newLineHeight(this.lineHeight);
         this.updateRangeInfo();
      }

   }

   public void setParaEnd(boolean var1) {
      this.paraEnd = var1;
   }

   public boolean addWord(WordInfo var1) {
      double var2 = (double)var1.getLineHeight();
      if (var2 <= this.lineHeight) {
         return this.insertWord(var1);
      } else {
         this.fr.newLineHeight(var2);
         if (!this.updateRangeInfo()) {
            if (this.lineHeight > 0.0) {
               this.fr.newLineHeight(this.lineHeight);
            }

            return false;
         } else if (!this.insertWord(var1)) {
            if (this.lineHeight > 0.0) {
               this.setLineHeight(this.lineHeight);
            }

            return false;
         } else {
            this.lineHeight = var2;
            if ((double)var1.getAscent() > this.ascent) {
               this.ascent = (double)var1.getAscent();
            }

            if ((double)var1.getDescent() > this.descent) {
               this.descent = (double)var1.getDescent();
            }

            this.hLeading = (var2 - (this.ascent + this.descent)) / 2.0;
            this.baseline = (double)((float)(this.fr.getCurrentY() + this.hLeading + this.ascent));
            return true;
         }
      }
   }

   public boolean insertWord(WordInfo var1) {
      this.mergeGlyphGroups(var1);
      if (!this.assignGlyphGroupRanges(this.newSize, this.newGGIS)) {
         return false;
      } else {
         this.swapGlyphGroupInfo();
         return true;
      }
   }

   public boolean assignGlyphGroupRanges(int var1, GlyphGroupInfo[] var2) {
      int var3 = 0;
      int var4 = 0;

      do {
         if (var4 >= this.numRanges) {
            return false;
         }

         double var5 = this.ranges[2 * var4 + 1] - this.ranges[2 * var4];
         float var7 = 0.0F;

         float var8;
         GlyphGroupInfo var9;
         for(var8 = 0.0F; var3 < var1; var8 += var7) {
            var9 = var2[var3];
            var9.setRange(var4);
            var7 = var9.getAdvance();
            double var10 = var5 - (double)(var8 + var7);
            if (var10 < 0.0) {
               break;
            }

            ++var3;
         }

         if (var3 == var1) {
            --var3;
            var8 -= var7;
         }

         var9 = var2[var3];

         float var12;
         for(var12 = var9.getLastAdvance(); (double)(var8 + var12) > var5; var12 = var9.getLastAdvance()) {
            --var3;
            var12 = 0.0F;
            if (var3 < 0) {
               break;
            }

            var9 = var2[var3];
            if (var4 != var9.getRange()) {
               break;
            }

            var8 -= var9.getAdvance();
         }

         ++var3;
         this.rangeAdv[var4] = (double)(var8 + var12);
         ++var4;
      } while(var3 != var1);

      return true;
   }

   public boolean setLineHeight(double var1) {
      this.fr.newLineHeight(var1);
      if (this.updateRangeInfo()) {
         this.lineHeight = var1;
         return true;
      } else {
         if (this.lineHeight > 0.0) {
            this.fr.newLineHeight(this.lineHeight);
         }

         return false;
      }
   }

   public double getCurrentY() {
      return this.fr.getCurrentY();
   }

   public boolean gotoY(double var1) {
      if (this.fr.gotoY(var1)) {
         return true;
      } else {
         if (this.lineHeight > 0.0) {
            this.updateRangeInfo();
         }

         this.baseline = (double)((float)(this.fr.getCurrentY() + this.hLeading + this.ascent));
         return false;
      }
   }

   protected boolean updateRangeInfo() {
      this.fr.resetRange();
      int var1 = this.fr.getNumRangeOnLine();
      if (var1 == 0) {
         return false;
      } else {
         this.numRanges = var1;
         int var2;
         if (this.ranges == null) {
            this.rangeAdv = new double[this.numRanges];
            this.ranges = new double[2 * this.numRanges];
         } else if (this.numRanges > this.rangeAdv.length) {
            var2 = 2 * this.rangeAdv.length;
            if (var2 < this.numRanges) {
               var2 = this.numRanges;
            }

            this.rangeAdv = new double[var2];
            this.ranges = new double[2 * var2];
         }

         for(var2 = 0; var2 < this.numRanges; ++var2) {
            double[] var3 = this.fr.nextRange();
            double var4 = var3[0];
            double var6;
            if (var2 == 0) {
               var6 = (double)this.bi.getLeftMargin();
               if (this.paraStart) {
                  double var8 = (double)this.bi.getIndent();
                  if (var6 < -var8) {
                     var6 = 0.0;
                  } else {
                     var6 += var8;
                  }
               }

               var4 += var6;
            }

            var6 = var3[1];
            if (var2 == this.numRanges - 1) {
               var6 -= (double)this.bi.getRightMargin();
            }

            this.ranges[2 * var2] = var4;
            this.ranges[2 * var2 + 1] = var6;
         }

         return true;
      }
   }

   protected void swapGlyphGroupInfo() {
      GlyphGroupInfo[] var1 = this.ggis;
      this.ggis = this.newGGIS;
      this.newGGIS = var1;
      this.size = this.newSize;
      this.newSize = 0;
   }

   protected void mergeGlyphGroups(WordInfo var1) {
      int var2 = var1.getNumGlyphGroups();
      this.newSize = 0;
      int var3;
      if (this.ggis == null) {
         this.newSize = var2;
         this.newGGIS = new GlyphGroupInfo[var2];

         for(var3 = 0; var3 < var2; ++var3) {
            this.newGGIS[var3] = var1.getGlyphGroup(var3);
         }
      } else {
         var3 = 0;
         int var4 = 0;
         GlyphGroupInfo var5 = var1.getGlyphGroup(var4);
         int var6 = var5.getStart();
         GlyphGroupInfo var7 = this.ggis[this.size - 1];
         int var8 = var7.getStart();
         this.newGGIS = assureSize(this.newGGIS, this.size + var2);
         if (var6 < var8) {
            var7 = this.ggis[var3];
            var8 = var7.getStart();

            while(var3 < this.size && var4 < var2) {
               if (var6 < var8) {
                  this.newGGIS[this.newSize++] = var5;
                  ++var4;
                  if (var4 < var2) {
                     var5 = var1.getGlyphGroup(var4);
                     var6 = var5.getStart();
                  }
               } else {
                  this.newGGIS[this.newSize++] = var7;
                  ++var3;
                  if (var3 < this.size) {
                     var7 = this.ggis[var3];
                     var8 = var7.getStart();
                  }
               }
            }
         }

         while(var3 < this.size) {
            this.newGGIS[this.newSize++] = this.ggis[var3++];
         }

         while(var4 < var2) {
            this.newGGIS[this.newSize++] = var1.getGlyphGroup(var4++);
         }
      }

   }

   public void layout() {
      if (this.size != 0) {
         this.assignGlyphGroupRanges(this.size, this.ggis);
         GVTGlyphVector var1 = this.ggis[0].getGlyphVector();
         boolean var2 = false;
         double var3 = 0.0;
         double var5 = 0.0;
         int[] var7 = new int[this.numRanges];
         int[] var8 = new int[this.numRanges];
         GlyphGroupInfo[] var9 = new GlyphGroupInfo[this.numRanges];
         GlyphGroupInfo var10 = this.ggis[0];
         int var11 = var10.getRange();
         int var10002 = var7[var11]++;
         var8[var11] += var10.getGlyphCount();

         int var12;
         for(var12 = 1; var12 < this.size; ++var12) {
            var10 = this.ggis[var12];
            var11 = var10.getRange();
            if (var9[var11] == null || !var9[var11].getHideLast()) {
               var10002 = var7[var11]++;
            }

            var9[var11] = var10;
            var8[var11] += var10.getGlyphCount();
            GlyphGroupInfo var13 = this.ggis[var12 - 1];
            int var14 = var13.getRange();
            if (var11 != var14) {
               var8[var14] += var13.getLastGlyphCount() - var13.getGlyphCount();
            }
         }

         var8[var11] += var10.getLastGlyphCount() - var10.getGlyphCount();
         var12 = -1;
         double var33 = 0.0;
         double var15 = 0.0;
         double var17 = 0.0;
         boolean var32 = true;
         var10 = null;

         for(int var19 = 0; var19 < this.size; ++var19) {
            GlyphGroupInfo var20 = var10;
            int var21 = var12;
            var10 = this.ggis[var19];
            var12 = var10.getRange();
            int var22;
            if (var12 != var21) {
               var33 = this.ranges[2 * var12];
               var15 = this.ranges[2 * var12 + 1] - var33;
               var17 = this.rangeAdv[var12];
               var22 = this.bi.getTextAlignment();
               if (this.paraEnd && var22 == 3) {
                  var22 = 0;
               }

               switch (var22) {
                  case 0:
                     break;
                  case 1:
                     var33 += (var15 - var17) / 2.0;
                     break;
                  case 2:
                     var33 += var15 - var17;
                     break;
                  case 3:
                  default:
                     double var23 = var15 - var17;
                     int var25;
                     if (!var2) {
                        var25 = var7[var12] - 1;
                        if (var25 >= 1) {
                           var3 = var23 / (double)var25;
                        }
                     } else {
                        var25 = var8[var12] - 1;
                        if (var25 >= 1) {
                           var5 = var23 / (double)var25;
                        }
                     }
               }
            } else if (var20 != null && var20.getHideLast()) {
               var1.setGlyphVisible(var20.getEnd(), false);
            }

            var22 = var10.getStart();
            int var34 = var10.getEnd();
            boolean[] var24 = var10.getHide();
            Point2D var35 = var1.getGlyphPosition(var22);
            double var26 = var35.getX();
            double var28 = 0.0;

            for(int var30 = var22; var30 <= var34; ++var30) {
               Point2D var31 = var1.getGlyphPosition(var30 + 1);
               if (var24[var30 - var22]) {
                  var1.setGlyphVisible(var30, false);
                  var28 += var31.getX() - var35.getX();
               } else {
                  var1.setGlyphVisible(var30, true);
               }

               var35.setLocation(var35.getX() - var26 - var28 + var33, var35.getY() + this.baseline);
               var1.setGlyphPosition(var30, var35);
               var35 = var31;
               var28 -= var5;
            }

            if (var10.getHideLast()) {
               var33 += (double)var10.getAdvance() - var28;
            } else {
               var33 += (double)var10.getAdvance() - var28 + var3;
            }
         }

      }
   }

   public static GlyphGroupInfo[] assureSize(GlyphGroupInfo[] var0, int var1) {
      if (var0 == null) {
         if (var1 < 10) {
            var1 = 10;
         }

         return new GlyphGroupInfo[var1];
      } else if (var1 <= var0.length) {
         return var0;
      } else {
         int var2 = var0.length * 2;
         if (var2 < var1) {
            var2 = var1;
         }

         return new GlyphGroupInfo[var2];
      }
   }
}

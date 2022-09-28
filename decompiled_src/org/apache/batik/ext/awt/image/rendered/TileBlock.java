package org.apache.batik.ext.awt.image.rendered;

import java.util.ArrayList;

public class TileBlock {
   int occX;
   int occY;
   int occW;
   int occH;
   int xOff;
   int yOff;
   int w;
   int h;
   int benefit;
   boolean[] occupied;

   TileBlock(int var1, int var2, int var3, int var4, boolean[] var5, int var6, int var7, int var8, int var9) {
      this.occX = var1;
      this.occY = var2;
      this.occW = var3;
      this.occH = var4;
      this.xOff = var6;
      this.yOff = var7;
      this.w = var8;
      this.h = var9;
      this.occupied = var5;

      for(int var10 = 0; var10 < var9; ++var10) {
         for(int var11 = 0; var11 < var8; ++var11) {
            if (!var5[var11 + var6 + var3 * (var10 + var7)]) {
               ++this.benefit;
            }
         }
      }

   }

   public String toString() {
      String var1 = "";

      for(int var2 = 0; var2 < this.occH; ++var2) {
         for(int var3 = 0; var3 < this.occW + 1; ++var3) {
            if (var3 != this.xOff && var3 != this.xOff + this.w) {
               if (var2 == this.yOff && var3 > this.xOff && var3 < this.xOff + this.w) {
                  var1 = var1 + "-";
               } else if (var2 == this.yOff + this.h - 1 && var3 > this.xOff && var3 < this.xOff + this.w) {
                  var1 = var1 + "_";
               } else {
                  var1 = var1 + " ";
               }
            } else if (var2 != this.yOff && var2 != this.yOff + this.h - 1) {
               if (var2 > this.yOff && var2 < this.yOff + this.h - 1) {
                  var1 = var1 + "|";
               } else {
                  var1 = var1 + " ";
               }
            } else {
               var1 = var1 + "+";
            }

            if (var3 != this.occW) {
               if (this.occupied[var3 + var2 * this.occW]) {
                  var1 = var1 + "*";
               } else {
                  var1 = var1 + ".";
               }
            }
         }

         var1 = var1 + "\n";
      }

      return var1;
   }

   int getXLoc() {
      return this.occX + this.xOff;
   }

   int getYLoc() {
      return this.occY + this.yOff;
   }

   int getWidth() {
      return this.w;
   }

   int getHeight() {
      return this.h;
   }

   int getBenefit() {
      return this.benefit;
   }

   int getWork() {
      return this.w * this.h + 1;
   }

   static int getWork(TileBlock[] var0) {
      int var1 = 0;

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1 += var0[var2].getWork();
      }

      return var1;
   }

   TileBlock[] getBestSplit() {
      if (this.simplify()) {
         return null;
      } else {
         return this.benefit == this.w * this.h ? new TileBlock[]{this} : this.splitOneGo();
      }
   }

   public TileBlock[] splitOneGo() {
      boolean[] var1 = (boolean[])this.occupied.clone();
      ArrayList var2 = new ArrayList();

      for(int var3 = this.yOff; var3 < this.yOff + this.h; ++var3) {
         for(int var4 = this.xOff; var4 < this.xOff + this.w; ++var4) {
            if (!var1[var4 + var3 * this.occW]) {
               int var5 = this.xOff + this.w - var4;

               int var6;
               for(var6 = var4; var6 < var4 + var5; ++var6) {
                  if (var1[var6 + var3 * this.occW]) {
                     var5 = var6 - var4;
                  } else {
                     var1[var6 + var3 * this.occW] = true;
                  }
               }

               var6 = 1;

               for(int var7 = var3 + 1; var7 < this.yOff + this.h; ++var7) {
                  int var8;
                  for(var8 = var4; var8 < var4 + var5 && !var1[var8 + var7 * this.occW]; ++var8) {
                  }

                  if (var8 != var4 + var5) {
                     break;
                  }

                  for(var8 = var4; var8 < var4 + var5; ++var8) {
                     var1[var8 + var7 * this.occW] = true;
                  }

                  ++var6;
               }

               var2.add(new TileBlock(this.occX, this.occY, this.occW, this.occH, this.occupied, var4, var3, var5, var6));
               var4 += var5 - 1;
            }
         }
      }

      TileBlock[] var9 = new TileBlock[var2.size()];
      var2.toArray(var9);
      return var9;
   }

   public boolean simplify() {
      boolean[] var1 = this.occupied;

      int var2;
      int var3;
      for(var2 = 0; var2 < this.h; ++var2) {
         for(var3 = 0; var3 < this.w && var1[var3 + this.xOff + this.occW * (var2 + this.yOff)]; ++var3) {
         }

         if (var3 != this.w) {
            break;
         }

         ++this.yOff;
         --var2;
         --this.h;
      }

      if (this.h == 0) {
         return true;
      } else {
         for(var2 = this.h - 1; var2 >= 0; --var2) {
            for(var3 = 0; var3 < this.w && var1[var3 + this.xOff + this.occW * (var2 + this.yOff)]; ++var3) {
            }

            if (var3 != this.w) {
               break;
            }

            --this.h;
         }

         for(var2 = 0; var2 < this.w; ++var2) {
            for(var3 = 0; var3 < this.h && var1[var2 + this.xOff + this.occW * (var3 + this.yOff)]; ++var3) {
            }

            if (var3 != this.h) {
               break;
            }

            ++this.xOff;
            --var2;
            --this.w;
         }

         for(var2 = this.w - 1; var2 >= 0; --var2) {
            for(var3 = 0; var3 < this.h && var1[var2 + this.xOff + this.occW * (var3 + this.yOff)]; ++var3) {
            }

            if (var3 != this.h) {
               break;
            }

            --this.w;
         }

         return false;
      }
   }
}

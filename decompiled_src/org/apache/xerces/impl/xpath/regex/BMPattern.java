package org.apache.xerces.impl.xpath.regex;

import java.text.CharacterIterator;

public class BMPattern {
   char[] pattern;
   int[] shiftTable;
   boolean ignoreCase;

   public BMPattern(String var1, boolean var2) {
      this(var1, 256, var2);
   }

   public BMPattern(String var1, int var2, boolean var3) {
      this.pattern = var1.toCharArray();
      this.shiftTable = new int[var2];
      this.ignoreCase = var3;
      int var4 = this.pattern.length;

      for(int var5 = 0; var5 < this.shiftTable.length; ++var5) {
         this.shiftTable[var5] = var4;
      }

      for(int var6 = 0; var6 < var4; ++var6) {
         char var7 = this.pattern[var6];
         int var8 = var4 - var6 - 1;
         int var9 = var7 % this.shiftTable.length;
         if (var8 < this.shiftTable[var9]) {
            this.shiftTable[var9] = var8;
         }

         if (this.ignoreCase) {
            var7 = Character.toUpperCase(var7);
            var9 = var7 % this.shiftTable.length;
            if (var8 < this.shiftTable[var9]) {
               this.shiftTable[var9] = var8;
            }

            var7 = Character.toLowerCase(var7);
            var9 = var7 % this.shiftTable.length;
            if (var8 < this.shiftTable[var9]) {
               this.shiftTable[var9] = var8;
            }
         }
      }

   }

   public int matches(CharacterIterator var1, int var2, int var3) {
      if (this.ignoreCase) {
         return this.matchesIgnoreCase(var1, var2, var3);
      } else {
         int var4 = this.pattern.length;
         if (var4 == 0) {
            return var2;
         } else {
            int var5 = var2 + var4;

            while(var5 <= var3) {
               int var6 = var4;
               int var7 = var5 + 1;

               char var8;
               do {
                  --var5;
                  char var10000 = var8 = var1.setIndex(var5);
                  --var6;
                  if (var10000 != this.pattern[var6]) {
                     break;
                  }

                  if (var6 == 0) {
                     return var5;
                  }
               } while(var6 > 0);

               var5 += this.shiftTable[var8 % this.shiftTable.length] + 1;
               if (var5 < var7) {
                  var5 = var7;
               }
            }

            return -1;
         }
      }
   }

   public int matches(String var1, int var2, int var3) {
      if (this.ignoreCase) {
         return this.matchesIgnoreCase(var1, var2, var3);
      } else {
         int var4 = this.pattern.length;
         if (var4 == 0) {
            return var2;
         } else {
            int var5 = var2 + var4;

            while(var5 <= var3) {
               int var6 = var4;
               int var7 = var5 + 1;

               char var8;
               do {
                  --var5;
                  char var10000 = var8 = var1.charAt(var5);
                  --var6;
                  if (var10000 != this.pattern[var6]) {
                     break;
                  }

                  if (var6 == 0) {
                     return var5;
                  }
               } while(var6 > 0);

               var5 += this.shiftTable[var8 % this.shiftTable.length] + 1;
               if (var5 < var7) {
                  var5 = var7;
               }
            }

            return -1;
         }
      }
   }

   public int matches(char[] var1, int var2, int var3) {
      if (this.ignoreCase) {
         return this.matchesIgnoreCase(var1, var2, var3);
      } else {
         int var4 = this.pattern.length;
         if (var4 == 0) {
            return var2;
         } else {
            int var5 = var2 + var4;

            while(var5 <= var3) {
               int var6 = var4;
               int var7 = var5 + 1;

               char var8;
               do {
                  --var5;
                  char var10000 = var8 = var1[var5];
                  --var6;
                  if (var10000 != this.pattern[var6]) {
                     break;
                  }

                  if (var6 == 0) {
                     return var5;
                  }
               } while(var6 > 0);

               var5 += this.shiftTable[var8 % this.shiftTable.length] + 1;
               if (var5 < var7) {
                  var5 = var7;
               }
            }

            return -1;
         }
      }
   }

   int matchesIgnoreCase(CharacterIterator var1, int var2, int var3) {
      int var4 = this.pattern.length;
      if (var4 == 0) {
         return var2;
      } else {
         int var5 = var2 + var4;

         while(var5 <= var3) {
            int var6 = var4;
            int var7 = var5 + 1;

            char var8;
            do {
               --var5;
               char var9 = var8 = var1.setIndex(var5);
               --var6;
               char var10 = this.pattern[var6];
               if (var9 != var10) {
                  var9 = Character.toUpperCase(var9);
                  var10 = Character.toUpperCase(var10);
                  if (var9 != var10 && Character.toLowerCase(var9) != Character.toLowerCase(var10)) {
                     break;
                  }
               }

               if (var6 == 0) {
                  return var5;
               }
            } while(var6 > 0);

            var5 += this.shiftTable[var8 % this.shiftTable.length] + 1;
            if (var5 < var7) {
               var5 = var7;
            }
         }

         return -1;
      }
   }

   int matchesIgnoreCase(String var1, int var2, int var3) {
      int var4 = this.pattern.length;
      if (var4 == 0) {
         return var2;
      } else {
         int var5 = var2 + var4;

         while(var5 <= var3) {
            int var6 = var4;
            int var7 = var5 + 1;

            char var8;
            do {
               --var5;
               char var9 = var8 = var1.charAt(var5);
               --var6;
               char var10 = this.pattern[var6];
               if (var9 != var10) {
                  var9 = Character.toUpperCase(var9);
                  var10 = Character.toUpperCase(var10);
                  if (var9 != var10 && Character.toLowerCase(var9) != Character.toLowerCase(var10)) {
                     break;
                  }
               }

               if (var6 == 0) {
                  return var5;
               }
            } while(var6 > 0);

            var5 += this.shiftTable[var8 % this.shiftTable.length] + 1;
            if (var5 < var7) {
               var5 = var7;
            }
         }

         return -1;
      }
   }

   int matchesIgnoreCase(char[] var1, int var2, int var3) {
      int var4 = this.pattern.length;
      if (var4 == 0) {
         return var2;
      } else {
         int var5 = var2 + var4;

         while(var5 <= var3) {
            int var6 = var4;
            int var7 = var5 + 1;

            char var8;
            do {
               --var5;
               char var9 = var8 = var1[var5];
               --var6;
               char var10 = this.pattern[var6];
               if (var9 != var10) {
                  var9 = Character.toUpperCase(var9);
                  var10 = Character.toUpperCase(var10);
                  if (var9 != var10 && Character.toLowerCase(var9) != Character.toLowerCase(var10)) {
                     break;
                  }
               }

               if (var6 == 0) {
                  return var5;
               }
            } while(var6 > 0);

            var5 += this.shiftTable[var8 % this.shiftTable.length] + 1;
            if (var5 < var7) {
               var5 = var7;
            }
         }

         return -1;
      }
   }
}

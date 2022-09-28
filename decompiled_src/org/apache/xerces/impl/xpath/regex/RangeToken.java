package org.apache.xerces.impl.xpath.regex;

import java.io.Serializable;

final class RangeToken extends Token implements Serializable {
   private static final long serialVersionUID = 3257568399592010545L;
   int[] ranges;
   boolean sorted;
   boolean compacted;
   RangeToken icaseCache = null;
   int[] map = null;
   int nonMapIndex;
   private static final int MAPSIZE = 256;

   RangeToken(int var1) {
      super(var1);
      this.setSorted(false);
   }

   protected void addRange(int var1, int var2) {
      this.icaseCache = null;
      int var3;
      int var4;
      if (var1 <= var2) {
         var3 = var1;
         var4 = var2;
      } else {
         var3 = var2;
         var4 = var1;
      }

      boolean var5 = false;
      if (this.ranges == null) {
         this.ranges = new int[2];
         this.ranges[0] = var3;
         this.ranges[1] = var4;
         this.setSorted(true);
      } else {
         int var7 = this.ranges.length;
         if (this.ranges[var7 - 1] + 1 == var3) {
            this.ranges[var7 - 1] = var4;
            return;
         }

         int[] var6 = new int[var7 + 2];
         System.arraycopy(this.ranges, 0, var6, 0, var7);
         this.ranges = var6;
         if (this.ranges[var7 - 1] >= var3) {
            this.setSorted(false);
         }

         this.ranges[var7++] = var3;
         this.ranges[var7] = var4;
         if (!this.sorted) {
            this.sortRanges();
         }
      }

   }

   private final boolean isSorted() {
      return this.sorted;
   }

   private final void setSorted(boolean var1) {
      this.sorted = var1;
      if (!var1) {
         this.compacted = false;
      }

   }

   private final boolean isCompacted() {
      return this.compacted;
   }

   private final void setCompacted() {
      this.compacted = true;
   }

   protected void sortRanges() {
      if (!this.isSorted()) {
         if (this.ranges != null) {
            for(int var1 = this.ranges.length - 4; var1 >= 0; var1 -= 2) {
               for(int var2 = 0; var2 <= var1; var2 += 2) {
                  if (this.ranges[var2] > this.ranges[var2 + 2] || this.ranges[var2] == this.ranges[var2 + 2] && this.ranges[var2 + 1] > this.ranges[var2 + 3]) {
                     int var3 = this.ranges[var2 + 2];
                     this.ranges[var2 + 2] = this.ranges[var2];
                     this.ranges[var2] = var3;
                     var3 = this.ranges[var2 + 3];
                     this.ranges[var2 + 3] = this.ranges[var2 + 1];
                     this.ranges[var2 + 1] = var3;
                  }
               }
            }

            this.setSorted(true);
         }
      }
   }

   protected void compactRanges() {
      boolean var1 = false;
      if (this.ranges != null && this.ranges.length > 2) {
         if (!this.isCompacted()) {
            int var2 = 0;

            for(int var3 = 0; var3 < this.ranges.length; var2 += 2) {
               if (var2 != var3) {
                  this.ranges[var2] = this.ranges[var3++];
                  this.ranges[var2 + 1] = this.ranges[var3++];
               } else {
                  var3 += 2;
               }

               int var4 = this.ranges[var2 + 1];

               while(var3 < this.ranges.length && var4 + 1 >= this.ranges[var3]) {
                  if (var4 + 1 == this.ranges[var3]) {
                     if (var1) {
                        System.err.println("Token#compactRanges(): Compaction: [" + this.ranges[var2] + ", " + this.ranges[var2 + 1] + "], [" + this.ranges[var3] + ", " + this.ranges[var3 + 1] + "] -> [" + this.ranges[var2] + ", " + this.ranges[var3 + 1] + "]");
                     }

                     this.ranges[var2 + 1] = this.ranges[var3 + 1];
                     var4 = this.ranges[var2 + 1];
                     var3 += 2;
                  } else if (var4 >= this.ranges[var3 + 1]) {
                     if (var1) {
                        System.err.println("Token#compactRanges(): Compaction: [" + this.ranges[var2] + ", " + this.ranges[var2 + 1] + "], [" + this.ranges[var3] + ", " + this.ranges[var3 + 1] + "] -> [" + this.ranges[var2] + ", " + this.ranges[var2 + 1] + "]");
                     }

                     var3 += 2;
                  } else {
                     if (var4 >= this.ranges[var3 + 1]) {
                        throw new RuntimeException("Token#compactRanges(): Internel Error: [" + this.ranges[var2] + "," + this.ranges[var2 + 1] + "] [" + this.ranges[var3] + "," + this.ranges[var3 + 1] + "]");
                     }

                     if (var1) {
                        System.err.println("Token#compactRanges(): Compaction: [" + this.ranges[var2] + ", " + this.ranges[var2 + 1] + "], [" + this.ranges[var3] + ", " + this.ranges[var3 + 1] + "] -> [" + this.ranges[var2] + ", " + this.ranges[var3 + 1] + "]");
                     }

                     this.ranges[var2 + 1] = this.ranges[var3 + 1];
                     var4 = this.ranges[var2 + 1];
                     var3 += 2;
                  }
               }
            }

            if (var2 != this.ranges.length) {
               int[] var5 = new int[var2];
               System.arraycopy(this.ranges, 0, var5, 0, var2);
               this.ranges = var5;
            }

            this.setCompacted();
         }
      }
   }

   protected void mergeRanges(Token var1) {
      RangeToken var2 = (RangeToken)var1;
      this.sortRanges();
      var2.sortRanges();
      if (var2.ranges != null) {
         this.icaseCache = null;
         this.setSorted(true);
         if (this.ranges == null) {
            this.ranges = new int[var2.ranges.length];
            System.arraycopy(var2.ranges, 0, this.ranges, 0, var2.ranges.length);
         } else {
            int[] var3 = new int[this.ranges.length + var2.ranges.length];
            int var4 = 0;
            int var5 = 0;
            int var6 = 0;

            while(true) {
               while(var4 < this.ranges.length || var5 < var2.ranges.length) {
                  if (var4 >= this.ranges.length) {
                     var3[var6++] = var2.ranges[var5++];
                     var3[var6++] = var2.ranges[var5++];
                  } else if (var5 >= var2.ranges.length) {
                     var3[var6++] = this.ranges[var4++];
                     var3[var6++] = this.ranges[var4++];
                  } else if (var2.ranges[var5] >= this.ranges[var4] && (var2.ranges[var5] != this.ranges[var4] || var2.ranges[var5 + 1] >= this.ranges[var4 + 1])) {
                     var3[var6++] = this.ranges[var4++];
                     var3[var6++] = this.ranges[var4++];
                  } else {
                     var3[var6++] = var2.ranges[var5++];
                     var3[var6++] = var2.ranges[var5++];
                  }
               }

               this.ranges = var3;
               return;
            }
         }
      }
   }

   protected void subtractRanges(Token var1) {
      if (var1.type == 5) {
         this.intersectRanges(var1);
      } else {
         RangeToken var2 = (RangeToken)var1;
         if (var2.ranges != null && this.ranges != null) {
            this.icaseCache = null;
            this.sortRanges();
            this.compactRanges();
            var2.sortRanges();
            var2.compactRanges();
            int[] var3 = new int[this.ranges.length + var2.ranges.length];
            int var4 = 0;
            int var5 = 0;
            int var6 = 0;

            while(var5 < this.ranges.length && var6 < var2.ranges.length) {
               int var7 = this.ranges[var5];
               int var8 = this.ranges[var5 + 1];
               int var9 = var2.ranges[var6];
               int var10 = var2.ranges[var6 + 1];
               if (var8 < var9) {
                  var3[var4++] = this.ranges[var5++];
                  var3[var4++] = this.ranges[var5++];
               } else if (var8 >= var9 && var7 <= var10) {
                  if (var9 <= var7 && var8 <= var10) {
                     var5 += 2;
                  } else if (var9 <= var7) {
                     this.ranges[var5] = var10 + 1;
                     var6 += 2;
                  } else if (var8 <= var10) {
                     var3[var4++] = var7;
                     var3[var4++] = var9 - 1;
                     var5 += 2;
                  } else {
                     var3[var4++] = var7;
                     var3[var4++] = var9 - 1;
                     this.ranges[var5] = var10 + 1;
                     var6 += 2;
                  }
               } else {
                  if (var10 >= var7) {
                     throw new RuntimeException("Token#subtractRanges(): Internal Error: [" + this.ranges[var5] + "," + this.ranges[var5 + 1] + "] - [" + var2.ranges[var6] + "," + var2.ranges[var6 + 1] + "]");
                  }

                  var6 += 2;
               }
            }

            while(var5 < this.ranges.length) {
               var3[var4++] = this.ranges[var5++];
               var3[var4++] = this.ranges[var5++];
            }

            this.ranges = new int[var4];
            System.arraycopy(var3, 0, this.ranges, 0, var4);
         }
      }
   }

   protected void intersectRanges(Token var1) {
      RangeToken var2 = (RangeToken)var1;
      if (var2.ranges != null && this.ranges != null) {
         this.icaseCache = null;
         this.sortRanges();
         this.compactRanges();
         var2.sortRanges();
         var2.compactRanges();
         int[] var3 = new int[this.ranges.length + var2.ranges.length];
         int var4 = 0;
         int var5 = 0;
         int var6 = 0;

         while(var5 < this.ranges.length && var6 < var2.ranges.length) {
            int var7 = this.ranges[var5];
            int var8 = this.ranges[var5 + 1];
            int var9 = var2.ranges[var6];
            int var10 = var2.ranges[var6 + 1];
            if (var8 < var9) {
               var5 += 2;
            } else if (var8 >= var9 && var7 <= var10) {
               if (var9 <= var9 && var8 <= var10) {
                  var3[var4++] = var7;
                  var3[var4++] = var8;
                  var5 += 2;
               } else if (var9 <= var7) {
                  var3[var4++] = var7;
                  var3[var4++] = var10;
                  this.ranges[var5] = var10 + 1;
                  var6 += 2;
               } else if (var8 <= var10) {
                  var3[var4++] = var9;
                  var3[var4++] = var8;
                  var5 += 2;
               } else {
                  var3[var4++] = var9;
                  var3[var4++] = var10;
                  this.ranges[var5] = var10 + 1;
               }
            } else {
               if (var10 >= var7) {
                  throw new RuntimeException("Token#intersectRanges(): Internal Error: [" + this.ranges[var5] + "," + this.ranges[var5 + 1] + "] & [" + var2.ranges[var6] + "," + var2.ranges[var6 + 1] + "]");
               }

               var6 += 2;
            }
         }

         while(var5 < this.ranges.length) {
            var3[var4++] = this.ranges[var5++];
            var3[var4++] = this.ranges[var5++];
         }

         this.ranges = new int[var4];
         System.arraycopy(var3, 0, this.ranges, 0, var4);
      }
   }

   static Token complementRanges(Token var0) {
      if (var0.type != 4 && var0.type != 5) {
         throw new IllegalArgumentException("Token#complementRanges(): must be RANGE: " + var0.type);
      } else {
         RangeToken var1 = (RangeToken)var0;
         var1.sortRanges();
         var1.compactRanges();
         int var2 = var1.ranges.length + 2;
         if (var1.ranges[0] == 0) {
            var2 -= 2;
         }

         int var3 = var1.ranges[var1.ranges.length - 1];
         if (var3 == 1114111) {
            var2 -= 2;
         }

         RangeToken var4 = Token.createRange();
         var4.ranges = new int[var2];
         int var5 = 0;
         if (var1.ranges[0] > 0) {
            var4.ranges[var5++] = 0;
            var4.ranges[var5++] = var1.ranges[0] - 1;
         }

         for(int var6 = 1; var6 < var1.ranges.length - 2; var6 += 2) {
            var4.ranges[var5++] = var1.ranges[var6] + 1;
            var4.ranges[var5++] = var1.ranges[var6 + 1] - 1;
         }

         if (var3 != 1114111) {
            var4.ranges[var5++] = var3 + 1;
            var4.ranges[var5] = 1114111;
         }

         var4.setCompacted();
         return var4;
      }
   }

   synchronized RangeToken getCaseInsensitiveToken() {
      if (this.icaseCache != null) {
         return this.icaseCache;
      } else {
         RangeToken var1 = super.type == 4 ? Token.createRange() : Token.createNRange();

         int var4;
         for(int var2 = 0; var2 < this.ranges.length; var2 += 2) {
            for(int var3 = this.ranges[var2]; var3 <= this.ranges[var2 + 1]; ++var3) {
               if (var3 > 65535) {
                  var1.addRange(var3, var3);
               } else {
                  var4 = Character.toUpperCase((char)var3);
                  var1.addRange(var4, var4);
               }
            }
         }

         RangeToken var7 = super.type == 4 ? Token.createRange() : Token.createNRange();

         for(var4 = 0; var4 < var1.ranges.length; var4 += 2) {
            for(int var5 = var1.ranges[var4]; var5 <= var1.ranges[var4 + 1]; ++var5) {
               if (var5 > 65535) {
                  var7.addRange(var5, var5);
               } else {
                  char var6 = Character.toUpperCase((char)var5);
                  var7.addRange(var6, var6);
               }
            }
         }

         var7.mergeRanges(var1);
         var7.mergeRanges(this);
         var7.compactRanges();
         this.icaseCache = var7;
         return var7;
      }
   }

   void dumpRanges() {
      System.err.print("RANGE: ");
      if (this.ranges == null) {
         System.err.println(" NULL");
      }

      for(int var1 = 0; var1 < this.ranges.length; var1 += 2) {
         System.err.print("[" + this.ranges[var1] + "," + this.ranges[var1 + 1] + "] ");
      }

      System.err.println("");
   }

   boolean match(int var1) {
      if (this.map == null) {
         this.createMap();
      }

      boolean var2;
      int var3;
      if (super.type == 4) {
         if (var1 < 256) {
            return (this.map[var1 / 32] & 1 << (var1 & 31)) != 0;
         }

         var2 = false;

         for(var3 = this.nonMapIndex; var3 < this.ranges.length; var3 += 2) {
            if (this.ranges[var3] <= var1 && var1 <= this.ranges[var3 + 1]) {
               return true;
            }
         }
      } else {
         if (var1 < 256) {
            return (this.map[var1 / 32] & 1 << (var1 & 31)) == 0;
         }

         var2 = true;

         for(var3 = this.nonMapIndex; var3 < this.ranges.length; var3 += 2) {
            if (this.ranges[var3] <= var1 && var1 <= this.ranges[var3 + 1]) {
               return false;
            }
         }
      }

      return var2;
   }

   private void createMap() {
      byte var1 = 8;
      this.map = new int[var1];
      this.nonMapIndex = this.ranges.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         this.map[var2] = 0;
      }

      for(int var3 = 0; var3 < this.ranges.length; var3 += 2) {
         int var4 = this.ranges[var3];
         int var5 = this.ranges[var3 + 1];
         if (var4 >= 256) {
            this.nonMapIndex = var3;
            break;
         }

         for(int var6 = var4; var6 <= var5 && var6 < 256; ++var6) {
            int[] var10000 = this.map;
            var10000[var6 / 32] |= 1 << (var6 & 31);
         }

         if (var5 >= 256) {
            this.nonMapIndex = var3;
            break;
         }
      }

   }

   public String toString(int var1) {
      String var2;
      StringBuffer var3;
      int var4;
      if (super.type == 4) {
         if (this == Token.token_dot) {
            var2 = ".";
         } else if (this == Token.token_0to9) {
            var2 = "\\d";
         } else if (this == Token.token_wordchars) {
            var2 = "\\w";
         } else if (this == Token.token_spaces) {
            var2 = "\\s";
         } else {
            var3 = new StringBuffer();
            var3.append("[");

            for(var4 = 0; var4 < this.ranges.length; var4 += 2) {
               if ((var1 & 1024) != 0 && var4 > 0) {
                  var3.append(",");
               }

               if (this.ranges[var4] == this.ranges[var4 + 1]) {
                  var3.append(escapeCharInCharClass(this.ranges[var4]));
               } else {
                  var3.append(escapeCharInCharClass(this.ranges[var4]));
                  var3.append('-');
                  var3.append(escapeCharInCharClass(this.ranges[var4 + 1]));
               }
            }

            var3.append("]");
            var2 = var3.toString();
         }
      } else if (this == Token.token_not_0to9) {
         var2 = "\\D";
      } else if (this == Token.token_not_wordchars) {
         var2 = "\\W";
      } else if (this == Token.token_not_spaces) {
         var2 = "\\S";
      } else {
         var3 = new StringBuffer();
         var3.append("[^");

         for(var4 = 0; var4 < this.ranges.length; var4 += 2) {
            if ((var1 & 1024) != 0 && var4 > 0) {
               var3.append(",");
            }

            if (this.ranges[var4] == this.ranges[var4 + 1]) {
               var3.append(escapeCharInCharClass(this.ranges[var4]));
            } else {
               var3.append(escapeCharInCharClass(this.ranges[var4]));
               var3.append('-');
               var3.append(escapeCharInCharClass(this.ranges[var4 + 1]));
            }
         }

         var3.append("]");
         var2 = var3.toString();
      }

      return var2;
   }

   private static String escapeCharInCharClass(int var0) {
      String var1;
      switch (var0) {
         case 9:
            var1 = "\\t";
            break;
         case 10:
            var1 = "\\n";
            break;
         case 12:
            var1 = "\\f";
            break;
         case 13:
            var1 = "\\r";
            break;
         case 27:
            var1 = "\\e";
            break;
         case 44:
         case 45:
         case 91:
         case 92:
         case 93:
         case 94:
            var1 = "\\" + (char)var0;
            break;
         default:
            String var2;
            if (var0 < 32) {
               var2 = "0" + Integer.toHexString(var0);
               var1 = "\\x" + var2.substring(var2.length() - 2, var2.length());
            } else if (var0 >= 65536) {
               var2 = "0" + Integer.toHexString(var0);
               var1 = "\\v" + var2.substring(var2.length() - 6, var2.length());
            } else {
               var1 = "" + (char)var0;
            }
      }

      return var1;
   }
}

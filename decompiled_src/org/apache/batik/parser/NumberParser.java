package org.apache.batik.parser;

import java.io.IOException;

public abstract class NumberParser extends AbstractParser {
   private static final double[] pow10 = new double[128];

   protected float parseFloat() throws ParseException, IOException {
      int var1 = 0;
      int var2 = 0;
      boolean var3 = true;
      boolean var4 = false;
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      boolean var8 = true;
      switch (this.current) {
         case 45:
            var3 = false;
         case 43:
            this.current = this.reader.read();
      }

      label158:
      switch (this.current) {
         case 46:
            break;
         case 47:
         default:
            this.reportUnexpectedCharacterError(this.current);
            return 0.0F;
         case 48:
            var4 = true;

            label156:
            while(true) {
               this.current = this.reader.read();
               switch (this.current) {
                  case 46:
                  case 69:
                  case 101:
                     break label158;
                  case 48:
                     break;
                  case 49:
                  case 50:
                  case 51:
                  case 52:
                  case 53:
                  case 54:
                  case 55:
                  case 56:
                  case 57:
                     break label156;
                  default:
                     return 0.0F;
               }
            }
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
            var4 = true;

            label140:
            while(true) {
               if (var2 < 9) {
                  ++var2;
                  var1 = var1 * 10 + (this.current - 48);
               } else {
                  ++var7;
               }

               this.current = this.reader.read();
               switch (this.current) {
                  case 48:
                  case 49:
                  case 50:
                  case 51:
                  case 52:
                  case 53:
                  case 54:
                  case 55:
                  case 56:
                  case 57:
                     break;
                  default:
                     break label140;
               }
            }
      }

      if (this.current == 46) {
         label175: {
            this.current = this.reader.read();
            switch (this.current) {
               case 48:
                  if (var2 == 0) {
                     label126:
                     while(true) {
                        this.current = this.reader.read();
                        --var7;
                        switch (this.current) {
                           case 48:
                              break;
                           case 49:
                           case 50:
                           case 51:
                           case 52:
                           case 53:
                           case 54:
                           case 55:
                           case 56:
                           case 57:
                              break label126;
                           default:
                              if (!var4) {
                                 return 0.0F;
                              }
                              break label175;
                        }
                     }
                  }
               case 49:
               case 50:
               case 51:
               case 52:
               case 53:
               case 54:
               case 55:
               case 56:
               case 57:
                  break;
               case 69:
               case 101:
               default:
                  if (!var4) {
                     this.reportUnexpectedCharacterError(this.current);
                     return 0.0F;
                  }
                  break label175;
            }

            label107:
            while(true) {
               if (var2 < 9) {
                  ++var2;
                  var1 = var1 * 10 + (this.current - 48);
                  --var7;
               }

               this.current = this.reader.read();
               switch (this.current) {
                  case 48:
                  case 49:
                  case 50:
                  case 51:
                  case 52:
                  case 53:
                  case 54:
                  case 55:
                  case 56:
                  case 57:
                     break;
                  default:
                     break label107;
               }
            }
         }
      }

      switch (this.current) {
         case 69:
         case 101:
            this.current = this.reader.read();
            switch (this.current) {
               case 44:
               case 46:
               case 47:
               default:
                  this.reportUnexpectedCharacterError(this.current);
                  return 0.0F;
               case 45:
                  var8 = false;
               case 43:
                  this.current = this.reader.read();
                  switch (this.current) {
                     case 48:
                     case 49:
                     case 50:
                     case 51:
                     case 52:
                     case 53:
                     case 54:
                     case 55:
                     case 56:
                     case 57:
                        break;
                     default:
                        this.reportUnexpectedCharacterError(this.current);
                        return 0.0F;
                  }
               case 48:
               case 49:
               case 50:
               case 51:
               case 52:
               case 53:
               case 54:
               case 55:
               case 56:
               case 57:
            }

            label95:
            switch (this.current) {
               case 48:
                  label94:
                  while(true) {
                     this.current = this.reader.read();
                     switch (this.current) {
                        case 48:
                           break;
                        case 49:
                        case 50:
                        case 51:
                        case 52:
                        case 53:
                        case 54:
                        case 55:
                        case 56:
                        case 57:
                           break label94;
                        default:
                           break label95;
                     }
                  }
               case 49:
               case 50:
               case 51:
               case 52:
               case 53:
               case 54:
               case 55:
               case 56:
               case 57:
                  label84:
                  while(true) {
                     if (var6 < 3) {
                        ++var6;
                        var5 = var5 * 10 + (this.current - 48);
                     }

                     this.current = this.reader.read();
                     switch (this.current) {
                        case 48:
                        case 49:
                        case 50:
                        case 51:
                        case 52:
                        case 53:
                        case 54:
                        case 55:
                        case 56:
                        case 57:
                           break;
                        default:
                           break label84;
                     }
                  }
            }
         default:
            if (!var8) {
               var5 = -var5;
            }

            var5 += var7;
            if (!var3) {
               var1 = -var1;
            }

            return buildFloat(var1, var5);
      }
   }

   public static float buildFloat(int var0, int var1) {
      if (var1 >= -125 && var0 != 0) {
         if (var1 >= 128) {
            return var0 > 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
         } else if (var1 == 0) {
            return (float)var0;
         } else {
            if (var0 >= 67108864) {
               ++var0;
            }

            return (float)(var1 > 0 ? (double)var0 * pow10[var1] : (double)var0 / pow10[-var1]);
         }
      } else {
         return 0.0F;
      }
   }

   static {
      for(int var0 = 0; var0 < pow10.length; ++var0) {
         pow10[var0] = Math.pow(10.0, (double)var0);
      }

   }
}

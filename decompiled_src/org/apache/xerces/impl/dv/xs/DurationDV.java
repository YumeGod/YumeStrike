package org.apache.xerces.impl.dv.xs;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.Duration;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

public class DurationDV extends AbstractDateTimeDV {
   public static final int DURATION_TYPE = 0;
   public static final int YEARMONTHDURATION_TYPE = 1;
   public static final int DAYTIMEDURATION_TYPE = 2;
   private static final AbstractDateTimeDV.DateTimeData[] DATETIMES = new AbstractDateTimeDV.DateTimeData[]{new AbstractDateTimeDV.DateTimeData(1696, 9, 1, 0, 0, 0.0, 90, (String)null, true, (AbstractDateTimeDV)null), new AbstractDateTimeDV.DateTimeData(1697, 2, 1, 0, 0, 0.0, 90, (String)null, true, (AbstractDateTimeDV)null), new AbstractDateTimeDV.DateTimeData(1903, 3, 1, 0, 0, 0.0, 90, (String)null, true, (AbstractDateTimeDV)null), new AbstractDateTimeDV.DateTimeData(1903, 7, 1, 0, 0, 0.0, 90, (String)null, true, (AbstractDateTimeDV)null)};

   public Object getActualValue(String var1, ValidationContext var2) throws InvalidDatatypeValueException {
      try {
         return this.parse(var1, 0);
      } catch (Exception var4) {
         throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{var1, "duration"});
      }
   }

   protected AbstractDateTimeDV.DateTimeData parse(String var1, int var2) throws SchemaDateTimeException {
      int var3 = var1.length();
      AbstractDateTimeDV.DateTimeData var4 = new AbstractDateTimeDV.DateTimeData(var1, this);
      int var5 = 0;
      char var6 = var1.charAt(var5++);
      if (var6 != 'P' && var6 != '-') {
         throw new SchemaDateTimeException();
      } else {
         var4.utc = var6 == '-' ? 45 : 0;
         if (var6 == '-' && var1.charAt(var5++) != 'P') {
            throw new SchemaDateTimeException();
         } else {
            byte var7 = 1;
            if (var4.utc == 45) {
               var7 = -1;
            }

            boolean var8 = false;
            int var9 = this.indexOf(var1, var5, var3, 'T');
            if (var9 == -1) {
               var9 = var3;
            } else if (var2 == 1) {
               throw new SchemaDateTimeException();
            }

            int var10 = this.indexOf(var1, var5, var9, 'Y');
            if (var10 != -1) {
               if (var2 == 2) {
                  throw new SchemaDateTimeException();
               }

               var4.year = var7 * this.parseInt(var1, var5, var10);
               var5 = var10 + 1;
               var8 = true;
            }

            var10 = this.indexOf(var1, var5, var9, 'M');
            if (var10 != -1) {
               if (var2 == 2) {
                  throw new SchemaDateTimeException();
               }

               var4.month = var7 * this.parseInt(var1, var5, var10);
               var5 = var10 + 1;
               var8 = true;
            }

            var10 = this.indexOf(var1, var5, var9, 'D');
            if (var10 != -1) {
               if (var2 == 1) {
                  throw new SchemaDateTimeException();
               }

               var4.day = var7 * this.parseInt(var1, var5, var10);
               var5 = var10 + 1;
               var8 = true;
            }

            if (var3 == var9 && var5 != var3) {
               throw new SchemaDateTimeException();
            } else {
               if (var3 != var9) {
                  label95: {
                     ++var5;
                     var10 = this.indexOf(var1, var5, var3, 'H');
                     if (var10 != -1) {
                        var4.hour = var7 * this.parseInt(var1, var5, var10);
                        var5 = var10 + 1;
                        var8 = true;
                     }

                     var10 = this.indexOf(var1, var5, var3, 'M');
                     if (var10 != -1) {
                        var4.minute = var7 * this.parseInt(var1, var5, var10);
                        var5 = var10 + 1;
                        var8 = true;
                     }

                     var10 = this.indexOf(var1, var5, var3, 'S');
                     if (var10 != -1) {
                        var4.second = (double)var7 * this.parseSecond(var1, var5, var10);
                        var5 = var10 + 1;
                        var8 = true;
                     }

                     if (var5 == var3) {
                        --var5;
                        if (var1.charAt(var5) != 'T') {
                           break label95;
                        }
                     }

                     throw new SchemaDateTimeException();
                  }
               }

               if (!var8) {
                  throw new SchemaDateTimeException();
               } else {
                  return var4;
               }
            }
         }
      }
   }

   protected short compareDates(AbstractDateTimeDV.DateTimeData var1, AbstractDateTimeDV.DateTimeData var2, boolean var3) {
      boolean var5 = true;
      short var4 = this.compareOrder(var1, var2);
      if (var4 == 0) {
         return 0;
      } else {
         AbstractDateTimeDV.DateTimeData[] var6 = new AbstractDateTimeDV.DateTimeData[]{new AbstractDateTimeDV.DateTimeData((String)null, this), new AbstractDateTimeDV.DateTimeData((String)null, this)};
         AbstractDateTimeDV.DateTimeData var7 = this.addDuration(var1, DATETIMES[0], var6[0]);
         AbstractDateTimeDV.DateTimeData var8 = this.addDuration(var2, DATETIMES[0], var6[1]);
         var4 = this.compareOrder(var7, var8);
         if (var4 == 2) {
            return 2;
         } else {
            var7 = this.addDuration(var1, DATETIMES[1], var6[0]);
            var8 = this.addDuration(var2, DATETIMES[1], var6[1]);
            short var9 = this.compareOrder(var7, var8);
            var4 = this.compareResults(var4, var9, var3);
            if (var4 == 2) {
               return 2;
            } else {
               var7 = this.addDuration(var1, DATETIMES[2], var6[0]);
               var8 = this.addDuration(var2, DATETIMES[2], var6[1]);
               var9 = this.compareOrder(var7, var8);
               var4 = this.compareResults(var4, var9, var3);
               if (var4 == 2) {
                  return 2;
               } else {
                  var7 = this.addDuration(var1, DATETIMES[3], var6[0]);
                  var8 = this.addDuration(var2, DATETIMES[3], var6[1]);
                  var9 = this.compareOrder(var7, var8);
                  var4 = this.compareResults(var4, var9, var3);
                  return var4;
               }
            }
         }
      }
   }

   private short compareResults(short var1, short var2, boolean var3) {
      if (var2 == 2) {
         return 2;
      } else if (var1 != var2 && var3) {
         return 2;
      } else if (var1 != var2 && !var3) {
         if (var1 != 0 && var2 != 0) {
            return 2;
         } else {
            return var1 != 0 ? var1 : var2;
         }
      } else {
         return var1;
      }
   }

   private AbstractDateTimeDV.DateTimeData addDuration(AbstractDateTimeDV.DateTimeData var1, AbstractDateTimeDV.DateTimeData var2, AbstractDateTimeDV.DateTimeData var3) {
      this.resetDateObj(var3);
      int var4 = var2.month + var1.month;
      var3.month = this.modulo(var4, 1, 13);
      int var5 = this.fQuotient(var4, 1, 13);
      var3.year = var2.year + var1.year + var5;
      double var6 = var2.second + var1.second;
      var5 = (int)Math.floor(var6 / 60.0);
      var3.second = var6 - (double)(var5 * 60);
      var4 = var2.minute + var1.minute + var5;
      var5 = this.fQuotient(var4, 60);
      var3.minute = this.mod(var4, 60, var5);
      var4 = var2.hour + var1.hour + var5;
      var5 = this.fQuotient(var4, 24);
      var3.hour = this.mod(var4, 24, var5);
      var3.day = var2.day + var1.day + var5;

      while(true) {
         var4 = this.maxDayInMonthFor(var3.year, var3.month);
         byte var8;
         if (var3.day < 1) {
            var3.day += this.maxDayInMonthFor(var3.year, var3.month - 1);
            var8 = -1;
         } else {
            if (var3.day <= var4) {
               var3.utc = 90;
               return var3;
            }

            var3.day -= var4;
            var8 = 1;
         }

         var4 = var3.month + var8;
         var3.month = this.modulo(var4, 1, 13);
         var3.year += this.fQuotient(var4, 1, 13);
      }
   }

   protected double parseSecond(String var1, int var2, int var3) throws NumberFormatException {
      int var4 = -1;

      for(int var5 = var2; var5 < var3; ++var5) {
         char var6 = var1.charAt(var5);
         if (var6 == '.') {
            var4 = var5;
         } else if (var6 > '9' || var6 < '0') {
            throw new NumberFormatException("'" + var1 + "' has wrong format");
         }
      }

      if (var4 + 1 == var3) {
         throw new NumberFormatException("'" + var1 + "' has wrong format");
      } else {
         return Double.parseDouble(var1.substring(var2, var3));
      }
   }

   protected String dateToString(AbstractDateTimeDV.DateTimeData var1) {
      StringBuffer var2 = new StringBuffer(30);
      if (var1.year < 0 || var1.month < 0 || var1.day < 0 || var1.hour < 0 || var1.minute < 0 || var1.second < 0.0) {
         var2.append('-');
      }

      var2.append('P');
      var2.append((var1.year < 0 ? -1 : 1) * var1.year);
      var2.append('Y');
      var2.append((var1.month < 0 ? -1 : 1) * var1.month);
      var2.append('M');
      var2.append((var1.day < 0 ? -1 : 1) * var1.day);
      var2.append('D');
      var2.append('T');
      var2.append((var1.hour < 0 ? -1 : 1) * var1.hour);
      var2.append('H');
      var2.append((var1.minute < 0 ? -1 : 1) * var1.minute);
      var2.append('M');
      var2.append((var1.second < 0.0 ? -1.0 : 1.0) * var1.second);
      var2.append('S');
      return var2.toString();
   }

   protected Duration getDuration(AbstractDateTimeDV.DateTimeData var1) {
      byte var2 = 1;
      if (var1.year < 0 || var1.month < 0 || var1.day < 0 || var1.hour < 0 || var1.minute < 0 || var1.second < 0.0) {
         var2 = -1;
      }

      return super.factory.newDuration(var2 == 1, var1.year != Integer.MIN_VALUE ? BigInteger.valueOf((long)(var2 * var1.year)) : null, var1.month != Integer.MIN_VALUE ? BigInteger.valueOf((long)(var2 * var1.month)) : null, var1.day != Integer.MIN_VALUE ? BigInteger.valueOf((long)(var2 * var1.day)) : null, var1.hour != Integer.MIN_VALUE ? BigInteger.valueOf((long)(var2 * var1.hour)) : null, var1.minute != Integer.MIN_VALUE ? BigInteger.valueOf((long)(var2 * var1.minute)) : null, var1.second != -2.147483648E9 ? new BigDecimal(String.valueOf((double)var2 * var1.second)) : null);
   }
}

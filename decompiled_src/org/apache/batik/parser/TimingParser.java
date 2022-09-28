package org.apache.batik.parser;

import java.io.IOException;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import org.apache.batik.xml.XMLUtilities;

public abstract class TimingParser extends AbstractParser {
   protected static final int TIME_OFFSET = 0;
   protected static final int TIME_SYNCBASE = 1;
   protected static final int TIME_EVENTBASE = 2;
   protected static final int TIME_REPEAT = 3;
   protected static final int TIME_ACCESSKEY = 4;
   protected static final int TIME_ACCESSKEY_SVG12 = 5;
   protected static final int TIME_MEDIA_MARKER = 6;
   protected static final int TIME_WALLCLOCK = 7;
   protected static final int TIME_INDEFINITE = 8;
   protected boolean useSVG11AccessKeys;
   protected boolean useSVG12AccessKeys;

   public TimingParser(boolean var1, boolean var2) {
      this.useSVG11AccessKeys = var1;
      this.useSVG12AccessKeys = var2;
   }

   protected Object[] parseTimingSpecifier() throws ParseException, IOException {
      this.skipSpaces();
      boolean var1 = false;
      if (this.current == 92) {
         var1 = true;
         this.current = this.reader.read();
      }

      Object[] var2 = null;
      if (this.current == 43 || this.current == 45 && !var1 || this.current >= 48 && this.current <= 57) {
         float var3 = this.parseOffset();
         var2 = new Object[]{new Integer(0), new Float(var3)};
      } else if (XMLUtilities.isXMLNameFirstCharacter((char)this.current)) {
         var2 = this.parseIDValue(var1);
      } else {
         this.reportUnexpectedCharacterError(this.current);
      }

      return var2;
   }

   protected String parseName() throws ParseException, IOException {
      StringBuffer var1 = new StringBuffer();
      boolean var2 = false;

      do {
         var1.append((char)this.current);
         this.current = this.reader.read();
         var2 = false;
         if (this.current == 92) {
            var2 = true;
            this.current = this.reader.read();
         }
      } while(XMLUtilities.isXMLNameCharacter((char)this.current) && (var2 || this.current != 45 && this.current != 46));

      return var1.toString();
   }

   protected Object[] parseIDValue(boolean var1) throws ParseException, IOException {
      String var2 = this.parseName();
      float var4;
      if ((var2.equals("accessKey") && this.useSVG11AccessKeys || var2.equals("accesskey")) && !var1) {
         if (this.current != 40) {
            this.reportUnexpectedCharacterError(this.current);
         }

         this.current = this.reader.read();
         if (this.current == -1) {
            this.reportError("end.of.stream", new Object[0]);
         }

         char var9 = (char)this.current;
         this.current = this.reader.read();
         if (this.current != 41) {
            this.reportUnexpectedCharacterError(this.current);
         }

         this.current = this.reader.read();
         this.skipSpaces();
         var4 = 0.0F;
         if (this.current == 43 || this.current == 45) {
            var4 = this.parseOffset();
         }

         return new Object[]{new Integer(4), new Float(var4), new Character(var9)};
      } else if (var2.equals("accessKey") && this.useSVG12AccessKeys && !var1) {
         if (this.current != 40) {
            this.reportUnexpectedCharacterError(this.current);
         }

         this.current = this.reader.read();

         StringBuffer var8;
         for(var8 = new StringBuffer(); this.current >= 65 && this.current <= 90 || this.current >= 97 && this.current <= 122 || this.current >= 48 && this.current <= 57 || this.current == 43; this.current = this.reader.read()) {
            var8.append((char)this.current);
         }

         if (this.current != 41) {
            this.reportUnexpectedCharacterError(this.current);
         }

         this.current = this.reader.read();
         this.skipSpaces();
         var4 = 0.0F;
         if (this.current == 43 || this.current == 45) {
            var4 = this.parseOffset();
         }

         return new Object[]{new Integer(5), new Float(var4), var8.toString()};
      } else if (var2.equals("wallclock") && !var1) {
         if (this.current != 40) {
            this.reportUnexpectedCharacterError(this.current);
         }

         this.current = this.reader.read();
         this.skipSpaces();
         Calendar var7 = this.parseWallclockValue();
         this.skipSpaces();
         if (this.current != 41) {
            this.reportError("character.unexpected", new Object[]{new Integer(this.current)});
         }

         this.current = this.reader.read();
         return new Object[]{new Integer(7), var7};
      } else if (var2.equals("indefinite") && !var1) {
         return new Object[]{new Integer(8)};
      } else if (this.current == 46) {
         this.current = this.reader.read();
         if (this.current == 92) {
            var1 = true;
            this.current = this.reader.read();
         }

         if (!XMLUtilities.isXMLNameFirstCharacter((char)this.current)) {
            this.reportUnexpectedCharacterError(this.current);
         }

         String var6 = this.parseName();
         if ((var6.equals("begin") || var6.equals("end")) && !var1) {
            this.skipSpaces();
            var4 = 0.0F;
            if (this.current == 43 || this.current == 45) {
               var4 = this.parseOffset();
            }

            return new Object[]{new Integer(1), new Float(var4), var2, var6};
         } else if (var6.equals("repeat") && !var1) {
            Integer var11 = null;
            if (this.current == 40) {
               this.current = this.reader.read();
               var11 = new Integer(this.parseDigits());
               if (this.current != 41) {
                  this.reportUnexpectedCharacterError(this.current);
               }

               this.current = this.reader.read();
            }

            this.skipSpaces();
            float var5 = 0.0F;
            if (this.current == 43 || this.current == 45) {
               var5 = this.parseOffset();
            }

            return new Object[]{new Integer(3), new Float(var5), var2, var11};
         } else if (var6.equals("marker") && !var1) {
            if (this.current != 40) {
               this.reportUnexpectedCharacterError(this.current);
            }

            String var10 = this.parseName();
            if (this.current != 41) {
               this.reportUnexpectedCharacterError(this.current);
            }

            this.current = this.reader.read();
            return new Object[]{new Integer(6), var2, var10};
         } else {
            this.skipSpaces();
            var4 = 0.0F;
            if (this.current == 43 || this.current == 45) {
               var4 = this.parseOffset();
            }

            return new Object[]{new Integer(2), new Float(var4), var2, var6};
         }
      } else {
         this.skipSpaces();
         float var3 = 0.0F;
         if (this.current == 43 || this.current == 45) {
            var3 = this.parseOffset();
         }

         return new Object[]{new Integer(2), new Float(var3), null, var2};
      }
   }

   protected float parseClockValue() throws ParseException, IOException {
      int var1 = this.parseDigits();
      float var2;
      if (this.current == 58) {
         this.current = this.reader.read();
         int var3 = this.parseDigits();
         if (this.current == 58) {
            this.current = this.reader.read();
            int var4 = this.parseDigits();
            var2 = (float)(var1 * 3600 + var3 * 60 + var4);
         } else {
            var2 = (float)(var1 * 60 + var3);
         }

         if (this.current == 46) {
            this.current = this.reader.read();
            var2 += this.parseFraction();
         }
      } else if (this.current == 46) {
         this.current = this.reader.read();
         var2 = (this.parseFraction() + (float)var1) * this.parseUnit();
      } else {
         var2 = (float)var1 * this.parseUnit();
      }

      return var2;
   }

   protected float parseOffset() throws ParseException, IOException {
      boolean var1 = false;
      if (this.current == 45) {
         var1 = true;
         this.current = this.reader.read();
         this.skipSpaces();
      } else if (this.current == 43) {
         this.current = this.reader.read();
         this.skipSpaces();
      }

      return var1 ? -this.parseClockValue() : this.parseClockValue();
   }

   protected int parseDigits() throws ParseException, IOException {
      int var1 = 0;
      if (this.current < 48 || this.current > 57) {
         this.reportUnexpectedCharacterError(this.current);
      }

      do {
         var1 = var1 * 10 + (this.current - 48);
         this.current = this.reader.read();
      } while(this.current >= 48 && this.current <= 57);

      return var1;
   }

   protected float parseFraction() throws ParseException, IOException {
      float var1 = 0.0F;
      if (this.current < 48 || this.current > 57) {
         this.reportUnexpectedCharacterError(this.current);
      }

      float var2 = 0.1F;

      do {
         var1 += var2 * (float)(this.current - 48);
         var2 *= 0.1F;
         this.current = this.reader.read();
      } while(this.current >= 48 && this.current <= 57);

      return var1;
   }

   protected float parseUnit() throws ParseException, IOException {
      if (this.current == 104) {
         this.current = this.reader.read();
         return 3600.0F;
      } else {
         if (this.current == 109) {
            this.current = this.reader.read();
            if (this.current == 105) {
               this.current = this.reader.read();
               if (this.current != 110) {
                  this.reportUnexpectedCharacterError(this.current);
               }

               this.current = this.reader.read();
               return 60.0F;
            }

            if (this.current == 115) {
               this.current = this.reader.read();
               return 0.001F;
            }

            this.reportUnexpectedCharacterError(this.current);
         } else if (this.current == 115) {
            this.current = this.reader.read();
         }

         return 1.0F;
      }
   }

   protected Calendar parseWallclockValue() throws ParseException, IOException {
      int var1;
      int var2;
      int var3;
      int var4;
      int var5;
      int var6;
      int var7;
      int var8;
      float var9;
      boolean var10;
      boolean var11;
      boolean var12;
      boolean var13;
      String var14;
      label87: {
         var1 = 0;
         var2 = 0;
         var3 = 0;
         var4 = 0;
         var5 = 0;
         var6 = 0;
         var7 = 0;
         var8 = 0;
         var9 = 0.0F;
         var10 = false;
         var11 = false;
         var12 = false;
         var13 = false;
         var14 = null;
         int var15 = this.parseDigits();
         if (this.current == 45) {
            var10 = true;
            var1 = var15;
            this.current = this.reader.read();
            var2 = this.parseDigits();
            if (this.current != 45) {
               this.reportUnexpectedCharacterError(this.current);
            }

            this.current = this.reader.read();
            var3 = this.parseDigits();
            if (this.current != 84) {
               break label87;
            }

            this.current = this.reader.read();
            var15 = this.parseDigits();
            if (this.current != 58) {
               this.reportUnexpectedCharacterError(this.current);
            }
         }

         if (this.current == 58) {
            var11 = true;
            var4 = var15;
            this.current = this.reader.read();
            var5 = this.parseDigits();
            if (this.current == 58) {
               this.current = this.reader.read();
               var6 = this.parseDigits();
               if (this.current == 46) {
                  this.current = this.reader.read();
                  var9 = this.parseFraction();
               }
            }

            if (this.current == 90) {
               var12 = true;
               var14 = "UTC";
               this.current = this.reader.read();
            } else if (this.current == 43 || this.current == 45) {
               StringBuffer var16 = new StringBuffer();
               var12 = true;
               if (this.current == 45) {
                  var13 = true;
                  var16.append('-');
               } else {
                  var16.append('+');
               }

               this.current = this.reader.read();
               var7 = this.parseDigits();
               if (var7 < 10) {
                  var16.append('0');
               }

               var16.append(var7);
               if (this.current != 58) {
                  this.reportUnexpectedCharacterError(this.current);
               }

               var16.append(':');
               this.current = this.reader.read();
               var8 = this.parseDigits();
               if (var8 < 10) {
                  var16.append('0');
               }

               var16.append(var8);
               var14 = var16.toString();
            }
         }
      }

      if (!var10 && !var11) {
         this.reportUnexpectedCharacterError(this.current);
      }

      Calendar var18;
      if (var12) {
         int var17 = (var13 ? -1 : 1) * (var7 * 3600000 + var8 * '\uea60');
         var18 = Calendar.getInstance(new SimpleTimeZone(var17, var14));
      } else {
         var18 = Calendar.getInstance();
      }

      if (var10 && var11) {
         var18.set(var1, var2, var3, var4, var5, var6);
      } else if (var10) {
         var18.set(var1, var2, var3, 0, 0, 0);
      } else {
         var18.set(10, var4);
         var18.set(12, var5);
         var18.set(13, var6);
      }

      if (var9 == 0.0F) {
         var18.set(14, (int)(var9 * 1000.0F));
      } else {
         var18.set(14, 0);
      }

      return var18;
   }
}

package org.apache.xerces.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;
import org.apache.xerces.impl.msg.XMLMessageFormatter;
import org.apache.xerces.util.MessageFormatter;

public class UTF8Reader extends Reader {
   public static final int DEFAULT_BUFFER_SIZE = 2048;
   private static final boolean DEBUG_READ = false;
   protected InputStream fInputStream;
   protected byte[] fBuffer;
   protected int fOffset;
   private int fSurrogate;
   private MessageFormatter fFormatter;
   private Locale fLocale;

   public UTF8Reader(InputStream var1) {
      this(var1, 2048, new XMLMessageFormatter(), Locale.getDefault());
   }

   public UTF8Reader(InputStream var1, MessageFormatter var2, Locale var3) {
      this(var1, 2048, var2, var3);
   }

   public UTF8Reader(InputStream var1, int var2, MessageFormatter var3, Locale var4) {
      this.fSurrogate = -1;
      this.fFormatter = null;
      this.fLocale = null;
      this.fInputStream = var1;
      this.fBuffer = new byte[var2];
      this.fFormatter = var3;
      this.fLocale = var4;
   }

   public int read() throws IOException {
      int var1 = this.fSurrogate;
      if (this.fSurrogate == -1) {
         int var2 = 0;
         int var3 = var2 == this.fOffset ? this.fInputStream.read() : this.fBuffer[var2++] & 255;
         if (var3 == -1) {
            return -1;
         }

         if (var3 < 128) {
            var1 = (char)var3;
         } else {
            int var4;
            if ((var3 & 224) == 192 && (var3 & 30) != 0) {
               var4 = var2 == this.fOffset ? this.fInputStream.read() : this.fBuffer[var2++] & 255;
               if (var4 == -1) {
                  this.expectedByte(2, 2);
               }

               if ((var4 & 192) != 128) {
                  this.invalidByte(2, 2, var4);
               }

               var1 = var3 << 6 & 1984 | var4 & 63;
            } else {
               int var5;
               if ((var3 & 240) == 224) {
                  var4 = var2 == this.fOffset ? this.fInputStream.read() : this.fBuffer[var2++] & 255;
                  if (var4 == -1) {
                     this.expectedByte(2, 3);
                  }

                  if ((var4 & 192) != 128 || var3 == 237 && var4 >= 160 || (var3 & 15) == 0 && (var4 & 32) == 0) {
                     this.invalidByte(2, 3, var4);
                  }

                  var5 = var2 == this.fOffset ? this.fInputStream.read() : this.fBuffer[var2++] & 255;
                  if (var5 == -1) {
                     this.expectedByte(3, 3);
                  }

                  if ((var5 & 192) != 128) {
                     this.invalidByte(3, 3, var5);
                  }

                  var1 = var3 << 12 & '\uf000' | var4 << 6 & 4032 | var5 & 63;
               } else if ((var3 & 248) == 240) {
                  var4 = var2 == this.fOffset ? this.fInputStream.read() : this.fBuffer[var2++] & 255;
                  if (var4 == -1) {
                     this.expectedByte(2, 4);
                  }

                  if ((var4 & 192) != 128 || (var4 & 48) == 0 && (var3 & 7) == 0) {
                     this.invalidByte(2, 3, var4);
                  }

                  var5 = var2 == this.fOffset ? this.fInputStream.read() : this.fBuffer[var2++] & 255;
                  if (var5 == -1) {
                     this.expectedByte(3, 4);
                  }

                  if ((var5 & 192) != 128) {
                     this.invalidByte(3, 3, var5);
                  }

                  int var6 = var2 == this.fOffset ? this.fInputStream.read() : this.fBuffer[var2++] & 255;
                  if (var6 == -1) {
                     this.expectedByte(4, 4);
                  }

                  if ((var6 & 192) != 128) {
                     this.invalidByte(4, 4, var6);
                  }

                  int var7 = var3 << 2 & 28 | var4 >> 4 & 3;
                  if (var7 > 16) {
                     this.invalidSurrogate(var7);
                  }

                  int var8 = var7 - 1;
                  int var9 = '\ud800' | var8 << 6 & 960 | var4 << 2 & 60 | var5 >> 4 & 3;
                  int var10 = '\udc00' | var5 << 6 & 960 | var6 & 63;
                  var1 = var9;
                  this.fSurrogate = var10;
               } else {
                  this.invalidByte(1, 1, var3);
               }
            }
         }
      } else {
         this.fSurrogate = -1;
      }

      return var1;
   }

   public int read(char[] var1, int var2, int var3) throws IOException {
      int var4 = var2;
      if (this.fSurrogate != -1) {
         var1[var2 + 1] = (char)this.fSurrogate;
         this.fSurrogate = -1;
         --var3;
         var4 = var2 + 1;
      }

      boolean var5 = false;
      int var21;
      if (this.fOffset == 0) {
         if (var3 > this.fBuffer.length) {
            var3 = this.fBuffer.length;
         }

         var21 = this.fInputStream.read(this.fBuffer, 0, var3);
         if (var21 == -1) {
            return -1;
         }

         var21 += var4 - var2;
      } else {
         var21 = this.fOffset;
         this.fOffset = 0;
      }

      int var6 = var21;

      int var7;
      byte var8;
      for(var7 = 0; var7 < var6; ++var7) {
         var8 = this.fBuffer[var7];
         if (var8 < 0) {
            break;
         }

         var1[var4++] = (char)var8;
      }

      for(; var7 < var6; ++var7) {
         var8 = this.fBuffer[var7];
         if (var8 >= 0) {
            var1[var4++] = (char)var8;
         } else {
            int var10 = var8 & 255;
            boolean var11;
            int var22;
            int var23;
            if ((var10 & 224) == 192 && (var10 & 30) != 0) {
               var11 = true;
               ++var7;
               if (var7 < var6) {
                  var22 = this.fBuffer[var7] & 255;
               } else {
                  var22 = this.fInputStream.read();
                  if (var22 == -1) {
                     if (var4 > var2) {
                        this.fBuffer[0] = (byte)var10;
                        this.fOffset = 1;
                        return var4 - var2;
                     }

                     this.expectedByte(2, 2);
                  }

                  ++var21;
               }

               if ((var22 & 192) != 128) {
                  if (var4 > var2) {
                     this.fBuffer[0] = (byte)var10;
                     this.fBuffer[1] = (byte)var22;
                     this.fOffset = 2;
                     return var4 - var2;
                  }

                  this.invalidByte(2, 2, var22);
               }

               var23 = var10 << 6 & 1984 | var22 & 63;
               var1[var4++] = (char)var23;
               --var21;
            } else {
               boolean var12;
               int var24;
               if ((var10 & 240) == 224) {
                  var11 = true;
                  ++var7;
                  if (var7 < var6) {
                     var22 = this.fBuffer[var7] & 255;
                  } else {
                     var22 = this.fInputStream.read();
                     if (var22 == -1) {
                        if (var4 > var2) {
                           this.fBuffer[0] = (byte)var10;
                           this.fOffset = 1;
                           return var4 - var2;
                        }

                        this.expectedByte(2, 3);
                     }

                     ++var21;
                  }

                  if ((var22 & 192) != 128 || var10 == 237 && var22 >= 160 || (var10 & 15) == 0 && (var22 & 32) == 0) {
                     if (var4 > var2) {
                        this.fBuffer[0] = (byte)var10;
                        this.fBuffer[1] = (byte)var22;
                        this.fOffset = 2;
                        return var4 - var2;
                     }

                     this.invalidByte(2, 3, var22);
                  }

                  var12 = true;
                  ++var7;
                  if (var7 < var6) {
                     var23 = this.fBuffer[var7] & 255;
                  } else {
                     var23 = this.fInputStream.read();
                     if (var23 == -1) {
                        if (var4 > var2) {
                           this.fBuffer[0] = (byte)var10;
                           this.fBuffer[1] = (byte)var22;
                           this.fOffset = 2;
                           return var4 - var2;
                        }

                        this.expectedByte(3, 3);
                     }

                     ++var21;
                  }

                  if ((var23 & 192) != 128) {
                     if (var4 > var2) {
                        this.fBuffer[0] = (byte)var10;
                        this.fBuffer[1] = (byte)var22;
                        this.fBuffer[2] = (byte)var23;
                        this.fOffset = 3;
                        return var4 - var2;
                     }

                     this.invalidByte(3, 3, var23);
                  }

                  var24 = var10 << 12 & '\uf000' | var22 << 6 & 4032 | var23 & 63;
                  var1[var4++] = (char)var24;
                  var21 -= 2;
               } else if ((var10 & 248) == 240) {
                  var11 = true;
                  ++var7;
                  if (var7 < var6) {
                     var22 = this.fBuffer[var7] & 255;
                  } else {
                     var22 = this.fInputStream.read();
                     if (var22 == -1) {
                        if (var4 > var2) {
                           this.fBuffer[0] = (byte)var10;
                           this.fOffset = 1;
                           return var4 - var2;
                        }

                        this.expectedByte(2, 4);
                     }

                     ++var21;
                  }

                  if ((var22 & 192) != 128 || (var22 & 48) == 0 && (var10 & 7) == 0) {
                     if (var4 > var2) {
                        this.fBuffer[0] = (byte)var10;
                        this.fBuffer[1] = (byte)var22;
                        this.fOffset = 2;
                        return var4 - var2;
                     }

                     this.invalidByte(2, 4, var22);
                  }

                  var12 = true;
                  ++var7;
                  if (var7 < var6) {
                     var23 = this.fBuffer[var7] & 255;
                  } else {
                     var23 = this.fInputStream.read();
                     if (var23 == -1) {
                        if (var4 > var2) {
                           this.fBuffer[0] = (byte)var10;
                           this.fBuffer[1] = (byte)var22;
                           this.fOffset = 2;
                           return var4 - var2;
                        }

                        this.expectedByte(3, 4);
                     }

                     ++var21;
                  }

                  if ((var23 & 192) != 128) {
                     if (var4 > var2) {
                        this.fBuffer[0] = (byte)var10;
                        this.fBuffer[1] = (byte)var22;
                        this.fBuffer[2] = (byte)var23;
                        this.fOffset = 3;
                        return var4 - var2;
                     }

                     this.invalidByte(3, 4, var23);
                  }

                  boolean var13 = true;
                  ++var7;
                  if (var7 < var6) {
                     var24 = this.fBuffer[var7] & 255;
                  } else {
                     var24 = this.fInputStream.read();
                     if (var24 == -1) {
                        if (var4 > var2) {
                           this.fBuffer[0] = (byte)var10;
                           this.fBuffer[1] = (byte)var22;
                           this.fBuffer[2] = (byte)var23;
                           this.fOffset = 3;
                           return var4 - var2;
                        }

                        this.expectedByte(4, 4);
                     }

                     ++var21;
                  }

                  if ((var24 & 192) != 128) {
                     if (var4 > var2) {
                        this.fBuffer[0] = (byte)var10;
                        this.fBuffer[1] = (byte)var22;
                        this.fBuffer[2] = (byte)var23;
                        this.fBuffer[3] = (byte)var24;
                        this.fOffset = 4;
                        return var4 - var2;
                     }

                     this.invalidByte(4, 4, var23);
                  }

                  int var14 = var10 << 2 & 28 | var22 >> 4 & 3;
                  if (var14 > 16) {
                     this.invalidSurrogate(var14);
                  }

                  int var15 = var14 - 1;
                  int var16 = var22 & 15;
                  int var17 = var23 & 63;
                  int var18 = var24 & 63;
                  int var19 = '\ud800' | var15 << 6 & 960 | var16 << 2 | var17 >> 4;
                  int var20 = '\udc00' | var17 << 6 & 960 | var18;
                  var1[var4++] = (char)var19;
                  var1[var4++] = (char)var20;
                  var21 -= 2;
               } else {
                  if (var4 > var2) {
                     this.fBuffer[0] = (byte)var10;
                     this.fOffset = 1;
                     return var4 - var2;
                  }

                  this.invalidByte(1, 1, var10);
               }
            }
         }
      }

      return var21;
   }

   public long skip(long var1) throws IOException {
      long var3 = var1;
      char[] var5 = new char[this.fBuffer.length];

      do {
         int var6 = (long)var5.length < var3 ? var5.length : (int)var3;
         int var7 = this.read(var5, 0, var6);
         if (var7 <= 0) {
            break;
         }

         var3 -= (long)var7;
      } while(var3 > 0L);

      long var8 = var1 - var3;
      return var8;
   }

   public boolean ready() throws IOException {
      return false;
   }

   public boolean markSupported() {
      return false;
   }

   public void mark(int var1) throws IOException {
      throw new IOException(this.fFormatter.formatMessage(this.fLocale, "OperationNotSupported", new Object[]{"mark()", "UTF-8"}));
   }

   public void reset() throws IOException {
      this.fOffset = 0;
      this.fSurrogate = -1;
   }

   public void close() throws IOException {
      this.fInputStream.close();
   }

   private void expectedByte(int var1, int var2) throws MalformedByteSequenceException {
      throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "ExpectedByte", new Object[]{Integer.toString(var1), Integer.toString(var2)});
   }

   private void invalidByte(int var1, int var2, int var3) throws MalformedByteSequenceException {
      throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidByte", new Object[]{Integer.toString(var1), Integer.toString(var2)});
   }

   private void invalidSurrogate(int var1) throws MalformedByteSequenceException {
      throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidHighSurrogate", new Object[]{Integer.toHexString(var1)});
   }
}

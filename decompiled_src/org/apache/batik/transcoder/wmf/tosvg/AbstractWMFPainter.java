package org.apache.batik.transcoder.wmf.tosvg;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

public class AbstractWMFPainter {
   public static final String WMF_FILE_EXTENSION = ".wmf";
   protected WMFFont wmfFont = null;
   protected int currentHorizAlign = 0;
   protected int currentVertAlign = 0;
   public static final int PEN = 1;
   public static final int BRUSH = 2;
   public static final int FONT = 3;
   public static final int NULL_PEN = 4;
   public static final int NULL_BRUSH = 5;
   public static final int PALETTE = 6;
   public static final int OBJ_BITMAP = 7;
   public static final int OBJ_REGION = 8;
   protected WMFRecordStore currentStore;
   protected transient boolean bReadingWMF = true;
   protected transient BufferedInputStream bufStream = null;

   protected BufferedImage getImage(byte[] var1, int var2, int var3) {
      int var4 = (var1[7] & 255) << 24 | (var1[6] & 255) << 16 | (var1[5] & 255) << 8 | var1[4] & 255;
      int var5 = (var1[11] & 255) << 24 | (var1[10] & 255) << 16 | (var1[9] & 255) << 8 | var1[8] & 255;
      return var2 == var4 && var3 == var5 ? this.getImage(var1) : null;
   }

   protected Dimension getImageDimension(byte[] var1) {
      int var2 = (var1[7] & 255) << 24 | (var1[6] & 255) << 16 | (var1[5] & 255) << 8 | var1[4] & 255;
      int var3 = (var1[11] & 255) << 24 | (var1[10] & 255) << 16 | (var1[9] & 255) << 8 | var1[8] & 255;
      return new Dimension(var2, var3);
   }

   protected BufferedImage getImage(byte[] var1) {
      int var2 = (var1[7] & 255) << 24 | (var1[6] & 255) << 16 | (var1[5] & 255) << 8 | var1[4] & 255;
      int var3 = (var1[11] & 255) << 24 | (var1[10] & 255) << 16 | (var1[9] & 255) << 8 | var1[8] & 255;
      int[] var4 = new int[var2 * var3];
      BufferedImage var5 = new BufferedImage(var2, var3, 1);
      WritableRaster var6 = var5.getRaster();
      int var7 = (var1[3] & 255) << 24 | (var1[2] & 255) << 16 | (var1[1] & 255) << 8 | var1[0] & 255;
      int var8 = (var1[13] & 255) << 8 | var1[12] & 255;
      int var9 = (var1[15] & 255) << 8 | var1[14] & 255;
      int var10 = (var1[23] & 255) << 24 | (var1[22] & 255) << 16 | (var1[21] & 255) << 8 | var1[20] & 255;
      if (var10 == 0) {
         var10 = ((var2 * var9 + 31 & -32) >> 3) * var3;
      }

      int var11 = (var1[35] & 255) << 24 | (var1[34] & 255) << 16 | (var1[33] & 255) << 8 | var1[32] & 255;
      int var12;
      int var13;
      int var15;
      if (var9 == 24) {
         var12 = var10 / var3 - var2 * 3;
         var13 = var7;

         for(int var14 = 0; var14 < var3; ++var14) {
            for(var15 = 0; var15 < var2; ++var15) {
               var4[var2 * (var3 - var14 - 1) + var15] = -16777216 | (var1[var13 + 2] & 255) << 16 | (var1[var13 + 1] & 255) << 8 | var1[var13] & 255;
               var13 += 3;
            }

            var13 += var12;
         }
      } else {
         int var17;
         int[] var22;
         if (var9 == 8) {
            boolean var20 = false;
            if (var11 > 0) {
               var12 = var11;
            } else {
               var12 = 256;
            }

            var13 = var7;
            var22 = new int[var12];

            for(var15 = 0; var15 < var12; ++var15) {
               var22[var15] = -16777216 | (var1[var13 + 2] & 255) << 16 | (var1[var13 + 1] & 255) << 8 | var1[var13] & 255;
               var13 += 4;
            }

            var10 = var1.length - var13;
            var15 = var10 / var3 - var2;

            for(int var16 = 0; var16 < var3; ++var16) {
               for(var17 = 0; var17 < var2; ++var17) {
                  var4[var2 * (var3 - var16 - 1) + var17] = var22[var1[var13] & 255];
                  ++var13;
               }

               var13 += var15;
            }
         } else if (var9 == 1) {
            byte var21 = 2;
            var13 = var7;
            var22 = new int[var21];

            for(var15 = 0; var15 < var21; ++var15) {
               var22[var15] = -16777216 | (var1[var13 + 2] & 255) << 16 | (var1[var13 + 1] & 255) << 8 | var1[var13] & 255;
               var13 += 4;
            }

            var15 = 7;
            byte var23 = var1[var13];
            var17 = var10 / var3 - var2 / 8;

            for(int var18 = 0; var18 < var3; ++var18) {
               for(int var19 = 0; var19 < var2; ++var19) {
                  if ((var23 & 1 << var15) != 0) {
                     var4[var2 * (var3 - var18 - 1) + var19] = var22[1];
                  } else {
                     var4[var2 * (var3 - var18 - 1) + var19] = var22[0];
                  }

                  --var15;
                  if (var15 == -1) {
                     var15 = 7;
                     ++var13;
                     var23 = var1[var13];
                  }
               }

               var13 += var17;
               var15 = 7;
               if (var13 < var1.length) {
                  var23 = var1[var13];
               }
            }
         }
      }

      var6.setDataElements(0, 0, var2, var3, var4);
      return var5;
   }

   protected AttributedCharacterIterator getCharacterIterator(Graphics2D var1, String var2, WMFFont var3) {
      return this.getAttributedString(var1, var2, var3).getIterator();
   }

   protected AttributedCharacterIterator getCharacterIterator(Graphics2D var1, String var2, WMFFont var3, int var4) {
      AttributedString var5 = this.getAttributedString(var1, var2, var3);
      return var5.getIterator();
   }

   protected AttributedString getAttributedString(Graphics2D var1, String var2, WMFFont var3) {
      AttributedString var4 = new AttributedString(var2);
      Font var5 = var1.getFont();
      var4.addAttribute(TextAttribute.SIZE, new Float(var5.getSize2D()));
      var4.addAttribute(TextAttribute.FONT, var5);
      if (this.wmfFont.underline != 0) {
         var4.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
      }

      if (this.wmfFont.italic != 0) {
         var4.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
      } else {
         var4.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_REGULAR);
      }

      if (this.wmfFont.weight > 400) {
         var4.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
      } else {
         var4.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
      }

      return var4;
   }

   public void setRecordStore(WMFRecordStore var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.currentStore = var1;
      }
   }

   public WMFRecordStore getRecordStore() {
      return this.currentStore;
   }

   protected int addObject(WMFRecordStore var1, int var2, Object var3) {
      return this.currentStore.addObject(var2, var3);
   }

   protected int addObjectAt(WMFRecordStore var1, int var2, Object var3, int var4) {
      return this.currentStore.addObjectAt(var2, var3, var4);
   }
}

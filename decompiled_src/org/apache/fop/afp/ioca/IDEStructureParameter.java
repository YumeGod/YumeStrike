package org.apache.fop.afp.ioca;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.Streamable;

public class IDEStructureParameter implements Streamable {
   public static final byte COLOR_MODEL_RGB = 1;
   public static final byte COLOR_MODEL_YCRCB = 2;
   public static final byte COLOR_MODEL_CMYK = 4;
   public static final byte COLOR_MODEL_YCBCR = 18;
   private boolean subtractive = false;
   private boolean grayCoding = false;
   private byte colorModel = 2;
   private byte[] bitsPerIDE = new byte[]{1};

   public void setColorModel(byte color) {
      this.colorModel = color;
   }

   public void setDefaultRGBColorModel() {
      this.colorModel = 1;
      this.setUniformBitsPerComponent(3, 8);
   }

   public void setDefaultCMYKColorModel() {
      this.colorModel = 4;
      this.setUniformBitsPerComponent(4, 8);
   }

   public void setUniformBitsPerComponent(int numComponents, int bitsPerComponent) {
      if (bitsPerComponent >= 0 && bitsPerComponent < 256) {
         this.bitsPerIDE = new byte[numComponents];

         for(int i = 0; i < numComponents; ++i) {
            this.bitsPerIDE[i] = (byte)bitsPerComponent;
         }

      } else {
         throw new IllegalArgumentException("The number of bits per component must be between 0 and 255");
      }
   }

   public void setBitsPerComponent(int[] bitsPerComponent) {
      int numComponents = bitsPerComponent.length;
      this.bitsPerIDE = new byte[numComponents];

      for(int i = 0; i < numComponents; ++i) {
         int bits = bitsPerComponent[i];
         if (bits < 0 || bits >= 256) {
            throw new IllegalArgumentException("The number of bits per component must be between 0 and 255");
         }

         this.bitsPerIDE[i] = (byte)bits;
      }

   }

   public void setSubtractive(boolean subtractive) {
      this.subtractive = subtractive;
   }

   public void writeToStream(OutputStream os) throws IOException {
      int length = 7 + this.bitsPerIDE.length;
      byte flags = 0;
      if (this.subtractive) {
         flags = (byte)(flags | 128);
      }

      if (this.grayCoding) {
         flags = (byte)(flags | 64);
      }

      DataOutputStream dout = new DataOutputStream(os);
      dout.writeByte(155);
      dout.writeByte(length - 2);
      dout.writeByte(flags);
      dout.writeByte(this.colorModel);

      for(int i = 0; i < 3; ++i) {
         dout.writeByte(0);
      }

      dout.write(this.bitsPerIDE);
   }
}

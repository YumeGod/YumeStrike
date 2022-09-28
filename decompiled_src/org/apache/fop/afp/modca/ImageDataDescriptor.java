package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class ImageDataDescriptor extends AbstractDescriptor {
   public static final byte FUNCTION_SET_FS10 = 10;
   public static final byte FUNCTION_SET_FS11 = 11;
   public static final byte FUNCTION_SET_FS45 = 45;
   private byte functionSet = 11;

   public ImageDataDescriptor(int width, int height, int widthRes, int heightRes) {
      super(width, height, widthRes, heightRes);
   }

   public void setFunctionSet(byte functionSet) {
      this.functionSet = functionSet;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[22];
      this.copySF(data, (byte)-90, (byte)-5);
      byte[] len = BinaryUtils.convert(data.length - 1, 2);
      data[1] = len[0];
      data[2] = len[1];
      byte[] x = BinaryUtils.convert(this.widthRes, 2);
      data[10] = x[0];
      data[11] = x[1];
      byte[] y = BinaryUtils.convert(this.heightRes, 2);
      data[12] = y[0];
      data[13] = y[1];
      byte[] w = BinaryUtils.convert(this.width, 2);
      data[14] = w[0];
      data[15] = w[1];
      byte[] h = BinaryUtils.convert(this.height, 2);
      data[16] = h[0];
      data[17] = h[1];
      data[18] = -9;
      data[19] = 2;
      data[20] = 1;
      data[21] = this.functionSet;
      os.write(data);
   }
}

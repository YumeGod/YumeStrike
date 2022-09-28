package org.apache.fop.afp.goca;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class GraphicsCharacterString extends AbstractGraphicsCoord {
   protected static final int MAX_STR_LEN = 255;
   protected final String str;

   public GraphicsCharacterString(String str, int x, int y) {
      super(x, y);
      this.str = this.truncate(str, 255);
   }

   public GraphicsCharacterString(String str) {
      super((int[])null);
      this.str = this.truncate(str, 255);
   }

   byte getOrderCode() {
      return (byte)(this.isRelative() ? -125 : -61);
   }

   public int getDataLength() {
      return super.getDataLength() + this.str.length();
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = this.getData();
      byte[] strData = this.getStringAsBytes();
      System.arraycopy(strData, 0, data, 6, strData.length);
      os.write(data);
   }

   private byte[] getStringAsBytes() throws UnsupportedEncodingException {
      return this.str.getBytes("Cp1146");
   }

   public String toString() {
      return "GraphicsCharacterString{" + (this.coords != null ? "x=" + this.coords[0] + ", y=" + this.coords[1] : "") + "str='" + this.str + "'" + "}";
   }
}

package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class PreprocessPresentationObject extends AbstractTripletStructuredObject {
   private static final byte TYPE_OTHER = -110;
   private static final byte TYPE_OVERLAY = -33;
   private static final byte TYPE_IMAGE = -5;
   private byte objType = -110;
   private byte objOrent = 0;
   private int objXOffset = -1;
   private int objYOffset = -1;
   public static final byte ORIENTATION_ZERO_DEGREES = 1;
   public static final byte ORIENTATION_90_DEGREES = 2;
   public static final byte ORIENTATION_180_DEGREES = 4;
   public static final byte ORIENTATION_270_DEGREES = 8;

   public PreprocessPresentationObject(AbstractTripletStructuredObject prePresObj) {
      if (!(prePresObj instanceof ImageObject) && !(prePresObj instanceof Overlay)) {
         this.objType = -110;
      } else {
         if (prePresObj instanceof ImageObject) {
            this.objType = -5;
         } else {
            this.objType = -33;
         }

         this.setFullyQualifiedName((byte)-124, (byte)0, prePresObj.getFullyQualifiedName());
      }

   }

   public void setOrientation(byte orientation) {
      this.objOrent = orientation;
   }

   public void setXOffset(int xOffset) {
      this.objXOffset = xOffset;
   }

   public void setYOffset(int yOffset) {
      this.objYOffset = yOffset;
   }

   public void writeStart(OutputStream os) throws IOException {
      super.writeStart(os);
      byte[] data = new byte[9];
      this.copySF(data, (byte)-83, (byte)-61);
      byte[] l = BinaryUtils.convert(19 + this.getTripletDataLength(), 2);
      data[1] = l[0];
      data[2] = l[1];
      os.write(data);
   }

   public void writeContent(OutputStream os) throws IOException {
      byte[] data = new byte[12];
      byte[] l = BinaryUtils.convert(12 + this.getTripletDataLength(), 2);
      data[0] = l[0];
      data[1] = l[1];
      data[2] = this.objType;
      data[3] = 0;
      data[4] = 0;
      data[5] = this.objOrent;
      byte[] yOff;
      if (this.objXOffset > 0) {
         yOff = BinaryUtils.convert(this.objYOffset, 3);
         data[6] = yOff[0];
         data[7] = yOff[1];
         data[8] = yOff[2];
      } else {
         data[6] = -1;
         data[7] = -1;
         data[8] = -1;
      }

      if (this.objYOffset > 0) {
         yOff = BinaryUtils.convert(this.objYOffset, 3);
         data[9] = yOff[0];
         data[10] = yOff[1];
         data[11] = yOff[2];
      } else {
         data[9] = -1;
         data[10] = -1;
         data[11] = -1;
      }

      os.write(data);
      this.writeTriplets(os);
   }
}

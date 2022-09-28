package org.apache.fop.afp.ioca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.modca.AbstractStructuredObject;

public class ImageContent extends AbstractStructuredObject {
   public static final byte COMPID_G3_MH = -128;
   public static final byte COMPID_G3_MR = -127;
   public static final byte COMPID_G3_MMR = -126;
   private ImageSizeParameter imageSizeParameter = null;
   private IDEStructureParameter ideStructureParameter = null;
   private byte encoding = 3;
   private byte ideSize = 1;
   private byte compression = -64;
   private byte[] data;
   private static final int MAX_DATA_LEN = 65535;

   public void setImageSizeParameter(ImageSizeParameter imageSizeParameter) {
      this.imageSizeParameter = imageSizeParameter;
   }

   public void setIDEStructureParameter(IDEStructureParameter parameter) {
      this.ideStructureParameter = parameter;
   }

   public IDEStructureParameter getIDEStructureParameter() {
      return this.ideStructureParameter;
   }

   public IDEStructureParameter needIDEStructureParameter() {
      if (this.ideStructureParameter == null) {
         this.setIDEStructureParameter(new IDEStructureParameter());
      }

      return this.getIDEStructureParameter();
   }

   public void setImageEncoding(byte enc) {
      this.encoding = enc;
   }

   public void setImageCompression(byte comp) {
      this.compression = comp;
   }

   public void setImageIDESize(byte s) {
      this.ideSize = s;
   }

   /** @deprecated */
   public void setImageIDEColorModel(byte color) {
      this.needIDEStructureParameter().setColorModel(color);
   }

   /** @deprecated */
   public void setSubtractive(boolean subtractive) {
      this.needIDEStructureParameter().setSubtractive(subtractive);
   }

   public void setImageData(byte[] imageData) {
      this.data = imageData;
   }

   protected void writeContent(OutputStream os) throws IOException {
      if (this.imageSizeParameter != null) {
         this.imageSizeParameter.writeToStream(os);
      }

      os.write(this.getImageEncodingParameter());
      os.write(this.getImageIDESizeParameter());
      if (this.getIDEStructureParameter() != null) {
         this.getIDEStructureParameter().writeToStream(os);
      }

      boolean useFS10 = this.ideSize == 1;
      if (!useFS10) {
         os.write(this.getExternalAlgorithmParameter());
      }

      byte[] dataHeader = new byte[]{-2, -110, 0, 0};
      int lengthOffset = true;
      if (this.data != null) {
         writeChunksToStream(this.data, dataHeader, 2, 65535, os);
      }

   }

   protected void writeStart(OutputStream os) throws IOException {
      byte[] startData = new byte[]{-111, 1, -1};
      os.write(startData);
   }

   protected void writeEnd(OutputStream os) throws IOException {
      byte[] endData = new byte[]{-109, 0};
      os.write(endData);
   }

   private byte[] getImageEncodingParameter() {
      byte[] encodingData = new byte[]{-107, 2, this.encoding, 1};
      return encodingData;
   }

   private byte[] getExternalAlgorithmParameter() {
      if (this.encoding == -125 && this.compression != 0) {
         byte[] extAlgData = new byte[]{-107, 0, 16, 0, -125, 0, 0, 0, this.compression, 0, 0, 0};
         extAlgData[1] = (byte)(extAlgData.length - 2);
         return extAlgData;
      } else {
         return new byte[0];
      }
   }

   private byte[] getImageIDESizeParameter() {
      if (this.ideSize != 1) {
         byte[] ideSizeData = new byte[]{-106, 1, this.ideSize};
         return ideSizeData;
      } else {
         return new byte[0];
      }
   }
}

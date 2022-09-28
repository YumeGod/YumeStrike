package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.fop.afp.AFPDataObjectInfo;
import org.apache.fop.afp.AFPImageObjectInfo;
import org.apache.fop.afp.Factory;
import org.apache.fop.afp.ioca.ImageSegment;

public class ImageObject extends AbstractDataObject {
   private static final int MAX_DATA_LEN = 8192;
   private ImageSegment imageSegment = null;

   public ImageObject(Factory factory, String name) {
      super(factory, name);
   }

   public ImageSegment getImageSegment() {
      if (this.imageSegment == null) {
         this.imageSegment = this.factory.createImageSegment();
      }

      return this.imageSegment;
   }

   public void setViewport(AFPDataObjectInfo dataObjectInfo) {
      super.setViewport(dataObjectInfo);
      AFPImageObjectInfo imageObjectInfo = (AFPImageObjectInfo)dataObjectInfo;
      int dataWidth = imageObjectInfo.getDataWidth();
      int dataHeight = imageObjectInfo.getDataHeight();
      int dataWidthRes = imageObjectInfo.getDataWidthRes();
      int dataHeightRes = imageObjectInfo.getDataWidthRes();
      ImageDataDescriptor imageDataDescriptor = this.factory.createImageDataDescriptor(dataWidth, dataHeight, dataWidthRes, dataHeightRes);
      if (imageObjectInfo.getBitsPerPixel() == 1) {
         imageDataDescriptor.setFunctionSet((byte)10);
      } else if ("image/x-afp+fs45".equals(imageObjectInfo.getMimeType())) {
         imageDataDescriptor.setFunctionSet((byte)45);
      }

      this.getObjectEnvironmentGroup().setDataDescriptor(imageDataDescriptor);
      this.getObjectEnvironmentGroup().setMapImageObject(new MapImageObject(dataObjectInfo.getMappingOption()));
      this.getImageSegment().setImageSize(dataWidth, dataHeight, dataWidthRes, dataHeightRes);
   }

   public void setEncoding(byte encoding) {
      this.getImageSegment().setEncoding(encoding);
   }

   public void setCompression(byte compression) {
      this.getImageSegment().setCompression(compression);
   }

   public void setIDESize(byte size) {
      this.getImageSegment().setIDESize(size);
   }

   /** @deprecated */
   public void setIDEColorModel(byte colorModel) {
      this.getImageSegment().setIDEColorModel(colorModel);
   }

   /** @deprecated */
   public void setSubtractive(boolean subtractive) {
      this.getImageSegment().setSubtractive(subtractive);
   }

   public void setData(byte[] imageData) {
      this.getImageSegment().setData(imageData);
   }

   protected void writeStart(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-88, (byte)-5);
      os.write(data);
   }

   protected void writeContent(OutputStream os) throws IOException {
      super.writeContent(os);
      if (this.imageSegment != null) {
         byte[] dataHeader = new byte[9];
         copySF(dataHeader, (byte)-45, (byte)-18, (byte)-5);
         int lengthOffset = true;
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         this.imageSegment.writeToStream(baos);
         byte[] data = baos.toByteArray();
         writeChunksToStream(data, dataHeader, 1, 8192, os);
      }

   }

   protected void writeEnd(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-87, (byte)-5);
      os.write(data);
   }
}

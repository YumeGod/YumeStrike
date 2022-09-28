package org.apache.fop.afp.ioca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.Factory;
import org.apache.fop.afp.modca.AbstractNamedAFPObject;

public class ImageSegment extends AbstractNamedAFPObject {
   private ImageContent imageContent = null;
   private final Factory factory;
   private static final int NAME_LENGTH = 4;

   public ImageSegment(Factory factory, String name) {
      super(name);
      this.factory = factory;
   }

   public ImageContent getImageContent() {
      if (this.imageContent == null) {
         this.imageContent = this.factory.createImageContent();
      }

      return this.imageContent;
   }

   public void setImageSize(int hsize, int vsize, int hresol, int vresol) {
      ImageSizeParameter imageSizeParameter = this.factory.createImageSizeParameter(hsize, vsize, hresol, vresol);
      this.getImageContent().setImageSizeParameter(imageSizeParameter);
   }

   public void setEncoding(byte encoding) {
      this.getImageContent().setImageEncoding(encoding);
   }

   public void setCompression(byte compression) {
      this.getImageContent().setImageCompression(compression);
   }

   public void setIDESize(byte size) {
      this.getImageContent().setImageIDESize(size);
   }

   /** @deprecated */
   public void setIDEColorModel(byte colorModel) {
      this.getImageContent().setImageIDEColorModel(colorModel);
   }

   /** @deprecated */
   public void setSubtractive(boolean subtractive) {
      this.getImageContent().setSubtractive(subtractive);
   }

   public void setData(byte[] imageData) {
      this.getImageContent().setImageData(imageData);
   }

   public void writeContent(OutputStream os) throws IOException {
      if (this.imageContent != null) {
         this.imageContent.writeToStream(os);
      }

   }

   protected int getNameLength() {
      return 4;
   }

   protected void writeStart(OutputStream os) throws IOException {
      byte[] data = new byte[]{112, 0};
      os.write(data);
   }

   protected void writeEnd(OutputStream os) throws IOException {
      byte[] data = new byte[]{113, 0};
      os.write(data);
   }
}

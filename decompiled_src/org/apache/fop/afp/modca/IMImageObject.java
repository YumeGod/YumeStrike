package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.ioca.ImageCellPosition;
import org.apache.fop.afp.ioca.ImageInputDescriptor;
import org.apache.fop.afp.ioca.ImageOutputControl;
import org.apache.fop.afp.ioca.ImageRasterData;

public class IMImageObject extends AbstractNamedAFPObject {
   private ImageOutputControl imageOutputControl = null;
   private ImageInputDescriptor imageInputDescriptor = null;
   private ImageCellPosition imageCellPosition = null;
   private ImageRasterData imageRasterData = null;

   public IMImageObject(String name) {
      super(name);
   }

   public void setImageOutputControl(ImageOutputControl imageOutputControl) {
      this.imageOutputControl = imageOutputControl;
   }

   public void setImageCellPosition(ImageCellPosition imageCellPosition) {
      this.imageCellPosition = imageCellPosition;
   }

   public void setImageInputDescriptor(ImageInputDescriptor imageInputDescriptor) {
      this.imageInputDescriptor = imageInputDescriptor;
   }

   public void setImageRasterData(ImageRasterData imageRasterData) {
      this.imageRasterData = imageRasterData;
   }

   protected void writeContent(OutputStream os) throws IOException {
      super.writeContent(os);
      if (this.imageOutputControl != null) {
         this.imageOutputControl.writeToStream(os);
      }

      if (this.imageInputDescriptor != null) {
         this.imageInputDescriptor.writeToStream(os);
      }

      if (this.imageCellPosition != null) {
         this.imageCellPosition.writeToStream(os);
      }

      if (this.imageRasterData != null) {
         this.imageRasterData.writeToStream(os);
      }

   }

   protected void writeStart(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-88, (byte)123);
      os.write(data);
   }

   protected void writeEnd(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-87, (byte)123);
      os.write(data);
   }
}

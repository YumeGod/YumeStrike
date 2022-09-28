package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.Factory;
import org.apache.fop.afp.ioca.ImageCellPosition;
import org.apache.fop.afp.ioca.ImageInputDescriptor;
import org.apache.fop.afp.ioca.ImageOutputControl;
import org.apache.fop.afp.ioca.ImageRasterData;
import org.apache.fop.afp.ioca.ImageRasterPattern;

public class PageObject extends AbstractResourceGroupContainer {
   public PageObject(Factory factory, String name, int width, int height, int rotation, int widthRes, int heightRes) {
      super(factory, name, width, height, rotation, widthRes, heightRes);
   }

   public void createIncludePageOverlay(String name, int x, int y, int orientation) {
      this.getActiveEnvironmentGroup().createOverlay(name);
      IncludePageOverlay ipo = new IncludePageOverlay(name, x, y, orientation);
      this.addObject(ipo);
   }

   public void createShading(int x, int y, int w, int h, int red, int green, int blue) {
      int xCoord = false;
      int yCoord = false;
      int areaWidth = 0;
      int areaHeight = 0;
      int xCoord;
      int yCoord;
      switch (this.rotation) {
         case 90:
            xCoord = areaWidth - y - h;
            yCoord = x;
            areaWidth = h;
            areaHeight = w;
            break;
         case 180:
            xCoord = areaWidth - x - w;
            yCoord = areaHeight - y - h;
            areaWidth = w;
            areaHeight = h;
            break;
         case 270:
            xCoord = y;
            yCoord = areaHeight - x - w;
            areaWidth = h;
            areaHeight = w;
            break;
         default:
            xCoord = x;
            yCoord = y;
            areaWidth = w;
            areaHeight = h;
      }

      float shade = (float)((double)red * 0.3 + (double)green * 0.59 + (double)blue * 0.11);
      int grayscale = Math.round(shade / 255.0F * 16.0F);
      IMImageObject imImageObject = this.factory.createIMImageObject();
      ImageOutputControl imageOutputControl = new ImageOutputControl(0, 0);
      ImageInputDescriptor imageInputDescriptor = new ImageInputDescriptor();
      ImageCellPosition imageCellPosition = new ImageCellPosition(xCoord, yCoord);
      imageCellPosition.setXFillSize(areaWidth);
      imageCellPosition.setYFillSize(areaHeight);
      imageCellPosition.setXSize(64);
      imageCellPosition.setYSize(8);
      byte[] rasterData = ImageRasterPattern.getRasterData(grayscale);
      ImageRasterData imageRasterData = this.factory.createImageRasterData(rasterData);
      imImageObject.setImageOutputControl(imageOutputControl);
      imImageObject.setImageInputDescriptor(imageInputDescriptor);
      imImageObject.setImageCellPosition(imageCellPosition);
      imImageObject.setImageRasterData(imageRasterData);
      this.addObject(imImageObject);
   }

   protected void writeStart(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-88, (byte)-81);
      os.write(data);
   }

   protected void writeContent(OutputStream os) throws IOException {
      this.writeTriplets(os);
      this.getActiveEnvironmentGroup().writeToStream(os);
      this.writeObjects(this.tagLogicalElements, os);
      this.writeObjects(this.objects, os);
   }

   protected void writeEnd(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-87, (byte)-81);
      os.write(data);
   }

   public void addObject(Object obj) {
      this.endPresentationObject();
      super.addObject(obj);
   }

   public String toString() {
      return this.getName();
   }

   protected boolean canWrite(AbstractAFPObject ao) {
      return true;
   }
}

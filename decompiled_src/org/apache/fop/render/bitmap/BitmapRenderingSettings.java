package org.apache.fop.render.bitmap;

import org.apache.fop.render.java2d.Java2DRenderingSettings;
import org.apache.xmlgraphics.image.writer.ImageWriterParams;

public class BitmapRenderingSettings extends Java2DRenderingSettings implements TIFFConstants {
   private ImageWriterParams writerParams = new ImageWriterParams();
   private int bufferedImageType = 2;
   private boolean antialiasing = true;
   private boolean qualityRendering = true;

   public ImageWriterParams getWriterParams() {
      return this.writerParams;
   }

   public int getBufferedImageType() {
      return this.bufferedImageType;
   }

   public void setBufferedImageType(int bufferedImageType) {
      this.bufferedImageType = bufferedImageType;
   }

   public void setAntiAliasing(boolean value) {
      this.antialiasing = value;
   }

   public boolean isAntiAliasingEnabled() {
      return this.antialiasing;
   }

   public void setQualityRendering(boolean quality) {
      this.qualityRendering = quality;
   }

   public boolean isQualityRenderingEnabled() {
      return this.qualityRendering;
   }
}

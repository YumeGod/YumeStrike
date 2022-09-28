package org.apache.batik.ext.awt.image.rendered;

import java.awt.Rectangle;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Map;

public class TranslateRed extends AbstractRed {
   protected int deltaX;
   protected int deltaY;

   public TranslateRed(CachableRed var1, int var2, int var3) {
      super((CachableRed)var1, new Rectangle(var2, var3, var1.getWidth(), var1.getHeight()), var1.getColorModel(), var1.getSampleModel(), var1.getTileGridXOffset() + var2 - var1.getMinX(), var1.getTileGridYOffset() + var3 - var1.getMinY(), (Map)null);
      this.deltaX = var2 - var1.getMinX();
      this.deltaY = var3 - var1.getMinY();
   }

   public int getDeltaX() {
      return this.deltaX;
   }

   public int getDeltaY() {
      return this.deltaY;
   }

   public CachableRed getSource() {
      return (CachableRed)this.getSources().get(0);
   }

   public Object getProperty(String var1) {
      return this.getSource().getProperty(var1);
   }

   public String[] getPropertyNames() {
      return this.getSource().getPropertyNames();
   }

   public Raster getTile(int var1, int var2) {
      Raster var3 = this.getSource().getTile(var1, var2);
      return var3.createTranslatedChild(var3.getMinX() + this.deltaX, var3.getMinY() + this.deltaY);
   }

   public Raster getData() {
      Raster var1 = this.getSource().getData();
      return var1.createTranslatedChild(var1.getMinX() + this.deltaX, var1.getMinY() + this.deltaY);
   }

   public Raster getData(Rectangle var1) {
      Rectangle var2 = (Rectangle)var1.clone();
      var2.translate(-this.deltaX, -this.deltaY);
      Raster var3 = this.getSource().getData(var2);
      return var3.createTranslatedChild(var3.getMinX() + this.deltaX, var3.getMinY() + this.deltaY);
   }

   public WritableRaster copyData(WritableRaster var1) {
      WritableRaster var2 = var1.createWritableTranslatedChild(var1.getMinX() - this.deltaX, var1.getMinY() - this.deltaY);
      this.getSource().copyData(var2);
      return var1;
   }
}

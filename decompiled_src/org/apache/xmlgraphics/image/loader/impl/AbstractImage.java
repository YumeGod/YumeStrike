package org.apache.xmlgraphics.image.loader.impl;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSize;

public abstract class AbstractImage implements Image {
   private ImageInfo info;

   public AbstractImage(ImageInfo info) {
      this.info = info;
   }

   public ImageInfo getInfo() {
      return this.info;
   }

   public ImageSize getSize() {
      return this.getInfo().getSize();
   }

   public ColorSpace getColorSpace() {
      return null;
   }

   public ICC_Profile getICCProfile() {
      return this.getColorSpace() instanceof ICC_ColorSpace ? ((ICC_ColorSpace)this.getColorSpace()).getProfile() : null;
   }

   public String toString() {
      return this.getClass().getName() + ": " + this.getInfo();
   }
}

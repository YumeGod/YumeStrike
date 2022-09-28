package org.apache.xmlgraphics.image.loader.impl;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.RenderedImage;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;

public class ImageRendered extends AbstractImage {
   private final RenderedImage red;
   private final Color transparentColor;
   private final ColorSpace colorSpace;
   private final ICC_Profile iccProfile;

   public ImageRendered(ImageInfo info, RenderedImage red, Color transparentColor) {
      super(info);
      this.red = red;
      this.transparentColor = transparentColor;
      this.colorSpace = red.getColorModel().getColorSpace();
      if (this.colorSpace instanceof ICC_ColorSpace) {
         ICC_ColorSpace icccs = (ICC_ColorSpace)this.colorSpace;
         this.iccProfile = icccs.getProfile();
      } else {
         this.iccProfile = null;
      }

   }

   public ImageFlavor getFlavor() {
      return ImageFlavor.RENDERED_IMAGE;
   }

   public boolean isCacheable() {
      return true;
   }

   public RenderedImage getRenderedImage() {
      return this.red;
   }

   public ColorSpace getColorSpace() {
      return this.colorSpace;
   }

   public ICC_Profile getICCProfile() {
      return this.iccProfile;
   }

   public Color getTransparentColor() {
      return this.transparentColor;
   }
}

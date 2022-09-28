package org.apache.xmlgraphics.image.loader.impl;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.InputStream;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;

public class ImageRawJPEG extends ImageRawStream {
   private int sofType;
   private ColorSpace colorSpace;
   private ICC_Profile iccProfile;
   private boolean invertImage = false;

   public ImageRawJPEG(ImageInfo info, InputStream in, int sofType, ColorSpace colorSpace, ICC_Profile iccProfile, boolean invertImage) {
      super(info, ImageFlavor.RAW_JPEG, in);
      this.sofType = sofType;
      this.colorSpace = colorSpace;
      this.iccProfile = iccProfile;
      this.invertImage = invertImage;
   }

   public int getSOFType() {
      return this.sofType;
   }

   public ICC_Profile getICCProfile() {
      return this.iccProfile;
   }

   public boolean isInverted() {
      return this.invertImage;
   }

   public ColorSpace getColorSpace() {
      return this.colorSpace;
   }
}

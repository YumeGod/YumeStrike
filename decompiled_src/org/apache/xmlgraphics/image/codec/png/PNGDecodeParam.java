package org.apache.xmlgraphics.image.codec.png;

import org.apache.xmlgraphics.image.codec.util.ImageDecodeParam;
import org.apache.xmlgraphics.image.codec.util.PropertyUtil;

public class PNGDecodeParam implements ImageDecodeParam {
   private boolean suppressAlpha = false;
   private boolean expandPalette = false;
   private boolean output8BitGray = false;
   private boolean performGammaCorrection = true;
   private float userExponent = 1.0F;
   private float displayExponent = 2.2F;
   private boolean expandGrayAlpha = false;
   private boolean generateEncodeParam = false;
   private PNGEncodeParam encodeParam = null;

   public boolean getSuppressAlpha() {
      return this.suppressAlpha;
   }

   public void setSuppressAlpha(boolean suppressAlpha) {
      this.suppressAlpha = suppressAlpha;
   }

   public boolean getExpandPalette() {
      return this.expandPalette;
   }

   public void setExpandPalette(boolean expandPalette) {
      this.expandPalette = expandPalette;
   }

   public boolean getOutput8BitGray() {
      return this.output8BitGray;
   }

   public void setOutput8BitGray(boolean output8BitGray) {
      this.output8BitGray = output8BitGray;
   }

   public boolean getPerformGammaCorrection() {
      return this.performGammaCorrection;
   }

   public void setPerformGammaCorrection(boolean performGammaCorrection) {
      this.performGammaCorrection = performGammaCorrection;
   }

   public float getUserExponent() {
      return this.userExponent;
   }

   public void setUserExponent(float userExponent) {
      if (userExponent <= 0.0F) {
         throw new IllegalArgumentException(PropertyUtil.getString("PNGDecodeParam0"));
      } else {
         this.userExponent = userExponent;
      }
   }

   public float getDisplayExponent() {
      return this.displayExponent;
   }

   public void setDisplayExponent(float displayExponent) {
      if (displayExponent <= 0.0F) {
         throw new IllegalArgumentException(PropertyUtil.getString("PNGDecodeParam1"));
      } else {
         this.displayExponent = displayExponent;
      }
   }

   public boolean getExpandGrayAlpha() {
      return this.expandGrayAlpha;
   }

   public void setExpandGrayAlpha(boolean expandGrayAlpha) {
      this.expandGrayAlpha = expandGrayAlpha;
   }

   public boolean getGenerateEncodeParam() {
      return this.generateEncodeParam;
   }

   public void setGenerateEncodeParam(boolean generateEncodeParam) {
      this.generateEncodeParam = generateEncodeParam;
   }

   public PNGEncodeParam getEncodeParam() {
      return this.encodeParam;
   }

   public void setEncodeParam(PNGEncodeParam encodeParam) {
      this.encodeParam = encodeParam;
   }
}

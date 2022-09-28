package com.mxgraph.util.png;

public class mxPngDecodeParam {
   private boolean suppressAlpha = false;
   private boolean expandPalette = false;
   private boolean output8BitGray = false;
   private boolean performGammaCorrection = true;
   private float userExponent = 1.0F;
   private float displayExponent = 2.2F;
   private boolean expandGrayAlpha = false;
   private boolean generateEncodeParam = false;
   private mxPngEncodeParam encodeParam = null;

   public boolean getSuppressAlpha() {
      return this.suppressAlpha;
   }

   public void setSuppressAlpha(boolean var1) {
      this.suppressAlpha = var1;
   }

   public boolean getExpandPalette() {
      return this.expandPalette;
   }

   public void setExpandPalette(boolean var1) {
      this.expandPalette = var1;
   }

   public boolean getOutput8BitGray() {
      return this.output8BitGray;
   }

   public void setOutput8BitGray(boolean var1) {
      this.output8BitGray = var1;
   }

   public boolean getPerformGammaCorrection() {
      return this.performGammaCorrection;
   }

   public void setPerformGammaCorrection(boolean var1) {
      this.performGammaCorrection = var1;
   }

   public float getUserExponent() {
      return this.userExponent;
   }

   public void setUserExponent(float var1) {
      if (var1 <= 0.0F) {
         throw new IllegalArgumentException("PNGDecodeParam0");
      } else {
         this.userExponent = var1;
      }
   }

   public float getDisplayExponent() {
      return this.displayExponent;
   }

   public void setDisplayExponent(float var1) {
      if (var1 <= 0.0F) {
         throw new IllegalArgumentException("PNGDecodeParam1");
      } else {
         this.displayExponent = var1;
      }
   }

   public boolean getExpandGrayAlpha() {
      return this.expandGrayAlpha;
   }

   public void setExpandGrayAlpha(boolean var1) {
      this.expandGrayAlpha = var1;
   }

   public boolean getGenerateEncodeParam() {
      return this.generateEncodeParam;
   }

   public void setGenerateEncodeParam(boolean var1) {
      this.generateEncodeParam = var1;
   }

   public mxPngEncodeParam getEncodeParam() {
      return this.encodeParam;
   }

   public void setEncodeParam(mxPngEncodeParam var1) {
      this.encodeParam = var1;
   }
}

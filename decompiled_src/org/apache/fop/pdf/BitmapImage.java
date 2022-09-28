package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;

public class BitmapImage implements PDFImage {
   private int height;
   private int width;
   private int bitsPerComponent;
   private PDFDeviceColorSpace colorSpace;
   private byte[] bitmaps;
   private PDFReference maskRef;
   private PDFColor transparent = null;
   private String key;
   private PDFDocument pdfDoc;

   public BitmapImage(String k, int width, int height, byte[] data, String mask) {
      this.key = k;
      this.height = height;
      this.width = width;
      this.bitsPerComponent = 8;
      this.colorSpace = new PDFDeviceColorSpace(2);
      this.bitmaps = data;
      if (mask != null) {
         this.maskRef = new PDFReference(mask);
      }

   }

   public void setup(PDFDocument doc) {
      this.pdfDoc = doc;
   }

   public String getKey() {
      return this.key;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public void setColorSpace(PDFDeviceColorSpace cs) {
      this.colorSpace = cs;
   }

   public PDFDeviceColorSpace getColorSpace() {
      return this.colorSpace;
   }

   public int getBitsPerComponent() {
      return this.bitsPerComponent;
   }

   public void setTransparent(PDFColor t) {
      this.transparent = t;
   }

   public boolean isTransparent() {
      return this.transparent != null;
   }

   public PDFColor getTransparentColor() {
      return this.transparent;
   }

   public String getMask() {
      return null;
   }

   public PDFReference getSoftMaskReference() {
      return this.maskRef;
   }

   public boolean isInverted() {
      return false;
   }

   public void outputContents(OutputStream out) throws IOException {
      out.write(this.bitmaps);
   }

   public void populateXObjectDictionary(PDFDictionary dict) {
   }

   public PDFICCStream getICCStream() {
      return null;
   }

   public boolean isPS() {
      return false;
   }

   public String getFilterHint() {
      return "image";
   }

   public PDFFilter getPDFFilter() {
      return null;
   }
}

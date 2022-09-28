package org.apache.fop.afp;

import java.awt.Point;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.afp.fonts.AFPPageFonts;
import org.apache.fop.util.AbstractPaintingState;
import org.apache.xmlgraphics.java2d.color.ColorConverter;
import org.apache.xmlgraphics.java2d.color.DefaultColorConverter;

public class AFPPaintingState extends AbstractPaintingState implements Cloneable {
   private static final long serialVersionUID = 8206711712452344473L;
   private static Log log = LogFactory.getLog("org.apache.xmlgraphics.afp");
   private int portraitRotation = 0;
   private int landscapeRotation = 270;
   private boolean colorImages = false;
   private float ditheringQuality;
   private ColorConverter colorConverter = GrayScaleColorConverter.getInstance();
   private boolean nativeImagesSupported = false;
   private boolean cmykImagesSupported;
   private int bitsPerPixel = 8;
   private int resolution = 240;
   private transient AFPPagePaintingState pagePaintingState = new AFPPagePaintingState();
   private final transient AFPUnitConverter unitConv = new AFPUnitConverter(this);

   public void setPortraitRotation(int rotation) {
      if (rotation != 0 && rotation != 90 && rotation != 180 && rotation != 270) {
         throw new IllegalArgumentException("The portrait rotation must be one of the values 0, 90, 180, 270");
      } else {
         this.portraitRotation = rotation;
      }
   }

   protected int getPortraitRotation() {
      return this.portraitRotation;
   }

   public void setLandscapeRotation(int rotation) {
      if (rotation != 0 && rotation != 90 && rotation != 180 && rotation != 270) {
         throw new IllegalArgumentException("The landscape rotation must be one of the values 0, 90, 180, 270");
      } else {
         this.landscapeRotation = rotation;
      }
   }

   protected int getLandscapeRotation() {
      return this.landscapeRotation;
   }

   public void setBitsPerPixel(int bitsPerPixel) {
      switch (bitsPerPixel) {
         case 1:
         case 4:
         case 8:
            this.bitsPerPixel = bitsPerPixel;
            break;
         default:
            log.warn("Invalid bits_per_pixel value, must be 1, 4 or 8.");
            this.bitsPerPixel = 8;
      }

   }

   public int getBitsPerPixel() {
      return this.bitsPerPixel;
   }

   public void setColorImages(boolean colorImages) {
      this.colorImages = colorImages;
      if (colorImages) {
         this.colorConverter = DefaultColorConverter.getInstance();
      }

   }

   public boolean isColorImages() {
      return this.colorImages;
   }

   public ColorConverter getColorConverter() {
      return this.colorConverter;
   }

   public void setNativeImagesSupported(boolean nativeImagesSupported) {
      this.nativeImagesSupported = nativeImagesSupported;
   }

   public boolean isNativeImagesSupported() {
      return this.nativeImagesSupported;
   }

   public void setCMYKImagesSupported(boolean value) {
      this.cmykImagesSupported = value;
   }

   public boolean isCMYKImagesSupported() {
      return this.cmykImagesSupported;
   }

   public float getDitheringQuality() {
      return this.ditheringQuality;
   }

   public void setDitheringQuality(float quality) {
      quality = Math.max(quality, 0.0F);
      quality = Math.min(quality, 1.0F);
      this.ditheringQuality = quality;
   }

   public void setResolution(int resolution) {
      if (log.isDebugEnabled()) {
         log.debug("renderer-resolution set to: " + resolution + "dpi");
      }

      this.resolution = resolution;
   }

   public int getResolution() {
      return this.resolution;
   }

   protected AbstractPaintingState.AbstractData instantiateData() {
      return new AFPData();
   }

   protected AbstractPaintingState instantiate() {
      return new AFPPaintingState();
   }

   protected AFPPagePaintingState getPagePaintingState() {
      return this.pagePaintingState;
   }

   public AFPPageFonts getPageFonts() {
      return this.pagePaintingState.getFonts();
   }

   public void setPageWidth(int pageWidth) {
      this.pagePaintingState.setWidth(pageWidth);
   }

   public int getPageWidth() {
      return this.pagePaintingState.getWidth();
   }

   public void setPageHeight(int pageHeight) {
      this.pagePaintingState.setHeight(pageHeight);
   }

   public int getPageHeight() {
      return this.pagePaintingState.getHeight();
   }

   public int getPageRotation() {
      return this.pagePaintingState.getOrientation();
   }

   public void setImageUri(String uri) {
      ((AFPData)this.getData()).imageUri = uri;
   }

   public String getImageUri() {
      return ((AFPData)this.getData()).imageUri;
   }

   public int getRotation() {
      return this.getData().getDerivedRotation();
   }

   public AFPUnitConverter getUnitConverter() {
      return this.unitConv;
   }

   public Point getPoint(int x, int y) {
      Point p = new Point();
      int rotation = this.getRotation();
      switch (rotation) {
         case 90:
            p.x = y;
            p.y = this.getPageWidth() - x;
            break;
         case 180:
            p.x = this.getPageWidth() - x;
            p.y = this.getPageHeight() - y;
            break;
         case 270:
            p.x = this.getPageHeight() - y;
            p.y = x;
            break;
         default:
            p.x = x;
            p.y = y;
      }

      return p;
   }

   public Object clone() {
      AFPPaintingState paintingState = (AFPPaintingState)super.clone();
      paintingState.pagePaintingState = (AFPPagePaintingState)this.pagePaintingState.clone();
      paintingState.portraitRotation = this.portraitRotation;
      paintingState.landscapeRotation = this.landscapeRotation;
      paintingState.bitsPerPixel = this.bitsPerPixel;
      paintingState.colorImages = this.colorImages;
      paintingState.colorConverter = this.colorConverter;
      paintingState.resolution = this.resolution;
      return paintingState;
   }

   public String toString() {
      return "AFPPaintingState{portraitRotation=" + this.portraitRotation + ", landscapeRotation=" + this.landscapeRotation + ", colorImages=" + this.colorImages + ", bitsPerPixel=" + this.bitsPerPixel + ", resolution=" + this.resolution + ", pageState=" + this.pagePaintingState + super.toString() + "}";
   }

   private class AFPData extends AbstractPaintingState.AbstractData {
      private static final long serialVersionUID = -1789481244175275686L;
      private boolean filled;
      private String imageUri;

      private AFPData() {
         super();
         this.filled = false;
         this.imageUri = null;
      }

      public Object clone() {
         AFPData obj = (AFPData)super.clone();
         obj.filled = this.filled;
         obj.imageUri = this.imageUri;
         return obj;
      }

      public String toString() {
         return "AFPData{" + super.toString() + ", filled=" + this.filled + ", imageUri=" + this.imageUri + "}";
      }

      protected AbstractPaintingState.AbstractData instantiate() {
         return AFPPaintingState.this.new AFPData();
      }

      // $FF: synthetic method
      AFPData(Object x1) {
         this();
      }
   }

   private class AFPPagePaintingState implements Cloneable {
      private int width;
      private int height;
      private AFPPageFonts fonts;
      private int fontCount;
      private int orientation;

      private AFPPagePaintingState() {
         this.width = 0;
         this.height = 0;
         this.fonts = new AFPPageFonts();
         this.fontCount = 0;
         this.orientation = 0;
      }

      protected int getWidth() {
         return this.width;
      }

      protected void setWidth(int width) {
         this.width = width;
      }

      protected int getHeight() {
         return this.height;
      }

      protected void setHeight(int height) {
         this.height = height;
      }

      protected AFPPageFonts getFonts() {
         return this.fonts;
      }

      protected void setFonts(AFPPageFonts fonts) {
         this.fonts = fonts;
      }

      protected int incrementFontCount() {
         return ++this.fontCount;
      }

      protected int getOrientation() {
         return this.orientation;
      }

      protected void setOrientation(int orientation) {
         this.orientation = orientation;
      }

      public Object clone() {
         AFPPagePaintingState state = AFPPaintingState.this.new AFPPagePaintingState();
         state.width = this.width;
         state.height = this.height;
         state.orientation = this.orientation;
         state.fonts = new AFPPageFonts(this.fonts);
         state.fontCount = this.fontCount;
         return state;
      }

      public String toString() {
         return "AFPPagePaintingState{width=" + this.width + ", height=" + this.height + ", orientation=" + this.orientation + ", fonts=" + this.fonts + ", fontCount=" + this.fontCount + "}";
      }

      // $FF: synthetic method
      AFPPagePaintingState(Object x1) {
         this();
      }
   }
}

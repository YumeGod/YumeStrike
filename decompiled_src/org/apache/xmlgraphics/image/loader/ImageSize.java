package org.apache.xmlgraphics.image.loader;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import org.apache.xmlgraphics.java2d.Dimension2DDouble;
import org.apache.xmlgraphics.util.UnitConv;

public class ImageSize {
   private int widthPx;
   private int heightPx;
   private int widthMpt;
   private int heightMpt;
   private int baselinePositionFromBottomMpt;
   private double dpiHorizontal;
   private double dpiVertical;

   public ImageSize(int widthPx, int heightPx, double dpiHorizontal, double dpiVertical) {
      this.setSizeInPixels(widthPx, heightPx);
      this.setResolution(dpiHorizontal, dpiVertical);
   }

   public ImageSize(int widthPx, int heightPx, double dpi) {
      this(widthPx, heightPx, dpi, dpi);
   }

   public ImageSize() {
   }

   public void setSizeInPixels(int width, int height) {
      this.widthPx = width;
      this.heightPx = height;
   }

   public void setSizeInMillipoints(int width, int height) {
      this.widthMpt = width;
      this.heightMpt = height;
   }

   public void setResolution(double horizontal, double vertical) {
      this.dpiHorizontal = horizontal;
      this.dpiVertical = vertical;
   }

   public void setResolution(double resolution) {
      this.setResolution(resolution, resolution);
   }

   public void setBaselinePositionFromBottom(int distance) {
      this.baselinePositionFromBottomMpt = distance;
   }

   public int getBaselinePositionFromBottom() {
      return this.baselinePositionFromBottomMpt;
   }

   public int getWidthPx() {
      return this.widthPx;
   }

   public int getHeightPx() {
      return this.heightPx;
   }

   public int getWidthMpt() {
      return this.widthMpt;
   }

   public int getHeightMpt() {
      return this.heightMpt;
   }

   public double getDpiHorizontal() {
      return this.dpiHorizontal;
   }

   public double getDpiVertical() {
      return this.dpiVertical;
   }

   public Dimension getDimensionMpt() {
      return new Dimension(this.getWidthMpt(), this.getHeightMpt());
   }

   public Dimension2D getDimensionPt() {
      return new Dimension2DDouble((double)this.getWidthMpt() / 1000.0, (double)this.getHeightMpt() / 1000.0);
   }

   public Dimension getDimensionPx() {
      return new Dimension(this.getWidthPx(), this.getHeightPx());
   }

   private void checkResolutionAvailable() {
      if (this.dpiHorizontal == 0.0 || this.dpiVertical == 0.0) {
         throw new IllegalStateException("The resolution must be set");
      }
   }

   public void calcSizeFromPixels() {
      this.checkResolutionAvailable();
      this.widthMpt = (int)Math.round(UnitConv.in2mpt((double)this.widthPx / this.dpiHorizontal));
      this.heightMpt = (int)Math.round(UnitConv.in2mpt((double)this.heightPx / this.dpiVertical));
   }

   public void calcPixelsFromSize() {
      this.checkResolutionAvailable();
      this.widthPx = (int)Math.round(UnitConv.mpt2in((double)this.widthMpt * this.dpiHorizontal));
      this.heightPx = (int)Math.round(UnitConv.mpt2in((double)this.heightMpt * this.dpiVertical));
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("Size: ");
      sb.append(this.getWidthMpt()).append('x').append(this.getHeightMpt()).append(" mpt");
      sb.append(" (");
      sb.append(this.getWidthPx()).append('x').append(this.getHeightPx()).append(" px");
      sb.append(" at ").append(this.getDpiHorizontal()).append('x').append(this.getDpiVertical());
      sb.append(" dpi");
      sb.append(")");
      return sb.toString();
   }
}

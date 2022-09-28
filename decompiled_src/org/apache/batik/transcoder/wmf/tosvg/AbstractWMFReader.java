package org.apache.batik.transcoder.wmf.tosvg;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWMFReader {
   public static final float PIXEL_PER_INCH = (float)Toolkit.getDefaultToolkit().getScreenResolution();
   public static final float MM_PER_PIXEL = 25.4F / (float)Toolkit.getDefaultToolkit().getScreenResolution();
   protected int left;
   protected int right;
   protected int top;
   protected int bottom;
   protected int width;
   protected int height;
   protected int inch;
   protected float scaleX;
   protected float scaleY;
   protected float scaleXY;
   protected int vpW;
   protected int vpH;
   protected int vpX;
   protected int vpY;
   protected int xSign;
   protected int ySign;
   protected volatile boolean bReading;
   protected boolean isAldus;
   protected boolean isotropic;
   protected int mtType;
   protected int mtHeaderSize;
   protected int mtVersion;
   protected int mtSize;
   protected int mtNoObjects;
   protected int mtMaxRecord;
   protected int mtNoParameters;
   protected int windowWidth;
   protected int windowHeight;
   protected int numObjects;
   protected List objectVector;
   public int lastObjectIdx;

   public AbstractWMFReader() {
      this.xSign = 1;
      this.ySign = 1;
      this.bReading = false;
      this.isAldus = false;
      this.isotropic = true;
      this.scaleX = 1.0F;
      this.scaleY = 1.0F;
      this.scaleXY = 1.0F;
      this.left = -1;
      this.top = -1;
      this.width = -1;
      this.height = -1;
      this.right = this.left + this.width;
      this.bottom = this.top + this.height;
      this.numObjects = 0;
      this.objectVector = new ArrayList();
   }

   public AbstractWMFReader(int var1, int var2) {
      this();
      this.width = var1;
      this.height = var2;
   }

   protected short readShort(DataInputStream var1) throws IOException {
      byte[] var2 = new byte[2];
      var1.readFully(var2);
      int var3 = (255 & var2[1]) << 8;
      short var4 = (short)('\uffff' & var3);
      var4 = (short)(var4 | 255 & var2[0]);
      return var4;
   }

   protected int readInt(DataInputStream var1) throws IOException {
      byte[] var2 = new byte[4];
      var1.readFully(var2);
      int var3 = (255 & var2[3]) << 24;
      var3 |= (255 & var2[2]) << 16;
      var3 |= (255 & var2[1]) << 8;
      var3 |= 255 & var2[0];
      return var3;
   }

   public float getViewportWidthUnits() {
      return (float)this.vpW;
   }

   public float getViewportHeightUnits() {
      return (float)this.vpH;
   }

   public float getViewportWidthInch() {
      return (float)this.vpW / (float)this.inch;
   }

   public float getViewportHeightInch() {
      return (float)this.vpH / (float)this.inch;
   }

   public float getPixelsPerUnit() {
      return PIXEL_PER_INCH / (float)this.inch;
   }

   public int getVpW() {
      return (int)(PIXEL_PER_INCH * (float)this.vpW / (float)this.inch);
   }

   public int getVpH() {
      return (int)(PIXEL_PER_INCH * (float)this.vpH / (float)this.inch);
   }

   public int getLeftUnits() {
      return this.left;
   }

   public int getRightUnits() {
      return this.right;
   }

   public int getTopUnits() {
      return this.top;
   }

   public int getWidthUnits() {
      return this.width;
   }

   public int getHeightUnits() {
      return this.height;
   }

   public int getBottomUnits() {
      return this.bottom;
   }

   public int getMetaFileUnitsPerInch() {
      return this.inch;
   }

   public Rectangle getRectangleUnits() {
      Rectangle var1 = new Rectangle(this.left, this.top, this.width, this.height);
      return var1;
   }

   public Rectangle2D getRectanglePixel() {
      float var1 = PIXEL_PER_INCH * (float)this.left / (float)this.inch;
      float var2 = PIXEL_PER_INCH * (float)this.right / (float)this.inch;
      float var3 = PIXEL_PER_INCH * (float)this.top / (float)this.inch;
      float var4 = PIXEL_PER_INCH * (float)this.bottom / (float)this.inch;
      Rectangle2D.Float var5 = new Rectangle2D.Float(var1, var3, var2 - var1, var4 - var3);
      return var5;
   }

   public Rectangle2D getRectangleInch() {
      float var1 = (float)this.left / (float)this.inch;
      float var2 = (float)this.right / (float)this.inch;
      float var3 = (float)this.top / (float)this.inch;
      float var4 = (float)this.bottom / (float)this.inch;
      Rectangle2D.Float var5 = new Rectangle2D.Float(var1, var3, var2 - var1, var4 - var3);
      return var5;
   }

   public int getWidthPixels() {
      return (int)(PIXEL_PER_INCH * (float)this.width / (float)this.inch);
   }

   public float getUnitsToPixels() {
      return PIXEL_PER_INCH / (float)this.inch;
   }

   public float getVpWFactor() {
      return PIXEL_PER_INCH * (float)this.width / (float)this.inch / (float)this.vpW;
   }

   public float getVpHFactor() {
      return PIXEL_PER_INCH * (float)this.height / (float)this.inch / (float)this.vpH;
   }

   public int getHeightPixels() {
      return (int)(PIXEL_PER_INCH * (float)this.height / (float)this.inch);
   }

   public int getXSign() {
      return this.xSign;
   }

   public int getYSign() {
      return this.ySign;
   }

   protected synchronized void setReading(boolean var1) {
      this.bReading = var1;
   }

   public synchronized boolean isReading() {
      return this.bReading;
   }

   public abstract void reset();

   protected abstract boolean readRecords(DataInputStream var1) throws IOException;

   public void read(DataInputStream var1) throws IOException {
      this.reset();
      this.setReading(true);
      int var2 = this.readInt(var1);
      if (var2 == -1698247209) {
         this.isAldus = true;
         this.readShort(var1);
         this.left = this.readShort(var1);
         this.top = this.readShort(var1);
         this.right = this.readShort(var1);
         this.bottom = this.readShort(var1);
         this.inch = this.readShort(var1);
         this.readInt(var1);
         this.readShort(var1);
         int var6;
         if (this.left > this.right) {
            var6 = this.right;
            this.right = this.left;
            this.left = var6;
            this.xSign = -1;
         }

         if (this.top > this.bottom) {
            var6 = this.bottom;
            this.bottom = this.top;
            this.top = var6;
            this.ySign = -1;
         }

         this.width = this.right - this.left;
         this.height = this.bottom - this.top;
         this.mtType = this.readShort(var1);
         this.mtHeaderSize = this.readShort(var1);
      } else {
         this.mtType = var2 << 16 >> 16;
         this.mtHeaderSize = var2 >> 16;
      }

      this.mtVersion = this.readShort(var1);
      this.mtSize = this.readInt(var1);
      this.mtNoObjects = this.readShort(var1);
      this.mtMaxRecord = this.readInt(var1);
      this.mtNoParameters = this.readShort(var1);
      this.numObjects = this.mtNoObjects;
      ArrayList var3 = new ArrayList(this.numObjects);

      for(int var4 = 0; var4 < this.numObjects; ++var4) {
         var3.add(new GdiObject(var4, false));
      }

      this.objectVector.addAll(var3);
      boolean var7 = this.readRecords(var1);
      var1.close();
      if (!var7) {
         throw new IOException("Unhandled exception while reading records");
      }
   }

   public int addObject(int var1, Object var2) {
      byte var3 = 0;

      for(int var4 = var3; var4 < this.numObjects; ++var4) {
         GdiObject var5 = (GdiObject)this.objectVector.get(var4);
         if (!var5.used) {
            var5.Setup(var1, var2);
            this.lastObjectIdx = var4;
            break;
         }
      }

      return this.lastObjectIdx;
   }

   public int addObjectAt(int var1, Object var2, int var3) {
      if (var3 != 0 && var3 <= this.numObjects) {
         this.lastObjectIdx = var3;

         for(int var4 = 0; var4 < this.numObjects; ++var4) {
            GdiObject var5 = (GdiObject)this.objectVector.get(var4);
            if (var4 == var3) {
               var5.Setup(var1, var2);
               break;
            }
         }

         return var3;
      } else {
         this.addObject(var1, var2);
         return this.lastObjectIdx;
      }
   }

   public GdiObject getObject(int var1) {
      return (GdiObject)this.objectVector.get(var1);
   }

   public int getNumObjects() {
      return this.numObjects;
   }
}

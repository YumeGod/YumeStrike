package org.apache.fop.render.pcl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.LookupOp;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.fop.util.bitmap.BitmapImageUtil;
import org.apache.fop.util.bitmap.DitherUtil;
import org.apache.fop.util.bitmap.MonochromeBitmapConverter;
import org.apache.xmlgraphics.image.GraphicsUtil;
import org.apache.xmlgraphics.util.UnitConv;

public class PCLGenerator {
   private static final String US_ASCII = "US-ASCII";
   private static final String ISO_8859_1 = "ISO-8859-1";
   public static final char ESC = '\u001b';
   public static final int[] PCL_RESOLUTIONS = new int[]{75, 100, 150, 200, 300, 600};
   private final DecimalFormatSymbols symbols;
   private final DecimalFormat df2;
   private final DecimalFormat df4;
   private final OutputStream out;
   private boolean currentSourceTransparency;
   private boolean currentPatternTransparency;
   private int maxBitmapResolution;
   private final boolean usePCLShades;
   private static int jaiAvailable = -1;
   private static final byte[] THRESHOLD_TABLE = new byte[256];

   public PCLGenerator(OutputStream out) {
      this.symbols = new DecimalFormatSymbols(Locale.US);
      this.df2 = new DecimalFormat("0.##", this.symbols);
      this.df4 = new DecimalFormat("0.####", this.symbols);
      this.currentSourceTransparency = true;
      this.currentPatternTransparency = true;
      this.maxBitmapResolution = PCL_RESOLUTIONS[PCL_RESOLUTIONS.length - 1];
      this.usePCLShades = false;
      this.out = out;
   }

   public PCLGenerator(OutputStream out, int maxResolution) {
      this(out);
      boolean found = false;

      for(int i = 0; i < PCL_RESOLUTIONS.length; ++i) {
         if (PCL_RESOLUTIONS[i] == maxResolution) {
            found = true;
            break;
         }
      }

      if (!found) {
         throw new IllegalArgumentException("Illegal value for maximum resolution!");
      } else {
         this.maxBitmapResolution = maxResolution;
      }
   }

   public OutputStream getOutputStream() {
      return this.out;
   }

   public String getTextEncoding() {
      return "ISO-8859-1";
   }

   public int getMaximumBitmapResolution() {
      return this.maxBitmapResolution;
   }

   public void writeCommand(String cmd) throws IOException {
      this.out.write(27);
      this.out.write(cmd.getBytes("US-ASCII"));
   }

   public void writeText(String s) throws IOException {
      this.out.write(s.getBytes("ISO-8859-1"));
   }

   public final String formatDouble2(double value) {
      return this.df2.format(value);
   }

   public final String formatDouble4(double value) {
      return this.df4.format(value);
   }

   public void universalEndOfLanguage() throws IOException {
      this.writeCommand("%-12345X");
   }

   public void resetPrinter() throws IOException {
      this.writeCommand("E");
   }

   public void separateJobs() throws IOException {
      this.writeCommand("&l1T");
   }

   public void formFeed() throws IOException {
      this.out.write(12);
   }

   public void setUnitOfMeasure(int value) throws IOException {
      this.writeCommand("&u" + value + "D");
   }

   public void setRasterGraphicsResolution(int value) throws IOException {
      this.writeCommand("*t" + value + "R");
   }

   public void selectPageSize(int selector) throws IOException {
      this.writeCommand("&l" + selector + "A");
   }

   public void selectPaperSource(int selector) throws IOException {
      this.writeCommand("&l" + selector + "H");
   }

   public void selectOutputBin(int selector) throws IOException {
      this.writeCommand("&l" + selector + "G");
   }

   public void selectDuplexMode(int selector) throws IOException {
      this.writeCommand("&l" + selector + "S");
   }

   public void clearHorizontalMargins() throws IOException {
      this.writeCommand("9");
   }

   public void setTopMargin(int numberOfLines) throws IOException {
      this.writeCommand("&l" + numberOfLines + "E");
   }

   public void setTextLength(int numberOfLines) throws IOException {
      this.writeCommand("&l" + numberOfLines + "F");
   }

   public void setVMI(double value) throws IOException {
      this.writeCommand("&l" + this.formatDouble4(value) + "C");
   }

   public void setCursorPos(double x, double y) throws IOException {
      if (x < 0.0) {
         this.writeCommand("&a0h" + this.formatDouble2(x / 100.0) + "h" + this.formatDouble2(y / 100.0) + "V");
      } else {
         this.writeCommand("&a" + this.formatDouble2(x / 100.0) + "h" + this.formatDouble2(y / 100.0) + "V");
      }

   }

   public void pushCursorPos() throws IOException {
      this.writeCommand("&f0S");
   }

   public void popCursorPos() throws IOException {
      this.writeCommand("&f1S");
   }

   public void changePrintDirection(int rotate) throws IOException {
      this.writeCommand("&a" + rotate + "P");
   }

   public void enterHPGL2Mode(boolean restorePreviousHPGL2Cursor) throws IOException {
      if (restorePreviousHPGL2Cursor) {
         this.writeCommand("%0B");
      } else {
         this.writeCommand("%1B");
      }

   }

   public void enterPCLMode(boolean restorePreviousPCLCursor) throws IOException {
      if (restorePreviousPCLCursor) {
         this.writeCommand("%0A");
      } else {
         this.writeCommand("%1A");
      }

   }

   protected void fillRect(int w, int h, Color col) throws IOException {
      if (w != 0 && h != 0) {
         if (h < 0) {
            h *= -1;
         }

         this.setPatternTransparencyMode(false);
         if (!Color.black.equals(col) && !Color.white.equals(col)) {
            this.defineGrayscalePattern(col, 32, 4);
            this.writeCommand("*c" + this.formatDouble4((double)w / 100.0) + "h" + this.formatDouble4((double)h / 100.0) + "V");
            this.writeCommand("*c32G");
            this.writeCommand("*c4P");
         } else {
            this.writeCommand("*c" + this.formatDouble4((double)w / 100.0) + "h" + this.formatDouble4((double)h / 100.0) + "V");
            int lineshade = this.convertToPCLShade(col);
            this.writeCommand("*c" + lineshade + "G");
            this.writeCommand("*c2P");
         }

         this.setPatternTransparencyMode(true);
      }
   }

   public void defineGrayscalePattern(Color col, int patternID, int ditherMatrixSize) throws IOException {
      ByteArrayOutputStream baout = new ByteArrayOutputStream();
      DataOutputStream data = new DataOutputStream(baout);
      data.writeByte(0);
      data.writeByte(0);
      data.writeByte(1);
      data.writeByte(0);
      data.writeShort(8);
      data.writeShort(8);
      int gray255 = this.convertToGray(col.getRed(), col.getGreen(), col.getBlue());
      byte[] pattern;
      if (ditherMatrixSize == 8) {
         pattern = DitherUtil.getBayerDither(8, gray255, false);
      } else {
         pattern = DitherUtil.getBayerDither(4, gray255, true);
      }

      data.write(pattern);
      if (baout.size() % 2 > 0) {
         baout.write(0);
      }

      this.writeCommand("*c" + patternID + "G");
      this.writeCommand("*c" + baout.size() + "W");
      baout.writeTo(this.out);
      this.writeCommand("*c4Q");
   }

   public void setSourceTransparencyMode(boolean transparent) throws IOException {
      this.setTransparencyMode(transparent, this.currentPatternTransparency);
   }

   public void setPatternTransparencyMode(boolean transparent) throws IOException {
      this.setTransparencyMode(this.currentSourceTransparency, transparent);
   }

   public void setTransparencyMode(boolean source, boolean pattern) throws IOException {
      if (source != this.currentSourceTransparency && pattern != this.currentPatternTransparency) {
         this.writeCommand("*v" + (source ? '0' : '1') + "n" + (pattern ? '0' : '1') + "O");
      } else if (source != this.currentSourceTransparency) {
         this.writeCommand("*v" + (source ? '0' : '1') + "N");
      } else if (pattern != this.currentPatternTransparency) {
         this.writeCommand("*v" + (pattern ? '0' : '1') + "O");
      }

      this.currentSourceTransparency = source;
      this.currentPatternTransparency = pattern;
   }

   public final int convertToGray(int r, int g, int b) {
      return BitmapImageUtil.convertToGray(r, g, b);
   }

   public final int convertToPCLShade(Color col) {
      float gray = (float)this.convertToGray(col.getRed(), col.getGreen(), col.getBlue()) / 255.0F;
      return (int)(100.0F - gray * 100.0F);
   }

   public void selectGrayscale(Color col) throws IOException {
      if (Color.black.equals(col)) {
         this.selectCurrentPattern(0, 0);
      } else if (Color.white.equals(col)) {
         this.selectCurrentPattern(0, 1);
      } else {
         this.defineGrayscalePattern(col, 32, 4);
         this.selectCurrentPattern(32, 4);
      }

   }

   public void selectCurrentPattern(int patternID, int pattern) throws IOException {
      if (pattern > 1) {
         this.writeCommand("*c" + patternID + "G");
      }

      this.writeCommand("*v" + pattern + "T");
   }

   public static boolean isMonochromeImage(RenderedImage img) {
      return BitmapImageUtil.isMonochromeImage(img);
   }

   public static boolean isGrayscaleImage(RenderedImage img) {
      return BitmapImageUtil.isGrayscaleImage(img);
   }

   public static boolean isJAIAvailable() {
      if (jaiAvailable < 0) {
         try {
            String clName = "javax.media.jai.JAI";
            Class.forName(clName);
            jaiAvailable = 1;
         } catch (ClassNotFoundException var1) {
            jaiAvailable = 0;
         }
      }

      return jaiAvailable > 0;
   }

   private int calculatePCLResolution(int resolution) {
      return this.calculatePCLResolution(resolution, false);
   }

   private int calculatePCLResolution(int resolution, boolean increased) {
      int choice = -1;

      for(int i = PCL_RESOLUTIONS.length - 2; i >= 0; --i) {
         if (resolution > PCL_RESOLUTIONS[i]) {
            int idx = i + 1;
            if (idx < PCL_RESOLUTIONS.length - 2) {
               idx += increased ? 2 : 0;
            } else if (idx < PCL_RESOLUTIONS.length - 1) {
               idx += increased ? 1 : 0;
            }

            choice = idx;
            break;
         }
      }

      if (choice < 0) {
         choice = increased ? 2 : 0;
      }

      while(choice > 0 && PCL_RESOLUTIONS[choice] > this.getMaximumBitmapResolution()) {
         --choice;
      }

      return PCL_RESOLUTIONS[choice];
   }

   private boolean isValidPCLResolution(int resolution) {
      return resolution == this.calculatePCLResolution(resolution);
   }

   private Dimension getAdjustedDimension(Dimension orgDim, double orgResolution, int pclResolution) {
      if (orgResolution == (double)pclResolution) {
         return orgDim;
      } else {
         Dimension result = new Dimension();
         result.width = (int)Math.round((double)orgDim.width * (double)pclResolution / orgResolution);
         result.height = (int)Math.round((double)orgDim.height * (double)pclResolution / orgResolution);
         return result;
      }
   }

   private RenderedImage getMask(RenderedImage img, Dimension targetDim) {
      ColorModel cm = img.getColorModel();
      if (cm.hasAlpha()) {
         BufferedImage alpha = new BufferedImage(img.getWidth(), img.getHeight(), 10);
         Raster raster = img.getData();
         GraphicsUtil.copyBand(raster, cm.getNumColorComponents(), alpha.getRaster(), 0);
         BufferedImageOp op1 = new LookupOp(new ByteLookupTable(0, THRESHOLD_TABLE), (RenderingHints)null);
         BufferedImage alphat = op1.filter(alpha, (BufferedImage)null);
         BufferedImage mask = new BufferedImage(targetDim.width, targetDim.height, 12);
         Graphics2D g2d = mask.createGraphics();

         try {
            AffineTransform at = new AffineTransform();
            double sx = targetDim.getWidth() / (double)img.getWidth();
            double sy = targetDim.getHeight() / (double)img.getHeight();
            at.scale(sx, sy);
            g2d.drawRenderedImage(alphat, at);
         } finally {
            g2d.dispose();
         }

         return mask;
      } else {
         return null;
      }
   }

   public void paintBitmap(RenderedImage img, Dimension targetDim, boolean sourceTransparency) throws IOException {
      double targetHResolution = (double)img.getWidth() / UnitConv.mpt2in((double)targetDim.width);
      double targetVResolution = (double)img.getHeight() / UnitConv.mpt2in((double)targetDim.height);
      double targetResolution = Math.max(targetHResolution, targetVResolution);
      int resolution = (int)Math.round(targetResolution);
      int effResolution = this.calculatePCLResolution(resolution, true);
      Dimension orgDim = new Dimension(img.getWidth(), img.getHeight());
      Dimension effDim;
      if (targetResolution == (double)effResolution) {
         effDim = orgDim;
      } else {
         effDim = new Dimension((int)Math.ceil(UnitConv.mpt2px((double)targetDim.width, effResolution)), (int)Math.ceil(UnitConv.mpt2px((double)targetDim.height, effResolution)));
      }

      boolean scaled = !orgDim.equals(effDim);
      boolean monochrome = isMonochromeImage(img);
      if (!monochrome) {
         boolean transparencyDisabled = true;
         RenderedImage mask = (RenderedImage)null;
         if (mask != null) {
            this.pushCursorPos();
            this.selectCurrentPattern(0, 1);
            this.setTransparencyMode(true, true);
            this.paintMonochromeBitmap(mask, effResolution);
            this.popCursorPos();
         }

         BufferedImage src = null;
         if (img instanceof BufferedImage && !scaled && isGrayscaleImage(img) && !img.getColorModel().hasAlpha()) {
            src = (BufferedImage)img;
         }

         if (src == null) {
            src = BitmapImageUtil.convertToGrayscale(img, effDim);
         }

         MonochromeBitmapConverter converter = BitmapImageUtil.createDefaultMonochromeBitmapConverter();
         converter.setHint("quality", "false");
         RenderedImage red = converter.convertToMonochrome(src);
         this.selectCurrentPattern(0, 0);
         this.setTransparencyMode(sourceTransparency || mask != null, true);
         this.paintMonochromeBitmap(red, effResolution);
      } else {
         RenderedImage effImg = img;
         if (scaled) {
            effImg = BitmapImageUtil.convertToMonochrome(img, effDim);
         }

         this.setSourceTransparencyMode(sourceTransparency);
         this.selectCurrentPattern(0, 0);
         this.paintMonochromeBitmap((RenderedImage)effImg, effResolution);
      }

   }

   private void clearBackground(Graphics2D g2d, Dimension effDim) {
      g2d.setBackground(Color.WHITE);
      g2d.clearRect(0, 0, effDim.width, effDim.height);
   }

   private int toGray(int rgb) {
      double greyVal = 0.072169 * (double)(rgb & 255);
      rgb >>= 8;
      greyVal += 0.71516 * (double)(rgb & 255);
      rgb >>= 8;
      greyVal += 0.212671 * (double)(rgb & 255);
      return (int)greyVal;
   }

   public void paintMonochromeBitmap(RenderedImage img, int resolution) throws IOException {
      if (!this.isValidPCLResolution(resolution)) {
         throw new IllegalArgumentException("Invalid PCL resolution: " + resolution);
      } else {
         boolean monochrome = isMonochromeImage(img);
         if (!monochrome) {
            throw new IllegalArgumentException("img must be a monochrome image");
         } else {
            this.setRasterGraphicsResolution(resolution);
            this.writeCommand("*r0f" + img.getHeight() + "t" + img.getWidth() + "s1A");
            Raster raster = img.getData();
            Encoder encoder = new Encoder(img);
            int imgw = img.getWidth();
            IndexColorModel cm = (IndexColorModel)img.getColorModel();
            int x;
            int scanlineStride;
            int idx;
            if (cm.getTransferType() == 0) {
               DataBufferByte dataBuffer = (DataBufferByte)raster.getDataBuffer();
               MultiPixelPackedSampleModel packedSampleModel = new MultiPixelPackedSampleModel(0, img.getWidth(), img.getHeight(), 1);
               int x;
               int maxx;
               if (img.getSampleModel().equals(packedSampleModel) && dataBuffer.getNumBanks() == 1) {
                  byte[] buf = dataBuffer.getData();
                  scanlineStride = packedSampleModel.getScanlineStride();
                  idx = 0;
                  x = this.toGray(cm.getRGB(0));
                  maxx = this.toGray(cm.getRGB(1));
                  boolean zeroIsWhite = x > maxx;
                  int y = 0;

                  for(int maxy = img.getHeight(); y < maxy; ++y) {
                     int x = 0;

                     for(int maxx = scanlineStride; x < maxx; ++x) {
                        if (zeroIsWhite) {
                           encoder.add8Bits(buf[idx]);
                        } else {
                           encoder.add8Bits((byte)(~buf[idx]));
                        }

                        ++idx;
                     }

                     encoder.endLine();
                  }
               } else {
                  x = 0;

                  for(scanlineStride = img.getHeight(); x < scanlineStride; ++x) {
                     byte[] line = (byte[])raster.getDataElements(0, x, imgw, 1, (Object)null);
                     x = 0;

                     for(maxx = imgw; x < maxx; ++x) {
                        encoder.addBit(line[x] == 0);
                     }

                     encoder.endLine();
                  }
               }
            } else {
               int y = 0;

               for(int maxy = img.getHeight(); y < maxy; ++y) {
                  x = 0;

                  for(scanlineStride = imgw; x < scanlineStride; ++x) {
                     idx = raster.getSample(x, y, 0);
                     encoder.addBit(idx == 0);
                  }

                  encoder.endLine();
               }
            }

            this.writeCommand("*rB");
         }
      }
   }

   static {
      for(int i = 0; i < 256; ++i) {
         THRESHOLD_TABLE[i] = (byte)(i < 240 ? 255 : 0);
      }

   }

   private class Encoder {
      private int imgw;
      private int bytewidth;
      private byte[] rle;
      private byte[] uncompressed;
      private int lastcount = -1;
      private byte lastbyte = 0;
      private int rlewidth = 0;
      private byte ib = 0;
      private int x = 0;
      private boolean zeroRow = true;

      public Encoder(RenderedImage img) {
         this.imgw = img.getWidth();
         this.bytewidth = this.imgw / 8;
         if (this.imgw % 8 != 0) {
            ++this.bytewidth;
         }

         this.rle = new byte[this.bytewidth * 2];
         this.uncompressed = new byte[this.bytewidth];
      }

      public void addBit(boolean bit) {
         if (bit) {
            this.ib = (byte)(this.ib | 1);
         }

         if (this.x % 8 != 7 && this.x + 1 != this.imgw) {
            this.ib = (byte)(this.ib << 1);
         } else {
            this.finishedByte();
         }

         ++this.x;
      }

      public void add8Bits(byte b) {
         this.ib = b;
         this.finishedByte();
         this.x += 8;
      }

      private void finishedByte() {
         if (this.rlewidth < this.bytewidth) {
            if (this.lastcount >= 0) {
               if (this.ib == this.lastbyte) {
                  ++this.lastcount;
               } else {
                  this.rle[this.rlewidth++] = (byte)(this.lastcount & 255);
                  this.rle[this.rlewidth++] = this.lastbyte;
                  this.lastbyte = this.ib;
                  this.lastcount = 0;
               }
            } else {
               this.lastbyte = this.ib;
               this.lastcount = 0;
            }

            if (this.lastcount == 255 || this.x + 1 == this.imgw) {
               this.rle[this.rlewidth++] = (byte)(this.lastcount & 255);
               this.rle[this.rlewidth++] = this.lastbyte;
               this.lastbyte = 0;
               this.lastcount = -1;
            }
         }

         this.uncompressed[this.x / 8] = this.ib;
         if (this.ib != 0) {
            this.zeroRow = false;
         }

         this.ib = 0;
      }

      public void endLine() throws IOException {
         if (this.zeroRow && PCLGenerator.this.currentSourceTransparency) {
            PCLGenerator.this.writeCommand("*b1Y");
         } else if (this.rlewidth < this.bytewidth) {
            PCLGenerator.this.writeCommand("*b1m" + this.rlewidth + "W");
            PCLGenerator.this.out.write(this.rle, 0, this.rlewidth);
         } else {
            PCLGenerator.this.writeCommand("*b0m" + this.bytewidth + "W");
            PCLGenerator.this.out.write(this.uncompressed);
         }

         this.lastcount = -1;
         this.rlewidth = 0;
         this.ib = 0;
         this.x = 0;
         this.zeroRow = true;
      }
   }
}

package org.apache.xmlgraphics.ps;

import java.awt.Dimension;
import java.awt.color.ColorSpace;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.xmlgraphics.util.io.ASCII85OutputStream;
import org.apache.xmlgraphics.util.io.Finalizable;
import org.apache.xmlgraphics.util.io.FlateEncodeOutputStream;
import org.apache.xmlgraphics.util.io.RunLengthEncodeOutputStream;

public class PSImageUtils {
   private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

   /** @deprecated */
   public static void writeImage(final byte[] img, Dimension imgDim, String imgDescription, Rectangle2D targetRect, final boolean isJPEG, ColorSpace colorSpace, PSGenerator gen) throws IOException {
      ImageEncoder encoder = new ImageEncoder() {
         public void writeTo(OutputStream out) throws IOException {
            out.write(img);
         }

         public String getImplicitFilter() {
            return isJPEG ? "<< >> /DCTDecode" : null;
         }
      };
      writeImage(encoder, imgDim, imgDescription, targetRect, colorSpace, 8, false, gen);
   }

   public static void writeImage(ImageEncoder encoder, Dimension imgDim, String imgDescription, Rectangle2D targetRect, ColorSpace colorSpace, int bitsPerComponent, boolean invertImage, PSGenerator gen) throws IOException {
      gen.saveGraphicsState();
      translateAndScale(gen, (Dimension2D)null, targetRect);
      gen.commentln("%AXGBeginBitmap: " + imgDescription);
      gen.writeln("{{");
      String implicitFilter = encoder.getImplicitFilter();
      if (implicitFilter != null) {
         gen.writeln("/RawData currentfile /ASCII85Decode filter def");
         gen.writeln("/Data RawData " + implicitFilter + " filter def");
      } else if (gen.getPSLevel() >= 3) {
         gen.writeln("/RawData currentfile /ASCII85Decode filter def");
         gen.writeln("/Data RawData /FlateDecode filter def");
      } else {
         gen.writeln("/RawData currentfile /ASCII85Decode filter def");
         gen.writeln("/Data RawData /RunLengthDecode filter def");
      }

      PSDictionary imageDict = new PSDictionary();
      imageDict.put("/DataSource", "Data");
      imageDict.put("/BitsPerComponent", Integer.toString(bitsPerComponent));
      writeImageCommand(imageDict, imgDim, colorSpace, invertImage, gen);
      gen.writeln("} stopped {handleerror} if");
      gen.writeln("  RawData flushfile");
      gen.writeln("} exec");
      compressAndWriteBitmap(encoder, gen);
      gen.newLine();
      gen.commentln("%AXGEndBitmap");
      gen.restoreGraphicsState();
   }

   private static void writeImage(RenderedImage img, Rectangle2D targetRect, PSGenerator gen) throws IOException {
      ImageEncoder encoder = ImageEncodingHelper.createRenderedImageEncoder(img);
      String imgDescription = img.getClass().getName();
      gen.saveGraphicsState();
      translateAndScale(gen, (Dimension2D)null, targetRect);
      gen.commentln("%AXGBeginBitmap: " + imgDescription);
      gen.writeln("{{");
      String implicitFilter = encoder.getImplicitFilter();
      if (implicitFilter != null) {
         gen.writeln("/RawData currentfile /ASCII85Decode filter def");
         gen.writeln("/Data RawData " + implicitFilter + " filter def");
      } else if (gen.getPSLevel() >= 3) {
         gen.writeln("/RawData currentfile /ASCII85Decode filter def");
         gen.writeln("/Data RawData /FlateDecode filter def");
      } else {
         gen.writeln("/RawData currentfile /ASCII85Decode filter def");
         gen.writeln("/Data RawData /RunLengthDecode filter def");
      }

      PSDictionary imageDict = new PSDictionary();
      imageDict.put("/DataSource", "Data");
      writeImageCommand(img, imageDict, gen);
      gen.writeln("} stopped {handleerror} if");
      gen.writeln("  RawData flushfile");
      gen.writeln("} exec");
      compressAndWriteBitmap(encoder, gen);
      gen.writeln("");
      gen.commentln("%AXGEndBitmap");
      gen.restoreGraphicsState();
   }

   private static ColorModel populateImageDictionary(ImageEncodingHelper helper, PSDictionary imageDict) {
      RenderedImage img = helper.getImage();
      String w = Integer.toString(img.getWidth());
      String h = Integer.toString(img.getHeight());
      imageDict.put("/ImageType", "1");
      imageDict.put("/Width", w);
      imageDict.put("/Height", h);
      ColorModel cm = helper.getEncodedColorModel();
      boolean invertColors = false;
      String decodeArray = getDecodeArray(cm.getNumComponents(), invertColors);
      int bitsPerComp = cm.getComponentSize(0);
      imageDict.put("/ImageMatrix", "[" + w + " 0 0 " + h + " 0 0]");
      if (cm instanceof IndexColorModel) {
         IndexColorModel im = (IndexColorModel)cm;
         int c = im.getMapSize();
         int hival = c - 1;
         if (hival > 4095) {
            throw new UnsupportedOperationException("hival must not go beyond 4095");
         }

         bitsPerComp = im.getPixelSize();
         int ceiling = (int)Math.pow(2.0, (double)bitsPerComp) - 1;
         decodeArray = "[0 " + ceiling + "]";
      }

      imageDict.put("/BitsPerComponent", Integer.toString(bitsPerComp));
      imageDict.put("/Decode", decodeArray);
      return cm;
   }

   private static String getDecodeArray(int numComponents, boolean invertColors) {
      StringBuffer sb = new StringBuffer("[");

      for(int i = 0; i < numComponents; ++i) {
         if (i > 0) {
            sb.append(" ");
         }

         if (invertColors) {
            sb.append("1 0");
         } else {
            sb.append("0 1");
         }
      }

      sb.append("]");
      String decodeArray = sb.toString();
      return decodeArray;
   }

   private static void prepareColorspace(PSGenerator gen, ColorSpace colorSpace) throws IOException {
      gen.writeln(getColorSpaceName(colorSpace) + " setcolorspace");
   }

   private static void prepareColorSpace(PSGenerator gen, ColorModel cm) throws IOException {
      if (cm instanceof IndexColorModel) {
         ColorSpace cs = cm.getColorSpace();
         IndexColorModel im = (IndexColorModel)cm;
         gen.write("[/Indexed " + getColorSpaceName(cs));
         int c = im.getMapSize();
         int hival = c - 1;
         if (hival > 4095) {
            throw new UnsupportedOperationException("hival must not go beyond 4095");
         }

         gen.writeln(" " + Integer.toString(hival));
         gen.write("  <");
         int[] palette = new int[c];
         im.getRGBs(palette);

         for(int i = 0; i < c; ++i) {
            if (i > 0) {
               if (i % 8 == 0) {
                  gen.newLine();
                  gen.write("   ");
               } else {
                  gen.write(" ");
               }
            }

            gen.write(rgb2Hex(palette[i]));
         }

         gen.writeln(">");
         gen.writeln("] setcolorspace");
      } else {
         gen.writeln(getColorSpaceName(cm.getColorSpace()) + " setcolorspace");
      }

   }

   static void writeImageCommand(RenderedImage img, PSDictionary imageDict, PSGenerator gen) throws IOException {
      ImageEncodingHelper helper = new ImageEncodingHelper(img, true);
      ColorModel cm = helper.getEncodedColorModel();
      populateImageDictionary(helper, imageDict);
      writeImageCommand(imageDict, cm, gen);
   }

   static void writeImageCommand(PSDictionary imageDict, ColorModel cm, PSGenerator gen) throws IOException {
      prepareColorSpace(gen, cm);
      gen.write(imageDict.toString());
      gen.writeln(" image");
   }

   static void writeImageCommand(PSDictionary imageDict, Dimension imgDim, ColorSpace colorSpace, boolean invertImage, PSGenerator gen) throws IOException {
      imageDict.put("/ImageType", "1");
      imageDict.put("/Width", Integer.toString(imgDim.width));
      imageDict.put("/Height", Integer.toString(imgDim.height));
      String decodeArray = getDecodeArray(colorSpace.getNumComponents(), invertImage);
      imageDict.put("/Decode", decodeArray);
      imageDict.put("/ImageMatrix", "[" + imgDim.width + " 0 0 " + imgDim.height + " 0 0]");
      prepareColorspace(gen, colorSpace);
      gen.write(imageDict.toString());
      gen.writeln(" image");
   }

   private static String rgb2Hex(int rgb) {
      StringBuffer sb = new StringBuffer();

      for(int i = 5; i >= 0; --i) {
         int shift = i * 4;
         int n = (rgb & 15 << shift) >> shift;
         sb.append(HEX[n % 16]);
      }

      return sb.toString();
   }

   public static void renderBitmapImage(RenderedImage img, float x, float y, float w, float h, PSGenerator gen) throws IOException {
      Rectangle2D targetRect = new Rectangle2D.Double((double)x, (double)y, (double)w, (double)h);
      writeImage(img, targetRect, gen);
   }

   /** @deprecated */
   public static PSResource writeReusableImage(final byte[] img, Dimension imgDim, String formName, String imageDescription, final boolean isJPEG, ColorSpace colorSpace, PSGenerator gen) throws IOException {
      ImageEncoder encoder = new ImageEncoder() {
         public void writeTo(OutputStream out) throws IOException {
            out.write(img);
         }

         public String getImplicitFilter() {
            return isJPEG ? "<< >> /DCTDecode" : null;
         }
      };
      return writeReusableImage(encoder, imgDim, formName, imageDescription, colorSpace, false, gen);
   }

   /** @deprecated */
   protected static PSResource writeReusableImage(ImageEncoder encoder, Dimension imgDim, String formName, String imageDescription, ColorSpace colorSpace, boolean invertImage, PSGenerator gen) throws IOException {
      if (gen.getPSLevel() < 2) {
         throw new UnsupportedOperationException("Reusable images requires at least Level 2 PostScript");
      } else {
         String dataName = formName + ":Data";
         gen.writeDSCComment("BeginResource", (Object)formName);
         if (imageDescription != null) {
            gen.writeDSCComment("Title", (Object)imageDescription);
         }

         String implicitFilter = encoder.getImplicitFilter();
         String additionalFilters;
         if (implicitFilter != null) {
            additionalFilters = "/ASCII85Decode filter " + implicitFilter + " filter";
         } else if (gen.getPSLevel() >= 3) {
            additionalFilters = "/ASCII85Decode filter /FlateDecode filter";
         } else {
            additionalFilters = "/ASCII85Decode filter /RunLengthDecode filter";
         }

         gen.writeln("/" + formName);
         gen.writeln("<< /FormType 1");
         gen.writeln("  /BBox [0 0 " + imgDim.width + " " + imgDim.height + "]");
         gen.writeln("  /Matrix [1 0 0 1 0 0]");
         gen.writeln("  /PaintProc {");
         gen.writeln("    pop");
         gen.writeln("    gsave");
         if (gen.getPSLevel() == 2) {
            gen.writeln("    userdict /i 0 put");
         } else {
            gen.writeln("    " + dataName + " 0 setfileposition");
         }

         String dataSource;
         if (gen.getPSLevel() == 2) {
            dataSource = "{ " + dataName + " i get /i i 1 add store } bind";
         } else {
            dataSource = dataName;
         }

         PSDictionary imageDict = new PSDictionary();
         imageDict.put("/DataSource", dataSource);
         imageDict.put("/BitsPerComponent", Integer.toString(8));
         writeImageCommand(imageDict, imgDim, colorSpace, invertImage, gen);
         gen.writeln("    grestore");
         gen.writeln("  } bind");
         gen.writeln(">> def");
         gen.writeln("/" + dataName + " currentfile");
         gen.writeln(additionalFilters);
         if (gen.getPSLevel() == 2) {
            gen.writeln("{ /temp exch def [ { temp 16384 string readstring not {exit } if } loop ] } exec");
         } else {
            gen.writeln("/ReusableStreamDecode filter");
         }

         compressAndWriteBitmap(encoder, gen);
         gen.writeln("def");
         gen.writeDSCComment("EndResource");
         PSResource res = new PSResource("form", formName);
         gen.getResourceTracker().registerSuppliedResource(res);
         return res;
      }
   }

   /** @deprecated */
   public static void paintReusableImage(String formName, Rectangle2D targetRect, PSGenerator gen) throws IOException {
      PSResource form = new PSResource("form", formName);
      paintForm(form, (Dimension2D)null, targetRect, gen);
   }

   /** @deprecated */
   public static void paintForm(PSResource form, Rectangle2D targetRect, PSGenerator gen) throws IOException {
      paintForm(form, (Dimension2D)null, targetRect, gen);
   }

   public static void paintForm(PSResource form, Dimension2D formDimensions, Rectangle2D targetRect, PSGenerator gen) throws IOException {
      gen.saveGraphicsState();
      translateAndScale(gen, formDimensions, targetRect);
      gen.writeln(form.getName() + " execform");
      gen.getResourceTracker().notifyResourceUsageOnPage(form);
      gen.restoreGraphicsState();
   }

   private static String getColorSpaceName(ColorSpace colorSpace) {
      if (colorSpace.getType() == 9) {
         return "/DeviceCMYK";
      } else {
         return colorSpace.getType() == 6 ? "/DeviceGray" : "/DeviceRGB";
      }
   }

   static void compressAndWriteBitmap(ImageEncoder encoder, PSGenerator gen) throws IOException {
      OutputStream out = gen.getOutputStream();
      OutputStream out = new ASCII85OutputStream(out);
      String implicitFilter = encoder.getImplicitFilter();
      if (implicitFilter == null) {
         if (gen.getPSLevel() >= 3) {
            out = new FlateEncodeOutputStream((OutputStream)out);
         } else {
            out = new RunLengthEncodeOutputStream((OutputStream)out);
         }
      }

      encoder.writeTo((OutputStream)out);
      if (out instanceof Finalizable) {
         ((Finalizable)out).finalizeStream();
      } else {
         ((OutputStream)out).flush();
      }

      gen.newLine();
   }

   public static void translateAndScale(PSGenerator gen, Dimension2D imageDimensions, Rectangle2D targetRect) throws IOException {
      gen.writeln(gen.formatDouble(targetRect.getX()) + " " + gen.formatDouble(targetRect.getY()) + " translate");
      if (imageDimensions == null) {
         imageDimensions = new Dimension(1, 1);
      }

      double sx = targetRect.getWidth() / ((Dimension2D)imageDimensions).getWidth();
      double sy = targetRect.getHeight() / ((Dimension2D)imageDimensions).getHeight();
      if (sx != 1.0 || sy != 1.0) {
         gen.writeln(gen.formatDouble(sx) + " " + gen.formatDouble(sy) + " scale");
      }

   }

   public static int[] getRGB(RenderedImage img, int startX, int startY, int w, int h, int[] rgbArray, int offset, int scansize) {
      Raster raster = img.getData();
      int yoff = offset;
      int nbands = raster.getNumBands();
      int dataType = raster.getDataBuffer().getDataType();
      Object var11;
      switch (dataType) {
         case 0:
            var11 = new byte[nbands];
            break;
         case 1:
            var11 = new short[nbands];
            break;
         case 2:
         default:
            throw new IllegalArgumentException("Unknown data buffer type: " + dataType);
         case 3:
            var11 = new int[nbands];
            break;
         case 4:
            var11 = new float[nbands];
            break;
         case 5:
            var11 = new double[nbands];
      }

      if (rgbArray == null) {
         rgbArray = new int[offset + h * scansize];
      }

      ColorModel colorModel = img.getColorModel();

      for(int y = startY; y < startY + h; yoff += scansize) {
         int off = yoff;

         for(int x = startX; x < startX + w; ++x) {
            rgbArray[off++] = colorModel.getRGB(raster.getDataElements(x, y, var11));
         }

         ++y;
      }

      return rgbArray;
   }

   /** @deprecated */
   public static void renderEPS(byte[] rawEPS, String name, float x, float y, float w, float h, float bboxx, float bboxy, float bboxw, float bboxh, PSGenerator gen) throws IOException {
      renderEPS(new ByteArrayInputStream(rawEPS), name, new Rectangle2D.Float(x, y, w, h), new Rectangle2D.Float(bboxx, bboxy, bboxw, bboxh), gen);
   }

   public static void renderEPS(InputStream in, String name, Rectangle2D viewport, Rectangle2D bbox, PSGenerator gen) throws IOException {
      gen.getResourceTracker().notifyResourceUsageOnPage(PSProcSets.EPS_PROCSET);
      gen.writeln("%AXGBeginEPS: " + name);
      gen.writeln("BeginEPSF");
      gen.writeln(gen.formatDouble(viewport.getX()) + " " + gen.formatDouble(viewport.getY()) + " translate");
      gen.writeln("0 " + gen.formatDouble(viewport.getHeight()) + " translate");
      gen.writeln("1 -1 scale");
      double sx = viewport.getWidth() / bbox.getWidth();
      double sy = viewport.getHeight() / bbox.getHeight();
      if (sx != 1.0 || sy != 1.0) {
         gen.writeln(gen.formatDouble(sx) + " " + gen.formatDouble(sy) + " scale");
      }

      if (bbox.getX() != 0.0 || bbox.getY() != 0.0) {
         gen.writeln(gen.formatDouble(-bbox.getX()) + " " + gen.formatDouble(-bbox.getY()) + " translate");
      }

      gen.writeln(gen.formatDouble(bbox.getX()) + " " + gen.formatDouble(bbox.getY()) + " " + gen.formatDouble(bbox.getWidth()) + " " + gen.formatDouble(bbox.getHeight()) + " re clip");
      gen.writeln("newpath");
      PSResource res = new PSResource("file", name);
      gen.getResourceTracker().registerSuppliedResource(res);
      gen.getResourceTracker().notifyResourceUsageOnPage(res);
      gen.writeDSCComment("BeginDocument", (Object)res.getName());
      IOUtils.copy(in, gen.getOutputStream());
      gen.newLine();
      gen.writeDSCComment("EndDocument");
      gen.writeln("EndEPSF");
      gen.writeln("%AXGEndEPS");
   }
}

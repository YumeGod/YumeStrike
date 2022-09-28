package com.xmlmind.fo.graphic;

import com.xmlmind.fo.util.URLUtil;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class GraphicFactoryImpl implements GraphicFactory {
   private String[] inputFormats;
   private String[] outputFormats;

   public GraphicFactoryImpl() {
      IIORegistry var1 = IIORegistry.getDefaultInstance();
      HashMap var2 = new HashMap();
      Iterator var3 = null;

      try {
         var3 = var1.getServiceProviders(ImageReaderSpi.class, true);
      } catch (IllegalArgumentException var9) {
      }

      String[] var5;
      int var6;
      String var7;
      if (var3 != null) {
         label88:
         while(true) {
            do {
               if (!var3.hasNext()) {
                  break label88;
               }

               ImageReaderSpi var4 = (ImageReaderSpi)var3.next();
               var5 = var4.getMIMETypes();
            } while(var5 == null);

            for(var6 = 0; var6 < var5.length; ++var6) {
               var7 = URLUtil.normalizeMIMEType(var5[var6]);
               if (var7 != null && var7.length() > 0) {
                  var2.put(var7, var7);
               }
            }
         }
      }

      this.inputFormats = new String[var2.size()];
      var2.values().toArray(this.inputFormats);
      var2.clear();
      var3 = null;

      try {
         var3 = var1.getServiceProviders(ImageWriterSpi.class, true);
      } catch (IllegalArgumentException var8) {
      }

      if (var3 != null) {
         label71:
         while(true) {
            do {
               if (!var3.hasNext()) {
                  break label71;
               }

               ImageWriterSpi var10 = (ImageWriterSpi)var3.next();
               var5 = var10.getMIMETypes();
            } while(var5 == null);

            for(var6 = 0; var6 < var5.length; ++var6) {
               var7 = URLUtil.normalizeMIMEType(var5[var6]);
               if ("image/png".equals(var7) || "image/jpeg".equals(var7)) {
                  var2.put(var7, var7);
               }
            }
         }
      }

      this.outputFormats = new String[var2.size()];
      var2.values().toArray(this.outputFormats);
   }

   public String[] getInputFormats() {
      return this.inputFormats;
   }

   public String[] getOutputFormats() {
      return this.outputFormats;
   }

   public Graphic createGraphic(String var1, String var2, Object var3, GraphicEnv var4) throws Exception {
      var2 = URLUtil.normalizeMIMEType(var2);
      double var7 = 0.0;
      double var9 = 0.0;
      ImageReader var11 = getImageReader(var2);

      int var5;
      int var6;
      try {
         InputStream var12 = URLUtil.openStream(var1);

         try {
            ImageInputStream var13 = ImageIO.createImageInputStream(var12);
            if (var13 == null) {
               throw new RuntimeException("cannot create an ImageInputStream out of a java.io.InputStream");
            }

            try {
               var11.setInput(var13, true, false);
               var5 = var11.getWidth(0);
               var6 = var11.getHeight(0);
               double[] var14 = getResolution(var11);
               if (var14 != null) {
                  var7 = var14[0];
                  var9 = var14[1];
               }
            } finally {
               var13.close();
            }
         } finally {
            var12.close();
         }
      } finally {
         var11.dispose();
      }

      return new GraphicImpl(var1, var2, var5, var6, var7, var9, var3);
   }

   private static ImageReader getImageReader(String var0) {
      ImageReader var1 = null;
      Iterator var2 = ImageIO.getImageReadersByMIMEType(var0);
      if (var2.hasNext()) {
         var1 = (ImageReader)var2.next();
      }

      if (var1 == null) {
         throw new RuntimeException("don't know how to read '" + var0 + "' images");
      } else {
         return var1;
      }
   }

   private static double[] getResolution(ImageReader var0) throws Exception {
      IIOMetadata var1 = var0.getImageMetadata(0);
      if (var1 != null) {
         Node var2 = var1.getAsTree("javax_imageio_1.0");
         if (var2 != null) {
            Element var3 = getChildElementByTag(var2, "Dimension");
            if (var3 != null) {
               double var4 = 0.0;
               double var6 = 0.0;

               for(Node var8 = var3.getFirstChild(); var8 != null; var8 = var8.getNextSibling()) {
                  String var9 = var8.getNodeName();
                  if ("HorizontalPixelSize".equals(var9)) {
                     var4 = getDoubleAttribute((Element)var8, "value");
                  } else if ("VerticalPixelSize".equals(var9)) {
                     var6 = getDoubleAttribute((Element)var8, "value");
                  }
               }

               if (var4 > 0.0 && var6 > 0.0) {
                  return new double[]{25.4 / var4, 25.4 / var6};
               }
            }
         }
      }

      return null;
   }

   private static Element getChildElementByTag(Node var0, String var1) {
      for(Node var2 = var0.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         if (var1.equals(var2.getNodeName())) {
            return (Element)var2;
         }
      }

      return null;
   }

   private static double getDoubleAttribute(Element var0, String var1) {
      String var2 = var0.getAttribute(var1);
      if (var2 != null && var2.length() != 0) {
         try {
            return Double.parseDouble(var2);
         } catch (NumberFormatException var4) {
            return -1.0;
         }
      } else {
         return -1.0;
      }
   }

   public Graphic convertGraphic(Graphic var1, String var2, double var3, double var5, Object var7, GraphicEnv var8) throws Exception {
      var2 = URLUtil.normalizeMIMEType(var2);
      if (var3 <= 0.0) {
         var3 = 1.0;
      }

      if (var5 <= 0.0) {
         var5 = 1.0;
      }

      double var9 = var1.getXResolution();
      double var11 = var1.getYResolution();
      File var13;
      int var14;
      int var15;
      if (var1.getFormat().equals(var2) && var3 == 1.0 && var5 == 1.0) {
         var13 = var8.createTempFile(GraphicUtil.formatToSuffix(var2));
         GraphicUtil.saveGraphic(var1, var13);
         var14 = var1.getWidth();
         var15 = var1.getHeight();
      } else {
         BufferedImage var16 = readImage(var1);
         if (var3 != 1.0 || var5 != 1.0) {
            var16 = scaleImage(var16, var3, var5);
         }

         var13 = var8.createTempFile(GraphicUtil.formatToSuffix(var2));
         writeImage(var16, var9, var11, var2, var13, var8);
         var14 = var16.getWidth();
         var15 = var16.getHeight();
      }

      return new GraphicImpl(URLUtil.fileToLocation(var13), var2, var14, var15, var9, var11, var7);
   }

   private static BufferedImage readImage(Graphic var0) throws Exception {
      ImageReader var2 = getImageReader(var0.getFormat());

      BufferedImage var1;
      try {
         InputStream var3 = URLUtil.openStream(var0.getLocation());

         try {
            ImageInputStream var4 = ImageIO.createImageInputStream(var3);
            if (var4 == null) {
               throw new RuntimeException("cannot create an ImageInputStream out of a java.io.InputStream");
            }

            try {
               ImageReadParam var5 = var2.getDefaultReadParam();
               var2.setInput(var4, true, true);
               var1 = var2.read(0, var5);
            } finally {
               var4.close();
            }
         } finally {
            var3.close();
         }
      } finally {
         var2.dispose();
      }

      return var1;
   }

   public static BufferedImage scaleImage(BufferedImage var0, double var1, double var3) {
      AffineTransform var5 = new AffineTransform();
      var5.setToScale(var1, var3);
      AffineTransformOp var6 = new AffineTransformOp(var5, 2);
      BufferedImage var7 = var6.filter(var0, (BufferedImage)null);
      return var7;
   }

   public static void writeImage(BufferedImage var0, double var1, double var3, String var5, File var6) throws Exception {
      writeImage(var0, var1, var3, var5, var6, (GraphicEnv)null);
   }

   private static void writeImage(BufferedImage var0, double var1, double var3, String var5, File var6, GraphicEnv var7) throws Exception {
      if ("image/jpeg".equals(var5) && var0.getType() == 2) {
         BufferedImage var8 = new BufferedImage(var0.getWidth(), var0.getHeight(), 1);
         ColorConvertOp var9 = new ColorConvertOp((RenderingHints)null);
         var9.filter(var0, var8);
         var0 = var8;
      }

      ImageWriter var21 = getImageWriter(var5);

      try {
         FileImageOutputStream var22 = new FileImageOutputStream(var6);

         try {
            var21.setOutput(var22);
            ImageWriteParam var10 = var21.getDefaultWriteParam();
            IIOMetadata var11 = null;
            if (var1 > 0.0 && var3 > 0.0) {
               ImageTypeSpecifier var12 = ImageTypeSpecifier.createFromRenderedImage(var0);
               var11 = var21.getDefaultImageMetadata(var12, var10);
               if (!setResolution(var11, var1, var3, var5) && var7 != null) {
                  var7.reportWarning("cannot set the resolution of '" + var6 + "' to " + Double.toString(var1) + "x" + Double.toString(var3) + "DPI");
               }
            }

            var21.write((IIOMetadata)null, new IIOImage(var0, (List)null, var11), var10);
            var22.flush();
         } finally {
            var22.close();
         }
      } finally {
         var21.dispose();
      }

   }

   private static boolean setResolution(IIOMetadata var0, double var1, double var3, String var5) throws Exception {
      if (var0.isReadOnly()) {
         return false;
      } else if ("image/png".equals(var5)) {
         Node var10 = var0.getAsTree("javax_imageio_png_1.0");
         Object var11 = getChildElementByTag(var10, "pHYs");
         if (var11 == null) {
            var11 = new IIOMetadataNode("pHYs");
            var10.appendChild((Node)var11);
         }

         ((Element)var11).setAttribute("unitSpecifier", "meter");
         int var12 = (int)Math.rint(var1 * 39.37);
         ((Element)var11).setAttribute("pixelsPerUnitXAxis", Integer.toString(var12));
         int var13 = (int)Math.rint(var3 * 39.37);
         ((Element)var11).setAttribute("pixelsPerUnitYAxis", Integer.toString(var13));
         var0.setFromTree("javax_imageio_png_1.0", var10);
         return true;
      } else if ("image/jpeg".equals(var5)) {
         if (!var0.isStandardMetadataFormatSupported()) {
            return false;
         } else {
            IIOMetadataNode var6 = new IIOMetadataNode("javax_imageio_1.0");
            IIOMetadataNode var7 = new IIOMetadataNode("Dimension");
            var6.appendChild(var7);
            IIOMetadataNode var8 = new IIOMetadataNode("HorizontalPixelSize");
            var7.appendChild(var8);
            var8.setAttribute("value", Double.toString(0.254 / var1));
            IIOMetadataNode var9 = new IIOMetadataNode("VerticalPixelSize");
            var7.appendChild(var9);
            var9.setAttribute("value", Double.toString(0.254 / var3));
            var0.mergeTree("javax_imageio_1.0", var6);
            return true;
         }
      } else {
         return false;
      }
   }

   private static ImageWriter getImageWriter(String var0) {
      ImageWriter var1 = null;
      Iterator var2 = ImageIO.getImageWritersByMIMEType(var0);
      if (var2.hasNext()) {
         var1 = (ImageWriter)var2.next();
      }

      if (var1 == null) {
         throw new RuntimeException("don't know how to write '" + var0 + "' images");
      } else {
         return var1;
      }
   }
}

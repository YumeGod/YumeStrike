package org.apache.fop.render.pdf;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import org.apache.fop.pdf.PDFDocument;
import org.apache.fop.pdf.PDFImage;
import org.apache.fop.pdf.PDFResourceContext;
import org.apache.fop.pdf.PDFXObject;
import org.apache.fop.render.ImageHandler;
import org.apache.fop.render.RendererContext;
import org.apache.fop.render.RenderingContext;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.ImageRawJPEG;

public class PDFImageHandlerRawJPEG implements PDFImageHandler, ImageHandler {
   private static final ImageFlavor[] FLAVORS;

   public PDFXObject generateImage(RendererContext context, Image image, Point origin, Rectangle pos) throws IOException {
      PDFRenderer renderer = (PDFRenderer)context.getRenderer();
      ImageRawJPEG jpeg = (ImageRawJPEG)image;
      PDFDocument pdfDoc = (PDFDocument)context.getProperty("pdfDoc");
      PDFResourceContext resContext = (PDFResourceContext)context.getProperty("pdfContext");
      PDFImage pdfimage = new ImageRawJPEGAdapter(jpeg, image.getInfo().getOriginalURI());
      PDFXObject xobj = pdfDoc.addImage(resContext, pdfimage);
      float x = (float)pos.getX() / 1000.0F;
      float y = (float)pos.getY() / 1000.0F;
      float w = (float)pos.getWidth() / 1000.0F;
      float h = (float)pos.getHeight() / 1000.0F;
      renderer.placeImage(x, y, w, h, xobj);
      return xobj;
   }

   public void handleImage(RenderingContext context, Image image, Rectangle pos) throws IOException {
      PDFRenderingContext pdfContext = (PDFRenderingContext)context;
      PDFContentGenerator generator = pdfContext.getGenerator();
      ImageRawJPEG imageJPEG = (ImageRawJPEG)image;
      PDFImage pdfimage = new ImageRawJPEGAdapter(imageJPEG, image.getInfo().getOriginalURI());
      PDFXObject xobj = generator.getDocument().addImage(generator.getResourceContext(), pdfimage);
      float x = (float)pos.getX() / 1000.0F;
      float y = (float)pos.getY() / 1000.0F;
      float w = (float)pos.getWidth() / 1000.0F;
      float h = (float)pos.getHeight() / 1000.0F;
      if (context.getUserAgent().isAccessibilityEnabled()) {
         PDFLogicalStructureHandler.MarkedContentInfo mci = pdfContext.getMarkedContentInfo();
         generator.placeImage(x, y, w, h, xobj, mci.tag, mci.mcid);
      } else {
         generator.placeImage(x, y, w, h, xobj);
      }

   }

   public int getPriority() {
      return 100;
   }

   public Class getSupportedImageClass() {
      return ImageRawJPEG.class;
   }

   public ImageFlavor[] getSupportedImageFlavors() {
      return FLAVORS;
   }

   public boolean isCompatible(RenderingContext targetContext, Image image) {
      return (image == null || image instanceof ImageRawJPEG) && targetContext instanceof PDFRenderingContext;
   }

   static {
      FLAVORS = new ImageFlavor[]{ImageFlavor.RAW_JPEG};
   }
}

package org.apache.fop.render.ps;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import org.apache.fop.render.ImageHandlerRegistry;
import org.apache.fop.render.RenderingContext;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.pipeline.ImageProviderPipeline;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.apache.xmlgraphics.ps.PSResource;

public class PSImageUtils extends org.apache.xmlgraphics.ps.PSImageUtils {
   public static boolean isImageInlined(ImageInfo info, PSRenderingContext renderingContext) {
      String uri = info.getOriginalURI();
      if (uri != null && !"".equals(uri)) {
         ImageFlavor[] inlineFlavors = determineSupportedImageFlavors(renderingContext);
         ImageManager manager = renderingContext.getUserAgent().getFactory().getImageManager();
         ImageProviderPipeline[] inlineCandidates = manager.getPipelineFactory().determineCandidatePipelines(info, inlineFlavors);
         ImageProviderPipeline inlineChoice = manager.choosePipeline(inlineCandidates);
         ImageFlavor inlineFlavor = inlineChoice != null ? inlineChoice.getTargetFlavor() : null;
         PSRenderingContext formContext = renderingContext.toFormContext();
         ImageFlavor[] formFlavors = determineSupportedImageFlavors(formContext);
         ImageProviderPipeline[] formCandidates = manager.getPipelineFactory().determineCandidatePipelines(info, formFlavors);
         ImageProviderPipeline formChoice = manager.choosePipeline(formCandidates);
         ImageFlavor formFlavor = formChoice != null ? formChoice.getTargetFlavor() : null;
         return formFlavor == null || !formFlavor.equals(inlineFlavor);
      } else {
         return true;
      }
   }

   private static ImageFlavor[] determineSupportedImageFlavors(RenderingContext renderingContext) {
      ImageHandlerRegistry imageHandlerRegistry = renderingContext.getUserAgent().getFactory().getImageHandlerRegistry();
      ImageFlavor[] inlineFlavors = imageHandlerRegistry.getSupportedFlavors(renderingContext);
      return inlineFlavors;
   }

   public static void drawForm(PSResource form, ImageInfo info, Rectangle rect, PSGenerator generator) throws IOException {
      Rectangle2D targetRect = new Rectangle2D.Double(rect.getMinX() / 1000.0, rect.getMinY() / 1000.0, rect.getWidth() / 1000.0, rect.getHeight() / 1000.0);
      generator.saveGraphicsState();
      translateAndScale(generator, info.getSize().getDimensionPt(), targetRect);
      generator.writeDSCComment("IncludeResource", (Object)form);
      generator.getResourceTracker().notifyResourceUsageOnPage(form);
      generator.writeln(form.getName() + " execform");
      generator.restoreGraphicsState();
   }
}

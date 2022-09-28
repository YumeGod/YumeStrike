package org.apache.xmlgraphics.image.loader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.transform.Source;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlgraphics.image.loader.cache.ImageCache;
import org.apache.xmlgraphics.image.loader.pipeline.ImageProviderPipeline;
import org.apache.xmlgraphics.image.loader.pipeline.PipelineFactory;
import org.apache.xmlgraphics.image.loader.spi.ImageImplRegistry;
import org.apache.xmlgraphics.image.loader.spi.ImagePreloader;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;
import org.apache.xmlgraphics.image.loader.util.Penalty;

public class ImageManager {
   protected static Log log;
   private ImageImplRegistry registry;
   private ImageContext imageContext;
   private ImageCache cache;
   private PipelineFactory pipelineFactory;

   public ImageManager(ImageContext context) {
      this(ImageImplRegistry.getDefaultInstance(), context);
   }

   public ImageManager(ImageImplRegistry registry, ImageContext context) {
      this.cache = new ImageCache();
      this.pipelineFactory = new PipelineFactory(this);
      this.registry = registry;
      this.imageContext = context;
   }

   public ImageImplRegistry getRegistry() {
      return this.registry;
   }

   public ImageContext getImageContext() {
      return this.imageContext;
   }

   public ImageCache getCache() {
      return this.cache;
   }

   public PipelineFactory getPipelineFactory() {
      return this.pipelineFactory;
   }

   public ImageInfo getImageInfo(String uri, ImageSessionContext session) throws ImageException, IOException {
      return this.getCache() != null ? this.getCache().needImageInfo(uri, session, this) : this.preloadImage(uri, session);
   }

   public ImageInfo preloadImage(String uri, ImageSessionContext session) throws ImageException, IOException {
      Source src = session.needSource(uri);
      ImageInfo info = this.preloadImage(uri, src);
      session.returnSource(uri, src);
      return info;
   }

   public ImageInfo preloadImage(String uri, Source src) throws ImageException, IOException {
      Iterator iter = this.registry.getPreloaderIterator();

      ImageInfo info;
      do {
         if (!iter.hasNext()) {
            throw new ImageException("The file format is not supported. No ImagePreloader found for " + uri);
         }

         ImagePreloader preloader = (ImagePreloader)iter.next();
         info = preloader.preloadImage(uri, src, this.imageContext);
      } while(info == null);

      return info;
   }

   private Map prepareHints(Map hints, ImageSessionContext sessionContext) {
      Map newHints = new HashMap();
      if (hints != null) {
         newHints.putAll(hints);
      }

      if (!newHints.containsKey(ImageProcessingHints.IMAGE_SESSION_CONTEXT) && sessionContext != null) {
         newHints.put(ImageProcessingHints.IMAGE_SESSION_CONTEXT, sessionContext);
      }

      if (!newHints.containsKey(ImageProcessingHints.IMAGE_MANAGER)) {
         newHints.put(ImageProcessingHints.IMAGE_MANAGER, this);
      }

      return newHints;
   }

   public Image getImage(ImageInfo info, ImageFlavor flavor, Map hints, ImageSessionContext session) throws ImageException, IOException {
      hints = this.prepareHints(hints, session);
      Image img = null;
      ImageProviderPipeline pipeline = this.getPipelineFactory().newImageConverterPipeline(info, flavor);
      if (pipeline != null) {
         img = pipeline.execute(info, hints, session);
      }

      if (img == null) {
         throw new ImageException("Cannot load image (no suitable loader/converter combination available) for " + info);
      } else {
         ImageUtil.closeQuietly(session.getSource(info.getOriginalURI()));
         return img;
      }
   }

   public Image getImage(ImageInfo info, ImageFlavor[] flavors, Map hints, ImageSessionContext session) throws ImageException, IOException {
      hints = this.prepareHints(hints, session);
      Image img = null;
      ImageProviderPipeline[] candidates = this.getPipelineFactory().determineCandidatePipelines(info, flavors);
      ImageProviderPipeline pipeline = this.choosePipeline(candidates);
      if (pipeline != null) {
         img = pipeline.execute(info, hints, session);
      }

      if (img == null) {
         throw new ImageException("Cannot load image (no suitable loader/converter combination available) for " + info);
      } else {
         ImageUtil.closeQuietly(session.getSource(info.getOriginalURI()));
         return img;
      }
   }

   public Image getImage(ImageInfo info, ImageFlavor flavor, ImageSessionContext session) throws ImageException, IOException {
      return this.getImage(info, flavor, ImageUtil.getDefaultHints(session), session);
   }

   public Image getImage(ImageInfo info, ImageFlavor[] flavors, ImageSessionContext session) throws ImageException, IOException {
      return this.getImage(info, flavors, ImageUtil.getDefaultHints(session), session);
   }

   public Image convertImage(Image image, ImageFlavor[] flavors, Map hints) throws ImageException, IOException {
      hints = this.prepareHints(hints, (ImageSessionContext)null);
      ImageInfo info = image.getInfo();
      Image img = null;
      int count = flavors.length;

      for(int i = 0; i < count; ++i) {
         if (image.getFlavor().equals(flavors[i])) {
            return image;
         }
      }

      ImageProviderPipeline[] candidates = this.getPipelineFactory().determineCandidatePipelines(image, flavors);
      ImageProviderPipeline pipeline = this.choosePipeline(candidates);
      if (pipeline != null) {
         img = pipeline.execute(info, image, hints, (ImageSessionContext)null);
      }

      if (img == null) {
         throw new ImageException("Cannot convert image " + image + " (no suitable converter combination available)");
      } else {
         return img;
      }
   }

   public Image convertImage(Image image, ImageFlavor[] flavors) throws ImageException, IOException {
      return this.convertImage(image, flavors, (Map)null);
   }

   public ImageProviderPipeline choosePipeline(ImageProviderPipeline[] candidates) {
      ImageProviderPipeline pipeline = null;
      int minPenalty = Integer.MAX_VALUE;
      int count = candidates.length;
      int i;
      if (log.isTraceEnabled()) {
         log.trace("Candidate Pipelines:");

         for(i = 0; i < count; ++i) {
            if (candidates[i] != null) {
               log.trace("  " + i + ": " + candidates[i].getConversionPenalty(this.getRegistry()) + " for " + candidates[i]);
            }
         }
      }

      for(i = count - 1; i >= 0; --i) {
         if (candidates[i] != null) {
            Penalty penalty = candidates[i].getConversionPenalty(this.getRegistry());
            if (!penalty.isInfinitePenalty() && penalty.getValue() <= minPenalty) {
               pipeline = candidates[i];
               minPenalty = penalty.getValue();
            }
         }
      }

      if (log.isDebugEnabled()) {
         log.debug("Chosen pipeline: " + pipeline);
      }

      return pipeline;
   }

   static {
      log = LogFactory.getLog(ImageManager.class);
   }
}

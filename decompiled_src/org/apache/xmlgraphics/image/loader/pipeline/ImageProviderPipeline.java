package org.apache.xmlgraphics.image.loader.pipeline;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.cache.ImageCache;
import org.apache.xmlgraphics.image.loader.impl.ImageRawStream;
import org.apache.xmlgraphics.image.loader.spi.ImageConverter;
import org.apache.xmlgraphics.image.loader.spi.ImageImplRegistry;
import org.apache.xmlgraphics.image.loader.spi.ImageLoader;
import org.apache.xmlgraphics.image.loader.util.Penalty;

public class ImageProviderPipeline {
   protected static Log log;
   private ImageCache cache;
   private ImageLoader loader;
   private List converters;

   public ImageProviderPipeline(ImageCache cache, ImageLoader loader) {
      this.converters = new ArrayList();
      this.cache = cache;
      this.setImageLoader(loader);
   }

   public ImageProviderPipeline(ImageLoader loader) {
      this((ImageCache)null, loader);
   }

   public ImageProviderPipeline() {
      this((ImageCache)null, (ImageLoader)null);
   }

   public Image execute(ImageInfo info, Map hints, ImageSessionContext context) throws ImageException, IOException {
      return this.execute(info, (Image)null, hints, context);
   }

   public Image execute(ImageInfo info, Image originalImage, Map hints, ImageSessionContext context) throws ImageException, IOException {
      if (hints == null) {
         hints = Collections.EMPTY_MAP;
      }

      long start = System.currentTimeMillis();
      Image img = null;
      Image lastCacheableImage = null;
      int converterCount = this.converters.size();
      int startingPoint = 0;
      if (this.cache != null) {
         for(int i = converterCount - 1; i >= 0; --i) {
            ImageConverter converter = this.getConverter(i);
            ImageFlavor flavor = converter.getTargetFlavor();
            img = this.cache.getImage(info, flavor);
            if (img != null) {
               startingPoint = i + 1;
               break;
            }
         }

         if (img == null && this.loader != null) {
            ImageFlavor flavor = this.loader.getTargetFlavor();
            img = this.cache.getImage(info, flavor);
         }
      }

      if (img == null && originalImage != null) {
         img = originalImage;
      }

      boolean entirelyInCache = true;
      long duration;
      if (img == null && this.loader != null) {
         img = this.loader.loadImage(info, hints, context);
         if (log.isTraceEnabled()) {
            duration = System.currentTimeMillis() - start;
            log.trace("Image loading using " + this.loader + " took " + duration + " ms.");
         }

         entirelyInCache = false;
         if (img.isCacheable()) {
            lastCacheableImage = img;
         }
      }

      if (img == null) {
         throw new ImageException("Pipeline fails. No ImageLoader and no original Image available.");
      } else {
         if (converterCount > 0) {
            for(int i = startingPoint; i < converterCount; ++i) {
               ImageConverter converter = this.getConverter(i);
               start = System.currentTimeMillis();
               img = converter.convert(img, hints);
               if (log.isTraceEnabled()) {
                  duration = System.currentTimeMillis() - start;
                  log.trace("Image conversion using " + converter + " took " + duration + " ms.");
               }

               entirelyInCache = false;
               if (img.isCacheable()) {
                  lastCacheableImage = img;
               }
            }
         }

         if (this.cache != null && !entirelyInCache) {
            if (lastCacheableImage == null) {
               lastCacheableImage = this.forceCaching(img);
            }

            if (lastCacheableImage != null) {
               if (log.isTraceEnabled()) {
                  log.trace("Caching image: " + lastCacheableImage);
               }

               this.cache.putImage(lastCacheableImage);
            }
         }

         return img;
      }
   }

   private ImageConverter getConverter(int index) {
      return (ImageConverter)this.converters.get(index);
   }

   protected Image forceCaching(Image img) throws IOException {
      if (img instanceof ImageRawStream) {
         ImageRawStream raw = (ImageRawStream)img;
         if (log.isDebugEnabled()) {
            log.debug("Image is made cacheable: " + img.getInfo());
         }

         ByteArrayOutputStream baout = new ByteArrayOutputStream();
         InputStream in = raw.createInputStream();

         try {
            IOUtils.copy((InputStream)in, (OutputStream)baout);
         } finally {
            IOUtils.closeQuietly(in);
         }

         byte[] data = baout.toByteArray();
         raw.setInputStreamFactory(new ImageRawStream.ByteArrayStreamFactory(data));
         return raw;
      } else {
         return null;
      }
   }

   public void setImageLoader(ImageLoader imageLoader) {
      this.loader = imageLoader;
   }

   public void addConverter(ImageConverter converter) {
      this.converters.add(converter);
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("Loader: ").append(this.loader);
      if (this.converters.size() > 0) {
         sb.append(" Converters: ");
         sb.append(this.converters);
      }

      return sb.toString();
   }

   public int getConversionPenalty() {
      return this.getConversionPenalty((ImageImplRegistry)null).getValue();
   }

   public Penalty getConversionPenalty(ImageImplRegistry registry) {
      Penalty penalty = Penalty.ZERO_PENALTY;
      if (this.loader != null) {
         penalty = penalty.add(this.loader.getUsagePenalty());
         if (registry != null) {
            penalty = penalty.add(registry.getAdditionalPenalty(this.loader.getClass().getName()));
         }
      }

      Iterator iter = this.converters.iterator();

      while(iter.hasNext()) {
         ImageConverter converter = (ImageConverter)iter.next();
         penalty = penalty.add(converter.getConversionPenalty());
         if (registry != null) {
            penalty = penalty.add(registry.getAdditionalPenalty(converter.getClass().getName()));
         }
      }

      return penalty;
   }

   public ImageFlavor getTargetFlavor() {
      if (this.converters.size() > 0) {
         return this.getConverter(this.converters.size() - 1).getTargetFlavor();
      } else {
         return this.loader != null ? this.loader.getTargetFlavor() : null;
      }
   }

   static {
      log = LogFactory.getLog(ImageProviderPipeline.class);
   }
}

package org.apache.xmlgraphics.image.loader.pipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.impl.CompositeImageLoader;
import org.apache.xmlgraphics.image.loader.spi.ImageConverter;
import org.apache.xmlgraphics.image.loader.spi.ImageImplRegistry;
import org.apache.xmlgraphics.image.loader.spi.ImageLoader;
import org.apache.xmlgraphics.image.loader.spi.ImageLoaderFactory;
import org.apache.xmlgraphics.image.loader.util.Penalty;
import org.apache.xmlgraphics.util.dijkstra.DefaultEdgeDirectory;
import org.apache.xmlgraphics.util.dijkstra.DijkstraAlgorithm;
import org.apache.xmlgraphics.util.dijkstra.Vertex;

public class PipelineFactory {
   protected static Log log;
   private ImageManager manager;
   private int converterEdgeDirectoryVersion = -1;
   private DefaultEdgeDirectory converterEdgeDirectory;

   public PipelineFactory(ImageManager manager) {
      this.manager = manager;
   }

   private DefaultEdgeDirectory getEdgeDirectory() {
      ImageImplRegistry registry = this.manager.getRegistry();
      if (registry.getImageConverterModifications() != this.converterEdgeDirectoryVersion) {
         Collection converters = registry.getImageConverters();
         DefaultEdgeDirectory dir = new DefaultEdgeDirectory();
         Iterator iter = converters.iterator();

         while(iter.hasNext()) {
            ImageConverter converter = (ImageConverter)iter.next();
            Penalty penalty = Penalty.toPenalty(converter.getConversionPenalty());
            penalty = penalty.add(registry.getAdditionalPenalty(converter.getClass().getName()));
            dir.addEdge(new ImageConversionEdge(converter, penalty));
         }

         this.converterEdgeDirectoryVersion = registry.getImageConverterModifications();
         this.converterEdgeDirectory = dir;
      }

      return this.converterEdgeDirectory;
   }

   public ImageProviderPipeline newImageConverterPipeline(Image originalImage, ImageFlavor targetFlavor) {
      DefaultEdgeDirectory dir = this.getEdgeDirectory();
      ImageRepresentation destination = new ImageRepresentation(targetFlavor);
      ImageProviderPipeline pipeline = this.findPipeline(dir, originalImage.getFlavor(), destination);
      return pipeline;
   }

   public ImageProviderPipeline newImageConverterPipeline(ImageInfo imageInfo, ImageFlavor targetFlavor) {
      ImageProviderPipeline[] candidates = this.determineCandidatePipelines(imageInfo, targetFlavor);
      if (candidates.length > 0) {
         Arrays.sort(candidates, new PipelineComparator());
         ImageProviderPipeline pipeline = candidates[0];
         if (pipeline != null && log.isDebugEnabled()) {
            log.debug("Pipeline: " + pipeline + " with penalty " + pipeline.getConversionPenalty());
         }

         return pipeline;
      } else {
         return null;
      }
   }

   public ImageProviderPipeline[] determineCandidatePipelines(ImageInfo imageInfo, ImageFlavor targetFlavor) {
      String originalMime = imageInfo.getMimeType();
      ImageImplRegistry registry = this.manager.getRegistry();
      List candidates = new ArrayList();
      DefaultEdgeDirectory dir = this.getEdgeDirectory();
      ImageLoaderFactory[] loaderFactories = registry.getImageLoaderFactories(imageInfo, targetFlavor);
      int i;
      if (loaderFactories != null) {
         Object loader;
         if (loaderFactories.length == 1) {
            loader = loaderFactories[0].newImageLoader(targetFlavor);
         } else {
            i = loaderFactories.length;
            ImageLoader[] loaders = new ImageLoader[i];

            for(int i = 0; i < i; ++i) {
               loaders[i] = loaderFactories[i].newImageLoader(targetFlavor);
            }

            loader = new CompositeImageLoader(loaders);
         }

         ImageProviderPipeline pipeline = new ImageProviderPipeline(this.manager.getCache(), (ImageLoader)loader);
         candidates.add(pipeline);
      } else {
         if (log.isTraceEnabled()) {
            log.trace("No ImageLoaderFactory found that can load this format (" + targetFlavor + ") directly. Trying ImageConverters instead...");
         }

         ImageRepresentation destination = new ImageRepresentation(targetFlavor);
         loaderFactories = registry.getImageLoaderFactories(originalMime);
         if (loaderFactories != null) {
            i = 0;

            for(int ci = loaderFactories.length; i < ci; ++i) {
               ImageLoaderFactory loaderFactory = loaderFactories[i];
               ImageFlavor[] flavors = loaderFactory.getSupportedFlavors(originalMime);
               int j = 0;

               for(int cj = flavors.length; j < cj; ++j) {
                  ImageProviderPipeline pipeline = this.findPipeline(dir, flavors[j], destination);
                  if (pipeline != null) {
                     ImageLoader loader = loaderFactory.newImageLoader(flavors[j]);
                     pipeline.setImageLoader(loader);
                     candidates.add(pipeline);
                  }
               }
            }
         }
      }

      return (ImageProviderPipeline[])candidates.toArray(new ImageProviderPipeline[candidates.size()]);
   }

   private ImageProviderPipeline findPipeline(DefaultEdgeDirectory dir, ImageFlavor originFlavor, ImageRepresentation destination) {
      DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(dir);
      ImageRepresentation origin = new ImageRepresentation(originFlavor);
      dijkstra.execute(origin, destination);
      if (log.isTraceEnabled()) {
         log.trace("Lowest penalty: " + dijkstra.getLowestPenalty(destination));
      }

      Vertex prev = destination;
      Vertex pred = dijkstra.getPredecessor(destination);
      if (pred == null) {
         if (log.isTraceEnabled()) {
            log.trace("No route found!");
         }

         return null;
      } else {
         LinkedList stops;
         for(stops = new LinkedList(); (pred = dijkstra.getPredecessor((Vertex)prev)) != null; prev = pred) {
            ImageConversionEdge edge = (ImageConversionEdge)dir.getBestEdge(pred, (Vertex)prev);
            stops.addFirst(edge);
         }

         ImageProviderPipeline pipeline = new ImageProviderPipeline(this.manager.getCache(), (ImageLoader)null);
         Iterator iter = stops.iterator();

         while(iter.hasNext()) {
            ImageConversionEdge edge = (ImageConversionEdge)iter.next();
            pipeline.addConverter(edge.getImageConverter());
         }

         return pipeline;
      }
   }

   public ImageProviderPipeline[] determineCandidatePipelines(ImageInfo imageInfo, ImageFlavor[] flavors) {
      List candidates = new ArrayList();
      int count = flavors.length;

      for(int i = 0; i < count; ++i) {
         ImageProviderPipeline pipeline = this.newImageConverterPipeline(imageInfo, flavors[i]);
         if (pipeline != null) {
            Penalty p = pipeline.getConversionPenalty(this.manager.getRegistry());
            if (!p.isInfinitePenalty()) {
               candidates.add(pipeline);
            }
         }
      }

      return (ImageProviderPipeline[])candidates.toArray(new ImageProviderPipeline[candidates.size()]);
   }

   public ImageProviderPipeline[] determineCandidatePipelines(Image sourceImage, ImageFlavor[] flavors) {
      List candidates = new ArrayList();
      int count = flavors.length;

      for(int i = 0; i < count; ++i) {
         ImageProviderPipeline pipeline = this.newImageConverterPipeline(sourceImage, flavors[i]);
         if (pipeline != null) {
            candidates.add(pipeline);
         }
      }

      return (ImageProviderPipeline[])candidates.toArray(new ImageProviderPipeline[candidates.size()]);
   }

   static {
      log = LogFactory.getLog(PipelineFactory.class);
   }

   private static class PipelineComparator implements Comparator {
      private PipelineComparator() {
      }

      public int compare(Object o1, Object o2) {
         ImageProviderPipeline p1 = (ImageProviderPipeline)o1;
         ImageProviderPipeline p2 = (ImageProviderPipeline)o2;
         return p1.getConversionPenalty() - p2.getConversionPenalty();
      }

      // $FF: synthetic method
      PipelineComparator(Object x0) {
         this();
      }
   }
}

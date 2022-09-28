package org.apache.xmlgraphics.image.loader.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.util.Penalty;
import org.apache.xmlgraphics.util.Service;

public class ImageImplRegistry {
   protected static Log log;
   public static final int INFINITE_PENALTY = Integer.MAX_VALUE;
   private List preloaders;
   private int lastPreloaderIdentifier;
   private int lastPreloaderSort;
   private Map loaders;
   private List converters;
   private int converterModifications;
   private Map additionalPenalties;
   private static ImageImplRegistry defaultInstance;

   public ImageImplRegistry(boolean discover) {
      this.preloaders = new ArrayList();
      this.loaders = new HashMap();
      this.converters = new ArrayList();
      this.additionalPenalties = new HashMap();
      if (discover) {
         this.discoverClasspathImplementations();
      }

   }

   public ImageImplRegistry() {
      this(true);
   }

   public static ImageImplRegistry getDefaultInstance() {
      if (defaultInstance == null) {
         defaultInstance = new ImageImplRegistry();
      }

      return defaultInstance;
   }

   public void discoverClasspathImplementations() {
      Iterator iter = Service.providers(ImagePreloader.class, true);

      while(iter.hasNext()) {
         this.registerPreloader((ImagePreloader)iter.next());
      }

      iter = Service.providers(ImageLoaderFactory.class, true);

      while(iter.hasNext()) {
         this.registerLoaderFactory((ImageLoaderFactory)iter.next());
      }

      iter = Service.providers(ImageConverter.class, true);

      while(iter.hasNext()) {
         this.registerConverter((ImageConverter)iter.next());
      }

   }

   public void registerPreloader(ImagePreloader preloader) {
      if (log.isDebugEnabled()) {
         log.debug("Registered " + preloader.getClass().getName() + " with priority " + preloader.getPriority());
      }

      this.preloaders.add(this.newPreloaderHolder(preloader));
   }

   private synchronized PreloaderHolder newPreloaderHolder(ImagePreloader preloader) {
      PreloaderHolder holder = new PreloaderHolder();
      holder.preloader = preloader;
      holder.identifier = ++this.lastPreloaderIdentifier;
      return holder;
   }

   private synchronized void sortPreloaders() {
      if (this.lastPreloaderIdentifier != this.lastPreloaderSort) {
         Collections.sort(this.preloaders, new Comparator() {
            public int compare(Object o1, Object o2) {
               PreloaderHolder h1 = (PreloaderHolder)o1;
               long p1 = (long)h1.preloader.getPriority();
               p1 += (long)ImageImplRegistry.this.getAdditionalPenalty(h1.preloader.getClass().getName()).getValue();
               PreloaderHolder h2 = (PreloaderHolder)o2;
               int p2 = h2.preloader.getPriority();
               p2 += ImageImplRegistry.this.getAdditionalPenalty(h2.preloader.getClass().getName()).getValue();
               int diff = Penalty.truncate(p1 - (long)p2);
               if (diff != 0) {
                  return diff;
               } else {
                  diff = h1.identifier - h2.identifier;
                  return diff;
               }
            }
         });
         this.lastPreloaderSort = this.lastPreloaderIdentifier;
      }

   }

   public void registerLoaderFactory(ImageLoaderFactory loaderFactory) {
      if (!loaderFactory.isAvailable()) {
         if (log.isDebugEnabled()) {
            log.debug("ImageLoaderFactory reports not available: " + loaderFactory.getClass().getName());
         }

      } else {
         String[] mimes = loaderFactory.getSupportedMIMETypes();
         int i = 0;

         for(int ci = mimes.length; i < ci; ++i) {
            String mime = mimes[i];
            synchronized(this.loaders) {
               Map flavorMap = (Map)this.loaders.get(mime);
               if (flavorMap == null) {
                  flavorMap = new HashMap();
                  this.loaders.put(mime, flavorMap);
               }

               ImageFlavor[] flavors = loaderFactory.getSupportedFlavors(mime);
               int j = 0;

               for(int cj = flavors.length; j < cj; ++j) {
                  ImageFlavor flavor = flavors[j];
                  List factoryList = (List)((Map)flavorMap).get(flavor);
                  if (factoryList == null) {
                     factoryList = new ArrayList();
                     ((Map)flavorMap).put(flavor, factoryList);
                  }

                  ((List)factoryList).add(loaderFactory);
                  if (log.isDebugEnabled()) {
                     log.debug("Registered " + loaderFactory.getClass().getName() + ": MIME = " + mime + ", Flavor = " + flavor);
                  }
               }
            }
         }

      }
   }

   public Collection getImageConverters() {
      return Collections.unmodifiableList(this.converters);
   }

   public int getImageConverterModifications() {
      return this.converterModifications;
   }

   public void registerConverter(ImageConverter converter) {
      this.converters.add(converter);
      ++this.converterModifications;
      if (log.isDebugEnabled()) {
         log.debug("Registered: " + converter.getClass().getName());
      }

   }

   public Iterator getPreloaderIterator() {
      this.sortPreloaders();
      final Iterator iter = this.preloaders.iterator();
      return new Iterator() {
         public boolean hasNext() {
            return iter.hasNext();
         }

         public Object next() {
            Object obj = iter.next();
            return obj != null ? ((PreloaderHolder)obj).preloader : null;
         }

         public void remove() {
            iter.remove();
         }
      };
   }

   public ImageLoaderFactory getImageLoaderFactory(ImageInfo imageInfo, ImageFlavor flavor) {
      String mime = imageInfo.getMimeType();
      Map flavorMap = (Map)this.loaders.get(mime);
      if (flavorMap != null) {
         List factoryList = (List)flavorMap.get(flavor);
         if (factoryList != null && factoryList.size() > 0) {
            Iterator iter = factoryList.iterator();
            int bestPenalty = Integer.MAX_VALUE;
            ImageLoaderFactory bestFactory = null;

            while(iter.hasNext()) {
               ImageLoaderFactory factory = (ImageLoaderFactory)iter.next();
               if (factory.isSupported(imageInfo)) {
                  ImageLoader loader = factory.newImageLoader(flavor);
                  int penalty = loader.getUsagePenalty();
                  if (penalty < bestPenalty) {
                     bestPenalty = penalty;
                     bestFactory = factory;
                  }
               }
            }

            return bestFactory;
         }
      }

      return null;
   }

   public ImageLoaderFactory[] getImageLoaderFactories(ImageInfo imageInfo, ImageFlavor flavor) {
      String mime = imageInfo.getMimeType();
      Collection matches = new TreeSet(new ImageLoaderFactoryComparator(flavor));
      Map flavorMap = (Map)this.loaders.get(mime);
      if (flavorMap != null) {
         Iterator flavorIter = flavorMap.keySet().iterator();

         while(true) {
            List factoryList;
            do {
               do {
                  ImageFlavor checkFlavor;
                  do {
                     if (!flavorIter.hasNext()) {
                        return matches.size() == 0 ? null : (ImageLoaderFactory[])matches.toArray(new ImageLoaderFactory[matches.size()]);
                     }

                     checkFlavor = (ImageFlavor)flavorIter.next();
                  } while(!checkFlavor.isCompatible(flavor));

                  factoryList = (List)flavorMap.get(checkFlavor);
               } while(factoryList == null);
            } while(factoryList.size() <= 0);

            Iterator factoryIter = factoryList.iterator();

            while(factoryIter.hasNext()) {
               ImageLoaderFactory factory = (ImageLoaderFactory)factoryIter.next();
               if (factory.isSupported(imageInfo)) {
                  matches.add(factory);
               }
            }
         }
      } else {
         return matches.size() == 0 ? null : (ImageLoaderFactory[])matches.toArray(new ImageLoaderFactory[matches.size()]);
      }
   }

   public ImageLoaderFactory[] getImageLoaderFactories(String mime) {
      Map flavorMap = (Map)this.loaders.get(mime);
      if (flavorMap != null) {
         Set factories = new HashSet();
         Iterator iter = flavorMap.values().iterator();

         while(iter.hasNext()) {
            List factoryList = (List)iter.next();
            factories.addAll(factoryList);
         }

         int factoryCount = factories.size();
         if (factoryCount > 0) {
            return (ImageLoaderFactory[])factories.toArray(new ImageLoaderFactory[factoryCount]);
         }
      }

      return null;
   }

   public void setAdditionalPenalty(String className, Penalty penalty) {
      if (penalty != null) {
         this.additionalPenalties.put(className, penalty);
      } else {
         this.additionalPenalties.remove(className);
      }

      this.lastPreloaderSort = -1;
   }

   public Penalty getAdditionalPenalty(String className) {
      Penalty p = (Penalty)this.additionalPenalties.get(className);
      return p != null ? p : Penalty.ZERO_PENALTY;
   }

   static {
      log = LogFactory.getLog(ImageImplRegistry.class);
   }

   private class ImageLoaderFactoryComparator implements Comparator {
      private ImageFlavor targetFlavor;

      public ImageLoaderFactoryComparator(ImageFlavor targetFlavor) {
         this.targetFlavor = targetFlavor;
      }

      public int compare(Object o1, Object o2) {
         ImageLoaderFactory f1 = (ImageLoaderFactory)o1;
         ImageLoader l1 = f1.newImageLoader(this.targetFlavor);
         long p1 = (long)l1.getUsagePenalty();
         p1 += (long)ImageImplRegistry.this.getAdditionalPenalty(l1.getClass().getName()).getValue();
         ImageLoaderFactory f2 = (ImageLoaderFactory)o2;
         ImageLoader l2 = f2.newImageLoader(this.targetFlavor);
         long p2 = (long)l2.getUsagePenalty();
         p2 = (long)ImageImplRegistry.this.getAdditionalPenalty(l2.getClass().getName()).getValue();
         return Penalty.truncate(p1 - p2);
      }
   }

   private static class PreloaderHolder {
      private ImagePreloader preloader;
      private int identifier;

      private PreloaderHolder() {
      }

      public String toString() {
         return this.preloader + " " + this.identifier;
      }

      // $FF: synthetic method
      PreloaderHolder(Object x0) {
         this();
      }
   }
}

package org.apache.fop.render;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.util.Service;

public abstract class AbstractImageHandlerRegistry {
   private static Log log;
   private static final Comparator HANDLER_COMPARATOR;
   private final Map handlers = new HashMap();
   private final List handlerList = new LinkedList();
   private ImageFlavor[] supportedFlavors = new ImageFlavor[0];
   private int handlerRegistrations;
   private int lastSync;

   public AbstractImageHandlerRegistry() {
      this.discoverHandlers();
   }

   public void addHandler(String classname) {
      try {
         ImageHandlerBase handlerInstance = (ImageHandlerBase)Class.forName(classname).newInstance();
         this.addHandler(handlerInstance);
      } catch (ClassNotFoundException var3) {
         throw new IllegalArgumentException("Could not find " + classname);
      } catch (InstantiationException var4) {
         throw new IllegalArgumentException("Could not instantiate " + classname);
      } catch (IllegalAccessException var5) {
         throw new IllegalArgumentException("Could not access " + classname);
      } catch (ClassCastException var6) {
         throw new IllegalArgumentException(classname + " is not an " + this.getHandlerClass().getName());
      }
   }

   public synchronized void addHandler(ImageHandlerBase handler) {
      this.handlers.put(handler.getSupportedImageClass(), handler);
      ListIterator iter = this.handlerList.listIterator();

      while(iter.hasNext()) {
         ImageHandlerBase h = (ImageHandlerBase)iter.next();
         if (this.getHandlerComparator().compare(handler, h) < 0) {
            iter.previous();
            break;
         }
      }

      iter.add(handler);
      ++this.handlerRegistrations;
   }

   public ImageHandlerBase getHandler(Image img) {
      return this.getHandler(img.getClass());
   }

   public synchronized ImageHandlerBase getHandler(Class imageClass) {
      ImageHandlerBase handler = null;

      for(Class cl = imageClass; cl != null; cl = cl.getSuperclass()) {
         handler = (ImageHandlerBase)this.handlers.get(cl);
         if (handler != null) {
            break;
         }
      }

      return handler;
   }

   public synchronized ImageFlavor[] getSupportedFlavors() {
      if (this.lastSync != this.handlerRegistrations) {
         List flavors = new ArrayList();
         Iterator iter = this.handlerList.iterator();

         while(iter.hasNext()) {
            ImageFlavor[] f = ((ImageHandlerBase)iter.next()).getSupportedImageFlavors();

            for(int i = 0; i < f.length; ++i) {
               flavors.add(f[i]);
            }
         }

         this.supportedFlavors = (ImageFlavor[])flavors.toArray(new ImageFlavor[flavors.size()]);
         this.lastSync = this.handlerRegistrations;
      }

      return this.supportedFlavors;
   }

   private void discoverHandlers() {
      Class imageHandlerClass = this.getHandlerClass();
      Iterator providers = Service.providers(imageHandlerClass);
      if (providers != null) {
         while(providers.hasNext()) {
            ImageHandlerBase handler = (ImageHandlerBase)providers.next();

            try {
               if (log.isDebugEnabled()) {
                  log.debug("Dynamically adding ImageHandler: " + handler.getClass().getName());
               }

               this.addHandler(handler);
            } catch (IllegalArgumentException var5) {
               log.error("Error while adding ImageHandler", var5);
            }
         }
      }

   }

   public Comparator getHandlerComparator() {
      return HANDLER_COMPARATOR;
   }

   public abstract Class getHandlerClass();

   static {
      log = LogFactory.getLog(AbstractImageHandlerRegistry.class);
      HANDLER_COMPARATOR = new Comparator() {
         public int compare(Object o1, Object o2) {
            ImageHandlerBase h1 = (ImageHandlerBase)o1;
            ImageHandlerBase h2 = (ImageHandlerBase)o2;
            return h1.getPriority() - h2.getPriority();
         }
      };
   }
}

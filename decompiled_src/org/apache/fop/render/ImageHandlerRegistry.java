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

public class ImageHandlerRegistry {
   private static Log log;
   private static final Comparator HANDLER_COMPARATOR;
   private Map handlers = new HashMap();
   private List handlerList = new LinkedList();
   private int handlerRegistrations;

   public ImageHandlerRegistry() {
      this.discoverHandlers();
   }

   public void addHandler(String classname) {
      try {
         ImageHandler handlerInstance = (ImageHandler)Class.forName(classname).newInstance();
         this.addHandler(handlerInstance);
      } catch (ClassNotFoundException var3) {
         throw new IllegalArgumentException("Could not find " + classname);
      } catch (InstantiationException var4) {
         throw new IllegalArgumentException("Could not instantiate " + classname);
      } catch (IllegalAccessException var5) {
         throw new IllegalArgumentException("Could not access " + classname);
      } catch (ClassCastException var6) {
         throw new IllegalArgumentException(classname + " is not an " + ImageHandler.class.getName());
      }
   }

   public synchronized void addHandler(ImageHandler handler) {
      Class imageClass = handler.getSupportedImageClass();
      this.handlers.put(imageClass, handler);
      ListIterator iter = this.handlerList.listIterator();

      while(iter.hasNext()) {
         ImageHandler h = (ImageHandler)iter.next();
         if (HANDLER_COMPARATOR.compare(handler, h) < 0) {
            iter.previous();
            break;
         }
      }

      iter.add(handler);
      ++this.handlerRegistrations;
   }

   public ImageHandler getHandler(RenderingContext targetContext, Image image) {
      ListIterator iter = this.handlerList.listIterator();

      ImageHandler h;
      do {
         if (!iter.hasNext()) {
            return null;
         }

         h = (ImageHandler)iter.next();
      } while(!h.isCompatible(targetContext, image));

      return h;
   }

   public synchronized ImageFlavor[] getSupportedFlavors(RenderingContext context) {
      List flavors = new ArrayList();
      Iterator iter = this.handlerList.iterator();

      while(true) {
         ImageHandler handler;
         do {
            if (!iter.hasNext()) {
               return (ImageFlavor[])flavors.toArray(new ImageFlavor[flavors.size()]);
            }

            handler = (ImageHandler)iter.next();
         } while(!handler.isCompatible(context, (Image)null));

         ImageFlavor[] f = handler.getSupportedImageFlavors();

         for(int i = 0; i < f.length; ++i) {
            flavors.add(f[i]);
         }
      }
   }

   private void discoverHandlers() {
      Iterator providers = Service.providers(ImageHandler.class);
      if (providers != null) {
         while(providers.hasNext()) {
            ImageHandler handler = (ImageHandler)providers.next();

            try {
               if (log.isDebugEnabled()) {
                  log.debug("Dynamically adding ImageHandler: " + handler.getClass().getName());
               }

               this.addHandler(handler);
            } catch (IllegalArgumentException var4) {
               log.error("Error while adding ImageHandler", var4);
            }
         }
      }

   }

   static {
      log = LogFactory.getLog(ImageHandlerRegistry.class);
      HANDLER_COMPARATOR = new Comparator() {
         public int compare(Object o1, Object o2) {
            ImageHandler h1 = (ImageHandler)o1;
            ImageHandler h2 = (ImageHandler)o2;
            return h1.getPriority() - h2.getPriority();
         }
      };
   }
}

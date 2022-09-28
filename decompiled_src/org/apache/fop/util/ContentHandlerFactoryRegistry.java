package org.apache.fop.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlgraphics.util.Service;

public class ContentHandlerFactoryRegistry {
   private static Log log;
   private Map factories = new HashMap();

   public ContentHandlerFactoryRegistry() {
      this.discover();
   }

   public void addContentHandlerFactory(String classname) {
      try {
         ContentHandlerFactory factory = (ContentHandlerFactory)Class.forName(classname).newInstance();
         this.addContentHandlerFactory(factory);
      } catch (ClassNotFoundException var3) {
         throw new IllegalArgumentException("Could not find " + classname);
      } catch (InstantiationException var4) {
         throw new IllegalArgumentException("Could not instantiate " + classname);
      } catch (IllegalAccessException var5) {
         throw new IllegalArgumentException("Could not access " + classname);
      } catch (ClassCastException var6) {
         throw new IllegalArgumentException(classname + " is not an " + ContentHandlerFactory.class.getName());
      }
   }

   public void addContentHandlerFactory(ContentHandlerFactory factory) {
      String[] ns = factory.getSupportedNamespaces();

      for(int i = 0; i < ns.length; ++i) {
         this.factories.put(ns[i], factory);
      }

   }

   public ContentHandlerFactory getFactory(String namespaceURI) {
      ContentHandlerFactory factory = (ContentHandlerFactory)this.factories.get(namespaceURI);
      return factory;
   }

   private void discover() {
      Iterator providers = Service.providers(ContentHandlerFactory.class);
      if (providers != null) {
         while(providers.hasNext()) {
            ContentHandlerFactory factory = (ContentHandlerFactory)providers.next();

            try {
               if (log.isDebugEnabled()) {
                  log.debug("Dynamically adding ContentHandlerFactory: " + factory.getClass().getName());
               }

               this.addContentHandlerFactory(factory);
            } catch (IllegalArgumentException var4) {
               log.error("Error while adding ContentHandlerFactory", var4);
            }
         }
      }

   }

   static {
      log = LogFactory.getLog(ContentHandlerFactoryRegistry.class);
   }
}

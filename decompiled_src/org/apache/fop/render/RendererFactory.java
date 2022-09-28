package org.apache.fop.render;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.area.AreaTreeHandler;
import org.apache.fop.fo.FOEventHandler;
import org.apache.fop.render.intermediate.AbstractIFDocumentHandlerMaker;
import org.apache.fop.render.intermediate.IFDocumentHandler;
import org.apache.fop.render.intermediate.IFDocumentHandlerConfigurator;
import org.apache.fop.render.intermediate.IFRenderer;
import org.apache.xmlgraphics.util.Service;

public class RendererFactory {
   private static Log log;
   private Map rendererMakerMapping = new HashMap();
   private Map eventHandlerMakerMapping = new HashMap();
   private Map documentHandlerMakerMapping = new HashMap();
   private boolean rendererPreferred = false;

   public RendererFactory() {
      this.discoverRenderers();
      this.discoverFOEventHandlers();
      this.discoverDocumentHandlers();
   }

   public void setRendererPreferred(boolean value) {
      this.rendererPreferred = value;
   }

   public boolean isRendererPreferred() {
      return this.rendererPreferred;
   }

   public void addRendererMaker(AbstractRendererMaker maker) {
      String[] mimes = maker.getSupportedMimeTypes();

      for(int i = 0; i < mimes.length; ++i) {
         if (this.rendererMakerMapping.get(mimes[i]) != null) {
            log.trace("Overriding renderer for " + mimes[i] + " with " + maker.getClass().getName());
         }

         this.rendererMakerMapping.put(mimes[i], maker);
      }

   }

   public void addFOEventHandlerMaker(AbstractFOEventHandlerMaker maker) {
      String[] mimes = maker.getSupportedMimeTypes();

      for(int i = 0; i < mimes.length; ++i) {
         if (this.eventHandlerMakerMapping.get(mimes[i]) != null) {
            log.trace("Overriding FOEventHandler for " + mimes[i] + " with " + maker.getClass().getName());
         }

         this.eventHandlerMakerMapping.put(mimes[i], maker);
      }

   }

   public void addDocumentHandlerMaker(AbstractIFDocumentHandlerMaker maker) {
      String[] mimes = maker.getSupportedMimeTypes();

      for(int i = 0; i < mimes.length; ++i) {
         if (this.documentHandlerMakerMapping.get(mimes[i]) != null) {
            log.trace("Overriding document handler for " + mimes[i] + " with " + maker.getClass().getName());
         }

         this.documentHandlerMakerMapping.put(mimes[i], maker);
      }

   }

   public void addRendererMaker(String className) {
      try {
         AbstractRendererMaker makerInstance = (AbstractRendererMaker)Class.forName(className).newInstance();
         this.addRendererMaker(makerInstance);
      } catch (ClassNotFoundException var3) {
         throw new IllegalArgumentException("Could not find " + className);
      } catch (InstantiationException var4) {
         throw new IllegalArgumentException("Could not instantiate " + className);
      } catch (IllegalAccessException var5) {
         throw new IllegalArgumentException("Could not access " + className);
      } catch (ClassCastException var6) {
         throw new IllegalArgumentException(className + " is not an " + AbstractRendererMaker.class.getName());
      }
   }

   public void addFOEventHandlerMaker(String className) {
      try {
         AbstractFOEventHandlerMaker makerInstance = (AbstractFOEventHandlerMaker)Class.forName(className).newInstance();
         this.addFOEventHandlerMaker(makerInstance);
      } catch (ClassNotFoundException var3) {
         throw new IllegalArgumentException("Could not find " + className);
      } catch (InstantiationException var4) {
         throw new IllegalArgumentException("Could not instantiate " + className);
      } catch (IllegalAccessException var5) {
         throw new IllegalArgumentException("Could not access " + className);
      } catch (ClassCastException var6) {
         throw new IllegalArgumentException(className + " is not an " + AbstractFOEventHandlerMaker.class.getName());
      }
   }

   public void addDocumentHandlerMaker(String className) {
      try {
         AbstractIFDocumentHandlerMaker makerInstance = (AbstractIFDocumentHandlerMaker)Class.forName(className).newInstance();
         this.addDocumentHandlerMaker(makerInstance);
      } catch (ClassNotFoundException var3) {
         throw new IllegalArgumentException("Could not find " + className);
      } catch (InstantiationException var4) {
         throw new IllegalArgumentException("Could not instantiate " + className);
      } catch (IllegalAccessException var5) {
         throw new IllegalArgumentException("Could not access " + className);
      } catch (ClassCastException var6) {
         throw new IllegalArgumentException(className + " is not an " + AbstractIFDocumentHandlerMaker.class.getName());
      }
   }

   public AbstractRendererMaker getRendererMaker(String mime) {
      AbstractRendererMaker maker = (AbstractRendererMaker)this.rendererMakerMapping.get(mime);
      return maker;
   }

   public AbstractFOEventHandlerMaker getFOEventHandlerMaker(String mime) {
      AbstractFOEventHandlerMaker maker = (AbstractFOEventHandlerMaker)this.eventHandlerMakerMapping.get(mime);
      return maker;
   }

   public AbstractIFDocumentHandlerMaker getDocumentHandlerMaker(String mime) {
      AbstractIFDocumentHandlerMaker maker = (AbstractIFDocumentHandlerMaker)this.documentHandlerMakerMapping.get(mime);
      return maker;
   }

   public Renderer createRenderer(FOUserAgent userAgent, String outputFormat) throws FOPException {
      if (userAgent.getDocumentHandlerOverride() != null) {
         return this.createRendererForDocumentHandler(userAgent.getDocumentHandlerOverride());
      } else if (userAgent.getRendererOverride() != null) {
         return userAgent.getRendererOverride();
      } else {
         Renderer renderer;
         if (this.isRendererPreferred()) {
            renderer = this.tryRendererMaker(userAgent, outputFormat);
            if (renderer == null) {
               renderer = this.tryIFDocumentHandlerMaker(userAgent, outputFormat);
            }
         } else {
            renderer = this.tryIFDocumentHandlerMaker(userAgent, outputFormat);
            if (renderer == null) {
               renderer = this.tryRendererMaker(userAgent, outputFormat);
            }
         }

         if (renderer == null) {
            throw new UnsupportedOperationException("No renderer for the requested format available: " + outputFormat);
         } else {
            return renderer;
         }
      }
   }

   private Renderer tryIFDocumentHandlerMaker(FOUserAgent userAgent, String outputFormat) throws FOPException {
      AbstractIFDocumentHandlerMaker documentHandlerMaker = this.getDocumentHandlerMaker(outputFormat);
      if (documentHandlerMaker != null) {
         IFDocumentHandler documentHandler = this.createDocumentHandler(userAgent, outputFormat);
         return this.createRendererForDocumentHandler(documentHandler);
      } else {
         return null;
      }
   }

   private Renderer tryRendererMaker(FOUserAgent userAgent, String outputFormat) throws FOPException {
      AbstractRendererMaker maker = this.getRendererMaker(outputFormat);
      if (maker != null) {
         Renderer rend = maker.makeRenderer(userAgent);
         rend.setUserAgent(userAgent);
         RendererConfigurator configurator = maker.getConfigurator(userAgent);
         if (configurator != null) {
            configurator.configure(rend);
         }

         return rend;
      } else {
         return null;
      }
   }

   private Renderer createRendererForDocumentHandler(IFDocumentHandler documentHandler) {
      IFRenderer rend = new IFRenderer();
      rend.setUserAgent(documentHandler.getContext().getUserAgent());
      rend.setDocumentHandler(documentHandler);
      return rend;
   }

   public FOEventHandler createFOEventHandler(FOUserAgent userAgent, String outputFormat, OutputStream out) throws FOPException {
      if (userAgent.getFOEventHandlerOverride() != null) {
         return userAgent.getFOEventHandlerOverride();
      } else {
         AbstractFOEventHandlerMaker maker = this.getFOEventHandlerMaker(outputFormat);
         if (maker != null) {
            return maker.makeFOEventHandler(userAgent, out);
         } else {
            AbstractRendererMaker rendMaker = this.getRendererMaker(outputFormat);
            AbstractIFDocumentHandlerMaker documentHandlerMaker = null;
            boolean outputStreamMissing = userAgent.getRendererOverride() == null && userAgent.getDocumentHandlerOverride() == null;
            if (rendMaker == null) {
               documentHandlerMaker = this.getDocumentHandlerMaker(outputFormat);
               if (documentHandlerMaker != null) {
                  outputStreamMissing &= out == null && documentHandlerMaker.needsOutputStream();
               }
            } else {
               outputStreamMissing &= out == null && rendMaker.needsOutputStream();
            }

            if (userAgent.getRendererOverride() == null && rendMaker == null && userAgent.getDocumentHandlerOverride() == null && documentHandlerMaker == null) {
               throw new UnsupportedOperationException("Don't know how to handle \"" + outputFormat + "\" as an output format." + " Neither an FOEventHandler, nor a Renderer could be found" + " for this output format.");
            } else if (outputStreamMissing) {
               throw new FOPException("OutputStream has not been set");
            } else {
               return new AreaTreeHandler(userAgent, outputFormat, out);
            }
         }
      }
   }

   public IFDocumentHandler createDocumentHandler(FOUserAgent userAgent, String outputFormat) throws FOPException {
      if (userAgent.getDocumentHandlerOverride() != null) {
         return userAgent.getDocumentHandlerOverride();
      } else {
         AbstractIFDocumentHandlerMaker maker = this.getDocumentHandlerMaker(outputFormat);
         if (maker == null) {
            throw new UnsupportedOperationException("No IF document handler for the requested format available: " + outputFormat);
         } else {
            IFDocumentHandler documentHandler = maker.makeIFDocumentHandler(userAgent);
            IFDocumentHandlerConfigurator configurator = documentHandler.getConfigurator();
            if (configurator != null) {
               configurator.configure(documentHandler);
            }

            return documentHandler;
         }
      }
   }

   public String[] listSupportedMimeTypes() {
      List lst = new ArrayList();
      Iterator iter = this.rendererMakerMapping.keySet().iterator();

      while(iter.hasNext()) {
         lst.add((String)iter.next());
      }

      iter = this.eventHandlerMakerMapping.keySet().iterator();

      while(iter.hasNext()) {
         lst.add((String)iter.next());
      }

      iter = this.documentHandlerMakerMapping.keySet().iterator();

      while(iter.hasNext()) {
         lst.add((String)iter.next());
      }

      Collections.sort(lst);
      return (String[])lst.toArray(new String[lst.size()]);
   }

   private void discoverRenderers() {
      Iterator providers = Service.providers(Renderer.class);
      if (providers != null) {
         while(providers.hasNext()) {
            AbstractRendererMaker maker = (AbstractRendererMaker)providers.next();

            try {
               if (log.isDebugEnabled()) {
                  log.debug("Dynamically adding maker for Renderer: " + maker.getClass().getName());
               }

               this.addRendererMaker(maker);
            } catch (IllegalArgumentException var4) {
               log.error("Error while adding maker for Renderer", var4);
            }
         }
      }

   }

   private void discoverFOEventHandlers() {
      Iterator providers = Service.providers(FOEventHandler.class);
      if (providers != null) {
         while(providers.hasNext()) {
            AbstractFOEventHandlerMaker maker = (AbstractFOEventHandlerMaker)providers.next();

            try {
               if (log.isDebugEnabled()) {
                  log.debug("Dynamically adding maker for FOEventHandler: " + maker.getClass().getName());
               }

               this.addFOEventHandlerMaker(maker);
            } catch (IllegalArgumentException var4) {
               log.error("Error while adding maker for FOEventHandler", var4);
            }
         }
      }

   }

   private void discoverDocumentHandlers() {
      Iterator providers = Service.providers(IFDocumentHandler.class);
      if (providers != null) {
         while(providers.hasNext()) {
            AbstractIFDocumentHandlerMaker maker = (AbstractIFDocumentHandlerMaker)providers.next();

            try {
               if (log.isDebugEnabled()) {
                  log.debug("Dynamically adding maker for IFDocumentHandler: " + maker.getClass().getName());
               }

               this.addDocumentHandlerMaker(maker);
            } catch (IllegalArgumentException var4) {
               log.error("Error while adding maker for IFDocumentHandler", var4);
            }
         }
      }

   }

   static {
      log = LogFactory.getLog(RendererFactory.class);
   }
}

package org.apache.fop.apps;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.Version;
import org.apache.fop.accessibility.StructureTree;
import org.apache.fop.events.DefaultEventBroadcaster;
import org.apache.fop.events.Event;
import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.events.EventListener;
import org.apache.fop.events.FOPEventListenerProxy;
import org.apache.fop.events.LoggingEventListener;
import org.apache.fop.fo.FOEventHandler;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererFactory;
import org.apache.fop.render.XMLHandlerRegistry;
import org.apache.fop.render.intermediate.IFDocumentHandler;
import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageSessionContext;

public class FOUserAgent {
   public static final float DEFAULT_TARGET_RESOLUTION = 72.0F;
   private static Log log = LogFactory.getLog("FOP");
   private FopFactory factory;
   private String base = null;
   private URIResolver uriResolver = null;
   private float targetResolution = 72.0F;
   private Map rendererOptions = new HashMap();
   private File outputFile = null;
   private IFDocumentHandler documentHandlerOverride = null;
   private Renderer rendererOverride = null;
   private FOEventHandler foEventHandlerOverride = null;
   private boolean locatorEnabled = true;
   private boolean conserveMemoryPolicy = false;
   private EventBroadcaster eventBroadcaster = new FOPEventBroadcaster();
   private StructureTree structureTree;
   protected String producer = "Apache FOP Version " + Version.getVersion();
   protected String creator = null;
   protected Date creationDate = null;
   protected String author = null;
   protected String title = null;
   protected String subject = null;
   protected String keywords = null;
   private ImageSessionContext imageSessionContext = new AbstractImageSessionContext() {
      public ImageContext getParentContext() {
         return FOUserAgent.this.getFactory();
      }

      public float getTargetResolution() {
         return FOUserAgent.this.getTargetResolution();
      }

      public Source resolveURI(String uri) {
         return FOUserAgent.this.resolveURI(uri);
      }
   };

   public FOUserAgent(FopFactory factory) {
      if (factory == null) {
         throw new NullPointerException("The factory parameter must not be null");
      } else {
         this.factory = factory;
         this.setBaseURL(factory.getBaseURL());
         this.setTargetResolution(factory.getTargetResolution());
         this.setAccessibility(factory.isAccessibilityEnabled());
      }
   }

   public FopFactory getFactory() {
      return this.factory;
   }

   public void setDocumentHandlerOverride(IFDocumentHandler documentHandler) {
      this.documentHandlerOverride = documentHandler;
   }

   public IFDocumentHandler getDocumentHandlerOverride() {
      return this.documentHandlerOverride;
   }

   public void setRendererOverride(Renderer renderer) {
      this.rendererOverride = renderer;
   }

   public Renderer getRendererOverride() {
      return this.rendererOverride;
   }

   public void setFOEventHandlerOverride(FOEventHandler handler) {
      this.foEventHandlerOverride = handler;
   }

   public FOEventHandler getFOEventHandlerOverride() {
      return this.foEventHandlerOverride;
   }

   public void setProducer(String producer) {
      this.producer = producer;
   }

   public String getProducer() {
      return this.producer;
   }

   public void setCreator(String creator) {
      this.creator = creator;
   }

   public String getCreator() {
      return this.creator;
   }

   public void setCreationDate(Date creationDate) {
      this.creationDate = creationDate;
   }

   public Date getCreationDate() {
      return this.creationDate;
   }

   public void setAuthor(String author) {
      this.author = author;
   }

   public String getAuthor() {
      return this.author;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getTitle() {
      return this.title;
   }

   public void setSubject(String subject) {
      this.subject = subject;
   }

   public String getSubject() {
      return this.subject;
   }

   public void setKeywords(String keywords) {
      this.keywords = keywords;
   }

   public String getKeywords() {
      return this.keywords;
   }

   public Map getRendererOptions() {
      return this.rendererOptions;
   }

   public void setBaseURL(String baseUrl) {
      this.base = baseUrl;
   }

   /** @deprecated */
   public void setFontBaseURL(String fontBaseUrl) {
      try {
         this.getFactory().getFontManager().setFontBaseURL(fontBaseUrl);
      } catch (MalformedURLException var3) {
         throw new IllegalArgumentException(var3.getMessage());
      }
   }

   public String getBaseURL() {
      return this.base;
   }

   public void setURIResolver(URIResolver resolver) {
      this.uriResolver = resolver;
   }

   public URIResolver getURIResolver() {
      return this.uriResolver;
   }

   public Source resolveURI(String uri) {
      return this.resolveURI(uri, this.getBaseURL());
   }

   public Source resolveURI(String href, String base) {
      Source source = null;
      boolean bypassURIResolution = href.startsWith("data:");
      if (!bypassURIResolution && this.uriResolver != null) {
         try {
            source = this.uriResolver.resolve(href, base);
         } catch (TransformerException var6) {
            log.error("Attempt to resolve URI '" + href + "' failed: ", var6);
         }
      }

      if (source == null) {
         source = this.getFactory().resolveURI(href, base);
      }

      return source;
   }

   public void setOutputFile(File f) {
      this.outputFile = f;
   }

   public File getOutputFile() {
      return this.outputFile;
   }

   public float getTargetPixelUnitToMillimeter() {
      return 25.4F / this.targetResolution;
   }

   public float getTargetResolution() {
      return this.targetResolution;
   }

   public void setTargetResolution(float dpi) {
      this.targetResolution = dpi;
      if (log.isDebugEnabled()) {
         log.debug("target-resolution set to: " + this.targetResolution + "dpi (px2mm=" + this.getTargetPixelUnitToMillimeter() + ")");
      }

   }

   public void setTargetResolution(int dpi) {
      this.setTargetResolution((float)dpi);
   }

   public ImageSessionContext getImageSessionContext() {
      return this.imageSessionContext;
   }

   /** @deprecated */
   public String getFontBaseURL() {
      String fontBase = this.getFactory().getFontManager().getFontBaseURL();
      return fontBase != null ? fontBase : this.getBaseURL();
   }

   public float getSourcePixelUnitToMillimeter() {
      return this.getFactory().getSourcePixelUnitToMillimeter();
   }

   public float getSourceResolution() {
      return this.getFactory().getSourceResolution();
   }

   public String getPageHeight() {
      return this.getFactory().getPageHeight();
   }

   public String getPageWidth() {
      return this.getFactory().getPageWidth();
   }

   public boolean validateStrictly() {
      return this.getFactory().validateStrictly();
   }

   public boolean isBreakIndentInheritanceOnReferenceAreaBoundary() {
      return this.getFactory().isBreakIndentInheritanceOnReferenceAreaBoundary();
   }

   public RendererFactory getRendererFactory() {
      return this.getFactory().getRendererFactory();
   }

   public XMLHandlerRegistry getXMLHandlerRegistry() {
      return this.getFactory().getXMLHandlerRegistry();
   }

   public void setLocatorEnabled(boolean enableLocator) {
      this.locatorEnabled = enableLocator;
   }

   public boolean isLocatorEnabled() {
      return this.locatorEnabled;
   }

   public EventBroadcaster getEventBroadcaster() {
      return this.eventBroadcaster;
   }

   public boolean isConserveMemoryPolicyEnabled() {
      return this.conserveMemoryPolicy;
   }

   public void setConserveMemoryPolicy(boolean conserveMemoryPolicy) {
      this.conserveMemoryPolicy = conserveMemoryPolicy;
   }

   public void setAccessibility(boolean accessibility) {
      if (accessibility) {
         this.getRendererOptions().put("accessibility", Boolean.TRUE);
      }

   }

   public boolean isAccessibilityEnabled() {
      Boolean enabled = (Boolean)this.getRendererOptions().get("accessibility");
      return enabled != null ? enabled : false;
   }

   public void setStructureTree(StructureTree structureTree) {
      this.structureTree = structureTree;
   }

   public StructureTree getStructureTree() {
      return this.structureTree;
   }

   private class FOPEventBroadcaster extends DefaultEventBroadcaster {
      private EventListener rootListener = new EventListener() {
         public void processEvent(Event event) {
            if (!FOPEventBroadcaster.this.listeners.hasEventListeners()) {
               FOPEventBroadcaster.this.addEventListener(new LoggingEventListener(LogFactory.getLog(FOUserAgent.class)));
            }

            FOPEventBroadcaster.this.rootListener = new FOPEventListenerProxy(FOPEventBroadcaster.this.listeners, FOUserAgent.this);
            FOPEventBroadcaster.this.rootListener.processEvent(event);
         }
      };

      public FOPEventBroadcaster() {
      }

      public void broadcastEvent(Event event) {
         this.rootListener.processEvent(event);
      }
   }
}

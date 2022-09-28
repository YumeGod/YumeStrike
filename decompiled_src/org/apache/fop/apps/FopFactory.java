package org.apache.fop.apps;

import java.awt.color.ColorSpace;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fo.ElementMapping;
import org.apache.fop.fo.ElementMappingRegistry;
import org.apache.fop.fonts.FontCache;
import org.apache.fop.fonts.FontManager;
import org.apache.fop.hyphenation.HyphenationTreeResolver;
import org.apache.fop.layoutmgr.LayoutManagerMaker;
import org.apache.fop.render.ImageHandlerRegistry;
import org.apache.fop.render.RendererFactory;
import org.apache.fop.render.XMLHandlerRegistry;
import org.apache.fop.util.ColorSpaceCache;
import org.apache.fop.util.ContentHandlerFactoryRegistry;
import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.xml.sax.SAXException;

public class FopFactory implements ImageContext {
   private static Log log;
   private RendererFactory rendererFactory;
   private XMLHandlerRegistry xmlHandlers;
   private ImageHandlerRegistry imageHandlers;
   private ElementMappingRegistry elementMappingRegistry;
   private ContentHandlerFactoryRegistry contentHandlerFactoryRegistry = new ContentHandlerFactoryRegistry();
   private HyphenationTreeResolver hyphResolver = null;
   private ColorSpaceCache colorSpaceCache = null;
   private ImageManager imageManager;
   private FontManager fontManager;
   private FopFactoryConfigurator config = null;
   private String base = null;
   private boolean accessibility = false;
   private String hyphenBase = null;
   private boolean strictFOValidation = true;
   private boolean strictUserConfigValidation = true;
   private float sourceResolution = 72.0F;
   private float targetResolution = 72.0F;
   private String pageHeight = "11in";
   private String pageWidth = "8.26in";
   private boolean breakIndentInheritanceOnReferenceAreaBoundary = false;
   private LayoutManagerMaker lmMakerOverride = null;
   private Set ignoredNamespaces;
   private FOURIResolver foURIResolver;

   protected FopFactory() {
      this.config = new FopFactoryConfigurator(this);
      this.elementMappingRegistry = new ElementMappingRegistry(this);
      this.foURIResolver = new FOURIResolver(this.validateUserConfigStrictly());
      this.fontManager = new FontManager() {
         public void setFontBaseURL(String fontBase) throws MalformedURLException {
            super.setFontBaseURL(FopFactory.this.getFOURIResolver().checkBaseURL(fontBase));
         }
      };
      this.colorSpaceCache = new ColorSpaceCache(this.foURIResolver);
      this.imageManager = new ImageManager(this);
      this.rendererFactory = new RendererFactory();
      this.xmlHandlers = new XMLHandlerRegistry();
      this.imageHandlers = new ImageHandlerRegistry();
      this.ignoredNamespaces = new HashSet();
   }

   public static FopFactory newInstance() {
      return new FopFactory();
   }

   public FOUserAgent newFOUserAgent() {
      FOUserAgent userAgent = new FOUserAgent(this);
      return userAgent;
   }

   void setAccessibility(boolean value) {
      this.accessibility = value;
   }

   boolean isAccessibilityEnabled() {
      return this.accessibility;
   }

   public Fop newFop(String outputFormat) throws FOPException {
      return this.newFop(outputFormat, this.newFOUserAgent());
   }

   public Fop newFop(String outputFormat, FOUserAgent userAgent) throws FOPException {
      return this.newFop(outputFormat, userAgent, (OutputStream)null);
   }

   public Fop newFop(String outputFormat, OutputStream stream) throws FOPException {
      return this.newFop(outputFormat, this.newFOUserAgent(), stream);
   }

   public Fop newFop(String outputFormat, FOUserAgent userAgent, OutputStream stream) throws FOPException {
      if (userAgent == null) {
         throw new NullPointerException("The userAgent parameter must not be null!");
      } else {
         return new Fop(outputFormat, userAgent, stream);
      }
   }

   public Fop newFop(FOUserAgent userAgent) throws FOPException {
      if (userAgent.getRendererOverride() == null && userAgent.getFOEventHandlerOverride() == null && userAgent.getDocumentHandlerOverride() == null) {
         throw new IllegalStateException("An overriding renderer, FOEventHandler or IFDocumentHandler must be set on the user agent when this factory method is used!");
      } else {
         return this.newFop((String)null, (FOUserAgent)userAgent);
      }
   }

   public RendererFactory getRendererFactory() {
      return this.rendererFactory;
   }

   public XMLHandlerRegistry getXMLHandlerRegistry() {
      return this.xmlHandlers;
   }

   public ImageHandlerRegistry getImageHandlerRegistry() {
      return this.imageHandlers;
   }

   public ElementMappingRegistry getElementMappingRegistry() {
      return this.elementMappingRegistry;
   }

   public ContentHandlerFactoryRegistry getContentHandlerFactoryRegistry() {
      return this.contentHandlerFactoryRegistry;
   }

   public ImageManager getImageManager() {
      return this.imageManager;
   }

   public void addElementMapping(ElementMapping elementMapping) {
      this.elementMappingRegistry.addElementMapping(elementMapping);
   }

   public void setLayoutManagerMakerOverride(LayoutManagerMaker lmMaker) {
      this.lmMakerOverride = lmMaker;
   }

   public LayoutManagerMaker getLayoutManagerMakerOverride() {
      return this.lmMakerOverride;
   }

   public void setBaseURL(String base) throws MalformedURLException {
      this.base = this.foURIResolver.checkBaseURL(base);
   }

   public String getBaseURL() {
      return this.base;
   }

   /** @deprecated */
   public void setFontBaseURL(String fontBase) throws MalformedURLException {
      this.getFontManager().setFontBaseURL(fontBase);
   }

   /** @deprecated */
   public String getFontBaseURL() {
      return this.getFontManager().getFontBaseURL();
   }

   public String getHyphenBaseURL() {
      return this.hyphenBase;
   }

   public void setHyphenBaseURL(final String hyphenBase) throws MalformedURLException {
      if (hyphenBase != null) {
         this.setHyphenationTreeResolver(new HyphenationTreeResolver() {
            public Source resolve(String href) {
               return FopFactory.this.resolveURI(href, hyphenBase);
            }
         });
      }

      this.hyphenBase = this.foURIResolver.checkBaseURL(hyphenBase);
   }

   public void setURIResolver(URIResolver uriResolver) {
      this.foURIResolver.setCustomURIResolver(uriResolver);
   }

   public URIResolver getURIResolver() {
      return this.foURIResolver;
   }

   public FOURIResolver getFOURIResolver() {
      return this.foURIResolver;
   }

   public HyphenationTreeResolver getHyphenationTreeResolver() {
      return this.hyphResolver;
   }

   public void setHyphenationTreeResolver(HyphenationTreeResolver hyphResolver) {
      this.hyphResolver = hyphResolver;
   }

   public void setStrictValidation(boolean validateStrictly) {
      this.strictFOValidation = validateStrictly;
   }

   public boolean validateStrictly() {
      return this.strictFOValidation;
   }

   public boolean isBreakIndentInheritanceOnReferenceAreaBoundary() {
      return this.breakIndentInheritanceOnReferenceAreaBoundary;
   }

   public void setBreakIndentInheritanceOnReferenceAreaBoundary(boolean value) {
      this.breakIndentInheritanceOnReferenceAreaBoundary = value;
   }

   /** @deprecated */
   public boolean isBase14KerningEnabled() {
      return this.getFontManager().isBase14KerningEnabled();
   }

   /** @deprecated */
   public void setBase14KerningEnabled(boolean value) {
      this.getFontManager().setBase14KerningEnabled(value);
   }

   public float getSourceResolution() {
      return this.sourceResolution;
   }

   public float getSourcePixelUnitToMillimeter() {
      return 25.4F / this.getSourceResolution();
   }

   public void setSourceResolution(float dpi) {
      this.sourceResolution = dpi;
      if (log.isDebugEnabled()) {
         log.debug("source-resolution set to: " + this.sourceResolution + "dpi (px2mm=" + this.getSourcePixelUnitToMillimeter() + ")");
      }

   }

   public float getTargetResolution() {
      return this.targetResolution;
   }

   public float getTargetPixelUnitToMillimeter() {
      return 25.4F / this.targetResolution;
   }

   public void setTargetResolution(float dpi) {
      this.targetResolution = dpi;
   }

   public void setSourceResolution(int dpi) {
      this.setSourceResolution((float)dpi);
   }

   public String getPageHeight() {
      return this.pageHeight;
   }

   public void setPageHeight(String pageHeight) {
      this.pageHeight = pageHeight;
      if (log.isDebugEnabled()) {
         log.debug("Default page-height set to: " + pageHeight);
      }

   }

   public String getPageWidth() {
      return this.pageWidth;
   }

   public void setPageWidth(String pageWidth) {
      this.pageWidth = pageWidth;
      if (log.isDebugEnabled()) {
         log.debug("Default page-width set to: " + pageWidth);
      }

   }

   public void ignoreNamespace(String namespaceURI) {
      this.ignoredNamespaces.add(namespaceURI);
   }

   public void ignoreNamespaces(Collection namespaceURIs) {
      this.ignoredNamespaces.addAll(namespaceURIs);
   }

   public boolean isNamespaceIgnored(String namespaceURI) {
      return this.ignoredNamespaces.contains(namespaceURI);
   }

   public Set getIgnoredNamespace() {
      return Collections.unmodifiableSet(this.ignoredNamespaces);
   }

   public void setUserConfig(File userConfigFile) throws SAXException, IOException {
      this.config.setUserConfig(userConfigFile);
   }

   public void setUserConfig(String uri) throws SAXException, IOException {
      this.config.setUserConfig(uri);
   }

   public void setUserConfig(Configuration userConfig) throws FOPException {
      this.config.setUserConfig(userConfig);
   }

   public Configuration getUserConfig() {
      return this.config.getUserConfig();
   }

   public void setStrictUserConfigValidation(boolean strictUserConfigValidation) {
      this.strictUserConfigValidation = strictUserConfigValidation;
      this.foURIResolver.setThrowExceptions(strictUserConfigValidation);
   }

   public boolean validateUserConfigStrictly() {
      return this.strictUserConfigValidation;
   }

   /** @deprecated */
   public void setUseCache(boolean useCache) {
      this.getFontManager().setUseCache(useCache);
   }

   /** @deprecated */
   public boolean useCache() {
      return this.getFontManager().useCache();
   }

   /** @deprecated */
   public FontCache getFontCache() {
      return this.getFontManager().getFontCache();
   }

   public FontManager getFontManager() {
      return this.fontManager;
   }

   public Source resolveURI(String href, String baseUri) {
      Source source = null;

      try {
         source = this.foURIResolver.resolve(href, baseUri);
      } catch (TransformerException var5) {
         log.error("Attempt to resolve URI '" + href + "' failed: ", var5);
      }

      return source;
   }

   public ColorSpace getColorSpace(String baseUri, String iccProfileSrc) {
      return this.colorSpaceCache.get(baseUri, iccProfileSrc);
   }

   static {
      log = LogFactory.getLog(FopFactory.class);
   }
}

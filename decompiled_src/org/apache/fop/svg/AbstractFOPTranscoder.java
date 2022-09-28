package org.apache.fop.svg;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.util.DocumentFactory;
import org.apache.batik.transcoder.ErrorHandler;
import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.keys.BooleanKey;
import org.apache.batik.transcoder.keys.FloatKey;
import org.apache.batik.util.ParsedURL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.SimpleLog;
import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageSessionContext;
import org.w3c.dom.DOMImplementation;
import org.xml.sax.EntityResolver;

public abstract class AbstractFOPTranscoder extends SVGAbstractTranscoder {
   public static final TranscodingHints.Key KEY_DEVICE_RESOLUTION = new FloatKey();
   public static final TranscodingHints.Key KEY_STROKE_TEXT = new BooleanKey();
   public static final TranscodingHints.Key KEY_AUTO_FONTS = new BooleanKey();
   public static final Boolean VALUE_FORMAT_ON;
   public static final Boolean VALUE_FORMAT_OFF;
   protected UserAgent userAgent = this.createUserAgent();
   private Log logger;
   private EntityResolver resolver;
   private Configuration cfg = null;
   private ImageManager imageManager;
   private ImageSessionContext imageSessionContext;

   public AbstractFOPTranscoder() {
      this.hints.put(KEY_DOCUMENT_ELEMENT_NAMESPACE_URI, "http://www.w3.org/2000/svg");
      this.hints.put(KEY_DOCUMENT_ELEMENT, "svg");
      this.hints.put(KEY_DOM_IMPLEMENTATION, SVGDOMImplementation.getDOMImplementation());
   }

   protected UserAgent createUserAgent() {
      return new FOPTranscoderUserAgent();
   }

   public void setLogger(Log logger) {
      this.logger = logger;
   }

   public void setEntityResolver(EntityResolver resolver) {
      this.resolver = resolver;
   }

   public void configure(Configuration cfg) throws ConfigurationException {
      this.cfg = cfg;
   }

   protected boolean getAutoFontsDefault() {
      return true;
   }

   protected Configuration getEffectiveConfiguration() {
      Configuration effCfg = this.cfg;
      if (effCfg == null) {
         boolean autoFonts = this.getAutoFontsDefault();
         if (this.hints.containsKey(KEY_AUTO_FONTS)) {
            autoFonts = (Boolean)this.hints.get(KEY_AUTO_FONTS);
         }

         if (autoFonts) {
            DefaultConfiguration c = new DefaultConfiguration("cfg");
            DefaultConfiguration fonts = new DefaultConfiguration("fonts");
            c.addChild(fonts);
            DefaultConfiguration autodetect = new DefaultConfiguration("auto-detect");
            fonts.addChild(autodetect);
            effCfg = c;
         }
      }

      return (Configuration)effCfg;
   }

   protected final Log getLogger() {
      if (this.logger == null) {
         this.logger = new SimpleLog("FOP/Transcoder");
         ((SimpleLog)this.logger).setLevel(3);
      }

      return this.logger;
   }

   protected DocumentFactory createDocumentFactory(DOMImplementation domImpl, String parserClassname) {
      FOPSAXSVGDocumentFactory factory = new FOPSAXSVGDocumentFactory(parserClassname);
      if (this.resolver != null) {
         factory.setAdditionalEntityResolver(this.resolver);
      }

      return factory;
   }

   protected boolean isTextStroked() {
      boolean stroke = false;
      if (this.hints.containsKey(KEY_STROKE_TEXT)) {
         stroke = (Boolean)this.hints.get(KEY_STROKE_TEXT);
      }

      return stroke;
   }

   protected float getDeviceResolution() {
      return this.hints.containsKey(KEY_DEVICE_RESOLUTION) ? (Float)this.hints.get(KEY_DEVICE_RESOLUTION) : 72.0F;
   }

   protected ImageManager getImageManager() {
      return this.imageManager;
   }

   protected ImageSessionContext getImageSessionContext() {
      return this.imageSessionContext;
   }

   protected void setupImageInfrastructure(final String baseURI) {
      final ImageContext imageContext = new ImageContext() {
         public float getSourceResolution() {
            return 25.4F / AbstractFOPTranscoder.this.userAgent.getPixelUnitToMillimeter();
         }
      };
      this.imageManager = new ImageManager(imageContext);
      this.imageSessionContext = new AbstractImageSessionContext() {
         public ImageContext getParentContext() {
            return imageContext;
         }

         public float getTargetResolution() {
            return AbstractFOPTranscoder.this.getDeviceResolution();
         }

         public Source resolveURI(String uri) {
            System.out.println("resolve " + uri);

            try {
               ParsedURL url = new ParsedURL(baseURI, uri);
               InputStream in = url.openStream();
               StreamSource source = new StreamSource(in, url.toString());
               return source;
            } catch (IOException var5) {
               AbstractFOPTranscoder.this.userAgent.displayError(var5);
               return null;
            }
         }
      };
   }

   static {
      VALUE_FORMAT_ON = Boolean.TRUE;
      VALUE_FORMAT_OFF = Boolean.FALSE;
   }

   protected class FOPTranscoderUserAgent extends SVGAbstractTranscoder.SVGAbstractTranscoderUserAgent {
      protected FOPTranscoderUserAgent() {
         super();
      }

      public void displayError(String message) {
         try {
            AbstractFOPTranscoder.this.getErrorHandler().error(new TranscoderException(message));
         } catch (TranscoderException var3) {
            throw new RuntimeException();
         }
      }

      public void displayError(Exception e) {
         try {
            AbstractFOPTranscoder.this.getErrorHandler().error(new TranscoderException(e));
         } catch (TranscoderException var3) {
            throw new RuntimeException();
         }
      }

      public void displayMessage(String message) {
         AbstractFOPTranscoder.this.getLogger().info(message);
      }

      public float getPixelUnitToMillimeter() {
         Object key = ImageTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER;
         return AbstractFOPTranscoder.this.getTranscodingHints().containsKey(key) ? (Float)AbstractFOPTranscoder.this.getTranscodingHints().get(key) : 0.26458332F;
      }

      public String getMedia() {
         return "print";
      }
   }

   protected class FOPErrorHandler implements ErrorHandler {
      public void error(TranscoderException te) throws TranscoderException {
         AbstractFOPTranscoder.this.getLogger().error(te.getMessage());
      }

      public void fatalError(TranscoderException te) throws TranscoderException {
         throw te;
      }

      public void warning(TranscoderException te) throws TranscoderException {
         AbstractFOPTranscoder.this.getLogger().warn(te.getMessage());
      }
   }
}

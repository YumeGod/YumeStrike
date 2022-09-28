package org.apache.fop.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.ResourceEventProducer;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.render.awt.viewer.Renderable;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class InputHandler implements ErrorListener, Renderable {
   protected File sourcefile;
   private File stylesheet;
   private Vector xsltParams;
   private EntityResolver entityResolver = null;
   private URIResolver uriResolver = null;
   protected Log log;

   public InputHandler(File xmlfile, File xsltfile, Vector params) {
      this.log = LogFactory.getLog(InputHandler.class);
      this.sourcefile = xmlfile;
      this.stylesheet = xsltfile;
      this.xsltParams = params;
   }

   public InputHandler(File fofile) {
      this.log = LogFactory.getLog(InputHandler.class);
      this.sourcefile = fofile;
   }

   public void renderTo(FOUserAgent userAgent, String outputFormat, OutputStream out) throws FOPException {
      FopFactory factory = userAgent.getFactory();
      Fop fop;
      if (out != null) {
         fop = factory.newFop(outputFormat, userAgent, out);
      } else {
         fop = factory.newFop(outputFormat, userAgent);
      }

      if (fop.getUserAgent().getBaseURL() == null && this.sourcefile != null) {
         String baseURL = null;

         try {
            baseURL = (new File(this.sourcefile.getAbsolutePath())).getParentFile().toURI().toURL().toExternalForm();
         } catch (Exception var8) {
            baseURL = "";
         }

         fop.getUserAgent().setBaseURL(baseURL);
      }

      Result res = new SAXResult(fop.getDefaultHandler());
      this.transformTo((Result)res);
   }

   public void renderTo(FOUserAgent userAgent, String outputFormat) throws FOPException {
      this.renderTo(userAgent, outputFormat, (OutputStream)null);
   }

   public void transformTo(OutputStream out) throws FOPException {
      Result res = new StreamResult(out);
      this.transformTo((Result)res);
   }

   protected Source createMainSource() {
      Object in;
      String uri;
      if (this.sourcefile != null) {
         try {
            in = new FileInputStream(this.sourcefile);
            uri = this.sourcefile.toURI().toASCIIString();
         } catch (FileNotFoundException var6) {
            return new StreamSource(this.sourcefile);
         }
      } else {
         in = System.in;
         uri = null;
      }

      Object source;
      try {
         InputSource is = new InputSource((InputStream)in);
         is.setSystemId(uri);
         XMLReader xr = this.getXMLReader();
         if (this.entityResolver != null) {
            xr.setEntityResolver(this.entityResolver);
         }

         source = new SAXSource(xr, is);
      } catch (SAXException var7) {
         if (this.sourcefile != null) {
            source = new StreamSource(this.sourcefile);
         } else {
            source = new StreamSource((InputStream)in, uri);
         }
      } catch (ParserConfigurationException var8) {
         if (this.sourcefile != null) {
            source = new StreamSource(this.sourcefile);
         } else {
            source = new StreamSource((InputStream)in, uri);
         }
      }

      return (Source)source;
   }

   public void createCatalogResolver(FOUserAgent userAgent) {
      String[] classNames = new String[]{"org.apache.xml.resolver.tools.CatalogResolver", "com.sun.org.apache.xml.internal.resolver.tools.CatalogResolver"};
      ResourceEventProducer eventProducer = ResourceEventProducer.Provider.get(userAgent.getEventBroadcaster());
      Class resolverClass = null;

      for(int i = 0; i < classNames.length && resolverClass == null; ++i) {
         try {
            resolverClass = Class.forName(classNames[i]);
         } catch (ClassNotFoundException var9) {
         }
      }

      if (resolverClass == null) {
         eventProducer.catalogResolverNotFound(this);
      } else {
         try {
            this.entityResolver = (EntityResolver)resolverClass.newInstance();
            this.uriResolver = (URIResolver)resolverClass.newInstance();
         } catch (InstantiationException var7) {
            this.log.error("Error creating the catalog resolver: " + var7.getMessage());
            eventProducer.catalogResolverNotCreated(this, var7.getMessage());
         } catch (IllegalAccessException var8) {
            this.log.error("Error creating the catalog resolver: " + var8.getMessage());
            eventProducer.catalogResolverNotCreated(this, var8.getMessage());
         }

      }
   }

   protected Source createXSLTSource() {
      Source xslt = null;
      if (this.stylesheet != null) {
         if (this.entityResolver != null) {
            try {
               InputSource is = new InputSource(this.stylesheet.getPath());
               XMLReader xr = this.getXMLReader();
               xr.setEntityResolver(this.entityResolver);
               xslt = new SAXSource(xr, is);
            } catch (SAXException var4) {
            } catch (ParserConfigurationException var5) {
            }
         }

         if (xslt == null) {
            xslt = new StreamSource(this.stylesheet);
         }
      }

      return (Source)xslt;
   }

   private XMLReader getXMLReader() throws ParserConfigurationException, SAXException {
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setFeature("http://xml.org/sax/features/namespaces", true);
      spf.setFeature("http://apache.org/xml/features/xinclude", true);
      XMLReader xr = spf.newSAXParser().getXMLReader();
      return xr;
   }

   protected void transformTo(Result result) throws FOPException {
      try {
         TransformerFactory factory = TransformerFactory.newInstance();
         Source xsltSource = this.createXSLTSource();
         Transformer transformer;
         if (xsltSource == null) {
            transformer = factory.newTransformer();
         } else {
            transformer = factory.newTransformer(xsltSource);
            if (this.xsltParams != null) {
               for(int i = 0; i < this.xsltParams.size(); i += 2) {
                  transformer.setParameter((String)this.xsltParams.elementAt(i), (String)this.xsltParams.elementAt(i + 1));
               }
            }

            if (this.uriResolver != null) {
               transformer.setURIResolver(this.uriResolver);
            }
         }

         transformer.setErrorListener(this);
         Source src = this.createMainSource();
         transformer.transform(src, result);
      } catch (Exception var6) {
         throw new FOPException(var6);
      }
   }

   public void warning(TransformerException exc) {
      this.log.warn(exc.getLocalizedMessage());
   }

   public void error(TransformerException exc) {
      this.log.error(exc.toString());
   }

   public void fatalError(TransformerException exc) throws TransformerException {
      throw exc;
   }
}

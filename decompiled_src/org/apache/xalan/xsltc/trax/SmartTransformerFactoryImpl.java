package org.apache.xalan.xsltc.trax;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.xml.sax.XMLFilter;

public class SmartTransformerFactoryImpl extends SAXTransformerFactory {
   private static final String CLASS_NAME = "SmartTransformerFactoryImpl";
   private SAXTransformerFactory _xsltcFactory = null;
   private SAXTransformerFactory _xalanFactory = null;
   private SAXTransformerFactory _currFactory = null;
   private ErrorListener _errorlistener = null;
   private URIResolver _uriresolver = null;
   private boolean featureSecureProcessing = false;

   private void createXSLTCTransformerFactory() {
      this._xsltcFactory = new TransformerFactoryImpl();
      this._currFactory = this._xsltcFactory;
   }

   private void createXalanTransformerFactory() {
      String xalanMessage = "org.apache.xalan.xsltc.trax.SmartTransformerFactoryImpl could not create an org.apache.xalan.processor.TransformerFactoryImpl.";

      try {
         Class xalanFactClass = ObjectFactory.findProviderClass("org.apache.xalan.processor.TransformerFactoryImpl", ObjectFactory.findClassLoader(), true);
         this._xalanFactory = (SAXTransformerFactory)xalanFactClass.newInstance();
      } catch (ClassNotFoundException var5) {
         System.err.println("org.apache.xalan.xsltc.trax.SmartTransformerFactoryImpl could not create an org.apache.xalan.processor.TransformerFactoryImpl.");
      } catch (InstantiationException var6) {
         System.err.println("org.apache.xalan.xsltc.trax.SmartTransformerFactoryImpl could not create an org.apache.xalan.processor.TransformerFactoryImpl.");
      } catch (IllegalAccessException var7) {
         System.err.println("org.apache.xalan.xsltc.trax.SmartTransformerFactoryImpl could not create an org.apache.xalan.processor.TransformerFactoryImpl.");
      }

      this._currFactory = this._xalanFactory;
   }

   public void setErrorListener(ErrorListener listener) throws IllegalArgumentException {
      this._errorlistener = listener;
   }

   public ErrorListener getErrorListener() {
      return this._errorlistener;
   }

   public Object getAttribute(String name) throws IllegalArgumentException {
      if (!name.equals("translet-name") && !name.equals("debug")) {
         if (this._xalanFactory == null) {
            this.createXalanTransformerFactory();
         }

         return this._xalanFactory.getAttribute(name);
      } else {
         if (this._xsltcFactory == null) {
            this.createXSLTCTransformerFactory();
         }

         return this._xsltcFactory.getAttribute(name);
      }
   }

   public void setAttribute(String name, Object value) throws IllegalArgumentException {
      if (!name.equals("translet-name") && !name.equals("debug")) {
         if (this._xalanFactory == null) {
            this.createXalanTransformerFactory();
         }

         this._xalanFactory.setAttribute(name, value);
      } else {
         if (this._xsltcFactory == null) {
            this.createXSLTCTransformerFactory();
         }

         this._xsltcFactory.setAttribute(name, value);
      }

   }

   public void setFeature(String name, boolean value) throws TransformerConfigurationException {
      ErrorMsg err;
      if (name == null) {
         err = new ErrorMsg("JAXP_SET_FEATURE_NULL_NAME");
         throw new NullPointerException(err.toString());
      } else if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
         this.featureSecureProcessing = value;
      } else {
         err = new ErrorMsg("JAXP_UNSUPPORTED_FEATURE", name);
         throw new TransformerConfigurationException(err.toString());
      }
   }

   public boolean getFeature(String name) {
      String[] features = new String[]{"http://javax.xml.transform.dom.DOMSource/feature", "http://javax.xml.transform.dom.DOMResult/feature", "http://javax.xml.transform.sax.SAXSource/feature", "http://javax.xml.transform.sax.SAXResult/feature", "http://javax.xml.transform.stream.StreamSource/feature", "http://javax.xml.transform.stream.StreamResult/feature"};
      if (name == null) {
         ErrorMsg err = new ErrorMsg("JAXP_GET_FEATURE_NULL_NAME");
         throw new NullPointerException(err.toString());
      } else {
         for(int i = 0; i < features.length; ++i) {
            if (name.equals(features[i])) {
               return true;
            }
         }

         return name.equals("http://javax.xml.XMLConstants/feature/secure-processing") ? this.featureSecureProcessing : false;
      }
   }

   public URIResolver getURIResolver() {
      return this._uriresolver;
   }

   public void setURIResolver(URIResolver resolver) {
      this._uriresolver = resolver;
   }

   public Source getAssociatedStylesheet(Source source, String media, String title, String charset) throws TransformerConfigurationException {
      if (this._currFactory == null) {
         this.createXSLTCTransformerFactory();
      }

      return this._currFactory.getAssociatedStylesheet(source, media, title, charset);
   }

   public Transformer newTransformer() throws TransformerConfigurationException {
      if (this._xalanFactory == null) {
         this.createXalanTransformerFactory();
      }

      if (this._errorlistener != null) {
         this._xalanFactory.setErrorListener(this._errorlistener);
      }

      if (this._uriresolver != null) {
         this._xalanFactory.setURIResolver(this._uriresolver);
      }

      this._currFactory = this._xalanFactory;
      return this._currFactory.newTransformer();
   }

   public Transformer newTransformer(Source source) throws TransformerConfigurationException {
      if (this._xalanFactory == null) {
         this.createXalanTransformerFactory();
      }

      if (this._errorlistener != null) {
         this._xalanFactory.setErrorListener(this._errorlistener);
      }

      if (this._uriresolver != null) {
         this._xalanFactory.setURIResolver(this._uriresolver);
      }

      this._currFactory = this._xalanFactory;
      return this._currFactory.newTransformer(source);
   }

   public Templates newTemplates(Source source) throws TransformerConfigurationException {
      if (this._xsltcFactory == null) {
         this.createXSLTCTransformerFactory();
      }

      if (this._errorlistener != null) {
         this._xsltcFactory.setErrorListener(this._errorlistener);
      }

      if (this._uriresolver != null) {
         this._xsltcFactory.setURIResolver(this._uriresolver);
      }

      this._currFactory = this._xsltcFactory;
      return this._currFactory.newTemplates(source);
   }

   public TemplatesHandler newTemplatesHandler() throws TransformerConfigurationException {
      if (this._xsltcFactory == null) {
         this.createXSLTCTransformerFactory();
      }

      if (this._errorlistener != null) {
         this._xsltcFactory.setErrorListener(this._errorlistener);
      }

      if (this._uriresolver != null) {
         this._xsltcFactory.setURIResolver(this._uriresolver);
      }

      return this._xsltcFactory.newTemplatesHandler();
   }

   public TransformerHandler newTransformerHandler() throws TransformerConfigurationException {
      if (this._xalanFactory == null) {
         this.createXalanTransformerFactory();
      }

      if (this._errorlistener != null) {
         this._xalanFactory.setErrorListener(this._errorlistener);
      }

      if (this._uriresolver != null) {
         this._xalanFactory.setURIResolver(this._uriresolver);
      }

      return this._xalanFactory.newTransformerHandler();
   }

   public TransformerHandler newTransformerHandler(Source src) throws TransformerConfigurationException {
      if (this._xalanFactory == null) {
         this.createXalanTransformerFactory();
      }

      if (this._errorlistener != null) {
         this._xalanFactory.setErrorListener(this._errorlistener);
      }

      if (this._uriresolver != null) {
         this._xalanFactory.setURIResolver(this._uriresolver);
      }

      return this._xalanFactory.newTransformerHandler(src);
   }

   public TransformerHandler newTransformerHandler(Templates templates) throws TransformerConfigurationException {
      if (this._xsltcFactory == null) {
         this.createXSLTCTransformerFactory();
      }

      if (this._errorlistener != null) {
         this._xsltcFactory.setErrorListener(this._errorlistener);
      }

      if (this._uriresolver != null) {
         this._xsltcFactory.setURIResolver(this._uriresolver);
      }

      return this._xsltcFactory.newTransformerHandler(templates);
   }

   public XMLFilter newXMLFilter(Source src) throws TransformerConfigurationException {
      if (this._xsltcFactory == null) {
         this.createXSLTCTransformerFactory();
      }

      if (this._errorlistener != null) {
         this._xsltcFactory.setErrorListener(this._errorlistener);
      }

      if (this._uriresolver != null) {
         this._xsltcFactory.setURIResolver(this._uriresolver);
      }

      Templates templates = this._xsltcFactory.newTemplates(src);
      return templates == null ? null : this.newXMLFilter(templates);
   }

   public XMLFilter newXMLFilter(Templates templates) throws TransformerConfigurationException {
      try {
         return new TrAXFilter(templates);
      } catch (TransformerConfigurationException var6) {
         TransformerConfigurationException e1 = var6;
         if (this._xsltcFactory == null) {
            this.createXSLTCTransformerFactory();
         }

         ErrorListener errorListener = this._xsltcFactory.getErrorListener();
         if (errorListener != null) {
            try {
               errorListener.fatalError(e1);
               return null;
            } catch (TransformerException var5) {
               new TransformerConfigurationException(var5);
            }
         }

         throw var6;
      }
   }
}

package org.apache.xalan.transformer;

import java.io.IOException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import org.apache.xalan.res.XSLMessages;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

public class TrAXFilter extends XMLFilterImpl {
   private Templates m_templates;
   private TransformerImpl m_transformer;

   public TrAXFilter(Templates templates) throws TransformerConfigurationException {
      this.m_templates = templates;
      this.m_transformer = (TransformerImpl)templates.newTransformer();
   }

   public TransformerImpl getTransformer() {
      return this.m_transformer;
   }

   public void setParent(XMLReader parent) {
      super.setParent(parent);
      if (null != parent.getContentHandler()) {
         this.setContentHandler(parent.getContentHandler());
      }

      this.setupParse();
   }

   public void parse(InputSource input) throws SAXException, IOException {
      if (null == this.getParent()) {
         XMLReader reader = null;

         try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            if (this.m_transformer.getStylesheet().isSecureProcessing()) {
               try {
                  factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
               } catch (SAXException var8) {
               }
            }

            SAXParser jaxpParser = factory.newSAXParser();
            reader = jaxpParser.getXMLReader();
         } catch (ParserConfigurationException var9) {
            throw new SAXException(var9);
         } catch (FactoryConfigurationError var10) {
            throw new SAXException(var10.toString());
         } catch (NoSuchMethodError var11) {
         } catch (AbstractMethodError var12) {
         }

         XMLReader parent;
         if (reader == null) {
            parent = XMLReaderFactory.createXMLReader();
         } else {
            parent = reader;
         }

         try {
            parent.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
         } catch (SAXException var7) {
         }

         this.setParent(parent);
      } else {
         this.setupParse();
      }

      if (null == this.m_transformer.getContentHandler()) {
         throw new SAXException(XSLMessages.createMessage("ER_CANNOT_CALL_PARSE", (Object[])null));
      } else {
         this.getParent().parse(input);
         Exception e = this.m_transformer.getExceptionThrown();
         if (null != e) {
            if (e instanceof SAXException) {
               throw (SAXException)e;
            } else {
               throw new SAXException(e);
            }
         }
      }
   }

   public void parse(String systemId) throws SAXException, IOException {
      this.parse(new InputSource(systemId));
   }

   private void setupParse() {
      XMLReader p = this.getParent();
      if (p == null) {
         throw new NullPointerException(XSLMessages.createMessage("ER_NO_PARENT_FOR_FILTER", (Object[])null));
      } else {
         ContentHandler ch = this.m_transformer.getInputContentHandler();
         p.setContentHandler(ch);
         p.setEntityResolver(this);
         p.setDTDHandler(this);
         p.setErrorHandler(this);
      }
   }

   public void setContentHandler(ContentHandler handler) {
      this.m_transformer.setContentHandler(handler);
   }

   public void setErrorListener(ErrorListener handler) {
      this.m_transformer.setErrorListener(handler);
   }
}

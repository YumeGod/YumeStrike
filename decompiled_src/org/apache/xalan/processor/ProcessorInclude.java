package org.apache.xalan.processor;

import java.io.IOException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.utils.DOM2Helper;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.TreeWalker;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class ProcessorInclude extends XSLTElementProcessor {
   static final long serialVersionUID = -4570078731972673481L;
   private String m_href = null;

   public String getHref() {
      return this.m_href;
   }

   public void setHref(String baseIdent) {
      this.m_href = baseIdent;
   }

   protected int getStylesheetType() {
      return 2;
   }

   protected String getStylesheetInclErr() {
      return "ER_STYLESHEET_INCLUDES_ITSELF";
   }

   public void startElement(StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes) throws SAXException {
      this.setPropertiesFromAttributes(handler, rawName, attributes, this);

      try {
         String hrefUrl = SystemIDResolver.getAbsoluteURI(this.getHref(), handler.getBaseIdentifier());
         if (handler.importStackContains(hrefUrl)) {
            throw new SAXException(XSLMessages.createMessage(this.getStylesheetInclErr(), new Object[]{hrefUrl}));
         }

         handler.pushImportURL(hrefUrl);
         int savedStylesheetType = handler.getStylesheetType();
         handler.setStylesheetType(this.getStylesheetType());
         handler.pushNewNamespaceSupport();

         try {
            this.parse(handler, uri, localName, rawName, attributes);
         } finally {
            handler.setStylesheetType(savedStylesheetType);
            handler.popImportURL();
            handler.popNamespaceSupport();
         }
      } catch (TransformerException var13) {
         handler.error(var13.getMessage(), var13);
      }

   }

   protected void parse(StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes) throws SAXException {
      TransformerFactoryImpl processor = handler.getStylesheetProcessor();
      URIResolver uriresolver = processor.getURIResolver();

      try {
         Source source = null;
         if (null != uriresolver) {
            source = uriresolver.resolve(this.getHref(), handler.getBaseIdentifier());
            if (null != source && source instanceof DOMSource) {
               Node node = ((DOMSource)source).getNode();
               String systemId = ((Source)source).getSystemId();
               if (systemId == null) {
                  systemId = SystemIDResolver.getAbsoluteURI(this.getHref(), handler.getBaseIdentifier());
               }

               TreeWalker walker = new TreeWalker(handler, new DOM2Helper(), systemId);

               try {
                  walker.traverse(node);
                  return;
               } catch (SAXException var26) {
                  throw new TransformerException(var26);
               }
            }
         }

         if (null == source) {
            String absURL = SystemIDResolver.getAbsoluteURI(this.getHref(), handler.getBaseIdentifier());
            source = new StreamSource(absURL);
         }

         Source source = this.processSource(handler, (Source)source);
         XMLReader reader = null;
         if (source instanceof SAXSource) {
            SAXSource saxSource = (SAXSource)source;
            reader = saxSource.getXMLReader();
         }

         InputSource inputSource = SAXSource.sourceToInputSource(source);
         if (null == reader) {
            try {
               SAXParserFactory factory = SAXParserFactory.newInstance();
               factory.setNamespaceAware(true);
               if (handler.getStylesheetProcessor().isSecureProcessing()) {
                  try {
                     factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                  } catch (SAXException var28) {
                  }
               }

               SAXParser jaxpParser = factory.newSAXParser();
               reader = jaxpParser.getXMLReader();
            } catch (ParserConfigurationException var29) {
               throw new SAXException(var29);
            } catch (FactoryConfigurationError var30) {
               throw new SAXException(var30.toString());
            } catch (NoSuchMethodError var31) {
            } catch (AbstractMethodError var32) {
            }
         }

         if (null == reader) {
            reader = XMLReaderFactory.createXMLReader();
         }

         if (null != reader) {
            reader.setContentHandler(handler);
            handler.pushBaseIndentifier(inputSource.getSystemId());

            try {
               reader.parse(inputSource);
            } finally {
               handler.popBaseIndentifier();
            }
         }
      } catch (IOException var33) {
         handler.error("ER_IOEXCEPTION", new Object[]{this.getHref()}, var33);
      } catch (TransformerException var34) {
         handler.error(var34.getMessage(), var34);
      }

   }

   protected Source processSource(StylesheetHandler handler, Source source) {
      return source;
   }
}

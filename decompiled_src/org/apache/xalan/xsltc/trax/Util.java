package org.apache.xalan.xsltc.trax;

import java.io.InputStream;
import java.io.Reader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public final class Util {
   public static String baseName(String name) {
      return org.apache.xalan.xsltc.compiler.util.Util.baseName(name);
   }

   public static String noExtName(String name) {
      return org.apache.xalan.xsltc.compiler.util.Util.noExtName(name);
   }

   public static String toJavaName(String name) {
      return org.apache.xalan.xsltc.compiler.util.Util.toJavaName(name);
   }

   public static InputSource getInputSource(XSLTC xsltc, Source source) throws TransformerConfigurationException {
      InputSource input = null;
      String systemId = source.getSystemId();

      try {
         if (source instanceof SAXSource) {
            SAXSource sax = (SAXSource)source;
            input = sax.getInputSource();

            try {
               XMLReader reader = sax.getXMLReader();
               if (reader == null) {
                  try {
                     reader = XMLReaderFactory.createXMLReader();
                  } catch (Exception var11) {
                     try {
                        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
                        parserFactory.setNamespaceAware(true);
                        if (xsltc.isSecureProcessing()) {
                           try {
                              parserFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                           } catch (SAXException var9) {
                           }
                        }

                        reader = parserFactory.newSAXParser().getXMLReader();
                     } catch (ParserConfigurationException var10) {
                        throw new TransformerConfigurationException("ParserConfigurationException", var10);
                     }
                  }
               }

               reader.setFeature("http://xml.org/sax/features/namespaces", true);
               reader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
               xsltc.setXMLReader(reader);
            } catch (SAXNotRecognizedException var12) {
               throw new TransformerConfigurationException("SAXNotRecognizedException ", var12);
            } catch (SAXNotSupportedException var13) {
               throw new TransformerConfigurationException("SAXNotSupportedException ", var13);
            } catch (SAXException var14) {
               throw new TransformerConfigurationException("SAXException ", var14);
            }
         } else if (source instanceof DOMSource) {
            DOMSource domsrc = (DOMSource)source;
            Document dom = (Document)domsrc.getNode();
            DOM2SAX dom2sax = new DOM2SAX(dom);
            xsltc.setXMLReader(dom2sax);
            input = SAXSource.sourceToInputSource(source);
            if (input == null) {
               input = new InputSource(domsrc.getSystemId());
            }
         } else {
            if (!(source instanceof StreamSource)) {
               ErrorMsg err = new ErrorMsg("JAXP_UNKNOWN_SOURCE_ERR");
               throw new TransformerConfigurationException(err.toString());
            }

            StreamSource stream = (StreamSource)source;
            InputStream istream = stream.getInputStream();
            Reader reader = stream.getReader();
            xsltc.setXMLReader((XMLReader)null);
            if (istream != null) {
               input = new InputSource(istream);
            } else if (reader != null) {
               input = new InputSource(reader);
            } else {
               input = new InputSource(systemId);
            }
         }

         input.setSystemId(systemId);
         return input;
      } catch (NullPointerException var15) {
         ErrorMsg err = new ErrorMsg("JAXP_NO_SOURCE_ERR", "TransformerFactory.newTemplates()");
         throw new TransformerConfigurationException(err.toString());
      } catch (SecurityException var16) {
         ErrorMsg err = new ErrorMsg("FILE_ACCESS_ERR", systemId);
         throw new TransformerConfigurationException(err.toString());
      }
   }
}

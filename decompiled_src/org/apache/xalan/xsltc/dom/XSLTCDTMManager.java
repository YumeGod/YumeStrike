package org.apache.xalan.xsltc.dom;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.xsltc.trax.DOM2SAX;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMException;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMManagerDefault;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLStringFactory;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class XSLTCDTMManager extends DTMManagerDefault {
   private static final String DEFAULT_CLASS_NAME = "org.apache.xalan.xsltc.dom.XSLTCDTMManager";
   private static final String DEFAULT_PROP_NAME = "org.apache.xalan.xsltc.dom.XSLTCDTMManager";
   private static final boolean DUMPTREE = false;
   private static final boolean DEBUG = false;
   // $FF: synthetic field
   static Class class$org$apache$xalan$xsltc$dom$XSLTCDTMManager;

   public static XSLTCDTMManager newInstance() {
      return new XSLTCDTMManager();
   }

   public static Class getDTMManagerClass() {
      Class mgrClass = ObjectFactory.lookUpFactoryClass("org.apache.xalan.xsltc.dom.XSLTCDTMManager", (String)null, "org.apache.xalan.xsltc.dom.XSLTCDTMManager");
      return mgrClass != null ? mgrClass : (class$org$apache$xalan$xsltc$dom$XSLTCDTMManager == null ? (class$org$apache$xalan$xsltc$dom$XSLTCDTMManager = class$("org.apache.xalan.xsltc.dom.XSLTCDTMManager")) : class$org$apache$xalan$xsltc$dom$XSLTCDTMManager);
   }

   public DTM getDTM(Source source, boolean unique, DTMWSFilter whiteSpaceFilter, boolean incremental, boolean doIndexing) {
      return this.getDTM(source, unique, whiteSpaceFilter, incremental, doIndexing, false, 0, true, false);
   }

   public DTM getDTM(Source source, boolean unique, DTMWSFilter whiteSpaceFilter, boolean incremental, boolean doIndexing, boolean buildIdIndex) {
      return this.getDTM(source, unique, whiteSpaceFilter, incremental, doIndexing, false, 0, buildIdIndex, false);
   }

   public DTM getDTM(Source source, boolean unique, DTMWSFilter whiteSpaceFilter, boolean incremental, boolean doIndexing, boolean buildIdIndex, boolean newNameTable) {
      return this.getDTM(source, unique, whiteSpaceFilter, incremental, doIndexing, false, 0, buildIdIndex, newNameTable);
   }

   public DTM getDTM(Source source, boolean unique, DTMWSFilter whiteSpaceFilter, boolean incremental, boolean doIndexing, boolean hasUserReader, int size, boolean buildIdIndex) {
      return this.getDTM(source, unique, whiteSpaceFilter, incremental, doIndexing, hasUserReader, size, buildIdIndex, false);
   }

   public DTM getDTM(Source source, boolean unique, DTMWSFilter whiteSpaceFilter, boolean incremental, boolean doIndexing, boolean hasUserReader, int size, boolean buildIdIndex, boolean newNameTable) {
      int dtmPos = this.getFirstFreeDTMID();
      int documentID = dtmPos << 16;
      if (null != source && source instanceof DOMSource) {
         DOMSource domsrc = (DOMSource)source;
         Node node = domsrc.getNode();
         DOM2SAX dom2sax = new DOM2SAX(node);
         SAXImpl dtm;
         if (size <= 0) {
            dtm = new SAXImpl(this, source, documentID, whiteSpaceFilter, (XMLStringFactory)null, doIndexing, 512, buildIdIndex, newNameTable);
         } else {
            dtm = new SAXImpl(this, source, documentID, whiteSpaceFilter, (XMLStringFactory)null, doIndexing, size, buildIdIndex, newNameTable);
         }

         dtm.setDocumentURI(source.getSystemId());
         this.addDTM(dtm, dtmPos, 0);
         dom2sax.setContentHandler(dtm);

         try {
            dom2sax.parse();
            return dtm;
         } catch (RuntimeException var29) {
            throw var29;
         } catch (Exception var30) {
            throw new WrappedRuntimeException(var30);
         }
      } else {
         boolean isSAXSource = null != source ? source instanceof SAXSource : true;
         boolean isStreamSource = null != source ? source instanceof StreamSource : false;
         if (!isSAXSource && !isStreamSource) {
            throw new DTMException(XMLMessages.createXMLMessage("ER_NOT_SUPPORTED", new Object[]{source}));
         } else {
            XMLReader reader;
            InputSource xmlSource;
            if (null == source) {
               xmlSource = null;
               reader = null;
               hasUserReader = false;
            } else {
               reader = this.getXMLReader(source);
               xmlSource = SAXSource.sourceToInputSource(source);
               String urlOfSource = xmlSource.getSystemId();
               if (null != urlOfSource) {
                  try {
                     urlOfSource = SystemIDResolver.getAbsoluteURI(urlOfSource);
                  } catch (Exception var35) {
                     System.err.println("Can not absolutize URL: " + urlOfSource);
                  }

                  xmlSource.setSystemId(urlOfSource);
               }
            }

            SAXImpl dtm;
            if (size <= 0) {
               dtm = new SAXImpl(this, source, documentID, whiteSpaceFilter, (XMLStringFactory)null, doIndexing, 512, buildIdIndex, newNameTable);
            } else {
               dtm = new SAXImpl(this, source, documentID, whiteSpaceFilter, (XMLStringFactory)null, doIndexing, size, buildIdIndex, newNameTable);
            }

            this.addDTM(dtm, dtmPos, 0);
            if (null == reader) {
               return dtm;
            } else {
               reader.setContentHandler(dtm.getBuilder());
               if (!hasUserReader || null == reader.getDTDHandler()) {
                  reader.setDTDHandler(dtm);
               }

               if (!hasUserReader || null == reader.getErrorHandler()) {
                  reader.setErrorHandler(dtm);
               }

               try {
                  reader.setProperty("http://xml.org/sax/properties/lexical-handler", dtm);
               } catch (SAXNotRecognizedException var33) {
               } catch (SAXNotSupportedException var34) {
               }

               try {
                  reader.parse(xmlSource);
               } catch (RuntimeException var31) {
                  throw var31;
               } catch (Exception var32) {
                  throw new WrappedRuntimeException(var32);
               } finally {
                  if (!hasUserReader) {
                     this.releaseXMLReader(reader);
                  }

               }

               return dtm;
            }
         }
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}

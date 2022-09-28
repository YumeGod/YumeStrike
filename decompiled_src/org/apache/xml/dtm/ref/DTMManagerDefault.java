package org.apache.xml.dtm.ref;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMException;
import org.apache.xml.dtm.DTMFilter;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.dom2dtm.DOM2DTM;
import org.apache.xml.dtm.ref.dom2dtm.DOM2DTMdefaultNamespaceDeclarationNode;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM;
import org.apache.xml.dtm.ref.sax2dtm.SAX2RTFDTM;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLReaderManager;
import org.apache.xml.utils.XMLStringFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class DTMManagerDefault extends DTMManager {
   private static final boolean DUMPTREE = false;
   private static final boolean DEBUG = false;
   protected DTM[] m_dtms = new DTM[256];
   int[] m_dtm_offsets = new int[256];
   protected XMLReaderManager m_readerManager = null;
   protected DefaultHandler m_defaultHandler = new DefaultHandler();
   private ExpandedNameTable m_expandedNameTable = new ExpandedNameTable();

   public synchronized void addDTM(DTM dtm, int id) {
      this.addDTM(dtm, id, 0);
   }

   public synchronized void addDTM(DTM dtm, int id, int offset) {
      if (id >= 65536) {
         throw new DTMException(XMLMessages.createXMLMessage("ER_NO_DTMIDS_AVAIL", (Object[])null));
      } else {
         int oldlen = this.m_dtms.length;
         if (oldlen <= id) {
            int newlen = Math.min(id + 256, 65536);
            DTM[] new_m_dtms = new DTM[newlen];
            System.arraycopy(this.m_dtms, 0, new_m_dtms, 0, oldlen);
            this.m_dtms = new_m_dtms;
            int[] new_m_dtm_offsets = new int[newlen];
            System.arraycopy(this.m_dtm_offsets, 0, new_m_dtm_offsets, 0, oldlen);
            this.m_dtm_offsets = new_m_dtm_offsets;
         }

         this.m_dtms[id] = dtm;
         this.m_dtm_offsets[id] = offset;
         dtm.documentRegistration();
      }
   }

   public synchronized int getFirstFreeDTMID() {
      int n = this.m_dtms.length;

      for(int i = 1; i < n; ++i) {
         if (null == this.m_dtms[i]) {
            return i;
         }
      }

      return n;
   }

   public synchronized DTM getDTM(Source source, boolean unique, DTMWSFilter whiteSpaceFilter, boolean incremental, boolean doIndexing) {
      XMLStringFactory xstringFactory = super.m_xsf;
      int dtmPos = this.getFirstFreeDTMID();
      int documentID = dtmPos << 16;
      if (null != source && source instanceof DOMSource) {
         DOM2DTM dtm = new DOM2DTM(this, (DOMSource)source, documentID, whiteSpaceFilter, xstringFactory, doIndexing);
         this.addDTM(dtm, dtmPos, 0);
         return dtm;
      } else {
         boolean isSAXSource = null != source ? source instanceof SAXSource : true;
         boolean isStreamSource = null != source ? source instanceof StreamSource : false;
         if (!isSAXSource && !isStreamSource) {
            throw new DTMException(XMLMessages.createXMLMessage("ER_NOT_SUPPORTED", new Object[]{source}));
         } else {
            XMLReader reader = null;

            Object coParser;
            try {
               InputSource xmlSource;
               if (null == source) {
                  xmlSource = null;
               } else {
                  reader = this.getXMLReader(source);
                  xmlSource = SAXSource.sourceToInputSource(source);
                  String urlOfSource = xmlSource.getSystemId();
                  if (null != urlOfSource) {
                     try {
                        urlOfSource = SystemIDResolver.getAbsoluteURI(urlOfSource);
                     } catch (Exception var42) {
                        System.err.println("Can not absolutize URL: " + urlOfSource);
                     }

                     xmlSource.setSystemId(urlOfSource);
                  }
               }

               Object dtm;
               if (source == null && unique && !incremental && !doIndexing) {
                  dtm = new SAX2RTFDTM(this, source, documentID, whiteSpaceFilter, xstringFactory, doIndexing);
               } else {
                  dtm = new SAX2DTM(this, source, documentID, whiteSpaceFilter, xstringFactory, doIndexing);
               }

               this.addDTM((DTM)dtm, dtmPos, 0);
               boolean haveXercesParser = null != reader && reader.getClass().getName().equals("org.apache.xerces.parsers.SAXParser");
               if (haveXercesParser) {
                  incremental = true;
               }

               if (super.m_incremental && incremental) {
                  coParser = null;
                  if (haveXercesParser) {
                     try {
                        coParser = (IncrementalSAXSource)Class.forName("org.apache.xml.dtm.ref.IncrementalSAXSource_Xerces").newInstance();
                     } catch (Exception var41) {
                        var41.printStackTrace();
                        coParser = null;
                     }
                  }

                  if (coParser == null) {
                     if (null == reader) {
                        coParser = new IncrementalSAXSource_Filter();
                     } else {
                        IncrementalSAXSource_Filter filter = new IncrementalSAXSource_Filter();
                        filter.setXMLReader(reader);
                        coParser = filter;
                     }
                  }

                  ((SAX2DTM)dtm).setIncrementalSAXSource((IncrementalSAXSource)coParser);
                  if (null == xmlSource) {
                     Object var46 = dtm;
                     return (DTM)var46;
                  }

                  if (null == reader.getErrorHandler()) {
                     reader.setErrorHandler((ErrorHandler)dtm);
                  }

                  reader.setDTDHandler((DTDHandler)dtm);

                  try {
                     ((IncrementalSAXSource)coParser).startParse(xmlSource);
                  } catch (RuntimeException var39) {
                     ((SAX2DTM)dtm).clearCoRoutine();
                     throw var39;
                  } catch (Exception var40) {
                     ((SAX2DTM)dtm).clearCoRoutine();
                     throw new WrappedRuntimeException(var40);
                  }
               } else {
                  if (null == reader) {
                     coParser = dtm;
                     return (DTM)coParser;
                  }

                  reader.setContentHandler((ContentHandler)dtm);
                  reader.setDTDHandler((DTDHandler)dtm);
                  if (null == reader.getErrorHandler()) {
                     reader.setErrorHandler((ErrorHandler)dtm);
                  }

                  try {
                     reader.setProperty("http://xml.org/sax/properties/lexical-handler", dtm);
                  } catch (SAXNotRecognizedException var37) {
                  } catch (SAXNotSupportedException var38) {
                  }

                  try {
                     reader.parse(xmlSource);
                  } catch (RuntimeException var35) {
                     ((SAX2DTM)dtm).clearCoRoutine();
                     throw var35;
                  } catch (Exception var36) {
                     ((SAX2DTM)dtm).clearCoRoutine();
                     throw new WrappedRuntimeException(var36);
                  }
               }

               coParser = dtm;
            } finally {
               if (reader != null && (!super.m_incremental || !incremental)) {
                  reader.setContentHandler(this.m_defaultHandler);
                  reader.setDTDHandler(this.m_defaultHandler);
                  reader.setErrorHandler(this.m_defaultHandler);

                  try {
                     reader.setProperty("http://xml.org/sax/properties/lexical-handler", (Object)null);
                  } catch (Exception var34) {
                  }
               }

               this.releaseXMLReader(reader);
            }

            return (DTM)coParser;
         }
      }
   }

   public synchronized int getDTMHandleFromNode(Node node) {
      if (null == node) {
         throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_NODE_NON_NULL", (Object[])null));
      } else if (node instanceof DTMNodeProxy) {
         return ((DTMNodeProxy)node).getDTMNodeNumber();
      } else {
         int max = this.m_dtms.length;

         for(int i = 0; i < max; ++i) {
            DTM thisDTM = this.m_dtms[i];
            if (null != thisDTM && thisDTM instanceof DOM2DTM) {
               int handle = ((DOM2DTM)thisDTM).getHandleOfNode(node);
               if (handle != -1) {
                  return handle;
               }
            }
         }

         Node root = node;

         for(Node p = node.getNodeType() == 2 ? ((Attr)node).getOwnerElement() : node.getParentNode(); p != null; p = ((Node)p).getParentNode()) {
            root = p;
         }

         DOM2DTM dtm = (DOM2DTM)this.getDTM(new DOMSource((Node)root), false, (DTMWSFilter)null, true, true);
         int handle;
         if (node instanceof DOM2DTMdefaultNamespaceDeclarationNode) {
            handle = dtm.getHandleOfNode(((Attr)node).getOwnerElement());
            handle = dtm.getAttributeNode(handle, node.getNamespaceURI(), node.getLocalName());
         } else {
            handle = dtm.getHandleOfNode(node);
         }

         if (-1 == handle) {
            throw new RuntimeException(XMLMessages.createXMLMessage("ER_COULD_NOT_RESOLVE_NODE", (Object[])null));
         } else {
            return handle;
         }
      }
   }

   public synchronized XMLReader getXMLReader(Source inputSource) {
      try {
         XMLReader reader = inputSource instanceof SAXSource ? ((SAXSource)inputSource).getXMLReader() : null;
         if (null == reader) {
            if (this.m_readerManager == null) {
               this.m_readerManager = XMLReaderManager.getInstance();
            }

            reader = this.m_readerManager.getXMLReader();
         }

         return reader;
      } catch (SAXException var3) {
         throw new DTMException(var3.getMessage(), var3);
      }
   }

   public synchronized void releaseXMLReader(XMLReader reader) {
      if (this.m_readerManager != null) {
         this.m_readerManager.releaseXMLReader(reader);
      }

   }

   public synchronized DTM getDTM(int nodeHandle) {
      try {
         return this.m_dtms[nodeHandle >>> 16];
      } catch (ArrayIndexOutOfBoundsException var3) {
         if (nodeHandle == -1) {
            return null;
         } else {
            throw var3;
         }
      }
   }

   public synchronized int getDTMIdentity(DTM dtm) {
      if (dtm instanceof DTMDefaultBase) {
         DTMDefaultBase dtmdb = (DTMDefaultBase)dtm;
         return dtmdb.getManager() == this ? dtmdb.getDTMIDs().elementAt(0) : -1;
      } else {
         int n = this.m_dtms.length;

         for(int i = 0; i < n; ++i) {
            DTM tdtm = this.m_dtms[i];
            if (tdtm == dtm && this.m_dtm_offsets[i] == 0) {
               return i << 16;
            }
         }

         return -1;
      }
   }

   public synchronized boolean release(DTM dtm, boolean shouldHardDelete) {
      if (dtm instanceof SAX2DTM) {
         ((SAX2DTM)dtm).clearCoRoutine();
      }

      if (dtm instanceof DTMDefaultBase) {
         SuballocatedIntVector ids = ((DTMDefaultBase)dtm).getDTMIDs();

         for(int i = ids.size() - 1; i >= 0; --i) {
            this.m_dtms[ids.elementAt(i) >>> 16] = null;
         }
      } else {
         int i = this.getDTMIdentity(dtm);
         if (i >= 0) {
            this.m_dtms[i >>> 16] = null;
         }
      }

      dtm.documentRelease();
      return true;
   }

   public synchronized DTM createDocumentFragment() {
      try {
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         dbf.setNamespaceAware(true);
         DocumentBuilder db = dbf.newDocumentBuilder();
         Document doc = db.newDocument();
         Node df = doc.createDocumentFragment();
         return this.getDTM(new DOMSource(df), true, (DTMWSFilter)null, false, false);
      } catch (Exception var5) {
         throw new DTMException(var5);
      }
   }

   public synchronized DTMIterator createDTMIterator(int whatToShow, DTMFilter filter, boolean entityReferenceExpansion) {
      return null;
   }

   public synchronized DTMIterator createDTMIterator(String xpathString, PrefixResolver presolver) {
      return null;
   }

   public synchronized DTMIterator createDTMIterator(int node) {
      return null;
   }

   public synchronized DTMIterator createDTMIterator(Object xpathCompiler, int pos) {
      return null;
   }

   public ExpandedNameTable getExpandedNameTable(DTM dtm) {
      return this.m_expandedNameTable;
   }
}

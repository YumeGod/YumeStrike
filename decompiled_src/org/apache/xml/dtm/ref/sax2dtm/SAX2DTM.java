package org.apache.xml.dtm.ref.sax2dtm;

import java.util.Hashtable;
import java.util.Vector;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators;
import org.apache.xml.dtm.ref.DTMManagerDefault;
import org.apache.xml.dtm.ref.DTMStringPool;
import org.apache.xml.dtm.ref.DTMTreeWalker;
import org.apache.xml.dtm.ref.IncrementalSAXSource;
import org.apache.xml.dtm.ref.IncrementalSAXSource_Filter;
import org.apache.xml.dtm.ref.NodeLocator;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.IntVector;
import org.apache.xml.utils.StringVector;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public class SAX2DTM extends DTMDefaultBaseIterators implements EntityResolver, DTDHandler, ContentHandler, ErrorHandler, DeclHandler, LexicalHandler {
   private static final boolean DEBUG = false;
   private IncrementalSAXSource m_incrementalSAXSource;
   protected FastStringBuffer m_chars;
   protected SuballocatedIntVector m_data;
   protected transient IntStack m_parents;
   protected transient int m_previous;
   protected transient Vector m_prefixMappings;
   protected transient IntStack m_contextIndexes;
   protected transient int m_textType;
   protected transient int m_coalescedTextType;
   protected transient Locator m_locator;
   private transient String m_systemId;
   protected transient boolean m_insideDTD;
   protected DTMTreeWalker m_walker;
   protected DTMStringPool m_valuesOrPrefixes;
   protected boolean m_endDocumentOccured;
   protected SuballocatedIntVector m_dataOrQName;
   protected Hashtable m_idAttributes;
   private static final String[] m_fixednames = new String[]{null, null, null, "#text", "#cdata_section", null, null, null, "#comment", "#document", null, "#document-fragment", null};
   private Vector m_entities;
   private static final int ENTITY_FIELD_PUBLICID = 0;
   private static final int ENTITY_FIELD_SYSTEMID = 1;
   private static final int ENTITY_FIELD_NOTATIONNAME = 2;
   private static final int ENTITY_FIELD_NAME = 3;
   private static final int ENTITY_FIELDS_PER = 4;
   protected int m_textPendingStart;
   protected boolean m_useSourceLocationProperty;
   protected StringVector m_sourceSystemId;
   protected IntVector m_sourceLine;
   protected IntVector m_sourceColumn;
   boolean m_pastFirstElement;

   public SAX2DTM(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing) {
      this(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, 512, true, false);
   }

   public SAX2DTM(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean usePrevsib, boolean newNameTable) {
      super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, blocksize, usePrevsib, newNameTable);
      this.m_incrementalSAXSource = null;
      this.m_previous = 0;
      this.m_prefixMappings = new Vector();
      this.m_textType = 3;
      this.m_coalescedTextType = 3;
      this.m_locator = null;
      this.m_systemId = null;
      this.m_insideDTD = false;
      this.m_walker = new DTMTreeWalker();
      this.m_endDocumentOccured = false;
      this.m_idAttributes = new Hashtable();
      this.m_entities = null;
      this.m_textPendingStart = -1;
      this.m_useSourceLocationProperty = false;
      this.m_pastFirstElement = false;
      if (blocksize <= 64) {
         this.m_data = new SuballocatedIntVector(blocksize, 4);
         this.m_dataOrQName = new SuballocatedIntVector(blocksize, 4);
         this.m_valuesOrPrefixes = new DTMStringPool(16);
         this.m_chars = new FastStringBuffer(7, 10);
         this.m_contextIndexes = new IntStack(4);
         this.m_parents = new IntStack(4);
      } else {
         this.m_data = new SuballocatedIntVector(blocksize, 32);
         this.m_dataOrQName = new SuballocatedIntVector(blocksize, 32);
         this.m_valuesOrPrefixes = new DTMStringPool();
         this.m_chars = new FastStringBuffer(10, 13);
         this.m_contextIndexes = new IntStack();
         this.m_parents = new IntStack();
      }

      this.m_data.addElement(0);
      this.m_useSourceLocationProperty = mgr.getSource_location();
      this.m_sourceSystemId = this.m_useSourceLocationProperty ? new StringVector() : null;
      this.m_sourceLine = this.m_useSourceLocationProperty ? new IntVector() : null;
      this.m_sourceColumn = this.m_useSourceLocationProperty ? new IntVector() : null;
   }

   public void setUseSourceLocation(boolean useSourceLocation) {
      this.m_useSourceLocationProperty = useSourceLocation;
   }

   protected int _dataOrQName(int identity) {
      if (identity < super.m_size) {
         return this.m_dataOrQName.elementAt(identity);
      } else {
         do {
            boolean isMore = this.nextNode();
            if (!isMore) {
               return -1;
            }
         } while(identity >= super.m_size);

         return this.m_dataOrQName.elementAt(identity);
      }
   }

   public void clearCoRoutine() {
      this.clearCoRoutine(true);
   }

   public void clearCoRoutine(boolean callDoTerminate) {
      if (null != this.m_incrementalSAXSource) {
         if (callDoTerminate) {
            this.m_incrementalSAXSource.deliverMoreNodes(false);
         }

         this.m_incrementalSAXSource = null;
      }

   }

   public void setIncrementalSAXSource(IncrementalSAXSource incrementalSAXSource) {
      this.m_incrementalSAXSource = incrementalSAXSource;
      incrementalSAXSource.setContentHandler(this);
      incrementalSAXSource.setLexicalHandler(this);
      incrementalSAXSource.setDTDHandler(this);
   }

   public ContentHandler getContentHandler() {
      return (ContentHandler)(this.m_incrementalSAXSource instanceof IncrementalSAXSource_Filter ? (ContentHandler)this.m_incrementalSAXSource : this);
   }

   public LexicalHandler getLexicalHandler() {
      return (LexicalHandler)(this.m_incrementalSAXSource instanceof IncrementalSAXSource_Filter ? (LexicalHandler)this.m_incrementalSAXSource : this);
   }

   public EntityResolver getEntityResolver() {
      return this;
   }

   public DTDHandler getDTDHandler() {
      return this;
   }

   public ErrorHandler getErrorHandler() {
      return this;
   }

   public DeclHandler getDeclHandler() {
      return this;
   }

   public boolean needsTwoThreads() {
      return null != this.m_incrementalSAXSource;
   }

   public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize) throws SAXException {
      int identity = this.makeNodeIdentity(nodeHandle);
      if (identity != -1) {
         int type = this._type(identity);
         int firstChild;
         int offset;
         int length;
         if (this.isTextType(type)) {
            firstChild = this.m_dataOrQName.elementAt(identity);
            offset = this.m_data.elementAt(firstChild);
            length = this.m_data.elementAt(firstChild + 1);
            if (normalize) {
               this.m_chars.sendNormalizedSAXcharacters(ch, offset, length);
            } else {
               this.m_chars.sendSAXcharacters(ch, offset, length);
            }
         } else {
            firstChild = this._firstch(identity);
            if (-1 != firstChild) {
               offset = -1;
               length = 0;
               int startNode = identity;
               identity = firstChild;

               do {
                  type = this._type(identity);
                  if (this.isTextType(type)) {
                     int dataIndex = this._dataOrQName(identity);
                     if (-1 == offset) {
                        offset = this.m_data.elementAt(dataIndex);
                     }

                     length += this.m_data.elementAt(dataIndex + 1);
                  }

                  identity = this.getNextNodeIdentity(identity);
               } while(-1 != identity && this._parent(identity) >= startNode);

               if (length > 0) {
                  if (normalize) {
                     this.m_chars.sendNormalizedSAXcharacters(ch, offset, length);
                  } else {
                     this.m_chars.sendSAXcharacters(ch, offset, length);
                  }
               }
            } else if (type != 1) {
               offset = this._dataOrQName(identity);
               if (offset < 0) {
                  offset = -offset;
                  offset = this.m_data.elementAt(offset + 1);
               }

               String str = this.m_valuesOrPrefixes.indexToString(offset);
               if (normalize) {
                  FastStringBuffer.sendNormalizedSAXcharacters(str.toCharArray(), 0, str.length(), ch);
               } else {
                  ch.characters(str.toCharArray(), 0, str.length());
               }
            }
         }

      }
   }

   public String getNodeName(int nodeHandle) {
      int expandedTypeID = this.getExpandedTypeID(nodeHandle);
      int namespaceID = super.m_expandedNameTable.getNamespaceID(expandedTypeID);
      if (0 == namespaceID) {
         int type = this.getNodeType(nodeHandle);
         if (type == 13) {
            return null == super.m_expandedNameTable.getLocalName(expandedTypeID) ? "xmlns" : "xmlns:" + super.m_expandedNameTable.getLocalName(expandedTypeID);
         } else {
            return 0 == super.m_expandedNameTable.getLocalNameID(expandedTypeID) ? m_fixednames[type] : super.m_expandedNameTable.getLocalName(expandedTypeID);
         }
      } else {
         int qnameIndex = this.m_dataOrQName.elementAt(this.makeNodeIdentity(nodeHandle));
         if (qnameIndex < 0) {
            qnameIndex = -qnameIndex;
            qnameIndex = this.m_data.elementAt(qnameIndex);
         }

         return this.m_valuesOrPrefixes.indexToString(qnameIndex);
      }
   }

   public String getNodeNameX(int nodeHandle) {
      int expandedTypeID = this.getExpandedTypeID(nodeHandle);
      int namespaceID = super.m_expandedNameTable.getNamespaceID(expandedTypeID);
      if (0 == namespaceID) {
         String name = super.m_expandedNameTable.getLocalName(expandedTypeID);
         return name == null ? "" : name;
      } else {
         int qnameIndex = this.m_dataOrQName.elementAt(this.makeNodeIdentity(nodeHandle));
         if (qnameIndex < 0) {
            qnameIndex = -qnameIndex;
            qnameIndex = this.m_data.elementAt(qnameIndex);
         }

         return this.m_valuesOrPrefixes.indexToString(qnameIndex);
      }
   }

   public boolean isAttributeSpecified(int attributeHandle) {
      return true;
   }

   public String getDocumentTypeDeclarationSystemIdentifier() {
      this.error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", (Object[])null));
      return null;
   }

   protected int getNextNodeIdentity(int identity) {
      ++identity;

      while(identity >= super.m_size) {
         if (null == this.m_incrementalSAXSource) {
            return -1;
         }

         this.nextNode();
      }

      return identity;
   }

   public void dispatchToEvents(int nodeHandle, ContentHandler ch) throws SAXException {
      DTMTreeWalker treeWalker = this.m_walker;
      ContentHandler prevCH = treeWalker.getcontentHandler();
      if (null != prevCH) {
         treeWalker = new DTMTreeWalker();
      }

      treeWalker.setcontentHandler(ch);
      treeWalker.setDTM(this);

      try {
         treeWalker.traverse(nodeHandle);
      } finally {
         treeWalker.setcontentHandler((ContentHandler)null);
      }

   }

   public int getNumberOfNodes() {
      return super.m_size;
   }

   protected boolean nextNode() {
      if (null == this.m_incrementalSAXSource) {
         return false;
      } else if (this.m_endDocumentOccured) {
         this.clearCoRoutine();
         return false;
      } else {
         Object gotMore = this.m_incrementalSAXSource.deliverMoreNodes(true);
         if (!(gotMore instanceof Boolean)) {
            if (gotMore instanceof RuntimeException) {
               throw (RuntimeException)gotMore;
            } else if (gotMore instanceof Exception) {
               throw new WrappedRuntimeException((Exception)gotMore);
            } else {
               this.clearCoRoutine();
               return false;
            }
         } else {
            if (gotMore != Boolean.TRUE) {
               this.clearCoRoutine();
            }

            return true;
         }
      }
   }

   private final boolean isTextType(int type) {
      return 3 == type || 4 == type;
   }

   protected int addNode(int type, int expandedTypeID, int parentIndex, int previousSibling, int dataOrPrefix, boolean canHaveFirstChild) {
      int nodeIndex = super.m_size++;
      if (super.m_dtmIdent.size() == nodeIndex >>> 16) {
         this.addNewDTMID(nodeIndex);
      }

      super.m_firstch.addElement(canHaveFirstChild ? -2 : -1);
      super.m_nextsib.addElement(-2);
      super.m_parent.addElement(parentIndex);
      super.m_exptype.addElement(expandedTypeID);
      this.m_dataOrQName.addElement(dataOrPrefix);
      if (super.m_prevsib != null) {
         super.m_prevsib.addElement(previousSibling);
      }

      if (-1 != previousSibling) {
         super.m_nextsib.setElementAt(nodeIndex, previousSibling);
      }

      if (this.m_locator != null && this.m_useSourceLocationProperty) {
         this.setSourceLocation();
      }

      switch (type) {
         case 2:
            break;
         case 13:
            this.declareNamespaceInContext(parentIndex, nodeIndex);
            break;
         default:
            if (-1 == previousSibling && -1 != parentIndex) {
               super.m_firstch.setElementAt(nodeIndex, parentIndex);
            }
      }

      return nodeIndex;
   }

   protected void addNewDTMID(int nodeIndex) {
      try {
         if (super.m_mgr == null) {
            throw new ClassCastException();
         }

         DTMManagerDefault mgrD = (DTMManagerDefault)super.m_mgr;
         int id = mgrD.getFirstFreeDTMID();
         mgrD.addDTM(this, id, nodeIndex);
         super.m_dtmIdent.addElement(id << 16);
      } catch (ClassCastException var4) {
         this.error(XMLMessages.createXMLMessage("ER_NO_DTMIDS_AVAIL", (Object[])null));
      }

   }

   public void migrateTo(DTMManager manager) {
      super.migrateTo(manager);
      int numDTMs = super.m_dtmIdent.size();
      int dtmId = super.m_mgrDefault.getFirstFreeDTMID();
      int nodeIndex = 0;

      for(int i = 0; i < numDTMs; ++i) {
         super.m_dtmIdent.setElementAt(dtmId << 16, i);
         super.m_mgrDefault.addDTM(this, dtmId, nodeIndex);
         ++dtmId;
         nodeIndex += 65536;
      }

   }

   protected void setSourceLocation() {
      this.m_sourceSystemId.addElement(this.m_locator.getSystemId());
      this.m_sourceLine.addElement(this.m_locator.getLineNumber());
      this.m_sourceColumn.addElement(this.m_locator.getColumnNumber());
      if (this.m_sourceSystemId.size() != super.m_size) {
         String msg = "CODING ERROR in Source Location: " + super.m_size + " != " + this.m_sourceSystemId.size();
         System.err.println(msg);
         throw new RuntimeException(msg);
      }
   }

   public String getNodeValue(int nodeHandle) {
      int identity = this.makeNodeIdentity(nodeHandle);
      int type = this._type(identity);
      int dataIndex;
      if (this.isTextType(type)) {
         dataIndex = this._dataOrQName(identity);
         int offset = this.m_data.elementAt(dataIndex);
         int length = this.m_data.elementAt(dataIndex + 1);
         return this.m_chars.getString(offset, length);
      } else if (1 != type && 11 != type && 9 != type) {
         dataIndex = this._dataOrQName(identity);
         if (dataIndex < 0) {
            dataIndex = -dataIndex;
            dataIndex = this.m_data.elementAt(dataIndex + 1);
         }

         return this.m_valuesOrPrefixes.indexToString(dataIndex);
      } else {
         return null;
      }
   }

   public String getLocalName(int nodeHandle) {
      return super.m_expandedNameTable.getLocalName(this._exptype(this.makeNodeIdentity(nodeHandle)));
   }

   public String getUnparsedEntityURI(String name) {
      String url = "";
      if (null == this.m_entities) {
         return url;
      } else {
         int n = this.m_entities.size();

         for(int i = 0; i < n; i += 4) {
            String ename = (String)this.m_entities.elementAt(i + 3);
            if (null != ename && ename.equals(name)) {
               String nname = (String)this.m_entities.elementAt(i + 2);
               if (null != nname) {
                  url = (String)this.m_entities.elementAt(i + 1);
                  if (null == url) {
                     url = (String)this.m_entities.elementAt(i + 0);
                  }
               }
               break;
            }
         }

         return url;
      }
   }

   public String getPrefix(int nodeHandle) {
      int identity = this.makeNodeIdentity(nodeHandle);
      int type = this._type(identity);
      int prefixIndex;
      String qname;
      if (1 == type) {
         prefixIndex = this._dataOrQName(identity);
         if (0 == prefixIndex) {
            return "";
         } else {
            qname = this.m_valuesOrPrefixes.indexToString(prefixIndex);
            return this.getPrefix(qname, (String)null);
         }
      } else {
         if (2 == type) {
            prefixIndex = this._dataOrQName(identity);
            if (prefixIndex < 0) {
               prefixIndex = this.m_data.elementAt(-prefixIndex);
               qname = this.m_valuesOrPrefixes.indexToString(prefixIndex);
               return this.getPrefix(qname, (String)null);
            }
         }

         return "";
      }
   }

   public int getAttributeNode(int nodeHandle, String namespaceURI, String name) {
      for(int attrH = this.getFirstAttribute(nodeHandle); -1 != attrH; attrH = this.getNextAttribute(attrH)) {
         String attrNS = this.getNamespaceURI(attrH);
         String attrName = this.getLocalName(attrH);
         boolean nsMatch = namespaceURI == attrNS || namespaceURI != null && namespaceURI.equals(attrNS);
         if (nsMatch && name.equals(attrName)) {
            return attrH;
         }
      }

      return -1;
   }

   public String getDocumentTypeDeclarationPublicIdentifier() {
      this.error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", (Object[])null));
      return null;
   }

   public String getNamespaceURI(int nodeHandle) {
      return super.m_expandedNameTable.getNamespace(this._exptype(this.makeNodeIdentity(nodeHandle)));
   }

   public XMLString getStringValue(int nodeHandle) {
      int identity = this.makeNodeIdentity(nodeHandle);
      short type;
      if (identity == -1) {
         type = -1;
      } else {
         type = this._type(identity);
      }

      int firstChild;
      int offset;
      int length;
      if (this.isTextType(type)) {
         firstChild = this._dataOrQName(identity);
         offset = this.m_data.elementAt(firstChild);
         length = this.m_data.elementAt(firstChild + 1);
         return super.m_xstrf.newstr(this.m_chars, offset, length);
      } else {
         firstChild = this._firstch(identity);
         if (-1 != firstChild) {
            offset = -1;
            length = 0;
            int startNode = identity;
            identity = firstChild;

            do {
               type = this._type(identity);
               if (this.isTextType(type)) {
                  int dataIndex = this._dataOrQName(identity);
                  if (-1 == offset) {
                     offset = this.m_data.elementAt(dataIndex);
                  }

                  length += this.m_data.elementAt(dataIndex + 1);
               }

               identity = this.getNextNodeIdentity(identity);
            } while(-1 != identity && this._parent(identity) >= startNode);

            if (length > 0) {
               return super.m_xstrf.newstr(this.m_chars, offset, length);
            }
         } else if (type != 1) {
            offset = this._dataOrQName(identity);
            if (offset < 0) {
               offset = -offset;
               offset = this.m_data.elementAt(offset + 1);
            }

            return super.m_xstrf.newstr(this.m_valuesOrPrefixes.indexToString(offset));
         }

         return super.m_xstrf.emptystr();
      }
   }

   public boolean isWhitespace(int nodeHandle) {
      int identity = this.makeNodeIdentity(nodeHandle);
      short type;
      if (identity == -1) {
         type = -1;
      } else {
         type = this._type(identity);
      }

      if (this.isTextType(type)) {
         int dataIndex = this._dataOrQName(identity);
         int offset = this.m_data.elementAt(dataIndex);
         int length = this.m_data.elementAt(dataIndex + 1);
         return this.m_chars.isWhitespace(offset, length);
      } else {
         return false;
      }
   }

   public int getElementById(String elementId) {
      boolean isMore = true;

      Integer intObj;
      do {
         intObj = (Integer)this.m_idAttributes.get(elementId);
         if (null != intObj) {
            return this.makeNodeHandle(intObj);
         }

         if (!isMore || this.m_endDocumentOccured) {
            break;
         }

         isMore = this.nextNode();
      } while(null == intObj);

      return -1;
   }

   public String getPrefix(String qname, String uri) {
      int uriIndex = -1;
      String prefix;
      int indexOfNSSep;
      if (null != uri && uri.length() > 0) {
         do {
            ++uriIndex;
            uriIndex = this.m_prefixMappings.indexOf(uri, uriIndex);
         } while((uriIndex & 1) == 0);

         if (uriIndex >= 0) {
            prefix = (String)this.m_prefixMappings.elementAt(uriIndex - 1);
         } else if (null != qname) {
            indexOfNSSep = qname.indexOf(58);
            if (qname.equals("xmlns")) {
               prefix = "";
            } else if (qname.startsWith("xmlns:")) {
               prefix = qname.substring(indexOfNSSep + 1);
            } else {
               prefix = indexOfNSSep > 0 ? qname.substring(0, indexOfNSSep) : null;
            }
         } else {
            prefix = null;
         }
      } else if (null != qname) {
         indexOfNSSep = qname.indexOf(58);
         if (indexOfNSSep > 0) {
            if (qname.startsWith("xmlns:")) {
               prefix = qname.substring(indexOfNSSep + 1);
            } else {
               prefix = qname.substring(0, indexOfNSSep);
            }
         } else if (qname.equals("xmlns")) {
            prefix = "";
         } else {
            prefix = null;
         }
      } else {
         prefix = null;
      }

      return prefix;
   }

   public int getIdForNamespace(String uri) {
      return this.m_valuesOrPrefixes.stringToIndex(uri);
   }

   public String getNamespaceURI(String prefix) {
      String uri = "";
      int prefixIndex = this.m_contextIndexes.peek() - 1;
      if (null == prefix) {
         prefix = "";
      }

      do {
         ++prefixIndex;
         prefixIndex = this.m_prefixMappings.indexOf(prefix, prefixIndex);
      } while(prefixIndex >= 0 && (prefixIndex & 1) == 1);

      if (prefixIndex > -1) {
         uri = (String)this.m_prefixMappings.elementAt(prefixIndex + 1);
      }

      return uri;
   }

   public void setIDAttribute(String id, int elem) {
      this.m_idAttributes.put(id, new Integer(elem));
   }

   protected void charactersFlush() {
      if (this.m_textPendingStart >= 0) {
         int length = this.m_chars.size() - this.m_textPendingStart;
         boolean doStrip = false;
         if (this.getShouldStripWhitespace()) {
            doStrip = this.m_chars.isWhitespace(this.m_textPendingStart, length);
         }

         if (doStrip) {
            this.m_chars.setLength(this.m_textPendingStart);
         } else if (length > 0) {
            int exName = super.m_expandedNameTable.getExpandedTypeID(3);
            int dataIndex = this.m_data.size();
            this.m_previous = this.addNode(this.m_coalescedTextType, exName, this.m_parents.peek(), this.m_previous, dataIndex, false);
            this.m_data.addElement(this.m_textPendingStart);
            this.m_data.addElement(length);
         }

         this.m_textPendingStart = -1;
         this.m_textType = this.m_coalescedTextType = 3;
      }

   }

   public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
      return null;
   }

   public void notationDecl(String name, String publicId, String systemId) throws SAXException {
   }

   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
      if (null == this.m_entities) {
         this.m_entities = new Vector();
      }

      try {
         systemId = SystemIDResolver.getAbsoluteURI(systemId, this.getDocumentBaseURI());
      } catch (Exception var6) {
         throw new SAXException(var6);
      }

      this.m_entities.addElement(publicId);
      this.m_entities.addElement(systemId);
      this.m_entities.addElement(notationName);
      this.m_entities.addElement(name);
   }

   public void setDocumentLocator(Locator locator) {
      this.m_locator = locator;
      this.m_systemId = locator.getSystemId();
   }

   public void startDocument() throws SAXException {
      int doc = this.addNode(9, super.m_expandedNameTable.getExpandedTypeID(9), -1, -1, 0, true);
      this.m_parents.push(doc);
      this.m_previous = -1;
      this.m_contextIndexes.push(this.m_prefixMappings.size());
   }

   public void endDocument() throws SAXException {
      this.charactersFlush();
      super.m_nextsib.setElementAt(-1, 0);
      if (super.m_firstch.elementAt(0) == -2) {
         super.m_firstch.setElementAt(-1, 0);
      }

      if (-1 != this.m_previous) {
         super.m_nextsib.setElementAt(-1, this.m_previous);
      }

      this.m_parents = null;
      this.m_prefixMappings = null;
      this.m_contextIndexes = null;
      this.m_endDocumentOccured = true;
      this.m_locator = null;
   }

   public void startPrefixMapping(String prefix, String uri) throws SAXException {
      if (null == prefix) {
         prefix = "";
      }

      this.m_prefixMappings.addElement(prefix);
      this.m_prefixMappings.addElement(uri);
   }

   public void endPrefixMapping(String prefix) throws SAXException {
      if (null == prefix) {
         prefix = "";
      }

      int index = this.m_contextIndexes.peek() - 1;

      do {
         ++index;
         index = this.m_prefixMappings.indexOf(prefix, index);
      } while(index >= 0 && (index & 1) == 1);

      if (index > -1) {
         this.m_prefixMappings.setElementAt("%@$#^@#", index);
         this.m_prefixMappings.setElementAt("%@$#^@#", index + 1);
      }

   }

   protected boolean declAlreadyDeclared(String prefix) {
      int startDecls = this.m_contextIndexes.peek();
      Vector prefixMappings = this.m_prefixMappings;
      int nDecls = prefixMappings.size();

      for(int i = startDecls; i < nDecls; i += 2) {
         String prefixDecl = (String)prefixMappings.elementAt(i);
         if (prefixDecl != null && prefixDecl.equals(prefix)) {
            return true;
         }
      }

      return false;
   }

   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      this.charactersFlush();
      int exName = super.m_expandedNameTable.getExpandedTypeID(uri, localName, 1);
      String prefix = this.getPrefix(qName, uri);
      int prefixIndex = null != prefix ? this.m_valuesOrPrefixes.stringToIndex(qName) : 0;
      int elemNode = this.addNode(1, exName, this.m_parents.peek(), this.m_previous, prefixIndex, true);
      if (super.m_indexing) {
         this.indexNode(exName, elemNode);
      }

      this.m_parents.push(elemNode);
      int startDecls = this.m_contextIndexes.peek();
      int nDecls = this.m_prefixMappings.size();
      int prev = -1;
      int n;
      if (!this.m_pastFirstElement) {
         prefix = "xml";
         String declURL = "http://www.w3.org/XML/1998/namespace";
         exName = super.m_expandedNameTable.getExpandedTypeID((String)null, prefix, 13);
         n = this.m_valuesOrPrefixes.stringToIndex(declURL);
         prev = this.addNode(13, exName, elemNode, prev, n, false);
         this.m_pastFirstElement = true;
      }

      int i;
      for(int i = startDecls; i < nDecls; i += 2) {
         prefix = (String)this.m_prefixMappings.elementAt(i);
         if (prefix != null) {
            String declURL = (String)this.m_prefixMappings.elementAt(i + 1);
            exName = super.m_expandedNameTable.getExpandedTypeID((String)null, prefix, 13);
            i = this.m_valuesOrPrefixes.stringToIndex(declURL);
            prev = this.addNode(13, exName, elemNode, prev, i, false);
         }
      }

      n = attributes.getLength();

      for(i = 0; i < n; ++i) {
         String attrUri = attributes.getURI(i);
         String attrQName = attributes.getQName(i);
         String valString = attributes.getValue(i);
         prefix = this.getPrefix(attrQName, attrUri);
         String attrLocalName = attributes.getLocalName(i);
         byte nodeType;
         if (null == attrQName || !attrQName.equals("xmlns") && !attrQName.startsWith("xmlns:")) {
            nodeType = 2;
            if (attributes.getType(i).equalsIgnoreCase("ID")) {
               this.setIDAttribute(valString, elemNode);
            }
         } else {
            if (this.declAlreadyDeclared(prefix)) {
               continue;
            }

            nodeType = 13;
         }

         if (null == valString) {
            valString = "";
         }

         int val = this.m_valuesOrPrefixes.stringToIndex(valString);
         if (null != prefix) {
            prefixIndex = this.m_valuesOrPrefixes.stringToIndex(attrQName);
            int dataIndex = this.m_data.size();
            this.m_data.addElement(prefixIndex);
            this.m_data.addElement(val);
            val = -dataIndex;
         }

         exName = super.m_expandedNameTable.getExpandedTypeID(attrUri, attrLocalName, nodeType);
         prev = this.addNode(nodeType, exName, elemNode, prev, val, false);
      }

      if (-1 != prev) {
         super.m_nextsib.setElementAt(-1, prev);
      }

      if (null != super.m_wsfilter) {
         short wsv = super.m_wsfilter.getShouldStripSpace(this.makeNodeHandle(elemNode), this);
         boolean shouldStrip = 3 == wsv ? this.getShouldStripWhitespace() : 2 == wsv;
         this.pushShouldStripWhitespace(shouldStrip);
      }

      this.m_previous = -1;
      this.m_contextIndexes.push(this.m_prefixMappings.size());
   }

   public void endElement(String uri, String localName, String qName) throws SAXException {
      this.charactersFlush();
      this.m_contextIndexes.quickPop(1);
      int topContextIndex = this.m_contextIndexes.peek();
      if (topContextIndex != this.m_prefixMappings.size()) {
         this.m_prefixMappings.setSize(topContextIndex);
      }

      int lastNode = this.m_previous;
      this.m_previous = this.m_parents.pop();
      if (-1 == lastNode) {
         super.m_firstch.setElementAt(-1, this.m_previous);
      } else {
         super.m_nextsib.setElementAt(-1, lastNode);
      }

      this.popShouldStripWhitespace();
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      if (this.m_textPendingStart == -1) {
         this.m_textPendingStart = this.m_chars.size();
         this.m_coalescedTextType = this.m_textType;
      } else if (this.m_textType == 3) {
         this.m_coalescedTextType = 3;
      }

      this.m_chars.append(ch, start, length);
   }

   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
      this.characters(ch, start, length);
   }

   public void processingInstruction(String target, String data) throws SAXException {
      this.charactersFlush();
      int exName = super.m_expandedNameTable.getExpandedTypeID((String)null, target, 7);
      int dataIndex = this.m_valuesOrPrefixes.stringToIndex(data);
      this.m_previous = this.addNode(7, exName, this.m_parents.peek(), this.m_previous, dataIndex, false);
   }

   public void skippedEntity(String name) throws SAXException {
   }

   public void warning(SAXParseException e) throws SAXException {
      System.err.println(e.getMessage());
   }

   public void error(SAXParseException e) throws SAXException {
      throw e;
   }

   public void fatalError(SAXParseException e) throws SAXException {
      throw e;
   }

   public void elementDecl(String name, String model) throws SAXException {
   }

   public void attributeDecl(String eName, String aName, String type, String valueDefault, String value) throws SAXException {
   }

   public void internalEntityDecl(String name, String value) throws SAXException {
   }

   public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
   }

   public void startDTD(String name, String publicId, String systemId) throws SAXException {
      this.m_insideDTD = true;
   }

   public void endDTD() throws SAXException {
      this.m_insideDTD = false;
   }

   public void startEntity(String name) throws SAXException {
   }

   public void endEntity(String name) throws SAXException {
   }

   public void startCDATA() throws SAXException {
      this.m_textType = 4;
   }

   public void endCDATA() throws SAXException {
      this.m_textType = 3;
   }

   public void comment(char[] ch, int start, int length) throws SAXException {
      if (!this.m_insideDTD) {
         this.charactersFlush();
         int exName = super.m_expandedNameTable.getExpandedTypeID(8);
         int dataIndex = this.m_valuesOrPrefixes.stringToIndex(new String(ch, start, length));
         this.m_previous = this.addNode(8, exName, this.m_parents.peek(), this.m_previous, dataIndex, false);
      }
   }

   public void setProperty(String property, Object value) {
   }

   public SourceLocator getSourceLocatorFor(int node) {
      if (this.m_useSourceLocationProperty) {
         node = this.makeNodeIdentity(node);
         return new NodeLocator((String)null, this.m_sourceSystemId.elementAt(node), this.m_sourceLine.elementAt(node), this.m_sourceColumn.elementAt(node));
      } else if (this.m_locator != null) {
         return new NodeLocator((String)null, this.m_locator.getSystemId(), -1, -1);
      } else {
         return this.m_systemId != null ? new NodeLocator((String)null, this.m_systemId, -1, -1) : null;
      }
   }

   public String getFixedNames(int type) {
      return m_fixednames[type];
   }
}

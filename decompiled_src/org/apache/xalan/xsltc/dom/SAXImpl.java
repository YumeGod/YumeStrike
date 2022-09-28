package org.apache.xalan.xsltc.dom;

import java.util.Enumeration;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMEnhancedForDTM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xml.dtm.Axis;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMAxisIterNodeList;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators;
import org.apache.xml.dtm.ref.DTMNodeProxy;
import org.apache.xml.dtm.ref.EmptyIterator;
import org.apache.xml.dtm.ref.ExpandedNameTable;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM2;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.serializer.ToXMLSAXHandler;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.XMLStringFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public final class SAXImpl extends SAX2DTM2 implements DOMEnhancedForDTM, DOMBuilder {
   private int _uriCount;
   private int _prefixCount;
   private int[] _xmlSpaceStack;
   private int _idx;
   private boolean _preserve;
   private static final String XML_STRING = "xml:";
   private static final String XML_PREFIX = "xml";
   private static final String XMLSPACE_STRING = "xml:space";
   private static final String PRESERVE_STRING = "preserve";
   private static final String XMLNS_PREFIX = "xmlns";
   private static final String XML_URI = "http://www.w3.org/XML/1998/namespace";
   private boolean _escaping;
   private boolean _disableEscaping;
   private int _textNodeToProcess;
   private static final String EMPTYSTRING = "";
   private static final DTMAxisIterator EMPTYITERATOR = EmptyIterator.getInstance();
   private int _namesSize;
   private Hashtable _nsIndex;
   private int _size;
   private BitArray _dontEscape;
   private String _documentURI;
   private static int _documentURIIndex = 0;
   private Document _document;
   private Hashtable _node2Ids;
   private boolean _hasDOMSource;
   private XSLTCDTMManager _dtmManager;
   private Node[] _nodes;
   private NodeList[] _nodeLists;
   private static final String XML_LANG_ATTRIBUTE = "http://www.w3.org/XML/1998/namespace:@lang";

   public void setDocumentURI(String uri) {
      if (uri != null) {
         this.setDocumentBaseURI(SystemIDResolver.getAbsoluteURI(uri));
      }

   }

   public String getDocumentURI() {
      String baseURI = this.getDocumentBaseURI();
      return baseURI != null ? baseURI : "rtf" + _documentURIIndex++;
   }

   public String getDocumentURI(int node) {
      return this.getDocumentURI();
   }

   public void setupMapping(String[] names, String[] urisArray, int[] typesArray, String[] namespaces) {
   }

   public String lookupNamespace(int node, String prefix) throws TransletException {
      SAX2DTM2.AncestorIterator ancestors = new SAX2DTM2.AncestorIterator();
      if (this.isElement(node)) {
         ancestors.includeSelf();
      }

      ancestors.setStartNode(node);

      int anode;
      while((anode = ancestors.next()) != -1) {
         DTMDefaultBaseIterators.NamespaceIterator namespaces = new DTMDefaultBaseIterators.NamespaceIterator();
         namespaces.setStartNode(anode);

         int nsnode;
         while((nsnode = namespaces.next()) != -1) {
            if (this.getLocalName(nsnode).equals(prefix)) {
               return this.getNodeValue(nsnode);
            }
         }
      }

      BasisLibrary.runTimeError("NAMESPACE_PREFIX_ERR", (Object)prefix);
      return null;
   }

   public boolean isElement(int node) {
      return this.getNodeType(node) == 1;
   }

   public boolean isAttribute(int node) {
      return this.getNodeType(node) == 2;
   }

   public int getSize() {
      return this.getNumberOfNodes();
   }

   public void setFilter(StripFilter filter) {
   }

   public boolean lessThan(int node1, int node2) {
      if (node1 == -1) {
         return false;
      } else if (node2 == -1) {
         return true;
      } else {
         return node1 < node2;
      }
   }

   public Node makeNode(int index) {
      if (this._nodes == null) {
         this._nodes = new Node[this._namesSize];
      }

      int nodeID = this.makeNodeIdentity(index);
      if (nodeID < 0) {
         return null;
      } else if (nodeID < this._nodes.length) {
         return this._nodes[nodeID] != null ? this._nodes[nodeID] : (this._nodes[nodeID] = new DTMNodeProxy(this, index));
      } else {
         return new DTMNodeProxy(this, index);
      }
   }

   public Node makeNode(DTMAxisIterator iter) {
      return this.makeNode(iter.next());
   }

   public NodeList makeNodeList(int index) {
      if (this._nodeLists == null) {
         this._nodeLists = new NodeList[this._namesSize];
      }

      int nodeID = this.makeNodeIdentity(index);
      if (nodeID < 0) {
         return null;
      } else if (nodeID < this._nodeLists.length) {
         return this._nodeLists[nodeID] != null ? this._nodeLists[nodeID] : (this._nodeLists[nodeID] = new DTMAxisIterNodeList(this, new DTMDefaultBaseIterators.SingletonIterator(index)));
      } else {
         return new DTMAxisIterNodeList(this, new DTMDefaultBaseIterators.SingletonIterator(index));
      }
   }

   public NodeList makeNodeList(DTMAxisIterator iter) {
      return new DTMAxisIterNodeList(this, iter);
   }

   public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iterator, int type, String value, boolean op) {
      return new NodeValueIterator(iterator, type, value, op);
   }

   public DTMAxisIterator orderNodes(DTMAxisIterator source, int node) {
      return new DupFilterIterator(source);
   }

   public DTMAxisIterator getIterator() {
      return new DTMDefaultBaseIterators.SingletonIterator(this.getDocument());
   }

   public int getNSType(int node) {
      String s = this.getNamespaceURI(node);
      if (s == null) {
         return 0;
      } else {
         int eType = this.getIdForNamespace(s);
         return (Integer)this._nsIndex.get(new Integer(eType));
      }
   }

   public int getNamespaceType(int node) {
      return super.getNamespaceType(node);
   }

   private int[] setupMapping(String[] names, String[] uris, int[] types, int nNames) {
      int[] result = new int[super.m_expandedNameTable.getSize()];

      for(int i = 0; i < nNames; ++i) {
         int type = super.m_expandedNameTable.getExpandedTypeID(uris[i], names[i], types[i], false);
         result[type] = type;
      }

      return result;
   }

   public int getGeneralizedType(String name) {
      return this.getGeneralizedType(name, true);
   }

   public int getGeneralizedType(String name, boolean searchOnly) {
      String ns = null;
      int index = true;
      int index;
      if ((index = name.lastIndexOf(":")) > -1) {
         ns = name.substring(0, index);
      }

      int lNameStartIdx = index + 1;
      byte code;
      if (name.charAt(lNameStartIdx) == '@') {
         code = 2;
         ++lNameStartIdx;
      } else {
         code = 1;
      }

      String lName = lNameStartIdx == 0 ? name : name.substring(lNameStartIdx);
      return super.m_expandedNameTable.getExpandedTypeID(ns, lName, code, searchOnly);
   }

   public short[] getMapping(String[] names, String[] uris, int[] types) {
      if (this._namesSize < 0) {
         return this.getMapping2(names, uris, types);
      } else {
         int namesLength = names.length;
         int exLength = super.m_expandedNameTable.getSize();
         short[] result = new short[exLength];

         int i;
         for(i = 0; i < 14; ++i) {
            result[i] = (short)i;
         }

         for(i = 14; i < exLength; ++i) {
            result[i] = super.m_expandedNameTable.getType(i);
         }

         for(i = 0; i < namesLength; ++i) {
            int genType = super.m_expandedNameTable.getExpandedTypeID(uris[i], names[i], types[i], true);
            if (genType >= 0 && genType < exLength) {
               result[genType] = (short)(i + 14);
            }
         }

         return result;
      }
   }

   public int[] getReverseMapping(String[] names, String[] uris, int[] types) {
      int[] result = new int[names.length + 14];

      int i;
      for(i = 0; i < 14; result[i] = i++) {
      }

      for(i = 0; i < names.length; ++i) {
         int type = super.m_expandedNameTable.getExpandedTypeID(uris[i], names[i], types[i], true);
         result[i + 14] = type;
      }

      return result;
   }

   private short[] getMapping2(String[] names, String[] uris, int[] types) {
      int namesLength = names.length;
      int exLength = super.m_expandedNameTable.getSize();
      int[] generalizedTypes = null;
      if (namesLength > 0) {
         generalizedTypes = new int[namesLength];
      }

      int resultLength = exLength;

      int i;
      for(i = 0; i < namesLength; ++i) {
         generalizedTypes[i] = super.m_expandedNameTable.getExpandedTypeID(uris[i], names[i], types[i], false);
         if (this._namesSize < 0 && generalizedTypes[i] >= resultLength) {
            resultLength = generalizedTypes[i] + 1;
         }
      }

      short[] result = new short[resultLength];

      for(i = 0; i < 14; ++i) {
         result[i] = (short)i;
      }

      for(i = 14; i < exLength; ++i) {
         result[i] = super.m_expandedNameTable.getType(i);
      }

      for(i = 0; i < namesLength; ++i) {
         int genType = generalizedTypes[i];
         if (genType >= 0 && genType < resultLength) {
            result[genType] = (short)(i + 14);
         }
      }

      return result;
   }

   public short[] getNamespaceMapping(String[] namespaces) {
      int nsLength = namespaces.length;
      int mappingLength = this._uriCount;
      short[] result = new short[mappingLength];

      int i;
      for(i = 0; i < mappingLength; ++i) {
         result[i] = -1;
      }

      for(i = 0; i < nsLength; ++i) {
         int eType = this.getIdForNamespace(namespaces[i]);
         Integer type = (Integer)this._nsIndex.get(new Integer(eType));
         if (type != null) {
            result[type] = (short)i;
         }
      }

      return result;
   }

   public short[] getReverseNamespaceMapping(String[] namespaces) {
      int length = namespaces.length;
      short[] result = new short[length];

      for(int i = 0; i < length; ++i) {
         int eType = this.getIdForNamespace(namespaces[i]);
         Integer type = (Integer)this._nsIndex.get(new Integer(eType));
         result[i] = type == null ? -1 : type.shortValue();
      }

      return result;
   }

   public SAXImpl(XSLTCDTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, boolean buildIdIndex) {
      this(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, 512, buildIdIndex, false);
   }

   public SAXImpl(XSLTCDTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean buildIdIndex, boolean newNameTable) {
      super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, blocksize, false, buildIdIndex, newNameTable);
      this._uriCount = 0;
      this._prefixCount = 0;
      this._idx = 1;
      this._preserve = false;
      this._escaping = true;
      this._disableEscaping = false;
      this._textNodeToProcess = -1;
      this._namesSize = -1;
      this._nsIndex = new Hashtable();
      this._size = 0;
      this._dontEscape = null;
      this._documentURI = null;
      this._node2Ids = null;
      this._hasDOMSource = false;
      this._dtmManager = mgr;
      this._size = blocksize;
      this._xmlSpaceStack = new int[blocksize <= 64 ? 4 : 64];
      this._xmlSpaceStack[0] = 0;
      if (source instanceof DOMSource) {
         this._hasDOMSource = true;
         DOMSource domsrc = (DOMSource)source;
         Node node = domsrc.getNode();
         if (node instanceof Document) {
            this._document = (Document)node;
         } else {
            this._document = node.getOwnerDocument();
         }

         this._node2Ids = new Hashtable();
      }

   }

   public void migrateTo(DTMManager manager) {
      super.migrateTo(manager);
      if (manager instanceof XSLTCDTMManager) {
         this._dtmManager = (XSLTCDTMManager)manager;
      }

   }

   public int getElementById(String idString) {
      Node node = this._document.getElementById(idString);
      if (node != null) {
         Integer id = (Integer)this._node2Ids.get(node);
         return id != null ? id : -1;
      } else {
         return -1;
      }
   }

   public boolean hasDOMSource() {
      return this._hasDOMSource;
   }

   private void xmlSpaceDefine(String val, int node) {
      boolean setting = val.equals("preserve");
      if (setting != this._preserve) {
         this._xmlSpaceStack[this._idx++] = node;
         this._preserve = setting;
      }

   }

   private void xmlSpaceRevert(int node) {
      if (node == this._xmlSpaceStack[this._idx - 1]) {
         --this._idx;
         this._preserve = !this._preserve;
      }

   }

   protected boolean getShouldStripWhitespace() {
      return this._preserve ? false : super.getShouldStripWhitespace();
   }

   private void handleTextEscaping() {
      if (this._disableEscaping && this._textNodeToProcess != -1 && this._type(this._textNodeToProcess) == 3) {
         if (this._dontEscape == null) {
            this._dontEscape = new BitArray(this._size);
         }

         if (this._textNodeToProcess >= this._dontEscape.size()) {
            this._dontEscape.resize(this._dontEscape.size() * 2);
         }

         this._dontEscape.setBit(this._textNodeToProcess);
         this._disableEscaping = false;
      }

      this._textNodeToProcess = -1;
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      super.characters(ch, start, length);
      this._disableEscaping = !this._escaping;
      this._textNodeToProcess = this.getNumberOfNodes();
   }

   public void startDocument() throws SAXException {
      super.startDocument();
      this._nsIndex.put(new Integer(0), new Integer(this._uriCount++));
      this.definePrefixAndUri("xml", "http://www.w3.org/XML/1998/namespace");
   }

   public void endDocument() throws SAXException {
      super.endDocument();
      this.handleTextEscaping();
      this._namesSize = super.m_expandedNameTable.getSize();
   }

   public void startElement(String uri, String localName, String qname, Attributes attributes, Node node) throws SAXException {
      this.startElement(uri, localName, qname, attributes);
      if (super.m_buildIdIndex) {
         this._node2Ids.put(node, new Integer(super.m_parents.peek()));
      }

   }

   public void startElement(String uri, String localName, String qname, Attributes attributes) throws SAXException {
      super.startElement(uri, localName, qname, attributes);
      this.handleTextEscaping();
      if (super.m_wsfilter != null) {
         int index = attributes.getIndex("xml:space");
         if (index >= 0) {
            this.xmlSpaceDefine(attributes.getValue(index), super.m_parents.peek());
         }
      }

   }

   public void endElement(String namespaceURI, String localName, String qname) throws SAXException {
      super.endElement(namespaceURI, localName, qname);
      this.handleTextEscaping();
      if (super.m_wsfilter != null) {
         this.xmlSpaceRevert(super.m_previous);
      }

   }

   public void processingInstruction(String target, String data) throws SAXException {
      super.processingInstruction(target, data);
      this.handleTextEscaping();
   }

   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
      super.ignorableWhitespace(ch, start, length);
      this._textNodeToProcess = this.getNumberOfNodes();
   }

   public void startPrefixMapping(String prefix, String uri) throws SAXException {
      super.startPrefixMapping(prefix, uri);
      this.handleTextEscaping();
      this.definePrefixAndUri(prefix, uri);
   }

   private void definePrefixAndUri(String prefix, String uri) throws SAXException {
      Integer eType = new Integer(this.getIdForNamespace(uri));
      if ((Integer)this._nsIndex.get(eType) == null) {
         this._nsIndex.put(eType, new Integer(this._uriCount++));
      }

   }

   public void comment(char[] ch, int start, int length) throws SAXException {
      super.comment(ch, start, length);
      this.handleTextEscaping();
   }

   public boolean setEscaping(boolean value) {
      boolean temp = this._escaping;
      this._escaping = value;
      return temp;
   }

   public void print(int node, int level) {
      switch (this.getNodeType(node)) {
         case 0:
         case 9:
            this.print(this.getFirstChild(node), level);
            break;
         case 1:
         case 2:
         case 4:
         case 5:
         case 6:
         default:
            String name = this.getNodeName(node);
            System.out.print("<" + name);

            for(int a = this.getFirstAttribute(node); a != -1; a = this.getNextAttribute(a)) {
               System.out.print("\n" + this.getNodeName(a) + "=\"" + this.getStringValueX(a) + "\"");
            }

            System.out.print('>');

            for(int child = this.getFirstChild(node); child != -1; child = this.getNextSibling(child)) {
               this.print(child, level + 1);
            }

            System.out.println("</" + name + '>');
            break;
         case 3:
         case 7:
         case 8:
            System.out.print(this.getStringValueX(node));
      }

   }

   public String getNodeName(int node) {
      short type = this.getNodeType(node);
      switch (type) {
         case 0:
         case 3:
         case 8:
         case 9:
            return "";
         case 1:
         case 2:
         case 4:
         case 5:
         case 6:
         case 7:
         case 10:
         case 11:
         case 12:
         default:
            return super.getNodeName(node);
         case 13:
            return this.getLocalName(node);
      }
   }

   public String getNamespaceName(int node) {
      if (node == -1) {
         return "";
      } else {
         String s;
         return (s = this.getNamespaceURI(node)) == null ? "" : s;
      }
   }

   public int getAttributeNode(int type, int element) {
      for(int attr = this.getFirstAttribute(element); attr != -1; attr = this.getNextAttribute(attr)) {
         if (this.getExpandedTypeID(attr) == type) {
            return attr;
         }
      }

      return -1;
   }

   public String getAttributeValue(int type, int element) {
      int attr = this.getAttributeNode(type, element);
      return attr != -1 ? this.getStringValueX(attr) : "";
   }

   public String getAttributeValue(String name, int element) {
      return this.getAttributeValue(this.getGeneralizedType(name), element);
   }

   public DTMAxisIterator getChildren(int node) {
      return (new SAX2DTM2.ChildrenIterator()).setStartNode(node);
   }

   public DTMAxisIterator getTypedChildren(int type) {
      return new SAX2DTM2.TypedChildrenIterator(type);
   }

   public DTMAxisIterator getAxisIterator(int axis) {
      switch (axis) {
         case 0:
            return new SAX2DTM2.AncestorIterator();
         case 1:
            return (new SAX2DTM2.AncestorIterator()).includeSelf();
         case 2:
            return new SAX2DTM2.AttributeIterator();
         case 3:
            return new SAX2DTM2.ChildrenIterator();
         case 4:
            return new SAX2DTM2.DescendantIterator();
         case 5:
            return (new SAX2DTM2.DescendantIterator()).includeSelf();
         case 6:
            return new SAX2DTM2.FollowingIterator();
         case 7:
            return new SAX2DTM2.FollowingSiblingIterator();
         case 8:
         default:
            BasisLibrary.runTimeError("AXIS_SUPPORT_ERR", (Object)Axis.getNames(axis));
            return null;
         case 9:
            return new DTMDefaultBaseIterators.NamespaceIterator();
         case 10:
            return new SAX2DTM2.ParentIterator();
         case 11:
            return new SAX2DTM2.PrecedingIterator();
         case 12:
            return new SAX2DTM2.PrecedingSiblingIterator();
         case 13:
            return new DTMDefaultBaseIterators.SingletonIterator();
      }
   }

   public DTMAxisIterator getTypedAxisIterator(int axis, int type) {
      if (axis == 3) {
         return new SAX2DTM2.TypedChildrenIterator(type);
      } else if (type == -1) {
         return EMPTYITERATOR;
      } else {
         switch (axis) {
            case 0:
               return new SAX2DTM2.TypedAncestorIterator(type);
            case 1:
               return (new SAX2DTM2.TypedAncestorIterator(type)).includeSelf();
            case 2:
               return new SAX2DTM2.TypedAttributeIterator(type);
            case 3:
               return new SAX2DTM2.TypedChildrenIterator(type);
            case 4:
               return new SAX2DTM2.TypedDescendantIterator(type);
            case 5:
               return (new SAX2DTM2.TypedDescendantIterator(type)).includeSelf();
            case 6:
               return new SAX2DTM2.TypedFollowingIterator(type);
            case 7:
               return new SAX2DTM2.TypedFollowingSiblingIterator(type);
            case 8:
            default:
               BasisLibrary.runTimeError("TYPED_AXIS_SUPPORT_ERR", (Object)Axis.getNames(axis));
               return null;
            case 9:
               return new TypedNamespaceIterator(type);
            case 10:
               return (new SAX2DTM2.ParentIterator()).setNodeType(type);
            case 11:
               return new SAX2DTM2.TypedPrecedingIterator(type);
            case 12:
               return new SAX2DTM2.TypedPrecedingSiblingIterator(type);
            case 13:
               return new SAX2DTM2.TypedSingletonIterator(type);
         }
      }
   }

   public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns) {
      DTMAxisIterator iterator = null;
      if (ns == -1) {
         return EMPTYITERATOR;
      } else {
         switch (axis) {
            case 2:
               return new NamespaceAttributeIterator(ns);
            case 3:
               return new NamespaceChildrenIterator(ns);
            default:
               return new NamespaceWildcardIterator(axis, ns);
         }
      }
   }

   public DTMAxisIterator getTypedDescendantIterator(int type) {
      return new SAX2DTM2.TypedDescendantIterator(type);
   }

   public DTMAxisIterator getNthDescendant(int type, int n, boolean includeself) {
      new SAX2DTM2.TypedDescendantIterator(type);
      return new DTMDefaultBaseIterators.NthDescendantIterator(n);
   }

   public void characters(int node, SerializationHandler handler) throws TransletException {
      if (node != -1) {
         try {
            this.dispatchCharactersEvents(node, handler, false);
         } catch (SAXException var4) {
            throw new TransletException(var4);
         }
      }

   }

   public void copy(DTMAxisIterator nodes, SerializationHandler handler) throws TransletException {
      int node;
      while((node = nodes.next()) != -1) {
         this.copy(node, handler);
      }

   }

   public void copy(SerializationHandler handler) throws TransletException {
      this.copy(this.getDocument(), handler);
   }

   public void copy(int node, SerializationHandler handler) throws TransletException {
      this.copy(node, handler, false);
   }

   private final void copy(int node, SerializationHandler handler, boolean isChild) throws TransletException {
      int nodeID = this.makeNodeIdentity(node);
      int eType = this._exptype2(nodeID);
      int type = this._exptype2Type(eType);

      try {
         switch (type) {
            case 0:
            case 9:
               for(int c = this._firstch2(nodeID); c != -1; c = this._nextsib2(c)) {
                  this.copy(this.makeNodeHandle(c), handler, true);
               }

               return;
            case 1:
            case 4:
            case 5:
            case 6:
            case 10:
            case 11:
            case 12:
            default:
               String name;
               if (type == 1) {
                  name = this.copyElement(nodeID, eType, handler);
                  this.copyNS(nodeID, handler, !isChild);
                  this.copyAttributes(nodeID, handler);

                  for(int c = this._firstch2(nodeID); c != -1; c = this._nextsib2(c)) {
                     this.copy(this.makeNodeHandle(c), handler, true);
                  }

                  handler.endElement(name);
               } else {
                  name = this.getNamespaceName(node);
                  if (name.length() != 0) {
                     String prefix = this.getPrefix(node);
                     handler.namespaceAfterStartElement(prefix, name);
                  }

                  handler.addAttribute(this.getNodeName(node), this.getNodeValue(node));
               }
               break;
            case 2:
               this.copyAttribute(nodeID, eType, handler);
               break;
            case 3:
               boolean oldEscapeSetting = false;
               boolean escapeBit = false;
               if (this._dontEscape != null) {
                  escapeBit = this._dontEscape.getBit(this.getNodeIdent(node));
                  if (escapeBit) {
                     oldEscapeSetting = handler.setEscaping(false);
                  }
               }

               this.copyTextNode(nodeID, handler);
               if (escapeBit) {
                  handler.setEscaping(oldEscapeSetting);
               }
               break;
            case 7:
               this.copyPI(node, handler);
               break;
            case 8:
               handler.comment(this.getStringValueX(node));
               break;
            case 13:
               handler.namespaceAfterStartElement(this.getNodeNameX(node), this.getNodeValue(node));
         }

      } catch (Exception var12) {
         throw new TransletException(var12);
      }
   }

   private void copyPI(int node, SerializationHandler handler) throws TransletException {
      String target = this.getNodeName(node);
      String value = this.getStringValueX(node);

      try {
         handler.processingInstruction(target, value);
      } catch (Exception var6) {
         throw new TransletException(var6);
      }
   }

   public String shallowCopy(int node, SerializationHandler handler) throws TransletException {
      int nodeID = this.makeNodeIdentity(node);
      int exptype = this._exptype2(nodeID);
      int type = this._exptype2Type(exptype);

      try {
         switch (type) {
            case 0:
            case 9:
               return "";
            case 1:
               String name = this.copyElement(nodeID, exptype, handler);
               this.copyNS(nodeID, handler, true);
               return name;
            case 2:
               this.copyAttribute(nodeID, exptype, handler);
               return null;
            case 3:
               this.copyTextNode(nodeID, handler);
               return null;
            case 4:
            case 5:
            case 6:
            case 10:
            case 11:
            case 12:
            default:
               String uri1 = this.getNamespaceName(node);
               if (uri1.length() != 0) {
                  String prefix = this.getPrefix(node);
                  handler.namespaceAfterStartElement(prefix, uri1);
               }

               handler.addAttribute(this.getNodeName(node), this.getNodeValue(node));
               return null;
            case 7:
               this.copyPI(node, handler);
               return null;
            case 8:
               handler.comment(this.getStringValueX(node));
               return null;
            case 13:
               handler.namespaceAfterStartElement(this.getNodeNameX(node), this.getNodeValue(node));
               return null;
         }
      } catch (Exception var9) {
         throw new TransletException(var9);
      }
   }

   public String getLanguage(int node) {
      for(int parent = node; -1 != parent; parent = this.getParent(parent)) {
         if (1 == this.getNodeType(parent)) {
            int langAttr = this.getAttributeNode(parent, "http://www.w3.org/XML/1998/namespace", "lang");
            if (-1 != langAttr) {
               return this.getNodeValue(langAttr);
            }
         }
      }

      return null;
   }

   public DOMBuilder getBuilder() {
      return this;
   }

   public SerializationHandler getOutputDomBuilder() {
      return new ToXMLSAXHandler(this, "UTF-8");
   }

   public DOM getResultTreeFrag(int initSize, int rtfType) {
      return this.getResultTreeFrag(initSize, rtfType, true);
   }

   public DOM getResultTreeFrag(int initSize, int rtfType, boolean addToManager) {
      int dtmPos;
      if (rtfType == 0) {
         if (addToManager) {
            dtmPos = this._dtmManager.getFirstFreeDTMID();
            SimpleResultTreeImpl rtf = new SimpleResultTreeImpl(this._dtmManager, dtmPos << 16);
            this._dtmManager.addDTM(rtf, dtmPos, 0);
            return rtf;
         } else {
            return new SimpleResultTreeImpl(this._dtmManager, 0);
         }
      } else if (rtfType == 1) {
         if (addToManager) {
            dtmPos = this._dtmManager.getFirstFreeDTMID();
            AdaptiveResultTreeImpl rtf = new AdaptiveResultTreeImpl(this._dtmManager, dtmPos << 16, super.m_wsfilter, initSize, super.m_buildIdIndex);
            this._dtmManager.addDTM(rtf, dtmPos, 0);
            return rtf;
         } else {
            return new AdaptiveResultTreeImpl(this._dtmManager, 0, super.m_wsfilter, initSize, super.m_buildIdIndex);
         }
      } else {
         return (DOM)this._dtmManager.getDTM((Source)null, true, super.m_wsfilter, true, false, false, initSize, super.m_buildIdIndex);
      }
   }

   public Hashtable getElementsWithIDs() {
      if (super.m_idAttributes == null) {
         return null;
      } else {
         Enumeration idValues = super.m_idAttributes.keys();
         if (!idValues.hasMoreElements()) {
            return null;
         } else {
            Hashtable idAttrsTable = new Hashtable();

            while(idValues.hasMoreElements()) {
               Object idValue = idValues.nextElement();
               idAttrsTable.put(idValue, super.m_idAttributes.get(idValue));
            }

            return idAttrsTable;
         }
      }
   }

   public String getUnparsedEntityURI(String name) {
      if (this._document != null) {
         String uri = "";
         DocumentType doctype = this._document.getDoctype();
         if (doctype != null) {
            NamedNodeMap entities = doctype.getEntities();
            if (entities == null) {
               return uri;
            }

            Entity entity = (Entity)entities.getNamedItem(name);
            if (entity == null) {
               return uri;
            }

            String notationName = entity.getNotationName();
            if (notationName != null) {
               uri = entity.getSystemId();
               if (uri == null) {
                  uri = entity.getPublicId();
               }
            }
         }

         return uri;
      } else {
         return super.getUnparsedEntityURI(name);
      }
   }

   // $FF: synthetic method
   static ExpandedNameTable access$001(SAXImpl x0) {
      return x0.m_expandedNameTable;
   }

   // $FF: synthetic method
   static ExpandedNameTable access$101(SAXImpl x0) {
      return x0.m_expandedNameTable;
   }

   public final class NamespaceAttributeIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
      private final int _nsType;

      public NamespaceAttributeIterator(int nsType) {
         super();
         this._nsType = nsType;
      }

      public DTMAxisIterator setStartNode(int node) {
         if (node == 0) {
            node = SAXImpl.this.getDocument();
         }

         if (!super._isRestartable) {
            return this;
         } else {
            int nsType = this._nsType;
            super._startNode = node;

            for(node = SAXImpl.this.getFirstAttribute(node); node != -1 && SAXImpl.this.getNSType(node) != nsType; node = SAXImpl.this.getNextAttribute(node)) {
            }

            super._currentNode = node;
            return this.resetPosition();
         }
      }

      public int next() {
         int node = super._currentNode;
         int nsType = this._nsType;
         if (node == -1) {
            return -1;
         } else {
            int nextNode;
            for(nextNode = SAXImpl.this.getNextAttribute(node); nextNode != -1 && SAXImpl.this.getNSType(nextNode) != nsType; nextNode = SAXImpl.this.getNextAttribute(nextNode)) {
            }

            super._currentNode = nextNode;
            return this.returnNode(node);
         }
      }
   }

   public final class NamespaceChildrenIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
      private final int _nsType;

      public NamespaceChildrenIterator(int type) {
         super();
         this._nsType = type;
      }

      public DTMAxisIterator setStartNode(int node) {
         if (node == 0) {
            node = SAXImpl.this.getDocument();
         }

         if (super._isRestartable) {
            super._startNode = node;
            super._currentNode = node == -1 ? -1 : -2;
            return this.resetPosition();
         } else {
            return this;
         }
      }

      public int next() {
         if (super._currentNode != -1) {
            for(int node = -2 == super._currentNode ? SAXImpl.super._firstch(SAXImpl.this.makeNodeIdentity(super._startNode)) : SAXImpl.super._nextsib(super._currentNode); node != -1; node = SAXImpl.super._nextsib(node)) {
               int nodeHandle = SAXImpl.this.makeNodeHandle(node);
               if (SAXImpl.this.getNSType(nodeHandle) == this._nsType) {
                  super._currentNode = node;
                  return this.returnNode(nodeHandle);
               }
            }
         }

         return -1;
      }
   }

   public final class NamespaceWildcardIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
      protected int m_nsType;
      protected DTMAxisIterator m_baseIterator;

      public NamespaceWildcardIterator(int axis, int nsType) {
         super();
         this.m_nsType = nsType;
         switch (axis) {
            case 2:
               this.m_baseIterator = SAXImpl.this.getAxisIterator(axis);
            case 9:
               this.m_baseIterator = SAXImpl.this.getAxisIterator(axis);
            default:
               this.m_baseIterator = SAXImpl.this.getTypedAxisIterator(axis, 1);
         }
      }

      public DTMAxisIterator setStartNode(int node) {
         if (super._isRestartable) {
            super._startNode = node;
            this.m_baseIterator.setStartNode(node);
            this.resetPosition();
         }

         return this;
      }

      public int next() {
         int node;
         do {
            if ((node = this.m_baseIterator.next()) == -1) {
               return -1;
            }
         } while(SAXImpl.this.getNSType(node) != this.m_nsType);

         return this.returnNode(node);
      }

      public DTMAxisIterator cloneIterator() {
         try {
            DTMAxisIterator nestedClone = this.m_baseIterator.cloneIterator();
            NamespaceWildcardIterator clone = (NamespaceWildcardIterator)super.clone();
            clone.m_baseIterator = nestedClone;
            clone.m_nsType = this.m_nsType;
            clone._isRestartable = false;
            return clone;
         } catch (CloneNotSupportedException var3) {
            BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", (Object)var3.toString());
            return null;
         }
      }

      public boolean isReverse() {
         return this.m_baseIterator.isReverse();
      }

      public void setMark() {
         this.m_baseIterator.setMark();
      }

      public void gotoMark() {
         this.m_baseIterator.gotoMark();
      }
   }

   private final class NodeValueIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
      private DTMAxisIterator _source;
      private String _value;
      private boolean _op;
      private final boolean _isReverse;
      private int _returnType = 1;

      public NodeValueIterator(DTMAxisIterator source, int returnType, String value, boolean op) {
         super();
         this._source = source;
         this._returnType = returnType;
         this._value = value;
         this._op = op;
         this._isReverse = source.isReverse();
      }

      public boolean isReverse() {
         return this._isReverse;
      }

      public DTMAxisIterator cloneIterator() {
         try {
            NodeValueIterator clone = (NodeValueIterator)super.clone();
            clone._isRestartable = false;
            clone._source = this._source.cloneIterator();
            clone._value = this._value;
            clone._op = this._op;
            return clone.reset();
         } catch (CloneNotSupportedException var2) {
            BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", (Object)var2.toString());
            return null;
         }
      }

      public void setRestartable(boolean isRestartable) {
         super._isRestartable = isRestartable;
         this._source.setRestartable(isRestartable);
      }

      public DTMAxisIterator reset() {
         this._source.reset();
         return this.resetPosition();
      }

      public int next() {
         int node;
         String val;
         do {
            if ((node = this._source.next()) == -1) {
               return -1;
            }

            val = SAXImpl.this.getStringValueX(node);
         } while(this._value.equals(val) != this._op);

         if (this._returnType == 0) {
            return this.returnNode(node);
         } else {
            return this.returnNode(SAXImpl.this.getParent(node));
         }
      }

      public DTMAxisIterator setStartNode(int node) {
         if (super._isRestartable) {
            this._source.setStartNode(super._startNode = node);
            return this.resetPosition();
         } else {
            return this;
         }
      }

      public void setMark() {
         this._source.setMark();
      }

      public void gotoMark() {
         this._source.gotoMark();
      }
   }

   public class TypedNamespaceIterator extends DTMDefaultBaseIterators.NamespaceIterator {
      private String _nsPrefix;

      public TypedNamespaceIterator(int nodeType) {
         super();
         if (SAXImpl.access$001(SAXImpl.this) != null) {
            this._nsPrefix = SAXImpl.access$101(SAXImpl.this).getLocalName(nodeType);
         }

      }

      public int next() {
         if (this._nsPrefix != null && this._nsPrefix.length() != 0) {
            int node = true;

            for(int nodex = super.next(); nodex != -1; nodex = super.next()) {
               if (this._nsPrefix.compareTo(SAXImpl.this.getLocalName(nodex)) == 0) {
                  return this.returnNode(nodex);
               }
            }

            return -1;
         } else {
            return -1;
         }
      }
   }
}

package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMEnhancedForDTM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.serializer.SerializationHandler;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class DOMAdapter implements DOM {
   private DOMEnhancedForDTM _enhancedDOM;
   private DOM _dom;
   private String[] _namesArray;
   private String[] _urisArray;
   private int[] _typesArray;
   private String[] _namespaceArray;
   private short[] _mapping = null;
   private int[] _reverse = null;
   private short[] _NSmapping = null;
   private short[] _NSreverse = null;
   private StripFilter _filter = null;
   private int _multiDOMMask;

   public DOMAdapter(DOM dom, String[] namesArray, String[] urisArray, int[] typesArray, String[] namespaceArray) {
      if (dom instanceof DOMEnhancedForDTM) {
         this._enhancedDOM = (DOMEnhancedForDTM)dom;
      }

      this._dom = dom;
      this._namesArray = namesArray;
      this._urisArray = urisArray;
      this._typesArray = typesArray;
      this._namespaceArray = namespaceArray;
   }

   public void setupMapping(String[] names, String[] urisArray, int[] typesArray, String[] namespaces) {
      this._namesArray = names;
      this._urisArray = urisArray;
      this._typesArray = typesArray;
      this._namespaceArray = namespaces;
   }

   public String[] getNamesArray() {
      return this._namesArray;
   }

   public String[] getUrisArray() {
      return this._urisArray;
   }

   public int[] getTypesArray() {
      return this._typesArray;
   }

   public String[] getNamespaceArray() {
      return this._namespaceArray;
   }

   public DOM getDOMImpl() {
      return this._dom;
   }

   private short[] getMapping() {
      if (this._mapping == null && this._enhancedDOM != null) {
         this._mapping = this._enhancedDOM.getMapping(this._namesArray, this._urisArray, this._typesArray);
      }

      return this._mapping;
   }

   private int[] getReverse() {
      if (this._reverse == null && this._enhancedDOM != null) {
         this._reverse = this._enhancedDOM.getReverseMapping(this._namesArray, this._urisArray, this._typesArray);
      }

      return this._reverse;
   }

   private short[] getNSMapping() {
      if (this._NSmapping == null && this._enhancedDOM != null) {
         this._NSmapping = this._enhancedDOM.getNamespaceMapping(this._namespaceArray);
      }

      return this._NSmapping;
   }

   private short[] getNSReverse() {
      if (this._NSreverse == null && this._enhancedDOM != null) {
         this._NSreverse = this._enhancedDOM.getReverseNamespaceMapping(this._namespaceArray);
      }

      return this._NSreverse;
   }

   public DTMAxisIterator getIterator() {
      return this._dom.getIterator();
   }

   public String getStringValue() {
      return this._dom.getStringValue();
   }

   public DTMAxisIterator getChildren(int node) {
      if (this._enhancedDOM != null) {
         return this._enhancedDOM.getChildren(node);
      } else {
         DTMAxisIterator iterator = this._dom.getChildren(node);
         return iterator.setStartNode(node);
      }
   }

   public void setFilter(StripFilter filter) {
      this._filter = filter;
   }

   public DTMAxisIterator getTypedChildren(int type) {
      int[] reverse = this.getReverse();
      return this._enhancedDOM != null ? this._enhancedDOM.getTypedChildren(reverse[type]) : this._dom.getTypedChildren(type);
   }

   public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns) {
      return this._dom.getNamespaceAxisIterator(axis, this.getNSReverse()[ns]);
   }

   public DTMAxisIterator getAxisIterator(int axis) {
      return this._enhancedDOM != null ? this._enhancedDOM.getAxisIterator(axis) : this._dom.getAxisIterator(axis);
   }

   public DTMAxisIterator getTypedAxisIterator(int axis, int type) {
      int[] reverse = this.getReverse();
      return this._enhancedDOM != null ? this._enhancedDOM.getTypedAxisIterator(axis, reverse[type]) : this._dom.getTypedAxisIterator(axis, type);
   }

   public int getMultiDOMMask() {
      return this._multiDOMMask;
   }

   public void setMultiDOMMask(int mask) {
      this._multiDOMMask = mask;
   }

   public DTMAxisIterator getNthDescendant(int type, int n, boolean includeself) {
      return this._dom.getNthDescendant(this.getReverse()[type], n, includeself);
   }

   public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iterator, int type, String value, boolean op) {
      return this._dom.getNodeValueIterator(iterator, type, value, op);
   }

   public DTMAxisIterator orderNodes(DTMAxisIterator source, int node) {
      return this._dom.orderNodes(source, node);
   }

   public int getExpandedTypeID(int node) {
      short[] mapping = this.getMapping();
      int type;
      if (this._enhancedDOM != null) {
         type = mapping[this._enhancedDOM.getExpandedTypeID2(node)];
      } else if (null != mapping) {
         type = mapping[this._dom.getExpandedTypeID(node)];
      } else {
         type = this._dom.getExpandedTypeID(node);
      }

      return type;
   }

   public int getNamespaceType(int node) {
      return this.getNSMapping()[this._dom.getNSType(node)];
   }

   public int getNSType(int node) {
      return this._dom.getNSType(node);
   }

   public int getParent(int node) {
      return this._dom.getParent(node);
   }

   public int getAttributeNode(int type, int element) {
      return this._dom.getAttributeNode(this.getReverse()[type], element);
   }

   public String getNodeName(int node) {
      return node == -1 ? "" : this._dom.getNodeName(node);
   }

   public String getNodeNameX(int node) {
      return node == -1 ? "" : this._dom.getNodeNameX(node);
   }

   public String getNamespaceName(int node) {
      return node == -1 ? "" : this._dom.getNamespaceName(node);
   }

   public String getStringValueX(int node) {
      if (this._enhancedDOM != null) {
         return this._enhancedDOM.getStringValueX(node);
      } else {
         return node == -1 ? "" : this._dom.getStringValueX(node);
      }
   }

   public void copy(int node, SerializationHandler handler) throws TransletException {
      this._dom.copy(node, handler);
   }

   public void copy(DTMAxisIterator nodes, SerializationHandler handler) throws TransletException {
      this._dom.copy(nodes, handler);
   }

   public String shallowCopy(int node, SerializationHandler handler) throws TransletException {
      return this._enhancedDOM != null ? this._enhancedDOM.shallowCopy(node, handler) : this._dom.shallowCopy(node, handler);
   }

   public boolean lessThan(int node1, int node2) {
      return this._dom.lessThan(node1, node2);
   }

   public void characters(int textNode, SerializationHandler handler) throws TransletException {
      if (this._enhancedDOM != null) {
         this._enhancedDOM.characters(textNode, handler);
      } else {
         this._dom.characters(textNode, handler);
      }

   }

   public Node makeNode(int index) {
      return this._dom.makeNode(index);
   }

   public Node makeNode(DTMAxisIterator iter) {
      return this._dom.makeNode(iter);
   }

   public NodeList makeNodeList(int index) {
      return this._dom.makeNodeList(index);
   }

   public NodeList makeNodeList(DTMAxisIterator iter) {
      return this._dom.makeNodeList(iter);
   }

   public String getLanguage(int node) {
      return this._dom.getLanguage(node);
   }

   public int getSize() {
      return this._dom.getSize();
   }

   public void setDocumentURI(String uri) {
      if (this._enhancedDOM != null) {
         this._enhancedDOM.setDocumentURI(uri);
      }

   }

   public String getDocumentURI() {
      return this._enhancedDOM != null ? this._enhancedDOM.getDocumentURI() : "";
   }

   public String getDocumentURI(int node) {
      return this._dom.getDocumentURI(node);
   }

   public int getDocument() {
      return this._dom.getDocument();
   }

   public boolean isElement(int node) {
      return this._dom.isElement(node);
   }

   public boolean isAttribute(int node) {
      return this._dom.isAttribute(node);
   }

   public int getNodeIdent(int nodeHandle) {
      return this._dom.getNodeIdent(nodeHandle);
   }

   public int getNodeHandle(int nodeId) {
      return this._dom.getNodeHandle(nodeId);
   }

   public DOM getResultTreeFrag(int initSize, int rtfType) {
      return this._enhancedDOM != null ? this._enhancedDOM.getResultTreeFrag(initSize, rtfType) : this._dom.getResultTreeFrag(initSize, rtfType);
   }

   public DOM getResultTreeFrag(int initSize, int rtfType, boolean addToManager) {
      return this._enhancedDOM != null ? this._enhancedDOM.getResultTreeFrag(initSize, rtfType, addToManager) : this._dom.getResultTreeFrag(initSize, rtfType, addToManager);
   }

   public SerializationHandler getOutputDomBuilder() {
      return this._dom.getOutputDomBuilder();
   }

   public String lookupNamespace(int node, String prefix) throws TransletException {
      return this._dom.lookupNamespace(node, prefix);
   }

   public String getUnparsedEntityURI(String entity) {
      return this._dom.getUnparsedEntityURI(entity);
   }

   public Hashtable getElementsWithIDs() {
      return this._dom.getElementsWithIDs();
   }
}

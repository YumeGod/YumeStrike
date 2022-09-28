package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xml.dtm.Axis;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;
import org.apache.xml.dtm.ref.DTMDefaultBase;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.SuballocatedIntVector;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class MultiDOM implements DOM {
   private static final int NO_TYPE = -2;
   private static final int INITIAL_SIZE = 4;
   private DOM[] _adapters = new DOM[4];
   private DOMAdapter _main;
   private DTMManager _dtmManager;
   private int _free = 1;
   private int _size = 4;
   private Hashtable _documents = new Hashtable();

   public MultiDOM(DOM main) {
      DOMAdapter adapter = (DOMAdapter)main;
      this._adapters[0] = adapter;
      this._main = adapter;
      DOM dom = adapter.getDOMImpl();
      if (dom instanceof DTMDefaultBase) {
         this._dtmManager = ((DTMDefaultBase)dom).getManager();
      }

      this.addDOMAdapter(adapter, false);
   }

   public int nextMask() {
      return this._free;
   }

   public void setupMapping(String[] names, String[] uris, int[] types, String[] namespaces) {
   }

   public int addDOMAdapter(DOMAdapter adapter) {
      return this.addDOMAdapter(adapter, true);
   }

   private int addDOMAdapter(DOMAdapter adapter, boolean indexByURI) {
      DOM dom = adapter.getDOMImpl();
      int domNo = 1;
      int dtmSize = 1;
      SuballocatedIntVector dtmIds = null;
      if (dom instanceof DTMDefaultBase) {
         DTMDefaultBase dtmdb = (DTMDefaultBase)dom;
         dtmIds = dtmdb.getDTMIDs();
         dtmSize = dtmIds.size();
         domNo = dtmIds.elementAt(dtmSize - 1) >>> 16;
      } else if (dom instanceof SimpleResultTreeImpl) {
         SimpleResultTreeImpl simpleRTF = (SimpleResultTreeImpl)dom;
         domNo = simpleRTF.getDocument() >>> 16;
      }

      int domPos;
      if (domNo >= this._size) {
         domPos = this._size;

         do {
            this._size *= 2;
         } while(this._size <= domNo);

         DOMAdapter[] newArray = new DOMAdapter[this._size];
         System.arraycopy(this._adapters, 0, newArray, 0, domPos);
         this._adapters = newArray;
      }

      this._free = domNo + 1;
      if (dtmSize == 1) {
         this._adapters[domNo] = adapter;
      } else if (dtmIds != null) {
         domPos = 0;

         for(int i = dtmSize - 1; i >= 0; --i) {
            domPos = dtmIds.elementAt(i) >>> 16;
            this._adapters[domPos] = adapter;
         }

         domNo = domPos;
      }

      if (indexByURI) {
         String uri = adapter.getDocumentURI(0);
         this._documents.put(uri, new Integer(domNo));
      }

      if (dom instanceof AdaptiveResultTreeImpl) {
         AdaptiveResultTreeImpl adaptiveRTF = (AdaptiveResultTreeImpl)dom;
         DOM nestedDom = adaptiveRTF.getNestedDOM();
         if (nestedDom != null) {
            DOMAdapter newAdapter = new DOMAdapter(nestedDom, adapter.getNamesArray(), adapter.getUrisArray(), adapter.getTypesArray(), adapter.getNamespaceArray());
            this.addDOMAdapter(newAdapter);
         }
      }

      return domNo;
   }

   public int getDocumentMask(String uri) {
      Integer domIdx = (Integer)this._documents.get(uri);
      return domIdx == null ? -1 : domIdx;
   }

   public DOM getDOMAdapter(String uri) {
      Integer domIdx = (Integer)this._documents.get(uri);
      return domIdx == null ? null : this._adapters[domIdx];
   }

   public int getDocument() {
      return this._main.getDocument();
   }

   public DTMManager getDTMManager() {
      return this._dtmManager;
   }

   public DTMAxisIterator getIterator() {
      return this._main.getIterator();
   }

   public String getStringValue() {
      return this._main.getStringValue();
   }

   public DTMAxisIterator getChildren(int node) {
      return this._adapters[this.getDTMId(node)].getChildren(node);
   }

   public DTMAxisIterator getTypedChildren(int type) {
      return new AxisIterator(3, type);
   }

   public DTMAxisIterator getAxisIterator(int axis) {
      return new AxisIterator(axis, -2);
   }

   public DTMAxisIterator getTypedAxisIterator(int axis, int type) {
      return new AxisIterator(axis, type);
   }

   public DTMAxisIterator getNthDescendant(int node, int n, boolean includeself) {
      return this._adapters[this.getDTMId(node)].getNthDescendant(node, n, includeself);
   }

   public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iterator, int type, String value, boolean op) {
      return new NodeValueIterator(iterator, type, value, op);
   }

   public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns) {
      DTMAxisIterator iterator = this._main.getNamespaceAxisIterator(axis, ns);
      return iterator;
   }

   public DTMAxisIterator orderNodes(DTMAxisIterator source, int node) {
      return this._adapters[this.getDTMId(node)].orderNodes(source, node);
   }

   public int getExpandedTypeID(int node) {
      return node != -1 ? this._adapters[node >>> 16].getExpandedTypeID(node) : -1;
   }

   public int getNamespaceType(int node) {
      return this._adapters[this.getDTMId(node)].getNamespaceType(node);
   }

   public int getNSType(int node) {
      return this._adapters[this.getDTMId(node)].getNSType(node);
   }

   public int getParent(int node) {
      return node == -1 ? -1 : this._adapters[node >>> 16].getParent(node);
   }

   public int getAttributeNode(int type, int el) {
      return el == -1 ? -1 : this._adapters[el >>> 16].getAttributeNode(type, el);
   }

   public String getNodeName(int node) {
      return node == -1 ? "" : this._adapters[node >>> 16].getNodeName(node);
   }

   public String getNodeNameX(int node) {
      return node == -1 ? "" : this._adapters[node >>> 16].getNodeNameX(node);
   }

   public String getNamespaceName(int node) {
      return node == -1 ? "" : this._adapters[node >>> 16].getNamespaceName(node);
   }

   public String getStringValueX(int node) {
      return node == -1 ? "" : this._adapters[node >>> 16].getStringValueX(node);
   }

   public void copy(int node, SerializationHandler handler) throws TransletException {
      if (node != -1) {
         this._adapters[node >>> 16].copy(node, handler);
      }

   }

   public void copy(DTMAxisIterator nodes, SerializationHandler handler) throws TransletException {
      int node;
      while((node = nodes.next()) != -1) {
         this._adapters[node >>> 16].copy(node, handler);
      }

   }

   public String shallowCopy(int node, SerializationHandler handler) throws TransletException {
      return node == -1 ? "" : this._adapters[node >>> 16].shallowCopy(node, handler);
   }

   public boolean lessThan(int node1, int node2) {
      if (node1 == -1) {
         return true;
      } else if (node2 == -1) {
         return false;
      } else {
         int dom1 = this.getDTMId(node1);
         int dom2 = this.getDTMId(node2);
         return dom1 == dom2 ? this._adapters[dom1].lessThan(node1, node2) : dom1 < dom2;
      }
   }

   public void characters(int textNode, SerializationHandler handler) throws TransletException {
      if (textNode != -1) {
         this._adapters[textNode >>> 16].characters(textNode, handler);
      }

   }

   public void setFilter(StripFilter filter) {
      for(int dom = 0; dom < this._free; ++dom) {
         if (this._adapters[dom] != null) {
            this._adapters[dom].setFilter(filter);
         }
      }

   }

   public Node makeNode(int index) {
      return index == -1 ? null : this._adapters[this.getDTMId(index)].makeNode(index);
   }

   public Node makeNode(DTMAxisIterator iter) {
      return this._main.makeNode(iter);
   }

   public NodeList makeNodeList(int index) {
      return index == -1 ? null : this._adapters[this.getDTMId(index)].makeNodeList(index);
   }

   public NodeList makeNodeList(DTMAxisIterator iter) {
      return this._main.makeNodeList(iter);
   }

   public String getLanguage(int node) {
      return this._adapters[this.getDTMId(node)].getLanguage(node);
   }

   public int getSize() {
      int size = 0;

      for(int i = 0; i < this._size; ++i) {
         size += this._adapters[i].getSize();
      }

      return size;
   }

   public String getDocumentURI(int node) {
      if (node == -1) {
         node = 0;
      }

      return this._adapters[node >>> 16].getDocumentURI(0);
   }

   public boolean isElement(int node) {
      return node == -1 ? false : this._adapters[node >>> 16].isElement(node);
   }

   public boolean isAttribute(int node) {
      return node == -1 ? false : this._adapters[node >>> 16].isAttribute(node);
   }

   public int getDTMId(int nodeHandle) {
      if (nodeHandle == -1) {
         return 0;
      } else {
         int id;
         for(id = nodeHandle >>> 16; id >= 2 && this._adapters[id] == this._adapters[id - 1]; --id) {
         }

         return id;
      }
   }

   public int getNodeIdent(int nodeHandle) {
      return this._adapters[nodeHandle >>> 16].getNodeIdent(nodeHandle);
   }

   public int getNodeHandle(int nodeId) {
      return this._main.getNodeHandle(nodeId);
   }

   public DOM getResultTreeFrag(int initSize, int rtfType) {
      return this._main.getResultTreeFrag(initSize, rtfType);
   }

   public DOM getResultTreeFrag(int initSize, int rtfType, boolean addToManager) {
      return this._main.getResultTreeFrag(initSize, rtfType, addToManager);
   }

   public DOM getMain() {
      return this._main;
   }

   public SerializationHandler getOutputDomBuilder() {
      return this._main.getOutputDomBuilder();
   }

   public String lookupNamespace(int node, String prefix) throws TransletException {
      return this._main.lookupNamespace(node, prefix);
   }

   public String getUnparsedEntityURI(String entity) {
      return this._main.getUnparsedEntityURI(entity);
   }

   public Hashtable getElementsWithIDs() {
      return this._main.getElementsWithIDs();
   }

   private final class NodeValueIterator extends DTMAxisIteratorBase {
      private DTMAxisIterator _source;
      private String _value;
      private boolean _op;
      private final boolean _isReverse;
      private int _returnType = 1;

      public NodeValueIterator(DTMAxisIterator source, int returnType, String value, boolean op) {
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
            clone._source = this._source.cloneIterator();
            clone.setRestartable(false);
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

            val = MultiDOM.this.getStringValueX(node);
         } while(this._value.equals(val) != this._op);

         if (this._returnType == 0) {
            return this.returnNode(node);
         } else {
            return this.returnNode(MultiDOM.this.getParent(node));
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

   private final class AxisIterator extends DTMAxisIteratorBase {
      private final int _axis;
      private final int _type;
      private DTMAxisIterator _source;
      private int _dtmId = -1;

      public AxisIterator(int axis, int type) {
         this._axis = axis;
         this._type = type;
      }

      public int next() {
         return this._source == null ? -1 : this._source.next();
      }

      public void setRestartable(boolean flag) {
         if (this._source != null) {
            this._source.setRestartable(flag);
         }

      }

      public DTMAxisIterator setStartNode(int node) {
         if (node == -1) {
            return this;
         } else {
            int dom = node >>> 16;
            if (this._source == null || this._dtmId != dom) {
               if (this._type == -2) {
                  this._source = MultiDOM.this._adapters[dom].getAxisIterator(this._axis);
               } else if (this._axis == 3) {
                  this._source = MultiDOM.this._adapters[dom].getTypedChildren(this._type);
               } else {
                  this._source = MultiDOM.this._adapters[dom].getTypedAxisIterator(this._axis, this._type);
               }
            }

            this._dtmId = dom;
            this._source.setStartNode(node);
            return this;
         }
      }

      public DTMAxisIterator reset() {
         if (this._source != null) {
            this._source.reset();
         }

         return this;
      }

      public int getLast() {
         return this._source != null ? this._source.getLast() : -1;
      }

      public int getPosition() {
         return this._source != null ? this._source.getPosition() : -1;
      }

      public boolean isReverse() {
         return Axis.isReverse(this._axis);
      }

      public void setMark() {
         if (this._source != null) {
            this._source.setMark();
         }

      }

      public void gotoMark() {
         if (this._source != null) {
            this._source.gotoMark();
         }

      }

      public DTMAxisIterator cloneIterator() {
         AxisIterator clone = MultiDOM.this.new AxisIterator(this._axis, this._type);
         if (this._source != null) {
            clone._source = this._source.cloneIterator();
         }

         clone._dtmId = this._dtmId;
         return clone;
      }
   }
}

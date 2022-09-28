package org.apache.xalan.xsltc.dom;

import java.util.StringTokenizer;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMEnhancedForDTM;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xalan.xsltc.util.IntegerArray;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public class KeyIndex extends DTMAxisIteratorBase {
   private Hashtable _index = new Hashtable();
   private IntegerArray _nodes = null;
   private DOM _dom;
   private DOMEnhancedForDTM _enhancedDOM;
   private int _markedPosition = 0;

   public KeyIndex(int dummy) {
   }

   public void setRestartable(boolean flag) {
   }

   public void add(Object value, int node) {
      IntegerArray nodes = (IntegerArray)this._index.get(value);
      if (nodes == null) {
         nodes = new IntegerArray();
         this._index.put(value, nodes);
      }

      nodes.add(node);
   }

   public void merge(KeyIndex other) {
      if (other != null) {
         if (other._nodes != null) {
            if (this._nodes == null) {
               this._nodes = (IntegerArray)other._nodes.clone();
            } else {
               this._nodes.merge(other._nodes);
            }
         }

      }
   }

   public void lookupId(Object value) {
      this._nodes = null;
      StringTokenizer values = new StringTokenizer((String)value);

      while(values.hasMoreElements()) {
         String token = (String)values.nextElement();
         IntegerArray nodes = (IntegerArray)this._index.get(token);
         if (nodes == null && this._enhancedDOM != null && this._enhancedDOM.hasDOMSource()) {
            nodes = this.getDOMNodeById(token);
         }

         if (nodes != null) {
            if (this._nodes == null) {
               nodes = (IntegerArray)nodes.clone();
               this._nodes = nodes;
            } else {
               this._nodes.merge(nodes);
            }
         }
      }

   }

   public IntegerArray getDOMNodeById(String id) {
      IntegerArray nodes = null;
      if (this._enhancedDOM != null) {
         int ident = this._enhancedDOM.getElementById(id);
         if (ident != -1) {
            nodes = new IntegerArray();
            this._index.put(id, nodes);
            nodes.add(ident);
         }
      }

      return nodes;
   }

   public void lookupKey(Object value) {
      IntegerArray nodes = (IntegerArray)this._index.get(value);
      this._nodes = nodes != null ? (IntegerArray)nodes.clone() : null;
      super._position = 0;
   }

   public int next() {
      if (this._nodes == null) {
         return -1;
      } else {
         return super._position < this._nodes.cardinality() ? this._dom.getNodeHandle(this._nodes.at(super._position++)) : -1;
      }
   }

   public int containsID(int node, Object value) {
      String string = (String)value;
      if (string.indexOf(32) > -1) {
         StringTokenizer values = new StringTokenizer(string);

         IntegerArray nodes;
         do {
            if (!values.hasMoreElements()) {
               return 0;
            }

            String token = (String)values.nextElement();
            nodes = (IntegerArray)this._index.get(token);
            if (nodes == null && this._enhancedDOM != null && this._enhancedDOM.hasDOMSource()) {
               nodes = this.getDOMNodeById(token);
            }
         } while(nodes == null || nodes.indexOf(node) < 0);

         return 1;
      } else {
         IntegerArray nodes = (IntegerArray)this._index.get(value);
         if (nodes == null && this._enhancedDOM != null && this._enhancedDOM.hasDOMSource()) {
            nodes = this.getDOMNodeById(string);
         }

         return nodes != null && nodes.indexOf(node) >= 0 ? 1 : 0;
      }
   }

   public int containsKey(int node, Object value) {
      IntegerArray nodes = (IntegerArray)this._index.get(value);
      return nodes != null && nodes.indexOf(node) >= 0 ? 1 : 0;
   }

   public DTMAxisIterator reset() {
      super._position = 0;
      return this;
   }

   public int getLast() {
      return this._nodes == null ? 0 : this._nodes.cardinality();
   }

   public int getPosition() {
      return super._position;
   }

   public void setMark() {
      this._markedPosition = super._position;
   }

   public void gotoMark() {
      super._position = this._markedPosition;
   }

   public DTMAxisIterator setStartNode(int start) {
      if (start == -1) {
         this._nodes = null;
      } else if (this._nodes != null) {
         super._position = 0;
      }

      return this;
   }

   public int getStartNode() {
      return 0;
   }

   public boolean isReverse() {
      return false;
   }

   public DTMAxisIterator cloneIterator() {
      KeyIndex other = new KeyIndex(0);
      other._index = this._index;
      other._nodes = this._nodes;
      other._position = super._position;
      return other;
   }

   public void setDom(DOM dom) {
      this._dom = dom;
      if (dom instanceof DOMEnhancedForDTM) {
         this._enhancedDOM = (DOMEnhancedForDTM)dom;
      } else if (dom instanceof DOMAdapter) {
         DOM idom = ((DOMAdapter)dom).getDOMImpl();
         if (idom instanceof DOMEnhancedForDTM) {
            this._enhancedDOM = (DOMEnhancedForDTM)idom;
         }
      }

   }
}

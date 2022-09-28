package org.apache.xpath;

import org.apache.xml.utils.DOM2Helper;
import org.apache.xpath.axes.ContextNodeList;
import org.apache.xpath.res.XPATHMessages;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

public class NodeSet implements NodeList, NodeIterator, Cloneable, ContextNodeList {
   protected transient int m_next;
   protected transient boolean m_mutable;
   protected transient boolean m_cacheNodes;
   private transient int m_last;
   private int m_blocksize;
   Node[] m_map;
   protected int m_firstFree;
   private int m_mapSize;

   public NodeSet() {
      this.m_next = 0;
      this.m_mutable = true;
      this.m_cacheNodes = true;
      this.m_last = 0;
      this.m_firstFree = 0;
      this.m_blocksize = 32;
      this.m_mapSize = 0;
   }

   public NodeSet(int blocksize) {
      this.m_next = 0;
      this.m_mutable = true;
      this.m_cacheNodes = true;
      this.m_last = 0;
      this.m_firstFree = 0;
      this.m_blocksize = blocksize;
      this.m_mapSize = 0;
   }

   public NodeSet(NodeList nodelist) {
      this(32);
      this.addNodes(nodelist);
   }

   public NodeSet(NodeSet nodelist) {
      this(32);
      this.addNodes((NodeIterator)nodelist);
   }

   public NodeSet(NodeIterator ni) {
      this(32);
      this.addNodes(ni);
   }

   public NodeSet(Node node) {
      this(32);
      this.addNode(node);
   }

   public Node getRoot() {
      return null;
   }

   public NodeIterator cloneWithReset() throws CloneNotSupportedException {
      NodeSet clone = (NodeSet)this.clone();
      clone.reset();
      return clone;
   }

   public void reset() {
      this.m_next = 0;
   }

   public int getWhatToShow() {
      return -17;
   }

   public NodeFilter getFilter() {
      return null;
   }

   public boolean getExpandEntityReferences() {
      return true;
   }

   public Node nextNode() throws DOMException {
      if (this.m_next < this.size()) {
         Node next = this.elementAt(this.m_next);
         ++this.m_next;
         return next;
      } else {
         return null;
      }
   }

   public Node previousNode() throws DOMException {
      if (!this.m_cacheNodes) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_CANNOT_ITERATE", (Object[])null));
      } else if (this.m_next - 1 > 0) {
         --this.m_next;
         return this.elementAt(this.m_next);
      } else {
         return null;
      }
   }

   public void detach() {
   }

   public boolean isFresh() {
      return this.m_next == 0;
   }

   public void runTo(int index) {
      if (!this.m_cacheNodes) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_CANNOT_INDEX", (Object[])null));
      } else {
         if (index >= 0 && this.m_next < this.m_firstFree) {
            this.m_next = index;
         } else {
            this.m_next = this.m_firstFree - 1;
         }

      }
   }

   public Node item(int index) {
      this.runTo(index);
      return this.elementAt(index);
   }

   public int getLength() {
      this.runTo(-1);
      return this.size();
   }

   public void addNode(Node n) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", (Object[])null));
      } else {
         this.addElement(n);
      }
   }

   public void insertNode(Node n, int pos) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", (Object[])null));
      } else {
         this.insertElementAt(n, pos);
      }
   }

   public void removeNode(Node n) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", (Object[])null));
      } else {
         this.removeElement(n);
      }
   }

   public void addNodes(NodeList nodelist) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", (Object[])null));
      } else {
         if (null != nodelist) {
            int nChildren = nodelist.getLength();

            for(int i = 0; i < nChildren; ++i) {
               Node obj = nodelist.item(i);
               if (null != obj) {
                  this.addElement(obj);
               }
            }
         }

      }
   }

   public void addNodes(NodeSet ns) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", (Object[])null));
      } else {
         this.addNodes((NodeIterator)ns);
      }
   }

   public void addNodes(NodeIterator iterator) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", (Object[])null));
      } else {
         Node obj;
         if (null != iterator) {
            while(null != (obj = iterator.nextNode())) {
               this.addElement(obj);
            }
         }

      }
   }

   public void addNodesInDocOrder(NodeList nodelist, XPathContext support) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", (Object[])null));
      } else {
         int nChildren = nodelist.getLength();

         for(int i = 0; i < nChildren; ++i) {
            Node node = nodelist.item(i);
            if (null != node) {
               this.addNodeInDocOrder(node, support);
            }
         }

      }
   }

   public void addNodesInDocOrder(NodeIterator iterator, XPathContext support) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", (Object[])null));
      } else {
         Node node;
         while(null != (node = iterator.nextNode())) {
            this.addNodeInDocOrder(node, support);
         }

      }
   }

   private boolean addNodesInDocOrder(int start, int end, int testIndex, NodeList nodelist, XPathContext support) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", (Object[])null));
      } else {
         boolean foundit = false;
         Node node = nodelist.item(testIndex);

         int i;
         for(i = end; i >= start; --i) {
            Node child = this.elementAt(i);
            if (child == node) {
               i = -2;
               break;
            }

            if (!DOM2Helper.isNodeAfter(node, child)) {
               this.insertElementAt(node, i + 1);
               --testIndex;
               if (testIndex > 0) {
                  boolean foundPrev = this.addNodesInDocOrder(0, i, testIndex, nodelist, support);
                  if (!foundPrev) {
                     this.addNodesInDocOrder(i, this.size() - 1, testIndex, nodelist, support);
                  }
               }
               break;
            }
         }

         if (i == -1) {
            this.insertElementAt(node, 0);
         }

         return foundit;
      }
   }

   public int addNodeInDocOrder(Node node, boolean test, XPathContext support) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", (Object[])null));
      } else {
         int insertIndex = -1;
         int i;
         if (test) {
            int size = this.size();

            for(i = size - 1; i >= 0; --i) {
               Node child = this.elementAt(i);
               if (child == node) {
                  i = -2;
                  break;
               }

               if (!DOM2Helper.isNodeAfter(node, child)) {
                  break;
               }
            }

            if (i != -2) {
               insertIndex = i + 1;
               this.insertElementAt(node, insertIndex);
            }
         } else {
            insertIndex = this.size();
            boolean foundit = false;

            for(i = 0; i < insertIndex; ++i) {
               if (this.item(i).equals(node)) {
                  foundit = true;
                  break;
               }
            }

            if (!foundit) {
               this.addElement(node);
            }
         }

         return insertIndex;
      }
   }

   public int addNodeInDocOrder(Node node, XPathContext support) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", (Object[])null));
      } else {
         return this.addNodeInDocOrder(node, true, support);
      }
   }

   public int getCurrentPos() {
      return this.m_next;
   }

   public void setCurrentPos(int i) {
      if (!this.m_cacheNodes) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_CANNOT_INDEX", (Object[])null));
      } else {
         this.m_next = i;
      }
   }

   public Node getCurrentNode() {
      if (!this.m_cacheNodes) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_CANNOT_INDEX", (Object[])null));
      } else {
         int saved = this.m_next;
         Node n = this.m_next < this.m_firstFree ? this.elementAt(this.m_next) : null;
         this.m_next = saved;
         return n;
      }
   }

   public boolean getShouldCacheNodes() {
      return this.m_cacheNodes;
   }

   public void setShouldCacheNodes(boolean b) {
      if (!this.isFresh()) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_CANNOT_CALL_SETSHOULDCACHENODE", (Object[])null));
      } else {
         this.m_cacheNodes = b;
         this.m_mutable = true;
      }
   }

   public int getLast() {
      return this.m_last;
   }

   public void setLast(int last) {
      this.m_last = last;
   }

   public Object clone() throws CloneNotSupportedException {
      NodeSet clone = (NodeSet)super.clone();
      if (null != this.m_map && this.m_map == clone.m_map) {
         clone.m_map = new Node[this.m_map.length];
         System.arraycopy(this.m_map, 0, clone.m_map, 0, this.m_map.length);
      }

      return clone;
   }

   public int size() {
      return this.m_firstFree;
   }

   public void addElement(Node value) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", (Object[])null));
      } else {
         if (this.m_firstFree + 1 >= this.m_mapSize) {
            if (null == this.m_map) {
               this.m_map = new Node[this.m_blocksize];
               this.m_mapSize = this.m_blocksize;
            } else {
               this.m_mapSize += this.m_blocksize;
               Node[] newMap = new Node[this.m_mapSize];
               System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
               this.m_map = newMap;
            }
         }

         this.m_map[this.m_firstFree] = value;
         ++this.m_firstFree;
      }
   }

   public final void push(Node value) {
      int ff = this.m_firstFree;
      if (ff + 1 >= this.m_mapSize) {
         if (null == this.m_map) {
            this.m_map = new Node[this.m_blocksize];
            this.m_mapSize = this.m_blocksize;
         } else {
            this.m_mapSize += this.m_blocksize;
            Node[] newMap = new Node[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, ff + 1);
            this.m_map = newMap;
         }
      }

      this.m_map[ff] = value;
      ++ff;
      this.m_firstFree = ff;
   }

   public final Node pop() {
      --this.m_firstFree;
      Node n = this.m_map[this.m_firstFree];
      this.m_map[this.m_firstFree] = null;
      return n;
   }

   public final Node popAndTop() {
      --this.m_firstFree;
      this.m_map[this.m_firstFree] = null;
      return this.m_firstFree == 0 ? null : this.m_map[this.m_firstFree - 1];
   }

   public final void popQuick() {
      --this.m_firstFree;
      this.m_map[this.m_firstFree] = null;
   }

   public final Node peepOrNull() {
      return null != this.m_map && this.m_firstFree > 0 ? this.m_map[this.m_firstFree - 1] : null;
   }

   public final void pushPair(Node v1, Node v2) {
      if (null == this.m_map) {
         this.m_map = new Node[this.m_blocksize];
         this.m_mapSize = this.m_blocksize;
      } else if (this.m_firstFree + 2 >= this.m_mapSize) {
         this.m_mapSize += this.m_blocksize;
         Node[] newMap = new Node[this.m_mapSize];
         System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree);
         this.m_map = newMap;
      }

      this.m_map[this.m_firstFree] = v1;
      this.m_map[this.m_firstFree + 1] = v2;
      this.m_firstFree += 2;
   }

   public final void popPair() {
      this.m_firstFree -= 2;
      this.m_map[this.m_firstFree] = null;
      this.m_map[this.m_firstFree + 1] = null;
   }

   public final void setTail(Node n) {
      this.m_map[this.m_firstFree - 1] = n;
   }

   public final void setTailSub1(Node n) {
      this.m_map[this.m_firstFree - 2] = n;
   }

   public final Node peepTail() {
      return this.m_map[this.m_firstFree - 1];
   }

   public final Node peepTailSub1() {
      return this.m_map[this.m_firstFree - 2];
   }

   public void insertElementAt(Node value, int at) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", (Object[])null));
      } else {
         if (null == this.m_map) {
            this.m_map = new Node[this.m_blocksize];
            this.m_mapSize = this.m_blocksize;
         } else if (this.m_firstFree + 1 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            Node[] newMap = new Node[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
            this.m_map = newMap;
         }

         if (at <= this.m_firstFree - 1) {
            System.arraycopy(this.m_map, at, this.m_map, at + 1, this.m_firstFree - at);
         }

         this.m_map[at] = value;
         ++this.m_firstFree;
      }
   }

   public void appendNodes(NodeSet nodes) {
      int nNodes = nodes.size();
      if (null == this.m_map) {
         this.m_mapSize = nNodes + this.m_blocksize;
         this.m_map = new Node[this.m_mapSize];
      } else if (this.m_firstFree + nNodes >= this.m_mapSize) {
         this.m_mapSize += nNodes + this.m_blocksize;
         Node[] newMap = new Node[this.m_mapSize];
         System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + nNodes);
         this.m_map = newMap;
      }

      System.arraycopy(nodes.m_map, 0, this.m_map, this.m_firstFree, nNodes);
      this.m_firstFree += nNodes;
   }

   public void removeAllElements() {
      if (null != this.m_map) {
         for(int i = 0; i < this.m_firstFree; ++i) {
            this.m_map[i] = null;
         }

         this.m_firstFree = 0;
      }
   }

   public boolean removeElement(Node s) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", (Object[])null));
      } else if (null == this.m_map) {
         return false;
      } else {
         for(int i = 0; i < this.m_firstFree; ++i) {
            Node node = this.m_map[i];
            if (null != node && node.equals(s)) {
               if (i < this.m_firstFree - 1) {
                  System.arraycopy(this.m_map, i + 1, this.m_map, i, this.m_firstFree - i - 1);
               }

               --this.m_firstFree;
               this.m_map[this.m_firstFree] = null;
               return true;
            }
         }

         return false;
      }
   }

   public void removeElementAt(int i) {
      if (null != this.m_map) {
         if (i >= this.m_firstFree) {
            throw new ArrayIndexOutOfBoundsException(i + " >= " + this.m_firstFree);
         } else if (i < 0) {
            throw new ArrayIndexOutOfBoundsException(i);
         } else {
            if (i < this.m_firstFree - 1) {
               System.arraycopy(this.m_map, i + 1, this.m_map, i, this.m_firstFree - i - 1);
            }

            --this.m_firstFree;
            this.m_map[this.m_firstFree] = null;
         }
      }
   }

   public void setElementAt(Node node, int index) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", (Object[])null));
      } else {
         if (null == this.m_map) {
            this.m_map = new Node[this.m_blocksize];
            this.m_mapSize = this.m_blocksize;
         }

         this.m_map[index] = node;
      }
   }

   public Node elementAt(int i) {
      return null == this.m_map ? null : this.m_map[i];
   }

   public boolean contains(Node s) {
      this.runTo(-1);
      if (null == this.m_map) {
         return false;
      } else {
         for(int i = 0; i < this.m_firstFree; ++i) {
            Node node = this.m_map[i];
            if (null != node && node.equals(s)) {
               return true;
            }
         }

         return false;
      }
   }

   public int indexOf(Node elem, int index) {
      this.runTo(-1);
      if (null == this.m_map) {
         return -1;
      } else {
         for(int i = index; i < this.m_firstFree; ++i) {
            Node node = this.m_map[i];
            if (null != node && node.equals(elem)) {
               return i;
            }
         }

         return -1;
      }
   }

   public int indexOf(Node elem) {
      this.runTo(-1);
      if (null == this.m_map) {
         return -1;
      } else {
         for(int i = 0; i < this.m_firstFree; ++i) {
            Node node = this.m_map[i];
            if (null != node && node.equals(elem)) {
               return i;
            }
         }

         return -1;
      }
   }
}

package org.apache.xpath;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMFilter;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.utils.NodeVector;
import org.apache.xpath.res.XPATHMessages;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;

public class NodeSetDTM extends NodeVector implements DTMIterator, Cloneable {
   static final long serialVersionUID = 7686480133331317070L;
   DTMManager m_manager;
   protected transient int m_next = 0;
   protected transient boolean m_mutable = true;
   protected transient boolean m_cacheNodes = true;
   protected int m_root = -1;
   private transient int m_last = 0;

   public NodeSetDTM(DTMManager dtmManager) {
      this.m_manager = dtmManager;
   }

   public NodeSetDTM(int blocksize, int dummy, DTMManager dtmManager) {
      super(blocksize);
      this.m_manager = dtmManager;
   }

   public NodeSetDTM(NodeSetDTM nodelist) {
      this.m_manager = nodelist.getDTMManager();
      this.m_root = nodelist.getRoot();
      this.addNodes(nodelist);
   }

   public NodeSetDTM(DTMIterator ni) {
      this.m_manager = ni.getDTMManager();
      this.m_root = ni.getRoot();
      this.addNodes(ni);
   }

   public NodeSetDTM(NodeIterator iterator, XPathContext xctxt) {
      this.m_manager = xctxt.getDTMManager();

      Node node;
      while(null != (node = iterator.nextNode())) {
         int handle = xctxt.getDTMHandleFromNode(node);
         this.addNodeInDocOrder(handle, xctxt);
      }

   }

   public NodeSetDTM(NodeList nodeList, XPathContext xctxt) {
      this.m_manager = xctxt.getDTMManager();
      int n = nodeList.getLength();

      for(int i = 0; i < n; ++i) {
         Node node = nodeList.item(i);
         int handle = xctxt.getDTMHandleFromNode(node);
         this.addNode(handle);
      }

   }

   public NodeSetDTM(int node, DTMManager dtmManager) {
      this.m_manager = dtmManager;
      this.addNode(node);
   }

   public void setEnvironment(Object environment) {
   }

   public int getRoot() {
      if (-1 == this.m_root) {
         return this.size() > 0 ? this.item(0) : -1;
      } else {
         return this.m_root;
      }
   }

   public void setRoot(int context, Object environment) {
   }

   public Object clone() throws CloneNotSupportedException {
      NodeSetDTM clone = (NodeSetDTM)super.clone();
      return clone;
   }

   public DTMIterator cloneWithReset() throws CloneNotSupportedException {
      NodeSetDTM clone = (NodeSetDTM)this.clone();
      clone.reset();
      return clone;
   }

   public void reset() {
      this.m_next = 0;
   }

   public int getWhatToShow() {
      return -17;
   }

   public DTMFilter getFilter() {
      return null;
   }

   public boolean getExpandEntityReferences() {
      return true;
   }

   public DTM getDTM(int nodeHandle) {
      return this.m_manager.getDTM(nodeHandle);
   }

   public DTMManager getDTMManager() {
      return this.m_manager;
   }

   public int nextNode() {
      if (this.m_next < this.size()) {
         int next = this.elementAt(this.m_next);
         ++this.m_next;
         return next;
      } else {
         return -1;
      }
   }

   public int previousNode() {
      if (!this.m_cacheNodes) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESETDTM_CANNOT_ITERATE", (Object[])null));
      } else if (this.m_next - 1 > 0) {
         --this.m_next;
         return this.elementAt(this.m_next);
      } else {
         return -1;
      }
   }

   public void detach() {
   }

   public void allowDetachToRelease(boolean allowRelease) {
   }

   public boolean isFresh() {
      return this.m_next == 0;
   }

   public void runTo(int index) {
      if (!this.m_cacheNodes) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESETDTM_CANNOT_INDEX", (Object[])null));
      } else {
         if (index >= 0 && this.m_next < super.m_firstFree) {
            this.m_next = index;
         } else {
            this.m_next = super.m_firstFree - 1;
         }

      }
   }

   public int item(int index) {
      this.runTo(index);
      return this.elementAt(index);
   }

   public int getLength() {
      this.runTo(-1);
      return this.size();
   }

   public void addNode(int n) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", (Object[])null));
      } else {
         this.addElement(n);
      }
   }

   public void insertNode(int n, int pos) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", (Object[])null));
      } else {
         this.insertElementAt(n, pos);
      }
   }

   public void removeNode(int n) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", (Object[])null));
      } else {
         this.removeElement(n);
      }
   }

   public void addNodes(DTMIterator iterator) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", (Object[])null));
      } else {
         int obj;
         if (null != iterator) {
            while(-1 != (obj = iterator.nextNode())) {
               this.addElement(obj);
            }
         }

      }
   }

   public void addNodesInDocOrder(DTMIterator iterator, XPathContext support) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", (Object[])null));
      } else {
         int node;
         while(-1 != (node = iterator.nextNode())) {
            this.addNodeInDocOrder(node, support);
         }

      }
   }

   public int addNodeInDocOrder(int node, boolean test, XPathContext support) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", (Object[])null));
      } else {
         int insertIndex = -1;
         int i;
         if (test) {
            int size = this.size();

            for(i = size - 1; i >= 0; --i) {
               int child = this.elementAt(i);
               if (child == node) {
                  i = -2;
                  break;
               }

               DTM dtm = support.getDTM(node);
               if (!dtm.isNodeAfter(node, child)) {
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
               if (i == node) {
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

   public int addNodeInDocOrder(int node, XPathContext support) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", (Object[])null));
      } else {
         return this.addNodeInDocOrder(node, true, support);
      }
   }

   public int size() {
      return super.size();
   }

   public void addElement(int value) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", (Object[])null));
      } else {
         super.addElement(value);
      }
   }

   public void insertElementAt(int value, int at) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", (Object[])null));
      } else {
         super.insertElementAt(value, at);
      }
   }

   public void appendNodes(NodeVector nodes) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", (Object[])null));
      } else {
         super.appendNodes(nodes);
      }
   }

   public void removeAllElements() {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", (Object[])null));
      } else {
         super.removeAllElements();
      }
   }

   public boolean removeElement(int s) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", (Object[])null));
      } else {
         return super.removeElement(s);
      }
   }

   public void removeElementAt(int i) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", (Object[])null));
      } else {
         super.removeElementAt(i);
      }
   }

   public void setElementAt(int node, int index) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", (Object[])null));
      } else {
         super.setElementAt(node, index);
      }
   }

   public void setItem(int node, int index) {
      if (!this.m_mutable) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", (Object[])null));
      } else {
         super.setElementAt(node, index);
      }
   }

   public int elementAt(int i) {
      this.runTo(i);
      return super.elementAt(i);
   }

   public boolean contains(int s) {
      this.runTo(-1);
      return super.contains(s);
   }

   public int indexOf(int elem, int index) {
      this.runTo(-1);
      return super.indexOf(elem, index);
   }

   public int indexOf(int elem) {
      this.runTo(-1);
      return super.indexOf(elem);
   }

   public int getCurrentPos() {
      return this.m_next;
   }

   public void setCurrentPos(int i) {
      if (!this.m_cacheNodes) {
         throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_NODESETDTM_CANNOT_INDEX", (Object[])null));
      } else {
         this.m_next = i;
      }
   }

   public int getCurrentNode() {
      if (!this.m_cacheNodes) {
         throw new RuntimeException("This NodeSetDTM can not do indexing or counting functions!");
      } else {
         int saved = this.m_next;
         int current = this.m_next > 0 ? this.m_next - 1 : this.m_next;
         int n = current < super.m_firstFree ? this.elementAt(current) : -1;
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

   public boolean isMutable() {
      return this.m_mutable;
   }

   public int getLast() {
      return this.m_last;
   }

   public void setLast(int last) {
      this.m_last = last;
   }

   public boolean isDocOrdered() {
      return true;
   }

   public int getAxis() {
      return -1;
   }
}

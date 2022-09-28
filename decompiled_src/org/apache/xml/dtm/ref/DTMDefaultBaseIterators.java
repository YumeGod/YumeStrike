package org.apache.xml.dtm.ref;

import javax.xml.transform.Source;
import org.apache.xml.dtm.Axis;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMException;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.NodeVector;
import org.apache.xml.utils.XMLStringFactory;

public abstract class DTMDefaultBaseIterators extends DTMDefaultBaseTraversers {
   public DTMDefaultBaseIterators(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing) {
      super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing);
   }

   public DTMDefaultBaseIterators(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean usePrevsib, boolean newNameTable) {
      super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, blocksize, usePrevsib, newNameTable);
   }

   public DTMAxisIterator getTypedAxisIterator(int axis, int type) {
      DTMAxisIterator iterator = null;
      switch (axis) {
         case 0:
            return new TypedAncestorIterator(type);
         case 1:
            return (new TypedAncestorIterator(type)).includeSelf();
         case 2:
            return new TypedAttributeIterator(type);
         case 3:
            iterator = new TypedChildrenIterator(type);
            break;
         case 4:
            iterator = new TypedDescendantIterator(type);
            break;
         case 5:
            iterator = (new TypedDescendantIterator(type)).includeSelf();
            break;
         case 6:
            iterator = new TypedFollowingIterator(type);
            break;
         case 7:
            iterator = new TypedFollowingSiblingIterator(type);
            break;
         case 8:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         default:
            throw new DTMException(XMLMessages.createXMLMessage("ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED", new Object[]{Axis.getNames(axis)}));
         case 9:
            iterator = new TypedNamespaceIterator(type);
            break;
         case 10:
            return (new ParentIterator()).setNodeType(type);
         case 11:
            iterator = new TypedPrecedingIterator(type);
            break;
         case 12:
            iterator = new TypedPrecedingSiblingIterator(type);
            break;
         case 13:
            iterator = new TypedSingletonIterator(type);
            break;
         case 19:
            iterator = new TypedRootIterator(type);
      }

      return (DTMAxisIterator)iterator;
   }

   public DTMAxisIterator getAxisIterator(int axis) {
      DTMAxisIterator iterator = null;
      switch (axis) {
         case 0:
            return new AncestorIterator();
         case 1:
            return (new AncestorIterator()).includeSelf();
         case 2:
            return new AttributeIterator();
         case 3:
            iterator = new ChildrenIterator();
            break;
         case 4:
            iterator = new DescendantIterator();
            break;
         case 5:
            iterator = (new DescendantIterator()).includeSelf();
            break;
         case 6:
            iterator = new FollowingIterator();
            break;
         case 7:
            iterator = new FollowingSiblingIterator();
            break;
         case 8:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         default:
            throw new DTMException(XMLMessages.createXMLMessage("ER_ITERATOR_AXIS_NOT_IMPLEMENTED", new Object[]{Axis.getNames(axis)}));
         case 9:
            iterator = new NamespaceIterator();
            break;
         case 10:
            return new ParentIterator();
         case 11:
            iterator = new PrecedingIterator();
            break;
         case 12:
            iterator = new PrecedingSiblingIterator();
            break;
         case 13:
            iterator = new SingletonIterator();
            break;
         case 19:
            iterator = new RootIterator();
      }

      return (DTMAxisIterator)iterator;
   }

   public final class TypedSingletonIterator extends SingletonIterator {
      private final int _nodeType;

      public TypedSingletonIterator(int nodeType) {
         super();
         this._nodeType = nodeType;
      }

      public int next() {
         int result = super._currentNode;
         int nodeType = this._nodeType;
         super._currentNode = -1;
         if (nodeType >= 14) {
            if (DTMDefaultBaseIterators.this.getExpandedTypeID(result) == nodeType) {
               return this.returnNode(result);
            }
         } else if (DTMDefaultBaseIterators.this.getNodeType(result) == nodeType) {
            return this.returnNode(result);
         }

         return -1;
      }
   }

   public class SingletonIterator extends InternalAxisIteratorBase {
      private boolean _isConstant;

      public SingletonIterator() {
         this(Integer.MIN_VALUE, false);
      }

      public SingletonIterator(int node) {
         this(node, false);
      }

      public SingletonIterator(int node, boolean constant) {
         super();
         super._currentNode = super._startNode = node;
         this._isConstant = constant;
      }

      public DTMAxisIterator setStartNode(int node) {
         if (node == 0) {
            node = DTMDefaultBaseIterators.this.getDocument();
         }

         if (this._isConstant) {
            super._currentNode = super._startNode;
            return this.resetPosition();
         } else if (super._isRestartable) {
            if (super._currentNode == Integer.MIN_VALUE) {
               super._currentNode = super._startNode = node;
            }

            return this.resetPosition();
         } else {
            return this;
         }
      }

      public DTMAxisIterator reset() {
         if (this._isConstant) {
            super._currentNode = super._startNode;
            return this.resetPosition();
         } else {
            boolean temp = super._isRestartable;
            super._isRestartable = true;
            this.setStartNode(super._startNode);
            super._isRestartable = temp;
            return this;
         }
      }

      public int next() {
         int result = super._currentNode;
         super._currentNode = -1;
         return this.returnNode(result);
      }
   }

   public class NthDescendantIterator extends DescendantIterator {
      int _pos;

      public NthDescendantIterator(int pos) {
         super();
         this._pos = pos;
      }

      public int next() {
         int node;
         int child;
         do {
            if ((node = super.next()) == -1) {
               return -1;
            }

            node = DTMDefaultBaseIterators.this.makeNodeIdentity(node);
            int parent = DTMDefaultBaseIterators.this._parent(node);
            child = DTMDefaultBaseIterators.this._firstch(parent);
            int pos = 0;

            do {
               int type = DTMDefaultBaseIterators.this._type(child);
               if (1 == type) {
                  ++pos;
               }
            } while(pos < this._pos && (child = DTMDefaultBaseIterators.this._nextsib(child)) != -1);
         } while(node != child);

         return node;
      }
   }

   public final class TypedDescendantIterator extends DescendantIterator {
      private final int _nodeType;

      public TypedDescendantIterator(int nodeType) {
         super();
         this._nodeType = nodeType;
      }

      public int next() {
         if (super._startNode == -1) {
            return -1;
         } else {
            int node = super._currentNode;

            while(true) {
               ++node;
               int type = DTMDefaultBaseIterators.this._type(node);
               if (-1 != type && this.isDescendant(node)) {
                  if (type != this._nodeType && DTMDefaultBaseIterators.this._exptype(node) != this._nodeType) {
                     continue;
                  }

                  super._currentNode = node;
                  return this.returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(node));
               }

               super._currentNode = -1;
               return -1;
            }
         }
      }
   }

   public class DescendantIterator extends InternalAxisIteratorBase {
      public DescendantIterator() {
         super();
      }

      public DTMAxisIterator setStartNode(int node) {
         if (node == 0) {
            node = DTMDefaultBaseIterators.this.getDocument();
         }

         if (super._isRestartable) {
            node = DTMDefaultBaseIterators.this.makeNodeIdentity(node);
            super._startNode = node;
            if (super._includeSelf) {
               --node;
            }

            super._currentNode = node;
            return this.resetPosition();
         } else {
            return this;
         }
      }

      protected boolean isDescendant(int identity) {
         return DTMDefaultBaseIterators.this._parent(identity) >= super._startNode || super._startNode == identity;
      }

      public int next() {
         if (super._startNode == -1) {
            return -1;
         } else if (super._includeSelf && super._currentNode + 1 == super._startNode) {
            return this.returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(++super._currentNode));
         } else {
            int node = super._currentNode;

            short type;
            do {
               ++node;
               type = DTMDefaultBaseIterators.this._type(node);
               if (-1 == type || !this.isDescendant(node)) {
                  super._currentNode = -1;
                  return -1;
               }
            } while(2 == type || 3 == type || 13 == type);

            super._currentNode = node;
            return this.returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(node));
         }
      }

      public DTMAxisIterator reset() {
         boolean temp = super._isRestartable;
         super._isRestartable = true;
         this.setStartNode(DTMDefaultBaseIterators.this.makeNodeHandle(super._startNode));
         super._isRestartable = temp;
         return this;
      }
   }

   public final class TypedAncestorIterator extends AncestorIterator {
      private final int _nodeType;

      public TypedAncestorIterator(int type) {
         super();
         this._nodeType = type;
      }

      public DTMAxisIterator setStartNode(int node) {
         if (node == 0) {
            node = DTMDefaultBaseIterators.this.getDocument();
         }

         super.m_realStartNode = node;
         if (super._isRestartable) {
            int nodeID = DTMDefaultBaseIterators.this.makeNodeIdentity(node);
            int nodeType = this._nodeType;
            if (!super._includeSelf && node != -1) {
               nodeID = DTMDefaultBaseIterators.this._parent(nodeID);
            }

            super._startNode = node;
            int eType;
            if (nodeType >= 14) {
               for(; nodeID != -1; nodeID = DTMDefaultBaseIterators.this._parent(nodeID)) {
                  eType = DTMDefaultBaseIterators.this._exptype(nodeID);
                  if (eType == nodeType) {
                     super.m_ancestors.addElement(DTMDefaultBaseIterators.this.makeNodeHandle(nodeID));
                  }
               }
            } else {
               for(; nodeID != -1; nodeID = DTMDefaultBaseIterators.this._parent(nodeID)) {
                  eType = DTMDefaultBaseIterators.this._exptype(nodeID);
                  if (eType >= 14 && DTMDefaultBaseIterators.super.m_expandedNameTable.getType(eType) == nodeType || eType < 14 && eType == nodeType) {
                     super.m_ancestors.addElement(DTMDefaultBaseIterators.this.makeNodeHandle(nodeID));
                  }
               }
            }

            super.m_ancestorsPos = super.m_ancestors.size() - 1;
            super._currentNode = super.m_ancestorsPos >= 0 ? super.m_ancestors.elementAt(super.m_ancestorsPos) : -1;
            return this.resetPosition();
         } else {
            return this;
         }
      }
   }

   public class AncestorIterator extends InternalAxisIteratorBase {
      NodeVector m_ancestors = new NodeVector();
      int m_ancestorsPos;
      int m_markedPos;
      int m_realStartNode;

      public AncestorIterator() {
         super();
      }

      public int getStartNode() {
         return this.m_realStartNode;
      }

      public final boolean isReverse() {
         return true;
      }

      public DTMAxisIterator cloneIterator() {
         super._isRestartable = false;

         try {
            AncestorIterator clone = (AncestorIterator)super.clone();
            clone._startNode = super._startNode;
            return clone;
         } catch (CloneNotSupportedException var2) {
            throw new DTMException(XMLMessages.createXMLMessage("ER_ITERATOR_CLONE_NOT_SUPPORTED", (Object[])null));
         }
      }

      public DTMAxisIterator setStartNode(int node) {
         if (node == 0) {
            node = DTMDefaultBaseIterators.this.getDocument();
         }

         this.m_realStartNode = node;
         if (!super._isRestartable) {
            return this;
         } else {
            int nodeID = DTMDefaultBaseIterators.this.makeNodeIdentity(node);
            if (!super._includeSelf && node != -1) {
               nodeID = DTMDefaultBaseIterators.this._parent(nodeID);
               node = DTMDefaultBaseIterators.this.makeNodeHandle(nodeID);
            }

            for(super._startNode = node; nodeID != -1; node = DTMDefaultBaseIterators.this.makeNodeHandle(nodeID)) {
               this.m_ancestors.addElement(node);
               nodeID = DTMDefaultBaseIterators.this._parent(nodeID);
            }

            this.m_ancestorsPos = this.m_ancestors.size() - 1;
            super._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors.elementAt(this.m_ancestorsPos) : -1;
            return this.resetPosition();
         }
      }

      public DTMAxisIterator reset() {
         this.m_ancestorsPos = this.m_ancestors.size() - 1;
         super._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors.elementAt(this.m_ancestorsPos) : -1;
         return this.resetPosition();
      }

      public int next() {
         int next = super._currentNode;
         int pos = --this.m_ancestorsPos;
         super._currentNode = pos >= 0 ? this.m_ancestors.elementAt(this.m_ancestorsPos) : -1;
         return this.returnNode(next);
      }

      public void setMark() {
         this.m_markedPos = this.m_ancestorsPos;
      }

      public void gotoMark() {
         this.m_ancestorsPos = this.m_markedPos;
         super._currentNode = this.m_ancestorsPos >= 0 ? this.m_ancestors.elementAt(this.m_ancestorsPos) : -1;
      }
   }

   public final class TypedFollowingIterator extends FollowingIterator {
      private final int _nodeType;

      public TypedFollowingIterator(int type) {
         super();
         this._nodeType = type;
      }

      public int next() {
         int node;
         do {
            node = super._currentNode;
            super._currentNode = super.m_traverser.next(super._startNode, super._currentNode);
         } while(node != -1 && DTMDefaultBaseIterators.this.getExpandedTypeID(node) != this._nodeType && DTMDefaultBaseIterators.this.getNodeType(node) != this._nodeType);

         return node == -1 ? -1 : this.returnNode(node);
      }
   }

   public class FollowingIterator extends InternalAxisIteratorBase {
      DTMAxisTraverser m_traverser = DTMDefaultBaseIterators.this.getAxisTraverser(6);

      public FollowingIterator() {
         super();
      }

      public DTMAxisIterator setStartNode(int node) {
         if (node == 0) {
            node = DTMDefaultBaseIterators.this.getDocument();
         }

         if (super._isRestartable) {
            super._startNode = node;
            super._currentNode = this.m_traverser.first(node);
            return this.resetPosition();
         } else {
            return this;
         }
      }

      public int next() {
         int node = super._currentNode;
         super._currentNode = this.m_traverser.next(super._startNode, super._currentNode);
         return this.returnNode(node);
      }
   }

   public final class TypedPrecedingIterator extends PrecedingIterator {
      private final int _nodeType;

      public TypedPrecedingIterator(int type) {
         super();
         this._nodeType = type;
      }

      public int next() {
         int node = super._currentNode;
         int nodeType = this._nodeType;
         if (nodeType >= 14) {
            while(true) {
               ++node;
               if (super._sp < 0) {
                  node = -1;
                  break;
               }

               if (node >= super._stack[super._sp]) {
                  if (--super._sp < 0) {
                     node = -1;
                     break;
                  }
               } else if (DTMDefaultBaseIterators.this._exptype(node) == nodeType) {
                  break;
               }
            }
         } else {
            while(true) {
               ++node;
               if (super._sp < 0) {
                  node = -1;
                  break;
               }

               if (node >= super._stack[super._sp]) {
                  if (--super._sp < 0) {
                     node = -1;
                     break;
                  }
               } else {
                  int expType = DTMDefaultBaseIterators.this._exptype(node);
                  if (expType < 14) {
                     if (expType == nodeType) {
                        break;
                     }
                  } else if (DTMDefaultBaseIterators.super.m_expandedNameTable.getType(expType) == nodeType) {
                     break;
                  }
               }
            }
         }

         super._currentNode = node;
         return node == -1 ? -1 : this.returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(node));
      }
   }

   public class PrecedingIterator extends InternalAxisIteratorBase {
      private final int _maxAncestors = 8;
      protected int[] _stack = new int[8];
      protected int _sp;
      protected int _oldsp;
      protected int _markedsp;
      protected int _markedNode;
      protected int _markedDescendant;

      public PrecedingIterator() {
         super();
      }

      public boolean isReverse() {
         return true;
      }

      public DTMAxisIterator cloneIterator() {
         super._isRestartable = false;

         try {
            PrecedingIterator clone = (PrecedingIterator)super.clone();
            int[] stackCopy = new int[this._stack.length];
            System.arraycopy(this._stack, 0, stackCopy, 0, this._stack.length);
            clone._stack = stackCopy;
            return clone;
         } catch (CloneNotSupportedException var3) {
            throw new DTMException(XMLMessages.createXMLMessage("ER_ITERATOR_CLONE_NOT_SUPPORTED", (Object[])null));
         }
      }

      public DTMAxisIterator setStartNode(int node) {
         if (node == 0) {
            node = DTMDefaultBaseIterators.this.getDocument();
         }

         if (super._isRestartable) {
            node = DTMDefaultBaseIterators.this.makeNodeIdentity(node);
            if (DTMDefaultBaseIterators.this._type(node) == 2) {
               node = DTMDefaultBaseIterators.this._parent(node);
            }

            super._startNode = node;
            int index = 0;
            this._stack[0] = node;

            for(int parent = node; (parent = DTMDefaultBaseIterators.this._parent(parent)) != -1; this._stack[index] = parent) {
               ++index;
               if (index == this._stack.length) {
                  int[] stack = new int[index + 4];
                  System.arraycopy(this._stack, 0, stack, 0, index);
                  this._stack = stack;
               }
            }

            if (index > 0) {
               --index;
            }

            super._currentNode = this._stack[index];
            this._oldsp = this._sp = index;
            return this.resetPosition();
         } else {
            return this;
         }
      }

      public int next() {
         ++super._currentNode;

         for(; this._sp >= 0; ++super._currentNode) {
            if (super._currentNode < this._stack[this._sp]) {
               if (DTMDefaultBaseIterators.this._type(super._currentNode) != 2 && DTMDefaultBaseIterators.this._type(super._currentNode) != 13) {
                  return this.returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(super._currentNode));
               }
            } else {
               --this._sp;
            }
         }

         return -1;
      }

      public DTMAxisIterator reset() {
         this._sp = this._oldsp;
         return this.resetPosition();
      }

      public void setMark() {
         this._markedsp = this._sp;
         this._markedNode = super._currentNode;
         this._markedDescendant = this._stack[0];
      }

      public void gotoMark() {
         this._sp = this._markedsp;
         super._currentNode = this._markedNode;
      }
   }

   public final class TypedPrecedingSiblingIterator extends PrecedingSiblingIterator {
      private final int _nodeType;

      public TypedPrecedingSiblingIterator(int type) {
         super();
         this._nodeType = type;
      }

      public int next() {
         int node = super._currentNode;
         int nodeType = this._nodeType;
         int startID = super._startNodeID;
         if (nodeType >= 14) {
            while(node != -1 && node != startID && DTMDefaultBaseIterators.this._exptype(node) != nodeType) {
               node = DTMDefaultBaseIterators.this._nextsib(node);
            }
         } else {
            for(; node != -1 && node != startID; node = DTMDefaultBaseIterators.this._nextsib(node)) {
               int expType = DTMDefaultBaseIterators.this._exptype(node);
               if (expType < 14) {
                  if (expType == nodeType) {
                     break;
                  }
               } else if (DTMDefaultBaseIterators.super.m_expandedNameTable.getType(expType) == nodeType) {
                  break;
               }
            }
         }

         if (node != -1 && node != super._startNodeID) {
            super._currentNode = DTMDefaultBaseIterators.this._nextsib(node);
            return this.returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(node));
         } else {
            super._currentNode = -1;
            return -1;
         }
      }
   }

   public class PrecedingSiblingIterator extends InternalAxisIteratorBase {
      protected int _startNodeID;

      public PrecedingSiblingIterator() {
         super();
      }

      public boolean isReverse() {
         return true;
      }

      public DTMAxisIterator setStartNode(int node) {
         if (node == 0) {
            node = DTMDefaultBaseIterators.this.getDocument();
         }

         if (super._isRestartable) {
            super._startNode = node;
            node = this._startNodeID = DTMDefaultBaseIterators.this.makeNodeIdentity(node);
            if (node == -1) {
               super._currentNode = node;
               return this.resetPosition();
            } else {
               int type = DTMDefaultBaseIterators.super.m_expandedNameTable.getType(DTMDefaultBaseIterators.this._exptype(node));
               if (2 != type && 13 != type) {
                  super._currentNode = DTMDefaultBaseIterators.this._parent(node);
                  if (-1 != super._currentNode) {
                     super._currentNode = DTMDefaultBaseIterators.this._firstch(super._currentNode);
                  } else {
                     super._currentNode = node;
                  }
               } else {
                  super._currentNode = node;
               }

               return this.resetPosition();
            }
         } else {
            return this;
         }
      }

      public int next() {
         if (super._currentNode != this._startNodeID && super._currentNode != -1) {
            int node = super._currentNode;
            super._currentNode = DTMDefaultBaseIterators.this._nextsib(node);
            return this.returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(node));
         } else {
            return -1;
         }
      }
   }

   public final class TypedAttributeIterator extends InternalAxisIteratorBase {
      private final int _nodeType;

      public TypedAttributeIterator(int nodeType) {
         super();
         this._nodeType = nodeType;
      }

      public DTMAxisIterator setStartNode(int node) {
         if (super._isRestartable) {
            super._startNode = node;
            super._currentNode = DTMDefaultBaseIterators.this.getTypedAttribute(node, this._nodeType);
            return this.resetPosition();
         } else {
            return this;
         }
      }

      public int next() {
         int node = super._currentNode;
         super._currentNode = -1;
         return this.returnNode(node);
      }
   }

   public final class AttributeIterator extends InternalAxisIteratorBase {
      public AttributeIterator() {
         super();
      }

      public DTMAxisIterator setStartNode(int node) {
         if (node == 0) {
            node = DTMDefaultBaseIterators.this.getDocument();
         }

         if (super._isRestartable) {
            super._startNode = node;
            super._currentNode = DTMDefaultBaseIterators.this.getFirstAttributeIdentity(DTMDefaultBaseIterators.this.makeNodeIdentity(node));
            return this.resetPosition();
         } else {
            return this;
         }
      }

      public int next() {
         int node = super._currentNode;
         if (node != -1) {
            super._currentNode = DTMDefaultBaseIterators.this.getNextAttributeIdentity(node);
            return this.returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(node));
         } else {
            return -1;
         }
      }
   }

   public final class TypedFollowingSiblingIterator extends FollowingSiblingIterator {
      private final int _nodeType;

      public TypedFollowingSiblingIterator(int type) {
         super();
         this._nodeType = type;
      }

      public int next() {
         if (super._currentNode == -1) {
            return -1;
         } else {
            int node = super._currentNode;
            int nodeType = this._nodeType;
            if (nodeType >= 14) {
               do {
                  node = DTMDefaultBaseIterators.this._nextsib(node);
               } while(node != -1 && DTMDefaultBaseIterators.this._exptype(node) != nodeType);
            } else {
               while((node = DTMDefaultBaseIterators.this._nextsib(node)) != -1) {
                  int eType = DTMDefaultBaseIterators.this._exptype(node);
                  if (eType < 14) {
                     if (eType == nodeType) {
                        break;
                     }
                  } else if (DTMDefaultBaseIterators.super.m_expandedNameTable.getType(eType) == nodeType) {
                     break;
                  }
               }
            }

            super._currentNode = node;
            return super._currentNode == -1 ? -1 : this.returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(super._currentNode));
         }
      }
   }

   public class FollowingSiblingIterator extends InternalAxisIteratorBase {
      public FollowingSiblingIterator() {
         super();
      }

      public DTMAxisIterator setStartNode(int node) {
         if (node == 0) {
            node = DTMDefaultBaseIterators.this.getDocument();
         }

         if (super._isRestartable) {
            super._startNode = node;
            super._currentNode = DTMDefaultBaseIterators.this.makeNodeIdentity(node);
            return this.resetPosition();
         } else {
            return this;
         }
      }

      public int next() {
         super._currentNode = super._currentNode == -1 ? -1 : DTMDefaultBaseIterators.this._nextsib(super._currentNode);
         return this.returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(super._currentNode));
      }
   }

   public final class NamespaceAttributeIterator extends InternalAxisIteratorBase {
      private final int _nsType;

      public NamespaceAttributeIterator(int nsType) {
         super();
         this._nsType = nsType;
      }

      public DTMAxisIterator setStartNode(int node) {
         if (node == 0) {
            node = DTMDefaultBaseIterators.this.getDocument();
         }

         if (super._isRestartable) {
            super._startNode = node;
            super._currentNode = DTMDefaultBaseIterators.this.getFirstNamespaceNode(node, false);
            return this.resetPosition();
         } else {
            return this;
         }
      }

      public int next() {
         int node = super._currentNode;
         if (-1 != node) {
            super._currentNode = DTMDefaultBaseIterators.this.getNextNamespaceNode(super._startNode, node, false);
         }

         return this.returnNode(node);
      }
   }

   public class TypedRootIterator extends RootIterator {
      private final int _nodeType;

      public TypedRootIterator(int nodeType) {
         super();
         this._nodeType = nodeType;
      }

      public int next() {
         if (super._startNode == super._currentNode) {
            return -1;
         } else {
            int nodeType = this._nodeType;
            int node = super._startNode;
            int expType = DTMDefaultBaseIterators.this.getExpandedTypeID(node);
            super._currentNode = node;
            if (nodeType >= 14) {
               if (nodeType == expType) {
                  return this.returnNode(node);
               }
            } else if (expType < 14) {
               if (expType == nodeType) {
                  return this.returnNode(node);
               }
            } else if (DTMDefaultBaseIterators.super.m_expandedNameTable.getType(expType) == nodeType) {
               return this.returnNode(node);
            }

            return -1;
         }
      }
   }

   public class RootIterator extends InternalAxisIteratorBase {
      public RootIterator() {
         super();
      }

      public DTMAxisIterator setStartNode(int node) {
         if (super._isRestartable) {
            super._startNode = DTMDefaultBaseIterators.this.getDocumentRoot(node);
            super._currentNode = -1;
            return this.resetPosition();
         } else {
            return this;
         }
      }

      public int next() {
         if (super._startNode == super._currentNode) {
            return -1;
         } else {
            super._currentNode = super._startNode;
            return this.returnNode(super._startNode);
         }
      }
   }

   public class TypedNamespaceIterator extends NamespaceIterator {
      private final int _nodeType;

      public TypedNamespaceIterator(int nodeType) {
         super();
         this._nodeType = nodeType;
      }

      public int next() {
         for(int node = super._currentNode; node != -1; node = DTMDefaultBaseIterators.this.getNextNamespaceNode(super._startNode, node, true)) {
            if (DTMDefaultBaseIterators.this.getExpandedTypeID(node) == this._nodeType || DTMDefaultBaseIterators.this.getNodeType(node) == this._nodeType || DTMDefaultBaseIterators.this.getNamespaceType(node) == this._nodeType) {
               super._currentNode = node;
               return this.returnNode(node);
            }
         }

         return super._currentNode = -1;
      }
   }

   public class NamespaceIterator extends InternalAxisIteratorBase {
      public NamespaceIterator() {
         super();
      }

      public DTMAxisIterator setStartNode(int node) {
         if (node == 0) {
            node = DTMDefaultBaseIterators.this.getDocument();
         }

         if (super._isRestartable) {
            super._startNode = node;
            super._currentNode = DTMDefaultBaseIterators.this.getFirstNamespaceNode(node, true);
            return this.resetPosition();
         } else {
            return this;
         }
      }

      public int next() {
         int node = super._currentNode;
         if (-1 != node) {
            super._currentNode = DTMDefaultBaseIterators.this.getNextNamespaceNode(super._startNode, node, true);
         }

         return this.returnNode(node);
      }
   }

   public final class NamespaceChildrenIterator extends InternalAxisIteratorBase {
      private final int _nsType;

      public NamespaceChildrenIterator(int type) {
         super();
         this._nsType = type;
      }

      public DTMAxisIterator setStartNode(int node) {
         if (node == 0) {
            node = DTMDefaultBaseIterators.this.getDocument();
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
            for(int node = -2 == super._currentNode ? DTMDefaultBaseIterators.this._firstch(DTMDefaultBaseIterators.this.makeNodeIdentity(super._startNode)) : DTMDefaultBaseIterators.this._nextsib(super._currentNode); node != -1; node = DTMDefaultBaseIterators.this._nextsib(node)) {
               if (DTMDefaultBaseIterators.super.m_expandedNameTable.getNamespaceID(DTMDefaultBaseIterators.this._exptype(node)) == this._nsType) {
                  super._currentNode = node;
                  return this.returnNode(node);
               }
            }
         }

         return -1;
      }
   }

   public final class TypedChildrenIterator extends InternalAxisIteratorBase {
      private final int _nodeType;

      public TypedChildrenIterator(int nodeType) {
         super();
         this._nodeType = nodeType;
      }

      public DTMAxisIterator setStartNode(int node) {
         if (node == 0) {
            node = DTMDefaultBaseIterators.this.getDocument();
         }

         if (super._isRestartable) {
            super._startNode = node;
            super._currentNode = node == -1 ? -1 : DTMDefaultBaseIterators.this._firstch(DTMDefaultBaseIterators.this.makeNodeIdentity(super._startNode));
            return this.resetPosition();
         } else {
            return this;
         }
      }

      public int next() {
         int node = super._currentNode;
         int nodeType = this._nodeType;
         if (nodeType >= 14) {
            while(node != -1 && DTMDefaultBaseIterators.this._exptype(node) != nodeType) {
               node = DTMDefaultBaseIterators.this._nextsib(node);
            }
         } else {
            for(; node != -1; node = DTMDefaultBaseIterators.this._nextsib(node)) {
               int eType = DTMDefaultBaseIterators.this._exptype(node);
               if (eType < 14) {
                  if (eType == nodeType) {
                     break;
                  }
               } else if (DTMDefaultBaseIterators.super.m_expandedNameTable.getType(eType) == nodeType) {
                  break;
               }
            }
         }

         if (node == -1) {
            super._currentNode = -1;
            return -1;
         } else {
            super._currentNode = DTMDefaultBaseIterators.this._nextsib(node);
            return this.returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(node));
         }
      }
   }

   public final class ParentIterator extends InternalAxisIteratorBase {
      private int _nodeType = -1;

      public ParentIterator() {
         super();
      }

      public DTMAxisIterator setStartNode(int node) {
         if (node == 0) {
            node = DTMDefaultBaseIterators.this.getDocument();
         }

         if (super._isRestartable) {
            super._startNode = node;
            super._currentNode = DTMDefaultBaseIterators.this.getParent(node);
            return this.resetPosition();
         } else {
            return this;
         }
      }

      public DTMAxisIterator setNodeType(int type) {
         this._nodeType = type;
         return this;
      }

      public int next() {
         int result = super._currentNode;
         if (this._nodeType >= 14) {
            if (this._nodeType != DTMDefaultBaseIterators.this.getExpandedTypeID(super._currentNode)) {
               result = -1;
            }
         } else if (this._nodeType != -1 && this._nodeType != DTMDefaultBaseIterators.this.getNodeType(super._currentNode)) {
            result = -1;
         }

         super._currentNode = -1;
         return this.returnNode(result);
      }
   }

   public final class ChildrenIterator extends InternalAxisIteratorBase {
      public ChildrenIterator() {
         super();
      }

      public DTMAxisIterator setStartNode(int node) {
         if (node == 0) {
            node = DTMDefaultBaseIterators.this.getDocument();
         }

         if (super._isRestartable) {
            super._startNode = node;
            super._currentNode = node == -1 ? -1 : DTMDefaultBaseIterators.this._firstch(DTMDefaultBaseIterators.this.makeNodeIdentity(node));
            return this.resetPosition();
         } else {
            return this;
         }
      }

      public int next() {
         if (super._currentNode != -1) {
            int node = super._currentNode;
            super._currentNode = DTMDefaultBaseIterators.this._nextsib(node);
            return this.returnNode(DTMDefaultBaseIterators.this.makeNodeHandle(node));
         } else {
            return -1;
         }
      }
   }

   public abstract class InternalAxisIteratorBase extends DTMAxisIteratorBase {
      protected int _currentNode;

      public void setMark() {
         super._markedNode = this._currentNode;
      }

      public void gotoMark() {
         this._currentNode = super._markedNode;
      }
   }
}

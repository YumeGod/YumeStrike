package org.apache.xml.dtm.ref;

import javax.xml.transform.Source;
import org.apache.xml.dtm.Axis;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMException;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.XMLStringFactory;

public abstract class DTMDefaultBaseTraversers extends DTMDefaultBase {
   public DTMDefaultBaseTraversers(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing) {
      super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing);
   }

   public DTMDefaultBaseTraversers(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean usePrevsib, boolean newNameTable) {
      super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, blocksize, usePrevsib, newNameTable);
   }

   public DTMAxisTraverser getAxisTraverser(int axis) {
      DTMAxisTraverser traverser;
      if (null == super.m_traversers) {
         super.m_traversers = new DTMAxisTraverser[Axis.getNamesLength()];
         traverser = null;
      } else {
         traverser = super.m_traversers[axis];
         if (traverser != null) {
            return traverser;
         }
      }

      Object traverser;
      switch (axis) {
         case 0:
            traverser = new AncestorTraverser();
            break;
         case 1:
            traverser = new AncestorOrSelfTraverser();
            break;
         case 2:
            traverser = new AttributeTraverser();
            break;
         case 3:
            traverser = new ChildTraverser();
            break;
         case 4:
            traverser = new DescendantTraverser();
            break;
         case 5:
            traverser = new DescendantOrSelfTraverser();
            break;
         case 6:
            traverser = new FollowingTraverser();
            break;
         case 7:
            traverser = new FollowingSiblingTraverser();
            break;
         case 8:
            traverser = new NamespaceDeclsTraverser();
            break;
         case 9:
            traverser = new NamespaceTraverser();
            break;
         case 10:
            traverser = new ParentTraverser();
            break;
         case 11:
            traverser = new PrecedingTraverser();
            break;
         case 12:
            traverser = new PrecedingSiblingTraverser();
            break;
         case 13:
            traverser = new SelfTraverser();
            break;
         case 14:
            traverser = new AllFromNodeTraverser();
            break;
         case 15:
            traverser = new PrecedingAndAncestorTraverser();
            break;
         case 16:
            traverser = new AllFromRootTraverser();
            break;
         case 17:
            traverser = new DescendantFromRootTraverser();
            break;
         case 18:
            traverser = new DescendantOrSelfFromRootTraverser();
            break;
         case 19:
            traverser = new RootTraverser();
            break;
         case 20:
            return null;
         default:
            throw new DTMException(XMLMessages.createXMLMessage("ER_UNKNOWN_AXIS_TYPE", new Object[]{Integer.toString(axis)}));
      }

      if (null == traverser) {
         throw new DTMException(XMLMessages.createXMLMessage("ER_AXIS_TRAVERSER_NOT_SUPPORTED", new Object[]{Axis.getNames(axis)}));
      } else {
         super.m_traversers[axis] = (DTMAxisTraverser)traverser;
         return (DTMAxisTraverser)traverser;
      }
   }

   private class DescendantFromRootTraverser extends DescendantTraverser {
      private DescendantFromRootTraverser() {
         super(null);
      }

      protected int getFirstPotential(int identity) {
         return DTMDefaultBaseTraversers.this._firstch(0);
      }

      protected int getSubtreeRoot(int handle) {
         return 0;
      }

      public int first(int context) {
         return DTMDefaultBaseTraversers.this.makeNodeHandle(DTMDefaultBaseTraversers.this._firstch(0));
      }

      public int first(int context, int expandedTypeID) {
         if (this.isIndexed(expandedTypeID)) {
            int identity = 0;
            int firstPotential = this.getFirstPotential(identity);
            return DTMDefaultBaseTraversers.this.makeNodeHandle(this.getNextIndexed(identity, firstPotential, expandedTypeID));
         } else {
            int root = DTMDefaultBaseTraversers.this.getDocumentRoot(context);
            return this.next(root, root, expandedTypeID);
         }
      }

      // $FF: synthetic method
      DescendantFromRootTraverser(Object x1) {
         this();
      }
   }

   private class DescendantOrSelfFromRootTraverser extends DescendantTraverser {
      private DescendantOrSelfFromRootTraverser() {
         super(null);
      }

      protected int getFirstPotential(int identity) {
         return identity;
      }

      protected int getSubtreeRoot(int handle) {
         return DTMDefaultBaseTraversers.this.makeNodeIdentity(DTMDefaultBaseTraversers.this.getDocument());
      }

      public int first(int context) {
         return DTMDefaultBaseTraversers.this.getDocumentRoot(context);
      }

      public int first(int context, int expandedTypeID) {
         if (this.isIndexed(expandedTypeID)) {
            int identity = 0;
            int firstPotential = this.getFirstPotential(identity);
            return DTMDefaultBaseTraversers.this.makeNodeHandle(this.getNextIndexed(identity, firstPotential, expandedTypeID));
         } else {
            int root = this.first(context);
            return this.next(root, root, expandedTypeID);
         }
      }

      // $FF: synthetic method
      DescendantOrSelfFromRootTraverser(Object x1) {
         this();
      }
   }

   private class RootTraverser extends AllFromRootTraverser {
      private RootTraverser() {
         super(null);
      }

      public int first(int context, int expandedTypeID) {
         int root = DTMDefaultBaseTraversers.this.getDocumentRoot(context);
         return DTMDefaultBaseTraversers.this.getExpandedTypeID(root) == expandedTypeID ? root : -1;
      }

      public int next(int context, int current) {
         return -1;
      }

      public int next(int context, int current, int expandedTypeID) {
         return -1;
      }

      // $FF: synthetic method
      RootTraverser(Object x1) {
         this();
      }
   }

   private class AllFromRootTraverser extends AllFromNodeTraverser {
      private AllFromRootTraverser() {
         super(null);
      }

      public int first(int context) {
         return DTMDefaultBaseTraversers.this.getDocumentRoot(context);
      }

      public int first(int context, int expandedTypeID) {
         return DTMDefaultBaseTraversers.this.getExpandedTypeID(DTMDefaultBaseTraversers.this.getDocumentRoot(context)) == expandedTypeID ? context : this.next(context, context, expandedTypeID);
      }

      public int next(int context, int current) {
         DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
         current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) + 1;
         short var4 = DTMDefaultBaseTraversers.this._type(current);
         return var4 == -1 ? -1 : DTMDefaultBaseTraversers.this.makeNodeHandle(current);
      }

      public int next(int context, int current, int expandedTypeID) {
         DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
         current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) + 1;

         while(true) {
            int exptype = DTMDefaultBaseTraversers.this._exptype(current);
            if (exptype == -1) {
               return -1;
            }

            if (exptype == expandedTypeID) {
               return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
            }

            ++current;
         }
      }

      // $FF: synthetic method
      AllFromRootTraverser(Object x1) {
         this();
      }
   }

   private class SelfTraverser extends DTMAxisTraverser {
      private SelfTraverser() {
      }

      public int first(int context) {
         return context;
      }

      public int first(int context, int expandedTypeID) {
         return DTMDefaultBaseTraversers.this.getExpandedTypeID(context) == expandedTypeID ? context : -1;
      }

      public int next(int context, int current) {
         return -1;
      }

      public int next(int context, int current, int expandedTypeID) {
         return -1;
      }

      // $FF: synthetic method
      SelfTraverser(Object x1) {
         this();
      }
   }

   private class PrecedingSiblingTraverser extends DTMAxisTraverser {
      private PrecedingSiblingTraverser() {
      }

      public int next(int context, int current) {
         return DTMDefaultBaseTraversers.this.getPreviousSibling(current);
      }

      public int next(int context, int current, int expandedTypeID) {
         do {
            if (-1 == (current = DTMDefaultBaseTraversers.this.getPreviousSibling(current))) {
               return -1;
            }
         } while(DTMDefaultBaseTraversers.this.getExpandedTypeID(current) != expandedTypeID);

         return current;
      }

      // $FF: synthetic method
      PrecedingSiblingTraverser(Object x1) {
         this();
      }
   }

   private class PrecedingAndAncestorTraverser extends DTMAxisTraverser {
      private PrecedingAndAncestorTraverser() {
      }

      public int next(int context, int current) {
         DTMDefaultBaseTraversers.this.makeNodeIdentity(context);

         for(current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) - 1; current >= 0; --current) {
            short type = DTMDefaultBaseTraversers.this._type(current);
            if (2 != type && 13 != type) {
               return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
            }
         }

         return -1;
      }

      public int next(int context, int current, int expandedTypeID) {
         DTMDefaultBaseTraversers.this.makeNodeIdentity(context);

         for(current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) - 1; current >= 0; --current) {
            int exptype = DTMDefaultBaseTraversers.super.m_exptype.elementAt(current);
            if (exptype == expandedTypeID) {
               return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
            }
         }

         return -1;
      }

      // $FF: synthetic method
      PrecedingAndAncestorTraverser(Object x1) {
         this();
      }
   }

   private class PrecedingTraverser extends DTMAxisTraverser {
      private PrecedingTraverser() {
      }

      protected boolean isAncestor(int contextIdent, int currentIdent) {
         for(contextIdent = DTMDefaultBaseTraversers.super.m_parent.elementAt(contextIdent); -1 != contextIdent; contextIdent = DTMDefaultBaseTraversers.super.m_parent.elementAt(contextIdent)) {
            if (contextIdent == currentIdent) {
               return true;
            }
         }

         return false;
      }

      public int next(int context, int current) {
         int subtreeRootIdent = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);

         for(current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) - 1; current >= 0; --current) {
            short type = DTMDefaultBaseTraversers.this._type(current);
            if (2 != type && 13 != type && !this.isAncestor(subtreeRootIdent, current)) {
               return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
            }
         }

         return -1;
      }

      public int next(int context, int current, int expandedTypeID) {
         int subtreeRootIdent = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);

         for(current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) - 1; current >= 0; --current) {
            int exptype = DTMDefaultBaseTraversers.super.m_exptype.elementAt(current);
            if (exptype == expandedTypeID && !this.isAncestor(subtreeRootIdent, current)) {
               return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
            }
         }

         return -1;
      }

      // $FF: synthetic method
      PrecedingTraverser(Object x1) {
         this();
      }
   }

   private class ParentTraverser extends DTMAxisTraverser {
      private ParentTraverser() {
      }

      public int first(int context) {
         return DTMDefaultBaseTraversers.this.getParent(context);
      }

      public int first(int current, int expandedTypeID) {
         current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current);

         while(-1 != (current = DTMDefaultBaseTraversers.super.m_parent.elementAt(current))) {
            if (DTMDefaultBaseTraversers.super.m_exptype.elementAt(current) == expandedTypeID) {
               return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
            }
         }

         return -1;
      }

      public int next(int context, int current) {
         return -1;
      }

      public int next(int context, int current, int expandedTypeID) {
         return -1;
      }

      // $FF: synthetic method
      ParentTraverser(Object x1) {
         this();
      }
   }

   private class NamespaceTraverser extends DTMAxisTraverser {
      private NamespaceTraverser() {
      }

      public int next(int context, int current) {
         return context == current ? DTMDefaultBaseTraversers.this.getFirstNamespaceNode(context, true) : DTMDefaultBaseTraversers.this.getNextNamespaceNode(context, current, true);
      }

      public int next(int context, int current, int expandedTypeID) {
         current = context == current ? DTMDefaultBaseTraversers.this.getFirstNamespaceNode(context, true) : DTMDefaultBaseTraversers.this.getNextNamespaceNode(context, current, true);

         while(DTMDefaultBaseTraversers.this.getExpandedTypeID(current) != expandedTypeID) {
            if (-1 == (current = DTMDefaultBaseTraversers.this.getNextNamespaceNode(context, current, true))) {
               return -1;
            }
         }

         return current;
      }

      // $FF: synthetic method
      NamespaceTraverser(Object x1) {
         this();
      }
   }

   private class NamespaceDeclsTraverser extends DTMAxisTraverser {
      private NamespaceDeclsTraverser() {
      }

      public int next(int context, int current) {
         return context == current ? DTMDefaultBaseTraversers.this.getFirstNamespaceNode(context, false) : DTMDefaultBaseTraversers.this.getNextNamespaceNode(context, current, false);
      }

      public int next(int context, int current, int expandedTypeID) {
         current = context == current ? DTMDefaultBaseTraversers.this.getFirstNamespaceNode(context, false) : DTMDefaultBaseTraversers.this.getNextNamespaceNode(context, current, false);

         while(DTMDefaultBaseTraversers.this.getExpandedTypeID(current) != expandedTypeID) {
            if (-1 == (current = DTMDefaultBaseTraversers.this.getNextNamespaceNode(context, current, false))) {
               return -1;
            }
         }

         return current;
      }

      // $FF: synthetic method
      NamespaceDeclsTraverser(Object x1) {
         this();
      }
   }

   private class FollowingSiblingTraverser extends DTMAxisTraverser {
      private FollowingSiblingTraverser() {
      }

      public int next(int context, int current) {
         return DTMDefaultBaseTraversers.this.getNextSibling(current);
      }

      public int next(int context, int current, int expandedTypeID) {
         do {
            if (-1 == (current = DTMDefaultBaseTraversers.this.getNextSibling(current))) {
               return -1;
            }
         } while(DTMDefaultBaseTraversers.this.getExpandedTypeID(current) != expandedTypeID);

         return current;
      }

      // $FF: synthetic method
      FollowingSiblingTraverser(Object x1) {
         this();
      }
   }

   private class FollowingTraverser extends DescendantTraverser {
      private FollowingTraverser() {
         super(null);
      }

      public int first(int context) {
         context = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
         int type = DTMDefaultBaseTraversers.this._type(context);
         int first;
         if (2 == type || 13 == type) {
            context = DTMDefaultBaseTraversers.this._parent(context);
            first = DTMDefaultBaseTraversers.this._firstch(context);
            if (-1 != first) {
               return DTMDefaultBaseTraversers.this.makeNodeHandle(first);
            }
         }

         do {
            first = DTMDefaultBaseTraversers.this._nextsib(context);
            if (-1 == first) {
               context = DTMDefaultBaseTraversers.this._parent(context);
            }
         } while(-1 == first && -1 != context);

         return DTMDefaultBaseTraversers.this.makeNodeHandle(first);
      }

      public int first(int context, int expandedTypeID) {
         int type = DTMDefaultBaseTraversers.this.getNodeType(context);
         int first;
         if (2 == type || 13 == type) {
            context = DTMDefaultBaseTraversers.this.getParent(context);
            first = DTMDefaultBaseTraversers.this.getFirstChild(context);
            if (-1 != first) {
               if (DTMDefaultBaseTraversers.this.getExpandedTypeID(first) == expandedTypeID) {
                  return first;
               }

               return this.next(context, first, expandedTypeID);
            }
         }

         do {
            first = DTMDefaultBaseTraversers.this.getNextSibling(context);
            if (-1 != first) {
               if (DTMDefaultBaseTraversers.this.getExpandedTypeID(first) == expandedTypeID) {
                  return first;
               }

               return this.next(context, first, expandedTypeID);
            }

            context = DTMDefaultBaseTraversers.this.getParent(context);
         } while(-1 == first && -1 != context);

         return first;
      }

      public int next(int context, int current) {
         current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current);

         short type;
         do {
            ++current;
            type = DTMDefaultBaseTraversers.this._type(current);
            if (-1 == type) {
               return -1;
            }
         } while(2 == type || 13 == type);

         return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
      }

      public int next(int context, int current, int expandedTypeID) {
         current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current);

         int etype;
         do {
            ++current;
            etype = DTMDefaultBaseTraversers.this._exptype(current);
            if (-1 == etype) {
               return -1;
            }
         } while(etype != expandedTypeID);

         return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
      }

      // $FF: synthetic method
      FollowingTraverser(Object x1) {
         this();
      }
   }

   private class AllFromNodeTraverser extends DescendantOrSelfTraverser {
      private AllFromNodeTraverser() {
         super(null);
      }

      public int next(int context, int current) {
         int subtreeRootIdent = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
         current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) + 1;
         DTMDefaultBaseTraversers.this._exptype(current);
         return !this.isDescendant(subtreeRootIdent, current) ? -1 : DTMDefaultBaseTraversers.this.makeNodeHandle(current);
      }

      // $FF: synthetic method
      AllFromNodeTraverser(Object x1) {
         this();
      }
   }

   private class DescendantOrSelfTraverser extends DescendantTraverser {
      private DescendantOrSelfTraverser() {
         super(null);
      }

      protected int getFirstPotential(int identity) {
         return identity;
      }

      public int first(int context) {
         return context;
      }

      // $FF: synthetic method
      DescendantOrSelfTraverser(Object x1) {
         this();
      }
   }

   private class DescendantTraverser extends IndexedDTMAxisTraverser {
      private DescendantTraverser() {
         super(null);
      }

      protected int getFirstPotential(int identity) {
         return identity + 1;
      }

      protected boolean axisHasBeenProcessed(int axisRoot) {
         return DTMDefaultBaseTraversers.super.m_nextsib.elementAt(axisRoot) != -2;
      }

      protected int getSubtreeRoot(int handle) {
         return DTMDefaultBaseTraversers.this.makeNodeIdentity(handle);
      }

      protected boolean isDescendant(int subtreeRootIdentity, int identity) {
         return DTMDefaultBaseTraversers.this._parent(identity) >= subtreeRootIdentity;
      }

      protected boolean isAfterAxis(int axisRoot, int identity) {
         do {
            if (identity == axisRoot) {
               return false;
            }

            identity = DTMDefaultBaseTraversers.super.m_parent.elementAt(identity);
         } while(identity >= axisRoot);

         return true;
      }

      public int first(int context, int expandedTypeID) {
         if (this.isIndexed(expandedTypeID)) {
            int identity = this.getSubtreeRoot(context);
            int firstPotential = this.getFirstPotential(identity);
            return DTMDefaultBaseTraversers.this.makeNodeHandle(this.getNextIndexed(identity, firstPotential, expandedTypeID));
         } else {
            return this.next(context, context, expandedTypeID);
         }
      }

      public int next(int context, int current) {
         int subtreeRootIdent = this.getSubtreeRoot(context);
         current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) + 1;

         while(true) {
            int type = DTMDefaultBaseTraversers.this._type(current);
            if (!this.isDescendant(subtreeRootIdent, current)) {
               return -1;
            }

            if (2 != type && 13 != type) {
               return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
            }

            ++current;
         }
      }

      public int next(int context, int current, int expandedTypeID) {
         int subtreeRootIdent = this.getSubtreeRoot(context);
         current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) + 1;
         if (this.isIndexed(expandedTypeID)) {
            return DTMDefaultBaseTraversers.this.makeNodeHandle(this.getNextIndexed(subtreeRootIdent, current, expandedTypeID));
         } else {
            while(true) {
               int exptype = DTMDefaultBaseTraversers.this._exptype(current);
               if (!this.isDescendant(subtreeRootIdent, current)) {
                  return -1;
               }

               if (exptype == expandedTypeID) {
                  return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
               }

               ++current;
            }
         }
      }

      // $FF: synthetic method
      DescendantTraverser(Object x1) {
         this();
      }
   }

   private abstract class IndexedDTMAxisTraverser extends DTMAxisTraverser {
      private IndexedDTMAxisTraverser() {
      }

      protected final boolean isIndexed(int expandedTypeID) {
         return DTMDefaultBaseTraversers.super.m_indexing && 1 == DTMDefaultBaseTraversers.super.m_expandedNameTable.getType(expandedTypeID);
      }

      protected abstract boolean isAfterAxis(int var1, int var2);

      protected abstract boolean axisHasBeenProcessed(int var1);

      protected int getNextIndexed(int axisRoot, int nextPotential, int expandedTypeID) {
         int nsIndex = DTMDefaultBaseTraversers.super.m_expandedNameTable.getNamespaceID(expandedTypeID);
         int lnIndex = DTMDefaultBaseTraversers.super.m_expandedNameTable.getLocalNameID(expandedTypeID);

         while(true) {
            int next = DTMDefaultBaseTraversers.this.findElementFromIndex(nsIndex, lnIndex, nextPotential);
            if (-2 != next) {
               return this.isAfterAxis(axisRoot, next) ? -1 : next;
            }

            if (this.axisHasBeenProcessed(axisRoot)) {
               return -1;
            }

            DTMDefaultBaseTraversers.this.nextNode();
         }
      }

      // $FF: synthetic method
      IndexedDTMAxisTraverser(Object x1) {
         this();
      }
   }

   private class ChildTraverser extends DTMAxisTraverser {
      private ChildTraverser() {
      }

      protected int getNextIndexed(int axisRoot, int nextPotential, int expandedTypeID) {
         int nsIndex = DTMDefaultBaseTraversers.super.m_expandedNameTable.getNamespaceID(expandedTypeID);
         int lnIndex = DTMDefaultBaseTraversers.super.m_expandedNameTable.getLocalNameID(expandedTypeID);

         while(true) {
            while(true) {
               int nextID = DTMDefaultBaseTraversers.this.findElementFromIndex(nsIndex, lnIndex, nextPotential);
               if (-2 != nextID) {
                  int parentID = DTMDefaultBaseTraversers.super.m_parent.elementAt(nextID);
                  if (parentID == axisRoot) {
                     return nextID;
                  }

                  if (parentID < axisRoot) {
                     return -1;
                  }

                  do {
                     parentID = DTMDefaultBaseTraversers.super.m_parent.elementAt(parentID);
                     if (parentID < axisRoot) {
                        return -1;
                     }
                  } while(parentID > axisRoot);

                  nextPotential = nextID + 1;
               } else {
                  DTMDefaultBaseTraversers.this.nextNode();
                  if (DTMDefaultBaseTraversers.super.m_nextsib.elementAt(axisRoot) != -2) {
                     return -1;
                  }
               }
            }
         }
      }

      public int first(int context) {
         return DTMDefaultBaseTraversers.this.getFirstChild(context);
      }

      public int first(int context, int expandedTypeID) {
         int identity = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
         int firstMatch = this.getNextIndexed(identity, DTMDefaultBaseTraversers.this._firstch(identity), expandedTypeID);
         return DTMDefaultBaseTraversers.this.makeNodeHandle(firstMatch);
      }

      public int next(int context, int current) {
         return DTMDefaultBaseTraversers.this.getNextSibling(current);
      }

      public int next(int context, int current, int expandedTypeID) {
         for(current = DTMDefaultBaseTraversers.this._nextsib(DTMDefaultBaseTraversers.this.makeNodeIdentity(current)); -1 != current; current = DTMDefaultBaseTraversers.this._nextsib(current)) {
            if (DTMDefaultBaseTraversers.super.m_exptype.elementAt(current) == expandedTypeID) {
               return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
            }
         }

         return -1;
      }

      // $FF: synthetic method
      ChildTraverser(Object x1) {
         this();
      }
   }

   private class AttributeTraverser extends DTMAxisTraverser {
      private AttributeTraverser() {
      }

      public int next(int context, int current) {
         return context == current ? DTMDefaultBaseTraversers.this.getFirstAttribute(context) : DTMDefaultBaseTraversers.this.getNextAttribute(current);
      }

      public int next(int context, int current, int expandedTypeID) {
         current = context == current ? DTMDefaultBaseTraversers.this.getFirstAttribute(context) : DTMDefaultBaseTraversers.this.getNextAttribute(current);

         while(DTMDefaultBaseTraversers.this.getExpandedTypeID(current) != expandedTypeID) {
            if (-1 == (current = DTMDefaultBaseTraversers.this.getNextAttribute(current))) {
               return -1;
            }
         }

         return current;
      }

      // $FF: synthetic method
      AttributeTraverser(Object x1) {
         this();
      }
   }

   private class AncestorOrSelfTraverser extends AncestorTraverser {
      private AncestorOrSelfTraverser() {
         super(null);
      }

      public int first(int context) {
         return context;
      }

      public int first(int context, int expandedTypeID) {
         return DTMDefaultBaseTraversers.this.getExpandedTypeID(context) == expandedTypeID ? context : this.next(context, context, expandedTypeID);
      }

      // $FF: synthetic method
      AncestorOrSelfTraverser(Object x1) {
         this();
      }
   }

   private class AncestorTraverser extends DTMAxisTraverser {
      private AncestorTraverser() {
      }

      public int next(int context, int current) {
         return DTMDefaultBaseTraversers.this.getParent(current);
      }

      public int next(int context, int current, int expandedTypeID) {
         current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current);

         while(-1 != (current = DTMDefaultBaseTraversers.super.m_parent.elementAt(current))) {
            if (DTMDefaultBaseTraversers.super.m_exptype.elementAt(current) == expandedTypeID) {
               return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
            }
         }

         return -1;
      }

      // $FF: synthetic method
      AncestorTraverser(Object x1) {
         this();
      }
   }
}

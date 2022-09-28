package org.apache.xerces.dom;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.dom.events.EventImpl;
import org.apache.xerces.dom.events.MutationEventImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.ranges.DocumentRange;
import org.w3c.dom.ranges.Range;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.traversal.TreeWalker;

public class DocumentImpl extends CoreDocumentImpl implements DocumentTraversal, DocumentEvent, DocumentRange {
   static final long serialVersionUID = 515687835542616694L;
   protected Vector iterators;
   protected Vector ranges;
   protected Hashtable eventListeners;
   protected boolean mutationEvents = false;
   EnclosingAttr savedEnclosingAttr;

   public DocumentImpl() {
   }

   public DocumentImpl(boolean var1) {
      super(var1);
   }

   public DocumentImpl(DocumentType var1) {
      super(var1);
   }

   public DocumentImpl(DocumentType var1, boolean var2) {
      super(var1, var2);
   }

   public Node cloneNode(boolean var1) {
      DocumentImpl var2 = new DocumentImpl();
      this.callUserDataHandlers(this, var2, (short)1);
      this.cloneNode(var2, var1);
      var2.mutationEvents = this.mutationEvents;
      return var2;
   }

   public DOMImplementation getImplementation() {
      return DOMImplementationImpl.getDOMImplementation();
   }

   public NodeIterator createNodeIterator(Node var1, short var2, NodeFilter var3) {
      return this.createNodeIterator(var1, var2, var3, true);
   }

   public NodeIterator createNodeIterator(Node var1, int var2, NodeFilter var3, boolean var4) {
      if (var1 == null) {
         String var6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
         throw new DOMException((short)9, var6);
      } else {
         NodeIteratorImpl var5 = new NodeIteratorImpl(this, var1, var2, var3, var4);
         if (this.iterators == null) {
            this.iterators = new Vector();
         }

         this.iterators.addElement(var5);
         return var5;
      }
   }

   public TreeWalker createTreeWalker(Node var1, short var2, NodeFilter var3) {
      return this.createTreeWalker(var1, var2, var3, true);
   }

   public TreeWalker createTreeWalker(Node var1, int var2, NodeFilter var3, boolean var4) {
      if (var1 == null) {
         String var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
         throw new DOMException((short)9, var5);
      } else {
         return new TreeWalkerImpl(var1, var2, var3, var4);
      }
   }

   void removeNodeIterator(NodeIterator var1) {
      if (var1 != null) {
         if (this.iterators != null) {
            this.iterators.removeElement(var1);
         }
      }
   }

   public Range createRange() {
      if (this.ranges == null) {
         this.ranges = new Vector();
      }

      RangeImpl var1 = new RangeImpl(this);
      this.ranges.addElement(var1);
      return var1;
   }

   void removeRange(Range var1) {
      if (var1 != null) {
         if (this.ranges != null) {
            this.ranges.removeElement(var1);
         }
      }
   }

   void replacedText(NodeImpl var1) {
      if (this.ranges != null) {
         int var2 = this.ranges.size();

         for(int var3 = 0; var3 != var2; ++var3) {
            ((RangeImpl)this.ranges.elementAt(var3)).receiveReplacedText(var1);
         }
      }

   }

   void deletedText(NodeImpl var1, int var2, int var3) {
      if (this.ranges != null) {
         int var4 = this.ranges.size();

         for(int var5 = 0; var5 != var4; ++var5) {
            ((RangeImpl)this.ranges.elementAt(var5)).receiveDeletedText(var1, var2, var3);
         }
      }

   }

   void insertedText(NodeImpl var1, int var2, int var3) {
      if (this.ranges != null) {
         int var4 = this.ranges.size();

         for(int var5 = 0; var5 != var4; ++var5) {
            ((RangeImpl)this.ranges.elementAt(var5)).receiveInsertedText(var1, var2, var3);
         }
      }

   }

   void splitData(Node var1, Node var2, int var3) {
      if (this.ranges != null) {
         int var4 = this.ranges.size();

         for(int var5 = 0; var5 != var4; ++var5) {
            ((RangeImpl)this.ranges.elementAt(var5)).receiveSplitData(var1, var2, var3);
         }
      }

   }

   public Event createEvent(String var1) throws DOMException {
      if (!var1.equalsIgnoreCase("Events") && !"Event".equals(var1)) {
         if (!var1.equalsIgnoreCase("MutationEvents") && !"MutationEvent".equals(var1)) {
            String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
            throw new DOMException((short)9, var2);
         } else {
            return new MutationEventImpl();
         }
      } else {
         return new EventImpl();
      }
   }

   void setMutationEvents(boolean var1) {
      this.mutationEvents = var1;
   }

   boolean getMutationEvents() {
      return this.mutationEvents;
   }

   protected void setEventListeners(NodeImpl var1, Vector var2) {
      if (this.eventListeners == null) {
         this.eventListeners = new Hashtable();
      }

      if (var2 == null) {
         this.eventListeners.remove(var1);
         if (this.eventListeners.isEmpty()) {
            this.mutationEvents = false;
         }
      } else {
         this.eventListeners.put(var1, var2);
         this.mutationEvents = true;
      }

   }

   protected Vector getEventListeners(NodeImpl var1) {
      return this.eventListeners == null ? null : (Vector)this.eventListeners.get(var1);
   }

   protected void addEventListener(NodeImpl var1, String var2, EventListener var3, boolean var4) {
      if (var2 != null && !var2.equals("") && var3 != null) {
         this.removeEventListener(var1, var2, var3, var4);
         Vector var5 = this.getEventListeners(var1);
         if (var5 == null) {
            var5 = new Vector();
            this.setEventListeners(var1, var5);
         }

         var5.addElement(new LEntry(var2, var3, var4));
         LCount var6 = LCount.lookup(var2);
         if (var4) {
            ++var6.captures;
            ++var6.total;
         } else {
            ++var6.bubbles;
            ++var6.total;
         }

      }
   }

   protected void removeEventListener(NodeImpl var1, String var2, EventListener var3, boolean var4) {
      if (var2 != null && !var2.equals("") && var3 != null) {
         Vector var5 = this.getEventListeners(var1);
         if (var5 != null) {
            for(int var6 = var5.size() - 1; var6 >= 0; --var6) {
               LEntry var7 = (LEntry)var5.elementAt(var6);
               if (var7.useCapture == var4 && var7.listener == var3 && var7.type.equals(var2)) {
                  var5.removeElementAt(var6);
                  if (var5.size() == 0) {
                     this.setEventListeners(var1, (Vector)null);
                  }

                  LCount var8 = LCount.lookup(var2);
                  if (var4) {
                     --var8.captures;
                     --var8.total;
                  } else {
                     --var8.bubbles;
                     --var8.total;
                  }
                  break;
               }
            }

         }
      }
   }

   protected void copyEventListeners(NodeImpl var1, NodeImpl var2) {
      Vector var3 = this.getEventListeners(var1);
      if (var3 != null) {
         this.setEventListeners(var2, (Vector)var3.clone());
      }
   }

   protected boolean dispatchEvent(NodeImpl var1, Event var2) {
      if (var2 == null) {
         return false;
      } else {
         EventImpl var3 = (EventImpl)var2;
         if (var3.initialized && var3.type != null && !var3.type.equals("")) {
            LCount var20 = LCount.lookup(var3.getType());
            if (var20.total == 0) {
               return var3.preventDefault;
            } else {
               var3.target = var1;
               var3.stopPropagation = false;
               var3.preventDefault = false;
               Vector var5 = new Vector(10, 10);

               for(Node var7 = var1.getParentNode(); var7 != null; var7 = var7.getParentNode()) {
                  var5.addElement(var7);
               }

               int var13;
               if (var20.captures > 0) {
                  var3.eventPhase = 1;

                  for(int var8 = var5.size() - 1; var8 >= 0 && !var3.stopPropagation; --var8) {
                     NodeImpl var9 = (NodeImpl)var5.elementAt(var8);
                     var3.currentTarget = var9;
                     Vector var10 = this.getEventListeners(var9);
                     if (var10 != null) {
                        Vector var11 = (Vector)var10.clone();
                        int var12 = var11.size();

                        for(var13 = 0; var13 < var12; ++var13) {
                           LEntry var14 = (LEntry)var11.elementAt(var13);
                           if (var14.useCapture && var14.type.equals(var3.type) && var10.contains(var14)) {
                              try {
                                 var14.listener.handleEvent(var3);
                              } catch (Exception var19) {
                              }
                           }
                        }
                     }
                  }
               }

               if (var20.bubbles > 0) {
                  var3.eventPhase = 2;
                  var3.currentTarget = var1;
                  Vector var21 = this.getEventListeners(var1);
                  int var24;
                  if (!var3.stopPropagation && var21 != null) {
                     Vector var22 = (Vector)var21.clone();
                     var24 = var22.size();

                     for(int var25 = 0; var25 < var24; ++var25) {
                        LEntry var27 = (LEntry)var22.elementAt(var25);
                        if (!var27.useCapture && var27.type.equals(var3.type) && var21.contains(var27)) {
                           try {
                              var27.listener.handleEvent(var3);
                           } catch (Exception var18) {
                           }
                        }
                     }
                  }

                  if (var3.bubbles) {
                     var3.eventPhase = 3;
                     int var23 = var5.size();

                     for(var24 = 0; var24 < var23 && !var3.stopPropagation; ++var24) {
                        NodeImpl var26 = (NodeImpl)var5.elementAt(var24);
                        var3.currentTarget = var26;
                        var21 = this.getEventListeners(var26);
                        if (var21 != null) {
                           Vector var28 = (Vector)var21.clone();
                           var13 = var28.size();

                           for(int var29 = 0; var29 < var13; ++var29) {
                              LEntry var15 = (LEntry)var28.elementAt(var29);
                              if (!var15.useCapture && var15.type.equals(var3.type) && var21.contains(var15)) {
                                 try {
                                    var15.listener.handleEvent(var3);
                                 } catch (Exception var17) {
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               if (var20.defaults > 0 && var3.cancelable && !var3.preventDefault) {
               }

               return var3.preventDefault;
            }
         } else {
            String var4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "UNSPECIFIED_EVENT_TYPE_ERR", (Object[])null);
            throw new EventException((short)0, var4);
         }
      }
   }

   protected void dispatchEventToSubtree(Node var1, Event var2) {
      ((NodeImpl)var1).dispatchEvent(var2);
      if (var1.getNodeType() == 1) {
         NamedNodeMap var3 = var1.getAttributes();

         for(int var4 = var3.getLength() - 1; var4 >= 0; --var4) {
            this.dispatchingEventToSubtree(var3.item(var4), var2);
         }
      }

      this.dispatchingEventToSubtree(var1.getFirstChild(), var2);
   }

   protected void dispatchingEventToSubtree(Node var1, Event var2) {
      if (var1 != null) {
         ((NodeImpl)var1).dispatchEvent(var2);
         if (var1.getNodeType() == 1) {
            NamedNodeMap var3 = var1.getAttributes();

            for(int var4 = var3.getLength() - 1; var4 >= 0; --var4) {
               this.dispatchingEventToSubtree(var3.item(var4), var2);
            }
         }

         this.dispatchingEventToSubtree(var1.getFirstChild(), var2);
         this.dispatchingEventToSubtree(var1.getNextSibling(), var2);
      }
   }

   protected void dispatchAggregateEvents(NodeImpl var1, EnclosingAttr var2) {
      if (var2 != null) {
         this.dispatchAggregateEvents(var1, var2.node, var2.oldvalue, (short)1);
      } else {
         this.dispatchAggregateEvents(var1, (AttrImpl)null, (String)null, (short)0);
      }

   }

   protected void dispatchAggregateEvents(NodeImpl var1, AttrImpl var2, String var3, short var4) {
      NodeImpl var5 = null;
      LCount var6;
      MutationEventImpl var7;
      if (var2 != null) {
         var6 = LCount.lookup("DOMAttrModified");
         var5 = (NodeImpl)var2.getOwnerElement();
         if (var6.total > 0 && var5 != null) {
            var7 = new MutationEventImpl();
            var7.initMutationEvent("DOMAttrModified", true, false, var2, var3, var2.getNodeValue(), var2.getNodeName(), var4);
            var5.dispatchEvent(var7);
         }
      }

      var6 = LCount.lookup("DOMSubtreeModified");
      if (var6.total > 0) {
         var7 = new MutationEventImpl();
         var7.initMutationEvent("DOMSubtreeModified", true, false, (Node)null, (String)null, (String)null, (String)null, (short)0);
         if (var2 != null) {
            this.dispatchEvent(var2, var7);
            if (var5 != null) {
               this.dispatchEvent(var5, var7);
            }
         } else {
            this.dispatchEvent(var1, var7);
         }
      }

   }

   protected void saveEnclosingAttr(NodeImpl var1) {
      this.savedEnclosingAttr = null;
      LCount var2 = LCount.lookup("DOMAttrModified");
      if (var2.total > 0) {
         NodeImpl var3 = var1;

         while(var3 != null) {
            short var4 = var3.getNodeType();
            if (var4 == 2) {
               EnclosingAttr var5 = new EnclosingAttr();
               var5.node = (AttrImpl)var3;
               var5.oldvalue = var5.node.getNodeValue();
               this.savedEnclosingAttr = var5;
               return;
            }

            if (var4 == 5) {
               var3 = var3.parentNode();
            } else {
               if (var4 != 3) {
                  return;
               }

               var3 = var3.parentNode();
            }
         }

      }
   }

   void modifyingCharacterData(NodeImpl var1, boolean var2) {
      if (this.mutationEvents && !var2) {
         this.saveEnclosingAttr(var1);
      }

   }

   void modifiedCharacterData(NodeImpl var1, String var2, String var3, boolean var4) {
      if (this.mutationEvents && !var4) {
         LCount var5 = LCount.lookup("DOMCharacterDataModified");
         if (var5.total > 0) {
            MutationEventImpl var6 = new MutationEventImpl();
            var6.initMutationEvent("DOMCharacterDataModified", true, false, (Node)null, var2, var3, (String)null, (short)0);
            this.dispatchEvent(var1, var6);
         }

         this.dispatchAggregateEvents(var1, this.savedEnclosingAttr);
      }

   }

   void replacedCharacterData(NodeImpl var1, String var2, String var3) {
      this.modifiedCharacterData(var1, var2, var3, false);
   }

   void insertingNode(NodeImpl var1, boolean var2) {
      if (this.mutationEvents && !var2) {
         this.saveEnclosingAttr(var1);
      }

   }

   void insertedNode(NodeImpl var1, NodeImpl var2, boolean var3) {
      if (this.mutationEvents) {
         LCount var4 = LCount.lookup("DOMNodeInserted");
         if (var4.total > 0) {
            MutationEventImpl var5 = new MutationEventImpl();
            var5.initMutationEvent("DOMNodeInserted", true, false, var1, (String)null, (String)null, (String)null, (short)0);
            this.dispatchEvent(var2, var5);
         }

         var4 = LCount.lookup("DOMNodeInsertedIntoDocument");
         if (var4.total > 0) {
            NodeImpl var9 = var1;
            if (this.savedEnclosingAttr != null) {
               var9 = (NodeImpl)this.savedEnclosingAttr.node.getOwnerElement();
            }

            if (var9 != null) {
               NodeImpl var6 = var9;

               while(var6 != null) {
                  var9 = var6;
                  if (var6.getNodeType() == 2) {
                     var6 = (NodeImpl)((AttrImpl)var6).getOwnerElement();
                  } else {
                     var6 = var6.parentNode();
                  }
               }

               if (var9.getNodeType() == 9) {
                  MutationEventImpl var7 = new MutationEventImpl();
                  var7.initMutationEvent("DOMNodeInsertedIntoDocument", false, false, (Node)null, (String)null, (String)null, (String)null, (short)0);
                  this.dispatchEventToSubtree(var2, var7);
               }
            }
         }

         if (!var3) {
            this.dispatchAggregateEvents(var1, this.savedEnclosingAttr);
         }
      }

      if (this.ranges != null) {
         int var8 = this.ranges.size();

         for(int var10 = 0; var10 != var8; ++var10) {
            ((RangeImpl)this.ranges.elementAt(var10)).insertedNodeFromDOM(var2);
         }
      }

   }

   void removingNode(NodeImpl var1, NodeImpl var2, boolean var3) {
      int var4;
      int var5;
      if (this.iterators != null) {
         var4 = this.iterators.size();

         for(var5 = 0; var5 != var4; ++var5) {
            ((NodeIteratorImpl)this.iterators.elementAt(var5)).removeNode(var2);
         }
      }

      if (this.ranges != null) {
         var4 = this.ranges.size();

         for(var5 = 0; var5 != var4; ++var5) {
            ((RangeImpl)this.ranges.elementAt(var5)).removeNode(var2);
         }
      }

      if (this.mutationEvents) {
         if (!var3) {
            this.saveEnclosingAttr(var1);
         }

         LCount var8 = LCount.lookup("DOMNodeRemoved");
         if (var8.total > 0) {
            MutationEventImpl var9 = new MutationEventImpl();
            var9.initMutationEvent("DOMNodeRemoved", true, false, var1, (String)null, (String)null, (String)null, (short)0);
            this.dispatchEvent(var2, var9);
         }

         var8 = LCount.lookup("DOMNodeRemovedFromDocument");
         if (var8.total > 0) {
            Object var10 = this;
            if (this.savedEnclosingAttr != null) {
               var10 = (NodeImpl)this.savedEnclosingAttr.node.getOwnerElement();
            }

            if (var10 != null) {
               for(NodeImpl var6 = ((NodeImpl)var10).parentNode(); var6 != null; var6 = var6.parentNode()) {
                  var10 = var6;
               }

               if (((NodeImpl)var10).getNodeType() == 9) {
                  MutationEventImpl var7 = new MutationEventImpl();
                  var7.initMutationEvent("DOMNodeRemovedFromDocument", false, false, (Node)null, (String)null, (String)null, (String)null, (short)0);
                  this.dispatchEventToSubtree(var2, var7);
               }
            }
         }
      }

   }

   void removedNode(NodeImpl var1, boolean var2) {
      if (this.mutationEvents && !var2) {
         this.dispatchAggregateEvents(var1, this.savedEnclosingAttr);
      }

   }

   void replacingNode(NodeImpl var1) {
      if (this.mutationEvents) {
         this.saveEnclosingAttr(var1);
      }

   }

   void replacingData(NodeImpl var1) {
      if (this.mutationEvents) {
         this.saveEnclosingAttr(var1);
      }

   }

   void replacedNode(NodeImpl var1) {
      if (this.mutationEvents) {
         this.dispatchAggregateEvents(var1, this.savedEnclosingAttr);
      }

   }

   void modifiedAttrValue(AttrImpl var1, String var2) {
      if (this.mutationEvents) {
         this.dispatchAggregateEvents(var1, var1, var2, (short)1);
      }

   }

   void setAttrNode(AttrImpl var1, AttrImpl var2) {
      if (this.mutationEvents) {
         if (var2 == null) {
            this.dispatchAggregateEvents(var1.ownerNode, var1, (String)null, (short)2);
         } else {
            this.dispatchAggregateEvents(var1.ownerNode, var1, var2.getNodeValue(), (short)1);
         }
      }

   }

   void removedAttrNode(AttrImpl var1, NodeImpl var2, String var3) {
      if (this.mutationEvents) {
         LCount var4 = LCount.lookup("DOMAttrModified");
         if (var4.total > 0) {
            MutationEventImpl var5 = new MutationEventImpl();
            var5.initMutationEvent("DOMAttrModified", true, false, var1, var1.getNodeValue(), (String)null, var3, (short)3);
            this.dispatchEvent(var2, var5);
         }

         this.dispatchAggregateEvents(var2, (AttrImpl)null, (String)null, (short)0);
      }

   }

   void renamedAttrNode(Attr var1, Attr var2) {
   }

   void renamedElement(Element var1, Element var2) {
   }

   class EnclosingAttr implements Serializable {
      private static final long serialVersionUID = 3257001077260759859L;
      AttrImpl node;
      String oldvalue;
   }

   class LEntry implements Serializable {
      private static final long serialVersionUID = 3258416144514626360L;
      String type;
      EventListener listener;
      boolean useCapture;

      LEntry(String var2, EventListener var3, boolean var4) {
         this.type = var2;
         this.listener = var3;
         this.useCapture = var4;
      }
   }
}

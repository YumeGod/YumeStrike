package org.apache.batik.bridge.svg12;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.event.EventListenerList;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.dom.svg12.XBLEventSupport;
import org.apache.batik.dom.svg12.XBLOMContentElement;
import org.apache.batik.dom.svg12.XBLOMShadowTreeElement;
import org.apache.batik.dom.xbl.XBLManager;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MutationEvent;

public class ContentManager {
   protected XBLOMShadowTreeElement shadowTree;
   protected Element boundElement;
   protected DefaultXBLManager xblManager;
   protected HashMap selectors = new HashMap();
   protected HashMap selectedNodes = new HashMap();
   protected LinkedList contentElementList = new LinkedList();
   protected Node removedNode;
   protected HashMap listeners = new HashMap();
   protected ContentElementDOMAttrModifiedEventListener contentElementDomAttrModifiedEventListener;
   protected DOMAttrModifiedEventListener domAttrModifiedEventListener;
   protected DOMNodeInsertedEventListener domNodeInsertedEventListener;
   protected DOMNodeRemovedEventListener domNodeRemovedEventListener;
   protected DOMSubtreeModifiedEventListener domSubtreeModifiedEventListener;
   protected ShadowTreeNodeInsertedListener shadowTreeNodeInsertedListener;
   protected ShadowTreeNodeRemovedListener shadowTreeNodeRemovedListener;
   protected ShadowTreeSubtreeModifiedListener shadowTreeSubtreeModifiedListener;
   // $FF: synthetic field
   static Class class$org$apache$batik$bridge$svg12$ContentSelectionChangedListener;

   public ContentManager(XBLOMShadowTreeElement var1, XBLManager var2) {
      this.shadowTree = var1;
      this.xblManager = (DefaultXBLManager)var2;
      this.xblManager.setContentManager(var1, this);
      this.boundElement = this.xblManager.getXblBoundElement(var1);
      this.contentElementDomAttrModifiedEventListener = new ContentElementDOMAttrModifiedEventListener();
      XBLEventSupport var3 = (XBLEventSupport)this.shadowTree.initializeEventSupport();
      this.shadowTreeNodeInsertedListener = new ShadowTreeNodeInsertedListener();
      this.shadowTreeNodeRemovedListener = new ShadowTreeNodeRemovedListener();
      this.shadowTreeSubtreeModifiedListener = new ShadowTreeSubtreeModifiedListener();
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", this.shadowTreeNodeInsertedListener, true);
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.shadowTreeNodeRemovedListener, true);
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMSubtreeModified", this.shadowTreeSubtreeModifiedListener, true);
      var3 = (XBLEventSupport)((AbstractNode)this.boundElement).initializeEventSupport();
      this.domAttrModifiedEventListener = new DOMAttrModifiedEventListener();
      this.domNodeInsertedEventListener = new DOMNodeInsertedEventListener();
      this.domNodeRemovedEventListener = new DOMNodeRemovedEventListener();
      this.domSubtreeModifiedEventListener = new DOMSubtreeModifiedEventListener();
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.domAttrModifiedEventListener, true);
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", this.domNodeInsertedEventListener, true);
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.domNodeRemovedEventListener, true);
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMSubtreeModified", this.domSubtreeModifiedEventListener, false);
      this.update(true);
   }

   public void dispose() {
      this.xblManager.setContentManager(this.shadowTree, (ContentManager)null);
      Iterator var1 = this.selectedNodes.entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry var2 = (Map.Entry)var1.next();
         NodeList var3 = (NodeList)var2.getValue();

         for(int var4 = 0; var4 < var3.getLength(); ++var4) {
            Node var5 = var3.item(var4);
            this.xblManager.getRecord(var5).contentElement = null;
         }
      }

      var1 = this.contentElementList.iterator();

      while(var1.hasNext()) {
         NodeEventTarget var6 = (NodeEventTarget)var1.next();
         var6.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.contentElementDomAttrModifiedEventListener, false);
      }

      this.contentElementList.clear();
      this.selectedNodes.clear();
      XBLEventSupport var7 = (XBLEventSupport)((AbstractNode)this.boundElement).getEventSupport();
      var7.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.domAttrModifiedEventListener, true);
      var7.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", this.domNodeInsertedEventListener, true);
      var7.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.domNodeRemovedEventListener, true);
      var7.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMSubtreeModified", this.domSubtreeModifiedEventListener, false);
   }

   public NodeList getSelectedContent(XBLOMContentElement var1) {
      return (NodeList)this.selectedNodes.get(var1);
   }

   protected XBLOMContentElement getContentElement(Node var1) {
      return this.xblManager.getXblContentElement(var1);
   }

   public void addContentSelectionChangedListener(XBLOMContentElement var1, ContentSelectionChangedListener var2) {
      EventListenerList var3 = (EventListenerList)this.listeners.get(var1);
      if (var3 == null) {
         var3 = new EventListenerList();
         this.listeners.put(var1, var3);
      }

      var3.add(class$org$apache$batik$bridge$svg12$ContentSelectionChangedListener == null ? (class$org$apache$batik$bridge$svg12$ContentSelectionChangedListener = class$("org.apache.batik.bridge.svg12.ContentSelectionChangedListener")) : class$org$apache$batik$bridge$svg12$ContentSelectionChangedListener, var2);
   }

   public void removeContentSelectionChangedListener(XBLOMContentElement var1, ContentSelectionChangedListener var2) {
      EventListenerList var3 = (EventListenerList)this.listeners.get(var1);
      if (var3 != null) {
         var3.remove(class$org$apache$batik$bridge$svg12$ContentSelectionChangedListener == null ? (class$org$apache$batik$bridge$svg12$ContentSelectionChangedListener = class$("org.apache.batik.bridge.svg12.ContentSelectionChangedListener")) : class$org$apache$batik$bridge$svg12$ContentSelectionChangedListener, var2);
      }

   }

   protected void dispatchContentSelectionChangedEvent(XBLOMContentElement var1) {
      this.xblManager.invalidateChildNodes(var1.getXblParentNode());
      ContentSelectionChangedEvent var2 = new ContentSelectionChangedEvent(var1);
      EventListenerList var3 = (EventListenerList)this.listeners.get(var1);
      Object[] var4;
      int var5;
      ContentSelectionChangedListener var6;
      if (var3 != null) {
         var4 = var3.getListenerList();

         for(var5 = var4.length - 2; var5 >= 0; var5 -= 2) {
            var6 = (ContentSelectionChangedListener)var4[var5 + 1];
            var6.contentSelectionChanged(var2);
         }
      }

      var4 = this.xblManager.getContentSelectionChangedListeners();

      for(var5 = var4.length - 2; var5 >= 0; var5 -= 2) {
         var6 = (ContentSelectionChangedListener)var4[var5 + 1];
         var6.contentSelectionChanged(var2);
      }

   }

   protected void update(boolean var1) {
      HashSet var2 = new HashSet();
      Iterator var3 = this.selectedNodes.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();
         NodeList var5 = (NodeList)var4.getValue();

         for(int var6 = 0; var6 < var5.getLength(); ++var6) {
            Node var7 = var5.item(var6);
            this.xblManager.getRecord(var7).contentElement = null;
            var2.add(var7);
         }
      }

      var3 = this.contentElementList.iterator();

      while(var3.hasNext()) {
         NodeEventTarget var10 = (NodeEventTarget)var3.next();
         var10.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.contentElementDomAttrModifiedEventListener, false);
      }

      this.contentElementList.clear();
      this.selectedNodes.clear();
      boolean var11 = false;

      for(Node var12 = this.shadowTree.getFirstChild(); var12 != null; var12 = var12.getNextSibling()) {
         if (this.update(var1, var12)) {
            var11 = true;
         }
      }

      if (var11) {
         HashSet var13 = new HashSet();
         var3 = this.selectedNodes.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry var14 = (Map.Entry)var3.next();
            NodeList var16 = (NodeList)var14.getValue();

            for(int var8 = 0; var8 < var16.getLength(); ++var8) {
               Node var9 = var16.item(var8);
               var13.add(var9);
            }
         }

         HashSet var15 = new HashSet();
         var15.addAll(var2);
         var15.removeAll(var13);
         HashSet var17 = new HashSet();
         var17.addAll(var13);
         var17.removeAll(var2);
         if (!var1) {
            this.xblManager.shadowTreeSelectedContentChanged(var15, var17);
         }
      }

   }

   protected boolean update(boolean var1, Node var2) {
      boolean var3 = false;

      for(Node var4 = var2.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
         if (this.update(var1, var4)) {
            var3 = true;
         }
      }

      if (var2 instanceof XBLOMContentElement) {
         this.contentElementList.add(var2);
         XBLOMContentElement var10 = (XBLOMContentElement)var2;
         var10.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.contentElementDomAttrModifiedEventListener, false, (Object)null);
         Object var5 = (AbstractContentSelector)this.selectors.get(var2);
         boolean var6;
         if (var5 == null) {
            if (var10.hasAttributeNS((String)null, "includes")) {
               String var7 = this.getContentSelectorLanguage(var10);
               String var8 = var10.getAttributeNS((String)null, "includes");
               var5 = AbstractContentSelector.createSelector(var7, this, var10, this.boundElement, var8);
            } else {
               var5 = new DefaultContentSelector(this, var10, this.boundElement);
            }

            this.selectors.put(var2, var5);
            var6 = true;
         } else {
            var6 = ((AbstractContentSelector)var5).update();
         }

         NodeList var11 = ((AbstractContentSelector)var5).getSelectedContent();
         this.selectedNodes.put(var2, var11);

         for(int var12 = 0; var12 < var11.getLength(); ++var12) {
            Node var9 = var11.item(var12);
            this.xblManager.getRecord(var9).contentElement = var10;
         }

         if (var6) {
            var3 = true;
            this.dispatchContentSelectionChangedEvent(var10);
         }
      }

      return var3;
   }

   protected String getContentSelectorLanguage(Element var1) {
      String var2 = var1.getAttributeNS("http://xml.apache.org/batik/ext", "selectorLanguage");
      if (var2.length() != 0) {
         return var2;
      } else {
         var2 = var1.getOwnerDocument().getDocumentElement().getAttributeNS("http://xml.apache.org/batik/ext", "selectorLanguage");
         return var2.length() != 0 ? var2 : null;
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   protected class ShadowTreeSubtreeModifiedListener implements EventListener {
      public void handleEvent(Event var1) {
         if (ContentManager.this.removedNode != null) {
            ContentManager.this.removedNode = null;
            ContentManager.this.update(false);
         }

      }
   }

   protected class ShadowTreeNodeRemovedListener implements EventListener {
      public void handleEvent(Event var1) {
         EventTarget var2 = var1.getTarget();
         if (var2 instanceof XBLOMContentElement) {
            ContentManager.this.removedNode = (Node)var1.getTarget();
         }

      }
   }

   protected class ShadowTreeNodeInsertedListener implements EventListener {
      public void handleEvent(Event var1) {
         if (var1.getTarget() instanceof XBLOMContentElement) {
            ContentManager.this.update(false);
         }

      }
   }

   protected class DOMSubtreeModifiedEventListener implements EventListener {
      public void handleEvent(Event var1) {
         if (ContentManager.this.removedNode != null) {
            ContentManager.this.removedNode = null;
            ContentManager.this.update(false);
         }

      }
   }

   protected class DOMNodeRemovedEventListener implements EventListener {
      public void handleEvent(Event var1) {
         ContentManager.this.removedNode = (Node)var1.getTarget();
      }
   }

   protected class DOMNodeInsertedEventListener implements EventListener {
      public void handleEvent(Event var1) {
         ContentManager.this.update(false);
      }
   }

   protected class DOMAttrModifiedEventListener implements EventListener {
      public void handleEvent(Event var1) {
         if (var1.getTarget() != ContentManager.this.boundElement) {
            ContentManager.this.update(false);
         }

      }
   }

   protected class ContentElementDOMAttrModifiedEventListener implements EventListener {
      public void handleEvent(Event var1) {
         MutationEvent var2 = (MutationEvent)var1;
         Attr var3 = (Attr)var2.getRelatedNode();
         Element var4 = (Element)var1.getTarget();
         if (var4 instanceof XBLOMContentElement) {
            String var5 = var3.getNamespaceURI();
            String var6 = var3.getLocalName();
            if (var6 == null) {
               var6 = var3.getNodeName();
            }

            if (var5 == null && "includes".equals(var6) || "http://xml.apache.org/batik/ext".equals(var5) && "selectorLanguage".equals(var6)) {
               ContentManager.this.selectors.remove(var4);
               ContentManager.this.update(false);
            }
         }

      }
   }
}

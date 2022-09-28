package org.apache.batik.bridge.svg12;

import java.util.Iterator;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.BridgeUpdateHandler;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.ScriptingEnvironment;
import org.apache.batik.bridge.URIResolver;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.events.EventSupport;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.svg12.XBLEventSupport;
import org.apache.batik.dom.svg12.XBLOMShadowTreeElement;
import org.apache.batik.dom.xbl.NodeXBL;
import org.apache.batik.dom.xbl.XBLManager;
import org.apache.batik.script.Interpreter;
import org.apache.batik.script.InterpreterPool;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.svg.SVGDocument;

public class SVG12BridgeContext extends BridgeContext {
   protected XBLBindingListener bindingListener;
   protected XBLContentListener contentListener;
   protected EventTarget mouseCaptureTarget;
   protected boolean mouseCaptureSendAll;
   protected boolean mouseCaptureAutoRelease;

   public SVG12BridgeContext(UserAgent var1) {
      super(var1);
   }

   public SVG12BridgeContext(UserAgent var1, DocumentLoader var2) {
      super(var1, var2);
   }

   public SVG12BridgeContext(UserAgent var1, InterpreterPool var2, DocumentLoader var3) {
      super(var1, var2, var3);
   }

   public URIResolver createURIResolver(SVGDocument var1, DocumentLoader var2) {
      return new SVG12URIResolver(var1, var2);
   }

   public void addGVTListener(Document var1) {
      SVG12BridgeEventSupport.addGVTListener(this, var1);
   }

   public void dispose() {
      this.clearChildContexts();
      synchronized(this.eventListenerSet) {
         Iterator var2 = this.eventListenerSet.iterator();

         while(true) {
            if (!var2.hasNext()) {
               break;
            }

            BridgeContext.EventListenerMememto var3 = (BridgeContext.EventListenerMememto)var2.next();
            NodeEventTarget var4 = var3.getTarget();
            EventListener var5 = var3.getListener();
            boolean var6 = var3.getUseCapture();
            String var7 = var3.getEventType();
            boolean var8 = var3.getNamespaced();
            if (var4 != null && var5 != null && var7 != null) {
               String var9;
               if (var3 instanceof ImplementationEventListenerMememto) {
                  var9 = var3.getNamespaceURI();
                  Node var10 = (Node)var4;
                  AbstractNode var11 = (AbstractNode)var10.getOwnerDocument();
                  if (var11 != null) {
                     XBLEventSupport var12 = (XBLEventSupport)var11.initializeEventSupport();
                     var12.removeImplementationEventListenerNS(var9, var7, var5, var6);
                  }
               } else if (var8) {
                  var9 = var3.getNamespaceURI();
                  var4.removeEventListenerNS(var9, var7, var5, var6);
               } else {
                  var4.removeEventListener(var7, var5, var6);
               }
            }
         }
      }

      if (this.document != null) {
         this.removeDOMListeners();
         this.removeBindingListener();
      }

      if (this.animationEngine != null) {
         this.animationEngine.dispose();
         this.animationEngine = null;
      }

      Iterator var1 = this.interpreterMap.values().iterator();

      while(var1.hasNext()) {
         Interpreter var15 = (Interpreter)var1.next();
         if (var15 != null) {
            var15.dispose();
         }
      }

      this.interpreterMap.clear();
      if (this.focusManager != null) {
         this.focusManager.dispose();
      }

   }

   public void addBindingListener() {
      AbstractDocument var1 = (AbstractDocument)this.document;
      DefaultXBLManager var2 = (DefaultXBLManager)var1.getXBLManager();
      if (var2 != null) {
         this.bindingListener = new XBLBindingListener();
         var2.addBindingListener(this.bindingListener);
         this.contentListener = new XBLContentListener();
         var2.addContentSelectionChangedListener(this.contentListener);
      }

   }

   public void removeBindingListener() {
      AbstractDocument var1 = (AbstractDocument)this.document;
      XBLManager var2 = var1.getXBLManager();
      if (var2 instanceof DefaultXBLManager) {
         DefaultXBLManager var3 = (DefaultXBLManager)var2;
         var3.removeBindingListener(this.bindingListener);
         var3.removeContentSelectionChangedListener(this.contentListener);
      }

   }

   public void addDOMListeners() {
      SVGOMDocument var1 = (SVGOMDocument)this.document;
      XBLEventSupport var2 = (XBLEventSupport)var1.initializeEventSupport();
      this.domAttrModifiedEventListener = new EventListenerWrapper(new BridgeContext.DOMAttrModifiedEventListener());
      var2.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.domAttrModifiedEventListener, true);
      this.domNodeInsertedEventListener = new EventListenerWrapper(new BridgeContext.DOMNodeInsertedEventListener());
      var2.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", this.domNodeInsertedEventListener, true);
      this.domNodeRemovedEventListener = new EventListenerWrapper(new BridgeContext.DOMNodeRemovedEventListener());
      var2.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.domNodeRemovedEventListener, true);
      this.domCharacterDataModifiedEventListener = new EventListenerWrapper(new BridgeContext.DOMCharacterDataModifiedEventListener());
      var2.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMCharacterDataModified", this.domCharacterDataModifiedEventListener, true);
      this.animatedAttributeListener = new BridgeContext.AnimatedAttrListener();
      var1.addAnimatedAttributeListener(this.animatedAttributeListener);
      this.focusManager = new SVG12FocusManager(this.document);
      CSSEngine var3 = var1.getCSSEngine();
      this.cssPropertiesChangedListener = new BridgeContext.CSSPropertiesChangedListener();
      var3.addCSSEngineListener(this.cssPropertiesChangedListener);
   }

   public void addUIEventListeners(Document var1) {
      EventTarget var2 = (EventTarget)var1.getDocumentElement();
      AbstractNode var3 = (AbstractNode)var2;
      XBLEventSupport var4 = (XBLEventSupport)var3.initializeEventSupport();
      EventListenerWrapper var5 = new EventListenerWrapper(new BridgeContext.DOMMouseOverEventListener());
      var4.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "mouseover", var5, true);
      this.storeImplementationEventListenerNS(var2, "http://www.w3.org/2001/xml-events", "mouseover", var5, true);
      EventListenerWrapper var6 = new EventListenerWrapper(new BridgeContext.DOMMouseOutEventListener());
      var4.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "mouseout", var6, true);
      this.storeImplementationEventListenerNS(var2, "http://www.w3.org/2001/xml-events", "mouseout", var6, true);
   }

   public void removeUIEventListeners(Document var1) {
      EventTarget var2 = (EventTarget)var1.getDocumentElement();
      AbstractNode var3 = (AbstractNode)var2;
      XBLEventSupport var4 = (XBLEventSupport)var3.initializeEventSupport();
      synchronized(this.eventListenerSet) {
         Iterator var6 = this.eventListenerSet.iterator();

         while(var6.hasNext()) {
            BridgeContext.EventListenerMememto var7 = (BridgeContext.EventListenerMememto)var6.next();
            NodeEventTarget var8 = var7.getTarget();
            if (var8 == var2) {
               EventListener var9 = var7.getListener();
               boolean var10 = var7.getUseCapture();
               String var11 = var7.getEventType();
               boolean var12 = var7.getNamespaced();
               if (var8 != null && var9 != null && var11 != null) {
                  String var13;
                  if (var7 instanceof ImplementationEventListenerMememto) {
                     var13 = var7.getNamespaceURI();
                     var4.removeImplementationEventListenerNS(var13, var11, var9, var10);
                  } else if (var12) {
                     var13 = var7.getNamespaceURI();
                     var8.removeEventListenerNS(var13, var11, var9, var10);
                  } else {
                     var8.removeEventListener(var11, var9, var10);
                  }
               }
            }
         }

      }
   }

   protected void removeDOMListeners() {
      SVGOMDocument var1 = (SVGOMDocument)this.document;
      var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.domAttrModifiedEventListener, true);
      var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", this.domNodeInsertedEventListener, true);
      var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.domNodeRemovedEventListener, true);
      var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMCharacterDataModified", this.domCharacterDataModifiedEventListener, true);
      var1.removeAnimatedAttributeListener(this.animatedAttributeListener);
      CSSEngine var2 = var1.getCSSEngine();
      if (var2 != null) {
         var2.removeCSSEngineListener(this.cssPropertiesChangedListener);
         var2.dispose();
         var1.setCSSEngine((CSSEngine)null);
      }

   }

   protected void storeImplementationEventListenerNS(EventTarget var1, String var2, String var3, EventListener var4, boolean var5) {
      synchronized(this.eventListenerSet) {
         ImplementationEventListenerMememto var7 = new ImplementationEventListenerMememto(var1, var2, var3, var4, var5, this);
         this.eventListenerSet.add(var7);
      }
   }

   public BridgeContext createSubBridgeContext(SVGOMDocument var1) {
      CSSEngine var2 = var1.getCSSEngine();
      if (var2 != null) {
         return (BridgeContext)var1.getCSSEngine().getCSSContext();
      } else {
         BridgeContext var3 = super.createSubBridgeContext(var1);
         if (this.isDynamic() && var3.isDynamic()) {
            this.setUpdateManager(var3, this.updateManager);
            if (this.updateManager != null) {
               Object var4;
               if (var1.isSVG12()) {
                  var4 = new SVG12ScriptingEnvironment(var3);
               } else {
                  var4 = new ScriptingEnvironment(var3);
               }

               ((ScriptingEnvironment)var4).loadScripts();
               ((ScriptingEnvironment)var4).dispatchSVGLoadEvent();
               if (var1.isSVG12()) {
                  DefaultXBLManager var5 = new DefaultXBLManager(var1, var3);
                  this.setXBLManager(var3, var5);
                  var1.setXBLManager(var5);
                  var5.startProcessing();
               }
            }
         }

         return var3;
      }
   }

   public void startMouseCapture(EventTarget var1, boolean var2, boolean var3) {
      this.mouseCaptureTarget = var1;
      this.mouseCaptureSendAll = var2;
      this.mouseCaptureAutoRelease = var3;
   }

   public void stopMouseCapture() {
      this.mouseCaptureTarget = null;
   }

   protected class XBLContentListener implements ContentSelectionChangedListener {
      public void contentSelectionChanged(ContentSelectionChangedEvent var1) {
         Element var2 = (Element)var1.getContentElement().getParentNode();
         if (var2 instanceof XBLOMShadowTreeElement) {
            var2 = ((NodeXBL)var2).getXblBoundElement();
         }

         BridgeUpdateHandler var3 = SVG12BridgeContext.getBridgeUpdateHandler(var2);
         if (var3 instanceof SVG12BridgeUpdateHandler) {
            SVG12BridgeUpdateHandler var4 = (SVG12BridgeUpdateHandler)var3;

            try {
               var4.handleContentSelectionChangedEvent(var1);
            } catch (Exception var6) {
               SVG12BridgeContext.this.userAgent.displayError(var6);
            }
         }

      }
   }

   protected class XBLBindingListener implements BindingListener {
      public void bindingChanged(Element var1, Element var2) {
         BridgeUpdateHandler var3 = SVG12BridgeContext.getBridgeUpdateHandler(var1);
         if (var3 instanceof SVG12BridgeUpdateHandler) {
            SVG12BridgeUpdateHandler var4 = (SVG12BridgeUpdateHandler)var3;

            try {
               var4.handleBindingEvent(var1, var2);
            } catch (Exception var6) {
               SVG12BridgeContext.this.userAgent.displayError(var6);
            }
         }

      }
   }

   protected class EventListenerWrapper implements EventListener {
      protected EventListener listener;

      public EventListenerWrapper(EventListener var2) {
         this.listener = var2;
      }

      public void handleEvent(Event var1) {
         this.listener.handleEvent(EventSupport.getUltimateOriginalEvent(var1));
      }

      public String toString() {
         return super.toString() + " [wrapping " + this.listener.toString() + "]";
      }
   }

   protected static class ImplementationEventListenerMememto extends BridgeContext.EventListenerMememto {
      public ImplementationEventListenerMememto(EventTarget var1, String var2, EventListener var3, boolean var4, BridgeContext var5) {
         super(var1, var2, var3, var4, var5);
      }

      public ImplementationEventListenerMememto(EventTarget var1, String var2, String var3, EventListener var4, boolean var5, BridgeContext var6) {
         super(var1, var2, var3, var4, var5, var6);
      }
   }
}

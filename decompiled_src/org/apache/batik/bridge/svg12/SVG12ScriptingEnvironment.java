package org.apache.batik.bridge.svg12;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.Messages;
import org.apache.batik.bridge.SVGUtilities;
import org.apache.batik.bridge.ScriptingEnvironment;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.AbstractElement;
import org.apache.batik.dom.events.EventSupport;
import org.apache.batik.dom.svg12.SVGGlobal;
import org.apache.batik.dom.svg12.XBLEventSupport;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.dom.util.TriplyIndexedTable;
import org.apache.batik.script.Interpreter;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class SVG12ScriptingEnvironment extends ScriptingEnvironment {
   public static final String HANDLER_SCRIPT_DESCRIPTION = "SVG12ScriptingEnvironment.constant.handler.script.description";
   protected TriplyIndexedTable handlerScriptingListeners;

   public SVG12ScriptingEnvironment(BridgeContext var1) {
      super(var1);
   }

   protected void addDocumentListeners() {
      this.domNodeInsertedListener = new DOMNodeInsertedListener();
      this.domNodeRemovedListener = new DOMNodeRemovedListener();
      this.domAttrModifiedListener = new DOMAttrModifiedListener();
      AbstractDocument var1 = (AbstractDocument)this.document;
      XBLEventSupport var2 = (XBLEventSupport)var1.initializeEventSupport();
      var2.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", this.domNodeInsertedListener, false);
      var2.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.domNodeRemovedListener, false);
      var2.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.domAttrModifiedListener, false);
   }

   protected void removeDocumentListeners() {
      AbstractDocument var1 = (AbstractDocument)this.document;
      XBLEventSupport var2 = (XBLEventSupport)var1.initializeEventSupport();
      var2.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", this.domNodeInsertedListener, false);
      var2.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.domNodeRemovedListener, false);
      var2.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.domAttrModifiedListener, false);
   }

   protected void addScriptingListenersOn(Element var1) {
      String var2 = var1.getNamespaceURI();
      String var3 = var1.getLocalName();
      if ("http://www.w3.org/2000/svg".equals(var2) && "handler".equals(var3)) {
         AbstractElement var4 = (AbstractElement)var1.getParentNode();
         String var5 = var1.getAttributeNS("http://www.w3.org/2001/xml-events", "event");
         String var6 = "http://www.w3.org/2001/xml-events";
         if (var5.indexOf(58) != -1) {
            String var7 = DOMUtilities.getPrefix(var5);
            var5 = DOMUtilities.getLocalName(var5);
            var6 = ((AbstractElement)var1).lookupNamespaceURI(var7);
         }

         HandlerScriptingEventListener var8 = new HandlerScriptingEventListener(var6, var5, (AbstractElement)var1);
         var4.addEventListenerNS(var6, var5, var8, false, (Object)null);
         if (this.handlerScriptingListeners == null) {
            this.handlerScriptingListeners = new TriplyIndexedTable();
         }

         this.handlerScriptingListeners.put(var6, var5, var1, var8);
      }

      super.addScriptingListenersOn(var1);
   }

   protected void removeScriptingListenersOn(Element var1) {
      String var2 = var1.getNamespaceURI();
      String var3 = var1.getLocalName();
      if ("http://www.w3.org/2000/svg".equals(var2) && "handler".equals(var3)) {
         AbstractElement var4 = (AbstractElement)var1.getParentNode();
         String var5 = var1.getAttributeNS("http://www.w3.org/2001/xml-events", "event");
         String var6 = "http://www.w3.org/2001/xml-events";
         if (var5.indexOf(58) != -1) {
            String var7 = DOMUtilities.getPrefix(var5);
            var5 = DOMUtilities.getLocalName(var5);
            var6 = ((AbstractElement)var1).lookupNamespaceURI(var7);
         }

         EventListener var8 = (EventListener)this.handlerScriptingListeners.put(var6, var5, var1, (Object)null);
         var4.removeEventListenerNS(var6, var5, var8, false);
      }

      super.removeScriptingListenersOn(var1);
   }

   public org.apache.batik.script.Window createWindow(Interpreter var1, String var2) {
      return new Global(var1, var2);
   }

   protected class Global extends ScriptingEnvironment.Window implements SVGGlobal {
      public Global(Interpreter var2, String var3) {
         super(var2, var3);
      }

      public void startMouseCapture(EventTarget var1, boolean var2, boolean var3) {
         ((SVG12BridgeContext)SVG12ScriptingEnvironment.this.bridgeContext.getPrimaryBridgeContext()).startMouseCapture(var1, var2, var3);
      }

      public void stopMouseCapture() {
         ((SVG12BridgeContext)SVG12ScriptingEnvironment.this.bridgeContext.getPrimaryBridgeContext()).stopMouseCapture();
      }
   }

   protected class HandlerScriptingEventListener implements EventListener {
      protected String eventNamespaceURI;
      protected String eventType;
      protected AbstractElement handlerElement;

      public HandlerScriptingEventListener(String var2, String var3, AbstractElement var4) {
         this.eventNamespaceURI = var2;
         this.eventType = var3;
         this.handlerElement = var4;
      }

      public void handleEvent(Event var1) {
         Element var2 = (Element)var1.getCurrentTarget();
         String var3 = this.handlerElement.getTextContent();
         if (var3.length() != 0) {
            DocumentLoader var4 = SVG12ScriptingEnvironment.this.bridgeContext.getDocumentLoader();
            AbstractDocument var5 = (AbstractDocument)this.handlerElement.getOwnerDocument();
            int var6 = var4.getLineNumber(this.handlerElement);
            String var7 = Messages.formatMessage("SVG12ScriptingEnvironment.constant.handler.script.description", new Object[]{var5.getDocumentURI(), this.eventNamespaceURI, this.eventType, new Integer(var6)});
            String var8 = this.handlerElement.getAttributeNS((String)null, "contentScriptType");
            if (var8.length() == 0) {
               Element var9;
               for(var9 = var2; var9 != null && (!"http://www.w3.org/2000/svg".equals(var9.getNamespaceURI()) || !"svg".equals(var9.getLocalName())); var9 = SVGUtilities.getParentElement(var9)) {
               }

               if (var9 == null) {
                  return;
               }

               var8 = var9.getAttributeNS((String)null, "contentScriptType");
            }

            SVG12ScriptingEnvironment.this.runEventHandler(var3, var1, var8, var7);
         }
      }
   }

   protected class DOMAttrModifiedListener extends ScriptingEnvironment.DOMAttrModifiedListener {
      protected DOMAttrModifiedListener() {
         super();
      }

      public void handleEvent(Event var1) {
         super.handleEvent(EventSupport.getUltimateOriginalEvent(var1));
      }
   }

   protected class DOMNodeRemovedListener extends ScriptingEnvironment.DOMNodeRemovedListener {
      protected DOMNodeRemovedListener() {
         super();
      }

      public void handleEvent(Event var1) {
         super.handleEvent(EventSupport.getUltimateOriginalEvent(var1));
      }
   }

   protected class DOMNodeInsertedListener extends ScriptingEnvironment.DOMNodeInsertedListener {
      protected DOMNodeInsertedListener() {
         super();
      }

      public void handleEvent(Event var1) {
         super.handleEvent(EventSupport.getUltimateOriginalEvent(var1));
      }
   }
}

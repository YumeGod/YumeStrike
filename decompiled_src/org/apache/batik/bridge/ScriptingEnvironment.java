package org.apache.batik.bridge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.dom.util.SAXDocumentFactory;
import org.apache.batik.script.Interpreter;
import org.apache.batik.script.InterpreterException;
import org.apache.batik.script.ScriptEventWrapper;
import org.apache.batik.util.EncodingUtilities;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.RunnableQueue;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.svg.SVGDocument;

public class ScriptingEnvironment extends BaseScriptingEnvironment {
   public static final String[] SVG_EVENT_ATTRS = new String[]{"onabort", "onerror", "onresize", "onscroll", "onunload", "onzoom", "onbegin", "onend", "onrepeat", "onfocusin", "onfocusout", "onactivate", "onclick", "onmousedown", "onmouseup", "onmouseover", "onmouseout", "onmousemove", "onkeypress", "onkeydown", "onkeyup"};
   public static final String[] SVG_DOM_EVENT = new String[]{"SVGAbort", "SVGError", "SVGResize", "SVGScroll", "SVGUnload", "SVGZoom", "beginEvent", "endEvent", "repeatEvent", "DOMFocusIn", "DOMFocusOut", "DOMActivate", "click", "mousedown", "mouseup", "mouseover", "mouseout", "mousemove", "keypress", "keydown", "keyup"};
   protected Timer timer = new Timer(true);
   protected UpdateManager updateManager;
   protected RunnableQueue updateRunnableQueue;
   protected EventListener domNodeInsertedListener;
   protected EventListener domNodeRemovedListener;
   protected EventListener domAttrModifiedListener;
   protected EventListener svgAbortListener = new ScriptingEventListener("onabort");
   protected EventListener svgErrorListener = new ScriptingEventListener("onerror");
   protected EventListener svgResizeListener = new ScriptingEventListener("onresize");
   protected EventListener svgScrollListener = new ScriptingEventListener("onscroll");
   protected EventListener svgUnloadListener = new ScriptingEventListener("onunload");
   protected EventListener svgZoomListener = new ScriptingEventListener("onzoom");
   protected EventListener beginListener = new ScriptingEventListener("onbegin");
   protected EventListener endListener = new ScriptingEventListener("onend");
   protected EventListener repeatListener = new ScriptingEventListener("onrepeat");
   protected EventListener focusinListener = new ScriptingEventListener("onfocusin");
   protected EventListener focusoutListener = new ScriptingEventListener("onfocusout");
   protected EventListener activateListener = new ScriptingEventListener("onactivate");
   protected EventListener clickListener = new ScriptingEventListener("onclick");
   protected EventListener mousedownListener = new ScriptingEventListener("onmousedown");
   protected EventListener mouseupListener = new ScriptingEventListener("onmouseup");
   protected EventListener mouseoverListener = new ScriptingEventListener("onmouseover");
   protected EventListener mouseoutListener = new ScriptingEventListener("onmouseout");
   protected EventListener mousemoveListener = new ScriptingEventListener("onmousemove");
   protected EventListener keypressListener = new ScriptingEventListener("onkeypress");
   protected EventListener keydownListener = new ScriptingEventListener("onkeydown");
   protected EventListener keyupListener = new ScriptingEventListener("onkeyup");
   protected EventListener[] listeners;
   Map attrToDOMEvent;
   Map attrToListener;

   public ScriptingEnvironment(BridgeContext var1) {
      super(var1);
      this.listeners = new EventListener[]{this.svgAbortListener, this.svgErrorListener, this.svgResizeListener, this.svgScrollListener, this.svgUnloadListener, this.svgZoomListener, this.beginListener, this.endListener, this.repeatListener, this.focusinListener, this.focusoutListener, this.activateListener, this.clickListener, this.mousedownListener, this.mouseupListener, this.mouseoverListener, this.mouseoutListener, this.mousemoveListener, this.keypressListener, this.keydownListener, this.keyupListener};
      this.attrToDOMEvent = new HashMap(SVG_EVENT_ATTRS.length);
      this.attrToListener = new HashMap(SVG_EVENT_ATTRS.length);

      for(int var2 = 0; var2 < SVG_EVENT_ATTRS.length; ++var2) {
         this.attrToDOMEvent.put(SVG_EVENT_ATTRS[var2], SVG_DOM_EVENT[var2]);
         this.attrToListener.put(SVG_EVENT_ATTRS[var2], this.listeners[var2]);
      }

      this.updateManager = var1.getUpdateManager();
      this.updateRunnableQueue = this.updateManager.getUpdateRunnableQueue();
      this.addScriptingListeners(this.document.getDocumentElement());
      this.addDocumentListeners();
   }

   protected void addDocumentListeners() {
      this.domNodeInsertedListener = new DOMNodeInsertedListener();
      this.domNodeRemovedListener = new DOMNodeRemovedListener();
      this.domAttrModifiedListener = new DOMAttrModifiedListener();
      NodeEventTarget var1 = (NodeEventTarget)this.document;
      var1.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", this.domNodeInsertedListener, false, (Object)null);
      var1.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.domNodeRemovedListener, false, (Object)null);
      var1.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.domAttrModifiedListener, false, (Object)null);
   }

   protected void removeDocumentListeners() {
      NodeEventTarget var1 = (NodeEventTarget)this.document;
      var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", this.domNodeInsertedListener, false);
      var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.domNodeRemovedListener, false);
      var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.domAttrModifiedListener, false);
   }

   public org.apache.batik.script.Window createWindow(Interpreter var1, String var2) {
      return new Window(var1, var2);
   }

   public void runEventHandler(String var1, Event var2, String var3, String var4) {
      Interpreter var5 = this.getInterpreter(var3);
      if (var5 != null) {
         try {
            this.checkCompatibleScriptURL(var3, this.docPURL);
            Object var6;
            if (var2 instanceof ScriptEventWrapper) {
               var6 = ((ScriptEventWrapper)var2).getEventObject();
            } else {
               var6 = var2;
            }

            var5.bindObject("event", var6);
            var5.bindObject("evt", var6);
            var5.evaluate(new StringReader(var1), var4);
         } catch (IOException var7) {
         } catch (InterpreterException var8) {
            this.handleInterpreterException(var8);
         } catch (SecurityException var9) {
            this.handleSecurityException(var9);
         }

      }
   }

   public void interrupt() {
      this.timer.cancel();
      this.removeScriptingListeners(this.document.getDocumentElement());
      this.removeDocumentListeners();
   }

   public void addScriptingListeners(Node var1) {
      if (var1.getNodeType() == 1) {
         this.addScriptingListenersOn((Element)var1);
      }

      for(Node var2 = var1.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         this.addScriptingListeners(var2);
      }

   }

   protected void addScriptingListenersOn(Element var1) {
      NodeEventTarget var2 = (NodeEventTarget)var1;
      if ("http://www.w3.org/2000/svg".equals(var1.getNamespaceURI())) {
         if ("svg".equals(var1.getLocalName())) {
            if (var1.hasAttributeNS((String)null, "onabort")) {
               var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "SVGAbort", this.svgAbortListener, false, (Object)null);
            }

            if (var1.hasAttributeNS((String)null, "onerror")) {
               var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "SVGError", this.svgErrorListener, false, (Object)null);
            }

            if (var1.hasAttributeNS((String)null, "onresize")) {
               var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "SVGResize", this.svgResizeListener, false, (Object)null);
            }

            if (var1.hasAttributeNS((String)null, "onscroll")) {
               var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "SVGScroll", this.svgScrollListener, false, (Object)null);
            }

            if (var1.hasAttributeNS((String)null, "onunload")) {
               var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "SVGUnload", this.svgUnloadListener, false, (Object)null);
            }

            if (var1.hasAttributeNS((String)null, "onzoom")) {
               var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "SVGZoom", this.svgZoomListener, false, (Object)null);
            }
         } else {
            String var3 = var1.getLocalName();
            if (var3.equals("set") || var3.startsWith("animate")) {
               if (var1.hasAttributeNS((String)null, "onbegin")) {
                  var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "beginEvent", this.beginListener, false, (Object)null);
               }

               if (var1.hasAttributeNS((String)null, "onend")) {
                  var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "endEvent", this.endListener, false, (Object)null);
               }

               if (var1.hasAttributeNS((String)null, "onrepeat")) {
                  var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "repeatEvent", this.repeatListener, false, (Object)null);
               }

               return;
            }
         }
      }

      if (var1.hasAttributeNS((String)null, "onfocusin")) {
         var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMFocusIn", this.focusinListener, false, (Object)null);
      }

      if (var1.hasAttributeNS((String)null, "onfocusout")) {
         var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMFocusOut", this.focusoutListener, false, (Object)null);
      }

      if (var1.hasAttributeNS((String)null, "onactivate")) {
         var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMActivate", this.activateListener, false, (Object)null);
      }

      if (var1.hasAttributeNS((String)null, "onclick")) {
         var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "click", this.clickListener, false, (Object)null);
      }

      if (var1.hasAttributeNS((String)null, "onmousedown")) {
         var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "mousedown", this.mousedownListener, false, (Object)null);
      }

      if (var1.hasAttributeNS((String)null, "onmouseup")) {
         var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseup", this.mouseupListener, false, (Object)null);
      }

      if (var1.hasAttributeNS((String)null, "onmouseover")) {
         var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseover", this.mouseoverListener, false, (Object)null);
      }

      if (var1.hasAttributeNS((String)null, "onmouseout")) {
         var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseout", this.mouseoutListener, false, (Object)null);
      }

      if (var1.hasAttributeNS((String)null, "onmousemove")) {
         var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "mousemove", this.mousemoveListener, false, (Object)null);
      }

      if (var1.hasAttributeNS((String)null, "onkeypress")) {
         var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "keypress", this.keypressListener, false, (Object)null);
      }

      if (var1.hasAttributeNS((String)null, "onkeydown")) {
         var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "keydown", this.keydownListener, false, (Object)null);
      }

      if (var1.hasAttributeNS((String)null, "onkeyup")) {
         var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "keyup", this.keyupListener, false, (Object)null);
      }

   }

   protected void removeScriptingListeners(Node var1) {
      if (var1.getNodeType() == 1) {
         this.removeScriptingListenersOn((Element)var1);
      }

      for(Node var2 = var1.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         this.removeScriptingListeners(var2);
      }

   }

   protected void removeScriptingListenersOn(Element var1) {
      NodeEventTarget var2 = (NodeEventTarget)var1;
      if ("http://www.w3.org/2000/svg".equals(var1.getNamespaceURI())) {
         if ("svg".equals(var1.getLocalName())) {
            var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "SVGAbort", this.svgAbortListener, false);
            var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "SVGError", this.svgErrorListener, false);
            var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "SVGResize", this.svgResizeListener, false);
            var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "SVGScroll", this.svgScrollListener, false);
            var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "SVGUnload", this.svgUnloadListener, false);
            var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "SVGZoom", this.svgZoomListener, false);
         } else {
            String var3 = var1.getLocalName();
            if (var3.equals("set") || var3.startsWith("animate")) {
               var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "beginEvent", this.beginListener, false);
               var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "endEvent", this.endListener, false);
               var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "repeatEvent", this.repeatListener, false);
               return;
            }
         }
      }

      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMFocusIn", this.focusinListener, false);
      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMFocusOut", this.focusoutListener, false);
      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMActivate", this.activateListener, false);
      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "click", this.clickListener, false);
      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mousedown", this.mousedownListener, false);
      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mouseup", this.mouseupListener, false);
      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mouseover", this.mouseoverListener, false);
      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mouseout", this.mouseoutListener, false);
      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mousemove", this.mousemoveListener, false);
      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "keypress", this.keypressListener, false);
      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "keydown", this.keydownListener, false);
      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "keyup", this.keyupListener, false);
   }

   protected void updateScriptingListeners(Element var1, String var2) {
      String var3 = (String)this.attrToDOMEvent.get(var2);
      if (var3 != null) {
         EventListener var4 = (EventListener)this.attrToListener.get(var2);
         NodeEventTarget var5 = (NodeEventTarget)var1;
         if (var1.hasAttributeNS((String)null, var2)) {
            var5.addEventListenerNS("http://www.w3.org/2001/xml-events", var3, var4, false, (Object)null);
         } else {
            var5.removeEventListenerNS("http://www.w3.org/2001/xml-events", var3, var4, false);
         }

      }
   }

   protected class ScriptingEventListener implements EventListener {
      protected String attribute;

      public ScriptingEventListener(String var2) {
         this.attribute = var2;
      }

      public void handleEvent(Event var1) {
         Element var2 = (Element)var1.getCurrentTarget();
         String var3 = var2.getAttributeNS((String)null, this.attribute);
         if (var3.length() != 0) {
            DocumentLoader var4 = ScriptingEnvironment.this.bridgeContext.getDocumentLoader();
            SVGDocument var5 = (SVGDocument)var2.getOwnerDocument();
            int var6 = var4.getLineNumber(var2);
            String var7 = Messages.formatMessage("BaseScriptingEnvironment.constant.event.script.description", new Object[]{var5.getURL(), this.attribute, new Integer(var6)});

            Element var8;
            for(var8 = var2; var8 != null && (!"http://www.w3.org/2000/svg".equals(var8.getNamespaceURI()) || !"svg".equals(var8.getLocalName())); var8 = SVGUtilities.getParentElement(var8)) {
            }

            if (var8 != null) {
               String var9 = var8.getAttributeNS((String)null, "contentScriptType");
               ScriptingEnvironment.this.runEventHandler(var3, var1, var9, var7);
            }
         }
      }
   }

   protected class DOMAttrModifiedListener implements EventListener {
      public void handleEvent(Event var1) {
         MutationEvent var2 = (MutationEvent)var1;
         if (var2.getAttrChange() != 1) {
            ScriptingEnvironment.this.updateScriptingListeners((Element)var2.getTarget(), var2.getAttrName());
         }

      }
   }

   protected class DOMNodeRemovedListener implements EventListener {
      public void handleEvent(Event var1) {
         ScriptingEnvironment.this.removeScriptingListeners((Node)var1.getTarget());
      }
   }

   protected class DOMNodeInsertedListener implements EventListener {
      public void handleEvent(Event var1) {
         ScriptingEnvironment.this.addScriptingListeners((Node)var1.getTarget());
      }
   }

   protected class Window implements org.apache.batik.script.Window {
      protected Interpreter interpreter;
      protected String language;
      static final String DEFLATE = "deflate";
      static final String GZIP = "gzip";
      static final String UTF_8 = "UTF-8";

      public Window(Interpreter var2, String var3) {
         this.interpreter = var2;
         this.language = var3;
      }

      public Object setInterval(final String var1, long var2) {
         TimerTask var4 = new TimerTask() {
            EvaluateIntervalRunnable eir;

            {
               this.eir = ScriptingEnvironment.this.new EvaluateIntervalRunnable(var1, Window.this.interpreter);
            }

            public void run() {
               synchronized(this.eir) {
                  if (this.eir.count > 1) {
                     return;
                  }

                  ++this.eir.count;
               }

               synchronized(ScriptingEnvironment.this.updateRunnableQueue.getIteratorLock()) {
                  if (ScriptingEnvironment.this.updateRunnableQueue.getThread() == null) {
                     this.cancel();
                     return;
                  }

                  ScriptingEnvironment.this.updateRunnableQueue.invokeLater(this.eir);
               }

               synchronized(this.eir) {
                  if (this.eir.error) {
                     this.cancel();
                  }

               }
            }
         };
         ScriptingEnvironment.this.timer.schedule(var4, var2, var2);
         return var4;
      }

      public Object setInterval(final Runnable var1, long var2) {
         TimerTask var4 = new TimerTask() {
            EvaluateRunnableRunnable eihr = ScriptingEnvironment.this.new EvaluateRunnableRunnable(var1);

            public void run() {
               synchronized(this.eihr) {
                  if (this.eihr.count > 1) {
                     return;
                  }

                  ++this.eihr.count;
               }

               ScriptingEnvironment.this.updateRunnableQueue.invokeLater(this.eihr);
               synchronized(this.eihr) {
                  if (this.eihr.error) {
                     this.cancel();
                  }

               }
            }
         };
         ScriptingEnvironment.this.timer.schedule(var4, var2, var2);
         return var4;
      }

      public void clearInterval(Object var1) {
         if (var1 != null) {
            ((TimerTask)var1).cancel();
         }
      }

      public Object setTimeout(final String var1, long var2) {
         TimerTask var4 = new TimerTask() {
            public void run() {
               ScriptingEnvironment.this.updateRunnableQueue.invokeLater(ScriptingEnvironment.this.new EvaluateRunnable(var1, Window.this.interpreter));
            }
         };
         ScriptingEnvironment.this.timer.schedule(var4, var2);
         return var4;
      }

      public Object setTimeout(final Runnable var1, long var2) {
         TimerTask var4 = new TimerTask() {
            public void run() {
               ScriptingEnvironment.this.updateRunnableQueue.invokeLater(new Runnable() {
                  public void run() {
                     try {
                        var1.run();
                     } catch (Exception var2) {
                        if (ScriptingEnvironment.this.userAgent != null) {
                           ScriptingEnvironment.this.userAgent.displayError(var2);
                        }
                     }

                  }
               });
            }
         };
         ScriptingEnvironment.this.timer.schedule(var4, var2);
         return var4;
      }

      public void clearTimeout(Object var1) {
         if (var1 != null) {
            ((TimerTask)var1).cancel();
         }
      }

      public Node parseXML(String var1, Document var2) {
         SAXSVGDocumentFactory var3 = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
         URL var4 = null;
         if (var2 instanceof SVGOMDocument) {
            var4 = ((SVGOMDocument)var2).getURLObject();
         }

         if (var4 == null) {
            var4 = ((SVGOMDocument)ScriptingEnvironment.this.bridgeContext.getDocument()).getURLObject();
         }

         String var5 = var4 == null ? "" : var4.toString();
         Node var6 = DOMUtilities.parseXML(var1, var2, var5, (Map)null, (String)null, var3);
         if (var6 != null) {
            return var6;
         } else {
            if (var2 instanceof SVGOMDocument) {
               HashMap var7 = new HashMap();
               var7.put("xmlns", "http://www.w3.org/2000/xmlns/");
               var7.put("xmlns:xlink", "http://www.w3.org/1999/xlink");
               var6 = DOMUtilities.parseXML(var1, var2, var5, var7, "svg", var3);
               if (var6 != null) {
                  return var6;
               }
            }

            SAXDocumentFactory var8;
            if (var2 != null) {
               var8 = new SAXDocumentFactory(var2.getImplementation(), XMLResourceDescriptor.getXMLParserClassName());
            } else {
               var8 = new SAXDocumentFactory(new GenericDOMImplementation(), XMLResourceDescriptor.getXMLParserClassName());
            }

            return DOMUtilities.parseXML(var1, var2, var5, (Map)null, (String)null, var8);
         }
      }

      public void getURL(String var1, org.apache.batik.script.Window.URLResponseHandler var2) {
         this.getURL(var1, var2, (String)null);
      }

      public void getURL(final String var1, final org.apache.batik.script.Window.URLResponseHandler var2, final String var3) {
         Thread var4 = new Thread() {
            public void run() {
               try {
                  ParsedURL var1x = ((SVGOMDocument)ScriptingEnvironment.this.document).getParsedURL();
                  final ParsedURL var2x = new ParsedURL(var1x, var1);
                  String var3x = null;
                  if (var3 != null) {
                     var3x = EncodingUtilities.javaEncoding(var3);
                     var3x = var3x == null ? var3 : var3x;
                  }

                  InputStream var4 = var2x.openStream();
                  InputStreamReader var5;
                  if (var3x == null) {
                     var5 = new InputStreamReader(var4);
                  } else {
                     try {
                        var5 = new InputStreamReader(var4, var3x);
                     } catch (UnsupportedEncodingException var9) {
                        var5 = new InputStreamReader(var4);
                     }
                  }

                  BufferedReader var11 = new BufferedReader(var5);
                  final StringBuffer var6 = new StringBuffer();
                  char[] var8 = new char[4096];

                  int var7;
                  while((var7 = var11.read(var8, 0, var8.length)) != -1) {
                     var6.append(var8, 0, var7);
                  }

                  var11.close();
                  ScriptingEnvironment.this.updateRunnableQueue.invokeLater(new Runnable() {
                     public void run() {
                        try {
                           var2.getURLDone(true, var2x.getContentType(), var6.toString());
                        } catch (Exception var2xx) {
                           if (ScriptingEnvironment.this.userAgent != null) {
                              ScriptingEnvironment.this.userAgent.displayError(var2xx);
                           }
                        }

                     }
                  });
               } catch (Exception var10) {
                  if (var10 instanceof SecurityException) {
                     ScriptingEnvironment.this.userAgent.displayError(var10);
                  }

                  ScriptingEnvironment.this.updateRunnableQueue.invokeLater(new Runnable() {
                     public void run() {
                        try {
                           var2.getURLDone(false, (String)null, (String)null);
                        } catch (Exception var2x) {
                           if (ScriptingEnvironment.this.userAgent != null) {
                              ScriptingEnvironment.this.userAgent.displayError(var2x);
                           }
                        }

                     }
                  });
               }

            }
         };
         var4.setPriority(1);
         var4.start();
      }

      public void postURL(String var1, String var2, org.apache.batik.script.Window.URLResponseHandler var3) {
         this.postURL(var1, var2, var3, "text/plain", (String)null);
      }

      public void postURL(String var1, String var2, org.apache.batik.script.Window.URLResponseHandler var3, String var4) {
         this.postURL(var1, var2, var3, var4, (String)null);
      }

      public void postURL(final String var1, final String var2, final org.apache.batik.script.Window.URLResponseHandler var3, final String var4, final String var5) {
         Thread var6 = new Thread() {
            public void run() {
               try {
                  String var1x = ((SVGOMDocument)ScriptingEnvironment.this.document).getDocumentURI();
                  URL var2x;
                  if (var1x == null) {
                     var2x = new URL(var1);
                  } else {
                     var2x = new URL(new URL(var1x), var1);
                  }

                  final URLConnection var3x = var2x.openConnection();
                  var3x.setDoOutput(true);
                  var3x.setDoInput(true);
                  var3x.setUseCaches(false);
                  var3x.setRequestProperty("Content-Type", var4);
                  Object var4x = var3x.getOutputStream();
                  String var5x = null;
                  String var6 = var5;
                  if (var6 != null) {
                     if (var6.startsWith("deflate")) {
                        var4x = new DeflaterOutputStream((OutputStream)var4x);
                        if (var6.length() > "deflate".length()) {
                           var6 = var6.substring("deflate".length() + 1);
                        } else {
                           var6 = "";
                        }

                        var3x.setRequestProperty("Content-Encoding", "deflate");
                     }

                     if (var6.startsWith("gzip")) {
                        var4x = new GZIPOutputStream((OutputStream)var4x);
                        if (var6.length() > "gzip".length()) {
                           var6 = var6.substring("gzip".length() + 1);
                        } else {
                           var6 = "";
                        }

                        var3x.setRequestProperty("Content-Encoding", "deflate");
                     }

                     if (var6.length() != 0) {
                        var5x = EncodingUtilities.javaEncoding(var6);
                        if (var5x == null) {
                           var5x = "UTF-8";
                        }
                     } else {
                        var5x = "UTF-8";
                     }
                  }

                  OutputStreamWriter var7;
                  if (var5x == null) {
                     var7 = new OutputStreamWriter((OutputStream)var4x);
                  } else {
                     var7 = new OutputStreamWriter((OutputStream)var4x, var5x);
                  }

                  var7.write(var2);
                  var7.flush();
                  var7.close();
                  ((OutputStream)var4x).close();
                  InputStream var8 = var3x.getInputStream();
                  var5x = "UTF-8";
                  InputStreamReader var9;
                  if (var5x == null) {
                     var9 = new InputStreamReader(var8);
                  } else {
                     var9 = new InputStreamReader(var8, var5x);
                  }

                  BufferedReader var14 = new BufferedReader(var9);
                  final StringBuffer var10 = new StringBuffer();
                  char[] var12 = new char[4096];

                  int var11;
                  while((var11 = var14.read(var12, 0, var12.length)) != -1) {
                     var10.append(var12, 0, var11);
                  }

                  var14.close();
                  ScriptingEnvironment.this.updateRunnableQueue.invokeLater(new Runnable() {
                     public void run() {
                        try {
                           var3.getURLDone(true, var3x.getContentType(), var10.toString());
                        } catch (Exception var2x) {
                           if (ScriptingEnvironment.this.userAgent != null) {
                              ScriptingEnvironment.this.userAgent.displayError(var2x);
                           }
                        }

                     }
                  });
               } catch (Exception var13) {
                  if (var13 instanceof SecurityException) {
                     ScriptingEnvironment.this.userAgent.displayError(var13);
                  }

                  ScriptingEnvironment.this.updateRunnableQueue.invokeLater(new Runnable() {
                     public void run() {
                        try {
                           var3.getURLDone(false, (String)null, (String)null);
                        } catch (Exception var2x) {
                           if (ScriptingEnvironment.this.userAgent != null) {
                              ScriptingEnvironment.this.userAgent.displayError(var2x);
                           }
                        }

                     }
                  });
               }

            }
         };
         var6.setPriority(1);
         var6.start();
      }

      public void alert(String var1) {
         if (ScriptingEnvironment.this.userAgent != null) {
            ScriptingEnvironment.this.userAgent.showAlert(var1);
         }

      }

      public boolean confirm(String var1) {
         return ScriptingEnvironment.this.userAgent != null ? ScriptingEnvironment.this.userAgent.showConfirm(var1) : false;
      }

      public String prompt(String var1) {
         return ScriptingEnvironment.this.userAgent != null ? ScriptingEnvironment.this.userAgent.showPrompt(var1) : null;
      }

      public String prompt(String var1, String var2) {
         return ScriptingEnvironment.this.userAgent != null ? ScriptingEnvironment.this.userAgent.showPrompt(var1, var2) : null;
      }

      public BridgeContext getBridgeContext() {
         return ScriptingEnvironment.this.bridgeContext;
      }

      public Interpreter getInterpreter() {
         return this.interpreter;
      }
   }

   protected class EvaluateRunnableRunnable implements Runnable {
      public int count;
      public boolean error;
      protected Runnable runnable;

      public EvaluateRunnableRunnable(Runnable var2) {
         this.runnable = var2;
      }

      public void run() {
         synchronized(this) {
            if (this.error) {
               return;
            }

            --this.count;
         }

         try {
            this.runnable.run();
         } catch (Exception var5) {
            if (ScriptingEnvironment.this.userAgent != null) {
               ScriptingEnvironment.this.userAgent.displayError(var5);
            } else {
               var5.printStackTrace();
            }

            synchronized(this) {
               this.error = true;
            }
         }

      }
   }

   protected class EvaluateIntervalRunnable implements Runnable {
      public int count;
      public boolean error;
      protected Interpreter interpreter;
      protected String script;

      public EvaluateIntervalRunnable(String var2, Interpreter var3) {
         this.interpreter = var3;
         this.script = var2;
      }

      public void run() {
         synchronized(this) {
            if (this.error) {
               return;
            }

            --this.count;
         }

         try {
            this.interpreter.evaluate(this.script);
         } catch (InterpreterException var7) {
            ScriptingEnvironment.this.handleInterpreterException(var7);
            synchronized(this) {
               this.error = true;
            }
         } catch (Exception var8) {
            if (ScriptingEnvironment.this.userAgent != null) {
               ScriptingEnvironment.this.userAgent.displayError(var8);
            } else {
               var8.printStackTrace();
            }

            synchronized(this) {
               this.error = true;
            }
         }

      }
   }

   protected class EvaluateRunnable implements Runnable {
      protected Interpreter interpreter;
      protected String script;

      public EvaluateRunnable(String var2, Interpreter var3) {
         this.interpreter = var3;
         this.script = var2;
      }

      public void run() {
         try {
            this.interpreter.evaluate(this.script);
         } catch (InterpreterException var2) {
            ScriptingEnvironment.this.handleInterpreterException(var2);
         }

      }
   }
}

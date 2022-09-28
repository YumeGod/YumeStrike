package org.apache.batik.bridge;

import java.awt.Cursor;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.apache.batik.bridge.svg12.SVG12BridgeContext;
import org.apache.batik.bridge.svg12.SVG12BridgeExtension;
import org.apache.batik.css.engine.CSSContext;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSEngineEvent;
import org.apache.batik.css.engine.CSSEngineListener;
import org.apache.batik.css.engine.CSSEngineUserAgent;
import org.apache.batik.css.engine.SystemColorSupport;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.dom.svg.AnimatedAttributeListener;
import org.apache.batik.dom.svg.AnimatedLiveAttributeValue;
import org.apache.batik.dom.svg.SVGContext;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.svg.SVGOMElement;
import org.apache.batik.dom.svg.SVGStylableElement;
import org.apache.batik.dom.xbl.XBLManager;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.TextPainter;
import org.apache.batik.script.Interpreter;
import org.apache.batik.script.InterpreterPool;
import org.apache.batik.util.CleanerThread;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

public class BridgeContext implements ErrorConstants, CSSContext {
   protected Document document;
   protected boolean isSVG12;
   protected GVTBuilder gvtBuilder;
   protected Map interpreterMap;
   private Map fontFamilyMap;
   protected Map viewportMap;
   protected List viewportStack;
   protected UserAgent userAgent;
   protected Map elementNodeMap;
   protected Map nodeElementMap;
   protected Map namespaceURIMap;
   protected Bridge defaultBridge;
   protected Set reservedNamespaceSet;
   protected Map elementDataMap;
   protected InterpreterPool interpreterPool;
   protected DocumentLoader documentLoader;
   protected Dimension2D documentSize;
   protected TextPainter textPainter;
   public static final int STATIC = 0;
   public static final int INTERACTIVE = 1;
   public static final int DYNAMIC = 2;
   protected int dynamicStatus;
   protected UpdateManager updateManager;
   protected XBLManager xblManager;
   protected BridgeContext primaryContext;
   protected HashSet childContexts;
   protected SVGAnimationEngine animationEngine;
   protected int animationLimitingMode;
   protected float animationLimitingAmount;
   private static InterpreterPool sharedPool = new InterpreterPool();
   protected Set eventListenerSet;
   protected EventListener domCharacterDataModifiedEventListener;
   protected EventListener domAttrModifiedEventListener;
   protected EventListener domNodeInsertedEventListener;
   protected EventListener domNodeRemovedEventListener;
   protected CSSEngineListener cssPropertiesChangedListener;
   protected AnimatedAttributeListener animatedAttributeListener;
   protected FocusManager focusManager;
   protected CursorManager cursorManager;
   protected List extensions;
   protected static List globalExtensions = null;
   // $FF: synthetic field
   static Class class$org$apache$batik$bridge$BridgeExtension;

   protected BridgeContext() {
      this.interpreterMap = new HashMap(7);
      this.viewportMap = new WeakHashMap();
      this.viewportStack = new LinkedList();
      this.dynamicStatus = 0;
      this.childContexts = new HashSet();
      this.eventListenerSet = new HashSet();
      this.cursorManager = new CursorManager(this);
      this.extensions = null;
   }

   public BridgeContext(UserAgent var1) {
      this(var1, sharedPool, new DocumentLoader(var1));
   }

   public BridgeContext(UserAgent var1, DocumentLoader var2) {
      this(var1, sharedPool, var2);
   }

   public BridgeContext(UserAgent var1, InterpreterPool var2, DocumentLoader var3) {
      this.interpreterMap = new HashMap(7);
      this.viewportMap = new WeakHashMap();
      this.viewportStack = new LinkedList();
      this.dynamicStatus = 0;
      this.childContexts = new HashSet();
      this.eventListenerSet = new HashSet();
      this.cursorManager = new CursorManager(this);
      this.extensions = null;
      this.userAgent = var1;
      this.viewportMap.put(var1, new UserAgentViewport(var1));
      this.interpreterPool = var2;
      this.documentLoader = var3;
   }

   protected void finalize() {
      if (this.primaryContext != null) {
         this.dispose();
      }

   }

   public BridgeContext createSubBridgeContext(SVGOMDocument var1) {
      CSSEngine var3 = var1.getCSSEngine();
      BridgeContext var2;
      if (var3 != null) {
         var2 = (BridgeContext)var1.getCSSEngine().getCSSContext();
         return var2;
      } else {
         var2 = this.createBridgeContext(var1);
         var2.primaryContext = this.primaryContext != null ? this.primaryContext : this;
         var2.primaryContext.childContexts.add(new WeakReference(var2));
         var2.dynamicStatus = this.dynamicStatus;
         var2.setGVTBuilder(this.getGVTBuilder());
         var2.setTextPainter(this.getTextPainter());
         var2.setDocument(var1);
         var2.initializeDocument(var1);
         if (this.isInteractive()) {
            var2.addUIEventListeners(var1);
         }

         return var2;
      }
   }

   public BridgeContext createBridgeContext(SVGOMDocument var1) {
      return (BridgeContext)(var1.isSVG12() ? new SVG12BridgeContext(this.getUserAgent(), this.getDocumentLoader()) : new BridgeContext(this.getUserAgent(), this.getDocumentLoader()));
   }

   protected void initializeDocument(Document var1) {
      SVGOMDocument var2 = (SVGOMDocument)var1;
      CSSEngine var3 = var2.getCSSEngine();
      if (var3 == null) {
         SVGDOMImplementation var4 = (SVGDOMImplementation)var2.getImplementation();
         var3 = var4.createCSSEngine(var2, this);
         var3.setCSSEngineUserAgent(new CSSEngineUserAgentWrapper(this.userAgent));
         var2.setCSSEngine(var3);
         var3.setMedia(this.userAgent.getMedia());
         String var5 = this.userAgent.getUserStyleSheetURI();
         if (var5 != null) {
            try {
               ParsedURL var6 = new ParsedURL(var5);
               var3.setUserAgentStyleSheet(var3.parseStyleSheet(var6, "all"));
            } catch (Exception var7) {
               this.userAgent.displayError(var7);
            }
         }

         var3.setAlternateStyleSheet(this.userAgent.getAlternateStyleSheet());
      }

   }

   public CSSEngine getCSSEngineForElement(Element var1) {
      SVGOMDocument var2 = (SVGOMDocument)var1.getOwnerDocument();
      return var2.getCSSEngine();
   }

   public void setTextPainter(TextPainter var1) {
      this.textPainter = var1;
   }

   public TextPainter getTextPainter() {
      return this.textPainter;
   }

   public Document getDocument() {
      return this.document;
   }

   protected void setDocument(Document var1) {
      if (this.document != var1) {
         this.fontFamilyMap = null;
      }

      this.document = var1;
      this.isSVG12 = ((SVGOMDocument)var1).isSVG12();
      this.registerSVGBridges();
   }

   public Map getFontFamilyMap() {
      if (this.fontFamilyMap == null) {
         this.fontFamilyMap = new HashMap();
      }

      return this.fontFamilyMap;
   }

   protected void setFontFamilyMap(Map var1) {
      this.fontFamilyMap = var1;
   }

   public void setElementData(Node var1, Object var2) {
      if (this.elementDataMap == null) {
         this.elementDataMap = new WeakHashMap();
      }

      this.elementDataMap.put(var1, new SoftReference(var2));
   }

   public Object getElementData(Node var1) {
      if (this.elementDataMap == null) {
         return null;
      } else {
         Object var2 = this.elementDataMap.get(var1);
         if (var2 == null) {
            return null;
         } else {
            SoftReference var3 = (SoftReference)var2;
            var2 = var3.get();
            if (var2 == null) {
               this.elementDataMap.remove(var1);
            }

            return var2;
         }
      }
   }

   public UserAgent getUserAgent() {
      return this.userAgent;
   }

   protected void setUserAgent(UserAgent var1) {
      this.userAgent = var1;
   }

   public GVTBuilder getGVTBuilder() {
      return this.gvtBuilder;
   }

   protected void setGVTBuilder(GVTBuilder var1) {
      this.gvtBuilder = var1;
   }

   public InterpreterPool getInterpreterPool() {
      return this.interpreterPool;
   }

   public FocusManager getFocusManager() {
      return this.focusManager;
   }

   public CursorManager getCursorManager() {
      return this.cursorManager;
   }

   protected void setInterpreterPool(InterpreterPool var1) {
      this.interpreterPool = var1;
   }

   public Interpreter getInterpreter(String var1) {
      if (this.document == null) {
         throw new RuntimeException("Unknown document");
      } else {
         Interpreter var2 = (Interpreter)this.interpreterMap.get(var1);
         if (var2 == null) {
            try {
               var2 = this.interpreterPool.createInterpreter(this.document, var1);
               this.interpreterMap.put(var1, var2);
            } catch (Exception var4) {
               if (this.userAgent != null) {
                  this.userAgent.displayError(var4);
                  return null;
               }
            }
         }

         if (var2 == null && this.userAgent != null) {
            this.userAgent.displayError(new Exception("Unknown language: " + var1));
         }

         return var2;
      }
   }

   public DocumentLoader getDocumentLoader() {
      return this.documentLoader;
   }

   protected void setDocumentLoader(DocumentLoader var1) {
      this.documentLoader = var1;
   }

   public Dimension2D getDocumentSize() {
      return this.documentSize;
   }

   protected void setDocumentSize(Dimension2D var1) {
      this.documentSize = var1;
   }

   public boolean isDynamic() {
      return this.dynamicStatus == 2;
   }

   public boolean isInteractive() {
      return this.dynamicStatus != 0;
   }

   public void setDynamicState(int var1) {
      this.dynamicStatus = var1;
   }

   public void setDynamic(boolean var1) {
      if (var1) {
         this.setDynamicState(2);
      } else {
         this.setDynamicState(0);
      }

   }

   public void setInteractive(boolean var1) {
      if (var1) {
         this.setDynamicState(1);
      } else {
         this.setDynamicState(0);
      }

   }

   public UpdateManager getUpdateManager() {
      return this.updateManager;
   }

   protected void setUpdateManager(UpdateManager var1) {
      this.updateManager = var1;
   }

   protected void setUpdateManager(BridgeContext var1, UpdateManager var2) {
      var1.setUpdateManager(var2);
   }

   protected void setXBLManager(BridgeContext var1, XBLManager var2) {
      var1.xblManager = var2;
   }

   public boolean isSVG12() {
      return this.isSVG12;
   }

   public BridgeContext getPrimaryBridgeContext() {
      return this.primaryContext != null ? this.primaryContext : this;
   }

   public BridgeContext[] getChildContexts() {
      BridgeContext[] var1 = new BridgeContext[this.childContexts.size()];
      Iterator var2 = this.childContexts.iterator();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         WeakReference var4 = (WeakReference)var2.next();
         var1[var3] = (BridgeContext)var4.get();
      }

      return var1;
   }

   public SVGAnimationEngine getAnimationEngine() {
      if (this.animationEngine == null) {
         this.animationEngine = new SVGAnimationEngine(this.document, this);
         this.setAnimationLimitingMode();
      }

      return this.animationEngine;
   }

   public URIResolver createURIResolver(SVGDocument var1, DocumentLoader var2) {
      return new URIResolver(var1, var2);
   }

   public Node getReferencedNode(Element var1, String var2) {
      try {
         SVGDocument var3 = (SVGDocument)var1.getOwnerDocument();
         URIResolver var4 = this.createURIResolver(var3, this.documentLoader);
         Node var5 = var4.getNode(var2, var1);
         if (var5 == null) {
            throw new BridgeException(this, var1, "uri.badTarget", new Object[]{var2});
         } else {
            SVGOMDocument var6 = (SVGOMDocument)(var5.getNodeType() == 9 ? var5 : var5.getOwnerDocument());
            if (var6 != var3) {
               this.createSubBridgeContext(var6);
            }

            return var5;
         }
      } catch (MalformedURLException var7) {
         throw new BridgeException(this, var1, var7, "uri.malformed", new Object[]{var2});
      } catch (InterruptedIOException var8) {
         throw new InterruptedBridgeException();
      } catch (IOException var9) {
         throw new BridgeException(this, var1, var9, "uri.io", new Object[]{var2});
      } catch (SecurityException var10) {
         throw new BridgeException(this, var1, var10, "uri.unsecure", new Object[]{var2});
      }
   }

   public Element getReferencedElement(Element var1, String var2) {
      Node var3 = this.getReferencedNode(var1, var2);
      if (var3 != null && var3.getNodeType() != 1) {
         throw new BridgeException(this, var1, "uri.referenceDocument", new Object[]{var2});
      } else {
         return (Element)var3;
      }
   }

   public Viewport getViewport(Element var1) {
      if (this.viewportStack != null) {
         return this.viewportStack.size() == 0 ? (Viewport)this.viewportMap.get(this.userAgent) : (Viewport)this.viewportStack.get(0);
      } else {
         for(var1 = SVGUtilities.getParentElement(var1); var1 != null; var1 = SVGUtilities.getParentElement(var1)) {
            Viewport var2 = (Viewport)this.viewportMap.get(var1);
            if (var2 != null) {
               return var2;
            }
         }

         return (Viewport)this.viewportMap.get(this.userAgent);
      }
   }

   public void openViewport(Element var1, Viewport var2) {
      this.viewportMap.put(var1, var2);
      if (this.viewportStack == null) {
         this.viewportStack = new LinkedList();
      }

      this.viewportStack.add(0, var2);
   }

   public void removeViewport(Element var1) {
      this.viewportMap.remove(var1);
   }

   public void closeViewport(Element var1) {
      this.viewportStack.remove(0);
      if (this.viewportStack.size() == 0) {
         this.viewportStack = null;
      }

   }

   public void bind(Node var1, GraphicsNode var2) {
      if (this.elementNodeMap == null) {
         this.elementNodeMap = new WeakHashMap();
         this.nodeElementMap = new WeakHashMap();
      }

      this.elementNodeMap.put(var1, new SoftReference(var2));
      this.nodeElementMap.put(var2, new SoftReference(var1));
   }

   public void unbind(Node var1) {
      if (this.elementNodeMap != null) {
         GraphicsNode var2 = null;
         SoftReference var3 = (SoftReference)this.elementNodeMap.get(var1);
         if (var3 != null) {
            var2 = (GraphicsNode)var3.get();
         }

         this.elementNodeMap.remove(var1);
         if (var2 != null) {
            this.nodeElementMap.remove(var2);
         }

      }
   }

   public GraphicsNode getGraphicsNode(Node var1) {
      if (this.elementNodeMap != null) {
         SoftReference var2 = (SoftReference)this.elementNodeMap.get(var1);
         if (var2 != null) {
            return (GraphicsNode)var2.get();
         }
      }

      return null;
   }

   public Element getElement(GraphicsNode var1) {
      if (this.nodeElementMap != null) {
         SoftReference var2 = (SoftReference)this.nodeElementMap.get(var1);
         if (var2 != null) {
            Node var3 = (Node)var2.get();
            if (var3.getNodeType() == 1) {
               return (Element)var3;
            }
         }
      }

      return null;
   }

   public boolean hasGraphicsNodeBridge(Element var1) {
      if (this.namespaceURIMap != null && var1 != null) {
         String var2 = var1.getLocalName();
         String var3 = var1.getNamespaceURI();
         var3 = var3 == null ? "" : var3;
         HashMap var4 = (HashMap)this.namespaceURIMap.get(var3);
         return var4 == null ? false : var4.get(var2) instanceof GraphicsNodeBridge;
      } else {
         return false;
      }
   }

   public DocumentBridge getDocumentBridge() {
      return new SVGDocumentBridge();
   }

   public Bridge getBridge(Element var1) {
      if (this.namespaceURIMap != null && var1 != null) {
         String var2 = var1.getLocalName();
         String var3 = var1.getNamespaceURI();
         var3 = var3 == null ? "" : var3;
         return this.getBridge(var3, var2);
      } else {
         return null;
      }
   }

   public Bridge getBridge(String var1, String var2) {
      Bridge var3 = null;
      if (this.namespaceURIMap != null) {
         HashMap var4 = (HashMap)this.namespaceURIMap.get(var1);
         if (var4 != null) {
            var3 = (Bridge)var4.get(var2);
         }
      }

      if (var3 == null && (this.reservedNamespaceSet == null || !this.reservedNamespaceSet.contains(var1))) {
         var3 = this.defaultBridge;
      }

      if (this.isDynamic()) {
         return var3 == null ? null : var3.getInstance();
      } else {
         return var3;
      }
   }

   public void putBridge(String var1, String var2, Bridge var3) {
      if (var1.equals(var3.getNamespaceURI()) && var2.equals(var3.getLocalName())) {
         if (this.namespaceURIMap == null) {
            this.namespaceURIMap = new HashMap();
         }

         var1 = var1 == null ? "" : var1;
         HashMap var4 = (HashMap)this.namespaceURIMap.get(var1);
         if (var4 == null) {
            var4 = new HashMap();
            this.namespaceURIMap.put(var1, var4);
         }

         var4.put(var2, var3);
      } else {
         throw new Error("Invalid Bridge: " + var1 + "/" + var3.getNamespaceURI() + " " + var2 + "/" + var3.getLocalName() + " " + var3.getClass());
      }
   }

   public void putBridge(Bridge var1) {
      this.putBridge(var1.getNamespaceURI(), var1.getLocalName(), var1);
   }

   public void removeBridge(String var1, String var2) {
      if (this.namespaceURIMap != null) {
         var1 = var1 == null ? "" : var1;
         HashMap var3 = (HashMap)this.namespaceURIMap.get(var1);
         if (var3 != null) {
            var3.remove(var2);
            if (var3.isEmpty()) {
               this.namespaceURIMap.remove(var1);
               if (this.namespaceURIMap.isEmpty()) {
                  this.namespaceURIMap = null;
               }
            }
         }

      }
   }

   public void setDefaultBridge(Bridge var1) {
      this.defaultBridge = var1;
   }

   public void putReservedNamespaceURI(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      if (this.reservedNamespaceSet == null) {
         this.reservedNamespaceSet = new HashSet();
      }

      this.reservedNamespaceSet.add(var1);
   }

   public void removeReservedNamespaceURI(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      if (this.reservedNamespaceSet != null) {
         this.reservedNamespaceSet.remove(var1);
         if (this.reservedNamespaceSet.isEmpty()) {
            this.reservedNamespaceSet = null;
         }
      }

   }

   public void addUIEventListeners(Document var1) {
      NodeEventTarget var2 = (NodeEventTarget)var1.getDocumentElement();
      DOMMouseOverEventListener var3 = new DOMMouseOverEventListener();
      var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseover", var3, true, (Object)null);
      this.storeEventListenerNS(var2, "http://www.w3.org/2001/xml-events", "mouseover", var3, true);
      DOMMouseOutEventListener var4 = new DOMMouseOutEventListener();
      var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseout", var4, true, (Object)null);
      this.storeEventListenerNS(var2, "http://www.w3.org/2001/xml-events", "mouseout", var4, true);
   }

   public void removeUIEventListeners(Document var1) {
      EventTarget var2 = (EventTarget)var1.getDocumentElement();
      synchronized(this.eventListenerSet) {
         Iterator var4 = this.eventListenerSet.iterator();

         while(var4.hasNext()) {
            EventListenerMememto var5 = (EventListenerMememto)var4.next();
            NodeEventTarget var6 = var5.getTarget();
            if (var6 == var2) {
               EventListener var7 = var5.getListener();
               boolean var8 = var5.getUseCapture();
               String var9 = var5.getEventType();
               boolean var10 = var5.getNamespaced();
               if (var6 != null && var7 != null && var9 != null) {
                  if (var10) {
                     String var11 = var5.getNamespaceURI();
                     var6.removeEventListenerNS(var11, var9, var7, var8);
                  } else {
                     var6.removeEventListener(var9, var7, var8);
                  }
               }
            }
         }

      }
   }

   public void addDOMListeners() {
      SVGOMDocument var1 = (SVGOMDocument)this.document;
      this.domAttrModifiedEventListener = new DOMAttrModifiedEventListener();
      var1.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.domAttrModifiedEventListener, true, (Object)null);
      this.domNodeInsertedEventListener = new DOMNodeInsertedEventListener();
      var1.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", this.domNodeInsertedEventListener, true, (Object)null);
      this.domNodeRemovedEventListener = new DOMNodeRemovedEventListener();
      var1.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.domNodeRemovedEventListener, true, (Object)null);
      this.domCharacterDataModifiedEventListener = new DOMCharacterDataModifiedEventListener();
      var1.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMCharacterDataModified", this.domCharacterDataModifiedEventListener, true, (Object)null);
      this.animatedAttributeListener = new AnimatedAttrListener();
      var1.addAnimatedAttributeListener(this.animatedAttributeListener);
      this.focusManager = new FocusManager(this.document);
      CSSEngine var2 = var1.getCSSEngine();
      this.cssPropertiesChangedListener = new CSSPropertiesChangedListener();
      var2.addCSSEngineListener(this.cssPropertiesChangedListener);
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

   protected void storeEventListener(EventTarget var1, String var2, EventListener var3, boolean var4) {
      synchronized(this.eventListenerSet) {
         this.eventListenerSet.add(new EventListenerMememto(var1, var2, var3, var4, this));
      }
   }

   protected void storeEventListenerNS(EventTarget var1, String var2, String var3, EventListener var4, boolean var5) {
      synchronized(this.eventListenerSet) {
         this.eventListenerSet.add(new EventListenerMememto(var1, var2, var3, var4, var5, this));
      }
   }

   public void addGVTListener(Document var1) {
      BridgeEventSupport.addGVTListener(this, var1);
   }

   protected void clearChildContexts() {
      this.childContexts.clear();
   }

   public void dispose() {
      this.clearChildContexts();
      synchronized(this.eventListenerSet) {
         Iterator var2 = this.eventListenerSet.iterator();

         while(true) {
            if (!var2.hasNext()) {
               break;
            }

            EventListenerMememto var3 = (EventListenerMememto)var2.next();
            NodeEventTarget var4 = var3.getTarget();
            EventListener var5 = var3.getListener();
            boolean var6 = var3.getUseCapture();
            String var7 = var3.getEventType();
            boolean var8 = var3.getNamespaced();
            if (var4 != null && var5 != null && var7 != null) {
               if (var8) {
                  String var9 = var3.getNamespaceURI();
                  var4.removeEventListenerNS(var9, var7, var5, var6);
               } else {
                  var4.removeEventListener(var7, var5, var6);
               }
            }
         }
      }

      if (this.document != null) {
         this.removeDOMListeners();
      }

      if (this.animationEngine != null) {
         this.animationEngine.dispose();
         this.animationEngine = null;
      }

      Iterator var1 = this.interpreterMap.values().iterator();

      while(var1.hasNext()) {
         Interpreter var12 = (Interpreter)var1.next();
         if (var12 != null) {
            var12.dispose();
         }
      }

      this.interpreterMap.clear();
      if (this.focusManager != null) {
         this.focusManager.dispose();
      }

      if (this.elementDataMap != null) {
         this.elementDataMap.clear();
      }

      if (this.nodeElementMap != null) {
         this.nodeElementMap.clear();
      }

      if (this.elementNodeMap != null) {
         this.elementNodeMap.clear();
      }

   }

   protected static SVGContext getSVGContext(Node var0) {
      if (var0 instanceof SVGOMElement) {
         return ((SVGOMElement)var0).getSVGContext();
      } else {
         return var0 instanceof SVGOMDocument ? ((SVGOMDocument)var0).getSVGContext() : null;
      }
   }

   protected static BridgeUpdateHandler getBridgeUpdateHandler(Node var0) {
      SVGContext var1 = getSVGContext(var0);
      return var1 == null ? null : (BridgeUpdateHandler)var1;
   }

   public Value getSystemColor(String var1) {
      return SystemColorSupport.getSystemColor(var1);
   }

   public Value getDefaultFontFamily() {
      SVGOMDocument var1 = (SVGOMDocument)this.document;
      SVGStylableElement var2 = (SVGStylableElement)var1.getRootElement();
      String var3 = this.userAgent.getDefaultFontFamily();
      return var1.getCSSEngine().parsePropertyValue(var2, "font-family", var3);
   }

   public float getLighterFontWeight(float var1) {
      return this.userAgent.getLighterFontWeight(var1);
   }

   public float getBolderFontWeight(float var1) {
      return this.userAgent.getBolderFontWeight(var1);
   }

   public float getPixelUnitToMillimeter() {
      return this.userAgent.getPixelUnitToMillimeter();
   }

   public float getPixelToMillimeter() {
      return this.getPixelUnitToMillimeter();
   }

   public float getMediumFontSize() {
      return this.userAgent.getMediumFontSize();
   }

   public float getBlockWidth(Element var1) {
      return this.getViewport(var1).getWidth();
   }

   public float getBlockHeight(Element var1) {
      return this.getViewport(var1).getHeight();
   }

   public void checkLoadExternalResource(ParsedURL var1, ParsedURL var2) throws SecurityException {
      this.userAgent.checkLoadExternalResource(var1, var2);
   }

   public boolean isDynamicDocument(Document var1) {
      return BaseScriptingEnvironment.isDynamicDocument(this, var1);
   }

   public boolean isInteractiveDocument(Document var1) {
      SVGSVGElement var2 = ((SVGDocument)var1).getRootElement();
      return !"http://www.w3.org/2000/svg".equals(var2.getNamespaceURI()) ? false : this.checkInteractiveElement(var2);
   }

   public boolean checkInteractiveElement(Element var1) {
      return this.checkInteractiveElement((SVGDocument)var1.getOwnerDocument(), var1);
   }

   public boolean checkInteractiveElement(SVGDocument var1, Element var2) {
      String var3 = var2.getLocalName();
      if ("a".equals(var3)) {
         return true;
      } else if ("title".equals(var3)) {
         return var2.getParentNode() != var1.getRootElement();
      } else if ("desc".equals(var3)) {
         return var2.getParentNode() != var1.getRootElement();
      } else if ("cursor".equals(var3)) {
         return true;
      } else if (var2.getAttribute("cursor").length() > 0) {
         return true;
      } else {
         for(Node var5 = var2.getFirstChild(); var5 != null; var5 = var5.getNextSibling()) {
            if (var5.getNodeType() == 1) {
               Element var6 = (Element)var5;
               if ("http://www.w3.org/2000/svg".equals(var6.getNamespaceURI()) && this.checkInteractiveElement(var6)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public void setAnimationLimitingNone() {
      this.animationLimitingMode = 0;
      if (this.animationEngine != null) {
         this.setAnimationLimitingMode();
      }

   }

   public void setAnimationLimitingCPU(float var1) {
      this.animationLimitingMode = 1;
      this.animationLimitingAmount = var1;
      if (this.animationEngine != null) {
         this.setAnimationLimitingMode();
      }

   }

   public void setAnimationLimitingFPS(float var1) {
      this.animationLimitingMode = 2;
      this.animationLimitingAmount = var1;
      if (this.animationEngine != null) {
         this.setAnimationLimitingMode();
      }

   }

   protected void setAnimationLimitingMode() {
      switch (this.animationLimitingMode) {
         case 0:
            this.animationEngine.setAnimationLimitingNone();
            break;
         case 1:
            this.animationEngine.setAnimationLimitingCPU(this.animationLimitingAmount);
            break;
         case 2:
            this.animationEngine.setAnimationLimitingFPS(this.animationLimitingAmount);
      }

   }

   public void registerSVGBridges() {
      UserAgent var1 = this.getUserAgent();
      List var2 = this.getBridgeExtensions(this.document);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         BridgeExtension var4 = (BridgeExtension)var3.next();
         var4.registerTags(this);
         var1.registerExtension(var4);
      }

   }

   public List getBridgeExtensions(Document var1) {
      SVGSVGElement var2 = ((SVGOMDocument)var1).getRootElement();
      String var3 = var2.getAttributeNS((String)null, "version");
      Object var4;
      if (var3.length() != 0 && !var3.equals("1.0") && !var3.equals("1.1")) {
         var4 = new SVG12BridgeExtension();
      } else {
         var4 = new SVGBridgeExtension();
      }

      float var5 = ((BridgeExtension)var4).getPriority();
      this.extensions = new LinkedList(getGlobalBridgeExtensions());
      ListIterator var6 = this.extensions.listIterator();

      while(true) {
         if (!var6.hasNext()) {
            var6.add(var4);
            break;
         }

         BridgeExtension var7 = (BridgeExtension)var6.next();
         if (var7.getPriority() > var5) {
            var6.previous();
            var6.add(var4);
            break;
         }
      }

      return this.extensions;
   }

   public static synchronized List getGlobalBridgeExtensions() {
      if (globalExtensions != null) {
         return globalExtensions;
      } else {
         globalExtensions = new LinkedList();
         Iterator var0 = Service.providers(class$org$apache$batik$bridge$BridgeExtension == null ? (class$org$apache$batik$bridge$BridgeExtension = class$("org.apache.batik.bridge.BridgeExtension")) : class$org$apache$batik$bridge$BridgeExtension);

         while(true) {
            label29:
            while(var0.hasNext()) {
               BridgeExtension var1 = (BridgeExtension)var0.next();
               float var2 = var1.getPriority();
               ListIterator var3 = globalExtensions.listIterator();

               BridgeExtension var4;
               do {
                  if (!var3.hasNext()) {
                     var3.add(var1);
                     continue label29;
                  }

                  var4 = (BridgeExtension)var3.next();
               } while(!(var4.getPriority() > var2));

               var3.previous();
               var3.add(var1);
            }

            return globalExtensions;
         }
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

   public static class CSSEngineUserAgentWrapper implements CSSEngineUserAgent {
      UserAgent ua;

      CSSEngineUserAgentWrapper(UserAgent var1) {
         this.ua = var1;
      }

      public void displayError(Exception var1) {
         this.ua.displayError(var1);
      }

      public void displayMessage(String var1) {
         this.ua.displayMessage(var1);
      }
   }

   protected class AnimatedAttrListener implements AnimatedAttributeListener {
      public AnimatedAttrListener() {
      }

      public void animatedAttributeChanged(Element var1, AnimatedLiveAttributeValue var2) {
         BridgeUpdateHandler var3 = BridgeContext.getBridgeUpdateHandler(var1);
         if (var3 != null) {
            try {
               var3.handleAnimatedAttributeChanged(var2);
            } catch (Exception var5) {
               BridgeContext.this.userAgent.displayError(var5);
            }
         }

      }

      public void otherAnimationChanged(Element var1, String var2) {
         BridgeUpdateHandler var3 = BridgeContext.getBridgeUpdateHandler(var1);
         if (var3 != null) {
            try {
               var3.handleOtherAnimationChanged(var2);
            } catch (Exception var5) {
               BridgeContext.this.userAgent.displayError(var5);
            }
         }

      }
   }

   protected class CSSPropertiesChangedListener implements CSSEngineListener {
      public CSSPropertiesChangedListener() {
      }

      public void propertiesChanged(CSSEngineEvent var1) {
         Element var2 = var1.getElement();
         SVGContext var3 = BridgeContext.getSVGContext(var2);
         if (var3 == null) {
            GraphicsNode var4 = BridgeContext.this.getGraphicsNode(var2.getParentNode());
            if (var4 == null || !(var4 instanceof CompositeGraphicsNode)) {
               return;
            }

            CompositeGraphicsNode var5 = (CompositeGraphicsNode)var4;
            int[] var6 = var1.getProperties();

            for(int var7 = 0; var7 < var6.length; ++var7) {
               if (var6[var7] == 12) {
                  if (CSSUtilities.convertDisplay(var2)) {
                     GVTBuilder var8 = BridgeContext.this.getGVTBuilder();
                     GraphicsNode var9 = var8.build(BridgeContext.this, (Element)var2);
                     if (var9 != null) {
                        int var10 = -1;

                        for(Node var11 = var2.getPreviousSibling(); var11 != null; var11 = var11.getPreviousSibling()) {
                           if (var11.getNodeType() == 1) {
                              Element var12 = (Element)var11;
                              GraphicsNode var13 = BridgeContext.this.getGraphicsNode(var12);
                              if (var13 != null) {
                                 var10 = var5.indexOf(var13);
                                 if (var10 != -1) {
                                    break;
                                 }
                              }
                           }
                        }

                        ++var10;
                        var5.add(var10, var9);
                     }
                  }
                  break;
               }
            }
         }

         if (var3 != null && var3 instanceof BridgeUpdateHandler) {
            ((BridgeUpdateHandler)var3).handleCSSEngineEvent(var1);
         }

      }
   }

   protected class DOMCharacterDataModifiedEventListener implements EventListener {
      public DOMCharacterDataModifiedEventListener() {
      }

      public void handleEvent(Event var1) {
         Node var2;
         for(var2 = (Node)var1.getTarget(); var2 != null && !(var2 instanceof SVGOMElement); var2 = (Node)((AbstractNode)var2).getParentNodeEventTarget()) {
         }

         BridgeUpdateHandler var3 = BridgeContext.getBridgeUpdateHandler(var2);
         if (var3 != null) {
            try {
               var3.handleDOMCharacterDataModified((MutationEvent)var1);
            } catch (Exception var5) {
               BridgeContext.this.userAgent.displayError(var5);
            }
         }

      }
   }

   protected class DOMNodeRemovedEventListener implements EventListener {
      public DOMNodeRemovedEventListener() {
      }

      public void handleEvent(Event var1) {
         Node var2 = (Node)var1.getTarget();
         BridgeUpdateHandler var3 = BridgeContext.getBridgeUpdateHandler(var2);
         if (var3 != null) {
            try {
               var3.handleDOMNodeRemovedEvent((MutationEvent)var1);
            } catch (Exception var5) {
               BridgeContext.this.userAgent.displayError(var5);
            }
         }

      }
   }

   protected class DOMNodeInsertedEventListener implements EventListener {
      public DOMNodeInsertedEventListener() {
      }

      public void handleEvent(Event var1) {
         MutationEvent var2 = (MutationEvent)var1;
         BridgeUpdateHandler var3 = BridgeContext.getBridgeUpdateHandler(var2.getRelatedNode());
         if (var3 != null) {
            try {
               var3.handleDOMNodeInsertedEvent(var2);
            } catch (InterruptedBridgeException var5) {
            } catch (Exception var6) {
               BridgeContext.this.userAgent.displayError(var6);
            }
         }

      }
   }

   protected class DOMMouseOverEventListener implements EventListener {
      public DOMMouseOverEventListener() {
      }

      public void handleEvent(Event var1) {
         Element var2 = (Element)var1.getTarget();
         Cursor var3 = CSSUtilities.convertCursor(var2, BridgeContext.this);
         if (var3 != null) {
            BridgeContext.this.userAgent.setSVGCursor(var3);
         }

      }
   }

   protected class DOMMouseOutEventListener implements EventListener {
      public DOMMouseOutEventListener() {
      }

      public void handleEvent(Event var1) {
         MouseEvent var2 = (MouseEvent)var1;
         Element var3 = (Element)var2.getRelatedTarget();
         Cursor var4 = CursorManager.DEFAULT_CURSOR;
         if (var3 != null) {
            var4 = CSSUtilities.convertCursor(var3, BridgeContext.this);
         }

         if (var4 == null) {
            var4 = CursorManager.DEFAULT_CURSOR;
         }

         BridgeContext.this.userAgent.setSVGCursor(var4);
      }
   }

   protected class DOMAttrModifiedEventListener implements EventListener {
      public DOMAttrModifiedEventListener() {
      }

      public void handleEvent(Event var1) {
         Node var2 = (Node)var1.getTarget();
         BridgeUpdateHandler var3 = BridgeContext.getBridgeUpdateHandler(var2);
         if (var3 != null) {
            try {
               var3.handleDOMAttrModifiedEvent((MutationEvent)var1);
            } catch (Exception var5) {
               BridgeContext.this.userAgent.displayError(var5);
            }
         }

      }
   }

   protected static class EventListenerMememto {
      public SoftReference target;
      public SoftReference listener;
      public boolean useCapture;
      public String namespaceURI;
      public String eventType;
      public boolean namespaced;

      public EventListenerMememto(EventTarget var1, String var2, EventListener var3, boolean var4, BridgeContext var5) {
         Set var6 = var5.eventListenerSet;
         this.target = new SoftReferenceMememto(var1, this, var6);
         this.listener = new SoftReferenceMememto(var3, this, var6);
         this.eventType = var2;
         this.useCapture = var4;
      }

      public EventListenerMememto(EventTarget var1, String var2, String var3, EventListener var4, boolean var5, BridgeContext var6) {
         this(var1, var3, var4, var5, var6);
         this.namespaceURI = var2;
         this.namespaced = true;
      }

      public EventListener getListener() {
         return (EventListener)this.listener.get();
      }

      public NodeEventTarget getTarget() {
         return (NodeEventTarget)this.target.get();
      }

      public boolean getUseCapture() {
         return this.useCapture;
      }

      public String getNamespaceURI() {
         return this.namespaceURI;
      }

      public String getEventType() {
         return this.eventType;
      }

      public boolean getNamespaced() {
         return this.namespaced;
      }
   }

   public static class SoftReferenceMememto extends CleanerThread.SoftReferenceCleared {
      Object mememto;
      Set set;

      SoftReferenceMememto(Object var1, Object var2, Set var3) {
         super(var1);
         this.mememto = var2;
         this.set = var3;
      }

      public void cleared() {
         synchronized(this.set) {
            this.set.remove(this.mememto);
            this.mememto = null;
            this.set = null;
         }
      }
   }
}

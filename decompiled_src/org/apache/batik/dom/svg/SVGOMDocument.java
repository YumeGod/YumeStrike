package org.apache.batik.dom.svg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.MissingResourceException;
import org.apache.batik.css.engine.CSSNavigableDocument;
import org.apache.batik.css.engine.CSSNavigableDocumentListener;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.dom.AbstractStylableDocument;
import org.apache.batik.dom.GenericAttr;
import org.apache.batik.dom.GenericAttrNS;
import org.apache.batik.dom.GenericCDATASection;
import org.apache.batik.dom.GenericComment;
import org.apache.batik.dom.GenericDocumentFragment;
import org.apache.batik.dom.GenericElement;
import org.apache.batik.dom.GenericEntityReference;
import org.apache.batik.dom.GenericProcessingInstruction;
import org.apache.batik.dom.GenericText;
import org.apache.batik.dom.StyleSheetFactory;
import org.apache.batik.dom.events.EventSupport;
import org.apache.batik.dom.util.XMLSupport;
import org.apache.batik.i18n.LocalizableSupport;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGLangSpace;
import org.w3c.dom.svg.SVGSVGElement;

public class SVGOMDocument extends AbstractStylableDocument implements SVGDocument, SVGConstants, CSSNavigableDocument, IdContainer {
   protected static final String RESOURCES = "org.apache.batik.dom.svg.resources.Messages";
   protected transient LocalizableSupport localizableSupport = new LocalizableSupport("org.apache.batik.dom.svg.resources.Messages", this.getClass().getClassLoader());
   protected String referrer = "";
   protected ParsedURL url;
   protected transient boolean readonly;
   protected boolean isSVG12;
   protected HashMap cssNavigableDocumentListeners = new HashMap();
   protected AnimatedAttributeListener mainAnimatedAttributeListener = new AnimAttrListener();
   protected LinkedList animatedAttributeListeners = new LinkedList();
   protected transient SVGContext svgContext;

   protected SVGOMDocument() {
   }

   public SVGOMDocument(DocumentType var1, DOMImplementation var2) {
      super(var1, var2);
   }

   public void setLocale(Locale var1) {
      super.setLocale(var1);
      this.localizableSupport.setLocale(var1);
   }

   public String formatMessage(String var1, Object[] var2) throws MissingResourceException {
      try {
         return super.formatMessage(var1, var2);
      } catch (MissingResourceException var4) {
         return this.localizableSupport.formatMessage(var1, var2);
      }
   }

   public String getTitle() {
      StringBuffer var1 = new StringBuffer();
      boolean var2 = false;

      label35:
      for(Node var3 = this.getDocumentElement().getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
         String var4 = var3.getNamespaceURI();
         if (var4 != null && var4.equals("http://www.w3.org/2000/svg") && var3.getLocalName().equals("title")) {
            var2 = ((SVGLangSpace)var3).getXMLspace().equals("preserve");
            var3 = var3.getFirstChild();

            while(true) {
               if (var3 == null) {
                  break label35;
               }

               if (var3.getNodeType() == 3) {
                  var1.append(var3.getNodeValue());
               }

               var3 = var3.getNextSibling();
            }
         }
      }

      String var5 = var1.toString();
      return var2 ? XMLSupport.preserveXMLSpace(var5) : XMLSupport.defaultXMLSpace(var5);
   }

   public String getReferrer() {
      return this.referrer;
   }

   public void setReferrer(String var1) {
      this.referrer = var1;
   }

   public String getDomain() {
      return this.url == null ? null : this.url.getHost();
   }

   public SVGSVGElement getRootElement() {
      return (SVGSVGElement)this.getDocumentElement();
   }

   public String getURL() {
      return this.documentURI;
   }

   public URL getURLObject() {
      try {
         return new URL(this.documentURI);
      } catch (MalformedURLException var2) {
         return null;
      }
   }

   public ParsedURL getParsedURL() {
      return this.url;
   }

   public void setURLObject(URL var1) {
      this.setParsedURL(new ParsedURL(var1));
   }

   public void setParsedURL(ParsedURL var1) {
      this.url = var1;
      this.documentURI = var1 == null ? null : var1.toString();
   }

   public void setDocumentURI(String var1) {
      this.documentURI = var1;
      this.url = var1 == null ? null : new ParsedURL(var1);
   }

   public Element createElement(String var1) throws DOMException {
      return new GenericElement(var1.intern(), this);
   }

   public DocumentFragment createDocumentFragment() {
      return new GenericDocumentFragment(this);
   }

   public Text createTextNode(String var1) {
      return new GenericText(var1, this);
   }

   public Comment createComment(String var1) {
      return new GenericComment(var1, this);
   }

   public CDATASection createCDATASection(String var1) throws DOMException {
      return new GenericCDATASection(var1, this);
   }

   public ProcessingInstruction createProcessingInstruction(String var1, String var2) throws DOMException {
      return (ProcessingInstruction)("xml-stylesheet".equals(var1) ? new SVGStyleSheetProcessingInstruction(var2, this, (StyleSheetFactory)this.getImplementation()) : new GenericProcessingInstruction(var1, var2, this));
   }

   public Attr createAttribute(String var1) throws DOMException {
      return new GenericAttr(var1.intern(), this);
   }

   public EntityReference createEntityReference(String var1) throws DOMException {
      return new GenericEntityReference(var1, this);
   }

   public Attr createAttributeNS(String var1, String var2) throws DOMException {
      return (Attr)(var1 == null ? new GenericAttr(var2.intern(), this) : new GenericAttrNS(var1.intern(), var2.intern(), this));
   }

   public Element createElementNS(String var1, String var2) throws DOMException {
      SVGDOMImplementation var3 = (SVGDOMImplementation)this.implementation;
      return var3.createElementNS(this, var1, var2);
   }

   public boolean isSVG12() {
      return this.isSVG12;
   }

   public void setIsSVG12(boolean var1) {
      this.isSVG12 = var1;
   }

   public boolean isId(Attr var1) {
      return var1.getNamespaceURI() == null ? "id".equals(var1.getNodeName()) : var1.getNodeName().equals("xml:id");
   }

   public void setSVGContext(SVGContext var1) {
      this.svgContext = var1;
   }

   public SVGContext getSVGContext() {
      return this.svgContext;
   }

   public void addCSSNavigableDocumentListener(CSSNavigableDocumentListener var1) {
      if (!this.cssNavigableDocumentListeners.containsKey(var1)) {
         DOMNodeInsertedListenerWrapper var2 = new DOMNodeInsertedListenerWrapper(var1);
         DOMNodeRemovedListenerWrapper var3 = new DOMNodeRemovedListenerWrapper(var1);
         DOMSubtreeModifiedListenerWrapper var4 = new DOMSubtreeModifiedListenerWrapper(var1);
         DOMCharacterDataModifiedListenerWrapper var5 = new DOMCharacterDataModifiedListenerWrapper(var1);
         DOMAttrModifiedListenerWrapper var6 = new DOMAttrModifiedListenerWrapper(var1);
         this.cssNavigableDocumentListeners.put(var1, new EventListener[]{var2, var3, var4, var5, var6});
         this.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", var2, false, (Object)null);
         this.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", var3, false, (Object)null);
         this.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMSubtreeModified", var4, false, (Object)null);
         this.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMCharacterDataModified", var5, false, (Object)null);
         this.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", var6, false, (Object)null);
      }
   }

   public void removeCSSNavigableDocumentListener(CSSNavigableDocumentListener var1) {
      EventListener[] var2 = (EventListener[])this.cssNavigableDocumentListeners.get(var1);
      if (var2 != null) {
         this.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", var2[0], false);
         this.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", var2[1], false);
         this.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMSubtreeModified", var2[2], false);
         this.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMCharacterDataModified", var2[3], false);
         this.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", var2[4], false);
         this.cssNavigableDocumentListeners.remove(var1);
      }
   }

   protected AnimatedAttributeListener getAnimatedAttributeListener() {
      return this.mainAnimatedAttributeListener;
   }

   protected void overrideStyleTextChanged(CSSStylableElement var1, String var2) {
      Iterator var3 = this.cssNavigableDocumentListeners.keySet().iterator();

      while(var3.hasNext()) {
         CSSNavigableDocumentListener var4 = (CSSNavigableDocumentListener)var3.next();
         var4.overrideStyleTextChanged(var1, var2);
      }

   }

   protected void overrideStylePropertyRemoved(CSSStylableElement var1, String var2) {
      Iterator var3 = this.cssNavigableDocumentListeners.keySet().iterator();

      while(var3.hasNext()) {
         CSSNavigableDocumentListener var4 = (CSSNavigableDocumentListener)var3.next();
         var4.overrideStylePropertyRemoved(var1, var2);
      }

   }

   protected void overrideStylePropertyChanged(CSSStylableElement var1, String var2, String var3, String var4) {
      Iterator var5 = this.cssNavigableDocumentListeners.keySet().iterator();

      while(var5.hasNext()) {
         CSSNavigableDocumentListener var6 = (CSSNavigableDocumentListener)var5.next();
         var6.overrideStylePropertyChanged(var1, var2, var3, var4);
      }

   }

   public void addAnimatedAttributeListener(AnimatedAttributeListener var1) {
      if (!this.animatedAttributeListeners.contains(var1)) {
         this.animatedAttributeListeners.add(var1);
      }
   }

   public void removeAnimatedAttributeListener(AnimatedAttributeListener var1) {
      this.animatedAttributeListeners.remove(var1);
   }

   public CSSStyleDeclaration getOverrideStyle(Element var1, String var2) {
      return var1 instanceof SVGStylableElement && var2 == null ? ((SVGStylableElement)var1).getOverrideStyle() : null;
   }

   public boolean isReadonly() {
      return this.readonly;
   }

   public void setReadonly(boolean var1) {
      this.readonly = var1;
   }

   protected Node newNode() {
      return new SVGOMDocument();
   }

   protected Node copyInto(Node var1) {
      super.copyInto(var1);
      SVGOMDocument var2 = (SVGOMDocument)var1;
      var2.localizableSupport = new LocalizableSupport("org.apache.batik.dom.svg.resources.Messages", this.getClass().getClassLoader());
      var2.referrer = this.referrer;
      var2.url = this.url;
      return var1;
   }

   protected Node deepCopyInto(Node var1) {
      super.deepCopyInto(var1);
      SVGOMDocument var2 = (SVGOMDocument)var1;
      var2.localizableSupport = new LocalizableSupport("org.apache.batik.dom.svg.resources.Messages", this.getClass().getClassLoader());
      var2.referrer = this.referrer;
      var2.url = this.url;
      return var1;
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.localizableSupport = new LocalizableSupport("org.apache.batik.dom.svg.resources.Messages", this.getClass().getClassLoader());
   }

   protected class AnimAttrListener implements AnimatedAttributeListener {
      public void animatedAttributeChanged(Element var1, AnimatedLiveAttributeValue var2) {
         Iterator var3 = SVGOMDocument.this.animatedAttributeListeners.iterator();

         while(var3.hasNext()) {
            AnimatedAttributeListener var4 = (AnimatedAttributeListener)var3.next();
            var4.animatedAttributeChanged(var1, var2);
         }

      }

      public void otherAnimationChanged(Element var1, String var2) {
         Iterator var3 = SVGOMDocument.this.animatedAttributeListeners.iterator();

         while(var3.hasNext()) {
            AnimatedAttributeListener var4 = (AnimatedAttributeListener)var3.next();
            var4.otherAnimationChanged(var1, var2);
         }

      }
   }

   protected class DOMAttrModifiedListenerWrapper implements EventListener {
      protected CSSNavigableDocumentListener listener;

      public DOMAttrModifiedListenerWrapper(CSSNavigableDocumentListener var2) {
         this.listener = var2;
      }

      public void handleEvent(Event var1) {
         var1 = EventSupport.getUltimateOriginalEvent(var1);
         MutationEvent var2 = (MutationEvent)var1;
         this.listener.attrModified((Element)var1.getTarget(), (Attr)var2.getRelatedNode(), var2.getAttrChange(), var2.getPrevValue(), var2.getNewValue());
      }
   }

   protected class DOMCharacterDataModifiedListenerWrapper implements EventListener {
      protected CSSNavigableDocumentListener listener;

      public DOMCharacterDataModifiedListenerWrapper(CSSNavigableDocumentListener var2) {
         this.listener = var2;
      }

      public void handleEvent(Event var1) {
         var1 = EventSupport.getUltimateOriginalEvent(var1);
         this.listener.subtreeModified((Node)var1.getTarget());
      }
   }

   protected class DOMSubtreeModifiedListenerWrapper implements EventListener {
      protected CSSNavigableDocumentListener listener;

      public DOMSubtreeModifiedListenerWrapper(CSSNavigableDocumentListener var2) {
         this.listener = var2;
      }

      public void handleEvent(Event var1) {
         var1 = EventSupport.getUltimateOriginalEvent(var1);
         this.listener.subtreeModified((Node)var1.getTarget());
      }
   }

   protected class DOMNodeRemovedListenerWrapper implements EventListener {
      protected CSSNavigableDocumentListener listener;

      public DOMNodeRemovedListenerWrapper(CSSNavigableDocumentListener var2) {
         this.listener = var2;
      }

      public void handleEvent(Event var1) {
         var1 = EventSupport.getUltimateOriginalEvent(var1);
         this.listener.nodeToBeRemoved((Node)var1.getTarget());
      }
   }

   protected class DOMNodeInsertedListenerWrapper implements EventListener {
      protected CSSNavigableDocumentListener listener;

      public DOMNodeInsertedListenerWrapper(CSSNavigableDocumentListener var2) {
         this.listener = var2;
      }

      public void handleEvent(Event var1) {
         var1 = EventSupport.getUltimateOriginalEvent(var1);
         this.listener.nodeInserted((Node)var1.getTarget());
      }
   }
}

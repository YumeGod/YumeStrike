package org.apache.batik.bridge.svg12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.event.EventListenerList;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.BridgeException;
import org.apache.batik.dom.AbstractAttrNS;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.dom.svg12.BindableElement;
import org.apache.batik.dom.svg12.XBLEventSupport;
import org.apache.batik.dom.svg12.XBLOMContentElement;
import org.apache.batik.dom.svg12.XBLOMDefinitionElement;
import org.apache.batik.dom.svg12.XBLOMImportElement;
import org.apache.batik.dom.svg12.XBLOMShadowTreeElement;
import org.apache.batik.dom.svg12.XBLOMTemplateElement;
import org.apache.batik.dom.xbl.NodeXBL;
import org.apache.batik.dom.xbl.ShadowTreeEvent;
import org.apache.batik.dom.xbl.XBLManager;
import org.apache.batik.dom.xbl.XBLManagerData;
import org.apache.batik.dom.xbl.XBLShadowTreeElement;
import org.apache.batik.util.DoublyIndexedTable;
import org.apache.batik.util.XBLConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MutationEvent;

public class DefaultXBLManager implements XBLManager, XBLConstants {
   protected boolean isProcessing;
   protected Document document;
   protected BridgeContext ctx;
   protected DoublyIndexedTable definitionLists = new DoublyIndexedTable();
   protected DoublyIndexedTable definitions = new DoublyIndexedTable();
   protected Map contentManagers = new HashMap();
   protected Map imports = new HashMap();
   protected DocInsertedListener docInsertedListener = new DocInsertedListener();
   protected DocRemovedListener docRemovedListener = new DocRemovedListener();
   protected DocSubtreeListener docSubtreeListener = new DocSubtreeListener();
   protected ImportAttrListener importAttrListener = new ImportAttrListener();
   protected RefAttrListener refAttrListener = new RefAttrListener();
   protected EventListenerList bindingListenerList = new EventListenerList();
   protected EventListenerList contentSelectionChangedListenerList = new EventListenerList();
   // $FF: synthetic field
   static Class class$org$apache$batik$bridge$svg12$ContentSelectionChangedListener;
   // $FF: synthetic field
   static Class class$org$apache$batik$bridge$svg12$BindingListener;

   public DefaultXBLManager(Document var1, BridgeContext var2) {
      this.document = var1;
      this.ctx = var2;
      ImportRecord var3 = new ImportRecord((Element)null, (Node)null);
      this.imports.put((Object)null, var3);
   }

   public void startProcessing() {
      if (!this.isProcessing) {
         NodeList var1 = this.document.getElementsByTagNameNS("http://www.w3.org/2004/xbl", "definition");
         XBLOMDefinitionElement[] var2 = new XBLOMDefinitionElement[var1.getLength()];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3] = (XBLOMDefinitionElement)var1.item(var3);
         }

         var1 = this.document.getElementsByTagNameNS("http://www.w3.org/2004/xbl", "import");
         Element[] var9 = new Element[var1.getLength()];

         for(int var4 = 0; var4 < var9.length; ++var4) {
            var9[var4] = (Element)var1.item(var4);
         }

         AbstractDocument var10 = (AbstractDocument)this.document;
         XBLEventSupport var5 = (XBLEventSupport)var10.initializeEventSupport();
         var5.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.docRemovedListener, true);
         var5.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", this.docInsertedListener, true);
         var5.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMSubtreeModified", this.docSubtreeListener, true);

         int var6;
         for(var6 = 0; var6 < var2.length; ++var6) {
            if (var2[var6].getAttributeNS((String)null, "ref").length() != 0) {
               this.addDefinitionRef(var2[var6]);
            } else {
               String var7 = var2[var6].getElementNamespaceURI();
               String var8 = var2[var6].getElementLocalName();
               this.addDefinition(var7, var8, var2[var6], (Element)null);
            }
         }

         for(var6 = 0; var6 < var9.length; ++var6) {
            this.addImport(var9[var6]);
         }

         this.isProcessing = true;
         this.bind(this.document.getDocumentElement());
      }
   }

   public void stopProcessing() {
      if (this.isProcessing) {
         this.isProcessing = false;
         AbstractDocument var1 = (AbstractDocument)this.document;
         XBLEventSupport var2 = (XBLEventSupport)var1.initializeEventSupport();
         var2.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.docRemovedListener, true);
         var2.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", this.docInsertedListener, true);
         var2.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMSubtreeModified", this.docSubtreeListener, true);
         int var3 = this.imports.values().size();
         ImportRecord[] var4 = new ImportRecord[var3];
         this.imports.values().toArray(var4);

         for(int var5 = 0; var5 < var4.length; ++var5) {
            ImportRecord var6 = var4[var5];
            if (var6.importElement.getLocalName().equals("definition")) {
               this.removeDefinitionRef(var6.importElement);
            } else {
               this.removeImport(var6.importElement);
            }
         }

         Object[] var9 = this.definitions.getValuesArray();
         this.definitions.clear();

         for(int var10 = 0; var10 < var9.length; ++var10) {
            DefinitionRecord var7 = (DefinitionRecord)var9[var10];
            TreeSet var8 = (TreeSet)this.definitionLists.get(var7.namespaceURI, var7.localName);
            if (var8 != null) {
               while(!var8.isEmpty()) {
                  var7 = (DefinitionRecord)var8.first();
                  var8.remove(var7);
                  this.removeDefinition(var7);
               }

               this.definitionLists.put(var7.namespaceURI, var7.localName, (Object)null);
            }
         }

         this.definitionLists = new DoublyIndexedTable();
         this.contentManagers.clear();
      }
   }

   public boolean isProcessing() {
      return this.isProcessing;
   }

   protected void addDefinitionRef(Element var1) {
      String var2 = var1.getAttributeNS((String)null, "ref");
      Element var3 = this.ctx.getReferencedElement(var1, var2);
      if ("http://www.w3.org/2004/xbl".equals(var3.getNamespaceURI()) && "definition".equals(var3.getLocalName())) {
         ImportRecord var4 = new ImportRecord(var1, var3);
         this.imports.put(var1, var4);
         NodeEventTarget var5 = (NodeEventTarget)var1;
         var5.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.refAttrListener, false, (Object)null);
         XBLOMDefinitionElement var6 = (XBLOMDefinitionElement)var1;
         String var7 = var6.getElementNamespaceURI();
         String var8 = var6.getElementLocalName();
         this.addDefinition(var7, var8, (XBLOMDefinitionElement)var3, var1);
      } else {
         throw new BridgeException(this.ctx, var1, "uri.badTarget", new Object[]{var2});
      }
   }

   protected void removeDefinitionRef(Element var1) {
      ImportRecord var2 = (ImportRecord)this.imports.get(var1);
      NodeEventTarget var3 = (NodeEventTarget)var1;
      var3.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.refAttrListener, false);
      DefinitionRecord var4 = (DefinitionRecord)this.definitions.get(var2.node, var1);
      this.removeDefinition(var4);
      this.imports.remove(var1);
   }

   protected void addImport(Element var1) {
      String var2 = var1.getAttributeNS((String)null, "bindings");
      Node var3 = this.ctx.getReferencedNode(var1, var2);
      if (var3.getNodeType() != 1 || "http://www.w3.org/2004/xbl".equals(var3.getNamespaceURI()) && "xbl".equals(var3.getLocalName())) {
         ImportRecord var4 = new ImportRecord(var1, var3);
         this.imports.put(var1, var4);
         NodeEventTarget var5 = (NodeEventTarget)var1;
         var5.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.importAttrListener, false, (Object)null);
         var5 = (NodeEventTarget)var3;
         var5.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", var4.importInsertedListener, false, (Object)null);
         var5.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", var4.importRemovedListener, false, (Object)null);
         var5.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMSubtreeModified", var4.importSubtreeListener, false, (Object)null);
         this.addImportedDefinitions(var1, var3);
      } else {
         throw new BridgeException(this.ctx, var1, "uri.badTarget", new Object[]{var3});
      }
   }

   protected void addImportedDefinitions(Element var1, Node var2) {
      if (var2 instanceof XBLOMDefinitionElement) {
         XBLOMDefinitionElement var3 = (XBLOMDefinitionElement)var2;
         String var4 = var3.getElementNamespaceURI();
         String var5 = var3.getElementLocalName();
         this.addDefinition(var4, var5, var3, var1);
      } else {
         for(var2 = var2.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
            this.addImportedDefinitions(var1, var2);
         }
      }

   }

   protected void removeImport(Element var1) {
      ImportRecord var2 = (ImportRecord)this.imports.get(var1);
      NodeEventTarget var3 = (NodeEventTarget)var2.node;
      var3.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", var2.importInsertedListener, false);
      var3.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", var2.importRemovedListener, false);
      var3.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMSubtreeModified", var2.importSubtreeListener, false);
      var3 = (NodeEventTarget)var1;
      var3.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.importAttrListener, false);
      Object[] var4 = this.definitions.getValuesArray();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         DefinitionRecord var6 = (DefinitionRecord)var4[var5];
         if (var6.importElement == var1) {
            this.removeDefinition(var6);
         }
      }

      this.imports.remove(var1);
   }

   protected void addDefinition(String var1, String var2, XBLOMDefinitionElement var3, Element var4) {
      ImportRecord var5 = (ImportRecord)this.imports.get(var4);
      DefinitionRecord var6 = null;
      TreeSet var8 = (TreeSet)this.definitionLists.get(var1, var2);
      if (var8 == null) {
         var8 = new TreeSet();
         this.definitionLists.put(var1, var2, var8);
      } else if (var8.size() > 0) {
         var6 = (DefinitionRecord)var8.first();
      }

      XBLOMTemplateElement var9 = null;

      for(Node var10 = var3.getFirstChild(); var10 != null; var10 = var10.getNextSibling()) {
         if (var10 instanceof XBLOMTemplateElement) {
            var9 = (XBLOMTemplateElement)var10;
            break;
         }
      }

      DefinitionRecord var7 = new DefinitionRecord(var1, var2, var3, var9, var4);
      var8.add(var7);
      this.definitions.put(var3, var4, var7);
      this.addDefinitionElementListeners(var3, var5);
      if (var8.first() == var7) {
         if (var6 != null) {
            XBLOMDefinitionElement var12 = var6.definition;
            XBLOMTemplateElement var11 = var6.template;
            if (var11 != null) {
               this.removeTemplateElementListeners(var11, var5);
            }

            this.removeDefinitionElementListeners(var12, var5);
         }

         if (var9 != null) {
            this.addTemplateElementListeners(var9, var5);
         }

         if (this.isProcessing) {
            this.rebind(var1, var2, this.document.getDocumentElement());
         }

      }
   }

   protected void addDefinitionElementListeners(XBLOMDefinitionElement var1, ImportRecord var2) {
      XBLEventSupport var3 = (XBLEventSupport)var1.initializeEventSupport();
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", var2.defAttrListener, false);
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", var2.defNodeInsertedListener, false);
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", var2.defNodeRemovedListener, false);
   }

   protected void addTemplateElementListeners(XBLOMTemplateElement var1, ImportRecord var2) {
      XBLEventSupport var3 = (XBLEventSupport)var1.initializeEventSupport();
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", var2.templateMutationListener, false);
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", var2.templateMutationListener, false);
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", var2.templateMutationListener, false);
      var3.addImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMCharacterDataModified", var2.templateMutationListener, false);
   }

   protected void removeDefinition(DefinitionRecord var1) {
      TreeSet var2 = (TreeSet)this.definitionLists.get(var1.namespaceURI, var1.localName);
      if (var2 != null) {
         Element var3 = var1.importElement;
         ImportRecord var4 = (ImportRecord)this.imports.get(var3);
         DefinitionRecord var5 = (DefinitionRecord)var2.first();
         var2.remove(var1);
         this.definitions.remove(var1.definition, var3);
         this.removeDefinitionElementListeners(var1.definition, var4);
         if (var1 == var5) {
            if (var1.template != null) {
               this.removeTemplateElementListeners(var1.template, var4);
            }

            this.rebind(var1.namespaceURI, var1.localName, this.document.getDocumentElement());
         }
      }
   }

   protected void removeDefinitionElementListeners(XBLOMDefinitionElement var1, ImportRecord var2) {
      XBLEventSupport var3 = (XBLEventSupport)var1.initializeEventSupport();
      var3.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", var2.defAttrListener, false);
      var3.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", var2.defNodeInsertedListener, false);
      var3.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", var2.defNodeRemovedListener, false);
   }

   protected void removeTemplateElementListeners(XBLOMTemplateElement var1, ImportRecord var2) {
      XBLEventSupport var3 = (XBLEventSupport)var1.initializeEventSupport();
      var3.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", var2.templateMutationListener, false);
      var3.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", var2.templateMutationListener, false);
      var3.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", var2.templateMutationListener, false);
      var3.removeImplementationEventListenerNS("http://www.w3.org/2001/xml-events", "DOMCharacterDataModified", var2.templateMutationListener, false);
   }

   protected DefinitionRecord getActiveDefinition(String var1, String var2) {
      TreeSet var3 = (TreeSet)this.definitionLists.get(var1, var2);
      return var3 != null && var3.size() != 0 ? (DefinitionRecord)var3.first() : null;
   }

   protected void unbind(Element var1) {
      if (var1 instanceof BindableElement) {
         this.setActiveDefinition((BindableElement)var1, (DefinitionRecord)null);
      } else {
         NodeList var2 = this.getXblScopedChildNodes(var1);

         for(int var3 = 0; var3 < var2.getLength(); ++var3) {
            Node var4 = var2.item(var3);
            if (var4.getNodeType() == 1) {
               this.unbind((Element)var4);
            }
         }
      }

   }

   protected void bind(Element var1) {
      AbstractDocument var2 = (AbstractDocument)var1.getOwnerDocument();
      if (var2 != this.document) {
         XBLManager var3 = var2.getXBLManager();
         if (var3 instanceof DefaultXBLManager) {
            ((DefaultXBLManager)var3).bind(var1);
            return;
         }
      }

      if (var1 instanceof BindableElement) {
         DefinitionRecord var6 = this.getActiveDefinition(var1.getNamespaceURI(), var1.getLocalName());
         this.setActiveDefinition((BindableElement)var1, var6);
      } else {
         NodeList var7 = this.getXblScopedChildNodes(var1);

         for(int var4 = 0; var4 < var7.getLength(); ++var4) {
            Node var5 = var7.item(var4);
            if (var5.getNodeType() == 1) {
               this.bind((Element)var5);
            }
         }
      }

   }

   protected void rebind(String var1, String var2, Element var3) {
      AbstractDocument var4 = (AbstractDocument)var3.getOwnerDocument();
      if (var4 != this.document) {
         XBLManager var5 = var4.getXBLManager();
         if (var5 instanceof DefaultXBLManager) {
            ((DefaultXBLManager)var5).rebind(var1, var2, var3);
            return;
         }
      }

      if (var3 instanceof BindableElement && var1.equals(var3.getNamespaceURI()) && var2.equals(var3.getLocalName())) {
         DefinitionRecord var9 = this.getActiveDefinition(var3.getNamespaceURI(), var3.getLocalName());
         this.setActiveDefinition((BindableElement)var3, var9);
      } else {
         NodeList var8 = this.getXblScopedChildNodes(var3);

         for(int var6 = 0; var6 < var8.getLength(); ++var6) {
            Node var7 = var8.item(var6);
            if (var7.getNodeType() == 1) {
               this.rebind(var1, var2, (Element)var7);
            }
         }
      }

   }

   protected void setActiveDefinition(BindableElement var1, DefinitionRecord var2) {
      XBLRecord var3 = this.getRecord(var1);
      var3.definitionElement = var2 == null ? null : var2.definition;
      if (var2 != null && var2.definition != null && var2.template != null) {
         this.setXblShadowTree(var1, this.cloneTemplate(var2.template));
      } else {
         this.setXblShadowTree(var1, (XBLOMShadowTreeElement)null);
      }

   }

   protected void setXblShadowTree(BindableElement var1, XBLOMShadowTreeElement var2) {
      XBLOMShadowTreeElement var3 = (XBLOMShadowTreeElement)this.getXblShadowTree(var1);
      if (var3 != null) {
         this.fireShadowTreeEvent(var1, "unbinding", var3);
         ContentManager var4 = this.getContentManager(var3);
         if (var4 != null) {
            var4.dispose();
         }

         var1.setShadowTree((XBLOMShadowTreeElement)null);
         XBLRecord var5 = this.getRecord(var3);
         var5.boundElement = null;
         var3.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMSubtreeModified", this.docSubtreeListener, false);
      }

      if (var2 != null) {
         var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMSubtreeModified", this.docSubtreeListener, false, (Object)null);
         this.fireShadowTreeEvent(var1, "prebind", var2);
         var1.setShadowTree(var2);
         XBLRecord var8 = this.getRecord(var2);
         var8.boundElement = var1;
         AbstractDocument var10 = (AbstractDocument)var1.getOwnerDocument();
         XBLManager var6 = var10.getXBLManager();
         ContentManager var7 = new ContentManager(var2, var6);
         this.setContentManager(var2, var7);
      }

      this.invalidateChildNodes(var1);
      if (var2 != null) {
         NodeList var9 = this.getXblScopedChildNodes(var1);

         for(int var11 = 0; var11 < var9.getLength(); ++var11) {
            Node var12 = var9.item(var11);
            if (var12.getNodeType() == 1) {
               this.bind((Element)var12);
            }
         }

         this.dispatchBindingChangedEvent(var1, var2);
         this.fireShadowTreeEvent(var1, "bound", var2);
      } else {
         this.dispatchBindingChangedEvent(var1, var2);
      }

   }

   protected void fireShadowTreeEvent(BindableElement var1, String var2, XBLShadowTreeElement var3) {
      DocumentEvent var4 = (DocumentEvent)var1.getOwnerDocument();
      ShadowTreeEvent var5 = (ShadowTreeEvent)var4.createEvent("ShadowTreeEvent");
      var5.initShadowTreeEventNS("http://www.w3.org/2004/xbl", var2, true, false, var3);
      var1.dispatchEvent(var5);
   }

   protected XBLOMShadowTreeElement cloneTemplate(XBLOMTemplateElement var1) {
      XBLOMShadowTreeElement var2 = (XBLOMShadowTreeElement)var1.getOwnerDocument().createElementNS("http://www.w3.org/2004/xbl", "shadowTree");
      NamedNodeMap var3 = var1.getAttributes();

      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         Attr var5 = (Attr)var3.item(var4);
         if (var5 instanceof AbstractAttrNS) {
            var2.setAttributeNodeNS(var5);
         } else {
            var2.setAttributeNode(var5);
         }
      }

      for(Node var6 = var1.getFirstChild(); var6 != null; var6 = var6.getNextSibling()) {
         var2.appendChild(var6.cloneNode(true));
      }

      return var2;
   }

   public Node getXblParentNode(Node var1) {
      XBLOMContentElement var2 = this.getXblContentElement(var1);
      Object var3 = var2 == null ? var1.getParentNode() : var2.getParentNode();
      if (var3 instanceof XBLOMContentElement) {
         var3 = ((Node)var3).getParentNode();
      }

      if (var3 instanceof XBLOMShadowTreeElement) {
         var3 = this.getXblBoundElement((Node)var3);
      }

      return (Node)var3;
   }

   public NodeList getXblChildNodes(Node var1) {
      XBLRecord var2 = this.getRecord(var1);
      if (var2.childNodes == null) {
         var2.childNodes = new XblChildNodes(var2);
      }

      return var2.childNodes;
   }

   public NodeList getXblScopedChildNodes(Node var1) {
      XBLRecord var2 = this.getRecord(var1);
      if (var2.scopedChildNodes == null) {
         var2.scopedChildNodes = new XblScopedChildNodes(var2);
      }

      return var2.scopedChildNodes;
   }

   public Node getXblFirstChild(Node var1) {
      NodeList var2 = this.getXblChildNodes(var1);
      return var2.item(0);
   }

   public Node getXblLastChild(Node var1) {
      NodeList var2 = this.getXblChildNodes(var1);
      return var2.item(var2.getLength() - 1);
   }

   public Node getXblPreviousSibling(Node var1) {
      Node var2 = this.getXblParentNode(var1);
      if (var2 != null && this.getRecord(var2).childNodes != null) {
         XBLRecord var3 = this.getRecord(var1);
         if (!var3.linksValid) {
            this.updateLinks(var1);
         }

         return var3.previousSibling;
      } else {
         return var1.getPreviousSibling();
      }
   }

   public Node getXblNextSibling(Node var1) {
      Node var2 = this.getXblParentNode(var1);
      if (var2 != null && this.getRecord(var2).childNodes != null) {
         XBLRecord var3 = this.getRecord(var1);
         if (!var3.linksValid) {
            this.updateLinks(var1);
         }

         return var3.nextSibling;
      } else {
         return var1.getNextSibling();
      }
   }

   public Element getXblFirstElementChild(Node var1) {
      for(var1 = this.getXblFirstChild(var1); var1 != null && var1.getNodeType() != 1; var1 = this.getXblNextSibling(var1)) {
      }

      return (Element)var1;
   }

   public Element getXblLastElementChild(Node var1) {
      for(var1 = this.getXblLastChild(var1); var1 != null && var1.getNodeType() != 1; var1 = this.getXblPreviousSibling(var1)) {
      }

      return (Element)var1;
   }

   public Element getXblPreviousElementSibling(Node var1) {
      do {
         var1 = this.getXblPreviousSibling(var1);
      } while(var1 != null && var1.getNodeType() != 1);

      return (Element)var1;
   }

   public Element getXblNextElementSibling(Node var1) {
      do {
         var1 = this.getXblNextSibling(var1);
      } while(var1 != null && var1.getNodeType() != 1);

      return (Element)var1;
   }

   public Element getXblBoundElement(Node var1) {
      for(; var1 != null && !(var1 instanceof XBLShadowTreeElement); var1 = ((Node)var1).getParentNode()) {
         XBLOMContentElement var2 = this.getXblContentElement((Node)var1);
         if (var2 != null) {
            var1 = var2;
         }
      }

      if (var1 == null) {
         return null;
      } else {
         return this.getRecord((Node)var1).boundElement;
      }
   }

   public Element getXblShadowTree(Node var1) {
      if (var1 instanceof BindableElement) {
         BindableElement var2 = (BindableElement)var1;
         return var2.getShadowTree();
      } else {
         return null;
      }
   }

   public NodeList getXblDefinitions(Node var1) {
      final String var2 = var1.getNamespaceURI();
      final String var3 = var1.getLocalName();
      return new NodeList() {
         public Node item(int var1) {
            TreeSet var2x = (TreeSet)DefaultXBLManager.this.definitionLists.get(var2, var3);
            if (var2x != null && var2x.size() != 0 && var1 == 0) {
               DefinitionRecord var3x = (DefinitionRecord)var2x.first();
               return var3x.definition;
            } else {
               return null;
            }
         }

         public int getLength() {
            TreeSet var1 = (TreeSet)DefaultXBLManager.this.definitionLists.get(var2, var3);
            return var1 != null && var1.size() != 0 ? 1 : 0;
         }
      };
   }

   protected XBLRecord getRecord(Node var1) {
      XBLManagerData var2 = (XBLManagerData)var1;
      XBLRecord var3 = (XBLRecord)var2.getManagerData();
      if (var3 == null) {
         var3 = new XBLRecord();
         var3.node = var1;
         var2.setManagerData(var3);
      }

      return var3;
   }

   protected void updateLinks(Node var1) {
      XBLRecord var2 = this.getRecord(var1);
      var2.previousSibling = null;
      var2.nextSibling = null;
      var2.linksValid = true;
      Node var3 = this.getXblParentNode(var1);
      if (var3 != null) {
         NodeList var4 = this.getXblChildNodes(var3);
         if (var4 instanceof XblChildNodes) {
            ((XblChildNodes)var4).update();
         }
      }

   }

   public XBLOMContentElement getXblContentElement(Node var1) {
      return this.getRecord(var1).contentElement;
   }

   public static int computeBubbleLimit(Node var0, Node var1) {
      ArrayList var2 = new ArrayList(10);

      ArrayList var3;
      for(var3 = new ArrayList(10); var0 != null; var0 = ((NodeXBL)var0).getXblParentNode()) {
         var2.add(var0);
      }

      while(var1 != null) {
         var3.add(var1);
         var1 = ((NodeXBL)var1).getXblParentNode();
      }

      int var4 = var2.size();
      int var5 = var3.size();

      for(int var6 = 0; var6 < var4 && var6 < var5; ++var6) {
         Node var7 = (Node)var2.get(var4 - var6 - 1);
         Node var8 = (Node)var3.get(var5 - var6 - 1);
         if (var7 != var8) {
            for(Element var9 = ((NodeXBL)var7).getXblBoundElement(); var6 > 0 && var9 != var2.get(var4 - var6 - 1); --var6) {
            }

            return var4 - var6 - 1;
         }
      }

      return 1;
   }

   public ContentManager getContentManager(Node var1) {
      Element var2 = this.getXblBoundElement(var1);
      if (var2 != null) {
         Element var3 = this.getXblShadowTree(var2);
         if (var3 != null) {
            Document var5 = var2.getOwnerDocument();
            ContentManager var4;
            if (var5 != this.document) {
               DefaultXBLManager var6 = (DefaultXBLManager)((AbstractDocument)var5).getXBLManager();
               var4 = (ContentManager)var6.contentManagers.get(var3);
            } else {
               var4 = (ContentManager)this.contentManagers.get(var3);
            }

            return var4;
         }
      }

      return null;
   }

   void setContentManager(Element var1, ContentManager var2) {
      if (var2 == null) {
         this.contentManagers.remove(var1);
      } else {
         this.contentManagers.put(var1, var2);
      }

   }

   public void invalidateChildNodes(Node var1) {
      XBLRecord var2 = this.getRecord(var1);
      if (var2.childNodes != null) {
         var2.childNodes.invalidate();
      }

      if (var2.scopedChildNodes != null) {
         var2.scopedChildNodes.invalidate();
      }

   }

   public void addContentSelectionChangedListener(ContentSelectionChangedListener var1) {
      this.contentSelectionChangedListenerList.add(class$org$apache$batik$bridge$svg12$ContentSelectionChangedListener == null ? (class$org$apache$batik$bridge$svg12$ContentSelectionChangedListener = class$("org.apache.batik.bridge.svg12.ContentSelectionChangedListener")) : class$org$apache$batik$bridge$svg12$ContentSelectionChangedListener, var1);
   }

   public void removeContentSelectionChangedListener(ContentSelectionChangedListener var1) {
      this.contentSelectionChangedListenerList.remove(class$org$apache$batik$bridge$svg12$ContentSelectionChangedListener == null ? (class$org$apache$batik$bridge$svg12$ContentSelectionChangedListener = class$("org.apache.batik.bridge.svg12.ContentSelectionChangedListener")) : class$org$apache$batik$bridge$svg12$ContentSelectionChangedListener, var1);
   }

   protected Object[] getContentSelectionChangedListeners() {
      return this.contentSelectionChangedListenerList.getListenerList();
   }

   void shadowTreeSelectedContentChanged(Set var1, Set var2) {
      Iterator var3 = var1.iterator();

      Node var4;
      while(var3.hasNext()) {
         var4 = (Node)var3.next();
         if (var4.getNodeType() == 1) {
            this.unbind((Element)var4);
         }
      }

      var3 = var2.iterator();

      while(var3.hasNext()) {
         var4 = (Node)var3.next();
         if (var4.getNodeType() == 1) {
            this.bind((Element)var4);
         }
      }

   }

   public void addBindingListener(BindingListener var1) {
      this.bindingListenerList.add(class$org$apache$batik$bridge$svg12$BindingListener == null ? (class$org$apache$batik$bridge$svg12$BindingListener = class$("org.apache.batik.bridge.svg12.BindingListener")) : class$org$apache$batik$bridge$svg12$BindingListener, var1);
   }

   public void removeBindingListener(BindingListener var1) {
      this.bindingListenerList.remove(class$org$apache$batik$bridge$svg12$BindingListener == null ? (class$org$apache$batik$bridge$svg12$BindingListener = class$("org.apache.batik.bridge.svg12.BindingListener")) : class$org$apache$batik$bridge$svg12$BindingListener, var1);
   }

   protected void dispatchBindingChangedEvent(Element var1, Element var2) {
      Object[] var3 = this.bindingListenerList.getListenerList();

      for(int var4 = var3.length - 2; var4 >= 0; var4 -= 2) {
         BindingListener var5 = (BindingListener)var3[var4 + 1];
         var5.bindingChanged(var1, var2);
      }

   }

   protected boolean isActiveDefinition(XBLOMDefinitionElement var1, Element var2) {
      DefinitionRecord var3 = (DefinitionRecord)this.definitions.get(var1, var2);
      if (var3 == null) {
         return false;
      } else {
         return var3 == this.getActiveDefinition(var3.namespaceURI, var3.localName);
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

   protected class XblScopedChildNodes extends XblChildNodes {
      public XblScopedChildNodes(XBLRecord var2) {
         super(var2);
      }

      protected void update() {
         this.size = 0;
         Element var1 = DefaultXBLManager.this.getXblShadowTree(this.record.node);

         for(Node var2 = var1 == null ? this.record.node.getFirstChild() : var1.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
            this.collectXblScopedChildNodes(var2);
         }

      }

      protected void collectXblScopedChildNodes(Node var1) {
         boolean var2 = false;
         if (var1.getNodeType() == 1) {
            if (!var1.getNamespaceURI().equals("http://www.w3.org/2004/xbl")) {
               var2 = true;
            } else if (var1 instanceof XBLOMContentElement) {
               ContentManager var3 = DefaultXBLManager.this.getContentManager(var1);
               if (var3 != null) {
                  NodeList var4 = var3.getSelectedContent((XBLOMContentElement)var1);

                  for(int var5 = 0; var5 < var4.getLength(); ++var5) {
                     this.collectXblScopedChildNodes(var4.item(var5));
                  }
               }
            }
         } else {
            var2 = true;
         }

         if (var2) {
            this.nodes.add(var1);
            ++this.size;
         }

      }
   }

   protected class XblChildNodes implements NodeList {
      protected XBLRecord record;
      protected List nodes;
      protected int size;

      public XblChildNodes(XBLRecord var2) {
         this.record = var2;
         this.nodes = new ArrayList();
         this.size = -1;
      }

      protected void update() {
         this.size = 0;
         Element var1 = DefaultXBLManager.this.getXblShadowTree(this.record.node);
         Node var2 = null;

         for(Node var3 = var1 == null ? this.record.node.getFirstChild() : var1.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
            var2 = this.collectXblChildNodes(var3, var2);
         }

         if (var2 != null) {
            XBLRecord var4 = DefaultXBLManager.this.getRecord(var2);
            var4.nextSibling = null;
            var4.linksValid = true;
         }

      }

      protected Node collectXblChildNodes(Node var1, Node var2) {
         boolean var3 = false;
         if (var1.getNodeType() == 1) {
            if (!"http://www.w3.org/2004/xbl".equals(var1.getNamespaceURI())) {
               var3 = true;
            } else if (var1 instanceof XBLOMContentElement) {
               ContentManager var4 = DefaultXBLManager.this.getContentManager(var1);
               if (var4 != null) {
                  NodeList var5 = var4.getSelectedContent((XBLOMContentElement)var1);

                  for(int var6 = 0; var6 < var5.getLength(); ++var6) {
                     var2 = this.collectXblChildNodes(var5.item(var6), var2);
                  }
               }
            }
         } else {
            var3 = true;
         }

         if (var3) {
            this.nodes.add(var1);
            ++this.size;
            XBLRecord var7;
            if (var2 != null) {
               var7 = DefaultXBLManager.this.getRecord(var2);
               var7.nextSibling = var1;
               var7.linksValid = true;
            }

            var7 = DefaultXBLManager.this.getRecord(var1);
            var7.previousSibling = var2;
            var7.linksValid = true;
            var2 = var1;
         }

         return var2;
      }

      public void invalidate() {
         for(int var1 = 0; var1 < this.size; ++var1) {
            XBLRecord var2 = DefaultXBLManager.this.getRecord((Node)this.nodes.get(var1));
            var2.previousSibling = null;
            var2.nextSibling = null;
            var2.linksValid = false;
         }

         this.nodes.clear();
         this.size = -1;
      }

      public Node getFirstNode() {
         if (this.size == -1) {
            this.update();
         }

         return this.size == 0 ? null : (Node)this.nodes.get(0);
      }

      public Node getLastNode() {
         if (this.size == -1) {
            this.update();
         }

         return this.size == 0 ? null : (Node)this.nodes.get(this.nodes.size() - 1);
      }

      public Node item(int var1) {
         if (this.size == -1) {
            this.update();
         }

         return var1 >= 0 && var1 < this.size ? (Node)this.nodes.get(var1) : null;
      }

      public int getLength() {
         if (this.size == -1) {
            this.update();
         }

         return this.size;
      }
   }

   protected class XBLRecord {
      public Node node;
      public XblChildNodes childNodes;
      public XblScopedChildNodes scopedChildNodes;
      public XBLOMContentElement contentElement;
      public XBLOMDefinitionElement definitionElement;
      public BindableElement boundElement;
      public boolean linksValid;
      public Node nextSibling;
      public Node previousSibling;
   }

   protected class RefAttrListener implements EventListener {
      public void handleEvent(Event var1) {
         EventTarget var2 = var1.getTarget();
         if (var2 == var1.getCurrentTarget()) {
            MutationEvent var3 = (MutationEvent)var1;
            if (var3.getAttrName().equals("ref")) {
               Element var4 = (Element)var2;
               DefaultXBLManager.this.removeDefinitionRef(var4);
               if (var3.getNewValue().length() == 0) {
                  XBLOMDefinitionElement var5 = (XBLOMDefinitionElement)var4;
                  String var6 = var5.getElementNamespaceURI();
                  String var7 = var5.getElementLocalName();
                  DefaultXBLManager.this.addDefinition(var6, var7, (XBLOMDefinitionElement)var4, (Element)null);
               } else {
                  DefaultXBLManager.this.addDefinitionRef(var4);
               }
            }

         }
      }
   }

   protected class ImportAttrListener implements EventListener {
      public void handleEvent(Event var1) {
         EventTarget var2 = var1.getTarget();
         if (var2 == var1.getCurrentTarget()) {
            MutationEvent var3 = (MutationEvent)var1;
            if (var3.getAttrName().equals("bindings")) {
               Element var4 = (Element)var2;
               DefaultXBLManager.this.removeImport(var4);
               DefaultXBLManager.this.addImport(var4);
            }

         }
      }
   }

   protected class DefNodeRemovedListener implements EventListener {
      protected Element importElement;

      public DefNodeRemovedListener(Element var2) {
         this.importElement = var2;
      }

      public void handleEvent(Event var1) {
         MutationEvent var2 = (MutationEvent)var1;
         Node var3 = var2.getRelatedNode();
         if (var3 instanceof XBLOMDefinitionElement) {
            EventTarget var4 = var1.getTarget();
            if (var4 instanceof XBLOMTemplateElement) {
               XBLOMTemplateElement var5 = (XBLOMTemplateElement)var4;
               DefinitionRecord var6 = (DefinitionRecord)DefaultXBLManager.this.definitions.get(var3, this.importElement);
               if (var6 != null && var6.template == var5) {
                  ImportRecord var7 = (ImportRecord)DefaultXBLManager.this.imports.get(this.importElement);
                  DefaultXBLManager.this.removeTemplateElementListeners(var5, var7);
                  var6.template = null;

                  for(Node var8 = var5.getNextSibling(); var8 != null; var8 = var8.getNextSibling()) {
                     if (var8 instanceof XBLOMTemplateElement) {
                        var6.template = (XBLOMTemplateElement)var8;
                        break;
                     }
                  }

                  DefaultXBLManager.this.addTemplateElementListeners(var6.template, var7);
                  DefaultXBLManager.this.rebind(var6.namespaceURI, var6.localName, DefaultXBLManager.this.document.getDocumentElement());
               }
            }
         }
      }
   }

   protected class DefNodeInsertedListener implements EventListener {
      protected Element importElement;

      public DefNodeInsertedListener(Element var2) {
         this.importElement = var2;
      }

      public void handleEvent(Event var1) {
         MutationEvent var2 = (MutationEvent)var1;
         Node var3 = var2.getRelatedNode();
         if (var3 instanceof XBLOMDefinitionElement) {
            EventTarget var4 = var1.getTarget();
            if (var4 instanceof XBLOMTemplateElement) {
               XBLOMTemplateElement var5 = (XBLOMTemplateElement)var4;
               DefinitionRecord var6 = (DefinitionRecord)DefaultXBLManager.this.definitions.get(var3, this.importElement);
               if (var6 != null) {
                  ImportRecord var7 = (ImportRecord)DefaultXBLManager.this.imports.get(this.importElement);
                  if (var6.template != null) {
                     for(Node var8 = var3.getFirstChild(); var8 != null; var8 = var8.getNextSibling()) {
                        if (var8 == var5) {
                           DefaultXBLManager.this.removeTemplateElementListeners(var6.template, var7);
                           var6.template = var5;
                           break;
                        }

                        if (var8 == var6.template) {
                           return;
                        }
                     }
                  } else {
                     var6.template = var5;
                  }

                  DefaultXBLManager.this.addTemplateElementListeners(var5, var7);
                  DefaultXBLManager.this.rebind(var6.namespaceURI, var6.localName, DefaultXBLManager.this.document.getDocumentElement());
               }
            }
         }
      }
   }

   protected class DefAttrListener implements EventListener {
      protected Element importElement;

      public DefAttrListener(Element var2) {
         this.importElement = var2;
      }

      public void handleEvent(Event var1) {
         EventTarget var2 = var1.getTarget();
         if (var2 instanceof XBLOMDefinitionElement) {
            XBLOMDefinitionElement var3 = (XBLOMDefinitionElement)var2;
            if (DefaultXBLManager.this.isActiveDefinition(var3, this.importElement)) {
               MutationEvent var4 = (MutationEvent)var1;
               String var5 = var4.getAttrName();
               DefinitionRecord var6;
               if (var5.equals("element")) {
                  var6 = (DefinitionRecord)DefaultXBLManager.this.definitions.get(var3, this.importElement);
                  DefaultXBLManager.this.removeDefinition(var6);
                  DefaultXBLManager.this.addDefinition(var3.getElementNamespaceURI(), var3.getElementLocalName(), var3, this.importElement);
               } else if (var5.equals("ref") && var4.getNewValue().length() != 0) {
                  var6 = (DefinitionRecord)DefaultXBLManager.this.definitions.get(var3, this.importElement);
                  DefaultXBLManager.this.removeDefinition(var6);
                  DefaultXBLManager.this.addDefinitionRef(var3);
               }

            }
         }
      }
   }

   protected class TemplateMutationListener implements EventListener {
      protected Element importElement;

      public TemplateMutationListener(Element var2) {
         this.importElement = var2;
      }

      public void handleEvent(Event var1) {
         Node var2;
         for(var2 = (Node)var1.getTarget(); var2 != null && !(var2 instanceof XBLOMDefinitionElement); var2 = var2.getParentNode()) {
         }

         DefinitionRecord var3 = (DefinitionRecord)DefaultXBLManager.this.definitions.get(var2, this.importElement);
         if (var3 != null) {
            DefaultXBLManager.this.rebind(var3.namespaceURI, var3.localName, DefaultXBLManager.this.document.getDocumentElement());
         }
      }
   }

   protected class DocSubtreeListener implements EventListener {
      public void handleEvent(Event var1) {
         Object[] var2 = DefaultXBLManager.this.docRemovedListener.defsToBeRemoved.toArray();
         DefaultXBLManager.this.docRemovedListener.defsToBeRemoved.clear();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            XBLOMDefinitionElement var4 = (XBLOMDefinitionElement)var2[var3];
            if (var4.getAttributeNS((String)null, "ref").length() == 0) {
               DefinitionRecord var5 = (DefinitionRecord)DefaultXBLManager.this.definitions.get(var4, (Object)null);
               DefaultXBLManager.this.removeDefinition(var5);
            } else {
               DefaultXBLManager.this.removeDefinitionRef(var4);
            }
         }

         Object[] var6 = DefaultXBLManager.this.docRemovedListener.importsToBeRemoved.toArray();
         DefaultXBLManager.this.docRemovedListener.importsToBeRemoved.clear();

         for(int var7 = 0; var7 < var6.length; ++var7) {
            DefaultXBLManager.this.removeImport((Element)var6[var7]);
         }

         Object[] var8 = DefaultXBLManager.this.docRemovedListener.nodesToBeInvalidated.toArray();
         DefaultXBLManager.this.docRemovedListener.nodesToBeInvalidated.clear();

         for(int var9 = 0; var9 < var8.length; ++var9) {
            DefaultXBLManager.this.invalidateChildNodes((Node)var8[var9]);
         }

      }
   }

   protected class DocRemovedListener implements EventListener {
      protected LinkedList defsToBeRemoved = new LinkedList();
      protected LinkedList importsToBeRemoved = new LinkedList();
      protected LinkedList nodesToBeInvalidated = new LinkedList();

      public void handleEvent(Event var1) {
         EventTarget var2 = var1.getTarget();
         if (var2 instanceof XBLOMDefinitionElement) {
            if (DefaultXBLManager.this.getXblBoundElement((Node)var2) == null) {
               this.defsToBeRemoved.add(var2);
            }
         } else if (var2 instanceof XBLOMImportElement && DefaultXBLManager.this.getXblBoundElement((Node)var2) == null) {
            this.importsToBeRemoved.add(var2);
         }

         Node var3 = DefaultXBLManager.this.getXblParentNode((Node)var2);
         if (var3 != null) {
            this.nodesToBeInvalidated.add(var3);
         }

      }
   }

   protected class DocInsertedListener implements EventListener {
      public void handleEvent(Event var1) {
         EventTarget var2 = var1.getTarget();
         if (var2 instanceof XBLOMDefinitionElement) {
            if (DefaultXBLManager.this.getXblBoundElement((Node)var2) == null) {
               XBLOMDefinitionElement var3 = (XBLOMDefinitionElement)var2;
               if (var3.getAttributeNS((String)null, "ref").length() == 0) {
                  DefaultXBLManager.this.addDefinition(var3.getElementNamespaceURI(), var3.getElementLocalName(), var3, (Element)null);
               } else {
                  DefaultXBLManager.this.addDefinitionRef(var3);
               }
            }
         } else if (var2 instanceof XBLOMImportElement) {
            if (DefaultXBLManager.this.getXblBoundElement((Node)var2) == null) {
               DefaultXBLManager.this.addImport((Element)var2);
            }
         } else {
            var1 = XBLEventSupport.getUltimateOriginalEvent(var1);
            var2 = var1.getTarget();
            Node var5 = DefaultXBLManager.this.getXblParentNode((Node)var2);
            if (var5 != null) {
               DefaultXBLManager.this.invalidateChildNodes(var5);
            }

            if (var2 instanceof BindableElement) {
               for(Node var4 = ((Node)var2).getParentNode(); var4 != null; var4 = var4.getParentNode()) {
                  if (var4 instanceof BindableElement && DefaultXBLManager.this.getRecord(var4).definitionElement != null) {
                     return;
                  }
               }

               DefaultXBLManager.this.bind((Element)var2);
            }
         }

      }
   }

   protected class ImportSubtreeListener implements EventListener {
      protected Element importElement;
      protected ImportRemovedListener importRemovedListener;

      public ImportSubtreeListener(Element var2, ImportRemovedListener var3) {
         this.importElement = var2;
         this.importRemovedListener = var3;
      }

      public void handleEvent(Event var1) {
         Object[] var2 = this.importRemovedListener.toBeRemoved.toArray();
         this.importRemovedListener.toBeRemoved.clear();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            XBLOMDefinitionElement var4 = (XBLOMDefinitionElement)var2[var3];
            DefinitionRecord var5 = (DefinitionRecord)DefaultXBLManager.this.definitions.get(var4, this.importElement);
            DefaultXBLManager.this.removeDefinition(var5);
         }

      }
   }

   protected class ImportRemovedListener implements EventListener {
      protected LinkedList toBeRemoved = new LinkedList();

      public void handleEvent(Event var1) {
         this.toBeRemoved.add(var1.getTarget());
      }
   }

   protected class ImportInsertedListener implements EventListener {
      protected Element importElement;

      public ImportInsertedListener(Element var2) {
         this.importElement = var2;
      }

      public void handleEvent(Event var1) {
         EventTarget var2 = var1.getTarget();
         if (var2 instanceof XBLOMDefinitionElement) {
            XBLOMDefinitionElement var3 = (XBLOMDefinitionElement)var2;
            DefaultXBLManager.this.addDefinition(var3.getElementNamespaceURI(), var3.getElementLocalName(), var3, this.importElement);
         }

      }
   }

   protected class ImportRecord {
      public Element importElement;
      public Node node;
      public DefNodeInsertedListener defNodeInsertedListener;
      public DefNodeRemovedListener defNodeRemovedListener;
      public DefAttrListener defAttrListener;
      public ImportInsertedListener importInsertedListener;
      public ImportRemovedListener importRemovedListener;
      public ImportSubtreeListener importSubtreeListener;
      public TemplateMutationListener templateMutationListener;

      public ImportRecord(Element var2, Node var3) {
         this.importElement = var2;
         this.node = var3;
         this.defNodeInsertedListener = DefaultXBLManager.this.new DefNodeInsertedListener(var2);
         this.defNodeRemovedListener = DefaultXBLManager.this.new DefNodeRemovedListener(var2);
         this.defAttrListener = DefaultXBLManager.this.new DefAttrListener(var2);
         this.importInsertedListener = DefaultXBLManager.this.new ImportInsertedListener(var2);
         this.importRemovedListener = DefaultXBLManager.this.new ImportRemovedListener();
         this.importSubtreeListener = DefaultXBLManager.this.new ImportSubtreeListener(var2, this.importRemovedListener);
         this.templateMutationListener = DefaultXBLManager.this.new TemplateMutationListener(var2);
      }
   }

   protected class DefinitionRecord implements Comparable {
      public String namespaceURI;
      public String localName;
      public XBLOMDefinitionElement definition;
      public XBLOMTemplateElement template;
      public Element importElement;

      public DefinitionRecord(String var2, String var3, XBLOMDefinitionElement var4, XBLOMTemplateElement var5, Element var6) {
         this.namespaceURI = var2;
         this.localName = var3;
         this.definition = var4;
         this.template = var5;
         this.importElement = var6;
      }

      public boolean equals(Object var1) {
         return this.compareTo(var1) == 0;
      }

      public int compareTo(Object var1) {
         DefinitionRecord var2 = (DefinitionRecord)var1;
         Object var3;
         Object var4;
         if (this.importElement == null) {
            var3 = this.definition;
            if (var2.importElement == null) {
               var4 = var2.definition;
            } else {
               var4 = (AbstractNode)var2.importElement;
            }
         } else if (var2.importElement == null) {
            var3 = (AbstractNode)this.importElement;
            var4 = var2.definition;
         } else if (this.definition.getOwnerDocument() == var2.definition.getOwnerDocument()) {
            var3 = this.definition;
            var4 = var2.definition;
         } else {
            var3 = (AbstractNode)this.importElement;
            var4 = (AbstractNode)var2.importElement;
         }

         short var5 = ((AbstractNode)var3).compareDocumentPosition((Node)var4);
         if ((var5 & 2) != 0) {
            return -1;
         } else {
            return (var5 & 4) != 0 ? 1 : 0;
         }
      }
   }
}

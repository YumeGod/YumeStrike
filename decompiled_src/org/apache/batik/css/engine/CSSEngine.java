package org.apache.batik.css.engine;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.batik.css.engine.sac.CSSConditionFactory;
import org.apache.batik.css.engine.sac.CSSSelectorFactory;
import org.apache.batik.css.engine.sac.ExtendedSelector;
import org.apache.batik.css.engine.value.ComputedValue;
import org.apache.batik.css.engine.value.InheritValue;
import org.apache.batik.css.engine.value.ShorthandManager;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueManager;
import org.apache.batik.css.parser.ExtendedParser;
import org.apache.batik.util.ParsedURL;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MutationEvent;

public abstract class CSSEngine {
   protected CSSEngineUserAgent userAgent;
   protected CSSContext cssContext;
   protected Document document;
   protected ParsedURL documentURI;
   protected boolean isCSSNavigableDocument;
   protected StringIntMap indexes;
   protected StringIntMap shorthandIndexes;
   protected ValueManager[] valueManagers;
   protected ShorthandManager[] shorthandManagers;
   protected ExtendedParser parser;
   protected String[] pseudoElementNames;
   protected int fontSizeIndex = -1;
   protected int lineHeightIndex = -1;
   protected int colorIndex = -1;
   protected StyleSheet userAgentStyleSheet;
   protected StyleSheet userStyleSheet;
   protected SACMediaList media;
   protected List styleSheetNodes;
   protected List fontFaces = new LinkedList();
   protected String styleNamespaceURI;
   protected String styleLocalName;
   protected String classNamespaceURI;
   protected String classLocalName;
   protected Set nonCSSPresentationalHints;
   protected String nonCSSPresentationalHintsNamespaceURI;
   protected StyleDeclarationDocumentHandler styleDeclarationDocumentHandler = new StyleDeclarationDocumentHandler();
   protected StyleDeclarationUpdateHandler styleDeclarationUpdateHandler;
   protected StyleSheetDocumentHandler styleSheetDocumentHandler = new StyleSheetDocumentHandler();
   protected StyleDeclarationBuilder styleDeclarationBuilder = new StyleDeclarationBuilder();
   protected CSSStylableElement element;
   protected ParsedURL cssBaseURI;
   protected String alternateStyleSheet;
   protected CSSNavigableDocumentHandler cssNavigableDocumentListener;
   protected EventListener domAttrModifiedListener;
   protected EventListener domNodeInsertedListener;
   protected EventListener domNodeRemovedListener;
   protected EventListener domSubtreeModifiedListener;
   protected EventListener domCharacterDataModifiedListener;
   protected boolean styleSheetRemoved;
   protected Node removedStylableElementSibling;
   protected List listeners = Collections.synchronizedList(new LinkedList());
   protected Set selectorAttributes;
   protected final int[] ALL_PROPERTIES;
   protected CSSConditionFactory cssConditionFactory;
   protected static final CSSEngineListener[] LISTENER_ARRAY = new CSSEngineListener[0];

   public static Node getCSSParentNode(Node var0) {
      return var0 instanceof CSSNavigableNode ? ((CSSNavigableNode)var0).getCSSParentNode() : var0.getParentNode();
   }

   protected static Node getCSSFirstChild(Node var0) {
      return var0 instanceof CSSNavigableNode ? ((CSSNavigableNode)var0).getCSSFirstChild() : var0.getFirstChild();
   }

   protected static Node getCSSNextSibling(Node var0) {
      return var0 instanceof CSSNavigableNode ? ((CSSNavigableNode)var0).getCSSNextSibling() : var0.getNextSibling();
   }

   protected static Node getCSSPreviousSibling(Node var0) {
      return var0 instanceof CSSNavigableNode ? ((CSSNavigableNode)var0).getCSSPreviousSibling() : var0.getPreviousSibling();
   }

   public static CSSStylableElement getParentCSSStylableElement(Element var0) {
      for(Node var1 = getCSSParentNode(var0); var1 != null; var1 = getCSSParentNode(var1)) {
         if (var1 instanceof CSSStylableElement) {
            return (CSSStylableElement)var1;
         }
      }

      return null;
   }

   protected CSSEngine(Document var1, ParsedURL var2, ExtendedParser var3, ValueManager[] var4, ShorthandManager[] var5, String[] var6, String var7, String var8, String var9, String var10, boolean var11, String var12, CSSContext var13) {
      this.document = var1;
      this.documentURI = var2;
      this.parser = var3;
      this.pseudoElementNames = var6;
      this.styleNamespaceURI = var7;
      this.styleLocalName = var8;
      this.classNamespaceURI = var9;
      this.classLocalName = var10;
      this.cssContext = var13;
      this.isCSSNavigableDocument = var1 instanceof CSSNavigableDocument;
      this.cssConditionFactory = new CSSConditionFactory(var9, var10, (String)null, "id");
      int var14 = var4.length;
      this.indexes = new StringIntMap(var14);
      this.valueManagers = var4;

      int var15;
      String var16;
      for(var15 = var14 - 1; var15 >= 0; --var15) {
         var16 = var4[var15].getPropertyName();
         this.indexes.put(var16, var15);
         if (this.fontSizeIndex == -1 && var16.equals("font-size")) {
            this.fontSizeIndex = var15;
         }

         if (this.lineHeightIndex == -1 && var16.equals("line-height")) {
            this.lineHeightIndex = var15;
         }

         if (this.colorIndex == -1 && var16.equals("color")) {
            this.colorIndex = var15;
         }
      }

      var14 = var5.length;
      this.shorthandIndexes = new StringIntMap(var14);
      this.shorthandManagers = var5;

      for(var15 = var14 - 1; var15 >= 0; --var15) {
         this.shorthandIndexes.put(var5[var15].getPropertyName(), var15);
      }

      if (var11) {
         this.nonCSSPresentationalHints = new HashSet(var4.length + var5.length);
         this.nonCSSPresentationalHintsNamespaceURI = var12;
         var14 = var4.length;

         for(var15 = 0; var15 < var14; ++var15) {
            var16 = var4[var15].getPropertyName();
            this.nonCSSPresentationalHints.add(var16);
         }

         var14 = var5.length;

         for(var15 = 0; var15 < var14; ++var15) {
            var16 = var5[var15].getPropertyName();
            this.nonCSSPresentationalHints.add(var16);
         }
      }

      if (this.cssContext.isDynamic() && this.document instanceof EventTarget) {
         this.addEventListeners((EventTarget)this.document);
         this.styleDeclarationUpdateHandler = new StyleDeclarationUpdateHandler();
      }

      this.ALL_PROPERTIES = new int[this.getNumberOfProperties()];

      for(var15 = this.getNumberOfProperties() - 1; var15 >= 0; this.ALL_PROPERTIES[var15] = var15--) {
      }

   }

   protected void addEventListeners(EventTarget var1) {
      if (this.isCSSNavigableDocument) {
         this.cssNavigableDocumentListener = new CSSNavigableDocumentHandler();
         CSSNavigableDocument var2 = (CSSNavigableDocument)var1;
         var2.addCSSNavigableDocumentListener(this.cssNavigableDocumentListener);
      } else {
         this.domAttrModifiedListener = new DOMAttrModifiedListener();
         var1.addEventListener("DOMAttrModified", this.domAttrModifiedListener, false);
         this.domNodeInsertedListener = new DOMNodeInsertedListener();
         var1.addEventListener("DOMNodeInserted", this.domNodeInsertedListener, false);
         this.domNodeRemovedListener = new DOMNodeRemovedListener();
         var1.addEventListener("DOMNodeRemoved", this.domNodeRemovedListener, false);
         this.domSubtreeModifiedListener = new DOMSubtreeModifiedListener();
         var1.addEventListener("DOMSubtreeModified", this.domSubtreeModifiedListener, false);
         this.domCharacterDataModifiedListener = new DOMCharacterDataModifiedListener();
         var1.addEventListener("DOMCharacterDataModified", this.domCharacterDataModifiedListener, false);
      }

   }

   protected void removeEventListeners(EventTarget var1) {
      if (this.isCSSNavigableDocument) {
         CSSNavigableDocument var2 = (CSSNavigableDocument)var1;
         var2.removeCSSNavigableDocumentListener(this.cssNavigableDocumentListener);
      } else {
         var1.removeEventListener("DOMAttrModified", this.domAttrModifiedListener, false);
         var1.removeEventListener("DOMNodeInserted", this.domNodeInsertedListener, false);
         var1.removeEventListener("DOMNodeRemoved", this.domNodeRemovedListener, false);
         var1.removeEventListener("DOMSubtreeModified", this.domSubtreeModifiedListener, false);
         var1.removeEventListener("DOMCharacterDataModified", this.domCharacterDataModifiedListener, false);
      }

   }

   public void dispose() {
      this.setCSSEngineUserAgent((CSSEngineUserAgent)null);
      this.disposeStyleMaps(this.document.getDocumentElement());
      if (this.document instanceof EventTarget) {
         this.removeEventListeners((EventTarget)this.document);
      }

   }

   protected void disposeStyleMaps(Node var1) {
      if (var1 instanceof CSSStylableElement) {
         ((CSSStylableElement)var1).setComputedStyleMap((String)null, (StyleMap)null);
      }

      for(Node var2 = getCSSFirstChild(var1); var2 != null; var2 = getCSSNextSibling(var2)) {
         if (var2.getNodeType() == 1) {
            this.disposeStyleMaps(var2);
         }
      }

   }

   public CSSContext getCSSContext() {
      return this.cssContext;
   }

   public Document getDocument() {
      return this.document;
   }

   public int getFontSizeIndex() {
      return this.fontSizeIndex;
   }

   public int getLineHeightIndex() {
      return this.lineHeightIndex;
   }

   public int getColorIndex() {
      return this.colorIndex;
   }

   public int getNumberOfProperties() {
      return this.valueManagers.length;
   }

   public int getPropertyIndex(String var1) {
      return this.indexes.get(var1);
   }

   public int getShorthandIndex(String var1) {
      return this.shorthandIndexes.get(var1);
   }

   public String getPropertyName(int var1) {
      return this.valueManagers[var1].getPropertyName();
   }

   public void setCSSEngineUserAgent(CSSEngineUserAgent var1) {
      this.userAgent = var1;
   }

   public CSSEngineUserAgent getCSSEngineUserAgent() {
      return this.userAgent;
   }

   public void setUserAgentStyleSheet(StyleSheet var1) {
      this.userAgentStyleSheet = var1;
   }

   public void setUserStyleSheet(StyleSheet var1) {
      this.userStyleSheet = var1;
   }

   public ValueManager[] getValueManagers() {
      return this.valueManagers;
   }

   public ShorthandManager[] getShorthandManagers() {
      return this.shorthandManagers;
   }

   public List getFontFaces() {
      return this.fontFaces;
   }

   public void setMedia(String var1) {
      try {
         this.media = this.parser.parseMedia(var1);
      } catch (Exception var5) {
         String var3 = var5.getMessage();
         if (var3 == null) {
            var3 = "";
         }

         String var4 = Messages.formatMessage("media.error", new Object[]{var1, var3});
         throw new DOMException((short)12, var4);
      }
   }

   public void setAlternateStyleSheet(String var1) {
      this.alternateStyleSheet = var1;
   }

   public void importCascadedStyleMaps(Element var1, CSSEngine var2, Element var3) {
      if (var1 instanceof CSSStylableElement) {
         CSSStylableElement var4 = (CSSStylableElement)var1;
         CSSStylableElement var5 = (CSSStylableElement)var3;
         StyleMap var6 = var2.getCascadedStyleMap(var4, (String)null);
         var6.setFixedCascadedStyle(true);
         var5.setComputedStyleMap((String)null, var6);
         if (this.pseudoElementNames != null) {
            int var7 = this.pseudoElementNames.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String var9 = this.pseudoElementNames[var8];
               var6 = var2.getCascadedStyleMap(var4, var9);
               var5.setComputedStyleMap(var9, var6);
            }
         }
      }

      Node var10 = getCSSFirstChild(var3);

      for(Node var11 = getCSSFirstChild(var1); var10 != null; var11 = getCSSNextSibling(var11)) {
         if (var11.getNodeType() == 1) {
            this.importCascadedStyleMaps((Element)var11, var2, (Element)var10);
         }

         var10 = getCSSNextSibling(var10);
      }

   }

   public ParsedURL getCSSBaseURI() {
      if (this.cssBaseURI == null) {
         this.cssBaseURI = this.element.getCSSBase();
      }

      return this.cssBaseURI;
   }

   public StyleMap getCascadedStyleMap(CSSStylableElement var1, String var2) {
      int var3 = this.getNumberOfProperties();
      final StyleMap var4 = new StyleMap(var3);
      ArrayList var5;
      if (this.userAgentStyleSheet != null) {
         var5 = new ArrayList();
         this.addMatchingRules(var5, this.userAgentStyleSheet, var1, var2);
         this.addRules(var1, var2, var4, var5, (short)0);
      }

      if (this.userStyleSheet != null) {
         var5 = new ArrayList();
         this.addMatchingRules(var5, this.userStyleSheet, var1, var2);
         this.addRules(var1, var2, var4, var5, (short)8192);
      }

      this.element = var1;

      try {
         int var7;
         String var10;
         String var11;
         if (this.nonCSSPresentationalHints != null) {
            ShorthandManager.PropertyHandler var24 = new ShorthandManager.PropertyHandler() {
               public void property(String var1, LexicalUnit var2, boolean var3) {
                  int var4x = CSSEngine.this.getPropertyIndex(var1);
                  if (var4x != -1) {
                     ValueManager var5 = CSSEngine.this.valueManagers[var4x];
                     Value var6 = var5.createValue(var2, CSSEngine.this);
                     CSSEngine.this.putAuthorProperty(var4, var4x, var6, var3, (short)16384);
                  } else {
                     var4x = CSSEngine.this.getShorthandIndex(var1);
                     if (var4x != -1) {
                        CSSEngine.this.shorthandManagers[var4x].setValues(CSSEngine.this, this, var2, var3);
                     }
                  }
               }
            };
            NamedNodeMap var6 = var1.getAttributes();
            var7 = var6.getLength();

            for(int var8 = 0; var8 < var7; ++var8) {
               Node var9 = var6.item(var8);
               var10 = var9.getNodeName();
               if (this.nonCSSPresentationalHints.contains(var10)) {
                  var11 = var9.getNodeValue();

                  try {
                     LexicalUnit var12 = this.parser.parsePropertyValue(var9.getNodeValue());
                     var24.property(var10, var12, false);
                  } catch (Exception var21) {
                     System.err.println("\n***** CSSEngine: exception property.syntax.error:" + var21);
                     System.err.println("\nAttrValue:" + var11);
                     System.err.println("\nException:" + var21.getClass().getName());
                     var21.printStackTrace(System.err);
                     System.err.println("\n***** CSSEngine: exception....");
                     String var13 = var21.getMessage();
                     if (var13 == null) {
                        var13 = "";
                     }

                     String var14 = this.documentURI == null ? "<unknown>" : this.documentURI.toString();
                     String var15 = Messages.formatMessage("property.syntax.error.at", new Object[]{var14, var10, var9.getNodeValue(), var13});
                     DOMException var16 = new DOMException((short)12, var15);
                     if (this.userAgent == null) {
                        throw var16;
                     }

                     this.userAgent.displayError(var16);
                  }
               }
            }
         }

         CSSEngine var25 = this.cssContext.getCSSEngineForElement(var1);
         List var26 = var25.getStyleSheetNodes();
         var7 = var26.size();
         if (var7 > 0) {
            ArrayList var27 = new ArrayList();
            int var29 = 0;

            while(true) {
               if (var29 >= var7) {
                  this.addRules(var1, var2, var4, var27, (short)24576);
                  break;
               }

               CSSStyleSheetNode var32 = (CSSStyleSheetNode)var26.get(var29);
               StyleSheet var33 = var32.getCSSStyleSheet();
               if (var33 != null && (!var33.isAlternate() || var33.getTitle() == null || var33.getTitle().equals(this.alternateStyleSheet)) && this.mediaMatch(var33.getMedia())) {
                  this.addMatchingRules(var27, var33, var1, var2);
               }

               ++var29;
            }
         }

         if (this.styleLocalName != null) {
            String var28 = var1.getAttributeNS(this.styleNamespaceURI, this.styleLocalName);
            if (var28.length() > 0) {
               try {
                  this.parser.setSelectorFactory(CSSSelectorFactory.INSTANCE);
                  this.parser.setConditionFactory(this.cssConditionFactory);
                  this.styleDeclarationDocumentHandler.styleMap = var4;
                  this.parser.setDocumentHandler(this.styleDeclarationDocumentHandler);
                  this.parser.parseStyleDeclaration(var28);
                  this.styleDeclarationDocumentHandler.styleMap = null;
               } catch (Exception var22) {
                  var10 = var22.getMessage();
                  if (var10 == null) {
                     var10 = var22.getClass().getName();
                  }

                  var11 = this.documentURI == null ? "<unknown>" : this.documentURI.toString();
                  String var36 = Messages.formatMessage("style.syntax.error.at", new Object[]{var11, this.styleLocalName, var28, var10});
                  DOMException var38 = new DOMException((short)12, var36);
                  if (this.userAgent == null) {
                     throw var38;
                  }

                  this.userAgent.displayError(var38);
               }
            }
         }

         StyleDeclarationProvider var30 = var1.getOverrideStyleDeclarationProvider();
         if (var30 != null) {
            StyleDeclaration var31 = var30.getStyleDeclaration();
            if (var31 != null) {
               int var34 = var31.size();

               for(int var35 = 0; var35 < var34; ++var35) {
                  int var37 = var31.getIndex(var35);
                  Value var39 = var31.getValue(var35);
                  boolean var40 = var31.getPriority(var35);
                  if (!var4.isImportant(var37) || var40) {
                     var4.putValue(var37, var39);
                     var4.putImportant(var37, var40);
                     var4.putOrigin(var37, (short)-24576);
                  }
               }
            }
         }
      } finally {
         this.element = null;
         this.cssBaseURI = null;
      }

      return var4;
   }

   public Value getComputedStyle(CSSStylableElement var1, String var2, int var3) {
      StyleMap var4 = var1.getComputedStyleMap(var2);
      if (var4 == null) {
         var4 = this.getCascadedStyleMap(var1, var2);
         var1.setComputedStyleMap(var2, var4);
      }

      Value var5 = var4.getValue(var3);
      if (var4.isComputed(var3)) {
         return var5;
      } else {
         Value var6 = var5;
         ValueManager var7 = this.valueManagers[var3];
         CSSStylableElement var8 = getParentCSSStylableElement(var1);
         if (var5 == null) {
            if (var8 == null || !var7.isInheritedProperty()) {
               var6 = var7.getDefaultValue();
            }
         } else if (var8 != null && var5 == InheritValue.INSTANCE) {
            var6 = null;
         }

         Object var10;
         if (var6 == null) {
            var10 = this.getComputedStyle(var8, (String)null, var3);
            var4.putParentRelative(var3, true);
            var4.putInherited(var3, true);
         } else {
            var10 = var7.computeValue(var1, var2, this, var3, var4, var6);
         }

         if (var5 == null) {
            var4.putValue(var3, (Value)var10);
            var4.putNullCascaded(var3, true);
         } else if (var10 != var5) {
            ComputedValue var9 = new ComputedValue(var5);
            var9.setComputedValue((Value)var10);
            var4.putValue(var3, var9);
            var10 = var9;
         }

         var4.putComputed(var3, true);
         return (Value)var10;
      }
   }

   public List getStyleSheetNodes() {
      if (this.styleSheetNodes == null) {
         this.styleSheetNodes = new ArrayList();
         this.selectorAttributes = new HashSet();
         this.findStyleSheetNodes(this.document);
         int var1 = this.styleSheetNodes.size();

         for(int var2 = 0; var2 < var1; ++var2) {
            CSSStyleSheetNode var3 = (CSSStyleSheetNode)this.styleSheetNodes.get(var2);
            StyleSheet var4 = var3.getCSSStyleSheet();
            if (var4 != null) {
               this.findSelectorAttributes(this.selectorAttributes, var4);
            }
         }
      }

      return this.styleSheetNodes;
   }

   protected void findStyleSheetNodes(Node var1) {
      if (var1 instanceof CSSStyleSheetNode) {
         this.styleSheetNodes.add(var1);
      }

      for(Node var2 = getCSSFirstChild(var1); var2 != null; var2 = getCSSNextSibling(var2)) {
         this.findStyleSheetNodes(var2);
      }

   }

   protected void findSelectorAttributes(Set var1, StyleSheet var2) {
      int var3 = var2.getSize();

      label26:
      for(int var4 = 0; var4 < var3; ++var4) {
         Rule var5 = var2.getRule(var4);
         switch (var5.getType()) {
            case 0:
               StyleRule var6 = (StyleRule)var5;
               SelectorList var7 = var6.getSelectorList();
               int var8 = var7.getLength();
               int var11 = 0;

               while(true) {
                  if (var11 >= var8) {
                     continue label26;
                  }

                  ExtendedSelector var10 = (ExtendedSelector)var7.item(var11);
                  var10.fillAttributeSet(var1);
                  ++var11;
               }
            case 1:
            case 2:
               MediaRule var9 = (MediaRule)var5;
               if (this.mediaMatch(var9.getMediaList())) {
                  this.findSelectorAttributes(var1, var9);
               }
         }
      }

   }

   public void setMainProperties(CSSStylableElement var1, final MainPropertyReceiver var2, String var3, String var4, boolean var5) {
      try {
         this.element = var1;
         LexicalUnit var6 = this.parser.parsePropertyValue(var4);
         ShorthandManager.PropertyHandler var16 = new ShorthandManager.PropertyHandler() {
            public void property(String var1, LexicalUnit var2x, boolean var3) {
               int var4 = CSSEngine.this.getPropertyIndex(var1);
               if (var4 != -1) {
                  ValueManager var5 = CSSEngine.this.valueManagers[var4];
                  Value var6 = var5.createValue(var2x, CSSEngine.this);
                  var2.setMainProperty(var1, var6, var3);
               } else {
                  var4 = CSSEngine.this.getShorthandIndex(var1);
                  if (var4 != -1) {
                     CSSEngine.this.shorthandManagers[var4].setValues(CSSEngine.this, this, var2x, var3);
                  }
               }
            }
         };
         var16.property(var3, var6, var5);
      } catch (Exception var14) {
         String var7 = var14.getMessage();
         if (var7 == null) {
            var7 = "";
         }

         String var8 = this.documentURI == null ? "<unknown>" : this.documentURI.toString();
         String var9 = Messages.formatMessage("property.syntax.error.at", new Object[]{var8, var3, var4, var7});
         DOMException var10 = new DOMException((short)12, var9);
         if (this.userAgent == null) {
            throw var10;
         }

         this.userAgent.displayError(var10);
      } finally {
         this.element = null;
         this.cssBaseURI = null;
      }

   }

   public Value parsePropertyValue(CSSStylableElement var1, String var2, String var3) {
      int var4 = this.getPropertyIndex(var2);
      if (var4 == -1) {
         return null;
      } else {
         ValueManager var5 = this.valueManagers[var4];

         try {
            String var7;
            try {
               this.element = var1;
               LexicalUnit var6 = this.parser.parsePropertyValue(var3);
               Value var16 = var5.createValue(var6, this);
               return var16;
            } catch (Exception var14) {
               var7 = var14.getMessage();
               if (var7 == null) {
                  var7 = "";
               }
            }

            String var8 = this.documentURI == null ? "<unknown>" : this.documentURI.toString();
            String var9 = Messages.formatMessage("property.syntax.error.at", new Object[]{var8, var2, var3, var7});
            DOMException var10 = new DOMException((short)12, var9);
            if (this.userAgent == null) {
               throw var10;
            }

            this.userAgent.displayError(var10);
         } finally {
            this.element = null;
            this.cssBaseURI = null;
         }

         return var5.getDefaultValue();
      }
   }

   public StyleDeclaration parseStyleDeclaration(CSSStylableElement var1, String var2) {
      this.styleDeclarationBuilder.styleDeclaration = new StyleDeclaration();

      try {
         this.element = var1;
         this.parser.setSelectorFactory(CSSSelectorFactory.INSTANCE);
         this.parser.setConditionFactory(this.cssConditionFactory);
         this.parser.setDocumentHandler(this.styleDeclarationBuilder);
         this.parser.parseStyleDeclaration(var2);
      } catch (Exception var11) {
         String var4 = var11.getMessage();
         if (var4 == null) {
            var4 = "";
         }

         String var5 = this.documentURI == null ? "<unknown>" : this.documentURI.toString();
         String var6 = Messages.formatMessage("syntax.error.at", new Object[]{var5, var4});
         DOMException var7 = new DOMException((short)12, var6);
         if (this.userAgent == null) {
            throw var7;
         }

         this.userAgent.displayError(var7);
      } finally {
         this.element = null;
         this.cssBaseURI = null;
      }

      return this.styleDeclarationBuilder.styleDeclaration;
   }

   public StyleSheet parseStyleSheet(ParsedURL var1, String var2) throws DOMException {
      StyleSheet var3 = new StyleSheet();

      try {
         var3.setMedia(this.parser.parseMedia(var2));
      } catch (Exception var9) {
         String var5 = var9.getMessage();
         if (var5 == null) {
            var5 = "";
         }

         String var6 = this.documentURI == null ? "<unknown>" : this.documentURI.toString();
         String var7 = Messages.formatMessage("syntax.error.at", new Object[]{var6, var5});
         DOMException var8 = new DOMException((short)12, var7);
         if (this.userAgent == null) {
            throw var8;
         }

         this.userAgent.displayError(var8);
         return var3;
      }

      this.parseStyleSheet(var3, var1);
      return var3;
   }

   public StyleSheet parseStyleSheet(InputSource var1, ParsedURL var2, String var3) throws DOMException {
      StyleSheet var4 = new StyleSheet();

      try {
         var4.setMedia(this.parser.parseMedia(var3));
         this.parseStyleSheet(var4, var1, var2);
      } catch (Exception var10) {
         String var6 = var10.getMessage();
         if (var6 == null) {
            var6 = "";
         }

         String var7 = this.documentURI == null ? "<unknown>" : this.documentURI.toString();
         String var8 = Messages.formatMessage("syntax.error.at", new Object[]{var7, var6});
         DOMException var9 = new DOMException((short)12, var8);
         if (this.userAgent == null) {
            throw var9;
         }

         this.userAgent.displayError(var9);
      }

      return var4;
   }

   public void parseStyleSheet(StyleSheet var1, ParsedURL var2) throws DOMException {
      if (var2 == null) {
         String var3 = Messages.formatMessage("syntax.error.at", new Object[]{"Null Document reference", ""});
         DOMException var9 = new DOMException((short)12, var3);
         if (this.userAgent == null) {
            throw var9;
         } else {
            this.userAgent.displayError(var9);
         }
      } else {
         try {
            this.cssContext.checkLoadExternalResource(var2, this.documentURI);
            this.parseStyleSheet(var1, new InputSource(var2.toString()), var2);
         } catch (SecurityException var7) {
            throw var7;
         } catch (Exception var8) {
            String var4 = var8.getMessage();
            if (var4 == null) {
               var4 = var8.getClass().getName();
            }

            String var5 = Messages.formatMessage("syntax.error.at", new Object[]{var2.toString(), var4});
            DOMException var6 = new DOMException((short)12, var5);
            if (this.userAgent == null) {
               throw var6;
            }

            this.userAgent.displayError(var6);
         }

      }
   }

   public StyleSheet parseStyleSheet(String var1, ParsedURL var2, String var3) throws DOMException {
      StyleSheet var4 = new StyleSheet();

      try {
         var4.setMedia(this.parser.parseMedia(var3));
      } catch (Exception var10) {
         String var6 = var10.getMessage();
         if (var6 == null) {
            var6 = "";
         }

         String var7 = this.documentURI == null ? "<unknown>" : this.documentURI.toString();
         String var8 = Messages.formatMessage("syntax.error.at", new Object[]{var7, var6});
         DOMException var9 = new DOMException((short)12, var8);
         if (this.userAgent == null) {
            throw var9;
         }

         this.userAgent.displayError(var9);
         return var4;
      }

      this.parseStyleSheet(var4, var1, var2);
      return var4;
   }

   public void parseStyleSheet(StyleSheet var1, String var2, ParsedURL var3) throws DOMException {
      try {
         this.parseStyleSheet(var1, new InputSource(new StringReader(var2)), var3);
      } catch (Exception var8) {
         String var5 = var8.getMessage();
         if (var5 == null) {
            var5 = "";
         }

         String var6 = Messages.formatMessage("stylesheet.syntax.error", new Object[]{var3.toString(), var2, var5});
         DOMException var7 = new DOMException((short)12, var6);
         if (this.userAgent == null) {
            throw var7;
         }

         this.userAgent.displayError(var7);
      }

   }

   protected void parseStyleSheet(StyleSheet var1, InputSource var2, ParsedURL var3) throws IOException {
      this.parser.setSelectorFactory(CSSSelectorFactory.INSTANCE);
      this.parser.setConditionFactory(this.cssConditionFactory);

      try {
         this.cssBaseURI = var3;
         this.styleSheetDocumentHandler.styleSheet = var1;
         this.parser.setDocumentHandler(this.styleSheetDocumentHandler);
         this.parser.parseStyleSheet(var2);
         int var4 = var1.getSize();

         for(int var5 = 0; var5 < var4; ++var5) {
            Rule var6 = var1.getRule(var5);
            if (var6.getType() != 2) {
               break;
            }

            ImportRule var7 = (ImportRule)var6;
            this.parseStyleSheet((StyleSheet)var7, (ParsedURL)var7.getURI());
         }
      } finally {
         this.cssBaseURI = null;
      }

   }

   protected void putAuthorProperty(StyleMap var1, int var2, Value var3, boolean var4, short var5) {
      Value var6 = var1.getValue(var2);
      short var7 = var1.getOrigin(var2);
      boolean var8 = var1.isImportant(var2);
      boolean var9 = var6 == null;
      if (!var9) {
         switch (var7) {
            case -24576:
               var9 = false;
               break;
            case 8192:
               var9 = !var8;
               break;
            case 24576:
               var9 = !var8 || var4;
               break;
            default:
               var9 = true;
         }
      }

      if (var9) {
         var1.putValue(var2, var3);
         var1.putImportant(var2, var4);
         var1.putOrigin(var2, var5);
      }

   }

   protected void addMatchingRules(List var1, StyleSheet var2, Element var3, String var4) {
      int var5 = var2.getSize();

      label30:
      for(int var6 = 0; var6 < var5; ++var6) {
         Rule var7 = var2.getRule(var6);
         switch (var7.getType()) {
            case 0:
               StyleRule var8 = (StyleRule)var7;
               SelectorList var9 = var8.getSelectorList();
               int var10 = var9.getLength();
               int var13 = 0;

               while(true) {
                  if (var13 >= var10) {
                     continue label30;
                  }

                  ExtendedSelector var12 = (ExtendedSelector)var9.item(var13);
                  if (var12.match(var3, var4)) {
                     var1.add(var8);
                  }

                  ++var13;
               }
            case 1:
            case 2:
               MediaRule var11 = (MediaRule)var7;
               if (this.mediaMatch(var11.getMediaList())) {
                  this.addMatchingRules(var1, var11, var3, var4);
               }
         }
      }

   }

   protected void addRules(Element var1, String var2, StyleMap var3, ArrayList var4, short var5) {
      this.sortRules(var4, var1, var2);
      int var6 = var4.size();
      int var7;
      StyleRule var8;
      StyleDeclaration var9;
      int var10;
      int var11;
      if (var5 == 24576) {
         for(var7 = 0; var7 < var6; ++var7) {
            var8 = (StyleRule)var4.get(var7);
            var9 = var8.getStyleDeclaration();
            var10 = var9.size();

            for(var11 = 0; var11 < var10; ++var11) {
               this.putAuthorProperty(var3, var9.getIndex(var11), var9.getValue(var11), var9.getPriority(var11), var5);
            }
         }
      } else {
         for(var7 = 0; var7 < var6; ++var7) {
            var8 = (StyleRule)var4.get(var7);
            var9 = var8.getStyleDeclaration();
            var10 = var9.size();

            for(var11 = 0; var11 < var10; ++var11) {
               int var12 = var9.getIndex(var11);
               var3.putValue(var12, var9.getValue(var11));
               var3.putImportant(var12, var9.getPriority(var11));
               var3.putOrigin(var12, var5);
            }
         }
      }

   }

   protected void sortRules(ArrayList var1, Element var2, String var3) {
      int var4 = var1.size();
      int[] var5 = new int[var4];

      int var6;
      int var9;
      for(var6 = 0; var6 < var4; ++var6) {
         StyleRule var7 = (StyleRule)var1.get(var6);
         SelectorList var8 = var7.getSelectorList();
         var9 = 0;
         int var10 = var8.getLength();

         for(int var11 = 0; var11 < var10; ++var11) {
            ExtendedSelector var12 = (ExtendedSelector)var8.item(var11);
            if (var12.match(var2, var3)) {
               int var13 = var12.getSpecificity();
               if (var13 > var9) {
                  var9 = var13;
               }
            }
         }

         var5[var6] = var9;
      }

      for(var6 = 1; var6 < var4; ++var6) {
         Object var14 = var1.get(var6);
         int var15 = var5[var6];

         for(var9 = var6 - 1; var9 >= 0 && var5[var9] > var15; --var9) {
            var1.set(var9 + 1, var1.get(var9));
            var5[var9 + 1] = var5[var9];
         }

         var1.set(var9 + 1, var14);
         var5[var9 + 1] = var15;
      }

   }

   protected boolean mediaMatch(SACMediaList var1) {
      if (this.media != null && var1 != null && this.media.getLength() != 0 && var1.getLength() != 0) {
         for(int var2 = 0; var2 < var1.getLength(); ++var2) {
            if (var1.item(var2).equalsIgnoreCase("all")) {
               return true;
            }

            for(int var3 = 0; var3 < this.media.getLength(); ++var3) {
               if (this.media.item(var3).equalsIgnoreCase("all") || var1.item(var2).equalsIgnoreCase(this.media.item(var3))) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return true;
      }
   }

   public void addCSSEngineListener(CSSEngineListener var1) {
      this.listeners.add(var1);
   }

   public void removeCSSEngineListener(CSSEngineListener var1) {
      this.listeners.remove(var1);
   }

   protected void firePropertiesChangedEvent(Element var1, int[] var2) {
      CSSEngineListener[] var3 = (CSSEngineListener[])this.listeners.toArray(LISTENER_ARRAY);
      int var4 = var3.length;
      if (var4 > 0) {
         CSSEngineEvent var5 = new CSSEngineEvent(this, var1, var2);

         for(int var6 = 0; var6 < var4; ++var6) {
            var3[var6].propertiesChanged(var5);
         }
      }

   }

   protected void inlineStyleAttributeUpdated(CSSStylableElement var1, StyleMap var2, short var3, String var4, String var5) {
      boolean[] var6 = this.styleDeclarationUpdateHandler.updatedProperties;

      for(int var7 = this.getNumberOfProperties() - 1; var7 >= 0; --var7) {
         var6[var7] = false;
      }

      switch (var3) {
         case 1:
         case 2:
            if (var5.length() > 0) {
               this.element = var1;

               try {
                  this.parser.setSelectorFactory(CSSSelectorFactory.INSTANCE);
                  this.parser.setConditionFactory(this.cssConditionFactory);
                  this.styleDeclarationUpdateHandler.styleMap = var2;
                  this.parser.setDocumentHandler(this.styleDeclarationUpdateHandler);
                  this.parser.parseStyleDeclaration(var5);
                  this.styleDeclarationUpdateHandler.styleMap = null;
               } catch (Exception var16) {
                  String var8 = var16.getMessage();
                  if (var8 == null) {
                     var8 = "";
                  }

                  String var9 = this.documentURI == null ? "<unknown>" : this.documentURI.toString();
                  String var10 = Messages.formatMessage("style.syntax.error.at", new Object[]{var9, this.styleLocalName, var5, var8});
                  DOMException var11 = new DOMException((short)12, var10);
                  if (this.userAgent == null) {
                     throw var11;
                  } else {
                     this.userAgent.displayError(var11);
                  }
               } finally {
                  this.element = null;
                  this.cssBaseURI = null;
               }
            }
         case 3:
            boolean var18 = false;
            int var19;
            if (var4 != null && var4.length() > 0) {
               for(var19 = this.getNumberOfProperties() - 1; var19 >= 0; --var19) {
                  if (var2.isComputed(var19) && !var6[var19]) {
                     short var20 = var2.getOrigin(var19);
                     if (var20 >= Short.MIN_VALUE) {
                        var18 = true;
                        var6[var19] = true;
                     }
                  }
               }
            }

            if (var18) {
               this.invalidateProperties(var1, (int[])null, var6, true);
            } else {
               var19 = 0;
               boolean var21 = this.fontSizeIndex == -1 ? false : var6[this.fontSizeIndex];
               boolean var22 = this.lineHeightIndex == -1 ? false : var6[this.lineHeightIndex];
               boolean var23 = this.colorIndex == -1 ? false : var6[this.colorIndex];

               for(int var12 = this.getNumberOfProperties() - 1; var12 >= 0; --var12) {
                  if (var6[var12]) {
                     ++var19;
                  } else if (var21 && var2.isFontSizeRelative(var12) || var22 && var2.isLineHeightRelative(var12) || var23 && var2.isColorRelative(var12)) {
                     var6[var12] = true;
                     clearComputedValue(var2, var12);
                     ++var19;
                  }
               }

               if (var19 > 0) {
                  int[] var24 = new int[var19];
                  var19 = 0;

                  for(int var13 = this.getNumberOfProperties() - 1; var13 >= 0; --var13) {
                     if (var6[var13]) {
                        var24[var19++] = var13;
                     }
                  }

                  this.invalidateProperties(var1, var24, (boolean[])null, true);
               }
            }

            return;
         default:
            throw new IllegalStateException("Invalid attrChangeType");
      }
   }

   private static void clearComputedValue(StyleMap var0, int var1) {
      if (var0.isNullCascaded(var1)) {
         var0.putValue(var1, (Value)null);
      } else {
         Value var2 = var0.getValue(var1);
         if (var2 instanceof ComputedValue) {
            ComputedValue var3 = (ComputedValue)var2;
            var2 = var3.getCascadedValue();
            var0.putValue(var1, var2);
         }
      }

      var0.putComputed(var1, false);
   }

   protected void invalidateProperties(Node var1, int[] var2, boolean[] var3, boolean var4) {
      if (var1 instanceof CSSStylableElement) {
         CSSStylableElement var5 = (CSSStylableElement)var1;
         StyleMap var6 = var5.getComputedStyleMap((String)null);
         if (var6 != null) {
            boolean[] var7 = new boolean[this.getNumberOfProperties()];
            if (var3 != null) {
               System.arraycopy(var3, 0, var7, 0, var3.length);
            }

            int var8;
            if (var2 != null) {
               for(var8 = 0; var8 < var2.length; ++var8) {
                  var7[var2[var8]] = true;
               }
            }

            var8 = 0;
            int var10;
            if (!var4) {
               for(int var9 = 0; var9 < var7.length; ++var9) {
                  if (var7[var9]) {
                     ++var8;
                  }
               }
            } else {
               StyleMap var15 = this.getCascadedStyleMap(var5, (String)null);
               var5.setComputedStyleMap((String)null, var15);

               for(var10 = 0; var10 < var7.length; ++var10) {
                  if (var7[var10]) {
                     ++var8;
                  } else {
                     Value var11 = var15.getValue(var10);
                     Value var12 = null;
                     if (!var6.isNullCascaded(var10)) {
                        var12 = var6.getValue(var10);
                        if (var12 instanceof ComputedValue) {
                           var12 = ((ComputedValue)var12).getCascadedValue();
                        }
                     }

                     if (var11 != var12) {
                        if (var11 != null && var12 != null) {
                           if (var11.equals(var12)) {
                              continue;
                           }

                           String var13 = var12.getCssText();
                           String var14 = var11.getCssText();
                           if (var14 == var13 || var14 != null && var14.equals(var13)) {
                              continue;
                           }
                        }

                        ++var8;
                        var7[var10] = true;
                     }
                  }
               }
            }

            int[] var16 = null;
            if (var8 != 0) {
               var16 = new int[var8];
               var8 = 0;

               for(var10 = 0; var10 < var7.length; ++var10) {
                  if (var7[var10]) {
                     var16[var8++] = var10;
                  }
               }
            }

            this.propagateChanges(var5, var16, var4);
         }
      }
   }

   protected void propagateChanges(Node var1, int[] var2, boolean var3) {
      if (var1 instanceof CSSStylableElement) {
         CSSStylableElement var4 = (CSSStylableElement)var1;
         StyleMap var5 = var4.getComputedStyleMap((String)null);
         int var7;
         int var8;
         if (var5 != null) {
            boolean[] var6 = this.styleDeclarationUpdateHandler.updatedProperties;

            for(var7 = this.getNumberOfProperties() - 1; var7 >= 0; --var7) {
               var6[var7] = false;
            }

            if (var2 != null) {
               for(var7 = var2.length - 1; var7 >= 0; --var7) {
                  var8 = var2[var7];
                  var6[var8] = true;
               }
            }

            boolean var13 = this.fontSizeIndex == -1 ? false : var6[this.fontSizeIndex];
            boolean var14 = this.lineHeightIndex == -1 ? false : var6[this.lineHeightIndex];
            boolean var9 = this.colorIndex == -1 ? false : var6[this.colorIndex];
            int var10 = 0;

            int var11;
            for(var11 = this.getNumberOfProperties() - 1; var11 >= 0; --var11) {
               if (var6[var11]) {
                  ++var10;
               } else if (var13 && var5.isFontSizeRelative(var11) || var14 && var5.isLineHeightRelative(var11) || var9 && var5.isColorRelative(var11)) {
                  var6[var11] = true;
                  clearComputedValue(var5, var11);
                  ++var10;
               }
            }

            if (var10 == 0) {
               var2 = null;
            } else {
               var2 = new int[var10];
               var10 = 0;

               for(var11 = this.getNumberOfProperties() - 1; var11 >= 0; --var11) {
                  if (var6[var11]) {
                     var2[var10++] = var11;
                  }
               }

               this.firePropertiesChangedEvent(var4, var2);
            }
         }

         int[] var12 = var2;
         if (var2 != null) {
            var7 = 0;

            for(var8 = 0; var8 < var2.length; ++var8) {
               ValueManager var15 = this.valueManagers[var2[var8]];
               if (var15.isInheritedProperty()) {
                  ++var7;
               } else {
                  var2[var8] = -1;
               }
            }

            if (var7 == 0) {
               var12 = null;
            } else {
               var12 = new int[var7];
               var7 = 0;

               for(var8 = 0; var8 < var2.length; ++var8) {
                  if (var2[var8] != -1) {
                     var12[var7++] = var2[var8];
                  }
               }
            }
         }

         for(Node var16 = getCSSFirstChild(var1); var16 != null; var16 = getCSSNextSibling(var16)) {
            if (var16.getNodeType() == 1) {
               this.invalidateProperties(var16, var12, (boolean[])null, var3);
            }
         }

      }
   }

   protected void nonCSSPresentationalHintUpdated(CSSStylableElement var1, StyleMap var2, String var3, short var4, String var5) {
      int var6 = this.getPropertyIndex(var3);
      if (!var2.isImportant(var6)) {
         if (var2.getOrigin(var6) < 24576) {
            switch (var4) {
               case 1:
               case 2:
                  this.element = var1;

                  try {
                     LexicalUnit var18 = this.parser.parsePropertyValue(var5);
                     ValueManager var20 = this.valueManagers[var6];
                     Value var22 = var20.createValue(var18, this);
                     var2.putMask(var6, (short)0);
                     var2.putValue(var6, var22);
                     var2.putOrigin(var6, (short)16384);
                  } catch (Exception var16) {
                     String var8 = var16.getMessage();
                     if (var8 == null) {
                        var8 = "";
                     }

                     String var9 = this.documentURI == null ? "<unknown>" : this.documentURI.toString();
                     String var10 = Messages.formatMessage("property.syntax.error.at", new Object[]{var9, var3, var5, var8});
                     DOMException var11 = new DOMException((short)12, var10);
                     if (this.userAgent == null) {
                        throw var11;
                     } else {
                        this.userAgent.displayError(var11);
                     }
                  } finally {
                     this.element = null;
                     this.cssBaseURI = null;
                  }
               default:
                  boolean[] var19 = this.styleDeclarationUpdateHandler.updatedProperties;

                  for(int var21 = this.getNumberOfProperties() - 1; var21 >= 0; --var21) {
                     var19[var21] = false;
                  }

                  var19[var6] = true;
                  boolean var24 = var6 == this.fontSizeIndex;
                  boolean var23 = var6 == this.lineHeightIndex;
                  boolean var25 = var6 == this.colorIndex;
                  int var26 = 0;

                  for(int var12 = this.getNumberOfProperties() - 1; var12 >= 0; --var12) {
                     if (var19[var12]) {
                        ++var26;
                     } else if (var24 && var2.isFontSizeRelative(var12) || var23 && var2.isLineHeightRelative(var12) || var25 && var2.isColorRelative(var12)) {
                        var19[var12] = true;
                        clearComputedValue(var2, var12);
                        ++var26;
                     }
                  }

                  int[] var27 = new int[var26];
                  var26 = 0;

                  for(int var13 = this.getNumberOfProperties() - 1; var13 >= 0; --var13) {
                     if (var19[var13]) {
                        var27[var26++] = var13;
                     }
                  }

                  this.invalidateProperties(var1, var27, (boolean[])null, true);
                  return;
               case 3:
                  int[] var7 = new int[]{var6};
                  this.invalidateProperties(var1, var7, (boolean[])null, true);
            }
         }
      }
   }

   protected boolean hasStyleSheetNode(Node var1) {
      if (var1 instanceof CSSStyleSheetNode) {
         return true;
      } else {
         for(var1 = getCSSFirstChild(var1); var1 != null; var1 = getCSSNextSibling(var1)) {
            if (this.hasStyleSheetNode(var1)) {
               return true;
            }
         }

         return false;
      }
   }

   protected void handleAttrModified(Element var1, Attr var2, short var3, String var4, String var5) {
      if (var1 instanceof CSSStylableElement) {
         if (!var5.equals(var4)) {
            String var6 = var2.getNamespaceURI();
            String var7 = var6 == null ? var2.getNodeName() : var2.getLocalName();
            CSSStylableElement var8 = (CSSStylableElement)var1;
            StyleMap var9 = var8.getComputedStyleMap((String)null);
            if (var9 != null) {
               if ((var6 == this.styleNamespaceURI || var6 != null && var6.equals(this.styleNamespaceURI)) && var7.equals(this.styleLocalName)) {
                  this.inlineStyleAttributeUpdated(var8, var9, var3, var4, var5);
                  return;
               }

               if (this.nonCSSPresentationalHints != null && (var6 == this.nonCSSPresentationalHintsNamespaceURI || var6 != null && var6.equals(this.nonCSSPresentationalHintsNamespaceURI)) && this.nonCSSPresentationalHints.contains(var7)) {
                  this.nonCSSPresentationalHintUpdated(var8, var9, var7, var3, var5);
                  return;
               }
            }

            if (this.selectorAttributes != null && this.selectorAttributes.contains(var7)) {
               this.invalidateProperties(var8, (int[])null, (boolean[])null, true);

               for(Node var10 = getCSSNextSibling(var8); var10 != null; var10 = getCSSNextSibling(var10)) {
                  this.invalidateProperties(var10, (int[])null, (boolean[])null, true);
               }
            }

         }
      }
   }

   protected void handleNodeInserted(Node var1) {
      if (this.hasStyleSheetNode(var1)) {
         this.styleSheetNodes = null;
         this.invalidateProperties(this.document.getDocumentElement(), (int[])null, (boolean[])null, true);
      } else if (var1 instanceof CSSStylableElement) {
         for(var1 = getCSSNextSibling(var1); var1 != null; var1 = getCSSNextSibling(var1)) {
            this.invalidateProperties(var1, (int[])null, (boolean[])null, true);
         }
      }

   }

   protected void handleNodeRemoved(Node var1) {
      if (this.hasStyleSheetNode(var1)) {
         this.styleSheetRemoved = true;
      } else if (var1 instanceof CSSStylableElement) {
         this.removedStylableElementSibling = getCSSNextSibling(var1);
      }

      this.disposeStyleMaps(var1);
   }

   protected void handleSubtreeModified(Node var1) {
      if (this.styleSheetRemoved) {
         this.styleSheetRemoved = false;
         this.styleSheetNodes = null;
         this.invalidateProperties(this.document.getDocumentElement(), (int[])null, (boolean[])null, true);
      } else if (this.removedStylableElementSibling != null) {
         for(Node var2 = this.removedStylableElementSibling; var2 != null; var2 = getCSSNextSibling(var2)) {
            this.invalidateProperties(var2, (int[])null, (boolean[])null, true);
         }

         this.removedStylableElementSibling = null;
      }

   }

   protected void handleCharacterDataModified(Node var1) {
      if (getCSSParentNode(var1) instanceof CSSStyleSheetNode) {
         this.styleSheetNodes = null;
         this.invalidateProperties(this.document.getDocumentElement(), (int[])null, (boolean[])null, true);
      }

   }

   protected class DOMAttrModifiedListener implements EventListener {
      public void handleEvent(Event var1) {
         MutationEvent var2 = (MutationEvent)var1;
         CSSEngine.this.handleAttrModified((Element)var1.getTarget(), (Attr)var2.getRelatedNode(), var2.getAttrChange(), var2.getPrevValue(), var2.getNewValue());
      }
   }

   protected class DOMCharacterDataModifiedListener implements EventListener {
      public void handleEvent(Event var1) {
         CSSEngine.this.handleCharacterDataModified((Node)var1.getTarget());
      }
   }

   protected class DOMSubtreeModifiedListener implements EventListener {
      public void handleEvent(Event var1) {
         CSSEngine.this.handleSubtreeModified((Node)var1.getTarget());
      }
   }

   protected class DOMNodeRemovedListener implements EventListener {
      public void handleEvent(Event var1) {
         CSSEngine.this.handleNodeRemoved((Node)var1.getTarget());
      }
   }

   protected class DOMNodeInsertedListener implements EventListener {
      public void handleEvent(Event var1) {
         CSSEngine.this.handleNodeInserted((Node)var1.getTarget());
      }
   }

   protected class CSSNavigableDocumentHandler implements CSSNavigableDocumentListener, MainPropertyReceiver {
      protected boolean[] mainPropertiesChanged;
      protected StyleDeclaration declaration;

      public void nodeInserted(Node var1) {
         CSSEngine.this.handleNodeInserted(var1);
      }

      public void nodeToBeRemoved(Node var1) {
         CSSEngine.this.handleNodeRemoved(var1);
      }

      public void subtreeModified(Node var1) {
         CSSEngine.this.handleSubtreeModified(var1);
      }

      public void characterDataModified(Node var1) {
         CSSEngine.this.handleCharacterDataModified(var1);
      }

      public void attrModified(Element var1, Attr var2, short var3, String var4, String var5) {
         CSSEngine.this.handleAttrModified(var1, var2, var3, var4, var5);
      }

      public void overrideStyleTextChanged(CSSStylableElement var1, String var2) {
         StyleDeclarationProvider var3 = var1.getOverrideStyleDeclarationProvider();
         StyleDeclaration var4 = var3.getStyleDeclaration();
         int var5 = var4.size();
         boolean[] var6 = new boolean[CSSEngine.this.getNumberOfProperties()];

         int var7;
         for(var7 = 0; var7 < var5; ++var7) {
            var6[var4.getIndex(var7)] = true;
         }

         var4 = CSSEngine.this.parseStyleDeclaration(var1, var2);
         var3.setStyleDeclaration(var4);
         var5 = var4.size();

         for(var7 = 0; var7 < var5; ++var7) {
            var6[var4.getIndex(var7)] = true;
         }

         CSSEngine.this.invalidateProperties(var1, (int[])null, var6, true);
      }

      public void overrideStylePropertyRemoved(CSSStylableElement var1, String var2) {
         StyleDeclarationProvider var3 = var1.getOverrideStyleDeclarationProvider();
         StyleDeclaration var4 = var3.getStyleDeclaration();
         int var5 = CSSEngine.this.getPropertyIndex(var2);
         int var6 = var4.size();

         for(int var7 = 0; var7 < var6; ++var7) {
            if (var5 == var4.getIndex(var7)) {
               var4.remove(var7);
               StyleMap var8 = var1.getComputedStyleMap((String)null);
               if (var8 != null && var8.getOrigin(var5) == -24576) {
                  CSSEngine.this.invalidateProperties(var1, new int[]{var5}, (boolean[])null, true);
               }
               break;
            }
         }

      }

      public void overrideStylePropertyChanged(CSSStylableElement var1, String var2, String var3, String var4) {
         boolean var5 = var4 != null && var4.length() != 0;
         StyleDeclarationProvider var6 = var1.getOverrideStyleDeclarationProvider();
         this.declaration = var6.getStyleDeclaration();
         CSSEngine.this.setMainProperties(var1, this, var2, var3, var5);
         this.declaration = null;
         CSSEngine.this.invalidateProperties(var1, (int[])null, this.mainPropertiesChanged, true);
      }

      public void setMainProperty(String var1, Value var2, boolean var3) {
         int var4 = CSSEngine.this.getPropertyIndex(var1);
         if (var4 != -1) {
            int var5;
            for(var5 = 0; var5 < this.declaration.size() && var4 != this.declaration.getIndex(var5); ++var5) {
            }

            if (var5 < this.declaration.size()) {
               this.declaration.put(var5, var2, var4, var3);
            } else {
               this.declaration.append(var2, var4, var3);
            }

         }
      }
   }

   protected class StyleDeclarationUpdateHandler extends DocumentAdapter implements ShorthandManager.PropertyHandler {
      public StyleMap styleMap;
      public boolean[] updatedProperties = new boolean[CSSEngine.this.getNumberOfProperties()];

      public void property(String var1, LexicalUnit var2, boolean var3) throws CSSException {
         int var4 = CSSEngine.this.getPropertyIndex(var1);
         if (var4 == -1) {
            var4 = CSSEngine.this.getShorthandIndex(var1);
            if (var4 == -1) {
               return;
            }

            CSSEngine.this.shorthandManagers[var4].setValues(CSSEngine.this, this, var2, var3);
         } else {
            if (this.styleMap.isImportant(var4)) {
               return;
            }

            this.updatedProperties[var4] = true;
            Value var5 = CSSEngine.this.valueManagers[var4].createValue(var2, CSSEngine.this);
            this.styleMap.putMask(var4, (short)0);
            this.styleMap.putValue(var4, var5);
            this.styleMap.putOrigin(var4, (short)Short.MIN_VALUE);
         }

      }
   }

   protected static class DocumentAdapter implements DocumentHandler {
      public void startDocument(InputSource var1) {
         this.throwUnsupportedEx();
      }

      public void endDocument(InputSource var1) {
         this.throwUnsupportedEx();
      }

      public void comment(String var1) {
      }

      public void ignorableAtRule(String var1) {
         this.throwUnsupportedEx();
      }

      public void namespaceDeclaration(String var1, String var2) {
         this.throwUnsupportedEx();
      }

      public void importStyle(String var1, SACMediaList var2, String var3) {
         this.throwUnsupportedEx();
      }

      public void startMedia(SACMediaList var1) {
         this.throwUnsupportedEx();
      }

      public void endMedia(SACMediaList var1) {
         this.throwUnsupportedEx();
      }

      public void startPage(String var1, String var2) {
         this.throwUnsupportedEx();
      }

      public void endPage(String var1, String var2) {
         this.throwUnsupportedEx();
      }

      public void startFontFace() {
         this.throwUnsupportedEx();
      }

      public void endFontFace() {
         this.throwUnsupportedEx();
      }

      public void startSelector(SelectorList var1) {
         this.throwUnsupportedEx();
      }

      public void endSelector(SelectorList var1) {
         this.throwUnsupportedEx();
      }

      public void property(String var1, LexicalUnit var2, boolean var3) {
         this.throwUnsupportedEx();
      }

      private void throwUnsupportedEx() {
         throw new UnsupportedOperationException("you try to use an empty method in Adapter-class");
      }
   }

   protected class StyleSheetDocumentHandler extends DocumentAdapter implements ShorthandManager.PropertyHandler {
      public StyleSheet styleSheet;
      protected StyleRule styleRule;
      protected StyleDeclaration styleDeclaration;

      public void startDocument(InputSource var1) throws CSSException {
      }

      public void endDocument(InputSource var1) throws CSSException {
      }

      public void ignorableAtRule(String var1) throws CSSException {
      }

      public void importStyle(String var1, SACMediaList var2, String var3) throws CSSException {
         ImportRule var4 = new ImportRule();
         var4.setMediaList(var2);
         var4.setParent(this.styleSheet);
         ParsedURL var5 = CSSEngine.this.getCSSBaseURI();
         ParsedURL var6;
         if (var5 == null) {
            var6 = new ParsedURL(var1);
         } else {
            var6 = new ParsedURL(var5, var1);
         }

         var4.setURI(var6);
         this.styleSheet.append(var4);
      }

      public void startMedia(SACMediaList var1) throws CSSException {
         MediaRule var2 = new MediaRule();
         var2.setMediaList(var1);
         var2.setParent(this.styleSheet);
         this.styleSheet.append(var2);
         this.styleSheet = var2;
      }

      public void endMedia(SACMediaList var1) throws CSSException {
         this.styleSheet = this.styleSheet.getParent();
      }

      public void startPage(String var1, String var2) throws CSSException {
      }

      public void endPage(String var1, String var2) throws CSSException {
      }

      public void startFontFace() throws CSSException {
         this.styleDeclaration = new StyleDeclaration();
      }

      public void endFontFace() throws CSSException {
         StyleMap var1 = new StyleMap(CSSEngine.this.getNumberOfProperties());
         int var2 = this.styleDeclaration.size();

         int var3;
         for(var3 = 0; var3 < var2; ++var3) {
            int var4 = this.styleDeclaration.getIndex(var3);
            var1.putValue(var4, this.styleDeclaration.getValue(var3));
            var1.putImportant(var4, this.styleDeclaration.getPriority(var3));
            var1.putOrigin(var4, (short)24576);
         }

         this.styleDeclaration = null;
         var3 = CSSEngine.this.getPropertyIndex("font-family");
         Value var6 = var1.getValue(var3);
         if (var6 != null) {
            ParsedURL var5 = CSSEngine.this.getCSSBaseURI();
            CSSEngine.this.fontFaces.add(new FontFaceRule(var1, var5));
         }
      }

      public void startSelector(SelectorList var1) throws CSSException {
         this.styleRule = new StyleRule();
         this.styleRule.setSelectorList(var1);
         this.styleDeclaration = new StyleDeclaration();
         this.styleRule.setStyleDeclaration(this.styleDeclaration);
         this.styleSheet.append(this.styleRule);
      }

      public void endSelector(SelectorList var1) throws CSSException {
         this.styleRule = null;
         this.styleDeclaration = null;
      }

      public void property(String var1, LexicalUnit var2, boolean var3) throws CSSException {
         int var4 = CSSEngine.this.getPropertyIndex(var1);
         if (var4 == -1) {
            var4 = CSSEngine.this.getShorthandIndex(var1);
            if (var4 == -1) {
               return;
            }

            CSSEngine.this.shorthandManagers[var4].setValues(CSSEngine.this, this, var2, var3);
         } else {
            Value var5 = CSSEngine.this.valueManagers[var4].createValue(var2, CSSEngine.this);
            this.styleDeclaration.append(var5, var4, var3);
         }

      }
   }

   protected class StyleDeclarationBuilder extends DocumentAdapter implements ShorthandManager.PropertyHandler {
      public StyleDeclaration styleDeclaration;

      public void property(String var1, LexicalUnit var2, boolean var3) throws CSSException {
         int var4 = CSSEngine.this.getPropertyIndex(var1);
         if (var4 == -1) {
            var4 = CSSEngine.this.getShorthandIndex(var1);
            if (var4 == -1) {
               return;
            }

            CSSEngine.this.shorthandManagers[var4].setValues(CSSEngine.this, this, var2, var3);
         } else {
            Value var5 = CSSEngine.this.valueManagers[var4].createValue(var2, CSSEngine.this);
            this.styleDeclaration.append(var5, var4, var3);
         }

      }
   }

   protected class StyleDeclarationDocumentHandler extends DocumentAdapter implements ShorthandManager.PropertyHandler {
      public StyleMap styleMap;

      public void property(String var1, LexicalUnit var2, boolean var3) throws CSSException {
         int var4 = CSSEngine.this.getPropertyIndex(var1);
         if (var4 == -1) {
            var4 = CSSEngine.this.getShorthandIndex(var1);
            if (var4 == -1) {
               return;
            }

            CSSEngine.this.shorthandManagers[var4].setValues(CSSEngine.this, this, var2, var3);
         } else {
            Value var5 = CSSEngine.this.valueManagers[var4].createValue(var2, CSSEngine.this);
            CSSEngine.this.putAuthorProperty(this.styleMap, var4, var5, var3, (short)Short.MIN_VALUE);
         }

      }
   }

   public interface MainPropertyReceiver {
      void setMainProperty(String var1, Value var2, boolean var3);
   }
}

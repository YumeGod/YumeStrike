package org.apache.batik.bridge;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.ref.SoftReference;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.apache.batik.css.engine.CSSEngineEvent;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.css.engine.value.ListValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.dom.svg.AbstractSVGAnimatedLength;
import org.apache.batik.dom.svg.AnimatedLiveAttributeValue;
import org.apache.batik.dom.svg.LiveAttributeException;
import org.apache.batik.dom.svg.SVGContext;
import org.apache.batik.dom.svg.SVGOMAnimatedEnumeration;
import org.apache.batik.dom.svg.SVGOMAnimatedLengthList;
import org.apache.batik.dom.svg.SVGOMAnimatedNumberList;
import org.apache.batik.dom.svg.SVGOMElement;
import org.apache.batik.dom.svg.SVGOMTextPositioningElement;
import org.apache.batik.dom.svg.SVGTextContent;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.dom.util.XMLSupport;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.font.FontFamilyResolver;
import org.apache.batik.gvt.font.GVTFont;
import org.apache.batik.gvt.font.GVTFontFamily;
import org.apache.batik.gvt.font.GVTGlyphMetrics;
import org.apache.batik.gvt.font.GVTGlyphVector;
import org.apache.batik.gvt.font.UnresolvedFontFamily;
import org.apache.batik.gvt.renderer.StrokingTextPainter;
import org.apache.batik.gvt.text.GVTAttributedCharacterIterator;
import org.apache.batik.gvt.text.Mark;
import org.apache.batik.gvt.text.TextHit;
import org.apache.batik.gvt.text.TextPaintInfo;
import org.apache.batik.gvt.text.TextPath;
import org.apache.batik.gvt.text.TextSpanLayout;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.svg.SVGLengthList;
import org.w3c.dom.svg.SVGNumberList;
import org.w3c.dom.svg.SVGTextContentElement;
import org.w3c.dom.svg.SVGTextPositioningElement;

public class SVGTextElementBridge extends AbstractGraphicsNodeBridge implements SVGTextContent {
   protected static final Integer ZERO = new Integer(0);
   public static final AttributedCharacterIterator.Attribute TEXT_COMPOUND_DELIMITER;
   public static final AttributedCharacterIterator.Attribute TEXT_COMPOUND_ID;
   public static final AttributedCharacterIterator.Attribute PAINT_INFO;
   public static final AttributedCharacterIterator.Attribute ALT_GLYPH_HANDLER;
   public static final AttributedCharacterIterator.Attribute TEXTPATH;
   public static final AttributedCharacterIterator.Attribute ANCHOR_TYPE;
   public static final AttributedCharacterIterator.Attribute GVT_FONT_FAMILIES;
   public static final AttributedCharacterIterator.Attribute GVT_FONTS;
   public static final AttributedCharacterIterator.Attribute BASELINE_SHIFT;
   protected AttributedString laidoutText;
   protected WeakHashMap elemTPI = new WeakHashMap();
   protected boolean usingComplexSVGFont = false;
   protected DOMChildNodeRemovedEventListener childNodeRemovedEventListener;
   protected DOMSubtreeModifiedEventListener subtreeModifiedEventListener;
   private boolean hasNewACI;
   private Element cssProceedElement;
   protected int endLimit;

   public String getLocalName() {
      return "text";
   }

   public Bridge getInstance() {
      return new SVGTextElementBridge();
   }

   protected TextNode getTextNode() {
      return (TextNode)this.node;
   }

   public GraphicsNode createGraphicsNode(BridgeContext var1, Element var2) {
      TextNode var3 = (TextNode)super.createGraphicsNode(var1, var2);
      if (var3 == null) {
         return null;
      } else {
         this.associateSVGContext(var1, var2, var3);

         for(Node var4 = this.getFirstChild(var2); var4 != null; var4 = this.getNextSibling(var4)) {
            if (var4.getNodeType() == 1) {
               this.addContextToChild(var1, (Element)var4);
            }
         }

         if (var1.getTextPainter() != null) {
            var3.setTextPainter(var1.getTextPainter());
         }

         RenderingHints var5 = null;
         var5 = CSSUtilities.convertColorRendering(var2, var5);
         var5 = CSSUtilities.convertTextRendering(var2, var5);
         if (var5 != null) {
            var3.setRenderingHints(var5);
         }

         var3.setLocation(this.getLocation(var1, var2));
         return var3;
      }
   }

   protected GraphicsNode instantiateGraphicsNode() {
      return new TextNode();
   }

   protected Point2D getLocation(BridgeContext var1, Element var2) {
      try {
         SVGOMTextPositioningElement var3 = (SVGOMTextPositioningElement)var2;
         SVGOMAnimatedLengthList var4 = (SVGOMAnimatedLengthList)var3.getX();
         var4.check();
         SVGLengthList var5 = var4.getAnimVal();
         float var6 = 0.0F;
         if (var5.getNumberOfItems() > 0) {
            var6 = var5.getItem(0).getValue();
         }

         SVGOMAnimatedLengthList var7 = (SVGOMAnimatedLengthList)var3.getY();
         var7.check();
         SVGLengthList var8 = var7.getAnimVal();
         float var9 = 0.0F;
         if (var8.getNumberOfItems() > 0) {
            var9 = var8.getItem(0).getValue();
         }

         return new Point2D.Float(var6, var9);
      } catch (LiveAttributeException var10) {
         throw new BridgeException(var1, var10);
      }
   }

   protected boolean isTextElement(Element var1) {
      if (!"http://www.w3.org/2000/svg".equals(var1.getNamespaceURI())) {
         return false;
      } else {
         String var2 = var1.getLocalName();
         return var2.equals("text") || var2.equals("tspan") || var2.equals("altGlyph") || var2.equals("a") || var2.equals("textPath") || var2.equals("tref");
      }
   }

   protected boolean isTextChild(Element var1) {
      if (!"http://www.w3.org/2000/svg".equals(var1.getNamespaceURI())) {
         return false;
      } else {
         String var2 = var1.getLocalName();
         return var2.equals("tspan") || var2.equals("altGlyph") || var2.equals("a") || var2.equals("textPath") || var2.equals("tref");
      }
   }

   public void buildGraphicsNode(BridgeContext var1, Element var2, GraphicsNode var3) {
      var2.normalize();
      this.computeLaidoutText(var1, var2, var3);
      var3.setComposite(CSSUtilities.convertOpacity(var2));
      var3.setFilter(CSSUtilities.convertFilter(var2, var3, var1));
      var3.setMask(CSSUtilities.convertMask(var2, var3, var1));
      var3.setClip(CSSUtilities.convertClipPath(var2, var3, var1));
      var3.setPointerEventType(CSSUtilities.convertPointerEvents(var2));
      this.initializeDynamicSupport(var1, var2, var3);
      if (!var1.isDynamic()) {
         this.elemTPI.clear();
      }

   }

   public boolean isComposite() {
      return false;
   }

   protected Node getFirstChild(Node var1) {
      return var1.getFirstChild();
   }

   protected Node getNextSibling(Node var1) {
      return var1.getNextSibling();
   }

   protected Node getParentNode(Node var1) {
      return var1.getParentNode();
   }

   protected void initializeDynamicSupport(BridgeContext var1, Element var2, GraphicsNode var3) {
      super.initializeDynamicSupport(var1, var2, var3);
      if (var1.isDynamic()) {
         this.addTextEventListeners(var1, (NodeEventTarget)var2);
      }

   }

   protected void addTextEventListeners(BridgeContext var1, NodeEventTarget var2) {
      if (this.childNodeRemovedEventListener == null) {
         this.childNodeRemovedEventListener = new DOMChildNodeRemovedEventListener();
      }

      if (this.subtreeModifiedEventListener == null) {
         this.subtreeModifiedEventListener = new DOMSubtreeModifiedEventListener();
      }

      var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.childNodeRemovedEventListener, true, (Object)null);
      var1.storeEventListenerNS(var2, "http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.childNodeRemovedEventListener, true);
      var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMSubtreeModified", this.subtreeModifiedEventListener, false, (Object)null);
      var1.storeEventListenerNS(var2, "http://www.w3.org/2001/xml-events", "DOMSubtreeModified", this.subtreeModifiedEventListener, false);
   }

   protected void removeTextEventListeners(BridgeContext var1, NodeEventTarget var2) {
      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.childNodeRemovedEventListener, true);
      var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMSubtreeModified", this.subtreeModifiedEventListener, false);
   }

   public void dispose() {
      this.removeTextEventListeners(this.ctx, (NodeEventTarget)this.e);
      super.dispose();
   }

   protected void addContextToChild(BridgeContext var1, Element var2) {
      if ("http://www.w3.org/2000/svg".equals(var2.getNamespaceURI())) {
         if (var2.getLocalName().equals("tspan")) {
            ((SVGOMElement)var2).setSVGContext(new TspanBridge(var1, this, var2));
         } else if (var2.getLocalName().equals("textPath")) {
            ((SVGOMElement)var2).setSVGContext(new TextPathBridge(var1, this, var2));
         } else if (var2.getLocalName().equals("tref")) {
            ((SVGOMElement)var2).setSVGContext(new TRefBridge(var1, this, var2));
         }
      }

      for(Node var3 = this.getFirstChild(var2); var3 != null; var3 = this.getNextSibling(var3)) {
         if (var3.getNodeType() == 1) {
            this.addContextToChild(var1, (Element)var3);
         }
      }

   }

   protected void removeContextFromChild(BridgeContext var1, Element var2) {
      if ("http://www.w3.org/2000/svg".equals(var2.getNamespaceURI())) {
         if (var2.getLocalName().equals("tspan")) {
            ((AbstractTextChildBridgeUpdateHandler)((SVGOMElement)var2).getSVGContext()).dispose();
         } else if (var2.getLocalName().equals("textPath")) {
            ((AbstractTextChildBridgeUpdateHandler)((SVGOMElement)var2).getSVGContext()).dispose();
         } else if (var2.getLocalName().equals("tref")) {
            ((AbstractTextChildBridgeUpdateHandler)((SVGOMElement)var2).getSVGContext()).dispose();
         }
      }

      for(Node var3 = this.getFirstChild(var2); var3 != null; var3 = this.getNextSibling(var3)) {
         if (var3.getNodeType() == 1) {
            this.removeContextFromChild(var1, (Element)var3);
         }
      }

   }

   public void handleDOMNodeInsertedEvent(MutationEvent var1) {
      Node var2 = (Node)var1.getTarget();
      switch (var2.getNodeType()) {
         case 1:
            Element var3 = (Element)var2;
            if (this.isTextChild(var3)) {
               this.addContextToChild(this.ctx, var3);
               this.laidoutText = null;
            }
         case 2:
         default:
            break;
         case 3:
         case 4:
            this.laidoutText = null;
      }

      if (this.laidoutText == null) {
         this.computeLaidoutText(this.ctx, this.e, this.getTextNode());
      }

   }

   public void handleDOMChildNodeRemovedEvent(MutationEvent var1) {
      Node var2 = (Node)var1.getTarget();
      switch (var2.getNodeType()) {
         case 1:
            Element var3 = (Element)var2;
            if (this.isTextChild(var3)) {
               this.laidoutText = null;
               this.removeContextFromChild(this.ctx, var3);
            }
         case 2:
         default:
            break;
         case 3:
         case 4:
            if (this.isParentDisplayed(var2)) {
               this.laidoutText = null;
            }
      }

   }

   public void handleDOMSubtreeModifiedEvent(MutationEvent var1) {
      if (this.laidoutText == null) {
         this.computeLaidoutText(this.ctx, this.e, this.getTextNode());
      }

   }

   public void handleDOMCharacterDataModified(MutationEvent var1) {
      Node var2 = (Node)var1.getTarget();
      if (this.isParentDisplayed(var2)) {
         this.laidoutText = null;
      }

   }

   protected boolean isParentDisplayed(Node var1) {
      Node var2 = this.getParentNode(var1);
      return this.isTextElement((Element)var2);
   }

   protected void computeLaidoutText(BridgeContext var1, Element var2, GraphicsNode var3) {
      TextNode var4 = (TextNode)var3;
      this.elemTPI.clear();
      AttributedString var5 = this.buildAttributedString(var1, var2);
      if (var5 == null) {
         var4.setAttributedCharacterIterator((AttributedCharacterIterator)null);
      } else {
         this.addGlyphPositionAttributes(var5, var2, var1);
         if (var1.isDynamic()) {
            this.laidoutText = new AttributedString(var5.getIterator());
         }

         var4.setAttributedCharacterIterator(var5.getIterator());
         TextPaintInfo var6 = new TextPaintInfo();
         this.setBaseTextPaintInfo(var6, var2, var3, var1);
         this.setDecorationTextPaintInfo(var6, var2);
         this.addPaintAttributes(var5, var2, var4, var6, var1);
         if (this.usingComplexSVGFont) {
            var4.setAttributedCharacterIterator(var5.getIterator());
         }

         if (var1.isDynamic()) {
            this.checkBBoxChange();
         }

      }
   }

   public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue var1) {
      if (var1.getNamespaceURI() == null) {
         String var2 = var1.getLocalName();
         if (var2.equals("x") || var2.equals("y") || var2.equals("dx") || var2.equals("dy") || var2.equals("rotate") || var2.equals("textLength") || var2.equals("lengthAdjust")) {
            char var3 = var2.charAt(0);
            if (var3 == 'x' || var3 == 'y') {
               this.getTextNode().setLocation(this.getLocation(this.ctx, this.e));
            }

            this.computeLaidoutText(this.ctx, this.e, this.getTextNode());
            return;
         }
      }

      super.handleAnimatedAttributeChanged(var1);
   }

   public void handleCSSEngineEvent(CSSEngineEvent var1) {
      this.hasNewACI = false;
      int[] var2 = var1.getProperties();
      int var3 = 0;

      while(var3 < var2.length) {
         switch (var2[var3]) {
            case 1:
            case 11:
            case 12:
            case 21:
            case 22:
            case 24:
            case 25:
            case 27:
            case 28:
            case 29:
            case 31:
            case 32:
            case 53:
            case 56:
            case 58:
            case 59:
               if (!this.hasNewACI) {
                  this.hasNewACI = true;
                  this.computeLaidoutText(this.ctx, this.e, this.getTextNode());
               }
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 23:
            case 26:
            case 30:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 54:
            case 55:
            case 57:
            default:
               ++var3;
         }
      }

      this.cssProceedElement = var1.getElement();
      super.handleCSSEngineEvent(var1);
      this.cssProceedElement = null;
   }

   protected void handleCSSPropertyChanged(int var1) {
      RenderingHints var2;
      switch (var1) {
         case 9:
            var2 = this.node.getRenderingHints();
            var2 = CSSUtilities.convertColorRendering(this.e, var2);
            if (var2 != null) {
               this.node.setRenderingHints(var2);
            }
            break;
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 53:
         case 56:
         default:
            super.handleCSSPropertyChanged(var1);
            break;
         case 15:
         case 16:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 54:
            this.rebuildACI();
            break;
         case 55:
            var2 = this.node.getRenderingHints();
            var2 = CSSUtilities.convertTextRendering(this.e, var2);
            if (var2 != null) {
               this.node.setRenderingHints(var2);
            }
            break;
         case 57:
            this.rebuildACI();
            super.handleCSSPropertyChanged(var1);
      }

   }

   protected void rebuildACI() {
      if (!this.hasNewACI) {
         TextNode var1 = this.getTextNode();
         if (var1.getAttributedCharacterIterator() != null) {
            TextPaintInfo var2;
            TextPaintInfo var3;
            if (this.cssProceedElement == this.e) {
               var2 = new TextPaintInfo();
               this.setBaseTextPaintInfo(var2, this.e, this.node, this.ctx);
               this.setDecorationTextPaintInfo(var2, this.e);
               var3 = (TextPaintInfo)this.elemTPI.get(this.e);
            } else {
               TextPaintInfo var4 = this.getParentTextPaintInfo(this.cssProceedElement);
               var2 = this.getTextPaintInfo(this.cssProceedElement, var1, var4, this.ctx);
               var3 = (TextPaintInfo)this.elemTPI.get(this.cssProceedElement);
            }

            if (var3 != null) {
               var1.swapTextPaintInfo(var2, var3);
               if (this.usingComplexSVGFont) {
                  var1.setAttributedCharacterIterator(var1.getAttributedCharacterIterator());
               }

            }
         }
      }
   }

   int getElementStartIndex(Element var1) {
      TextPaintInfo var2 = (TextPaintInfo)this.elemTPI.get(var1);
      return var2 == null ? -1 : var2.startChar;
   }

   int getElementEndIndex(Element var1) {
      TextPaintInfo var2 = (TextPaintInfo)this.elemTPI.get(var1);
      return var2 == null ? -1 : var2.endChar;
   }

   protected AttributedString buildAttributedString(BridgeContext var1, Element var2) {
      AttributedStringBuffer var3 = new AttributedStringBuffer();
      this.fillAttributedStringBuffer(var1, var2, true, (TextPath)null, (Integer)null, (Map)null, var3);
      return var3.toAttributedString();
   }

   protected void fillAttributedStringBuffer(BridgeContext var1, Element var2, boolean var3, TextPath var4, Integer var5, Map var6, AttributedStringBuffer var7) {
      if (SVGUtilities.matchUserAgent(var2, var1.getUserAgent()) && CSSUtilities.convertDisplay(var2)) {
         String var8 = XMLSupport.getXMLSpace(var2);
         boolean var9 = var8.equals("preserve");
         int var12 = var7.length();
         if (var3) {
            this.endLimit = 0;
         }

         if (var9) {
            this.endLimit = var7.length();
         }

         HashMap var13 = var6 == null ? new HashMap() : new HashMap(var6);
         var6 = this.getAttributeMap(var1, var2, var4, var5, var13);
         Object var14 = var13.get(TextAttribute.BIDI_EMBEDDING);
         Integer var15 = var5;
         if (var14 != null) {
            var15 = (Integer)var14;
         }

         for(Node var16 = this.getFirstChild(var2); var16 != null; var16 = this.getNextSibling(var16)) {
            boolean var10;
            if (var9) {
               var10 = false;
            } else if (var7.length() == 0) {
               var10 = true;
            } else {
               var10 = var7.getLastChar() == 32;
            }

            switch (var16.getNodeType()) {
               case 1:
                  if ("http://www.w3.org/2000/svg".equals(var16.getNamespaceURI())) {
                     Element var11 = (Element)var16;
                     String var17 = var16.getLocalName();
                     if (!var17.equals("tspan") && !var17.equals("altGlyph")) {
                        int var20;
                        if (var17.equals("textPath")) {
                           SVGTextPathElementBridge var28 = (SVGTextPathElementBridge)var1.getBridge(var11);
                           TextPath var19 = var28.createTextPath(var1, var11);
                           if (var19 != null) {
                              var20 = var7.count;
                              this.fillAttributedStringBuffer(var1, var11, false, var19, var15, var6, var7);
                              if (var7.count != var20) {
                                 var6 = null;
                              }
                           }
                        } else {
                           int var22;
                           if (var17.equals("tref")) {
                              String var29 = XLinkSupport.getXLinkHref((Element)var16);
                              Element var32 = var1.getReferencedElement((Element)var16, var29);
                              var8 = TextUtilities.getElementContent(var32);
                              var8 = this.normalizeString(var8, var9, var10);
                              if (var8.length() != 0) {
                                 var20 = var7.length();
                                 HashMap var21 = var6 == null ? new HashMap() : new HashMap(var6);
                                 this.getAttributeMap(var1, var11, var4, var5, var21);
                                 var7.append(var8, var21);
                                 var22 = var7.length() - 1;
                                 TextPaintInfo var23 = (TextPaintInfo)this.elemTPI.get(var11);
                                 var23.startChar = var20;
                                 var23.endChar = var22;
                                 var6 = null;
                              }
                           } else if (var17.equals("a")) {
                              NodeEventTarget var30 = (NodeEventTarget)var11;
                              UserAgent var33 = var1.getUserAgent();
                              SVGAElementBridge.CursorHolder var34 = new SVGAElementBridge.CursorHolder(CursorManager.DEFAULT_CURSOR);
                              SVGAElementBridge.AnchorListener var35 = new SVGAElementBridge.AnchorListener(var33, var34);
                              var30.addEventListenerNS("http://www.w3.org/2001/xml-events", "click", var35, false, (Object)null);
                              var1.storeEventListenerNS(var30, "http://www.w3.org/2001/xml-events", "click", var35, false);
                              var22 = var7.count;
                              this.fillAttributedStringBuffer(var1, var11, false, var4, var15, var6, var7);
                              if (var7.count != var22) {
                                 var6 = null;
                              }
                           }
                        }
                     } else {
                        int var18 = var7.count;
                        this.fillAttributedStringBuffer(var1, var11, false, var4, var15, var6, var7);
                        if (var7.count != var18) {
                           var6 = null;
                        }
                     }
                  }
               case 2:
               default:
                  break;
               case 3:
               case 4:
                  var8 = var16.getNodeValue();
                  var8 = this.normalizeString(var8, var9, var10);
                  if (var8.length() != 0) {
                     var7.append(var8, var13);
                     if (var9) {
                        this.endLimit = var7.length();
                     }

                     var6 = null;
                  }
            }
         }

         if (var3) {
            boolean var24;
            for(var24 = false; this.endLimit < var7.length() && var7.getLastChar() == 32; var24 = true) {
               var7.stripLast();
            }

            if (var24) {
               Iterator var26 = this.elemTPI.values().iterator();

               while(var26.hasNext()) {
                  TextPaintInfo var31 = (TextPaintInfo)var26.next();
                  if (var31.endChar >= var7.length()) {
                     var31.endChar = var7.length() - 1;
                     if (var31.startChar > var31.endChar) {
                        var31.startChar = var31.endChar;
                     }
                  }
               }
            }
         }

         int var25 = var7.length() - 1;
         TextPaintInfo var27 = (TextPaintInfo)this.elemTPI.get(var2);
         var27.startChar = var12;
         var27.endChar = var25;
      }
   }

   protected String normalizeString(String var1, boolean var2, boolean var3) {
      StringBuffer var4 = new StringBuffer(var1.length());
      int var5;
      if (var2) {
         for(var5 = 0; var5 < var1.length(); ++var5) {
            char var9 = var1.charAt(var5);
            switch (var9) {
               case '\t':
               case '\n':
               case '\r':
                  var4.append(' ');
                  break;
               case '\u000b':
               case '\f':
               default:
                  var4.append(var9);
            }
         }

         return var4.toString();
      } else {
         var5 = 0;
         if (var3) {
            label48:
            while(var5 < var1.length()) {
               switch (var1.charAt(var5)) {
                  case '\t':
                  case '\n':
                  case '\r':
                  case ' ':
                     ++var5;
                     break;
                  default:
                     break label48;
               }
            }
         }

         boolean var6 = false;

         for(int var7 = var5; var7 < var1.length(); ++var7) {
            char var8 = var1.charAt(var7);
            switch (var8) {
               case '\t':
               case ' ':
                  if (!var6) {
                     var4.append(' ');
                     var6 = true;
                  }
               case '\n':
               case '\r':
                  break;
               default:
                  var4.append(var8);
                  var6 = false;
            }
         }

         return var4.toString();
      }
   }

   protected boolean nodeAncestorOf(Node var1, Node var2) {
      if (var2 != null && var1 != null) {
         Node var3;
         for(var3 = this.getParentNode(var2); var3 != null && var3 != var1; var3 = this.getParentNode(var3)) {
         }

         return var3 == var1;
      } else {
         return false;
      }
   }

   protected void addGlyphPositionAttributes(AttributedString var1, Element var2, BridgeContext var3) {
      if (SVGUtilities.matchUserAgent(var2, var3.getUserAgent()) && CSSUtilities.convertDisplay(var2)) {
         if (var2.getLocalName().equals("textPath")) {
            this.addChildGlyphPositionAttributes(var1, var2, var3);
         } else {
            int var4 = this.getElementStartIndex(var2);
            if (var4 != -1) {
               int var5 = this.getElementEndIndex(var2);
               if (!(var2 instanceof SVGTextPositioningElement)) {
                  this.addChildGlyphPositionAttributes(var1, var2, var3);
               } else {
                  SVGTextPositioningElement var6 = (SVGTextPositioningElement)var2;

                  try {
                     SVGOMAnimatedLengthList var7 = (SVGOMAnimatedLengthList)var6.getX();
                     var7.check();
                     SVGOMAnimatedLengthList var8 = (SVGOMAnimatedLengthList)var6.getY();
                     var8.check();
                     SVGOMAnimatedLengthList var9 = (SVGOMAnimatedLengthList)var6.getDx();
                     var9.check();
                     SVGOMAnimatedLengthList var10 = (SVGOMAnimatedLengthList)var6.getDy();
                     var10.check();
                     SVGOMAnimatedNumberList var11 = (SVGOMAnimatedNumberList)var6.getRotate();
                     var11.check();
                     SVGLengthList var12 = var7.getAnimVal();
                     SVGLengthList var13 = var8.getAnimVal();
                     SVGLengthList var14 = var9.getAnimVal();
                     SVGLengthList var15 = var10.getAnimVal();
                     SVGNumberList var16 = var11.getAnimVal();
                     int var17 = var12.getNumberOfItems();

                     int var18;
                     for(var18 = 0; var18 < var17 && var4 + var18 <= var5; ++var18) {
                        var1.addAttribute(GVTAttributedCharacterIterator.TextAttribute.X, new Float(var12.getItem(var18).getValue()), var4 + var18, var4 + var18 + 1);
                     }

                     var17 = var13.getNumberOfItems();

                     for(var18 = 0; var18 < var17 && var4 + var18 <= var5; ++var18) {
                        var1.addAttribute(GVTAttributedCharacterIterator.TextAttribute.Y, new Float(var13.getItem(var18).getValue()), var4 + var18, var4 + var18 + 1);
                     }

                     var17 = var14.getNumberOfItems();

                     for(var18 = 0; var18 < var17 && var4 + var18 <= var5; ++var18) {
                        var1.addAttribute(GVTAttributedCharacterIterator.TextAttribute.DX, new Float(var14.getItem(var18).getValue()), var4 + var18, var4 + var18 + 1);
                     }

                     var17 = var15.getNumberOfItems();

                     for(var18 = 0; var18 < var17 && var4 + var18 <= var5; ++var18) {
                        var1.addAttribute(GVTAttributedCharacterIterator.TextAttribute.DY, new Float(var15.getItem(var18).getValue()), var4 + var18, var4 + var18 + 1);
                     }

                     var17 = var16.getNumberOfItems();
                     if (var17 == 1) {
                        Float var21 = new Float(Math.toRadians((double)var16.getItem(0).getValue()));
                        var1.addAttribute(GVTAttributedCharacterIterator.TextAttribute.ROTATION, var21, var4, var5 + 1);
                     } else if (var17 > 1) {
                        for(var18 = 0; var18 < var17 && var4 + var18 <= var5; ++var18) {
                           Float var19 = new Float(Math.toRadians((double)var16.getItem(var18).getValue()));
                           var1.addAttribute(GVTAttributedCharacterIterator.TextAttribute.ROTATION, var19, var4 + var18, var4 + var18 + 1);
                        }
                     }

                     this.addChildGlyphPositionAttributes(var1, var2, var3);
                  } catch (LiveAttributeException var20) {
                     throw new BridgeException(var3, var20);
                  }
               }
            }
         }
      }
   }

   protected void addChildGlyphPositionAttributes(AttributedString var1, Element var2, BridgeContext var3) {
      for(Node var4 = this.getFirstChild(var2); var4 != null; var4 = this.getNextSibling(var4)) {
         if (var4.getNodeType() == 1) {
            Element var5 = (Element)var4;
            if (this.isTextChild(var5)) {
               this.addGlyphPositionAttributes(var1, var5, var3);
            }
         }
      }

   }

   protected void addPaintAttributes(AttributedString var1, Element var2, TextNode var3, TextPaintInfo var4, BridgeContext var5) {
      if (SVGUtilities.matchUserAgent(var2, var5.getUserAgent()) && CSSUtilities.convertDisplay(var2)) {
         Object var6 = this.elemTPI.get(var2);
         if (var6 != null) {
            var3.swapTextPaintInfo(var4, (TextPaintInfo)var6);
         }

         this.addChildPaintAttributes(var1, var2, var3, var4, var5);
      }
   }

   protected void addChildPaintAttributes(AttributedString var1, Element var2, TextNode var3, TextPaintInfo var4, BridgeContext var5) {
      for(Node var6 = this.getFirstChild(var2); var6 != null; var6 = this.getNextSibling(var6)) {
         if (var6.getNodeType() == 1) {
            Element var7 = (Element)var6;
            if (this.isTextChild(var7)) {
               TextPaintInfo var8 = this.getTextPaintInfo(var7, var3, var4, var5);
               this.addPaintAttributes(var1, var7, var3, var8, var5);
            }
         }
      }

   }

   protected List getFontList(BridgeContext var1, Element var2, Map var3) {
      var3.put(TEXT_COMPOUND_ID, new SoftReference(var2));
      Float var4 = TextUtilities.convertFontSize(var2);
      float var5 = var4;
      var3.put(TextAttribute.SIZE, var4);
      var3.put(TextAttribute.WIDTH, TextUtilities.convertFontStretch(var2));
      var3.put(TextAttribute.POSTURE, TextUtilities.convertFontStyle(var2));
      var3.put(TextAttribute.WEIGHT, TextUtilities.convertFontWeight(var2));
      Value var6 = CSSUtilities.getComputedStyle(var2, 27);
      String var7 = var6.getCssText();
      String var8 = CSSUtilities.getComputedStyle(var2, 25).getStringValue();
      var3.put(TEXT_COMPOUND_DELIMITER, var2);
      Value var9 = CSSUtilities.getComputedStyle(var2, 21);
      ArrayList var10 = new ArrayList();
      ArrayList var11 = new ArrayList();
      int var12 = var9.getLength();

      for(int var13 = 0; var13 < var12; ++var13) {
         Value var14 = var9.item(var13);
         String var15 = var14.getStringValue();
         GVTFontFamily var16 = SVGFontUtilities.getFontFamily(var2, var1, var15, var7, var8);
         if (var16 != null) {
            if (var16 instanceof UnresolvedFontFamily) {
               var16 = FontFamilyResolver.resolve((UnresolvedFontFamily)var16);
               if (var16 == null) {
                  continue;
               }
            }

            var10.add(var16);
            if (var16 instanceof SVGFontFamily) {
               SVGFontFamily var17 = (SVGFontFamily)var16;
               if (var17.isComplex()) {
                  this.usingComplexSVGFont = true;
               }
            }

            GVTFont var18 = var16.deriveFont(var5, var3);
            var11.add(var18);
         }
      }

      var3.put(GVT_FONT_FAMILIES, var10);
      if (!var1.isDynamic()) {
         var3.remove(TEXT_COMPOUND_DELIMITER);
      }

      return var11;
   }

   protected Map getAttributeMap(BridgeContext var1, Element var2, TextPath var3, Integer var4, Map var5) {
      SVGTextContentElement var6 = null;
      if (var2 instanceof SVGTextContentElement) {
         var6 = (SVGTextContentElement)var2;
      }

      HashMap var7 = null;
      if ("http://www.w3.org/2000/svg".equals(var2.getNamespaceURI()) && var2.getLocalName().equals("altGlyph")) {
         var5.put(ALT_GLYPH_HANDLER, new SVGAltGlyphHandler(var1, var2));
      }

      TextPaintInfo var9 = new TextPaintInfo();
      var9.visible = true;
      var9.fillPaint = Color.black;
      var5.put(PAINT_INFO, var9);
      this.elemTPI.put(var2, var9);
      if (var3 != null) {
         var5.put(TEXTPATH, var3);
      }

      TextNode.Anchor var10 = TextUtilities.convertTextAnchor(var2);
      var5.put(ANCHOR_TYPE, var10);
      List var11 = this.getFontList(var1, var2, var5);
      var5.put(GVT_FONTS, var11);
      Object var12 = TextUtilities.convertBaselineShift(var2);
      if (var12 != null) {
         var5.put(BASELINE_SHIFT, var12);
      }

      Value var13 = CSSUtilities.getComputedStyle(var2, 56);
      String var8 = var13.getStringValue();
      if (var8.charAt(0) == 'n') {
         if (var4 != null) {
            var5.put(TextAttribute.BIDI_EMBEDDING, var4);
         }
      } else {
         var13 = CSSUtilities.getComputedStyle(var2, 11);
         String var14 = var13.getStringValue();
         int var15 = 0;
         if (var4 != null) {
            var15 = var4;
         }

         if (var15 < 0) {
            var15 = -var15;
         }

         switch (var14.charAt(0)) {
            case 'l':
               var5.put(TextAttribute.RUN_DIRECTION, TextAttribute.RUN_DIRECTION_LTR);
               if ((var15 & 1) == 1) {
                  ++var15;
               } else {
                  var15 += 2;
               }
               break;
            case 'r':
               var5.put(TextAttribute.RUN_DIRECTION, TextAttribute.RUN_DIRECTION_RTL);
               if ((var15 & 1) == 1) {
                  var15 += 2;
               } else {
                  ++var15;
               }
         }

         switch (var8.charAt(0)) {
            case 'b':
               var15 = -var15;
            default:
               var5.put(TextAttribute.BIDI_EMBEDDING, new Integer(var15));
         }
      }

      var13 = CSSUtilities.getComputedStyle(var2, 59);
      var8 = var13.getStringValue();
      switch (var8.charAt(0)) {
         case 'l':
            var5.put(GVTAttributedCharacterIterator.TextAttribute.WRITING_MODE, GVTAttributedCharacterIterator.TextAttribute.WRITING_MODE_LTR);
            break;
         case 'r':
            var5.put(GVTAttributedCharacterIterator.TextAttribute.WRITING_MODE, GVTAttributedCharacterIterator.TextAttribute.WRITING_MODE_RTL);
            break;
         case 't':
            var5.put(GVTAttributedCharacterIterator.TextAttribute.WRITING_MODE, GVTAttributedCharacterIterator.TextAttribute.WRITING_MODE_TTB);
      }

      var13 = CSSUtilities.getComputedStyle(var2, 29);
      short var20 = var13.getPrimitiveType();
      switch (var20) {
         case 11:
            var5.put(GVTAttributedCharacterIterator.TextAttribute.VERTICAL_ORIENTATION, GVTAttributedCharacterIterator.TextAttribute.ORIENTATION_ANGLE);
            var5.put(GVTAttributedCharacterIterator.TextAttribute.VERTICAL_ORIENTATION_ANGLE, new Float(var13.getFloatValue()));
            break;
         case 12:
            var5.put(GVTAttributedCharacterIterator.TextAttribute.VERTICAL_ORIENTATION, GVTAttributedCharacterIterator.TextAttribute.ORIENTATION_ANGLE);
            var5.put(GVTAttributedCharacterIterator.TextAttribute.VERTICAL_ORIENTATION_ANGLE, new Float(Math.toDegrees((double)var13.getFloatValue())));
            break;
         case 13:
            var5.put(GVTAttributedCharacterIterator.TextAttribute.VERTICAL_ORIENTATION, GVTAttributedCharacterIterator.TextAttribute.ORIENTATION_ANGLE);
            var5.put(GVTAttributedCharacterIterator.TextAttribute.VERTICAL_ORIENTATION_ANGLE, new Float(var13.getFloatValue() * 9.0F / 5.0F));
            break;
         case 21:
            var5.put(GVTAttributedCharacterIterator.TextAttribute.VERTICAL_ORIENTATION, GVTAttributedCharacterIterator.TextAttribute.ORIENTATION_AUTO);
            break;
         default:
            throw new IllegalStateException("unexpected primitiveType (V):" + var20);
      }

      var13 = CSSUtilities.getComputedStyle(var2, 28);
      var20 = var13.getPrimitiveType();
      switch (var20) {
         case 11:
            var5.put(GVTAttributedCharacterIterator.TextAttribute.HORIZONTAL_ORIENTATION_ANGLE, new Float(var13.getFloatValue()));
            break;
         case 12:
            var5.put(GVTAttributedCharacterIterator.TextAttribute.HORIZONTAL_ORIENTATION_ANGLE, new Float(Math.toDegrees((double)var13.getFloatValue())));
            break;
         case 13:
            var5.put(GVTAttributedCharacterIterator.TextAttribute.HORIZONTAL_ORIENTATION_ANGLE, new Float(var13.getFloatValue() * 9.0F / 5.0F));
            break;
         default:
            throw new IllegalStateException("unexpected primitiveType (H):" + var20);
      }

      Float var21 = TextUtilities.convertLetterSpacing(var2);
      if (var21 != null) {
         var5.put(GVTAttributedCharacterIterator.TextAttribute.LETTER_SPACING, var21);
         var5.put(GVTAttributedCharacterIterator.TextAttribute.CUSTOM_SPACING, Boolean.TRUE);
      }

      var21 = TextUtilities.convertWordSpacing(var2);
      if (var21 != null) {
         var5.put(GVTAttributedCharacterIterator.TextAttribute.WORD_SPACING, var21);
         var5.put(GVTAttributedCharacterIterator.TextAttribute.CUSTOM_SPACING, Boolean.TRUE);
      }

      var21 = TextUtilities.convertKerning(var2);
      if (var21 != null) {
         var5.put(GVTAttributedCharacterIterator.TextAttribute.KERNING, var21);
         var5.put(GVTAttributedCharacterIterator.TextAttribute.CUSTOM_SPACING, Boolean.TRUE);
      }

      if (var6 == null) {
         return var7;
      } else {
         try {
            AbstractSVGAnimatedLength var16 = (AbstractSVGAnimatedLength)var6.getTextLength();
            if (var16.isSpecified()) {
               if (var7 == null) {
                  var7 = new HashMap();
               }

               Float var17 = new Float(var16.getCheckedValue());
               var5.put(GVTAttributedCharacterIterator.TextAttribute.BBOX_WIDTH, var17);
               var7.put(GVTAttributedCharacterIterator.TextAttribute.BBOX_WIDTH, var17);
               SVGOMAnimatedEnumeration var18 = (SVGOMAnimatedEnumeration)var6.getLengthAdjust();
               if (var18.getCheckedVal() == 2) {
                  var5.put(GVTAttributedCharacterIterator.TextAttribute.LENGTH_ADJUST, GVTAttributedCharacterIterator.TextAttribute.ADJUST_ALL);
                  var7.put(GVTAttributedCharacterIterator.TextAttribute.LENGTH_ADJUST, GVTAttributedCharacterIterator.TextAttribute.ADJUST_ALL);
               } else {
                  var5.put(GVTAttributedCharacterIterator.TextAttribute.LENGTH_ADJUST, GVTAttributedCharacterIterator.TextAttribute.ADJUST_SPACING);
                  var7.put(GVTAttributedCharacterIterator.TextAttribute.LENGTH_ADJUST, GVTAttributedCharacterIterator.TextAttribute.ADJUST_SPACING);
                  var5.put(GVTAttributedCharacterIterator.TextAttribute.CUSTOM_SPACING, Boolean.TRUE);
                  var7.put(GVTAttributedCharacterIterator.TextAttribute.CUSTOM_SPACING, Boolean.TRUE);
               }
            }

            return var7;
         } catch (LiveAttributeException var19) {
            throw new BridgeException(var1, var19);
         }
      }
   }

   protected TextPaintInfo getParentTextPaintInfo(Element var1) {
      for(Node var2 = this.getParentNode(var1); var2 != null; var2 = this.getParentNode(var2)) {
         TextPaintInfo var3 = (TextPaintInfo)this.elemTPI.get(var2);
         if (var3 != null) {
            return var3;
         }
      }

      return null;
   }

   protected TextPaintInfo getTextPaintInfo(Element var1, GraphicsNode var2, TextPaintInfo var3, BridgeContext var4) {
      CSSUtilities.getComputedStyle(var1, 54);
      TextPaintInfo var5 = new TextPaintInfo(var3);
      StyleMap var6 = ((CSSStylableElement)var1).getComputedStyleMap((String)null);
      if (var6.isNullCascaded(54) && var6.isNullCascaded(15) && var6.isNullCascaded(45) && var6.isNullCascaded(52) && var6.isNullCascaded(38)) {
         return var5;
      } else {
         this.setBaseTextPaintInfo(var5, var1, var2, var4);
         if (!var6.isNullCascaded(54)) {
            this.setDecorationTextPaintInfo(var5, var1);
         }

         return var5;
      }
   }

   public void setBaseTextPaintInfo(TextPaintInfo var1, Element var2, GraphicsNode var3, BridgeContext var4) {
      if (!var2.getLocalName().equals("text")) {
         var1.composite = CSSUtilities.convertOpacity(var2);
      } else {
         var1.composite = AlphaComposite.SrcOver;
      }

      var1.visible = CSSUtilities.convertVisibility(var2);
      var1.fillPaint = PaintServer.convertFillPaint(var2, var3, var4);
      var1.strokePaint = PaintServer.convertStrokePaint(var2, var3, var4);
      var1.strokeStroke = PaintServer.convertStroke(var2);
   }

   public void setDecorationTextPaintInfo(TextPaintInfo var1, Element var2) {
      Value var3 = CSSUtilities.getComputedStyle(var2, 54);
      switch (var3.getCssValueType()) {
         case 2:
            ListValue var4 = (ListValue)var3;
            int var5 = var4.getLength();

            for(int var6 = 0; var6 < var5; ++var6) {
               Value var7 = var4.item(var6);
               String var8 = var7.getStringValue();
               switch (var8.charAt(0)) {
                  case 'l':
                     if (var1.fillPaint != null) {
                        var1.strikethroughPaint = var1.fillPaint;
                     }

                     if (var1.strokePaint != null) {
                        var1.strikethroughStrokePaint = var1.strokePaint;
                     }

                     if (var1.strokeStroke != null) {
                        var1.strikethroughStroke = var1.strokeStroke;
                     }
                     break;
                  case 'o':
                     if (var1.fillPaint != null) {
                        var1.overlinePaint = var1.fillPaint;
                     }

                     if (var1.strokePaint != null) {
                        var1.overlineStrokePaint = var1.strokePaint;
                     }

                     if (var1.strokeStroke != null) {
                        var1.overlineStroke = var1.strokeStroke;
                     }
                     break;
                  case 'u':
                     if (var1.fillPaint != null) {
                        var1.underlinePaint = var1.fillPaint;
                     }

                     if (var1.strokePaint != null) {
                        var1.underlineStrokePaint = var1.strokePaint;
                     }

                     if (var1.strokeStroke != null) {
                        var1.underlineStroke = var1.strokeStroke;
                     }
               }
            }

            return;
         default:
            var1.underlinePaint = null;
            var1.underlineStrokePaint = null;
            var1.underlineStroke = null;
            var1.overlinePaint = null;
            var1.overlineStrokePaint = null;
            var1.overlineStroke = null;
            var1.strikethroughPaint = null;
            var1.strikethroughStrokePaint = null;
            var1.strikethroughStroke = null;
      }
   }

   public int getNumberOfChars() {
      return this.getNumberOfChars(this.e);
   }

   public Rectangle2D getExtentOfChar(int var1) {
      return this.getExtentOfChar(this.e, var1);
   }

   public Point2D getStartPositionOfChar(int var1) {
      return this.getStartPositionOfChar(this.e, var1);
   }

   public Point2D getEndPositionOfChar(int var1) {
      return this.getEndPositionOfChar(this.e, var1);
   }

   public void selectSubString(int var1, int var2) {
      this.selectSubString(this.e, var1, var2);
   }

   public float getRotationOfChar(int var1) {
      return this.getRotationOfChar(this.e, var1);
   }

   public float getComputedTextLength() {
      return this.getComputedTextLength(this.e);
   }

   public float getSubStringLength(int var1, int var2) {
      return this.getSubStringLength(this.e, var1, var2);
   }

   public int getCharNumAtPosition(float var1, float var2) {
      return this.getCharNumAtPosition(this.e, var1, var2);
   }

   protected int getNumberOfChars(Element var1) {
      AttributedCharacterIterator var2 = this.getTextNode().getAttributedCharacterIterator();
      if (var2 == null) {
         return 0;
      } else {
         int var3 = this.getElementStartIndex(var1);
         if (var3 == -1) {
            return 0;
         } else {
            int var4 = this.getElementEndIndex(var1);
            return var4 - var3 + 1;
         }
      }
   }

   protected Rectangle2D getExtentOfChar(Element var1, int var2) {
      TextNode var3 = this.getTextNode();
      AttributedCharacterIterator var4 = var3.getAttributedCharacterIterator();
      if (var4 == null) {
         return null;
      } else {
         int var5 = this.getElementStartIndex(var1);
         if (var5 == -1) {
            return null;
         } else {
            List var6 = this.getTextRuns(var3);
            CharacterInformation var7 = this.getCharacterInformation(var6, var5, var2, var4);
            if (var7 == null) {
               return null;
            } else {
               GVTGlyphVector var8 = var7.layout.getGlyphVector();
               Object var9 = null;
               if (var7.glyphIndexStart == var7.glyphIndexEnd) {
                  if (var8.isGlyphVisible(var7.glyphIndexStart)) {
                     var9 = var8.getGlyphCellBounds(var7.glyphIndexStart);
                  }
               } else {
                  GeneralPath var10 = null;

                  for(int var11 = var7.glyphIndexStart; var11 <= var7.glyphIndexEnd; ++var11) {
                     if (var8.isGlyphVisible(var11)) {
                        Rectangle2D var12 = var8.getGlyphCellBounds(var11);
                        if (var10 == null) {
                           var10 = new GeneralPath(var12);
                        } else {
                           var10.append(var12, false);
                        }
                     }
                  }

                  var9 = var10;
               }

               return var9 == null ? null : ((Shape)var9).getBounds2D();
            }
         }
      }
   }

   protected Point2D getStartPositionOfChar(Element var1, int var2) {
      TextNode var3 = this.getTextNode();
      AttributedCharacterIterator var4 = var3.getAttributedCharacterIterator();
      if (var4 == null) {
         return null;
      } else {
         int var5 = this.getElementStartIndex(var1);
         if (var5 == -1) {
            return null;
         } else {
            List var6 = this.getTextRuns(var3);
            CharacterInformation var7 = this.getCharacterInformation(var6, var5, var2, var4);
            return var7 == null ? null : this.getStartPoint(var7);
         }
      }
   }

   protected Point2D getStartPoint(CharacterInformation var1) {
      GVTGlyphVector var2 = var1.layout.getGlyphVector();
      if (!var2.isGlyphVisible(var1.glyphIndexStart)) {
         return null;
      } else {
         Point2D var3 = var2.getGlyphPosition(var1.glyphIndexStart);
         AffineTransform var4 = var2.getGlyphTransform(var1.glyphIndexStart);
         Point2D.Float var5 = new Point2D.Float(0.0F, 0.0F);
         if (var4 != null) {
            var4.transform(var5, var5);
         }

         var5.x = (float)((double)var5.x + var3.getX());
         var5.y = (float)((double)var5.y + var3.getY());
         return var5;
      }
   }

   protected Point2D getEndPositionOfChar(Element var1, int var2) {
      TextNode var3 = this.getTextNode();
      AttributedCharacterIterator var4 = var3.getAttributedCharacterIterator();
      if (var4 == null) {
         return null;
      } else {
         int var5 = this.getElementStartIndex(var1);
         if (var5 == -1) {
            return null;
         } else {
            List var6 = this.getTextRuns(var3);
            CharacterInformation var7 = this.getCharacterInformation(var6, var5, var2, var4);
            return var7 == null ? null : this.getEndPoint(var7);
         }
      }
   }

   protected Point2D getEndPoint(CharacterInformation var1) {
      GVTGlyphVector var2 = var1.layout.getGlyphVector();
      if (!var2.isGlyphVisible(var1.glyphIndexEnd)) {
         return null;
      } else {
         Point2D var3 = var2.getGlyphPosition(var1.glyphIndexEnd);
         AffineTransform var4 = var2.getGlyphTransform(var1.glyphIndexEnd);
         GVTGlyphMetrics var5 = var2.getGlyphMetrics(var1.glyphIndexEnd);
         Point2D.Float var6 = new Point2D.Float(var5.getHorizontalAdvance(), 0.0F);
         if (var4 != null) {
            var4.transform(var6, var6);
         }

         var6.x = (float)((double)var6.x + var3.getX());
         var6.y = (float)((double)var6.y + var3.getY());
         return var6;
      }
   }

   protected float getRotationOfChar(Element var1, int var2) {
      TextNode var3 = this.getTextNode();
      AttributedCharacterIterator var4 = var3.getAttributedCharacterIterator();
      if (var4 == null) {
         return 0.0F;
      } else {
         int var5 = this.getElementStartIndex(var1);
         if (var5 == -1) {
            return 0.0F;
         } else {
            List var6 = this.getTextRuns(var3);
            CharacterInformation var7 = this.getCharacterInformation(var6, var5, var2, var4);
            double var8 = 0.0;
            int var10 = 0;
            if (var7 != null) {
               GVTGlyphVector var11 = var7.layout.getGlyphVector();

               for(int var12 = var7.glyphIndexStart; var12 <= var7.glyphIndexEnd; ++var12) {
                  if (var11.isGlyphVisible(var12)) {
                     ++var10;
                     AffineTransform var13 = var11.getGlyphTransform(var12);
                     if (var13 != null) {
                        double var14 = 0.0;
                        double var16 = var13.getScaleX();
                        double var18 = var13.getShearX();
                        if (var16 == 0.0) {
                           if (var18 > 0.0) {
                              var14 = Math.PI;
                           } else {
                              var14 = -3.141592653589793;
                           }
                        } else {
                           var14 = Math.atan(var18 / var16);
                           if (var16 < 0.0) {
                              var14 += Math.PI;
                           }
                        }

                        var14 = Math.toDegrees(-var14) % 360.0;
                        var8 += var14 - var7.getComputedOrientationAngle();
                     }
                  }
               }
            }

            return var10 == 0 ? 0.0F : (float)(var8 / (double)var10);
         }
      }
   }

   protected float getComputedTextLength(Element var1) {
      return this.getSubStringLength(var1, 0, this.getNumberOfChars(var1));
   }

   protected float getSubStringLength(Element var1, int var2, int var3) {
      if (var3 == 0) {
         return 0.0F;
      } else {
         float var4 = 0.0F;
         TextNode var5 = this.getTextNode();
         AttributedCharacterIterator var6 = var5.getAttributedCharacterIterator();
         if (var6 == null) {
            return -1.0F;
         } else {
            int var7 = this.getElementStartIndex(var1);
            if (var7 == -1) {
               return -1.0F;
            } else {
               List var8 = this.getTextRuns(var5);
               CharacterInformation var9 = this.getCharacterInformation(var8, var7, var2, var6);
               CharacterInformation var10 = null;
               int var11 = var9.characterIndex + 1;
               GVTGlyphVector var12 = var9.layout.getGlyphVector();
               float[] var13 = var9.layout.getGlyphAdvances();
               boolean[] var14 = new boolean[var13.length];

               int var15;
               for(var15 = var2 + 1; var15 < var2 + var3; ++var15) {
                  if (!var9.layout.isOnATextPath()) {
                     if (var9.layout.hasCharacterIndex(var11)) {
                        ++var11;
                     } else {
                        var10 = this.getCharacterInformation(var8, var7, var15 - 1, var6);
                        var4 += this.distanceFirstLastCharacterInRun(var9, var10);
                        var9 = this.getCharacterInformation(var8, var7, var15, var6);
                        var11 = var9.characterIndex + 1;
                        var12 = var9.layout.getGlyphVector();
                        var13 = var9.layout.getGlyphAdvances();
                        var14 = new boolean[var13.length];
                        var10 = null;
                     }
                  } else {
                     for(int var16 = var9.glyphIndexStart; var16 <= var9.glyphIndexEnd; ++var16) {
                        if (var12.isGlyphVisible(var16) && !var14[var16]) {
                           var4 += var13[var16 + 1] - var13[var16];
                        }

                        var14[var16] = true;
                     }

                     CharacterInformation var17 = this.getCharacterInformation(var8, var7, var15, var6);
                     if (var17.layout != var9.layout) {
                        var12 = var17.layout.getGlyphVector();
                        var13 = var17.layout.getGlyphAdvances();
                        var14 = new boolean[var13.length];
                        var11 = var9.characterIndex + 1;
                     }

                     var9 = var17;
                  }
               }

               if (var9.layout.isOnATextPath()) {
                  for(var15 = var9.glyphIndexStart; var15 <= var9.glyphIndexEnd; ++var15) {
                     if (var12.isGlyphVisible(var15) && !var14[var15]) {
                        var4 += var13[var15 + 1] - var13[var15];
                     }

                     var14[var15] = true;
                  }
               } else {
                  if (var10 == null) {
                     var10 = this.getCharacterInformation(var8, var7, var2 + var3 - 1, var6);
                  }

                  var4 += this.distanceFirstLastCharacterInRun(var9, var10);
               }

               return var4;
            }
         }
      }
   }

   protected float distanceFirstLastCharacterInRun(CharacterInformation var1, CharacterInformation var2) {
      float[] var3 = var1.layout.getGlyphAdvances();
      int var4 = var1.glyphIndexStart;
      int var5 = var1.glyphIndexEnd;
      int var6 = var2.glyphIndexStart;
      int var7 = var2.glyphIndexEnd;
      int var8 = var4 < var6 ? var4 : var6;
      int var9 = var5 < var7 ? var7 : var5;
      return var3[var9 + 1] - var3[var8];
   }

   protected float distanceBetweenRun(CharacterInformation var1, CharacterInformation var2) {
      CharacterInformation var6 = new CharacterInformation();
      var6.layout = var1.layout;
      var6.glyphIndexEnd = var1.layout.getGlyphCount() - 1;
      Point2D var4 = this.getEndPoint(var6);
      var6.layout = var2.layout;
      var6.glyphIndexStart = 0;
      Point2D var5 = this.getStartPoint(var6);
      float var3;
      if (var2.isVertical()) {
         var3 = (float)(var5.getY() - var4.getY());
      } else {
         var3 = (float)(var5.getX() - var4.getX());
      }

      return var3;
   }

   protected void selectSubString(Element var1, int var2, int var3) {
      TextNode var4 = this.getTextNode();
      AttributedCharacterIterator var5 = var4.getAttributedCharacterIterator();
      if (var5 != null) {
         int var6 = this.getElementStartIndex(var1);
         if (var6 != -1) {
            List var7 = this.getTextRuns(var4);
            int var8 = this.getElementEndIndex(var1);
            CharacterInformation var9 = this.getCharacterInformation(var7, var6, var2, var5);
            CharacterInformation var10 = this.getCharacterInformation(var7, var6, var2 + var3 - 1, var5);
            Mark var11 = var4.getMarkerForChar(var9.characterIndex, true);
            Mark var12;
            if (var10 != null && var10.characterIndex <= var8) {
               var12 = var4.getMarkerForChar(var10.characterIndex, false);
            } else {
               var12 = var4.getMarkerForChar(var8, false);
            }

            this.ctx.getUserAgent().setTextSelection(var11, var12);
         }
      }
   }

   protected int getCharNumAtPosition(Element var1, float var2, float var3) {
      TextNode var4 = this.getTextNode();
      AttributedCharacterIterator var5 = var4.getAttributedCharacterIterator();
      if (var5 == null) {
         return -1;
      } else {
         List var6 = this.getTextRuns(var4);
         TextHit var7 = null;

         int var8;
         for(var8 = var6.size() - 1; var8 >= 0 && var7 == null; --var8) {
            StrokingTextPainter.TextRun var9 = (StrokingTextPainter.TextRun)var6.get(var8);
            var7 = var9.getLayout().hitTestChar(var2, var3);
         }

         if (var7 == null) {
            return -1;
         } else {
            var8 = this.getElementStartIndex(var1);
            int var11 = this.getElementEndIndex(var1);
            int var10 = var7.getCharIndex();
            return var10 >= var8 && var10 <= var11 ? var10 - var8 : -1;
         }
      }
   }

   protected List getTextRuns(TextNode var1) {
      if (var1.getTextRuns() == null) {
         var1.getPrimitiveBounds();
      }

      return var1.getTextRuns();
   }

   protected CharacterInformation getCharacterInformation(List var1, int var2, int var3, AttributedCharacterIterator var4) {
      CharacterInformation var5 = new CharacterInformation();
      var5.characterIndex = var2 + var3;

      for(int var6 = 0; var6 < var1.size(); ++var6) {
         StrokingTextPainter.TextRun var7 = (StrokingTextPainter.TextRun)var1.get(var6);
         if (var7.getLayout().hasCharacterIndex(var5.characterIndex)) {
            var5.layout = var7.getLayout();
            var4.setIndex(var5.characterIndex);
            if (var4.getAttribute(ALT_GLYPH_HANDLER) != null) {
               var5.glyphIndexStart = 0;
               var5.glyphIndexEnd = var5.layout.getGlyphCount() - 1;
            } else {
               var5.glyphIndexStart = var5.layout.getGlyphIndex(var5.characterIndex);
               if (var5.glyphIndexStart == -1) {
                  var5.glyphIndexStart = 0;
                  var5.glyphIndexEnd = var5.layout.getGlyphCount() - 1;
               } else {
                  var5.glyphIndexEnd = var5.glyphIndexStart;
               }
            }

            return var5;
         }
      }

      return null;
   }

   public Set getTextIntersectionSet(AffineTransform var1, Rectangle2D var2) {
      HashSet var3 = new HashSet();
      TextNode var4 = this.getTextNode();
      List var5 = var4.getTextRuns();
      if (var5 == null) {
         return var3;
      } else {
         for(int var6 = 0; var6 < var5.size(); ++var6) {
            StrokingTextPainter.TextRun var7 = (StrokingTextPainter.TextRun)var5.get(var6);
            TextSpanLayout var8 = var7.getLayout();
            AttributedCharacterIterator var9 = var7.getACI();
            var9.first();
            SoftReference var10 = (SoftReference)var9.getAttribute(TEXT_COMPOUND_ID);
            Element var11 = (Element)var10.get();
            if (var11 != null && !var3.contains(var11) && isTextSensitive(var11)) {
               Rectangle2D var12 = var8.getBounds2D();
               if (var12 != null) {
                  var12 = var1.createTransformedShape(var12).getBounds2D();
                  if (!var2.intersects(var12)) {
                     continue;
                  }
               }

               GVTGlyphVector var13 = var8.getGlyphVector();

               for(int var14 = 0; var14 < var13.getNumGlyphs(); ++var14) {
                  Shape var15 = var13.getGlyphLogicalBounds(var14);
                  if (var15 != null) {
                     Rectangle2D var16 = var1.createTransformedShape(var15).getBounds2D();
                     if (var16.intersects(var2)) {
                        var3.add(var11);
                        break;
                     }
                  }
               }
            }
         }

         return var3;
      }
   }

   public Set getTextEnclosureSet(AffineTransform var1, Rectangle2D var2) {
      TextNode var3 = this.getTextNode();
      HashSet var4 = new HashSet();
      List var5 = var3.getTextRuns();
      if (var5 == null) {
         return var4;
      } else {
         HashSet var6 = new HashSet();

         for(int var7 = 0; var7 < var5.size(); ++var7) {
            StrokingTextPainter.TextRun var8 = (StrokingTextPainter.TextRun)var5.get(var7);
            TextSpanLayout var9 = var8.getLayout();
            AttributedCharacterIterator var10 = var8.getACI();
            var10.first();
            SoftReference var11 = (SoftReference)var10.getAttribute(TEXT_COMPOUND_ID);
            Element var12 = (Element)var11.get();
            if (var12 != null && !var6.contains(var12)) {
               if (!isTextSensitive(var12)) {
                  var6.add(var12);
               } else {
                  Rectangle2D var13 = var9.getBounds2D();
                  if (var13 != null) {
                     var13 = var1.createTransformedShape(var13).getBounds2D();
                     if (var2.contains(var13)) {
                        var4.add(var12);
                     } else {
                        var6.add(var12);
                        var4.remove(var12);
                     }
                  }
               }
            }
         }

         return var4;
      }
   }

   public static boolean getTextIntersection(BridgeContext var0, Element var1, AffineTransform var2, Rectangle2D var3, boolean var4) {
      SVGContext var5 = null;
      if (var1 instanceof SVGOMElement) {
         var5 = ((SVGOMElement)var1).getSVGContext();
      }

      if (var5 == null) {
         return false;
      } else {
         SVGTextElementBridge var6 = null;
         if (var5 instanceof SVGTextElementBridge) {
            var6 = (SVGTextElementBridge)var5;
         } else if (var5 instanceof AbstractTextChildSVGContext) {
            AbstractTextChildSVGContext var7 = (AbstractTextChildSVGContext)var5;
            var6 = var7.getTextBridge();
         }

         if (var6 == null) {
            return false;
         } else {
            TextNode var24 = var6.getTextNode();
            List var8 = var24.getTextRuns();
            if (var8 == null) {
               return false;
            } else {
               Element var9 = var6.e;
               AffineTransform var10 = var24.getGlobalTransform();
               var10.preConcatenate(var2);
               Rectangle2D var11 = var24.getBounds();
               var11 = var10.createTransformedShape(var11).getBounds2D();
               if (!var3.intersects(var11)) {
                  return false;
               } else {
                  for(int var12 = 0; var12 < var8.size(); ++var12) {
                     StrokingTextPainter.TextRun var13 = (StrokingTextPainter.TextRun)var8.get(var12);
                     TextSpanLayout var14 = var13.getLayout();
                     AttributedCharacterIterator var15 = var13.getACI();
                     var15.first();
                     SoftReference var16 = (SoftReference)var15.getAttribute(TEXT_COMPOUND_ID);
                     Element var17 = (Element)var16.get();
                     if (var17 != null && (!var4 || isTextSensitive(var17))) {
                        Element var18;
                        for(var18 = var17; var18 != null && var18 != var9 && var18 != var1; var18 = (Element)var6.getParentNode(var18)) {
                        }

                        if (var18 == var1) {
                           Rectangle2D var19 = var14.getBounds2D();
                           if (var19 != null) {
                              var19 = var10.createTransformedShape(var19).getBounds2D();
                              if (var3.intersects(var19)) {
                                 GVTGlyphVector var20 = var14.getGlyphVector();

                                 for(int var21 = 0; var21 < var20.getNumGlyphs(); ++var21) {
                                    Shape var22 = var20.getGlyphLogicalBounds(var21);
                                    if (var22 != null) {
                                       Rectangle2D var23 = var10.createTransformedShape(var22).getBounds2D();
                                       if (var23.intersects(var3)) {
                                          return true;
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }

                  return false;
               }
            }
         }
      }
   }

   public static Rectangle2D getTextBounds(BridgeContext var0, Element var1, boolean var2) {
      SVGContext var3 = null;
      if (var1 instanceof SVGOMElement) {
         var3 = ((SVGOMElement)var1).getSVGContext();
      }

      if (var3 == null) {
         return null;
      } else {
         SVGTextElementBridge var4 = null;
         if (var3 instanceof SVGTextElementBridge) {
            var4 = (SVGTextElementBridge)var3;
         } else if (var3 instanceof AbstractTextChildSVGContext) {
            AbstractTextChildSVGContext var5 = (AbstractTextChildSVGContext)var3;
            var4 = var5.getTextBridge();
         }

         if (var4 == null) {
            return null;
         } else {
            TextNode var17 = var4.getTextNode();
            List var6 = var17.getTextRuns();
            if (var6 == null) {
               return null;
            } else {
               Element var7 = var4.e;
               Rectangle2D var8 = null;

               for(int var9 = 0; var9 < var6.size(); ++var9) {
                  StrokingTextPainter.TextRun var10 = (StrokingTextPainter.TextRun)var6.get(var9);
                  TextSpanLayout var11 = var10.getLayout();
                  AttributedCharacterIterator var12 = var10.getACI();
                  var12.first();
                  SoftReference var13 = (SoftReference)var12.getAttribute(TEXT_COMPOUND_ID);
                  Element var14 = (Element)var13.get();
                  if (var14 != null && (!var2 || isTextSensitive(var14))) {
                     Element var15;
                     for(var15 = var14; var15 != null && var15 != var7 && var15 != var1; var15 = (Element)var4.getParentNode(var15)) {
                     }

                     if (var15 == var1) {
                        Rectangle2D var16 = var11.getBounds2D();
                        if (var16 != null) {
                           if (var8 == null) {
                              var8 = (Rectangle2D)var16.clone();
                           } else {
                              var8.add(var16);
                           }
                        }
                     }
                  }
               }

               return var8;
            }
         }
      }
   }

   public static boolean isTextSensitive(Element var0) {
      int var1 = CSSUtilities.convertPointerEvents(var0);
      switch (var1) {
         case 0:
         case 1:
         case 2:
         case 3:
            return CSSUtilities.convertVisibility(var0);
         case 4:
         case 5:
         case 6:
         case 7:
            return true;
         case 8:
         default:
            return false;
      }
   }

   static {
      TEXT_COMPOUND_DELIMITER = GVTAttributedCharacterIterator.TextAttribute.TEXT_COMPOUND_DELIMITER;
      TEXT_COMPOUND_ID = GVTAttributedCharacterIterator.TextAttribute.TEXT_COMPOUND_ID;
      PAINT_INFO = GVTAttributedCharacterIterator.TextAttribute.PAINT_INFO;
      ALT_GLYPH_HANDLER = GVTAttributedCharacterIterator.TextAttribute.ALT_GLYPH_HANDLER;
      TEXTPATH = GVTAttributedCharacterIterator.TextAttribute.TEXTPATH;
      ANCHOR_TYPE = GVTAttributedCharacterIterator.TextAttribute.ANCHOR_TYPE;
      GVT_FONT_FAMILIES = GVTAttributedCharacterIterator.TextAttribute.GVT_FONT_FAMILIES;
      GVT_FONTS = GVTAttributedCharacterIterator.TextAttribute.GVT_FONTS;
      BASELINE_SHIFT = GVTAttributedCharacterIterator.TextAttribute.BASELINE_SHIFT;
   }

   protected static class CharacterInformation {
      TextSpanLayout layout;
      int glyphIndexStart;
      int glyphIndexEnd;
      int characterIndex;

      public boolean isVertical() {
         return this.layout.isVertical();
      }

      public double getComputedOrientationAngle() {
         return this.layout.getComputedOrientationAngle(this.characterIndex);
      }
   }

   protected class TspanBridge extends AbstractTextChildTextContent {
      protected TspanBridge(BridgeContext var2, SVGTextElementBridge var3, Element var4) {
         super(var2, var3, var4);
      }

      public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue var1) {
         if (var1.getNamespaceURI() == null) {
            String var2 = var1.getLocalName();
            if (var2.equals("x") || var2.equals("y") || var2.equals("dx") || var2.equals("dy") || var2.equals("rotate") || var2.equals("textLength") || var2.equals("lengthAdjust")) {
               this.textBridge.computeLaidoutText(this.ctx, this.textBridge.e, this.textBridge.getTextNode());
               return;
            }
         }

         super.handleAnimatedAttributeChanged(var1);
      }
   }

   protected class TextPathBridge extends AbstractTextChildTextContent {
      protected TextPathBridge(BridgeContext var2, SVGTextElementBridge var3, Element var4) {
         super(var2, var3, var4);
      }
   }

   protected class TRefBridge extends AbstractTextChildTextContent {
      protected TRefBridge(BridgeContext var2, SVGTextElementBridge var3, Element var4) {
         super(var2, var3, var4);
      }

      public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue var1) {
         if (var1.getNamespaceURI() == null) {
            String var2 = var1.getLocalName();
            if (var2.equals("x") || var2.equals("y") || var2.equals("dx") || var2.equals("dy") || var2.equals("rotate") || var2.equals("textLength") || var2.equals("lengthAdjust")) {
               this.textBridge.computeLaidoutText(this.ctx, this.textBridge.e, this.textBridge.getTextNode());
               return;
            }
         }

         super.handleAnimatedAttributeChanged(var1);
      }
   }

   protected class AbstractTextChildTextContent extends AbstractTextChildBridgeUpdateHandler implements SVGTextContent {
      protected AbstractTextChildTextContent(BridgeContext var2, SVGTextElementBridge var3, Element var4) {
         super(var2, var3, var4);
      }

      public int getNumberOfChars() {
         return this.textBridge.getNumberOfChars(this.e);
      }

      public Rectangle2D getExtentOfChar(int var1) {
         return this.textBridge.getExtentOfChar(this.e, var1);
      }

      public Point2D getStartPositionOfChar(int var1) {
         return this.textBridge.getStartPositionOfChar(this.e, var1);
      }

      public Point2D getEndPositionOfChar(int var1) {
         return this.textBridge.getEndPositionOfChar(this.e, var1);
      }

      public void selectSubString(int var1, int var2) {
         this.textBridge.selectSubString(this.e, var1, var2);
      }

      public float getRotationOfChar(int var1) {
         return this.textBridge.getRotationOfChar(this.e, var1);
      }

      public float getComputedTextLength() {
         return this.textBridge.getComputedTextLength(this.e);
      }

      public float getSubStringLength(int var1, int var2) {
         return this.textBridge.getSubStringLength(this.e, var1, var2);
      }

      public int getCharNumAtPosition(float var1, float var2) {
         return this.textBridge.getCharNumAtPosition(this.e, var1, var2);
      }
   }

   protected abstract class AbstractTextChildBridgeUpdateHandler extends AbstractTextChildSVGContext implements BridgeUpdateHandler {
      protected AbstractTextChildBridgeUpdateHandler(BridgeContext var2, SVGTextElementBridge var3, Element var4) {
         super(var2, var3, var4);
      }

      public void handleDOMAttrModifiedEvent(MutationEvent var1) {
      }

      public void handleDOMNodeInsertedEvent(MutationEvent var1) {
         this.textBridge.handleDOMNodeInsertedEvent(var1);
      }

      public void handleDOMNodeRemovedEvent(MutationEvent var1) {
      }

      public void handleDOMCharacterDataModified(MutationEvent var1) {
         this.textBridge.handleDOMCharacterDataModified(var1);
      }

      public void handleCSSEngineEvent(CSSEngineEvent var1) {
         this.textBridge.handleCSSEngineEvent(var1);
      }

      public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue var1) {
      }

      public void handleOtherAnimationChanged(String var1) {
      }

      public void dispose() {
         ((SVGOMElement)this.e).setSVGContext((SVGContext)null);
         SVGTextElementBridge.this.elemTPI.remove(this.e);
      }
   }

   public abstract class AbstractTextChildSVGContext extends AnimatableSVGBridge {
      protected SVGTextElementBridge textBridge;

      public AbstractTextChildSVGContext(BridgeContext var2, SVGTextElementBridge var3, Element var4) {
         this.ctx = var2;
         this.textBridge = var3;
         this.e = var4;
      }

      public String getNamespaceURI() {
         return null;
      }

      public String getLocalName() {
         return null;
      }

      public Bridge getInstance() {
         return null;
      }

      public SVGTextElementBridge getTextBridge() {
         return this.textBridge;
      }

      public float getPixelUnitToMillimeter() {
         return this.ctx.getUserAgent().getPixelUnitToMillimeter();
      }

      public float getPixelToMM() {
         return this.getPixelUnitToMillimeter();
      }

      public Rectangle2D getBBox() {
         return null;
      }

      public AffineTransform getCTM() {
         return null;
      }

      public AffineTransform getGlobalTransform() {
         return null;
      }

      public AffineTransform getScreenTransform() {
         return null;
      }

      public void setScreenTransform(AffineTransform var1) {
      }

      public float getViewportWidth() {
         return this.ctx.getBlockWidth(this.e);
      }

      public float getViewportHeight() {
         return this.ctx.getBlockHeight(this.e);
      }

      public float getFontSize() {
         return CSSUtilities.getComputedStyle(this.e, 22).getFloatValue();
      }
   }

   protected static class AttributedStringBuffer {
      protected List strings = new ArrayList();
      protected List attributes = new ArrayList();
      protected int count = 0;
      protected int length = 0;

      public AttributedStringBuffer() {
      }

      public boolean isEmpty() {
         return this.count == 0;
      }

      public int length() {
         return this.length;
      }

      public void append(String var1, Map var2) {
         if (var1.length() != 0) {
            this.strings.add(var1);
            this.attributes.add(var2);
            ++this.count;
            this.length += var1.length();
         }
      }

      public int getLastChar() {
         if (this.count == 0) {
            return -1;
         } else {
            String var1 = (String)this.strings.get(this.count - 1);
            return var1.charAt(var1.length() - 1);
         }
      }

      public void stripFirst() {
         String var1 = (String)this.strings.get(0);
         if (var1.charAt(var1.length() - 1) == ' ') {
            --this.length;
            if (var1.length() == 1) {
               this.attributes.remove(0);
               this.strings.remove(0);
               --this.count;
            } else {
               this.strings.set(0, var1.substring(1));
            }
         }
      }

      public void stripLast() {
         String var1 = (String)this.strings.get(this.count - 1);
         if (var1.charAt(var1.length() - 1) == ' ') {
            --this.length;
            if (var1.length() == 1) {
               this.attributes.remove(--this.count);
               this.strings.remove(this.count);
            } else {
               this.strings.set(this.count - 1, var1.substring(0, var1.length() - 1));
            }
         }
      }

      public AttributedString toAttributedString() {
         switch (this.count) {
            case 0:
               return null;
            case 1:
               return new AttributedString((String)this.strings.get(0), (Map)this.attributes.get(0));
            default:
               StringBuffer var1 = new StringBuffer(this.strings.size() * 5);
               Iterator var2 = this.strings.iterator();

               while(var2.hasNext()) {
                  var1.append((String)var2.next());
               }

               AttributedString var3 = new AttributedString(var1.toString());
               Iterator var4 = this.strings.iterator();
               Iterator var5 = this.attributes.iterator();

               int var8;
               for(int var6 = 0; var4.hasNext(); var6 = var8) {
                  String var7 = (String)var4.next();
                  var8 = var6 + var7.length();
                  Map var9 = (Map)var5.next();
                  Iterator var10 = var9.keySet().iterator();
                  Iterator var11 = var9.values().iterator();

                  while(var10.hasNext()) {
                     AttributedCharacterIterator.Attribute var12 = (AttributedCharacterIterator.Attribute)var10.next();
                     Object var13 = var11.next();
                     var3.addAttribute(var12, var13, var6, var8);
                  }
               }

               return var3;
         }
      }

      public String toString() {
         switch (this.count) {
            case 0:
               return "";
            case 1:
               return (String)this.strings.get(0);
            default:
               StringBuffer var1 = new StringBuffer(this.strings.size() * 5);
               Iterator var2 = this.strings.iterator();

               while(var2.hasNext()) {
                  var1.append((String)var2.next());
               }

               return var1.toString();
         }
      }
   }

   protected class DOMSubtreeModifiedEventListener implements EventListener {
      public void handleEvent(Event var1) {
         SVGTextElementBridge.this.handleDOMSubtreeModifiedEvent((MutationEvent)var1);
      }
   }

   protected class DOMChildNodeRemovedEventListener implements EventListener {
      public void handleEvent(Event var1) {
         SVGTextElementBridge.this.handleDOMChildNodeRemovedEvent((MutationEvent)var1);
      }
   }
}

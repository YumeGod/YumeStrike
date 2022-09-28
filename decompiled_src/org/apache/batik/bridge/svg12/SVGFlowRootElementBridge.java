package org.apache.batik.bridge.svg12;

import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.batik.bridge.Bridge;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.CSSUtilities;
import org.apache.batik.bridge.CursorManager;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.SVGAElementBridge;
import org.apache.batik.bridge.SVGTextElementBridge;
import org.apache.batik.bridge.SVGUtilities;
import org.apache.batik.bridge.TextUtilities;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.ComputedValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.apache.batik.css.engine.value.svg12.LineHeightValue;
import org.apache.batik.css.engine.value.svg12.SVG12ValueConstants;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.dom.svg.SVGOMElement;
import org.apache.batik.dom.svg12.SVGOMFlowRegionElement;
import org.apache.batik.dom.svg12.XBLEventSupport;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.dom.util.XMLSupport;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.flow.BlockInfo;
import org.apache.batik.gvt.flow.FlowTextNode;
import org.apache.batik.gvt.flow.RegionInfo;
import org.apache.batik.gvt.flow.TextLineBreaks;
import org.apache.batik.gvt.text.GVTAttributedCharacterIterator;
import org.apache.batik.gvt.text.TextPaintInfo;
import org.apache.batik.gvt.text.TextPath;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class SVGFlowRootElementBridge extends SVG12TextElementBridge {
   public static final AttributedCharacterIterator.Attribute FLOW_PARAGRAPH;
   public static final AttributedCharacterIterator.Attribute FLOW_EMPTY_PARAGRAPH;
   public static final AttributedCharacterIterator.Attribute FLOW_LINE_BREAK;
   public static final AttributedCharacterIterator.Attribute FLOW_REGIONS;
   public static final AttributedCharacterIterator.Attribute LINE_HEIGHT;
   public static final GVTAttributedCharacterIterator.TextAttribute TEXTPATH;
   public static final GVTAttributedCharacterIterator.TextAttribute ANCHOR_TYPE;
   public static final GVTAttributedCharacterIterator.TextAttribute LETTER_SPACING;
   public static final GVTAttributedCharacterIterator.TextAttribute WORD_SPACING;
   public static final GVTAttributedCharacterIterator.TextAttribute KERNING;
   protected Map flowRegionNodes;
   protected TextNode textNode;
   protected RegionChangeListener regionChangeListener;
   protected int startLen;
   int marginTopIndex = -1;
   int marginRightIndex = -1;
   int marginBottomIndex = -1;
   int marginLeftIndex = -1;
   int indentIndex = -1;
   int textAlignIndex = -1;
   int lineHeightIndex = -1;

   protected TextNode getTextNode() {
      return this.textNode;
   }

   public String getNamespaceURI() {
      return "http://www.w3.org/2000/svg";
   }

   public String getLocalName() {
      return "flowRoot";
   }

   public Bridge getInstance() {
      return new SVGFlowRootElementBridge();
   }

   public boolean isComposite() {
      return false;
   }

   public GraphicsNode createGraphicsNode(BridgeContext var1, Element var2) {
      if (!SVGUtilities.matchUserAgent(var2, var1.getUserAgent())) {
         return null;
      } else {
         CompositeGraphicsNode var3 = new CompositeGraphicsNode();
         String var4 = var2.getAttributeNS((String)null, "transform");
         if (var4.length() != 0) {
            var3.setTransform(SVGUtilities.convertTransform(var2, "transform", var4, var1));
         }

         var3.setVisible(CSSUtilities.convertVisibility(var2));
         RenderingHints var5 = null;
         var5 = CSSUtilities.convertColorRendering(var2, var5);
         var5 = CSSUtilities.convertTextRendering(var2, var5);
         if (var5 != null) {
            var3.setRenderingHints(var5);
         }

         CompositeGraphicsNode var6 = new CompositeGraphicsNode();
         var3.add(var6);
         FlowTextNode var7 = (FlowTextNode)this.instantiateGraphicsNode();
         var7.setLocation(this.getLocation(var1, var2));
         if (var1.getTextPainter() != null) {
            var7.setTextPainter(var1.getTextPainter());
         }

         this.textNode = var7;
         var3.add(var7);
         this.associateSVGContext(var1, var2, var3);

         for(Node var8 = this.getFirstChild(var2); var8 != null; var8 = this.getNextSibling(var8)) {
            if (var8.getNodeType() == 1) {
               this.addContextToChild(var1, (Element)var8);
            }
         }

         return var3;
      }
   }

   protected GraphicsNode instantiateGraphicsNode() {
      return new FlowTextNode();
   }

   protected Point2D getLocation(BridgeContext var1, Element var2) {
      return new Point2D.Float(0.0F, 0.0F);
   }

   protected boolean isTextElement(Element var1) {
      if (!"http://www.w3.org/2000/svg".equals(var1.getNamespaceURI())) {
         return false;
      } else {
         String var2 = var1.getLocalName();
         return var2.equals("flowDiv") || var2.equals("flowLine") || var2.equals("flowPara") || var2.equals("flowRegionBreak") || var2.equals("flowSpan");
      }
   }

   protected boolean isTextChild(Element var1) {
      if (!"http://www.w3.org/2000/svg".equals(var1.getNamespaceURI())) {
         return false;
      } else {
         String var2 = var1.getLocalName();
         return var2.equals("a") || var2.equals("flowLine") || var2.equals("flowPara") || var2.equals("flowRegionBreak") || var2.equals("flowSpan");
      }
   }

   public void buildGraphicsNode(BridgeContext var1, Element var2, GraphicsNode var3) {
      CompositeGraphicsNode var4 = (CompositeGraphicsNode)var3;
      boolean var5 = !var1.isDynamic();
      if (var5) {
         this.flowRegionNodes = new HashMap();
      } else {
         this.regionChangeListener = new RegionChangeListener();
      }

      CompositeGraphicsNode var6 = (CompositeGraphicsNode)var4.get(0);
      GVTBuilder var7 = var1.getGVTBuilder();

      for(Node var8 = this.getFirstChild(var2); var8 != null; var8 = this.getNextSibling(var8)) {
         if (var8 instanceof SVGOMFlowRegionElement) {
            for(Node var9 = this.getFirstChild(var8); var9 != null; var9 = this.getNextSibling(var9)) {
               if (var9.getNodeType() == 1) {
                  GraphicsNode var10 = var7.build(var1, (Element)var9);
                  if (var10 != null) {
                     var6.add(var10);
                     if (var5) {
                        this.flowRegionNodes.put(var9, var10);
                     }
                  }
               }
            }

            if (!var5) {
               AbstractNode var12 = (AbstractNode)var8;
               XBLEventSupport var13 = (XBLEventSupport)var12.initializeEventSupport();
               var13.addImplementationEventListenerNS("http://www.w3.org/2000/svg", "shapechange", this.regionChangeListener, false);
            }
         }
      }

      GraphicsNode var11 = (GraphicsNode)var4.get(1);
      super.buildGraphicsNode(var1, var2, var11);
      this.flowRegionNodes = null;
   }

   protected void computeLaidoutText(BridgeContext var1, Element var2, GraphicsNode var3) {
      super.computeLaidoutText(var1, this.getFlowDivElement(var2), var3);
   }

   protected void addContextToChild(BridgeContext var1, Element var2) {
      if ("http://www.w3.org/2000/svg".equals(var2.getNamespaceURI())) {
         String var3 = var2.getLocalName();
         if (var3.equals("flowDiv") || var3.equals("flowLine") || var3.equals("flowPara") || var3.equals("flowSpan")) {
            ((SVGOMElement)var2).setSVGContext(new FlowContentBridge(var1, this, var2));
         }
      }

      for(Node var4 = this.getFirstChild(var2); var4 != null; var4 = this.getNextSibling(var4)) {
         if (var4.getNodeType() == 1) {
            this.addContextToChild(var1, (Element)var4);
         }
      }

   }

   protected void removeContextFromChild(BridgeContext var1, Element var2) {
      if ("http://www.w3.org/2000/svg".equals(var2.getNamespaceURI())) {
         String var3 = var2.getLocalName();
         if (var3.equals("flowDiv") || var3.equals("flowLine") || var3.equals("flowPara") || var3.equals("flowSpan")) {
            ((SVGTextElementBridge.AbstractTextChildBridgeUpdateHandler)((SVGOMElement)var2).getSVGContext()).dispose();
         }
      }

      for(Node var4 = this.getFirstChild(var2); var4 != null; var4 = this.getNextSibling(var4)) {
         if (var4.getNodeType() == 1) {
            this.removeContextFromChild(var1, (Element)var4);
         }
      }

   }

   protected AttributedString buildAttributedString(BridgeContext var1, Element var2) {
      if (var2 == null) {
         return null;
      } else {
         List var3 = this.getRegions(var1, var2);
         AttributedString var4 = this.getFlowDiv(var1, var2);
         if (var4 == null) {
            return var4;
         } else {
            var4.addAttribute(FLOW_REGIONS, var3, 0, 1);
            TextLineBreaks.findLineBrk(var4);
            return var4;
         }
      }
   }

   protected void dumpACIWord(AttributedString var1) {
      if (var1 != null) {
         StringBuffer var2 = new StringBuffer();
         StringBuffer var3 = new StringBuffer();
         AttributedCharacterIterator var4 = var1.getIterator();
         AttributedCharacterIterator.Attribute var5 = TextLineBreaks.WORD_LIMIT;

         for(char var6 = var4.current(); var6 != '\uffff'; var6 = var4.next()) {
            var2.append(var6).append(' ').append(' ');
            int var7 = (Integer)var4.getAttribute(var5);
            var3.append(var7).append(' ');
            if (var7 < 10) {
               var3.append(' ');
            }
         }

         System.out.println(var2.toString());
         System.out.println(var3.toString());
      }
   }

   protected Element getFlowDivElement(Element var1) {
      String var2 = var1.getNamespaceURI();
      if (!var2.equals("http://www.w3.org/2000/svg")) {
         return null;
      } else {
         String var3 = var1.getLocalName();
         if (var3.equals("flowDiv")) {
            return var1;
         } else if (!var3.equals("flowRoot")) {
            return null;
         } else {
            for(Node var4 = this.getFirstChild(var1); var4 != null; var4 = this.getNextSibling(var4)) {
               if (var4.getNodeType() == 1) {
                  String var5 = var4.getNamespaceURI();
                  if ("http://www.w3.org/2000/svg".equals(var5)) {
                     Element var6 = (Element)var4;
                     String var7 = var6.getLocalName();
                     if (var7.equals("flowDiv")) {
                        return var6;
                     }
                  }
               }
            }

            return null;
         }
      }
   }

   protected AttributedString getFlowDiv(BridgeContext var1, Element var2) {
      Element var3 = this.getFlowDivElement(var2);
      return var3 == null ? null : this.gatherFlowPara(var1, var3);
   }

   protected AttributedString gatherFlowPara(BridgeContext var1, Element var2) {
      TextPaintInfo var3 = new TextPaintInfo();
      var3.visible = true;
      var3.fillPaint = Color.black;
      this.elemTPI.put(var2, var3);
      SVGTextElementBridge.AttributedStringBuffer var4 = new SVGTextElementBridge.AttributedStringBuffer();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();
      ArrayList var7 = new ArrayList();

      for(Node var8 = this.getFirstChild(var2); var8 != null; var8 = this.getNextSibling(var8)) {
         if (var8.getNodeType() == 1 && this.getNamespaceURI().equals(var8.getNamespaceURI())) {
            Element var9 = (Element)var8;
            String var10 = var9.getLocalName();
            if (var10.equals("flowPara")) {
               this.fillAttributedStringBuffer(var1, var9, true, (Integer)null, (Map)null, var4, var7);
               var6.add(var9);
               var5.add(new Integer(var4.length()));
            } else if (var10.equals("flowRegionBreak")) {
               this.fillAttributedStringBuffer(var1, var9, true, (Integer)null, (Map)null, var4, var7);
               var6.add(var9);
               var5.add(new Integer(var4.length()));
            }
         }
      }

      var3.startChar = 0;
      var3.endChar = var4.length() - 1;
      AttributedString var16 = var4.toAttributedString();
      if (var16 == null) {
         return null;
      } else {
         int var17 = 0;
         Iterator var18 = var7.iterator();

         int var11;
         while(var18.hasNext()) {
            var11 = (Integer)var18.next();
            if (var11 != var17) {
               var16.addAttribute(FLOW_LINE_BREAK, new Object(), var17, var11);
               var17 = var11;
            }
         }

         var11 = 0;
         LinkedList var13 = null;

         int var12;
         for(int var14 = 0; var14 < var6.size(); var11 = var12) {
            Element var15 = (Element)var6.get(var14);
            var12 = (Integer)var5.get(var14);
            if (var11 == var12) {
               if (var13 == null) {
                  var13 = new LinkedList();
               }

               var13.add(this.makeBlockInfo(var1, var15));
            } else {
               var16.addAttribute(FLOW_PARAGRAPH, this.makeBlockInfo(var1, var15), var11, var12);
               if (var13 != null) {
                  var16.addAttribute(FLOW_EMPTY_PARAGRAPH, var13, var11, var12);
                  var13 = null;
               }
            }

            ++var14;
         }

         return var16;
      }
   }

   protected List getRegions(BridgeContext var1, Element var2) {
      var2 = (Element)var2.getParentNode();
      LinkedList var3 = new LinkedList();

      for(Node var4 = this.getFirstChild(var2); var4 != null; var4 = this.getNextSibling(var4)) {
         if (var4.getNodeType() == 1 && "http://www.w3.org/2000/svg".equals(var4.getNamespaceURI())) {
            Element var5 = (Element)var4;
            String var6 = var5.getLocalName();
            if ("flowRegion".equals(var6)) {
               float var7 = 0.0F;
               this.gatherRegionInfo(var1, var5, var7, var3);
            }
         }
      }

      return var3;
   }

   protected void gatherRegionInfo(BridgeContext var1, Element var2, float var3, List var4) {
      boolean var5 = !var1.isDynamic();

      for(Node var6 = this.getFirstChild(var2); var6 != null; var6 = this.getNextSibling(var6)) {
         if (var6.getNodeType() == 1) {
            GraphicsNode var7 = var5 ? (GraphicsNode)this.flowRegionNodes.get(var6) : var1.getGraphicsNode(var6);
            Shape var8 = var7.getOutline();
            if (var8 != null) {
               AffineTransform var9 = var7.getTransform();
               if (var9 != null) {
                  var8 = var9.createTransformedShape(var8);
               }

               var4.add(new RegionInfo(var8, var3));
            }
         }
      }

   }

   protected void fillAttributedStringBuffer(BridgeContext var1, Element var2, boolean var3, Integer var4, Map var5, SVGTextElementBridge.AttributedStringBuffer var6, List var7) {
      if (SVGUtilities.matchUserAgent(var2, var1.getUserAgent()) && CSSUtilities.convertDisplay(var2)) {
         String var8 = XMLSupport.getXMLSpace(var2);
         boolean var9 = var8.equals("preserve");
         int var12 = var6.length();
         if (var3) {
            this.endLimit = this.startLen = var6.length();
         }

         if (var9) {
            this.endLimit = this.startLen;
         }

         HashMap var13 = var5 == null ? new HashMap() : new HashMap(var5);
         var5 = this.getAttributeMap(var1, var2, (TextPath)null, var4, var13);
         Object var14 = var13.get(TextAttribute.BIDI_EMBEDDING);
         Integer var15 = var4;
         if (var14 != null) {
            var15 = (Integer)var14;
         }

         boolean var16 = true;
         int var25;
         if (var7.size() != 0) {
            var25 = (Integer)var7.get(var7.size() - 1);
         }

         int var18;
         int var19;
         Integer var20;
         for(Node var17 = this.getFirstChild(var2); var17 != null; var17 = this.getNextSibling(var17)) {
            boolean var10;
            if (var9) {
               var10 = false;
            } else {
               var18 = var6.length();
               if (var18 == this.startLen) {
                  var10 = true;
               } else {
                  var10 = var6.getLastChar() == 32;
                  var19 = var7.size() - 1;
                  if (!var10 && var19 >= 0) {
                     var20 = (Integer)var7.get(var19);
                     if (var20 == var18) {
                        var10 = true;
                     }
                  }
               }
            }

            switch (var17.getNodeType()) {
               case 1:
                  if ("http://www.w3.org/2000/svg".equals(var17.getNamespaceURI())) {
                     Element var11 = (Element)var17;
                     String var28 = var17.getLocalName();
                     if (var28.equals("flowLine")) {
                        var19 = var6.length();
                        this.fillAttributedStringBuffer(var1, var11, false, var15, var5, var6, var7);
                        var25 = var6.length();
                        var7.add(new Integer(var25));
                        if (var19 != var25) {
                           var5 = null;
                        }
                     } else if (!var28.equals("flowSpan") && !var28.equals("altGlyph")) {
                        if (var28.equals("a")) {
                           if (var1.isInteractive()) {
                              NodeEventTarget var31 = (NodeEventTarget)var11;
                              UserAgent var32 = var1.getUserAgent();
                              SVGAElementBridge.CursorHolder var21 = new SVGAElementBridge.CursorHolder(CursorManager.DEFAULT_CURSOR);
                              var31.addEventListenerNS("http://www.w3.org/2001/xml-events", "click", new SVGAElementBridge.AnchorListener(var32, var21), false, (Object)null);
                              var31.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseover", new SVGAElementBridge.CursorMouseOverListener(var32, var21), false, (Object)null);
                              var31.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseout", new SVGAElementBridge.CursorMouseOutListener(var32, var21), false, (Object)null);
                           }

                           var19 = var6.length();
                           this.fillAttributedStringBuffer(var1, var11, false, var15, var5, var6, var7);
                           if (var6.length() != var19) {
                              var5 = null;
                           }
                        } else if (var28.equals("tref")) {
                           String var33 = XLinkSupport.getXLinkHref((Element)var17);
                           Element var34 = var1.getReferencedElement((Element)var17, var33);
                           var8 = TextUtilities.getElementContent(var34);
                           var8 = this.normalizeString(var8, var9, var10);
                           if (var8.length() != 0) {
                              int var36 = var6.length();
                              HashMap var22 = new HashMap();
                              this.getAttributeMap(var1, var11, (TextPath)null, var4, var22);
                              var6.append(var8, var22);
                              int var23 = var6.length() - 1;
                              TextPaintInfo var24 = (TextPaintInfo)this.elemTPI.get(var11);
                              var24.startChar = var36;
                              var24.endChar = var23;
                           }
                        }
                     } else {
                        var19 = var6.length();
                        this.fillAttributedStringBuffer(var1, var11, false, var15, var5, var6, var7);
                        if (var6.length() != var19) {
                           var5 = null;
                        }
                     }
                  }
               case 2:
               default:
                  break;
               case 3:
               case 4:
                  var8 = var17.getNodeValue();
                  var8 = this.normalizeString(var8, var9, var10);
                  if (var8.length() != 0) {
                     var6.append(var8, var13);
                     if (var9) {
                        this.endLimit = var6.length();
                     }

                     var5 = null;
                  }
            }
         }

         if (var3) {
            boolean var26;
            for(var26 = false; this.endLimit < var6.length() && var6.getLastChar() == 32; var26 = true) {
               var18 = var7.size() - 1;
               var19 = var6.length();
               if (var18 >= 0) {
                  var20 = (Integer)var7.get(var18);
                  if (var20 >= var19) {
                     var20 = new Integer(var19 - 1);
                     var7.set(var18, var20);
                     --var18;

                     while(var18 >= 0) {
                        var20 = (Integer)var7.get(var18);
                        if (var20 < var19 - 1) {
                           break;
                        }

                        var7.remove(var18);
                        --var18;
                     }
                  }
               }

               var6.stripLast();
            }

            if (var26) {
               Iterator var29 = this.elemTPI.values().iterator();

               while(var29.hasNext()) {
                  TextPaintInfo var35 = (TextPaintInfo)var29.next();
                  if (var35.endChar >= var6.length()) {
                     var35.endChar = var6.length() - 1;
                     if (var35.startChar > var35.endChar) {
                        var35.startChar = var35.endChar;
                     }
                  }
               }
            }
         }

         int var27 = var6.length() - 1;
         TextPaintInfo var30 = (TextPaintInfo)this.elemTPI.get(var2);
         var30.startChar = var12;
         var30.endChar = var27;
      }
   }

   protected Map getAttributeMap(BridgeContext var1, Element var2, TextPath var3, Integer var4, Map var5) {
      Map var6 = super.getAttributeMap(var1, var2, var3, var4, var5);
      float var7 = TextUtilities.convertFontSize(var2);
      float var8 = this.getLineHeight(var1, var2, var7);
      var5.put(LINE_HEIGHT, new Float(var8));
      return var6;
   }

   protected void checkMap(Map var1) {
      if (!var1.containsKey(TEXTPATH)) {
         if (!var1.containsKey(ANCHOR_TYPE)) {
            if (!var1.containsKey(LETTER_SPACING)) {
               if (!var1.containsKey(WORD_SPACING)) {
                  if (!var1.containsKey(KERNING)) {
                     ;
                  }
               }
            }
         }
      }
   }

   protected void initCSSPropertyIndexes(Element var1) {
      CSSEngine var2 = CSSUtilities.getCSSEngine(var1);
      this.marginTopIndex = var2.getPropertyIndex("margin-top");
      this.marginRightIndex = var2.getPropertyIndex("margin-right");
      this.marginBottomIndex = var2.getPropertyIndex("margin-bottom");
      this.marginLeftIndex = var2.getPropertyIndex("margin-left");
      this.indentIndex = var2.getPropertyIndex("indent");
      this.textAlignIndex = var2.getPropertyIndex("text-align");
      this.lineHeightIndex = var2.getPropertyIndex("line-height");
   }

   public BlockInfo makeBlockInfo(BridgeContext var1, Element var2) {
      if (this.marginTopIndex == -1) {
         this.initCSSPropertyIndexes(var2);
      }

      Value var3 = CSSUtilities.getComputedStyle(var2, this.marginTopIndex);
      float var4 = var3.getFloatValue();
      var3 = CSSUtilities.getComputedStyle(var2, this.marginRightIndex);
      float var5 = var3.getFloatValue();
      var3 = CSSUtilities.getComputedStyle(var2, this.marginBottomIndex);
      float var6 = var3.getFloatValue();
      var3 = CSSUtilities.getComputedStyle(var2, this.marginLeftIndex);
      float var7 = var3.getFloatValue();
      var3 = CSSUtilities.getComputedStyle(var2, this.indentIndex);
      float var8 = var3.getFloatValue();
      var3 = CSSUtilities.getComputedStyle(var2, this.textAlignIndex);
      if (var3 == ValueConstants.INHERIT_VALUE) {
         var3 = CSSUtilities.getComputedStyle(var2, 11);
         if (var3 == ValueConstants.LTR_VALUE) {
            var3 = SVG12ValueConstants.START_VALUE;
         } else {
            var3 = SVG12ValueConstants.END_VALUE;
         }
      }

      byte var9;
      if (var3 == SVG12ValueConstants.START_VALUE) {
         var9 = 0;
      } else if (var3 == SVG12ValueConstants.MIDDLE_VALUE) {
         var9 = 1;
      } else if (var3 == SVG12ValueConstants.END_VALUE) {
         var9 = 2;
      } else {
         var9 = 3;
      }

      HashMap var10 = new HashMap(20);
      List var11 = this.getFontList(var1, var2, var10);
      Float var12 = (Float)var10.get(TextAttribute.SIZE);
      float var13 = var12;
      float var14 = this.getLineHeight(var1, var2, var13);
      String var15 = var2.getLocalName();
      boolean var16 = var15.equals("flowRegionBreak");
      return new BlockInfo(var4, var5, var6, var7, var8, var9, var14, var11, var10, var16);
   }

   protected float getLineHeight(BridgeContext var1, Element var2, float var3) {
      if (this.lineHeightIndex == -1) {
         this.initCSSPropertyIndexes(var2);
      }

      Value var4 = CSSUtilities.getComputedStyle(var2, this.lineHeightIndex);
      if (var4 != ValueConstants.INHERIT_VALUE && var4 != SVG12ValueConstants.NORMAL_VALUE) {
         float var5 = var4.getFloatValue();
         if (var4 instanceof ComputedValue) {
            var4 = ((ComputedValue)var4).getComputedValue();
         }

         if (var4 instanceof LineHeightValue && ((LineHeightValue)var4).getFontSizeRelative()) {
            var5 *= var3;
         }

         return var5;
      } else {
         return var3 * 1.1F;
      }
   }

   static {
      FLOW_PARAGRAPH = GVTAttributedCharacterIterator.TextAttribute.FLOW_PARAGRAPH;
      FLOW_EMPTY_PARAGRAPH = GVTAttributedCharacterIterator.TextAttribute.FLOW_EMPTY_PARAGRAPH;
      FLOW_LINE_BREAK = GVTAttributedCharacterIterator.TextAttribute.FLOW_LINE_BREAK;
      FLOW_REGIONS = GVTAttributedCharacterIterator.TextAttribute.FLOW_REGIONS;
      LINE_HEIGHT = GVTAttributedCharacterIterator.TextAttribute.LINE_HEIGHT;
      TEXTPATH = GVTAttributedCharacterIterator.TextAttribute.TEXTPATH;
      ANCHOR_TYPE = GVTAttributedCharacterIterator.TextAttribute.ANCHOR_TYPE;
      LETTER_SPACING = GVTAttributedCharacterIterator.TextAttribute.LETTER_SPACING;
      WORD_SPACING = GVTAttributedCharacterIterator.TextAttribute.WORD_SPACING;
      KERNING = GVTAttributedCharacterIterator.TextAttribute.KERNING;
   }

   protected class RegionChangeListener implements EventListener {
      public void handleEvent(Event var1) {
         SVGFlowRootElementBridge.this.laidoutText = null;
         SVGFlowRootElementBridge.this.computeLaidoutText(SVGFlowRootElementBridge.this.ctx, SVGFlowRootElementBridge.this.e, SVGFlowRootElementBridge.this.getTextNode());
      }
   }

   protected class FlowContentBridge extends SVGTextElementBridge.AbstractTextChildTextContent {
      public FlowContentBridge(BridgeContext var2, SVGTextElementBridge var3, Element var4) {
         super(var2, var3, var4);
      }
   }
}

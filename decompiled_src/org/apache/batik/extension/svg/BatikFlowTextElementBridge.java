package org.apache.batik.extension.svg;

import java.awt.Color;
import java.awt.font.TextAttribute;
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
import org.apache.batik.bridge.BridgeException;
import org.apache.batik.bridge.CSSUtilities;
import org.apache.batik.bridge.CursorManager;
import org.apache.batik.bridge.SVGAElementBridge;
import org.apache.batik.bridge.SVGTextElementBridge;
import org.apache.batik.bridge.SVGUtilities;
import org.apache.batik.bridge.TextUtilities;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.dom.svg.SVGOMElement;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.dom.util.XMLSupport;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.text.GVTAttributedCharacterIterator;
import org.apache.batik.gvt.text.TextPaintInfo;
import org.apache.batik.gvt.text.TextPath;
import org.apache.batik.parser.UnitProcessor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class BatikFlowTextElementBridge extends SVGTextElementBridge implements BatikExtConstants {
   public static final AttributedCharacterIterator.Attribute FLOW_PARAGRAPH;
   public static final AttributedCharacterIterator.Attribute FLOW_EMPTY_PARAGRAPH;
   public static final AttributedCharacterIterator.Attribute FLOW_LINE_BREAK;
   public static final AttributedCharacterIterator.Attribute FLOW_REGIONS;
   public static final AttributedCharacterIterator.Attribute PREFORMATTED;
   protected static final GVTAttributedCharacterIterator.TextAttribute TEXTPATH;
   protected static final GVTAttributedCharacterIterator.TextAttribute ANCHOR_TYPE;
   protected static final GVTAttributedCharacterIterator.TextAttribute LETTER_SPACING;
   protected static final GVTAttributedCharacterIterator.TextAttribute WORD_SPACING;
   protected static final GVTAttributedCharacterIterator.TextAttribute KERNING;

   public String getNamespaceURI() {
      return "http://xml.apache.org/batik/ext";
   }

   public String getLocalName() {
      return "flowText";
   }

   public Bridge getInstance() {
      return new BatikFlowTextElementBridge();
   }

   public boolean isComposite() {
      return false;
   }

   protected GraphicsNode instantiateGraphicsNode() {
      return new FlowExtTextNode();
   }

   protected Point2D getLocation(BridgeContext var1, Element var2) {
      return new Point2D.Float(0.0F, 0.0F);
   }

   protected void addContextToChild(BridgeContext var1, Element var2) {
      if (this.getNamespaceURI().equals(var2.getNamespaceURI())) {
         String var3 = var2.getLocalName();
         if (var3.equals("flowPara") || var3.equals("flowRegionBreak") || var3.equals("flowLine") || var3.equals("flowSpan") || var3.equals("a") || var3.equals("tref")) {
            ((SVGOMElement)var2).setSVGContext(new BatikFlowContentBridge(var1, this, var2));
         }
      }

      for(Node var4 = this.getFirstChild(var2); var4 != null; var4 = this.getNextSibling(var4)) {
         if (var4.getNodeType() == 1) {
            this.addContextToChild(var1, (Element)var4);
         }
      }

   }

   protected AttributedString buildAttributedString(BridgeContext var1, Element var2) {
      List var3 = this.getRegions(var1, var2);
      AttributedString var4 = this.getFlowDiv(var1, var2);
      if (var4 == null) {
         return var4;
      } else {
         var4.addAttribute(FLOW_REGIONS, var3, 0, 1);
         return var4;
      }
   }

   protected void addGlyphPositionAttributes(AttributedString var1, Element var2, BridgeContext var3) {
      if (var2.getNodeType() == 1) {
         String var4 = var2.getNamespaceURI();
         if (var4.equals(this.getNamespaceURI()) || var4.equals("http://www.w3.org/2000/svg")) {
            if (var2.getLocalName() != "flowText") {
               super.addGlyphPositionAttributes(var1, var2, var3);
            } else {
               for(Node var5 = var2.getFirstChild(); var5 != null; var5 = var5.getNextSibling()) {
                  if (var5.getNodeType() == 1) {
                     String var6 = var5.getNamespaceURI();
                     if (this.getNamespaceURI().equals(var6) || "http://www.w3.org/2000/svg".equals(var6)) {
                        Element var7 = (Element)var5;
                        String var8 = var7.getLocalName();
                        if (var8.equals("flowDiv")) {
                           super.addGlyphPositionAttributes(var1, var7, var3);
                           return;
                        }
                     }
                  }
               }

            }
         }
      }
   }

   protected void addChildGlyphPositionAttributes(AttributedString var1, Element var2, BridgeContext var3) {
      for(Node var4 = var2.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
         if (var4.getNodeType() == 1) {
            String var5 = var4.getNamespaceURI();
            if (this.getNamespaceURI().equals(var5) || "http://www.w3.org/2000/svg".equals(var5)) {
               String var6 = var4.getLocalName();
               if (var6.equals("flowPara") || var6.equals("flowRegionBreak") || var6.equals("flowLine") || var6.equals("flowSpan") || var6.equals("a") || var6.equals("tref")) {
                  this.addGlyphPositionAttributes(var1, (Element)var4, var3);
               }
            }
         }
      }

   }

   protected void addPaintAttributes(AttributedString var1, Element var2, TextNode var3, TextPaintInfo var4, BridgeContext var5) {
      if (var2.getNodeType() == 1) {
         String var6 = var2.getNamespaceURI();
         if (var6.equals(this.getNamespaceURI()) || var6.equals("http://www.w3.org/2000/svg")) {
            if (var2.getLocalName() != "flowText") {
               super.addPaintAttributes(var1, var2, var3, var4, var5);
            } else {
               for(Node var7 = var2.getFirstChild(); var7 != null; var7 = var7.getNextSibling()) {
                  if (var7.getNodeType() == 1 && this.getNamespaceURI().equals(var7.getNamespaceURI())) {
                     Element var8 = (Element)var7;
                     String var9 = var8.getLocalName();
                     if (var9.equals("flowDiv")) {
                        super.addPaintAttributes(var1, var8, var3, var4, var5);
                        return;
                     }
                  }
               }

            }
         }
      }
   }

   protected void addChildPaintAttributes(AttributedString var1, Element var2, TextNode var3, TextPaintInfo var4, BridgeContext var5) {
      for(Node var6 = var2.getFirstChild(); var6 != null; var6 = var6.getNextSibling()) {
         if (var6.getNodeType() == 1) {
            String var7 = var6.getNamespaceURI();
            if (this.getNamespaceURI().equals(var7) || "http://www.w3.org/2000/svg".equals(var7)) {
               String var8 = var6.getLocalName();
               if (var8.equals("flowPara") || var8.equals("flowRegionBreak") || var8.equals("flowLine") || var8.equals("flowSpan") || var8.equals("a") || var8.equals("tref")) {
                  Element var9 = (Element)var6;
                  TextPaintInfo var10 = this.getTextPaintInfo(var9, var3, var4, var5);
                  this.addPaintAttributes(var1, var9, var3, var10, var5);
               }
            }
         }
      }

   }

   protected AttributedString getFlowDiv(BridgeContext var1, Element var2) {
      for(Node var3 = var2.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
         if (var3.getNodeType() == 1 && this.getNamespaceURI().equals(var3.getNamespaceURI())) {
            Element var4 = (Element)var3;
            String var5 = var3.getLocalName();
            if (var5.equals("flowDiv")) {
               return this.gatherFlowPara(var1, var4);
            }
         }
      }

      return null;
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

      for(Node var8 = var2.getFirstChild(); var8 != null; var8 = var8.getNextSibling()) {
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

            var13.add(this.makeMarginInfo(var15));
         } else {
            var16.addAttribute(FLOW_PARAGRAPH, this.makeMarginInfo(var15), var11, var12);
            if (var13 != null) {
               var16.addAttribute(FLOW_EMPTY_PARAGRAPH, var13, var11, var12);
               var13 = null;
            }
         }

         ++var14;
      }

      return var16;
   }

   protected List getRegions(BridgeContext var1, Element var2) {
      LinkedList var3 = new LinkedList();

      for(Node var4 = var2.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
         if (var4.getNodeType() == 1 && this.getNamespaceURI().equals(var4.getNamespaceURI())) {
            Element var5 = (Element)var4;
            String var6 = var5.getLocalName();
            if ("flowRegion".equals(var6)) {
               float var7 = 0.0F;
               String var8 = var5.getAttribute("vertical-align");
               if (var8 != null && var8.length() > 0) {
                  if ("top".equals(var8)) {
                     var7 = 0.0F;
                  } else if ("middle".equals(var8)) {
                     var7 = 0.5F;
                  } else if ("bottom".equals(var8)) {
                     var7 = 1.0F;
                  }
               }

               this.gatherRegionInfo(var1, var5, var7, var3);
            }
         }
      }

      return var3;
   }

   protected void gatherRegionInfo(BridgeContext var1, Element var2, float var3, List var4) {
      for(Node var5 = var2.getFirstChild(); var5 != null; var5 = var5.getNextSibling()) {
         if (var5.getNodeType() == 1 && this.getNamespaceURI().equals(var5.getNamespaceURI())) {
            Element var6 = (Element)var5;
            String var7 = var5.getLocalName();
            if (var7.equals("rect")) {
               UnitProcessor.Context var8 = org.apache.batik.bridge.UnitProcessor.createContext(var1, var6);
               RegionInfo var9 = this.buildRegion(var8, var6, var3);
               if (var9 != null) {
                  var4.add(var9);
               }
            }
         }
      }

   }

   protected RegionInfo buildRegion(UnitProcessor.Context var1, Element var2, float var3) {
      String var4 = var2.getAttribute("x");
      float var5 = 0.0F;
      if (var4.length() != 0) {
         var5 = org.apache.batik.bridge.UnitProcessor.svgHorizontalCoordinateToUserSpace(var4, "x", var1);
      }

      var4 = var2.getAttribute("y");
      float var6 = 0.0F;
      if (var4.length() != 0) {
         var6 = org.apache.batik.bridge.UnitProcessor.svgVerticalCoordinateToUserSpace(var4, "y", var1);
      }

      var4 = var2.getAttribute("width");
      if (var4.length() != 0) {
         float var7 = org.apache.batik.bridge.UnitProcessor.svgHorizontalLengthToUserSpace(var4, "width", var1);
         if (var7 == 0.0F) {
            return null;
         } else {
            var4 = var2.getAttribute("height");
            if (var4.length() != 0) {
               float var8 = org.apache.batik.bridge.UnitProcessor.svgVerticalLengthToUserSpace(var4, "height", var1);
               return var8 == 0.0F ? null : new RegionInfo(var5, var6, var7, var8, var3);
            } else {
               throw new BridgeException(this.ctx, var2, "attribute.missing", new Object[]{"height", var4});
            }
         }
      } else {
         throw new BridgeException(this.ctx, var2, "attribute.missing", new Object[]{"width", var4});
      }
   }

   protected void fillAttributedStringBuffer(BridgeContext var1, Element var2, boolean var3, Integer var4, Map var5, SVGTextElementBridge.AttributedStringBuffer var6, List var7) {
      if (SVGUtilities.matchUserAgent(var2, var1.getUserAgent()) && CSSUtilities.convertDisplay(var2)) {
         String var8 = XMLSupport.getXMLSpace(var2);
         boolean var9 = var8.equals("preserve");
         int var12 = var6.length();
         if (var3) {
            this.endLimit = 0;
         }

         if (var9) {
            this.endLimit = var6.length();
         }

         HashMap var13 = var5 == null ? new HashMap() : new HashMap(var5);
         var5 = this.getAttributeMap(var1, var2, (TextPath)null, var4, var13);
         Object var14 = var13.get(TextAttribute.BIDI_EMBEDDING);
         Integer var15 = var4;
         if (var14 != null) {
            var15 = (Integer)var14;
         }

         for(Node var16 = var2.getFirstChild(); var16 != null; var16 = var16.getNextSibling()) {
            boolean var10;
            if (var9) {
               var10 = false;
            } else if (var6.length() == 0) {
               var10 = true;
            } else {
               var10 = var6.getLastChar() == 32;
            }

            switch (var16.getNodeType()) {
               case 1:
                  if (this.getNamespaceURI().equals(var16.getNamespaceURI()) || "http://www.w3.org/2000/svg".equals(var16.getNamespaceURI())) {
                     Element var11 = (Element)var16;
                     String var17 = var16.getLocalName();
                     int var18;
                     if (var17.equals("flowLine")) {
                        var18 = var6.length();
                        this.fillAttributedStringBuffer(var1, var11, false, var15, var5, var6, var7);
                        var7.add(new Integer(var6.length()));
                        if (var6.length() != var18) {
                           var5 = null;
                        }
                     } else if (!var17.equals("flowSpan") && !var17.equals("altGlyph")) {
                        if (var17.equals("a")) {
                           if (var1.isInteractive()) {
                              NodeEventTarget var28 = (NodeEventTarget)var11;
                              UserAgent var19 = var1.getUserAgent();
                              SVGAElementBridge.CursorHolder var20 = new SVGAElementBridge.CursorHolder(CursorManager.DEFAULT_CURSOR);
                              var28.addEventListenerNS("http://www.w3.org/2001/xml-events", "click", new SVGAElementBridge.AnchorListener(var19, var20), false, (Object)null);
                              var28.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseover", new SVGAElementBridge.CursorMouseOverListener(var19, var20), false, (Object)null);
                              var28.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseout", new SVGAElementBridge.CursorMouseOutListener(var19, var20), false, (Object)null);
                           }

                           var18 = var6.length();
                           this.fillAttributedStringBuffer(var1, var11, false, var15, var5, var6, var7);
                           if (var6.length() != var18) {
                              var5 = null;
                           }
                        } else if (var17.equals("tref")) {
                           String var29 = XLinkSupport.getXLinkHref((Element)var16);
                           Element var30 = var1.getReferencedElement((Element)var16, var29);
                           var8 = TextUtilities.getElementContent(var30);
                           var8 = this.normalizeString(var8, var9, var10);
                           if (var8.length() != 0) {
                              int var32 = var6.length();
                              HashMap var21 = var5 == null ? new HashMap() : new HashMap(var5);
                              this.getAttributeMap(var1, var11, (TextPath)null, var4, var21);
                              var6.append(var8, var21);
                              int var22 = var6.length() - 1;
                              TextPaintInfo var23 = (TextPaintInfo)this.elemTPI.get(var11);
                              var23.startChar = var32;
                              var23.endChar = var22;
                              var5 = null;
                           }
                        }
                     } else {
                        var18 = var6.length();
                        this.fillAttributedStringBuffer(var1, var11, false, var15, var5, var6, var7);
                        if (var6.length() != var18) {
                           var5 = null;
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
                     var6.append(var8, var13);
                     if (var9) {
                        this.endLimit = var6.length();
                     }

                     var5 = null;
                  }
            }
         }

         if (var3) {
            boolean var24;
            for(var24 = false; this.endLimit < var6.length() && var6.getLastChar() == 32; var24 = true) {
               var6.stripLast();
            }

            if (var24) {
               Iterator var26 = this.elemTPI.values().iterator();

               while(var26.hasNext()) {
                  TextPaintInfo var31 = (TextPaintInfo)var26.next();
                  if (var31.endChar >= var6.length()) {
                     var31.endChar = var6.length() - 1;
                     if (var31.startChar > var31.endChar) {
                        var31.startChar = var31.endChar;
                     }
                  }
               }
            }
         }

         int var25 = var6.length() - 1;
         TextPaintInfo var27 = (TextPaintInfo)this.elemTPI.get(var2);
         var27.startChar = var12;
         var27.endChar = var25;
      }
   }

   protected Map getAttributeMap(BridgeContext var1, Element var2, TextPath var3, Integer var4, Map var5) {
      Map var6 = super.getAttributeMap(var1, var2, var3, var4, var5);
      String var7 = var2.getAttribute("preformatted");
      if (var7.length() != 0 && var7.equals("true")) {
         var5.put(PREFORMATTED, Boolean.TRUE);
      }

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

   public MarginInfo makeMarginInfo(Element var1) {
      float var3 = 0.0F;
      float var4 = 0.0F;
      float var5 = 0.0F;
      float var6 = 0.0F;
      String var2 = var1.getAttribute("margin");

      float var7;
      try {
         if (var2.length() != 0) {
            var7 = Float.parseFloat(var2);
            var6 = var7;
            var5 = var7;
            var4 = var7;
            var3 = var7;
         }
      } catch (NumberFormatException var17) {
      }

      var2 = var1.getAttribute("top-margin");

      try {
         if (var2.length() != 0) {
            var7 = Float.parseFloat(var2);
            var3 = var7;
         }
      } catch (NumberFormatException var16) {
      }

      var2 = var1.getAttribute("right-margin");

      try {
         if (var2.length() != 0) {
            var7 = Float.parseFloat(var2);
            var4 = var7;
         }
      } catch (NumberFormatException var15) {
      }

      var2 = var1.getAttribute("bottom-margin");

      try {
         if (var2.length() != 0) {
            var7 = Float.parseFloat(var2);
            var5 = var7;
         }
      } catch (NumberFormatException var14) {
      }

      var2 = var1.getAttribute("left-margin");

      try {
         if (var2.length() != 0) {
            var7 = Float.parseFloat(var2);
            var6 = var7;
         }
      } catch (NumberFormatException var13) {
      }

      var7 = 0.0F;
      var2 = var1.getAttribute("indent");

      try {
         if (var2.length() != 0) {
            float var8 = Float.parseFloat(var2);
            var7 = var8;
         }
      } catch (NumberFormatException var12) {
      }

      byte var18 = 0;
      var2 = var1.getAttribute("justification");

      try {
         if (var2.length() != 0) {
            if ("start".equals(var2)) {
               var18 = 0;
            } else if ("middle".equals(var2)) {
               var18 = 1;
            } else if ("end".equals(var2)) {
               var18 = 2;
            } else if ("full".equals(var2)) {
               var18 = 3;
            }
         }
      } catch (NumberFormatException var11) {
      }

      String var9 = var1.getLocalName();
      boolean var10 = var9.equals("flowRegionBreak");
      return new MarginInfo(var3, var4, var5, var6, var7, var18, var10);
   }

   static {
      FLOW_PARAGRAPH = GVTAttributedCharacterIterator.TextAttribute.FLOW_PARAGRAPH;
      FLOW_EMPTY_PARAGRAPH = GVTAttributedCharacterIterator.TextAttribute.FLOW_EMPTY_PARAGRAPH;
      FLOW_LINE_BREAK = GVTAttributedCharacterIterator.TextAttribute.FLOW_LINE_BREAK;
      FLOW_REGIONS = GVTAttributedCharacterIterator.TextAttribute.FLOW_REGIONS;
      PREFORMATTED = GVTAttributedCharacterIterator.TextAttribute.PREFORMATTED;
      TEXTPATH = GVTAttributedCharacterIterator.TextAttribute.TEXTPATH;
      ANCHOR_TYPE = GVTAttributedCharacterIterator.TextAttribute.ANCHOR_TYPE;
      LETTER_SPACING = GVTAttributedCharacterIterator.TextAttribute.LETTER_SPACING;
      WORD_SPACING = GVTAttributedCharacterIterator.TextAttribute.WORD_SPACING;
      KERNING = GVTAttributedCharacterIterator.TextAttribute.KERNING;
   }

   protected class BatikFlowContentBridge extends SVGTextElementBridge.AbstractTextChildTextContent {
      public BatikFlowContentBridge(BridgeContext var2, SVGTextElementBridge var3, Element var4) {
         super(var2, var3, var4);
      }
   }

   public static class LineBreakInfo {
      int breakIdx;
      float lineAdvAdj;
      boolean relative;

      public LineBreakInfo(int var1, float var2, boolean var3) {
         this.breakIdx = var1;
         this.lineAdvAdj = var2;
         this.relative = var3;
      }

      public int getBreakIdx() {
         return this.breakIdx;
      }

      public boolean isRelative() {
         return this.relative;
      }

      public float getLineAdvAdj() {
         return this.lineAdvAdj;
      }
   }
}

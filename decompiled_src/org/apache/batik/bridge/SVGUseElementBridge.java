package org.apache.batik.bridge;

import java.awt.Cursor;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.dom.svg.AbstractSVGAnimatedLength;
import org.apache.batik.dom.svg.AnimatedLiveAttributeValue;
import org.apache.batik.dom.svg.LiveAttributeException;
import org.apache.batik.dom.svg.SVGOMAnimatedLength;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.svg.SVGOMUseElement;
import org.apache.batik.dom.svg.SVGOMUseShadowRoot;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.svg.SVGTransformable;
import org.w3c.dom.svg.SVGUseElement;

public class SVGUseElementBridge extends AbstractGraphicsNodeBridge {
   protected ReferencedElementMutationListener l;
   protected BridgeContext subCtx;

   public String getLocalName() {
      return "use";
   }

   public Bridge getInstance() {
      return new SVGUseElementBridge();
   }

   public GraphicsNode createGraphicsNode(BridgeContext var1, Element var2) {
      if (!SVGUtilities.matchUserAgent(var2, var1.getUserAgent())) {
         return null;
      } else {
         CompositeGraphicsNode var3 = this.buildCompositeGraphicsNode(var1, var2, (CompositeGraphicsNode)null);
         this.associateSVGContext(var1, var2, var3);
         return var3;
      }
   }

   public CompositeGraphicsNode buildCompositeGraphicsNode(BridgeContext var1, Element var2, CompositeGraphicsNode var3) {
      SVGOMUseElement var4 = (SVGOMUseElement)var2;
      String var5 = var4.getHref().getAnimVal();
      if (var5.length() == 0) {
         throw new BridgeException(var1, var2, "attribute.missing", new Object[]{"xlink:href"});
      } else {
         Element var6 = var1.getReferencedElement(var2, var5);
         SVGOMDocument var7 = (SVGOMDocument)var2.getOwnerDocument();
         SVGOMDocument var8 = (SVGOMDocument)var6.getOwnerDocument();
         boolean var9 = var8 == var7;
         BridgeContext var10 = var1;
         this.subCtx = null;
         if (!var9) {
            this.subCtx = (BridgeContext)var8.getCSSEngine().getCSSContext();
            var10 = this.subCtx;
         }

         Element var11 = (Element)var7.importNode(var6, true, true);
         int var14;
         if ("symbol".equals(var11.getLocalName())) {
            Element var12 = var7.createElementNS("http://www.w3.org/2000/svg", "svg");
            NamedNodeMap var13 = var11.getAttributes();
            var14 = var13.getLength();

            for(int var15 = 0; var15 < var14; ++var15) {
               Attr var16 = (Attr)var13.item(var15);
               var12.setAttributeNS(var16.getNamespaceURI(), var16.getName(), var16.getValue());
            }

            for(Node var25 = var11.getFirstChild(); var25 != null; var25 = var11.getFirstChild()) {
               var12.appendChild(var25);
            }

            var11 = var12;
         }

         if ("svg".equals(var11.getLocalName())) {
            try {
               SVGOMAnimatedLength var21 = (SVGOMAnimatedLength)var4.getWidth();
               if (var21.isSpecified()) {
                  var11.setAttributeNS((String)null, "width", var21.getAnimVal().getValueAsString());
               }

               var21 = (SVGOMAnimatedLength)var4.getHeight();
               if (var21.isSpecified()) {
                  var11.setAttributeNS((String)null, "height", var21.getAnimVal().getValueAsString());
               }
            } catch (LiveAttributeException var20) {
               throw new BridgeException(var1, var20);
            }
         }

         SVGOMUseShadowRoot var22 = new SVGOMUseShadowRoot(var7, var2, var9);
         var22.appendChild(var11);
         if (var3 == null) {
            var3 = new CompositeGraphicsNode();
            this.associateSVGContext(var1, var2, this.node);
         } else {
            int var23 = var3.size();

            for(var14 = 0; var14 < var23; ++var14) {
               var3.remove(0);
            }
         }

         Node var24 = var4.getCSSFirstChild();
         if (var24 != null) {
            this.disposeTree(var24);
         }

         var4.setUseShadowTree(var22);
         CSSUtilities.computeStyleAndURIs(var6, var11, var5);
         GVTBuilder var26 = var1.getGVTBuilder();
         GraphicsNode var27 = var26.build(var1, var11);
         var3.getChildren().add(var27);
         var3.setTransform(this.computeTransform((SVGTransformable)var2, var1));
         var3.setVisible(CSSUtilities.convertVisibility(var2));
         RenderingHints var17 = null;
         var17 = CSSUtilities.convertColorRendering(var2, var17);
         if (var17 != null) {
            var3.setRenderingHints(var17);
         }

         Rectangle2D var18 = CSSUtilities.convertEnableBackground(var2);
         if (var18 != null) {
            var3.setBackgroundEnable(var18);
         }

         NodeEventTarget var19;
         if (this.l != null) {
            var19 = this.l.target;
            var19.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.l, true);
            var19.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", this.l, true);
            var19.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.l, true);
            var19.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMCharacterDataModified", this.l, true);
            this.l = null;
         }

         if (var9 && var1.isDynamic()) {
            this.l = new ReferencedElementMutationListener();
            var19 = (NodeEventTarget)var6;
            this.l.target = var19;
            var19.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.l, true, (Object)null);
            var10.storeEventListenerNS(var19, "http://www.w3.org/2001/xml-events", "DOMAttrModified", this.l, true);
            var19.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", this.l, true, (Object)null);
            var10.storeEventListenerNS(var19, "http://www.w3.org/2001/xml-events", "DOMNodeInserted", this.l, true);
            var19.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.l, true, (Object)null);
            var10.storeEventListenerNS(var19, "http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.l, true);
            var19.addEventListenerNS("http://www.w3.org/2001/xml-events", "DOMCharacterDataModified", this.l, true, (Object)null);
            var10.storeEventListenerNS(var19, "http://www.w3.org/2001/xml-events", "DOMCharacterDataModified", this.l, true);
         }

         return var3;
      }
   }

   public void dispose() {
      if (this.l != null) {
         NodeEventTarget var1 = this.l.target;
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", this.l, true);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeInserted", this.l, true);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMNodeRemoved", this.l, true);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "DOMCharacterDataModified", this.l, true);
         this.l = null;
      }

      SVGOMUseElement var2 = (SVGOMUseElement)this.e;
      if (var2 != null && var2.getCSSFirstChild() != null) {
         this.disposeTree(var2.getCSSFirstChild());
      }

      super.dispose();
      this.subCtx = null;
   }

   protected AffineTransform computeTransform(SVGTransformable var1, BridgeContext var2) {
      AffineTransform var3 = super.computeTransform(var1, var2);
      SVGUseElement var4 = (SVGUseElement)var1;

      try {
         AbstractSVGAnimatedLength var5 = (AbstractSVGAnimatedLength)var4.getX();
         float var6 = var5.getCheckedValue();
         AbstractSVGAnimatedLength var7 = (AbstractSVGAnimatedLength)var4.getY();
         float var8 = var7.getCheckedValue();
         AffineTransform var9 = AffineTransform.getTranslateInstance((double)var6, (double)var8);
         var9.preConcatenate(var3);
         return var9;
      } catch (LiveAttributeException var10) {
         throw new BridgeException(var2, var10);
      }
   }

   protected GraphicsNode instantiateGraphicsNode() {
      return null;
   }

   public boolean isComposite() {
      return false;
   }

   public void buildGraphicsNode(BridgeContext var1, Element var2, GraphicsNode var3) {
      super.buildGraphicsNode(var1, var2, var3);
      if (var1.isInteractive()) {
         NodeEventTarget var4 = (NodeEventTarget)var2;
         CursorMouseOverListener var5 = new CursorMouseOverListener(var1);
         var4.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseover", var5, false, (Object)null);
         var1.storeEventListenerNS(var4, "http://www.w3.org/2001/xml-events", "mouseover", var5, false);
      }

   }

   public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue var1) {
      try {
         String var2 = var1.getNamespaceURI();
         String var3 = var1.getLocalName();
         if (var2 != null || !var3.equals("x") && !var3.equals("y") && !var3.equals("transform")) {
            if (var2 == null && (var3.equals("width") || var3.equals("height")) || var2.equals("http://www.w3.org/1999/xlink") && var3.equals("href")) {
               this.buildCompositeGraphicsNode(this.ctx, this.e, (CompositeGraphicsNode)this.node);
            }
         } else {
            this.node.setTransform(this.computeTransform((SVGTransformable)this.e, this.ctx));
            this.handleGeometryChanged();
         }
      } catch (LiveAttributeException var4) {
         throw new BridgeException(this.ctx, var4);
      }

      super.handleAnimatedAttributeChanged(var1);
   }

   protected class ReferencedElementMutationListener implements EventListener {
      protected NodeEventTarget target;

      public void handleEvent(Event var1) {
         SVGUseElementBridge.this.buildCompositeGraphicsNode(SVGUseElementBridge.this.ctx, SVGUseElementBridge.this.e, (CompositeGraphicsNode)SVGUseElementBridge.this.node);
      }
   }

   public static class CursorMouseOverListener implements EventListener {
      protected BridgeContext ctx;

      public CursorMouseOverListener(BridgeContext var1) {
         this.ctx = var1;
      }

      public void handleEvent(Event var1) {
         Element var2 = (Element)var1.getCurrentTarget();
         if (!CSSUtilities.isAutoCursor(var2)) {
            Cursor var3 = CSSUtilities.convertCursor(var2, this.ctx);
            if (var3 != null) {
               this.ctx.getUserAgent().setSVGCursor(var3);
            }
         }

      }
   }
}

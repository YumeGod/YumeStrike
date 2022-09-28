package org.apache.batik.bridge;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.lang.ref.SoftReference;
import org.apache.batik.css.engine.CSSEngineEvent;
import org.apache.batik.css.engine.SVGCSSEngine;
import org.apache.batik.dom.events.AbstractEvent;
import org.apache.batik.dom.svg.AbstractSVGTransformList;
import org.apache.batik.dom.svg.AnimatedLiveAttributeValue;
import org.apache.batik.dom.svg.LiveAttributeException;
import org.apache.batik.dom.svg.SVGContext;
import org.apache.batik.dom.svg.SVGMotionAnimatableElement;
import org.apache.batik.dom.svg.SVGOMAnimatedTransformList;
import org.apache.batik.dom.svg.SVGOMElement;
import org.apache.batik.ext.awt.geom.SegmentList;
import org.apache.batik.gvt.CanvasGraphicsNode;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.svg.SVGFitToViewBox;
import org.w3c.dom.svg.SVGTransformable;

public abstract class AbstractGraphicsNodeBridge extends AnimatableSVGBridge implements SVGContext, BridgeUpdateHandler, GraphicsNodeBridge, ErrorConstants {
   protected GraphicsNode node;
   protected boolean isSVG12;
   protected org.apache.batik.parser.UnitProcessor.Context unitContext;
   protected SoftReference bboxShape = null;
   protected Rectangle2D bbox = null;

   protected AbstractGraphicsNodeBridge() {
   }

   public GraphicsNode createGraphicsNode(BridgeContext var1, Element var2) {
      if (!SVGUtilities.matchUserAgent(var2, var1.getUserAgent())) {
         return null;
      } else {
         GraphicsNode var3 = this.instantiateGraphicsNode();
         this.setTransform(var3, var2, var1);
         var3.setVisible(CSSUtilities.convertVisibility(var2));
         this.associateSVGContext(var1, var2, var3);
         return var3;
      }
   }

   protected abstract GraphicsNode instantiateGraphicsNode();

   public void buildGraphicsNode(BridgeContext var1, Element var2, GraphicsNode var3) {
      var3.setComposite(CSSUtilities.convertOpacity(var2));
      var3.setFilter(CSSUtilities.convertFilter(var2, var3, var1));
      var3.setMask(CSSUtilities.convertMask(var2, var3, var1));
      var3.setClip(CSSUtilities.convertClipPath(var2, var3, var1));
      var3.setPointerEventType(CSSUtilities.convertPointerEvents(var2));
      this.initializeDynamicSupport(var1, var2, var3);
   }

   public boolean getDisplay(Element var1) {
      return CSSUtilities.convertDisplay(var1);
   }

   protected AffineTransform computeTransform(SVGTransformable var1, BridgeContext var2) {
      try {
         AffineTransform var3 = new AffineTransform();
         SVGOMAnimatedTransformList var4 = (SVGOMAnimatedTransformList)var1.getTransform();
         if (var4.isSpecified()) {
            var4.check();
            AbstractSVGTransformList var5 = (AbstractSVGTransformList)var1.getTransform().getAnimVal();
            var3.concatenate(var5.getAffineTransform());
         }

         if (this.e instanceof SVGMotionAnimatableElement) {
            SVGMotionAnimatableElement var8 = (SVGMotionAnimatableElement)this.e;
            AffineTransform var6 = var8.getMotionTransform();
            if (var6 != null) {
               var3.concatenate(var6);
            }
         }

         return var3;
      } catch (LiveAttributeException var7) {
         throw new BridgeException(var2, var7);
      }
   }

   protected void setTransform(GraphicsNode var1, Element var2, BridgeContext var3) {
      var1.setTransform(this.computeTransform((SVGTransformable)var2, var3));
   }

   protected void associateSVGContext(BridgeContext var1, Element var2, GraphicsNode var3) {
      this.e = var2;
      this.node = var3;
      this.ctx = var1;
      this.unitContext = UnitProcessor.createContext(var1, var2);
      this.isSVG12 = var1.isSVG12();
      ((SVGOMElement)var2).setSVGContext(this);
   }

   protected void initializeDynamicSupport(BridgeContext var1, Element var2, GraphicsNode var3) {
      if (var1.isInteractive()) {
         var1.bind(var2, var3);
      }

   }

   public void handleDOMAttrModifiedEvent(MutationEvent var1) {
   }

   protected void handleGeometryChanged() {
      this.node.setFilter(CSSUtilities.convertFilter(this.e, this.node, this.ctx));
      this.node.setMask(CSSUtilities.convertMask(this.e, this.node, this.ctx));
      this.node.setClip(CSSUtilities.convertClipPath(this.e, this.node, this.ctx));
      if (this.isSVG12) {
         if (!"use".equals(this.e.getLocalName())) {
            this.fireShapeChangeEvent();
         }

         this.fireBBoxChangeEvent();
      }

   }

   protected void fireShapeChangeEvent() {
      DocumentEvent var1 = (DocumentEvent)this.e.getOwnerDocument();
      AbstractEvent var2 = (AbstractEvent)var1.createEvent("SVGEvents");
      var2.initEventNS("http://www.w3.org/2000/svg", "shapechange", true, false);

      try {
         ((EventTarget)this.e).dispatchEvent(var2);
      } catch (RuntimeException var4) {
         this.ctx.getUserAgent().displayError(var4);
      }

   }

   public void handleDOMNodeInsertedEvent(MutationEvent var1) {
      if (var1.getTarget() instanceof Element) {
         Element var2 = (Element)var1.getTarget();
         Bridge var3 = this.ctx.getBridge(var2);
         if (var3 instanceof GenericBridge) {
            ((GenericBridge)var3).handleElement(this.ctx, var2);
         }
      }

   }

   public void handleDOMNodeRemovedEvent(MutationEvent var1) {
      Node var2 = this.e.getParentNode();
      if (var2 instanceof SVGOMElement) {
         SVGContext var3 = ((SVGOMElement)var2).getSVGContext();
         if (var3 instanceof SVGSwitchElementBridge) {
            ((SVGSwitchElementBridge)var3).handleChildElementRemoved(this.e);
            return;
         }
      }

      CompositeGraphicsNode var4 = this.node.getParent();
      var4.remove(this.node);
      this.disposeTree(this.e);
   }

   public void handleDOMCharacterDataModified(MutationEvent var1) {
   }

   public void dispose() {
      SVGOMElement var1 = (SVGOMElement)this.e;
      var1.setSVGContext((SVGContext)null);
      this.ctx.unbind(this.e);
      this.bboxShape = null;
   }

   protected void disposeTree(Node var1) {
      this.disposeTree(var1, true);
   }

   protected void disposeTree(Node var1, boolean var2) {
      if (var1 instanceof SVGOMElement) {
         SVGOMElement var3 = (SVGOMElement)var1;
         SVGContext var4 = var3.getSVGContext();
         if (var4 instanceof BridgeUpdateHandler) {
            BridgeUpdateHandler var5 = (BridgeUpdateHandler)var4;
            if (var2) {
               var3.setSVGContext((SVGContext)null);
            }

            var5.dispose();
         }
      }

      for(Node var6 = var1.getFirstChild(); var6 != null; var6 = var6.getNextSibling()) {
         this.disposeTree(var6, var2);
      }

   }

   public void handleCSSEngineEvent(CSSEngineEvent var1) {
      try {
         SVGCSSEngine var2 = (SVGCSSEngine)var1.getSource();
         int[] var3 = var1.getProperties();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            int var5 = var3[var4];
            this.handleCSSPropertyChanged(var5);
            String var6 = var2.getPropertyName(var5);
            this.fireBaseAttributeListeners(var6);
         }
      } catch (Exception var7) {
         this.ctx.getUserAgent().displayError(var7);
      }

   }

   protected void handleCSSPropertyChanged(int var1) {
      switch (var1) {
         case 3:
            this.node.setClip(CSSUtilities.convertClipPath(this.e, this.node, this.ctx));
            break;
         case 12:
            if (!this.getDisplay(this.e)) {
               CompositeGraphicsNode var2 = this.node.getParent();
               var2.remove(this.node);
               this.disposeTree(this.e, false);
            }
            break;
         case 18:
            this.node.setFilter(CSSUtilities.convertFilter(this.e, this.node, this.ctx));
            break;
         case 37:
            this.node.setMask(CSSUtilities.convertMask(this.e, this.node, this.ctx));
            break;
         case 38:
            this.node.setComposite(CSSUtilities.convertOpacity(this.e));
            break;
         case 40:
            this.node.setPointerEventType(CSSUtilities.convertPointerEvents(this.e));
            break;
         case 57:
            this.node.setVisible(CSSUtilities.convertVisibility(this.e));
      }

   }

   public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue var1) {
      if (var1.getNamespaceURI() == null && var1.getLocalName().equals("transform")) {
         this.setTransform(this.node, this.e, this.ctx);
         this.handleGeometryChanged();
      }

   }

   public void handleOtherAnimationChanged(String var1) {
      if (var1.equals("motion")) {
         this.setTransform(this.node, this.e, this.ctx);
         this.handleGeometryChanged();
      }

   }

   protected void checkBBoxChange() {
      if (this.e != null) {
         this.fireBBoxChangeEvent();
      }

   }

   protected void fireBBoxChangeEvent() {
      DocumentEvent var1 = (DocumentEvent)this.e.getOwnerDocument();
      AbstractEvent var2 = (AbstractEvent)var1.createEvent("SVGEvents");
      var2.initEventNS("http://www.w3.org/2000/svg", "RenderedBBoxChange", true, false);

      try {
         ((EventTarget)this.e).dispatchEvent(var2);
      } catch (RuntimeException var4) {
         this.ctx.getUserAgent().displayError(var4);
      }

   }

   public float getPixelUnitToMillimeter() {
      return this.ctx.getUserAgent().getPixelUnitToMillimeter();
   }

   public float getPixelToMM() {
      return this.getPixelUnitToMillimeter();
   }

   public Rectangle2D getBBox() {
      if (this.node == null) {
         return null;
      } else {
         Shape var1 = this.node.getOutline();
         if (this.bboxShape != null && var1 == this.bboxShape.get()) {
            return this.bbox;
         } else {
            this.bboxShape = new SoftReference(var1);
            this.bbox = null;
            if (var1 == null) {
               return this.bbox;
            } else {
               SegmentList var2 = new SegmentList(var1);
               this.bbox = var2.getBounds2D();
               return this.bbox;
            }
         }
      }
   }

   public AffineTransform getCTM() {
      Object var1 = this.node;
      AffineTransform var2 = new AffineTransform();

      for(Object var3 = this.e; var3 != null; var1 = ((GraphicsNode)var1).getParent()) {
         AffineTransform var4;
         if (var3 instanceof SVGFitToViewBox) {
            if (var1 instanceof CanvasGraphicsNode) {
               var4 = ((CanvasGraphicsNode)var1).getViewingTransform();
            } else {
               var4 = ((GraphicsNode)var1).getTransform();
            }

            if (var4 != null) {
               var2.preConcatenate(var4);
            }
            break;
         }

         var4 = ((GraphicsNode)var1).getTransform();
         if (var4 != null) {
            var2.preConcatenate(var4);
         }

         var3 = SVGCSSEngine.getParentCSSStylableElement((Element)var3);
      }

      return var2;
   }

   public AffineTransform getScreenTransform() {
      return this.ctx.getUserAgent().getTransform();
   }

   public void setScreenTransform(AffineTransform var1) {
      this.ctx.getUserAgent().setTransform(var1);
   }

   public AffineTransform getGlobalTransform() {
      return this.node.getGlobalTransform();
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

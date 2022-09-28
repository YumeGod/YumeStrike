package org.apache.batik.bridge;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.apache.batik.css.engine.CSSEngineEvent;
import org.apache.batik.dom.svg.AnimatedLiveAttributeValue;
import org.apache.batik.dom.svg.SVGContext;
import org.apache.batik.dom.svg.SVGOMElement;
import org.w3c.dom.Element;
import org.w3c.dom.events.MutationEvent;

public abstract class AnimatableGenericSVGBridge extends AnimatableSVGBridge implements GenericBridge, BridgeUpdateHandler, SVGContext {
   public void handleElement(BridgeContext var1, Element var2) {
      if (var1.isDynamic()) {
         this.e = var2;
         this.ctx = var1;
         ((SVGOMElement)var2).setSVGContext(this);
      }

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

   public AffineTransform getScreenTransform() {
      return this.ctx.getUserAgent().getTransform();
   }

   public void setScreenTransform(AffineTransform var1) {
      this.ctx.getUserAgent().setTransform(var1);
   }

   public AffineTransform getCTM() {
      return null;
   }

   public AffineTransform getGlobalTransform() {
      return null;
   }

   public float getViewportWidth() {
      return 0.0F;
   }

   public float getViewportHeight() {
      return 0.0F;
   }

   public float getFontSize() {
      return 0.0F;
   }

   public void dispose() {
      ((SVGOMElement)this.e).setSVGContext((SVGContext)null);
   }

   public void handleDOMNodeInsertedEvent(MutationEvent var1) {
   }

   public void handleDOMCharacterDataModified(MutationEvent var1) {
   }

   public void handleDOMNodeRemovedEvent(MutationEvent var1) {
      this.dispose();
   }

   public void handleDOMAttrModifiedEvent(MutationEvent var1) {
   }

   public void handleCSSEngineEvent(CSSEngineEvent var1) {
   }

   public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue var1) {
   }

   public void handleOtherAnimationChanged(String var1) {
   }
}

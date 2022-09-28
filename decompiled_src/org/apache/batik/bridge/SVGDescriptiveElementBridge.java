package org.apache.batik.bridge;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.apache.batik.css.engine.CSSEngineEvent;
import org.apache.batik.dom.svg.AnimatedLiveAttributeValue;
import org.apache.batik.dom.svg.SVGContext;
import org.apache.batik.dom.svg.SVGOMElement;
import org.w3c.dom.Element;
import org.w3c.dom.events.MutationEvent;

public abstract class SVGDescriptiveElementBridge extends AbstractSVGBridge implements GenericBridge, BridgeUpdateHandler, SVGContext {
   Element theElt;
   Element parent;
   BridgeContext theCtx;

   public void handleElement(BridgeContext var1, Element var2) {
      UserAgent var3 = var1.getUserAgent();
      var3.handleElement(var2, Boolean.TRUE);
      if (var1.isDynamic()) {
         SVGDescriptiveElementBridge var4 = (SVGDescriptiveElementBridge)this.getInstance();
         var4.theElt = var2;
         var4.parent = (Element)var2.getParentNode();
         var4.theCtx = var1;
         ((SVGOMElement)var2).setSVGContext(var4);
      }

   }

   public void dispose() {
      UserAgent var1 = this.theCtx.getUserAgent();
      ((SVGOMElement)this.theElt).setSVGContext((SVGContext)null);
      var1.handleElement(this.theElt, this.parent);
      this.theElt = null;
      this.parent = null;
   }

   public void handleDOMNodeInsertedEvent(MutationEvent var1) {
      UserAgent var2 = this.theCtx.getUserAgent();
      var2.handleElement(this.theElt, Boolean.TRUE);
   }

   public void handleDOMCharacterDataModified(MutationEvent var1) {
      UserAgent var2 = this.theCtx.getUserAgent();
      var2.handleElement(this.theElt, Boolean.TRUE);
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

   public float getPixelUnitToMillimeter() {
      return this.theCtx.getUserAgent().getPixelUnitToMillimeter();
   }

   public float getPixelToMM() {
      return this.getPixelUnitToMillimeter();
   }

   public Rectangle2D getBBox() {
      return null;
   }

   public AffineTransform getScreenTransform() {
      return this.theCtx.getUserAgent().getTransform();
   }

   public void setScreenTransform(AffineTransform var1) {
      this.theCtx.getUserAgent().setTransform(var1);
   }

   public AffineTransform getCTM() {
      return null;
   }

   public AffineTransform getGlobalTransform() {
      return null;
   }

   public float getViewportWidth() {
      return this.theCtx.getBlockWidth(this.theElt);
   }

   public float getViewportHeight() {
      return this.theCtx.getBlockHeight(this.theElt);
   }

   public float getFontSize() {
      return 0.0F;
   }
}

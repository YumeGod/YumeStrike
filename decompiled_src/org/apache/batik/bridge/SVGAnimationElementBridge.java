package org.apache.batik.bridge;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import org.apache.batik.anim.AbstractAnimation;
import org.apache.batik.anim.timing.TimedElement;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.css.engine.CSSEngineEvent;
import org.apache.batik.dom.anim.AnimatableElement;
import org.apache.batik.dom.anim.AnimationTarget;
import org.apache.batik.dom.anim.AnimationTargetListener;
import org.apache.batik.dom.svg.AnimatedLiveAttributeValue;
import org.apache.batik.dom.svg.SVGAnimationContext;
import org.apache.batik.dom.svg.SVGContext;
import org.apache.batik.dom.svg.SVGOMElement;
import org.apache.batik.dom.util.XLinkSupport;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.svg.SVGElement;

public abstract class SVGAnimationElementBridge extends AbstractSVGBridge implements GenericBridge, BridgeUpdateHandler, SVGAnimationContext, AnimatableElement {
   protected SVGOMElement element;
   protected BridgeContext ctx;
   protected SVGAnimationEngine eng;
   protected TimedElement timedElement;
   protected AbstractAnimation animation;
   protected String attributeNamespaceURI;
   protected String attributeLocalName;
   protected short animationType;
   protected SVGOMElement targetElement;
   protected AnimationTarget animationTarget;

   public TimedElement getTimedElement() {
      return this.timedElement;
   }

   public AnimatableValue getUnderlyingValue() {
      return this.animationType == 0 ? this.animationTarget.getUnderlyingValue(this.attributeNamespaceURI, this.attributeLocalName) : this.eng.getUnderlyingCSSValue(this.element, this.animationTarget, this.attributeLocalName);
   }

   public void handleElement(BridgeContext var1, Element var2) {
      if (var1.isDynamic() && BridgeContext.getSVGContext(var2) == null) {
         SVGAnimationElementBridge var3 = (SVGAnimationElementBridge)this.getInstance();
         var3.element = (SVGOMElement)var2;
         var3.ctx = var1;
         var3.eng = var1.getAnimationEngine();
         var3.element.setSVGContext(var3);
         if (var3.eng.hasStarted()) {
            var3.initializeAnimation();
            var3.initializeTimedElement();
         } else {
            var3.eng.addInitialBridge(var3);
         }
      }

   }

   protected void initializeAnimation() {
      String var1 = XLinkSupport.getXLinkHref(this.element);
      Object var2;
      if (var1.length() == 0) {
         var2 = this.element.getParentNode();
      } else {
         var2 = this.ctx.getReferencedElement(this.element, var1);
         if (((Node)var2).getOwnerDocument() != this.element.getOwnerDocument()) {
            throw new BridgeException(this.ctx, this.element, "uri.badTarget", new Object[]{var1});
         }
      }

      this.animationTarget = null;
      if (var2 instanceof SVGOMElement) {
         this.targetElement = (SVGOMElement)var2;
         this.animationTarget = this.targetElement;
      }

      if (this.animationTarget == null) {
         throw new BridgeException(this.ctx, this.element, "uri.badTarget", new Object[]{var1});
      } else {
         String var3 = this.element.getAttributeNS((String)null, "attributeName");
         int var4 = var3.indexOf(58);
         if (var4 == -1) {
            if (this.element.hasProperty(var3)) {
               this.animationType = 1;
               this.attributeLocalName = var3;
            } else {
               this.animationType = 0;
               this.attributeLocalName = var3;
            }
         } else {
            this.animationType = 0;
            String var5 = var3.substring(0, var4);
            this.attributeNamespaceURI = this.element.lookupNamespaceURI(var5);
            this.attributeLocalName = var3.substring(var4 + 1);
         }

         if ((this.animationType != 1 || this.targetElement.isPropertyAnimatable(this.attributeLocalName)) && (this.animationType != 0 || this.targetElement.isAttributeAnimatable(this.attributeNamespaceURI, this.attributeLocalName))) {
            int var6;
            if (this.animationType == 1) {
               var6 = this.targetElement.getPropertyType(this.attributeLocalName);
            } else {
               var6 = this.targetElement.getAttributeType(this.attributeNamespaceURI, this.attributeLocalName);
            }

            if (!this.canAnimateType(var6)) {
               throw new BridgeException(this.ctx, this.element, "type.not.animatable", new Object[]{this.targetElement.getNodeName(), var3, this.element.getNodeName()});
            } else {
               this.timedElement = this.createTimedElement();
               this.animation = this.createAnimation(this.animationTarget);
               this.eng.addAnimation(this.animationTarget, this.animationType, this.attributeNamespaceURI, this.attributeLocalName, this.animation);
            }
         } else {
            throw new BridgeException(this.ctx, this.element, "attribute.not.animatable", new Object[]{this.targetElement.getNodeName(), var3});
         }
      }
   }

   protected abstract boolean canAnimateType(int var1);

   protected boolean checkValueType(AnimatableValue var1) {
      return true;
   }

   protected void initializeTimedElement() {
      this.initializeTimedElement(this.timedElement);
      this.timedElement.initialize();
   }

   protected TimedElement createTimedElement() {
      return new SVGTimedElement();
   }

   protected abstract AbstractAnimation createAnimation(AnimationTarget var1);

   protected AnimatableValue parseAnimatableValue(String var1) {
      if (!this.element.hasAttributeNS((String)null, var1)) {
         return null;
      } else {
         String var2 = this.element.getAttributeNS((String)null, var1);
         AnimatableValue var3 = this.eng.parseAnimatableValue(this.element, this.animationTarget, this.attributeNamespaceURI, this.attributeLocalName, this.animationType == 1, var2);
         if (!this.checkValueType(var3)) {
            throw new BridgeException(this.ctx, this.element, "attribute.malformed", new Object[]{var1, var2});
         } else {
            return var3;
         }
      }
   }

   protected void initializeTimedElement(TimedElement var1) {
      var1.parseAttributes(this.element.getAttributeNS((String)null, "begin"), this.element.getAttributeNS((String)null, "dur"), this.element.getAttributeNS((String)null, "end"), this.element.getAttributeNS((String)null, "min"), this.element.getAttributeNS((String)null, "max"), this.element.getAttributeNS((String)null, "repeatCount"), this.element.getAttributeNS((String)null, "repeatDur"), this.element.getAttributeNS((String)null, "fill"), this.element.getAttributeNS((String)null, "restart"));
   }

   public void handleDOMAttrModifiedEvent(MutationEvent var1) {
   }

   public void handleDOMNodeInsertedEvent(MutationEvent var1) {
   }

   public void handleDOMNodeRemovedEvent(MutationEvent var1) {
      this.element.setSVGContext((SVGContext)null);
      this.dispose();
   }

   public void handleDOMCharacterDataModified(MutationEvent var1) {
   }

   public void handleCSSEngineEvent(CSSEngineEvent var1) {
   }

   public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue var1) {
   }

   public void handleOtherAnimationChanged(String var1) {
   }

   public void dispose() {
      if (this.element.getSVGContext() == null) {
         this.eng.removeAnimation(this.animation);
         this.timedElement.deinitialize();
         this.timedElement = null;
         this.element = null;
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
      return this.ctx.getBlockWidth(this.element);
   }

   public float getViewportHeight() {
      return this.ctx.getBlockHeight(this.element);
   }

   public float getFontSize() {
      return 0.0F;
   }

   public float svgToUserSpace(float var1, int var2, int var3) {
      return 0.0F;
   }

   public void addTargetListener(String var1, AnimationTargetListener var2) {
   }

   public void removeTargetListener(String var1, AnimationTargetListener var2) {
   }

   public SVGElement getTargetElement() {
      return this.targetElement;
   }

   public float getStartTime() {
      return this.timedElement.getCurrentBeginTime();
   }

   public float getCurrentTime() {
      return this.timedElement.getLastSampleTime();
   }

   public float getSimpleDuration() {
      return this.timedElement.getSimpleDur();
   }

   public float getHyperlinkBeginTime() {
      return this.timedElement.getHyperlinkBeginTime();
   }

   public boolean beginElement() throws DOMException {
      this.timedElement.beginElement();
      return this.timedElement.canBegin();
   }

   public boolean beginElementAt(float var1) throws DOMException {
      this.timedElement.beginElement(var1);
      return true;
   }

   public boolean endElement() throws DOMException {
      this.timedElement.endElement();
      return this.timedElement.canEnd();
   }

   public boolean endElementAt(float var1) throws DOMException {
      this.timedElement.endElement(var1);
      return true;
   }

   protected boolean isConstantAnimation() {
      return false;
   }

   protected class SVGTimedElement extends TimedElement {
      public Element getElement() {
         return SVGAnimationElementBridge.this.element;
      }

      protected void fireTimeEvent(String var1, Calendar var2, int var3) {
         AnimationSupport.fireTimeEvent(SVGAnimationElementBridge.this.element, var1, var2, var3);
      }

      protected void toActive(float var1) {
         SVGAnimationElementBridge.this.eng.toActive(SVGAnimationElementBridge.this.animation, var1);
      }

      protected void toInactive(boolean var1, boolean var2) {
         SVGAnimationElementBridge.this.eng.toInactive(SVGAnimationElementBridge.this.animation, var2);
      }

      protected void removeFill() {
         SVGAnimationElementBridge.this.eng.removeFill(SVGAnimationElementBridge.this.animation);
      }

      protected void sampledAt(float var1, float var2, int var3) {
         SVGAnimationElementBridge.this.eng.sampledAt(SVGAnimationElementBridge.this.animation, var1, var2, var3);
      }

      protected void sampledLastValue(int var1) {
         SVGAnimationElementBridge.this.eng.sampledLastValue(SVGAnimationElementBridge.this.animation, var1);
      }

      protected TimedElement getTimedElementById(String var1) {
         return AnimationSupport.getTimedElementById(var1, SVGAnimationElementBridge.this.element);
      }

      protected EventTarget getEventTargetById(String var1) {
         return AnimationSupport.getEventTargetById(var1, SVGAnimationElementBridge.this.element);
      }

      protected EventTarget getRootEventTarget() {
         return (EventTarget)SVGAnimationElementBridge.this.element.getOwnerDocument();
      }

      protected EventTarget getAnimationEventTarget() {
         return SVGAnimationElementBridge.this.targetElement;
      }

      public boolean isBefore(TimedElement var1) {
         Element var2 = ((SVGTimedElement)var1).getElement();
         short var3 = SVGAnimationElementBridge.this.element.compareDocumentPosition(var2);
         return (var3 & 2) != 0;
      }

      public String toString() {
         if (SVGAnimationElementBridge.this.element != null) {
            String var1 = SVGAnimationElementBridge.this.element.getAttributeNS((String)null, "id");
            if (var1.length() != 0) {
               return var1;
            }
         }

         return super.toString();
      }

      protected boolean isConstantAnimation() {
         return SVGAnimationElementBridge.this.isConstantAnimation();
      }
   }
}

package org.apache.batik.bridge;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.apache.batik.css.engine.CSSEngineEvent;
import org.apache.batik.dom.svg.AnimatedLiveAttributeValue;
import org.apache.batik.dom.svg.SVGContext;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.RootGraphicsNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.MutationEvent;

public class SVGDocumentBridge implements DocumentBridge, BridgeUpdateHandler, SVGContext {
   protected Document document;
   protected RootGraphicsNode node;
   protected BridgeContext ctx;

   public String getNamespaceURI() {
      return null;
   }

   public String getLocalName() {
      return null;
   }

   public Bridge getInstance() {
      return new SVGDocumentBridge();
   }

   public RootGraphicsNode createGraphicsNode(BridgeContext var1, Document var2) {
      RootGraphicsNode var3 = new RootGraphicsNode();
      this.document = var2;
      this.node = var3;
      this.ctx = var1;
      ((SVGOMDocument)var2).setSVGContext(this);
      return var3;
   }

   public void buildGraphicsNode(BridgeContext var1, Document var2, RootGraphicsNode var3) {
      if (var1.isDynamic()) {
         var1.bind(var2, var3);
      }

   }

   public void handleDOMAttrModifiedEvent(MutationEvent var1) {
   }

   public void handleDOMNodeInsertedEvent(MutationEvent var1) {
      if (var1.getTarget() instanceof Element) {
         Element var2 = (Element)var1.getTarget();
         GVTBuilder var3 = this.ctx.getGVTBuilder();
         GraphicsNode var4 = var3.build(this.ctx, var2);
         if (var4 == null) {
            return;
         }

         this.node.add(var4);
      }

   }

   public void handleDOMNodeRemovedEvent(MutationEvent var1) {
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
      ((SVGOMDocument)this.document).setSVGContext((SVGContext)null);
      this.ctx.unbind(this.document);
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
}

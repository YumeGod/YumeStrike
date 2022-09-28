package org.apache.batik.bridge;

import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.color.ICC_Profile;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.events.DOMMouseEvent;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.dom.svg.AbstractSVGAnimatedLength;
import org.apache.batik.dom.svg.AnimatedLiveAttributeValue;
import org.apache.batik.dom.svg.LiveAttributeException;
import org.apache.batik.dom.svg.SVGOMAnimatedPreserveAspectRatio;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.svg.SVGOMElement;
import org.apache.batik.ext.awt.color.ICCColorSpaceExt;
import org.apache.batik.ext.awt.image.renderable.ClipRable;
import org.apache.batik.ext.awt.image.renderable.ClipRable8Bit;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.spi.BrokenLinkProvider;
import org.apache.batik.ext.awt.image.spi.ImageTagRegistry;
import org.apache.batik.gvt.CanvasGraphicsNode;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.ImageNode;
import org.apache.batik.gvt.RasterImageNode;
import org.apache.batik.gvt.ShapeNode;
import org.apache.batik.util.HaltingThread;
import org.apache.batik.util.MimeTypeConstants;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGImageElement;
import org.w3c.dom.svg.SVGSVGElement;

public class SVGImageElementBridge extends AbstractGraphicsNodeBridge {
   protected SVGDocument imgDocument;
   protected EventListener listener = null;
   protected BridgeContext subCtx = null;
   protected boolean hitCheckChildren = false;
   static SVGBrokenLinkProvider brokenLinkProvider = new SVGBrokenLinkProvider();

   public String getLocalName() {
      return "image";
   }

   public Bridge getInstance() {
      return new SVGImageElementBridge();
   }

   public GraphicsNode createGraphicsNode(BridgeContext var1, Element var2) {
      ImageNode var3 = (ImageNode)super.createGraphicsNode(var1, var2);
      if (var3 == null) {
         return null;
      } else {
         this.associateSVGContext(var1, var2, var3);
         this.hitCheckChildren = false;
         GraphicsNode var4 = this.buildImageGraphicsNode(var1, var2);
         if (var4 == null) {
            SVGImageElement var7 = (SVGImageElement)var2;
            String var6 = var7.getHref().getAnimVal();
            throw new BridgeException(var1, var2, "uri.image.invalid", new Object[]{var6});
         } else {
            var3.setImage(var4);
            var3.setHitCheckChildren(this.hitCheckChildren);
            RenderingHints var5 = null;
            var5 = CSSUtilities.convertImageRendering(var2, var5);
            var5 = CSSUtilities.convertColorRendering(var2, var5);
            if (var5 != null) {
               var3.setRenderingHints(var5);
            }

            return var3;
         }
      }
   }

   protected GraphicsNode buildImageGraphicsNode(BridgeContext var1, Element var2) {
      SVGImageElement var3 = (SVGImageElement)var2;
      String var4 = var3.getHref().getAnimVal();
      if (var4.length() == 0) {
         throw new BridgeException(var1, var2, "attribute.missing", new Object[]{"xlink:href"});
      } else if (var4.indexOf(35) != -1) {
         throw new BridgeException(var1, var2, "attribute.malformed", new Object[]{"xlink:href", var4});
      } else {
         String var5 = AbstractNode.getBaseURI(var2);
         ParsedURL var6;
         if (var5 == null) {
            var6 = new ParsedURL(var4);
         } else {
            var6 = new ParsedURL(var5, var4);
         }

         return this.createImageGraphicsNode(var1, var2, var6);
      }
   }

   protected GraphicsNode createImageGraphicsNode(BridgeContext var1, Element var2, ParsedURL var3) {
      Rectangle2D var4 = getImageBounds(var1, var2);
      if (var4.getWidth() != 0.0 && var4.getHeight() != 0.0) {
         SVGDocument var46 = (SVGDocument)var2.getOwnerDocument();
         String var6 = var46.getURL();
         ParsedURL var7 = null;
         if (var6 != null) {
            var7 = new ParsedURL(var6);
         }

         UserAgent var8 = var1.getUserAgent();

         try {
            var8.checkLoadExternalResource(var3, var7);
         } catch (SecurityException var39) {
            throw new BridgeException(var1, var2, var39, "uri.unsecure", new Object[]{var3});
         }

         DocumentLoader var9 = var1.getDocumentLoader();
         ImageTagRegistry var10 = ImageTagRegistry.getRegistry();
         ICCColorSpaceExt var11 = extractColorSpace(var2, var1);

         Document var12;
         try {
            var12 = var9.checkCache(var3.toString());
            if (var12 != null) {
               this.imgDocument = (SVGDocument)var12;
               return this.createSVGImageNode(var1, var2, this.imgDocument);
            }
         } catch (BridgeException var37) {
            throw var37;
         } catch (Exception var38) {
         }

         Filter var47 = var10.checkCache(var3, var11);
         if (var47 != null) {
            return this.createRasterImageNode(var1, var2, var47, var3);
         } else {
            var12 = null;

            ProtectedStream var48;
            try {
               var48 = this.openStream(var2, var3);
            } catch (SecurityException var35) {
               throw new BridgeException(var1, var2, var35, "uri.unsecure", new Object[]{var3});
            } catch (IOException var36) {
               return this.createBrokenImageNode(var1, var2, var3.toString(), var36.getLocalizedMessage());
            }

            Filter var13 = var10.readURL(var48, var3, var11, false, false);
            if (var13 != null) {
               return this.createRasterImageNode(var1, var2, var13, var3);
            } else {
               try {
                  var48.retry();
               } catch (IOException var34) {
                  var48.release();
                  var12 = null;

                  try {
                     var48 = this.openStream(var2, var3);
                  } catch (IOException var33) {
                     return this.createBrokenImageNode(var1, var2, var3.toString(), var33.getLocalizedMessage());
                  }
               }

               try {
                  Document var49 = var9.loadDocument(var3.toString(), var48);
                  this.imgDocument = (SVGDocument)var49;
                  return this.createSVGImageNode(var1, var2, this.imgDocument);
               } catch (BridgeException var41) {
                  throw var41;
               } catch (SecurityException var42) {
                  throw new BridgeException(var1, var2, var42, "uri.unsecure", new Object[]{var3});
               } catch (InterruptedIOException var43) {
                  if (HaltingThread.hasBeenHalted()) {
                     throw new InterruptedBridgeException();
                  }
               } catch (InterruptedBridgeException var44) {
                  throw var44;
               } catch (Exception var45) {
               }

               try {
                  var48.retry();
               } catch (IOException var32) {
                  var48.release();
                  var12 = null;

                  try {
                     var48 = this.openStream(var2, var3);
                  } catch (IOException var31) {
                     return this.createBrokenImageNode(var1, var2, var3.toString(), var31.getLocalizedMessage());
                  }
               }

               GraphicsNode var14;
               try {
                  var13 = var10.readURL(var48, var3, var11, true, true);
                  if (var13 == null) {
                     return null;
                  }

                  var14 = this.createRasterImageNode(var1, var2, var13, var3);
               } finally {
                  var48.release();
               }

               return var14;
            }
         }
      } else {
         ShapeNode var5 = new ShapeNode();
         var5.setShape(var4);
         return var5;
      }
   }

   protected ProtectedStream openStream(Element var1, ParsedURL var2) throws IOException {
      ArrayList var3 = new ArrayList(ImageTagRegistry.getRegistry().getRegisteredMimeTypes());
      var3.add(MimeTypeConstants.MIME_TYPES_SVG);
      InputStream var4 = var2.openStream(var3.iterator());
      return new ProtectedStream(var4);
   }

   protected GraphicsNode instantiateGraphicsNode() {
      return new ImageNode();
   }

   public boolean isComposite() {
      return false;
   }

   protected void initializeDynamicSupport(BridgeContext var1, Element var2, GraphicsNode var3) {
      if (var1.isInteractive()) {
         var1.bind(var2, var3);
         if (var1.isDynamic()) {
            this.e = var2;
            this.node = var3;
            this.ctx = var1;
            ((SVGOMElement)var2).setSVGContext(this);
         }

      }
   }

   public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue var1) {
      try {
         String var2 = var1.getNamespaceURI();
         String var3 = var1.getLocalName();
         if (var2 == null) {
            label50: {
               if (!var3.equals("x") && !var3.equals("y")) {
                  if (!var3.equals("width") && !var3.equals("height")) {
                     if (var3.equals("preserveAspectRatio")) {
                        this.updateImageBounds();
                        return;
                     }
                     break label50;
                  }

                  SVGImageElement var4 = (SVGImageElement)this.e;
                  ImageNode var5 = (ImageNode)this.node;
                  AbstractSVGAnimatedLength var6;
                  if (var3.charAt(0) == 'w') {
                     var6 = (AbstractSVGAnimatedLength)var4.getWidth();
                  } else {
                     var6 = (AbstractSVGAnimatedLength)var4.getHeight();
                  }

                  float var7 = var6.getCheckedValue();
                  if (var7 != 0.0F && !(var5.getImage() instanceof ShapeNode)) {
                     this.updateImageBounds();
                  } else {
                     this.rebuildImageNode();
                  }

                  return;
               }

               this.updateImageBounds();
               return;
            }
         } else if (var2.equals("http://www.w3.org/1999/xlink") && var3.equals("href")) {
            this.rebuildImageNode();
            return;
         }
      } catch (LiveAttributeException var8) {
         throw new BridgeException(this.ctx, var8);
      }

      super.handleAnimatedAttributeChanged(var1);
   }

   protected void updateImageBounds() {
      Rectangle2D var1 = getImageBounds(this.ctx, this.e);
      GraphicsNode var2 = ((ImageNode)this.node).getImage();
      float[] var3 = null;
      if (var2 instanceof RasterImageNode) {
         Rectangle2D var4 = ((RasterImageNode)var2).getImageBounds();
         var3 = new float[]{0.0F, 0.0F, (float)var4.getWidth(), (float)var4.getHeight()};
      } else if (this.imgDocument != null) {
         SVGSVGElement var6 = this.imgDocument.getRootElement();
         String var5 = var6.getAttributeNS((String)null, "viewBox");
         var3 = ViewBox.parseViewBoxAttribute(this.e, var5, this.ctx);
      }

      if (var2 != null) {
         initializeViewport(this.ctx, this.e, var2, var3, var1);
      }

   }

   protected void rebuildImageNode() {
      if (this.imgDocument != null && this.listener != null) {
         NodeEventTarget var1 = (NodeEventTarget)this.imgDocument.getRootElement();
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "click", this.listener, false);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "keydown", this.listener, false);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "keypress", this.listener, false);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "keyup", this.listener, false);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mousedown", this.listener, false);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mousemove", this.listener, false);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mouseout", this.listener, false);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mouseover", this.listener, false);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mouseup", this.listener, false);
         this.listener = null;
      }

      if (this.imgDocument != null) {
         SVGSVGElement var5 = this.imgDocument.getRootElement();
         this.disposeTree(var5);
      }

      this.imgDocument = null;
      this.subCtx = null;
      GraphicsNode var6 = this.buildImageGraphicsNode(this.ctx, this.e);
      ImageNode var2 = (ImageNode)this.node;
      var2.setImage(var6);
      if (var6 == null) {
         SVGImageElement var3 = (SVGImageElement)this.e;
         String var4 = var3.getHref().getAnimVal();
         throw new BridgeException(this.ctx, this.e, "uri.image.invalid", new Object[]{var4});
      }
   }

   protected void handleCSSPropertyChanged(int var1) {
      switch (var1) {
         case 6:
         case 30:
            RenderingHints var2 = CSSUtilities.convertImageRendering(this.e, (RenderingHints)null);
            var2 = CSSUtilities.convertColorRendering(this.e, var2);
            if (var2 != null) {
               this.node.setRenderingHints(var2);
            }
            break;
         default:
            super.handleCSSPropertyChanged(var1);
      }

   }

   protected GraphicsNode createRasterImageNode(BridgeContext var1, Element var2, Filter var3, ParsedURL var4) {
      Rectangle2D var5 = getImageBounds(var1, var2);
      if (var5.getWidth() != 0.0 && var5.getHeight() != 0.0) {
         if (BrokenLinkProvider.hasBrokenLinkProperty(var3)) {
            Object var10 = var3.getProperty("org.apache.batik.BrokenLinkImage");
            String var11 = "unknown";
            if (var10 instanceof String) {
               var11 = (String)var10;
            }

            SVGDocument var12 = var1.getUserAgent().getBrokenLinkDocument(var2, var4.toString(), var11);
            return this.createSVGImageNode(var1, var2, var12);
         } else {
            RasterImageNode var9 = new RasterImageNode();
            var9.setImage(var3);
            Rectangle2D var7 = var3.getBounds2D();
            float[] var8 = new float[]{0.0F, 0.0F, (float)var7.getWidth(), (float)var7.getHeight()};
            initializeViewport(var1, var2, var9, var8, var5);
            return var9;
         }
      } else {
         ShapeNode var6 = new ShapeNode();
         var6.setShape(var5);
         return var6;
      }
   }

   protected GraphicsNode createSVGImageNode(BridgeContext var1, Element var2, SVGDocument var3) {
      CSSEngine var4 = ((SVGOMDocument)var3).getCSSEngine();
      this.subCtx = var1.createSubBridgeContext((SVGOMDocument)var3);
      CompositeGraphicsNode var5 = new CompositeGraphicsNode();
      Rectangle2D var6 = getImageBounds(var1, var2);
      if (var6.getWidth() != 0.0 && var6.getHeight() != 0.0) {
         Rectangle2D var13 = CSSUtilities.convertEnableBackground(var2);
         if (var13 != null) {
            var5.setBackgroundEnable(var13);
         }

         SVGSVGElement var8 = var3.getRootElement();
         CanvasGraphicsNode var9 = (CanvasGraphicsNode)this.subCtx.getGVTBuilder().build(this.subCtx, (Element)var8);
         if (var4 == null && var1.isInteractive()) {
            this.subCtx.addUIEventListeners(var3);
         }

         var9.setClip((ClipRable)null);
         var9.setViewingTransform(new AffineTransform());
         var5.getChildren().add(var9);
         String var10 = var8.getAttributeNS((String)null, "viewBox");
         float[] var11 = ViewBox.parseViewBoxAttribute(var2, var10, var1);
         initializeViewport(var1, var2, var5, var11, var6);
         if (var1.isInteractive()) {
            this.listener = new ForwardEventListener(var8, var2);
            NodeEventTarget var12 = (NodeEventTarget)var8;
            var12.addEventListenerNS("http://www.w3.org/2001/xml-events", "click", this.listener, false, (Object)null);
            this.subCtx.storeEventListenerNS(var12, "http://www.w3.org/2001/xml-events", "click", this.listener, false);
            var12.addEventListenerNS("http://www.w3.org/2001/xml-events", "keydown", this.listener, false, (Object)null);
            this.subCtx.storeEventListenerNS(var12, "http://www.w3.org/2001/xml-events", "keydown", this.listener, false);
            var12.addEventListenerNS("http://www.w3.org/2001/xml-events", "keypress", this.listener, false, (Object)null);
            this.subCtx.storeEventListenerNS(var12, "http://www.w3.org/2001/xml-events", "keypress", this.listener, false);
            var12.addEventListenerNS("http://www.w3.org/2001/xml-events", "keyup", this.listener, false, (Object)null);
            this.subCtx.storeEventListenerNS(var12, "http://www.w3.org/2001/xml-events", "keyup", this.listener, false);
            var12.addEventListenerNS("http://www.w3.org/2001/xml-events", "mousedown", this.listener, false, (Object)null);
            this.subCtx.storeEventListenerNS(var12, "http://www.w3.org/2001/xml-events", "mousedown", this.listener, false);
            var12.addEventListenerNS("http://www.w3.org/2001/xml-events", "mousemove", this.listener, false, (Object)null);
            this.subCtx.storeEventListenerNS(var12, "http://www.w3.org/2001/xml-events", "mousemove", this.listener, false);
            var12.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseout", this.listener, false, (Object)null);
            this.subCtx.storeEventListenerNS(var12, "http://www.w3.org/2001/xml-events", "mouseout", this.listener, false);
            var12.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseover", this.listener, false, (Object)null);
            this.subCtx.storeEventListenerNS(var12, "http://www.w3.org/2001/xml-events", "mouseover", this.listener, false);
            var12.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseup", this.listener, false, (Object)null);
            this.subCtx.storeEventListenerNS(var12, "http://www.w3.org/2001/xml-events", "mouseup", this.listener, false);
         }

         return var5;
      } else {
         ShapeNode var7 = new ShapeNode();
         var7.setShape(var6);
         var5.getChildren().add(var7);
         return var5;
      }
   }

   public void dispose() {
      if (this.imgDocument != null && this.listener != null) {
         NodeEventTarget var1 = (NodeEventTarget)this.imgDocument.getRootElement();
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "click", this.listener, false);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "keydown", this.listener, false);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "keypress", this.listener, false);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "keyup", this.listener, false);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mousedown", this.listener, false);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mousemove", this.listener, false);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mouseout", this.listener, false);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mouseover", this.listener, false);
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mouseup", this.listener, false);
         this.listener = null;
      }

      if (this.imgDocument != null) {
         SVGSVGElement var2 = this.imgDocument.getRootElement();
         this.disposeTree(var2);
         this.imgDocument = null;
         this.subCtx = null;
      }

      super.dispose();
   }

   protected static void initializeViewport(BridgeContext var0, Element var1, GraphicsNode var2, float[] var3, Rectangle2D var4) {
      float var5 = (float)var4.getX();
      float var6 = (float)var4.getY();
      float var7 = (float)var4.getWidth();
      float var8 = (float)var4.getHeight();

      try {
         SVGImageElement var9 = (SVGImageElement)var1;
         SVGOMAnimatedPreserveAspectRatio var10 = (SVGOMAnimatedPreserveAspectRatio)var9.getPreserveAspectRatio();
         var10.check();
         AffineTransform var11 = ViewBox.getPreserveAspectRatioTransform(var1, var3, var7, var8, var10, var0);
         var11.preConcatenate(AffineTransform.getTranslateInstance((double)var5, (double)var6));
         var2.setTransform(var11);
         Rectangle2D.Float var12 = null;
         if (CSSUtilities.convertOverflow(var1)) {
            float[] var13 = CSSUtilities.convertClip(var1);
            if (var13 == null) {
               var12 = new Rectangle2D.Float(var5, var6, var7, var8);
            } else {
               var12 = new Rectangle2D.Float(var5 + var13[3], var6 + var13[0], var7 - var13[1] - var13[3], var8 - var13[2] - var13[0]);
            }
         }

         if (var12 != null) {
            try {
               var11 = var11.createInverse();
               Filter var16 = var2.getGraphicsNodeRable(true);
               Shape var17 = var11.createTransformedShape(var12);
               var2.setClip(new ClipRable8Bit(var16, var17));
            } catch (NoninvertibleTransformException var14) {
            }
         }

      } catch (LiveAttributeException var15) {
         throw new BridgeException(var0, var15);
      }
   }

   protected static ICCColorSpaceExt extractColorSpace(Element var0, BridgeContext var1) {
      String var2 = CSSUtilities.getComputedStyle(var0, 8).getStringValue();
      ICCColorSpaceExt var3 = null;
      if ("srgb".equalsIgnoreCase(var2)) {
         var3 = new ICCColorSpaceExt(ICC_Profile.getInstance(1000), 4);
      } else if (!"auto".equalsIgnoreCase(var2) && !"".equalsIgnoreCase(var2)) {
         SVGColorProfileElementBridge var4 = (SVGColorProfileElementBridge)var1.getBridge("http://www.w3.org/2000/svg", "color-profile");
         if (var4 != null) {
            var3 = var4.createICCColorSpaceExt(var1, var0, var2);
         }
      }

      return var3;
   }

   protected static Rectangle2D getImageBounds(BridgeContext var0, Element var1) {
      try {
         SVGImageElement var2 = (SVGImageElement)var1;
         AbstractSVGAnimatedLength var3 = (AbstractSVGAnimatedLength)var2.getX();
         float var4 = var3.getCheckedValue();
         AbstractSVGAnimatedLength var5 = (AbstractSVGAnimatedLength)var2.getY();
         float var6 = var5.getCheckedValue();
         AbstractSVGAnimatedLength var7 = (AbstractSVGAnimatedLength)var2.getWidth();
         float var8 = var7.getCheckedValue();
         AbstractSVGAnimatedLength var9 = (AbstractSVGAnimatedLength)var2.getHeight();
         float var10 = var9.getCheckedValue();
         return new Rectangle2D.Float(var4, var6, var8, var10);
      } catch (LiveAttributeException var11) {
         throw new BridgeException(var0, var11);
      }
   }

   GraphicsNode createBrokenImageNode(BridgeContext var1, Element var2, String var3, String var4) {
      SVGDocument var5 = var1.getUserAgent().getBrokenLinkDocument(var2, var3, Messages.formatMessage("uri.image.error", new Object[]{var4}));
      return this.createSVGImageNode(var1, var2, var5);
   }

   static {
      ImageTagRegistry.setBrokenLinkProvider(brokenLinkProvider);
   }

   protected static class ForwardEventListener implements EventListener {
      protected Element svgElement;
      protected Element imgElement;

      public ForwardEventListener(Element var1, Element var2) {
         this.svgElement = var1;
         this.imgElement = var2;
      }

      public void handleEvent(Event var1) {
         DOMMouseEvent var2 = (DOMMouseEvent)var1;
         DOMMouseEvent var3 = (DOMMouseEvent)((DocumentEvent)this.imgElement.getOwnerDocument()).createEvent("MouseEvents");
         var3.initMouseEventNS("http://www.w3.org/2001/xml-events", var2.getType(), var2.getBubbles(), var2.getCancelable(), var2.getView(), var2.getDetail(), var2.getScreenX(), var2.getScreenY(), var2.getClientX(), var2.getClientY(), var2.getButton(), (EventTarget)this.imgElement, var2.getModifiersString());
         ((EventTarget)this.imgElement).dispatchEvent(var3);
      }
   }

   public static class ProtectedStream extends BufferedInputStream {
      static final int BUFFER_SIZE = 8192;

      ProtectedStream(InputStream var1) {
         super(var1, 8192);
         super.mark(8192);
      }

      ProtectedStream(InputStream var1, int var2) {
         super(var1, var2);
         super.mark(var2);
      }

      public boolean markSupported() {
         return false;
      }

      public void mark(int var1) {
      }

      public void reset() throws IOException {
         throw new IOException("Reset unsupported");
      }

      public void retry() throws IOException {
         super.reset();
      }

      public void close() throws IOException {
      }

      public void release() {
         try {
            super.close();
         } catch (IOException var2) {
         }

      }
   }
}

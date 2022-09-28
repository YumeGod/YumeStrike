package org.apache.batik.bridge;

import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.batik.dom.svg.AbstractSVGAnimatedLength;
import org.apache.batik.dom.svg.AnimatedLiveAttributeValue;
import org.apache.batik.dom.svg.LiveAttributeException;
import org.apache.batik.dom.svg.SVGContext;
import org.apache.batik.dom.svg.SVGOMAnimatedRect;
import org.apache.batik.dom.svg.SVGOMElement;
import org.apache.batik.dom.svg.SVGOMSVGElement;
import org.apache.batik.dom.svg.SVGSVGContext;
import org.apache.batik.ext.awt.image.renderable.ClipRable8Bit;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.gvt.CanvasGraphicsNode;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.ShapeNode;
import org.apache.batik.gvt.TextNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedPreserveAspectRatio;
import org.w3c.dom.svg.SVGAnimatedRect;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGRect;

public class SVGSVGElementBridge extends SVGGElementBridge implements SVGSVGContext {
   public String getLocalName() {
      return "svg";
   }

   public Bridge getInstance() {
      return new SVGSVGElementBridge();
   }

   protected GraphicsNode instantiateGraphicsNode() {
      return new CanvasGraphicsNode();
   }

   public GraphicsNode createGraphicsNode(BridgeContext var1, Element var2) {
      if (!SVGUtilities.matchUserAgent(var2, var1.getUserAgent())) {
         return null;
      } else {
         CanvasGraphicsNode var3 = (CanvasGraphicsNode)this.instantiateGraphicsNode();
         this.associateSVGContext(var1, var2, var3);

         try {
            SVGDocument var4 = (SVGDocument)var2.getOwnerDocument();
            SVGOMSVGElement var5 = (SVGOMSVGElement)var2;
            boolean var6 = var4.getRootElement() == var2;
            float var7 = 0.0F;
            float var8 = 0.0F;
            AbstractSVGAnimatedLength var9;
            if (!var6) {
               var9 = (AbstractSVGAnimatedLength)var5.getX();
               var7 = var9.getCheckedValue();
               AbstractSVGAnimatedLength var10 = (AbstractSVGAnimatedLength)var5.getY();
               var8 = var10.getCheckedValue();
            }

            var9 = (AbstractSVGAnimatedLength)var5.getWidth();
            float var26 = var9.getCheckedValue();
            AbstractSVGAnimatedLength var11 = (AbstractSVGAnimatedLength)var5.getHeight();
            float var12 = var11.getCheckedValue();
            var3.setVisible(CSSUtilities.convertVisibility(var2));
            SVGOMAnimatedRect var13 = (SVGOMAnimatedRect)var5.getViewBox();
            SVGAnimatedPreserveAspectRatio var14 = var5.getPreserveAspectRatio();
            AffineTransform var15 = ViewBox.getPreserveAspectRatioTransform(var2, (SVGAnimatedRect)var13, (SVGAnimatedPreserveAspectRatio)var14, var26, var12, var1);
            float var16 = var26;
            float var17 = var12;

            AffineTransform var18;
            try {
               var18 = var15.createInverse();
               var16 = (float)((double)var26 * var18.getScaleX());
               var17 = (float)((double)var12 * var18.getScaleY());
            } catch (NoninvertibleTransformException var24) {
            }

            var18 = AffineTransform.getTranslateInstance((double)var7, (double)var8);
            if (!var6) {
               var3.setPositionTransform(var18);
            } else if (var4 == var1.getDocument()) {
               var1.setDocumentSize(new Dimension((int)(var26 + 0.5F), (int)(var12 + 0.5F)));
            }

            var3.setViewingTransform(var15);
            Rectangle2D.Float var19 = null;
            if (CSSUtilities.convertOverflow(var2)) {
               float[] var20 = CSSUtilities.convertClip(var2);
               if (var20 == null) {
                  var19 = new Rectangle2D.Float(var7, var8, var26, var12);
               } else {
                  var19 = new Rectangle2D.Float(var7 + var20[3], var8 + var20[0], var26 - var20[1] - var20[3], var12 - var20[2] - var20[0]);
               }
            }

            AffineTransform var27;
            if (var19 != null) {
               try {
                  var27 = new AffineTransform(var18);
                  var27.concatenate(var15);
                  var27 = var27.createInverse();
                  Shape var28 = var27.createTransformedShape(var19);
                  Filter var21 = var3.getGraphicsNodeRable(true);
                  var3.setClip(new ClipRable8Bit(var21, var28));
               } catch (NoninvertibleTransformException var23) {
               }
            }

            var27 = null;
            RenderingHints var29 = CSSUtilities.convertColorRendering(var2, var27);
            if (var29 != null) {
               var3.setRenderingHints(var29);
            }

            Rectangle2D var30 = CSSUtilities.convertEnableBackground(var2);
            if (var30 != null) {
               var3.setBackgroundEnable(var30);
            }

            if (var13.isSpecified()) {
               SVGRect var22 = var13.getAnimVal();
               var16 = var22.getWidth();
               var17 = var22.getHeight();
            }

            var1.openViewport(var2, new SVGSVGElementViewport(var16, var17));
            return var3;
         } catch (LiveAttributeException var25) {
            throw new BridgeException(var1, var25);
         }
      }
   }

   public void buildGraphicsNode(BridgeContext var1, Element var2, GraphicsNode var3) {
      var3.setComposite(CSSUtilities.convertOpacity(var2));
      var3.setFilter(CSSUtilities.convertFilter(var2, var3, var1));
      var3.setMask(CSSUtilities.convertMask(var2, var3, var1));
      var3.setPointerEventType(CSSUtilities.convertPointerEvents(var2));
      this.initializeDynamicSupport(var1, var2, var3);
      var1.closeViewport(var2);
   }

   public void dispose() {
      this.ctx.removeViewport(this.e);
      super.dispose();
   }

   public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue var1) {
      try {
         boolean var2 = false;
         if (var1.getNamespaceURI() == null) {
            String var3 = var1.getLocalName();
            if (!var3.equals("width") && !var3.equals("height")) {
               SVGDocument var4;
               SVGOMSVGElement var5;
               boolean var6;
               float var8;
               AbstractSVGAnimatedLength var9;
               float var10;
               if (!var3.equals("x") && !var3.equals("y")) {
                  if (var3.equals("viewBox") || var3.equals("preserveAspectRatio")) {
                     var4 = (SVGDocument)this.e.getOwnerDocument();
                     var5 = (SVGOMSVGElement)this.e;
                     var6 = var4.getRootElement() == this.e;
                     float var24 = 0.0F;
                     var8 = 0.0F;
                     if (!var6) {
                        var9 = (AbstractSVGAnimatedLength)var5.getX();
                        var24 = var9.getCheckedValue();
                        AbstractSVGAnimatedLength var25 = (AbstractSVGAnimatedLength)var5.getY();
                        var8 = var25.getCheckedValue();
                     }

                     var9 = (AbstractSVGAnimatedLength)var5.getWidth();
                     var10 = var9.getCheckedValue();
                     AbstractSVGAnimatedLength var26 = (AbstractSVGAnimatedLength)var5.getHeight();
                     float var27 = var26.getCheckedValue();
                     CanvasGraphicsNode var13 = (CanvasGraphicsNode)this.node;
                     SVGOMAnimatedRect var14 = (SVGOMAnimatedRect)var5.getViewBox();
                     SVGAnimatedPreserveAspectRatio var15 = var5.getPreserveAspectRatio();
                     AffineTransform var16 = ViewBox.getPreserveAspectRatioTransform(this.e, (SVGAnimatedRect)var14, (SVGAnimatedPreserveAspectRatio)var15, var10, var27, this.ctx);
                     AffineTransform var17 = var13.getViewingTransform();
                     if (var16.getScaleX() == var17.getScaleX() && var16.getScaleY() == var17.getScaleY() && var16.getShearX() == var17.getShearX() && var16.getShearY() == var17.getShearY()) {
                        var13.setViewingTransform(var16);
                        Rectangle2D.Float var18 = null;
                        if (CSSUtilities.convertOverflow(this.e)) {
                           float[] var19 = CSSUtilities.convertClip(this.e);
                           if (var19 == null) {
                              var18 = new Rectangle2D.Float(var24, var8, var10, var27);
                           } else {
                              var18 = new Rectangle2D.Float(var24 + var19[3], var8 + var19[0], var10 - var19[1] - var19[3], var27 - var19[2] - var19[0]);
                           }
                        }

                        if (var18 != null) {
                           try {
                              AffineTransform var28 = var13.getPositionTransform();
                              if (var28 == null) {
                                 var28 = new AffineTransform();
                              } else {
                                 var28 = new AffineTransform(var28);
                              }

                              var28.concatenate(var16);
                              var28 = var28.createInverse();
                              Shape var29 = var28.createTransformedShape(var18);
                              Filter var20 = var13.getGraphicsNodeRable(true);
                              var13.setClip(new ClipRable8Bit(var20, var29));
                           } catch (NoninvertibleTransformException var21) {
                           }
                        }
                     } else {
                        var2 = true;
                     }
                  }
               } else {
                  var4 = (SVGDocument)this.e.getOwnerDocument();
                  var5 = (SVGOMSVGElement)this.e;
                  var6 = var4.getRootElement() == this.e;
                  if (!var6) {
                     AbstractSVGAnimatedLength var7 = (AbstractSVGAnimatedLength)var5.getX();
                     var8 = var7.getCheckedValue();
                     var9 = (AbstractSVGAnimatedLength)var5.getY();
                     var10 = var9.getCheckedValue();
                     AffineTransform var11 = AffineTransform.getTranslateInstance((double)var8, (double)var10);
                     CanvasGraphicsNode var12 = (CanvasGraphicsNode)this.node;
                     var12.setPositionTransform(var11);
                     return;
                  }
               }
            } else {
               var2 = true;
            }

            if (var2) {
               CompositeGraphicsNode var23 = this.node.getParent();
               var23.remove(this.node);
               this.disposeTree(this.e, false);
               this.handleElementAdded(var23, this.e.getParentNode(), this.e);
               return;
            }
         }
      } catch (LiveAttributeException var22) {
         throw new BridgeException(this.ctx, var22);
      }

      super.handleAnimatedAttributeChanged(var1);
   }

   public List getIntersectionList(SVGRect var1, Element var2) {
      ArrayList var3 = new ArrayList();
      Rectangle2D.Float var4 = new Rectangle2D.Float(var1.getX(), var1.getY(), var1.getWidth(), var1.getHeight());
      GraphicsNode var5 = this.ctx.getGraphicsNode(this.e);
      if (var5 == null) {
         return var3;
      } else {
         Rectangle2D var6 = var5.getSensitiveBounds();
         if (var6 == null) {
            return var3;
         } else if (!var4.intersects(var6)) {
            return var3;
         } else {
            Element var7 = this.e;
            AffineTransform var8 = var5.getGlobalTransform();

            try {
               var8 = var8.createInverse();
            } catch (NoninvertibleTransformException var21) {
            }

            Node var10;
            for(var10 = var7.getFirstChild(); var10 != null && !(var10 instanceof Element); var10 = var10.getNextSibling()) {
            }

            if (var10 == null) {
               return var3;
            } else {
               Element var9 = (Element)var10;
               Set var11 = null;
               if (var2 != null) {
                  var11 = this.getAncestors(var2, var7);
                  if (var11 == null) {
                     var2 = null;
                  }
               }

               while(var9 != null) {
                  String var12 = var9.getNamespaceURI();
                  String var13 = var9.getLocalName();
                  boolean var14 = "http://www.w3.org/2000/svg".equals(var12) && ("g".equals(var13) || "svg".equals(var13) || "a".equals(var13));
                  GraphicsNode var15 = this.ctx.getGraphicsNode(var9);
                  if (var15 == null) {
                     if (var11 != null && var11.contains(var9)) {
                        break;
                     }

                     var9 = this.getNext(var9, var7, var2);
                  } else {
                     AffineTransform var16 = var15.getGlobalTransform();
                     Rectangle2D var17 = var15.getSensitiveBounds();
                     var16.preConcatenate(var8);
                     if (var17 != null) {
                        var17 = var16.createTransformedShape(var17).getBounds2D();
                     }

                     if (var17 != null && var4.intersects(var17)) {
                        if (var14) {
                           for(var10 = var9.getFirstChild(); var10 != null && !(var10 instanceof Element); var10 = var10.getNextSibling()) {
                           }

                           if (var10 != null) {
                              var9 = (Element)var10;
                              continue;
                           }
                        } else {
                           if (var9 == var2) {
                              break;
                           }

                           if ("http://www.w3.org/2000/svg".equals(var12) && "use".equals(var13) && var4.contains(var17)) {
                              var3.add(var9);
                           }

                           if (var15 instanceof ShapeNode) {
                              ShapeNode var18 = (ShapeNode)var15;
                              Shape var19 = var18.getSensitiveArea();
                              if (var19 != null) {
                                 var19 = var16.createTransformedShape(var19);
                                 if (var19.intersects(var4)) {
                                    var3.add(var9);
                                 }
                              }
                           } else if (var15 instanceof TextNode) {
                              SVGOMElement var22 = (SVGOMElement)var9;
                              SVGTextElementBridge var23 = (SVGTextElementBridge)var22.getSVGContext();
                              Set var20 = var23.getTextIntersectionSet(var16, var4);
                              if (var11 != null && var11.contains(var9)) {
                                 this.filterChildren(var9, var2, var20, var3);
                              } else {
                                 var3.addAll(var20);
                              }
                           } else {
                              var3.add(var9);
                           }
                        }

                        var9 = this.getNext(var9, var7, var2);
                     } else {
                        if (var11 != null && var11.contains(var9)) {
                           break;
                        }

                        var9 = this.getNext(var9, var7, var2);
                     }
                  }
               }

               return var3;
            }
         }
      }
   }

   public List getEnclosureList(SVGRect var1, Element var2) {
      ArrayList var3 = new ArrayList();
      Rectangle2D.Float var4 = new Rectangle2D.Float(var1.getX(), var1.getY(), var1.getWidth(), var1.getHeight());
      GraphicsNode var5 = this.ctx.getGraphicsNode(this.e);
      if (var5 == null) {
         return var3;
      } else {
         Rectangle2D var6 = var5.getSensitiveBounds();
         if (var6 == null) {
            return var3;
         } else if (!var4.intersects(var6)) {
            return var3;
         } else {
            Element var7 = this.e;
            AffineTransform var8 = var5.getGlobalTransform();

            try {
               var8 = var8.createInverse();
            } catch (NoninvertibleTransformException var21) {
            }

            Node var10;
            for(var10 = var7.getFirstChild(); var10 != null && !(var10 instanceof Element); var10 = var10.getNextSibling()) {
            }

            if (var10 == null) {
               return var3;
            } else {
               Element var9 = (Element)var10;
               Set var11 = null;
               if (var2 != null) {
                  var11 = this.getAncestors(var2, var7);
                  if (var11 == null) {
                     var2 = null;
                  }
               }

               while(var9 != null) {
                  String var12 = var9.getNamespaceURI();
                  String var13 = var9.getLocalName();
                  boolean var14 = "http://www.w3.org/2000/svg".equals(var12) && ("g".equals(var13) || "svg".equals(var13) || "a".equals(var13));
                  GraphicsNode var15 = this.ctx.getGraphicsNode(var9);
                  if (var15 == null) {
                     if (var11 != null && var11.contains(var9)) {
                        break;
                     }

                     var9 = this.getNext(var9, var7, var2);
                  } else {
                     AffineTransform var16 = var15.getGlobalTransform();
                     Rectangle2D var17 = var15.getSensitiveBounds();
                     var16.preConcatenate(var8);
                     if (var17 != null) {
                        var17 = var16.createTransformedShape(var17).getBounds2D();
                     }

                     if (var17 != null && var4.intersects(var17)) {
                        if (var14) {
                           for(var10 = var9.getFirstChild(); var10 != null && !(var10 instanceof Element); var10 = var10.getNextSibling()) {
                           }

                           if (var10 != null) {
                              var9 = (Element)var10;
                              continue;
                           }
                        } else {
                           if (var9 == var2) {
                              break;
                           }

                           if ("http://www.w3.org/2000/svg".equals(var12) && "use".equals(var13)) {
                              if (var4.contains(var17)) {
                                 var3.add(var9);
                              }
                           } else if (var15 instanceof TextNode) {
                              SVGOMElement var18 = (SVGOMElement)var9;
                              SVGTextElementBridge var19 = (SVGTextElementBridge)var18.getSVGContext();
                              Set var20 = var19.getTextEnclosureSet(var16, var4);
                              if (var11 != null && var11.contains(var9)) {
                                 this.filterChildren(var9, var2, var20, var3);
                              } else {
                                 var3.addAll(var20);
                              }
                           } else if (var4.contains(var17)) {
                              var3.add(var9);
                           }
                        }

                        var9 = this.getNext(var9, var7, var2);
                     } else {
                        if (var11 != null && var11.contains(var9)) {
                           break;
                        }

                        var9 = this.getNext(var9, var7, var2);
                     }
                  }
               }

               return var3;
            }
         }
      }
   }

   public boolean checkIntersection(Element var1, SVGRect var2) {
      GraphicsNode var3 = this.ctx.getGraphicsNode(this.e);
      if (var3 == null) {
         return false;
      } else {
         Rectangle2D.Float var4 = new Rectangle2D.Float(var2.getX(), var2.getY(), var2.getWidth(), var2.getHeight());
         AffineTransform var5 = var3.getGlobalTransform();

         try {
            var5 = var5.createInverse();
         } catch (NoninvertibleTransformException var12) {
         }

         SVGContext var6 = null;
         if (var1 instanceof SVGOMElement) {
            var6 = ((SVGOMElement)var1).getSVGContext();
            if (var6 instanceof SVGTextElementBridge || var6 instanceof SVGTextElementBridge.AbstractTextChildSVGContext) {
               return SVGTextElementBridge.getTextIntersection(this.ctx, var1, var5, var4, true);
            }
         }

         Rectangle2D var7 = null;
         GraphicsNode var8 = this.ctx.getGraphicsNode(var1);
         if (var8 != null) {
            var7 = var8.getSensitiveBounds();
         }

         if (var7 == null) {
            return false;
         } else {
            AffineTransform var9 = var8.getGlobalTransform();
            var9.preConcatenate(var5);
            var7 = var9.createTransformedShape(var7).getBounds2D();
            if (!var4.intersects(var7)) {
               return false;
            } else if (!(var8 instanceof ShapeNode)) {
               return true;
            } else {
               ShapeNode var10 = (ShapeNode)var8;
               Shape var11 = var10.getSensitiveArea();
               if (var11 == null) {
                  return false;
               } else {
                  var11 = var9.createTransformedShape(var11);
                  return var11.intersects(var4);
               }
            }
         }
      }
   }

   public boolean checkEnclosure(Element var1, SVGRect var2) {
      GraphicsNode var3 = this.ctx.getGraphicsNode(var1);
      Rectangle2D var4 = null;
      SVGContext var5 = null;
      if (var1 instanceof SVGOMElement) {
         var5 = ((SVGOMElement)var1).getSVGContext();
         if (!(var5 instanceof SVGTextElementBridge) && !(var5 instanceof SVGTextElementBridge.AbstractTextChildSVGContext)) {
            if (var3 != null) {
               var4 = var3.getSensitiveBounds();
            }
         } else {
            var4 = SVGTextElementBridge.getTextBounds(this.ctx, var1, true);

            for(Element var6 = (Element)var1.getParentNode(); var6 != null && var3 == null; var6 = (Element)var6.getParentNode()) {
               var3 = this.ctx.getGraphicsNode(var6);
            }
         }
      } else if (var3 != null) {
         var4 = var3.getSensitiveBounds();
      }

      if (var4 == null) {
         return false;
      } else {
         GraphicsNode var11 = this.ctx.getGraphicsNode(this.e);
         if (var11 == null) {
            return false;
         } else {
            Rectangle2D.Float var7 = new Rectangle2D.Float(var2.getX(), var2.getY(), var2.getWidth(), var2.getHeight());
            AffineTransform var8 = var11.getGlobalTransform();

            try {
               var8 = var8.createInverse();
            } catch (NoninvertibleTransformException var10) {
            }

            AffineTransform var9 = var3.getGlobalTransform();
            var9.preConcatenate(var8);
            var4 = var9.createTransformedShape(var4).getBounds2D();
            return var7.contains(var4);
         }
      }
   }

   public boolean filterChildren(Element var1, Element var2, Set var3, List var4) {
      for(Node var5 = var1.getFirstChild(); var5 != null; var5 = var5.getNextSibling()) {
         if (var5 instanceof Element && this.filterChildren((Element)var5, var2, var3, var4)) {
            return true;
         }
      }

      if (var1 == var2) {
         return true;
      } else {
         if (var3.contains(var1)) {
            var4.add(var1);
         }

         return false;
      }
   }

   protected Set getAncestors(Element var1, Element var2) {
      HashSet var3 = new HashSet();
      Element var4 = var1;

      do {
         var3.add(var4);
         var4 = (Element)var4.getParentNode();
      } while(var4 != null && var4 != var2);

      return var4 == null ? null : var3;
   }

   protected Element getNext(Element var1, Element var2, Element var3) {
      Node var4;
      for(var4 = var1.getNextSibling(); var4 != null && !(var4 instanceof Element); var4 = var4.getNextSibling()) {
      }

      label31:
      while(var4 == null) {
         var1 = (Element)var1.getParentNode();
         if (var1 != var3 && var1 != var2) {
            var4 = var1.getNextSibling();

            while(true) {
               if (var4 == null || var4 instanceof Element) {
                  continue label31;
               }

               var4 = var4.getNextSibling();
            }
         }

         var4 = null;
         break;
      }

      return (Element)var4;
   }

   public void deselectAll() {
      this.ctx.getUserAgent().deselectAll();
   }

   public int suspendRedraw(int var1) {
      UpdateManager var2 = this.ctx.getUpdateManager();
      return var2 != null ? var2.addRedrawSuspension(var1) : -1;
   }

   public boolean unsuspendRedraw(int var1) {
      UpdateManager var2 = this.ctx.getUpdateManager();
      return var2 != null ? var2.releaseRedrawSuspension(var1) : false;
   }

   public void unsuspendRedrawAll() {
      UpdateManager var1 = this.ctx.getUpdateManager();
      if (var1 != null) {
         var1.releaseAllRedrawSuspension();
      }

   }

   public void forceRedraw() {
      UpdateManager var1 = this.ctx.getUpdateManager();
      if (var1 != null) {
         var1.forceRepaint();
      }

   }

   public void pauseAnimations() {
      this.ctx.getAnimationEngine().pause();
   }

   public void unpauseAnimations() {
      this.ctx.getAnimationEngine().unpause();
   }

   public boolean animationsPaused() {
      return this.ctx.getAnimationEngine().isPaused();
   }

   public float getCurrentTime() {
      return this.ctx.getAnimationEngine().getCurrentTime();
   }

   public void setCurrentTime(float var1) {
      this.ctx.getAnimationEngine().setCurrentTime(var1);
   }

   public static class SVGSVGElementViewport implements Viewport {
      private float width;
      private float height;

      public SVGSVGElementViewport(float var1, float var2) {
         this.width = var1;
         this.height = var2;
      }

      public float getWidth() {
         return this.width;
      }

      public float getHeight() {
         return this.height;
      }
   }
}

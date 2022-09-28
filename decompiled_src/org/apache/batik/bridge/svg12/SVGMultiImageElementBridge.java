package org.apache.batik.bridge.svg12;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.batik.bridge.Bridge;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.BridgeException;
import org.apache.batik.bridge.CSSUtilities;
import org.apache.batik.bridge.SVGImageElementBridge;
import org.apache.batik.bridge.SVGUtilities;
import org.apache.batik.bridge.Viewport;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.ext.awt.image.renderable.ClipRable8Bit;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.ImageNode;
import org.apache.batik.gvt.svg12.MultiResGraphicsNode;
import org.apache.batik.parser.UnitProcessor;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class SVGMultiImageElementBridge extends SVGImageElementBridge {
   public String getNamespaceURI() {
      return "http://www.w3.org/2000/svg";
   }

   public String getLocalName() {
      return "multiImage";
   }

   public Bridge getInstance() {
      return new SVGMultiImageElementBridge();
   }

   public GraphicsNode createGraphicsNode(BridgeContext var1, Element var2) {
      if (!SVGUtilities.matchUserAgent(var2, var1.getUserAgent())) {
         return null;
      } else {
         ImageNode var3 = (ImageNode)this.instantiateGraphicsNode();
         if (var3 == null) {
            return null;
         } else {
            this.associateSVGContext(var1, var2, var3);
            Rectangle2D var4 = getImageBounds(var1, var2);
            AffineTransform var5 = null;
            String var6 = var2.getAttribute("transform");
            if (var6.length() != 0) {
               var5 = SVGUtilities.convertTransform(var2, "transform", var6, var1);
            } else {
               var5 = new AffineTransform();
            }

            var5.translate(var4.getX(), var4.getY());
            var3.setTransform(var5);
            var3.setVisible(CSSUtilities.convertVisibility(var2));
            Rectangle2D.Double var7 = new Rectangle2D.Double(0.0, 0.0, var4.getWidth(), var4.getHeight());
            Filter var8 = var3.getGraphicsNodeRable(true);
            var3.setClip(new ClipRable8Bit(var8, var7));
            Rectangle2D var9 = CSSUtilities.convertEnableBackground(var2);
            if (var9 != null) {
               var3.setBackgroundEnable(var9);
            }

            var1.openViewport(var2, new MultiImageElementViewport((float)var4.getWidth(), (float)var4.getHeight()));
            LinkedList var10 = new LinkedList();
            LinkedList var11 = new LinkedList();
            LinkedList var12 = new LinkedList();

            for(Node var13 = var2.getFirstChild(); var13 != null; var13 = var13.getNextSibling()) {
               if (var13.getNodeType() == 1) {
                  Element var14 = (Element)var13;
                  if (this.getNamespaceURI().equals(var14.getNamespaceURI())) {
                     if (var14.getLocalName().equals("subImage")) {
                        this.addInfo(var14, var10, var11, var12, var4);
                     }

                     if (var14.getLocalName().equals("subImageRef")) {
                        this.addRefInfo(var14, var10, var11, var12, var4);
                     }
                  }
               }
            }

            Dimension[] var24 = new Dimension[var10.size()];
            Dimension[] var25 = new Dimension[var10.size()];
            Element[] var15 = new Element[var10.size()];
            Iterator var16 = var11.iterator();
            Iterator var17 = var12.iterator();
            Iterator var18 = var10.iterator();

            for(int var19 = 0; var16.hasNext(); ++var19) {
               Dimension var20 = (Dimension)var16.next();
               Dimension var21 = (Dimension)var17.next();
               int var22 = 0;
               if (var20 != null) {
                  while(var22 < var19 && (var24[var22] == null || var20.width >= var24[var22].width)) {
                     ++var22;
                  }
               }

               for(int var23 = var19; var23 > var22; --var23) {
                  var15[var23] = var15[var23 - 1];
                  var24[var23] = var24[var23 - 1];
                  var25[var23] = var25[var23 - 1];
               }

               var15[var22] = (Element)var18.next();
               var24[var22] = var20;
               var25[var22] = var21;
            }

            MultiResGraphicsNode var26 = new MultiResGraphicsNode(var2, var7, var15, var24, var25, var1);
            var3.setImage(var26);
            return var3;
         }
      }
   }

   public boolean isComposite() {
      return false;
   }

   public void buildGraphicsNode(BridgeContext var1, Element var2, GraphicsNode var3) {
      this.initializeDynamicSupport(var1, var2, var3);
      var1.closeViewport(var2);
   }

   protected void initializeDynamicSupport(BridgeContext var1, Element var2, GraphicsNode var3) {
      if (var1.isInteractive()) {
         ImageNode var4 = (ImageNode)var3;
         var1.bind(var2, var4.getImage());
      }

   }

   public void dispose() {
      this.ctx.removeViewport(this.e);
      super.dispose();
   }

   protected static Rectangle2D getImageBounds(BridgeContext var0, Element var1) {
      UnitProcessor.Context var2 = org.apache.batik.bridge.UnitProcessor.createContext(var0, var1);
      String var3 = var1.getAttributeNS((String)null, "x");
      float var4 = 0.0F;
      if (var3.length() != 0) {
         var4 = org.apache.batik.bridge.UnitProcessor.svgHorizontalCoordinateToUserSpace(var3, "x", var2);
      }

      var3 = var1.getAttributeNS((String)null, "y");
      float var5 = 0.0F;
      if (var3.length() != 0) {
         var5 = org.apache.batik.bridge.UnitProcessor.svgVerticalCoordinateToUserSpace(var3, "y", var2);
      }

      var3 = var1.getAttributeNS((String)null, "width");
      if (var3.length() == 0) {
         throw new BridgeException(var0, var1, "attribute.missing", new Object[]{"width"});
      } else {
         float var6 = org.apache.batik.bridge.UnitProcessor.svgHorizontalLengthToUserSpace(var3, "width", var2);
         var3 = var1.getAttributeNS((String)null, "height");
         if (var3.length() == 0) {
            throw new BridgeException(var0, var1, "attribute.missing", new Object[]{"height"});
         } else {
            float var7 = org.apache.batik.bridge.UnitProcessor.svgVerticalLengthToUserSpace(var3, "height", var2);
            return new Rectangle2D.Float(var4, var5, var6, var7);
         }
      }
   }

   protected void addInfo(Element var1, Collection var2, Collection var3, Collection var4, Rectangle2D var5) {
      Document var6 = var1.getOwnerDocument();
      Element var7 = var6.createElementNS("http://www.w3.org/2000/svg", "g");
      NamedNodeMap var8 = var1.getAttributes();
      int var9 = var8.getLength();

      for(int var10 = 0; var10 < var9; ++var10) {
         Attr var11 = (Attr)var8.item(var10);
         var7.setAttributeNS(var11.getNamespaceURI(), var11.getName(), var11.getValue());
      }

      for(Node var12 = var1.getFirstChild(); var12 != null; var12 = var1.getFirstChild()) {
         var7.appendChild(var12);
      }

      var1.appendChild(var7);
      var2.add(var7);
      var3.add(this.getElementMinPixel(var1, var5));
      var4.add(this.getElementMaxPixel(var1, var5));
   }

   protected void addRefInfo(Element var1, Collection var2, Collection var3, Collection var4, Rectangle2D var5) {
      String var6 = XLinkSupport.getXLinkHref(var1);
      if (var6.length() == 0) {
         throw new BridgeException(this.ctx, var1, "attribute.missing", new Object[]{"xlink:href"});
      } else {
         String var7 = AbstractNode.getBaseURI(var1);
         ParsedURL var8;
         if (var7 == null) {
            var8 = new ParsedURL(var6);
         } else {
            var8 = new ParsedURL(var7, var6);
         }

         Document var9 = var1.getOwnerDocument();
         Element var10 = var9.createElementNS("http://www.w3.org/2000/svg", "image");
         var10.setAttributeNS("http://www.w3.org/1999/xlink", "href", var8.toString());
         NamedNodeMap var11 = var1.getAttributes();
         int var12 = var11.getLength();

         for(int var13 = 0; var13 < var12; ++var13) {
            Attr var14 = (Attr)var11.item(var13);
            var10.setAttributeNS(var14.getNamespaceURI(), var14.getName(), var14.getValue());
         }

         String var15 = var1.getAttribute("x");
         if (var15.length() == 0) {
            var10.setAttribute("x", "0");
         }

         var15 = var1.getAttribute("y");
         if (var15.length() == 0) {
            var10.setAttribute("y", "0");
         }

         var15 = var1.getAttribute("width");
         if (var15.length() == 0) {
            var10.setAttribute("width", "100%");
         }

         var15 = var1.getAttribute("height");
         if (var15.length() == 0) {
            var10.setAttribute("height", "100%");
         }

         var1.appendChild(var10);
         var2.add(var10);
         var3.add(this.getElementMinPixel(var1, var5));
         var4.add(this.getElementMaxPixel(var1, var5));
      }
   }

   protected Dimension getElementMinPixel(Element var1, Rectangle2D var2) {
      return this.getElementPixelSize(var1, "max-pixel-size", var2);
   }

   protected Dimension getElementMaxPixel(Element var1, Rectangle2D var2) {
      return this.getElementPixelSize(var1, "min-pixel-size", var2);
   }

   protected Dimension getElementPixelSize(Element var1, String var2, Rectangle2D var3) {
      String var4 = var1.getAttribute(var2);
      if (var4.length() == 0) {
         return null;
      } else {
         Float[] var5 = SVGUtilities.convertSVGNumberOptionalNumber(var1, var2, var4, this.ctx);
         if (var5[0] == null) {
            return null;
         } else {
            float var6 = var5[0];
            float var7 = var6;
            if (var5[1] != null) {
               var7 = var5[1];
            }

            return new Dimension((int)(var3.getWidth() / (double)var6 + 0.5), (int)(var3.getHeight() / (double)var7 + 0.5));
         }
      }
   }

   public static class MultiImageElementViewport implements Viewport {
      private float width;
      private float height;

      public MultiImageElementViewport(float var1, float var2) {
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

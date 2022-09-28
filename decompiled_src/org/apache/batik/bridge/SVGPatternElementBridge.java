package org.apache.batik.bridge;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.ext.awt.image.ConcreteComponentTransferFunction;
import org.apache.batik.ext.awt.image.renderable.ComponentTransferRable8Bit;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.gvt.AbstractGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.PatternPaint;
import org.apache.batik.gvt.RootGraphicsNode;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SVGPatternElementBridge extends AnimatableGenericSVGBridge implements PaintBridge, ErrorConstants {
   public String getLocalName() {
      return "pattern";
   }

   public Paint createPaint(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, float var5) {
      RootGraphicsNode var6 = (RootGraphicsNode)var1.getElementData(var2);
      if (var6 == null) {
         var6 = extractPatternContent(var2, var1);
         var1.setElementData(var2, var6);
      }

      if (var6 == null) {
         return null;
      } else {
         Rectangle2D var7 = SVGUtilities.convertPatternRegion(var2, var3, var4, var1);
         String var8 = SVGUtilities.getChainableAttributeNS(var2, (String)null, "patternTransform", var1);
         AffineTransform var9;
         if (var8.length() != 0) {
            var9 = SVGUtilities.convertTransform(var2, "patternTransform", var8, var1);
         } else {
            var9 = new AffineTransform();
         }

         boolean var10 = CSSUtilities.convertOverflow(var2);
         var8 = SVGUtilities.getChainableAttributeNS(var2, (String)null, "patternContentUnits", var1);
         short var11;
         if (var8.length() == 0) {
            var11 = 1;
         } else {
            var11 = SVGUtilities.parseCoordinateSystem(var2, "patternContentUnits", var8, var1);
         }

         AffineTransform var12 = new AffineTransform();
         var12.translate(var7.getX(), var7.getY());
         String var13 = SVGUtilities.getChainableAttributeNS(var2, (String)null, "viewBox", var1);
         if (var13.length() > 0) {
            String var14 = SVGUtilities.getChainableAttributeNS(var2, (String)null, "preserveAspectRatio", var1);
            float var15 = (float)var7.getWidth();
            float var16 = (float)var7.getHeight();
            AffineTransform var17 = ViewBox.getPreserveAspectRatioTransform(var2, var13, var14, var15, var16, var1);
            var12.concatenate(var17);
         } else if (var11 == 2) {
            AffineTransform var18 = new AffineTransform();
            Rectangle2D var20 = var4.getGeometryBounds();
            var18.translate(var20.getX(), var20.getY());
            var18.scale(var20.getWidth(), var20.getHeight());
            var12.concatenate(var18);
         }

         PatternGraphicsNode var19 = new PatternGraphicsNode(var6);
         var19.setTransform(var12);
         if (var5 != 1.0F) {
            Filter var21 = var19.getGraphicsNodeRable(true);
            ComponentTransferRable8Bit var22 = new ComponentTransferRable8Bit(var21, ConcreteComponentTransferFunction.getLinearTransfer(var5, 0.0F), ConcreteComponentTransferFunction.getIdentityTransfer(), ConcreteComponentTransferFunction.getIdentityTransfer(), ConcreteComponentTransferFunction.getIdentityTransfer());
            var19.setFilter(var22);
         }

         return new PatternPaint(var19, var7, !var10, var9);
      }
   }

   protected static RootGraphicsNode extractPatternContent(Element var0, BridgeContext var1) {
      LinkedList var2 = new LinkedList();

      while(true) {
         RootGraphicsNode var3 = extractLocalPatternContent(var0, var1);
         if (var3 != null) {
            return var3;
         }

         String var4 = XLinkSupport.getXLinkHref(var0);
         if (var4.length() == 0) {
            return null;
         }

         SVGOMDocument var5 = (SVGOMDocument)var0.getOwnerDocument();
         ParsedURL var6 = new ParsedURL(var5.getURL(), var4);
         if (!var6.complete()) {
            throw new BridgeException(var1, var0, "uri.malformed", new Object[]{var4});
         }

         if (contains(var2, var6)) {
            throw new BridgeException(var1, var0, "xlink.href.circularDependencies", new Object[]{var4});
         }

         var2.add(var6);
         var0 = var1.getReferencedElement(var0, var4);
      }
   }

   protected static RootGraphicsNode extractLocalPatternContent(Element var0, BridgeContext var1) {
      GVTBuilder var2 = var1.getGVTBuilder();
      RootGraphicsNode var3 = null;

      for(Node var4 = var0.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
         if (var4.getNodeType() == 1) {
            GraphicsNode var5 = var2.build(var1, (Element)var4);
            if (var5 != null) {
               if (var3 == null) {
                  var3 = new RootGraphicsNode();
               }

               var3.getChildren().add(var5);
            }
         }
      }

      return var3;
   }

   private static boolean contains(List var0, ParsedURL var1) {
      Iterator var2 = var0.iterator();

      do {
         if (!var2.hasNext()) {
            return false;
         }
      } while(!var1.equals(var2.next()));

      return true;
   }

   public static class PatternGraphicsNode extends AbstractGraphicsNode {
      GraphicsNode pcn;
      Rectangle2D pBounds;
      Rectangle2D gBounds;
      Rectangle2D sBounds;
      Shape oShape;

      public PatternGraphicsNode(GraphicsNode var1) {
         this.pcn = var1;
      }

      public void primitivePaint(Graphics2D var1) {
         this.pcn.paint(var1);
      }

      public Rectangle2D getPrimitiveBounds() {
         if (this.pBounds != null) {
            return this.pBounds;
         } else {
            this.pBounds = this.pcn.getTransformedBounds(GraphicsNode.IDENTITY);
            return this.pBounds;
         }
      }

      public Rectangle2D getGeometryBounds() {
         if (this.gBounds != null) {
            return this.gBounds;
         } else {
            this.gBounds = this.pcn.getTransformedGeometryBounds(GraphicsNode.IDENTITY);
            return this.gBounds;
         }
      }

      public Rectangle2D getSensitiveBounds() {
         if (this.sBounds != null) {
            return this.sBounds;
         } else {
            this.sBounds = this.pcn.getTransformedSensitiveBounds(GraphicsNode.IDENTITY);
            return this.sBounds;
         }
      }

      public Shape getOutline() {
         if (this.oShape != null) {
            return this.oShape;
         } else {
            this.oShape = this.pcn.getOutline();
            AffineTransform var1 = this.pcn.getTransform();
            if (var1 != null) {
               this.oShape = var1.createTransformedShape(this.oShape);
            }

            return this.oShape;
         }
      }

      protected void invalidateGeometryCache() {
         this.pBounds = null;
         this.gBounds = null;
         this.sBounds = null;
         this.oShape = null;
         super.invalidateGeometryCache();
      }
   }
}

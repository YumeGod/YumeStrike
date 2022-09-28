package org.apache.batik.bridge;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import org.apache.batik.dom.svg.AnimatedLiveAttributeValue;
import org.apache.batik.dom.svg.LiveAttributeException;
import org.apache.batik.dom.svg.SVGAnimatedPathDataSupport;
import org.apache.batik.dom.svg.SVGOMAnimatedPathData;
import org.apache.batik.dom.svg.SVGOMPathElement;
import org.apache.batik.dom.svg.SVGPathContext;
import org.apache.batik.ext.awt.geom.PathLength;
import org.apache.batik.gvt.ShapeNode;
import org.apache.batik.parser.AWTPathProducer;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGPathSegList;

public class SVGPathElementBridge extends SVGDecoratedShapeElementBridge implements SVGPathContext {
   protected static final Shape DEFAULT_SHAPE = new GeneralPath();
   protected Shape pathLengthShape;
   protected PathLength pathLength;

   public String getLocalName() {
      return "path";
   }

   public Bridge getInstance() {
      return new SVGPathElementBridge();
   }

   protected void buildShape(BridgeContext var1, Element var2, ShapeNode var3) {
      SVGOMPathElement var4 = (SVGOMPathElement)var2;
      AWTPathProducer var5 = new AWTPathProducer();

      try {
         SVGOMAnimatedPathData var6 = var4.getAnimatedPathData();
         var6.check();
         SVGPathSegList var7 = var6.getAnimatedPathSegList();
         var5.setWindingRule(CSSUtilities.convertFillRule(var2));
         SVGAnimatedPathDataSupport.handlePathSegList(var7, var5);
      } catch (LiveAttributeException var11) {
         throw new BridgeException(var1, var11);
      } finally {
         var3.setShape(var5.getShape());
      }

   }

   public void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue var1) {
      if (var1.getNamespaceURI() == null && var1.getLocalName().equals("d")) {
         this.buildShape(this.ctx, this.e, (ShapeNode)this.node);
         this.handleGeometryChanged();
      } else {
         super.handleAnimatedAttributeChanged(var1);
      }

   }

   protected void handleCSSPropertyChanged(int var1) {
      switch (var1) {
         case 17:
            this.buildShape(this.ctx, this.e, (ShapeNode)this.node);
            this.handleGeometryChanged();
            break;
         default:
            super.handleCSSPropertyChanged(var1);
      }

   }

   protected PathLength getPathLengthObj() {
      Shape var1 = ((ShapeNode)this.node).getShape();
      if (this.pathLengthShape != var1) {
         this.pathLength = new PathLength(var1);
         this.pathLengthShape = var1;
      }

      return this.pathLength;
   }

   public float getTotalLength() {
      PathLength var1 = this.getPathLengthObj();
      return var1.lengthOfPath();
   }

   public Point2D getPointAtLength(float var1) {
      PathLength var2 = this.getPathLengthObj();
      return var2.pointAtLength(var1);
   }

   public int getPathSegAtLength(float var1) {
      PathLength var2 = this.getPathLengthObj();
      return var2.segmentAtLength(var1);
   }
}

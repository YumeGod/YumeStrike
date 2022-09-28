package org.apache.fop.svg;

import java.awt.geom.AffineTransform;
import org.apache.batik.bridge.AbstractGraphicsNodeBridge;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGAElement;

public class PDFAElementBridge extends AbstractGraphicsNodeBridge {
   private AffineTransform transform;

   public void setCurrentTransform(AffineTransform tf) {
      this.transform = tf;
   }

   public AffineTransform getCurrentTransform() {
      return this.transform;
   }

   public String getLocalName() {
      return "a";
   }

   protected GraphicsNode instantiateGraphicsNode() {
      return new PDFANode();
   }

   public GraphicsNode createGraphicsNode(BridgeContext ctx, Element e) {
      PDFANode aNode = (PDFANode)super.createGraphicsNode(ctx, e);
      aNode.setDestination(((SVGAElement)e).getHref().getBaseVal());
      aNode.setTransform(this.transform);
      return aNode;
   }

   public boolean isComposite() {
      return true;
   }
}

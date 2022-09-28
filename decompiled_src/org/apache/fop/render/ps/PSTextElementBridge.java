package org.apache.fop.render.ps;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.SVGTextElementBridge;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.TextPainter;
import org.w3c.dom.Element;

public class PSTextElementBridge extends SVGTextElementBridge {
   private TextPainter textPainter;

   public PSTextElementBridge(TextPainter textPainter) {
      this.textPainter = textPainter;
   }

   public GraphicsNode createGraphicsNode(BridgeContext ctx, Element e) {
      GraphicsNode node = super.createGraphicsNode(ctx, e);
      ((TextNode)node).setTextPainter(this.getTextPainter());
      return node;
   }

   private TextPainter getTextPainter() {
      return this.textPainter;
   }
}

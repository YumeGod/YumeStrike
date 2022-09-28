package org.apache.fop.svg;

import org.apache.batik.extension.svg.BatikFlowTextElementBridge;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.TextPainter;
import org.apache.fop.fonts.FontInfo;

public class PDFBatikFlowTextElementBridge extends BatikFlowTextElementBridge {
   private PDFTextPainter textPainter;

   public PDFBatikFlowTextElementBridge(FontInfo fontInfo) {
      this.textPainter = new PDFFlowExtTextPainter(fontInfo);
   }

   protected GraphicsNode instantiateGraphicsNode() {
      GraphicsNode node = super.instantiateGraphicsNode();
      if (node != null) {
         ((TextNode)node).setTextPainter(this.getTextPainter());
      }

      return node;
   }

   public TextPainter getTextPainter() {
      return this.textPainter;
   }
}

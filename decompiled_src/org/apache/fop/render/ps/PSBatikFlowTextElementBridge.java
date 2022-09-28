package org.apache.fop.render.ps;

import java.text.AttributedCharacterIterator;
import java.util.List;
import org.apache.batik.extension.svg.BatikFlowTextElementBridge;
import org.apache.batik.extension.svg.FlowExtTextPainter;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.TextPainter;
import org.apache.fop.fonts.FontInfo;

public class PSBatikFlowTextElementBridge extends BatikFlowTextElementBridge {
   private PSTextPainter textPainter;

   public PSBatikFlowTextElementBridge(FontInfo fontInfo) {
      this.textPainter = new PSFlowExtTextPainter(fontInfo);
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

   private class PSFlowExtTextPainter extends PSTextPainter {
      public PSFlowExtTextPainter(FontInfo fontInfo) {
         super(fontInfo);
      }

      public List getTextRuns(TextNode node, AttributedCharacterIterator aci) {
         FlowExtTextPainter delegate = (FlowExtTextPainter)FlowExtTextPainter.getInstance();
         return delegate.getTextRuns(node, aci);
      }
   }
}

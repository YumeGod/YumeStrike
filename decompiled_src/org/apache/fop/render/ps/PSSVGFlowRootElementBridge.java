package org.apache.fop.render.ps;

import java.text.AttributedCharacterIterator;
import java.util.List;
import org.apache.batik.bridge.svg12.SVGFlowRootElementBridge;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.TextPainter;
import org.apache.batik.gvt.flow.FlowTextPainter;
import org.apache.fop.fonts.FontInfo;

public class PSSVGFlowRootElementBridge extends SVGFlowRootElementBridge {
   private PSTextPainter textPainter;

   public PSSVGFlowRootElementBridge(FontInfo fontInfo) {
      this.textPainter = new PSFlowTextPainter(fontInfo);
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

   private class PSFlowTextPainter extends PSTextPainter {
      public PSFlowTextPainter(FontInfo fontInfo) {
         super(fontInfo);
      }

      public List getTextRuns(TextNode node, AttributedCharacterIterator aci) {
         FlowTextPainter delegate = (FlowTextPainter)FlowTextPainter.getInstance();
         return delegate.getTextRuns(node, aci);
      }
   }
}

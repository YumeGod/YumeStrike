package org.apache.batik.gvt.flow;

import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.TextPainter;

public class FlowTextNode extends TextNode {
   public FlowTextNode() {
      this.textPainter = FlowTextPainter.getInstance();
   }

   public void setTextPainter(TextPainter var1) {
      if (var1 == null) {
         this.textPainter = FlowTextPainter.getInstance();
      } else {
         this.textPainter = var1;
      }

   }
}

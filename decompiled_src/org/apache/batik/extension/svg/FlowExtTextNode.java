package org.apache.batik.extension.svg;

import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.TextPainter;

public class FlowExtTextNode extends TextNode {
   public FlowExtTextNode() {
      this.textPainter = FlowExtTextPainter.getInstance();
   }

   public void setTextPainter(TextPainter var1) {
      if (var1 == null) {
         this.textPainter = FlowExtTextPainter.getInstance();
      } else {
         this.textPainter = var1;
      }

   }
}

package org.apache.fop.svg;

import java.text.AttributedCharacterIterator;
import java.util.List;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.flow.FlowTextPainter;
import org.apache.fop.fonts.FontInfo;

public class PDFFlowTextPainter extends PDFTextPainter {
   public PDFFlowTextPainter(FontInfo fontInfo) {
      super(fontInfo);
   }

   public List getTextRuns(TextNode node, AttributedCharacterIterator aci) {
      FlowTextPainter delegate = (FlowTextPainter)FlowTextPainter.getInstance();
      return delegate.getTextRuns(node, aci);
   }
}

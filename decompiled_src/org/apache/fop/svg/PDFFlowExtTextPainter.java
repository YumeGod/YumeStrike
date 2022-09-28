package org.apache.fop.svg;

import java.text.AttributedCharacterIterator;
import java.util.List;
import org.apache.batik.extension.svg.FlowExtTextPainter;
import org.apache.batik.gvt.TextNode;
import org.apache.fop.fonts.FontInfo;

public class PDFFlowExtTextPainter extends PDFTextPainter {
   public PDFFlowExtTextPainter(FontInfo fontInfo) {
      super(fontInfo);
   }

   public List getTextRuns(TextNode node, AttributedCharacterIterator aci) {
      FlowExtTextPainter delegate = (FlowExtTextPainter)FlowExtTextPainter.getInstance();
      return delegate.getTextRuns(node, aci);
   }
}

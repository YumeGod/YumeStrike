package org.apache.xalan.trace;

import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPath;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;

public class EndSelectionEvent extends SelectionEvent {
   public EndSelectionEvent(TransformerImpl processor, Node sourceNode, ElemTemplateElement styleNode, String attributeName, XPath xpath, XObject selection) {
      super(processor, sourceNode, styleNode, attributeName, xpath, selection);
   }
}

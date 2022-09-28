package org.apache.xalan.transformer;

import javax.xml.transform.Transformer;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xml.serializer.TransformStateSetter;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

public interface TransformState extends TransformStateSetter {
   ElemTemplateElement getCurrentElement();

   Node getCurrentNode();

   ElemTemplate getCurrentTemplate();

   ElemTemplate getMatchedTemplate();

   Node getMatchedNode();

   NodeIterator getContextNodeList();

   Transformer getTransformer();
}

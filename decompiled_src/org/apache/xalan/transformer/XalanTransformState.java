package org.apache.xalan.transformer;

import javax.xml.transform.Transformer;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.ref.DTMNodeIterator;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

public class XalanTransformState implements TransformState {
   Node m_node = null;
   ElemTemplateElement m_currentElement = null;
   ElemTemplate m_currentTemplate = null;
   ElemTemplate m_matchedTemplate = null;
   int m_currentNodeHandle = -1;
   Node m_currentNode = null;
   int m_matchedNode = -1;
   DTMIterator m_contextNodeList = null;
   boolean m_elemPending = false;
   TransformerImpl m_transformer = null;

   public void setCurrentNode(Node n) {
      this.m_node = n;
   }

   public void resetState(Transformer transformer) {
      if (transformer != null && transformer instanceof TransformerImpl) {
         this.m_transformer = (TransformerImpl)transformer;
         this.m_currentElement = this.m_transformer.getCurrentElement();
         this.m_currentTemplate = this.m_transformer.getCurrentTemplate();
         this.m_matchedTemplate = this.m_transformer.getMatchedTemplate();
         int currentNodeHandle = this.m_transformer.getCurrentNode();
         DTM dtm = this.m_transformer.getXPathContext().getDTM(currentNodeHandle);
         this.m_currentNode = dtm.getNode(currentNodeHandle);
         this.m_matchedNode = this.m_transformer.getMatchedNode();
         this.m_contextNodeList = this.m_transformer.getContextNodeList();
      }

   }

   public ElemTemplateElement getCurrentElement() {
      return this.m_elemPending ? this.m_currentElement : this.m_transformer.getCurrentElement();
   }

   public Node getCurrentNode() {
      if (this.m_currentNode != null) {
         return this.m_currentNode;
      } else {
         DTM dtm = this.m_transformer.getXPathContext().getDTM(this.m_transformer.getCurrentNode());
         return dtm.getNode(this.m_transformer.getCurrentNode());
      }
   }

   public ElemTemplate getCurrentTemplate() {
      return this.m_elemPending ? this.m_currentTemplate : this.m_transformer.getCurrentTemplate();
   }

   public ElemTemplate getMatchedTemplate() {
      return this.m_elemPending ? this.m_matchedTemplate : this.m_transformer.getMatchedTemplate();
   }

   public Node getMatchedNode() {
      DTM dtm;
      if (this.m_elemPending) {
         dtm = this.m_transformer.getXPathContext().getDTM(this.m_matchedNode);
         return dtm.getNode(this.m_matchedNode);
      } else {
         dtm = this.m_transformer.getXPathContext().getDTM(this.m_transformer.getMatchedNode());
         return dtm.getNode(this.m_transformer.getMatchedNode());
      }
   }

   public NodeIterator getContextNodeList() {
      return this.m_elemPending ? new DTMNodeIterator(this.m_contextNodeList) : new DTMNodeIterator(this.m_transformer.getContextNodeList());
   }

   public Transformer getTransformer() {
      return this.m_transformer;
   }
}

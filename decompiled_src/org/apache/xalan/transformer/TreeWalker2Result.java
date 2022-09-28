package org.apache.xalan.transformer;

import javax.xml.transform.TransformerException;
import org.apache.xalan.serialize.SerializerUtils;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.ref.DTMTreeWalker;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xpath.XPathContext;
import org.xml.sax.SAXException;

public class TreeWalker2Result extends DTMTreeWalker {
   TransformerImpl m_transformer;
   SerializationHandler m_handler;
   int m_startNode;

   public TreeWalker2Result(TransformerImpl transformer, SerializationHandler handler) {
      super(handler, (DTM)null);
      this.m_transformer = transformer;
      this.m_handler = handler;
   }

   public void traverse(int pos) throws SAXException {
      super.m_dtm = this.m_transformer.getXPathContext().getDTM(pos);
      this.m_startNode = pos;
      super.traverse(pos);
   }

   protected void endNode(int node) throws SAXException {
      super.endNode(node);
      if (1 == super.m_dtm.getNodeType(node)) {
         this.m_transformer.getXPathContext().popCurrentNode();
      }

   }

   protected void startNode(int node) throws SAXException {
      XPathContext xcntxt = this.m_transformer.getXPathContext();

      try {
         if (1 == super.m_dtm.getNodeType(node)) {
            xcntxt.pushCurrentNode(node);
            if (this.m_startNode != node) {
               super.startNode(node);
            } else {
               String elemName = super.m_dtm.getNodeName(node);
               String localName = super.m_dtm.getLocalName(node);
               String namespace = super.m_dtm.getNamespaceURI(node);
               this.m_handler.startElement(namespace, localName, elemName);
               boolean hasNSDecls = false;
               DTM dtm = super.m_dtm;

               for(int ns = dtm.getFirstNamespaceNode(node, true); -1 != ns; ns = dtm.getNextNamespaceNode(node, ns, true)) {
                  SerializerUtils.ensureNamespaceDeclDeclared(this.m_handler, dtm, ns);
               }

               for(int attr = dtm.getFirstAttribute(node); -1 != attr; attr = dtm.getNextAttribute(attr)) {
                  SerializerUtils.addAttribute(this.m_handler, attr);
               }
            }
         } else {
            xcntxt.pushCurrentNode(node);
            super.startNode(node);
            xcntxt.popCurrentNode();
         }

      } catch (TransformerException var10) {
         throw new SAXException(var10);
      }
   }
}

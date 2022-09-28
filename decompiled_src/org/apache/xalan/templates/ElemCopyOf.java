package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.serialize.SerializerUtils;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xalan.transformer.TreeWalker2Result;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.ref.DTMTreeWalker;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.xml.sax.SAXException;

public class ElemCopyOf extends ElemTemplateElement {
   static final long serialVersionUID = -7433828829497411127L;
   public XPath m_selectExpression = null;

   public void setSelect(XPath expr) {
      this.m_selectExpression = expr;
   }

   public XPath getSelect() {
      return this.m_selectExpression;
   }

   public void compose(StylesheetRoot sroot) throws TransformerException {
      super.compose(sroot);
      StylesheetRoot.ComposeState cstate = sroot.getComposeState();
      this.m_selectExpression.fixupVariables(cstate.getVariableNames(), cstate.getGlobalsSize());
   }

   public int getXSLToken() {
      return 74;
   }

   public String getNodeName() {
      return "copy-of";
   }

   public void execute(TransformerImpl transformer) throws TransformerException {
      if (transformer.getDebug()) {
         transformer.getTraceManager().fireTraceEvent((ElemTemplateElement)this);
      }

      try {
         XPathContext xctxt = transformer.getXPathContext();
         int sourceNode = xctxt.getCurrentNode();
         XObject value = this.m_selectExpression.execute(xctxt, sourceNode, this);
         if (transformer.getDebug()) {
            transformer.getTraceManager().fireSelectedEvent(sourceNode, this, "select", this.m_selectExpression, value);
         }

         SerializationHandler handler = transformer.getSerializationHandler();
         if (null != value) {
            int type = value.getType();
            String s;
            switch (type) {
               case 1:
               case 2:
               case 3:
                  s = value.str();
                  handler.characters(s.toCharArray(), 0, s.length());
                  break;
               case 4:
                  DTMIterator nl = value.iter();
                  DTMTreeWalker tw = new TreeWalker2Result(transformer, handler);

                  while(true) {
                     int pos;
                     while(-1 != (pos = nl.nextNode())) {
                        DTM dtm = xctxt.getDTMManager().getDTM(pos);
                        short t = dtm.getNodeType(pos);
                        if (t == 9) {
                           for(int child = dtm.getFirstChild(pos); child != -1; child = dtm.getNextSibling(child)) {
                              tw.traverse(child);
                           }
                        } else if (t == 2) {
                           SerializerUtils.addAttribute(handler, pos);
                        } else {
                           tw.traverse(pos);
                        }
                     }

                     return;
                  }
               case 5:
                  SerializerUtils.outputResultTreeFragment(handler, value, transformer.getXPathContext());
                  break;
               default:
                  s = value.str();
                  handler.characters(s.toCharArray(), 0, s.length());
            }
         }
      } catch (SAXException var18) {
         throw new TransformerException(var18);
      } finally {
         if (transformer.getDebug()) {
            transformer.getTraceManager().fireTraceEndEvent((ElemTemplateElement)this);
         }

      }

   }

   public ElemTemplateElement appendChild(ElemTemplateElement newChild) {
      this.error("ER_CANNOT_ADD", new Object[]{newChild.getNodeName(), this.getNodeName()});
      return null;
   }

   protected void callChildVisitors(XSLTVisitor visitor, boolean callAttrs) {
      if (callAttrs) {
         this.m_selectExpression.getExpression().callVisitors(this.m_selectExpression, visitor);
      }

      super.callChildVisitors(visitor, callAttrs);
   }
}

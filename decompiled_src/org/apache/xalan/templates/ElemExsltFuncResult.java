package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;

public class ElemExsltFuncResult extends ElemVariable {
   static final long serialVersionUID = -3478311949388304563L;
   private boolean m_isResultSet = false;
   private XObject m_result = null;
   private int m_callerFrameSize = 0;

   public void execute(TransformerImpl transformer) throws TransformerException {
      XPathContext context = transformer.getXPathContext();
      if (transformer.getDebug()) {
         transformer.getTraceManager().fireTraceEvent((ElemTemplateElement)this);
      }

      if (transformer.currentFuncResultSeen()) {
         throw new TransformerException("An EXSLT function cannot set more than one result!");
      } else {
         int sourceNode = context.getCurrentNode();
         XObject var = this.getValue(transformer, sourceNode);
         transformer.popCurrentFuncResult();
         transformer.pushCurrentFuncResult(var);
         if (transformer.getDebug()) {
            transformer.getTraceManager().fireTraceEndEvent((ElemTemplateElement)this);
         }

      }
   }

   public int getXSLToken() {
      return 89;
   }

   public String getNodeName() {
      return "result";
   }
}

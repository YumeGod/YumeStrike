package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.transformer.TransformerImpl;

public class ElemMessage extends ElemTemplateElement {
   static final long serialVersionUID = 1530472462155060023L;
   private boolean m_terminate = false;

   public void setTerminate(boolean v) {
      this.m_terminate = v;
   }

   public boolean getTerminate() {
      return this.m_terminate;
   }

   public int getXSLToken() {
      return 75;
   }

   public String getNodeName() {
      return "message";
   }

   public void execute(TransformerImpl transformer) throws TransformerException {
      if (transformer.getDebug()) {
         transformer.getTraceManager().fireTraceEvent((ElemTemplateElement)this);
      }

      String data = transformer.transformToString(this);
      transformer.getMsgMgr().message(this, data, this.m_terminate);
      if (this.m_terminate) {
         transformer.getErrorListener().fatalError(new TransformerException(XSLMessages.createMessage("ER_STYLESHEET_DIRECTED_TERMINATION", (Object[])null)));
      }

      if (transformer.getDebug()) {
         transformer.getTraceManager().fireTraceEndEvent((ElemTemplateElement)this);
      }

   }
}

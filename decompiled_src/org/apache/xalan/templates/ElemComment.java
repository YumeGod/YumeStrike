package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.TransformerImpl;
import org.xml.sax.SAXException;

public class ElemComment extends ElemTemplateElement {
   static final long serialVersionUID = -8813199122875770142L;

   public int getXSLToken() {
      return 59;
   }

   public String getNodeName() {
      return "comment";
   }

   public void execute(TransformerImpl transformer) throws TransformerException {
      if (transformer.getDebug()) {
         transformer.getTraceManager().fireTraceEvent((ElemTemplateElement)this);
      }

      try {
         String data = transformer.transformToString(this);
         transformer.getResultTreeHandler().comment(data);
      } catch (SAXException var7) {
         throw new TransformerException(var7);
      } finally {
         if (transformer.getDebug()) {
            transformer.getTraceManager().fireTraceEndEvent((ElemTemplateElement)this);
         }

      }

   }

   public ElemTemplateElement appendChild(ElemTemplateElement newChild) {
      int type = newChild.getXSLToken();
      switch (type) {
         default:
            this.error("ER_CANNOT_ADD", new Object[]{newChild.getNodeName(), this.getNodeName()});
         case 9:
         case 17:
         case 28:
         case 30:
         case 35:
         case 36:
         case 37:
         case 42:
         case 50:
         case 72:
         case 73:
         case 74:
         case 75:
         case 78:
            return super.appendChild(newChild);
      }
   }
}

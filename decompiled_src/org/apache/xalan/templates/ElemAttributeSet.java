package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;

public class ElemAttributeSet extends ElemUse {
   static final long serialVersionUID = -426740318278164496L;
   public QName m_qname = null;

   public void setName(QName name) {
      this.m_qname = name;
   }

   public QName getName() {
      return this.m_qname;
   }

   public int getXSLToken() {
      return 40;
   }

   public String getNodeName() {
      return "attribute-set";
   }

   public void execute(TransformerImpl transformer) throws TransformerException {
      if (transformer.getDebug()) {
         transformer.getTraceManager().fireTraceEvent((ElemTemplateElement)this);
      }

      if (transformer.isRecursiveAttrSet(this)) {
         throw new TransformerException(XSLMessages.createMessage("ER_XSLATTRSET_USED_ITSELF", new Object[]{this.m_qname.getLocalPart()}));
      } else {
         transformer.pushElemAttributeSet(this);
         super.execute(transformer);

         for(ElemAttribute attr = (ElemAttribute)this.getFirstChildElem(); null != attr; attr = (ElemAttribute)attr.getNextSiblingElem()) {
            attr.execute(transformer);
         }

         transformer.popElemAttributeSet();
         if (transformer.getDebug()) {
            transformer.getTraceManager().fireTraceEndEvent((ElemTemplateElement)this);
         }

      }
   }

   public ElemTemplateElement appendChildElem(ElemTemplateElement newChild) {
      int type = newChild.getXSLToken();
      switch (type) {
         default:
            this.error("ER_CANNOT_ADD", new Object[]{newChild.getNodeName(), this.getNodeName()});
         case 48:
            return super.appendChild(newChild);
      }
   }

   public void recompose(StylesheetRoot root) {
      root.recomposeAttributeSets(this);
   }
}

package org.apache.xalan.templates;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;

public class ElemCallTemplate extends ElemForEach {
   static final long serialVersionUID = 5009634612916030591L;
   public QName m_templateName = null;
   private ElemTemplate m_template = null;
   protected ElemWithParam[] m_paramElems = null;

   public void setName(QName name) {
      this.m_templateName = name;
   }

   public QName getName() {
      return this.m_templateName;
   }

   public int getXSLToken() {
      return 17;
   }

   public String getNodeName() {
      return "call-template";
   }

   public void compose(StylesheetRoot sroot) throws TransformerException {
      super.compose(sroot);
      int length = this.getParamElemCount();

      for(int i = 0; i < length; ++i) {
         ElemWithParam ewp = this.getParamElem(i);
         ewp.compose(sroot);
      }

      if (null != this.m_templateName && null == this.m_template) {
         this.m_template = this.getStylesheetRoot().getTemplateComposed(this.m_templateName);
         if (null == this.m_template) {
            String themsg = XSLMessages.createMessage("ER_ELEMTEMPLATEELEM_ERR", new Object[]{this.m_templateName});
            throw new TransformerException(themsg, this);
         }

         length = this.getParamElemCount();

         for(int i = 0; i < length; ++i) {
            ElemWithParam ewp = this.getParamElem(i);
            ewp.m_index = -1;
            int etePos = 0;

            for(ElemTemplateElement ete = this.m_template.getFirstChildElem(); null != ete && ete.getXSLToken() == 41; ete = ete.getNextSiblingElem()) {
               ElemParam ep = (ElemParam)ete;
               if (ep.getName().equals(ewp.getName())) {
                  ewp.m_index = etePos;
               }

               ++etePos;
            }
         }
      }

   }

   public void endCompose(StylesheetRoot sroot) throws TransformerException {
      int length = this.getParamElemCount();

      for(int i = 0; i < length; ++i) {
         ElemWithParam ewp = this.getParamElem(i);
         ewp.endCompose(sroot);
      }

      super.endCompose(sroot);
   }

   public void execute(TransformerImpl transformer) throws TransformerException {
      if (transformer.getDebug()) {
         transformer.getTraceManager().fireTraceEvent((ElemTemplateElement)this);
      }

      if (null != this.m_template) {
         XPathContext xctxt = transformer.getXPathContext();
         VariableStack vars = xctxt.getVarStack();
         int thisframe = vars.getStackFrame();
         int nextFrame = vars.link(this.m_template.m_frameSize);
         if (this.m_template.m_inArgsSize > 0) {
            vars.clearLocalSlots(0, this.m_template.m_inArgsSize);
            if (null != this.m_paramElems) {
               int currentNode = xctxt.getCurrentNode();
               vars.setStackFrame(thisframe);
               int size = this.m_paramElems.length;

               for(int i = 0; i < size; ++i) {
                  ElemWithParam ewp = this.m_paramElems[i];
                  if (ewp.m_index >= 0) {
                     if (transformer.getDebug()) {
                        transformer.getTraceManager().fireTraceEvent((ElemTemplateElement)ewp);
                     }

                     XObject obj = ewp.getValue(transformer, currentNode);
                     if (transformer.getDebug()) {
                        transformer.getTraceManager().fireTraceEndEvent((ElemTemplateElement)ewp);
                     }

                     vars.setLocalVariable(ewp.m_index, obj, nextFrame);
                  }
               }

               vars.setStackFrame(nextFrame);
            }
         }

         SourceLocator savedLocator = xctxt.getSAXLocator();

         try {
            xctxt.setSAXLocator(this.m_template);
            transformer.pushElemTemplateElement(this.m_template);
            this.m_template.execute(transformer);
         } finally {
            transformer.popElemTemplateElement();
            xctxt.setSAXLocator(savedLocator);
            vars.unlink(thisframe);
         }
      } else {
         transformer.getMsgMgr().error(this, "ER_TEMPLATE_NOT_FOUND", (Object[])(new Object[]{this.m_templateName}));
      }

      if (transformer.getDebug()) {
         transformer.getTraceManager().fireTraceEndEvent((ElemTemplateElement)this);
      }

   }

   public int getParamElemCount() {
      return this.m_paramElems == null ? 0 : this.m_paramElems.length;
   }

   public ElemWithParam getParamElem(int i) {
      return this.m_paramElems[i];
   }

   public void setParamElem(ElemWithParam ParamElem) {
      if (null == this.m_paramElems) {
         this.m_paramElems = new ElemWithParam[1];
         this.m_paramElems[0] = ParamElem;
      } else {
         int length = this.m_paramElems.length;
         ElemWithParam[] ewp = new ElemWithParam[length + 1];
         System.arraycopy(this.m_paramElems, 0, ewp, 0, length);
         this.m_paramElems = ewp;
         ewp[length] = ParamElem;
      }

   }

   public ElemTemplateElement appendChild(ElemTemplateElement newChild) {
      int type = newChild.getXSLToken();
      if (2 == type) {
         this.setParamElem((ElemWithParam)newChild);
      }

      return super.appendChild(newChild);
   }

   public void callChildVisitors(XSLTVisitor visitor, boolean callAttrs) {
      super.callChildVisitors(visitor, callAttrs);
   }
}

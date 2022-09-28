package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;

public class ElemIf extends ElemTemplateElement {
   static final long serialVersionUID = 2158774632427453022L;
   private XPath m_test = null;

   public void setTest(XPath v) {
      this.m_test = v;
   }

   public XPath getTest() {
      return this.m_test;
   }

   public void compose(StylesheetRoot sroot) throws TransformerException {
      super.compose(sroot);
      Vector vnames = sroot.getComposeState().getVariableNames();
      if (null != this.m_test) {
         this.m_test.fixupVariables(vnames, sroot.getComposeState().getGlobalsSize());
      }

   }

   public int getXSLToken() {
      return 36;
   }

   public String getNodeName() {
      return "if";
   }

   public void execute(TransformerImpl transformer) throws TransformerException {
      XPathContext xctxt = transformer.getXPathContext();
      int sourceNode = xctxt.getCurrentNode();
      if (transformer.getDebug()) {
         XObject test = this.m_test.execute(xctxt, sourceNode, this);
         if (transformer.getDebug()) {
            transformer.getTraceManager().fireSelectedEvent(sourceNode, this, "test", this.m_test, test);
         }

         if (transformer.getDebug()) {
            transformer.getTraceManager().fireTraceEvent((ElemTemplateElement)this);
         }

         if (test.bool()) {
            transformer.executeChildTemplates(this, true);
         }

         if (transformer.getDebug()) {
            transformer.getTraceManager().fireTraceEndEvent((ElemTemplateElement)this);
         }
      } else if (this.m_test.bool(xctxt, sourceNode, this)) {
         transformer.executeChildTemplates(this, true);
      }

   }

   protected void callChildVisitors(XSLTVisitor visitor, boolean callAttrs) {
      if (callAttrs) {
         this.m_test.getExpression().callVisitors(this.m_test, visitor);
      }

      super.callChildVisitors(visitor, callAttrs);
   }
}

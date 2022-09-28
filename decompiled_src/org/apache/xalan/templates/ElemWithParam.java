package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XRTreeFrag;
import org.apache.xpath.objects.XString;

public class ElemWithParam extends ElemTemplateElement {
   static final long serialVersionUID = -1070355175864326257L;
   int m_index;
   private XPath m_selectPattern = null;
   private QName m_qname = null;
   int m_qnameID;

   public void setSelect(XPath v) {
      this.m_selectPattern = v;
   }

   public XPath getSelect() {
      return this.m_selectPattern;
   }

   public void setName(QName v) {
      this.m_qname = v;
   }

   public QName getName() {
      return this.m_qname;
   }

   public int getXSLToken() {
      return 2;
   }

   public String getNodeName() {
      return "with-param";
   }

   public void compose(StylesheetRoot sroot) throws TransformerException {
      if (null == this.m_selectPattern && sroot.getOptimizer()) {
         XPath newSelect = ElemVariable.rewriteChildToExpression(this);
         if (null != newSelect) {
            this.m_selectPattern = newSelect;
         }
      }

      this.m_qnameID = sroot.getComposeState().getQNameID(this.m_qname);
      super.compose(sroot);
      Vector vnames = sroot.getComposeState().getVariableNames();
      if (null != this.m_selectPattern) {
         this.m_selectPattern.fixupVariables(vnames, sroot.getComposeState().getGlobalsSize());
      }

   }

   public void setParentElem(ElemTemplateElement p) {
      super.setParentElem(p);
      p.m_hasVariableDecl = true;
   }

   public XObject getValue(TransformerImpl transformer, int sourceNode) throws TransformerException {
      XPathContext xctxt = transformer.getXPathContext();
      xctxt.pushCurrentNode(sourceNode);

      Object var;
      try {
         if (null != this.m_selectPattern) {
            var = this.m_selectPattern.execute(xctxt, sourceNode, this);
            ((XObject)var).allowDetachToRelease(false);
            if (transformer.getDebug()) {
               transformer.getTraceManager().fireSelectedEvent(sourceNode, this, "select", this.m_selectPattern, (XObject)var);
            }
         } else if (null == this.getFirstChildElem()) {
            var = XString.EMPTYSTRING;
         } else {
            int df = transformer.transformToRTF(this);
            var = new XRTreeFrag(df, xctxt, this);
         }
      } finally {
         xctxt.popCurrentNode();
      }

      return (XObject)var;
   }

   protected void callChildVisitors(XSLTVisitor visitor, boolean callAttrs) {
      if (callAttrs && null != this.m_selectPattern) {
         this.m_selectPattern.getExpression().callVisitors(this.m_selectPattern, visitor);
      }

      super.callChildVisitors(visitor, callAttrs);
   }

   public ElemTemplateElement appendChild(ElemTemplateElement elem) {
      if (this.m_selectPattern != null) {
         this.error("ER_CANT_HAVE_CONTENT_AND_SELECT", new Object[]{"xsl:" + this.getNodeName()});
         return null;
      } else {
         return super.appendChild(elem);
      }
   }
}

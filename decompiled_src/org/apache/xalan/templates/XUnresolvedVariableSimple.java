package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;

public class XUnresolvedVariableSimple extends XObject {
   static final long serialVersionUID = -1224413807443958985L;

   public XUnresolvedVariableSimple(ElemVariable obj) {
      super(obj);
   }

   public XObject execute(XPathContext xctxt) throws TransformerException {
      Expression expr = ((ElemVariable)super.m_obj).getSelect().getExpression();
      XObject xobj = expr.execute(xctxt);
      xobj.allowDetachToRelease(false);
      return xobj;
   }

   public int getType() {
      return 600;
   }

   public String getTypeString() {
      return "XUnresolvedVariableSimple (" + this.object().getClass().getName() + ")";
   }
}

package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

public class FuncQname extends FunctionDef1Arg {
   static final long serialVersionUID = -1532307875532617380L;

   public XObject execute(XPathContext xctxt) throws TransformerException {
      int context = this.getArg0AsNode(xctxt);
      XString val;
      if (-1 != context) {
         DTM dtm = xctxt.getDTM(context);
         String qname = dtm.getNodeNameX(context);
         val = null == qname ? XString.EMPTYSTRING : new XString(qname);
      } else {
         val = XString.EMPTYSTRING;
      }

      return val;
   }
}

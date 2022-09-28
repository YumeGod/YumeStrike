package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XString;
import org.apache.xpath.res.XPATHMessages;

public class FunctionDef1Arg extends FunctionOneArg {
   static final long serialVersionUID = 2325189412814149264L;

   protected int getArg0AsNode(XPathContext xctxt) throws TransformerException {
      return null == super.m_arg0 ? xctxt.getCurrentNode() : super.m_arg0.asNode(xctxt);
   }

   public boolean Arg0IsNodesetExpr() {
      return null == super.m_arg0 ? true : super.m_arg0.isNodesetExpr();
   }

   protected XMLString getArg0AsString(XPathContext xctxt) throws TransformerException {
      if (null == super.m_arg0) {
         int currentNode = xctxt.getCurrentNode();
         if (-1 == currentNode) {
            return XString.EMPTYSTRING;
         } else {
            DTM dtm = xctxt.getDTM(currentNode);
            return dtm.getStringValue(currentNode);
         }
      } else {
         return super.m_arg0.execute(xctxt).xstr();
      }
   }

   protected double getArg0AsNumber(XPathContext xctxt) throws TransformerException {
      if (null == super.m_arg0) {
         int currentNode = xctxt.getCurrentNode();
         if (-1 == currentNode) {
            return 0.0;
         } else {
            DTM dtm = xctxt.getDTM(currentNode);
            XMLString str = dtm.getStringValue(currentNode);
            return str.toDouble();
         }
      } else {
         return super.m_arg0.execute(xctxt).num();
      }
   }

   public void checkNumberArgs(int argNum) throws WrongNumberArgsException {
      if (argNum > 1) {
         this.reportWrongNumberArgs();
      }

   }

   protected void reportWrongNumberArgs() throws WrongNumberArgsException {
      throw new WrongNumberArgsException(XPATHMessages.createXPATHMessage("ER_ZERO_OR_ONE", (Object[])null));
   }

   public boolean canTraverseOutsideSubtree() {
      return null == super.m_arg0 ? false : super.canTraverseOutsideSubtree();
   }
}

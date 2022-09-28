package org.apache.xpath.operations;

import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

public class Number extends UnaryOperation {
   static final long serialVersionUID = 7196954482871619765L;

   public XObject operate(XObject right) throws TransformerException {
      return (XObject)(2 == right.getType() ? right : new XNumber(right.num()));
   }

   public double num(XPathContext xctxt) throws TransformerException {
      return super.m_right.num(xctxt);
   }
}

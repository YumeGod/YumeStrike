package org.apache.xpath.functions;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.axes.PredicatedNodeTest;
import org.apache.xpath.axes.SubContextList;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.patterns.StepPattern;

public class FuncCurrent extends Function {
   static final long serialVersionUID = 5715316804877715008L;

   public XObject execute(XPathContext xctxt) throws TransformerException {
      SubContextList subContextList = xctxt.getCurrentNodeList();
      int currentNode = -1;
      if (null != subContextList) {
         if (subContextList instanceof PredicatedNodeTest) {
            LocPathIterator iter = ((PredicatedNodeTest)subContextList).getLocPathIterator();
            currentNode = iter.getCurrentContextNode();
         } else if (subContextList instanceof StepPattern) {
            throw new RuntimeException(XSLMessages.createMessage("ER_PROCESSOR_ERROR", (Object[])null));
         }
      } else {
         currentNode = xctxt.getContextNode();
      }

      return new XNodeSet(currentNode, xctxt.getDTMManager());
   }

   public void fixupVariables(Vector vars, int globalsSize) {
   }
}

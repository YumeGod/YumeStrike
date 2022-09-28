package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.compiler.Compiler;

public class ChildIterator extends LocPathIterator {
   static final long serialVersionUID = -6935428015142993583L;

   ChildIterator(Compiler compiler, int opPos, int analysis) throws TransformerException {
      super(compiler, opPos, analysis, false);
      this.initNodeTest(-1);
   }

   public int asNode(XPathContext xctxt) throws TransformerException {
      int current = xctxt.getCurrentNode();
      DTM dtm = xctxt.getDTM(current);
      return dtm.getFirstChild(current);
   }

   public int nextNode() {
      if (super.m_foundLast) {
         return -1;
      } else {
         int next;
         super.m_lastFetched = next = -1 == super.m_lastFetched ? super.m_cdtm.getFirstChild(super.m_context) : super.m_cdtm.getNextSibling(super.m_lastFetched);
         if (-1 != next) {
            ++super.m_pos;
            return next;
         } else {
            super.m_foundLast = true;
            return -1;
         }
      }
   }

   public int getAxis() {
      return 3;
   }
}

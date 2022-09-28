package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPathContext;
import org.apache.xpath.compiler.Compiler;

public class SelfIteratorNoPredicate extends LocPathIterator {
   static final long serialVersionUID = -4226887905279814201L;

   SelfIteratorNoPredicate(Compiler compiler, int opPos, int analysis) throws TransformerException {
      super(compiler, opPos, analysis, false);
   }

   public SelfIteratorNoPredicate() throws TransformerException {
      super((PrefixResolver)null);
   }

   public int nextNode() {
      if (super.m_foundLast) {
         return -1;
      } else {
         DTM dtm = super.m_cdtm;
         int var10001 = -1 == super.m_lastFetched ? super.m_context : -1;
         int next = var10001;
         super.m_lastFetched = var10001;
         if (-1 != next) {
            ++super.m_pos;
            return next;
         } else {
            super.m_foundLast = true;
            return -1;
         }
      }
   }

   public int asNode(XPathContext xctxt) throws TransformerException {
      return xctxt.getCurrentNode();
   }

   public int getLastPos(XPathContext xctxt) {
      return 1;
   }
}

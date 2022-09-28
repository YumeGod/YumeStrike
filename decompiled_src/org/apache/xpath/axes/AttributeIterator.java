package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xpath.compiler.Compiler;

public class AttributeIterator extends ChildTestIterator {
   static final long serialVersionUID = -8417986700712229686L;

   AttributeIterator(Compiler compiler, int opPos, int analysis) throws TransformerException {
      super(compiler, opPos, analysis);
   }

   protected int getNextNode() {
      super.m_lastFetched = -1 == super.m_lastFetched ? super.m_cdtm.getFirstAttribute(super.m_context) : super.m_cdtm.getNextAttribute(super.m_lastFetched);
      return super.m_lastFetched;
   }

   public int getAxis() {
      return 2;
   }
}

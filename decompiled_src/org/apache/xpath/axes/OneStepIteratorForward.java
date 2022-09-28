package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xpath.Expression;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;

public class OneStepIteratorForward extends ChildTestIterator {
   static final long serialVersionUID = -1576936606178190566L;
   protected int m_axis = -1;

   OneStepIteratorForward(Compiler compiler, int opPos, int analysis) throws TransformerException {
      super(compiler, opPos, analysis);
      int firstStepPos = OpMap.getFirstChildPos(opPos);
      this.m_axis = WalkerFactory.getAxisFromStep(compiler, firstStepPos);
   }

   public OneStepIteratorForward(int axis) {
      super((DTMAxisTraverser)null);
      this.m_axis = axis;
      int whatToShow = -1;
      this.initNodeTest(whatToShow);
   }

   public void setRoot(int context, Object environment) {
      super.setRoot(context, environment);
      super.m_traverser = super.m_cdtm.getAxisTraverser(this.m_axis);
   }

   protected int getNextNode() {
      super.m_lastFetched = -1 == super.m_lastFetched ? super.m_traverser.first(super.m_context) : super.m_traverser.next(super.m_context, super.m_lastFetched);
      return super.m_lastFetched;
   }

   public int getAxis() {
      return this.m_axis;
   }

   public boolean deepEquals(Expression expr) {
      if (!super.deepEquals(expr)) {
         return false;
      } else {
         return this.m_axis == ((OneStepIteratorForward)expr).m_axis;
      }
   }
}

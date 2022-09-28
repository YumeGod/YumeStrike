package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.VariableStack;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;

public abstract class BasicTestIterator extends LocPathIterator {
   static final long serialVersionUID = 3505378079378096623L;

   protected BasicTestIterator() {
   }

   protected BasicTestIterator(PrefixResolver nscontext) {
      super(nscontext);
   }

   protected BasicTestIterator(Compiler compiler, int opPos, int analysis) throws TransformerException {
      super(compiler, opPos, analysis, false);
      int firstStepPos = OpMap.getFirstChildPos(opPos);
      int whatToShow = compiler.getWhatToShow(firstStepPos);
      if (0 != (whatToShow & 4163) && whatToShow != -1) {
         this.initNodeTest(whatToShow, compiler.getStepNS(firstStepPos), compiler.getStepLocalName(firstStepPos));
      } else {
         this.initNodeTest(whatToShow);
      }

      this.initPredicateInfo(compiler, firstStepPos);
   }

   protected BasicTestIterator(Compiler compiler, int opPos, int analysis, boolean shouldLoadWalkers) throws TransformerException {
      super(compiler, opPos, analysis, shouldLoadWalkers);
   }

   protected abstract int getNextNode();

   public int nextNode() {
      if (super.m_foundLast) {
         super.m_lastFetched = -1;
         return -1;
      } else {
         if (-1 == super.m_lastFetched) {
            this.resetProximityPositions();
         }

         VariableStack vars;
         int savedStart;
         if (-1 != super.m_stackFrame) {
            vars = super.m_execContext.getVarStack();
            savedStart = vars.getStackFrame();
            vars.setStackFrame(super.m_stackFrame);
         } else {
            vars = null;
            savedStart = 0;
         }

         int var4;
         try {
            int next;
            do {
               next = this.getNextNode();
            } while(-1 != next && 1 != this.acceptNode(next) && next != -1);

            if (-1 != next) {
               ++super.m_pos;
               var4 = next;
               return var4;
            }

            super.m_foundLast = true;
            var4 = -1;
         } finally {
            if (-1 != super.m_stackFrame) {
               vars.setStackFrame(savedStart);
            }

         }

         return var4;
      }
   }

   public DTMIterator cloneWithReset() throws CloneNotSupportedException {
      ChildTestIterator clone = (ChildTestIterator)super.cloneWithReset();
      clone.resetProximityPositions();
      return clone;
   }
}

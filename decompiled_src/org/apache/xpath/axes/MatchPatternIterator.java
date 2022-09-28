package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.patterns.NodeTest;
import org.apache.xpath.patterns.StepPattern;

public class MatchPatternIterator extends LocPathIterator {
   static final long serialVersionUID = -5201153767396296474L;
   protected StepPattern m_pattern;
   protected int m_superAxis = -1;
   protected DTMAxisTraverser m_traverser;
   private static final boolean DEBUG = false;

   MatchPatternIterator(Compiler compiler, int opPos, int analysis) throws TransformerException {
      super(compiler, opPos, analysis, false);
      int firstStepPos = OpMap.getFirstChildPos(opPos);
      this.m_pattern = WalkerFactory.loadSteps(this, compiler, firstStepPos, 0);
      boolean fromRoot = false;
      boolean walkBack = false;
      boolean walkDescendants = false;
      boolean walkAttributes = false;
      if (0 != (analysis & 671088640)) {
         fromRoot = true;
      }

      if (0 != (analysis & 98066432)) {
         walkBack = true;
      }

      if (0 != (analysis & 458752)) {
         walkDescendants = true;
      }

      if (0 != (analysis & 2129920)) {
         walkAttributes = true;
      }

      if (!fromRoot && !walkBack) {
         if (walkDescendants) {
            if (walkAttributes) {
               this.m_superAxis = 14;
            } else {
               this.m_superAxis = 5;
            }
         } else {
            this.m_superAxis = 16;
         }
      } else if (walkAttributes) {
         this.m_superAxis = 16;
      } else {
         this.m_superAxis = 17;
      }

   }

   public void setRoot(int context, Object environment) {
      super.setRoot(context, environment);
      this.m_traverser = super.m_cdtm.getAxisTraverser(this.m_superAxis);
   }

   public void detach() {
      if (super.m_allowDetach) {
         this.m_traverser = null;
         super.detach();
      }

   }

   protected int getNextNode() {
      super.m_lastFetched = -1 == super.m_lastFetched ? this.m_traverser.first(super.m_context) : this.m_traverser.next(super.m_context, super.m_lastFetched);
      return super.m_lastFetched;
   }

   public int nextNode() {
      if (super.m_foundLast) {
         return -1;
      } else {
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
            } while(-1 != next && 1 != this.acceptNode(next, super.m_execContext) && next != -1);

            if (-1 == next) {
               super.m_foundLast = true;
               byte var9 = -1;
               return var9;
            }

            this.incrementCurrentPos();
            var4 = next;
         } finally {
            if (-1 != super.m_stackFrame) {
               vars.setStackFrame(savedStart);
            }

         }

         return var4;
      }
   }

   public short acceptNode(int n, XPathContext xctxt) {
      int var4;
      try {
         xctxt.pushCurrentNode(n);
         xctxt.pushIteratorRoot(super.m_context);
         XObject score = this.m_pattern.execute(xctxt);
         var4 = score == NodeTest.SCORE_NONE ? 3 : 1;
      } catch (TransformerException var9) {
         throw new RuntimeException(var9.getMessage());
      } finally {
         xctxt.popCurrentNode();
         xctxt.popIteratorRoot();
      }

      return (short)var4;
   }
}

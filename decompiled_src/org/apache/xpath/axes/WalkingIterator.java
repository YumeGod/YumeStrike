package org.apache.xpath.axes;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;

public class WalkingIterator extends LocPathIterator implements ExpressionOwner {
   static final long serialVersionUID = 9110225941815665906L;
   protected AxesWalker m_lastUsedWalker;
   protected AxesWalker m_firstWalker;

   WalkingIterator(Compiler compiler, int opPos, int analysis, boolean shouldLoadWalkers) throws TransformerException {
      super(compiler, opPos, analysis, shouldLoadWalkers);
      int firstStepPos = OpMap.getFirstChildPos(opPos);
      if (shouldLoadWalkers) {
         this.m_firstWalker = WalkerFactory.loadWalkers(this, compiler, firstStepPos, 0);
         this.m_lastUsedWalker = this.m_firstWalker;
      }

   }

   public WalkingIterator(PrefixResolver nscontext) {
      super(nscontext);
   }

   public int getAnalysisBits() {
      int bits = 0;
      if (null != this.m_firstWalker) {
         for(AxesWalker walker = this.m_firstWalker; null != walker; walker = walker.getNextWalker()) {
            int bit = walker.getAnalysisBits();
            bits |= bit;
         }
      }

      return bits;
   }

   public Object clone() throws CloneNotSupportedException {
      WalkingIterator clone = (WalkingIterator)super.clone();
      if (null != this.m_firstWalker) {
         clone.m_firstWalker = this.m_firstWalker.cloneDeep(clone, (Vector)null);
      }

      return clone;
   }

   public void reset() {
      super.reset();
      if (null != this.m_firstWalker) {
         this.m_lastUsedWalker = this.m_firstWalker;
         this.m_firstWalker.setRoot(super.m_context);
      }

   }

   public void setRoot(int context, Object environment) {
      super.setRoot(context, environment);
      if (null != this.m_firstWalker) {
         this.m_firstWalker.setRoot(context);
         this.m_lastUsedWalker = this.m_firstWalker;
      }

   }

   public int nextNode() {
      if (super.m_foundLast) {
         return -1;
      } else if (-1 == super.m_stackFrame) {
         return this.returnNextNode(this.m_firstWalker.nextNode());
      } else {
         VariableStack vars = super.m_execContext.getVarStack();
         int savedStart = vars.getStackFrame();
         vars.setStackFrame(super.m_stackFrame);
         int n = this.returnNextNode(this.m_firstWalker.nextNode());
         vars.setStackFrame(savedStart);
         return n;
      }
   }

   public final AxesWalker getFirstWalker() {
      return this.m_firstWalker;
   }

   public final void setFirstWalker(AxesWalker walker) {
      this.m_firstWalker = walker;
   }

   public final void setLastUsedWalker(AxesWalker walker) {
      this.m_lastUsedWalker = walker;
   }

   public final AxesWalker getLastUsedWalker() {
      return this.m_lastUsedWalker;
   }

   public void detach() {
      if (super.m_allowDetach) {
         for(AxesWalker walker = this.m_firstWalker; null != walker; walker = walker.getNextWalker()) {
            walker.detach();
         }

         this.m_lastUsedWalker = null;
         super.detach();
      }

   }

   public void fixupVariables(Vector vars, int globalsSize) {
      super.m_predicateIndex = -1;

      for(AxesWalker walker = this.m_firstWalker; null != walker; walker = walker.getNextWalker()) {
         walker.fixupVariables(vars, globalsSize);
      }

   }

   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
      if (visitor.visitLocationPath(owner, this) && null != this.m_firstWalker) {
         this.m_firstWalker.callVisitors(this, visitor);
      }

   }

   public Expression getExpression() {
      return this.m_firstWalker;
   }

   public void setExpression(Expression exp) {
      exp.exprSetParent(this);
      this.m_firstWalker = (AxesWalker)exp;
   }

   public boolean deepEquals(Expression expr) {
      if (!super.deepEquals(expr)) {
         return false;
      } else {
         AxesWalker walker1 = this.m_firstWalker;

         AxesWalker walker2;
         for(walker2 = ((WalkingIterator)expr).m_firstWalker; null != walker1 && null != walker2; walker2 = walker2.getNextWalker()) {
            if (!walker1.deepEquals(walker2)) {
               return false;
            }

            walker1 = walker1.getNextWalker();
         }

         return null == walker1 && null == walker2;
      }
   }
}

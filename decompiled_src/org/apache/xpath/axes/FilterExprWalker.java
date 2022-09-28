package org.apache.xpath.axes;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.operations.Variable;

public class FilterExprWalker extends AxesWalker {
   static final long serialVersionUID = 5457182471424488375L;
   private Expression m_expr;
   private transient XNodeSet m_exprObj;
   private boolean m_mustHardReset = false;
   private boolean m_canDetachNodeset = true;

   public FilterExprWalker(WalkingIterator locPathIterator) {
      super(locPathIterator, 20);
   }

   public void init(Compiler compiler, int opPos, int stepType) throws TransformerException {
      super.init(compiler, opPos, stepType);
      switch (stepType) {
         case 24:
         case 25:
            this.m_mustHardReset = true;
         case 22:
         case 23:
            this.m_expr = compiler.compile(opPos);
            this.m_expr.exprSetParent(this);
            if (this.m_expr instanceof Variable) {
               this.m_canDetachNodeset = false;
            }
            break;
         default:
            this.m_expr = compiler.compile(opPos + 2);
            this.m_expr.exprSetParent(this);
      }

   }

   public void detach() {
      super.detach();
      if (this.m_canDetachNodeset) {
         this.m_exprObj.detach();
      }

      this.m_exprObj = null;
   }

   public void setRoot(int root) {
      super.setRoot(root);
      this.m_exprObj = FilterExprIteratorSimple.executeFilterExpr(root, super.m_lpi.getXPathContext(), super.m_lpi.getPrefixResolver(), super.m_lpi.getIsTopLevel(), super.m_lpi.m_stackFrame, this.m_expr);
   }

   public Object clone() throws CloneNotSupportedException {
      FilterExprWalker clone = (FilterExprWalker)super.clone();
      if (null != this.m_exprObj) {
         clone.m_exprObj = (XNodeSet)this.m_exprObj.clone();
      }

      return clone;
   }

   public short acceptNode(int n) {
      try {
         if (this.getPredicateCount() > 0) {
            this.countProximityPosition(0);
            if (!this.executePredicates(n, super.m_lpi.getXPathContext())) {
               return 3;
            }
         }

         return 1;
      } catch (TransformerException var3) {
         throw new RuntimeException(var3.getMessage());
      }
   }

   public int getNextNode() {
      if (null != this.m_exprObj) {
         int next = this.m_exprObj.nextNode();
         return next;
      } else {
         return -1;
      }
   }

   public int getLastPos(XPathContext xctxt) {
      return this.m_exprObj.getLength();
   }

   public void fixupVariables(Vector vars, int globalsSize) {
      super.fixupVariables(vars, globalsSize);
      this.m_expr.fixupVariables(vars, globalsSize);
   }

   public Expression getInnerExpression() {
      return this.m_expr;
   }

   public void setInnerExpression(Expression expr) {
      expr.exprSetParent(this);
      this.m_expr = expr;
   }

   public int getAnalysisBits() {
      return null != this.m_expr && this.m_expr instanceof PathComponent ? ((PathComponent)this.m_expr).getAnalysisBits() : 67108864;
   }

   public boolean isDocOrdered() {
      return this.m_exprObj.isDocOrdered();
   }

   public int getAxis() {
      return this.m_exprObj.getAxis();
   }

   public void callPredicateVisitors(XPathVisitor visitor) {
      this.m_expr.callVisitors(new filterExprOwner(), visitor);
      super.callPredicateVisitors(visitor);
   }

   public boolean deepEquals(Expression expr) {
      if (!super.deepEquals(expr)) {
         return false;
      } else {
         FilterExprWalker walker = (FilterExprWalker)expr;
         return this.m_expr.deepEquals(walker.m_expr);
      }
   }

   class filterExprOwner implements ExpressionOwner {
      public Expression getExpression() {
         return FilterExprWalker.this.m_expr;
      }

      public void setExpression(Expression exp) {
         exp.exprSetParent(FilterExprWalker.this);
         FilterExprWalker.this.m_expr = exp;
      }
   }
}

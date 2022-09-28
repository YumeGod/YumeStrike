package org.apache.xpath.axes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.patterns.NodeTest;

public abstract class PredicatedNodeTest extends NodeTest implements SubContextList {
   static final long serialVersionUID = -6193530757296377351L;
   protected int m_predCount = -1;
   protected transient boolean m_foundLast = false;
   protected LocPathIterator m_lpi;
   transient int m_predicateIndex = -1;
   private Expression[] m_predicates;
   protected transient int[] m_proximityPositions;
   static final boolean DEBUG_PREDICATECOUNTING = false;

   PredicatedNodeTest(LocPathIterator locPathIterator) {
      this.m_lpi = locPathIterator;
   }

   PredicatedNodeTest() {
   }

   private void readObject(ObjectInputStream stream) throws IOException, TransformerException {
      try {
         stream.defaultReadObject();
         this.m_predicateIndex = -1;
         this.resetProximityPositions();
      } catch (ClassNotFoundException var3) {
         throw new TransformerException(var3);
      }
   }

   public Object clone() throws CloneNotSupportedException {
      PredicatedNodeTest clone = (PredicatedNodeTest)super.clone();
      if (null != this.m_proximityPositions && this.m_proximityPositions == clone.m_proximityPositions) {
         clone.m_proximityPositions = new int[this.m_proximityPositions.length];
         System.arraycopy(this.m_proximityPositions, 0, clone.m_proximityPositions, 0, this.m_proximityPositions.length);
      }

      if (clone.m_lpi == this) {
         clone.m_lpi = (LocPathIterator)clone;
      }

      return clone;
   }

   public int getPredicateCount() {
      if (-1 == this.m_predCount) {
         return null == this.m_predicates ? 0 : this.m_predicates.length;
      } else {
         return this.m_predCount;
      }
   }

   public void setPredicateCount(int count) {
      if (count > 0) {
         Expression[] newPredicates = new Expression[count];

         for(int i = 0; i < count; ++i) {
            newPredicates[i] = this.m_predicates[i];
         }

         this.m_predicates = newPredicates;
      } else {
         this.m_predicates = null;
      }

   }

   protected void initPredicateInfo(Compiler compiler, int opPos) throws TransformerException {
      int pos = compiler.getFirstPredicateOpPos(opPos);
      if (pos > 0) {
         this.m_predicates = compiler.getCompiledPredicates(pos);
         if (null != this.m_predicates) {
            for(int i = 0; i < this.m_predicates.length; ++i) {
               this.m_predicates[i].exprSetParent(this);
            }
         }
      }

   }

   public Expression getPredicate(int index) {
      return this.m_predicates[index];
   }

   public int getProximityPosition() {
      return this.getProximityPosition(this.m_predicateIndex);
   }

   public int getProximityPosition(XPathContext xctxt) {
      return this.getProximityPosition();
   }

   public abstract int getLastPos(XPathContext var1);

   protected int getProximityPosition(int predicateIndex) {
      return predicateIndex >= 0 ? this.m_proximityPositions[predicateIndex] : 0;
   }

   public void resetProximityPositions() {
      int nPredicates = this.getPredicateCount();
      if (nPredicates > 0) {
         if (null == this.m_proximityPositions) {
            this.m_proximityPositions = new int[nPredicates];
         }

         for(int i = 0; i < nPredicates; ++i) {
            try {
               this.initProximityPosition(i);
            } catch (Exception var4) {
               throw new WrappedRuntimeException(var4);
            }
         }
      }

   }

   public void initProximityPosition(int i) throws TransformerException {
      this.m_proximityPositions[i] = 0;
   }

   protected void countProximityPosition(int i) {
      int[] pp = this.m_proximityPositions;
      if (null != pp && i < pp.length) {
         int var10002 = pp[i]++;
      }

   }

   public boolean isReverseAxes() {
      return false;
   }

   public int getPredicateIndex() {
      return this.m_predicateIndex;
   }

   boolean executePredicates(int context, XPathContext xctxt) throws TransformerException {
      int nPredicates = this.getPredicateCount();
      if (nPredicates == 0) {
         return true;
      } else {
         PrefixResolver savedResolver = xctxt.getNamespaceContext();

         try {
            this.m_predicateIndex = 0;
            xctxt.pushSubContextList(this);
            xctxt.pushNamespaceContext(this.m_lpi.getPrefixResolver());
            xctxt.pushCurrentNode(context);

            for(int i = 0; i < nPredicates; ++i) {
               XObject pred = this.m_predicates[i].execute(xctxt);
               if (2 == pred.getType()) {
                  int proxPos = this.getProximityPosition(this.m_predicateIndex);
                  int predIndex = (int)pred.num();
                  if (proxPos != predIndex) {
                     boolean var9 = false;
                     return var9;
                  }

                  if (this.m_predicates[i].isStableNumber() && i == nPredicates - 1) {
                     this.m_foundLast = true;
                  }
               } else if (!pred.bool()) {
                  boolean var14 = false;
                  return var14;
               }

               this.countProximityPosition(++this.m_predicateIndex);
            }
         } finally {
            xctxt.popCurrentNode();
            xctxt.popNamespaceContext();
            xctxt.popSubContextList();
            this.m_predicateIndex = -1;
         }

         return true;
      }
   }

   public void fixupVariables(Vector vars, int globalsSize) {
      super.fixupVariables(vars, globalsSize);
      int nPredicates = this.getPredicateCount();

      for(int i = 0; i < nPredicates; ++i) {
         this.m_predicates[i].fixupVariables(vars, globalsSize);
      }

   }

   protected String nodeToString(int n) {
      if (-1 != n) {
         DTM dtm = this.m_lpi.getXPathContext().getDTM(n);
         return dtm.getNodeName(n) + "{" + (n + 1) + "}";
      } else {
         return "null";
      }
   }

   public short acceptNode(int n) {
      XPathContext xctxt = this.m_lpi.getXPathContext();

      try {
         xctxt.pushCurrentNode(n);
         XObject score = this.execute(xctxt, n);
         if (score == NodeTest.SCORE_NONE) {
            return 3;
         } else {
            byte var4;
            if (this.getPredicateCount() > 0) {
               this.countProximityPosition(0);
               if (!this.executePredicates(n, xctxt)) {
                  var4 = 3;
                  return var4;
               }
            }

            var4 = 1;
            return var4;
         }
      } catch (TransformerException var9) {
         throw new RuntimeException(var9.getMessage());
      } finally {
         xctxt.popCurrentNode();
      }
   }

   public LocPathIterator getLocPathIterator() {
      return this.m_lpi;
   }

   public void setLocPathIterator(LocPathIterator li) {
      this.m_lpi = li;
      if (this != li) {
         li.exprSetParent(this);
      }

   }

   public boolean canTraverseOutsideSubtree() {
      int n = this.getPredicateCount();

      for(int i = 0; i < n; ++i) {
         if (this.getPredicate(i).canTraverseOutsideSubtree()) {
            return true;
         }
      }

      return false;
   }

   public void callPredicateVisitors(XPathVisitor visitor) {
      if (null != this.m_predicates) {
         int n = this.m_predicates.length;

         for(int i = 0; i < n; ++i) {
            ExpressionOwner predOwner = new PredOwner(i);
            if (visitor.visitPredicate(predOwner, this.m_predicates[i])) {
               this.m_predicates[i].callVisitors(predOwner, visitor);
            }
         }
      }

   }

   public boolean deepEquals(Expression expr) {
      if (!super.deepEquals(expr)) {
         return false;
      } else {
         PredicatedNodeTest pnt = (PredicatedNodeTest)expr;
         if (null != this.m_predicates) {
            int n = this.m_predicates.length;
            if (null == pnt.m_predicates || pnt.m_predicates.length != n) {
               return false;
            }

            for(int i = 0; i < n; ++i) {
               if (!this.m_predicates[i].deepEquals(pnt.m_predicates[i])) {
                  return false;
               }
            }
         } else if (null != pnt.m_predicates) {
            return false;
         }

         return true;
      }
   }

   class PredOwner implements ExpressionOwner {
      int m_index;

      PredOwner(int index) {
         this.m_index = index;
      }

      public Expression getExpression() {
         return PredicatedNodeTest.this.m_predicates[this.m_index];
      }

      public void setExpression(Expression exp) {
         exp.exprSetParent(PredicatedNodeTest.this);
         PredicatedNodeTest.this.m_predicates[this.m_index] = exp;
      }
   }
}

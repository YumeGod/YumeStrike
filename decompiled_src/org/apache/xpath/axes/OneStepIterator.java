package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;

public class OneStepIterator extends ChildTestIterator {
   static final long serialVersionUID = 4623710779664998283L;
   protected int m_axis = -1;
   protected DTMAxisIterator m_iterator;

   OneStepIterator(Compiler compiler, int opPos, int analysis) throws TransformerException {
      super(compiler, opPos, analysis);
      int firstStepPos = OpMap.getFirstChildPos(opPos);
      this.m_axis = WalkerFactory.getAxisFromStep(compiler, firstStepPos);
   }

   public OneStepIterator(DTMAxisIterator iterator, int axis) throws TransformerException {
      super((DTMAxisTraverser)null);
      this.m_iterator = iterator;
      this.m_axis = axis;
      int whatToShow = -1;
      this.initNodeTest(whatToShow);
   }

   public void setRoot(int context, Object environment) {
      super.setRoot(context, environment);
      if (this.m_axis > -1) {
         this.m_iterator = super.m_cdtm.getAxisIterator(this.m_axis);
      }

      this.m_iterator.setStartNode(super.m_context);
   }

   public void detach() {
      if (super.m_allowDetach) {
         if (this.m_axis > -1) {
            this.m_iterator = null;
         }

         super.detach();
      }

   }

   protected int getNextNode() {
      return super.m_lastFetched = this.m_iterator.next();
   }

   public Object clone() throws CloneNotSupportedException {
      OneStepIterator clone = (OneStepIterator)super.clone();
      if (this.m_iterator != null) {
         clone.m_iterator = this.m_iterator.cloneIterator();
      }

      return clone;
   }

   public DTMIterator cloneWithReset() throws CloneNotSupportedException {
      OneStepIterator clone = (OneStepIterator)super.cloneWithReset();
      clone.m_iterator = this.m_iterator;
      return clone;
   }

   public boolean isReverseAxes() {
      return this.m_iterator.isReverse();
   }

   protected int getProximityPosition(int predicateIndex) {
      if (!this.isReverseAxes()) {
         return super.getProximityPosition(predicateIndex);
      } else if (predicateIndex < 0) {
         return -1;
      } else {
         if (super.m_proximityPositions[predicateIndex] <= 0) {
            XPathContext xctxt = this.getXPathContext();

            try {
               OneStepIterator clone = (OneStepIterator)this.clone();
               int root = this.getRoot();
               xctxt.pushCurrentNode(root);
               clone.setRoot(root, xctxt);
               clone.m_predCount = predicateIndex;

               int count;
               for(count = 1; -1 != clone.nextNode(); ++count) {
               }

               int[] var10000 = super.m_proximityPositions;
               var10000[predicateIndex] += count;
            } catch (CloneNotSupportedException var11) {
            } finally {
               xctxt.popCurrentNode();
            }
         }

         return super.m_proximityPositions[predicateIndex];
      }
   }

   public int getLength() {
      if (!this.isReverseAxes()) {
         return super.getLength();
      } else {
         boolean isPredicateTest = this == super.m_execContext.getSubContextList();
         int predCount = this.getPredicateCount();
         if (-1 != super.m_length && isPredicateTest && super.m_predicateIndex < 1) {
            return super.m_length;
         } else {
            int count = 0;
            XPathContext xctxt = this.getXPathContext();

            try {
               OneStepIterator clone = (OneStepIterator)this.cloneWithReset();
               int root = this.getRoot();
               xctxt.pushCurrentNode(root);
               clone.setRoot(root, xctxt);

               for(clone.m_predCount = super.m_predicateIndex; -1 != clone.nextNode(); ++count) {
               }
            } catch (CloneNotSupportedException var12) {
            } finally {
               xctxt.popCurrentNode();
            }

            if (isPredicateTest && super.m_predicateIndex < 1) {
               super.m_length = count;
            }

            return count;
         }
      }
   }

   protected void countProximityPosition(int i) {
      if (!this.isReverseAxes()) {
         super.countProximityPosition(i);
      } else if (i < super.m_proximityPositions.length) {
         int var10002 = super.m_proximityPositions[i]--;
      }

   }

   public void reset() {
      super.reset();
      if (null != this.m_iterator) {
         this.m_iterator.reset();
      }

   }

   public int getAxis() {
      return this.m_axis;
   }

   public boolean deepEquals(Expression expr) {
      if (!super.deepEquals(expr)) {
         return false;
      } else {
         return this.m_axis == ((OneStepIterator)expr).m_axis;
      }
   }
}

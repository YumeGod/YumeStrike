package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.Expression;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;
import org.apache.xpath.patterns.NodeTest;

public class DescendantIterator extends LocPathIterator {
   static final long serialVersionUID = -1190338607743976938L;
   protected transient DTMAxisTraverser m_traverser;
   protected int m_axis;
   protected int m_extendedTypeID;

   DescendantIterator(Compiler compiler, int opPos, int analysis) throws TransformerException {
      super(compiler, opPos, analysis, false);
      int firstStepPos = OpMap.getFirstChildPos(opPos);
      int stepType = compiler.getOp(firstStepPos);
      boolean orSelf = 42 == stepType;
      boolean fromRoot = false;
      int nextStepPos;
      if (48 == stepType) {
         orSelf = true;
      } else if (50 == stepType) {
         fromRoot = true;
         nextStepPos = compiler.getNextStepPos(firstStepPos);
         if (compiler.getOp(nextStepPos) == 42) {
            orSelf = true;
         }
      }

      nextStepPos = firstStepPos;

      int whatToShow;
      while(true) {
         nextStepPos = compiler.getNextStepPos(nextStepPos);
         if (nextStepPos <= 0) {
            break;
         }

         whatToShow = compiler.getOp(nextStepPos);
         if (-1 == whatToShow) {
            break;
         }

         firstStepPos = nextStepPos;
      }

      if ((analysis & 65536) != 0) {
         orSelf = false;
      }

      if (fromRoot) {
         if (orSelf) {
            this.m_axis = 18;
         } else {
            this.m_axis = 17;
         }
      } else if (orSelf) {
         this.m_axis = 5;
      } else {
         this.m_axis = 4;
      }

      whatToShow = compiler.getWhatToShow(firstStepPos);
      if (0 != (whatToShow & 67) && whatToShow != -1) {
         this.initNodeTest(whatToShow, compiler.getStepNS(firstStepPos), compiler.getStepLocalName(firstStepPos));
      } else {
         this.initNodeTest(whatToShow);
      }

      this.initPredicateInfo(compiler, firstStepPos);
   }

   public DescendantIterator() {
      super((PrefixResolver)null);
      this.m_axis = 18;
      int whatToShow = -1;
      this.initNodeTest(whatToShow);
   }

   public DTMIterator cloneWithReset() throws CloneNotSupportedException {
      DescendantIterator clone = (DescendantIterator)super.cloneWithReset();
      clone.m_traverser = this.m_traverser;
      clone.resetProximityPositions();
      return clone;
   }

   public int nextNode() {
      if (super.m_foundLast) {
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
               if (0 == this.m_extendedTypeID) {
                  next = super.m_lastFetched = -1 == super.m_lastFetched ? this.m_traverser.first(super.m_context) : this.m_traverser.next(super.m_context, super.m_lastFetched);
               } else {
                  next = super.m_lastFetched = -1 == super.m_lastFetched ? this.m_traverser.first(super.m_context, this.m_extendedTypeID) : this.m_traverser.next(super.m_context, super.m_lastFetched, this.m_extendedTypeID);
               }
            } while(-1 != next && 1 != this.acceptNode(next) && next != -1);

            if (-1 == next) {
               super.m_foundLast = true;
               byte var9 = -1;
               return var9;
            }

            ++super.m_pos;
            var4 = next;
         } finally {
            if (-1 != super.m_stackFrame) {
               vars.setStackFrame(savedStart);
            }

         }

         return var4;
      }
   }

   public void setRoot(int context, Object environment) {
      super.setRoot(context, environment);
      this.m_traverser = super.m_cdtm.getAxisTraverser(this.m_axis);
      String localName = this.getLocalName();
      String namespace = this.getNamespace();
      int what = super.m_whatToShow;
      if (-1 != what && !"*".equals(localName) && !"*".equals(namespace)) {
         int type = NodeTest.getNodeTypeTest(what);
         this.m_extendedTypeID = super.m_cdtm.getExpandedTypeID(namespace, localName, type);
      } else {
         this.m_extendedTypeID = 0;
      }

   }

   public int asNode(XPathContext xctxt) throws TransformerException {
      if (this.getPredicateCount() > 0) {
         return super.asNode(xctxt);
      } else {
         int current = xctxt.getCurrentNode();
         DTM dtm = xctxt.getDTM(current);
         DTMAxisTraverser traverser = dtm.getAxisTraverser(this.m_axis);
         String localName = this.getLocalName();
         String namespace = this.getNamespace();
         int what = super.m_whatToShow;
         if (-1 != what && localName != "*" && namespace != "*") {
            int type = NodeTest.getNodeTypeTest(what);
            int extendedType = dtm.getExpandedTypeID(namespace, localName, type);
            return traverser.first(current, extendedType);
         } else {
            return traverser.first(current);
         }
      }
   }

   public void detach() {
      if (super.m_allowDetach) {
         this.m_traverser = null;
         this.m_extendedTypeID = 0;
         super.detach();
      }

   }

   public int getAxis() {
      return this.m_axis;
   }

   public boolean deepEquals(Expression expr) {
      if (!super.deepEquals(expr)) {
         return false;
      } else {
         return this.m_axis == ((DescendantIterator)expr).m_axis;
      }
   }
}

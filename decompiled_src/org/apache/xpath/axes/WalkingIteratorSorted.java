package org.apache.xpath.axes;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.compiler.Compiler;

public class WalkingIteratorSorted extends WalkingIterator {
   static final long serialVersionUID = -4512512007542368213L;
   protected boolean m_inNaturalOrderStatic = false;

   public WalkingIteratorSorted(PrefixResolver nscontext) {
      super(nscontext);
   }

   WalkingIteratorSorted(Compiler compiler, int opPos, int analysis, boolean shouldLoadWalkers) throws TransformerException {
      super(compiler, opPos, analysis, shouldLoadWalkers);
   }

   public boolean isDocOrdered() {
      return this.m_inNaturalOrderStatic;
   }

   boolean canBeWalkedInNaturalDocOrderStatic() {
      if (null == super.m_firstWalker) {
         return false;
      } else {
         AxesWalker walker = super.m_firstWalker;
         int prevAxis = true;
         boolean prevIsSimpleDownAxis = true;

         for(int i = 0; null != walker; ++i) {
            int axis = walker.getAxis();
            if (!walker.isDocOrdered()) {
               return false;
            }

            boolean isSimpleDownAxis = axis == 3 || axis == 13 || axis == 19;
            if (!isSimpleDownAxis && axis != -1) {
               boolean isLastWalker = null == walker.getNextWalker();
               if (!isLastWalker || (!walker.isDocOrdered() || axis != 4 && axis != 5 && axis != 17 && axis != 18) && axis != 2) {
                  return false;
               }

               return true;
            }

            walker = walker.getNextWalker();
         }

         return true;
      }
   }

   public void fixupVariables(Vector vars, int globalsSize) {
      super.fixupVariables(vars, globalsSize);
      int analysis = this.getAnalysisBits();
      if (WalkerFactory.isNaturalDocOrder(analysis)) {
         this.m_inNaturalOrderStatic = true;
      } else {
         this.m_inNaturalOrderStatic = false;
      }

   }
}

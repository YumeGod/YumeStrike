package org.apache.fop.layoutmgr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.traits.MinOptMax;

public class BalancingColumnBreakingAlgorithm extends PageBreakingAlgorithm {
   private Log log;
   private int columnCount;
   private int fullLen;
   private int idealPartLen;

   public BalancingColumnBreakingAlgorithm(LayoutManager topLevelLM, PageProvider pageProvider, PageBreakingAlgorithm.PageBreakingLayoutListener layoutListener, int alignment, int alignmentLast, MinOptMax footnoteSeparatorLength, boolean partOverflowRecovery, int columnCount) {
      super(topLevelLM, pageProvider, layoutListener, alignment, alignmentLast, footnoteSeparatorLength, partOverflowRecovery, false, false);
      this.log = LogFactory.getLog(BalancingColumnBreakingAlgorithm.class);
      this.columnCount = columnCount;
      this.considerTooShort = true;
   }

   protected double computeDemerits(BreakingAlgorithm.KnuthNode activeNode, KnuthElement element, int fitnessClass, double r) {
      double dem = super.computeDemerits(activeNode, element, fitnessClass, r);
      if (this.log.isTraceEnabled()) {
         this.log.trace("original demerit=" + dem + " " + this.totalWidth + " line=" + activeNode.line + "/" + this.columnCount + " pos=" + activeNode.position + "/" + (this.par.size() - 1));
      }

      int remParts = this.columnCount - activeNode.line;
      int curPos = this.par.indexOf(element);
      if (this.fullLen == 0) {
         this.fullLen = ElementListUtils.calcContentLength(this.par, activeNode.position, this.par.size() - 1);
         this.idealPartLen = this.fullLen / this.columnCount;
      }

      int partLen = ElementListUtils.calcContentLength(this.par, activeNode.position, curPos - 1);
      int restLen = ElementListUtils.calcContentLength(this.par, curPos - 1, this.par.size() - 1);
      int avgRestLen = 0;
      if (remParts > 0) {
         avgRestLen = restLen / remParts;
      }

      if (this.log.isTraceEnabled()) {
         this.log.trace("remaining parts: " + remParts + " rest len: " + restLen + " avg=" + avgRestLen);
      }

      double balance = (double)((float)(this.idealPartLen - partLen) / 1000.0F);
      if (this.log.isTraceEnabled()) {
         this.log.trace("balance=" + balance);
      }

      double absBalance = Math.abs(balance);
      dem = absBalance;
      if (this.columnCount > 2) {
         if (balance > 0.0) {
            dem = absBalance * 1.2000000476837158;
         }
      } else if (balance < 0.0) {
         dem = absBalance * 1.2000000476837158;
      }

      dem += (double)((float)avgRestLen / 1000.0F);
      if (activeNode.line >= this.columnCount) {
         dem = Double.MAX_VALUE;
      }

      if (this.log.isTraceEnabled()) {
         this.log.trace("effective dem=" + dem + " " + this.totalWidth);
      }

      return dem;
   }
}

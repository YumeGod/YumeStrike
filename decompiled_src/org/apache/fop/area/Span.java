package org.apache.fop.area;

import java.util.ArrayList;
import java.util.List;

public class Span extends Area {
   private List flowAreas;
   private int colCount;
   private int colGap;
   private int colWidth;
   private int curFlowIdx;

   public Span(int colCount, int colGap, int ipd) {
      this.addTrait(Trait.IS_REFERENCE_AREA, Boolean.TRUE);
      this.colCount = colCount;
      this.colGap = colGap;
      this.ipd = ipd;
      this.curFlowIdx = 0;
      this.createNormalFlows();
   }

   private void createNormalFlows() {
      this.flowAreas = new ArrayList(this.colCount);
      this.colWidth = (this.ipd - (this.colCount - 1) * this.colGap) / this.colCount;

      for(int i = 0; i < this.colCount; ++i) {
         NormalFlow newFlow = new NormalFlow(this.colWidth);
         this.flowAreas.add(newFlow);
      }

   }

   public int getColumnCount() {
      return this.colCount;
   }

   public int getColumnWidth() {
      return this.colWidth;
   }

   public int getHeight() {
      return this.getBPD();
   }

   public NormalFlow getNormalFlow(int colRequested) {
      if (colRequested >= 0 && colRequested < this.colCount) {
         return (NormalFlow)this.flowAreas.get(colRequested);
      } else {
         throw new IllegalArgumentException("Invalid column number " + colRequested + " requested; only 0-" + (this.colCount - 1) + " available.");
      }
   }

   public NormalFlow getCurrentFlow() {
      return this.getNormalFlow(this.curFlowIdx);
   }

   public int getCurrentFlowIndex() {
      return this.curFlowIdx;
   }

   public NormalFlow moveToNextFlow() {
      if (this.hasMoreFlows()) {
         ++this.curFlowIdx;
         return this.getNormalFlow(this.curFlowIdx);
      } else {
         throw new IllegalStateException("(Internal error.) No more flows left in span.");
      }
   }

   public boolean hasMoreFlows() {
      return this.curFlowIdx < this.colCount - 1;
   }

   public void notifyFlowsFinished() {
      int maxFlowBPD = Integer.MIN_VALUE;

      for(int i = 0; i < this.colCount; ++i) {
         maxFlowBPD = Math.max(maxFlowBPD, this.getNormalFlow(i).getAllocBPD());
      }

      this.bpd = maxFlowBPD;
   }

   public boolean isEmpty() {
      int areaCount = 0;

      for(int i = 0; i < this.getColumnCount(); ++i) {
         NormalFlow flow = this.getNormalFlow(i);
         if (flow != null && flow.getChildAreas() != null) {
            areaCount += flow.getChildAreas().size();
         }
      }

      return areaCount == 0;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer(super.toString());
      if (this.colCount > 1) {
         sb.append(" {colCount=").append(this.colCount);
         sb.append(", colWidth=").append(this.colWidth);
         sb.append(", curFlowIdx=").append(this.curFlowIdx);
         sb.append("}");
      }

      return sb.toString();
   }
}

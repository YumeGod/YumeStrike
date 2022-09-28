package org.apache.fop.layoutmgr;

import java.util.List;

public class BreakElement extends UnresolvedListElement {
   private int penaltyWidth;
   private int penaltyValue;
   private int breakClass;
   private List pendingBeforeMarks;
   private List pendingAfterMarks;

   public BreakElement(Position position, int penaltyValue, LayoutContext context) {
      this(position, penaltyValue, -1, context);
   }

   public BreakElement(Position position, int penaltyValue, int breakClass, LayoutContext context) {
      this(position, 0, penaltyValue, breakClass, context);
   }

   public BreakElement(Position position, int penaltyWidth, int penaltyValue, int breakClass, LayoutContext context) {
      super(position);
      this.breakClass = -1;
      this.penaltyWidth = penaltyWidth;
      this.penaltyValue = penaltyValue;
      this.breakClass = breakClass;
      this.pendingBeforeMarks = context.getPendingBeforeMarks();
      this.pendingAfterMarks = context.getPendingAfterMarks();
   }

   private static String getBreakClassName(int breakClass) {
      return AbstractBreaker.getBreakClassName(breakClass);
   }

   public boolean isConditional() {
      return false;
   }

   public int getPenaltyWidth() {
      return this.penaltyWidth;
   }

   public int getPenaltyValue() {
      return this.penaltyValue;
   }

   public void setPenaltyValue(int p) {
      this.penaltyValue = p;
   }

   public boolean isForcedBreak() {
      return this.penaltyValue == -1000;
   }

   public int getBreakClass() {
      return this.breakClass;
   }

   public void setBreakClass(int breakClass) {
      this.breakClass = breakClass;
   }

   public List getPendingBeforeMarks() {
      return this.pendingBeforeMarks;
   }

   public List getPendingAfterMarks() {
      return this.pendingAfterMarks;
   }

   public void clearPendingMarks() {
      this.pendingBeforeMarks = null;
      this.pendingAfterMarks = null;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer(64);
      sb.append("BreakPossibility[p:");
      sb.append(KnuthPenalty.valueOf(this.penaltyValue));
      if (this.isForcedBreak()) {
         sb.append(" (forced break, ").append(getBreakClassName(this.breakClass)).append(")");
      } else if (this.penaltyValue >= 0 && this.breakClass != -1) {
         sb.append(" (keep constraint, ").append(getBreakClassName(this.breakClass)).append(")");
      }

      sb.append("; w:");
      sb.append(this.penaltyWidth);
      sb.append("]");
      return sb.toString();
   }
}

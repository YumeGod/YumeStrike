package org.apache.fop.layoutmgr;

public class KnuthPenalty extends KnuthElement {
   public static final int FLAGGED_PENALTY = 50;
   private int penalty;
   private boolean penaltyFlagged;
   private int breakClass;

   public KnuthPenalty(int width, int penalty, boolean penaltyFlagged, Position pos, boolean auxiliary) {
      super(width, pos, auxiliary);
      this.breakClass = -1;
      this.penalty = penalty;
      this.penaltyFlagged = penaltyFlagged;
   }

   public KnuthPenalty(int width, int penalty, boolean penaltyFlagged, int breakClass, Position pos, boolean isAuxiliary) {
      this(width, penalty, penaltyFlagged, pos, isAuxiliary);
      this.breakClass = breakClass;
   }

   private static String getBreakClassName(int breakClass) {
      return AbstractBreaker.getBreakClassName(breakClass);
   }

   protected static String valueOf(int penaltyValue) {
      String result = penaltyValue < 0 ? "-" : "";
      int tmpValue = Math.abs(penaltyValue);
      result = result + (tmpValue == 1000 ? "INFINITE" : String.valueOf(tmpValue));
      return result;
   }

   public boolean isPenalty() {
      return true;
   }

   public int getPenalty() {
      return this.penalty;
   }

   public void setPenalty(int penalty) {
      this.penalty = penalty;
   }

   public boolean isPenaltyFlagged() {
      return this.penaltyFlagged;
   }

   public boolean isForcedBreak() {
      return this.penalty == -1000;
   }

   public int getBreakClass() {
      return this.breakClass;
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer(64);
      if (this.isAuxiliary()) {
         buffer.append("aux. ");
      }

      buffer.append("penalty");
      buffer.append(" p=");
      buffer.append(valueOf(this.penalty));
      if (this.penaltyFlagged) {
         buffer.append(" [flagged]");
      }

      buffer.append(" w=");
      buffer.append(this.getWidth());
      if (this.isForcedBreak()) {
         buffer.append(" (forced break, ").append(getBreakClassName(this.breakClass)).append(")");
      } else if (this.penalty >= 0 && this.breakClass != -1) {
         buffer.append(" (keep constraint, ").append(getBreakClassName(this.breakClass)).append(")");
      }

      return buffer.toString();
   }
}

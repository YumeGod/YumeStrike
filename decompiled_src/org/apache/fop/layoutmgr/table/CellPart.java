package org.apache.fop.layoutmgr.table;

import org.apache.fop.fo.flow.table.PrimaryGridUnit;

class CellPart {
   protected PrimaryGridUnit pgu;
   protected int start;
   protected int end;
   private int condBeforeContentLength;
   private int length;
   private int condAfterContentLength;
   private int bpBeforeNormal;
   private int bpBeforeFirst;
   private int bpAfterNormal;
   private int bpAfterLast;
   private boolean isLast;

   protected CellPart(PrimaryGridUnit pgu, int start, int end, boolean last, int condBeforeContentLength, int length, int condAfterContentLength, int bpBeforeNormal, int bpBeforeFirst, int bpAfterNormal, int bpAfterLast) {
      this.pgu = pgu;
      this.start = start;
      this.end = end;
      this.isLast = last;
      this.condBeforeContentLength = condBeforeContentLength;
      this.length = length;
      this.condAfterContentLength = condAfterContentLength;
      this.bpBeforeNormal = bpBeforeNormal;
      this.bpBeforeFirst = bpBeforeFirst;
      this.bpAfterNormal = bpAfterNormal;
      this.bpAfterLast = bpAfterLast;
   }

   public boolean isFirstPart() {
      return this.start == 0;
   }

   boolean isLastPart() {
      return this.isLast;
   }

   int getBorderPaddingBefore(boolean firstOnPage) {
      return firstOnPage ? this.bpBeforeFirst : this.bpBeforeNormal;
   }

   int getBorderPaddingAfter(boolean lastOnPage) {
      return lastOnPage ? this.bpAfterLast : this.bpAfterNormal;
   }

   int getConditionalBeforeContentLength() {
      return this.condBeforeContentLength;
   }

   int getLength() {
      return this.length;
   }

   int getConditionalAfterContentLength() {
      return this.condAfterContentLength;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer("Part: ");
      sb.append(this.start).append("-").append(this.end);
      sb.append(" [").append(this.isFirstPart() ? "F" : "-").append(this.isLastPart() ? "L" : "-");
      sb.append("] ").append(this.pgu);
      return sb.toString();
   }
}

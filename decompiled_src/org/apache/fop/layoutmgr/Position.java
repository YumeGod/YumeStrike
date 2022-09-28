package org.apache.fop.layoutmgr;

public class Position {
   private LayoutManager layoutManager;
   private int index;

   public Position(LayoutManager lm) {
      this.index = -1;
      this.layoutManager = lm;
   }

   public Position(LayoutManager lm, int index) {
      this(lm);
      this.setIndex(index);
   }

   public LayoutManager getLM() {
      return this.layoutManager;
   }

   public Position getPosition() {
      return null;
   }

   public boolean generatesAreas() {
      return false;
   }

   public void setIndex(int value) {
      this.index = value;
   }

   public int getIndex() {
      return this.index;
   }

   public String getShortLMName() {
      if (this.getLM() != null) {
         String lm = this.getLM().toString();
         int idx = lm.lastIndexOf(46);
         return idx >= 0 && lm.indexOf(64) > 0 ? lm.substring(idx + 1) : lm;
      } else {
         return "null";
      }
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("Position:").append(this.getIndex()).append("(");
      sb.append(this.getShortLMName());
      sb.append(")");
      return sb.toString();
   }
}

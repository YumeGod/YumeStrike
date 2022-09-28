package org.apache.fop.layoutmgr;

public class NonLeafPosition extends Position {
   private Position subPos;

   public NonLeafPosition(LayoutManager lm, Position sub) {
      super(lm);
      this.subPos = sub;
   }

   public Position getPosition() {
      return this.subPos;
   }

   public boolean generatesAreas() {
      return this.subPos != null ? this.subPos.generatesAreas() : false;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("NonLeafPos:").append(this.getIndex()).append("(");
      sb.append(this.getShortLMName());
      sb.append(", ");
      if (this.getPosition() != null) {
         sb.append(this.getPosition().toString());
      } else {
         sb.append("null");
      }

      sb.append(")");
      return sb.toString();
   }
}

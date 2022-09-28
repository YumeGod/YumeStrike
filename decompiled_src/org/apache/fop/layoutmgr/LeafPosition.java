package org.apache.fop.layoutmgr;

public class LeafPosition extends Position {
   private int leafPos;

   public LeafPosition(LayoutManager layoutManager, int pos) {
      super(layoutManager);
      this.leafPos = pos;
   }

   public LeafPosition(LayoutManager layoutManager, int pos, int index) {
      super(layoutManager, index);
      this.leafPos = pos;
   }

   public int getLeafPos() {
      return this.leafPos;
   }

   public boolean generatesAreas() {
      return this.getLM() != null;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("LeafPos:").append(this.getIndex()).append("(");
      sb.append("pos=").append(this.getLeafPos());
      sb.append(", lm=").append(this.getShortLMName()).append(")");
      return sb.toString();
   }
}

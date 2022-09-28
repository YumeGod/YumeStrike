package org.apache.fop.layoutmgr;

public abstract class UnresolvedListElement extends ListElement {
   public UnresolvedListElement(Position position) {
      super(position);
   }

   public abstract boolean isConditional();

   protected LayoutManager getOriginatingLayoutManager() {
      Position pos;
      for(pos = this.getPosition(); pos instanceof NonLeafPosition && pos.getPosition() != null; pos = pos.getPosition()) {
      }

      return pos.getLM();
   }
}

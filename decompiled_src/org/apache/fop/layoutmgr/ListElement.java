package org.apache.fop.layoutmgr;

public abstract class ListElement {
   private Position position;

   public ListElement(Position position) {
      this.position = position;
   }

   public Position getPosition() {
      return this.position;
   }

   public void setPosition(Position position) {
      this.position = position;
   }

   public LayoutManager getLayoutManager() {
      return this.position != null ? this.position.getLM() : null;
   }

   public boolean isBox() {
      return false;
   }

   public boolean isGlue() {
      return false;
   }

   public boolean isPenalty() {
      return false;
   }

   public boolean isForcedBreak() {
      return false;
   }

   public boolean isUnresolvedElement() {
      return true;
   }
}

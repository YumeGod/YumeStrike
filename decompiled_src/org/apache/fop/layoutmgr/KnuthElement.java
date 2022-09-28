package org.apache.fop.layoutmgr;

public abstract class KnuthElement extends ListElement {
   public static final int INFINITE = 1000;
   private int width;
   private boolean auxiliary;

   protected KnuthElement(int width, Position pos, boolean auxiliary) {
      super(pos);
      this.width = width;
      this.auxiliary = auxiliary;
   }

   public boolean isAuxiliary() {
      return this.auxiliary;
   }

   public int getWidth() {
      return this.width;
   }

   public int getPenalty() {
      throw new RuntimeException("Element is not a penalty");
   }

   public int getStretch() {
      throw new RuntimeException("Element is not a glue");
   }

   public int getShrink() {
      throw new RuntimeException("Element is not a glue");
   }

   public boolean isUnresolvedElement() {
      return false;
   }
}

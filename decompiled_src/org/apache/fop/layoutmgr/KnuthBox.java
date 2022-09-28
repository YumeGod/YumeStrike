package org.apache.fop.layoutmgr;

public class KnuthBox extends KnuthElement {
   public KnuthBox(int width, Position pos, boolean auxiliary) {
      super(width, pos, auxiliary);
   }

   public boolean isBox() {
      return true;
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer(64);
      if (this.isAuxiliary()) {
         buffer.append("aux. ");
      }

      buffer.append("box");
      buffer.append(" w=");
      buffer.append(this.getWidth());
      return buffer.toString();
   }
}

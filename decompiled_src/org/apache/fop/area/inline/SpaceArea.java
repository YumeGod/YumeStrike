package org.apache.fop.area.inline;

public class SpaceArea extends InlineArea {
   protected String space;
   protected boolean isAdjustable;

   public SpaceArea(char s, int o, boolean a) {
      this.space = new String() + s;
      this.offset = o;
      this.isAdjustable = a;
   }

   public String getSpace() {
      return new String(this.space);
   }

   public boolean isAdjustable() {
      return this.isAdjustable;
   }
}

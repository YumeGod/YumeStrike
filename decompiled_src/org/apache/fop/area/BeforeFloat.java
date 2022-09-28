package org.apache.fop.area;

public class BeforeFloat extends BlockParent {
   private Block separator = null;

   public void setSeparator(Block sep) {
      this.separator = sep;
   }

   public Block getSeparator() {
      return this.separator;
   }

   public int getBPD() {
      int h = super.getBPD();
      if (this.separator != null) {
         h += this.separator.getBPD();
      }

      return h;
   }

   public boolean isEmpty() {
      return true;
   }
}

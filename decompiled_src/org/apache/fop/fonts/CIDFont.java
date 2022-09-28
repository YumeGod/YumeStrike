package org.apache.fop.fonts;

public abstract class CIDFont extends CustomFont {
   protected int[] width = null;

   public abstract CIDFontType getCIDType();

   public abstract String getRegistry();

   public abstract String getOrdering();

   public abstract int getSupplement();

   public abstract CIDSubset getCIDSubset();

   public int getDefaultWidth() {
      return 0;
   }

   public boolean isMultiByte() {
      return true;
   }
}

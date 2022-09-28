package org.apache.batik.gvt.text;

public class TextHit {
   private int charIndex;
   private boolean leadingEdge;

   public TextHit(int var1, boolean var2) {
      this.charIndex = var1;
      this.leadingEdge = var2;
   }

   public int getCharIndex() {
      return this.charIndex;
   }

   public boolean isLeadingEdge() {
      return this.leadingEdge;
   }
}

package org.apache.batik.util.gui.xmleditor;

public class XMLToken {
   private int context;
   private int startOffset;
   private int endOffset;

   public XMLToken(int var1, int var2, int var3) {
      this.context = var1;
      this.startOffset = var2;
      this.endOffset = var3;
   }

   public int getContext() {
      return this.context;
   }

   public int getStartOffset() {
      return this.startOffset;
   }

   public int getEndOffset() {
      return this.endOffset;
   }
}

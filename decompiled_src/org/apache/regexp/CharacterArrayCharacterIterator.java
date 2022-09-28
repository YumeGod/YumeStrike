package org.apache.regexp;

public final class CharacterArrayCharacterIterator implements CharacterIterator {
   private final char[] src;
   private final int off;
   private final int len;

   public CharacterArrayCharacterIterator(char[] var1, int var2, int var3) {
      this.src = var1;
      this.off = var2;
      this.len = var3;
   }

   public char charAt(int var1) {
      return this.src[this.off + var1];
   }

   public boolean isEnd(int var1) {
      return var1 >= this.len;
   }

   public String substring(int var1) {
      return new String(this.src, this.off + var1, this.len);
   }

   public String substring(int var1, int var2) {
      return new String(this.src, this.off + var1, var2);
   }
}

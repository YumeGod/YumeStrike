package org.apache.regexp;

public final class StringCharacterIterator implements CharacterIterator {
   private final String src;

   public StringCharacterIterator(String var1) {
      this.src = var1;
   }

   public char charAt(int var1) {
      return this.src.charAt(var1);
   }

   public boolean isEnd(int var1) {
      return var1 >= this.src.length();
   }

   public String substring(int var1) {
      return this.src.substring(var1);
   }

   public String substring(int var1, int var2) {
      return this.src.substring(var1, var2);
   }
}

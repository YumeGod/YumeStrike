package org.apache.batik.gvt.font;

public class UnicodeRange {
   private int firstUnicodeValue;
   private int lastUnicodeValue;

   public UnicodeRange(String var1) {
      if (var1.startsWith("U+") && var1.length() > 2) {
         var1 = var1.substring(2);
         int var2 = var1.indexOf(45);
         String var3;
         String var4;
         if (var2 != -1) {
            var3 = var1.substring(0, var2);
            var4 = var1.substring(var2 + 1);
         } else {
            var3 = var1;
            var4 = var1;
            if (var1.indexOf(63) != -1) {
               var3 = var1.replace('?', '0');
               var4 = var1.replace('?', 'F');
            }
         }

         try {
            this.firstUnicodeValue = Integer.parseInt(var3, 16);
            this.lastUnicodeValue = Integer.parseInt(var4, 16);
         } catch (NumberFormatException var6) {
            this.firstUnicodeValue = -1;
            this.lastUnicodeValue = -1;
         }
      } else {
         this.firstUnicodeValue = -1;
         this.lastUnicodeValue = -1;
      }

   }

   public boolean contains(String var1) {
      if (var1.length() == 1) {
         char var2 = var1.charAt(0);
         return this.contains(var2);
      } else {
         return false;
      }
   }

   public boolean contains(int var1) {
      return var1 >= this.firstUnicodeValue && var1 <= this.lastUnicodeValue;
   }
}

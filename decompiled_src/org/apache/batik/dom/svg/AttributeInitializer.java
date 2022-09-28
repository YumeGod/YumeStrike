package org.apache.batik.dom.svg;

import org.apache.batik.util.DoublyIndexedTable;

public class AttributeInitializer {
   protected String[] keys;
   protected int length;
   protected DoublyIndexedTable values = new DoublyIndexedTable();

   public AttributeInitializer(int var1) {
      this.keys = new String[var1 * 3];
   }

   public void addAttribute(String var1, String var2, String var3, String var4) {
      int var5 = this.keys.length;
      if (this.length == var5) {
         String[] var6 = new String[var5 * 2];
         System.arraycopy(this.keys, 0, var6, 0, var5);
         this.keys = var6;
      }

      this.keys[this.length++] = var1;
      this.keys[this.length++] = var2;
      this.keys[this.length++] = var3;
      this.values.put(var1, var3, var4);
   }

   public void initializeAttributes(AbstractElement var1) {
      for(int var2 = this.length - 1; var2 >= 2; var2 -= 3) {
         this.resetAttribute(var1, this.keys[var2 - 2], this.keys[var2 - 1], this.keys[var2]);
      }

   }

   public boolean resetAttribute(AbstractElement var1, String var2, String var3, String var4) {
      String var5 = (String)this.values.get(var2, var4);
      if (var5 == null) {
         return false;
      } else {
         if (var3 != null) {
            var4 = var3 + ':' + var4;
         }

         var1.setUnspecifiedAttribute(var2, var4, var5);
         return true;
      }
   }
}

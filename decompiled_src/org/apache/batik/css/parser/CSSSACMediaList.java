package org.apache.batik.css.parser;

import org.w3c.css.sac.SACMediaList;

public class CSSSACMediaList implements SACMediaList {
   protected String[] list = new String[3];
   protected int length;

   public int getLength() {
      return this.length;
   }

   public String item(int var1) {
      return var1 >= 0 && var1 < this.length ? this.list[var1] : null;
   }

   public void append(String var1) {
      if (this.length == this.list.length) {
         String[] var2 = this.list;
         this.list = new String[1 + this.list.length + this.list.length / 2];
         System.arraycopy(var2, 0, this.list, 0, var2.length);
      }

      this.list[this.length++] = var1;
   }
}

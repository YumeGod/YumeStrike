package c2profile;

import common.CommonUtils;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

public class SmartBuffer implements Serializable {
   protected LinkedList data = new LinkedList();
   protected int prepend_len = 0;

   public SmartBuffer copy() {
      SmartBuffer var1 = new SmartBuffer();
      var1.data = new LinkedList(this.data);
      var1.prepend_len = this.prepend_len;
      return var1;
   }

   public int getDataOffset() {
      return this.prepend_len;
   }

   public void append(byte[] var1) {
      this.data.add(var1);
   }

   public void prepend(byte[] var1) {
      this.data.add(0, var1);
      this.prepend_len += var1.length;
   }

   public void clear() {
      this.prepend_len = 0;
      this.data.clear();
   }

   public void strrep(String var1, String var2) {
      Iterator var3 = (new LinkedList(this.data)).iterator();
      this.clear();

      while(var3.hasNext()) {
         byte[] var4 = (byte[])((byte[])var3.next());
         if (var4.length >= var1.length()) {
            this.append(CommonUtils.strrep(var4, var1, var2));
         } else {
            this.append(var4);
         }
      }

   }

   public Iterator iterator() {
      return this.data.iterator();
   }

   public byte[] getBytes() {
      if (this.data.size() == 1) {
         return (byte[])((byte[])this.data.getFirst());
      } else if (this.data.size() == 0) {
         return new byte[0];
      } else {
         byte[] var1 = new byte[this.size()];
         int var2 = 0;

         byte[] var4;
         for(Iterator var3 = this.data.iterator(); var3.hasNext(); var2 += var4.length) {
            var4 = (byte[])((byte[])var3.next());
            System.arraycopy(var4, 0, var1, var2, var4.length);
         }

         return var1;
      }
   }

   public int size() {
      if (this.data.size() == 1) {
         return ((byte[])((byte[])this.data.getFirst())).length;
      } else if (this.data.size() == 0) {
         return 0;
      } else {
         int var1 = 0;

         byte[] var3;
         for(Iterator var2 = this.data.iterator(); var2.hasNext(); var1 += var3.length) {
            var3 = (byte[])((byte[])var2.next());
         }

         return var1;
      }
   }

   public String toString() {
      byte[] var1 = this.getBytes();
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         char var4 = (char)var1[var3];
         if (!Character.isDigit(var4) && !Character.isLetter(var4) && !Character.isWhitespace(var4) && var4 != '%' && var4 != '!' && var4 != '.') {
            var2.append("[" + var1[var3] + "]");
         } else {
            var2.append(var4);
         }
      }

      return var2.toString();
   }
}

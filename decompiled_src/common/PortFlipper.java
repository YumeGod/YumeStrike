package common;

import java.util.Iterator;
import java.util.LinkedList;

public class PortFlipper {
   protected String ports;
   protected boolean hasError;
   protected String description;

   public boolean hasError() {
      return this.hasError;
   }

   public String getError() {
      return this.description;
   }

   public int check(int var1) {
      if (var1 < 0 || var1 > 65535) {
         this.hasError = true;
         this.description = "Invalid port value '" + var1 + "'";
      }

      return var1;
   }

   public LinkedList parse() {
      LinkedList var1 = new LinkedList();
      String[] var2 = this.ports.split(",");

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (CommonUtils.isNumber(var2[var3])) {
            var1.add(this.check(CommonUtils.toNumber(var2[var3], -1)));
         } else if (var2[var3].matches("\\d+-\\d+")) {
            String[] var4 = var2[var3].split("-");
            int var5 = this.check(CommonUtils.toNumber(var4[0], 0));

            for(int var6 = this.check(CommonUtils.toNumber(var4[1], 0)); var5 <= var6; ++var5) {
               var1.add(var5);
            }
         } else {
            this.description = "Invalid port or range '" + var2[var3] + "'";
            this.hasError = true;
         }
      }

      return var1;
   }

   public PortFlipper(String var1) {
      this.ports = var1;
   }

   private static void flip(byte[] var0, int var1) {
      int var2 = var1 / 8;
      int var3 = var1 % 8;
      var0[var2] = (byte)(var0[var2] + (1 << var3));
   }

   public Iterator iterator() {
      return this.parse().iterator();
   }

   public byte[] getMask() {
      byte[] var1 = new byte[8192];
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         flip(var1, (Integer)var2.next());
      }

      return var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         var1.append(var2.next());
         if (var2.hasNext()) {
            var1.append(", ");
         }
      }

      return var1.toString();
   }
}

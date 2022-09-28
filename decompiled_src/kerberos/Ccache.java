package kerberos;

import common.CommonUtils;
import common.DataParser;
import common.MudgeSanity;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Ccache {
   protected Principal primary_principal;
   protected List credentials = new LinkedList();

   public Ccache(String var1) {
      this.parse(CommonUtils.readFile(var1));
   }

   public void parse(byte[] var1) {
      try {
         DataParser var2 = new DataParser(var1);
         var2.big();
         int var3 = var2.readShort();
         if (var3 != 1284) {
            CommonUtils.print_error("VERSION FAIL: " + var3);
            return;
         }

         int var4 = var2.readShort();
         var2.consume(var4);
         this.primary_principal = new Principal(var2);

         while(var2.more()) {
            this.credentials.add(new Credential(var2));
         }
      } catch (IOException var5) {
         MudgeSanity.logException("CredCacheParse", var5, false);
      }

   }

   public String toString() {
      return this.primary_principal + "\n" + this.credentials;
   }

   public static void main(String[] var0) {
      CommonUtils.print_good(new Ccache(var0[0]) + "");
   }
}

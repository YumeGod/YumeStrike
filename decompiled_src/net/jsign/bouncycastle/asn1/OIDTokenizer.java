package net.jsign.bouncycastle.asn1;

public class OIDTokenizer {
   private String oid;
   private int index;

   public OIDTokenizer(String var1) {
      this.oid = var1;
      this.index = 0;
   }

   public boolean hasMoreTokens() {
      return this.index != -1;
   }

   public String nextToken() {
      if (this.index == -1) {
         return null;
      } else {
         int var1 = this.oid.indexOf(46, this.index);
         String var2;
         if (var1 == -1) {
            var2 = this.oid.substring(this.index);
            this.index = -1;
            return var2;
         } else {
            var2 = this.oid.substring(this.index, var1);
            this.index = var1 + 1;
            return var2;
         }
      }
   }
}

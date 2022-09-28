package graph;

public class Route {
   private static final long RANGE_MAX = ipToLong("255.255.255.255");
   protected long begin;
   protected long end;
   protected String gateway;
   protected String network;
   protected String mask;

   public static long ipToLong(String var0) {
      if (var0 == null) {
         return 0L;
      } else {
         String[] var1 = var0.split("\\.");
         long var2 = 0L;
         if (var1.length != 4) {
            return 0L;
         } else {
            try {
               var2 += (long)Integer.parseInt(var1[3]);
               var2 += Long.parseLong(var1[2]) << 8;
               var2 += Long.parseLong(var1[1]) << 16;
               var2 += Long.parseLong(var1[0]) << 24;
               return var2;
            } catch (Exception var5) {
               return var2;
            }
         }
      }
   }

   public Route(String var1) {
      String[] var2 = var1.split("/");
      String var3 = "";
      String var4 = "";
      if (var2.length == 1) {
         var3 = var1;
         String[] var5 = var1.split("\\.");
         if (var5[0].equals("0")) {
            var4 = "1";
         } else if (var5[1].equals("0")) {
            var4 = "8";
         } else if (var5[2].equals("0")) {
            var4 = "16";
         } else if (var5[3].equals("0")) {
            var4 = "24";
         } else {
            var4 = "32";
         }
      } else {
         var3 = var2[0];
         var4 = var2[1];
      }

      this.network = var3;
      this.mask = var4;
      this.gateway = "undefined";
      this.begin = ipToLong(var3);

      try {
         this.end = this.begin + (RANGE_MAX >> Integer.parseInt(var4));
      } catch (Exception var6) {
         System.err.println(var4 + " is malformed!");
      }

   }

   public Route(String var1, String var2, String var3) {
      this.begin = ipToLong(var1);
      this.end = this.begin + (RANGE_MAX - ipToLong(var2));
      this.gateway = var3;
      this.network = var1;
      this.mask = var2;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Route)) {
         return false;
      } else {
         Route var2 = (Route)var1;
         return var2.begin == this.begin && var2.end == this.end && var2.gateway.equals(this.gateway);
      }
   }

   public int hashCode() {
      return (int)(this.begin + this.end + (long)this.gateway.hashCode());
   }

   public String getGateway() {
      return this.gateway;
   }

   public boolean shouldRoute(String var1) {
      long var2 = ipToLong(var1);
      return var2 >= this.begin && var2 <= this.end;
   }

   public String toString() {
      return this.network + "/" + this.mask + " via " + this.gateway;
   }
}

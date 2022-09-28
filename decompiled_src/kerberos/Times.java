package kerberos;

import common.DataParser;
import java.io.IOException;

public class Times {
   protected int authtime;
   protected int starttime;
   protected int endtime;
   protected int renew_till;

   public Times(DataParser var1) throws IOException {
      this.authtime = var1.readInt();
      this.starttime = var1.readInt();
      this.endtime = var1.readInt();
      this.renew_till = var1.readInt();
   }

   public String toString() {
      return "Times... meh";
   }
}

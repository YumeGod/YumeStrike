package kerberos;

import common.DataParser;
import java.io.IOException;

public class AuthData {
   protected int authtype;
   protected String authdata;

   public AuthData(DataParser var1) throws IOException {
      this.authtype = var1.readShort();
      this.authdata = var1.readCountedString();
   }

   public String toString() {
      return this.authtype + "/" + this.authdata;
   }
}

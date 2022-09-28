package kerberos;

import common.DataParser;
import java.io.IOException;

public class Address {
   protected int addrtype;
   protected String addrdata;

   public Address(DataParser var1) throws IOException {
      this.addrtype = var1.readShort();
      this.addrdata = var1.readCountedString();
   }

   public String toString() {
      return this.addrtype + "/" + this.addrdata;
   }
}

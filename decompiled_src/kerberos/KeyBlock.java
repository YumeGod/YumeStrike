package kerberos;

import common.CommonUtils;
import common.DataParser;
import java.io.IOException;

public class KeyBlock {
   protected int keytype;
   protected int etype;
   protected int keylen;
   protected byte[] keyvalue;

   public KeyBlock(DataParser var1) throws IOException {
      this.keytype = var1.readShort();
      this.etype = var1.readShort();
      this.keylen = var1.readShort();
      this.keyvalue = var1.readBytes(this.keylen);
   }

   public String toString() {
      return "KeyBlock: " + this.keytype + "/" + this.etype + " " + CommonUtils.toHexString(this.keyvalue);
   }
}

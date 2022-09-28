package beacon.dns;

import common.Packer;
import java.math.BigInteger;

public class RecvConversation {
   protected String id;
   protected String dtype;
   protected long size = -1L;
   protected Packer buffer = new Packer();

   public RecvConversation(String var1, String var2) {
      this.id = var1;
      this.dtype = var2;
   }

   public long next(String var1) {
      if (this.size == -1L) {
         BigInteger var2 = new BigInteger(var1, 16);
         this.size = var2.longValue();
      } else {
         this.buffer.addHex(var1);
      }

      return 0L;
   }

   public boolean isComplete() {
      return this.buffer.size() >= this.size;
   }

   public byte[] result() {
      byte[] var1 = this.buffer.getBytes();
      return var1;
   }

   public String toString() {
      return "[id: " + this.id + ", type: " + this.dtype + ", recv'd: " + this.buffer.size() + ", total: " + this.size + "]";
   }
}

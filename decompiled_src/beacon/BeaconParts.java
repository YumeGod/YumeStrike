package beacon;

import common.CommonUtils;
import common.Packer;
import java.util.HashMap;
import java.util.Map;

public class BeaconParts {
   protected Map parts = new HashMap();

   public void start(String var1, int var2) {
      Part var3 = new Part();
      var3.length = var2;
      if (var2 > 0) {
         synchronized(this) {
            this.parts.put(var1, var3);
         }
      }
   }

   public boolean isReady(String var1) {
      Part var2 = null;
      synchronized(this) {
         var2 = (Part)this.parts.get(var1);
      }

      return var2 != null && var2.buffer.size() >= (long)var2.length;
   }

   public boolean hasPart(String var1) {
      Part var2 = null;
      synchronized(this) {
         var2 = (Part)this.parts.get(var1);
      }

      return var2 != null;
   }

   public void put(String var1, byte[] var2) {
      Part var3 = null;
      synchronized(this) {
         var3 = (Part)this.parts.get(var1);
      }

      if (var3 == null) {
         CommonUtils.print_error("CALLBACK_CHUNK_SEND " + var1 + ": no pending transmission");
      } else {
         var3.buffer.addString(var2, var2.length);
      }
   }

   public byte[] data(String var1) {
      Part var2 = null;
      synchronized(this) {
         var2 = (Part)this.parts.get(var1);
         this.parts.remove(var1);
      }

      return var2 == null ? new byte[0] : var2.buffer.getBytes();
   }

   public static class Part {
      public int length;
      public Packer buffer = new Packer();
   }
}

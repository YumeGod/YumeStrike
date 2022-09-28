package beacon;

import common.CommonUtils;
import common.MudgeSanity;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class BeaconCharsets {
   protected Map charsets_ansi = new HashMap();
   protected Map charsets_oem = new HashMap();

   protected BeaconCharsets() {
   }

   public String process(String var1, byte[] var2) {
      return this.process(this.charsets_ansi, var1, var2);
   }

   public String processOEM(String var1, byte[] var2) {
      return this.process(this.charsets_oem, var1, var2);
   }

   public String process(Map var1, String var2, byte[] var3) {
      Charset var4 = this.get(var1, var2);
      if (var4 == null) {
         return CommonUtils.bString(var3);
      } else {
         try {
            return var4.decode(ByteBuffer.wrap(var3)).toString();
         } catch (Exception var6) {
            MudgeSanity.logException("could not convert text for id " + var2 + " with " + var4, var6, false);
            return CommonUtils.bString(var3);
         }
      }
   }

   public Charset get(Map var1, String var2) {
      synchronized(this) {
         return (Charset)var1.get(var2);
      }
   }

   public void register(String var1, String var2, String var3) {
      this.register(this.charsets_ansi, var1, var2);
      this.register(this.charsets_oem, var1, var3);
   }

   public void register(Map var1, String var2, String var3) {
      if (var3 != null) {
         try {
            Charset var4 = Charset.forName(var3);
            synchronized(this) {
               var1.put(var2, var4);
            }
         } catch (Exception var8) {
            MudgeSanity.logException("Could not find charset '" + var3 + "' for Beacon ID " + var2, var8, false);
         }

      }
   }
}

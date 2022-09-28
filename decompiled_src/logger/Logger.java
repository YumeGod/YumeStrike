package logger;

import common.BeaconEntry;
import common.CommonUtils;
import common.Loggable;
import common.MudgeSanity;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import server.Resources;
import server.ServerUtils;

public class Logger extends ProcessBackend {
   protected Resources r;
   private static final SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyMMdd");

   public Logger(Resources var1) {
      this.r = var1;
      this.start("logger");
   }

   protected File base(String var1) {
      Date var2 = new Date(System.currentTimeMillis());
      File var3 = new File("logs");
      var3 = CommonUtils.SafeFile(var3, fileDateFormat.format(var2));
      if (var1 != null) {
         var3 = CommonUtils.SafeFile(var3, var1);
      }

      if (!var3.exists()) {
         var3.mkdirs();
      }

      return var3;
   }

   protected File beacon(String var1, String var2) {
      File var3 = this.base((String)null);
      BeaconEntry var5 = ServerUtils.getBeacon(this.r, var1);
      File var4;
      if (var5 != null && !"".equals(var5.getInternal())) {
         var4 = CommonUtils.SafeFile(var3, var5.getInternal());
      } else {
         var4 = CommonUtils.SafeFile(var3, "unknown");
      }

      if (var2 != null) {
         var4 = CommonUtils.SafeFile(var4, var2);
      }

      if (!var4.exists()) {
         var4.mkdirs();
      }

      return var4;
   }

   public void process(Object var1) {
      Loggable var2 = (Loggable)var1;
      String var3 = var2.getBeaconId();
      File var4 = null;
      if (var3 != null) {
         var4 = CommonUtils.SafeFile(this.beacon(var3, var2.getLogFolder()), var2.getLogFile());
      } else {
         var4 = CommonUtils.SafeFile(this.base(var2.getLogFolder()), var2.getLogFile());
      }

      try {
         FileOutputStream var5 = new FileOutputStream(var4, true);
         DataOutputStream var6 = new DataOutputStream(var5);
         var2.formatEvent(var6);
         var6.flush();
         var6.close();
      } catch (IOException var7) {
         MudgeSanity.logException("Writing to: " + var4, var7, false);
      }

   }

   static {
      fileDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
   }
}

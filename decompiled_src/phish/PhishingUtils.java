package phish;

import common.CommonUtils;
import common.RegexParser;
import java.util.Iterator;
import java.util.Map;

public class PhishingUtils {
   public static String updateMessage(String var0, Map var1, String var2, String var3) {
      Map.Entry var5;
      for(Iterator var4 = var1.entrySet().iterator(); var4.hasNext(); var0 = CommonUtils.strrep(var0, "%" + var5.getKey() + "%", var5.getValue() + "")) {
         var5 = (Map.Entry)var4.next();
      }

      var0 = CommonUtils.strrep(var0, "%TOKEN%", var3);
      if (!"".equals(var2) && var2.length() > 0) {
         var2 = CommonUtils.strrep(var2, "%TOKEN%", var3);
         var0 = CommonUtils.strrep(var0, "%URL%", var2);
         String var7 = "$1\"" + var2 + "\"";
         String var6 = "(?is:(href=)[\"'].*?[\"'])";
         var0 = var0.replaceAll(var6, var7);
      }

      return var0;
   }

   public static MailServer parseServerString(String var0) {
      MailServer var1 = new MailServer();
      RegexParser var2 = new RegexParser(var0);
      if (var2.matches("(.*?):(.*?)@(.*)")) {
         var1.username = var2.group(1);
         var1.password = var2.group(2);
         var2.whittle(3);
      }

      if (var2.matches("(.*?),(\\d+)")) {
         var1.delay = Integer.parseInt(var2.group(2));
         var2.whittle(1);
      } else {
         var1.delay = 0;
      }

      if (var2.endsWith("-ssl")) {
         var1.ssl = true;
      } else {
         var1.ssl = false;
      }

      if (var2.endsWith("-starttls")) {
         var1.starttls = true;
      } else {
         var1.starttls = false;
      }

      if (var2.matches("(.*?):(.*)")) {
         var1.lhost = var2.group(1);
         var1.lport = Integer.parseInt(var2.group(2));
      } else {
         var1.lhost = var2.getText();
         var1.lport = 25;
      }

      return var1;
   }
}

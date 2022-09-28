package common;

import dialog.DialogUtils;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Keys {
   public static final Set models = CommonUtils.toSet("applications, c2info, credentials, services, sessions, targets, tokens");

   public static String[] getCols(String var0) {
      if ("applications".equals(var0)) {
         return CommonUtils.toArray("external, internal, application, version, date, id");
      } else if ("c2info".equals(var0)) {
         return CommonUtils.toArray("bid, port, proto, domains");
      } else if ("credentials".equals(var0)) {
         return CommonUtils.toArray("user, password, realm, source, host, note");
      } else if ("services".equals(var0)) {
         return CommonUtils.toArray("address, port, banner, note");
      } else if ("sessions".equals(var0)) {
         return CommonUtils.toArray("id, opened, external, internal, user, computer, pid, is64, pbid, note");
      } else if ("targets".equals(var0)) {
         return CommonUtils.toArray("address, name, os, version, note");
      } else {
         return "tokens".equals(var0) ? CommonUtils.toArray("token, email, cid") : new String[0];
      }
   }

   public static int size() {
      return models.size();
   }

   public static boolean isDataModel(String var0) {
      return models.contains(var0);
   }

   public static Iterator getDataModelIterator() {
      return models.iterator();
   }

   public static String C2InfoKey(Map var0) {
      return DialogUtils.string(var0, "bid");
   }

   public static String ToKey(String var0, Map var1) {
      if ("applications".equals(var0)) {
         return CommonUtils.ApplicationKey(var1);
      } else if ("credentials".equals(var0)) {
         return CommonUtils.CredKey(var1);
      } else if ("services".equals(var0)) {
         return CommonUtils.ServiceKey(var1);
      } else if ("targets".equals(var0)) {
         return CommonUtils.TargetKey(var1);
      } else if ("listeners".equals(var0)) {
         return DialogUtils.string(var1, "name");
      } else if ("beacons".equals(var0)) {
         return DialogUtils.string(var1, "id");
      } else {
         CommonUtils.print_error("No key for '" + var0 + "'");
         return "";
      }
   }
}

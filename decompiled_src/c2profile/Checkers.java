package c2profile;

import common.CommonUtils;
import java.util.Set;

public class Checkers {
   public static final boolean isComment(String var0) {
      return var0.charAt(0) == '#' && var0.charAt(var0.length() - 1) == '\n';
   }

   public static final boolean isBlock(String var0) {
      return var0.charAt(0) == '{' && var0.charAt(var0.length() - 1) == '}';
   }

   public static final boolean isString(String var0) {
      return var0.charAt(0) == '"' && var0.charAt(var0.length() - 1) == '"';
   }

   public static final boolean isBoolean(String var0) {
      return var0.equals("true") || var0.equals("false");
   }

   public static final boolean isStatement(String var0, String var1) {
      return var1.equals("EOT");
   }

   public static final boolean isSetStatement(String var0, String var1, String var2, String var3) {
      return var0.equals("set") && isStatementArg(var1, var2, var3);
   }

   public static final boolean isIndicator(String var0, String var1, String var2, String var3) {
      return (var0.equals("header") || var0.equals("parameter") || var0.equals("strrep")) && isString(var1) && isString(var2) && isStatement(var2, var3);
   }

   public static final boolean isStatementArg(String var0, String var1, String var2) {
      return isString(var1) && isStatement(var0, var2);
   }

   public static final boolean isStatementArgBlock(String var0, String var1, String var2) {
      return isString(var1) && isBlock(var2);
   }

   public static final boolean isStatementBlock(String var0, String var1) {
      return isBlock(var1);
   }

   public static final boolean isDate(String var0) {
      return CommonUtils.isDate(var0, "dd MMM yyyy HH:mm:ss");
   }

   public static final boolean isNumber(String var0) {
      try {
         Integer.parseInt(var0);
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   public static final boolean isHTTPVerb(String var0) {
      Set var1 = CommonUtils.toSet("GET, POST");
      return var1.contains(var0);
   }

   public static final boolean isAllocator(String var0) {
      Set var1 = CommonUtils.toSet("VirtualAllocEx, NtMapViewOfSection");
      return var1.contains(var0);
   }
}

package org.apache.regexp;

public class REUtil {
   private static final String complexPrefix = "complex:";

   public static RE createRE(String var0) throws RESyntaxException {
      return createRE(var0, 0);
   }

   public static RE createRE(String var0, int var1) throws RESyntaxException {
      return var0.startsWith("complex:") ? new RE(var0.substring("complex:".length()), var1) : new RE(RE.simplePatternToFullRegularExpression(var0), var1);
   }
}

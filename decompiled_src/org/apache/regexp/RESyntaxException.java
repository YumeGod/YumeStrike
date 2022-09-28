package org.apache.regexp;

public class RESyntaxException extends Exception {
   public RESyntaxException(String var1) {
      super("Syntax error: " + var1);
   }
}

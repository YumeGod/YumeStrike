package org.w3c.css.sac;

public class CSSException extends RuntimeException {
   protected String s;
   public static final short SAC_UNSPECIFIED_ERR = 0;
   public static final short SAC_NOT_SUPPORTED_ERR = 1;
   public static final short SAC_SYNTAX_ERR = 2;
   protected static final String S_SAC_UNSPECIFIED_ERR = "unknown error";
   protected static final String S_SAC_NOT_SUPPORTED_ERR = "not supported";
   protected static final String S_SAC_SYNTAX_ERR = "syntax error";
   protected Exception e;
   protected short code;

   public CSSException() {
   }

   public CSSException(String var1) {
      this.code = 0;
      this.s = var1;
   }

   public CSSException(Exception var1) {
      this.code = 0;
      this.e = var1;
   }

   public CSSException(short var1) {
      this.code = var1;
   }

   public CSSException(short var1, String var2, Exception var3) {
      this.code = var1;
      this.s = var2;
      this.e = var3;
   }

   public String getMessage() {
      if (this.s != null) {
         return this.s;
      } else if (this.e != null) {
         return this.e.getMessage();
      } else {
         switch (this.code) {
            case 0:
               return "unknown error";
            case 1:
               return "not supported";
            case 2:
               return "syntax error";
            default:
               return null;
         }
      }
   }

   public short getCode() {
      return this.code;
   }

   public Exception getException() {
      return this.e;
   }
}

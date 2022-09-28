package org.apache.batik.css.parser;

public class ParseException extends RuntimeException {
   protected Exception exception;
   protected int lineNumber;
   protected int columnNumber;

   public ParseException(String var1, int var2, int var3) {
      super(var1);
      this.exception = null;
      this.lineNumber = var2;
      this.columnNumber = var3;
   }

   public ParseException(Exception var1) {
      this.exception = var1;
      this.lineNumber = -1;
      this.columnNumber = -1;
   }

   public ParseException(String var1, Exception var2) {
      super(var1);
      this.exception = var2;
   }

   public String getMessage() {
      String var1 = super.getMessage();
      return var1 == null && this.exception != null ? this.exception.getMessage() : var1;
   }

   public Exception getException() {
      return this.exception;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public int getColumnNumber() {
      return this.columnNumber;
   }
}

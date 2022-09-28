package org.apache.batik.script;

public class InterpreterException extends RuntimeException {
   private int line;
   private int column;
   private Exception embedded;

   public InterpreterException(String var1, int var2, int var3) {
      super(var1);
      this.line = -1;
      this.column = -1;
      this.embedded = null;
      this.line = var2;
      this.column = var3;
   }

   public InterpreterException(Exception var1, String var2, int var3, int var4) {
      this(var2, var3, var4);
      this.embedded = var1;
   }

   public int getLineNumber() {
      return this.line;
   }

   public int getColumnNumber() {
      return this.column;
   }

   public Exception getException() {
      return this.embedded;
   }

   public String getMessage() {
      String var1 = super.getMessage();
      if (var1 != null) {
         return var1;
      } else {
         return this.embedded != null ? this.embedded.getMessage() : null;
      }
   }
}

package org.w3c.css.sac;

public class CSSParseException extends CSSException {
   private String uri;
   private int lineNumber;
   private int columnNumber;

   public CSSParseException(String var1, Locator var2) {
      super(var1);
      super.code = 2;
      this.uri = var2.getURI();
      this.lineNumber = var2.getLineNumber();
      this.columnNumber = var2.getColumnNumber();
   }

   public CSSParseException(String var1, Locator var2, Exception var3) {
      super((short)2, var1, var3);
      this.uri = var2.getURI();
      this.lineNumber = var2.getLineNumber();
      this.columnNumber = var2.getColumnNumber();
   }

   public CSSParseException(String var1, String var2, int var3, int var4) {
      super(var1);
      super.code = 2;
      this.uri = var2;
      this.lineNumber = var3;
      this.columnNumber = var4;
   }

   public CSSParseException(String var1, String var2, int var3, int var4, Exception var5) {
      super((short)2, var1, var5);
      this.uri = var2;
      this.lineNumber = var3;
      this.columnNumber = var4;
   }

   public String getURI() {
      return this.uri;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public int getColumnNumber() {
      return this.columnNumber;
   }
}

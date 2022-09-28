package sleep.error;

public class SyntaxError {
   protected String description;
   protected String code;
   protected String marker;
   protected int lineNo;

   public SyntaxError(String var1, String var2, int var3) {
      this(var1, var2, var3, (String)null);
   }

   public SyntaxError(String var1, String var2, int var3, String var4) {
      this.description = var1;
      this.code = var2;
      this.lineNo = var3;
      this.marker = var4;
   }

   public String getMarker() {
      return this.marker;
   }

   public String getDescription() {
      return this.description;
   }

   public String getCodeSnippet() {
      return this.code;
   }

   public int getLineNumber() {
      return this.lineNo;
   }
}

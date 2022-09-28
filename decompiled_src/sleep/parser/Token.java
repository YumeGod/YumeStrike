package sleep.parser;

public class Token {
   protected String term;
   protected int hint;
   protected int marker;
   protected int tophint;

   public Token(String var1, int var2) {
      this(var1, var2, -1);
   }

   public Token(String var1, int var2, int var3) {
      this.term = var1;
      this.hint = var2;
      this.marker = var3;
      this.tophint = -1;
   }

   public String toString() {
      return this.term;
   }

   public int getMarkerIndex() {
      return this.marker;
   }

   public Token copy(int var1) {
      return new Token(this.term, var1);
   }

   public Token copy(String var1) {
      return new Token(var1, this.getHint());
   }

   public String getMarker() {
      if (this.marker <= -1) {
         return null;
      } else {
         StringBuffer var1 = new StringBuffer();

         for(int var2 = 0; var2 < this.marker - 1; ++var2) {
            var1.append(" ");
         }

         var1.append("^");
         return var1.toString();
      }
   }

   public int getTopHint() {
      if (this.tophint >= 0) {
         return this.tophint;
      } else {
         this.tophint = this.hint;

         for(int var1 = -1; (var1 = this.term.indexOf(10, var1 + 1)) > -1; ++this.tophint) {
         }

         return this.tophint;
      }
   }

   public int getHint() {
      return this.hint;
   }
}

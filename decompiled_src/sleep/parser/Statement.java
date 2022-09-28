package sleep.parser;

public class Statement extends TokenList {
   protected int type;

   public int getType() {
      return this.type;
   }

   public void setType(int var1) {
      this.type = var1;
   }

   public String toString() {
      return "[" + this.type + "] " + super.toString();
   }
}

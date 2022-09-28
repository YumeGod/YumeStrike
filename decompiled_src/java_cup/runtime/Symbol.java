package java_cup.runtime;

public class Symbol {
   public int sym;
   public int parse_state;
   boolean used_by_parser;
   public int left;
   public int right;
   public Object value;

   public Symbol(int var1) {
      this(var1, -1);
      this.left = -1;
      this.right = -1;
      this.value = null;
   }

   Symbol(int var1, int var2) {
      this.used_by_parser = false;
      this.sym = var1;
      this.parse_state = var2;
   }

   public Symbol(int var1, int var2, int var3) {
      this(var1, var2, var3, (Object)null);
   }

   public Symbol(int var1, int var2, int var3, Object var4) {
      this(var1);
      this.left = var2;
      this.right = var3;
      this.value = var4;
   }

   public Symbol(int var1, Object var2) {
      this(var1, -1, -1, var2);
   }

   public String toString() {
      return "#" + this.sym;
   }
}

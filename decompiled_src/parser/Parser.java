package parser;

import common.MudgeSanity;
import server.Resources;

public abstract class Parser {
   protected Resources resources = null;

   public Parser(Resources var1) {
      this.resources = var1;
   }

   public abstract boolean check(String var1, int var2);

   public abstract void parse(String var1, String var2) throws Exception;

   public boolean isOutput(int var1) {
      return var1 == 0 || var1 == 30 || var1 == 32;
   }

   public void process(String var1, String var2, int var3) {
      try {
         if (this.check(var1, var3)) {
            this.parse(var1, var2);
         }
      } catch (Exception var5) {
         MudgeSanity.logException("output parser", var5, false);
      }

   }
}

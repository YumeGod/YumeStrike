package sleep.parser;

import java.util.Iterator;
import java.util.LinkedList;

public class TokenList {
   protected LinkedList terms = new LinkedList();
   protected String[] sarray = null;
   protected Token[] tarray = null;
   private static final Token[] dummyT = new Token[0];
   private static final String[] dummyS = new String[0];

   public void add(Token var1) {
      this.terms.add(var1);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.terms.iterator();

      while(var2.hasNext()) {
         var1.append(var2.next().toString());
         var1.append(" ");
      }

      return var1.toString();
   }

   public LinkedList getList() {
      return this.terms;
   }

   public Token[] getTokens() {
      if (this.tarray == null) {
         this.tarray = (Token[])((Token[])this.terms.toArray(dummyT));
      }

      return this.tarray;
   }

   public String[] getStrings() {
      if (this.sarray == null) {
         Token[] var1 = this.getTokens();
         this.sarray = new String[var1.length];

         for(int var2 = 0; var2 < var1.length; ++var2) {
            this.sarray[var2] = var1[var2].toString();
         }
      }

      return this.sarray;
   }
}

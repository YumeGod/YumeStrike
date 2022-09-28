package sleep.parser;

import java.util.LinkedList;
import sleep.error.SyntaxError;

public class Rule {
   public static int PRESERVE_ENTITY = 100;
   public static int PRESERVE_SINGLE = 101;
   int type;
   char left;
   char right;
   char single;
   String errorDescription1;
   String errorDescription2;
   protected LinkedList open = new LinkedList();
   protected LinkedList close = new LinkedList();

   public int getType() {
      return this.type;
   }

   public String toString() {
      return this.errorDescription1;
   }

   public SyntaxError getSyntaxError() {
      while(this.open.size() > 0 && this.close.size() > 0) {
         this.open.removeLast();
         this.close.removeLast();
      }

      String var1;
      if (this.type == PRESERVE_ENTITY && this.open.size() > 0) {
         var1 = this.errorDescription2;
      } else {
         var1 = this.errorDescription1;
      }

      Token var2;
      if (this.open.size() > 0) {
         var2 = (Token)this.open.getFirst();
      } else {
         var2 = (Token)this.close.getFirst();
      }

      this.open.clear();
      this.close.clear();
      return new SyntaxError(var1, var2.toString(), var2.getHint(), var2.getMarker());
   }

   public String wrap(String var1) {
      StringBuffer var2 = new StringBuffer(var1.length() + 2);
      if (this.type == PRESERVE_ENTITY) {
         var2.append(this.left);
         var2.append(var1);
         var2.append(this.right);
      } else {
         var2.append(this.single);
         var2.append(var1);
         var2.append(this.single);
      }

      return var2.toString();
   }

   public boolean isLeft(char var1) {
      return this.type == PRESERVE_ENTITY && this.left == var1;
   }

   public boolean isRight(char var1) {
      return this.type == PRESERVE_ENTITY && this.right == var1;
   }

   public boolean isMatch(char var1) {
      return this.type == PRESERVE_SINGLE && this.single == var1;
   }

   public boolean isBalanced() {
      if (this.open.size() == this.close.size()) {
         this.open.clear();
         this.close.clear();
         return true;
      } else {
         return false;
      }
   }

   public void witnessOpen(Token var1) {
      this.open.add(var1);
      this.adjustLists();
   }

   public void witnessClose(Token var1) {
      if (this.type == PRESERVE_ENTITY) {
         this.close.addFirst(var1);
      } else {
         this.close.add(var1);
      }

      this.adjustLists();
   }

   private void adjustLists() {
      if (this.open.size() > 0 && this.close.size() > 0 && ((Token)this.open.getLast()).getHint() == ((Token)this.close.getLast()).getHint()) {
         this.open.removeLast();
         this.close.removeLast();
      }

   }

   public char getLeft() {
      return this.left;
   }

   public char getRight() {
      return this.right;
   }

   public Rule copyRule() {
      return this.type == PRESERVE_ENTITY ? new Rule(this.errorDescription1, this.errorDescription2, this.left, this.right) : new Rule(this.errorDescription1, this.single);
   }

   public Rule(String var1, String var2, char var3, char var4) {
      this.type = PRESERVE_ENTITY;
      this.left = var3;
      this.right = var4;
      this.errorDescription1 = var1;
      this.errorDescription2 = var2;
   }

   public Rule(String var1, char var2) {
      this.type = PRESERVE_SINGLE;
      this.single = var2;
      this.errorDescription1 = var1;
   }

   public Rule() {
   }
}

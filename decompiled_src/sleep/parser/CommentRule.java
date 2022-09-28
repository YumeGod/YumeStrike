package sleep.parser;

public class CommentRule extends Rule {
   public int getType() {
      return PRESERVE_SINGLE;
   }

   public String toString() {
      return "Comment parsing information";
   }

   public String wrap(String var1) {
      StringBuffer var2 = new StringBuffer(var1.length() + 2);
      var2.append('#');
      var2.append(var1);
      var2.append('\n');
      return var2.toString();
   }

   public boolean isLeft(char var1) {
      return var1 == '#';
   }

   public boolean isRight(char var1) {
      return var1 == '\n';
   }

   public boolean isMatch(char var1) {
      return false;
   }

   public boolean isBalanced() {
      return true;
   }

   public Rule copyRule() {
      return this;
   }

   public void witnessOpen(Token var1) {
   }

   public void witnessClose(Token var1) {
   }
}

package common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexParser {
   protected String text;
   protected Matcher last = null;

   public RegexParser(String var1) {
      this.text = var1;
   }

   public static boolean isMatch(String var0, String var1) {
      Pattern var2 = Pattern.compile(var1);
      Matcher var3 = var2.matcher(var0);
      return var3.matches();
   }

   public boolean matches(String var1) {
      Pattern var2 = Pattern.compile(var1);
      Matcher var3 = var2.matcher(this.text);
      this.last = var3;
      return var3.matches();
   }

   public boolean endsWith(String var1) {
      if (this.text.endsWith(var1)) {
         this.text = this.text.substring(0, this.text.length() - var1.length());
         return true;
      } else {
         return false;
      }
   }

   public String group(int var1) {
      return this.last.group(var1);
   }

   public void whittle(int var1) {
      this.text = this.last.group(var1);
   }

   public String getText() {
      return this.text;
   }
}

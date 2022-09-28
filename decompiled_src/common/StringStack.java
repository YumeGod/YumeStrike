package common;

import java.util.LinkedList;
import java.util.List;

public class StringStack {
   protected String string;
   protected String delimeter;

   public StringStack(String var1) {
      this(var1, " ");
   }

   public StringStack(String var1, String var2) {
      this.string = var1;
      this.delimeter = var2;
   }

   public List toList() {
      LinkedList var1 = new LinkedList();
      StringStack var2 = new StringStack(this.string, this.delimeter);

      while(!var2.isEmpty()) {
         var1.add(var2.shift());
      }

      return var1;
   }

   public void push(String var1) {
      if (this.string.length() > 0) {
         this.string = this.string + this.delimeter + var1;
      } else {
         this.string = var1;
      }

   }

   public int length() {
      return this.string.length();
   }

   public boolean isEmpty() {
      return this.string.length() == 0;
   }

   public String peekFirst() {
      if (this.string.indexOf(this.delimeter) > -1) {
         String var1 = this.string.substring(0, this.string.indexOf(this.delimeter));
         return var1;
      } else {
         return this.string;
      }
   }

   public String shift() {
      String var1;
      if (this.string.indexOf(this.delimeter) > -1) {
         var1 = this.string.substring(0, this.string.indexOf(this.delimeter));
         if (var1.length() >= this.string.length()) {
            this.string = "";
            return var1;
         } else {
            this.string = this.string.substring(var1.length() + 1, this.string.length());
            return var1;
         }
      } else {
         var1 = this.string;
         this.string = "";
         return var1;
      }
   }

   public String pop() {
      int var1 = this.string.lastIndexOf(this.delimeter);
      String var2;
      if (var1 > -1) {
         var2 = this.string.substring(var1 + 1, this.string.length());
         this.string = this.string.substring(0, var1);
         return var2;
      } else {
         var2 = this.string;
         this.string = "";
         return var2;
      }
   }

   public String toString() {
      return this.string;
   }

   public void setDelimeter(String var1) {
      this.delimeter = var1;
   }
}

package com.xmlmind.fo.converter.rtf;

import java.io.PrintWriter;
import java.util.Random;
import java.util.Vector;

public final class ListTable {
   private Vector lists = new Vector();
   private Random random = new Random(123456789L);

   public int add(List var1) {
      var1.id = (int)Math.rint(this.random.nextDouble() * 1.0E9);
      this.lists.addElement(var1);
      return this.lists.size();
   }

   public void print(PrintWriter var1) {
      if (this.lists.size() != 0) {
         var1.println("{\\*\\listtable");
         int var2 = 0;

         int var3;
         List var4;
         for(var3 = this.lists.size(); var2 < var3; ++var2) {
            var4 = (List)this.lists.elementAt(var2);
            var4.print(var1);
         }

         var1.println("}");
         var1.println("{\\*\\listoverridetable");
         var2 = 0;

         for(var3 = this.lists.size(); var2 < var3; ++var2) {
            var4 = (List)this.lists.elementAt(var2);
            var1.print("{\\listoverride");
            var1.print("\\listid" + var4.id);
            var1.print("\\listoverridecount0");
            var1.print("\\ls" + (var2 + 1));
            var1.println("}");
         }

         var1.println("}");
      }
   }

   public static class List implements Cloneable {
      public static final int STYLE_ARABIC = 0;
      public static final int STYLE_UPPERCASE_ROMAN = 1;
      public static final int STYLE_LOWERCASE_ROMAN = 2;
      public static final int STYLE_UPPERCASE_LETTER = 3;
      public static final int STYLE_LOWERCASE_LETTER = 4;
      public static final int STYLE_BULLET = 23;
      public static final int ALIGNMENT_LEFT = 0;
      public static final int ALIGNMENT_CENTER = 1;
      public static final int ALIGNMENT_RIGHT = 2;
      public int style;
      public int start;
      public int alignment;
      public String textBefore;
      public String textAfter;
      public RunProperties properties;
      private int id;

      public List() {
         this(0);
      }

      public List(int var1) {
         this.style = var1;
         this.start = 1;
         this.alignment = 0;
      }

      public List(int var1, int var2, int var3, String var4, String var5, RunProperties var6) {
         this.style = var1;
         this.start = var2;
         this.alignment = var3;
         this.textBefore = var4;
         this.textAfter = var5;
         this.properties = var6;
      }

      public char getBullet() {
         return this.style == 23 && this.textAfter != null ? this.textAfter.charAt(0) : '\u0000';
      }

      public void setBullet(char var1) {
         if (this.style == 23) {
            this.textAfter = new String(new char[]{var1});
         }

      }

      public void print(PrintWriter var1) {
         var1.println("{\\list\\listid" + this.id + "\\listsimple");
         var1.print("{\\listlevel");
         var1.print("\\levelnfc" + this.style);
         var1.print("\\leveljc" + this.alignment);
         var1.print("\\levelstartat" + this.start);
         var1.println("\\levelfollow0");
         var1.println("{\\leveltext" + this.levelText() + ";}");
         var1.println("{\\levelnumbers" + this.levelNumbers() + ";}");
         if (this.properties != null) {
            this.properties.print(var1);
            var1.println();
         }

         var1.println("}");
         var1.println("}");
      }

      private String levelText() {
         int var1 = 1;
         StringBuffer var2 = new StringBuffer();
         if (this.textBefore != null) {
            var1 += this.textBefore.length();
         }

         if (this.textAfter != null) {
            var1 += this.textAfter.length();
         }

         var2.append(hexString(var1));
         if (this.textBefore != null) {
            var2.append(this.textBefore);
         }

         var2.append("\\'00");
         if (this.textAfter != null) {
            var2.append(this.textAfter);
         }

         return var2.toString();
      }

      private String levelNumbers() {
         int var1 = 1;
         if (this.textBefore != null) {
            var1 += this.textBefore.length();
         }

         return hexString(var1);
      }

      private static String hexString(int var0) {
         return "\\'" + Integer.toHexString(var0 >> 4 & 15) + Integer.toHexString(var0 & 15);
      }

      public List copy() {
         try {
            return (List)this.clone();
         } catch (CloneNotSupportedException var2) {
            return null;
         }
      }
   }
}

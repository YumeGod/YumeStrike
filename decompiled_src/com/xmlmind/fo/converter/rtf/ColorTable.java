package com.xmlmind.fo.converter.rtf;

import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

public final class ColorTable {
   private Vector colors = new Vector();
   private Hashtable indexes = new Hashtable();

   public int add(Color var1) {
      this.colors.addElement(var1);
      int var2 = this.colors.size();
      Integer var3 = new Integer(var2);
      this.indexes.put(this.key(var1.red, var1.green, var1.blue), var3);
      if (var1.name != null) {
         this.indexes.put(var1.name, var3);
      }

      return var2;
   }

   private String key(int var1, int var2, int var3) {
      String var4 = Integer.toString(var1) + "," + Integer.toString(var2) + "," + Integer.toString(var3);
      return var4;
   }

   public void add(Color[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.add(var1[var2]);
      }

   }

   public int add(int var1, int var2, int var3) {
      return this.add(new Color(var1, var2, var3));
   }

   public void print(PrintWriter var1) {
      var1.println("{\\colortbl");
      var1.println(";");
      int var2 = 0;

      for(int var3 = this.colors.size(); var2 < var3; ++var2) {
         Color var4 = (Color)this.colors.elementAt(var2);
         var1.println("\\red" + var4.red + "\\green" + var4.green + "\\blue" + var4.blue + ";");
      }

      var1.println("}");
   }

   public int index(int var1, int var2, int var3) {
      Integer var4 = (Integer)this.indexes.get(this.key(var1, var2, var3));
      return var4 != null ? var4 : -1;
   }

   public int index(String var1) {
      Integer var2 = (Integer)this.indexes.get(var1);
      return var2 != null ? var2 : -1;
   }

   public Color color(int var1) {
      return var1 > 0 && var1 <= this.colors.size() ? (Color)this.colors.elementAt(var1 - 1) : null;
   }

   public static class Color {
      public String name;
      public int red;
      public int green;
      public int blue;

      public Color(String var1, int var2, int var3, int var4) {
         this.name = var1;
         this.red = var2;
         this.green = var3;
         this.blue = var4;
      }

      public Color(int var1, int var2, int var3) {
         this((String)null, var1, var2, var3);
      }
   }
}

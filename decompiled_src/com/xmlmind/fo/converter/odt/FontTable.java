package com.xmlmind.fo.converter.odt;

import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

public final class FontTable {
   private String charset;
   private Hashtable fonts;
   private Vector fontList;
   private Hashtable familyAliases;

   public FontTable() {
      this((String)null);
   }

   public FontTable(String var1) {
      this.fonts = new Hashtable();
      this.fontList = new Vector();
      this.familyAliases = new Hashtable();
      this.charset = var1;
   }

   public void aliasFontFamily(String var1, String var2) {
      this.familyAliases.put(var1, var2);
   }

   public String unaliasFontFamily(String var1) {
      String var2 = (String)this.familyAliases.get(var1);
      if (var2 == null) {
         var2 = var1;
      }

      return var2;
   }

   public FontFace add(String var1) {
      return this.add(var1, (String)null);
   }

   public FontFace add(String var1, String var2) {
      return this.add(var1, var2, this.charset);
   }

   public FontFace add(String var1, String var2, String var3) {
      return this.add(new FontFace(this.unaliasFontFamily(var1), var2, var3));
   }

   public FontFace add(FontFace var1) {
      FontFace var2 = (FontFace)this.fonts.get(var1);
      if (var2 == null) {
         var1.id = "F" + this.fonts.size();
         this.fonts.put(var1, var1);
         this.fontList.addElement(var1);
      } else {
         var1 = var2;
      }

      return var1;
   }

   public void print(PrintWriter var1) {
      var1.println("<office:font-face-decls>");
      int var2 = 0;

      for(int var3 = this.fontList.size(); var2 < var3; ++var2) {
         FontFace var4 = (FontFace)this.fontList.elementAt(var2);
         var4.print(var1);
      }

      var1.println("</office:font-face-decls>");
   }
}

package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.util.Encoding;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Vector;

public final class FontTable {
   public static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.fontTable+xml";
   private int charset;
   private Hashtable fonts;
   private Vector fontList;
   private Hashtable familyAliases;

   public FontTable() {
      this(-1);
   }

   public FontTable(int var1) {
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

   public FontFace add(String var1, String var2, int var3) {
      return this.add(new FontFace(this.unaliasFontFamily(var1), var2, var3));
   }

   public FontFace add(FontFace var1) {
      FontFace var2 = (FontFace)this.fonts.get(var1);
      if (var2 == null) {
         this.fonts.put(var1, var1);
         this.fontList.addElement(var1);
      } else {
         var1 = var2;
      }

      return var1;
   }

   public byte[] getBytes(String var1) throws Exception {
      StringWriter var2 = new StringWriter();
      PrintWriter var3 = new PrintWriter(var2);
      var3.println("<?xml version=\"1.0\" encoding=\"" + Encoding.officialName(var1) + "\"?>");
      this.print(var3);
      var3.flush();
      return var2.toString().getBytes(var1);
   }

   private void print(PrintWriter var1) {
      var1.println("<w:fonts xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">");
      int var2 = 0;

      for(int var3 = this.fontList.size(); var2 < var3; ++var2) {
         FontFace var4 = (FontFace)this.fontList.elementAt(var2);
         if (var4.family != null) {
            var4.print(var1);
         }
      }

      var1.println("</w:fonts>");
   }
}

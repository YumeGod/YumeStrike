package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.util.Encoding;
import com.xmlmind.fo.zip.ZipFile;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;

public final class Footnotes {
   private Vector footnotes = new Vector();
   private Relationships relationships = new Relationships();

   public void add(Footnote var1) {
      var1.id = this.footnotes.size() + 1;
      this.footnotes.addElement(var1);
   }

   public Relationships relationships() {
      return this.relationships;
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
      var1.println("<w:footnotes xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" xmlns:p=\"http://schemas.openxmlformats.org/drawingml/2006/picture\" xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" xmlns:v=\"urn:schemas-microsoft-com:vml\">");
      int var2 = 0;

      for(int var3 = this.footnotes.size(); var2 < var3; ++var2) {
         Footnote var4 = (Footnote)this.footnotes.elementAt(var2);
         var4.print(var1);
      }

      var1.println("</w:footnotes>");
   }

   public void store(ZipFile var1, String var2, String var3) throws Exception {
      var1.add(var2, this.getBytes(var3));
      if (this.relationships.count() > 0) {
         var1.add("_rels/" + var2 + ".rels", this.relationships.getBytes(var3));
      }

   }
}

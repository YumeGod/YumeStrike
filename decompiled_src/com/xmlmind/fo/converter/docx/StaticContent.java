package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.util.Encoding;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;

public final class StaticContent {
   public static final int TYPE_HEADER = 0;
   public static final int TYPE_FOOTER = 1;
   private int type;
   private Vector content = new Vector();
   private Relationships relationships = new Relationships();

   public StaticContent(int var1) {
      this.type = var1;
   }

   public void add(Paragraph var1) {
      this.add(1, var1);
   }

   public void add(Table var1) {
      this.add(2, var1);
   }

   public void add(TableAndCaption var1) {
      this.add(3, var1);
   }

   public void add(List var1) {
      this.add(4, var1);
   }

   private void add(int var1, Object var2) {
      this.content.addElement(new Element(var1, var2));
   }

   public Relationships relationships() {
      return this.relationships;
   }

   public void write(String var1, String var2) throws Exception {
      FileOutputStream var3 = new FileOutputStream(var1);
      var3.write(this.getBytes(var2));
      var3.flush();
      var3.close();
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
      var1.println("<w:" + this.type() + " xmlns:w=\"" + "http://schemas.openxmlformats.org/wordprocessingml/2006/main" + "\"" + " xmlns:r=\"" + "http://schemas.openxmlformats.org/officeDocument/2006/relationships" + "\"" + " xmlns:a=\"" + "http://schemas.openxmlformats.org/drawingml/2006/main" + "\"" + " xmlns:p=\"" + "http://schemas.openxmlformats.org/drawingml/2006/picture" + "\"" + " xmlns:wp=\"" + "http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing" + "\"" + " xmlns:v=\"" + "urn:schemas-microsoft-com:vml" + "\"" + ">");
      int var2 = 0;

      for(int var3 = this.content.size(); var2 < var3; ++var2) {
         Element var4 = (Element)this.content.elementAt(var2);
         switch (var4.type) {
            case 1:
               Paragraph var5 = (Paragraph)var4.object;
               var5.print(var1);
               break;
            case 2:
               Table var6 = (Table)var4.object;
               var6.print(var1);
               break;
            case 3:
               TableAndCaption var7 = (TableAndCaption)var4.object;
               var7.print(var1);
               break;
            case 4:
               List var8 = (List)var4.object;
               var8.print(var1);
         }
      }

      var1.println("</w:" + this.type() + ">");
   }

   private String type() {
      String var1;
      switch (this.type) {
         case 0:
         default:
            var1 = "hdr";
            break;
         case 1:
            var1 = "ftr";
      }

      return var1;
   }

   private class Element {
      static final int TYPE_PARAGRAPH = 1;
      static final int TYPE_TABLE = 2;
      static final int TYPE_TABLE_AND_CAPTION = 3;
      static final int TYPE_LIST = 4;
      int type;
      Object object;

      Element(int var2, Object var3) {
         this.type = var2;
         this.object = var3;
      }
   }
}

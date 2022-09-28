package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.util.Encoding;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;

public final class Relationships {
   public static final String TYPE_BASE_URI = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/";
   public static final String TYPE_CUSTOMXML = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/customXml";
   public static final String TYPE_CUSTOMXML_PROPERTIES = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/customXmlProps";
   public static final String TYPE_DOCUMENT_SETTINGS = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings";
   public static final String TYPE_FONT_TABLE = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/fontTable";
   public static final String TYPE_FOOTER = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/footer";
   public static final String TYPE_FOOTNOTES = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/footnotes";
   public static final String TYPE_HEADER = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/header";
   public static final String TYPE_HYPERLINK = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink";
   public static final String TYPE_IMAGE = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image";
   public static final String TYPE_MAIN_DOCUMENT = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument";
   public static final String TYPE_NUMBERING_DEFINITIONS = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/numbering";
   public static final int MODE_INTERNAL = 0;
   public static final int MODE_EXTERNAL = 1;
   private static final String NAMESPACE_URI = "http://schemas.openxmlformats.org/package/2006/relationships";
   private Vector relationships = new Vector();

   public String add(String var1, String var2) {
      return this.add(var1, var2, 0);
   }

   public String add(String var1, String var2, int var3) {
      String var4 = "r" + (this.relationships.size() + 1);
      this.relationships.addElement(new Relationship(var4, var1, var2, var3));
      return var4;
   }

   public int count() {
      return this.relationships.size();
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
      var1.println("<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">");
      int var2 = 0;

      for(int var3 = this.relationships.size(); var2 < var3; ++var2) {
         Relationship var4 = (Relationship)this.relationships.elementAt(var2);
         var1.println("  <Relationship Id=\"" + var4.id + "\"");
         var1.println("    Type=\"" + var4.type + "\"");
         if (var4.mode == 1) {
            var1.println("    TargetMode=\"External\"");
         }

         var1.println("    Target=\"" + var4.target + "\"/>");
      }

      var1.println("</Relationships>");
   }

   private class Relationship {
      String id;
      String type;
      String target;
      int mode;

      Relationship(String var2, String var3, String var4, int var5) {
         this.id = var2;
         this.type = var3;
         this.target = var4;
         this.mode = var5;
      }
   }
}

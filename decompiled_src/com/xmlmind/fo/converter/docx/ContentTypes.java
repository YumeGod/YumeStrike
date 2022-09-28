package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.util.Encoding;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;

public final class ContentTypes {
   public static final String TYPE_OFFICE_DOCUMENT = "application/vnd.openxmlformats-officedocument";
   public static final String TYPE_WORDPROCESSINGML = "application/vnd.openxmlformats-officedocument.wordprocessingml";
   public static final String TYPE_CUSTOMXML_PROPERTIES = "application/vnd.openxmlformats-officedocument.customXmlProperties+xml";
   public static final String TYPE_DOCUMENT_SETTINGS = "application/vnd.openxmlformats-officedocument.wordprocessingml.settings+xml";
   public static final String TYPE_FONT_TABLE = "application/vnd.openxmlformats-officedocument.wordprocessingml.fontTable+xml";
   public static final String TYPE_FOOTER = "application/vnd.openxmlformats-officedocument.wordprocessingml.footer+xml";
   public static final String TYPE_FOOTNOTES = "application/vnd.openxmlformats-officedocument.wordprocessingml.footnotes+xml";
   public static final String TYPE_HEADER = "application/vnd.openxmlformats-officedocument.wordprocessingml.header+xml";
   public static final String TYPE_MAIN_DOCUMENT = "application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml";
   public static final String TYPE_NUMBERING_DEFINITIONS = "application/vnd.openxmlformats-officedocument.wordprocessingml.numbering+xml";
   public static final String TYPE_RELATIONSHIPS = "application/vnd.openxmlformats-package.relationships+xml";
   private static final String NAMESPACE_URI = "http://schemas.openxmlformats.org/package/2006/content-types";
   private Vector defaults = new Vector();
   private Vector overrides = new Vector();

   public ContentTypes() {
      this.addDefault("rels", "application/vnd.openxmlformats-package.relationships+xml");
      this.addDefault("xml", "application/xml");
      this.addDefault("gif", "image/gif");
      this.addDefault("jpg", "image/jpeg");
      this.addDefault("png", "image/png");
      this.addDefault("wmf", "image/x-wmf");
      this.addDefault("emf", "image/x-emf");
   }

   public void addDefault(String var1, String var2) {
      this.defaults.addElement(new Default(var1, var2));
   }

   public void addOverride(String var1, String var2) {
      this.overrides.addElement(new Override(var1, var2));
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
      var1.println("<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">");
      int var2 = 0;

      int var3;
      for(var3 = this.defaults.size(); var2 < var3; ++var2) {
         Default var4 = (Default)this.defaults.elementAt(var2);
         var1.println("  <Default Extension=\"" + var4.extension + "\"");
         var1.println("    ContentType=\"" + var4.contentType + "\"/>");
      }

      var2 = 0;

      for(var3 = this.overrides.size(); var2 < var3; ++var2) {
         Override var5 = (Override)this.overrides.elementAt(var2);
         var1.println("  <Override PartName=\"" + var5.partName + "\"");
         var1.println("    ContentType=\"" + var5.contentType + "\"/>");
      }

      var1.println("</Types>");
   }

   private class Override {
      String partName;
      String contentType;

      Override(String var2, String var3) {
         this.partName = var2;
         this.contentType = var3;
      }
   }

   private class Default {
      String extension;
      String contentType;

      Default(String var2, String var3) {
         this.extension = var2;
         this.contentType = var3;
      }
   }
}

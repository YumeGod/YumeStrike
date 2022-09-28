package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.util.Encoding;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Vector;

public class Manifest {
   private Vector entries = new Vector();

   public Manifest() {
      this.add("/", "application/vnd.oasis.opendocument.text");
   }

   public void add(String var1, String var2) {
      this.entries.addElement(new Entry(var1, var2));
   }

   public void write(String var1, String var2) throws Exception {
      FileOutputStream var3 = new FileOutputStream(var1);
      OutputStreamWriter var4 = new OutputStreamWriter(new BufferedOutputStream(var3), var2);
      PrintWriter var5 = new PrintWriter(new BufferedWriter(var4));
      var5.println("<?xml version=\"1.0\" encoding=\"" + Encoding.officialName(var2) + "\"?>");
      var5.print("<manifest:manifest");
      var5.print(" xmlns:manifest=\"urn:oasis:names:tc:opendocument:xmlns:manifest:1.0\"");
      var5.println(">");
      int var6 = 0;

      for(int var7 = this.entries.size(); var6 < var7; ++var6) {
         Entry var8 = (Entry)this.entries.elementAt(var6);
         var5.println("<manifest:file-entry");
         var5.println(" manifest:full-path=\"" + var8.path + "\"");
         var5.print(" manifest:media-type=\"");
         if (var8.type != null) {
            var5.print(var8.type);
         }

         var5.println("\"");
         var5.println("/>");
      }

      var5.println("</manifest:manifest>");
      var5.flush();
      var5.close();
   }

   private class Entry {
      String path;
      String type;

      Entry(String var2, String var3) {
         this.path = var2;
         this.type = var3;
      }
   }
}

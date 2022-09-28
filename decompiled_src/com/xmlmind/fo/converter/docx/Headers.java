package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.zip.ZipFile;
import java.util.Vector;

public final class Headers {
   private Vector headers = new Vector();

   public String add(StaticContent var1) {
      String var2 = "header" + (this.headers.size() + 1) + ".xml";
      this.headers.addElement(new Header(var2, var1));
      return var2;
   }

   public void store(ZipFile var1, String var2) throws Exception {
      int var3 = 0;

      for(int var4 = this.headers.size(); var3 < var4; ++var3) {
         Header var5 = (Header)this.headers.elementAt(var3);
         var1.add(var5.name, var5.content.getBytes(var2));
         Relationships var6 = var5.content.relationships();
         if (var6.count() > 0) {
            var1.add("_rels/" + var5.name + ".rels", var6.getBytes(var2));
         }
      }

   }

   private class Header {
      String name;
      StaticContent content;

      Header(String var2, StaticContent var3) {
         this.name = var2;
         this.content = var3;
      }
   }
}

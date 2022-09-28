package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.graphic.GraphicUtil;
import com.xmlmind.fo.zip.ZipEntry;
import com.xmlmind.fo.zip.ZipFile;
import java.util.Vector;

public final class Images {
   private Vector images = new Vector();
   private int nextId = 0;

   public int getNextId() {
      return ++this.nextId;
   }

   public int count() {
      return this.images.size();
   }

   public String add(String var1, String var2) {
      String var3 = "images/" + (this.images.size() + 1) + GraphicUtil.formatToSuffix(var2);
      this.images.addElement(new ZipEntry(var3, var1));
      return var3;
   }

   public void store(ZipFile var1) {
      int var2 = 0;

      for(int var3 = this.images.size(); var2 < var3; ++var2) {
         ZipEntry var4 = (ZipEntry)this.images.elementAt(var2);
         var1.add(var4);
      }

   }
}

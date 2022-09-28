package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public final class RtfFontManager {
   private static RtfFontManager instance = null;
   private Hashtable fontIndex = null;
   private Vector fontTable = null;

   private RtfFontManager() {
      this.fontTable = new Vector();
      this.fontIndex = new Hashtable();
      this.init();
   }

   public static RtfFontManager getInstance() {
      if (instance == null) {
         instance = new RtfFontManager();
      }

      return instance;
   }

   private void init() {
      this.getFontNumber("Arial");
      this.getFontNumber("Symbol");
      this.getFontNumber("Times New Roman");
   }

   public int getFontNumber(String family) {
      Object o = this.fontIndex.get(this.getFontKey(family));
      int retVal;
      if (o == null) {
         this.addFont(family);
         retVal = this.fontTable.size() - 1;
      } else {
         retVal = (Integer)o;
      }

      return retVal;
   }

   public void writeFonts(RtfHeader header) throws IOException {
      if (this.fontTable != null && this.fontTable.size() != 0) {
         header.newLine();
         header.writeGroupMark(true);
         header.writeControlWord("fonttbl");
         int len = this.fontTable.size();

         for(int i = 0; i < len; ++i) {
            header.writeGroupMark(true);
            header.newLine();
            header.write("\\f" + i);
            header.write(" " + (String)this.fontTable.elementAt(i));
            header.write(";");
            header.writeGroupMark(false);
         }

         header.newLine();
         header.writeGroupMark(false);
      }
   }

   private String getFontKey(String family) {
      return family.toLowerCase();
   }

   private void addFont(String family) {
      this.fontIndex.put(this.getFontKey(family), new Integer(this.fontTable.size()));
      this.fontTable.addElement(family);
   }
}

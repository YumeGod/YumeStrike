package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.zip.ZipEntry;

public class OdtEntry extends ZipEntry {
   public String type;

   public OdtEntry(String var1, String var2) {
      super(var1);
      this.type = var2;
   }

   public OdtEntry(String var1, String var2, String var3) {
      this(var1, var2);
      this.setPath(var3);
   }

   public OdtEntry(String var1, String var2, String[] var3) {
      this(var1, var2);
      this.paths = var3;
   }

   public void setPath(String var1) {
      this.paths = new String[]{var1};
   }
}

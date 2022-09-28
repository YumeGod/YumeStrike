package com.xmlmind.fo.converter.docx.sdt;

import com.xmlmind.fo.converter.docx.Wml;
import java.io.PrintWriter;

public final class SdtDataBinding {
   private String storeItemId;
   private String xpath;
   private String prefixMappings;

   public SdtDataBinding(String var1, String var2, String var3) {
      this.storeItemId = var1;
      this.xpath = var2;
      this.prefixMappings = var3;
   }

   public void print(PrintWriter var1) {
      var1.print("<w:dataBinding");
      var1.print(" w:storeItemID=\"" + this.storeItemId + "\"");
      var1.print(" w:xpath=\"" + Wml.escape(this.xpath) + "\"");
      if (this.prefixMappings != null) {
         var1.print(" w:prefixMappings=\"" + Wml.escape(this.prefixMappings) + "\"");
      }

      var1.println(" />");
   }
}

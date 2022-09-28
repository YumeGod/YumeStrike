package com.xmlmind.fo.converter.docx.sdt;

import com.xmlmind.fo.util.Encoding;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

public final class CustomXmlProperties {
   private static final Random random = new Random(123456789L);
   private String id = this.nextId();

   private String nextId() {
      StringBuffer var4 = new StringBuffer();
      long var1 = Double.doubleToLongBits(random.nextDouble() - 1.0);
      String var3 = Long.toHexString(var1);
      var4.append(var3.substring(8));
      var1 = Double.doubleToLongBits(random.nextDouble() - 1.0);
      var3 = Long.toHexString(var1);
      var4.append("-" + var3.substring(4, 8));
      var4.append("-" + var3.substring(8, 12));
      var4.append("-" + var3.substring(12));
      var1 = Double.doubleToLongBits(random.nextDouble() - 1.0);
      var3 = Long.toHexString(var1);
      var4.append("-" + var3.substring(4));
      return "{" + var4.toString() + "}";
   }

   public String id() {
      return this.id;
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
      var1.print("<ds:dataStoreItem");
      var1.print(" xmlns:ds=\"http://schemas.openxmlformats.org/officeDocument/2006/customXml\"");
      var1.print(" ds:itemID=\"" + this.id + "\"");
      var1.println("/>");
   }
}

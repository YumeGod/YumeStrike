package com.xmlmind.fo.converter.docx.sdt;

import com.xmlmind.fo.converter.docx.Wml;
import com.xmlmind.fo.util.Encoding;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Vector;

public final class CustomXml {
   private static final String DEFAULT_ROOT = "root";
   private static final String NAMESPACE_PREFIX = "ns";
   private String root;
   private String namespace;
   private String prefixMapping;
   private CustomXmlProperties properties;
   private Vector elements;
   private Hashtable elementCounts;

   public CustomXml() {
      this((String)null);
   }

   public CustomXml(String var1) {
      this(var1, (String)null);
   }

   public CustomXml(String var1, String var2) {
      this.properties = new CustomXmlProperties();
      this.elements = new Vector();
      this.elementCounts = new Hashtable();
      if (var2 != null) {
         this.prefixMapping = "xmlns:ns=\"" + var2 + "\"";
      }

      this.root = var1 != null ? var1 : "root";
      this.namespace = var2;
   }

   public String id() {
      return this.properties.id();
   }

   public CustomXmlProperties properties() {
      return this.properties;
   }

   public SdtDataBinding add(String var1) {
      return this.add(var1, (String)null);
   }

   public SdtDataBinding add(String var1, String var2) {
      return this.add(var1, var2, false);
   }

   public SdtDataBinding add(String var1, String var2, boolean var3) {
      int var4 = this.add(new Element(var1, var2, var3));
      String var5;
      if (this.namespace != null) {
         var5 = "ns:";
      } else {
         var5 = "";
      }

      String var6 = "/" + var5 + this.root + "/" + var5 + var1 + "[" + var4 + "]";
      return new SdtDataBinding(this.id(), var6, this.prefixMapping);
   }

   private int add(Element var1) {
      int var2 = 1;
      Integer var3 = (Integer)this.elementCounts.get(var1.name);
      if (var3 != null) {
         var2 = var3 + 1;
      }

      this.elements.addElement(var1);
      this.elementCounts.put(var1.name, new Integer(var2));
      return var2;
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
      var1.print("<" + this.root);
      if (this.namespace != null) {
         var1.print(" xmlns=\"" + this.namespace + "\"");
      }

      var1.println(">");
      int var2 = 0;

      for(int var3 = this.elements.size(); var2 < var3; ++var2) {
         Element var4 = (Element)this.elements.elementAt(var2);
         var1.print("  <" + var4.name);
         if (var4.preserveSpace) {
            var1.print(" xml:space=\"preserve\"");
         }

         var1.print(">");
         if (var4.content != null) {
            var1.print(Wml.escape(var4.content));
         }

         var1.println("</" + var4.name + ">");
      }

      var1.println("</" + this.root + ">");
   }

   private class Element {
      String name;
      String content;
      boolean preserveSpace;

      Element(String var2, String var3, boolean var4) {
         this.name = var2;
         this.content = var3;
         this.preserveSpace = var4;
      }
   }
}

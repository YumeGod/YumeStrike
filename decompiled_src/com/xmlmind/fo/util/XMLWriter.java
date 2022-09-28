package com.xmlmind.fo.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Vector;
import org.xml.sax.Attributes;

public final class XMLWriter {
   private final File outFile;
   private final Writer out;
   private Vector nsStack;
   private String rootElementNS;

   public XMLWriter(File var1) throws IOException {
      this.outFile = var1;
      this.out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var1), "UTF8"));
      this.out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
      this.nsStack = new Vector();
      StackEntry var2 = this.pushEntry();
      var2.add("xml", "http://www.w3.org/XML/1998/namespace");
      var2.add("", "");
   }

   public File getOutputFile() {
      return this.outFile;
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws IOException {
      if (this.rootElementNS == null) {
         this.rootElementNS = var1;
      }

      this.out.write(60);
      this.out.write(var3);
      StackEntry var5 = this.pushEntry();
      this.definePrefix(var3, var1, false);
      int var6 = var4.getLength();

      for(int var7 = 0; var7 < var6; ++var7) {
         String var8 = var4.getQName(var7);
         if (!var8.startsWith("xmlns")) {
            String var9 = var4.getURI(var7);
            String var10 = var4.getValue(var7);
            this.out.write(32);
            this.out.write(var8);
            this.out.write("=\"");
            this.characters(var10);
            this.out.write(34);
            this.definePrefix(var8, var9, true);
         }
      }

      var5.declarePrefixes();
      this.out.write(62);
   }

   public void characters(String var1) throws IOException {
      char[] var2 = var1.toCharArray();
      this.characters(var2, 0, var2.length);
   }

   public void characters(char[] var1, int var2, int var3) throws IOException {
      int var4 = var2 + var3;

      for(int var5 = var2; var5 < var4; ++var5) {
         char var6 = var1[var5];
         switch (var6) {
            case '"':
               this.out.write("&quot;");
               break;
            case '&':
               this.out.write("&amp;");
               break;
            case '\'':
               this.out.write("&apos;");
               break;
            case '<':
               this.out.write("&lt;");
               break;
            case '>':
               this.out.write("&gt;");
               break;
            default:
               this.out.write(var6);
         }
      }

   }

   public void endElement(String var1, String var2, String var3) throws IOException {
      this.out.write("</");
      this.out.write(var3);
      this.out.write(62);
      this.popEntry();
   }

   public void close() throws IOException {
      this.out.flush();
      this.out.close();
   }

   public String getRootElementNamespaceURI() {
      return this.rootElementNS;
   }

   private StackEntry pushEntry() {
      StackEntry var1 = new StackEntry();
      this.nsStack.addElement(var1);
      return var1;
   }

   private void popEntry() {
      this.nsStack.removeElementAt(this.nsStack.size() - 1);
   }

   private StackEntry topEntry() {
      return (StackEntry)this.nsStack.lastElement();
   }

   private boolean inScope(String var1, String var2) {
      for(int var3 = this.nsStack.size() - 1; var3 >= 0; --var3) {
         StackEntry var4 = (StackEntry)this.nsStack.elementAt(var3);
         if (var4.contains(var1, var2)) {
            return true;
         }
      }

      return false;
   }

   private void definePrefix(String var1, String var2, boolean var3) {
      int var5 = var1.indexOf(58);
      String var4;
      if (var5 > 0 && var5 != var1.length() - 1) {
         var4 = var1.substring(0, var5);
      } else {
         var4 = "";
      }

      if (!var3 || var4.length() != 0) {
         if (!this.inScope(var4, var2)) {
            this.topEntry().add(var4, var2);
         }
      }
   }

   private class StackEntry {
      private String[] pairs;

      private StackEntry() {
      }

      public boolean contains(String var1, String var2) {
         if (this.pairs != null) {
            for(int var3 = 0; var3 < this.pairs.length; var3 += 2) {
               if (var1.equals(this.pairs[var3]) && var2.equals(this.pairs[var3 + 1])) {
                  return true;
               }
            }
         }

         return false;
      }

      public void add(String var1, String var2) {
         if (this.pairs == null) {
            this.pairs = new String[]{var1, var2};
         } else {
            int var3 = this.pairs.length;
            String[] var4 = new String[var3 + 2];
            System.arraycopy(this.pairs, 0, var4, 0, var3);
            var4[var3] = var1;
            var4[var3 + 1] = var2;
            this.pairs = var4;
         }

      }

      public void declarePrefixes() throws IOException {
         if (this.pairs != null) {
            for(int var1 = 0; var1 < this.pairs.length; var1 += 2) {
               String var2 = this.pairs[var1];
               String var3 = this.pairs[var1 + 1];
               XMLWriter.this.out.write(32);
               if (var2.length() == 0) {
                  XMLWriter.this.out.write("xmlns");
               } else {
                  XMLWriter.this.out.write("xmlns:");
                  XMLWriter.this.out.write(var2);
               }

               XMLWriter.this.out.write("=\"");
               XMLWriter.this.characters(var3);
               XMLWriter.this.out.write(34);
            }
         }

      }

      // $FF: synthetic method
      StackEntry(Object var2) {
         this();
      }
   }
}

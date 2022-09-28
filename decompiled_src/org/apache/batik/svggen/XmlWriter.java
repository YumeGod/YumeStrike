package org.apache.batik.svggen;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

class XmlWriter implements SVGConstants {
   private static String EOL;
   private static final String TAG_END = "/>";
   private static final String TAG_START = "</";
   private static final char[] SPACES = new char[]{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
   private static final int SPACES_LEN;

   private static void writeXml(Attr var0, IndentWriter var1, boolean var2) throws IOException {
      String var3 = var0.getName();
      var1.write(var3);
      var1.write("=\"");
      writeChildrenXml(var0, var1, var2);
      var1.write(34);
   }

   private static void writeChildrenXml(Attr var0, IndentWriter var1, boolean var2) throws IOException {
      char[] var3 = var0.getValue().toCharArray();
      if (var3 != null) {
         int var4 = var3.length;
         int var5 = 0;

         int var6;
         for(var6 = 0; var6 < var4; ++var6) {
            char var7 = var3[var6];
            switch (var7) {
               case '"':
                  var1.write(var3, var5, var6 - var5);
                  var5 = var6 + 1;
                  var1.write("&quot;");
                  break;
               case '&':
                  var1.write(var3, var5, var6 - var5);
                  var5 = var6 + 1;
                  var1.write("&amp;");
                  break;
               case '\'':
                  var1.write(var3, var5, var6 - var5);
                  var5 = var6 + 1;
                  var1.write("&apos;");
                  break;
               case '<':
                  var1.write(var3, var5, var6 - var5);
                  var5 = var6 + 1;
                  var1.write("&lt;");
                  break;
               case '>':
                  var1.write(var3, var5, var6 - var5);
                  var5 = var6 + 1;
                  var1.write("&gt;");
                  break;
               default:
                  if (var2 && var7 > 127) {
                     var1.write(var3, var5, var6 - var5);
                     String var8 = "0000" + Integer.toHexString(var7);
                     var1.write("&#x" + var8.substring(var8.length() - 4) + ";");
                     var5 = var6 + 1;
                  }
            }
         }

         var1.write(var3, var5, var6 - var5);
      }
   }

   private static void writeXml(Comment var0, IndentWriter var1, boolean var2) throws IOException {
      char[] var3 = var0.getData().toCharArray();
      if (var3 == null) {
         var1.write("<!---->");
      } else {
         var1.write("<!--");
         boolean var4 = false;
         int var5 = var3.length;
         int var6 = 0;

         int var7;
         for(var7 = 0; var7 < var5; ++var7) {
            char var8 = var3[var7];
            if (var8 == '-') {
               if (var4) {
                  var1.write(var3, var6, var7 - var6);
                  var6 = var7;
                  var1.write(32);
               }

               var4 = true;
            } else {
               var4 = false;
            }
         }

         var1.write(var3, var6, var7 - var6);
         if (var4) {
            var1.write(32);
         }

         var1.write("-->");
      }
   }

   private static void writeXml(Text var0, IndentWriter var1, boolean var2) throws IOException {
      writeXml(var0, var1, false, var2);
   }

   private static void writeXml(Text var0, IndentWriter var1, boolean var2, boolean var3) throws IOException {
      char[] var4 = var0.getData().toCharArray();
      if (var4 == null) {
         System.err.println("Null text data??");
      } else {
         int var5 = var4.length;
         int var6 = 0;
         int var7 = 0;
         char var8;
         if (var2) {
            label60:
            while(var7 < var5) {
               var8 = var4[var7];
               switch (var8) {
                  case '\t':
                  case '\n':
                  case '\r':
                  case ' ':
                     ++var7;
                     break;
                  default:
                     break label60;
               }
            }

            var6 = var7;
         }

         int var9;
         label51:
         do {
            for(; var7 < var5; ++var7) {
               var8 = var4[var7];
               switch (var8) {
                  case '\t':
                  case '\n':
                  case '\r':
                  case ' ':
                     if (var2) {
                        var9 = var7++;

                        while(var7 < var5) {
                           switch (var4[var7]) {
                              case '\t':
                              case '\n':
                              case '\r':
                              case ' ':
                                 ++var7;
                                 break;
                              default:
                                 continue label51;
                           }
                        }
                        continue label51;
                     }
                     break;
                  case '&':
                     var1.write(var4, var6, var7 - var6);
                     var6 = var7 + 1;
                     var1.write("&amp;");
                     break;
                  case '<':
                     var1.write(var4, var6, var7 - var6);
                     var6 = var7 + 1;
                     var1.write("&lt;");
                     break;
                  case '>':
                     var1.write(var4, var6, var7 - var6);
                     var6 = var7 + 1;
                     var1.write("&gt;");
                     break;
                  default:
                     if (var3 && var8 > 127) {
                        var1.write(var4, var6, var7 - var6);
                        String var10 = "0000" + Integer.toHexString(var8);
                        var1.write("&#x" + var10.substring(var10.length() - 4) + ";");
                        var6 = var7 + 1;
                     }
               }
            }

            var1.write(var4, var6, var7 - var6);
            return;
         } while(var7 != var5);

         var1.write(var4, var6, var9 - var6);
      }
   }

   private static void writeXml(CDATASection var0, IndentWriter var1, boolean var2) throws IOException {
      char[] var3 = var0.getData().toCharArray();
      if (var3 == null) {
         var1.write("<![CDATA[]]>");
      } else {
         var1.write("<![CDATA[");
         int var4 = var3.length;
         int var5 = 0;
         int var6 = 0;

         while(true) {
            while(var6 < var4) {
               char var7 = var3[var6];
               if (var7 == ']' && var6 + 2 < var3.length && var3[var6 + 1] == ']' && var3[var6 + 2] == '>') {
                  var1.write(var3, var5, var6 - var5);
                  var5 = var6 + 1;
                  var1.write("]]]]><![CDATA[>");
               } else {
                  ++var6;
               }
            }

            var1.write(var3, var5, var6 - var5);
            var1.write("]]>");
            return;
         }
      }
   }

   private static void writeXml(Element var0, IndentWriter var1, boolean var2) throws IOException, SVGGraphics2DIOException {
      var1.write((String)"</", 0, 1);
      var1.write(var0.getTagName());
      NamedNodeMap var3 = var0.getAttributes();
      if (var3 != null) {
         int var4 = var3.getLength();

         for(int var5 = 0; var5 < var4; ++var5) {
            Attr var6 = (Attr)var3.item(var5);
            var1.write(32);
            writeXml(var6, var1, var2);
         }
      }

      boolean var7 = var0.getParentNode().getLastChild() == var0;
      if (!var0.hasChildNodes()) {
         if (var7) {
            var1.setIndentLevel(var1.getIndentLevel() - 2);
         }

         var1.printIndent();
         var1.write((String)"/>", 0, 2);
      } else {
         Node var8 = var0.getFirstChild();
         var1.printIndent();
         var1.write((String)"/>", 1, 1);
         if (var8.getNodeType() != 3 || var0.getLastChild() != var8) {
            var1.setIndentLevel(var1.getIndentLevel() + 2);
         }

         writeChildrenXml(var0, var1, var2);
         var1.write((String)"</", 0, 2);
         var1.write(var0.getTagName());
         if (var7) {
            var1.setIndentLevel(var1.getIndentLevel() - 2);
         }

         var1.printIndent();
         var1.write((String)"/>", 1, 1);
      }
   }

   private static void writeChildrenXml(Element var0, IndentWriter var1, boolean var2) throws IOException, SVGGraphics2DIOException {
      for(Node var3 = var0.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
         writeXml((Node)var3, (Writer)var1, var2);
      }

   }

   private static void writeDocumentHeader(IndentWriter var0) throws IOException {
      String var1 = null;
      if (var0.getProxied() instanceof OutputStreamWriter) {
         OutputStreamWriter var2 = (OutputStreamWriter)var0.getProxied();
         var1 = java2std(var2.getEncoding());
      }

      var0.write("<?xml version=\"1.0\"");
      if (var1 != null) {
         var0.write(" encoding=\"");
         var0.write(var1);
         var0.write(34);
      }

      var0.write("?>");
      var0.write(EOL);
      var0.write("<!DOCTYPE svg PUBLIC '");
      var0.write("-//W3C//DTD SVG 1.0//EN");
      var0.write("'");
      var0.write(EOL);
      var0.write("          '");
      var0.write("http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd");
      var0.write("'");
      var0.write(">");
      var0.write(EOL);
   }

   private static void writeXml(Document var0, IndentWriter var1, boolean var2) throws IOException, SVGGraphics2DIOException {
      writeDocumentHeader(var1);
      NodeList var3 = var0.getChildNodes();
      writeXml(var3, var1, var2);
   }

   private static void writeXml(NodeList var0, IndentWriter var1, boolean var2) throws IOException, SVGGraphics2DIOException {
      int var3 = var0.getLength();
      if (var3 != 0) {
         for(int var4 = 0; var4 < var3; ++var4) {
            Node var5 = var0.item(var4);
            writeXml((Node)var5, (Writer)var1, var2);
            var1.write(EOL);
         }

      }
   }

   static String java2std(String var0) {
      if (var0 == null) {
         return null;
      } else if (var0.startsWith("ISO8859_")) {
         return "ISO-8859-" + var0.substring(8);
      } else if (var0.startsWith("8859_")) {
         return "ISO-8859-" + var0.substring(5);
      } else if (!"ASCII7".equalsIgnoreCase(var0) && !"ASCII".equalsIgnoreCase(var0)) {
         if ("UTF8".equalsIgnoreCase(var0)) {
            return "UTF-8";
         } else if (var0.startsWith("Unicode")) {
            return "UTF-16";
         } else if ("SJIS".equalsIgnoreCase(var0)) {
            return "Shift_JIS";
         } else if ("JIS".equalsIgnoreCase(var0)) {
            return "ISO-2022-JP";
         } else {
            return "EUCJIS".equalsIgnoreCase(var0) ? "EUC-JP" : "UTF-8";
         }
      } else {
         return "US-ASCII";
      }
   }

   public static void writeXml(Node var0, Writer var1, boolean var2) throws SVGGraphics2DIOException {
      try {
         IndentWriter var3 = null;
         if (var1 instanceof IndentWriter) {
            var3 = (IndentWriter)var1;
         } else {
            var3 = new IndentWriter(var1);
         }

         switch (var0.getNodeType()) {
            case 1:
               writeXml((Element)var0, var3, var2);
               break;
            case 2:
               writeXml((Attr)var0, var3, var2);
               break;
            case 3:
               writeXml((Text)var0, var3, var2);
               break;
            case 4:
               writeXml((CDATASection)var0, var3, var2);
               break;
            case 5:
            case 6:
            case 7:
            case 10:
            default:
               throw new SVGGraphics2DRuntimeException("Unable to write node of type " + var0.getClass().getName());
            case 8:
               writeXml((Comment)var0, var3, var2);
               break;
            case 9:
               writeXml((Document)var0, var3, var2);
               break;
            case 11:
               writeDocumentHeader(var3);
               NodeList var4 = var0.getChildNodes();
               writeXml(var4, var3, var2);
         }

      } catch (IOException var5) {
         throw new SVGGraphics2DIOException(var5);
      }
   }

   static {
      SPACES_LEN = SPACES.length;

      String var0;
      try {
         var0 = System.getProperty("line.separator", "\n");
      } catch (SecurityException var2) {
         var0 = "\n";
      }

      EOL = var0;
   }

   static class IndentWriter extends Writer {
      protected Writer proxied;
      protected int indentLevel;
      protected int column;

      public IndentWriter(Writer var1) {
         if (var1 == null) {
            throw new SVGGraphics2DRuntimeException("proxy should not be null");
         } else {
            this.proxied = var1;
         }
      }

      public void setIndentLevel(int var1) {
         this.indentLevel = var1;
      }

      public int getIndentLevel() {
         return this.indentLevel;
      }

      public void printIndent() throws IOException {
         this.proxied.write(XmlWriter.EOL);

         for(int var1 = this.indentLevel; var1 > 0; var1 -= XmlWriter.SPACES_LEN) {
            if (var1 <= XmlWriter.SPACES_LEN) {
               this.proxied.write(XmlWriter.SPACES, 0, var1);
               break;
            }

            this.proxied.write(XmlWriter.SPACES, 0, XmlWriter.SPACES_LEN);
         }

         this.column = this.indentLevel;
      }

      public Writer getProxied() {
         return this.proxied;
      }

      public int getColumn() {
         return this.column;
      }

      public void write(int var1) throws IOException {
         ++this.column;
         this.proxied.write(var1);
      }

      public void write(char[] var1) throws IOException {
         this.column += var1.length;
         this.proxied.write(var1);
      }

      public void write(char[] var1, int var2, int var3) throws IOException {
         this.column += var3;
         this.proxied.write(var1, var2, var3);
      }

      public void write(String var1) throws IOException {
         this.column += var1.length();
         this.proxied.write(var1);
      }

      public void write(String var1, int var2, int var3) throws IOException {
         this.column += var3;
         this.proxied.write(var1, var2, var3);
      }

      public void flush() throws IOException {
         this.proxied.flush();
      }

      public void close() throws IOException {
         this.column = -1;
         this.proxied.close();
      }
   }
}

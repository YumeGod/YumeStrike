package org.apache.batik.transcoder.svg2svg;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.batik.xml.XMLUtilities;

public class OutputManager {
   protected PrettyPrinter prettyPrinter;
   protected Writer writer;
   protected int level;
   protected StringBuffer margin = new StringBuffer();
   protected int line = 1;
   protected int column;
   protected List xmlSpace = new LinkedList();
   protected boolean canIndent;
   protected List startingLines;
   protected boolean lineAttributes;

   public OutputManager(PrettyPrinter var1, Writer var2) {
      this.xmlSpace.add(Boolean.FALSE);
      this.canIndent = true;
      this.startingLines = new LinkedList();
      this.lineAttributes = false;
      this.prettyPrinter = var1;
      this.writer = var2;
   }

   public void printCharacter(char var1) throws IOException {
      if (var1 == '\n') {
         this.printNewline();
      } else {
         ++this.column;
         this.writer.write(var1);
      }

   }

   public void printNewline() throws IOException {
      String var1 = this.prettyPrinter.getNewline();

      for(int var2 = 0; var2 < var1.length(); ++var2) {
         this.writer.write(var1.charAt(var2));
      }

      this.column = 0;
      ++this.line;
   }

   public void printString(String var1) throws IOException {
      for(int var2 = 0; var2 < var1.length(); ++var2) {
         this.printCharacter(var1.charAt(var2));
      }

   }

   public void printCharacters(char[] var1) throws IOException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.printCharacter(var1[var2]);
      }

   }

   public void printSpaces(char[] var1, boolean var2) throws IOException {
      if (this.prettyPrinter.getFormat()) {
         if (!var2) {
            this.printCharacter(' ');
         }
      } else {
         this.printCharacters(var1);
      }

   }

   public void printTopSpaces(char[] var1) throws IOException {
      if (this.prettyPrinter.getFormat()) {
         int var2 = this.newlines(var1);

         for(int var3 = 0; var3 < var2; ++var3) {
            this.printNewline();
         }
      } else {
         this.printCharacters(var1);
      }

   }

   public void printComment(char[] var1) throws IOException {
      if (this.prettyPrinter.getFormat()) {
         if (this.canIndent) {
            this.printNewline();
            this.printString(this.margin.toString());
         }

         this.printString("<!--");
         if (this.column + var1.length + 3 < this.prettyPrinter.getDocumentWidth()) {
            this.printCharacters(var1);
         } else {
            this.formatText(var1, this.margin.toString(), false);
            this.printCharacter(' ');
         }

         if (this.column + 3 > this.prettyPrinter.getDocumentWidth()) {
            this.printNewline();
            this.printString(this.margin.toString());
         }

         this.printString("-->");
      } else {
         this.printString("<!--");
         this.printCharacters(var1);
         this.printString("-->");
      }

   }

   public void printXMLDecl(char[] var1, char[] var2, char[] var3, char[] var4, char var5, char[] var6, char[] var7, char[] var8, char[] var9, char var10, char[] var11, char[] var12, char[] var13, char[] var14, char var15, char[] var16) throws IOException {
      this.printString("<?xml");
      this.printSpaces(var1, false);
      this.printString("version");
      if (var2 != null) {
         this.printSpaces(var2, true);
      }

      this.printCharacter('=');
      if (var3 != null) {
         this.printSpaces(var3, true);
      }

      this.printCharacter(var5);
      this.printCharacters(var4);
      this.printCharacter(var5);
      if (var6 != null) {
         this.printSpaces(var6, false);
         if (var9 != null) {
            this.printString("encoding");
            if (var7 != null) {
               this.printSpaces(var7, true);
            }

            this.printCharacter('=');
            if (var8 != null) {
               this.printSpaces(var8, true);
            }

            this.printCharacter(var10);
            this.printCharacters(var9);
            this.printCharacter(var10);
            if (var11 != null) {
               this.printSpaces(var11, var14 == null);
            }
         }

         if (var14 != null) {
            this.printString("standalone");
            if (var12 != null) {
               this.printSpaces(var12, true);
            }

            this.printCharacter('=');
            if (var13 != null) {
               this.printSpaces(var13, true);
            }

            this.printCharacter(var15);
            this.printCharacters(var14);
            this.printCharacter(var15);
            if (var16 != null) {
               this.printSpaces(var16, true);
            }
         }
      }

      this.printString("?>");
   }

   public void printPI(char[] var1, char[] var2, char[] var3) throws IOException {
      if (this.prettyPrinter.getFormat() && this.canIndent) {
         this.printNewline();
         this.printString(this.margin.toString());
      }

      this.printString("<?");
      this.printCharacters(var1);
      this.printSpaces(var2, false);
      this.printCharacters(var3);
      this.printString("?>");
   }

   public void printDoctypeStart(char[] var1, char[] var2, char[] var3, String var4, char[] var5, char[] var6, char var7, char[] var8, char[] var9, char var10, char[] var11) throws IOException {
      if (this.prettyPrinter.getFormat()) {
         this.printString("<!DOCTYPE");
         this.printCharacter(' ');
         this.printCharacters(var2);
         if (var3 != null) {
            this.printCharacter(' ');
            this.printString(var4);
            this.printCharacter(' ');
            this.printCharacter(var7);
            this.printCharacters(var6);
            this.printCharacter(var7);
            if (var8 != null && var9 != null) {
               if (this.column + var9.length + 3 > this.prettyPrinter.getDocumentWidth()) {
                  this.printNewline();

                  for(int var12 = 0; var12 < this.prettyPrinter.getTabulationWidth(); ++var12) {
                     this.printCharacter(' ');
                  }
               } else {
                  this.printCharacter(' ');
               }

               this.printCharacter(var10);
               this.printCharacters(var9);
               this.printCharacter(var10);
               this.printCharacter(' ');
            }
         }
      } else {
         this.printString("<!DOCTYPE");
         this.printSpaces(var1, false);
         this.printCharacters(var2);
         if (var3 != null) {
            this.printSpaces(var3, false);
            this.printString(var4);
            this.printSpaces(var5, false);
            this.printCharacter(var7);
            this.printCharacters(var6);
            this.printCharacter(var7);
            if (var8 != null) {
               this.printSpaces(var8, var9 == null);
               if (var9 != null) {
                  this.printCharacter(var10);
                  this.printCharacters(var9);
                  this.printCharacter(var10);
                  if (var11 != null) {
                     this.printSpaces(var11, true);
                  }
               }
            }
         }
      }

   }

   public void printDoctypeEnd(char[] var1) throws IOException {
      if (var1 != null) {
         this.printSpaces(var1, true);
      }

      this.printCharacter('>');
   }

   public void printParameterEntityReference(char[] var1) throws IOException {
      this.printCharacter('%');
      this.printCharacters(var1);
      this.printCharacter(';');
   }

   public void printEntityReference(char[] var1, boolean var2) throws IOException {
      if (this.prettyPrinter.getFormat() && this.xmlSpace.get(0) != Boolean.TRUE && var2) {
         this.printNewline();
         this.printString(this.margin.toString());
      }

      this.printCharacter('&');
      this.printCharacters(var1);
      this.printCharacter(';');
   }

   public void printCharacterEntityReference(char[] var1, boolean var2, boolean var3) throws IOException {
      if (this.prettyPrinter.getFormat() && this.xmlSpace.get(0) != Boolean.TRUE) {
         if (var2) {
            this.printNewline();
            this.printString(this.margin.toString());
         } else if (var3) {
            int var4 = this.column + var1.length + 3;
            if (var4 > this.prettyPrinter.getDocumentWidth()) {
               this.printNewline();
               this.printString(this.margin.toString());
            } else {
               this.printCharacter(' ');
            }
         }
      }

      this.printString("&#");
      this.printCharacters(var1);
      this.printCharacter(';');
   }

   public void printElementStart(char[] var1, List var2, char[] var3) throws IOException {
      this.xmlSpace.add(0, this.xmlSpace.get(0));
      this.startingLines.add(0, new Integer(this.line));
      if (this.prettyPrinter.getFormat() && this.canIndent) {
         this.printNewline();
         this.printString(this.margin.toString());
      }

      this.printCharacter('<');
      this.printCharacters(var1);
      Iterator var4;
      AttributeInfo var5;
      if (this.prettyPrinter.getFormat()) {
         var4 = var2.iterator();
         if (var4.hasNext()) {
            var5 = (AttributeInfo)var4.next();
            if (var5.isAttribute("xml:space")) {
               this.xmlSpace.set(0, var5.value.equals("preserve") ? Boolean.TRUE : Boolean.FALSE);
            }

            this.printCharacter(' ');
            this.printCharacters(var5.name);
            this.printCharacter('=');
            this.printCharacter(var5.delimiter);
            this.printString(var5.value);
            this.printCharacter(var5.delimiter);
         }

         while(var4.hasNext()) {
            var5 = (AttributeInfo)var4.next();
            if (var5.isAttribute("xml:space")) {
               this.xmlSpace.set(0, var5.value.equals("preserve") ? Boolean.TRUE : Boolean.FALSE);
            }

            int var6 = var5.name.length + var5.value.length() + 4;
            if (!this.lineAttributes && var6 + this.column <= this.prettyPrinter.getDocumentWidth()) {
               this.printCharacter(' ');
            } else {
               this.printNewline();
               this.printString(this.margin.toString());

               for(int var7 = 0; var7 < var1.length + 2; ++var7) {
                  this.printCharacter(' ');
               }
            }

            this.printCharacters(var5.name);
            this.printCharacter('=');
            this.printCharacter(var5.delimiter);
            this.printString(var5.value);
            this.printCharacter(var5.delimiter);
         }
      } else {
         var4 = var2.iterator();

         while(var4.hasNext()) {
            var5 = (AttributeInfo)var4.next();
            if (var5.isAttribute("xml:space")) {
               this.xmlSpace.set(0, var5.value.equals("preserve") ? Boolean.TRUE : Boolean.FALSE);
            }

            this.printSpaces(var5.space, false);
            this.printCharacters(var5.name);
            if (var5.space1 != null) {
               this.printSpaces(var5.space1, true);
            }

            this.printCharacter('=');
            if (var5.space2 != null) {
               this.printSpaces(var5.space2, true);
            }

            this.printCharacter(var5.delimiter);
            this.printString(var5.value);
            this.printCharacter(var5.delimiter);
         }
      }

      if (var3 != null) {
         this.printSpaces(var3, true);
      }

      ++this.level;

      for(int var8 = 0; var8 < this.prettyPrinter.getTabulationWidth(); ++var8) {
         this.margin.append(' ');
      }

      this.canIndent = true;
   }

   public void printElementEnd(char[] var1, char[] var2) throws IOException {
      for(int var3 = 0; var3 < this.prettyPrinter.getTabulationWidth(); ++var3) {
         this.margin.deleteCharAt(0);
      }

      --this.level;
      if (var1 != null) {
         if (this.prettyPrinter.getFormat() && this.xmlSpace.get(0) != Boolean.TRUE && (this.line != (Integer)this.startingLines.get(0) || this.column + var1.length + 3 >= this.prettyPrinter.getDocumentWidth())) {
            this.printNewline();
            this.printString(this.margin.toString());
         }

         this.printString("</");
         this.printCharacters(var1);
         if (var2 != null) {
            this.printSpaces(var2, true);
         }

         this.printCharacter('>');
      } else {
         this.printString("/>");
      }

      this.startingLines.remove(0);
      this.xmlSpace.remove(0);
   }

   public boolean printCharacterData(char[] var1, boolean var2, boolean var3) throws IOException {
      if (!this.prettyPrinter.getFormat()) {
         this.printCharacters(var1);
         return false;
      } else {
         this.canIndent = true;
         if (!this.isWhiteSpace(var1)) {
            if (this.xmlSpace.get(0) == Boolean.TRUE) {
               this.printCharacters(var1);
               this.canIndent = false;
               return false;
            } else {
               if (var2) {
                  this.printNewline();
                  this.printString(this.margin.toString());
               }

               return this.formatText(var1, this.margin.toString(), var3);
            }
         } else {
            int var4 = this.newlines(var1);

            for(int var5 = 0; var5 < var4 - 1; ++var5) {
               this.printNewline();
            }

            return true;
         }
      }
   }

   public void printCDATASection(char[] var1) throws IOException {
      this.printString("<![CDATA[");
      this.printCharacters(var1);
      this.printString("]]>");
   }

   public void printNotation(char[] var1, char[] var2, char[] var3, String var4, char[] var5, char[] var6, char var7, char[] var8, char[] var9, char var10, char[] var11) throws IOException {
      this.writer.write("<!NOTATION");
      this.printSpaces(var1, false);
      this.writer.write(var2);
      this.printSpaces(var3, false);
      this.writer.write(var4);
      this.printSpaces(var5, false);
      this.writer.write(var7);
      this.writer.write(var6);
      this.writer.write(var7);
      if (var8 != null) {
         this.printSpaces(var8, false);
         if (var9 != null) {
            this.writer.write(var10);
            this.writer.write(var9);
            this.writer.write(var10);
         }
      }

      if (var11 != null) {
         this.printSpaces(var11, true);
      }

      this.writer.write(62);
   }

   public void printAttlistStart(char[] var1, char[] var2) throws IOException {
      this.writer.write("<!ATTLIST");
      this.printSpaces(var1, false);
      this.writer.write(var2);
   }

   public void printAttlistEnd(char[] var1) throws IOException {
      if (var1 != null) {
         this.printSpaces(var1, false);
      }

      this.writer.write(62);
   }

   public void printAttName(char[] var1, char[] var2, char[] var3) throws IOException {
      this.printSpaces(var1, false);
      this.writer.write(var2);
      this.printSpaces(var3, false);
   }

   public void printEnumeration(List var1) throws IOException {
      this.writer.write(40);
      Iterator var2 = var1.iterator();
      NameInfo var3 = (NameInfo)var2.next();
      if (var3.space1 != null) {
         this.printSpaces(var3.space1, true);
      }

      this.writer.write(var3.name);
      if (var3.space2 != null) {
         this.printSpaces(var3.space2, true);
      }

      while(var2.hasNext()) {
         this.writer.write(124);
         var3 = (NameInfo)var2.next();
         if (var3.space1 != null) {
            this.printSpaces(var3.space1, true);
         }

         this.writer.write(var3.name);
         if (var3.space2 != null) {
            this.printSpaces(var3.space2, true);
         }
      }

      this.writer.write(41);
   }

   protected int newlines(char[] var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var1[var3] == '\n') {
            ++var2;
         }
      }

      return var2;
   }

   protected boolean isWhiteSpace(char[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (!XMLUtilities.isXMLSpace(var1[var2])) {
            return false;
         }
      }

      return true;
   }

   protected boolean formatText(char[] var1, String var2, boolean var3) throws IOException {
      int var4 = 0;
      boolean var5 = var3;

      while(var4 < var1.length) {
         while(var4 < var1.length) {
            if (!XMLUtilities.isXMLSpace(var1[var4])) {
               StringBuffer var6 = new StringBuffer();

               while(var4 < var1.length && !XMLUtilities.isXMLSpace(var1[var4])) {
                  var6.append(var1[var4++]);
               }

               if (var6.length() == 0) {
                  return var5;
               }

               if (var5) {
                  int var7 = this.column + var6.length();
                  if (var7 < this.prettyPrinter.getDocumentWidth() - 1 || var2.length() + var6.length() >= this.prettyPrinter.getDocumentWidth() - 1 && var2.length() >= this.column) {
                     if (this.column > var2.length()) {
                        this.printCharacter(' ');
                     }
                  } else {
                     this.printNewline();
                     this.printString(var2);
                  }
               }

               this.printString(var6.toString());
               var5 = false;
            } else {
               var5 = true;
               ++var4;
            }
         }

         return var5;
      }

      return var5;
   }

   public static class AttributeInfo {
      public char[] space;
      public char[] name;
      public char[] space1;
      public char[] space2;
      public String value;
      public char delimiter;
      public boolean entityReferences;

      public AttributeInfo(char[] var1, char[] var2, char[] var3, char[] var4, String var5, char var6, boolean var7) {
         this.space = var1;
         this.name = var2;
         this.space1 = var3;
         this.space2 = var4;
         this.value = var5;
         this.delimiter = var6;
         this.entityReferences = var7;
      }

      public boolean isAttribute(String var1) {
         if (this.name.length == var1.length()) {
            for(int var2 = 0; var2 < this.name.length; ++var2) {
               if (this.name[var2] != var1.charAt(var2)) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public static class NameInfo {
      public char[] space1;
      public char[] name;
      public char[] space2;

      public NameInfo(char[] var1, char[] var2, char[] var3) {
         this.space1 = var1;
         this.name = var2;
         this.space2 = var3;
      }
   }
}

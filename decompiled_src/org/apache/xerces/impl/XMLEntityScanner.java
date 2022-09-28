package org.apache.xerces.impl;

import java.io.EOFException;
import java.io.IOException;
import java.util.Locale;
import org.apache.xerces.impl.io.UCSReader;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLString;

public class XMLEntityScanner implements XMLLocator {
   private static final boolean DEBUG_ENCODINGS = false;
   private static final boolean DEBUG_BUFFER = false;
   private XMLEntityManager fEntityManager = null;
   protected XMLEntityManager.ScannedEntity fCurrentEntity = null;
   protected SymbolTable fSymbolTable = null;
   protected int fBufferSize = 2048;
   protected XMLErrorReporter fErrorReporter;

   public String getBaseSystemId() {
      return this.fCurrentEntity != null && this.fCurrentEntity.entityLocation != null ? this.fCurrentEntity.entityLocation.getExpandedSystemId() : null;
   }

   public void setEncoding(String var1) throws IOException {
      if (this.fCurrentEntity.stream != null && (this.fCurrentEntity.encoding == null || !this.fCurrentEntity.encoding.equals(var1))) {
         if (this.fCurrentEntity.encoding != null && this.fCurrentEntity.encoding.startsWith("UTF-16")) {
            String var2 = var1.toUpperCase(Locale.ENGLISH);
            if (var2.equals("UTF-16")) {
               return;
            }

            if (var2.equals("ISO-10646-UCS-4")) {
               if (this.fCurrentEntity.encoding.equals("UTF-16BE")) {
                  this.fCurrentEntity.reader = new UCSReader(this.fCurrentEntity.stream, (short)8);
               } else {
                  this.fCurrentEntity.reader = new UCSReader(this.fCurrentEntity.stream, (short)4);
               }

               return;
            }

            if (var2.equals("ISO-10646-UCS-2")) {
               if (this.fCurrentEntity.encoding.equals("UTF-16BE")) {
                  this.fCurrentEntity.reader = new UCSReader(this.fCurrentEntity.stream, (short)2);
               } else {
                  this.fCurrentEntity.reader = new UCSReader(this.fCurrentEntity.stream, (short)1);
               }

               return;
            }
         }

         this.fCurrentEntity.setReader(this.fCurrentEntity.stream, var1, (Boolean)null);
         this.fCurrentEntity.encoding = var1;
      }

   }

   public void setXMLVersion(String var1) {
      this.fCurrentEntity.xmlVersion = var1;
   }

   public boolean isExternal() {
      return this.fCurrentEntity.isExternal();
   }

   public int peekChar() throws IOException {
      if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
         this.load(0, true);
      }

      char var1 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
      if (this.fCurrentEntity.isExternal()) {
         return var1 != '\r' ? var1 : 10;
      } else {
         return var1;
      }
   }

   public int scanChar() throws IOException {
      if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
         this.load(0, true);
      }

      char var1 = this.fCurrentEntity.ch[this.fCurrentEntity.position++];
      boolean var2 = false;
      if (var1 == '\n' || var1 == '\r' && (var2 = this.fCurrentEntity.isExternal())) {
         ++this.fCurrentEntity.lineNumber;
         this.fCurrentEntity.columnNumber = 1;
         if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.fCurrentEntity.ch[0] = (char)var1;
            this.load(1, false);
         }

         if (var1 == '\r' && var2) {
            if (this.fCurrentEntity.ch[this.fCurrentEntity.position++] != '\n') {
               --this.fCurrentEntity.position;
            }

            var1 = '\n';
         }
      }

      ++this.fCurrentEntity.columnNumber;
      return var1;
   }

   public String scanNmtoken() throws IOException {
      if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
         this.load(0, true);
      }

      int var1 = this.fCurrentEntity.position;

      int var2;
      while(XMLChar.isName(this.fCurrentEntity.ch[this.fCurrentEntity.position])) {
         if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
            var2 = this.fCurrentEntity.position - var1;
            if (var2 == this.fCurrentEntity.ch.length) {
               char[] var3 = new char[this.fCurrentEntity.ch.length << 1];
               System.arraycopy(this.fCurrentEntity.ch, var1, var3, 0, var2);
               this.fCurrentEntity.ch = var3;
            } else {
               System.arraycopy(this.fCurrentEntity.ch, var1, this.fCurrentEntity.ch, 0, var2);
            }

            var1 = 0;
            if (this.load(var2, false)) {
               break;
            }
         }
      }

      var2 = this.fCurrentEntity.position - var1;
      XMLEntityManager.ScannedEntity var10000 = this.fCurrentEntity;
      var10000.columnNumber += var2;
      String var4 = null;
      if (var2 > 0) {
         var4 = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, var1, var2);
      }

      return var4;
   }

   public String scanName() throws IOException {
      if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
         this.load(0, true);
      }

      int var1 = this.fCurrentEntity.position;
      int var2;
      if (XMLChar.isNameStart(this.fCurrentEntity.ch[var1])) {
         if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[var1];
            var1 = 0;
            if (this.load(1, false)) {
               ++this.fCurrentEntity.columnNumber;
               String var4 = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 1);
               return var4;
            }
         }

         while(XMLChar.isName(this.fCurrentEntity.ch[this.fCurrentEntity.position])) {
            if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
               var2 = this.fCurrentEntity.position - var1;
               if (var2 == this.fCurrentEntity.ch.length) {
                  char[] var3 = new char[this.fCurrentEntity.ch.length << 1];
                  System.arraycopy(this.fCurrentEntity.ch, var1, var3, 0, var2);
                  this.fCurrentEntity.ch = var3;
               } else {
                  System.arraycopy(this.fCurrentEntity.ch, var1, this.fCurrentEntity.ch, 0, var2);
               }

               var1 = 0;
               if (this.load(var2, false)) {
                  break;
               }
            }
         }
      }

      var2 = this.fCurrentEntity.position - var1;
      XMLEntityManager.ScannedEntity var10000 = this.fCurrentEntity;
      var10000.columnNumber += var2;
      String var5 = null;
      if (var2 > 0) {
         var5 = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, var1, var2);
      }

      return var5;
   }

   public String scanNCName() throws IOException {
      if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
         this.load(0, true);
      }

      int var1 = this.fCurrentEntity.position;
      int var2;
      if (XMLChar.isNCNameStart(this.fCurrentEntity.ch[var1])) {
         if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[var1];
            var1 = 0;
            if (this.load(1, false)) {
               ++this.fCurrentEntity.columnNumber;
               String var4 = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 1);
               return var4;
            }
         }

         while(XMLChar.isNCName(this.fCurrentEntity.ch[this.fCurrentEntity.position])) {
            if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
               var2 = this.fCurrentEntity.position - var1;
               if (var2 == this.fCurrentEntity.ch.length) {
                  char[] var3 = new char[this.fCurrentEntity.ch.length << 1];
                  System.arraycopy(this.fCurrentEntity.ch, var1, var3, 0, var2);
                  this.fCurrentEntity.ch = var3;
               } else {
                  System.arraycopy(this.fCurrentEntity.ch, var1, this.fCurrentEntity.ch, 0, var2);
               }

               var1 = 0;
               if (this.load(var2, false)) {
                  break;
               }
            }
         }
      }

      var2 = this.fCurrentEntity.position - var1;
      XMLEntityManager.ScannedEntity var10000 = this.fCurrentEntity;
      var10000.columnNumber += var2;
      String var5 = null;
      if (var2 > 0) {
         var5 = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, var1, var2);
      }

      return var5;
   }

   public boolean scanQName(QName var1) throws IOException {
      if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
         this.load(0, true);
      }

      int var2 = this.fCurrentEntity.position;
      if (XMLChar.isNCNameStart(this.fCurrentEntity.ch[var2])) {
         if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[var2];
            var2 = 0;
            if (this.load(1, false)) {
               ++this.fCurrentEntity.columnNumber;
               String var11 = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 1);
               var1.setValues((String)null, var11, var11, (String)null);
               return true;
            }
         }

         int var3 = -1;

         int var4;
         while(XMLChar.isName(this.fCurrentEntity.ch[this.fCurrentEntity.position])) {
            var4 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
            if (var4 == 58) {
               if (var3 != -1) {
                  break;
               }

               var3 = this.fCurrentEntity.position;
            }

            if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
               int var5 = this.fCurrentEntity.position - var2;
               if (var5 == this.fCurrentEntity.ch.length) {
                  char[] var6 = new char[this.fCurrentEntity.ch.length << 1];
                  System.arraycopy(this.fCurrentEntity.ch, var2, var6, 0, var5);
                  this.fCurrentEntity.ch = var6;
               } else {
                  System.arraycopy(this.fCurrentEntity.ch, var2, this.fCurrentEntity.ch, 0, var5);
               }

               if (var3 != -1) {
                  var3 -= var2;
               }

               var2 = 0;
               if (this.load(var5, false)) {
                  break;
               }
            }
         }

         var4 = this.fCurrentEntity.position - var2;
         XMLEntityManager.ScannedEntity var10000 = this.fCurrentEntity;
         var10000.columnNumber += var4;
         if (var4 > 0) {
            String var12 = null;
            String var13 = null;
            String var7 = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, var2, var4);
            if (var3 != -1) {
               int var8 = var3 - var2;
               var12 = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, var2, var8);
               int var9 = var4 - var8 - 1;
               int var10 = var3 + 1;
               if (!XMLChar.isNCNameStart(this.fCurrentEntity.ch[var10])) {
                  this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "IllegalQName", (Object[])null, (short)2);
               }

               var13 = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, var10, var9);
            } else {
               var13 = var7;
            }

            var1.setValues(var12, var13, var7, (String)null);
            return true;
         }
      }

      return false;
   }

   public int scanContent(XMLString var1) throws IOException {
      if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
         this.load(0, true);
      } else if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
         this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[this.fCurrentEntity.count - 1];
         this.load(1, false);
         this.fCurrentEntity.position = 0;
         this.fCurrentEntity.startPosition = 0;
      }

      int var2 = this.fCurrentEntity.position;
      int var3 = this.fCurrentEntity.ch[var2];
      int var4 = 0;
      boolean var5 = this.fCurrentEntity.isExternal();
      XMLEntityManager.ScannedEntity var10000;
      int var6;
      if (var3 == 10 || var3 == 13 && var5) {
         do {
            var3 = this.fCurrentEntity.ch[this.fCurrentEntity.position++];
            if (var3 == 13 && var5) {
               ++var4;
               ++this.fCurrentEntity.lineNumber;
               this.fCurrentEntity.columnNumber = 1;
               if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                  var2 = 0;
                  var10000 = this.fCurrentEntity;
                  var10000.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
                  this.fCurrentEntity.position = var4;
                  this.fCurrentEntity.startPosition = var4;
                  if (this.load(var4, false)) {
                     break;
                  }
               }

               if (this.fCurrentEntity.ch[this.fCurrentEntity.position] == '\n') {
                  ++this.fCurrentEntity.position;
                  ++var2;
               } else {
                  ++var4;
               }
            } else {
               if (var3 != 10) {
                  --this.fCurrentEntity.position;
                  break;
               }

               ++var4;
               ++this.fCurrentEntity.lineNumber;
               this.fCurrentEntity.columnNumber = 1;
               if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                  var2 = 0;
                  var10000 = this.fCurrentEntity;
                  var10000.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
                  this.fCurrentEntity.position = var4;
                  this.fCurrentEntity.startPosition = var4;
                  if (this.load(var4, false)) {
                     break;
                  }
               }
            }
         } while(this.fCurrentEntity.position < this.fCurrentEntity.count - 1);

         for(var6 = var2; var6 < this.fCurrentEntity.position; ++var6) {
            this.fCurrentEntity.ch[var6] = '\n';
         }

         int var7 = this.fCurrentEntity.position - var2;
         if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
            var1.setValues(this.fCurrentEntity.ch, var2, var7);
            return -1;
         }
      }

      while(this.fCurrentEntity.position < this.fCurrentEntity.count) {
         var3 = this.fCurrentEntity.ch[this.fCurrentEntity.position++];
         if (!XMLChar.isContent(var3)) {
            --this.fCurrentEntity.position;
            break;
         }
      }

      var6 = this.fCurrentEntity.position - var2;
      var10000 = this.fCurrentEntity;
      var10000.columnNumber += var6 - var4;
      var1.setValues(this.fCurrentEntity.ch, var2, var6);
      if (this.fCurrentEntity.position != this.fCurrentEntity.count) {
         var3 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
         if (var3 == 13 && var5) {
            var3 = 10;
         }
      } else {
         var3 = -1;
      }

      return var3;
   }

   public int scanLiteral(int var1, XMLString var2) throws IOException {
      if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
         this.load(0, true);
      } else if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
         this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[this.fCurrentEntity.count - 1];
         this.load(1, false);
         this.fCurrentEntity.position = 0;
         this.fCurrentEntity.startPosition = 0;
      }

      int var3 = this.fCurrentEntity.position;
      int var4 = this.fCurrentEntity.ch[var3];
      int var5 = 0;
      boolean var6 = this.fCurrentEntity.isExternal();
      XMLEntityManager.ScannedEntity var10000;
      int var7;
      if (var4 == 10 || var4 == 13 && var6) {
         do {
            var4 = this.fCurrentEntity.ch[this.fCurrentEntity.position++];
            if (var4 == 13 && var6) {
               ++var5;
               ++this.fCurrentEntity.lineNumber;
               this.fCurrentEntity.columnNumber = 1;
               if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                  var3 = 0;
                  var10000 = this.fCurrentEntity;
                  var10000.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
                  this.fCurrentEntity.position = var5;
                  this.fCurrentEntity.startPosition = var5;
                  if (this.load(var5, false)) {
                     break;
                  }
               }

               if (this.fCurrentEntity.ch[this.fCurrentEntity.position] == '\n') {
                  ++this.fCurrentEntity.position;
                  ++var3;
               } else {
                  ++var5;
               }
            } else {
               if (var4 != 10) {
                  --this.fCurrentEntity.position;
                  break;
               }

               ++var5;
               ++this.fCurrentEntity.lineNumber;
               this.fCurrentEntity.columnNumber = 1;
               if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                  var3 = 0;
                  var10000 = this.fCurrentEntity;
                  var10000.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
                  this.fCurrentEntity.position = var5;
                  this.fCurrentEntity.startPosition = var5;
                  if (this.load(var5, false)) {
                     break;
                  }
               }
            }
         } while(this.fCurrentEntity.position < this.fCurrentEntity.count - 1);

         for(var7 = var3; var7 < this.fCurrentEntity.position; ++var7) {
            this.fCurrentEntity.ch[var7] = '\n';
         }

         int var8 = this.fCurrentEntity.position - var3;
         if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
            var2.setValues(this.fCurrentEntity.ch, var3, var8);
            return -1;
         }
      }

      while(this.fCurrentEntity.position < this.fCurrentEntity.count) {
         var4 = this.fCurrentEntity.ch[this.fCurrentEntity.position++];
         if (var4 == var1 && (!this.fCurrentEntity.literal || var6) || var4 == 37 || !XMLChar.isContent(var4)) {
            --this.fCurrentEntity.position;
            break;
         }
      }

      var7 = this.fCurrentEntity.position - var3;
      var10000 = this.fCurrentEntity;
      var10000.columnNumber += var7 - var5;
      var2.setValues(this.fCurrentEntity.ch, var3, var7);
      if (this.fCurrentEntity.position != this.fCurrentEntity.count) {
         var4 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
         if (var4 == var1 && this.fCurrentEntity.literal) {
            var4 = -1;
         }
      } else {
         var4 = -1;
      }

      return var4;
   }

   public boolean scanData(String var1, XMLStringBuffer var2) throws IOException {
      boolean var3 = false;
      int var4 = var1.length();
      char var5 = var1.charAt(0);
      boolean var6 = this.fCurrentEntity.isExternal();
      if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
         this.load(0, true);
      }

      for(boolean var7 = false; this.fCurrentEntity.position > this.fCurrentEntity.count - var4 && !var7; this.fCurrentEntity.startPosition = 0) {
         System.arraycopy(this.fCurrentEntity.ch, this.fCurrentEntity.position, this.fCurrentEntity.ch, 0, this.fCurrentEntity.count - this.fCurrentEntity.position);
         var7 = this.load(this.fCurrentEntity.count - this.fCurrentEntity.position, false);
         this.fCurrentEntity.position = 0;
      }

      int var8;
      XMLEntityManager.ScannedEntity var10000;
      if (this.fCurrentEntity.position > this.fCurrentEntity.count - var4) {
         var8 = this.fCurrentEntity.count - this.fCurrentEntity.position;
         var2.append(this.fCurrentEntity.ch, this.fCurrentEntity.position, var8);
         var10000 = this.fCurrentEntity;
         var10000.columnNumber += this.fCurrentEntity.count;
         var10000 = this.fCurrentEntity;
         var10000.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
         this.fCurrentEntity.position = this.fCurrentEntity.count;
         this.fCurrentEntity.startPosition = this.fCurrentEntity.count;
         this.load(0, true);
         return false;
      } else {
         var8 = this.fCurrentEntity.position;
         char var9 = this.fCurrentEntity.ch[var8];
         int var10 = 0;
         int var11;
         int var12;
         if (var9 == '\n' || var9 == '\r' && var6) {
            do {
               var9 = this.fCurrentEntity.ch[this.fCurrentEntity.position++];
               if (var9 == '\r' && var6) {
                  ++var10;
                  ++this.fCurrentEntity.lineNumber;
                  this.fCurrentEntity.columnNumber = 1;
                  if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                     var8 = 0;
                     var10000 = this.fCurrentEntity;
                     var10000.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
                     this.fCurrentEntity.position = var10;
                     this.fCurrentEntity.startPosition = var10;
                     if (this.load(var10, false)) {
                        break;
                     }
                  }

                  if (this.fCurrentEntity.ch[this.fCurrentEntity.position] == '\n') {
                     ++this.fCurrentEntity.position;
                     ++var8;
                  } else {
                     ++var10;
                  }
               } else {
                  if (var9 != '\n') {
                     --this.fCurrentEntity.position;
                     break;
                  }

                  ++var10;
                  ++this.fCurrentEntity.lineNumber;
                  this.fCurrentEntity.columnNumber = 1;
                  if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                     var8 = 0;
                     var10000 = this.fCurrentEntity;
                     var10000.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
                     this.fCurrentEntity.position = var10;
                     this.fCurrentEntity.startPosition = var10;
                     this.fCurrentEntity.count = var10;
                     if (this.load(var10, false)) {
                        break;
                     }
                  }
               }
            } while(this.fCurrentEntity.position < this.fCurrentEntity.count - 1);

            for(var11 = var8; var11 < this.fCurrentEntity.position; ++var11) {
               this.fCurrentEntity.ch[var11] = '\n';
            }

            var12 = this.fCurrentEntity.position - var8;
            if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
               var2.append(this.fCurrentEntity.ch, var8, var12);
               return true;
            }
         }

         label92:
         while(this.fCurrentEntity.position < this.fCurrentEntity.count) {
            var9 = this.fCurrentEntity.ch[this.fCurrentEntity.position++];
            if (var9 == var5) {
               var11 = this.fCurrentEntity.position - 1;

               for(var12 = 1; var12 < var4; ++var12) {
                  if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                     var10000 = this.fCurrentEntity;
                     var10000.position -= var12;
                     break label92;
                  }

                  var9 = this.fCurrentEntity.ch[this.fCurrentEntity.position++];
                  if (var1.charAt(var12) != var9) {
                     --this.fCurrentEntity.position;
                     break;
                  }
               }

               if (this.fCurrentEntity.position == var11 + var4) {
                  var3 = true;
                  break;
               }
            } else {
               if (var9 == '\n' || var6 && var9 == '\r') {
                  --this.fCurrentEntity.position;
                  break;
               }

               if (XMLChar.isInvalid(var9)) {
                  --this.fCurrentEntity.position;
                  var11 = this.fCurrentEntity.position - var8;
                  var10000 = this.fCurrentEntity;
                  var10000.columnNumber += var11 - var10;
                  var2.append(this.fCurrentEntity.ch, var8, var11);
                  return true;
               }
            }
         }

         var11 = this.fCurrentEntity.position - var8;
         var10000 = this.fCurrentEntity;
         var10000.columnNumber += var11 - var10;
         if (var3) {
            var11 -= var4;
         }

         var2.append(this.fCurrentEntity.ch, var8, var11);
         return !var3;
      }
   }

   public boolean skipChar(int var1) throws IOException {
      if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
         this.load(0, true);
      }

      char var2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
      if (var2 == var1) {
         ++this.fCurrentEntity.position;
         if (var1 == 10) {
            ++this.fCurrentEntity.lineNumber;
            this.fCurrentEntity.columnNumber = 1;
         } else {
            ++this.fCurrentEntity.columnNumber;
         }

         return true;
      } else if (var1 == 10 && var2 == '\r' && this.fCurrentEntity.isExternal()) {
         if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.fCurrentEntity.ch[0] = (char)var2;
            this.load(1, false);
         }

         ++this.fCurrentEntity.position;
         if (this.fCurrentEntity.ch[this.fCurrentEntity.position] == '\n') {
            ++this.fCurrentEntity.position;
         }

         ++this.fCurrentEntity.lineNumber;
         this.fCurrentEntity.columnNumber = 1;
         return true;
      } else {
         return false;
      }
   }

   public boolean skipSpaces() throws IOException {
      if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
         this.load(0, true);
      }

      char var1 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
      if (!XMLChar.isSpace(var1)) {
         return false;
      } else {
         boolean var2 = this.fCurrentEntity.isExternal();

         do {
            boolean var3 = false;
            if (var1 != '\n' && (!var2 || var1 != '\r')) {
               ++this.fCurrentEntity.columnNumber;
            } else {
               ++this.fCurrentEntity.lineNumber;
               this.fCurrentEntity.columnNumber = 1;
               if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
                  this.fCurrentEntity.ch[0] = (char)var1;
                  var3 = this.load(1, true);
                  if (!var3) {
                     this.fCurrentEntity.position = 0;
                     this.fCurrentEntity.startPosition = 0;
                  }
               }

               if (var1 == '\r' && var2 && this.fCurrentEntity.ch[++this.fCurrentEntity.position] != '\n') {
                  --this.fCurrentEntity.position;
               }
            }

            if (!var3) {
               ++this.fCurrentEntity.position;
            }

            if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
               this.load(0, true);
            }
         } while(XMLChar.isSpace(var1 = this.fCurrentEntity.ch[this.fCurrentEntity.position]));

         return true;
      }
   }

   public boolean skipDeclSpaces() throws IOException {
      if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
         this.load(0, true);
      }

      char var1 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
      if (!XMLChar.isSpace(var1)) {
         return false;
      } else {
         boolean var2 = this.fCurrentEntity.isExternal();

         do {
            boolean var3 = false;
            if (var1 != '\n' && (!var2 || var1 != '\r')) {
               ++this.fCurrentEntity.columnNumber;
            } else {
               ++this.fCurrentEntity.lineNumber;
               this.fCurrentEntity.columnNumber = 1;
               if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
                  this.fCurrentEntity.ch[0] = (char)var1;
                  var3 = this.load(1, true);
                  if (!var3) {
                     this.fCurrentEntity.position = 0;
                     this.fCurrentEntity.startPosition = 0;
                  }
               }

               if (var1 == '\r' && var2 && this.fCurrentEntity.ch[++this.fCurrentEntity.position] != '\n') {
                  --this.fCurrentEntity.position;
               }
            }

            if (!var3) {
               ++this.fCurrentEntity.position;
            }

            if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
               this.load(0, true);
            }
         } while(XMLChar.isSpace(var1 = this.fCurrentEntity.ch[this.fCurrentEntity.position]));

         return true;
      }
   }

   public boolean skipString(String var1) throws IOException {
      if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
         this.load(0, true);
      }

      int var2 = var1.length();

      XMLEntityManager.ScannedEntity var10000;
      for(int var3 = 0; var3 < var2; ++var3) {
         char var4 = this.fCurrentEntity.ch[this.fCurrentEntity.position++];
         if (var4 != var1.charAt(var3)) {
            var10000 = this.fCurrentEntity;
            var10000.position -= var3 + 1;
            return false;
         }

         if (var3 < var2 - 1 && this.fCurrentEntity.position == this.fCurrentEntity.count) {
            System.arraycopy(this.fCurrentEntity.ch, this.fCurrentEntity.count - var3 - 1, this.fCurrentEntity.ch, 0, var3 + 1);
            if (this.load(var3 + 1, false)) {
               var10000 = this.fCurrentEntity;
               var10000.startPosition -= var3 + 1;
               var10000 = this.fCurrentEntity;
               var10000.position -= var3 + 1;
               return false;
            }
         }
      }

      var10000 = this.fCurrentEntity;
      var10000.columnNumber += var2;
      return true;
   }

   public String getPublicId() {
      return this.fCurrentEntity != null && this.fCurrentEntity.entityLocation != null ? this.fCurrentEntity.entityLocation.getPublicId() : null;
   }

   public String getExpandedSystemId() {
      if (this.fCurrentEntity != null) {
         return this.fCurrentEntity.entityLocation != null && this.fCurrentEntity.entityLocation.getExpandedSystemId() != null ? this.fCurrentEntity.entityLocation.getExpandedSystemId() : this.fCurrentEntity.getExpandedSystemId();
      } else {
         return null;
      }
   }

   public String getLiteralSystemId() {
      if (this.fCurrentEntity != null) {
         return this.fCurrentEntity.entityLocation != null && this.fCurrentEntity.entityLocation.getLiteralSystemId() != null ? this.fCurrentEntity.entityLocation.getLiteralSystemId() : this.fCurrentEntity.getLiteralSystemId();
      } else {
         return null;
      }
   }

   public int getLineNumber() {
      if (this.fCurrentEntity != null) {
         return this.fCurrentEntity.isExternal() ? this.fCurrentEntity.lineNumber : this.fCurrentEntity.getLineNumber();
      } else {
         return -1;
      }
   }

   public int getColumnNumber() {
      if (this.fCurrentEntity != null) {
         return this.fCurrentEntity.isExternal() ? this.fCurrentEntity.columnNumber : this.fCurrentEntity.getColumnNumber();
      } else {
         return -1;
      }
   }

   public int getCharacterOffset() {
      if (this.fCurrentEntity != null) {
         return this.fCurrentEntity.isExternal() ? this.fCurrentEntity.baseCharOffset + (this.fCurrentEntity.position - this.fCurrentEntity.startPosition) : this.fCurrentEntity.getCharacterOffset();
      } else {
         return -1;
      }
   }

   public String getEncoding() {
      if (this.fCurrentEntity != null) {
         return this.fCurrentEntity.isExternal() ? this.fCurrentEntity.encoding : this.fCurrentEntity.getEncoding();
      } else {
         return null;
      }
   }

   public String getXMLVersion() {
      if (this.fCurrentEntity != null) {
         return this.fCurrentEntity.isExternal() ? this.fCurrentEntity.xmlVersion : this.fCurrentEntity.getXMLVersion();
      } else {
         return null;
      }
   }

   public void setCurrentEntity(XMLEntityManager.ScannedEntity var1) {
      this.fCurrentEntity = var1;
   }

   public void setBufferSize(int var1) {
      this.fBufferSize = var1;
   }

   public void reset(SymbolTable var1, XMLEntityManager var2, XMLErrorReporter var3) {
      this.fCurrentEntity = null;
      this.fSymbolTable = var1;
      this.fEntityManager = var2;
      this.fErrorReporter = var3;
   }

   final boolean load(int var1, boolean var2) throws IOException {
      XMLEntityManager.ScannedEntity var10000 = this.fCurrentEntity;
      var10000.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
      int var3 = this.fCurrentEntity.mayReadChunks ? this.fCurrentEntity.ch.length - var1 : 64;
      int var4 = this.fCurrentEntity.reader.read(this.fCurrentEntity.ch, var1, var3);
      boolean var5 = false;
      if (var4 != -1) {
         if (var4 != 0) {
            this.fCurrentEntity.count = var4 + var1;
            this.fCurrentEntity.position = var1;
            this.fCurrentEntity.startPosition = var1;
         }
      } else {
         this.fCurrentEntity.count = var1;
         this.fCurrentEntity.position = var1;
         this.fCurrentEntity.startPosition = var1;
         var5 = true;
         if (var2) {
            this.fEntityManager.endEntity();
            if (this.fCurrentEntity == null) {
               throw new EOFException();
            }

            if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
               this.load(0, true);
            }
         }
      }

      return var5;
   }
}

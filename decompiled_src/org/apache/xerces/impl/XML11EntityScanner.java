package org.apache.xerces.impl;

import java.io.IOException;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLString;

public class XML11EntityScanner extends XMLEntityScanner {
   public int peekChar() throws IOException {
      if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
         this.load(0, true);
      }

      char var1 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
      if (!super.fCurrentEntity.isExternal()) {
         return var1;
      } else {
         return var1 != '\r' && var1 != 133 && var1 != 8232 ? var1 : 10;
      }
   }

   public int scanChar() throws IOException {
      if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
         this.load(0, true);
      }

      char var1 = super.fCurrentEntity.ch[super.fCurrentEntity.position++];
      boolean var2 = false;
      if (var1 == '\n' || (var1 == '\r' || var1 == 133 || var1 == 8232) && (var2 = super.fCurrentEntity.isExternal())) {
         ++super.fCurrentEntity.lineNumber;
         super.fCurrentEntity.columnNumber = 1;
         if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
            super.fCurrentEntity.ch[0] = (char)var1;
            this.load(1, false);
         }

         if (var1 == '\r' && var2) {
            char var3 = super.fCurrentEntity.ch[super.fCurrentEntity.position++];
            if (var3 != '\n' && var3 != 133) {
               --super.fCurrentEntity.position;
            }
         }

         var1 = '\n';
      }

      ++super.fCurrentEntity.columnNumber;
      return var1;
   }

   public String scanNmtoken() throws IOException {
      if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
         this.load(0, true);
      }

      int var1 = super.fCurrentEntity.position;

      int var2;
      while(true) {
         var2 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
         int var3;
         char[] var4;
         if (XML11Char.isXML11Name(var2)) {
            if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
               var3 = super.fCurrentEntity.position - var1;
               if (var3 == super.fCurrentEntity.ch.length) {
                  var4 = new char[super.fCurrentEntity.ch.length << 1];
                  System.arraycopy(super.fCurrentEntity.ch, var1, var4, 0, var3);
                  super.fCurrentEntity.ch = var4;
               } else {
                  System.arraycopy(super.fCurrentEntity.ch, var1, super.fCurrentEntity.ch, 0, var3);
               }

               var1 = 0;
               if (this.load(var3, false)) {
                  break;
               }
            }
         } else {
            if (!XML11Char.isXML11NameHighSurrogate(var2)) {
               break;
            }

            if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
               var3 = super.fCurrentEntity.position - var1;
               if (var3 == super.fCurrentEntity.ch.length) {
                  var4 = new char[super.fCurrentEntity.ch.length << 1];
                  System.arraycopy(super.fCurrentEntity.ch, var1, var4, 0, var3);
                  super.fCurrentEntity.ch = var4;
               } else {
                  System.arraycopy(super.fCurrentEntity.ch, var1, super.fCurrentEntity.ch, 0, var3);
               }

               var1 = 0;
               if (this.load(var3, false)) {
                  --super.fCurrentEntity.startPosition;
                  --super.fCurrentEntity.position;
                  break;
               }
            }

            char var6 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
            if (!XMLChar.isLowSurrogate(var6) || !XML11Char.isXML11Name(XMLChar.supplemental((char)var2, var6))) {
               --super.fCurrentEntity.position;
               break;
            }

            if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
               int var8 = super.fCurrentEntity.position - var1;
               if (var8 == super.fCurrentEntity.ch.length) {
                  char[] var5 = new char[super.fCurrentEntity.ch.length << 1];
                  System.arraycopy(super.fCurrentEntity.ch, var1, var5, 0, var8);
                  super.fCurrentEntity.ch = var5;
               } else {
                  System.arraycopy(super.fCurrentEntity.ch, var1, super.fCurrentEntity.ch, 0, var8);
               }

               var1 = 0;
               if (this.load(var8, false)) {
                  break;
               }
            }
         }
      }

      var2 = super.fCurrentEntity.position - var1;
      XMLEntityManager.ScannedEntity var10000 = super.fCurrentEntity;
      var10000.columnNumber += var2;
      String var7 = null;
      if (var2 > 0) {
         var7 = super.fSymbolTable.addSymbol(super.fCurrentEntity.ch, var1, var2);
      }

      return var7;
   }

   public String scanName() throws IOException {
      if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
         this.load(0, true);
      }

      int var1 = super.fCurrentEntity.position;
      char var2 = super.fCurrentEntity.ch[var1];
      XMLEntityManager.ScannedEntity var10000;
      int var3;
      String var4;
      if (XML11Char.isXML11NameStart(var2)) {
         if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
            super.fCurrentEntity.ch[0] = var2;
            var1 = 0;
            if (this.load(1, false)) {
               ++super.fCurrentEntity.columnNumber;
               String var8 = super.fSymbolTable.addSymbol(super.fCurrentEntity.ch, 0, 1);
               return var8;
            }
         }
      } else {
         label99: {
            if (XML11Char.isXML11NameHighSurrogate(var2)) {
               if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
                  super.fCurrentEntity.ch[0] = var2;
                  var1 = 0;
                  if (this.load(1, false)) {
                     --super.fCurrentEntity.position;
                     --super.fCurrentEntity.startPosition;
                     return null;
                  }
               }

               var3 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
               if (XMLChar.isLowSurrogate(var3) && XML11Char.isXML11NameStart(XMLChar.supplemental(var2, (char)var3))) {
                  if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
                     super.fCurrentEntity.ch[0] = var2;
                     super.fCurrentEntity.ch[1] = (char)var3;
                     var1 = 0;
                     if (this.load(2, false)) {
                        var10000 = super.fCurrentEntity;
                        var10000.columnNumber += 2;
                        var4 = super.fSymbolTable.addSymbol(super.fCurrentEntity.ch, 0, 2);
                        return var4;
                     }
                  }
                  break label99;
               }

               --super.fCurrentEntity.position;
               return null;
            }

            return null;
         }
      }

      while(true) {
         var2 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
         char[] var7;
         if (XML11Char.isXML11Name(var2)) {
            if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
               var3 = super.fCurrentEntity.position - var1;
               if (var3 == super.fCurrentEntity.ch.length) {
                  var7 = new char[super.fCurrentEntity.ch.length << 1];
                  System.arraycopy(super.fCurrentEntity.ch, var1, var7, 0, var3);
                  super.fCurrentEntity.ch = var7;
               } else {
                  System.arraycopy(super.fCurrentEntity.ch, var1, super.fCurrentEntity.ch, 0, var3);
               }

               var1 = 0;
               if (this.load(var3, false)) {
                  break;
               }
            }
         } else {
            if (!XML11Char.isXML11NameHighSurrogate(var2)) {
               break;
            }

            if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
               var3 = super.fCurrentEntity.position - var1;
               if (var3 == super.fCurrentEntity.ch.length) {
                  var7 = new char[super.fCurrentEntity.ch.length << 1];
                  System.arraycopy(super.fCurrentEntity.ch, var1, var7, 0, var3);
                  super.fCurrentEntity.ch = var7;
               } else {
                  System.arraycopy(super.fCurrentEntity.ch, var1, super.fCurrentEntity.ch, 0, var3);
               }

               var1 = 0;
               if (this.load(var3, false)) {
                  --super.fCurrentEntity.position;
                  --super.fCurrentEntity.startPosition;
                  break;
               }
            }

            char var6 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
            if (!XMLChar.isLowSurrogate(var6) || !XML11Char.isXML11Name(XMLChar.supplemental(var2, var6))) {
               --super.fCurrentEntity.position;
               break;
            }

            if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
               int var9 = super.fCurrentEntity.position - var1;
               if (var9 == super.fCurrentEntity.ch.length) {
                  char[] var5 = new char[super.fCurrentEntity.ch.length << 1];
                  System.arraycopy(super.fCurrentEntity.ch, var1, var5, 0, var9);
                  super.fCurrentEntity.ch = var5;
               } else {
                  System.arraycopy(super.fCurrentEntity.ch, var1, super.fCurrentEntity.ch, 0, var9);
               }

               var1 = 0;
               if (this.load(var9, false)) {
                  break;
               }
            }
         }
      }

      var3 = super.fCurrentEntity.position - var1;
      var10000 = super.fCurrentEntity;
      var10000.columnNumber += var3;
      var4 = null;
      if (var3 > 0) {
         var4 = super.fSymbolTable.addSymbol(super.fCurrentEntity.ch, var1, var3);
      }

      return var4;
   }

   public String scanNCName() throws IOException {
      if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
         this.load(0, true);
      }

      int var1 = super.fCurrentEntity.position;
      char var2 = super.fCurrentEntity.ch[var1];
      XMLEntityManager.ScannedEntity var10000;
      int var3;
      String var4;
      if (XML11Char.isXML11NCNameStart(var2)) {
         if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
            super.fCurrentEntity.ch[0] = var2;
            var1 = 0;
            if (this.load(1, false)) {
               ++super.fCurrentEntity.columnNumber;
               String var8 = super.fSymbolTable.addSymbol(super.fCurrentEntity.ch, 0, 1);
               return var8;
            }
         }
      } else {
         label99: {
            if (XML11Char.isXML11NameHighSurrogate(var2)) {
               if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
                  super.fCurrentEntity.ch[0] = var2;
                  var1 = 0;
                  if (this.load(1, false)) {
                     --super.fCurrentEntity.position;
                     --super.fCurrentEntity.startPosition;
                     return null;
                  }
               }

               var3 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
               if (XMLChar.isLowSurrogate(var3) && XML11Char.isXML11NCNameStart(XMLChar.supplemental(var2, (char)var3))) {
                  if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
                     super.fCurrentEntity.ch[0] = var2;
                     super.fCurrentEntity.ch[1] = (char)var3;
                     var1 = 0;
                     if (this.load(2, false)) {
                        var10000 = super.fCurrentEntity;
                        var10000.columnNumber += 2;
                        var4 = super.fSymbolTable.addSymbol(super.fCurrentEntity.ch, 0, 2);
                        return var4;
                     }
                  }
                  break label99;
               }

               --super.fCurrentEntity.position;
               return null;
            }

            return null;
         }
      }

      while(true) {
         var2 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
         char[] var7;
         if (XML11Char.isXML11NCName(var2)) {
            if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
               var3 = super.fCurrentEntity.position - var1;
               if (var3 == super.fCurrentEntity.ch.length) {
                  var7 = new char[super.fCurrentEntity.ch.length << 1];
                  System.arraycopy(super.fCurrentEntity.ch, var1, var7, 0, var3);
                  super.fCurrentEntity.ch = var7;
               } else {
                  System.arraycopy(super.fCurrentEntity.ch, var1, super.fCurrentEntity.ch, 0, var3);
               }

               var1 = 0;
               if (this.load(var3, false)) {
                  break;
               }
            }
         } else {
            if (!XML11Char.isXML11NameHighSurrogate(var2)) {
               break;
            }

            if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
               var3 = super.fCurrentEntity.position - var1;
               if (var3 == super.fCurrentEntity.ch.length) {
                  var7 = new char[super.fCurrentEntity.ch.length << 1];
                  System.arraycopy(super.fCurrentEntity.ch, var1, var7, 0, var3);
                  super.fCurrentEntity.ch = var7;
               } else {
                  System.arraycopy(super.fCurrentEntity.ch, var1, super.fCurrentEntity.ch, 0, var3);
               }

               var1 = 0;
               if (this.load(var3, false)) {
                  --super.fCurrentEntity.startPosition;
                  --super.fCurrentEntity.position;
                  break;
               }
            }

            char var6 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
            if (!XMLChar.isLowSurrogate(var6) || !XML11Char.isXML11NCName(XMLChar.supplemental(var2, var6))) {
               --super.fCurrentEntity.position;
               break;
            }

            if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
               int var9 = super.fCurrentEntity.position - var1;
               if (var9 == super.fCurrentEntity.ch.length) {
                  char[] var5 = new char[super.fCurrentEntity.ch.length << 1];
                  System.arraycopy(super.fCurrentEntity.ch, var1, var5, 0, var9);
                  super.fCurrentEntity.ch = var5;
               } else {
                  System.arraycopy(super.fCurrentEntity.ch, var1, super.fCurrentEntity.ch, 0, var9);
               }

               var1 = 0;
               if (this.load(var9, false)) {
                  break;
               }
            }
         }
      }

      var3 = super.fCurrentEntity.position - var1;
      var10000 = super.fCurrentEntity;
      var10000.columnNumber += var3;
      var4 = null;
      if (var3 > 0) {
         var4 = super.fSymbolTable.addSymbol(super.fCurrentEntity.ch, var1, var3);
      }

      return var4;
   }

   public boolean scanQName(QName var1) throws IOException {
      if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
         this.load(0, true);
      }

      int var2 = super.fCurrentEntity.position;
      char var3 = super.fCurrentEntity.ch[var2];
      int var4;
      XMLEntityManager.ScannedEntity var10000;
      if (XML11Char.isXML11NCNameStart(var3)) {
         if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
            super.fCurrentEntity.ch[0] = var3;
            var2 = 0;
            if (this.load(1, false)) {
               ++super.fCurrentEntity.columnNumber;
               String var15 = super.fSymbolTable.addSymbol(super.fCurrentEntity.ch, 0, 1);
               var1.setValues((String)null, var15, var15, (String)null);
               return true;
            }
         }
      } else {
         if (!XML11Char.isXML11NameHighSurrogate(var3)) {
            return false;
         }

         if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
            super.fCurrentEntity.ch[0] = var3;
            var2 = 0;
            if (this.load(1, false)) {
               --super.fCurrentEntity.startPosition;
               --super.fCurrentEntity.position;
               return false;
            }
         }

         var4 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
         if (!XMLChar.isLowSurrogate(var4) || !XML11Char.isXML11NCNameStart(XMLChar.supplemental(var3, (char)var4))) {
            --super.fCurrentEntity.position;
            return false;
         }

         if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
            super.fCurrentEntity.ch[0] = var3;
            super.fCurrentEntity.ch[1] = (char)var4;
            var2 = 0;
            if (this.load(2, false)) {
               var10000 = super.fCurrentEntity;
               var10000.columnNumber += 2;
               String var5 = super.fSymbolTable.addSymbol(super.fCurrentEntity.ch, 0, 2);
               var1.setValues((String)null, var5, var5, (String)null);
               return true;
            }
         }
      }

      var4 = -1;
      boolean var13 = false;

      int var6;
      while(true) {
         var3 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
         char[] var7;
         if (XML11Char.isXML11Name(var3)) {
            if (var3 == ':') {
               if (var4 != -1) {
                  break;
               }

               var4 = super.fCurrentEntity.position;
            }

            if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
               var6 = super.fCurrentEntity.position - var2;
               if (var6 == super.fCurrentEntity.ch.length) {
                  var7 = new char[super.fCurrentEntity.ch.length << 1];
                  System.arraycopy(super.fCurrentEntity.ch, var2, var7, 0, var6);
                  super.fCurrentEntity.ch = var7;
               } else {
                  System.arraycopy(super.fCurrentEntity.ch, var2, super.fCurrentEntity.ch, 0, var6);
               }

               if (var4 != -1) {
                  var4 -= var2;
               }

               var2 = 0;
               if (this.load(var6, false)) {
                  break;
               }
            }
         } else {
            if (!XML11Char.isXML11NameHighSurrogate(var3)) {
               break;
            }

            if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
               var6 = super.fCurrentEntity.position - var2;
               if (var6 == super.fCurrentEntity.ch.length) {
                  var7 = new char[super.fCurrentEntity.ch.length << 1];
                  System.arraycopy(super.fCurrentEntity.ch, var2, var7, 0, var6);
                  super.fCurrentEntity.ch = var7;
               } else {
                  System.arraycopy(super.fCurrentEntity.ch, var2, super.fCurrentEntity.ch, 0, var6);
               }

               if (var4 != -1) {
                  var4 -= var2;
               }

               var2 = 0;
               if (this.load(var6, false)) {
                  var13 = true;
                  --super.fCurrentEntity.startPosition;
                  --super.fCurrentEntity.position;
                  break;
               }
            }

            char var14 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
            if (!XMLChar.isLowSurrogate(var14) || !XML11Char.isXML11Name(XMLChar.supplemental(var3, var14))) {
               var13 = true;
               --super.fCurrentEntity.position;
               break;
            }

            if (++super.fCurrentEntity.position == super.fCurrentEntity.count) {
               int var16 = super.fCurrentEntity.position - var2;
               if (var16 == super.fCurrentEntity.ch.length) {
                  char[] var8 = new char[super.fCurrentEntity.ch.length << 1];
                  System.arraycopy(super.fCurrentEntity.ch, var2, var8, 0, var16);
                  super.fCurrentEntity.ch = var8;
               } else {
                  System.arraycopy(super.fCurrentEntity.ch, var2, super.fCurrentEntity.ch, 0, var16);
               }

               if (var4 != -1) {
                  var4 -= var2;
               }

               var2 = 0;
               if (this.load(var16, false)) {
                  break;
               }
            }
         }
      }

      var6 = super.fCurrentEntity.position - var2;
      var10000 = super.fCurrentEntity;
      var10000.columnNumber += var6;
      if (var6 <= 0) {
         return false;
      } else {
         String var17 = null;
         String var18 = null;
         String var9 = super.fSymbolTable.addSymbol(super.fCurrentEntity.ch, var2, var6);
         if (var4 != -1) {
            int var10 = var4 - var2;
            var17 = super.fSymbolTable.addSymbol(super.fCurrentEntity.ch, var2, var10);
            int var11 = var6 - var10 - 1;
            int var12 = var4 + 1;
            if (!XML11Char.isXML11NCNameStart(super.fCurrentEntity.ch[var12]) && (!XML11Char.isXML11NameHighSurrogate(super.fCurrentEntity.ch[var12]) || var13)) {
               super.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "IllegalQName", (Object[])null, (short)2);
            }

            var18 = super.fSymbolTable.addSymbol(super.fCurrentEntity.ch, var4 + 1, var11);
         } else {
            var18 = var9;
         }

         var1.setValues(var17, var18, var9, (String)null);
         return true;
      }
   }

   public int scanContent(XMLString var1) throws IOException {
      if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
         this.load(0, true);
      } else if (super.fCurrentEntity.position == super.fCurrentEntity.count - 1) {
         super.fCurrentEntity.ch[0] = super.fCurrentEntity.ch[super.fCurrentEntity.count - 1];
         this.load(1, false);
         super.fCurrentEntity.position = 0;
         super.fCurrentEntity.startPosition = 0;
      }

      int var2 = super.fCurrentEntity.position;
      int var3 = super.fCurrentEntity.ch[var2];
      int var4 = 0;
      boolean var5 = super.fCurrentEntity.isExternal();
      XMLEntityManager.ScannedEntity var10000;
      int var6;
      if (var3 == 10 || (var3 == 13 || var3 == 133 || var3 == 8232) && var5) {
         do {
            var3 = super.fCurrentEntity.ch[super.fCurrentEntity.position++];
            if (var3 == 13 && var5) {
               ++var4;
               ++super.fCurrentEntity.lineNumber;
               super.fCurrentEntity.columnNumber = 1;
               if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
                  var2 = 0;
                  var10000 = super.fCurrentEntity;
                  var10000.baseCharOffset += super.fCurrentEntity.position - super.fCurrentEntity.startPosition;
                  super.fCurrentEntity.position = var4;
                  super.fCurrentEntity.startPosition = var4;
                  if (this.load(var4, false)) {
                     break;
                  }
               }

               var6 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
               if (var6 != 10 && var6 != 133) {
                  ++var4;
               } else {
                  ++super.fCurrentEntity.position;
                  ++var2;
               }
            } else {
               if (var3 != 10 && (var3 != 133 && var3 != 8232 || !var5)) {
                  --super.fCurrentEntity.position;
                  break;
               }

               ++var4;
               ++super.fCurrentEntity.lineNumber;
               super.fCurrentEntity.columnNumber = 1;
               if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
                  var2 = 0;
                  var10000 = super.fCurrentEntity;
                  var10000.baseCharOffset += super.fCurrentEntity.position - super.fCurrentEntity.startPosition;
                  super.fCurrentEntity.position = var4;
                  super.fCurrentEntity.startPosition = var4;
                  if (this.load(var4, false)) {
                     break;
                  }
               }
            }
         } while(super.fCurrentEntity.position < super.fCurrentEntity.count - 1);

         for(var6 = var2; var6 < super.fCurrentEntity.position; ++var6) {
            super.fCurrentEntity.ch[var6] = '\n';
         }

         int var7 = super.fCurrentEntity.position - var2;
         if (super.fCurrentEntity.position == super.fCurrentEntity.count - 1) {
            var1.setValues(super.fCurrentEntity.ch, var2, var7);
            return -1;
         }
      }

      if (!var5) {
         while(super.fCurrentEntity.position < super.fCurrentEntity.count) {
            var3 = super.fCurrentEntity.ch[super.fCurrentEntity.position++];
            if (!XML11Char.isXML11InternalEntityContent(var3)) {
               --super.fCurrentEntity.position;
               break;
            }
         }
      } else {
         label137: {
            do {
               if (super.fCurrentEntity.position >= super.fCurrentEntity.count) {
                  break label137;
               }

               var3 = super.fCurrentEntity.ch[super.fCurrentEntity.position++];
            } while(XML11Char.isXML11Content(var3) && var3 != 133 && var3 != 8232);

            --super.fCurrentEntity.position;
         }
      }

      var6 = super.fCurrentEntity.position - var2;
      var10000 = super.fCurrentEntity;
      var10000.columnNumber += var6 - var4;
      var1.setValues(super.fCurrentEntity.ch, var2, var6);
      if (super.fCurrentEntity.position != super.fCurrentEntity.count) {
         var3 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
         if ((var3 == 13 || var3 == 133 || var3 == 8232) && var5) {
            var3 = 10;
         }
      } else {
         var3 = -1;
      }

      return var3;
   }

   public int scanLiteral(int var1, XMLString var2) throws IOException {
      if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
         this.load(0, true);
      } else if (super.fCurrentEntity.position == super.fCurrentEntity.count - 1) {
         super.fCurrentEntity.ch[0] = super.fCurrentEntity.ch[super.fCurrentEntity.count - 1];
         this.load(1, false);
         super.fCurrentEntity.startPosition = 0;
         super.fCurrentEntity.position = 0;
      }

      int var3 = super.fCurrentEntity.position;
      int var4 = super.fCurrentEntity.ch[var3];
      int var5 = 0;
      boolean var6 = super.fCurrentEntity.isExternal();
      XMLEntityManager.ScannedEntity var10000;
      int var7;
      if (var4 == 10 || (var4 == 13 || var4 == 133 || var4 == 8232) && var6) {
         do {
            var4 = super.fCurrentEntity.ch[super.fCurrentEntity.position++];
            if (var4 == 13 && var6) {
               ++var5;
               ++super.fCurrentEntity.lineNumber;
               super.fCurrentEntity.columnNumber = 1;
               if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
                  var3 = 0;
                  var10000 = super.fCurrentEntity;
                  var10000.baseCharOffset += super.fCurrentEntity.position - super.fCurrentEntity.startPosition;
                  super.fCurrentEntity.position = var5;
                  super.fCurrentEntity.startPosition = var5;
                  if (this.load(var5, false)) {
                     break;
                  }
               }

               var7 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
               if (var7 != 10 && var7 != 133) {
                  ++var5;
               } else {
                  ++super.fCurrentEntity.position;
                  ++var3;
               }
            } else {
               if (var4 != 10 && (var4 != 133 && var4 != 8232 || !var6)) {
                  --super.fCurrentEntity.position;
                  break;
               }

               ++var5;
               ++super.fCurrentEntity.lineNumber;
               super.fCurrentEntity.columnNumber = 1;
               if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
                  var3 = 0;
                  var10000 = super.fCurrentEntity;
                  var10000.baseCharOffset += super.fCurrentEntity.position - super.fCurrentEntity.startPosition;
                  super.fCurrentEntity.position = var5;
                  super.fCurrentEntity.startPosition = var5;
                  if (this.load(var5, false)) {
                     break;
                  }
               }
            }
         } while(super.fCurrentEntity.position < super.fCurrentEntity.count - 1);

         for(var7 = var3; var7 < super.fCurrentEntity.position; ++var7) {
            super.fCurrentEntity.ch[var7] = '\n';
         }

         int var8 = super.fCurrentEntity.position - var3;
         if (super.fCurrentEntity.position == super.fCurrentEntity.count - 1) {
            var2.setValues(super.fCurrentEntity.ch, var3, var8);
            return -1;
         }
      }

      if (var6) {
         label80: {
            do {
               if (super.fCurrentEntity.position >= super.fCurrentEntity.count) {
                  break label80;
               }

               var4 = super.fCurrentEntity.ch[super.fCurrentEntity.position++];
            } while(var4 != var1 && var4 != 37 && XML11Char.isXML11Content(var4) && var4 != 133 && var4 != 8232);

            --super.fCurrentEntity.position;
         }
      } else {
         label143: {
            do {
               if (super.fCurrentEntity.position >= super.fCurrentEntity.count) {
                  break label143;
               }

               var4 = super.fCurrentEntity.ch[super.fCurrentEntity.position++];
            } while((var4 != var1 || super.fCurrentEntity.literal) && var4 != 37 && XML11Char.isXML11InternalEntityContent(var4));

            --super.fCurrentEntity.position;
         }
      }

      var7 = super.fCurrentEntity.position - var3;
      var10000 = super.fCurrentEntity;
      var10000.columnNumber += var7 - var5;
      var2.setValues(super.fCurrentEntity.ch, var3, var7);
      if (super.fCurrentEntity.position != super.fCurrentEntity.count) {
         var4 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
         if (var4 == var1 && super.fCurrentEntity.literal) {
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
      boolean var6 = super.fCurrentEntity.isExternal();

      do {
         if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
            this.load(0, true);
         }

         for(boolean var7 = false; super.fCurrentEntity.position >= super.fCurrentEntity.count - var4 && !var7; super.fCurrentEntity.startPosition = 0) {
            System.arraycopy(super.fCurrentEntity.ch, super.fCurrentEntity.position, super.fCurrentEntity.ch, 0, super.fCurrentEntity.count - super.fCurrentEntity.position);
            var7 = this.load(super.fCurrentEntity.count - super.fCurrentEntity.position, false);
            super.fCurrentEntity.position = 0;
         }

         int var8;
         XMLEntityManager.ScannedEntity var10000;
         if (super.fCurrentEntity.position >= super.fCurrentEntity.count - var4) {
            var8 = super.fCurrentEntity.count - super.fCurrentEntity.position;
            var2.append(super.fCurrentEntity.ch, super.fCurrentEntity.position, var8);
            var10000 = super.fCurrentEntity;
            var10000.columnNumber += super.fCurrentEntity.count;
            var10000 = super.fCurrentEntity;
            var10000.baseCharOffset += super.fCurrentEntity.position - super.fCurrentEntity.startPosition;
            super.fCurrentEntity.position = super.fCurrentEntity.count;
            super.fCurrentEntity.startPosition = super.fCurrentEntity.count;
            this.load(0, true);
            return false;
         }

         var8 = super.fCurrentEntity.position;
         char var9 = super.fCurrentEntity.ch[var8];
         int var10 = 0;
         int var11;
         int var12;
         if (var9 == '\n' || (var9 == '\r' || var9 == 133 || var9 == 8232) && var6) {
            do {
               var9 = super.fCurrentEntity.ch[super.fCurrentEntity.position++];
               if (var9 == '\r' && var6) {
                  ++var10;
                  ++super.fCurrentEntity.lineNumber;
                  super.fCurrentEntity.columnNumber = 1;
                  if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
                     var8 = 0;
                     var10000 = super.fCurrentEntity;
                     var10000.baseCharOffset += super.fCurrentEntity.position - super.fCurrentEntity.startPosition;
                     super.fCurrentEntity.position = var10;
                     super.fCurrentEntity.startPosition = var10;
                     if (this.load(var10, false)) {
                        break;
                     }
                  }

                  var11 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
                  if (var11 != 10 && var11 != 133) {
                     ++var10;
                  } else {
                     ++super.fCurrentEntity.position;
                     ++var8;
                  }
               } else {
                  if (var9 != '\n' && (var9 != 133 && var9 != 8232 || !var6)) {
                     --super.fCurrentEntity.position;
                     break;
                  }

                  ++var10;
                  ++super.fCurrentEntity.lineNumber;
                  super.fCurrentEntity.columnNumber = 1;
                  if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
                     var8 = 0;
                     var10000 = super.fCurrentEntity;
                     var10000.baseCharOffset += super.fCurrentEntity.position - super.fCurrentEntity.startPosition;
                     super.fCurrentEntity.position = var10;
                     super.fCurrentEntity.startPosition = var10;
                     super.fCurrentEntity.count = var10;
                     if (this.load(var10, false)) {
                        break;
                     }
                  }
               }
            } while(super.fCurrentEntity.position < super.fCurrentEntity.count - 1);

            for(var11 = var8; var11 < super.fCurrentEntity.position; ++var11) {
               super.fCurrentEntity.ch[var11] = '\n';
            }

            var12 = super.fCurrentEntity.position - var8;
            if (super.fCurrentEntity.position == super.fCurrentEntity.count - 1) {
               var2.append(super.fCurrentEntity.ch, var8, var12);
               return true;
            }
         }

         if (var6) {
            label180:
            while(true) {
               while(true) {
                  if (super.fCurrentEntity.position >= super.fCurrentEntity.count) {
                     break label180;
                  }

                  var9 = super.fCurrentEntity.ch[super.fCurrentEntity.position++];
                  if (var9 != var5) {
                     if (var9 == '\n' || var9 == '\r' || var9 == 133 || var9 == 8232) {
                        --super.fCurrentEntity.position;
                        break label180;
                     }

                     if (!XML11Char.isXML11ValidLiteral(var9)) {
                        --super.fCurrentEntity.position;
                        var11 = super.fCurrentEntity.position - var8;
                        var10000 = super.fCurrentEntity;
                        var10000.columnNumber += var11 - var10;
                        var2.append(super.fCurrentEntity.ch, var8, var11);
                        return true;
                     }
                  } else {
                     var11 = super.fCurrentEntity.position - 1;

                     for(var12 = 1; var12 < var4; ++var12) {
                        if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
                           var10000 = super.fCurrentEntity;
                           var10000.position -= var12;
                           break label180;
                        }

                        var9 = super.fCurrentEntity.ch[super.fCurrentEntity.position++];
                        if (var1.charAt(var12) != var9) {
                           --super.fCurrentEntity.position;
                           break;
                        }
                     }

                     if (super.fCurrentEntity.position == var11 + var4) {
                        var3 = true;
                        break label180;
                     }
                  }
               }
            }
         } else {
            label199:
            while(true) {
               while(true) {
                  if (super.fCurrentEntity.position >= super.fCurrentEntity.count) {
                     break label199;
                  }

                  var9 = super.fCurrentEntity.ch[super.fCurrentEntity.position++];
                  if (var9 == var5) {
                     var11 = super.fCurrentEntity.position - 1;

                     for(var12 = 1; var12 < var4; ++var12) {
                        if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
                           var10000 = super.fCurrentEntity;
                           var10000.position -= var12;
                           break label199;
                        }

                        var9 = super.fCurrentEntity.ch[super.fCurrentEntity.position++];
                        if (var1.charAt(var12) != var9) {
                           --super.fCurrentEntity.position;
                           break;
                        }
                     }

                     if (super.fCurrentEntity.position == var11 + var4) {
                        var3 = true;
                        break label199;
                     }
                  } else {
                     if (var9 == '\n') {
                        --super.fCurrentEntity.position;
                        break label199;
                     }

                     if (!XML11Char.isXML11Valid(var9)) {
                        --super.fCurrentEntity.position;
                        var11 = super.fCurrentEntity.position - var8;
                        var10000 = super.fCurrentEntity;
                        var10000.columnNumber += var11 - var10;
                        var2.append(super.fCurrentEntity.ch, var8, var11);
                        return true;
                     }
                  }
               }
            }
         }

         var11 = super.fCurrentEntity.position - var8;
         var10000 = super.fCurrentEntity;
         var10000.columnNumber += var11 - var10;
         if (var3) {
            var11 -= var4;
         }

         var2.append(super.fCurrentEntity.ch, var8, var11);
      } while(!var3);

      return !var3;
   }

   public boolean skipChar(int var1) throws IOException {
      if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
         this.load(0, true);
      }

      char var2 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
      if (var2 == var1) {
         ++super.fCurrentEntity.position;
         if (var1 == 10) {
            ++super.fCurrentEntity.lineNumber;
            super.fCurrentEntity.columnNumber = 1;
         } else {
            ++super.fCurrentEntity.columnNumber;
         }

         return true;
      } else if (var1 == 10 && (var2 == 8232 || var2 == 133) && super.fCurrentEntity.isExternal()) {
         ++super.fCurrentEntity.position;
         ++super.fCurrentEntity.lineNumber;
         super.fCurrentEntity.columnNumber = 1;
         return true;
      } else if (var1 == 10 && var2 == '\r' && super.fCurrentEntity.isExternal()) {
         if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
            super.fCurrentEntity.ch[0] = (char)var2;
            this.load(1, false);
         }

         char var3 = super.fCurrentEntity.ch[++super.fCurrentEntity.position];
         if (var3 == '\n' || var3 == 133) {
            ++super.fCurrentEntity.position;
         }

         ++super.fCurrentEntity.lineNumber;
         super.fCurrentEntity.columnNumber = 1;
         return true;
      } else {
         return false;
      }
   }

   public boolean skipSpaces() throws IOException {
      if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
         this.load(0, true);
      }

      char var1 = super.fCurrentEntity.ch[super.fCurrentEntity.position];
      boolean var2;
      if (super.fCurrentEntity.isExternal()) {
         if (XML11Char.isXML11Space(var1)) {
            do {
               var2 = false;
               if (var1 != '\n' && var1 != '\r' && var1 != 133 && var1 != 8232) {
                  ++super.fCurrentEntity.columnNumber;
               } else {
                  ++super.fCurrentEntity.lineNumber;
                  super.fCurrentEntity.columnNumber = 1;
                  if (super.fCurrentEntity.position == super.fCurrentEntity.count - 1) {
                     super.fCurrentEntity.ch[0] = (char)var1;
                     var2 = this.load(1, true);
                     if (!var2) {
                        super.fCurrentEntity.startPosition = 0;
                        super.fCurrentEntity.position = 0;
                     }
                  }

                  if (var1 == '\r') {
                     char var3 = super.fCurrentEntity.ch[++super.fCurrentEntity.position];
                     if (var3 != '\n' && var3 != 133) {
                        --super.fCurrentEntity.position;
                     }
                  }
               }

               if (!var2) {
                  ++super.fCurrentEntity.position;
               }

               if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
                  this.load(0, true);
               }
            } while(XML11Char.isXML11Space(var1 = super.fCurrentEntity.ch[super.fCurrentEntity.position]));

            return true;
         }
      } else if (XMLChar.isSpace(var1)) {
         do {
            var2 = false;
            if (var1 == '\n') {
               ++super.fCurrentEntity.lineNumber;
               super.fCurrentEntity.columnNumber = 1;
               if (super.fCurrentEntity.position == super.fCurrentEntity.count - 1) {
                  super.fCurrentEntity.ch[0] = (char)var1;
                  var2 = this.load(1, true);
                  if (!var2) {
                     super.fCurrentEntity.startPosition = 0;
                     super.fCurrentEntity.position = 0;
                  }
               }
            } else {
               ++super.fCurrentEntity.columnNumber;
            }

            if (!var2) {
               ++super.fCurrentEntity.position;
            }

            if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
               this.load(0, true);
            }
         } while(XMLChar.isSpace(var1 = super.fCurrentEntity.ch[super.fCurrentEntity.position]));

         return true;
      }

      return false;
   }

   public boolean skipString(String var1) throws IOException {
      if (super.fCurrentEntity.position == super.fCurrentEntity.count) {
         this.load(0, true);
      }

      int var2 = var1.length();

      XMLEntityManager.ScannedEntity var10000;
      for(int var3 = 0; var3 < var2; ++var3) {
         char var4 = super.fCurrentEntity.ch[super.fCurrentEntity.position++];
         if (var4 != var1.charAt(var3)) {
            var10000 = super.fCurrentEntity;
            var10000.position -= var3 + 1;
            return false;
         }

         if (var3 < var2 - 1 && super.fCurrentEntity.position == super.fCurrentEntity.count) {
            System.arraycopy(super.fCurrentEntity.ch, super.fCurrentEntity.count - var3 - 1, super.fCurrentEntity.ch, 0, var3 + 1);
            if (this.load(var3 + 1, false)) {
               var10000 = super.fCurrentEntity;
               var10000.startPosition -= var3 + 1;
               var10000 = super.fCurrentEntity;
               var10000.position -= var3 + 1;
               return false;
            }
         }
      }

      var10000 = super.fCurrentEntity;
      var10000.columnNumber += var2;
      return true;
   }
}

package org.apache.xerces.impl.xpath;

import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;

public class XPath {
   private static final boolean DEBUG_ALL = false;
   private static final boolean DEBUG_XPATH_PARSE = false;
   private static final boolean DEBUG_ANY = false;
   protected String fExpression;
   protected SymbolTable fSymbolTable;
   protected LocationPath[] fLocationPaths;

   public XPath(String var1, SymbolTable var2, NamespaceContext var3) throws XPathException {
      this.fExpression = var1;
      this.fSymbolTable = var2;
      this.parseExpression(var3);
   }

   public LocationPath[] getLocationPaths() {
      LocationPath[] var1 = new LocationPath[this.fLocationPaths.length];

      for(int var2 = 0; var2 < this.fLocationPaths.length; ++var2) {
         var1[var2] = (LocationPath)this.fLocationPaths[var2].clone();
      }

      return var1;
   }

   public LocationPath getLocationPath() {
      return (LocationPath)this.fLocationPaths[0].clone();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < this.fLocationPaths.length; ++var2) {
         if (var2 > 0) {
            var1.append("|");
         }

         var1.append(this.fLocationPaths[var2].toString());
      }

      return var1.toString();
   }

   private static void check(boolean var0) throws XPathException {
      if (!var0) {
         throw new XPathException("c-general-xpath");
      }
   }

   private LocationPath buildLocationPath(Vector var1) throws XPathException {
      int var2 = var1.size();
      check(var2 != 0);
      Step[] var3 = new Step[var2];
      var1.copyInto(var3);
      var1.removeAllElements();
      return new LocationPath(var3);
   }

   private void parseExpression(NamespaceContext var1) throws XPathException {
      Tokens var2 = new Tokens(this.fSymbolTable);
      Scanner var3 = new Scanner(this.fSymbolTable) {
         protected void addToken(Tokens var1, int var2) throws XPathException {
            if (var2 != 6 && var2 != 11 && var2 != 21 && var2 != 4 && var2 != 9 && var2 != 10 && var2 != 22 && var2 != 23) {
               throw new XPathException("c-general-xpath");
            } else {
               super.addToken(var1, var2);
            }
         }
      };
      int var4 = this.fExpression.length();
      boolean var5 = var3.scanExpr(this.fSymbolTable, var2, this.fExpression, 0, var4);
      if (!var5) {
         throw new XPathException("c-general-xpath");
      } else {
         Vector var6 = new Vector();
         Vector var7 = new Vector();
         boolean var8 = true;

         while(var2.hasMore()) {
            int var9 = var2.nextToken();
            Step var10;
            switch (var9) {
               case 4:
                  check(var8);
                  var8 = false;
                  if (var6.size() == 0) {
                     Axis var13 = new Axis((short)3);
                     NodeTest var11 = new NodeTest((short)3);
                     Step var12 = new Step(var13, var11);
                     var6.addElement(var12);
                     if (var2.hasMore() && var2.peekToken() == 22) {
                        var2.nextToken();
                        var13 = new Axis((short)4);
                        var11 = new NodeTest((short)3);
                        var12 = new Step(var13, var11);
                        var6.addElement(var12);
                        var8 = true;
                     }
                  }
                  break;
               case 5:
               case 7:
               case 8:
               case 12:
               case 13:
               case 14:
               case 15:
               case 16:
               case 17:
               case 18:
               case 19:
               case 20:
               default:
                  throw new InternalError();
               case 6:
                  check(var8);
                  var10 = new Step(new Axis((short)2), this.parseNodeTest(var2.nextToken(), var2, var1));
                  var6.addElement(var10);
                  var8 = false;
                  break;
               case 9:
               case 10:
               case 11:
                  check(var8);
                  var10 = new Step(new Axis((short)1), this.parseNodeTest(var9, var2, var1));
                  var6.addElement(var10);
                  var8 = false;
                  break;
               case 21:
                  check(!var8);
                  var8 = true;
                  break;
               case 22:
                  throw new XPathException("c-general-xpath");
               case 23:
                  check(!var8);
                  var7.addElement(this.buildLocationPath(var6));
                  var8 = true;
            }
         }

         check(!var8);
         var7.addElement(this.buildLocationPath(var6));
         this.fLocationPaths = new LocationPath[var7.size()];
         var7.copyInto(this.fLocationPaths);
      }
   }

   private NodeTest parseNodeTest(int var1, Tokens var2, NamespaceContext var3) throws XPathException {
      switch (var1) {
         case 9:
            return new NodeTest((short)2);
         case 10:
         case 11:
            String var4 = var2.nextTokenAsString();
            String var5 = null;
            if (var3 != null && var4 != XMLSymbols.EMPTY_STRING) {
               var5 = var3.getURI(var4);
            }

            if (var4 != XMLSymbols.EMPTY_STRING && var3 != null && var5 == null) {
               throw new XPathException("c-general-xpath-ns");
            } else {
               if (var1 == 10) {
                  return new NodeTest(var4, var5);
               }

               String var6 = var2.nextTokenAsString();
               String var7 = var4 != XMLSymbols.EMPTY_STRING ? this.fSymbolTable.addSymbol(var4 + ':' + var6) : var6;
               return new NodeTest(new QName(var4, var6, var7, var5));
            }
         default:
            throw new InternalError();
      }
   }

   public static void main(String[] var0) throws Exception {
      for(int var1 = 0; var1 < var0.length; ++var1) {
         String var2 = var0[var1];
         System.out.println("# XPath expression: \"" + var2 + '"');

         try {
            SymbolTable var3 = new SymbolTable();
            XPath var4 = new XPath(var2, var3, (NamespaceContext)null);
            System.out.println("expanded xpath: \"" + var4.toString() + '"');
         } catch (XPathException var5) {
            System.out.println("error: " + var5.getMessage());
         }
      }

   }

   private static class Scanner {
      private static final byte CHARTYPE_INVALID = 0;
      private static final byte CHARTYPE_OTHER = 1;
      private static final byte CHARTYPE_WHITESPACE = 2;
      private static final byte CHARTYPE_EXCLAMATION = 3;
      private static final byte CHARTYPE_QUOTE = 4;
      private static final byte CHARTYPE_DOLLAR = 5;
      private static final byte CHARTYPE_OPEN_PAREN = 6;
      private static final byte CHARTYPE_CLOSE_PAREN = 7;
      private static final byte CHARTYPE_STAR = 8;
      private static final byte CHARTYPE_PLUS = 9;
      private static final byte CHARTYPE_COMMA = 10;
      private static final byte CHARTYPE_MINUS = 11;
      private static final byte CHARTYPE_PERIOD = 12;
      private static final byte CHARTYPE_SLASH = 13;
      private static final byte CHARTYPE_DIGIT = 14;
      private static final byte CHARTYPE_COLON = 15;
      private static final byte CHARTYPE_LESS = 16;
      private static final byte CHARTYPE_EQUAL = 17;
      private static final byte CHARTYPE_GREATER = 18;
      private static final byte CHARTYPE_ATSIGN = 19;
      private static final byte CHARTYPE_LETTER = 20;
      private static final byte CHARTYPE_OPEN_BRACKET = 21;
      private static final byte CHARTYPE_CLOSE_BRACKET = 22;
      private static final byte CHARTYPE_UNDERSCORE = 23;
      private static final byte CHARTYPE_UNION = 24;
      private static final byte CHARTYPE_NONASCII = 25;
      private static final byte[] fASCIICharMap = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 4, 1, 5, 1, 1, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 15, 1, 16, 17, 18, 1, 19, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21, 1, 22, 1, 23, 1, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 1, 24, 1, 1, 1};
      private SymbolTable fSymbolTable;
      private static final String fAndSymbol = "and".intern();
      private static final String fOrSymbol = "or".intern();
      private static final String fModSymbol = "mod".intern();
      private static final String fDivSymbol = "div".intern();
      private static final String fCommentSymbol = "comment".intern();
      private static final String fTextSymbol = "text".intern();
      private static final String fPISymbol = "processing-instruction".intern();
      private static final String fNodeSymbol = "node".intern();
      private static final String fAncestorSymbol = "ancestor".intern();
      private static final String fAncestorOrSelfSymbol = "ancestor-or-self".intern();
      private static final String fAttributeSymbol = "attribute".intern();
      private static final String fChildSymbol = "child".intern();
      private static final String fDescendantSymbol = "descendant".intern();
      private static final String fDescendantOrSelfSymbol = "descendant-or-self".intern();
      private static final String fFollowingSymbol = "following".intern();
      private static final String fFollowingSiblingSymbol = "following-sibling".intern();
      private static final String fNamespaceSymbol = "namespace".intern();
      private static final String fParentSymbol = "parent".intern();
      private static final String fPrecedingSymbol = "preceding".intern();
      private static final String fPrecedingSiblingSymbol = "preceding-sibling".intern();
      private static final String fSelfSymbol = "self".intern();

      public Scanner(SymbolTable var1) {
         this.fSymbolTable = var1;
      }

      public boolean scanExpr(SymbolTable var1, Tokens var2, String var3, int var4, int var5) throws XPathException {
         boolean var9 = false;

         while(var4 != var5) {
            int var10;
            for(var10 = var3.charAt(var4); var10 == 32 || var10 == 10 || var10 == 9 || var10 == 13; var10 = var3.charAt(var4)) {
               ++var4;
               if (var4 == var5) {
                  break;
               }
            }

            if (var4 == var5) {
               break;
            }

            byte var11 = var10 >= 128 ? 25 : fASCIICharMap[var10];
            int var6;
            String var7;
            String var8;
            char var17;
            switch (var11) {
               case 3:
                  ++var4;
                  if (var4 == var5) {
                     return false;
                  }

                  var17 = var3.charAt(var4);
                  if (var17 != '=') {
                     return false;
                  }

                  this.addToken(var2, 27);
                  var9 = false;
                  ++var4;
                  if (var4 == var5) {
                  }
                  break;
               case 4:
                  char var12 = (char)var10;
                  ++var4;
                  if (var4 == var5) {
                     return false;
                  }

                  var17 = var3.charAt(var4);

                  int var13;
                  for(var13 = var4; var17 != var12; var17 = var3.charAt(var4)) {
                     ++var4;
                     if (var4 == var5) {
                        return false;
                     }
                  }

                  int var14 = var4 - var13;
                  this.addToken(var2, 46);
                  var9 = true;
                  var2.addToken(var1.addSymbol(var3.substring(var13, var13 + var14)));
                  ++var4;
                  if (var4 == var5) {
                  }
                  break;
               case 5:
                  ++var4;
                  if (var4 == var5) {
                     return false;
                  }

                  var6 = var4;
                  var4 = this.scanNCName(var3, var5, var4);
                  if (var4 == var6) {
                     return false;
                  }

                  if (var4 < var5) {
                     var10 = var3.charAt(var4);
                  } else {
                     var10 = -1;
                  }

                  var7 = var1.addSymbol(var3.substring(var6, var4));
                  if (var10 != 58) {
                     var8 = XMLSymbols.EMPTY_STRING;
                  } else {
                     var8 = var7;
                     ++var4;
                     if (var4 == var5) {
                        return false;
                     }

                     var6 = var4;
                     var4 = this.scanNCName(var3, var5, var4);
                     if (var4 == var6) {
                        return false;
                     }

                     if (var4 < var5) {
                        var3.charAt(var4);
                     } else {
                        boolean var18 = true;
                     }

                     var7 = var1.addSymbol(var3.substring(var6, var4));
                  }

                  this.addToken(var2, 48);
                  var9 = true;
                  var2.addToken(var8);
                  var2.addToken(var7);
                  break;
               case 6:
                  this.addToken(var2, 0);
                  var9 = false;
                  ++var4;
                  if (var4 == var5) {
                  }
                  break;
               case 7:
                  this.addToken(var2, 1);
                  var9 = true;
                  ++var4;
                  if (var4 == var5) {
                  }
                  break;
               case 8:
                  if (var9) {
                     this.addToken(var2, 20);
                     var9 = false;
                  } else {
                     this.addToken(var2, 9);
                     var9 = true;
                  }

                  ++var4;
                  if (var4 == var5) {
                  }
                  break;
               case 9:
                  this.addToken(var2, 24);
                  var9 = false;
                  ++var4;
                  if (var4 == var5) {
                  }
                  break;
               case 10:
                  this.addToken(var2, 7);
                  var9 = false;
                  ++var4;
                  if (var4 == var5) {
                  }
                  break;
               case 11:
                  this.addToken(var2, 25);
                  var9 = false;
                  ++var4;
                  if (var4 == var5) {
                  }
                  break;
               case 12:
                  if (var4 + 1 == var5) {
                     this.addToken(var2, 4);
                     var9 = true;
                     ++var4;
                  } else {
                     var17 = var3.charAt(var4 + 1);
                     if (var17 == '.') {
                        this.addToken(var2, 5);
                        var9 = true;
                        var4 += 2;
                     } else if (var17 >= '0' && var17 <= '9') {
                        this.addToken(var2, 47);
                        var9 = true;
                        var4 = this.scanNumber(var2, var3, var5, var4);
                     } else {
                        if (var17 != '/') {
                           if (var17 == '|') {
                              this.addToken(var2, 4);
                              var9 = true;
                              ++var4;
                              continue;
                           }

                           if (var17 != ' ' && var17 != '\n' && var17 != '\t' && var17 != '\r') {
                              throw new XPathException("c-general-xpath");
                           }

                           do {
                              ++var4;
                              if (var4 == var5) {
                                 break;
                              }

                              var17 = var3.charAt(var4);
                           } while(var17 == ' ' || var17 == '\n' || var17 == '\t' || var17 == '\r');

                           if (var4 != var5 && var17 != '|') {
                              throw new XPathException("c-general-xpath");
                           }

                           this.addToken(var2, 4);
                           var9 = true;
                           continue;
                        }

                        this.addToken(var2, 4);
                        var9 = true;
                        ++var4;
                     }

                     if (var4 == var5) {
                     }
                  }
                  break;
               case 13:
                  ++var4;
                  if (var4 == var5) {
                     this.addToken(var2, 21);
                     var9 = false;
                  } else {
                     var17 = var3.charAt(var4);
                     if (var17 == '/') {
                        this.addToken(var2, 22);
                        var9 = false;
                        ++var4;
                        if (var4 == var5) {
                        }
                     } else {
                        this.addToken(var2, 21);
                        var9 = false;
                     }
                  }
                  break;
               case 14:
                  this.addToken(var2, 47);
                  var9 = true;
                  var4 = this.scanNumber(var2, var3, var5, var4);
                  break;
               case 15:
                  ++var4;
                  if (var4 == var5) {
                     return false;
                  }

                  var17 = var3.charAt(var4);
                  if (var17 != ':') {
                     return false;
                  }

                  this.addToken(var2, 8);
                  var9 = false;
                  ++var4;
                  if (var4 == var5) {
                  }
                  break;
               case 16:
                  ++var4;
                  if (var4 == var5) {
                     this.addToken(var2, 28);
                     var9 = false;
                  } else {
                     var17 = var3.charAt(var4);
                     if (var17 == '=') {
                        this.addToken(var2, 29);
                        var9 = false;
                        ++var4;
                        if (var4 == var5) {
                        }
                     } else {
                        this.addToken(var2, 28);
                        var9 = false;
                     }
                  }
                  break;
               case 17:
                  this.addToken(var2, 26);
                  var9 = false;
                  ++var4;
                  if (var4 == var5) {
                  }
                  break;
               case 18:
                  ++var4;
                  if (var4 == var5) {
                     this.addToken(var2, 30);
                     var9 = false;
                  } else {
                     var17 = var3.charAt(var4);
                     if (var17 == '=') {
                        this.addToken(var2, 31);
                        var9 = false;
                        ++var4;
                        if (var4 == var5) {
                        }
                     } else {
                        this.addToken(var2, 30);
                        var9 = false;
                     }
                  }
                  break;
               case 19:
                  this.addToken(var2, 6);
                  var9 = false;
                  ++var4;
                  if (var4 == var5) {
                  }
                  break;
               case 20:
               case 23:
               case 25:
                  var6 = var4;
                  var4 = this.scanNCName(var3, var5, var4);
                  if (var4 == var6) {
                     return false;
                  }

                  if (var4 < var5) {
                     var10 = var3.charAt(var4);
                  } else {
                     var10 = -1;
                  }

                  var7 = var1.addSymbol(var3.substring(var6, var4));
                  boolean var15 = false;
                  boolean var16 = false;
                  var8 = XMLSymbols.EMPTY_STRING;
                  if (var10 == 58) {
                     ++var4;
                     if (var4 == var5) {
                        return false;
                     }

                     var10 = var3.charAt(var4);
                     if (var10 == 42) {
                        ++var4;
                        if (var4 < var5) {
                           var10 = var3.charAt(var4);
                        }

                        var15 = true;
                     } else if (var10 == 58) {
                        ++var4;
                        if (var4 < var5) {
                           var10 = var3.charAt(var4);
                        }

                        var16 = true;
                     } else {
                        var8 = var7;
                        var6 = var4;
                        var4 = this.scanNCName(var3, var5, var4);
                        if (var4 == var6) {
                           return false;
                        }

                        if (var4 < var5) {
                           var10 = var3.charAt(var4);
                        } else {
                           var10 = -1;
                        }

                        var7 = var1.addSymbol(var3.substring(var6, var4));
                     }
                  }

                  while(var10 == 32 || var10 == 10 || var10 == 9 || var10 == 13) {
                     ++var4;
                     if (var4 == var5) {
                        break;
                     }

                     var10 = var3.charAt(var4);
                  }

                  if (var9) {
                     if (var7 == fAndSymbol) {
                        this.addToken(var2, 16);
                        var9 = false;
                     } else if (var7 == fOrSymbol) {
                        this.addToken(var2, 17);
                        var9 = false;
                     } else if (var7 == fModSymbol) {
                        this.addToken(var2, 18);
                        var9 = false;
                     } else {
                        if (var7 != fDivSymbol) {
                           return false;
                        }

                        this.addToken(var2, 19);
                        var9 = false;
                     }

                     if (var15) {
                        return false;
                     }

                     if (var16) {
                        return false;
                     }
                  } else if (var10 == 40 && !var15 && !var16) {
                     if (var7 == fCommentSymbol) {
                        this.addToken(var2, 12);
                     } else if (var7 == fTextSymbol) {
                        this.addToken(var2, 13);
                     } else if (var7 == fPISymbol) {
                        this.addToken(var2, 14);
                     } else if (var7 == fNodeSymbol) {
                        this.addToken(var2, 15);
                     } else {
                        this.addToken(var2, 32);
                        var2.addToken(var8);
                        var2.addToken(var7);
                     }

                     this.addToken(var2, 0);
                     var9 = false;
                     ++var4;
                     if (var4 == var5) {
                     }
                  } else if (var16 || var10 == 58 && var4 + 1 < var5 && var3.charAt(var4 + 1) == ':') {
                     if (var7 == fAncestorSymbol) {
                        this.addToken(var2, 33);
                     } else if (var7 == fAncestorOrSelfSymbol) {
                        this.addToken(var2, 34);
                     } else if (var7 == fAttributeSymbol) {
                        this.addToken(var2, 35);
                     } else if (var7 == fChildSymbol) {
                        this.addToken(var2, 36);
                     } else if (var7 == fDescendantSymbol) {
                        this.addToken(var2, 37);
                     } else if (var7 == fDescendantOrSelfSymbol) {
                        this.addToken(var2, 38);
                     } else if (var7 == fFollowingSymbol) {
                        this.addToken(var2, 39);
                     } else if (var7 == fFollowingSiblingSymbol) {
                        this.addToken(var2, 40);
                     } else if (var7 == fNamespaceSymbol) {
                        this.addToken(var2, 41);
                     } else if (var7 == fParentSymbol) {
                        this.addToken(var2, 42);
                     } else if (var7 == fPrecedingSymbol) {
                        this.addToken(var2, 43);
                     } else if (var7 == fPrecedingSiblingSymbol) {
                        this.addToken(var2, 44);
                     } else {
                        if (var7 != fSelfSymbol) {
                           return false;
                        }

                        this.addToken(var2, 45);
                     }

                     if (var15) {
                        return false;
                     }

                     this.addToken(var2, 8);
                     var9 = false;
                     if (!var16) {
                        ++var4;
                        ++var4;
                        if (var4 == var5) {
                        }
                     }
                  } else if (var15) {
                     this.addToken(var2, 10);
                     var9 = true;
                     var2.addToken(var7);
                  } else {
                     this.addToken(var2, 11);
                     var9 = true;
                     var2.addToken(var8);
                     var2.addToken(var7);
                  }
                  break;
               case 21:
                  this.addToken(var2, 2);
                  var9 = false;
                  ++var4;
                  if (var4 == var5) {
                  }
                  break;
               case 22:
                  this.addToken(var2, 3);
                  var9 = true;
                  ++var4;
                  if (var4 == var5) {
                  }
                  break;
               case 24:
                  this.addToken(var2, 23);
                  var9 = false;
                  ++var4;
                  if (var4 == var5) {
                  }
            }
         }

         return true;
      }

      int scanNCName(String var1, int var2, int var3) {
         char var4 = var1.charAt(var3);
         byte var5;
         if (var4 >= 128) {
            if (!XMLChar.isNameStart(var4)) {
               return var3;
            }
         } else {
            var5 = fASCIICharMap[var4];
            if (var5 != 20 && var5 != 23) {
               return var3;
            }
         }

         while(true) {
            ++var3;
            if (var3 >= var2) {
               break;
            }

            var4 = var1.charAt(var3);
            if (var4 >= 128) {
               if (!XMLChar.isName(var4)) {
                  break;
               }
            } else {
               var5 = fASCIICharMap[var4];
               if (var5 != 20 && var5 != 14 && var5 != 12 && var5 != 11 && var5 != 23) {
                  break;
               }
            }
         }

         return var3;
      }

      private int scanNumber(Tokens var1, String var2, int var3, int var4) {
         char var5 = var2.charAt(var4);
         int var6 = 0;

         int var7;
         for(var7 = 0; var5 >= '0' && var5 <= '9'; var5 = var2.charAt(var4)) {
            var6 = var6 * 10 + (var5 - 48);
            ++var4;
            if (var4 == var3) {
               break;
            }
         }

         if (var5 == '.') {
            ++var4;
            if (var4 < var3) {
               for(var5 = var2.charAt(var4); var5 >= '0' && var5 <= '9'; var5 = var2.charAt(var4)) {
                  var7 = var7 * 10 + (var5 - 48);
                  ++var4;
                  if (var4 == var3) {
                     break;
                  }
               }

               if (var7 != 0) {
                  throw new RuntimeException("find a solution!");
               }
            }
         }

         var1.addToken(var6);
         var1.addToken(var7);
         return var4;
      }

      protected void addToken(Tokens var1, int var2) throws XPathException {
         var1.addToken(var2);
      }
   }

   private static final class Tokens {
      static final boolean DUMP_TOKENS = false;
      public static final int EXPRTOKEN_OPEN_PAREN = 0;
      public static final int EXPRTOKEN_CLOSE_PAREN = 1;
      public static final int EXPRTOKEN_OPEN_BRACKET = 2;
      public static final int EXPRTOKEN_CLOSE_BRACKET = 3;
      public static final int EXPRTOKEN_PERIOD = 4;
      public static final int EXPRTOKEN_DOUBLE_PERIOD = 5;
      public static final int EXPRTOKEN_ATSIGN = 6;
      public static final int EXPRTOKEN_COMMA = 7;
      public static final int EXPRTOKEN_DOUBLE_COLON = 8;
      public static final int EXPRTOKEN_NAMETEST_ANY = 9;
      public static final int EXPRTOKEN_NAMETEST_NAMESPACE = 10;
      public static final int EXPRTOKEN_NAMETEST_QNAME = 11;
      public static final int EXPRTOKEN_NODETYPE_COMMENT = 12;
      public static final int EXPRTOKEN_NODETYPE_TEXT = 13;
      public static final int EXPRTOKEN_NODETYPE_PI = 14;
      public static final int EXPRTOKEN_NODETYPE_NODE = 15;
      public static final int EXPRTOKEN_OPERATOR_AND = 16;
      public static final int EXPRTOKEN_OPERATOR_OR = 17;
      public static final int EXPRTOKEN_OPERATOR_MOD = 18;
      public static final int EXPRTOKEN_OPERATOR_DIV = 19;
      public static final int EXPRTOKEN_OPERATOR_MULT = 20;
      public static final int EXPRTOKEN_OPERATOR_SLASH = 21;
      public static final int EXPRTOKEN_OPERATOR_DOUBLE_SLASH = 22;
      public static final int EXPRTOKEN_OPERATOR_UNION = 23;
      public static final int EXPRTOKEN_OPERATOR_PLUS = 24;
      public static final int EXPRTOKEN_OPERATOR_MINUS = 25;
      public static final int EXPRTOKEN_OPERATOR_EQUAL = 26;
      public static final int EXPRTOKEN_OPERATOR_NOT_EQUAL = 27;
      public static final int EXPRTOKEN_OPERATOR_LESS = 28;
      public static final int EXPRTOKEN_OPERATOR_LESS_EQUAL = 29;
      public static final int EXPRTOKEN_OPERATOR_GREATER = 30;
      public static final int EXPRTOKEN_OPERATOR_GREATER_EQUAL = 31;
      public static final int EXPRTOKEN_FUNCTION_NAME = 32;
      public static final int EXPRTOKEN_AXISNAME_ANCESTOR = 33;
      public static final int EXPRTOKEN_AXISNAME_ANCESTOR_OR_SELF = 34;
      public static final int EXPRTOKEN_AXISNAME_ATTRIBUTE = 35;
      public static final int EXPRTOKEN_AXISNAME_CHILD = 36;
      public static final int EXPRTOKEN_AXISNAME_DESCENDANT = 37;
      public static final int EXPRTOKEN_AXISNAME_DESCENDANT_OR_SELF = 38;
      public static final int EXPRTOKEN_AXISNAME_FOLLOWING = 39;
      public static final int EXPRTOKEN_AXISNAME_FOLLOWING_SIBLING = 40;
      public static final int EXPRTOKEN_AXISNAME_NAMESPACE = 41;
      public static final int EXPRTOKEN_AXISNAME_PARENT = 42;
      public static final int EXPRTOKEN_AXISNAME_PRECEDING = 43;
      public static final int EXPRTOKEN_AXISNAME_PRECEDING_SIBLING = 44;
      public static final int EXPRTOKEN_AXISNAME_SELF = 45;
      public static final int EXPRTOKEN_LITERAL = 46;
      public static final int EXPRTOKEN_NUMBER = 47;
      public static final int EXPRTOKEN_VARIABLE_REFERENCE = 48;
      private static final String[] fgTokenNames = new String[]{"EXPRTOKEN_OPEN_PAREN", "EXPRTOKEN_CLOSE_PAREN", "EXPRTOKEN_OPEN_BRACKET", "EXPRTOKEN_CLOSE_BRACKET", "EXPRTOKEN_PERIOD", "EXPRTOKEN_DOUBLE_PERIOD", "EXPRTOKEN_ATSIGN", "EXPRTOKEN_COMMA", "EXPRTOKEN_DOUBLE_COLON", "EXPRTOKEN_NAMETEST_ANY", "EXPRTOKEN_NAMETEST_NAMESPACE", "EXPRTOKEN_NAMETEST_QNAME", "EXPRTOKEN_NODETYPE_COMMENT", "EXPRTOKEN_NODETYPE_TEXT", "EXPRTOKEN_NODETYPE_PI", "EXPRTOKEN_NODETYPE_NODE", "EXPRTOKEN_OPERATOR_AND", "EXPRTOKEN_OPERATOR_OR", "EXPRTOKEN_OPERATOR_MOD", "EXPRTOKEN_OPERATOR_DIV", "EXPRTOKEN_OPERATOR_MULT", "EXPRTOKEN_OPERATOR_SLASH", "EXPRTOKEN_OPERATOR_DOUBLE_SLASH", "EXPRTOKEN_OPERATOR_UNION", "EXPRTOKEN_OPERATOR_PLUS", "EXPRTOKEN_OPERATOR_MINUS", "EXPRTOKEN_OPERATOR_EQUAL", "EXPRTOKEN_OPERATOR_NOT_EQUAL", "EXPRTOKEN_OPERATOR_LESS", "EXPRTOKEN_OPERATOR_LESS_EQUAL", "EXPRTOKEN_OPERATOR_GREATER", "EXPRTOKEN_OPERATOR_GREATER_EQUAL", "EXPRTOKEN_FUNCTION_NAME", "EXPRTOKEN_AXISNAME_ANCESTOR", "EXPRTOKEN_AXISNAME_ANCESTOR_OR_SELF", "EXPRTOKEN_AXISNAME_ATTRIBUTE", "EXPRTOKEN_AXISNAME_CHILD", "EXPRTOKEN_AXISNAME_DESCENDANT", "EXPRTOKEN_AXISNAME_DESCENDANT_OR_SELF", "EXPRTOKEN_AXISNAME_FOLLOWING", "EXPRTOKEN_AXISNAME_FOLLOWING_SIBLING", "EXPRTOKEN_AXISNAME_NAMESPACE", "EXPRTOKEN_AXISNAME_PARENT", "EXPRTOKEN_AXISNAME_PRECEDING", "EXPRTOKEN_AXISNAME_PRECEDING_SIBLING", "EXPRTOKEN_AXISNAME_SELF", "EXPRTOKEN_LITERAL", "EXPRTOKEN_NUMBER", "EXPRTOKEN_VARIABLE_REFERENCE"};
      private static final int INITIAL_TOKEN_COUNT = 256;
      private int[] fTokens = new int[256];
      private int fTokenCount = 0;
      private SymbolTable fSymbolTable;
      private Hashtable fSymbolMapping = new Hashtable();
      private Hashtable fTokenNames = new Hashtable();
      private int fCurrentTokenIndex;

      public Tokens(SymbolTable var1) {
         this.fSymbolTable = var1;
         String[] var2 = new String[]{"ancestor", "ancestor-or-self", "attribute", "child", "descendant", "descendant-or-self", "following", "following-sibling", "namespace", "parent", "preceding", "preceding-sibling", "self"};

         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.fSymbolMapping.put(this.fSymbolTable.addSymbol(var2[var3]), new Integer(var3));
         }

         this.fTokenNames.put(new Integer(0), "EXPRTOKEN_OPEN_PAREN");
         this.fTokenNames.put(new Integer(1), "EXPRTOKEN_CLOSE_PAREN");
         this.fTokenNames.put(new Integer(2), "EXPRTOKEN_OPEN_BRACKET");
         this.fTokenNames.put(new Integer(3), "EXPRTOKEN_CLOSE_BRACKET");
         this.fTokenNames.put(new Integer(4), "EXPRTOKEN_PERIOD");
         this.fTokenNames.put(new Integer(5), "EXPRTOKEN_DOUBLE_PERIOD");
         this.fTokenNames.put(new Integer(6), "EXPRTOKEN_ATSIGN");
         this.fTokenNames.put(new Integer(7), "EXPRTOKEN_COMMA");
         this.fTokenNames.put(new Integer(8), "EXPRTOKEN_DOUBLE_COLON");
         this.fTokenNames.put(new Integer(9), "EXPRTOKEN_NAMETEST_ANY");
         this.fTokenNames.put(new Integer(10), "EXPRTOKEN_NAMETEST_NAMESPACE");
         this.fTokenNames.put(new Integer(11), "EXPRTOKEN_NAMETEST_QNAME");
         this.fTokenNames.put(new Integer(12), "EXPRTOKEN_NODETYPE_COMMENT");
         this.fTokenNames.put(new Integer(13), "EXPRTOKEN_NODETYPE_TEXT");
         this.fTokenNames.put(new Integer(14), "EXPRTOKEN_NODETYPE_PI");
         this.fTokenNames.put(new Integer(15), "EXPRTOKEN_NODETYPE_NODE");
         this.fTokenNames.put(new Integer(16), "EXPRTOKEN_OPERATOR_AND");
         this.fTokenNames.put(new Integer(17), "EXPRTOKEN_OPERATOR_OR");
         this.fTokenNames.put(new Integer(18), "EXPRTOKEN_OPERATOR_MOD");
         this.fTokenNames.put(new Integer(19), "EXPRTOKEN_OPERATOR_DIV");
         this.fTokenNames.put(new Integer(20), "EXPRTOKEN_OPERATOR_MULT");
         this.fTokenNames.put(new Integer(21), "EXPRTOKEN_OPERATOR_SLASH");
         this.fTokenNames.put(new Integer(22), "EXPRTOKEN_OPERATOR_DOUBLE_SLASH");
         this.fTokenNames.put(new Integer(23), "EXPRTOKEN_OPERATOR_UNION");
         this.fTokenNames.put(new Integer(24), "EXPRTOKEN_OPERATOR_PLUS");
         this.fTokenNames.put(new Integer(25), "EXPRTOKEN_OPERATOR_MINUS");
         this.fTokenNames.put(new Integer(26), "EXPRTOKEN_OPERATOR_EQUAL");
         this.fTokenNames.put(new Integer(27), "EXPRTOKEN_OPERATOR_NOT_EQUAL");
         this.fTokenNames.put(new Integer(28), "EXPRTOKEN_OPERATOR_LESS");
         this.fTokenNames.put(new Integer(29), "EXPRTOKEN_OPERATOR_LESS_EQUAL");
         this.fTokenNames.put(new Integer(30), "EXPRTOKEN_OPERATOR_GREATER");
         this.fTokenNames.put(new Integer(31), "EXPRTOKEN_OPERATOR_GREATER_EQUAL");
         this.fTokenNames.put(new Integer(32), "EXPRTOKEN_FUNCTION_NAME");
         this.fTokenNames.put(new Integer(33), "EXPRTOKEN_AXISNAME_ANCESTOR");
         this.fTokenNames.put(new Integer(34), "EXPRTOKEN_AXISNAME_ANCESTOR_OR_SELF");
         this.fTokenNames.put(new Integer(35), "EXPRTOKEN_AXISNAME_ATTRIBUTE");
         this.fTokenNames.put(new Integer(36), "EXPRTOKEN_AXISNAME_CHILD");
         this.fTokenNames.put(new Integer(37), "EXPRTOKEN_AXISNAME_DESCENDANT");
         this.fTokenNames.put(new Integer(38), "EXPRTOKEN_AXISNAME_DESCENDANT_OR_SELF");
         this.fTokenNames.put(new Integer(39), "EXPRTOKEN_AXISNAME_FOLLOWING");
         this.fTokenNames.put(new Integer(40), "EXPRTOKEN_AXISNAME_FOLLOWING_SIBLING");
         this.fTokenNames.put(new Integer(41), "EXPRTOKEN_AXISNAME_NAMESPACE");
         this.fTokenNames.put(new Integer(42), "EXPRTOKEN_AXISNAME_PARENT");
         this.fTokenNames.put(new Integer(43), "EXPRTOKEN_AXISNAME_PRECEDING");
         this.fTokenNames.put(new Integer(44), "EXPRTOKEN_AXISNAME_PRECEDING_SIBLING");
         this.fTokenNames.put(new Integer(45), "EXPRTOKEN_AXISNAME_SELF");
         this.fTokenNames.put(new Integer(46), "EXPRTOKEN_LITERAL");
         this.fTokenNames.put(new Integer(47), "EXPRTOKEN_NUMBER");
         this.fTokenNames.put(new Integer(48), "EXPRTOKEN_VARIABLE_REFERENCE");
      }

      public String getTokenString(int var1) {
         return (String)this.fTokenNames.get(new Integer(var1));
      }

      public void addToken(String var1) {
         Integer var2 = (Integer)this.fTokenNames.get(var1);
         if (var2 == null) {
            var2 = new Integer(this.fTokenNames.size());
            this.fTokenNames.put(var2, var1);
         }

         this.addToken(var2);
      }

      public void addToken(int var1) {
         try {
            this.fTokens[this.fTokenCount] = var1;
         } catch (ArrayIndexOutOfBoundsException var4) {
            int[] var3 = this.fTokens;
            this.fTokens = new int[this.fTokenCount << 1];
            System.arraycopy(var3, 0, this.fTokens, 0, this.fTokenCount);
            this.fTokens[this.fTokenCount] = var1;
         }

         ++this.fTokenCount;
      }

      public void rewind() {
         this.fCurrentTokenIndex = 0;
      }

      public boolean hasMore() {
         return this.fCurrentTokenIndex < this.fTokenCount;
      }

      public int nextToken() throws XPathException {
         if (this.fCurrentTokenIndex == this.fTokenCount) {
            throw new XPathException("c-general-xpath");
         } else {
            return this.fTokens[this.fCurrentTokenIndex++];
         }
      }

      public int peekToken() throws XPathException {
         if (this.fCurrentTokenIndex == this.fTokenCount) {
            throw new XPathException("c-general-xpath");
         } else {
            return this.fTokens[this.fCurrentTokenIndex];
         }
      }

      public String nextTokenAsString() throws XPathException {
         String var1 = this.getTokenString(this.nextToken());
         if (var1 == null) {
            throw new XPathException("c-general-xpath");
         } else {
            return var1;
         }
      }

      public void dumpTokens() {
         for(int var1 = 0; var1 < this.fTokenCount; ++var1) {
            PrintStream var10000;
            StringBuffer var10001;
            switch (this.fTokens[var1]) {
               case 0:
                  System.out.print("<OPEN_PAREN/>");
                  break;
               case 1:
                  System.out.print("<CLOSE_PAREN/>");
                  break;
               case 2:
                  System.out.print("<OPEN_BRACKET/>");
                  break;
               case 3:
                  System.out.print("<CLOSE_BRACKET/>");
                  break;
               case 4:
                  System.out.print("<PERIOD/>");
                  break;
               case 5:
                  System.out.print("<DOUBLE_PERIOD/>");
                  break;
               case 6:
                  System.out.print("<ATSIGN/>");
                  break;
               case 7:
                  System.out.print("<COMMA/>");
                  break;
               case 8:
                  System.out.print("<DOUBLE_COLON/>");
                  break;
               case 9:
                  System.out.print("<NAMETEST_ANY/>");
                  break;
               case 10:
                  System.out.print("<NAMETEST_NAMESPACE");
                  var10000 = System.out;
                  var10001 = (new StringBuffer()).append(" prefix=\"");
                  ++var1;
                  var10000.print(var10001.append(this.getTokenString(this.fTokens[var1])).append("\"").toString());
                  System.out.print("/>");
                  break;
               case 11:
                  System.out.print("<NAMETEST_QNAME");
                  ++var1;
                  if (this.fTokens[var1] != -1) {
                     System.out.print(" prefix=\"" + this.getTokenString(this.fTokens[var1]) + "\"");
                  }

                  var10000 = System.out;
                  var10001 = (new StringBuffer()).append(" localpart=\"");
                  ++var1;
                  var10000.print(var10001.append(this.getTokenString(this.fTokens[var1])).append("\"").toString());
                  System.out.print("/>");
                  break;
               case 12:
                  System.out.print("<NODETYPE_COMMENT/>");
                  break;
               case 13:
                  System.out.print("<NODETYPE_TEXT/>");
                  break;
               case 14:
                  System.out.print("<NODETYPE_PI/>");
                  break;
               case 15:
                  System.out.print("<NODETYPE_NODE/>");
                  break;
               case 16:
                  System.out.print("<OPERATOR_AND/>");
                  break;
               case 17:
                  System.out.print("<OPERATOR_OR/>");
                  break;
               case 18:
                  System.out.print("<OPERATOR_MOD/>");
                  break;
               case 19:
                  System.out.print("<OPERATOR_DIV/>");
                  break;
               case 20:
                  System.out.print("<OPERATOR_MULT/>");
                  break;
               case 21:
                  System.out.print("<OPERATOR_SLASH/>");
                  if (var1 + 1 < this.fTokenCount) {
                     System.out.println();
                     System.out.print("  ");
                  }
                  break;
               case 22:
                  System.out.print("<OPERATOR_DOUBLE_SLASH/>");
                  break;
               case 23:
                  System.out.print("<OPERATOR_UNION/>");
                  break;
               case 24:
                  System.out.print("<OPERATOR_PLUS/>");
                  break;
               case 25:
                  System.out.print("<OPERATOR_MINUS/>");
                  break;
               case 26:
                  System.out.print("<OPERATOR_EQUAL/>");
                  break;
               case 27:
                  System.out.print("<OPERATOR_NOT_EQUAL/>");
                  break;
               case 28:
                  System.out.print("<OPERATOR_LESS/>");
                  break;
               case 29:
                  System.out.print("<OPERATOR_LESS_EQUAL/>");
                  break;
               case 30:
                  System.out.print("<OPERATOR_GREATER/>");
                  break;
               case 31:
                  System.out.print("<OPERATOR_GREATER_EQUAL/>");
                  break;
               case 32:
                  System.out.print("<FUNCTION_NAME");
                  ++var1;
                  if (this.fTokens[var1] != -1) {
                     System.out.print(" prefix=\"" + this.getTokenString(this.fTokens[var1]) + "\"");
                  }

                  var10000 = System.out;
                  var10001 = (new StringBuffer()).append(" localpart=\"");
                  ++var1;
                  var10000.print(var10001.append(this.getTokenString(this.fTokens[var1])).append("\"").toString());
                  System.out.print("/>");
                  break;
               case 33:
                  System.out.print("<AXISNAME_ANCESTOR/>");
                  break;
               case 34:
                  System.out.print("<AXISNAME_ANCESTOR_OR_SELF/>");
                  break;
               case 35:
                  System.out.print("<AXISNAME_ATTRIBUTE/>");
                  break;
               case 36:
                  System.out.print("<AXISNAME_CHILD/>");
                  break;
               case 37:
                  System.out.print("<AXISNAME_DESCENDANT/>");
                  break;
               case 38:
                  System.out.print("<AXISNAME_DESCENDANT_OR_SELF/>");
                  break;
               case 39:
                  System.out.print("<AXISNAME_FOLLOWING/>");
                  break;
               case 40:
                  System.out.print("<AXISNAME_FOLLOWING_SIBLING/>");
                  break;
               case 41:
                  System.out.print("<AXISNAME_NAMESPACE/>");
                  break;
               case 42:
                  System.out.print("<AXISNAME_PARENT/>");
                  break;
               case 43:
                  System.out.print("<AXISNAME_PRECEDING/>");
                  break;
               case 44:
                  System.out.print("<AXISNAME_PRECEDING_SIBLING/>");
                  break;
               case 45:
                  System.out.print("<AXISNAME_SELF/>");
                  break;
               case 46:
                  System.out.print("<LITERAL");
                  var10000 = System.out;
                  var10001 = (new StringBuffer()).append(" value=\"");
                  ++var1;
                  var10000.print(var10001.append(this.getTokenString(this.fTokens[var1])).append("\"").toString());
                  System.out.print("/>");
                  break;
               case 47:
                  System.out.print("<NUMBER");
                  var10000 = System.out;
                  var10001 = (new StringBuffer()).append(" whole=\"");
                  ++var1;
                  var10000.print(var10001.append(this.getTokenString(this.fTokens[var1])).append("\"").toString());
                  var10000 = System.out;
                  var10001 = (new StringBuffer()).append(" part=\"");
                  ++var1;
                  var10000.print(var10001.append(this.getTokenString(this.fTokens[var1])).append("\"").toString());
                  System.out.print("/>");
                  break;
               case 48:
                  System.out.print("<VARIABLE_REFERENCE");
                  ++var1;
                  if (this.fTokens[var1] != -1) {
                     System.out.print(" prefix=\"" + this.getTokenString(this.fTokens[var1]) + "\"");
                  }

                  var10000 = System.out;
                  var10001 = (new StringBuffer()).append(" localpart=\"");
                  ++var1;
                  var10000.print(var10001.append(this.getTokenString(this.fTokens[var1])).append("\"").toString());
                  System.out.print("/>");
                  break;
               default:
                  System.out.println("<???/>");
            }
         }

         System.out.println();
      }
   }

   public static class NodeTest implements Cloneable {
      public static final short QNAME = 1;
      public static final short WILDCARD = 2;
      public static final short NODE = 3;
      public static final short NAMESPACE = 4;
      public short type;
      public final QName name = new QName();

      public NodeTest(short var1) {
         this.type = var1;
      }

      public NodeTest(QName var1) {
         this.type = 1;
         this.name.setValues(var1);
      }

      public NodeTest(String var1, String var2) {
         this.type = 4;
         this.name.setValues(var1, (String)null, (String)null, var2);
      }

      public NodeTest(NodeTest var1) {
         this.type = var1.type;
         this.name.setValues(var1.name);
      }

      public String toString() {
         switch (this.type) {
            case 1:
               if (this.name.prefix.length() != 0) {
                  if (this.name.uri != null) {
                     return this.name.prefix + ':' + this.name.localpart;
                  }

                  return "{" + this.name.uri + '}' + this.name.prefix + ':' + this.name.localpart;
               }

               return this.name.localpart;
            case 2:
               return "*";
            case 3:
               return "node()";
            case 4:
               if (this.name.prefix.length() != 0) {
                  if (this.name.uri != null) {
                     return this.name.prefix + ":*";
                  }

                  return "{" + this.name.uri + '}' + this.name.prefix + ":*";
               }

               return "???:*";
            default:
               return "???";
         }
      }

      public Object clone() {
         return new NodeTest(this);
      }
   }

   public static class Axis implements Cloneable {
      public static final short CHILD = 1;
      public static final short ATTRIBUTE = 2;
      public static final short SELF = 3;
      public static final short DESCENDANT = 4;
      public short type;

      public Axis(short var1) {
         this.type = var1;
      }

      protected Axis(Axis var1) {
         this.type = var1.type;
      }

      public String toString() {
         switch (this.type) {
            case 1:
               return "child";
            case 2:
               return "attribute";
            case 3:
               return "self";
            case 4:
               return "descendant";
            default:
               return "???";
         }
      }

      public Object clone() {
         return new Axis(this);
      }
   }

   public static class Step implements Cloneable {
      public Axis axis;
      public NodeTest nodeTest;

      public Step(Axis var1, NodeTest var2) {
         this.axis = var1;
         this.nodeTest = var2;
      }

      protected Step(Step var1) {
         this.axis = (Axis)var1.axis.clone();
         this.nodeTest = (NodeTest)var1.nodeTest.clone();
      }

      public String toString() {
         if (this.axis.type == 3) {
            return ".";
         } else if (this.axis.type == 2) {
            return "@" + this.nodeTest.toString();
         } else if (this.axis.type == 1) {
            return this.nodeTest.toString();
         } else {
            return this.axis.type == 4 ? "//" : "??? (" + this.axis.type + ')';
         }
      }

      public Object clone() {
         return new Step(this);
      }
   }

   public static class LocationPath implements Cloneable {
      public Step[] steps;

      public LocationPath(Step[] var1) {
         this.steps = var1;
      }

      protected LocationPath(LocationPath var1) {
         this.steps = new Step[var1.steps.length];

         for(int var2 = 0; var2 < this.steps.length; ++var2) {
            this.steps[var2] = (Step)var1.steps[var2].clone();
         }

      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();

         for(int var2 = 0; var2 < this.steps.length; ++var2) {
            if (var2 > 0 && this.steps[var2 - 1].axis.type != 4 && this.steps[var2].axis.type != 4) {
               var1.append('/');
            }

            var1.append(this.steps[var2].toString());
         }

         return var1.toString();
      }

      public Object clone() {
         return new LocationPath(this);
      }
   }
}

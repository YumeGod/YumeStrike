package org.apache.batik.transcoder.svg2svg;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import org.apache.batik.transcoder.ErrorHandler;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.xml.XMLException;
import org.apache.batik.xml.XMLScanner;

public class PrettyPrinter {
   public static final int DOCTYPE_CHANGE = 0;
   public static final int DOCTYPE_REMOVE = 1;
   public static final int DOCTYPE_KEEP_UNCHANGED = 2;
   protected XMLScanner scanner;
   protected OutputManager output;
   protected Writer writer;
   protected ErrorHandler errorHandler;
   protected String newline;
   protected boolean format;
   protected int tabulationWidth;
   protected int documentWidth;
   protected int doctypeOption;
   protected String publicId;
   protected String systemId;
   protected String xmlDeclaration;
   protected int type;

   public PrettyPrinter() {
      this.errorHandler = SVGTranscoder.DEFAULT_ERROR_HANDLER;
      this.newline = "\n";
      this.format = true;
      this.tabulationWidth = 4;
      this.documentWidth = 80;
      this.doctypeOption = 2;
   }

   public void setXMLDeclaration(String var1) {
      this.xmlDeclaration = var1;
   }

   public void setDoctypeOption(int var1) {
      this.doctypeOption = var1;
   }

   public void setPublicId(String var1) {
      this.publicId = var1;
   }

   public void setSystemId(String var1) {
      this.systemId = var1;
   }

   public void setNewline(String var1) {
      this.newline = var1;
   }

   public String getNewline() {
      return this.newline;
   }

   public void setFormat(boolean var1) {
      this.format = var1;
   }

   public boolean getFormat() {
      return this.format;
   }

   public void setTabulationWidth(int var1) {
      this.tabulationWidth = Math.max(var1, 0);
   }

   public int getTabulationWidth() {
      return this.tabulationWidth;
   }

   public void setDocumentWidth(int var1) {
      this.documentWidth = Math.max(var1, 0);
   }

   public int getDocumentWidth() {
      return this.documentWidth;
   }

   public void print(Reader var1, Writer var2) throws TranscoderException, IOException {
      try {
         this.scanner = new XMLScanner(var1);
         this.output = new OutputManager(this, var2);
         this.writer = var2;
         this.type = this.scanner.next();
         this.printXMLDecl();

         while(true) {
            while(true) {
               switch (this.type) {
                  case 1:
                     this.output.printTopSpaces(this.getCurrentValue());
                     this.scanner.clearBuffer();
                     this.type = this.scanner.next();
                     break;
                  case 2:
                  case 3:
                  default:
                     this.printDoctype();

                     while(true) {
                        while(true) {
                           this.scanner.clearBuffer();
                           switch (this.type) {
                              case 1:
                                 this.output.printTopSpaces(this.getCurrentValue());
                                 this.scanner.clearBuffer();
                                 this.type = this.scanner.next();
                                 break;
                              case 2:
                              case 3:
                              default:
                                 if (this.type != 9) {
                                    throw this.fatalError("element", (Object[])null);
                                 }

                                 this.printElement();

                                 while(true) {
                                    while(true) {
                                       switch (this.type) {
                                          case 1:
                                             this.output.printTopSpaces(this.getCurrentValue());
                                             this.scanner.clearBuffer();
                                             this.type = this.scanner.next();
                                             break;
                                          case 2:
                                          case 3:
                                          default:
                                             return;
                                          case 4:
                                             this.output.printComment(this.getCurrentValue());
                                             this.scanner.clearBuffer();
                                             this.type = this.scanner.next();
                                             break;
                                          case 5:
                                             this.printPI();
                                       }
                                    }
                                 }
                              case 4:
                                 this.output.printComment(this.getCurrentValue());
                                 this.scanner.clearBuffer();
                                 this.type = this.scanner.next();
                                 break;
                              case 5:
                                 this.printPI();
                           }
                        }
                     }
                  case 4:
                     this.output.printComment(this.getCurrentValue());
                     this.scanner.clearBuffer();
                     this.type = this.scanner.next();
                     break;
                  case 5:
                     this.printPI();
               }
            }
         }
      } catch (XMLException var4) {
         this.errorHandler.fatalError(new TranscoderException(var4.getMessage()));
      }
   }

   protected void printXMLDecl() throws TranscoderException, XMLException, IOException {
      if (this.xmlDeclaration == null) {
         if (this.type == 2) {
            if (this.scanner.next() != 1) {
               throw this.fatalError("space", (Object[])null);
            }

            char[] var1 = this.getCurrentValue();
            if (this.scanner.next() != 22) {
               throw this.fatalError("token", new Object[]{"version"});
            }

            this.type = this.scanner.next();
            char[] var2 = null;
            if (this.type == 1) {
               var2 = this.getCurrentValue();
               this.type = this.scanner.next();
            }

            if (this.type != 15) {
               throw this.fatalError("token", new Object[]{"="});
            }

            this.type = this.scanner.next();
            char[] var3 = null;
            if (this.type == 1) {
               var3 = this.getCurrentValue();
               this.type = this.scanner.next();
            }

            if (this.type != 25) {
               throw this.fatalError("string", (Object[])null);
            }

            char[] var4 = this.getCurrentValue();
            char var5 = this.scanner.getStringDelimiter();
            char[] var6 = null;
            char[] var7 = null;
            char[] var8 = null;
            char[] var9 = null;
            char var10 = 0;
            char[] var11 = null;
            char[] var12 = null;
            char[] var13 = null;
            char[] var14 = null;
            char var15 = 0;
            char[] var16 = null;
            this.type = this.scanner.next();
            if (this.type == 1) {
               var6 = this.getCurrentValue();
               this.type = this.scanner.next();
               if (this.type == 23) {
                  this.type = this.scanner.next();
                  if (this.type == 1) {
                     var7 = this.getCurrentValue();
                     this.type = this.scanner.next();
                  }

                  if (this.type != 15) {
                     throw this.fatalError("token", new Object[]{"="});
                  }

                  this.type = this.scanner.next();
                  if (this.type == 1) {
                     var8 = this.getCurrentValue();
                     this.type = this.scanner.next();
                  }

                  if (this.type != 25) {
                     throw this.fatalError("string", (Object[])null);
                  }

                  var9 = this.getCurrentValue();
                  var10 = this.scanner.getStringDelimiter();
                  this.type = this.scanner.next();
                  if (this.type == 1) {
                     var11 = this.getCurrentValue();
                     this.type = this.scanner.next();
                  }
               }

               if (this.type == 24) {
                  this.type = this.scanner.next();
                  if (this.type == 1) {
                     var12 = this.getCurrentValue();
                     this.type = this.scanner.next();
                  }

                  if (this.type != 15) {
                     throw this.fatalError("token", new Object[]{"="});
                  }

                  this.type = this.scanner.next();
                  if (this.type == 1) {
                     var13 = this.getCurrentValue();
                     this.type = this.scanner.next();
                  }

                  if (this.type != 25) {
                     throw this.fatalError("string", (Object[])null);
                  }

                  var14 = this.getCurrentValue();
                  var15 = this.scanner.getStringDelimiter();
                  this.type = this.scanner.next();
                  if (this.type == 1) {
                     var16 = this.getCurrentValue();
                     this.type = this.scanner.next();
                  }
               }
            }

            if (this.type != 7) {
               throw this.fatalError("pi.end", (Object[])null);
            }

            this.output.printXMLDecl(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16);
            this.type = this.scanner.next();
         }
      } else {
         this.output.printString(this.xmlDeclaration);
         this.output.printNewline();
         if (this.type == 2) {
            if (this.scanner.next() != 1) {
               throw this.fatalError("space", (Object[])null);
            }

            if (this.scanner.next() != 22) {
               throw this.fatalError("token", new Object[]{"version"});
            }

            this.type = this.scanner.next();
            if (this.type == 1) {
               this.type = this.scanner.next();
            }

            if (this.type != 15) {
               throw this.fatalError("token", new Object[]{"="});
            }

            this.type = this.scanner.next();
            if (this.type == 1) {
               this.type = this.scanner.next();
            }

            if (this.type != 25) {
               throw this.fatalError("string", (Object[])null);
            }

            this.type = this.scanner.next();
            if (this.type == 1) {
               this.type = this.scanner.next();
               if (this.type == 23) {
                  this.type = this.scanner.next();
                  if (this.type == 1) {
                     this.type = this.scanner.next();
                  }

                  if (this.type != 15) {
                     throw this.fatalError("token", new Object[]{"="});
                  }

                  this.type = this.scanner.next();
                  if (this.type == 1) {
                     this.type = this.scanner.next();
                  }

                  if (this.type != 25) {
                     throw this.fatalError("string", (Object[])null);
                  }

                  this.type = this.scanner.next();
                  if (this.type == 1) {
                     this.type = this.scanner.next();
                  }
               }

               if (this.type == 24) {
                  this.type = this.scanner.next();
                  if (this.type == 1) {
                     this.type = this.scanner.next();
                  }

                  if (this.type != 15) {
                     throw this.fatalError("token", new Object[]{"="});
                  }

                  this.type = this.scanner.next();
                  if (this.type == 1) {
                     this.type = this.scanner.next();
                  }

                  if (this.type != 25) {
                     throw this.fatalError("string", (Object[])null);
                  }

                  this.type = this.scanner.next();
                  if (this.type == 1) {
                     this.type = this.scanner.next();
                  }
               }
            }

            if (this.type != 7) {
               throw this.fatalError("pi.end", (Object[])null);
            }

            this.type = this.scanner.next();
         }
      }

   }

   protected void printPI() throws TranscoderException, XMLException, IOException {
      char[] var1 = this.getCurrentValue();
      this.type = this.scanner.next();
      char[] var2 = new char[0];
      if (this.type == 1) {
         var2 = this.getCurrentValue();
         this.type = this.scanner.next();
      }

      if (this.type != 6) {
         throw this.fatalError("pi.data", (Object[])null);
      } else {
         char[] var3 = this.getCurrentValue();
         this.type = this.scanner.next();
         if (this.type != 7) {
            throw this.fatalError("pi.end", (Object[])null);
         } else {
            this.output.printPI(var1, var2, var3);
            this.type = this.scanner.next();
         }
      }
   }

   protected void printDoctype() throws TranscoderException, XMLException, IOException {
      switch (this.doctypeOption) {
         case 1:
            if (this.type == 3) {
               this.type = this.scanner.next();
               if (this.type != 1) {
                  throw this.fatalError("space", (Object[])null);
               }

               this.type = this.scanner.next();
               if (this.type != 14) {
                  throw this.fatalError("name", (Object[])null);
               }

               this.type = this.scanner.next();
               if (this.type == 1) {
                  this.type = this.scanner.next();
                  switch (this.type) {
                     case 26:
                        this.type = this.scanner.next();
                        if (this.type != 1) {
                           throw this.fatalError("space", (Object[])null);
                        }

                        this.type = this.scanner.next();
                        if (this.type != 25) {
                           throw this.fatalError("string", (Object[])null);
                        }

                        this.type = this.scanner.next();
                        if (this.type == 1) {
                           this.type = this.scanner.next();
                        }
                        break;
                     case 27:
                        this.type = this.scanner.next();
                        if (this.type != 1) {
                           throw this.fatalError("space", (Object[])null);
                        }

                        this.type = this.scanner.next();
                        if (this.type != 25) {
                           throw this.fatalError("string", (Object[])null);
                        }

                        this.type = this.scanner.next();
                        if (this.type != 1) {
                           throw this.fatalError("space", (Object[])null);
                        }

                        this.type = this.scanner.next();
                        if (this.type != 25) {
                           throw this.fatalError("string", (Object[])null);
                        }

                        this.type = this.scanner.next();
                        if (this.type == 1) {
                           this.type = this.scanner.next();
                        }
                  }
               }

               if (this.type == 28) {
                  do {
                     this.type = this.scanner.next();
                  } while(this.type != 29);
               }

               if (this.type == 1) {
                  this.type = this.scanner.next();
               }

               if (this.type != 20) {
                  throw this.fatalError("end", (Object[])null);
               }
            }

            this.type = this.scanner.next();
            break;
         default:
            char[] var2;
            char[] var3;
            if (this.type == 3) {
               this.type = this.scanner.next();
               if (this.type != 1) {
                  throw this.fatalError("space", (Object[])null);
               }

               char[] var13 = this.getCurrentValue();
               this.type = this.scanner.next();
               if (this.type != 14) {
                  throw this.fatalError("name", (Object[])null);
               }

               var2 = this.getCurrentValue();
               var3 = null;
               String var4 = null;
               char[] var5 = null;
               char[] var6 = null;
               char var7 = 0;
               char[] var8 = null;
               char[] var9 = null;
               char var10 = 0;
               char[] var11 = null;
               this.type = this.scanner.next();
               if (this.type == 1) {
                  var3 = this.getCurrentValue();
                  this.type = this.scanner.next();
                  switch (this.type) {
                     case 26:
                        var4 = "SYSTEM";
                        this.type = this.scanner.next();
                        if (this.type != 1) {
                           throw this.fatalError("space", (Object[])null);
                        }

                        var5 = this.getCurrentValue();
                        this.type = this.scanner.next();
                        if (this.type != 25) {
                           throw this.fatalError("string", (Object[])null);
                        }

                        var6 = this.getCurrentValue();
                        var7 = this.scanner.getStringDelimiter();
                        this.type = this.scanner.next();
                        if (this.type == 1) {
                           var8 = this.getCurrentValue();
                           this.type = this.scanner.next();
                        }
                        break;
                     case 27:
                        var4 = "PUBLIC";
                        this.type = this.scanner.next();
                        if (this.type != 1) {
                           throw this.fatalError("space", (Object[])null);
                        }

                        var5 = this.getCurrentValue();
                        this.type = this.scanner.next();
                        if (this.type != 25) {
                           throw this.fatalError("string", (Object[])null);
                        }

                        var6 = this.getCurrentValue();
                        var7 = this.scanner.getStringDelimiter();
                        this.type = this.scanner.next();
                        if (this.type != 1) {
                           throw this.fatalError("space", (Object[])null);
                        }

                        var8 = this.getCurrentValue();
                        this.type = this.scanner.next();
                        if (this.type != 25) {
                           throw this.fatalError("string", (Object[])null);
                        }

                        var9 = this.getCurrentValue();
                        var10 = this.scanner.getStringDelimiter();
                        this.type = this.scanner.next();
                        if (this.type == 1) {
                           var11 = this.getCurrentValue();
                           this.type = this.scanner.next();
                        }
                  }
               }

               if (this.doctypeOption == 0) {
                  if (this.publicId != null) {
                     var4 = "PUBLIC";
                     var6 = this.publicId.toCharArray();
                     var7 = '"';
                     if (this.systemId != null) {
                        var9 = this.systemId.toCharArray();
                        var10 = '"';
                     }
                  } else if (this.systemId != null) {
                     var4 = "SYSTEM";
                     var6 = this.systemId.toCharArray();
                     var7 = '"';
                     var9 = null;
                  }
               }

               this.output.printDoctypeStart(var13, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
               if (this.type == 28) {
                  this.output.printCharacter('[');
                  this.type = this.scanner.next();

                  label169:
                  while(true) {
                     switch (this.type) {
                        case 1:
                           this.output.printSpaces(this.getCurrentValue(), true);
                           this.scanner.clearBuffer();
                           this.type = this.scanner.next();
                           break;
                        case 2:
                        case 3:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                        case 26:
                        case 27:
                        case 28:
                        default:
                           throw this.fatalError("xml", (Object[])null);
                        case 4:
                           this.output.printComment(this.getCurrentValue());
                           this.scanner.clearBuffer();
                           this.type = this.scanner.next();
                           break;
                        case 5:
                           this.printPI();
                           break;
                        case 29:
                           this.output.printCharacter(']');
                           this.scanner.clearBuffer();
                           this.type = this.scanner.next();
                           break label169;
                        case 30:
                           this.scanner.clearBuffer();
                           this.printElementDeclaration();
                           break;
                        case 31:
                           this.scanner.clearBuffer();
                           this.printAttlist();
                           break;
                        case 32:
                           this.scanner.clearBuffer();
                           this.printEntityDeclaration();
                           break;
                        case 33:
                           this.scanner.clearBuffer();
                           this.printNotation();
                           break;
                        case 34:
                           this.output.printParameterEntityReference(this.getCurrentValue());
                           this.scanner.clearBuffer();
                           this.type = this.scanner.next();
                     }
                  }
               }

               char[] var12 = null;
               if (this.type == 1) {
                  var12 = this.getCurrentValue();
                  this.type = this.scanner.next();
               }

               if (this.type != 20) {
                  throw this.fatalError("end", (Object[])null);
               }

               this.type = this.scanner.next();
               this.output.printDoctypeEnd(var12);
            } else if (this.doctypeOption == 0) {
               String var1 = "PUBLIC";
               var2 = "-//W3C//DTD SVG 1.0//EN".toCharArray();
               var3 = "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd".toCharArray();
               if (this.publicId != null) {
                  var2 = this.publicId.toCharArray();
                  if (this.systemId != null) {
                     var3 = this.systemId.toCharArray();
                  }
               } else if (this.systemId != null) {
                  var1 = "SYSTEM";
                  var2 = this.systemId.toCharArray();
                  var3 = null;
               }

               this.output.printDoctypeStart(new char[]{' '}, new char[]{'s', 'v', 'g'}, new char[]{' '}, var1, new char[]{' '}, var2, '"', new char[]{' '}, var3, '"', (char[])null);
               this.output.printDoctypeEnd((char[])null);
            }
      }

   }

   protected String printElement() throws TranscoderException, XMLException, IOException {
      char[] var1 = this.getCurrentValue();
      String var2 = new String(var1);
      LinkedList var3 = new LinkedList();
      char[] var4 = null;
      this.type = this.scanner.next();

      label67:
      while(true) {
         do {
            if (this.type != 1) {
               this.output.printElementStart(var1, var3, var4);
               switch (this.type) {
                  case 19:
                     this.output.printElementEnd((char[])null, (char[])null);
                     break;
                  case 20:
                     this.output.printCharacter('>');
                     this.type = this.scanner.next();
                     this.printContent(this.allowSpaceAtStart(var2));
                     if (this.type != 10) {
                        throw this.fatalError("end.tag", (Object[])null);
                     }

                     var1 = this.getCurrentValue();
                     this.type = this.scanner.next();
                     var4 = null;
                     if (this.type == 1) {
                        var4 = this.getCurrentValue();
                        this.type = this.scanner.next();
                     }

                     this.output.printElementEnd(var1, var4);
                     if (this.type != 20) {
                        throw this.fatalError("end", (Object[])null);
                     }
                     break;
                  default:
                     throw this.fatalError("xml", (Object[])null);
               }

               this.type = this.scanner.next();
               return var2;
            }

            var4 = this.getCurrentValue();
            this.type = this.scanner.next();
         } while(this.type != 14);

         char[] var5 = this.getCurrentValue();
         char[] var6 = null;
         this.type = this.scanner.next();
         if (this.type == 1) {
            var6 = this.getCurrentValue();
            this.type = this.scanner.next();
         }

         if (this.type != 15) {
            throw this.fatalError("token", new Object[]{"="});
         }

         this.type = this.scanner.next();
         char[] var7 = null;
         if (this.type == 1) {
            var7 = this.getCurrentValue();
            this.type = this.scanner.next();
         }

         if (this.type != 25 && this.type != 16) {
            throw this.fatalError("string", (Object[])null);
         }

         char var8 = this.scanner.getStringDelimiter();
         boolean var9 = false;
         StringBuffer var10 = new StringBuffer();
         var10.append(this.getCurrentValue());

         while(true) {
            while(true) {
               this.scanner.clearBuffer();
               this.type = this.scanner.next();
               switch (this.type) {
                  case 12:
                     var9 = true;
                     var10.append("&#");
                     var10.append(this.getCurrentValue());
                     var10.append(";");
                     break;
                  case 13:
                     var9 = true;
                     var10.append("&");
                     var10.append(this.getCurrentValue());
                     var10.append(";");
                     break;
                  case 14:
                  case 15:
                  case 19:
                  case 20:
                  case 21:
                  case 22:
                  case 23:
                  case 24:
                  default:
                     var3.add(new OutputManager.AttributeInfo(var4, var5, var6, var7, new String(var10), var8, var9));
                     var4 = null;
                     continue label67;
                  case 16:
                  case 17:
                  case 18:
                  case 25:
                     var10.append(this.getCurrentValue());
               }
            }
         }
      }
   }

   boolean allowSpaceAtStart(String var1) {
      return true;
   }

   protected void printContent(boolean var1) throws TranscoderException, XMLException, IOException {
      boolean var2 = false;

      while(true) {
         switch (this.type) {
            case 4:
               this.output.printComment(this.getCurrentValue());
               this.scanner.clearBuffer();
               this.type = this.scanner.next();
               var2 = false;
               break;
            case 5:
               this.printPI();
               var2 = false;
               break;
            case 6:
            case 7:
            case 10:
            default:
               return;
            case 8:
               var2 = this.output.printCharacterData(this.getCurrentValue(), var1, var2);
               this.scanner.clearBuffer();
               this.type = this.scanner.next();
               var1 = false;
               break;
            case 9:
               String var3 = this.printElement();
               var1 = this.allowSpaceAtStart(var3);
               break;
            case 11:
               this.type = this.scanner.next();
               if (this.type != 8) {
                  throw this.fatalError("character.data", (Object[])null);
               }

               this.output.printCDATASection(this.getCurrentValue());
               if (this.scanner.next() != 21) {
                  throw this.fatalError("section.end", (Object[])null);
               }

               this.scanner.clearBuffer();
               this.type = this.scanner.next();
               var2 = false;
               var1 = false;
               break;
            case 12:
               this.output.printCharacterEntityReference(this.getCurrentValue(), var1, var2);
               this.scanner.clearBuffer();
               this.type = this.scanner.next();
               var1 = false;
               var2 = false;
               break;
            case 13:
               this.output.printEntityReference(this.getCurrentValue(), var1);
               this.scanner.clearBuffer();
               this.type = this.scanner.next();
               var1 = false;
               var2 = false;
         }
      }
   }

   protected void printNotation() throws TranscoderException, XMLException, IOException {
      int var1 = this.scanner.next();
      if (var1 != 1) {
         throw this.fatalError("space", (Object[])null);
      } else {
         char[] var2 = this.getCurrentValue();
         var1 = this.scanner.next();
         if (var1 != 14) {
            throw this.fatalError("name", (Object[])null);
         } else {
            char[] var3 = this.getCurrentValue();
            var1 = this.scanner.next();
            if (var1 != 1) {
               throw this.fatalError("space", (Object[])null);
            } else {
               char[] var4 = this.getCurrentValue();
               var1 = this.scanner.next();
               String var5 = null;
               Object var6 = null;
               Object var7 = null;
               boolean var8 = false;
               char[] var9 = null;
               char[] var10 = null;
               char var11 = 0;
               char[] var13;
               char[] var14;
               char var15;
               switch (var1) {
                  case 26:
                     var5 = "SYSTEM";
                     var1 = this.scanner.next();
                     if (var1 != 1) {
                        throw this.fatalError("space", (Object[])null);
                     }

                     var13 = this.getCurrentValue();
                     var1 = this.scanner.next();
                     if (var1 != 25) {
                        throw this.fatalError("string", (Object[])null);
                     }

                     var14 = this.getCurrentValue();
                     var15 = this.scanner.getStringDelimiter();
                     var1 = this.scanner.next();
                     break;
                  case 27:
                     var5 = "PUBLIC";
                     var1 = this.scanner.next();
                     if (var1 != 1) {
                        throw this.fatalError("space", (Object[])null);
                     }

                     var13 = this.getCurrentValue();
                     var1 = this.scanner.next();
                     if (var1 != 25) {
                        throw this.fatalError("string", (Object[])null);
                     }

                     var14 = this.getCurrentValue();
                     var15 = this.scanner.getStringDelimiter();
                     var1 = this.scanner.next();
                     if (var1 == 1) {
                        var9 = this.getCurrentValue();
                        var1 = this.scanner.next();
                        if (var1 == 25) {
                           var10 = this.getCurrentValue();
                           var11 = this.scanner.getStringDelimiter();
                           var1 = this.scanner.next();
                        }
                     }
                     break;
                  default:
                     throw this.fatalError("notation.definition", (Object[])null);
               }

               char[] var12 = null;
               if (var1 == 1) {
                  var12 = this.getCurrentValue();
                  var1 = this.scanner.next();
               }

               if (var1 != 20) {
                  throw this.fatalError("end", (Object[])null);
               } else {
                  this.output.printNotation(var2, var3, var4, var5, var13, var14, var15, var9, var10, var11, var12);
                  this.scanner.next();
               }
            }
         }
      }
   }

   protected void printAttlist() throws TranscoderException, XMLException, IOException {
      this.type = this.scanner.next();
      if (this.type != 1) {
         throw this.fatalError("space", (Object[])null);
      } else {
         char[] var1 = this.getCurrentValue();
         this.type = this.scanner.next();
         if (this.type != 14) {
            throw this.fatalError("name", (Object[])null);
         } else {
            char[] var2 = this.getCurrentValue();
            this.type = this.scanner.next();
            this.output.printAttlistStart(var1, var2);

            while(true) {
               if (this.type == 1) {
                  var1 = this.getCurrentValue();
                  this.type = this.scanner.next();
                  if (this.type == 14) {
                     var2 = this.getCurrentValue();
                     this.type = this.scanner.next();
                     if (this.type != 1) {
                        throw this.fatalError("space", (Object[])null);
                     }

                     char[] var3 = this.getCurrentValue();
                     this.type = this.scanner.next();
                     this.output.printAttName(var1, var2, var3);
                     LinkedList var4;
                     switch (this.type) {
                        case 40:
                           this.type = this.scanner.next();
                           var4 = new LinkedList();
                           var1 = null;
                           if (this.type == 1) {
                              var1 = this.getCurrentValue();
                              this.type = this.scanner.next();
                           }

                           if (this.type != 56) {
                              throw this.fatalError("nmtoken", (Object[])null);
                           }

                           var2 = this.getCurrentValue();
                           this.type = this.scanner.next();
                           var3 = null;
                           if (this.type == 1) {
                              var3 = this.getCurrentValue();
                              this.type = this.scanner.next();
                           }

                           var4.add(new OutputManager.NameInfo(var1, var2, var3));

                           label152:
                           while(true) {
                              switch (this.type) {
                                 case 42:
                                    this.type = this.scanner.next();
                                    var1 = null;
                                    if (this.type == 1) {
                                       var1 = this.getCurrentValue();
                                       this.type = this.scanner.next();
                                    }

                                    if (this.type != 56) {
                                       throw this.fatalError("nmtoken", (Object[])null);
                                    }

                                    var2 = this.getCurrentValue();
                                    this.type = this.scanner.next();
                                    var3 = null;
                                    if (this.type == 1) {
                                       var3 = this.getCurrentValue();
                                       this.type = this.scanner.next();
                                    }

                                    var4.add(new OutputManager.NameInfo(var1, var2, var3));
                                    break;
                                 default:
                                    if (this.type != 41) {
                                       throw this.fatalError("right.brace", (Object[])null);
                                    }

                                    this.output.printEnumeration(var4);
                                    this.type = this.scanner.next();
                                    break label152;
                              }
                           }
                        case 41:
                        case 42:
                        case 43:
                        case 44:
                        case 53:
                        case 54:
                        case 55:
                        case 56:
                        default:
                           break;
                        case 45:
                        case 46:
                        case 47:
                        case 48:
                        case 49:
                        case 50:
                        case 51:
                        case 52:
                           this.output.printCharacters(this.getCurrentValue());
                           this.type = this.scanner.next();
                           break;
                        case 57:
                           this.output.printCharacters(this.getCurrentValue());
                           this.type = this.scanner.next();
                           if (this.type != 1) {
                              throw this.fatalError("space", (Object[])null);
                           }

                           this.output.printSpaces(this.getCurrentValue(), false);
                           this.type = this.scanner.next();
                           if (this.type != 40) {
                              throw this.fatalError("left.brace", (Object[])null);
                           }

                           this.type = this.scanner.next();
                           var4 = new LinkedList();
                           var1 = null;
                           if (this.type == 1) {
                              var1 = this.getCurrentValue();
                              this.type = this.scanner.next();
                           }

                           if (this.type != 14) {
                              throw this.fatalError("name", (Object[])null);
                           }

                           var2 = this.getCurrentValue();
                           this.type = this.scanner.next();
                           var3 = null;
                           if (this.type == 1) {
                              var3 = this.getCurrentValue();
                              this.type = this.scanner.next();
                           }

                           var4.add(new OutputManager.NameInfo(var1, var2, var3));

                           label144:
                           while(true) {
                              switch (this.type) {
                                 case 42:
                                    this.type = this.scanner.next();
                                    var1 = null;
                                    if (this.type == 1) {
                                       var1 = this.getCurrentValue();
                                       this.type = this.scanner.next();
                                    }

                                    if (this.type != 14) {
                                       throw this.fatalError("name", (Object[])null);
                                    }

                                    var2 = this.getCurrentValue();
                                    this.type = this.scanner.next();
                                    var3 = null;
                                    if (this.type == 1) {
                                       var3 = this.getCurrentValue();
                                       this.type = this.scanner.next();
                                    }

                                    var4.add(new OutputManager.NameInfo(var1, var2, var3));
                                    break;
                                 default:
                                    if (this.type != 41) {
                                       throw this.fatalError("right.brace", (Object[])null);
                                    }

                                    this.output.printEnumeration(var4);
                                    this.type = this.scanner.next();
                                    break label144;
                              }
                           }
                     }

                     if (this.type == 1) {
                        this.output.printSpaces(this.getCurrentValue(), true);
                        this.type = this.scanner.next();
                     }

                     label136:
                     switch (this.type) {
                        case 53:
                        case 54:
                           this.output.printCharacters(this.getCurrentValue());
                           this.type = this.scanner.next();
                           break;
                        case 55:
                           this.output.printCharacters(this.getCurrentValue());
                           this.type = this.scanner.next();
                           if (this.type != 1) {
                              throw this.fatalError("space", (Object[])null);
                           }

                           this.output.printSpaces(this.getCurrentValue(), false);
                           this.type = this.scanner.next();
                           if (this.type != 25 && this.type != 16) {
                              throw this.fatalError("space", (Object[])null);
                           }
                        case 16:
                        case 25:
                           this.output.printCharacter(this.scanner.getStringDelimiter());
                           this.output.printCharacters(this.getCurrentValue());

                           while(true) {
                              while(true) {
                                 this.type = this.scanner.next();
                                 switch (this.type) {
                                    case 12:
                                       this.output.printString("&#");
                                       this.output.printCharacters(this.getCurrentValue());
                                       this.output.printCharacter(';');
                                       break;
                                    case 13:
                                       this.output.printCharacter('&');
                                       this.output.printCharacters(this.getCurrentValue());
                                       this.output.printCharacter(';');
                                       break;
                                    case 14:
                                    case 15:
                                    case 19:
                                    case 20:
                                    case 21:
                                    case 22:
                                    case 23:
                                    case 24:
                                    default:
                                       this.output.printCharacter(this.scanner.getStringDelimiter());
                                       break label136;
                                    case 16:
                                    case 17:
                                    case 18:
                                    case 25:
                                       this.output.printCharacters(this.getCurrentValue());
                                 }
                              }
                           }
                        default:
                           throw this.fatalError("default.decl", (Object[])null);
                     }

                     var1 = null;
                     continue;
                  }
               }

               if (this.type != 20) {
                  throw this.fatalError("end", (Object[])null);
               }

               this.output.printAttlistEnd(var1);
               this.type = this.scanner.next();
               return;
            }
         }
      }
   }

   protected void printEntityDeclaration() throws TranscoderException, XMLException, IOException {
      this.writer.write("<!ENTITY");
      this.type = this.scanner.next();
      if (this.type != 1) {
         throw this.fatalError("space", (Object[])null);
      } else {
         this.writer.write(this.getCurrentValue());
         this.type = this.scanner.next();
         boolean var1 = false;
         switch (this.type) {
            case 14:
               this.writer.write(this.getCurrentValue());
               this.type = this.scanner.next();
               break;
            case 58:
               var1 = true;
               this.writer.write(37);
               this.type = this.scanner.next();
               if (this.type != 1) {
                  throw this.fatalError("space", (Object[])null);
               }

               this.writer.write(this.getCurrentValue());
               this.type = this.scanner.next();
               if (this.type != 14) {
                  throw this.fatalError("name", (Object[])null);
               }

               this.writer.write(this.getCurrentValue());
               this.type = this.scanner.next();
               break;
            default:
               throw this.fatalError("xml", (Object[])null);
         }

         if (this.type != 1) {
            throw this.fatalError("space", (Object[])null);
         } else {
            this.writer.write(this.getCurrentValue());
            this.type = this.scanner.next();
            switch (this.type) {
               case 16:
               case 25:
                  char var2 = this.scanner.getStringDelimiter();
                  this.writer.write(var2);

                  while(true) {
                     switch (this.type) {
                        case 13:
                           this.writer.write(38);
                           this.writer.write(this.getCurrentValue());
                           this.writer.write(59);
                           break;
                        case 16:
                        case 17:
                        case 18:
                        case 25:
                           this.writer.write(this.getCurrentValue());
                           break;
                        case 34:
                           this.writer.write(38);
                           this.writer.write(this.getCurrentValue());
                           this.writer.write(59);
                           break;
                        default:
                           this.writer.write(var2);
                           if (this.type == 1) {
                              this.writer.write(this.getCurrentValue());
                              this.type = this.scanner.next();
                           }

                           if (this.type != 20) {
                              throw this.fatalError("end", (Object[])null);
                           }

                           this.writer.write(">");
                           this.type = this.scanner.next();
                           return;
                     }

                     this.type = this.scanner.next();
                  }
               case 26:
                  this.writer.write("SYSTEM");
                  this.type = this.scanner.next();
                  if (this.type != 1) {
                     throw this.fatalError("space", (Object[])null);
                  }

                  this.type = this.scanner.next();
                  if (this.type != 25) {
                     throw this.fatalError("string", (Object[])null);
                  }

                  this.writer.write(" \"");
                  this.writer.write(this.getCurrentValue());
                  this.writer.write(34);
                  break;
               case 27:
                  this.writer.write("PUBLIC");
                  this.type = this.scanner.next();
                  if (this.type != 1) {
                     throw this.fatalError("space", (Object[])null);
                  }

                  this.type = this.scanner.next();
                  if (this.type != 25) {
                     throw this.fatalError("string", (Object[])null);
                  }

                  this.writer.write(" \"");
                  this.writer.write(this.getCurrentValue());
                  this.writer.write("\" \"");
                  this.type = this.scanner.next();
                  if (this.type != 1) {
                     throw this.fatalError("space", (Object[])null);
                  }

                  this.type = this.scanner.next();
                  if (this.type != 25) {
                     throw this.fatalError("string", (Object[])null);
                  }

                  this.writer.write(this.getCurrentValue());
                  this.writer.write(34);
            }

            this.type = this.scanner.next();
            if (this.type == 1) {
               this.writer.write(this.getCurrentValue());
               this.type = this.scanner.next();
               if (!var1 && this.type == 59) {
                  this.writer.write("NDATA");
                  this.type = this.scanner.next();
                  if (this.type != 1) {
                     throw this.fatalError("space", (Object[])null);
                  }

                  this.writer.write(this.getCurrentValue());
                  this.type = this.scanner.next();
                  if (this.type != 14) {
                     throw this.fatalError("name", (Object[])null);
                  }

                  this.writer.write(this.getCurrentValue());
                  this.type = this.scanner.next();
               }

               if (this.type == 1) {
                  this.writer.write(this.getCurrentValue());
                  this.type = this.scanner.next();
               }
            }

            if (this.type != 20) {
               throw this.fatalError("end", (Object[])null);
            } else {
               this.writer.write(62);
               this.type = this.scanner.next();
            }
         }
      }
   }

   protected void printElementDeclaration() throws TranscoderException, XMLException, IOException {
      this.writer.write("<!ELEMENT");
      this.type = this.scanner.next();
      if (this.type != 1) {
         throw this.fatalError("space", (Object[])null);
      } else {
         this.writer.write(this.getCurrentValue());
         this.type = this.scanner.next();
         switch (this.type) {
            case 14:
               this.writer.write(this.getCurrentValue());
               this.type = this.scanner.next();
               if (this.type != 1) {
                  throw this.fatalError("space", (Object[])null);
               } else {
                  this.writer.write(this.getCurrentValue());
                  switch (this.type = this.scanner.next()) {
                     case 35:
                        this.writer.write("EMPTY");
                        this.type = this.scanner.next();
                        break;
                     case 36:
                        this.writer.write("ANY");
                        this.type = this.scanner.next();
                        break;
                     case 40:
                        this.writer.write(40);
                        this.type = this.scanner.next();
                        if (this.type == 1) {
                           this.writer.write(this.getCurrentValue());
                           this.type = this.scanner.next();
                        }

                        label71:
                        switch (this.type) {
                           case 14:
                           case 40:
                              this.printChildren();
                              if (this.type != 41) {
                                 throw this.fatalError("right.brace", (Object[])null);
                              }

                              this.writer.write(41);
                              this.type = this.scanner.next();
                              if (this.type == 1) {
                                 this.writer.write(this.getCurrentValue());
                                 this.type = this.scanner.next();
                              }

                              switch (this.type) {
                                 case 37:
                                    this.writer.write(63);
                                    this.type = this.scanner.next();
                                    break label71;
                                 case 38:
                                    this.writer.write(43);
                                    this.type = this.scanner.next();
                                    break label71;
                                 case 39:
                                    this.writer.write(42);
                                    this.type = this.scanner.next();
                                 default:
                                    break label71;
                              }
                           case 44:
                              this.writer.write("#PCDATA");
                              this.type = this.scanner.next();

                              label68:
                              while(true) {
                                 while(true) {
                                    switch (this.type) {
                                       case 1:
                                          this.writer.write(this.getCurrentValue());
                                          this.type = this.scanner.next();
                                          break;
                                       case 41:
                                          this.writer.write(41);
                                          this.type = this.scanner.next();
                                          break label68;
                                       case 42:
                                          this.writer.write(124);
                                          this.type = this.scanner.next();
                                          if (this.type == 1) {
                                             this.writer.write(this.getCurrentValue());
                                             this.type = this.scanner.next();
                                          }

                                          if (this.type != 14) {
                                             throw this.fatalError("name", (Object[])null);
                                          }

                                          this.writer.write(this.getCurrentValue());
                                          this.type = this.scanner.next();
                                    }
                                 }
                              }
                        }
                  }

                  if (this.type == 1) {
                     this.writer.write(this.getCurrentValue());
                     this.type = this.scanner.next();
                  }

                  if (this.type != 20) {
                     throw this.fatalError("end", (Object[])null);
                  }

                  this.writer.write(62);
                  this.scanner.next();
                  return;
               }
            default:
               throw this.fatalError("name", (Object[])null);
         }
      }
   }

   protected void printChildren() throws TranscoderException, XMLException, IOException {
      int var1 = 0;

      while(true) {
         switch (this.type) {
            case 14:
               this.writer.write(this.getCurrentValue());
               this.type = this.scanner.next();
               break;
            case 40:
               this.writer.write(40);
               this.type = this.scanner.next();
               if (this.type == 1) {
                  this.writer.write(this.getCurrentValue());
                  this.type = this.scanner.next();
               }

               this.printChildren();
               if (this.type != 41) {
                  throw this.fatalError("right.brace", (Object[])null);
               }

               this.writer.write(41);
               this.type = this.scanner.next();
               break;
            default:
               throw new RuntimeException("Invalid XML");
         }

         if (this.type == 1) {
            this.writer.write(this.getCurrentValue());
            this.type = this.scanner.next();
         }

         switch (this.type) {
            case 37:
               this.writer.write(63);
               this.type = this.scanner.next();
               break;
            case 38:
               this.writer.write(43);
               this.type = this.scanner.next();
               break;
            case 39:
               this.writer.write(42);
               this.type = this.scanner.next();
            case 40:
            default:
               break;
            case 41:
               return;
         }

         if (this.type == 1) {
            this.writer.write(this.getCurrentValue());
            this.type = this.scanner.next();
         }

         switch (this.type) {
            case 42:
               if (var1 != 0 && var1 != this.type) {
                  throw new RuntimeException("Invalid XML");
               }

               this.writer.write(124);
               var1 = this.type;
               this.type = this.scanner.next();
               break;
            case 43:
               if (var1 != 0 && var1 != this.type) {
                  throw new RuntimeException("Invalid XML");
               }

               this.writer.write(44);
               var1 = this.type;
               this.type = this.scanner.next();
         }

         if (this.type == 1) {
            this.writer.write(this.getCurrentValue());
            this.type = this.scanner.next();
         }
      }
   }

   protected char[] getCurrentValue() {
      int var1 = this.scanner.getStart() + this.scanner.getStartOffset();
      int var2 = this.scanner.getEnd() + this.scanner.getEndOffset() - var1;
      char[] var3 = new char[var2];
      char[] var4 = this.scanner.getBuffer();
      System.arraycopy(var4, var1, var3, 0, var2);
      return var3;
   }

   protected TranscoderException fatalError(String var1, Object[] var2) throws TranscoderException {
      TranscoderException var3 = new TranscoderException(var1);
      this.errorHandler.fatalError(var3);
      return var3;
   }
}

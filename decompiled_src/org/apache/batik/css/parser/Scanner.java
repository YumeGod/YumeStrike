package org.apache.batik.css.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.apache.batik.util.io.NormalizingReader;
import org.apache.batik.util.io.StreamNormalizingReader;
import org.apache.batik.util.io.StringNormalizingReader;

public class Scanner {
   protected NormalizingReader reader;
   protected int current;
   protected char[] buffer = new char[128];
   protected int position;
   protected int type;
   protected int start;
   protected int end;
   protected int blankCharacters;

   public Scanner(Reader var1) throws ParseException {
      try {
         this.reader = new StreamNormalizingReader(var1);
         this.current = this.nextChar();
      } catch (IOException var3) {
         throw new ParseException(var3);
      }
   }

   public Scanner(InputStream var1, String var2) throws ParseException {
      try {
         this.reader = new StreamNormalizingReader(var1, var2);
         this.current = this.nextChar();
      } catch (IOException var4) {
         throw new ParseException(var4);
      }
   }

   public Scanner(String var1) throws ParseException {
      try {
         this.reader = new StringNormalizingReader(var1);
         this.current = this.nextChar();
      } catch (IOException var3) {
         throw new ParseException(var3);
      }
   }

   public int getLine() {
      return this.reader.getLine();
   }

   public int getColumn() {
      return this.reader.getColumn();
   }

   public char[] getBuffer() {
      return this.buffer;
   }

   public int getStart() {
      return this.start;
   }

   public int getEnd() {
      return this.end;
   }

   public void clearBuffer() {
      if (this.position <= 0) {
         this.position = 0;
      } else {
         this.buffer[0] = this.buffer[this.position - 1];
         this.position = 1;
      }

   }

   public int getType() {
      return this.type;
   }

   public String getStringValue() {
      return new String(this.buffer, this.start, this.end - this.start);
   }

   public void scanAtRule() throws ParseException {
      try {
         while(true) {
            switch (this.current) {
               case 123:
                  int var1 = 1;

                  label32:
                  do {
                     while(true) {
                        this.nextChar();
                        switch (this.current) {
                           case -1:
                              break label32;
                           case 123:
                              ++var1;
                              break;
                           case 125:
                              --var1;
                              continue label32;
                        }
                     }
                  } while(var1 > 0);
               case -1:
               case 59:
                  this.end = this.position;
                  return;
               default:
                  this.nextChar();
            }
         }
      } catch (IOException var2) {
         throw new ParseException(var2);
      }
   }

   public int next() throws ParseException {
      this.blankCharacters = 0;
      this.start = this.position - 1;
      this.nextToken();
      this.end = this.position - this.endGap();
      return this.type;
   }

   protected int endGap() {
      int var1 = this.current == -1 ? 0 : 1;
      switch (this.type) {
         case 18:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 44:
         case 45:
         case 46:
            var1 += 2;
            break;
         case 19:
         case 42:
         case 43:
         case 52:
            ++var1;
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 51:
         default:
            break;
         case 47:
         case 48:
         case 50:
            var1 += 3;
            break;
         case 49:
            var1 += 4;
      }

      return var1 + this.blankCharacters;
   }

   protected void nextToken() throws ParseException {
      try {
         switch (this.current) {
            case -1:
               this.type = 0;
               return;
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 11:
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
            case 29:
            case 30:
            case 31:
            case 36:
            case 37:
            case 38:
            case 63:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 92:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
            case 116:
            case 118:
            case 119:
            case 120:
            case 121:
            case 122:
            default:
               if (this.current == 92) {
                  do {
                     this.nextChar();
                     this.escape();
                  } while(this.current == 92);
               } else if (!ScannerUtilities.isCSSIdentifierStartCharacter((char)this.current)) {
                  this.nextChar();
                  throw new ParseException("identifier.character", this.reader.getLine(), this.reader.getColumn());
               }

               while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                  this.nextChar();

                  while(this.current == 92) {
                     this.nextChar();
                     this.escape();
                  }
               }

               if (this.current == 40) {
                  this.nextChar();
                  this.type = 52;
                  return;
               } else {
                  this.type = 20;
                  return;
               }
            case 9:
            case 10:
            case 12:
            case 13:
            case 32:
               do {
                  this.nextChar();
               } while(ScannerUtilities.isCSSSpace((char)this.current));

               this.type = 17;
               return;
            case 33:
               do {
                  this.nextChar();
               } while(this.current != -1 && ScannerUtilities.isCSSSpace((char)this.current));

               if (isEqualIgnoreCase(this.current, 'i') && isEqualIgnoreCase(this.nextChar(), 'm') && isEqualIgnoreCase(this.nextChar(), 'p') && isEqualIgnoreCase(this.nextChar(), 'o') && isEqualIgnoreCase(this.nextChar(), 'r') && isEqualIgnoreCase(this.nextChar(), 't') && isEqualIgnoreCase(this.nextChar(), 'a') && isEqualIgnoreCase(this.nextChar(), 'n') && isEqualIgnoreCase(this.nextChar(), 't')) {
                  this.nextChar();
                  this.type = 23;
                  return;
               } else {
                  if (this.current == -1) {
                     throw new ParseException("eof", this.reader.getLine(), this.reader.getColumn());
                  }

                  throw new ParseException("character", this.reader.getLine(), this.reader.getColumn());
               }
            case 34:
               this.type = this.string2();
               return;
            case 35:
               this.nextChar();
               if (!ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                  throw new ParseException("character", this.reader.getLine(), this.reader.getColumn());
               }

               this.start = this.position - 1;

               do {
                  this.nextChar();

                  while(this.current == 92) {
                     this.nextChar();
                     this.escape();
                  }
               } while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current));

               this.type = 27;
               return;
            case 39:
               this.type = this.string1();
               return;
            case 40:
               this.nextChar();
               this.type = 14;
               return;
            case 41:
               this.nextChar();
               this.type = 15;
               return;
            case 42:
               this.nextChar();
               this.type = 13;
               return;
            case 43:
               this.nextChar();
               this.type = 4;
               return;
            case 44:
               this.nextChar();
               this.type = 6;
               return;
            case 45:
               this.nextChar();
               if (this.current != 45) {
                  this.type = 5;
                  return;
               } else {
                  this.nextChar();
                  if (this.current == 62) {
                     this.nextChar();
                     this.type = 22;
                     return;
                  }

                  throw new ParseException("character", this.reader.getLine(), this.reader.getColumn());
               }
            case 46:
               switch (this.nextChar()) {
                  case 48:
                  case 49:
                  case 50:
                  case 51:
                  case 52:
                  case 53:
                  case 54:
                  case 55:
                  case 56:
                  case 57:
                     this.type = this.dotNumber();
                     return;
                  default:
                     this.type = 7;
                     return;
               }
            case 47:
               this.nextChar();
               if (this.current != 42) {
                  this.type = 10;
                  return;
               } else {
                  this.nextChar();
                  this.start = this.position - 1;

                  do {
                     while(this.current != -1 && this.current != 42) {
                        this.nextChar();
                     }

                     do {
                        this.nextChar();
                     } while(this.current != -1 && this.current == 42);
                  } while(this.current != -1 && this.current != 47);

                  if (this.current == -1) {
                     throw new ParseException("eof", this.reader.getLine(), this.reader.getColumn());
                  }

                  this.nextChar();
                  this.type = 18;
                  return;
               }
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
               this.type = this.number();
               return;
            case 58:
               this.nextChar();
               this.type = 16;
               return;
            case 59:
               this.nextChar();
               this.type = 8;
               return;
            case 60:
               this.nextChar();
               if (this.current != 33) {
                  throw new ParseException("character", this.reader.getLine(), this.reader.getColumn());
               } else {
                  this.nextChar();
                  if (this.current == 45) {
                     this.nextChar();
                     if (this.current == 45) {
                        this.nextChar();
                        this.type = 21;
                        return;
                     }
                  }

                  throw new ParseException("character", this.reader.getLine(), this.reader.getColumn());
               }
            case 61:
               this.nextChar();
               this.type = 3;
               return;
            case 62:
               this.nextChar();
               this.type = 9;
               return;
            case 64:
               this.nextChar();
               switch (this.current) {
                  case 67:
                  case 99:
                     this.start = this.position - 1;
                     if (isEqualIgnoreCase(this.nextChar(), 'h') && isEqualIgnoreCase(this.nextChar(), 'a') && isEqualIgnoreCase(this.nextChar(), 'r') && isEqualIgnoreCase(this.nextChar(), 's') && isEqualIgnoreCase(this.nextChar(), 'e') && isEqualIgnoreCase(this.nextChar(), 't')) {
                        this.nextChar();
                        this.type = 30;
                        return;
                     }
                     break;
                  case 70:
                  case 102:
                     this.start = this.position - 1;
                     if (isEqualIgnoreCase(this.nextChar(), 'o') && isEqualIgnoreCase(this.nextChar(), 'n') && isEqualIgnoreCase(this.nextChar(), 't') && isEqualIgnoreCase(this.nextChar(), '-') && isEqualIgnoreCase(this.nextChar(), 'f') && isEqualIgnoreCase(this.nextChar(), 'a') && isEqualIgnoreCase(this.nextChar(), 'c') && isEqualIgnoreCase(this.nextChar(), 'e')) {
                        this.nextChar();
                        this.type = 31;
                        return;
                     }
                     break;
                  case 73:
                  case 105:
                     this.start = this.position - 1;
                     if (isEqualIgnoreCase(this.nextChar(), 'm') && isEqualIgnoreCase(this.nextChar(), 'p') && isEqualIgnoreCase(this.nextChar(), 'o') && isEqualIgnoreCase(this.nextChar(), 'r') && isEqualIgnoreCase(this.nextChar(), 't')) {
                        this.nextChar();
                        this.type = 28;
                        return;
                     }
                     break;
                  case 77:
                  case 109:
                     this.start = this.position - 1;
                     if (isEqualIgnoreCase(this.nextChar(), 'e') && isEqualIgnoreCase(this.nextChar(), 'd') && isEqualIgnoreCase(this.nextChar(), 'i') && isEqualIgnoreCase(this.nextChar(), 'a')) {
                        this.nextChar();
                        this.type = 32;
                        return;
                     }
                     break;
                  case 80:
                  case 112:
                     this.start = this.position - 1;
                     if (isEqualIgnoreCase(this.nextChar(), 'a') && isEqualIgnoreCase(this.nextChar(), 'g') && isEqualIgnoreCase(this.nextChar(), 'e')) {
                        this.nextChar();
                        this.type = 33;
                        return;
                     }
                     break;
                  default:
                     if (!ScannerUtilities.isCSSIdentifierStartCharacter((char)this.current)) {
                        throw new ParseException("identifier.character", this.reader.getLine(), this.reader.getColumn());
                     }

                     this.start = this.position - 1;
               }

               do {
                  this.nextChar();

                  while(this.current == 92) {
                     this.nextChar();
                     this.escape();
                  }
               } while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current));

               this.type = 29;
               return;
            case 85:
            case 117:
               this.nextChar();
               switch (this.current) {
                  case 43:
                     boolean var1 = false;

                     for(int var2 = 0; var2 < 6; ++var2) {
                        this.nextChar();
                        switch (this.current) {
                           case 63:
                              var1 = true;
                              break;
                           default:
                              if (var1 && !ScannerUtilities.isCSSHexadecimalCharacter((char)this.current)) {
                                 throw new ParseException("character", this.reader.getLine(), this.reader.getColumn());
                              }
                        }
                     }

                     this.nextChar();
                     if (var1) {
                        this.type = 53;
                        return;
                     }

                     if (this.current == 45) {
                        this.nextChar();
                        if (!ScannerUtilities.isCSSHexadecimalCharacter((char)this.current)) {
                           throw new ParseException("character", this.reader.getLine(), this.reader.getColumn());
                        }

                        this.nextChar();
                        if (!ScannerUtilities.isCSSHexadecimalCharacter((char)this.current)) {
                           this.type = 53;
                           return;
                        }

                        this.nextChar();
                        if (!ScannerUtilities.isCSSHexadecimalCharacter((char)this.current)) {
                           this.type = 53;
                           return;
                        }

                        this.nextChar();
                        if (!ScannerUtilities.isCSSHexadecimalCharacter((char)this.current)) {
                           this.type = 53;
                           return;
                        }

                        this.nextChar();
                        if (!ScannerUtilities.isCSSHexadecimalCharacter((char)this.current)) {
                           this.type = 53;
                           return;
                        }

                        this.nextChar();
                        if (!ScannerUtilities.isCSSHexadecimalCharacter((char)this.current)) {
                           this.type = 53;
                           return;
                        }

                        this.nextChar();
                        this.type = 53;
                        return;
                     }
                  case 82:
                  case 114:
                     this.nextChar();
                     switch (this.current) {
                        case 76:
                        case 108:
                           this.nextChar();
                           switch (this.current) {
                              case 40:
                                 do {
                                    this.nextChar();
                                 } while(this.current != -1 && ScannerUtilities.isCSSSpace((char)this.current));

                                 switch (this.current) {
                                    case 34:
                                       this.string2();
                                       this.blankCharacters += 2;

                                       while(this.current != -1 && ScannerUtilities.isCSSSpace((char)this.current)) {
                                          ++this.blankCharacters;
                                          this.nextChar();
                                       }

                                       if (this.current == -1) {
                                          throw new ParseException("eof", this.reader.getLine(), this.reader.getColumn());
                                       }

                                       if (this.current != 41) {
                                          throw new ParseException("character", this.reader.getLine(), this.reader.getColumn());
                                       }

                                       this.nextChar();
                                       this.type = 51;
                                       return;
                                    case 39:
                                       this.string1();
                                       this.blankCharacters += 2;

                                       while(this.current != -1 && ScannerUtilities.isCSSSpace((char)this.current)) {
                                          ++this.blankCharacters;
                                          this.nextChar();
                                       }

                                       if (this.current == -1) {
                                          throw new ParseException("eof", this.reader.getLine(), this.reader.getColumn());
                                       }

                                       if (this.current != 41) {
                                          throw new ParseException("character", this.reader.getLine(), this.reader.getColumn());
                                       }

                                       this.nextChar();
                                       this.type = 51;
                                       return;
                                    case 41:
                                       throw new ParseException("character", this.reader.getLine(), this.reader.getColumn());
                                    default:
                                       if (!ScannerUtilities.isCSSURICharacter((char)this.current)) {
                                          throw new ParseException("character", this.reader.getLine(), this.reader.getColumn());
                                       }

                                       this.start = this.position - 1;

                                       do {
                                          this.nextChar();
                                       } while(this.current != -1 && ScannerUtilities.isCSSURICharacter((char)this.current));

                                       ++this.blankCharacters;

                                       while(this.current != -1 && ScannerUtilities.isCSSSpace((char)this.current)) {
                                          ++this.blankCharacters;
                                          this.nextChar();
                                       }

                                       if (this.current == -1) {
                                          throw new ParseException("eof", this.reader.getLine(), this.reader.getColumn());
                                       }

                                       if (this.current != 41) {
                                          throw new ParseException("character", this.reader.getLine(), this.reader.getColumn());
                                       }

                                       this.nextChar();
                                       this.type = 51;
                                       return;
                                 }
                           }
                     }
               }

               while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                  this.nextChar();
               }

               if (this.current == 40) {
                  this.nextChar();
                  this.type = 52;
                  return;
               }

               this.type = 20;
               return;
            case 91:
               this.nextChar();
               this.type = 11;
               return;
            case 93:
               this.nextChar();
               this.type = 12;
               return;
            case 123:
               this.nextChar();
               this.type = 1;
               return;
            case 124:
               this.nextChar();
               if (this.current == 61) {
                  this.nextChar();
                  this.type = 25;
                  return;
               }

               throw new ParseException("character", this.reader.getLine(), this.reader.getColumn());
            case 125:
               this.nextChar();
               this.type = 2;
               return;
            case 126:
               this.nextChar();
               if (this.current == 61) {
                  this.nextChar();
                  this.type = 26;
               } else {
                  throw new ParseException("character", this.reader.getLine(), this.reader.getColumn());
               }
         }
      } catch (IOException var3) {
         throw new ParseException(var3);
      }
   }

   protected int string1() throws IOException {
      this.start = this.position;

      while(true) {
         switch (this.nextChar()) {
            case -1:
               throw new ParseException("eof", this.reader.getLine(), this.reader.getColumn());
            case 34:
               break;
            case 39:
               this.nextChar();
               return 19;
            case 92:
               switch (this.nextChar()) {
                  case 10:
                  case 12:
                     continue;
                  default:
                     this.escape();
                     continue;
               }
            default:
               if (!ScannerUtilities.isCSSStringCharacter((char)this.current)) {
                  throw new ParseException("character", this.reader.getLine(), this.reader.getColumn());
               }
         }
      }
   }

   protected int string2() throws IOException {
      this.start = this.position;

      while(true) {
         switch (this.nextChar()) {
            case -1:
               throw new ParseException("eof", this.reader.getLine(), this.reader.getColumn());
            case 34:
               this.nextChar();
               return 19;
            case 39:
               break;
            case 92:
               switch (this.nextChar()) {
                  case 10:
                  case 12:
                     continue;
                  default:
                     this.escape();
                     continue;
               }
            default:
               if (!ScannerUtilities.isCSSStringCharacter((char)this.current)) {
                  throw new ParseException("character", this.reader.getLine(), this.reader.getColumn());
               }
         }
      }
   }

   protected int number() throws IOException {
      while(true) {
         switch (this.nextChar()) {
            case 46:
               switch (this.nextChar()) {
                  case 48:
                  case 49:
                  case 50:
                  case 51:
                  case 52:
                  case 53:
                  case 54:
                  case 55:
                  case 56:
                  case 57:
                     return this.dotNumber();
                  default:
                     throw new ParseException("character", this.reader.getLine(), this.reader.getColumn());
               }
            case 47:
            default:
               return this.numberUnit(true);
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
         }
      }
   }

   protected int dotNumber() throws IOException {
      while(true) {
         switch (this.nextChar()) {
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
               break;
            default:
               return this.numberUnit(false);
         }
      }
   }

   protected int numberUnit(boolean var1) throws IOException {
      switch (this.current) {
         case 37:
            this.nextChar();
            return 42;
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 70:
         case 74:
         case 76:
         case 78:
         case 79:
         case 81:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 102:
         case 106:
         case 108:
         case 110:
         case 111:
         case 113:
         default:
            if (this.current != -1 && ScannerUtilities.isCSSIdentifierStartCharacter((char)this.current)) {
               do {
                  this.nextChar();
               } while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current));

               return 34;
            } else {
               return var1 ? 24 : 54;
            }
         case 67:
         case 99:
            switch (this.nextChar()) {
               case 77:
               case 109:
                  this.nextChar();
                  if (this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                     do {
                        this.nextChar();
                     } while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current));

                     return 34;
                  }

                  return 37;
               default:
                  while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                     this.nextChar();
                  }

                  return 34;
            }
         case 68:
         case 100:
            switch (this.nextChar()) {
               case 69:
               case 101:
                  switch (this.nextChar()) {
                     case 71:
                     case 103:
                        this.nextChar();
                        if (this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                           do {
                              this.nextChar();
                           } while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current));

                           return 34;
                        }

                        return 47;
                  }
            }

            while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
               this.nextChar();
            }

            return 34;
         case 69:
         case 101:
            switch (this.nextChar()) {
               case 77:
               case 109:
                  this.nextChar();
                  if (this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                     do {
                        this.nextChar();
                     } while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current));

                     return 34;
                  }

                  return 36;
               case 88:
               case 120:
                  this.nextChar();
                  if (this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                     do {
                        this.nextChar();
                     } while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current));

                     return 34;
                  }

                  return 35;
               default:
                  while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                     this.nextChar();
                  }

                  return 34;
            }
         case 71:
         case 103:
            switch (this.nextChar()) {
               case 82:
               case 114:
                  switch (this.nextChar()) {
                     case 65:
                     case 97:
                        switch (this.nextChar()) {
                           case 68:
                           case 100:
                              this.nextChar();
                              if (this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                                 do {
                                    this.nextChar();
                                 } while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current));

                                 return 34;
                              }

                              return 49;
                        }
                  }
            }

            while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
               this.nextChar();
            }

            return 34;
         case 72:
         case 104:
            this.nextChar();
            switch (this.current) {
               case 90:
               case 122:
                  this.nextChar();
                  if (this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                     do {
                        this.nextChar();
                     } while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current));

                     return 34;
                  }

                  return 41;
               default:
                  while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                     this.nextChar();
                  }

                  return 34;
            }
         case 73:
         case 105:
            switch (this.nextChar()) {
               case 78:
               case 110:
                  this.nextChar();
                  if (this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                     do {
                        this.nextChar();
                     } while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current));

                     return 34;
                  }

                  return 39;
               default:
                  while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                     this.nextChar();
                  }

                  return 34;
            }
         case 75:
         case 107:
            switch (this.nextChar()) {
               case 72:
               case 104:
                  switch (this.nextChar()) {
                     case 90:
                     case 122:
                        this.nextChar();
                        if (this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                           do {
                              this.nextChar();
                           } while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current));

                           return 34;
                        }

                        return 50;
                  }
            }

            while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
               this.nextChar();
            }

            return 34;
         case 77:
         case 109:
            switch (this.nextChar()) {
               case 77:
               case 109:
                  this.nextChar();
                  if (this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                     do {
                        this.nextChar();
                     } while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current));

                     return 34;
                  }

                  return 38;
               case 83:
               case 115:
                  this.nextChar();
                  if (this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                     do {
                        this.nextChar();
                     } while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current));

                     return 34;
                  }

                  return 40;
               default:
                  while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                     this.nextChar();
                  }

                  return 34;
            }
         case 80:
         case 112:
            switch (this.nextChar()) {
               case 67:
               case 99:
                  this.nextChar();
                  if (this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                     do {
                        this.nextChar();
                     } while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current));

                     return 34;
                  }

                  return 44;
               case 84:
               case 116:
                  this.nextChar();
                  if (this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                     do {
                        this.nextChar();
                     } while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current));

                     return 34;
                  }

                  return 45;
               case 88:
               case 120:
                  this.nextChar();
                  if (this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                     do {
                        this.nextChar();
                     } while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current));

                     return 34;
                  }

                  return 46;
               default:
                  while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                     this.nextChar();
                  }

                  return 34;
            }
         case 82:
         case 114:
            switch (this.nextChar()) {
               case 65:
               case 97:
                  switch (this.nextChar()) {
                     case 68:
                     case 100:
                        this.nextChar();
                        if (this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
                           do {
                              this.nextChar();
                           } while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current));

                           return 34;
                        }

                        return 48;
                  }
            }

            while(this.current != -1 && ScannerUtilities.isCSSNameCharacter((char)this.current)) {
               this.nextChar();
            }

            return 34;
         case 83:
         case 115:
            this.nextChar();
            return 43;
      }
   }

   protected void escape() throws IOException {
      if (ScannerUtilities.isCSSHexadecimalCharacter((char)this.current)) {
         this.nextChar();
         if (!ScannerUtilities.isCSSHexadecimalCharacter((char)this.current)) {
            if (ScannerUtilities.isCSSSpace((char)this.current)) {
               this.nextChar();
            }

            return;
         }

         this.nextChar();
         if (!ScannerUtilities.isCSSHexadecimalCharacter((char)this.current)) {
            if (ScannerUtilities.isCSSSpace((char)this.current)) {
               this.nextChar();
            }

            return;
         }

         this.nextChar();
         if (!ScannerUtilities.isCSSHexadecimalCharacter((char)this.current)) {
            if (ScannerUtilities.isCSSSpace((char)this.current)) {
               this.nextChar();
            }

            return;
         }

         this.nextChar();
         if (!ScannerUtilities.isCSSHexadecimalCharacter((char)this.current)) {
            if (ScannerUtilities.isCSSSpace((char)this.current)) {
               this.nextChar();
            }

            return;
         }

         this.nextChar();
         if (!ScannerUtilities.isCSSHexadecimalCharacter((char)this.current)) {
            if (ScannerUtilities.isCSSSpace((char)this.current)) {
               this.nextChar();
            }

            return;
         }
      }

      if ((this.current < 32 || this.current > 126) && this.current < 128) {
         throw new ParseException("character", this.reader.getLine(), this.reader.getColumn());
      } else {
         this.nextChar();
      }
   }

   protected static boolean isEqualIgnoreCase(int var0, char var1) {
      return var0 == -1 ? false : Character.toLowerCase((char)var0) == var1;
   }

   protected int nextChar() throws IOException {
      this.current = this.reader.read();
      if (this.current == -1) {
         return this.current;
      } else {
         if (this.position == this.buffer.length) {
            char[] var1 = new char[1 + this.position + this.position / 2];
            System.arraycopy(this.buffer, 0, var1, 0, this.position);
            this.buffer = var1;
         }

         return this.buffer[this.position++] = (char)this.current;
      }
   }
}

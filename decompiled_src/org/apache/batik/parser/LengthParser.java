package org.apache.batik.parser;

import java.io.IOException;

public class LengthParser extends AbstractParser {
   protected LengthHandler lengthHandler;

   public LengthParser() {
      this.lengthHandler = DefaultLengthHandler.INSTANCE;
   }

   public void setLengthHandler(LengthHandler var1) {
      this.lengthHandler = var1;
   }

   public LengthHandler getLengthHandler() {
      return this.lengthHandler;
   }

   protected void doParse() throws ParseException, IOException {
      this.lengthHandler.startLength();
      this.current = this.reader.read();
      this.skipSpaces();
      this.parseLength();
      this.skipSpaces();
      if (this.current != -1) {
         this.reportError("end.of.stream.expected", new Object[]{new Integer(this.current)});
      }

      this.lengthHandler.endLength();
   }

   protected void parseLength() throws ParseException, IOException {
      int var1 = 0;
      int var2 = 0;
      boolean var3 = true;
      boolean var4 = false;
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      boolean var8 = true;
      byte var9 = 0;
      switch (this.current) {
         case 45:
            var3 = false;
         case 43:
            this.current = this.reader.read();
      }

      label182:
      switch (this.current) {
         case 46:
            break;
         case 47:
         default:
            this.reportUnexpectedCharacterError(this.current);
            return;
         case 48:
            var4 = true;

            label180:
            while(true) {
               this.current = this.reader.read();
               switch (this.current) {
                  case 48:
                     break;
                  case 49:
                  case 50:
                  case 51:
                  case 52:
                  case 53:
                  case 54:
                  case 55:
                  case 56:
                  case 57:
                     break label180;
                  default:
                     break label182;
               }
            }
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
            var4 = true;

            label167:
            while(true) {
               if (var2 < 9) {
                  ++var2;
                  var1 = var1 * 10 + (this.current - 48);
               } else {
                  ++var7;
               }

               this.current = this.reader.read();
               switch (this.current) {
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
                     break label167;
               }
            }
      }

      if (this.current == 46) {
         this.current = this.reader.read();
         label157:
         switch (this.current) {
            case 48:
               if (var2 == 0) {
                  label155:
                  while(true) {
                     this.current = this.reader.read();
                     --var7;
                     switch (this.current) {
                        case 48:
                           break;
                        case 49:
                        case 50:
                        case 51:
                        case 52:
                        case 53:
                        case 54:
                        case 55:
                        case 56:
                        case 57:
                           break label155;
                        default:
                           break label157;
                     }
                  }
               }
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
               while(true) {
                  if (var2 < 9) {
                     ++var2;
                     var1 = var1 * 10 + (this.current - 48);
                     --var7;
                  }

                  this.current = this.reader.read();
                  switch (this.current) {
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
                        break label157;
                  }
               }
            case 69:
            case 101:
            default:
               if (!var4) {
                  this.reportUnexpectedCharacterError(this.current);
                  return;
               }
         }
      }

      boolean var10 = false;
      switch (this.current) {
         case 101:
            var10 = true;
         case 69:
            this.current = this.reader.read();
            label136:
            switch (this.current) {
               case 45:
                  var8 = false;
               case 43:
                  this.current = this.reader.read();
                  switch (this.current) {
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
                        this.reportUnexpectedCharacterError(this.current);
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
                  switch (this.current) {
                     case 48:
                        label132:
                        while(true) {
                           this.current = this.reader.read();
                           switch (this.current) {
                              case 48:
                                 break;
                              case 49:
                              case 50:
                              case 51:
                              case 52:
                              case 53:
                              case 54:
                              case 55:
                              case 56:
                              case 57:
                                 break label132;
                              default:
                                 break label136;
                           }
                        }
                     case 49:
                     case 50:
                     case 51:
                     case 52:
                     case 53:
                     case 54:
                     case 55:
                     case 56:
                     case 57:
                        while(true) {
                           if (var6 < 3) {
                              ++var6;
                              var5 = var5 * 10 + (this.current - 48);
                           }

                           this.current = this.reader.read();
                           switch (this.current) {
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
                                 break label136;
                           }
                        }
                     default:
                        break label136;
                  }
               case 109:
                  if (!var10) {
                     this.reportUnexpectedCharacterError(this.current);
                     return;
                  }

                  var9 = 1;
                  break;
               case 120:
                  if (!var10) {
                     this.reportUnexpectedCharacterError(this.current);
                     return;
                  }

                  var9 = 2;
                  break;
               default:
                  this.reportUnexpectedCharacterError(this.current);
                  return;
            }
         default:
            if (!var8) {
               var5 = -var5;
            }

            var5 += var7;
            if (!var3) {
               var1 = -var1;
            }

            this.lengthHandler.lengthValue(NumberParser.buildFloat(var1, var5));
            switch (var9) {
               case 1:
                  this.lengthHandler.em();
                  this.current = this.reader.read();
                  return;
               case 2:
                  this.lengthHandler.ex();
                  this.current = this.reader.read();
                  return;
               default:
                  switch (this.current) {
                     case 37:
                        this.lengthHandler.percentage();
                        this.current = this.reader.read();
                        break;
                     case 99:
                        this.current = this.reader.read();
                        if (this.current != 109) {
                           this.reportCharacterExpectedError('m', this.current);
                        } else {
                           this.lengthHandler.cm();
                           this.current = this.reader.read();
                        }
                        break;
                     case 101:
                        this.current = this.reader.read();
                        switch (this.current) {
                           case 109:
                              this.lengthHandler.em();
                              this.current = this.reader.read();
                              return;
                           case 120:
                              this.lengthHandler.ex();
                              this.current = this.reader.read();
                              return;
                           default:
                              this.reportUnexpectedCharacterError(this.current);
                              return;
                        }
                     case 105:
                        this.current = this.reader.read();
                        if (this.current != 110) {
                           this.reportCharacterExpectedError('n', this.current);
                        } else {
                           this.lengthHandler.in();
                           this.current = this.reader.read();
                        }
                        break;
                     case 109:
                        this.current = this.reader.read();
                        if (this.current != 109) {
                           this.reportCharacterExpectedError('m', this.current);
                        } else {
                           this.lengthHandler.mm();
                           this.current = this.reader.read();
                        }
                        break;
                     case 112:
                        this.current = this.reader.read();
                        switch (this.current) {
                           case 99:
                              this.lengthHandler.pc();
                              this.current = this.reader.read();
                              break;
                           case 116:
                              this.lengthHandler.pt();
                              this.current = this.reader.read();
                              break;
                           case 120:
                              this.lengthHandler.px();
                              this.current = this.reader.read();
                              break;
                           default:
                              this.reportUnexpectedCharacterError(this.current);
                        }
                  }

            }
      }
   }
}

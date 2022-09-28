package org.apache.batik.parser;

import java.io.IOException;

public class PreserveAspectRatioParser extends AbstractParser {
   protected PreserveAspectRatioHandler preserveAspectRatioHandler;

   public PreserveAspectRatioParser() {
      this.preserveAspectRatioHandler = DefaultPreserveAspectRatioHandler.INSTANCE;
   }

   public void setPreserveAspectRatioHandler(PreserveAspectRatioHandler var1) {
      this.preserveAspectRatioHandler = var1;
   }

   public PreserveAspectRatioHandler getPreserveAspectRatioHandler() {
      return this.preserveAspectRatioHandler;
   }

   protected void doParse() throws ParseException, IOException {
      this.current = this.reader.read();
      this.skipSpaces();
      this.parsePreserveAspectRatio();
   }

   protected void parsePreserveAspectRatio() throws ParseException, IOException {
      this.preserveAspectRatioHandler.startPreserveAspectRatio();
      label124:
      switch (this.current) {
         case 110:
            this.current = this.reader.read();
            if (this.current != 111) {
               this.reportCharacterExpectedError('o', this.current);
               this.skipIdentifier();
            } else {
               this.current = this.reader.read();
               if (this.current != 110) {
                  this.reportCharacterExpectedError('o', this.current);
                  this.skipIdentifier();
               } else {
                  this.current = this.reader.read();
                  if (this.current != 101) {
                     this.reportCharacterExpectedError('e', this.current);
                     this.skipIdentifier();
                  } else {
                     this.current = this.reader.read();
                     this.skipSpaces();
                     this.preserveAspectRatioHandler.none();
                  }
               }
            }
            break;
         case 120:
            this.current = this.reader.read();
            if (this.current != 77) {
               this.reportCharacterExpectedError('M', this.current);
               this.skipIdentifier();
            } else {
               this.current = this.reader.read();
               switch (this.current) {
                  case 97:
                     this.current = this.reader.read();
                     if (this.current != 120) {
                        this.reportCharacterExpectedError('x', this.current);
                        this.skipIdentifier();
                     } else {
                        this.current = this.reader.read();
                        if (this.current != 89) {
                           this.reportCharacterExpectedError('Y', this.current);
                           this.skipIdentifier();
                        } else {
                           this.current = this.reader.read();
                           if (this.current != 77) {
                              this.reportCharacterExpectedError('M', this.current);
                              this.skipIdentifier();
                           } else {
                              this.current = this.reader.read();
                              switch (this.current) {
                                 case 97:
                                    this.current = this.reader.read();
                                    if (this.current != 120) {
                                       this.reportCharacterExpectedError('x', this.current);
                                       this.skipIdentifier();
                                    } else {
                                       this.preserveAspectRatioHandler.xMaxYMax();
                                       this.current = this.reader.read();
                                    }
                                    break label124;
                                 case 105:
                                    this.current = this.reader.read();
                                    switch (this.current) {
                                       case 100:
                                          this.preserveAspectRatioHandler.xMaxYMid();
                                          this.current = this.reader.read();
                                          break label124;
                                       case 110:
                                          this.preserveAspectRatioHandler.xMaxYMin();
                                          this.current = this.reader.read();
                                          break label124;
                                       default:
                                          this.reportUnexpectedCharacterError(this.current);
                                          this.skipIdentifier();
                                    }
                              }
                           }
                        }
                     }
                     break label124;
                  case 105:
                     this.current = this.reader.read();
                     switch (this.current) {
                        case 100:
                           this.current = this.reader.read();
                           if (this.current != 89) {
                              this.reportCharacterExpectedError('Y', this.current);
                              this.skipIdentifier();
                           } else {
                              this.current = this.reader.read();
                              if (this.current != 77) {
                                 this.reportCharacterExpectedError('M', this.current);
                                 this.skipIdentifier();
                              } else {
                                 this.current = this.reader.read();
                                 switch (this.current) {
                                    case 97:
                                       this.current = this.reader.read();
                                       if (this.current != 120) {
                                          this.reportCharacterExpectedError('x', this.current);
                                          this.skipIdentifier();
                                       } else {
                                          this.preserveAspectRatioHandler.xMidYMax();
                                          this.current = this.reader.read();
                                       }
                                       break label124;
                                    case 105:
                                       this.current = this.reader.read();
                                       switch (this.current) {
                                          case 100:
                                             this.preserveAspectRatioHandler.xMidYMid();
                                             this.current = this.reader.read();
                                             break label124;
                                          case 110:
                                             this.preserveAspectRatioHandler.xMidYMin();
                                             this.current = this.reader.read();
                                             break label124;
                                          default:
                                             this.reportUnexpectedCharacterError(this.current);
                                             this.skipIdentifier();
                                       }
                                 }
                              }
                           }
                           break label124;
                        case 110:
                           this.current = this.reader.read();
                           if (this.current != 89) {
                              this.reportCharacterExpectedError('Y', this.current);
                              this.skipIdentifier();
                           } else {
                              this.current = this.reader.read();
                              if (this.current != 77) {
                                 this.reportCharacterExpectedError('M', this.current);
                                 this.skipIdentifier();
                              } else {
                                 this.current = this.reader.read();
                                 switch (this.current) {
                                    case 97:
                                       this.current = this.reader.read();
                                       if (this.current != 120) {
                                          this.reportCharacterExpectedError('x', this.current);
                                          this.skipIdentifier();
                                       } else {
                                          this.preserveAspectRatioHandler.xMinYMax();
                                          this.current = this.reader.read();
                                       }
                                       break label124;
                                    case 105:
                                       this.current = this.reader.read();
                                       switch (this.current) {
                                          case 100:
                                             this.preserveAspectRatioHandler.xMinYMid();
                                             this.current = this.reader.read();
                                             break label124;
                                          case 110:
                                             this.preserveAspectRatioHandler.xMinYMin();
                                             this.current = this.reader.read();
                                             break label124;
                                          default:
                                             this.reportUnexpectedCharacterError(this.current);
                                             this.skipIdentifier();
                                       }
                                 }
                              }
                           }
                           break label124;
                        default:
                           this.reportUnexpectedCharacterError(this.current);
                           this.skipIdentifier();
                           break label124;
                     }
                  default:
                     this.reportUnexpectedCharacterError(this.current);
                     this.skipIdentifier();
               }
            }
            break;
         default:
            if (this.current != -1) {
               this.reportUnexpectedCharacterError(this.current);
               this.skipIdentifier();
            }
      }

      this.skipCommaSpaces();
      switch (this.current) {
         case 109:
            this.current = this.reader.read();
            if (this.current != 101) {
               this.reportCharacterExpectedError('e', this.current);
               this.skipIdentifier();
            } else {
               this.current = this.reader.read();
               if (this.current != 101) {
                  this.reportCharacterExpectedError('e', this.current);
                  this.skipIdentifier();
               } else {
                  this.current = this.reader.read();
                  if (this.current != 116) {
                     this.reportCharacterExpectedError('t', this.current);
                     this.skipIdentifier();
                  } else {
                     this.preserveAspectRatioHandler.meet();
                     this.current = this.reader.read();
                  }
               }
            }
            break;
         case 115:
            this.current = this.reader.read();
            if (this.current != 108) {
               this.reportCharacterExpectedError('l', this.current);
               this.skipIdentifier();
            } else {
               this.current = this.reader.read();
               if (this.current != 105) {
                  this.reportCharacterExpectedError('i', this.current);
                  this.skipIdentifier();
               } else {
                  this.current = this.reader.read();
                  if (this.current != 99) {
                     this.reportCharacterExpectedError('c', this.current);
                     this.skipIdentifier();
                  } else {
                     this.current = this.reader.read();
                     if (this.current != 101) {
                        this.reportCharacterExpectedError('e', this.current);
                        this.skipIdentifier();
                     } else {
                        this.preserveAspectRatioHandler.slice();
                        this.current = this.reader.read();
                     }
                  }
               }
            }
            break;
         default:
            if (this.current != -1) {
               this.reportUnexpectedCharacterError(this.current);
               this.skipIdentifier();
            }
      }

      this.skipSpaces();
      if (this.current != -1) {
         this.reportError("end.of.stream.expected", new Object[]{new Integer(this.current)});
      }

      this.preserveAspectRatioHandler.endPreserveAspectRatio();
   }

   protected void skipIdentifier() throws IOException {
      while(true) {
         this.current = this.reader.read();
         switch (this.current) {
            case 9:
            case 10:
            case 13:
            case 32:
               this.current = this.reader.read();
               break;
            default:
               if (this.current != -1) {
                  continue;
               }
         }

         return;
      }
   }
}

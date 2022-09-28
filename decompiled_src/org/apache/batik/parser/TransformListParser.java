package org.apache.batik.parser;

import java.io.IOException;

public class TransformListParser extends NumberParser {
   protected TransformListHandler transformListHandler;

   public TransformListParser() {
      this.transformListHandler = DefaultTransformListHandler.INSTANCE;
   }

   public void setTransformListHandler(TransformListHandler var1) {
      this.transformListHandler = var1;
   }

   public TransformListHandler getTransformListHandler() {
      return this.transformListHandler;
   }

   protected void doParse() throws ParseException, IOException {
      this.transformListHandler.startTransformList();

      label35:
      while(true) {
         try {
            this.current = this.reader.read();
            switch (this.current) {
               case -1:
                  break label35;
               case 9:
               case 10:
               case 13:
               case 32:
               case 44:
                  break;
               case 109:
                  this.parseMatrix();
                  break;
               case 114:
                  this.parseRotate();
                  break;
               case 115:
                  this.current = this.reader.read();
                  switch (this.current) {
                     case 99:
                        this.parseScale();
                        continue;
                     case 107:
                        this.parseSkew();
                        continue;
                     default:
                        this.reportUnexpectedCharacterError(this.current);
                        this.skipTransform();
                        continue;
                  }
               case 116:
                  this.parseTranslate();
                  break;
               default:
                  this.reportUnexpectedCharacterError(this.current);
                  this.skipTransform();
            }
         } catch (ParseException var2) {
            this.errorHandler.error(var2);
            this.skipTransform();
         }
      }

      this.skipSpaces();
      if (this.current != -1) {
         this.reportError("end.of.stream.expected", new Object[]{new Integer(this.current)});
      }

      this.transformListHandler.endTransformList();
   }

   protected void parseMatrix() throws ParseException, IOException {
      this.current = this.reader.read();
      if (this.current != 97) {
         this.reportCharacterExpectedError('a', this.current);
         this.skipTransform();
      } else {
         this.current = this.reader.read();
         if (this.current != 116) {
            this.reportCharacterExpectedError('t', this.current);
            this.skipTransform();
         } else {
            this.current = this.reader.read();
            if (this.current != 114) {
               this.reportCharacterExpectedError('r', this.current);
               this.skipTransform();
            } else {
               this.current = this.reader.read();
               if (this.current != 105) {
                  this.reportCharacterExpectedError('i', this.current);
                  this.skipTransform();
               } else {
                  this.current = this.reader.read();
                  if (this.current != 120) {
                     this.reportCharacterExpectedError('x', this.current);
                     this.skipTransform();
                  } else {
                     this.current = this.reader.read();
                     this.skipSpaces();
                     if (this.current != 40) {
                        this.reportCharacterExpectedError('(', this.current);
                        this.skipTransform();
                     } else {
                        this.current = this.reader.read();
                        this.skipSpaces();
                        float var1 = this.parseFloat();
                        this.skipCommaSpaces();
                        float var2 = this.parseFloat();
                        this.skipCommaSpaces();
                        float var3 = this.parseFloat();
                        this.skipCommaSpaces();
                        float var4 = this.parseFloat();
                        this.skipCommaSpaces();
                        float var5 = this.parseFloat();
                        this.skipCommaSpaces();
                        float var6 = this.parseFloat();
                        this.skipSpaces();
                        if (this.current != 41) {
                           this.reportCharacterExpectedError(')', this.current);
                           this.skipTransform();
                        } else {
                           this.transformListHandler.matrix(var1, var2, var3, var4, var5, var6);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   protected void parseRotate() throws ParseException, IOException {
      this.current = this.reader.read();
      if (this.current != 111) {
         this.reportCharacterExpectedError('o', this.current);
         this.skipTransform();
      } else {
         this.current = this.reader.read();
         if (this.current != 116) {
            this.reportCharacterExpectedError('t', this.current);
            this.skipTransform();
         } else {
            this.current = this.reader.read();
            if (this.current != 97) {
               this.reportCharacterExpectedError('a', this.current);
               this.skipTransform();
            } else {
               this.current = this.reader.read();
               if (this.current != 116) {
                  this.reportCharacterExpectedError('t', this.current);
                  this.skipTransform();
               } else {
                  this.current = this.reader.read();
                  if (this.current != 101) {
                     this.reportCharacterExpectedError('e', this.current);
                     this.skipTransform();
                  } else {
                     this.current = this.reader.read();
                     this.skipSpaces();
                     if (this.current != 40) {
                        this.reportCharacterExpectedError('(', this.current);
                        this.skipTransform();
                     } else {
                        this.current = this.reader.read();
                        this.skipSpaces();
                        float var1 = this.parseFloat();
                        this.skipSpaces();
                        switch (this.current) {
                           case 41:
                              this.transformListHandler.rotate(var1);
                              return;
                           case 44:
                              this.current = this.reader.read();
                              this.skipSpaces();
                           default:
                              float var2 = this.parseFloat();
                              this.skipCommaSpaces();
                              float var3 = this.parseFloat();
                              this.skipSpaces();
                              if (this.current != 41) {
                                 this.reportCharacterExpectedError(')', this.current);
                                 this.skipTransform();
                              } else {
                                 this.transformListHandler.rotate(var1, var2, var3);
                              }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   protected void parseTranslate() throws ParseException, IOException {
      this.current = this.reader.read();
      if (this.current != 114) {
         this.reportCharacterExpectedError('r', this.current);
         this.skipTransform();
      } else {
         this.current = this.reader.read();
         if (this.current != 97) {
            this.reportCharacterExpectedError('a', this.current);
            this.skipTransform();
         } else {
            this.current = this.reader.read();
            if (this.current != 110) {
               this.reportCharacterExpectedError('n', this.current);
               this.skipTransform();
            } else {
               this.current = this.reader.read();
               if (this.current != 115) {
                  this.reportCharacterExpectedError('s', this.current);
                  this.skipTransform();
               } else {
                  this.current = this.reader.read();
                  if (this.current != 108) {
                     this.reportCharacterExpectedError('l', this.current);
                     this.skipTransform();
                  } else {
                     this.current = this.reader.read();
                     if (this.current != 97) {
                        this.reportCharacterExpectedError('a', this.current);
                        this.skipTransform();
                     } else {
                        this.current = this.reader.read();
                        if (this.current != 116) {
                           this.reportCharacterExpectedError('t', this.current);
                           this.skipTransform();
                        } else {
                           this.current = this.reader.read();
                           if (this.current != 101) {
                              this.reportCharacterExpectedError('e', this.current);
                              this.skipTransform();
                           } else {
                              this.current = this.reader.read();
                              this.skipSpaces();
                              if (this.current != 40) {
                                 this.reportCharacterExpectedError('(', this.current);
                                 this.skipTransform();
                              } else {
                                 this.current = this.reader.read();
                                 this.skipSpaces();
                                 float var1 = this.parseFloat();
                                 this.skipSpaces();
                                 switch (this.current) {
                                    case 41:
                                       this.transformListHandler.translate(var1);
                                       return;
                                    case 44:
                                       this.current = this.reader.read();
                                       this.skipSpaces();
                                    default:
                                       float var2 = this.parseFloat();
                                       this.skipSpaces();
                                       if (this.current != 41) {
                                          this.reportCharacterExpectedError(')', this.current);
                                          this.skipTransform();
                                       } else {
                                          this.transformListHandler.translate(var1, var2);
                                       }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   protected void parseScale() throws ParseException, IOException {
      this.current = this.reader.read();
      if (this.current != 97) {
         this.reportCharacterExpectedError('a', this.current);
         this.skipTransform();
      } else {
         this.current = this.reader.read();
         if (this.current != 108) {
            this.reportCharacterExpectedError('l', this.current);
            this.skipTransform();
         } else {
            this.current = this.reader.read();
            if (this.current != 101) {
               this.reportCharacterExpectedError('e', this.current);
               this.skipTransform();
            } else {
               this.current = this.reader.read();
               this.skipSpaces();
               if (this.current != 40) {
                  this.reportCharacterExpectedError('(', this.current);
                  this.skipTransform();
               } else {
                  this.current = this.reader.read();
                  this.skipSpaces();
                  float var1 = this.parseFloat();
                  this.skipSpaces();
                  switch (this.current) {
                     case 41:
                        this.transformListHandler.scale(var1);
                        return;
                     case 44:
                        this.current = this.reader.read();
                        this.skipSpaces();
                     default:
                        float var2 = this.parseFloat();
                        this.skipSpaces();
                        if (this.current != 41) {
                           this.reportCharacterExpectedError(')', this.current);
                           this.skipTransform();
                        } else {
                           this.transformListHandler.scale(var1, var2);
                        }
                  }
               }
            }
         }
      }
   }

   protected void parseSkew() throws ParseException, IOException {
      this.current = this.reader.read();
      if (this.current != 101) {
         this.reportCharacterExpectedError('e', this.current);
         this.skipTransform();
      } else {
         this.current = this.reader.read();
         if (this.current != 119) {
            this.reportCharacterExpectedError('w', this.current);
            this.skipTransform();
         } else {
            this.current = this.reader.read();
            boolean var1 = false;
            switch (this.current) {
               case 88:
                  var1 = true;
               case 89:
                  this.current = this.reader.read();
                  this.skipSpaces();
                  if (this.current != 40) {
                     this.reportCharacterExpectedError('(', this.current);
                     this.skipTransform();
                     return;
                  } else {
                     this.current = this.reader.read();
                     this.skipSpaces();
                     float var2 = this.parseFloat();
                     this.skipSpaces();
                     if (this.current != 41) {
                        this.reportCharacterExpectedError(')', this.current);
                        this.skipTransform();
                        return;
                     }

                     if (var1) {
                        this.transformListHandler.skewX(var2);
                     } else {
                        this.transformListHandler.skewY(var2);
                     }

                     return;
                  }
               default:
                  this.reportCharacterExpectedError('X', this.current);
                  this.skipTransform();
            }
         }
      }
   }

   protected void skipTransform() throws IOException {
      while(true) {
         this.current = this.reader.read();
         switch (this.current) {
            default:
               if (this.current != -1) {
                  break;
               }
            case 41:
               return;
         }
      }
   }
}

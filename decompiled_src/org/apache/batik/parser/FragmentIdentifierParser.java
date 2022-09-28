package org.apache.batik.parser;

import java.io.IOException;
import org.apache.batik.xml.XMLUtilities;

public class FragmentIdentifierParser extends NumberParser {
   protected char[] buffer = new char[16];
   protected int bufferSize;
   protected FragmentIdentifierHandler fragmentIdentifierHandler;

   public FragmentIdentifierParser() {
      this.fragmentIdentifierHandler = DefaultFragmentIdentifierHandler.INSTANCE;
   }

   public void setFragmentIdentifierHandler(FragmentIdentifierHandler var1) {
      this.fragmentIdentifierHandler = var1;
   }

   public FragmentIdentifierHandler getFragmentIdentifierHandler() {
      return this.fragmentIdentifierHandler;
   }

   protected void doParse() throws ParseException, IOException {
      label92: {
         this.bufferSize = 0;
         this.current = this.reader.read();
         this.fragmentIdentifierHandler.startFragmentIdentifier();
         String var1 = null;
         switch (this.current) {
            case 115:
               this.bufferize();
               this.current = this.reader.read();
               if (this.current != 118) {
                  this.parseIdentifier();
               } else {
                  this.bufferize();
                  this.current = this.reader.read();
                  if (this.current != 103) {
                     this.parseIdentifier();
                  } else {
                     this.bufferize();
                     this.current = this.reader.read();
                     if (this.current != 86) {
                        this.parseIdentifier();
                     } else {
                        this.bufferize();
                        this.current = this.reader.read();
                        if (this.current != 105) {
                           this.parseIdentifier();
                        } else {
                           this.bufferize();
                           this.current = this.reader.read();
                           if (this.current != 101) {
                              this.parseIdentifier();
                           } else {
                              this.bufferize();
                              this.current = this.reader.read();
                              if (this.current != 119) {
                                 this.parseIdentifier();
                              } else {
                                 this.bufferize();
                                 this.current = this.reader.read();
                                 if (this.current == 40) {
                                    this.bufferSize = 0;
                                    this.current = this.reader.read();
                                    this.parseViewAttributes();
                                    if (this.current != 41) {
                                       this.reportCharacterExpectedError(')', this.current);
                                    }
                                    break label92;
                                 }

                                 this.parseIdentifier();
                              }
                           }
                        }
                     }
                  }
               }
               break;
            case 120:
               this.bufferize();
               this.current = this.reader.read();
               if (this.current != 112) {
                  this.parseIdentifier();
               } else {
                  this.bufferize();
                  this.current = this.reader.read();
                  if (this.current != 111) {
                     this.parseIdentifier();
                  } else {
                     this.bufferize();
                     this.current = this.reader.read();
                     if (this.current != 105) {
                        this.parseIdentifier();
                     } else {
                        this.bufferize();
                        this.current = this.reader.read();
                        if (this.current != 110) {
                           this.parseIdentifier();
                        } else {
                           this.bufferize();
                           this.current = this.reader.read();
                           if (this.current != 116) {
                              this.parseIdentifier();
                           } else {
                              this.bufferize();
                              this.current = this.reader.read();
                              if (this.current != 101) {
                                 this.parseIdentifier();
                              } else {
                                 this.bufferize();
                                 this.current = this.reader.read();
                                 if (this.current != 114) {
                                    this.parseIdentifier();
                                 } else {
                                    this.bufferize();
                                    this.current = this.reader.read();
                                    if (this.current == 40) {
                                       this.bufferSize = 0;
                                       this.current = this.reader.read();
                                       if (this.current != 105) {
                                          this.reportCharacterExpectedError('i', this.current);
                                       } else {
                                          this.current = this.reader.read();
                                          if (this.current != 100) {
                                             this.reportCharacterExpectedError('d', this.current);
                                          } else {
                                             this.current = this.reader.read();
                                             if (this.current != 40) {
                                                this.reportCharacterExpectedError('(', this.current);
                                             } else {
                                                this.current = this.reader.read();
                                                if (this.current != 34 && this.current != 39) {
                                                   this.reportCharacterExpectedError('\'', this.current);
                                                } else {
                                                   char var2 = (char)this.current;
                                                   this.current = this.reader.read();
                                                   this.parseIdentifier();
                                                   var1 = this.getBufferContent();
                                                   this.bufferSize = 0;
                                                   this.fragmentIdentifierHandler.idReference(var1);
                                                   if (this.current != var2) {
                                                      this.reportCharacterExpectedError(var2, this.current);
                                                   } else {
                                                      this.current = this.reader.read();
                                                      if (this.current != 41) {
                                                         this.reportCharacterExpectedError(')', this.current);
                                                      } else {
                                                         this.current = this.reader.read();
                                                         if (this.current != 41) {
                                                            this.reportCharacterExpectedError(')', this.current);
                                                         }
                                                      }
                                                   }
                                                }
                                             }
                                          }
                                       }
                                       break label92;
                                    }

                                    this.parseIdentifier();
                                 }
                              }
                           }
                        }
                     }
                  }
               }
               break;
            default:
               if (this.current == -1 || !XMLUtilities.isXMLNameFirstCharacter((char)this.current)) {
                  break label92;
               }

               this.bufferize();
               this.current = this.reader.read();
               this.parseIdentifier();
         }

         var1 = this.getBufferContent();
         this.fragmentIdentifierHandler.idReference(var1);
      }

      this.fragmentIdentifierHandler.endFragmentIdentifier();
   }

   protected void parseViewAttributes() throws ParseException, IOException {
      boolean var1 = true;

      label356:
      while(true) {
         switch (this.current) {
            case -1:
            case 41:
               if (var1) {
                  this.reportUnexpectedCharacterError(this.current);
               }

               return;
            case 59:
               if (var1) {
                  this.reportUnexpectedCharacterError(this.current);
                  return;
               }

               this.current = this.reader.read();
               break;
            case 112:
               var1 = false;
               this.current = this.reader.read();
               if (this.current != 114) {
                  this.reportCharacterExpectedError('r', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 101) {
                  this.reportCharacterExpectedError('e', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 115) {
                  this.reportCharacterExpectedError('s', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 101) {
                  this.reportCharacterExpectedError('e', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 114) {
                  this.reportCharacterExpectedError('r', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 118) {
                  this.reportCharacterExpectedError('v', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 101) {
                  this.reportCharacterExpectedError('e', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 65) {
                  this.reportCharacterExpectedError('A', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 115) {
                  this.reportCharacterExpectedError('s', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 112) {
                  this.reportCharacterExpectedError('p', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 101) {
                  this.reportCharacterExpectedError('e', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 99) {
                  this.reportCharacterExpectedError('c', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 116) {
                  this.reportCharacterExpectedError('t', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 82) {
                  this.reportCharacterExpectedError('R', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 97) {
                  this.reportCharacterExpectedError('a', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 116) {
                  this.reportCharacterExpectedError('t', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 105) {
                  this.reportCharacterExpectedError('i', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 111) {
                  this.reportCharacterExpectedError('o', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 40) {
                  this.reportCharacterExpectedError('(', this.current);
                  return;
               }

               this.current = this.reader.read();
               this.parsePreserveAspectRatio();
               if (this.current != 41) {
                  this.reportCharacterExpectedError(')', this.current);
                  return;
               }

               this.current = this.reader.read();
               break;
            case 116:
               var1 = false;
               this.current = this.reader.read();
               if (this.current != 114) {
                  this.reportCharacterExpectedError('r', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 97) {
                  this.reportCharacterExpectedError('a', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 110) {
                  this.reportCharacterExpectedError('n', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 115) {
                  this.reportCharacterExpectedError('s', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 102) {
                  this.reportCharacterExpectedError('f', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 111) {
                  this.reportCharacterExpectedError('o', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 114) {
                  this.reportCharacterExpectedError('r', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 109) {
                  this.reportCharacterExpectedError('m', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 40) {
                  this.reportCharacterExpectedError('(', this.current);
                  return;
               }

               this.fragmentIdentifierHandler.startTransformList();

               label353:
               while(true) {
                  try {
                     this.current = this.reader.read();
                     switch (this.current) {
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
                           break label353;
                     }
                  } catch (ParseException var7) {
                     this.errorHandler.error(var7);
                     this.skipTransform();
                  }
               }

               this.fragmentIdentifierHandler.endTransformList();
               break;
            case 118:
               var1 = false;
               this.current = this.reader.read();
               if (this.current != 105) {
                  this.reportCharacterExpectedError('i', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 101) {
                  this.reportCharacterExpectedError('e', this.current);
                  return;
               }

               this.current = this.reader.read();
               if (this.current != 119) {
                  this.reportCharacterExpectedError('w', this.current);
                  return;
               }

               this.current = this.reader.read();
               switch (this.current) {
                  case 66:
                     this.current = this.reader.read();
                     if (this.current != 111) {
                        this.reportCharacterExpectedError('o', this.current);
                        return;
                     }

                     this.current = this.reader.read();
                     if (this.current != 120) {
                        this.reportCharacterExpectedError('x', this.current);
                        return;
                     }

                     this.current = this.reader.read();
                     if (this.current != 40) {
                        this.reportCharacterExpectedError('(', this.current);
                        return;
                     }

                     this.current = this.reader.read();
                     float var2 = this.parseFloat();
                     if (this.current != 44) {
                        this.reportCharacterExpectedError(',', this.current);
                        return;
                     }

                     this.current = this.reader.read();
                     float var3 = this.parseFloat();
                     if (this.current != 44) {
                        this.reportCharacterExpectedError(',', this.current);
                        return;
                     }

                     this.current = this.reader.read();
                     float var4 = this.parseFloat();
                     if (this.current != 44) {
                        this.reportCharacterExpectedError(',', this.current);
                        return;
                     }

                     this.current = this.reader.read();
                     float var5 = this.parseFloat();
                     if (this.current != 41) {
                        this.reportCharacterExpectedError(')', this.current);
                        return;
                     }

                     this.current = this.reader.read();
                     this.fragmentIdentifierHandler.viewBox(var2, var3, var4, var5);
                     if (this.current == 41 || this.current == 59) {
                        continue;
                     }

                     this.reportCharacterExpectedError(')', this.current);
                     return;
                  case 84:
                     this.current = this.reader.read();
                     if (this.current != 97) {
                        this.reportCharacterExpectedError('a', this.current);
                        return;
                     }

                     this.current = this.reader.read();
                     if (this.current != 114) {
                        this.reportCharacterExpectedError('r', this.current);
                        return;
                     }

                     this.current = this.reader.read();
                     if (this.current != 103) {
                        this.reportCharacterExpectedError('g', this.current);
                        return;
                     }

                     this.current = this.reader.read();
                     if (this.current != 101) {
                        this.reportCharacterExpectedError('e', this.current);
                        return;
                     }

                     this.current = this.reader.read();
                     if (this.current != 116) {
                        this.reportCharacterExpectedError('t', this.current);
                        return;
                     }

                     this.current = this.reader.read();
                     if (this.current != 40) {
                        this.reportCharacterExpectedError('(', this.current);
                        return;
                     }

                     this.current = this.reader.read();
                     this.fragmentIdentifierHandler.startViewTarget();

                     while(true) {
                        this.bufferSize = 0;
                        if (this.current == -1 || !XMLUtilities.isXMLNameFirstCharacter((char)this.current)) {
                           this.reportUnexpectedCharacterError(this.current);
                           return;
                        }

                        this.bufferize();
                        this.current = this.reader.read();
                        this.parseIdentifier();
                        String var6 = this.getBufferContent();
                        this.fragmentIdentifierHandler.viewTarget(var6);
                        this.bufferSize = 0;
                        switch (this.current) {
                           case 41:
                              this.current = this.reader.read();
                              this.fragmentIdentifierHandler.endViewTarget();
                              continue label356;
                           case 44:
                           case 59:
                              this.current = this.reader.read();
                              break;
                           default:
                              this.reportUnexpectedCharacterError(this.current);
                              return;
                        }
                     }
                  default:
                     this.reportUnexpectedCharacterError(this.current);
                     return;
               }
            case 122:
               var1 = false;
               this.current = this.reader.read();
               if (this.current == 111) {
                  this.current = this.reader.read();
                  if (this.current == 111) {
                     this.current = this.reader.read();
                     if (this.current == 109) {
                        this.current = this.reader.read();
                        if (this.current == 65) {
                           this.current = this.reader.read();
                           if (this.current == 110) {
                              this.current = this.reader.read();
                              if (this.current == 100) {
                                 this.current = this.reader.read();
                                 if (this.current == 80) {
                                    this.current = this.reader.read();
                                    if (this.current == 97) {
                                       this.current = this.reader.read();
                                       if (this.current == 110) {
                                          this.current = this.reader.read();
                                          if (this.current == 40) {
                                             this.current = this.reader.read();
                                             switch (this.current) {
                                                case 100:
                                                   this.current = this.reader.read();
                                                   if (this.current != 105) {
                                                      this.reportCharacterExpectedError('i', this.current);
                                                      return;
                                                   }

                                                   this.current = this.reader.read();
                                                   if (this.current != 115) {
                                                      this.reportCharacterExpectedError('s', this.current);
                                                      return;
                                                   }

                                                   this.current = this.reader.read();
                                                   if (this.current != 97) {
                                                      this.reportCharacterExpectedError('a', this.current);
                                                      return;
                                                   }

                                                   this.current = this.reader.read();
                                                   if (this.current != 98) {
                                                      this.reportCharacterExpectedError('b', this.current);
                                                      return;
                                                   }

                                                   this.current = this.reader.read();
                                                   if (this.current != 108) {
                                                      this.reportCharacterExpectedError('l', this.current);
                                                      return;
                                                   }

                                                   this.current = this.reader.read();
                                                   if (this.current != 101) {
                                                      this.reportCharacterExpectedError('e', this.current);
                                                      return;
                                                   }

                                                   this.current = this.reader.read();
                                                   this.fragmentIdentifierHandler.zoomAndPan(false);
                                                   break;
                                                case 109:
                                                   this.current = this.reader.read();
                                                   if (this.current != 97) {
                                                      this.reportCharacterExpectedError('a', this.current);
                                                      return;
                                                   }

                                                   this.current = this.reader.read();
                                                   if (this.current != 103) {
                                                      this.reportCharacterExpectedError('g', this.current);
                                                      return;
                                                   }

                                                   this.current = this.reader.read();
                                                   if (this.current != 110) {
                                                      this.reportCharacterExpectedError('n', this.current);
                                                      return;
                                                   }

                                                   this.current = this.reader.read();
                                                   if (this.current != 105) {
                                                      this.reportCharacterExpectedError('i', this.current);
                                                      return;
                                                   }

                                                   this.current = this.reader.read();
                                                   if (this.current != 102) {
                                                      this.reportCharacterExpectedError('f', this.current);
                                                      return;
                                                   }

                                                   this.current = this.reader.read();
                                                   if (this.current != 121) {
                                                      this.reportCharacterExpectedError('y', this.current);
                                                      return;
                                                   }

                                                   this.current = this.reader.read();
                                                   this.fragmentIdentifierHandler.zoomAndPan(true);
                                                   break;
                                                default:
                                                   this.reportUnexpectedCharacterError(this.current);
                                                   return;
                                             }

                                             if (this.current == 41) {
                                                this.current = this.reader.read();
                                                break;
                                             } else {
                                                this.reportCharacterExpectedError(')', this.current);
                                             }
                                          } else {
                                             this.reportCharacterExpectedError('(', this.current);
                                          }
                                       } else {
                                          this.reportCharacterExpectedError('n', this.current);
                                       }
                                    } else {
                                       this.reportCharacterExpectedError('a', this.current);
                                    }
                                 } else {
                                    this.reportCharacterExpectedError('P', this.current);
                                 }
                              } else {
                                 this.reportCharacterExpectedError('d', this.current);
                              }
                           } else {
                              this.reportCharacterExpectedError('n', this.current);
                           }
                        } else {
                           this.reportCharacterExpectedError('A', this.current);
                        }
                     } else {
                        this.reportCharacterExpectedError('m', this.current);
                     }
                  } else {
                     this.reportCharacterExpectedError('o', this.current);
                  }
               } else {
                  this.reportCharacterExpectedError('o', this.current);
               }

               return;
            default:
               return;
         }
      }
   }

   protected void parseIdentifier() throws ParseException, IOException {
      while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
         this.bufferize();
         this.current = this.reader.read();
      }

   }

   protected String getBufferContent() {
      return new String(this.buffer, 0, this.bufferSize);
   }

   protected void bufferize() {
      if (this.bufferSize >= this.buffer.length) {
         char[] var1 = new char[this.buffer.length * 2];
         System.arraycopy(this.buffer, 0, var1, 0, this.bufferSize);
         this.buffer = var1;
      }

      this.buffer[this.bufferSize++] = (char)this.current;
   }

   protected void skipSpaces() throws IOException {
      if (this.current == 44) {
         this.current = this.reader.read();
      }

   }

   protected void skipCommaSpaces() throws IOException {
      if (this.current == 44) {
         this.current = this.reader.read();
      }

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
                           this.fragmentIdentifierHandler.matrix(var1, var2, var3, var4, var5, var6);
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
                              this.fragmentIdentifierHandler.rotate(var1);
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
                                 this.fragmentIdentifierHandler.rotate(var1, var2, var3);
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
                                       this.fragmentIdentifierHandler.translate(var1);
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
                                          this.fragmentIdentifierHandler.translate(var1, var2);
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
                        this.fragmentIdentifierHandler.scale(var1);
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
                           this.fragmentIdentifierHandler.scale(var1, var2);
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
                        this.fragmentIdentifierHandler.skewX(var2);
                     } else {
                        this.fragmentIdentifierHandler.skewY(var2);
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

   protected void parsePreserveAspectRatio() throws ParseException, IOException {
      this.fragmentIdentifierHandler.startPreserveAspectRatio();
      label119:
      switch (this.current) {
         case 110:
            this.current = this.reader.read();
            if (this.current != 111) {
               this.reportCharacterExpectedError('o', this.current);
               this.skipIdentifier();
            } else {
               this.current = this.reader.read();
               if (this.current != 110) {
                  this.reportCharacterExpectedError('n', this.current);
                  this.skipIdentifier();
               } else {
                  this.current = this.reader.read();
                  if (this.current != 101) {
                     this.reportCharacterExpectedError('e', this.current);
                     this.skipIdentifier();
                  } else {
                     this.current = this.reader.read();
                     this.skipSpaces();
                     this.fragmentIdentifierHandler.none();
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
                                       this.fragmentIdentifierHandler.xMaxYMax();
                                       this.current = this.reader.read();
                                    }
                                    break label119;
                                 case 105:
                                    this.current = this.reader.read();
                                    switch (this.current) {
                                       case 100:
                                          this.fragmentIdentifierHandler.xMaxYMid();
                                          this.current = this.reader.read();
                                          break label119;
                                       case 110:
                                          this.fragmentIdentifierHandler.xMaxYMin();
                                          this.current = this.reader.read();
                                          break label119;
                                       default:
                                          this.reportUnexpectedCharacterError(this.current);
                                          this.skipIdentifier();
                                    }
                              }
                           }
                        }
                     }
                     break label119;
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
                                          this.fragmentIdentifierHandler.xMidYMax();
                                          this.current = this.reader.read();
                                       }
                                       break label119;
                                    case 105:
                                       this.current = this.reader.read();
                                       switch (this.current) {
                                          case 100:
                                             this.fragmentIdentifierHandler.xMidYMid();
                                             this.current = this.reader.read();
                                             break label119;
                                          case 110:
                                             this.fragmentIdentifierHandler.xMidYMin();
                                             this.current = this.reader.read();
                                             break label119;
                                          default:
                                             this.reportUnexpectedCharacterError(this.current);
                                             this.skipIdentifier();
                                       }
                                 }
                              }
                           }
                           break label119;
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
                                          this.fragmentIdentifierHandler.xMinYMax();
                                          this.current = this.reader.read();
                                       }
                                       break label119;
                                    case 105:
                                       this.current = this.reader.read();
                                       switch (this.current) {
                                          case 100:
                                             this.fragmentIdentifierHandler.xMinYMid();
                                             this.current = this.reader.read();
                                             break label119;
                                          case 110:
                                             this.fragmentIdentifierHandler.xMinYMin();
                                             this.current = this.reader.read();
                                             break label119;
                                          default:
                                             this.reportUnexpectedCharacterError(this.current);
                                             this.skipIdentifier();
                                       }
                                 }
                              }
                           }
                           break label119;
                        default:
                           this.reportUnexpectedCharacterError(this.current);
                           this.skipIdentifier();
                           break label119;
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
                     this.fragmentIdentifierHandler.meet();
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
                        this.fragmentIdentifierHandler.slice();
                        this.current = this.reader.read();
                     }
                  }
               }
            }
      }

      this.fragmentIdentifierHandler.endPreserveAspectRatio();
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
            case -1:
               return;
         }
      }
   }
}

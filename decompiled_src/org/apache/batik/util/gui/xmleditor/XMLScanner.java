package org.apache.batik.util.gui.xmleditor;

import org.apache.batik.xml.XMLUtilities;

public class XMLScanner {
   public static final int TEMP_ERROR_CONTEXT = -2;
   public static final int EOF_CONTEXT = -1;
   public static final int DEFAULT_CONTEXT = 0;
   public static final int COMMENT_CONTEXT = 1;
   public static final int ELEMENT_CONTEXT = 2;
   public static final int CHARACTER_DATA_CONTEXT = 3;
   public static final int ATTRIBUTE_NAME_CONTEXT = 4;
   public static final int ATTRIBUTE_VALUE_CONTEXT = 5;
   public static final int XML_DECLARATION_CONTEXT = 6;
   public static final int DOCTYPE_CONTEXT = 7;
   public static final int ENTITY_CONTEXT = 8;
   public static final int ELEMENT_DECLARATION_CONTEXT = 9;
   public static final int CDATA_CONTEXT = 10;
   public static final int PI_CONTEXT = 11;
   private int position;
   private String string;
   private int current;
   private int scanValue;
   private int startOffset;

   public XMLScanner() {
      this.reset();
   }

   public void reset() {
      this.position = 0;
      this.startOffset = 0;
   }

   public void setString(String var1) {
      this.string = var1;
   }

   protected int nextChar() {
      try {
         this.current = this.string.charAt(this.position);
         ++this.position;
      } catch (Exception var2) {
         this.current = -1;
      }

      return this.current;
   }

   protected int skipSpaces() {
      do {
         this.nextChar();
      } while(this.current != -1 && XMLUtilities.isXMLSpace((char)this.current));

      return this.current;
   }

   public int getScanValue() {
      return this.scanValue;
   }

   public int getStartOffset() {
      return this.startOffset;
   }

   public int scan(int var1) {
      this.nextChar();
      switch (var1) {
         case 1:
            this.scanValue = this.scanComment();
            break;
         case 2:
            this.scanValue = this.scanElement();
            break;
         case 3:
         case 8:
         case 9:
         default:
            this.scanValue = this.scanCharacterData();
            break;
         case 4:
            this.scanValue = this.scanAttributeName();
            break;
         case 5:
            this.scanValue = this.scanAttributeValue();
            break;
         case 6:
            this.scanValue = this.scanXMLDeclaration();
            break;
         case 7:
            this.scanValue = this.scanDOCTYPE();
            break;
         case 10:
            this.scanValue = this.scanCDATA();
      }

      return this.position;
   }

   private int scanCharacterData() {
      for(; this.current != -1; this.nextChar()) {
         if (this.current == 60) {
            this.nextChar();
            if (this.current == 63) {
               this.position -= 2;
               return 6;
            }

            if (this.current != 33) {
               this.position -= 2;
               return 2;
            }

            this.nextChar();
            if (this.current == 68) {
               this.position -= 3;
               return 7;
            }

            if (this.current == 45) {
               this.nextChar();
               if (this.current == 45) {
                  this.position -= 4;
                  return 1;
               }
            } else if (this.current == 91 && this.nextChar() == 67 && this.nextChar() == 68 && this.nextChar() == 65 && this.nextChar() == 84 && this.nextChar() == 65 && this.nextChar() == 91) {
               this.position -= 9;
               return 10;
            }
         }
      }

      if (this.current == -1) {
         return -1;
      } else {
         return 3;
      }
   }

   private int scanXMLDeclaration() {
      this.position += 2;

      while(this.current != -1) {
         if (this.current == 63) {
            if (this.nextChar() == 62) {
               return 3;
            }

            return -2;
         }

         this.nextChar();
      }

      if (this.current == -1) {
         return -1;
      } else {
         return 6;
      }
   }

   private int scanDOCTYPE() {
      this.position += 3;

      for(boolean var1 = true; this.current != -1; this.nextChar()) {
         if (this.current == 91) {
            var1 = false;
         } else if (this.current == 93) {
            var1 = true;
         } else if (this.current == 62 && var1) {
            return 3;
         }
      }

      if (this.current == -1) {
         return -1;
      } else {
         return 7;
      }
   }

   private int scanComment() {
      while(this.current != -1) {
         if (this.current == 45 && this.nextChar() == 45 && this.nextChar() == 62) {
            return 3;
         }

         this.nextChar();
      }

      if (this.current == -1) {
         return -1;
      } else {
         return 1;
      }
   }

   private int scanElement() {
      while(this.current != -1) {
         if (this.current == 62) {
            return 3;
         }

         if (XMLUtilities.isXMLSpace((char)this.current)) {
            return 4;
         }

         this.nextChar();
      }

      if (this.current == -1) {
         return -1;
      } else {
         return 2;
      }
   }

   private int scanAttributeName() {
      while(this.current != -1) {
         if (this.current == 61) {
            return 5;
         }

         if (this.current == 47) {
            --this.position;
            return 2;
         }

         if (this.current == 62) {
            --this.position;
            return 2;
         }

         this.nextChar();
      }

      if (this.current == -1) {
         return -1;
      } else {
         return 4;
      }
   }

   private int scanAttributeValue() {
      int var1 = 34;

      while(this.current != -1) {
         if (this.current == 34 || this.current == 39) {
            var1 = this.current;
            break;
         }

         this.nextChar();
      }

      this.nextChar();

      while(this.current != -1) {
         if (this.current == var1) {
            return 2;
         }

         this.nextChar();
      }

      if (this.current == -1) {
         return -1;
      } else {
         return 5;
      }
   }

   private int scanCDATA() {
      while(this.current != -1) {
         if (this.current == 93 && this.nextChar() == 93 && this.nextChar() == 62) {
            return 3;
         }

         this.nextChar();
      }

      if (this.current == -1) {
         return -1;
      } else {
         return 10;
      }
   }
}

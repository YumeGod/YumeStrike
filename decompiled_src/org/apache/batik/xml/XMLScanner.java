package org.apache.batik.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;
import java.util.MissingResourceException;
import org.apache.batik.i18n.Localizable;
import org.apache.batik.i18n.LocalizableSupport;
import org.apache.batik.util.io.NormalizingReader;
import org.apache.batik.util.io.StreamNormalizingReader;
import org.apache.batik.util.io.StringNormalizingReader;

public class XMLScanner implements Localizable {
   public static final int DOCUMENT_START_CONTEXT = 0;
   public static final int TOP_LEVEL_CONTEXT = 1;
   public static final int PI_CONTEXT = 2;
   public static final int XML_DECL_CONTEXT = 3;
   public static final int DOCTYPE_CONTEXT = 4;
   public static final int START_TAG_CONTEXT = 5;
   public static final int CONTENT_CONTEXT = 6;
   public static final int DTD_DECLARATIONS_CONTEXT = 7;
   public static final int CDATA_SECTION_CONTEXT = 8;
   public static final int END_TAG_CONTEXT = 9;
   public static final int ATTRIBUTE_VALUE_CONTEXT = 10;
   public static final int ATTLIST_CONTEXT = 11;
   public static final int ELEMENT_DECLARATION_CONTEXT = 12;
   public static final int ENTITY_CONTEXT = 13;
   public static final int NOTATION_CONTEXT = 14;
   public static final int NOTATION_TYPE_CONTEXT = 15;
   public static final int ENUMERATION_CONTEXT = 16;
   public static final int ENTITY_VALUE_CONTEXT = 17;
   protected static final String BUNDLE_CLASSNAME = "org.apache.batik.xml.resources.Messages";
   protected LocalizableSupport localizableSupport;
   protected NormalizingReader reader;
   protected int current;
   protected int type;
   protected char[] buffer;
   protected int position;
   protected int start;
   protected int end;
   protected int context;
   protected int depth;
   protected boolean piEndRead;
   protected boolean inDTD;
   protected char attrDelimiter;
   protected boolean cdataEndRead;
   // $FF: synthetic field
   static Class class$org$apache$batik$xml$XMLScanner;

   public XMLScanner(Reader var1) throws XMLException {
      this.localizableSupport = new LocalizableSupport("org.apache.batik.xml.resources.Messages", (class$org$apache$batik$xml$XMLScanner == null ? (class$org$apache$batik$xml$XMLScanner = class$("org.apache.batik.xml.XMLScanner")) : class$org$apache$batik$xml$XMLScanner).getClassLoader());
      this.buffer = new char[1024];
      this.context = 0;

      try {
         this.reader = new StreamNormalizingReader(var1);
         this.current = this.nextChar();
      } catch (IOException var3) {
         throw new XMLException(var3);
      }
   }

   public XMLScanner(InputStream var1, String var2) throws XMLException {
      this.localizableSupport = new LocalizableSupport("org.apache.batik.xml.resources.Messages", (class$org$apache$batik$xml$XMLScanner == null ? (class$org$apache$batik$xml$XMLScanner = class$("org.apache.batik.xml.XMLScanner")) : class$org$apache$batik$xml$XMLScanner).getClassLoader());
      this.buffer = new char[1024];
      this.context = 0;

      try {
         this.reader = new StreamNormalizingReader(var1, var2);
         this.current = this.nextChar();
      } catch (IOException var4) {
         throw new XMLException(var4);
      }
   }

   public XMLScanner(String var1) throws XMLException {
      this.localizableSupport = new LocalizableSupport("org.apache.batik.xml.resources.Messages", (class$org$apache$batik$xml$XMLScanner == null ? (class$org$apache$batik$xml$XMLScanner = class$("org.apache.batik.xml.XMLScanner")) : class$org$apache$batik$xml$XMLScanner).getClassLoader());
      this.buffer = new char[1024];
      this.context = 0;

      try {
         this.reader = new StringNormalizingReader(var1);
         this.current = this.nextChar();
      } catch (IOException var3) {
         throw new XMLException(var3);
      }
   }

   public void setLocale(Locale var1) {
      this.localizableSupport.setLocale(var1);
   }

   public Locale getLocale() {
      return this.localizableSupport.getLocale();
   }

   public String formatMessage(String var1, Object[] var2) throws MissingResourceException {
      return this.localizableSupport.formatMessage(var1, var2);
   }

   public void setDepth(int var1) {
      this.depth = var1;
   }

   public int getDepth() {
      return this.depth;
   }

   public void setContext(int var1) {
      this.context = var1;
   }

   public int getContext() {
      return this.context;
   }

   public int getType() {
      return this.type;
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

   public char getStringDelimiter() {
      return this.attrDelimiter;
   }

   public int getStartOffset() {
      switch (this.type) {
         case 4:
            return 4;
         case 5:
         case 10:
         case 12:
            return 2;
         case 6:
         case 8:
         case 11:
         case 14:
         case 15:
         case 17:
         case 18:
         case 19:
         case 20:
         case 22:
         case 23:
         case 24:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         default:
            return 0;
         case 7:
            return -2;
         case 9:
         case 13:
         case 16:
         case 25:
         case 34:
            return 1;
         case 21:
            return -3;
      }
   }

   public int getEndOffset() {
      switch (this.type) {
         case 4:
            return -3;
         case 6:
            return -2;
         case 8:
            if (this.cdataEndRead) {
               return -3;
            }

            return 0;
         case 12:
         case 13:
         case 18:
         case 25:
         case 34:
            return -1;
         default:
            return 0;
      }
   }

   public void clearBuffer() {
      if (this.position <= 0) {
         this.position = 0;
      } else {
         this.buffer[0] = this.buffer[this.position - 1];
         this.position = 1;
      }

   }

   public int next() throws XMLException {
      return this.next(this.context);
   }

   public int next(int var1) throws XMLException {
      this.start = this.position - 1;

      try {
         switch (var1) {
            case 0:
               this.type = this.nextInDocumentStart();
               break;
            case 1:
               this.type = this.nextInTopLevel();
               break;
            case 2:
               this.type = this.nextInPI();
               break;
            case 3:
               this.type = this.nextInXMLDecl();
               break;
            case 4:
               this.type = this.nextInDoctype();
               break;
            case 5:
               this.type = this.nextInStartTag();
               break;
            case 6:
               this.type = this.nextInContent();
               break;
            case 7:
               this.type = this.nextInDTDDeclarations();
               break;
            case 8:
               this.type = this.nextInCDATASection();
               break;
            case 9:
               this.type = this.nextInEndTag();
               break;
            case 10:
               this.type = this.nextInAttributeValue();
               break;
            case 11:
               this.type = this.nextInAttList();
               break;
            case 12:
               this.type = this.nextInElementDeclaration();
               break;
            case 13:
               this.type = this.nextInEntity();
               break;
            case 14:
               this.type = this.nextInNotation();
               break;
            case 15:
               return this.nextInNotationType();
            case 16:
               return this.nextInEnumeration();
            case 17:
               return this.nextInEntityValue();
            default:
               throw new IllegalArgumentException("unexpected ctx:" + var1);
         }
      } catch (IOException var3) {
         throw new XMLException(var3);
      }

      this.end = this.position - (this.current == -1 ? 0 : 1);
      return this.type;
   }

   protected int nextInDocumentStart() throws IOException, XMLException {
      switch (this.current) {
         case -1:
            return 0;
         case 9:
         case 10:
         case 13:
         case 32:
            do {
               this.nextChar();
            } while(this.current != -1 && XMLUtilities.isXMLSpace((char)this.current));

            this.context = this.depth == 0 ? 1 : 6;
            return 1;
         case 60:
            switch (this.nextChar()) {
               case 33:
                  switch (this.nextChar()) {
                     case 45:
                        return this.readComment();
                     case 68:
                        this.context = 4;
                        return this.readIdentifier("OCTYPE", 3, -1);
                     default:
                        throw this.createXMLException("invalid.doctype");
                  }
               case 63:
                  int var1 = this.nextChar();
                  if (var1 != -1 && XMLUtilities.isXMLNameFirstCharacter((char)var1)) {
                     this.context = 2;
                     int var2 = this.nextChar();
                     if (var2 != -1 && XMLUtilities.isXMLNameCharacter((char)var2)) {
                        int var3 = this.nextChar();
                        if (var3 != -1 && XMLUtilities.isXMLNameCharacter((char)var3)) {
                           int var4 = this.nextChar();
                           if (var4 != -1 && XMLUtilities.isXMLNameCharacter((char)var4)) {
                              do {
                                 this.nextChar();
                              } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                              return 5;
                           }

                           if (var1 == 120 && var2 == 109 && var3 == 108) {
                              this.context = 3;
                              return 2;
                           }

                           if (var1 != 120 && var1 != 88 || var2 != 109 && var2 != 77 || var3 != 108 && var3 != 76) {
                              return 5;
                           }

                           throw this.createXMLException("xml.reserved");
                        }

                        return 5;
                     }

                     return 5;
                  }

                  throw this.createXMLException("invalid.pi.target");
               default:
                  this.context = 5;
                  ++this.depth;
                  return this.readName(9);
            }
         default:
            if (this.depth == 0) {
               throw this.createXMLException("invalid.character");
            } else {
               return this.nextInContent();
            }
      }
   }

   protected int nextInTopLevel() throws IOException, XMLException {
      switch (this.current) {
         case -1:
            return 0;
         case 9:
         case 10:
         case 13:
         case 32:
            do {
               this.nextChar();
            } while(this.current != -1 && XMLUtilities.isXMLSpace((char)this.current));

            return 1;
         case 60:
            switch (this.nextChar()) {
               case 33:
                  switch (this.nextChar()) {
                     case 45:
                        return this.readComment();
                     case 68:
                        this.context = 4;
                        return this.readIdentifier("OCTYPE", 3, -1);
                     default:
                        throw this.createXMLException("invalid.character");
                  }
               case 63:
                  this.context = 2;
                  return this.readPIStart();
               default:
                  this.context = 5;
                  ++this.depth;
                  return this.readName(9);
            }
         default:
            throw this.createXMLException("invalid.character");
      }
   }

   protected int nextInPI() throws IOException, XMLException {
      if (this.piEndRead) {
         this.piEndRead = false;
         this.context = this.depth == 0 ? 1 : 6;
         return 7;
      } else {
         switch (this.current) {
            case 9:
            case 10:
            case 13:
            case 32:
               do {
                  this.nextChar();
               } while(this.current != -1 && XMLUtilities.isXMLSpace((char)this.current));

               return 1;
            case 63:
               if (this.nextChar() != 62) {
                  throw this.createXMLException("pi.end.expected");
               }

               this.nextChar();
               if (this.inDTD) {
                  this.context = 7;
               } else if (this.depth == 0) {
                  this.context = 1;
               } else {
                  this.context = 6;
               }

               return 7;
            default:
               do {
                  do {
                     this.nextChar();
                  } while(this.current != -1 && this.current != 63);

                  this.nextChar();
               } while(this.current != -1 && this.current != 62);

               this.nextChar();
               this.piEndRead = true;
               return 6;
         }
      }
   }

   protected int nextInStartTag() throws IOException, XMLException {
      switch (this.current) {
         case 9:
         case 10:
         case 13:
         case 32:
            do {
               this.nextChar();
            } while(this.current != -1 && XMLUtilities.isXMLSpace((char)this.current));

            return 1;
         case 34:
            this.attrDelimiter = '"';
            this.nextChar();

            while(true) {
               switch (this.current) {
                  case -1:
                     throw this.createXMLException("unexpected.eof");
                  case 34:
                     this.nextChar();
                     return 25;
                  case 38:
                     this.context = 10;
                     return 16;
                  case 60:
                     throw this.createXMLException("invalid.character");
                  default:
                     this.nextChar();
               }
            }
         case 39:
            this.attrDelimiter = '\'';
            this.nextChar();

            while(true) {
               switch (this.current) {
                  case -1:
                     throw this.createXMLException("unexpected.eof");
                  case 38:
                     this.context = 10;
                     return 16;
                  case 39:
                     this.nextChar();
                     return 25;
                  case 60:
                     throw this.createXMLException("invalid.character");
                  default:
                     this.nextChar();
               }
            }
         case 47:
            if (this.nextChar() != 62) {
               throw this.createXMLException("malformed.tag.end");
            }

            this.nextChar();
            this.context = --this.depth == 0 ? 1 : 6;
            return 19;
         case 61:
            this.nextChar();
            return 15;
         case 62:
            this.nextChar();
            this.context = 6;
            return 20;
         default:
            return this.readName(14);
      }
   }

   protected int nextInAttributeValue() throws IOException, XMLException {
      if (this.current == -1) {
         return 0;
      } else if (this.current == 38) {
         return this.readReference();
      } else {
         label31:
         while(true) {
            switch (this.current) {
               case -1:
               case 38:
               case 60:
                  break label31;
               case 34:
               case 39:
                  if (this.current == this.attrDelimiter) {
                     break label31;
                  }
               default:
                  this.nextChar();
            }
         }

         switch (this.current) {
            case 34:
            case 39:
               this.nextChar();
               if (this.inDTD) {
                  this.context = 11;
               } else {
                  this.context = 5;
               }
            case -1:
            default:
               return 18;
            case 38:
               return 17;
            case 60:
               throw this.createXMLException("invalid.character");
         }
      }
   }

   protected int nextInContent() throws IOException, XMLException {
      switch (this.current) {
         case -1:
            return 0;
         case 38:
            return this.readReference();
         case 60:
            switch (this.nextChar()) {
               case 33:
                  switch (this.nextChar()) {
                     case 45:
                        return this.readComment();
                     case 91:
                        this.context = 8;
                        return this.readIdentifier("CDATA[", 11, -1);
                     default:
                        throw this.createXMLException("invalid.character");
                  }
               case 47:
                  this.nextChar();
                  this.context = 9;
                  return this.readName(10);
               case 63:
                  this.context = 2;
                  return this.readPIStart();
               default:
                  ++this.depth;
                  this.context = 5;
                  return this.readName(9);
            }
         default:
            while(true) {
               switch (this.current) {
                  case -1:
                  case 38:
                  case 60:
                     return 8;
                  default:
                     this.nextChar();
               }
            }
      }
   }

   protected int nextInEndTag() throws IOException, XMLException {
      switch (this.current) {
         case 9:
         case 10:
         case 13:
         case 32:
            do {
               this.nextChar();
            } while(this.current != -1 && XMLUtilities.isXMLSpace((char)this.current));

            return 1;
         case 62:
            if (--this.depth < 0) {
               throw this.createXMLException("unexpected.end.tag");
            }

            if (this.depth == 0) {
               this.context = 1;
            } else {
               this.context = 6;
            }

            this.nextChar();
            return 20;
         default:
            throw this.createXMLException("invalid.character");
      }
   }

   protected int nextInCDATASection() throws IOException, XMLException {
      if (this.cdataEndRead) {
         this.cdataEndRead = false;
         this.context = 6;
         return 21;
      } else {
         while(this.current != -1) {
            while(this.current != 93 && this.current != -1) {
               this.nextChar();
            }

            if (this.current != -1) {
               this.nextChar();
               if (this.current == 93) {
                  this.nextChar();
                  if (this.current == 62) {
                     break;
                  }
               }
            }
         }

         if (this.current == -1) {
            throw this.createXMLException("unexpected.eof");
         } else {
            this.nextChar();
            this.cdataEndRead = true;
            return 8;
         }
      }
   }

   protected int nextInXMLDecl() throws IOException, XMLException {
      switch (this.current) {
         case 9:
         case 10:
         case 13:
         case 32:
            do {
               this.nextChar();
            } while(this.current != -1 && XMLUtilities.isXMLSpace((char)this.current));

            return 1;
         case 34:
            this.attrDelimiter = '"';
            return this.readString();
         case 39:
            this.attrDelimiter = '\'';
            return this.readString();
         case 61:
            this.nextChar();
            return 15;
         case 63:
            this.nextChar();
            if (this.current != 62) {
               throw this.createXMLException("pi.end.expected");
            }

            this.nextChar();
            this.context = 1;
            return 7;
         case 101:
            return this.readIdentifier("ncoding", 23, -1);
         case 115:
            return this.readIdentifier("tandalone", 24, -1);
         case 118:
            return this.readIdentifier("ersion", 22, -1);
         default:
            throw this.createXMLException("invalid.character");
      }
   }

   protected int nextInDoctype() throws IOException, XMLException {
      switch (this.current) {
         case 9:
         case 10:
         case 13:
         case 32:
            do {
               this.nextChar();
            } while(this.current != -1 && XMLUtilities.isXMLSpace((char)this.current));

            return 1;
         case 34:
            this.attrDelimiter = '"';
            return this.readString();
         case 39:
            this.attrDelimiter = '\'';
            return this.readString();
         case 62:
            this.nextChar();
            this.context = 1;
            return 20;
         case 80:
            return this.readIdentifier("UBLIC", 27, 14);
         case 83:
            return this.readIdentifier("YSTEM", 26, 14);
         case 91:
            this.nextChar();
            this.context = 7;
            this.inDTD = true;
            return 28;
         default:
            return this.readName(14);
      }
   }

   protected int nextInDTDDeclarations() throws IOException, XMLException {
      switch (this.current) {
         case 9:
         case 10:
         case 13:
         case 32:
            do {
               this.nextChar();
            } while(this.current != -1 && XMLUtilities.isXMLSpace((char)this.current));

            return 1;
         case 37:
            return this.readPEReference();
         case 60:
            switch (this.nextChar()) {
               case 33:
                  switch (this.nextChar()) {
                     case 45:
                        return this.readComment();
                     case 65:
                        this.context = 11;
                        return this.readIdentifier("TTLIST", 31, -1);
                     case 69:
                        switch (this.nextChar()) {
                           case 76:
                              this.context = 12;
                              return this.readIdentifier("EMENT", 30, -1);
                           case 78:
                              this.context = 13;
                              return this.readIdentifier("TITY", 32, -1);
                           default:
                              throw this.createXMLException("invalid.character");
                        }
                     case 78:
                        this.context = 14;
                        return this.readIdentifier("OTATION", 33, -1);
                     default:
                        throw this.createXMLException("invalid.character");
                  }
               case 63:
                  this.context = 2;
                  return this.readPIStart();
               default:
                  throw this.createXMLException("invalid.character");
            }
         case 93:
            this.nextChar();
            this.context = 4;
            this.inDTD = false;
            return 29;
         default:
            throw this.createXMLException("invalid.character");
      }
   }

   protected int readString() throws IOException, XMLException {
      do {
         this.nextChar();
      } while(this.current != -1 && this.current != this.attrDelimiter);

      if (this.current == -1) {
         throw this.createXMLException("unexpected.eof");
      } else {
         this.nextChar();
         return 25;
      }
   }

   protected int readComment() throws IOException, XMLException {
      if (this.nextChar() != 45) {
         throw this.createXMLException("malformed.comment");
      } else {
         int var1 = this.nextChar();

         while(var1 != -1) {
            while(var1 != -1 && var1 != 45) {
               var1 = this.nextChar();
            }

            var1 = this.nextChar();
            if (var1 == 45) {
               break;
            }
         }

         if (var1 == -1) {
            throw this.createXMLException("unexpected.eof");
         } else {
            var1 = this.nextChar();
            if (var1 != 62) {
               throw this.createXMLException("malformed.comment");
            } else {
               this.nextChar();
               return 4;
            }
         }
      }
   }

   protected int readIdentifier(String var1, int var2, int var3) throws IOException, XMLException {
      int var4 = var1.length();

      for(int var5 = 0; var5 < var4; ++var5) {
         this.nextChar();
         if (this.current != var1.charAt(var5)) {
            if (var3 == -1) {
               throw this.createXMLException("invalid.character");
            }

            while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
               this.nextChar();
            }

            return var3;
         }
      }

      this.nextChar();
      return var2;
   }

   protected int readName(int var1) throws IOException, XMLException {
      if (this.current == -1) {
         throw this.createXMLException("unexpected.eof");
      } else if (!XMLUtilities.isXMLNameFirstCharacter((char)this.current)) {
         throw this.createXMLException("invalid.name");
      } else {
         do {
            this.nextChar();
         } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

         return var1;
      }
   }

   protected int readPIStart() throws IOException, XMLException {
      int var1 = this.nextChar();
      if (var1 == -1) {
         throw this.createXMLException("unexpected.eof");
      } else if (!XMLUtilities.isXMLNameFirstCharacter((char)this.current)) {
         throw this.createXMLException("malformed.pi.target");
      } else {
         int var2 = this.nextChar();
         if (var2 != -1 && XMLUtilities.isXMLNameCharacter((char)var2)) {
            int var3 = this.nextChar();
            if (var3 != -1 && XMLUtilities.isXMLNameCharacter((char)var3)) {
               int var4 = this.nextChar();
               if (var4 != -1 && XMLUtilities.isXMLNameCharacter((char)var4)) {
                  do {
                     this.nextChar();
                  } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                  return 5;
               } else if (var1 != 120 && var1 != 88 || var2 != 109 && var2 != 77 || var3 != 108 && var3 != 76) {
                  return 5;
               } else {
                  throw this.createXMLException("xml.reserved");
               }
            } else {
               return 5;
            }
         } else {
            return 5;
         }
      }
   }

   protected int nextInElementDeclaration() throws IOException, XMLException {
      switch (this.current) {
         case 9:
         case 10:
         case 13:
         case 32:
            do {
               this.nextChar();
            } while(this.current != -1 && XMLUtilities.isXMLSpace((char)this.current));

            return 1;
         case 35:
            return this.readIdentifier("PCDATA", 44, -1);
         case 37:
            this.nextChar();
            int var1 = this.readName(34);
            if (this.current != 59) {
               throw this.createXMLException("malformed.parameter.entity");
            }

            this.nextChar();
            return var1;
         case 40:
            this.nextChar();
            return 40;
         case 41:
            this.nextChar();
            return 41;
         case 42:
            this.nextChar();
            return 39;
         case 43:
            this.nextChar();
            return 38;
         case 44:
            this.nextChar();
            return 43;
         case 62:
            this.nextChar();
            this.context = 7;
            return 20;
         case 63:
            this.nextChar();
            return 37;
         case 65:
            return this.readIdentifier("NY", 36, 14);
         case 69:
            return this.readIdentifier("MPTY", 35, 14);
         case 124:
            this.nextChar();
            return 42;
         default:
            return this.readName(14);
      }
   }

   protected int nextInAttList() throws IOException, XMLException {
      switch (this.current) {
         case 9:
         case 10:
         case 13:
         case 32:
            do {
               this.nextChar();
            } while(this.current != -1 && XMLUtilities.isXMLSpace((char)this.current));

            return 1;
         case 34:
            this.attrDelimiter = '"';
            this.nextChar();
            if (this.current == -1) {
               throw this.createXMLException("unexpected.eof");
            } else {
               if (this.current != 34 && this.current != 38) {
                  do {
                     this.nextChar();
                  } while(this.current != -1 && this.current != 34 && this.current != 38);
               }

               switch (this.current) {
                  case 34:
                     this.nextChar();
                     return 25;
                  case 38:
                     this.context = 10;
                     return 16;
                  default:
                     throw this.createXMLException("invalid.character");
               }
            }
         case 35:
            switch (this.nextChar()) {
               case 70:
                  return this.readIdentifier("IXED", 55, -1);
               case 73:
                  return this.readIdentifier("MPLIED", 54, -1);
               case 82:
                  return this.readIdentifier("EQUIRED", 53, -1);
               default:
                  throw this.createXMLException("invalid.character");
            }
         case 37:
            int var1 = this.readName(34);
            if (this.current != 59) {
               throw this.createXMLException("malformed.parameter.entity");
            }

            this.nextChar();
            return var1;
         case 39:
            this.attrDelimiter = '\'';
            this.nextChar();
            if (this.current == -1) {
               throw this.createXMLException("unexpected.eof");
            } else {
               if (this.current != 39 && this.current != 38) {
                  do {
                     this.nextChar();
                  } while(this.current != -1 && this.current != 39 && this.current != 38);
               }

               switch (this.current) {
                  case 38:
                     this.context = 10;
                     return 16;
                  case 39:
                     this.nextChar();
                     return 25;
                  default:
                     throw this.createXMLException("invalid.character");
               }
            }
         case 40:
            this.nextChar();
            this.context = 16;
            return 40;
         case 62:
            this.nextChar();
            this.context = 7;
            return this.type = 20;
         case 67:
            return this.readIdentifier("DATA", 45, 14);
         case 69:
            this.nextChar();
            if (this.current != 78) {
               do {
                  this.nextChar();
               } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

               return 14;
            } else {
               this.nextChar();
               if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                  if (this.current != 84) {
                     do {
                        this.nextChar();
                     } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                     return 14;
                  }

                  this.nextChar();
                  if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                     if (this.current != 73) {
                        do {
                           this.nextChar();
                        } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                        return 14;
                     }

                     this.nextChar();
                     if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                        if (this.current != 84) {
                           do {
                              this.nextChar();
                           } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                           return this.type = 14;
                        }

                        this.nextChar();
                        if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                           switch (this.current) {
                              case 73:
                                 this.nextChar();
                                 if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                                    if (this.current != 69) {
                                       do {
                                          this.nextChar();
                                       } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                                       return 14;
                                    }

                                    this.nextChar();
                                    if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                                       if (this.current == 83) {
                                          return 52;
                                       }

                                       do {
                                          this.nextChar();
                                       } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                                       return 14;
                                    }

                                    return 14;
                                 }

                                 return 14;
                              case 89:
                                 this.nextChar();
                                 if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                                    do {
                                       this.nextChar();
                                    } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                                    return 14;
                                 }

                                 return 51;
                              default:
                                 if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                                    do {
                                       this.nextChar();
                                    } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                                    return 14;
                                 }

                                 return 14;
                           }
                        }

                        return 14;
                     }

                     return 14;
                  }

                  return 14;
               }

               return 14;
            }
         case 73:
            this.nextChar();
            if (this.current != 68) {
               do {
                  this.nextChar();
               } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

               return 14;
            } else {
               this.nextChar();
               if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                  if (this.current != 82) {
                     do {
                        this.nextChar();
                     } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                     return 14;
                  }

                  this.nextChar();
                  if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                     if (this.current != 69) {
                        do {
                           this.nextChar();
                        } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                        return 14;
                     }

                     this.nextChar();
                     if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                        if (this.current != 70) {
                           do {
                              this.nextChar();
                           } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                           return 14;
                        }

                        this.nextChar();
                        if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                           if (this.current != 83) {
                              do {
                                 this.nextChar();
                              } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                              return 14;
                           }

                           this.nextChar();
                           if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                              do {
                                 this.nextChar();
                              } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                              return this.type = 14;
                           }

                           return 48;
                        }

                        return 47;
                     }

                     return 14;
                  }

                  return 14;
               }

               return 46;
            }
         case 78:
            switch (this.nextChar()) {
               case 77:
                  this.nextChar();
                  if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                     if (this.current != 84) {
                        do {
                           this.nextChar();
                        } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                        return 14;
                     }

                     this.nextChar();
                     if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                        if (this.current != 79) {
                           do {
                              this.nextChar();
                           } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                           return 14;
                        }

                        this.nextChar();
                        if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                           if (this.current != 75) {
                              do {
                                 this.nextChar();
                              } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                              return 14;
                           }

                           this.nextChar();
                           if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                              if (this.current != 69) {
                                 do {
                                    this.nextChar();
                                 } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                                 return 14;
                              }

                              this.nextChar();
                              if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                                 if (this.current != 78) {
                                    do {
                                       this.nextChar();
                                    } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                                    return 14;
                                 }

                                 this.nextChar();
                                 if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                                    if (this.current != 83) {
                                       do {
                                          this.nextChar();
                                       } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                                       return 14;
                                    }

                                    this.nextChar();
                                    if (this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current)) {
                                       do {
                                          this.nextChar();
                                       } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                                       return 14;
                                    }

                                    return 50;
                                 }

                                 return 49;
                              }

                              return 14;
                           }

                           return 14;
                        }

                        return 14;
                     }

                     return 14;
                  }

                  return 14;
               case 79:
                  this.context = 15;
                  return this.readIdentifier("TATION", 57, 14);
               default:
                  do {
                     this.nextChar();
                  } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

                  return 14;
            }
         default:
            return this.readName(14);
      }
   }

   protected int nextInNotation() throws IOException, XMLException {
      switch (this.current) {
         case 9:
         case 10:
         case 13:
         case 32:
            do {
               this.nextChar();
            } while(this.current != -1 && XMLUtilities.isXMLSpace((char)this.current));

            return 1;
         case 34:
            this.attrDelimiter = '"';
            return this.readString();
         case 37:
            int var1 = this.readName(34);
            if (this.current != 59) {
               throw this.createXMLException("malformed.parameter.entity");
            }

            this.nextChar();
            return var1;
         case 39:
            this.attrDelimiter = '\'';
            return this.readString();
         case 62:
            this.nextChar();
            this.context = 7;
            return 20;
         case 80:
            return this.readIdentifier("UBLIC", 27, 14);
         case 83:
            return this.readIdentifier("YSTEM", 26, 14);
         default:
            return this.readName(14);
      }
   }

   protected int nextInEntity() throws IOException, XMLException {
      switch (this.current) {
         case 9:
         case 10:
         case 13:
         case 32:
            do {
               this.nextChar();
            } while(this.current != -1 && XMLUtilities.isXMLSpace((char)this.current));

            return 1;
         case 34:
            this.attrDelimiter = '"';
            this.nextChar();
            if (this.current == -1) {
               throw this.createXMLException("unexpected.eof");
            } else {
               if (this.current != 34 && this.current != 38 && this.current != 37) {
                  do {
                     this.nextChar();
                  } while(this.current != -1 && this.current != 34 && this.current != 38 && this.current != 37);
               }

               switch (this.current) {
                  case 34:
                     this.nextChar();
                     return 25;
                  case 35:
                  case 36:
                  default:
                     throw this.createXMLException("invalid.character");
                  case 37:
                  case 38:
                     this.context = 17;
                     return 16;
               }
            }
         case 37:
            this.nextChar();
            return 58;
         case 39:
            this.attrDelimiter = '\'';
            this.nextChar();
            if (this.current == -1) {
               throw this.createXMLException("unexpected.eof");
            } else {
               if (this.current != 39 && this.current != 38 && this.current != 37) {
                  do {
                     this.nextChar();
                  } while(this.current != -1 && this.current != 39 && this.current != 38 && this.current != 37);
               }

               switch (this.current) {
                  case 37:
                  case 38:
                     this.context = 17;
                     return 16;
                  case 39:
                     this.nextChar();
                     return 25;
                  default:
                     throw this.createXMLException("invalid.character");
               }
            }
         case 62:
            this.nextChar();
            this.context = 7;
            return 20;
         case 78:
            return this.readIdentifier("DATA", 59, 14);
         case 80:
            return this.readIdentifier("UBLIC", 27, 14);
         case 83:
            return this.readIdentifier("YSTEM", 26, 14);
         default:
            return this.readName(14);
      }
   }

   protected int nextInEntityValue() throws IOException, XMLException {
      switch (this.current) {
         case 37:
            int var1 = this.nextChar();
            this.readName(34);
            if (this.current != 59) {
               throw this.createXMLException("invalid.parameter.entity");
            }

            this.nextChar();
            return var1;
         case 38:
            return this.readReference();
         default:
            while(this.current != -1 && this.current != this.attrDelimiter && this.current != 38 && this.current != 37) {
               this.nextChar();
            }

            switch (this.current) {
               case -1:
                  throw this.createXMLException("unexpected.eof");
               case 34:
               case 39:
                  this.nextChar();
                  this.context = 13;
                  return 25;
               default:
                  return 16;
            }
      }
   }

   protected int nextInNotationType() throws IOException, XMLException {
      switch (this.current) {
         case 9:
         case 10:
         case 13:
         case 32:
            do {
               this.nextChar();
            } while(this.current != -1 && XMLUtilities.isXMLSpace((char)this.current));

            return 1;
         case 40:
            this.nextChar();
            return 40;
         case 41:
            this.nextChar();
            this.context = 11;
            return 41;
         case 124:
            this.nextChar();
            return 42;
         default:
            return this.readName(14);
      }
   }

   protected int nextInEnumeration() throws IOException, XMLException {
      switch (this.current) {
         case 9:
         case 10:
         case 13:
         case 32:
            do {
               this.nextChar();
            } while(this.current != -1 && XMLUtilities.isXMLSpace((char)this.current));

            return 1;
         case 41:
            this.nextChar();
            this.context = 11;
            return 41;
         case 124:
            this.nextChar();
            return 42;
         default:
            return this.readNmtoken();
      }
   }

   protected int readReference() throws IOException, XMLException {
      this.nextChar();
      int var1;
      if (this.current != 35) {
         var1 = this.readName(13);
         if (this.current != 59) {
            throw this.createXMLException("character.reference");
         } else {
            this.nextChar();
            return var1;
         }
      } else {
         this.nextChar();
         var1 = 0;
         switch (this.current) {
            case -1:
               throw this.createXMLException("unexpected.eof");
            case 120:
               do {
                  do {
                     ++var1;
                     this.nextChar();
                  } while(this.current >= 48 && this.current <= 57 || this.current >= 97 && this.current <= 102);
               } while(this.current >= 65 && this.current <= 70);
               break;
            default:
               do {
                  ++var1;
                  this.nextChar();
               } while(this.current >= 48 && this.current <= 57);
         }

         if (var1 != 1 && this.current == 59) {
            this.nextChar();
            return 12;
         } else {
            throw this.createXMLException("character.reference");
         }
      }
   }

   protected int readPEReference() throws IOException, XMLException {
      this.nextChar();
      if (this.current == -1) {
         throw this.createXMLException("unexpected.eof");
      } else if (!XMLUtilities.isXMLNameFirstCharacter((char)this.current)) {
         throw this.createXMLException("invalid.parameter.entity");
      } else {
         do {
            this.nextChar();
         } while(this.current != -1 && XMLUtilities.isXMLNameCharacter((char)this.current));

         if (this.current != 59) {
            throw this.createXMLException("invalid.parameter.entity");
         } else {
            this.nextChar();
            return 34;
         }
      }
   }

   protected int readNmtoken() throws IOException, XMLException {
      if (this.current == -1) {
         throw this.createXMLException("unexpected.eof");
      } else {
         while(XMLUtilities.isXMLNameCharacter((char)this.current)) {
            this.nextChar();
         }

         return 56;
      }
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

   protected XMLException createXMLException(String var1) {
      String var2;
      try {
         var2 = this.formatMessage(var1, new Object[]{new Integer(this.reader.getLine()), new Integer(this.reader.getColumn())});
      } catch (MissingResourceException var4) {
         var2 = var1;
      }

      return new XMLException(var2);
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}

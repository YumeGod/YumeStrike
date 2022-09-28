package org.apache.batik.bridge.svg12;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.batik.dom.svg12.XBLOMContentElement;
import org.apache.batik.parser.AbstractScanner;
import org.apache.batik.parser.ParseException;
import org.apache.batik.xml.XMLUtilities;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathSubsetContentSelector extends AbstractContentSelector {
   protected static final int SELECTOR_INVALID = -1;
   protected static final int SELECTOR_ANY = 0;
   protected static final int SELECTOR_QNAME = 1;
   protected static final int SELECTOR_ID = 2;
   protected int selectorType;
   protected String prefix;
   protected String localName;
   protected int index;
   protected SelectedNodes selectedContent;

   public XPathSubsetContentSelector(ContentManager var1, XBLOMContentElement var2, Element var3, String var4) {
      super(var1, var2, var3);
      this.parseSelector(var4);
   }

   protected void parseSelector(String var1) {
      this.selectorType = -1;
      Scanner var2 = new Scanner(var1);
      int var3 = var2.next();
      if (var3 == 1) {
         String var4 = var2.getStringValue();
         var3 = var2.next();
         if (var3 == 0) {
            this.selectorType = 1;
            this.prefix = null;
            this.localName = var4;
            this.index = 0;
            return;
         }

         if (var3 == 2) {
            var3 = var2.next();
            String var5;
            if (var3 == 1) {
               var5 = var2.getStringValue();
               var3 = var2.next();
               if (var3 == 0) {
                  this.selectorType = 1;
                  this.prefix = var4;
                  this.localName = var5;
                  this.index = 0;
                  return;
               }

               if (var3 == 3) {
                  var3 = var2.next();
                  if (var3 == 8) {
                     int var6 = Integer.parseInt(var2.getStringValue());
                     var3 = var2.next();
                     if (var3 == 4) {
                        var3 = var2.next();
                        if (var3 == 0) {
                           this.selectorType = 1;
                           this.prefix = var4;
                           this.localName = var5;
                           this.index = var6;
                           return;
                        }
                     }
                  }
               }
            } else if (var3 == 3) {
               var3 = var2.next();
               if (var3 == 8) {
                  int var8 = Integer.parseInt(var2.getStringValue());
                  var3 = var2.next();
                  if (var3 == 4) {
                     var3 = var2.next();
                     if (var3 == 0) {
                        this.selectorType = 1;
                        this.prefix = null;
                        this.localName = var4;
                        this.index = var8;
                        return;
                     }
                  }
               }
            } else if (var3 == 5 && var4.equals("id")) {
               var3 = var2.next();
               if (var3 == 7) {
                  var5 = var2.getStringValue();
                  var3 = var2.next();
                  if (var3 == 6) {
                     var3 = var2.next();
                     if (var3 == 0) {
                        this.selectorType = 2;
                        this.localName = var5;
                        return;
                     }
                  }
               }
            }
         }
      } else if (var3 == 9) {
         var3 = var2.next();
         if (var3 == 0) {
            this.selectorType = 0;
            return;
         }

         if (var3 == 3) {
            var3 = var2.next();
            if (var3 == 8) {
               int var7 = Integer.parseInt(var2.getStringValue());
               var3 = var2.next();
               if (var3 == 4) {
                  var3 = var2.next();
                  if (var3 == 0) {
                     this.selectorType = 0;
                     this.index = var7;
                     return;
                  }
               }
            }
         }
      }

   }

   public NodeList getSelectedContent() {
      if (this.selectedContent == null) {
         this.selectedContent = new SelectedNodes();
      }

      return this.selectedContent;
   }

   boolean update() {
      if (this.selectedContent == null) {
         this.selectedContent = new SelectedNodes();
         return true;
      } else {
         return this.selectedContent.update();
      }
   }

   protected static class Scanner extends AbstractScanner {
      public static final int EOF = 0;
      public static final int NAME = 1;
      public static final int COLON = 2;
      public static final int LEFT_SQUARE_BRACKET = 3;
      public static final int RIGHT_SQUARE_BRACKET = 4;
      public static final int LEFT_PARENTHESIS = 5;
      public static final int RIGHT_PARENTHESIS = 6;
      public static final int STRING = 7;
      public static final int NUMBER = 8;
      public static final int ASTERISK = 9;

      public Scanner(String var1) {
         super(var1);
      }

      protected int endGap() {
         return this.current == -1 ? 0 : 1;
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
               case 33:
               case 35:
               case 36:
               case 37:
               case 38:
               case 43:
               case 44:
               case 45:
               case 46:
               case 47:
               case 59:
               case 60:
               case 61:
               case 62:
               case 63:
               case 64:
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
               case 85:
               case 86:
               case 87:
               case 88:
               case 89:
               case 90:
               case 92:
               default:
                  if (!XMLUtilities.isXMLNameFirstCharacter((char)this.current)) {
                     this.nextChar();
                     throw new ParseException("identifier.character", this.reader.getLine(), this.reader.getColumn());
                  }

                  do {
                     this.nextChar();
                  } while(this.current != -1 && this.current != 58 && XMLUtilities.isXMLNameCharacter((char)this.current));

                  this.type = 1;
                  return;
               case 9:
               case 10:
               case 12:
               case 13:
               case 32:
                  do {
                     this.nextChar();
                  } while(XMLUtilities.isXMLSpace((char)this.current));

                  this.nextToken();
                  return;
               case 34:
                  this.type = this.string2();
                  return;
               case 39:
                  this.type = this.string1();
                  return;
               case 40:
                  this.nextChar();
                  this.type = 5;
                  return;
               case 41:
                  this.nextChar();
                  this.type = 6;
                  return;
               case 42:
                  this.nextChar();
                  this.type = 9;
                  return;
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
                  this.type = 2;
                  return;
               case 91:
                  this.nextChar();
                  this.type = 3;
                  return;
               case 93:
                  this.nextChar();
                  this.type = 4;
            }
         } catch (IOException var2) {
            throw new ParseException(var2);
         }
      }

      protected int string1() throws IOException {
         this.start = this.position;

         while(true) {
            switch (this.nextChar()) {
               case -1:
                  throw new ParseException("eof", this.reader.getLine(), this.reader.getColumn());
               case 39:
                  this.nextChar();
                  return 7;
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
                  return 7;
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
                  return 8;
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
                  return 8;
            }
         }
      }
   }

   protected class SelectedNodes implements NodeList {
      protected ArrayList nodes = new ArrayList(10);

      public SelectedNodes() {
         this.update();
      }

      protected boolean update() {
         ArrayList var1 = (ArrayList)this.nodes.clone();
         this.nodes.clear();
         int var2 = 0;

         for(Node var3 = XPathSubsetContentSelector.this.boundElement.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
            if (var3.getNodeType() == 1) {
               Element var4 = (Element)var3;
               boolean var5 = XPathSubsetContentSelector.this.selectorType == 0;
               switch (XPathSubsetContentSelector.this.selectorType) {
                  case 1:
                     if (XPathSubsetContentSelector.this.prefix == null) {
                        var5 = var4.getNamespaceURI() == null;
                     } else {
                        String var6 = XPathSubsetContentSelector.this.contentElement.lookupNamespaceURI(XPathSubsetContentSelector.this.prefix);
                        if (var6 != null) {
                           var5 = var4.getNamespaceURI().equals(var6);
                        }
                     }

                     var5 = var5 && XPathSubsetContentSelector.this.localName.equals(var4.getLocalName());
                     break;
                  case 2:
                     var5 = var4.getAttributeNS((String)null, "id").equals(XPathSubsetContentSelector.this.localName);
               }

               if (XPathSubsetContentSelector.this.selectorType == 0 || XPathSubsetContentSelector.this.selectorType == 1) {
                  boolean var10000;
                  label73: {
                     label72: {
                        if (var5) {
                           if (XPathSubsetContentSelector.this.index == 0) {
                              break label72;
                           }

                           ++var2;
                           if (var2 == XPathSubsetContentSelector.this.index) {
                              break label72;
                           }
                        }

                        var10000 = false;
                        break label73;
                     }

                     var10000 = true;
                  }

                  var5 = var10000;
               }

               if (var5 && !XPathSubsetContentSelector.this.isSelected(var3)) {
                  this.nodes.add(var4);
               }
            }
         }

         int var7 = this.nodes.size();
         if (var1.size() != var7) {
            return true;
         } else {
            for(int var8 = 0; var8 < var7; ++var8) {
               if (var1.get(var8) != this.nodes.get(var8)) {
                  return true;
               }
            }

            return false;
         }
      }

      public Node item(int var1) {
         return var1 >= 0 && var1 < this.nodes.size() ? (Node)this.nodes.get(var1) : null;
      }

      public int getLength() {
         return this.nodes.size();
      }
   }
}

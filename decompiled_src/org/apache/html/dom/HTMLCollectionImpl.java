package org.apache.html.dom;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLAppletElement;
import org.w3c.dom.html.HTMLAreaElement;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLImageElement;
import org.w3c.dom.html.HTMLObjectElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

class HTMLCollectionImpl implements HTMLCollection {
   static final short ANCHOR = 1;
   static final short FORM = 2;
   static final short IMAGE = 3;
   static final short APPLET = 4;
   static final short LINK = 5;
   static final short OPTION = 6;
   static final short ROW = 7;
   static final short ELEMENT = 8;
   static final short AREA = -1;
   static final short TBODY = -2;
   static final short CELL = -3;
   private short _lookingFor;
   private Element _topLevel;

   HTMLCollectionImpl(HTMLElement var1, short var2) {
      if (var1 == null) {
         throw new NullPointerException("HTM011 Argument 'topLevel' is null.");
      } else {
         this._topLevel = var1;
         this._lookingFor = var2;
      }
   }

   public final int getLength() {
      return this.getLength(this._topLevel);
   }

   public final Node item(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("HTM012 Argument 'index' is negative.");
      } else {
         return this.item(this._topLevel, new CollectionIndex(var1));
      }
   }

   public final Node namedItem(String var1) {
      if (var1 == null) {
         throw new NullPointerException("HTM013 Argument 'name' is null.");
      } else {
         return this.namedItem(this._topLevel, var1);
      }
   }

   private int getLength(Element var1) {
      synchronized(var1) {
         int var2 = 0;

         for(Node var3 = var1.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
            if (var3 instanceof Element) {
               if (this.collectionMatch((Element)var3, (String)null)) {
                  ++var2;
               } else if (this.recurse()) {
                  var2 += this.getLength((Element)var3);
               }
            }
         }

         return var2;
      }
   }

   private Node item(Element var1, CollectionIndex var2) {
      synchronized(var1) {
         for(Node var3 = var1.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
            if (var3 instanceof Element) {
               if (this.collectionMatch((Element)var3, (String)null)) {
                  if (var2.isZero()) {
                     return var3;
                  }

                  var2.decrement();
               } else if (this.recurse()) {
                  Node var4 = this.item((Element)var3, var2);
                  if (var4 != null) {
                     return var4;
                  }
               }
            }
         }

         return null;
      }
   }

   private Node namedItem(Element var1, String var2) {
      synchronized(var1) {
         Node var3;
         for(var3 = var1.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
            if (var3 instanceof Element) {
               if (this.collectionMatch((Element)var3, var2)) {
                  return var3;
               }

               if (this.recurse()) {
                  Node var4 = this.namedItem((Element)var3, var2);
                  if (var4 != null) {
                     return var4;
                  }
               }
            }
         }

         return var3;
      }
   }

   protected boolean recurse() {
      return this._lookingFor > 0;
   }

   protected boolean collectionMatch(Element var1, String var2) {
      synchronized(var1) {
         boolean var3 = false;
         switch (this._lookingFor) {
            case -3:
               var3 = var1 instanceof HTMLTableCellElement;
               break;
            case -2:
               var3 = var1 instanceof HTMLTableSectionElement && var1.getTagName().equals("tbody");
               break;
            case -1:
               var3 = var1 instanceof HTMLAreaElement;
            case 0:
            default:
               break;
            case 1:
               var3 = var1 instanceof HTMLAnchorElement && var1.getAttribute("name").length() > 0;
               break;
            case 2:
               var3 = var1 instanceof HTMLFormElement;
               break;
            case 3:
               var3 = var1 instanceof HTMLImageElement;
               break;
            case 4:
               var3 = var1 instanceof HTMLAppletElement || var1 instanceof HTMLObjectElement && ("application/java".equals(var1.getAttribute("codetype")) || var1.getAttribute("classid").startsWith("java:"));
               break;
            case 5:
               var3 = (var1 instanceof HTMLAnchorElement || var1 instanceof HTMLAreaElement) && var1.getAttribute("href").length() > 0;
               break;
            case 6:
               var3 = var1 instanceof HTMLOptionElement;
               break;
            case 7:
               var3 = var1 instanceof HTMLTableRowElement;
               break;
            case 8:
               var3 = var1 instanceof HTMLFormControl;
         }

         if (var3 && var2 != null) {
            if (var1 instanceof HTMLAnchorElement && var2.equals(var1.getAttribute("name"))) {
               boolean var5 = true;
               return var5;
            }

            var3 = var2.equals(var1.getAttribute("id"));
         }

         return var3;
      }
   }
}

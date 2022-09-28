package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public final class Paragraph {
   public ParagraphStyle style;
   public Vector bookmarks;
   public boolean hasImage;
   public ListStyle listStyle;
   private Vector elements;

   public Paragraph() {
      this((ParagraphStyle)null);
   }

   public Paragraph(ParagraphStyle var1) {
      this.elements = new Vector();
      this.style = var1;
   }

   private void add(int var1, Object var2) {
      this.elements.addElement(new Element(var1, var2));
   }

   public void add(Text var1) {
      this.add(0, var1);
   }

   public void add(Image var1) {
      this.add(3, var1);
      this.hasImage = true;
   }

   public void add(Footnote var1) {
      this.add(4, var1);
   }

   public void add(Leader var1) {
      if (this.style != null) {
         this.style.tabStops.add(var1.tabStop());
         this.addTab(var1.textStyle);
      }

   }

   public void add(Bookmark var1) {
      this.add(6, var1);
   }

   public void add(BookmarkReference var1) {
      this.add(7, var1);
   }

   public void addBreak(TextStyle var1) {
      this.add(1, var1);
   }

   public void addTab(TextStyle var1) {
      this.add(2, var1);
   }

   public void addPageNumber(TextStyle var1) {
      this.add(5, var1);
   }

   public void start(Link var1) {
      this.add(8, var1);
   }

   public void end(Link var1) {
      this.add(9, var1);
   }

   public void layout(double var1) throws Exception {
      double var3 = var1;
      if (this.style != null) {
         this.style.tabStops.layout(var1, this.style.marginLeft, this.style.marginRight);
         var3 = var1 - (this.style.marginLeft + this.style.marginRight);
         if (this.listStyle != null) {
            var3 -= this.listStyle.labelWidth;
         }
      }

      int var5 = 0;

      for(int var6 = this.elements.size(); var5 < var6; ++var5) {
         Element var7 = (Element)this.elements.elementAt(var5);
         if (var7.type == 3) {
            Image var8 = (Image)var7.object;
            var8.layout(var3);
         }
      }

   }

   public void prepend(Paragraph var1) {
      this.style.textIndent = -(this.style.marginLeft - var1.style.marginLeft);
      TabStops var2 = new TabStops();
      var2.add(new TabStop(this.style.marginLeft));
      var2.add(this.style.tabStops);
      this.style.tabStops = var2;
      int var3 = 0;

      for(int var4 = var1.elements.size(); var3 < var4; ++var3) {
         this.elements.insertElementAt(var1.elements.elementAt(var3), var3);
      }

      Element var5 = new Element(2, this.textStyle());
      this.elements.insertElementAt(var5, var1.elements.size());
   }

   public void print(PrintWriter var1, Encoder var2) {
      Element[] var3 = new Element[this.elements.size()];

      int var4;
      for(var4 = 0; var4 < var3.length; ++var4) {
         var3[var4] = (Element)this.elements.elementAt(var4);
      }

      Text var5;
      for(var4 = 0; var4 < var3.length && var3[var4].type == 0; ++var4) {
         var5 = (Text)var3[var4].object;
         if (!var5.preserveSpace) {
            var5.trimLeft();
         }

         if (var5.content.length() != 0) {
            break;
         }
      }

      for(var4 = var3.length - 1; var4 >= 0 && var3[var4].type == 0; --var4) {
         var5 = (Text)var3[var4].object;
         if (!var5.preserveSpace) {
            var5.trimRight();
         }

         if (var5.content.length() != 0) {
            break;
         }
      }

      boolean var16 = this.style != null && this.style.name != null;
      int var6;
      String var15;
      if (var16 && this.style.outlineLevel >= 1 && this.style.outlineLevel <= 9) {
         var15 = "h";
         var6 = this.style.outlineLevel;
      } else {
         var15 = "p";
         var6 = -1;
      }

      var1.print("<text:" + var15);
      if (var16) {
         if (var6 > 0) {
            var1.println(" text:outline-level=\"" + Integer.toString(var6) + "\"");
         }

         var1.println(" text:style-name=\"" + this.style.name + "\"");
      } else {
         var1.println();
      }

      var1.print(">");
      int var7;
      if (this.bookmarks != null) {
         var7 = 0;

         for(int var8 = this.bookmarks.size(); var7 < var8; ++var7) {
            Bookmark var9 = (Bookmark)this.bookmarks.elementAt(var7);
            var9.print(var1, var2);
         }
      }

      for(var7 = 0; var7 < var3.length; ++var7) {
         Element var17 = var3[var7];
         Link var14;
         switch (var17.type) {
            case 0:
               Text var18 = (Text)var17.object;
               if (var18.content.length() != 0) {
                  var18.print(var1, var2);
               }
               break;
            case 1:
               Span.start(var1, (TextStyle)var17.object);
               var1.print("<text:line-break/>");
               Span.end(var1);
               break;
            case 2:
               Span.start(var1, (TextStyle)var17.object);
               var1.print("<text:tab/>");
               Span.end(var1);
               break;
            case 3:
               Image var10 = (Image)var17.object;
               var10.print(var1);
               break;
            case 4:
               Footnote var11 = (Footnote)var17.object;
               var11.print(var1, var2);
               break;
            case 5:
               Span.start(var1, (TextStyle)var17.object);
               var1.print("<text:page-number/>");
               Span.end(var1);
               break;
            case 6:
               Bookmark var12 = (Bookmark)var17.object;
               var12.print(var1, var2);
               break;
            case 7:
               BookmarkReference var13 = (BookmarkReference)var17.object;
               var13.print(var1, var2);
               break;
            case 8:
               var14 = (Link)var17.object;
               var14.start(var1, var2);
               break;
            case 9:
               var14 = (Link)var17.object;
               var14.end(var1);
         }
      }

      var1.println("</text:" + var15 + ">");
   }

   public boolean requiresLayout() {
      return this.hasImage || this.hasTabs();
   }

   public boolean hasTabs() {
      return this.style != null && this.style.tabStops.count() > 0;
   }

   public boolean isEmpty() {
      if (this.bookmarks != null && this.bookmarks.size() > 0) {
         return false;
      } else {
         int var1 = 0;

         for(int var2 = this.elements.size(); var1 < var2; ++var1) {
            Element var3 = (Element)this.elements.elementAt(var1);
            if (var3.type != 0) {
               return false;
            }

            Text var4 = (Text)var3.object;
            if (var4.content.length() != 0 && (!var4.isSpace() || var4.preserveSpace)) {
               return false;
            }
         }

         return true;
      }
   }

   public int elementCount() {
      return this.elements.size();
   }

   public Object firstElement() {
      if (this.elements.size() > 0) {
         Element var1 = (Element)this.elements.firstElement();
         return var1.object;
      } else {
         return null;
      }
   }

   public Text text() {
      StringBuffer var1 = new StringBuffer();
      TextStyle var2 = null;
      int var3 = 0;

      for(int var4 = this.elements.size(); var3 < var4; ++var3) {
         Element var5 = (Element)this.elements.elementAt(var3);
         if (var5.type == 0) {
            Text var6 = (Text)var5.object;
            var1.append(var6.content);
            if (var2 == null) {
               var2 = var6.style;
            }
         }
      }

      return new Text(var1.toString(), var2);
   }

   public TextStyle textStyle() {
      TextStyle var1 = null;
      int var2 = 0;

      for(int var3 = this.elements.size(); var2 < var3; ++var2) {
         Element var4 = (Element)this.elements.elementAt(var2);
         if (var4.type == 0) {
            Text var5 = (Text)var4.object;
            if (var5.style != null) {
               var1 = var5.style;
               break;
            }
         }
      }

      return var1;
   }

   public double minWidth() {
      double var1 = 0.0;
      int var3 = 0;

      for(int var4 = this.elements.size(); var3 < var4; ++var3) {
         Element var5 = (Element)this.elements.elementAt(var3);
         double var7;
         switch (var5.type) {
            case 0:
               Text var6 = (Text)var5.object;
               var7 = var6.wordWidth();
               if (var7 > var1) {
                  var1 = var7;
               }
               break;
            case 3:
               Image var9 = (Image)var5.object;
               var7 = var9.width();
               if (var7 > var1) {
                  var1 = var7;
               }
         }
      }

      if (this.style != null) {
         var1 += this.style.marginLeft + this.style.marginRight;
      }

      return var1;
   }

   public double maxWidth() {
      double var1 = 0.0;
      double var3 = 0.0;
      int var5 = 0;

      for(int var6 = this.elements.size(); var5 < var6; ++var5) {
         Element var7 = (Element)this.elements.elementAt(var5);
         switch (var7.type) {
            case 0:
               Text var8 = (Text)var7.object;
               var1 += var8.textWidth();
               break;
            case 1:
               if (var1 > var3) {
                  var3 = var1;
               }

               var1 = 0.0;
            case 2:
            default:
               break;
            case 3:
               Image var9 = (Image)var7.object;
               var1 += var9.width();
         }
      }

      if (var1 > var3) {
         var3 = var1;
      }

      if (this.style != null) {
         var3 += this.style.marginLeft + this.style.marginRight;
      }

      return var3;
   }

   private class Element {
      static final int TYPE_TEXT = 0;
      static final int TYPE_BREAK = 1;
      static final int TYPE_TAB = 2;
      static final int TYPE_IMAGE = 3;
      static final int TYPE_FOOTNOTE = 4;
      static final int TYPE_PAGE_NUMBER = 5;
      static final int TYPE_BOOKMARK = 6;
      static final int TYPE_BOOKMARK_REFERENCE = 7;
      static final int TYPE_LINK_START = 8;
      static final int TYPE_LINK_END = 9;
      int type;
      Object object;

      Element(int var2, Object var3) {
         this.type = var2;
         this.object = var3;
      }
   }
}

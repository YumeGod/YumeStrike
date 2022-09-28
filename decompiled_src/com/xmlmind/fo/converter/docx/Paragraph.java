package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.converter.docx.sdt.SdtElement;
import java.io.PrintWriter;
import java.util.Vector;

public final class Paragraph {
   public static final Paragraph EMPTY = empty();
   public ParagraphProperties properties;
   public Bookmark[] bookmarks;
   public boolean startsListItem;
   public boolean hasPicture;
   private Vector runs;

   public Paragraph() {
      this((ParagraphProperties)null);
   }

   public Paragraph(ParagraphProperties var1) {
      this.runs = new Vector();
      this.properties = var1;
   }

   public void add(Text var1) {
      this.add(0, var1);
   }

   public void add(Field var1) {
      this.add(4, var1);
   }

   public void add(Picture var1) {
      this.add(5, var1);
      this.hasPicture = true;
   }

   public void add(Leader var1) {
      if (this.properties != null) {
         this.properties.tabStops.add(var1.tabStop());
         this.addTab(var1.properties);
      }

   }

   public void add(SdtElement var1) {
      this.add(10, var1);
   }

   public void addBreak(RunProperties var1) {
      this.add(1, var1);
   }

   public void addTab(RunProperties var1) {
      this.add(2, var1);
   }

   public void addPageNumber(RunProperties var1) {
      this.add(3, var1);
   }

   public void addFootnote(int var1) {
      Text var2 = null;
      int var3 = this.runs.size();
      if (var3 > 0) {
         Run var4 = (Run)this.runs.elementAt(var3 - 1);
         if (var4.type == 9) {
            if (var3 > 1) {
               var4 = (Run)this.runs.elementAt(var3 - 2);
            } else {
               var4 = null;
            }
         }

         if (var4 != null && var4.type == 0) {
            var2 = (Text)var4.object;
         }
      }

      if (var2 == null) {
         var2 = new Text();
         this.add(var2);
      }

      var2.footnoteId = var1;
   }

   public void startLink(Link var1) {
      this.add(6, var1);
   }

   public void endLink(Link var1) {
      this.add(7, var1);
   }

   public void startBookmark(Bookmark var1) {
      this.add(8, var1);
   }

   public void endBookmark(Bookmark var1) {
      this.add(9, var1);
   }

   private void add(int var1, Object var2) {
      this.runs.addElement(new Run(var1, var2));
   }

   public void layout(double var1) throws Exception {
      double var3 = var1;
      if (this.properties != null) {
         this.properties.tabStops.layout(var1, this.properties.startIndent, this.properties.endIndent);
         var3 = var1 - (this.properties.startIndent + this.properties.endIndent);
      }

      int var5 = 0;

      for(int var6 = this.runs.size(); var5 < var6; ++var5) {
         Run var7 = (Run)this.runs.elementAt(var5);
         if (var7.type == 5) {
            Picture var8 = (Picture)var7.object;
            var8.layout(var3);
         }
      }

   }

   public void prepend(Paragraph var1) {
      int var2 = 0;

      for(int var3 = var1.runs.size(); var2 < var3; ++var2) {
         this.runs.insertElementAt(var1.runs.elementAt(var2), var2);
      }

      Run var4 = new Run(2, var1.runProperties());
      this.runs.insertElementAt(var4, var1.runs.size());
   }

   public void print(PrintWriter var1) {
      Run[] var2 = new Run[this.runs.size()];

      int var3;
      for(var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = (Run)this.runs.elementAt(var3);
      }

      Text var4;
      for(var3 = 0; var3 < var2.length && var2[var3].type == 0; ++var3) {
         var4 = (Text)var2[var3].object;
         if (!var4.preserveSpace) {
            var4.trimLeft();
         }

         if (var4.content.length() != 0) {
            break;
         }
      }

      for(var3 = var2.length - 1; var3 >= 0 && var2[var3].type == 0; --var3) {
         var4 = (Text)var2[var3].object;
         if (!var4.preserveSpace) {
            var4.trimRight();
         }

         if (var4.content.length() != 0) {
            break;
         }
      }

      if (this.startsListItem) {
         label148:
         for(var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3].type == 2) {
               Text var5;
               int var12;
               for(var12 = var3 - 1; var12 >= 0 && var2[var12].type == 0; --var12) {
                  var5 = (Text)var2[var12].object;
                  if (!var5.preserveSpace) {
                     var5.trimRight();
                  }

                  if (var5.content.length() != 0) {
                     break;
                  }
               }

               var12 = var3 + 1;

               while(true) {
                  if (var12 >= var2.length || var2[var12].type != 0) {
                     break label148;
                  }

                  var5 = (Text)var2[var12].object;
                  if (!var5.preserveSpace) {
                     var5.trimLeft();
                  }

                  if (var5.content.length() != 0) {
                     break label148;
                  }

                  ++var12;
               }
            }
         }
      }

      if (this.bookmarks != null) {
         for(var3 = 0; var3 < this.bookmarks.length; ++var3) {
            this.bookmarks[var3].start(var1);
         }
      }

      var1.println("<w:p>");
      if (this.properties != null) {
         this.properties.print(var1);
         if (this.properties.breakBefore == 1) {
            var1.println("<w:r>");
            var1.println("<w:br w:type=\"column\" />");
            var1.println("</w:r>");
         }
      }

      for(var3 = 0; var3 < var2.length; ++var3) {
         Run var14 = var2[var3];
         Link var6;
         Bookmark var7;
         RunProperties var13;
         switch (var14.type) {
            case 0:
               Text var8 = (Text)var14.object;
               if (!var8.isEmpty()) {
                  var8.print(var1);
               }
               break;
            case 1:
               var1.println("<w:r>");
               var13 = (RunProperties)var14.object;
               if (var13 != null) {
                  var13.print(var1);
               }

               var1.println("<w:br w:type=\"textWrapping\" />");
               var1.println("</w:r>");
               break;
            case 2:
               var1.println("<w:r>");
               var13 = (RunProperties)var14.object;
               if (var13 != null) {
                  var13.print(var1);
               }

               var1.println("<w:tab />");
               var1.println("</w:r>");
               break;
            case 3:
               var1.println("<w:r>");
               var13 = (RunProperties)var14.object;
               if (var13 != null) {
                  var13.print(var1);
               }

               var1.println("<w:pgNum />");
               var1.println("</w:r>");
               break;
            case 4:
               Field var9 = (Field)var14.object;
               var9.print(var1);
               break;
            case 5:
               Picture var10 = (Picture)var14.object;
               var10.print(var1);
               break;
            case 6:
               var6 = (Link)var14.object;
               var6.start(var1);
               break;
            case 7:
               var6 = (Link)var14.object;
               var6.end(var1);
               break;
            case 8:
               var7 = (Bookmark)var14.object;
               var7.start(var1);
               break;
            case 9:
               var7 = (Bookmark)var14.object;
               var7.end(var1);
               break;
            case 10:
               SdtElement var11 = (SdtElement)var14.object;
               var11.print(var1);
         }
      }

      var1.println("</w:p>");
      if (this.bookmarks != null) {
         for(var3 = this.bookmarks.length - 1; var3 >= 0; --var3) {
            this.bookmarks[var3].end(var1);
         }
      }

   }

   public boolean requiresLayout() {
      return this.hasPicture || this.hasTabs();
   }

   public boolean hasTabs() {
      return this.properties != null && this.properties.tabStops.count() > 0;
   }

   public boolean isEmpty() {
      if (this.bookmarks != null && this.bookmarks.length > 0) {
         return false;
      } else {
         int var1 = 0;

         for(int var2 = this.runs.size(); var1 < var2; ++var1) {
            Run var3 = (Run)this.runs.elementAt(var1);
            if (var3.type != 0) {
               return false;
            }

            Text var4 = (Text)var3.object;
            if (!var4.isEmpty() && (!var4.isSpace() || var4.preserveSpace)) {
               return false;
            }
         }

         return true;
      }
   }

   public int elementCount() {
      return this.runs.size();
   }

   public Object firstElement() {
      if (this.runs.size() > 0) {
         Run var1 = (Run)this.runs.firstElement();
         return var1.object;
      } else {
         return null;
      }
   }

   public RunProperties runProperties() {
      RunProperties var1 = null;
      int var2 = 0;

      for(int var3 = this.runs.size(); var2 < var3; ++var2) {
         Run var4 = (Run)this.runs.elementAt(var2);
         if (var4.type == 0) {
            Text var5 = (Text)var4.object;
            if (var5.properties != null) {
               var1 = var5.properties;
               break;
            }
         }
      }

      return var1;
   }

   public double minWidth() {
      double var1 = 0.0;
      int var3 = 0;

      for(int var4 = this.runs.size(); var3 < var4; ++var3) {
         Run var5 = (Run)this.runs.elementAt(var3);
         double var7;
         switch (var5.type) {
            case 0:
               Text var6 = (Text)var5.object;
               var7 = var6.wordWidth();
               if (var7 > var1) {
                  var1 = var7;
               }
               break;
            case 5:
               Picture var9 = (Picture)var5.object;
               var7 = var9.width();
               if (var7 > var1) {
                  var1 = var7;
               }
         }
      }

      if (this.properties != null) {
         var1 += this.properties.startIndent + this.properties.endIndent;
      }

      return var1;
   }

   public double maxWidth() {
      double var1 = 0.0;
      double var3 = 0.0;
      int var5 = 0;

      for(int var6 = this.runs.size(); var5 < var6; ++var5) {
         Run var7 = (Run)this.runs.elementAt(var5);
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
               break;
            case 5:
               Picture var9 = (Picture)var7.object;
               var1 += var9.width();
         }
      }

      if (var1 > var3) {
         var3 = var1;
      }

      if (this.properties != null) {
         var3 += this.properties.startIndent + this.properties.endIndent;
      }

      return var3;
   }

   public static Paragraph empty() {
      return empty(0.0);
   }

   public static Paragraph empty(double var0) {
      Paragraph var2 = new Paragraph(new ParagraphProperties());
      if (var0 > 0.0) {
         RunProperties var3 = new RunProperties();
         var3.fontSize = var0;
         var2.properties.markProperties = var3;
      }

      return var2;
   }

   private class Run {
      static final int TYPE_TEXT = 0;
      static final int TYPE_BREAK = 1;
      static final int TYPE_TAB = 2;
      static final int TYPE_PAGE_NUMBER = 3;
      static final int TYPE_FIELD = 4;
      static final int TYPE_PICTURE = 5;
      static final int TYPE_LINK_START = 6;
      static final int TYPE_LINK_END = 7;
      static final int TYPE_BOOKMARK_START = 8;
      static final int TYPE_BOOKMARK_END = 9;
      static final int TYPE_SDT_ELEMENT = 10;
      int type;
      Object object;

      Run(int var2, Object var3) {
         this.type = var2;
         this.object = var3;
      }
   }
}

package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public final class Paragraph {
   public static final int RUN_TYPE_TEXT = 1;
   public static final int RUN_TYPE_BREAK = 2;
   public static final int RUN_TYPE_TAB = 3;
   public static final int RUN_TYPE_PGNUM = 4;
   public static final int RUN_TYPE_FIELD = 5;
   public static final int RUN_TYPE_PICTURE = 6;
   public static final int RUN_TYPE_XML = 7;
   public static final Paragraph EMPTY = new Paragraph();
   public Vector runs;
   public ParProperties properties;
   public Bookmark[] bookmarks;
   public boolean startsListItem;
   public boolean hasPicture;

   public Paragraph() {
      this((ParProperties)null);
   }

   public Paragraph(ParProperties var1) {
      this.runs = new Vector();
      this.properties = var1;
   }

   public void add(Object var1, int var2) {
      this.runs.addElement(new Run(var2, var1));
   }

   public void add(Text var1) {
      this.add(var1, 1);
   }

   public void add(Break var1) {
      this.add(var1, 2);
   }

   public void add(Tab var1) {
      this.add(var1, 3);
   }

   public void add(PageNumber var1) {
      this.add(var1, 4);
   }

   public void add(Field var1) {
      this.add(var1, 5);
   }

   public void add(Picture var1) {
      this.add(var1, 6);
      this.hasPicture = true;
   }

   public void add(String var1) {
      this.add(var1, 7);
   }

   public void add(Footnote var1) {
      Text var2 = null;
      int var3 = this.runs.size();
      if (var3 > 0) {
         Run var4 = (Run)this.runs.elementAt(var3 - 1);
         if (var4.type == 7) {
            if (var3 > 1) {
               var4 = (Run)this.runs.elementAt(var3 - 2);
            } else {
               var4 = null;
            }
         }

         if (var4 != null && var4.type == 1) {
            var2 = (Text)var4.object;
         }
      }

      if (var2 == null) {
         var2 = new Text();
         this.add(var2);
      }

      var2.footnote = var1;
   }

   public void add(Vector var1) {
      int var2 = 0;

      for(int var3 = var1.size(); var2 < var3; ++var2) {
         this.runs.addElement(var1.elementAt(var2));
      }

   }

   public void layout(int var1) throws Exception {
      if (this.hasPicture) {
         if (this.properties != null) {
            var1 -= this.properties.startIndent + this.properties.endIndent;
         }

         double var2 = (double)var1 / 20.0;
         int var4 = 0;

         for(int var5 = this.runs.size(); var4 < var5; ++var4) {
            Run var6 = (Run)this.runs.elementAt(var4);
            if (var6.type == 6) {
               Picture var7 = (Picture)var6.object;
               var7.layout(var2);
            }
         }

      }
   }

   public void print(PrintWriter var1) throws Exception {
      this.print(var1, (Encoder)null);
   }

   public void print(PrintWriter var1, Encoder var2) throws Exception {
      Run[] var3 = new Run[this.runs.size()];

      int var4;
      for(var4 = 0; var4 < var3.length; ++var4) {
         var3[var4] = (Run)this.runs.elementAt(var4);
      }

      if (this.bookmarks != null) {
         for(var4 = 0; var4 < this.bookmarks.length; ++var4) {
            this.bookmarks[var4].start(var1, var2);
         }
      }

      var1.println("<w:p>");
      if (this.properties != null) {
         this.properties.print(var1);
         switch (this.properties.breakBefore) {
            case 1:
               Break.COLUMN.print(var1);
         }
      }

      Text var5;
      for(var4 = 0; var4 < var3.length && var3[var4].type == 1; ++var4) {
         var5 = (Text)var3[var4].object;
         var5.trimLeft();
         if (!var5.isVoid()) {
            break;
         }

         var3[var4].discard = true;
      }

      for(var4 = var3.length - 1; var4 >= 0 && var3[var4].type == 1; --var4) {
         var5 = (Text)var3[var4].object;
         var5.trimRight();
         if (!var5.isVoid()) {
            break;
         }

         var3[var4].discard = true;
      }

      if (this.startsListItem) {
         label117:
         for(var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].type == 3) {
               Tab var12 = (Tab)var3[var4].object;
               if (var12.isSeparator) {
                  int var6;
                  Text var7;
                  for(var6 = var4 - 1; var6 >= 0 && var3[var6].type == 1; --var6) {
                     var7 = (Text)var3[var6].object;
                     var7.trimRight();
                     if (!var7.isVoid()) {
                        break;
                     }

                     var3[var6].discard = true;
                  }

                  var6 = var4 + 1;

                  while(true) {
                     if (var6 >= var3.length || var3[var6].type != 1) {
                        break label117;
                     }

                     var7 = (Text)var3[var6].object;
                     var7.trimLeft();
                     if (!var7.isVoid()) {
                        break label117;
                     }

                     var3[var6].discard = true;
                     ++var6;
                  }
               }
            }
         }
      }

      for(var4 = 0; var4 < var3.length; ++var4) {
         Run var13 = var3[var4];
         if (!var13.discard) {
            switch (var13.type) {
               case 1:
                  Text var14 = (Text)var13.object;
                  var14.print(var1, var2);
                  break;
               case 2:
                  Break var15 = (Break)var13.object;
                  var15.print(var1);
                  break;
               case 3:
                  Tab var8 = (Tab)var13.object;
                  var8.print(var1);
                  break;
               case 4:
                  PageNumber var9 = (PageNumber)var13.object;
                  var9.print(var1);
                  break;
               case 5:
                  Field var10 = (Field)var13.object;
                  var10.print(var1, var2);
                  break;
               case 6:
                  Picture var11 = (Picture)var13.object;
                  var11.print(var1);
                  break;
               case 7:
                  var1.println((String)var13.object);
            }
         }
      }

      var1.println("</w:p>");
      if (this.bookmarks != null) {
         for(var4 = this.bookmarks.length - 1; var4 >= 0; --var4) {
            this.bookmarks[var4].end(var1);
         }
      }

   }

   public boolean isVoid() {
      int var1 = 0;

      for(int var2 = this.runs.size(); var1 < var2; ++var1) {
         Run var3 = (Run)this.runs.elementAt(var1);
         if (var3.type != 1) {
            return false;
         }

         Text var4 = (Text)var3.object;
         if (!var4.isSpace() || var4.preserveSpace()) {
            return false;
         }

         if (var4.footnote != null) {
            return false;
         }
      }

      if (this.properties != null && this.properties.listId != 0) {
         return false;
      } else if (this.bookmarks != null) {
         return false;
      } else {
         return true;
      }
   }

   public boolean hasTabs() {
      return this.properties != null && this.properties.hasTabs();
   }

   public int maxWidth() {
      int var1 = 0;
      int var2 = 0;
      int var3 = 0;

      for(int var4 = this.runs.size(); var3 < var4; ++var3) {
         Run var5 = (Run)this.runs.elementAt(var3);
         switch (var5.type) {
            case 1:
               Text var6 = (Text)var5.object;
               var2 += var6.textWidth();
               break;
            case 2:
               if (var2 > var1) {
                  var1 = var2;
               }

               var2 = 0;
               break;
            case 6:
               Picture var7 = (Picture)var5.object;
               var2 += var7.width();
         }
      }

      if (var2 > var1) {
         var1 = var2;
      }

      if (this.properties != null) {
         var1 += this.properties.startIndent;
         var1 += this.properties.endIndent;
      }

      return var1;
   }

   public int minWidth() {
      int var2 = 0;
      int var3 = 0;

      for(int var4 = this.runs.size(); var3 < var4; ++var3) {
         Run var5 = (Run)this.runs.elementAt(var3);
         int var1;
         switch (var5.type) {
            case 1:
               Text var6 = (Text)var5.object;
               var1 = var6.wordWidth();
               if (var1 > var2) {
                  var2 = var1;
               }
               break;
            case 6:
               Picture var7 = (Picture)var5.object;
               var1 = var7.width();
               if (var1 > var2) {
                  var2 = var1;
               }
         }
      }

      if (this.properties != null) {
         var2 += this.properties.startIndent;
         var2 += this.properties.endIndent;
      }

      return var2;
   }

   public static Paragraph empty() {
      return empty(0);
   }

   public static Paragraph empty(int var0) {
      Paragraph var1 = new Paragraph(new ParProperties());
      if (var0 > 0) {
         RunProperties var2 = new RunProperties();
         var2.fontSize = var0;
         var1.properties.markProperties = var2;
      }

      return var1;
   }

   public static class Tab {
      public RunProperties properties;
      public boolean isSeparator;

      public Tab(RunProperties var1) {
         this.properties = var1;
      }

      public void print(PrintWriter var1) {
         var1.println("<w:r>");
         if (this.properties != null) {
            this.properties.print(var1);
         }

         var1.println("<w:tab />");
         var1.println("</w:r>");
      }
   }

   public static class Run {
      public int type;
      public Object object;
      public boolean discard;

      Run(int var1, Object var2) {
         this.type = var1;
         this.object = var2;
      }
   }
}

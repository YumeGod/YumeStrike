package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.LabelFormat;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;
import java.util.Vector;

public final class ListItem {
   public int breakBefore;
   public boolean keepTogether;
   public boolean keepWithNext;
   public double spaceBefore;
   public double spaceAfter;
   public double labelStart;
   public double labelWidth;
   public double labelSeparation;
   public double bodyStart;
   public boolean containsTable;
   public boolean containsList;
   private Paragraph label;
   private Vector body = new Vector();
   private double referenceWidth;

   public ListItem(Context var1) {
      this.initialize(var1);
   }

   private void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      this.breakBefore = var1.breakBefore;
      this.keepTogether = keep(var2[137]);
      this.keepWithNext = keep(var2[141]);
      this.spaceBefore = var1.spaceBefore();
      Context var3 = var1.ancestor(19);
      var2 = var3.properties.values;
      this.labelSeparation = var2[224].length();
   }

   private static boolean keep(Value var0) {
      boolean var1 = false;
      switch (var0.type) {
         case 1:
            if (var0.keyword() == 8) {
               var1 = true;
            }
            break;
         case 2:
            if (var0.integer() > 0) {
               var1 = true;
            }
      }

      return var1;
   }

   public void setLabel(Paragraph var1) {
      if (this.label == null) {
         this.label = var1;
      }

   }

   public void add(Paragraph var1) {
      this.add(0, var1);
   }

   public void add(Table var1) {
      this.add(1, var1);
      this.containsTable = true;
   }

   public void add(TableAndCaption var1) {
      this.add(2, var1);
      this.containsTable = true;
   }

   public void add(List var1) {
      this.add(3, var1);
      this.containsList = true;
   }

   private void add(int var1, Object var2) {
      this.body.addElement(new Element(var1, var2));
   }

   public void layout(double var1, NumberingDefinitions var3) throws Exception {
      if (this.body.size() != 0) {
         if (this.containsTable || this.containsList) {
            int var6 = 0;

            for(int var7 = this.body.size(); var6 < var7; ++var6) {
               Element var8 = (Element)this.body.elementAt(var6);
               switch (var8.type) {
                  case 1:
                     Table var9 = (Table)var8.object;
                     var9.layout(var1, var3);
                     break;
                  case 2:
                     TableAndCaption var10 = (TableAndCaption)var8.object;
                     var10.layout(var1, var3);
                     break;
                  case 3:
                     List var11 = (List)var8.object;
                     var11.layout(var1, var3);
               }
            }
         }

         Element var13 = (Element)this.body.firstElement();
         switch (var13.type) {
            case 0:
               Paragraph var14 = (Paragraph)var13.object;
               this.bodyStart = var14.properties.startIndent;
               break;
            case 1:
               Table var15 = (Table)var13.object;
               this.bodyStart = var15.startIndent();
               break;
            case 2:
               TableAndCaption var16 = (TableAndCaption)var13.object;
               this.bodyStart = var16.table.startIndent();
               break;
            case 3:
               List var12 = (List)var13.object;
               this.bodyStart = var12.startIndent();
         }

         if (this.label != null) {
            this.labelStart = this.label.properties.startIndent;
            double var4;
            if (this.label.properties.endIndent > 0.0) {
               var4 = var1 - this.label.properties.endIndent;
               this.labelSeparation = this.bodyStart - var4;
            } else {
               var4 = this.bodyStart - this.labelSeparation;
            }

            this.labelWidth = var4 - this.labelStart;
         } else {
            this.labelSeparation = this.bodyStart;
         }

         this.referenceWidth = var1;
      }
   }

   public void setup(Numbering var1) throws Exception {
      Element var4 = null;
      if (this.body.size() != 0) {
         var4 = (Element)this.body.firstElement();
      }

      Paragraph var5;
      Element var7;
      if (var4 != null && var4.type == 0) {
         var5 = (Paragraph)var4.object;
      } else {
         var5 = Paragraph.empty();
         var5.properties.startIndent = this.bodyStart;
         var7 = new Element(0, var5);
         this.body.insertElementAt(var7, 0);
      }

      ParagraphProperties var6 = var5.properties;
      var6.breakBefore = this.breakBefore;
      var6.spaceBefore = this.spaceBefore;
      if (var6.alignment == 1 || var6.alignment == 2) {
         var6.alignment = 0;
      }

      var6.firstLineIndent = this.labelStart - this.bodyStart;
      if (var6.startIndent > 0.0) {
         TabStops var8 = new TabStops();
         var8.add(new TabStop(var6.startIndent));
         var8.add(var6.tabStops);
         var6.tabStops = var8;
      }

      if (var1 != null) {
         var6.numberingId = var1.id;
         switch (var1.alignment) {
            case 1:
               var6.firstLineIndent += this.labelWidth / 2.0;
               break;
            case 2:
               var6.firstLineIndent += this.labelWidth;
         }
      } else if (this.label != null) {
         if (this.label.requiresLayout()) {
            this.label.layout(this.referenceWidth);
         }

         var5.prepend(this.label);
         var5.startsListItem = true;
      }

      int var2 = 0;

      for(int var3 = this.body.size(); var2 < var3; ++var2) {
         var7 = (Element)this.body.elementAt(var2);
         if (var7.type == 0) {
            var5 = (Paragraph)var7.object;
            if (var5.requiresLayout()) {
               var5.layout(this.referenceWidth);
            }
         }
      }

   }

   public void print(PrintWriter var1) {
      int var2 = 0;

      for(int var3 = this.body.size(); var2 < var3; ++var2) {
         Element var4 = (Element)this.body.elementAt(var2);
         switch (var4.type) {
            case 0:
               Paragraph var5 = (Paragraph)var4.object;
               var5.print(var1);
               break;
            case 1:
               Table var6 = (Table)var4.object;
               var6.print(var1);
               break;
            case 2:
               TableAndCaption var7 = (TableAndCaption)var4.object;
               var7.print(var1);
               break;
            case 3:
               List var8 = (List)var4.object;
               var8.print(var1);
         }
      }

   }

   public Numbering numbering(LabelFormat var1) {
      if (this.label != null && this.label.elementCount() == 1) {
         Object var2 = this.label.firstElement();
         Text var3 = null;
         if (var2 instanceof Text) {
            var3 = (Text)var2;
         }

         if (var3 == null) {
            return null;
         } else {
            Numbering var4 = null;
            if (var1 != null) {
               if (var1.type == 0) {
                  return null;
               }

               switch (var1.type) {
                  case 1:
                  default:
                     var4 = new Numbering(5);
                     var4.format = var1.suffix;
                     break;
                  case 2:
                     var4 = new Numbering(0, var1.start);
                     break;
                  case 3:
                     var4 = new Numbering(4, var1.start);
                     break;
                  case 4:
                     var4 = new Numbering(3, var1.start);
                     break;
                  case 5:
                     var4 = new Numbering(2, var1.start);
                     break;
                  case 6:
                     var4 = new Numbering(1, var1.start);
               }

               if (var1.type != 1) {
                  var4.format = "%1";
                  if (var1.prefix != null) {
                     var4.format = var1.prefix + var4.format;
                  }

                  if (var1.suffix != null) {
                     var4.format = var4.format + var1.suffix;
                  }
               }
            } else {
               String var8 = var3.content.trim();
               char var5;
               char var7;
               switch (var8.length()) {
                  case 1:
                     var5 = var8.charAt(0);
                     var4 = this.numbering(var5);
                     if (var4 != null) {
                        if (var4.style != 5) {
                           var4.format = "%1";
                        } else {
                           var4.format = "" + var5;
                        }
                     }
                     break;
                  case 2:
                     var7 = var8.charAt(1);
                     if (var7 == '.' || var7 == ')') {
                        var5 = var8.charAt(0);
                        var4 = this.numbering(var5);
                        if (var4 != null) {
                           if (var4.style != 5) {
                              var4.format = "%1" + var7;
                           } else {
                              var4 = null;
                           }
                        }
                     }
                     break;
                  case 3:
                     char var6 = var8.charAt(0);
                     var7 = var8.charAt(2);
                     if (var6 == '(' && var7 == ')' || var6 == '<' && var7 == '>' || var6 == '[' && var7 == ']') {
                        var5 = var8.charAt(1);
                        var4 = this.numbering(var5);
                        if (var4 != null) {
                           if (var4.style != 5) {
                              var4.format = var6 + "%1" + var7;
                           } else {
                              var4 = null;
                           }
                        }
                     }
               }
            }

            if (var4 != null) {
               byte var9 = 0;
               switch (this.label.properties.alignment) {
                  case 1:
                     var9 = 1;
                     break;
                  case 2:
                     var9 = 2;
               }

               var4.alignment = var9;
               var4.properties = var3.properties;
            }

            return var4;
         }
      } else {
         return null;
      }
   }

   private Numbering numbering(char var1) {
      byte var3 = 1;
      byte var2;
      switch (var1) {
         case '*':
         case '+':
         case '-':
         case '–':
         case '•':
            var2 = 5;
            break;
         case '0':
            var2 = 0;
            var3 = 0;
            break;
         case '1':
            var2 = 0;
            break;
         case 'A':
            var2 = 3;
            break;
         case 'I':
            var2 = 1;
            break;
         case 'a':
            var2 = 4;
            break;
         case 'i':
            var2 = 2;
            break;
         default:
            return null;
      }

      return new Numbering(var2, var3);
   }

   public double minWidth() {
      double var3 = 0.0;
      int var5 = 0;

      for(int var6 = this.body.size(); var5 < var6; ++var5) {
         Element var7 = (Element)this.body.elementAt(var5);
         double var1;
         switch (var7.type) {
            case 0:
               Paragraph var8 = (Paragraph)var7.object;
               var1 = var8.minWidth();
               if (var1 > var3) {
                  var3 = var1;
               }
               break;
            case 1:
               Table var9 = (Table)var7.object;
               var1 = var9.minWidth();
               if (var1 > var3) {
                  var3 = var1;
               }
               break;
            case 2:
               TableAndCaption var10 = (TableAndCaption)var7.object;
               var1 = var10.minWidth();
               if (var1 > var3) {
                  var3 = var1;
               }
               break;
            case 3:
               List var11 = (List)var7.object;
               var1 = var11.minWidth();
               if (var1 > var3) {
                  var3 = var1;
               }
         }
      }

      return var3;
   }

   public double maxWidth() {
      double var3 = 0.0;
      int var5 = 0;

      for(int var6 = this.body.size(); var5 < var6; ++var5) {
         Element var7 = (Element)this.body.elementAt(var5);
         double var1;
         switch (var7.type) {
            case 0:
               Paragraph var8 = (Paragraph)var7.object;
               var1 = var8.maxWidth();
               if (var1 > var3) {
                  var3 = var1;
               }
               break;
            case 1:
               Table var9 = (Table)var7.object;
               var1 = var9.maxWidth();
               if (var1 > var3) {
                  var3 = var1;
               }
               break;
            case 2:
               TableAndCaption var10 = (TableAndCaption)var7.object;
               var1 = var10.maxWidth();
               if (var1 > var3) {
                  var3 = var1;
               }
               break;
            case 3:
               List var11 = (List)var7.object;
               var1 = var11.maxWidth();
               if (var1 > var3) {
                  var3 = var1;
               }
         }
      }

      return var3;
   }

   private class Element {
      static final int TYPE_PARAGRAPH = 0;
      static final int TYPE_TABLE = 1;
      static final int TYPE_TABLE_AND_CAPTION = 2;
      static final int TYPE_LIST = 3;
      int type;
      Object object;

      Element(int var2, Object var3) {
         this.type = var2;
         this.object = var3;
      }
   }
}

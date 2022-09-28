package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.LabelFormat;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public final class ListItem {
   public int breakBefore;
   public boolean keepTogether;
   public boolean keepWithNext;
   public double marginTop;
   public double marginBottom;
   public double labelStart;
   public double labelWidth;
   public double labelSeparation;
   public double bodyStart;
   public boolean containsTable;
   public boolean containsList;
   private Paragraph label;
   private Vector body = new Vector();
   private double referenceWidth;
   private int listType;

   public ListItem(Context var1) {
      this.initialize(var1);
   }

   private void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      switch (var1.breakBefore) {
         case 1:
            this.breakBefore = 1;
            break;
         case 2:
            this.breakBefore = 2;
      }

      this.keepTogether = keep(var2[137]);
      this.keepWithNext = keep(var2[141]);
      this.marginTop = var1.spaceBefore();
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

   private void add(int var1, Object var2) {
      this.body.addElement(new Element(var1, var2));
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

   public void layout(double var1, StyleTable var3) throws Exception {
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
               this.bodyStart = var14.style.marginLeft;
               break;
            case 1:
               Table var15 = (Table)var13.object;
               this.bodyStart = var15.marginLeft();
               break;
            case 2:
               TableAndCaption var16 = (TableAndCaption)var13.object;
               this.bodyStart = var16.table.marginLeft();
               break;
            case 3:
               List var12 = (List)var13.object;
               this.bodyStart = var12.marginLeft();
         }

         if (this.label != null) {
            this.labelStart = this.label.style.marginLeft;
            double var4;
            if (this.label.style.marginRight > 0.0) {
               var4 = var1 - this.label.style.marginRight;
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

   public void setup(int var1, ListStyle var2, StyleTable var3) throws Exception {
      if (this.body.size() != 0) {
         Element var7 = (Element)this.body.firstElement();
         Paragraph var6;
         Element var8;
         if (var7.type != 0) {
            var6 = new Paragraph(new ParagraphStyle());
            var6.style.marginLeft = this.bodyStart;
            var8 = new Element(0, var6);
            this.body.insertElementAt(var8, 0);
         } else {
            var6 = (Paragraph)var7.object;
         }

         int var4;
         int var5;
         var6.style.marginTop = this.marginTop;
         label52:
         switch (var1) {
            case 0:
               var6.style.marginLeft = this.labelStart;
               var6.style.listStyleName = var2.name;
               var6.listStyle = var2;
               var4 = 1;
               var5 = this.body.size();

               while(true) {
                  if (var4 >= var5) {
                     break label52;
                  }

                  var8 = (Element)this.body.elementAt(var4);
                  switch (var8.type) {
                     case 0:
                        var6 = (Paragraph)var8.object;
                        double var10 = this.labelStart + (var6.style.marginLeft - this.bodyStart);
                        var6.style.marginLeft = var10;
                        var6.style.listStyleName = var2.name;
                        var6.listStyle = var2;
                        break;
                     case 1:
                     case 2:
                        var4 = var5;
                        break;
                     case 3:
                        List var9 = (List)var8.object;
                        if (var9.type() == 1) {
                           var4 = var5;
                        }
                  }

                  ++var4;
               }
            case 1:
               if (this.label != null) {
                  if (this.label.requiresLayout()) {
                     this.label.layout(this.referenceWidth);
                  }

                  var6.prepend(this.label);
               }
         }

         var4 = 0;

         for(var5 = this.body.size(); var4 < var5; ++var4) {
            var8 = (Element)this.body.elementAt(var4);
            if (var8.type == 0) {
               var6 = (Paragraph)var8.object;
               if (var6.requiresLayout()) {
                  var6.layout(this.referenceWidth);
               }

               var6.style = var3.add(var6.style);
            }
         }

         this.listType = var1;
      }
   }

   public boolean print(PrintWriter var1, Encoder var2) {
      boolean var3 = false;
      if (this.listType == 0) {
         var1.println("<text:list-item>");
      }

      int var4 = 0;

      for(int var5 = this.body.size(); var4 < var5; ++var4) {
         Element var6 = (Element)this.body.elementAt(var4);
         switch (var6.type) {
            case 0:
               Paragraph var7 = (Paragraph)var6.object;
               var7.print(var1, var2);
               break;
            case 1:
               if (this.listType == 0 && !var3) {
                  var1.println("</text:list-item>");
                  var1.println("</text:list>");
                  var3 = true;
               }

               Table var8 = (Table)var6.object;
               var8.print(var1, var2);
               break;
            case 2:
               if (this.listType == 0 && !var3) {
                  var1.println("</text:list-item>");
                  var1.println("</text:list>");
                  var3 = true;
               }

               TableAndCaption var9 = (TableAndCaption)var6.object;
               var9.print(var1, var2);
               break;
            case 3:
               List var10 = (List)var6.object;
               if (var10.type() == 1 && this.listType == 0 && !var3) {
                  var1.println("</text:list-item>");
                  var1.println("</text:list>");
                  var3 = true;
               }

               var10.print(var1, var2);
         }
      }

      if (this.listType == 0 && !var3) {
         var1.println("</text:list-item>");
      }

      return var3;
   }

   public ListStyle listStyle(LabelFormat var1) {
      if (this.label != null && this.label.elementCount() == 1) {
         Object var2 = this.label.firstElement();
         Text var3 = null;
         if (var2 instanceof Text) {
            var3 = (Text)var2;
         }

         if (var3 == null) {
            return null;
         } else {
            ListStyle var4 = null;
            if (var1 != null) {
               if (var1.type == 0) {
                  return null;
               }

               switch (var1.type) {
                  case 1:
                  default:
                     var4 = new ListStyle(var1.suffix.charAt(0));
                     break;
                  case 2:
                     var4 = new ListStyle(0, var1.start);
                     break;
                  case 3:
                     var4 = new ListStyle(1, var1.start);
                     break;
                  case 4:
                     var4 = new ListStyle(2, var1.start);
                     break;
                  case 5:
                     var4 = new ListStyle(3, var1.start);
                     break;
                  case 6:
                     var4 = new ListStyle(4, var1.start);
               }

               if (var1.type != 1) {
                  if (var1.prefix != null) {
                     var4.numberPrefix = var1.prefix;
                  }

                  if (var1.suffix != null) {
                     var4.numberSuffix = var1.suffix;
                  }
               }
            } else {
               String var8 = var3.content.trim();
               char var5;
               char var7;
               switch (var8.length()) {
                  case 1:
                     var5 = var8.charAt(0);
                     var4 = listStyle(var5);
                     break;
                  case 2:
                     var7 = var8.charAt(1);
                     if (var7 == '.' || var7 == ')') {
                        var5 = var8.charAt(0);
                        var4 = listStyle(var5);
                        if (var4 != null) {
                           if (var4.labelType == 0) {
                              var4.numberSuffix = toString(var7);
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
                        var4 = listStyle(var5);
                        if (var4 != null) {
                           if (var4.labelType == 0) {
                              var4.numberPrefix = toString(var6);
                              var4.numberSuffix = toString(var7);
                           } else {
                              var4 = null;
                           }
                        }
                     }
               }
            }

            if (var4 != null) {
               var4.textStyle = var3.style;
               if (this.label.style.textAlign == 5) {
                  var4.labelAlignment = this.label.style.textAlignLast;
               } else {
                  var4.labelAlignment = this.label.style.textAlign;
               }

               var4.labelWidth = this.labelWidth + this.labelSeparation;
               var4.labelDistance = this.labelSeparation;
            }

            return var4;
         }
      } else {
         return null;
      }
   }

   private static ListStyle listStyle(char var0) {
      byte var2 = 1;
      byte var1;
      switch (var0) {
         case '*':
         case '+':
         case '-':
         case '–':
         case '•':
            return new ListStyle(var0);
         case '0':
            var1 = 0;
            var2 = 0;
            break;
         case '1':
            var1 = 0;
            break;
         case 'A':
            var1 = 2;
            break;
         case 'I':
            var1 = 4;
            break;
         case 'a':
            var1 = 1;
            break;
         case 'i':
            var1 = 3;
            break;
         default:
            return null;
      }

      return new ListStyle(var1, var2);
   }

   private static String toString(char var0) {
      return new String(new char[]{var0});
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

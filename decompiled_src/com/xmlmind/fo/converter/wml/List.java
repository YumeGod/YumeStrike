package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.LabelFormat;
import com.xmlmind.fo.properties.Value;
import java.util.Vector;

public final class List {
   public int id;
   public boolean plainText;
   public int labelSeparation;
   public LabelFormat labelFormat;
   public Item item;

   public List() {
      this.id = -1;
   }

   public List(Context var1) {
      this();
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      Value var3 = var2[224];
      if (var3.type == 4) {
         this.labelSeparation = Wml.toTwips(var3.length());
      }

      var3 = var2[321];
      if (var3 != null && var3.type == 30) {
         this.labelFormat = var3.labelFormat();
      }

   }

   public static class Item {
      public int breakBefore;
      public int spaceBefore;
      public Label label;

      public Item() {
      }

      public Item(Paragraph var1) {
         this();
         this.initialize(var1);
      }

      public void initialize(Paragraph var1) {
         this.breakBefore = var1.properties.breakBefore;
         this.spaceBefore = var1.properties.spaceBefore;
         this.label = new Label(var1);
      }

      public static class Label {
         public static final int ALIGNMENT_LEFT = 0;
         public static final int ALIGNMENT_CENTER = 1;
         public static final int ALIGNMENT_RIGHT = 2;
         private static final int STYLE_NONE = 0;
         private static final int STYLE_ARABIC = 1;
         private static final int STYLE_UPPERCASE_ROMAN = 2;
         private static final int STYLE_LOWERCASE_ROMAN = 3;
         private static final int STYLE_UPPERCASE_LETTER = 4;
         private static final int STYLE_LOWERCASE_LETTER = 5;
         private static final int STYLE_BULLET = 6;
         public int alignment;
         public int startIndent;
         public int endIndent;
         public Vector runs;
         public RunProperties properties;
         public String text;

         public Label() {
            this.alignment = 0;
            this.runs = new Vector();
            this.text = "";
         }

         public Label(Paragraph var1) {
            this();
            this.initialize(var1);
         }

         public void initialize(Paragraph var1) {
            String var2 = var1.properties.alignment;
            boolean var3 = true;
            if (var2 == "center") {
               this.alignment = 1;
            } else if (var2 == "right") {
               this.alignment = 2;
            }

            this.startIndent = var1.properties.startIndent;
            this.endIndent = var1.properties.endIndent;
            int var5 = 0;

            Paragraph.Run var4;
            int var6;
            for(var6 = var1.runs.size(); var5 < var6; ++var5) {
               var4 = (Paragraph.Run)var1.runs.elementAt(var5);
               if (var4.type == 2) {
                  break;
               }

               if (var4.type != 1) {
                  var3 = false;
               }

               this.runs.addElement(var4);
            }

            if (var3) {
               StringBuffer var9 = new StringBuffer();
               var6 = 0;

               for(int var7 = this.runs.size(); var6 < var7; ++var6) {
                  var4 = (Paragraph.Run)this.runs.elementAt(var6);
                  Text var8 = (Text)var4.object;
                  if (this.properties == null) {
                     this.properties = var8.properties;
                  }

                  var9.append(var8.text);
               }

               this.text = var9.toString().trim();
            }

         }

         public ListTable.List list(LabelFormat var1) {
            ListTable.List var2;
            if (var1 != null) {
               if (var1.type == 0) {
                  return null;
               }

               var2 = new ListTable.List();
               var2.format = "%1";
               switch (var1.type) {
                  case 1:
                  default:
                     var2.style = 23;
                     var2.format = Wml.escape(var1.suffix);
                     break;
                  case 2:
                     var2.style = 0;
                     break;
                  case 3:
                     var2.style = 4;
                     break;
                  case 4:
                     var2.style = 3;
                     break;
                  case 5:
                     var2.style = 2;
                     break;
                  case 6:
                     var2.style = 1;
               }

               if (var1.type != 1) {
                  var2.start = var1.start;
                  if (var1.prefix != null) {
                     var2.format = Wml.escape(var1.prefix) + var2.format;
                  }

                  if (var1.suffix != null) {
                     var2.format = var2.format + Wml.escape(var1.suffix);
                  }
               }
            } else {
               char var3 = '1';
               int var6 = 0;
               String var7 = "%1";
               char var5;
               switch (this.text.length()) {
                  case 1:
                     var3 = this.text.charAt(0);
                     var6 = style(var3);
                     if (var6 == 6) {
                        var7 = "&#" + Integer.toString(var3) + ";";
                     }
                     break;
                  case 2:
                     var5 = this.text.charAt(1);
                     if (var5 == '.' || var5 == ')') {
                        var3 = this.text.charAt(0);
                        var6 = style(var3);
                        switch (var6) {
                           case 0:
                              break;
                           case 6:
                              var6 = 0;
                              break;
                           default:
                              var7 = "%1" + Wml.escape(var5);
                        }
                     }
                     break;
                  case 3:
                     char var4 = this.text.charAt(0);
                     var5 = this.text.charAt(2);
                     if (var4 == '(' && var5 == ')' || var4 == '<' && var5 == '>' || var4 == '[' && var5 == ']') {
                        var3 = this.text.charAt(1);
                        var6 = style(var3);
                        switch (var6) {
                           case 0:
                              break;
                           case 6:
                              var6 = 0;
                              break;
                           default:
                              var7 = Wml.escape(var4) + "%1" + Wml.escape(var5);
                        }
                     }
               }

               if (var6 == 0) {
                  return null;
               }

               var2 = new ListTable.List();
               switch (var6) {
                  case 1:
                     var2.style = 0;
                     break;
                  case 2:
                     var2.style = 1;
                     break;
                  case 3:
                     var2.style = 2;
                     break;
                  case 4:
                     var2.style = 3;
                     break;
                  case 5:
                     var2.style = 4;
                     break;
                  case 6:
                     var2.style = 23;
               }

               if (var3 == '0') {
                  var2.start = 0;
               } else {
                  var2.start = 1;
               }

               var2.format = var7;
            }

            switch (this.alignment) {
               case 0:
               default:
                  var2.alignment = "left";
                  break;
               case 1:
                  var2.alignment = "center";
                  break;
               case 2:
                  var2.alignment = "right";
            }

            var2.properties = this.properties;
            return var2;
         }

         private static int style(char var0) {
            byte var1 = 0;
            switch (var0) {
               case '*':
               case '+':
               case '-':
               case '–':
               case '•':
                  var1 = 6;
                  break;
               case '0':
               case '1':
                  var1 = 1;
                  break;
               case 'A':
                  var1 = 4;
                  break;
               case 'I':
                  var1 = 2;
                  break;
               case 'a':
                  var1 = 5;
                  break;
               case 'i':
                  var1 = 3;
            }

            return var1;
         }
      }
   }
}

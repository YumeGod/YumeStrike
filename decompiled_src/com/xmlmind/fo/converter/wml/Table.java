package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public class Table {
   public static final int ALIGNMENT_LEFT = 0;
   public static final int ALIGNMENT_CENTER = 1;
   public static final int ALIGNMENT_RIGHT = 2;
   private int breakBefore;
   private int spaceBefore;
   private boolean keepTogether;
   private int tableLayout;
   private int borderCollapse;
   private int rowSpacing;
   private int columnSpacing;
   private int width;
   private double percentage;
   private int startIndent;
   private int endIndent;
   private Borders borders;
   private Color background;
   private int alignment;
   private int tableIndent;
   private int numColumns;
   private Vector columns;
   private Vector groups;
   private boolean hasFooter;
   private boolean requiresLayout;
   private boolean isNested;
   private TableGroup group;
   private TableRow row;
   private TableCell cell;
   private TableRow[] rows;
   private int maxWidth;
   private int[] columnWidths;
   private int[][] borderWidths;

   public Table() {
      this.alignment = 0;
      this.columns = new Vector();
      this.groups = new Vector();
   }

   public Table(Context var1) {
      this();
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      this.breakBefore = var1.breakBefore;
      this.spaceBefore = Wml.toTwips(var1.spaceBefore());
      this.keepTogether = keep(var2[137]);
      this.tableLayout = var2[283].keyword();
      this.borderCollapse = var2[37].keyword();
      if (this.borderCollapse == 177) {
         byte var3 = 54;
         byte var4 = 55;
         this.rowSpacing = length(var2[var3]);
         this.columnSpacing = length(var2[var4]);
         this.rowSpacing = Math.min(this.rowSpacing, this.columnSpacing);
         this.columnSpacing = this.rowSpacing;
      }

      Value var5 = var2[308];
      switch (var5.type) {
         case 4:
            this.width = Wml.toTwips(var5.length());
            break;
         case 13:
            this.percentage = var5.percentage();
      }

      this.startIndent = length(var2[277]);
      this.endIndent = length(var2[97]);
      this.borders = new Borders(var2);
      this.borders.top.space = 0;
      this.borders.bottom.space = 0;
      this.borders.left.space = 0;
      this.borders.right.space = 0;
      this.background = var1.background;
   }

   private static int length(Value var0) {
      int var1 = 0;
      if (var0.type == 4) {
         var1 = Wml.toTwips(var0.length());
      }

      return var1;
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

   public void setColumn(Context var1) {
      TableColumn var2 = new TableColumn(var1);
      this.columns.addElement(var2);
      int var3 = var2.number + var2.repeat * var2.span - 1;
      if (var3 > this.numColumns) {
         this.numColumns = var3;
      }

   }

   public void startHeader(Context var1) {
      this.startGroup(var1, true);
   }

   private void startGroup(Context var1) {
      this.startGroup(var1, false);
   }

   private void startGroup(Context var1, boolean var2) {
      this.group = new TableGroup(var2, var1);
   }

   public void endHeader() {
      this.endGroup();
   }

   private void endGroup() {
      if (this.row != null) {
         this.endRow();
      }

      if (this.hasFooter) {
         this.groups.insertElementAt(this.group, this.groups.size() - 1);
      } else {
         this.groups.addElement(this.group);
      }

      this.group = null;
   }

   public void startBody(Context var1) {
      this.startGroup(var1);
   }

   public void endBody() {
      this.endGroup();
   }

   public void startFooter(Context var1) {
      this.startGroup(var1);
   }

   public void endFooter() {
      this.endGroup();
      this.hasFooter = true;
   }

   public void startRow(Context var1, Vector var2) {
      this.row = new TableRow(var1);
      if (var2 != null) {
         this.row.bookmarks = new Bookmark[var2.size()];

         for(int var3 = 0; var3 < this.row.bookmarks.length; ++var3) {
            this.row.bookmarks[var3] = (Bookmark)var2.elementAt(var3);
         }
      }

      this.group.add(this.row);
   }

   public void endRow() {
      this.row = null;
   }

   public void startCell(Context var1) {
      this.cell = new TableCell(var1);
      if (this.cell.startsRow || this.row == null) {
         if (this.row != null) {
            this.endRow();
         }

         this.row = new TableRow();
         this.row.borders = new Borders();
         this.group.add(this.row);
      }

      this.row.add(this.cell);
      int var2 = this.cell.columnNumber + this.cell.columnSpan - 1;
      if (var2 > this.numColumns) {
         this.numColumns = var2;
      }

   }

   public void endCell() {
      if (this.cell.endsRow) {
         this.endRow();
      }

      this.cell = null;
   }

   public void add(Paragraph var1) {
      this.cell.add(var1);
      if (var1.hasPicture) {
         this.requiresLayout = true;
      }

   }

   public void add(Table var1) {
      var1.isNested = true;
      this.cell.add(var1);
      this.requiresLayout = true;
   }

   public void add(TableAndCaption var1) {
      if (var1.table != null) {
         var1.table.isNested = true;
      }

      this.cell.add(var1);
      this.requiresLayout = true;
   }

   public void layout(int var1) throws Exception {
      if (this.columnWidths == null) {
         this.maxWidth = var1 - (this.startIndent + this.endIndent);
         if (this.percentage > 0.0) {
            this.width = (int)((double)this.maxWidth * this.percentage / 100.0);
         }

         if (this.rows == null) {
            this.rows = this.expandRowSpans();
            if (this.rows == null) {
               return;
            }
         }

         if (this.borderWidths == null) {
            this.layoutBorders();
         }

         this.setTableLayout();
         if (this.tableLayout == 69) {
            this.columnWidths = this.fixedLayout();
         } else {
            this.columnWidths = this.automaticLayout();
         }

         if (this.requiresLayout) {
            this.layoutCells();
         }

         this.expandColumnSpans();
         this.setCells();
         this.setRows();
         this.setTableIndent();
      }
   }

   public void layout(int var1, int var2) throws Exception {
      this.alignment = var2;
      this.layout(var1);
   }

   public void print(PrintWriter var1, Encoder var2) throws Exception {
      if (this.columnWidths != null) {
         if (this.rows != null) {
            int var3;
            if (this.spaceBefore > 0) {
               var3 = this.spaceBefore / 10;
               if (var3 == 0) {
                  var3 = 1;
               }

               Paragraph var4 = Paragraph.empty(var3);
               var4.print(var1);
            }

            var1.println("<w:tbl>");
            var1.print("<w:tblPr>");
            if (this.rowSpacing > 0) {
               var1.print("<w:tblCellSpacing");
               var1.print(" w:w=\"" + this.rowSpacing / 2 + "\"");
               var1.print(" w:type=\"dxa\"");
               var1.print(" />");
            }

            if (this.tableIndent != 0) {
               var1.print("<w:tblInd");
               var1.print(" w:w=\"" + this.tableIndent + "\"");
               var1.print(" w:type=\"dxa\"");
               var1.print(" />");
            }

            if (this.background != null) {
               var1.print("<w:shd");
               var1.print(" w:val=\"clear\"");
               var1.print(" w:fill=\"" + Wml.hexColorType(this.background) + "\"");
               var1.print(" />");
            }

            if (this.borderCollapse == 177 && this.borders != null && this.borders.materialized()) {
               var1.print("<w:tblBorders>");
               this.borders.print(var1);
               var1.print("</w:tblBorders>");
            }

            var1.print("<w:tblLayout w:type=\"Fixed\" />");
            var1.println("</w:tblPr>");
            var1.print("<w:tblGrid>");

            int var6;
            for(var3 = 0; var3 < this.numColumns; ++var3) {
               var6 = this.columnWidths[var3];
               if (this.borderCollapse == 177) {
                  var6 += this.borderWidths[var3][0];
                  var6 += this.borderWidths[var3][1];
                  var6 += this.columnSpacing;
               }

               var1.print("<w:gridCol w:w=\"" + var6 + "\" />");
            }

            var1.println("</w:tblGrid>");
            var3 = 0;

            for(var6 = this.groups.size(); var3 < var6; ++var3) {
               TableGroup var5 = (TableGroup)this.groups.elementAt(var3);
               var5.print(var1, var2);
            }

            var1.println("</w:tbl>");
         }
      }
   }

   private void layoutBorders() {
      if (this.borderWidths == null) {
         if (this.rows == null) {
            this.rows = this.expandRowSpans();
            if (this.rows == null) {
               return;
            }
         }

         if (this.borderCollapse == 177) {
            this.setSeparateBorders();
         } else {
            this.setCollapsedBorders();
         }

         this.setBorderWidths();
      }
   }

   private TableRow[] expandRowSpans() {
      int var5 = 0;

      for(int var6 = this.groups.size(); var5 < var6; ++var5) {
         TableGroup var7 = (TableGroup)this.groups.elementAt(var5);
         int var8 = 0;

         for(int var9 = var7.size(); var8 < var9; ++var8) {
            TableRow var1 = var7.get(var8);
            int var10 = 0;

            for(int var11 = var1.size(); var10 < var11; ++var10) {
               TableCell var3 = var1.get(var10);
               if (var3.rowSpan != 1) {
                  int var12 = var8 + 1;

                  for(int var13 = var8 + var3.rowSpan; var12 < var13; ++var12) {
                     TableRow var2;
                     if (var12 >= var7.size()) {
                        var2 = new TableRow();
                        var2.borders = new Borders();
                        var7.add(var2);
                     } else {
                        var2 = var7.get(var12);
                     }

                     TableCell var4 = var3.copy();
                     var4.rowSpan = 1;
                     var4.vMerge = "continue";
                     var4.borders.top.style = 75;
                     var4.borders.top.width = 0;
                     var4.marginTop = 0;
                     if (var12 < var13 - 1) {
                        var4.borders.bottom.style = 75;
                        var4.borders.bottom.width = 0;
                        var4.marginBottom = 0;
                     }

                     var2.add(var4);
                  }

                  var3.rowSpan = 1;
                  var3.vMerge = "restart";
                  var3.borders.bottom.style = 75;
                  var3.borders.bottom.width = 0;
                  var3.marginBottom = 0;
               }
            }
         }
      }

      return this.rows();
   }

   private void layoutCells() throws Exception {
      for(int var1 = 0; var1 < this.rows.length; ++var1) {
         int var2 = 0;

         for(int var3 = this.rows[var1].size(); var2 < var3; ++var2) {
            TableCell var4 = this.rows[var1].get(var2);
            if (var4.requiresLayout()) {
               int var5 = var4.columnNumber - 1;
               int var6 = var4.columnSpan;
               int var7 = 0;
               if (var6 > 1) {
                  for(int var8 = 0; var8 < var6; ++var8) {
                     var7 += this.columnWidths[var5 + var8];
                  }

                  if (this.borderCollapse == 177) {
                     var7 += (var6 - 1) * this.columnSpacing;
                  }
               } else {
                  var7 = this.columnWidths[var5];
               }

               var7 -= var4.marginLeft;
               var7 -= var4.marginRight;
               var4.layout(var7);
            }
         }
      }

   }

   private void expandColumnSpans() {
      for(int var3 = 0; var3 < this.rows.length; ++var3) {
         int var4 = 0;

         for(int var5 = this.rows[var3].size(); var4 < var5; ++var4) {
            TableCell var1 = this.rows[var3].get(var4);
            if (var1.columnSpan != 1) {
               int var6 = 1;

               for(int var7 = var1.columnSpan; var6 < var7; ++var6) {
                  TableCell var2 = var1.copy();
                  var2.columnNumber = var1.columnNumber + var6;
                  var2.columnSpan = 1;
                  var2.hMerge = "continue";
                  var2.borders.left.style = 75;
                  var2.borders.left.width = 0;
                  var2.marginLeft = 0;
                  if (var6 < var7 - 1) {
                     var2.borders.right.style = 75;
                     var2.borders.right.width = 0;
                     var2.marginRight = 0;
                  }

                  this.rows[var3].add(var2);
               }

               var1.columnSpan = 1;
               var1.hMerge = "restart";
               var1.borders.right.style = 75;
               var1.borders.right.width = 0;
               var1.marginRight = 0;
            }
         }
      }

   }

   private void setSeparateBorders() {
      for(int var1 = 0; var1 < this.rows.length; ++var1) {
         this.rows[var1].borders.left = this.borders.left;
         this.rows[var1].borders.right = this.borders.right;
         this.rows[var1].borders.top.style = 75;
         this.rows[var1].borders.bottom.style = 75;
      }

      this.rows[0].borders.top = this.borders.top;
      this.rows[this.rows.length - 1].borders.bottom = this.borders.bottom;
   }

   private void setCollapsedBorders() {
      Vector var1 = new Vector();
      Border var2 = null;
      TableColumn[] var3 = this.columns();
      int var5 = 0;

      for(int var6 = this.groups.size(); var5 < var6; ++var5) {
         TableGroup var7 = (TableGroup)this.groups.elementAt(var5);
         int var8 = 0;

         for(int var9 = var7.size(); var8 < var9; ++var8) {
            TableRow var10 = var7.get(var8);
            int var11 = 0;

            for(int var12 = var10.size(); var11 < var12; ++var11) {
               TableCell var13 = var10.get(var11);
               int var14;
               int var15;
               Border var16;
               if (var13.borders.top.style != 75) {
                  var2 = null;
                  var1.setSize(0);
                  var1.addElement(var10.borders.top);
                  if (var8 == 0) {
                     var1.addElement(var7.borders.top);
                     if (var5 == 0) {
                        var1.addElement(this.borders.top);
                     }
                  }

                  var14 = 0;

                  for(var15 = var1.size(); var14 < var15; ++var14) {
                     var16 = (Border)var1.elementAt(var14);
                     if (var16.style != 125) {
                        var2 = var16;
                        if (var16.style == 75) {
                           break;
                        }
                     }
                  }

                  if (var2 != null) {
                     var13.borders.top.style = var2.style;
                     var13.borders.top.width = var2.width;
                     var13.borders.top.color = var2.color;
                  }
               }

               if (var13.borders.bottom.style != 75) {
                  var2 = null;
                  var1.setSize(0);
                  var1.addElement(var10.borders.bottom);
                  if (var8 == var9 - 1) {
                     var1.addElement(var7.borders.bottom);
                     if (var5 == var6 - 1) {
                        var1.addElement(this.borders.bottom);
                     }
                  }

                  var14 = 0;

                  for(var15 = var1.size(); var14 < var15; ++var14) {
                     var16 = (Border)var1.elementAt(var14);
                     if (var16.style != 125) {
                        var2 = var16;
                        if (var16.style == 75) {
                           break;
                        }
                     }
                  }

                  if (var2 != null) {
                     var13.borders.bottom.style = var2.style;
                     var13.borders.bottom.width = var2.width;
                     var13.borders.bottom.color = var2.color;
                  }
               }

               TableColumn var4;
               if (var13.borders.left.style != 75) {
                  var2 = null;
                  var1.setSize(0);
                  var4 = var3[var13.columnNumber];
                  if (var4 != null && var4.number == var13.columnNumber) {
                     var1.addElement(var4.borders.left);
                  }

                  if (var13.columnNumber == 1) {
                     var1.addElement(this.borders.left);
                  }

                  var14 = 0;

                  for(var15 = var1.size(); var14 < var15; ++var14) {
                     var16 = (Border)var1.elementAt(var14);
                     if (var16.style != 125) {
                        var2 = var16;
                        if (var16.style == 75) {
                           break;
                        }
                     }
                  }

                  if (var2 != null) {
                     var13.borders.left.style = var2.style;
                     var13.borders.left.width = var2.width;
                     var13.borders.left.color = var2.color;
                  }
               }

               if (var13.borders.right.style != 75) {
                  var2 = null;
                  var1.setSize(0);
                  var14 = var13.columnNumber + var13.columnSpan - 1;
                  var4 = var3[var14];
                  if (var4 != null) {
                     var15 = var4.number + var4.repeat * var4.span - 1;
                     if (var15 == var14) {
                        var1.addElement(var4.borders.right);
                     }
                  }

                  if (var14 == this.numColumns) {
                     var1.addElement(this.borders.right);
                  }

                  var15 = 0;

                  for(int var18 = var1.size(); var15 < var18; ++var15) {
                     Border var17 = (Border)var1.elementAt(var15);
                     if (var17.style != 125) {
                        var2 = var17;
                        if (var17.style == 75) {
                           break;
                        }
                     }
                  }

                  if (var2 != null) {
                     var13.borders.right.style = var2.style;
                     var13.borders.right.width = var2.width;
                     var13.borders.right.color = var2.color;
                  }
               }
            }

            var10.borders = null;
         }
      }

   }

   private void setBorderWidths() {
      this.borderWidths = new int[this.numColumns][2];

      for(int var1 = 0; var1 < this.rows.length; ++var1) {
         int var2 = 0;

         for(int var3 = this.rows[var1].size(); var2 < var3; ++var2) {
            TableCell var4 = this.rows[var1].get(var2);
            int var5 = var4.columnNumber - 1;
            if (var4.borders.left.width > this.borderWidths[var5][0]) {
               this.borderWidths[var5][0] = var4.borders.left.width;
            }

            var5 += var4.columnSpan - 1;
            if (var4.borders.right.width > this.borderWidths[var5][1]) {
               this.borderWidths[var5][1] = var4.borders.right.width;
            }
         }
      }

   }

   private void setTableLayout() {
      int var1 = this.width > 0 ? this.width : this.maxWidth;
      TableColumn[] var2 = this.columns();

      int var3;
      for(var3 = 1; var3 <= this.numColumns; ++var3) {
         if (var2[var3] != null) {
            var2[var3].setReference(var1);
         }
      }

      if (this.tableLayout == 69) {
         for(var3 = 1; var3 <= this.numColumns; ++var3) {
            if (var2[var3] == null) {
               this.tableLayout = 10;
               break;
            }

            if (var2[var3].width == 0 && var2[var3].proportion == 0.0) {
               this.tableLayout = 10;
               break;
            }
         }
      } else {
         boolean var5 = true;

         for(int var4 = 1; var4 <= this.numColumns; ++var4) {
            if (var2[var4] == null) {
               var5 = false;
               break;
            }

            if (var2[var4].width == 0 && var2[var4].proportion == 0.0) {
               var5 = false;
               break;
            }
         }

         if (var5) {
            this.tableLayout = 69;
         }
      }

   }

   private int[] fixedLayout() {
      int[] var1 = new int[this.numColumns];
      TableColumn[] var2 = this.columns();
      int var3 = this.width > 0 ? this.width : this.maxWidth;
      double var4 = 0.0;
      if (this.borderCollapse == 177) {
         var3 -= (this.numColumns + 1) * this.columnSpacing;
      }

      int var6;
      for(var6 = 0; var6 < this.numColumns; ++var6) {
         if (var2[var6 + 1].proportion > 0.0) {
            var4 += var2[var6 + 1].proportion;
            var1[var6] = 0;
         } else {
            var1[var6] = var2[var6 + 1].width;
            var3 -= var1[var6];
         }
      }

      if (var4 > 0.0) {
         for(var6 = 0; var6 < this.numColumns; ++var6) {
            if (var1[var6] == 0) {
               double var7 = (double)var3 * var2[var6 + 1].proportion / var4;
               var1[var6] = (int)Math.round(var7);
            }
         }
      }

      return var1;
   }

   private int[] automaticLayout() {
      int[] var2 = new int[this.numColumns];
      int[] var3 = new int[this.numColumns];
      boolean var4 = false;
      TableColumn[] var5 = this.columns();

      int var6;
      for(var6 = 0; var6 < this.numColumns; ++var6) {
         TableColumn var7 = var5[var6 + 1];
         if (var7 != null && var7.width > 0) {
            var2[var6] = var7.width / var7.span;
            var3[var6] = var2[var6];
         }
      }

      var6 = this.width > 0 ? this.width : this.maxWidth;

      int var8;
      int var9;
      TableCell var10;
      int var11;
      int var12;
      int var13;
      int var17;
      for(var17 = 0; var17 < this.rows.length; ++var17) {
         var8 = 0;

         for(var9 = this.rows[var17].size(); var8 < var9; ++var8) {
            var10 = this.rows[var17].get(var8);
            if (var10.columnSpan == 1) {
               var10.setReference(var6);
               var11 = var10.columnNumber - 1;
               var12 = var10.minWidth();
               if (var12 > var2[var11]) {
                  var2[var11] = var12;
               }

               var13 = var10.maxWidth();
               if (var13 > var3[var11]) {
                  var3[var11] = var13;
               }
            }
         }
      }

      for(var17 = 0; var17 < this.rows.length; ++var17) {
         var8 = 0;

         for(var9 = this.rows[var17].size(); var8 < var9; ++var8) {
            var10 = this.rows[var17].get(var8);
            if (var10.columnSpan >= 2) {
               var10.setReference(var6);
               var11 = var10.columnNumber - 1;
               var12 = 0;

               for(var13 = 0; var13 < var10.columnSpan; ++var13) {
                  var12 += var2[var11 + var13];
               }

               var13 = var10.minWidth();
               int var14;
               int var15;
               if (var13 > var12) {
                  var14 = (var13 - var12) / var10.columnSpan;
                  if (var14 == 0) {
                     var14 = 1;
                  }

                  for(var15 = 0; var15 < var10.columnSpan; ++var15) {
                     var2[var11 + var15] += var14;
                  }
               }

               var12 = 0;

               for(var14 = 0; var14 < var10.columnSpan; ++var14) {
                  var12 += var3[var11 + var14];
               }

               var13 = var10.maxWidth();
               if (var13 > var12) {
                  var14 = (var13 - var12) / var10.columnSpan;
                  if (var14 == 0) {
                     var14 = 1;
                  }

                  for(var15 = 0; var15 < var10.columnSpan; ++var15) {
                     var3[var11 + var15] += var14;
                  }
               }
            }
         }
      }

      int var16;
      if (this.borderCollapse == 177) {
         var16 = (this.numColumns + 1) * this.columnSpacing;

         for(var17 = 0; var17 < this.numColumns; ++var17) {
            var16 += this.borderWidths[var17][0];
            var16 += this.borderWidths[var17][1];
         }

         var16 += this.borders.left.width;
         var16 += this.borders.right.width;
      } else {
         var16 = this.borderWidths[0][0];
         var16 += this.borderWidths[this.numColumns - 1][1];

         for(var17 = 1; var17 < this.numColumns; ++var17) {
            var8 = this.borderWidths[var17 - 1][1];
            var9 = this.borderWidths[var17][0];
            var16 += Math.max(var8, var9);
         }
      }

      int[] var1;
      if (this.width != 0) {
         var17 = var16;

         for(var8 = 0; var8 < this.numColumns; ++var8) {
            var17 += var2[var8];
         }

         if (this.width > var17) {
            var8 = this.width - var17;
            var1 = this.distribute(var8, var2, var3);
         } else if (this.width < var17) {
            var8 = var17 - this.width;
            var1 = this.shrink(var8, var2);
         } else {
            var1 = var2;
         }
      } else {
         var17 = this.maxWidth;
         var8 = var16;

         for(var9 = 0; var9 < this.numColumns; ++var9) {
            var8 += var3[var9];
         }

         if (var8 > var17) {
            var9 = var16;

            int var18;
            for(var18 = 0; var18 < this.numColumns; ++var18) {
               var9 += var2[var18];
            }

            if (var17 > var9) {
               var18 = var17 - var9;
               var1 = this.distribute(var18, var2, var3);
            } else if (var17 < var9) {
               var18 = var9 - var17;
               var1 = this.shrink(var18, var2);
            } else {
               var1 = var2;
            }
         } else {
            var1 = var3;
         }
      }

      for(var17 = 0; var17 < this.numColumns; ++var17) {
         var1[var17] += this.borderWidths[var17][0] / 2;
         var1[var17] += this.borderWidths[var17][1] / 2;
      }

      return var1;
   }

   private int[] distribute(int var1, int[] var2, int[] var3) {
      int[] var7 = new int[this.numColumns];
      int var4 = 0;

      int var5;
      for(var5 = 0; var4 < this.numColumns; ++var4) {
         var7[var4] = var2[var4];
         if (var7[var4] < var3[var4]) {
            ++var5;
         }
      }

      while(true) {
         while(var1 > 0) {
            int var6;
            if (var5 == 0) {
               var6 = var1 / this.numColumns;

               for(var4 = 0; var4 < this.numColumns; ++var4) {
                  var7[var4] += var6;
               }

               var1 = 0;
            } else {
               var6 = var1 / var5;
               var4 = 0;
               var5 = 0;

               for(var1 = 0; var4 < this.numColumns; ++var4) {
                  if (var7[var4] < var3[var4]) {
                     var7[var4] += var6;
                  }

                  if (var7[var4] > var3[var4]) {
                     var1 += var7[var4] - var3[var4];
                     var7[var4] = var3[var4];
                  } else if (var7[var4] < var3[var4]) {
                     ++var5;
                  }
               }
            }
         }

         return var7;
      }
   }

   private int[] shrink(int var1, int[] var2) {
      int var5 = var2[0];
      int var6 = var2[0];
      int var7 = 0;
      int[] var8 = new int[this.numColumns];

      int var3;
      for(var3 = 1; var3 < this.numColumns; ++var3) {
         if (var2[var3] < var5) {
            var5 = var2[var3];
         } else if (var2[var3] > var6) {
            var6 = var2[var3];
         }
      }

      int var4 = (var5 + var6) / 2;

      for(var3 = 0; var3 < this.numColumns; ++var3) {
         if (var2[var3] >= var4) {
            var7 += var2[var3];
         }
      }

      for(var3 = 0; var3 < this.numColumns; ++var3) {
         if (var2[var3] >= var4) {
            var8[var3] = var2[var3] - var1 * var2[var3] / var7;
            if (var8[var3] <= 0) {
               var8[var3] = var2[var3];
            }
         } else {
            var8[var3] = var2[var3];
         }
      }

      return var8;
   }

   private void setCells() {
      TableColumn[] var1 = this.columns();
      int var2 = 0;

      for(int var3 = this.groups.size(); var2 < var3; ++var2) {
         TableGroup var4 = (TableGroup)this.groups.elementAt(var2);
         int var5 = 0;

         for(int var6 = var4.size(); var5 < var6; ++var5) {
            TableRow var7 = var4.get(var5);
            TableCell[] var8 = new TableCell[this.numColumns];
            int var9 = 0;

            for(int var10 = var7.size(); var9 < var10; ++var9) {
               TableCell var11 = var7.get(var9);
               var8[var11.columnNumber - 1] = var11;
            }

            var7.cells.setSize(0);

            for(var9 = 0; var9 < this.numColumns; ++var9) {
               if (var8[var9] == null) {
                  var8[var9] = new TableCell();
                  var8[var9].borders = new Borders();
               } else if (var8[var9].background == null) {
                  if (var7.background != null) {
                     var8[var9].background = var7.background;
                  } else if (var4.background != null) {
                     var8[var9].background = var4.background;
                  } else if (var1[var9 + 1] != null && var1[var9 + 1].background != null) {
                     var8[var9].background = var1[var9 + 1].background;
                  } else {
                     var8[var9].background = this.background;
                  }
               }

               var8[var9].width = 0;
               var7.cells.addElement(var8[var9]);
            }
         }
      }

   }

   private void setRows() {
      int var3;
      int var4;
      for(var3 = 0; var3 < this.rows.length; ++var3) {
         this.rows[var3].background = null;
         if (this.rows[var3].height > 0) {
            var4 = 0;
            int var5 = 0;

            for(int var6 = this.rows[var3].size(); var5 < var6; ++var5) {
               TableCell var7 = this.rows[var3].get(var5);
               int var8 = var7.marginTop + var7.marginBottom;
               if (var8 > var4) {
                  var4 = var8;
               }
            }

            if (var4 < this.rows[var3].height) {
               TableRow var10000 = this.rows[var3];
               var10000.height -= var4;
            }
         }
      }

      if (this.keepTogether) {
         var3 = 0;

         for(var4 = this.rows.length - 1; var3 < var4; ++var3) {
            this.rows[var3].keepTogether = true;
            this.rows[var3].keepWithNext = true;
         }

         this.rows[this.rows.length - 1].keepTogether = true;
      }

      TableRow var1 = this.rows[0];
      if (this.breakBefore > var1.breakBefore) {
         var1.breakBefore = this.breakBefore;
      }

      for(var3 = 1; var3 < this.rows.length; ++var3) {
         TableRow var2 = this.rows[var3];
         if (var1.breakAfter > var2.breakBefore) {
            var2.breakBefore = var1.breakAfter;
         }

         if (var2.keepWithPrevious) {
            var1.keepWithNext = true;
         }

         var1 = var2;
      }

      for(var3 = 0; var3 < this.rows.length; ++var3) {
         if (this.rows[var3].breakBefore != 0) {
            this.rows[var3].get(0).breakBefore(this.rows[var3].breakBefore);
         }

         if (this.rows[var3].keepWithNext) {
            this.rows[var3].get(0).keepWithNext();
         }
      }

   }

   private void setTableIndent() {
      this.tableIndent = this.startIndent;
      if (!this.isNested) {
         if (this.borderCollapse == 177) {
            this.tableIndent += this.borders.left.width / 2;
            this.tableIndent += this.columnSpacing;
            this.tableIndent += this.borderWidths[0][0];
         } else {
            this.tableIndent += this.borderWidths[0][0] / 2;
         }

         this.tableIndent += this.rows[0].get(0).marginLeft;
      }

      switch (this.alignment) {
         case 1:
            this.tableIndent += (this.maxWidth - this.width()) / 2;
            break;
         case 2:
            this.tableIndent += this.maxWidth - this.width();
      }

   }

   private TableColumn column(int var1) {
      int var2 = 0;

      for(int var3 = this.columns.size(); var2 < var3; ++var2) {
         TableColumn var4 = (TableColumn)this.columns.elementAt(var2);
         int var5 = var4.number;
         int var6 = var5 + var4.repeat * var4.span - 1;
         if (var5 <= var1 && var6 >= var1) {
            return var4;
         }
      }

      return null;
   }

   private TableColumn[] columns() {
      TableColumn[] var1 = new TableColumn[this.numColumns + 1];
      int var2 = 0;

      for(int var3 = this.columns.size(); var2 < var3; ++var2) {
         TableColumn var4 = (TableColumn)this.columns.elementAt(var2);
         int var5 = var4.number + var4.repeat * var4.span - 1;

         for(int var6 = var4.number; var6 <= var5; ++var6) {
            var1[var6] = var4;
         }
      }

      return var1;
   }

   private TableRow[] rows() {
      int var1 = 0;
      TableRow[] var2 = null;
      int var3 = 0;

      int var4;
      for(var4 = this.groups.size(); var3 < var4; ++var3) {
         TableGroup var5 = (TableGroup)this.groups.elementAt(var3);
         var1 += var5.rows.size();
      }

      if (var1 > 0) {
         var2 = new TableRow[var1];
         var3 = 0;
         var4 = 0;

         for(int var9 = this.groups.size(); var3 < var9; ++var3) {
            TableGroup var6 = (TableGroup)this.groups.elementAt(var3);
            int var7 = 0;

            for(int var8 = var6.size(); var7 < var8; ++var4) {
               var2[var4] = var6.get(var7);
               ++var7;
            }
         }
      }

      return var2;
   }

   private int width() {
      int var1 = 0;

      int var2;
      for(var2 = 0; var2 < this.numColumns; ++var2) {
         var1 += this.columnWidths[var2];
      }

      if (this.borderCollapse == 177) {
         var1 += (this.numColumns + 1) * this.columnSpacing;

         for(var2 = 0; var2 < this.numColumns; ++var2) {
            var1 += this.borderWidths[var2][0];
            var1 += this.borderWidths[var2][1];
         }

         var1 += this.borders.left.width;
         var1 += this.borders.right.width;
      } else {
         var1 += this.borderWidths[0][0];
         var1 += this.borderWidths[this.numColumns - 1][1];

         for(var2 = 1; var2 < this.numColumns; ++var2) {
            int var3 = this.borderWidths[var2 - 1][1];
            int var4 = this.borderWidths[var2][0];
            var1 += Math.max(var3, var4);
         }
      }

      return var1;
   }

   public int maxWidth() {
      int var3 = 0;
      if (this.width > 0) {
         return this.width + this.startIndent + this.endIndent;
      } else {
         if (this.rows == null) {
            this.rows = this.expandRowSpans();
            if (this.rows == null) {
               return 0;
            }

            this.layoutBorders();
         }

         int var1;
         if (this.tableLayout == 69) {
            TableColumn[] var4 = this.columns();
            var1 = 1;

            for(var3 = 0; var1 <= this.numColumns; ++var1) {
               if (var4[var1] == null || var4[var1].width == 0) {
                  var3 = 0;
                  break;
               }

               var3 += var4[var1].width / var4[var1].span;
            }
         }

         int var5;
         if (var3 == 0) {
            int[] var12 = new int[this.numColumns];
            var5 = 0;

            for(int var6 = this.rows.length; var5 < var6; ++var5) {
               int var7 = 0;

               for(int var8 = this.rows[var5].size(); var7 < var8; ++var7) {
                  TableCell var9 = this.rows[var5].get(var7);
                  var3 = var9.maxWidth();
                  int var10 = var9.columnNumber - 1;
                  int var11 = var9.columnSpan;
                  if (var11 > 1) {
                     var3 /= var11;

                     for(var1 = 0; var1 < var11; ++var1) {
                        int var2 = var10 + var1;
                        if (var3 > var12[var2]) {
                           var12[var2] = var3;
                        }
                     }
                  } else if (var3 > var12[var10]) {
                     var12[var10] = var3;
                  }
               }
            }

            var1 = 0;

            for(var3 = 0; var1 < this.numColumns; ++var1) {
               var3 += var12[var1];
            }
         }

         if (this.borderCollapse == 177) {
            var3 += (this.numColumns + 1) * this.columnSpacing;

            for(var1 = 0; var1 < this.numColumns; ++var1) {
               var3 += this.borderWidths[var1][0];
               var3 += this.borderWidths[var1][1];
            }

            var3 += this.borders.left.width;
            var3 += this.borders.right.width;
         } else {
            var3 += this.borderWidths[0][0];
            var3 += this.borderWidths[this.numColumns - 1][1];

            for(var1 = 1; var1 < this.numColumns; ++var1) {
               int var13 = this.borderWidths[var1 - 1][1];
               var5 = this.borderWidths[var1][0];
               var3 += Math.max(var13, var5);
            }
         }

         var3 += this.startIndent;
         var3 += this.endIndent;
         return var3;
      }
   }

   public int minWidth() {
      int var3 = 0;
      if (this.width > 0) {
         return this.width + this.startIndent + this.endIndent;
      } else {
         if (this.rows == null) {
            this.rows = this.expandRowSpans();
            if (this.rows == null) {
               return 0;
            }

            this.layoutBorders();
         }

         int var1;
         if (this.tableLayout == 69) {
            TableColumn[] var4 = this.columns();
            var1 = 1;

            for(var3 = 0; var1 <= this.numColumns; ++var1) {
               if (var4[var1] == null || var4[var1].width == 0) {
                  var3 = 0;
                  break;
               }

               var3 += var4[var1].width / var4[var1].span;
            }
         }

         int var5;
         if (var3 == 0) {
            int[] var12 = new int[this.numColumns];
            var5 = 0;

            for(int var6 = this.rows.length; var5 < var6; ++var5) {
               int var7 = 0;

               for(int var8 = this.rows[var5].size(); var7 < var8; ++var7) {
                  TableCell var9 = this.rows[var5].get(var7);
                  var3 = var9.minWidth();
                  int var10 = var9.columnNumber - 1;
                  int var11 = var9.columnSpan;
                  if (var11 > 1) {
                     var3 /= var11;

                     for(var1 = 0; var1 < var11; ++var1) {
                        int var2 = var10 + var1;
                        if (var3 > var12[var2]) {
                           var12[var2] = var3;
                        }
                     }
                  } else if (var3 > var12[var10]) {
                     var12[var10] = var3;
                  }
               }
            }

            var1 = 0;

            for(var3 = 0; var1 < this.numColumns; ++var1) {
               var3 += var12[var1];
            }
         }

         if (this.borderCollapse == 177) {
            var3 += (this.numColumns + 1) * this.columnSpacing;

            for(var1 = 0; var1 < this.numColumns; ++var1) {
               var3 += this.borderWidths[var1][0];
               var3 += this.borderWidths[var1][1];
            }

            var3 += this.borders.left.width;
            var3 += this.borders.right.width;
         } else {
            var3 += this.borderWidths[0][0];
            var3 += this.borderWidths[this.numColumns - 1][1];

            for(var1 = 1; var1 < this.numColumns; ++var1) {
               int var13 = this.borderWidths[var1 - 1][1];
               var5 = this.borderWidths[var1][0];
               var3 += Math.max(var13, var5);
            }
         }

         var3 += this.startIndent;
         var3 += this.endIndent;
         return var3;
      }
   }
}

package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

public class Table {
   private static final int ALIGNMENT_LEFT = 0;
   private static final int ALIGNMENT_CENTER = 1;
   private static final int ALIGNMENT_RIGHT = 2;
   private int breakBefore;
   private double spaceBefore;
   private boolean keepTogether;
   private int tableLayout;
   private int borderCollapse;
   private double rowSpacing;
   private double columnSpacing;
   private double width;
   private boolean relativeWidth;
   private double startIndent;
   private double endIndent;
   private int alignment;
   private Borders borders;
   private Color background;
   private int numColumns;
   private Vector columnList = new Vector();
   private Hashtable columnHash = new Hashtable();
   private TableColumn[] columns;
   private TableGroup header;
   private TableGroup footer;
   private Vector bodies = new Vector();
   private TableGroup[] groups;
   private TableRow[] rows;
   private TableGroup group;
   private TableRow row;
   private TableCell cell;
   private double tableWidth;
   private double maxTableWidth;
   private double[] columnWidths;
   private double tableIndent;
   private boolean isNestedTable;

   public Table(Context var1) {
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      this.breakBefore = var1.breakBefore;
      this.spaceBefore = var1.spaceBefore();
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
            this.width = var5.length();
            break;
         case 13:
            this.width = var5.percentage();
            this.relativeWidth = true;
      }

      this.startIndent = length(var2[277]);
      this.endIndent = length(var2[97]);
      this.borders = new Borders(var2);
      this.borders.top.space = 0.0;
      this.borders.bottom.space = 0.0;
      this.borders.left.space = 0.0;
      this.borders.right.space = 0.0;
      this.background = var1.background;
      Context var6 = var1.parent();
      if (var6.fo == 46) {
         var2 = var6.properties.values;
         switch (var2[289].keyword()) {
            case 31:
            case 93:
               this.alignment = 1;
               break;
            case 52:
            case 165:
               this.alignment = 2;
               break;
            case 100:
            case 190:
            default:
               this.alignment = 0;
         }
      }

   }

   private static double length(Value var0) {
      double var1 = 0.0;
      if (var0.type == 4) {
         var1 = var0.length();
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

   public void add(TableColumn var1) {
      int var2;
      if (var1.repeat > 1) {
         var2 = var1.number;

         for(int var3 = 0; var3 < var1.repeat; ++var3) {
            TableColumn var4 = var1.copy();
            var4.number = var2;
            var4.repeat = 1;
            this.columnList.addElement(var4);
            this.columnHash.put(new Integer(var4.number), var4);
            var2 += var1.span;
         }
      } else {
         this.columnList.addElement(var1);
         this.columnHash.put(new Integer(var1.number), var1);
      }

      var2 = var1.last();
      if (var2 > this.numColumns) {
         this.numColumns = var2;
      }

   }

   public void startHeader(Context var1) {
      this.startGroup(0, var1);
      this.header = this.group;
   }

   public void endHeader() {
      this.endGroup();
   }

   public void startFooter(Context var1) {
      this.startGroup(2, var1);
      this.footer = this.group;
   }

   public void endFooter() {
      this.endGroup();
   }

   public void startBody(Context var1) {
      this.startGroup(1, var1);
      this.bodies.addElement(this.group);
   }

   public void endBody() {
      this.endGroup();
   }

   private void startGroup(int var1, Context var2) {
      this.group = new TableGroup(var1, var2);
      this.row = null;
   }

   private void endGroup() {
      this.endRow();
      this.group = null;
   }

   public void startRow(Context var1, Bookmark[] var2) {
      this.row = new TableRow(var1);
      this.row.bookmarks = var2;
      if (this.group != null) {
         this.group.add(this.row);
      }

   }

   public void endRow() {
      this.row = null;
   }

   public double startCell(Context var1) {
      double var2 = 0.0;
      this.cell = new TableCell(var1);
      int var4 = this.cell.colNumber + this.cell.colSpan - 1;
      if (var4 > this.numColumns) {
         this.numColumns = var4;
      }

      if (this.cell.startsRow) {
         this.endRow();
      }

      if (this.row == null) {
         this.row = new TableRow();
         if (this.group != null) {
            this.group.add(this.row);
         }
      }

      this.row.add(this.cell);
      if (this.cell.width > 0.0 && !this.cell.relativeWidth) {
         var2 = this.cell.width;
      } else {
         TableColumn var5 = (TableColumn)this.columnHash.get(new Integer(this.cell.colNumber));
         if (var5 != null && var5.width > 0.0 && var5.widthType == 0) {
            var2 = var5.width;
         }
      }

      if (var2 > 0.0) {
         var2 -= this.cell.marginLeft + this.cell.marginRight;
      }

      return var2;
   }

   public void endCell() {
      if (this.cell.endsRow) {
         this.endRow();
      }

      this.cell = null;
   }

   public void add(Paragraph var1) {
      if (this.cell != null) {
         this.cell.add(var1);
         if (var1.requiresLayout()) {
            this.cell.requiresLayout = true;
            this.row.requiresLayout = true;
         }
      }

   }

   public void add(Table var1) {
      if (this.cell != null) {
         var1.isNestedTable = true;
         this.cell.add(var1);
         this.cell.requiresLayout = true;
         this.row.requiresLayout = true;
      }

   }

   public void add(TableAndCaption var1) {
      if (this.cell != null) {
         if (var1.table != null) {
            var1.table.isNestedTable = true;
         }

         this.cell.add(var1);
         this.cell.requiresLayout = true;
         this.row.requiresLayout = true;
      }

   }

   public void add(List var1) {
      if (this.cell != null) {
         this.cell.add(var1);
         this.cell.requiresLayout = true;
         this.row.requiresLayout = true;
      }

   }

   public void layout(double var1, NumberingDefinitions var3) throws Exception {
      if (this.columns == null) {
         this.normalize();
      }

      this.maxTableWidth = var1 - (this.startIndent + this.endIndent);
      if (this.relativeWidth) {
         double var8 = this.width;
         this.width = this.maxTableWidth * var8 / 100.0;
         this.relativeWidth = false;
      }

      this.tableWidth = this.width > 0.0 ? this.width : this.maxTableWidth;

      int var4;
      double var9;
      for(var4 = 0; var4 < this.numColumns; ++var4) {
         TableColumn var14 = this.columns[var4];
         if (var14 != null && var14.widthType == 1) {
            var9 = var14.width;
            var14.width = this.tableWidth * var9 / 100.0;
            var14.widthType = 0;
         }
      }

      int var6;
      int var7;
      double var10;
      TableRow var15;
      TableCell var17;
      for(var6 = 0; var6 < this.rows.length; ++var6) {
         var15 = this.rows[var6];

         for(var7 = 0; var7 < var15.cells.length; ++var7) {
            var17 = var15.cells[var7];
            if (var17.relativeWidth) {
               var10 = var17.width;
               var17.width = this.tableWidth * var10 / 100.0;
               var17.relativeWidth = false;
            }
         }
      }

      if (this.tableLayout == 69) {
         for(var4 = 0; var4 < this.columns.length; ++var4) {
            if (this.columns[var4] == null || this.columns[var4].width == 0.0) {
               this.tableLayout = 10;
               break;
            }
         }
      } else {
         boolean var16 = true;

         for(var4 = 0; var4 < this.columns.length; ++var4) {
            if (this.columns[var4] == null || this.columns[var4].width == 0.0) {
               var16 = false;
               break;
            }
         }

         if (var16) {
            this.tableLayout = 69;
         }
      }

      if (this.tableLayout == 69) {
         this.columnWidths = this.fixedLayout();
      } else {
         this.columnWidths = this.automaticLayout();
      }

      var4 = 0;

      for(this.tableWidth = 0.0; var4 < this.numColumns; ++var4) {
         this.tableWidth += this.columnWidths[var4];
      }

      if (this.borderCollapse == 177) {
         this.tableWidth += (double)(this.numColumns + 1) * this.columnSpacing;
         this.tableWidth += this.borders.left.width + this.borders.right.width;
      }

      for(var6 = 0; var6 < this.rows.length; ++var6) {
         var15 = this.rows[var6];
         if (var15.requiresLayout) {
            for(var7 = 0; var7 < var15.cells.length; ++var7) {
               var17 = var15.cells[var7];
               if (var17.requiresLayout) {
                  var10 = 0.0;
                  int var12 = var17.colSpan;
                  if (var12 > 1) {
                     for(var4 = 0; var4 < var12; ++var4) {
                        var10 += this.columnWidths[var7 + var4];
                     }

                     var10 += (double)(var12 - 1) * this.columnSpacing;
                  } else {
                     var10 = this.columnWidths[var7];
                  }

                  var10 -= this.leftBorderWidth(var15, var17);
                  var10 -= this.rightBorderWidth(var15, var17);
                  var17.layout(var10, var3);
               }
            }
         }
      }

      for(var6 = 0; var6 < this.rows.length; ++var6) {
         var15 = this.rows[var6];

         for(var7 = 0; var7 < var15.cells.length; ++var7) {
            var17 = var15.cells[var7];
            if (var17.background == null) {
               if (var17.isInvisible) {
                  if (this.borderCollapse == 177) {
                     var17.background = this.background;
                  }
               } else if (var15.background != null) {
                  var17.background = var15.background;
               } else {
                  TableColumn var20 = this.columns[var7];
                  if (var20 != null && var20.background != null && var20.number == var17.colNumber && var20.span == var17.colSpan) {
                     var17.background = var20.background;
                  } else {
                     var17.background = this.background;
                  }
               }
            }
         }

         var15.background = null;
      }

      for(var6 = 0; var6 < this.rows.length; ++var6) {
         var15 = this.rows[var6];
         if (var15.height > 0.0) {
            var9 = 0.0;

            for(var7 = 0; var7 < var15.cells.length; ++var7) {
               TableCell var11 = var15.cells[var7];
               double var19 = var11.marginTop + var11.marginBottom;
               if (var19 > var9) {
                  var9 = var19;
               }
            }

            if (var9 < var15.height) {
               var15.height -= var9;
            }
         }
      }

      if (this.keepTogether) {
         var4 = 0;

         for(int var5 = this.rows.length - 1; var4 < var5; ++var4) {
            this.rows[var4].keepTogether = true;
            this.rows[var4].keepWithNext = true;
         }

         this.rows[this.rows.length - 1].keepTogether = true;
      }

      var15 = this.rows[0];
      if (this.breakBefore > var15.breakBefore) {
         var15.breakBefore = this.breakBefore;
      }

      TableRow var18;
      for(var4 = 1; var4 < this.rows.length; ++var4) {
         var18 = this.rows[var4];
         if (var15.breakAfter > var18.breakBefore) {
            var18.breakBefore = var15.breakAfter;
         }

         if (var18.keepWithPrevious) {
            var15.keepWithNext = true;
         }

         var15 = var18;
      }

      for(var4 = 0; var4 < this.rows.length; ++var4) {
         var18 = this.rows[var4];
         if (var18.breakBefore != 0) {
            var18.cells[0].breakBefore(var18.breakBefore);
         }

         if (var18.keepWithNext) {
            var18.cells[0].keepWithNext();
         }
      }

      this.tableIndent = this.startIndent;
      if (!this.isNestedTable) {
         var17 = this.groups[0].rows[0].cells[0];
         if (this.borderCollapse == 177) {
            this.tableIndent += this.borders.left.width / 2.0;
            this.tableIndent += this.columnSpacing;
            this.tableIndent += var17.borders.left.width;
         } else {
            this.tableIndent += var17.borders.left.width / 2.0;
         }

         this.tableIndent += var17.marginLeft;
      }

      switch (this.alignment) {
         case 1:
            this.tableIndent += (this.maxTableWidth - this.tableWidth) / 2.0;
            break;
         case 2:
            this.tableIndent += this.maxTableWidth - this.tableWidth;
      }

   }

   private void normalize() {
      this.columns = new TableColumn[this.numColumns];
      int var1 = 0;

      int var2;
      int var4;
      for(var4 = this.columnList.size(); var1 < var4; ++var1) {
         TableColumn var7 = (TableColumn)this.columnList.elementAt(var1);

         for(var2 = 0; var2 < var7.span; ++var2) {
            this.columns[var7.number + var2 - 1] = var7;
         }
      }

      int var5 = this.bodies.size();
      if (this.header != null) {
         ++var5;
      }

      if (this.footer != null) {
         ++var5;
      }

      this.groups = new TableGroup[var5];
      if (this.header != null) {
         this.groups[0] = this.header;
      }

      var1 = 0;
      var2 = this.header == null ? 0 : 1;

      for(var4 = this.bodies.size(); var1 < var4; ++var2) {
         this.groups[var2] = (TableGroup)this.bodies.elementAt(var1);
         ++var1;
      }

      if (this.footer != null) {
         this.groups[var5 - 1] = this.footer;
      }

      for(var1 = 0; var1 < this.groups.length; ++var1) {
         this.groups[var1].normalize(this.numColumns);
      }

      var1 = 0;

      int var6;
      for(var6 = 0; var1 < this.groups.length; ++var1) {
         var6 += this.groups[var1].rows.length;
      }

      this.rows = new TableRow[var6];
      var1 = 0;

      for(int var3 = 0; var1 < this.groups.length; ++var1) {
         TableGroup var8 = this.groups[var1];

         for(var2 = 0; var2 < var8.rows.length; ++var3) {
            this.rows[var3] = var8.rows[var2];
            ++var2;
         }
      }

      if (this.borderCollapse == 37) {
         this.setCollapsedBorders();
      } else {
         this.setSeparateBorders();
      }

   }

   public void setCollapsedBorders() {
      Vector var1 = new Vector();
      Border var2 = null;
      int var3 = 0;

      for(int var4 = this.groups.length; var3 < var4; ++var3) {
         TableGroup var5 = this.groups[var3];
         int var6 = 0;

         for(int var7 = var5.rows.length; var6 < var7; ++var6) {
            TableRow var8 = var5.rows[var6];
            int var9 = 0;

            for(int var10 = var8.cells.length; var9 < var10; ++var9) {
               TableCell var11 = var8.cells[var9];
               if (!var11.isInvisible) {
                  int var12;
                  int var13;
                  if (var11.borders.top.style != 75) {
                     var1.setSize(0);
                     var2 = null;
                     var1.addElement(var8.borders.top);
                     if (var6 == 0) {
                        var1.addElement(var5.borders.top);
                        if (var3 == 0) {
                           var1.addElement(this.borders.top);
                        }
                     }

                     var12 = 0;

                     for(var13 = var1.size(); var12 < var13; ++var12) {
                        Border var14 = (Border)var1.elementAt(var12);
                        if (var14.style != 125) {
                           var2 = var14;
                           if (var14.style == 75) {
                              break;
                           }
                        }
                     }

                     if (var2 != null) {
                        var11.borders.top = var2;
                     }
                  }

                  Border var15;
                  int var18;
                  if (var11.borders.bottom.style != 75) {
                     var1.setSize(0);
                     var2 = null;
                     var12 = var6 + var11.rowSpan - 1;
                     var1.addElement(var5.rows[var12].borders.bottom);
                     if (var12 == var7 - 1) {
                        var1.addElement(var5.borders.bottom);
                        if (var3 == var4 - 1) {
                           var1.addElement(this.borders.bottom);
                        }
                     }

                     var13 = 0;

                     for(var18 = var1.size(); var13 < var18; ++var13) {
                        var15 = (Border)var1.elementAt(var13);
                        if (var15.style != 125) {
                           var2 = var15;
                           if (var15.style == 75) {
                              break;
                           }
                        }
                     }

                     if (var2 != null) {
                        var11.borders.bottom = var2;
                     }
                  }

                  if (var11.borders.left.style != 75) {
                     var1.setSize(0);
                     var2 = null;
                     TableColumn var17 = this.columns[var9];
                     if (var17 != null && var17.number == var9 + 1) {
                        var1.addElement(var17.borders.left);
                     }

                     if (var9 == 0) {
                        var1.addElement(this.borders.left);
                     }

                     var13 = 0;

                     for(var18 = var1.size(); var13 < var18; ++var13) {
                        var15 = (Border)var1.elementAt(var13);
                        if (var15.style != 125) {
                           var2 = var15;
                           if (var15.style == 75) {
                              break;
                           }
                        }
                     }

                     if (var2 != null) {
                        var11.borders.left = var2;
                     }
                  }

                  if (var11.borders.right.style != 75) {
                     var1.setSize(0);
                     var2 = null;
                     var12 = var9 + var11.colSpan - 1;
                     TableColumn var19 = this.columns[var12];
                     if (var19 != null && var19.last() == var12 + 1) {
                        var1.addElement(var19.borders.right);
                     }

                     if (var12 == var10 - 1) {
                        var1.addElement(this.borders.right);
                     }

                     var18 = 0;

                     for(int var20 = var1.size(); var18 < var20; ++var18) {
                        Border var16 = (Border)var1.elementAt(var18);
                        if (var16.style != 125) {
                           var2 = var16;
                           if (var16.style == 75) {
                              break;
                           }
                        }
                     }

                     if (var2 != null) {
                        var11.borders.right = var2;
                     }
                  }
               }
            }
         }
      }

      for(var3 = 0; var3 < this.rows.length; ++var3) {
         this.rows[var3].borders = null;
      }

      this.borders = null;
   }

   private void setSeparateBorders() {
      for(int var1 = 0; var1 < this.rows.length; ++var1) {
         this.rows[var1].borders = null;
      }

   }

   private double[] fixedLayout() {
      double var1 = this.tableWidth;
      double var3 = 0.0;
      double[] var5 = new double[this.numColumns];
      if (this.borderCollapse == 177) {
         var1 -= (double)(this.numColumns + 1) * this.columnSpacing;
         var1 -= this.borders.left.width + this.borders.right.width;
      }

      int var6;
      for(var6 = 0; var6 < this.numColumns; ++var6) {
         TableColumn var7 = this.columns[var6];
         if (var7 != null) {
            if (var7.widthType == 2) {
               var3 += var7.width;
            } else {
               var5[var6] = var7.width;
               var1 -= var7.width;
            }
         }
      }

      if (var3 > 0.0) {
         for(var6 = 0; var6 < this.numColumns; ++var6) {
            if (var5[var6] == 0.0) {
               var5[var6] = var1 * this.columns[var6].width / var3;
            }
         }
      }

      return var5;
   }

   private double[] automaticLayout() {
      double[] var7 = new double[this.numColumns];
      double[] var8 = new double[this.numColumns];

      int var1;
      for(var1 = 0; var1 < this.numColumns; ++var1) {
         TableColumn var9 = this.columns[var1];
         if (var9 != null && var9.width > 0.0) {
            var7[var1] = var9.width;
            var8[var1] = var9.width;
         }
      }

      int var3;
      int var4;
      int var5;
      TableRow var10;
      TableCell var11;
      TableGroup var19;
      for(var3 = 0; var3 < this.groups.length; ++var3) {
         var19 = this.groups[var3];

         for(var4 = 0; var4 < var19.rows.length; ++var4) {
            var10 = var19.rows[var4];

            for(var5 = 0; var5 < var10.cells.length; ++var5) {
               var11 = var10.cells[var5];
               if (var11.hMerge == 0 && var11.vMerge != 2) {
                  double var12 = var11.minWidth();
                  double var14 = var11.maxWidth();
                  double var16 = 0.0;
                  var16 += this.leftBorderWidth(var10, var11);
                  var16 += this.rightBorderWidth(var10, var11);
                  var12 += var16;
                  var14 += var16;
                  if (var12 > var7[var5]) {
                     var7[var5] = var12;
                  }

                  if (var14 > var8[var5]) {
                     var8[var5] = var14;
                  }
               }
            }
         }
      }

      double var13;
      for(var3 = 0; var3 < this.groups.length; ++var3) {
         var19 = this.groups[var3];

         for(var4 = 0; var4 < var19.rows.length; ++var4) {
            var10 = var19.rows[var4];

            for(var5 = 0; var5 < var10.cells.length; ++var5) {
               var11 = var10.cells[var5];
               if (var11.hMerge == 1 && var11.vMerge != 2) {
                  int var22 = var11.colSpan;
                  var13 = var11.minWidth();
                  double var15 = var11.maxWidth();
                  double var17 = 0.0;
                  var17 += this.leftBorderWidth(var10, var11);
                  var17 += this.rightBorderWidth(var10, var11);
                  var13 += var17;
                  var15 += var17;
                  var1 = var5;
                  int var2 = var5 + var22;

                  for(var17 = 0.0; var1 < var2; ++var1) {
                     var17 += var7[var1];
                  }

                  if (var13 > var17) {
                     var17 = (var13 - var17) / (double)var22;
                     var1 = var5;

                     for(var2 = var5 + var22; var1 < var2; ++var1) {
                        var7[var1] += var17;
                     }
                  }

                  var1 = var5;
                  var2 = var5 + var22;

                  for(var17 = 0.0; var1 < var2; ++var1) {
                     var17 += var8[var1];
                  }

                  if (var15 > var17) {
                     var17 = (var15 - var17) / (double)var22;
                     var1 = var5;

                     for(var2 = var5 + var22; var1 < var2; ++var1) {
                        var8[var1] += var17;
                     }
                  }
               }
            }
         }
      }

      double var20 = 0.0;
      double var21 = 0.0;

      for(var1 = 0; var1 < this.numColumns; ++var1) {
         var20 += var7[var1];
         var21 += var8[var1];
      }

      double[] var6;
      if (this.width > 0.0) {
         if (this.width > var20) {
            var13 = this.width - var20;
            var6 = this.expand(var7, var8, var13);
         } else if (this.width < var20) {
            var13 = var20 - this.width;
            var6 = this.shrink(var7, var13);
         } else {
            var6 = var7;
         }
      } else if (var21 > this.maxTableWidth) {
         if (this.maxTableWidth > var20) {
            var13 = this.maxTableWidth - var20;
            var6 = this.expand(var7, var8, var13);
         } else if (this.maxTableWidth < var20) {
            var13 = var20 - this.maxTableWidth;
            var6 = this.shrink(var7, var13);
         } else {
            var6 = var7;
         }
      } else {
         var6 = var8;
      }

      return var6;
   }

   private double[] expand(double[] var1, double[] var2, double var3) {
      double[] var9 = new double[this.numColumns];
      int var5 = 0;

      int var6;
      for(var6 = 0; var5 < this.numColumns; ++var5) {
         var9[var5] = var1[var5];
         if (var9[var5] < var2[var5]) {
            ++var6;
         }
      }

      while(true) {
         while(var3 > 0.0) {
            double var7;
            if (var6 == 0) {
               var7 = var3 / (double)this.numColumns;

               for(var5 = 0; var5 < this.numColumns; ++var5) {
                  var9[var5] += var7;
               }

               var3 = 0.0;
            } else {
               var7 = var3 / (double)var6;
               var5 = 0;
               var6 = 0;

               for(var3 = 0.0; var5 < this.numColumns; ++var5) {
                  if (var9[var5] < var2[var5]) {
                     var9[var5] += var7;
                  }

                  if (var9[var5] > var2[var5]) {
                     var3 += var9[var5] - var2[var5];
                     var9[var5] = var2[var5];
                  } else if (var9[var5] < var2[var5]) {
                     ++var6;
                  }
               }
            }
         }

         return var9;
      }
   }

   private double[] shrink(double[] var1, double var2) {
      double var7 = var1[0];
      double var9 = var1[0];
      double var11 = 0.0;
      double[] var13 = new double[this.numColumns];

      int var4;
      for(var4 = 1; var4 < this.numColumns; ++var4) {
         if (var1[var4] < var7) {
            var7 = var1[var4];
         } else if (var1[var4] > var9) {
            var9 = var1[var4];
         }
      }

      double var5 = (var7 + var9) / 2.0;

      for(var4 = 0; var4 < this.numColumns; ++var4) {
         if (var1[var4] >= var5) {
            var11 += var1[var4];
         }
      }

      for(var4 = 0; var4 < this.numColumns; ++var4) {
         if (var1[var4] >= var5) {
            var13[var4] = var1[var4] - var2 * var1[var4] / var11;
            if (var13[var4] <= 0.0) {
               var13[var4] = var1[var4];
            }
         } else {
            var13[var4] = var1[var4];
         }
      }

      return var13;
   }

   private double leftBorderWidth(TableRow var1, TableCell var2) {
      double var3 = 0.0;
      Border var5 = var2.borders.left;
      if (var5.materialized()) {
         var3 = var5.width;
      }

      if (this.borderCollapse == 37) {
         int var6 = var2.colNumber;
         if (var6 > 1) {
            var5 = var1.cells[var6 - 2].borders.right;
            if (var5.materialized() && var5.width > var3) {
               var3 = var5.width;
            }

            var3 /= 2.0;
         }
      }

      return var3;
   }

   private double rightBorderWidth(TableRow var1, TableCell var2) {
      double var3 = 0.0;
      int var6;
      if (var2.colSpan > 1) {
         var6 = var2.colNumber + var2.colSpan - 1;
         var2 = var1.cells[var6 - 1];
      }

      Border var5 = var2.borders.right;
      if (var5.materialized()) {
         var3 = var5.width;
      }

      if (this.borderCollapse == 37) {
         var6 = var2.colNumber;
         if (var6 < var1.cells.length) {
            var5 = var1.cells[var6].borders.left;
            if (var5.materialized() && var5.width > var3) {
               var3 = var5.width;
            }

            var3 /= 2.0;
         }
      }

      return var3;
   }

   public void print(PrintWriter var1) {
      if (this.columns != null) {
         double var2;
         if (this.spaceBefore > 0.0) {
            var2 = Math.rint(2.0 * this.spaceBefore);
            if (var2 < 2.0) {
               var2 = 2.0;
            }

            Paragraph var4 = Paragraph.empty(var2 / 2.0);
            var4.print(var1);
         }

         var1.println("<w:tbl>");
         var1.print("<w:tblPr>");
         if (this.columnSpacing > 0.0) {
            var2 = this.columnSpacing / 2.0;
            var1.print("<w:tblCellSpacing");
            var1.print(" w:w=\"" + Math.round(20.0 * var2) + "\"");
            var1.print(" w:type=\"dxa\"");
            var1.print(" />");
         }

         if (this.tableIndent > 0.0) {
            var1.print("<w:tblInd");
            var1.print(" w:w=\"" + Math.round(20.0 * this.tableIndent) + "\"");
            var1.print(" w:type=\"dxa\"");
            var1.print(" />");
         }

         if (this.borderCollapse == 177 && this.borders != null && this.borders.materialized()) {
            var1.print("<w:tblBorders>");
            this.borders.print(var1);
            var1.print("</w:tblBorders>");
         }

         if (this.background != null) {
            var1.print("<w:shd");
            var1.print(" w:val=\"clear\"");
            var1.print(" w:fill=\"" + Wml.hexColorType(this.background) + "\"");
            var1.print(" />");
         }

         var1.print("<w:tblLayout w:type=\"fixed\" />");
         var1.println("</w:tblPr>");
         var1.print("<w:tblGrid>");

         int var5;
         for(var5 = 0; var5 < this.numColumns; ++var5) {
            double var3 = this.columnWidths[var5];
            if (this.borderCollapse == 177) {
               var3 += this.columnSpacing;
               if (var5 == 0 || var5 == this.numColumns - 1) {
                  var3 += this.columnSpacing / 2.0;
               }
            }

            var1.print("<w:gridCol w:w=\"" + Math.round(20.0 * var3) + "\" />");
         }

         var1.println("</w:tblGrid>");

         for(var5 = 0; var5 < this.groups.length; ++var5) {
            this.groups[var5].print(var1);
         }

         var1.println("</w:tbl>");
      }
   }

   public double minWidth() {
      double var4 = this.width;
      if (var4 > 0.0 && !this.relativeWidth) {
         return var4 + this.startIndent + this.endIndent;
      } else {
         if (this.columns == null) {
            this.normalize();
         }

         double[] var7 = new double[this.numColumns];

         int var1;
         for(var1 = 0; var1 < this.numColumns; ++var1) {
            TableColumn var8 = this.columns[var1];
            if (var8 != null && var8.width > 0.0 && var8.widthType == 0) {
               var7[var8.number - 1] = var8.width;
            }
         }

         var1 = 0;
         var4 = 0.0;

         boolean var6;
         for(var6 = true; var1 < this.numColumns; ++var1) {
            if (var7[var1] == 0.0) {
               var6 = false;
               break;
            }

            var4 += var7[var1];
         }

         if (!var6) {
            for(int var15 = 0; var15 < this.groups.length; ++var15) {
               TableGroup var9 = this.groups[var15];

               for(int var10 = 0; var10 < var9.rows.length; ++var10) {
                  TableRow var11 = var9.rows[var10];

                  for(int var12 = 0; var12 < var11.cells.length; ++var12) {
                     TableCell var13 = var11.cells[var12];
                     if (var13.hMerge != 2 && var13.vMerge != 2) {
                        var4 = var13.minWidth();
                        var4 += this.leftBorderWidth(var11, var13);
                        var4 += this.rightBorderWidth(var11, var13);
                        var1 = var13.colNumber - 1;
                        int var14 = var13.colSpan;
                        if (var14 > 1) {
                           var4 /= (double)var14;

                           for(int var2 = var1 + var14; var1 < var2; ++var1) {
                              if (var4 > var7[var1]) {
                                 var7[var1] = var4;
                              }
                           }
                        } else if (var4 > var7[var1]) {
                           var7[var1] = var4;
                        }
                     }
                  }
               }
            }

            var1 = 0;

            for(var4 = 0.0; var1 < this.numColumns; ++var1) {
               var4 += var7[var1];
            }
         }

         var4 += this.startIndent + this.endIndent;
         return var4;
      }
   }

   public double maxWidth() {
      double var4 = this.width;
      if (var4 > 0.0 && !this.relativeWidth) {
         return var4 + this.startIndent + this.endIndent;
      } else {
         if (this.columns == null) {
            this.normalize();
         }

         double[] var7 = new double[this.numColumns];

         int var1;
         for(var1 = 0; var1 < this.numColumns; ++var1) {
            TableColumn var8 = this.columns[var1];
            if (var8 != null && var8.width > 0.0 && var8.widthType == 0) {
               var7[var8.number - 1] = var8.width;
            }
         }

         var1 = 0;
         var4 = 0.0;

         boolean var6;
         for(var6 = true; var1 < this.numColumns; ++var1) {
            if (var7[var1] == 0.0) {
               var6 = false;
               break;
            }

            var4 += var7[var1];
         }

         if (!var6) {
            for(int var15 = 0; var15 < this.groups.length; ++var15) {
               TableGroup var9 = this.groups[var15];

               for(int var10 = 0; var10 < var9.rows.length; ++var10) {
                  TableRow var11 = var9.rows[var10];

                  for(int var12 = 0; var12 < var11.cells.length; ++var12) {
                     TableCell var13 = var11.cells[var12];
                     if (var13.hMerge != 2 && var13.vMerge != 2) {
                        var4 = var13.maxWidth();
                        var4 += this.leftBorderWidth(var11, var13);
                        var4 += this.rightBorderWidth(var11, var13);
                        var1 = var13.colNumber - 1;
                        int var14 = var13.colSpan;
                        if (var14 > 1) {
                           var4 /= (double)var14;

                           for(int var2 = var1 + var14; var1 < var2; ++var1) {
                              if (var4 > var7[var1]) {
                                 var7[var1] = var4;
                              }
                           }
                        } else if (var4 > var7[var1]) {
                           var7[var1] = var4;
                        }
                     }
                  }
               }
            }

            var1 = 0;

            for(var4 = 0.0; var1 < this.numColumns; ++var1) {
               var4 += var7[var1];
            }
         }

         var4 += this.startIndent + this.endIndent;
         return var4;
      }
   }

   public double startIndent() {
      return this.startIndent;
   }
}

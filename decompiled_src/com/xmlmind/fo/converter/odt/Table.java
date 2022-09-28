package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

public class Table {
   private int layout;
   private Borders borders;
   private TableStyle style;
   private int numColumns;
   private Vector columnList = new Vector();
   private Hashtable columnHash = new Hashtable();
   private TableColumn[] columns;
   private TableGroup header;
   private TableGroup footer;
   private Vector bodies = new Vector();
   private TableGroup[] groups;
   private TableGroup group;
   private TableRow row;
   private TableCell cell;
   private double tableWidth;
   private double maxTableWidth;
   private double[] columnWidths;
   private TableColumnStyle[] columnStyles;

   public Table(Context var1) {
      this.initialize(var1);
   }

   private void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      this.layout = var2[283].keyword();
      this.borders = new Borders(var2);
      this.style = new TableStyle(var1);
   }

   public void setMasterPageName(String var1) {
      this.style.masterPageName = var1;
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

   public void startRow(Context var1) {
      this.row = new TableRow(var1);
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
         this.group.add(this.row);
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
         var2 -= this.cell.style.paddingLeft + this.cell.style.paddingRight;
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
         this.cell.add(var1);
         this.cell.requiresLayout = true;
         this.row.requiresLayout = true;
      }

   }

   public void add(TableAndCaption var1) {
      if (this.cell != null) {
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

   public void layout(double var1, StyleTable var3) throws Exception {
      if (this.columns == null) {
         this.normalize();
      }

      this.maxTableWidth = var1 - (this.style.marginLeft + this.style.marginRight);
      if (this.style.relativeWidth) {
         double var8 = this.style.width;
         this.style.width = this.maxTableWidth * var8 / 100.0;
         this.style.relativeWidth = false;
      }

      this.tableWidth = this.style.width > 0.0 ? this.style.width : this.maxTableWidth;

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

      int var5;
      int var6;
      int var7;
      TableCell var10;
      TableGroup var15;
      TableRow var17;
      for(var5 = 0; var5 < this.groups.length; ++var5) {
         var15 = this.groups[var5];

         for(var6 = 0; var6 < var15.rows.length; ++var6) {
            var17 = var15.rows[var6];

            for(var7 = 0; var7 < var17.cells.length; ++var7) {
               var10 = var17.cells[var7];
               if (var10.relativeWidth) {
                  double var11 = var10.width;
                  var10.width = this.tableWidth * var11 / 100.0;
                  var10.relativeWidth = false;
               }
            }
         }
      }

      boolean var16;
      if (this.layout == 69) {
         for(var4 = 0; var4 < this.columns.length; ++var4) {
            if (this.columns[var4] == null || this.columns[var4].width == 0.0) {
               this.layout = 10;
               break;
            }
         }
      } else {
         var16 = true;

         for(var4 = 0; var4 < this.columns.length; ++var4) {
            if (this.columns[var4] == null || this.columns[var4].width == 0.0) {
               var16 = false;
               break;
            }
         }

         if (var16) {
            this.layout = 69;
         }
      }

      if (this.layout == 69) {
         this.columnWidths = this.fixedLayout();
      } else {
         this.columnWidths = this.automaticLayout();
      }

      var4 = 0;

      for(this.tableWidth = 0.0; var4 < this.numColumns; ++var4) {
         this.tableWidth += this.columnWidths[var4];
      }

      this.style.width = this.tableWidth;
      if (Math.abs(this.maxTableWidth - this.tableWidth) < 1.0E-6) {
         this.style.alignment = 3;
      }

      if (this.style.alignment == 1 && this.style.marginRight != 0.0) {
         TableStyle var10000 = this.style;
         var10000.marginLeft += this.maxTableWidth - this.tableWidth;
         this.style.alignment = 3;
      }

      for(var5 = 0; var5 < this.groups.length; ++var5) {
         var15 = this.groups[var5];

         for(var6 = 0; var6 < var15.rows.length; ++var6) {
            TableRow var19 = var15.rows[var6];
            if (var19.requiresLayout) {
               for(var7 = 0; var7 < var19.cells.length; ++var7) {
                  TableCell var12 = var19.cells[var7];
                  if (var12.requiresLayout) {
                     if (var12.colSpan > 1) {
                        var4 = 0;

                        for(var9 = 0.0; var4 < var12.colSpan; ++var4) {
                           var9 += this.columnWidths[var7 + var4];
                        }
                     } else {
                        var9 = this.columnWidths[var7];
                     }

                     if (this.style.borderModel == 0) {
                        var9 -= this.leftBorderWidth(var19, var12);
                        var9 -= this.rightBorderWidth(var19, var12);
                     } else {
                        Borders var13 = var12.style.borders;
                        if (var13.left.materialized()) {
                           var9 -= var13.left.width;
                        }

                        if (var13.right.materialized()) {
                           var9 -= var13.right.width;
                        }
                     }

                     var12.layout(var9, var3);
                  }
               }
            }
         }
      }

      for(var5 = 0; var5 < this.groups.length; ++var5) {
         var15 = this.groups[var5];

         for(var6 = 0; var6 < var15.rows.length; ++var6) {
            var17 = var15.rows[var6];
            var17.style = var3.add(var17.style);

            for(var7 = 0; var7 < var17.cells.length; ++var7) {
               var10 = var17.cells[var7];
               if (!var10.isCovered && !var10.hasBackground() && !var17.hasBackground()) {
                  TableColumn var20 = this.columns[var7];
                  if (var20 != null && var20.background != null && var20.number == var10.colNumber && var20.span == var10.colSpan) {
                     var10.setBackground(var20.background);
                  }
               }
            }
         }
      }

      this.style = var3.add(this.style);
      this.columnStyles = new TableColumnStyle[this.numColumns];
      var16 = this.layout == 10;

      for(var4 = 0; var4 < this.numColumns; ++var4) {
         this.columnStyles[var4] = new TableColumnStyle(this.columnWidths[var4]);
         this.columnStyles[var4].optimizeWidth = var16;
         this.columnStyles[var4] = var3.add(this.columnStyles[var4]);
      }

      for(var5 = 0; var5 < this.groups.length; ++var5) {
         TableGroup var22 = this.groups[var5];

         for(var6 = 0; var6 < var22.rows.length; ++var6) {
            TableRow var18 = var22.rows[var6];
            var18.style = var3.add(var18.style);

            for(var7 = 0; var7 < var18.cells.length; ++var7) {
               TableCell var21 = var18.cells[var7];
               var21.style = var3.add(var21.style);
            }
         }
      }

   }

   private void normalize() {
      this.columns = new TableColumn[this.numColumns];
      int var1 = 0;

      int var2;
      int var3;
      for(var3 = this.columnList.size(); var1 < var3; ++var1) {
         TableColumn var8 = (TableColumn)this.columnList.elementAt(var1);

         for(var2 = 0; var2 < var8.span; ++var2) {
            this.columns[var8.number + var2 - 1] = var8;
         }
      }

      int var7 = this.bodies.size();
      if (this.header != null) {
         ++var7;
      }

      if (this.footer != null) {
         ++var7;
      }

      this.groups = new TableGroup[var7];
      if (this.header != null) {
         this.groups[0] = this.header;
      }

      var1 = 0;
      var2 = this.header == null ? 0 : 1;

      for(var3 = this.bodies.size(); var1 < var3; ++var2) {
         this.groups[var2] = (TableGroup)this.bodies.elementAt(var1);
         ++var1;
      }

      if (this.footer != null) {
         this.groups[var7 - 1] = this.footer;
      }

      for(var1 = 0; var1 < this.groups.length; ++var1) {
         this.groups[var1].normalize(this.numColumns);
      }

      if (this.style.borderModel == 0) {
         this.setBorders();
      }

   }

   public void setBorders() {
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
               if (!var11.isCovered) {
                  int var12;
                  int var13;
                  if (var11.style.borders.top.style != 75) {
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
                        var11.style.borders.top = var2;
                     }
                  }

                  Border var15;
                  int var18;
                  if (var11.style.borders.bottom.style != 75) {
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
                        var11.style.borders.bottom = var2;
                     }
                  }

                  if (var11.style.borders.left.style != 75) {
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
                        var11.style.borders.left = var2;
                     }
                  }

                  if (var11.style.borders.right.style != 75) {
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
                        var11.style.borders.right = var2;
                     }
                  }
               }
            }
         }
      }

   }

   private double[] fixedLayout() {
      double var1 = this.tableWidth;
      double var3 = 0.0;
      double[] var5 = new double[this.numColumns];

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
      TableGroup var20;
      for(var3 = 0; var3 < this.groups.length; ++var3) {
         var20 = this.groups[var3];

         for(var4 = 0; var4 < var20.rows.length; ++var4) {
            var10 = var20.rows[var4];

            for(var5 = 0; var5 < var10.cells.length; ++var5) {
               var11 = var10.cells[var5];
               if (!var11.isCovered && var11.colSpan <= 1) {
                  double var12 = var11.minWidth();
                  double var14 = var11.maxWidth();
                  double var16 = 0.0;
                  if (this.style.borderModel == 0) {
                     var16 += this.leftBorderWidth(var10, var11);
                     var16 += this.rightBorderWidth(var10, var11);
                  } else {
                     Borders var18 = var11.style.borders;
                     if (var18.left.materialized()) {
                        var16 += var18.left.width;
                     }

                     if (var18.right.materialized()) {
                        var16 += var18.right.width;
                     }
                  }

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
         var20 = this.groups[var3];

         for(var4 = 0; var4 < var20.rows.length; ++var4) {
            var10 = var20.rows[var4];

            for(var5 = 0; var5 < var10.cells.length; ++var5) {
               var11 = var10.cells[var5];
               if (!var11.isCovered && var11.colSpan != 1) {
                  int var23 = var11.colSpan;
                  var13 = var11.minWidth();
                  double var15 = var11.maxWidth();
                  double var17 = 0.0;
                  if (this.style.borderModel == 0) {
                     var17 += this.leftBorderWidth(var10, var11);
                     var17 += this.rightBorderWidth(var10, var11);
                  } else {
                     Borders var19 = var11.style.borders;
                     if (var19.left.materialized()) {
                        var17 += var19.left.width;
                     }

                     if (var19.right.materialized()) {
                        var17 += var19.right.width;
                     }
                  }

                  var13 += var17;
                  var15 += var17;
                  var1 = var5;
                  int var2 = var5 + var23;

                  for(var17 = 0.0; var1 < var2; ++var1) {
                     var17 += var7[var1];
                  }

                  if (var13 > var17) {
                     var17 = (var13 - var17) / (double)var23;
                     var1 = var5;

                     for(var2 = var5 + var23; var1 < var2; ++var1) {
                        var7[var1] += var17;
                     }
                  }

                  var1 = var5;
                  var2 = var5 + var23;

                  for(var17 = 0.0; var1 < var2; ++var1) {
                     var17 += var8[var1];
                  }

                  if (var15 > var17) {
                     var17 = (var15 - var17) / (double)var23;
                     var1 = var5;

                     for(var2 = var5 + var23; var1 < var2; ++var1) {
                        var8[var1] += var17;
                     }
                  }
               }
            }
         }
      }

      double var21 = 0.0;
      double var22 = 0.0;

      for(var1 = 0; var1 < this.numColumns; ++var1) {
         var21 += var7[var1];
         var22 += var8[var1];
      }

      double[] var6;
      if (this.style.width > 0.0) {
         if (this.style.width > var21) {
            var13 = this.style.width - var21;
            var6 = this.expand(var7, var8, var13);
         } else if (this.style.width < var21) {
            var13 = var21 - this.style.width;
            var6 = this.shrink(var7, var13);
         } else {
            var6 = var7;
         }
      } else if (var22 > this.maxTableWidth) {
         if (this.maxTableWidth > var21) {
            var13 = this.maxTableWidth - var21;
            var6 = this.expand(var7, var8, var13);
         } else if (this.maxTableWidth < var21) {
            var13 = var21 - this.maxTableWidth;
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
      Borders var5 = var2.style.borders;
      if (var5.left.materialized()) {
         var3 = var5.left.width;
      }

      int var6 = var2.colNumber - 1;
      if (var6 >= 1) {
         var5 = var1.cells[var6 - 1].style.borders;
         if (var5.right.materialized() && var5.right.width > var3) {
            var3 = var5.right.width;
         }

         var3 /= 2.0;
      }

      return var3;
   }

   private double rightBorderWidth(TableRow var1, TableCell var2) {
      double var3 = 0.0;
      Borders var5 = var2.style.borders;
      if (var5.right.materialized()) {
         var3 = var5.right.width;
      }

      int var6 = var2.colNumber + var2.colSpan;
      if (var6 <= var1.cells.length) {
         var5 = var1.cells[var6 - 1].style.borders;
         if (var5.left.materialized() && var5.left.width > var3) {
            var3 = var5.left.width;
         }

         var3 /= 2.0;
      }

      return var3;
   }

   public void print(PrintWriter var1, Encoder var2) {
      var1.print("<table:table");
      var1.print(" table:style-name=\"" + this.style.name + "\"");
      var1.println(">");

      int var3;
      for(var3 = 0; var3 < this.numColumns; ++var3) {
         var1.print("<table:table-column");
         var1.print(" table:style-name=\"" + this.columnStyles[var3].name + "\"");
         var1.println("/>");
      }

      if (this.header != null) {
         var1.println("<table:table-header-rows>");
         this.header.print(var1, var2);
         var1.println("</table:table-header-rows>");
      }

      var1.println("<table:table-rows>");
      var3 = 0;

      for(int var4 = this.bodies.size(); var3 < var4; ++var3) {
         TableGroup var5 = (TableGroup)this.bodies.elementAt(var3);
         var5.print(var1, var2);
      }

      if (this.footer != null) {
         this.footer.print(var1, var2);
      }

      var1.println("</table:table-rows>");
      var1.println("</table:table>");
   }

   public double minWidth() {
      if (this.style.width > 0.0 && !this.style.relativeWidth) {
         return this.style.width + this.style.marginLeft + this.style.marginRight;
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
         double var4 = 0.0;

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
                     if (!var13.isCovered) {
                        var4 = var13.minWidth();
                        if (this.style.borderModel == 0) {
                           var4 += this.leftBorderWidth(var11, var13);
                           var4 += this.rightBorderWidth(var11, var13);
                        } else {
                           Borders var14 = var13.style.borders;
                           if (var14.left.materialized()) {
                              var4 += var14.left.width;
                           }

                           if (var14.right.materialized()) {
                              var4 += var14.right.width;
                           }
                        }

                        var1 = var13.colNumber - 1;
                        int var16 = var13.colSpan;
                        if (var16 > 1) {
                           var4 /= (double)var16;

                           for(int var2 = var1 + var16; var1 < var2; ++var1) {
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

         var4 += this.style.marginLeft + this.style.marginRight;
         return var4;
      }
   }

   public double maxWidth() {
      if (this.style.width > 0.0 && !this.style.relativeWidth) {
         return this.style.width + this.style.marginLeft + this.style.marginRight;
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
         double var4 = 0.0;

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
                     if (!var13.isCovered) {
                        var4 = var13.maxWidth();
                        if (this.style.borderModel == 0) {
                           var4 += this.leftBorderWidth(var11, var13);
                           var4 += this.rightBorderWidth(var11, var13);
                        } else {
                           Borders var14 = var13.style.borders;
                           if (var14.left.materialized()) {
                              var4 += var14.left.width;
                           }

                           if (var14.right.materialized()) {
                              var4 += var14.right.width;
                           }
                        }

                        var1 = var13.colNumber - 1;
                        int var16 = var13.colSpan;
                        if (var16 > 1) {
                           var4 /= (double)var16;

                           for(int var2 = var1 + var16; var1 < var2; ++var1) {
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

         var4 += this.style.marginLeft + this.style.marginRight;
         return var4;
      }
   }

   public double marginLeft() {
      return this.style.marginLeft;
   }
}

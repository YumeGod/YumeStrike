package com.xmlmind.fo.converter.odt;

import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

public class AutomaticStyles {
   private FontTable fontTable;
   private Hashtable paragraphStyles = new Hashtable();
   private Vector paragraphStyleList = new Vector();
   private Hashtable textStyles = new Hashtable();
   private Vector textStyleList = new Vector();
   private Hashtable graphicStyles = new Hashtable();
   private Vector graphicStyleList = new Vector();
   private Hashtable listStyles = new Hashtable();
   private Vector listStyleList = new Vector();
   private Hashtable tableStyles = new Hashtable();
   private Vector tableStyleList = new Vector();
   private Hashtable tableColumnStyles = new Hashtable();
   private Vector tableColumnStyleList = new Vector();
   private Hashtable tableRowStyles = new Hashtable();
   private Vector tableRowStyleList = new Vector();
   private Hashtable tableCellStyles = new Hashtable();
   private Vector tableCellStyleList = new Vector();

   public AutomaticStyles(FontTable var1) {
      this.fontTable = var1;
   }

   public ParagraphStyle add(ParagraphStyle var1) {
      ParagraphStyle var2 = (ParagraphStyle)this.paragraphStyles.get(var1);
      if (var2 == null) {
         var1.name = "P" + this.paragraphStyles.size();
         this.paragraphStyles.put(var1, var1);
         this.paragraphStyleList.addElement(var1);
      } else {
         var1 = var2;
      }

      return var1;
   }

   public TextStyle add(TextStyle var1) {
      TextStyle var2 = (TextStyle)this.textStyles.get(var1);
      if (var2 == null) {
         var1.font = this.fontTable.add(var1.font);
         var1.name = "T" + this.textStyles.size();
         this.textStyles.put(var1, var1);
         this.textStyleList.addElement(var1);
      } else {
         var1 = var2;
      }

      return var1;
   }

   public GraphicStyle add(GraphicStyle var1) {
      GraphicStyle var2 = (GraphicStyle)this.graphicStyles.get(var1);
      if (var2 == null) {
         var1.name = "G" + this.graphicStyles.size();
         this.graphicStyles.put(var1, var1);
         this.graphicStyleList.addElement(var1);
      } else {
         var1 = var2;
      }

      return var1;
   }

   public ListStyle add(ListStyle var1) {
      ListStyle var2 = (ListStyle)this.listStyles.get(var1);
      if (var2 == null) {
         var1.textStyle.font = this.fontTable.add(var1.textStyle.font);
         var1.name = "L" + this.listStyles.size();
         this.listStyles.put(var1, var1);
         this.listStyleList.addElement(var1);
      } else {
         if (var1.listLevel > var2.listLevel) {
            var2.listLevel = var1.listLevel;
         }

         var1 = var2;
      }

      return var1;
   }

   public TableStyle add(TableStyle var1) {
      TableStyle var2 = (TableStyle)this.tableStyles.get(var1);
      if (var2 == null) {
         var1.name = "TB" + this.tableStyles.size();
         this.tableStyles.put(var1, var1);
         this.tableStyleList.addElement(var1);
      } else {
         var1 = var2;
      }

      return var1;
   }

   public TableColumnStyle add(TableColumnStyle var1) {
      TableColumnStyle var2 = (TableColumnStyle)this.tableColumnStyles.get(var1);
      if (var2 == null) {
         var1.name = "TC" + this.tableColumnStyles.size();
         this.tableColumnStyles.put(var1, var1);
         this.tableColumnStyleList.addElement(var1);
      } else {
         var1 = var2;
      }

      return var1;
   }

   public TableRowStyle add(TableRowStyle var1) {
      TableRowStyle var2 = (TableRowStyle)this.tableRowStyles.get(var1);
      if (var2 == null) {
         var1.name = "TR" + this.tableRowStyles.size();
         this.tableRowStyles.put(var1, var1);
         this.tableRowStyleList.addElement(var1);
      } else {
         var1 = var2;
      }

      return var1;
   }

   public TableCellStyle add(TableCellStyle var1) {
      TableCellStyle var2 = (TableCellStyle)this.tableCellStyles.get(var1);
      if (var2 == null) {
         var1.name = "TL" + this.tableCellStyles.size();
         this.tableCellStyles.put(var1, var1);
         this.tableCellStyleList.addElement(var1);
      } else {
         var1 = var2;
      }

      return var1;
   }

   public void print(PrintWriter var1) {
      int var2 = 0;

      int var3;
      for(var3 = this.paragraphStyleList.size(); var2 < var3; ++var2) {
         ParagraphStyle var4 = (ParagraphStyle)this.paragraphStyleList.elementAt(var2);
         var4.print(var1);
      }

      var2 = 0;

      for(var3 = this.textStyleList.size(); var2 < var3; ++var2) {
         TextStyle var5 = (TextStyle)this.textStyleList.elementAt(var2);
         var5.print(var1);
      }

      var2 = 0;

      for(var3 = this.graphicStyleList.size(); var2 < var3; ++var2) {
         GraphicStyle var6 = (GraphicStyle)this.graphicStyleList.elementAt(var2);
         var6.print(var1);
      }

      var2 = 0;

      for(var3 = this.listStyleList.size(); var2 < var3; ++var2) {
         ListStyle var7 = (ListStyle)this.listStyleList.elementAt(var2);
         var7.print(var1);
      }

      var2 = 0;

      for(var3 = this.tableStyleList.size(); var2 < var3; ++var2) {
         TableStyle var8 = (TableStyle)this.tableStyleList.elementAt(var2);
         var8.print(var1);
      }

      var2 = 0;

      for(var3 = this.tableColumnStyleList.size(); var2 < var3; ++var2) {
         TableColumnStyle var9 = (TableColumnStyle)this.tableColumnStyleList.elementAt(var2);
         var9.print(var1);
      }

      var2 = 0;

      for(var3 = this.tableRowStyleList.size(); var2 < var3; ++var2) {
         TableRowStyle var10 = (TableRowStyle)this.tableRowStyleList.elementAt(var2);
         var10.print(var1);
      }

      var2 = 0;

      for(var3 = this.tableCellStyleList.size(); var2 < var3; ++var2) {
         TableCellStyle var11 = (TableCellStyle)this.tableCellStyleList.elementAt(var2);
         var11.print(var1);
      }

   }
}

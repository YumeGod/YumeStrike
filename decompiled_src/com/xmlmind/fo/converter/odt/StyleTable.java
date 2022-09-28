package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.util.Encoder;
import com.xmlmind.fo.util.EncoderFactory;
import com.xmlmind.fo.util.Encoding;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

public class StyleTable {
   public static final int CONTENT_STYLES = 0;
   public static final int LOCAL_STYLES = 1;
   private FontTable fontTable;
   private AutomaticStyles contentStyles;
   private AutomaticStyles localStyles;
   private AutomaticStyles automaticStyles;
   private Hashtable labelStyles = new Hashtable();
   private Vector labelStyleList = new Vector();
   private Vector pageLayouts = new Vector();
   private Vector masterPages = new Vector();

   public StyleTable(FontTable var1) {
      this.fontTable = var1;
      this.contentStyles = new AutomaticStyles(var1);
      this.localStyles = new AutomaticStyles(var1);
      this.select(0);
   }

   public void select(int var1) {
      if (var1 == 1) {
         this.automaticStyles = this.localStyles;
      } else {
         this.automaticStyles = this.contentStyles;
      }

   }

   public ParagraphStyle add(ParagraphStyle var1) {
      return this.automaticStyles.add(var1);
   }

   public TextStyle add(TextStyle var1) {
      return this.automaticStyles.add(var1);
   }

   public GraphicStyle add(GraphicStyle var1) {
      return this.automaticStyles.add(var1);
   }

   public ListStyle add(ListStyle var1) {
      if (var1.textStyle != null) {
         var1.textStyle = this.addLabelStyle(var1.textStyle.copy());
      }

      return this.automaticStyles.add(var1);
   }

   private TextStyle addLabelStyle(TextStyle var1) {
      TextStyle var2 = (TextStyle)this.labelStyles.get(var1);
      if (var2 == null) {
         var1.font = this.fontTable.add(var1.font);
         var1.name = "List_Item_Label_" + this.labelStyles.size();
         this.labelStyles.put(var1, var1);
         this.labelStyleList.addElement(var1);
      } else {
         var1 = var2;
      }

      return var1;
   }

   public TableStyle add(TableStyle var1) {
      return this.automaticStyles.add(var1);
   }

   public TableColumnStyle add(TableColumnStyle var1) {
      return this.automaticStyles.add(var1);
   }

   public TableRowStyle add(TableRowStyle var1) {
      return this.automaticStyles.add(var1);
   }

   public TableCellStyle add(TableCellStyle var1) {
      return this.automaticStyles.add(var1);
   }

   public String add(PageLayout var1) {
      var1.name = "PL" + this.pageLayouts.size();
      this.pageLayouts.addElement(var1);
      return var1.name;
   }

   public String add(MasterPage var1) {
      var1.name = "MP" + this.masterPages.size();
      this.masterPages.addElement(var1);
      return var1.name;
   }

   public void write(String var1, String var2) throws Exception {
      FileOutputStream var5 = new FileOutputStream(var1);
      OutputStreamWriter var6 = new OutputStreamWriter(new BufferedOutputStream(var5), var2);
      PrintWriter var7 = new PrintWriter(new BufferedWriter(var6));
      var7.println("<?xml version=\"1.0\" encoding=\"" + Encoding.officialName(var2) + "\"?>");
      var7.print("<office:document-styles");
      var7.print(" xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\"");
      var7.print(" xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\"");
      var7.print(" xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\"");
      var7.print(" xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\"");
      var7.print(" xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\"");
      var7.print(" xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\"");
      var7.print(" xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\"");
      var7.print(" xmlns:xlink=\"http://www.w3.org/1999/xlink\"");
      var7.print(" office:version=\"1.0\"");
      var7.println(">");
      this.fontTable.print(var7);
      var7.println("<office:styles>");
      int var3 = 0;

      int var4;
      for(var4 = this.labelStyleList.size(); var3 < var4; ++var3) {
         TextStyle var8 = (TextStyle)this.labelStyleList.elementAt(var3);
         var8.print(var7);
      }

      var7.println("</office:styles>");
      var7.println("<office:automatic-styles>");
      this.localStyles.print(var7);
      var3 = 0;

      for(var4 = this.pageLayouts.size(); var3 < var4; ++var3) {
         PageLayout var11 = (PageLayout)this.pageLayouts.elementAt(var3);
         var11.print(var7);
      }

      var7.println("</office:automatic-styles>");
      var7.println("<office:master-styles>");
      Encoder var12 = null;
      String var9 = Encoding.officialName(var2);
      if (!var9.startsWith("UTF")) {
         var12 = EncoderFactory.newEncoder(var9);
      }

      var3 = 0;

      for(var4 = this.masterPages.size(); var3 < var4; ++var3) {
         MasterPage var10 = (MasterPage)this.masterPages.elementAt(var3);
         var10.print(var7, var12);
      }

      var7.println("</office:master-styles>");
      var7.println("</office:document-styles>");
      var7.flush();
      var7.close();
   }

   public void print(PrintWriter var1) {
      this.fontTable.print(var1);
      var1.println("<office:automatic-styles>");
      this.contentStyles.print(var1);
      var1.println("</office:automatic-styles>");
   }
}

package com.xmlmind.fo.converter.docx.sdt;

import com.xmlmind.fo.converter.docx.RunProperties;
import java.io.PrintWriter;
import org.xml.sax.Attributes;

public final class SdtPicture extends SdtElement {
   private static final double DEFAULT_WIDTH = 144.0;
   private static final double DEFAULT_HEIGHT = 144.0;
   private int width = toEmu(144.0);
   private int height = toEmu(144.0);
   private int id;
   private String rId;

   public SdtPicture(Attributes var1, RunProperties var2) {
      super(var1, var2);
      if (this.title == null) {
         this.title = "Picture";
      }

      this.initialValue = null;
   }

   public void setImageData(int var1, String var2) {
      this.id = var1;
      this.rId = var2;
   }

   public void setGeometry(double var1, double var3) {
      this.width = toEmu(var1);
      this.height = toEmu(var3);
   }

   public void setInitialValue(String var1) {
      this.initialValue = var1;
   }

   private static int toEmu(double var0) {
      double var2 = var0 * 2.54 * 360000.0 / 72.0;
      return (int)Math.round(var2 / 1000.0) * 1000;
   }

   protected void printPlaceholder(PrintWriter var1) {
      this.printContent(var1);
   }

   protected void printContent(PrintWriter var1) {
      if (this.rId != null) {
         String var2 = "SDT_PICTURE_" + Integer.toString(this.id / 2 + 1);
         var1.println("<w:sdtContent>");
         var1.println("<w:r>");
         if (this.properties != null) {
            this.properties.print(var1);
         }

         var1.println("<w:drawing>");
         var1.println("<wp:inline>");
         var1.print("<wp:extent");
         var1.print(" cx=\"" + this.width + "\"");
         var1.print(" cy=\"" + this.height + "\"");
         var1.println(" />");
         var1.print("<wp:docPr");
         var1.print(" id=\"" + (this.id + 1) + "\"");
         var1.print(" name=\"" + var2 + "\"");
         var1.println(" />");
         var1.println("<wp:cNvGraphicFramePr>");
         var1.println("<a:graphicFrameLocks noChangeAspect=\"1\" />");
         var1.println("</wp:cNvGraphicFramePr>");
         var1.println("<a:graphic>");
         var1.println("<a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">");
         var1.println("<p:pic>");
         var1.println("<p:nvPicPr>");
         var1.print("<p:cNvPr");
         var1.print(" id=\"" + this.id + "\"");
         var1.print(" name=\"" + var2 + "\"");
         var1.println(" />");
         var1.println("<p:cNvPicPr />");
         var1.println("</p:nvPicPr>");
         var1.println("<p:blipFill>");
         var1.println("<a:blip r:embed=\"" + this.rId + "\" />");
         var1.println("<a:stretch />");
         var1.println("</p:blipFill>");
         var1.println("<p:spPr>");
         var1.println("<a:xfrm>");
         var1.println("<a:off x=\"0\" y=\"0\" />");
         var1.print("<a:ext");
         var1.print(" cx=\"" + this.width + "\"");
         var1.print(" cy=\"" + this.height + "\"");
         var1.println(" />");
         var1.println("</a:xfrm>");
         var1.println("<a:prstGeom prst=\"rect\" />");
         var1.println("</p:spPr>");
         var1.println("</p:pic>");
         var1.println("</a:graphicData>");
         var1.println("</a:graphic>");
         var1.println("</wp:inline>");
         var1.println("</w:drawing>");
         var1.println("</w:r>");
         var1.println("</w:sdtContent>");
      }
   }

   protected void printType(PrintWriter var1) {
      var1.println("<w:picture />");
   }
}

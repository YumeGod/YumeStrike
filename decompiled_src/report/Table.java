package report;

import dialog.DialogUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Table implements ReportElement {
   protected List columns;
   protected List widths;
   protected List rows;

   public Table(List var1, List var2, List var3) {
      this.columns = var1;
      this.widths = var2;
      this.rows = var3;
   }

   public void publish(StringBuffer var1) {
      if (this.rows.size() == 0) {
         var1.append(ReportUtils.br());
      } else {
         var1.append("<fo:table border-bottom=\"1pt solid black\" margin-bottom=\"12pt\" border-left=\"none\" border-right=\"none\" width=\"100%\" border-separation=\"0\">\n");
         Iterator var2 = this.widths.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append(ReportUtils.ColumnWidth(var3) + "\n");
         }

         var1.append("\t<fo:table-header border-bottom=\"1pt solid black\" background-color=\"#cccccc\">\n");
         Iterator var11 = this.columns.iterator();

         for(int var4 = 0; var11.hasNext(); ++var4) {
            String var5 = (String)var11.next();
            var1.append("\t\t<fo:table-cell>\n");
            var1.append("\t\t\t<fo:block font-weight=\"bold\" ");
            if (var4 == 0) {
               var1.append("margin-left=\"2pt\"");
            }

            var1.append(" font-family=\"sans-serif\" padding=\"0.02in\" padding-left=\"0in\">\n");
            var1.append(Content.fixText(var5));
            var1.append("\t\t\t</fo:block>\n");
            var1.append("\t\t</fo:table-cell>\n");
         }

         var1.append("\t</fo:table-header>\n");
         var1.append("\t<fo:table-body>\n");
         Iterator var12 = this.rows.iterator();

         for(int var13 = 0; var12.hasNext(); ++var13) {
            Map var6 = (Map)var12.next();
            if (var13 % 2 == 1) {
               var1.append("\t\t<fo:table-row background-color=\"#eeeeee\" border=\"none\" margin=\"0\" padding=\"0\">\n");
            } else {
               var1.append("\t\t<fo:table-row>\n");
            }

            Iterator var7 = this.columns.iterator();

            for(int var8 = 0; var7.hasNext(); ++var8) {
               String var9 = (String)var7.next();
               if (var8 == 0) {
                  var1.append("\t\t\t<fo:table-cell margin-left=\"2pt\">\n");
               } else {
                  var1.append("\t\t\t<fo:table-cell>\n");
               }

               String var10 = DialogUtils.string(var6, var9);
               if (var10 == null) {
                  var10 = "";
               }

               var1.append("\t\t\t\t<fo:block padding-top=\"2pt\" padding-bottom=\"1pt\" font-family=\"sans-serif\">" + var10 + "</fo:block>\n");
               var1.append("\t\t\t</fo:table-cell>\n");
            }

            var1.append("\t\t</fo:table-row>\n");
         }

         var1.append("\t</fo:table-body>\n");
         var1.append("</fo:table>");
      }
   }
}

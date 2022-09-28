package report;

import dialog.DialogUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Layout implements ReportElement {
   protected List columns;
   protected List widths;
   protected List rows;

   public Layout(List var1, List var2, List var3) {
      this.columns = var1;
      this.widths = var2;
      this.rows = var3;
   }

   public void publish(StringBuffer var1) {
      if (this.rows.size() == 0) {
         var1.append(ReportUtils.br());
      } else {
         var1.append("<fo:table border-bottom=\"none\" border-left=\"none\" border-right=\"none\" width=\"100%\" border-separation=\"0\">\n");
         Iterator var2 = this.widths.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append(ReportUtils.ColumnWidth(var3) + "\n");
         }

         var1.append("\t<fo:table-body>\n");
         Iterator var10 = this.rows.iterator();

         for(int var4 = 0; var10.hasNext(); ++var4) {
            Map var5 = (Map)var10.next();
            var1.append("\t\t<fo:table-row>\n");
            Iterator var6 = this.columns.iterator();

            for(int var7 = 0; var6.hasNext(); ++var7) {
               String var8 = (String)var6.next();
               var1.append("\t\t\t<fo:table-cell>\n");
               String var9 = DialogUtils.string(var5, var8);
               if (var9 == null) {
                  var9 = "";
               }

               var1.append("\t\t\t\t<fo:block font-family=\"sans-serif\">" + var9 + "</fo:block>\n");
               var1.append("\t\t\t</fo:table-cell>\n");
            }

            var1.append("\t\t</fo:table-row>\n");
         }

         var1.append("\t</fo:table-body>\n");
         var1.append("</fo:table>");
      }
   }
}

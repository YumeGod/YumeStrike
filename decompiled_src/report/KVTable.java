package report;

import java.util.Iterator;
import java.util.Map;

public class KVTable implements ReportElement {
   protected Map entries;

   public KVTable(Map var1) {
      this.entries = var1;
   }

   public void publish(StringBuffer var1) {
      var1.append("<fo:table width=\"100%\" border-separation=\"0\" margin-top=\"8pt\" margin-bottom=\"8pt\">\n");
      var1.append(ReportUtils.ColumnWidth("2in") + "\n");
      var1.append(ReportUtils.ColumnWidth("4.5in") + "\n");
      var1.append("<fo:table-body>\n");
      Iterator var2 = this.entries.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         var1.append("\t\t<fo:table-row>\n");
         var1.append("\t\t\t<fo:table-cell>\n");
         var1.append("<fo:block font-weight=\"bold\" font-family=\"sans-serif\">" + Content.fixText((String)var3.getKey()) + ":</fo:block>\n");
         var1.append("\t\t\t</fo:table-cell>\n");
         var1.append("\t\t\t<fo:table-cell>\n");
         var1.append("<fo:block font-family=\"sans-serif\">" + Content.fixText((String)var3.getValue()) + "</fo:block>\n");
         var1.append("\t\t\t</fo:table-cell>\n");
         var1.append("\t\t</fo:table-row>\n");
      }

      var1.append("\t</fo:table-body>\n");
      var1.append("</fo:table>");
   }
}

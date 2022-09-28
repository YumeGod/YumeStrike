package report;

import common.CommonUtils;

public class Page extends Content {
   public static final int PAGE_FIRST = 0;
   public static final int PAGE_REST = 1;
   public static final int PAGE_FIRST_CENTER = 2;
   public static final int PAGE_SINGLE = 3;
   protected int type;
   protected String title;

   public Page(Document var1, int var2, String var3) {
      super(var1);
      this.type = var2;
      this.title = var3;
   }

   public void publish(StringBuffer var1) {
      if (!this.isEmpty()) {
         if (this.type == 0) {
            var1.append("<fo:page-sequence master-reference=\"first\">\n");
            var1.append("<fo:static-content flow-name=\"xsl-region-before\" color=\"black\">\n");
            var1.append("\t<fo:block border-bottom=\"2pt solid " + ReportUtils.accent() + "\">\n");
            var1.append("\t\t<fo:external-graphic src=\"" + ReportUtils.logo() + "\" />\n");
            var1.append("\t</fo:block>\n");
            var1.append("</fo:static-content>");
         } else if (this.type == 2) {
            var1.append("<fo:page-sequence master-reference=\"first\">\n");
            var1.append("\t<fo:static-content flow-name=\"xsl-region-before\" color=\"black\">\n");
            var1.append("\t\t<fo:block>\n");
            var1.append("\t\t\t<fo:table border-bottom=\"2pt solid " + ReportUtils.accent() + "\">");
            var1.append("\t\t\t\t" + ReportUtils.ColumnWidth("2.35in") + "\n");
            var1.append("\t\t\t\t" + ReportUtils.ColumnWidth("4.0in") + "\n");
            var1.append("\t\t\t\t" + ReportUtils.ColumnWidth("2.35in") + "\n");
            var1.append("\t\t\t\t<fo:table-body>\n");
            var1.append("\t\t\t\t\t<fo:table-row>\n");
            var1.append("\t\t\t\t\t\t<fo:table-cell><fo:block></fo:block></fo:table-cell>\n");
            var1.append("\t\t\t\t\t\t<fo:table-cell>\n");
            var1.append("\t\t\t\t\t\t\t<fo:block>\n");
            var1.append("\t\t\t\t\t\t\t\t<fo:external-graphic src=\"" + ReportUtils.logo() + "\" />\n");
            var1.append("\t\t\t\t\t\t\t</fo:block>\n");
            var1.append("\t\t\t\t\t\t</fo:table-cell>\n");
            var1.append("\t\t\t\t\t\t<fo:table-cell><fo:block></fo:block></fo:table-cell>\n");
            var1.append("\t\t\t\t\t</fo:table-row>\n");
            var1.append("\t\t\t\t</fo:table-body>\n");
            var1.append("\t\t\t</fo:table>\n");
            var1.append("\t\t</fo:block>\n");
            var1.append("\t</fo:static-content>\n");
         } else if (this.type == 1) {
            var1.append("<fo:page-sequence master-reference=\"rest\">\n");
            var1.append("<fo:static-content flow-name=\"xsl-region-before\" color=\"black\">\n");
            var1.append("\t<fo:block border-bottom=\"2pt solid black\" font-family=\"sans-serif\">\n");
            var1.append("\t\t\t" + Content.fixText(this.title) + "\n");
            var1.append("\t</fo:block>");
            var1.append("</fo:static-content>");
            var1.append(CommonUtils.readResourceAsString("resources/fso/page_footer.fso"));
         } else if (this.type == 3) {
            var1.append("<fo:page-sequence master-reference=\"first\">\n");
            var1.append("\t<fo:static-content flow-name=\"xsl-region-before\" color=\"black\">\n");
            var1.append("\t\t<fo:block>\n");
            var1.append("\t\t\t<fo:table>");
            var1.append("\t\t\t\t" + ReportUtils.ColumnWidth("1.1in") + "\n");
            var1.append("\t\t\t\t" + ReportUtils.ColumnWidth("4.0in") + "\n");
            var1.append("\t\t\t\t" + ReportUtils.ColumnWidth("1.1in") + "\n");
            var1.append("\t\t\t\t<fo:table-body>\n");
            var1.append("\t\t\t\t\t<fo:table-row>\n");
            var1.append("\t\t\t\t\t\t<fo:table-cell><fo:block></fo:block></fo:table-cell>\n");
            var1.append("\t\t\t\t\t\t<fo:table-cell>\n");
            var1.append("\t\t\t\t\t\t\t<fo:block>\n");
            var1.append("\t\t\t\t\t\t\t\t<fo:external-graphic src=\"" + ReportUtils.logo() + "\" />\n");
            var1.append("\t\t\t\t\t\t\t</fo:block>\n");
            var1.append("\t\t\t\t\t\t</fo:table-cell>\n");
            var1.append("\t\t\t\t\t\t<fo:table-cell><fo:block></fo:block></fo:table-cell>\n");
            var1.append("\t\t\t\t\t</fo:table-row>\n");
            var1.append("\t\t\t\t</fo:table-body>\n");
            var1.append("\t\t\t</fo:table>\n");
            var1.append("\t\t</fo:block>\n");
            var1.append("</fo:static-content>");
         }

         var1.append("\t\t<fo:flow flow-name=\"xsl-region-body\">");
         super.publish(var1);
         var1.append("\t</fo:flow>\n</fo:page-sequence>");
      }
   }
}

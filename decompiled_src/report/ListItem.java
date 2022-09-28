package report;

public class ListItem extends Content {
   public ListItem(Document var1) {
      super(var1);
   }

   public void publish(StringBuffer var1) {
      var1.append("<fo:list-item>\n");
      var1.append("\t<fo:list-item-label end-indent=\"label-end()\">\n");
      var1.append("\t\t<fo:block font-family=\"sans-serif\">&#x2022;</fo:block>\n");
      var1.append("\t</fo:list-item-label>\n");
      var1.append("\t<fo:list-item-body start-indent=\"body-start()\">\n");
      var1.append("\t\t<fo:block margin-left=\"0.05in\" font-family=\"sans-serif\">");
      super.publish(var1);
      var1.append("\t\t</fo:block>");
      var1.append("\t</fo:list-item-body>");
      var1.append("</fo:list-item>");
   }
}

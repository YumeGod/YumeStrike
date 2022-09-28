package report;

public class NoBreak extends Content {
   public NoBreak(Document var1) {
      super(var1);
   }

   public void publish(StringBuffer var1) {
      var1.append("<fo:block page-break-inside=\"avoid\" padding=\"0\" margin=\"0\">");
      super.publish(var1);
      var1.append("</fo:block>");
   }
}

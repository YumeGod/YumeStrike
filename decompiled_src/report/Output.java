package report;

public class Output extends Content {
   protected String width;

   public Output(Document var1, String var2) {
      super(var1);
      this.width = var2;
   }

   public void publish(StringBuffer var1) {
      var1.append("<fo:block background-color=\"#eeeeee\" content-width=\"" + this.width + "\" linefeed-treatment=\"preserve\" padding=\"8pt\">");
      super.publish(var1);
      var1.append("</fo:block>");
   }
}

package report;

public class FoBlock extends Content {
   protected String align;

   public FoBlock(Document var1, String var2) {
      super(var1);
      this.align = var2;
   }

   public void publish(StringBuffer var1) {
      var1.append("<fo:block linefeed-treatment=\"preserve\" text-align=\"" + this.align + "\">");
      super.publish(var1);
      var1.append("</fo:block>");
   }
}

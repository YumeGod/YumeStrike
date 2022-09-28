package report;

public class Piece implements ReportElement {
   protected String text;

   public Piece(String var1) {
      this.text = var1;
   }

   public void publish(StringBuffer var1) {
      var1.append(this.text);
   }
}

package report;

public class UnorderedList extends Content {
   public UnorderedList(Document var1) {
      super(var1);
   }

   public void publish(StringBuffer var1) {
      var1.append("<fo:list-block provisional-distance-between-starts=\"0.2cm\" provisional-label-separation=\"0.5cm\" padding-top=\"6pt\" space-after=\"12pt\" start-indent=\"1cm\">");
      super.publish(var1);
      var1.append("</fo:list-block>");
   }
}

package org.apache.xmlgraphics.ps.dsc.events;

import java.io.IOException;
import org.apache.xmlgraphics.ps.PSGenerator;

public class DSCHeaderComment extends AbstractEvent {
   private String comment;

   public DSCHeaderComment(String comment) {
      this.comment = comment;
   }

   public String getComment() {
      return this.comment;
   }

   public boolean isPSAdobe30() {
      return this.getComment().startsWith("%!PS-Adobe-3.0".substring(2));
   }

   public void generate(PSGenerator gen) throws IOException {
      gen.writeln("%!" + this.getComment());
   }

   public int getEventType() {
      return 0;
   }

   public boolean isHeaderComment() {
      return true;
   }
}

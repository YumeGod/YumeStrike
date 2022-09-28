package org.apache.xmlgraphics.ps.dsc.events;

import java.io.IOException;
import org.apache.xmlgraphics.ps.PSGenerator;

public class PostScriptComment extends AbstractEvent {
   private String comment;

   public PostScriptComment(String comment) {
      if (comment != null && comment.startsWith("%")) {
         this.comment = this.comment.substring(1);
      } else {
         this.comment = comment;
      }

   }

   public String getComment() {
      return this.comment;
   }

   public void generate(PSGenerator gen) throws IOException {
      gen.commentln("%" + this.getComment());
   }

   public int getEventType() {
      return 2;
   }

   public boolean isComment() {
      return true;
   }
}

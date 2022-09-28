package org.apache.xmlgraphics.ps.dsc.events;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.xmlgraphics.ps.PSGenerator;

public class DSCCommentTitle extends AbstractDSCComment {
   private String title;

   public DSCCommentTitle() {
   }

   public DSCCommentTitle(String title) {
      this.title = title;
   }

   public String getTitle() {
      return this.title;
   }

   public String getName() {
      return "Title";
   }

   public boolean hasValues() {
      return true;
   }

   public void parseValue(String value) {
      List params = this.splitParams(value);
      Iterator iter = params.iterator();
      this.title = (String)iter.next();
   }

   public void generate(PSGenerator gen) throws IOException {
      gen.writeDSCComment(this.getName(), (Object)this.getTitle());
   }
}

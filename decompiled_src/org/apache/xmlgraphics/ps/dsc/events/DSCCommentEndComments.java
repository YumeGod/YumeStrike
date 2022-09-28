package org.apache.xmlgraphics.ps.dsc.events;

import java.io.IOException;
import org.apache.xmlgraphics.ps.PSGenerator;

public class DSCCommentEndComments extends AbstractDSCComment {
   public String getName() {
      return "EndComments";
   }

   public boolean hasValues() {
      return false;
   }

   public void parseValue(String value) {
   }

   public void generate(PSGenerator gen) throws IOException {
      gen.writeDSCComment(this.getName());
   }
}

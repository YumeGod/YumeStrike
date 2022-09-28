package org.apache.xmlgraphics.ps.dsc.events;

import java.io.IOException;
import org.apache.xmlgraphics.ps.DSCConstants;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.apache.xmlgraphics.ps.dsc.DSCCommentFactory;

public class DSCAtend extends AbstractDSCComment {
   private String name;

   public DSCAtend(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public boolean hasValues() {
      return false;
   }

   public boolean isAtend() {
      return true;
   }

   public void parseValue(String value) {
   }

   public void generate(PSGenerator gen) throws IOException {
      gen.writeDSCComment(this.getName(), DSCConstants.ATEND);
   }

   public DSCComment createDSCCommentFromAtend() {
      return DSCCommentFactory.createDSCCommentFor(this.getName());
   }
}

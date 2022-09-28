package org.apache.xmlgraphics.ps.dsc.events;

import java.io.IOException;
import org.apache.xmlgraphics.ps.PSGenerator;

public class UnparsedDSCComment extends AbstractEvent implements DSCComment {
   private String name;
   private String value;

   public UnparsedDSCComment(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public boolean hasValues() {
      return this.value != null;
   }

   public boolean isAtend() {
      return false;
   }

   public void parseValue(String value) {
      this.value = value;
   }

   public void generate(PSGenerator gen) throws IOException {
      gen.writeln("%%" + this.name + (this.hasValues() ? ": " + this.value : ""));
   }

   public boolean isDSCComment() {
      return true;
   }

   public int getEventType() {
      return 1;
   }

   public DSCComment asDSCComment() {
      return this;
   }
}

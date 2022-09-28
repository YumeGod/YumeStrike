package org.apache.xmlgraphics.ps.dsc.events;

import java.io.IOException;
import org.apache.xmlgraphics.ps.PSGenerator;

public class PostScriptLine extends AbstractEvent {
   private String line;

   public PostScriptLine(String line) {
      this.line = line;
   }

   public String getLine() {
      return this.line;
   }

   public void generate(PSGenerator gen) throws IOException {
      gen.writeln(this.getLine());
   }

   public int getEventType() {
      return 3;
   }

   public PostScriptLine asLine() {
      return this;
   }

   public boolean isLine() {
      return true;
   }
}

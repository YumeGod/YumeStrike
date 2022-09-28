package org.apache.xmlgraphics.ps.dsc;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.apache.xmlgraphics.ps.dsc.events.DSCComment;

public class DefaultDSCHandler implements DSCHandler {
   protected OutputStream out;
   protected PSGenerator gen;

   public DefaultDSCHandler(OutputStream out) {
      this.out = out;
      this.gen = new PSGenerator(this.out);
   }

   public void startDocument(String header) throws IOException {
      this.gen.writeln(header);
   }

   public void endDocument() throws IOException {
      this.gen.writeDSCComment("EOF");
   }

   public void handleDSCComment(DSCComment comment) throws IOException {
      comment.generate(this.gen);
   }

   public void line(String line) throws IOException {
      this.gen.writeln(line);
   }

   public void comment(String comment) throws IOException {
      this.gen.commentln("%" + comment);
   }
}

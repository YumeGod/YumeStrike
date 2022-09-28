package org.apache.xmlgraphics.ps.dsc;

import java.io.IOException;
import org.apache.xmlgraphics.ps.dsc.events.DSCComment;

public interface DSCHandler {
   void startDocument(String var1) throws IOException;

   void endDocument() throws IOException;

   void handleDSCComment(DSCComment var1) throws IOException;

   void line(String var1) throws IOException;

   void comment(String var1) throws IOException;
}

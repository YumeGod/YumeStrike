package org.apache.xmlgraphics.ps.dsc.events;

import java.io.IOException;
import org.apache.xmlgraphics.ps.PSGenerator;

public interface DSCComment extends DSCEvent {
   String getName();

   void parseValue(String var1);

   boolean hasValues();

   boolean isAtend();

   void generate(PSGenerator var1) throws IOException;
}

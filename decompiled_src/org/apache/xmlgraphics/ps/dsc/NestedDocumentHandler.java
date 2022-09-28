package org.apache.xmlgraphics.ps.dsc;

import java.io.IOException;
import org.apache.xmlgraphics.ps.dsc.events.DSCEvent;

public interface NestedDocumentHandler {
   void handle(DSCEvent var1, DSCParser var2) throws IOException, DSCException;
}

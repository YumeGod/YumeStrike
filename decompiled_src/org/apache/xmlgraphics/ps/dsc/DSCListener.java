package org.apache.xmlgraphics.ps.dsc;

import java.io.IOException;
import org.apache.xmlgraphics.ps.dsc.events.DSCEvent;

public interface DSCListener {
   void processEvent(DSCEvent var1, DSCParser var2) throws IOException, DSCException;
}

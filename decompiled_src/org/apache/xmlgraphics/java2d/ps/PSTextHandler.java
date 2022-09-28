package org.apache.xmlgraphics.java2d.ps;

import java.io.IOException;
import org.apache.xmlgraphics.java2d.TextHandler;

public interface PSTextHandler extends TextHandler {
   void writeSetup() throws IOException;

   void writePageSetup() throws IOException;
}

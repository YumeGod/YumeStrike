package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;

public interface IRtfTextContainer {
   RtfText newText(String var1, RtfAttributes var2) throws IOException;

   RtfText newText(String var1) throws IOException;

   void newLineBreak() throws IOException;

   RtfAttributes getTextContainerAttributes();
}

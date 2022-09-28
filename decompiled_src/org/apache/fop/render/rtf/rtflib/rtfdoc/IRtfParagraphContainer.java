package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;

public interface IRtfParagraphContainer {
   RtfParagraph newParagraph() throws IOException;

   RtfParagraph newParagraph(RtfAttributes var1) throws IOException;
}

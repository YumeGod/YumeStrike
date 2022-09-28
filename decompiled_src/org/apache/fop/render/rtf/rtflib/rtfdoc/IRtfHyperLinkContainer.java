package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;

public interface IRtfHyperLinkContainer extends IRtfTextContainer {
   RtfHyperLink newHyperLink(String var1, RtfAttributes var2) throws IOException;
}

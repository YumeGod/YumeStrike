package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;

public interface IRtfPageNumberContainer {
   RtfPageNumber newPageNumber() throws IOException;
}

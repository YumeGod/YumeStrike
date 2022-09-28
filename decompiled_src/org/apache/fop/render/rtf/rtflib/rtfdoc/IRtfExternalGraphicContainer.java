package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;

public interface IRtfExternalGraphicContainer {
   RtfExternalGraphic newImage() throws IOException;
}

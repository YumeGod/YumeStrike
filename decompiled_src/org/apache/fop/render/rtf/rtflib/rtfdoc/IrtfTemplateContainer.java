package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;

public interface IrtfTemplateContainer {
   RtfTemplate newTemplate(String var1, RtfAttributes var2) throws IOException;
}

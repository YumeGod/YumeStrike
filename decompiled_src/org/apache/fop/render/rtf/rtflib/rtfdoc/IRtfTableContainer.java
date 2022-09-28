package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;

public interface IRtfTableContainer {
   RtfTable newTable(ITableColumnsInfo var1) throws IOException;

   RtfTable newTable(RtfAttributes var1, ITableColumnsInfo var2) throws IOException;
}

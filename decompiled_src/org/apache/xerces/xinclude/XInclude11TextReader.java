package org.apache.xerces.xinclude;

import java.io.IOException;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XInclude11TextReader extends XIncludeTextReader {
   public XInclude11TextReader(XMLInputSource var1, XIncludeHandler var2, int var3) throws IOException {
      super(var1, var2, var3);
   }

   protected boolean isValid(int var1) {
      return XML11Char.isXML11Valid(var1);
   }
}

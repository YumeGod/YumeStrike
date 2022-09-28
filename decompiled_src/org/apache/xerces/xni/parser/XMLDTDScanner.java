package org.apache.xerces.xni.parser;

import java.io.IOException;
import org.apache.xerces.xni.XNIException;

public interface XMLDTDScanner extends XMLDTDSource, XMLDTDContentModelSource {
   void setInputSource(XMLInputSource var1) throws IOException;

   boolean scanDTDInternalSubset(boolean var1, boolean var2, boolean var3) throws IOException, XNIException;

   boolean scanDTDExternalSubset(boolean var1) throws IOException, XNIException;
}

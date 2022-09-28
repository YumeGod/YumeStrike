package org.apache.xerces.dom3.as;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

/** @deprecated */
public interface DOMASBuilder extends LSParser {
   ASModel getAbstractSchema();

   void setAbstractSchema(ASModel var1);

   ASModel parseASURI(String var1) throws DOMASException, Exception;

   ASModel parseASInputSource(LSInput var1) throws DOMASException, Exception;
}

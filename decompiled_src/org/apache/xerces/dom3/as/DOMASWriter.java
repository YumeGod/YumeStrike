package org.apache.xerces.dom3.as;

import java.io.OutputStream;
import org.w3c.dom.ls.LSSerializer;

/** @deprecated */
public interface DOMASWriter extends LSSerializer {
   void writeASModel(OutputStream var1, ASModel var2) throws Exception;
}

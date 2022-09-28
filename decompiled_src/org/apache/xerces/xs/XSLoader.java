package org.apache.xerces.xs;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.ls.LSInput;

public interface XSLoader {
   DOMConfiguration getConfig();

   XSModel loadURIList(StringList var1);

   XSModel loadInputList(LSInputList var1);

   XSModel loadURI(String var1);

   XSModel load(LSInput var1);
}

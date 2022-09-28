package org.w3c.dom.ls;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public interface LSSerializer {
   DOMConfiguration getDomConfig();

   String getNewLine();

   void setNewLine(String var1);

   LSSerializerFilter getFilter();

   void setFilter(LSSerializerFilter var1);

   boolean write(Node var1, LSOutput var2) throws LSException;

   boolean writeToURI(Node var1, String var2) throws LSException;

   String writeToString(Node var1) throws DOMException, LSException;
}

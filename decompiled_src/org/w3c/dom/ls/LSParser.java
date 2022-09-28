package org.w3c.dom.ls;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface LSParser {
   short ACTION_APPEND_AS_CHILDREN = 1;
   short ACTION_REPLACE_CHILDREN = 2;
   short ACTION_INSERT_BEFORE = 3;
   short ACTION_INSERT_AFTER = 4;
   short ACTION_REPLACE = 5;

   DOMConfiguration getDomConfig();

   LSParserFilter getFilter();

   void setFilter(LSParserFilter var1);

   boolean getAsync();

   boolean getBusy();

   Document parse(LSInput var1) throws DOMException, LSException;

   Document parseURI(String var1) throws DOMException, LSException;

   Node parseWithContext(LSInput var1, Node var2, short var3) throws DOMException, LSException;

   void abort();
}

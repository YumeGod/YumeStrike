package org.w3c.dom.ls;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface LSParserFilter {
   short FILTER_ACCEPT = 1;
   short FILTER_REJECT = 2;
   short FILTER_SKIP = 3;
   short FILTER_INTERRUPT = 4;

   short startElement(Element var1);

   short acceptNode(Node var1);

   int getWhatToShow();
}

package org.w3c.dom;

public interface ElementTraversal {
   Element getFirstElementChild();

   Element getLastElementChild();

   Element getNextElementSibling();

   Element getPreviousElementSibling();

   int getChildElementCount();
}

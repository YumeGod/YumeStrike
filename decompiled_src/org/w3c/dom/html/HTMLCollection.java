package org.w3c.dom.html;

import org.w3c.dom.Node;

public interface HTMLCollection {
   int getLength();

   Node item(int var1);

   Node namedItem(String var1);
}

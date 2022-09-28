package org.apache.batik.gvt.text;

import org.apache.batik.gvt.TextNode;

public interface Mark {
   TextNode getTextNode();

   int getCharIndex();
}

package org.apache.batik.dom.svg;

import org.w3c.dom.Attr;

public interface LiveAttributeValue {
   void attrAdded(Attr var1, String var2);

   void attrModified(Attr var1, String var2, String var3);

   void attrRemoved(Attr var1, String var2);
}

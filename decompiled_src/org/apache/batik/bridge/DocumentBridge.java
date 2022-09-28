package org.apache.batik.bridge;

import org.apache.batik.gvt.RootGraphicsNode;
import org.w3c.dom.Document;

public interface DocumentBridge extends Bridge {
   RootGraphicsNode createGraphicsNode(BridgeContext var1, Document var2);

   void buildGraphicsNode(BridgeContext var1, Document var2, RootGraphicsNode var3);
}

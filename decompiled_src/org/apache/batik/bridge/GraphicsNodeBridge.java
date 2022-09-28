package org.apache.batik.bridge;

import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;

public interface GraphicsNodeBridge extends Bridge {
   GraphicsNode createGraphicsNode(BridgeContext var1, Element var2);

   void buildGraphicsNode(BridgeContext var1, Element var2, GraphicsNode var3);

   boolean isComposite();

   boolean getDisplay(Element var1);

   Bridge getInstance();
}

package org.apache.batik.bridge;

import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;

public interface FilterBridge extends Bridge {
   Filter createFilter(BridgeContext var1, Element var2, Element var3, GraphicsNode var4);
}

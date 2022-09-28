package org.apache.batik.bridge;

import java.awt.Paint;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;

public interface PaintBridge extends Bridge {
   Paint createPaint(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, float var5);
}

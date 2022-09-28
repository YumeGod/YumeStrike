package org.apache.xmlgraphics.java2d;

import java.awt.Graphics2D;
import java.io.IOException;

public interface TextHandler {
   void drawString(Graphics2D var1, String var2, float var3, float var4) throws IOException;

   /** @deprecated */
   void drawString(String var1, float var2, float var3) throws IOException;
}

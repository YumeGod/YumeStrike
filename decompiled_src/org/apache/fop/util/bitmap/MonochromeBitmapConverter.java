package org.apache.fop.util.bitmap;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

public interface MonochromeBitmapConverter {
   void setHint(String var1, String var2);

   RenderedImage convertToMonochrome(BufferedImage var1);
}

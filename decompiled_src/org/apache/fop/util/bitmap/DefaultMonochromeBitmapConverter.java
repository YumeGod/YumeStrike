package org.apache.fop.util.bitmap;

import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.RenderedImage;
import java.util.Map;

public class DefaultMonochromeBitmapConverter implements MonochromeBitmapConverter {
   public void setHint(String name, String value) {
   }

   public RenderedImage convertToMonochrome(BufferedImage img) {
      BufferedImage buf = new BufferedImage(img.getWidth(), img.getHeight(), 12);
      RenderingHints hints = new RenderingHints((Map)null);
      hints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
      ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(1003), hints);
      op.filter(img, buf);
      return buf;
   }
}

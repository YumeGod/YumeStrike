package org.apache.fop.render.java2d;

import java.awt.Graphics2D;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.render.AbstractRenderingContext;

public class Java2DRenderingContext extends AbstractRenderingContext {
   private FontInfo fontInfo;
   private Graphics2D g2d;

   public Java2DRenderingContext(FOUserAgent userAgent, Graphics2D g2d, FontInfo fontInfo) {
      super(userAgent);
      this.g2d = g2d;
      this.fontInfo = fontInfo;
   }

   public String getMimeType() {
      return null;
   }

   public Graphics2D getGraphics2D() {
      return this.g2d;
   }
}

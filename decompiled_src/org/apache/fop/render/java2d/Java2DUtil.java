package org.apache.fop.render.java2d;

import java.awt.Graphics2D;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fonts.FontCollection;
import org.apache.fop.fonts.FontEventAdapter;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontManager;

public class Java2DUtil {
   public static FontInfo buildDefaultJava2DBasedFontInfo(FontInfo fontInfo, FOUserAgent userAgent) {
      Graphics2D graphics2D = Java2DFontMetrics.createFontMetricsGraphics2D();
      FontManager fontManager = userAgent.getFactory().getFontManager();
      FontCollection[] fontCollections = new FontCollection[]{new Base14FontCollection(graphics2D), new InstalledFontCollection(graphics2D)};
      FontInfo fi = fontInfo != null ? fontInfo : new FontInfo();
      fi.setEventListener(new FontEventAdapter(userAgent.getEventBroadcaster()));
      fontManager.setup(fi, fontCollections);
      return fi;
   }
}

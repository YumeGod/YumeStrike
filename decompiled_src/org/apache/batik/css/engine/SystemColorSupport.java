package org.apache.batik.css.engine;

import java.awt.SystemColor;
import java.util.HashMap;
import java.util.Map;
import org.apache.batik.css.engine.value.FloatValue;
import org.apache.batik.css.engine.value.RGBColorValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.util.CSSConstants;

public class SystemColorSupport implements CSSConstants {
   protected static final Map factories = new HashMap();

   public static Value getSystemColor(String var0) {
      var0 = var0.toLowerCase();
      SystemColor var1 = (SystemColor)factories.get(var0);
      return new RGBColorValue(new FloatValue((short)1, (float)var1.getRed()), new FloatValue((short)1, (float)var1.getGreen()), new FloatValue((short)1, (float)var1.getBlue()));
   }

   protected SystemColorSupport() {
   }

   static {
      factories.put("activeborder", SystemColor.windowBorder);
      factories.put("activecaption", SystemColor.activeCaption);
      factories.put("appworkspace", SystemColor.desktop);
      factories.put("background", SystemColor.desktop);
      factories.put("buttonface", SystemColor.control);
      factories.put("buttonhighlight", SystemColor.controlLtHighlight);
      factories.put("buttonshadow", SystemColor.controlDkShadow);
      factories.put("buttontext", SystemColor.controlText);
      factories.put("captiontext", SystemColor.activeCaptionText);
      factories.put("graytext", SystemColor.textInactiveText);
      factories.put("highlight", SystemColor.textHighlight);
      factories.put("highlighttext", SystemColor.textHighlightText);
      factories.put("inactiveborder", SystemColor.windowBorder);
      factories.put("inactivecaption", SystemColor.inactiveCaption);
      factories.put("inactivecaptiontext", SystemColor.inactiveCaptionText);
      factories.put("infobackground", SystemColor.info);
      factories.put("infotext", SystemColor.infoText);
      factories.put("menu", SystemColor.menu);
      factories.put("menutext", SystemColor.menuText);
      factories.put("scrollbar", SystemColor.scrollbar);
      factories.put("threeddarkshadow", SystemColor.controlDkShadow);
      factories.put("threedface", SystemColor.control);
      factories.put("threedhighlight", SystemColor.controlHighlight);
      factories.put("threedlightshadow", SystemColor.controlLtHighlight);
      factories.put("threedshadow", SystemColor.controlShadow);
      factories.put("window", SystemColor.window);
      factories.put("windowframe", SystemColor.windowBorder);
      factories.put("windowtext", SystemColor.windowText);
   }
}

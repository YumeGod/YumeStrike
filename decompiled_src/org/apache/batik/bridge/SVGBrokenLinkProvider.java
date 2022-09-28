package org.apache.batik.bridge;

import java.util.HashMap;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.spi.DefaultBrokenLinkProvider;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.filter.GraphicsNodeRable8Bit;

public class SVGBrokenLinkProvider extends DefaultBrokenLinkProvider implements ErrorConstants {
   public Filter getBrokenLinkImage(Object var1, String var2, Object[] var3) {
      String var4 = formatMessage(var1, var2, var3);
      HashMap var5 = new HashMap();
      var5.put("org.apache.batik.BrokenLinkImage", var4);
      CompositeGraphicsNode var6 = new CompositeGraphicsNode();
      return new GraphicsNodeRable8Bit(var6, var5);
   }
}

package org.apache.fop.render;

import java.util.Map;
import org.apache.fop.apps.FOUserAgent;

public interface RenderingContext {
   String getMimeType();

   FOUserAgent getUserAgent();

   void putHints(Map var1);

   void putHint(Object var1, Object var2);

   Map getHints();

   Object getHint(Object var1);
}

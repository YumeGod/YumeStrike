package org.apache.fop.render;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.fop.apps.FOUserAgent;

public abstract class AbstractRenderingContext implements RenderingContext {
   private FOUserAgent userAgent;
   private Map hints;

   public AbstractRenderingContext(FOUserAgent userAgent) {
      this.userAgent = userAgent;
   }

   public FOUserAgent getUserAgent() {
      return this.userAgent;
   }

   public void putHints(Map additionalHints) {
      if (additionalHints != null) {
         if (this.hints == null) {
            this.hints = new HashMap();
         }

         this.hints.putAll(additionalHints);
      }
   }

   public void putHint(Object key, Object value) {
      this.hints.put(key, value);
   }

   public Map getHints() {
      return this.hints == null ? Collections.EMPTY_MAP : Collections.unmodifiableMap(this.hints);
   }

   public Object getHint(Object key) {
      return this.hints == null ? null : this.hints.get(key);
   }
}

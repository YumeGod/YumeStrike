package org.apache.batik.dom.events;

import org.w3c.dom.events.CustomEvent;

public class DOMCustomEvent extends DOMEvent implements CustomEvent {
   protected Object detail;

   public Object getDetail() {
      return this.detail;
   }

   public void initCustomEventNS(String var1, String var2, boolean var3, boolean var4, Object var5) {
      this.initEventNS(var1, var2, var3, var4);
      this.detail = var5;
   }
}

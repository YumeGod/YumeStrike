package org.apache.batik.dom.events;

import org.w3c.dom.events.TextEvent;
import org.w3c.dom.views.AbstractView;

public class DOMTextEvent extends DOMUIEvent implements TextEvent {
   protected String data;

   public String getData() {
      return this.data;
   }

   public void initTextEvent(String var1, boolean var2, boolean var3, AbstractView var4, String var5) {
      this.initUIEvent(var1, var2, var3, var4, 0);
      this.data = var5;
   }

   public void initTextEventNS(String var1, String var2, boolean var3, boolean var4, AbstractView var5, String var6) {
      this.initUIEventNS(var1, var2, var3, var4, var5, 0);
      this.data = var6;
   }
}

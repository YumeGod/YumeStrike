package org.apache.batik.dom.svg12;

import org.apache.batik.dom.events.DOMUIEvent;
import org.w3c.dom.views.AbstractView;

public class SVGOMWheelEvent extends DOMUIEvent {
   protected int wheelDelta;

   public int getWheelDelta() {
      return this.wheelDelta;
   }

   public void initWheelEvent(String var1, boolean var2, boolean var3, AbstractView var4, int var5) {
      this.initUIEvent(var1, var2, var3, var4, 0);
      this.wheelDelta = var5;
   }

   public void initWheelEventNS(String var1, String var2, boolean var3, boolean var4, AbstractView var5, int var6) {
      this.initUIEventNS(var1, var2, var3, var4, var5, 0);
      this.wheelDelta = var6;
   }
}

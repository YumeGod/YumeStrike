package org.apache.batik.dom.events;

import org.w3c.dom.smil.TimeEvent;
import org.w3c.dom.views.AbstractView;

public class DOMTimeEvent extends AbstractEvent implements TimeEvent {
   protected AbstractView view;
   protected int detail;

   public AbstractView getView() {
      return this.view;
   }

   public int getDetail() {
      return this.detail;
   }

   public void initTimeEvent(String var1, AbstractView var2, int var3) {
      this.initEvent(var1, false, false);
      this.view = var2;
      this.detail = var3;
   }

   public void initTimeEventNS(String var1, String var2, AbstractView var3, int var4) {
      this.initEventNS(var1, var2, false, false);
      this.view = var3;
      this.detail = var4;
   }

   public void setTimestamp(long var1) {
      this.timeStamp = var1;
   }
}

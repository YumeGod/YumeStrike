package org.w3c.dom.events;

import org.w3c.dom.views.AbstractView;

public interface TextEvent extends UIEvent {
   String getData();

   void initTextEvent(String var1, boolean var2, boolean var3, AbstractView var4, String var5);

   void initTextEventNS(String var1, String var2, boolean var3, boolean var4, AbstractView var5, String var6);
}

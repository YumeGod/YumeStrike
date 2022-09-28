package org.w3c.dom.events;

import org.w3c.dom.views.AbstractView;

public interface KeyboardEvent extends UIEvent {
   int DOM_KEY_LOCATION_STANDARD = 0;
   int DOM_KEY_LOCATION_LEFT = 1;
   int DOM_KEY_LOCATION_RIGHT = 2;
   int DOM_KEY_LOCATION_NUMPAD = 3;

   String getKeyIdentifier();

   int getKeyLocation();

   boolean getCtrlKey();

   boolean getShiftKey();

   boolean getAltKey();

   boolean getMetaKey();

   boolean getModifierState(String var1);

   void initKeyboardEvent(String var1, boolean var2, boolean var3, AbstractView var4, String var5, int var6, String var7);

   void initKeyboardEventNS(String var1, String var2, boolean var3, boolean var4, AbstractView var5, String var6, int var7, String var8);
}

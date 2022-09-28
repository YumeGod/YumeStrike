package org.apache.batik.dom.xbl;

import org.w3c.dom.events.Event;

public interface ShadowTreeEvent extends Event {
   XBLShadowTreeElement getXblShadowTree();

   void initShadowTreeEvent(String var1, boolean var2, boolean var3, XBLShadowTreeElement var4);

   void initShadowTreeEventNS(String var1, String var2, boolean var3, boolean var4, XBLShadowTreeElement var5);
}

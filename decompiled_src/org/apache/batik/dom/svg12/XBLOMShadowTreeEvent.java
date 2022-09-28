package org.apache.batik.dom.svg12;

import org.apache.batik.dom.events.AbstractEvent;
import org.apache.batik.dom.xbl.ShadowTreeEvent;
import org.apache.batik.dom.xbl.XBLShadowTreeElement;

public class XBLOMShadowTreeEvent extends AbstractEvent implements ShadowTreeEvent {
   protected XBLShadowTreeElement xblShadowTree;

   public XBLShadowTreeElement getXblShadowTree() {
      return this.xblShadowTree;
   }

   public void initShadowTreeEvent(String var1, boolean var2, boolean var3, XBLShadowTreeElement var4) {
      this.initEvent(var1, var2, var3);
      this.xblShadowTree = var4;
   }

   public void initShadowTreeEventNS(String var1, String var2, boolean var3, boolean var4, XBLShadowTreeElement var5) {
      this.initEventNS(var1, var2, var3, var4);
      this.xblShadowTree = var5;
   }
}

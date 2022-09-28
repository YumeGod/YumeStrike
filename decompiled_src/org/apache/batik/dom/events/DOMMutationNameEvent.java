package org.apache.batik.dom.events;

import org.w3c.dom.Node;
import org.w3c.dom.events.MutationNameEvent;

public class DOMMutationNameEvent extends DOMMutationEvent implements MutationNameEvent {
   protected String prevNamespaceURI;
   protected String prevNodeName;

   public void initMutationNameEvent(String var1, boolean var2, boolean var3, Node var4, String var5, String var6) {
      this.initMutationEvent(var1, var2, var3, var4, (String)null, (String)null, (String)null, (short)0);
      this.prevNamespaceURI = var5;
      this.prevNodeName = var6;
   }

   public void initMutationNameEventNS(String var1, String var2, boolean var3, boolean var4, Node var5, String var6, String var7) {
      this.initMutationEventNS(var1, var2, var3, var4, var5, (String)null, (String)null, (String)null, (short)0);
      this.prevNamespaceURI = var6;
      this.prevNodeName = var7;
   }

   public String getPrevNamespaceURI() {
      return this.prevNamespaceURI;
   }

   public String getPrevNodeName() {
      return this.prevNodeName;
   }
}

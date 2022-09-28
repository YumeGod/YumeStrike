package org.apache.batik.dom.events;

import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;

public class DOMMutationEvent extends AbstractEvent implements MutationEvent {
   private Node relatedNode;
   private String prevValue;
   private String newValue;
   private String attrName;
   private short attrChange;

   public Node getRelatedNode() {
      return this.relatedNode;
   }

   public String getPrevValue() {
      return this.prevValue;
   }

   public String getNewValue() {
      return this.newValue;
   }

   public String getAttrName() {
      return this.attrName;
   }

   public short getAttrChange() {
      return this.attrChange;
   }

   public void initMutationEvent(String var1, boolean var2, boolean var3, Node var4, String var5, String var6, String var7, short var8) {
      this.initEvent(var1, var2, var3);
      this.relatedNode = var4;
      this.prevValue = var5;
      this.newValue = var6;
      this.attrName = var7;
      this.attrChange = var8;
   }

   public void initMutationEventNS(String var1, String var2, boolean var3, boolean var4, Node var5, String var6, String var7, String var8, short var9) {
      this.initEventNS(var1, var2, var3, var4);
      this.relatedNode = var5;
      this.prevValue = var6;
      this.newValue = var7;
      this.attrName = var8;
      this.attrChange = var9;
   }
}

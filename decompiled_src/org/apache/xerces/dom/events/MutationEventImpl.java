package org.apache.xerces.dom.events;

import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;

public class MutationEventImpl extends EventImpl implements MutationEvent {
   Node relatedNode = null;
   String prevValue = null;
   String newValue = null;
   String attrName = null;
   public short attrChange;
   public static final String DOM_SUBTREE_MODIFIED = "DOMSubtreeModified";
   public static final String DOM_NODE_INSERTED = "DOMNodeInserted";
   public static final String DOM_NODE_REMOVED = "DOMNodeRemoved";
   public static final String DOM_NODE_REMOVED_FROM_DOCUMENT = "DOMNodeRemovedFromDocument";
   public static final String DOM_NODE_INSERTED_INTO_DOCUMENT = "DOMNodeInsertedIntoDocument";
   public static final String DOM_ATTR_MODIFIED = "DOMAttrModified";
   public static final String DOM_CHARACTER_DATA_MODIFIED = "DOMCharacterDataModified";

   public String getAttrName() {
      return this.attrName;
   }

   public short getAttrChange() {
      return this.attrChange;
   }

   public String getNewValue() {
      return this.newValue;
   }

   public String getPrevValue() {
      return this.prevValue;
   }

   public Node getRelatedNode() {
      return this.relatedNode;
   }

   public void initMutationEvent(String var1, boolean var2, boolean var3, Node var4, String var5, String var6, String var7, short var8) {
      this.relatedNode = var4;
      this.prevValue = var5;
      this.newValue = var6;
      this.attrName = var7;
      this.attrChange = var8;
      super.initEvent(var1, var2, var3);
   }
}

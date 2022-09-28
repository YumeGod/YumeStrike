package org.w3c.dom.events;

import org.w3c.dom.Node;

public interface MutationEvent extends Event {
   short MODIFICATION = 1;
   short ADDITION = 2;
   short REMOVAL = 3;

   Node getRelatedNode();

   String getPrevValue();

   String getNewValue();

   String getAttrName();

   short getAttrChange();

   void initMutationEvent(String var1, boolean var2, boolean var3, Node var4, String var5, String var6, String var7, short var8);
}

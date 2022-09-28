package org.w3c.dom;

public interface UserDataHandler {
   short NODE_CLONED = 1;
   short NODE_IMPORTED = 2;
   short NODE_DELETED = 3;
   short NODE_RENAMED = 4;
   short NODE_ADOPTED = 5;

   void handle(short var1, String var2, Object var3, Node var4, Node var5);
}

package org.apache.xerces.xni;

import java.util.Enumeration;

public interface Augmentations {
   Object putItem(String var1, Object var2);

   Object getItem(String var1);

   Object removeItem(String var1);

   Enumeration keys();

   void removeAllItems();
}

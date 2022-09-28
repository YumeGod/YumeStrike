package org.apache.avalon.framework.component;

public interface ComponentManager {
   Component lookup(String var1) throws ComponentException;

   boolean hasComponent(String var1);

   void release(Component var1);
}

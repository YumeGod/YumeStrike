package org.apache.avalon.framework.component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultComponentManager implements ComponentManager {
   private final HashMap m_components;
   private final ComponentManager m_parent;
   private boolean m_readOnly;

   public DefaultComponentManager() {
      this((ComponentManager)null);
   }

   public DefaultComponentManager(ComponentManager parent) {
      this.m_components = new HashMap();
      this.m_parent = parent;
   }

   public Component lookup(String key) throws ComponentException {
      Component component = (Component)this.m_components.get(key);
      if (null != component) {
         return component;
      } else if (null != this.m_parent) {
         return this.m_parent.lookup(key);
      } else {
         throw new ComponentException(key, "Unable to provide implementation.");
      }
   }

   public boolean hasComponent(String key) {
      boolean componentExists = false;

      try {
         this.release(this.lookup(key));
         componentExists = true;
      } catch (Throwable var4) {
      }

      return componentExists;
   }

   public void put(String key, Component component) {
      this.checkWriteable();
      this.m_components.put(key, component);
   }

   public void release(Component component) {
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      Iterator components = this.m_components.keySet().iterator();
      buffer.append("Components:");

      while(components.hasNext()) {
         buffer.append("[");
         buffer.append(components.next());
         buffer.append("]");
      }

      return buffer.toString();
   }

   protected final ComponentManager getParent() {
      return this.m_parent;
   }

   protected final Map getComponentMap() {
      return this.m_components;
   }

   public void makeReadOnly() {
      this.m_readOnly = true;
   }

   protected final void checkWriteable() throws IllegalStateException {
      if (this.m_readOnly) {
         String message = "ComponentManager is read only and can not be modified";
         throw new IllegalStateException("ComponentManager is read only and can not be modified");
      }
   }
}

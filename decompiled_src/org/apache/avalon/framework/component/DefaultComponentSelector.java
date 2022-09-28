package org.apache.avalon.framework.component;

import java.util.HashMap;
import java.util.Map;

public class DefaultComponentSelector implements ComponentSelector {
   private final HashMap m_components = new HashMap();
   private boolean m_readOnly;

   public Component select(Object hint) throws ComponentException {
      Component component = (Component)this.m_components.get(hint);
      if (null != component) {
         return component;
      } else {
         throw new ComponentException(hint.toString(), "Unable to provide implementation.");
      }
   }

   public boolean hasComponent(Object hint) {
      boolean componentExists = false;

      try {
         this.release(this.select(hint));
         componentExists = true;
      } catch (Throwable var4) {
      }

      return componentExists;
   }

   public void release(Component component) {
   }

   public void put(Object hint, Component component) {
      this.checkWriteable();
      this.m_components.put(hint, component);
   }

   protected final Map getComponentMap() {
      return this.m_components;
   }

   public void makeReadOnly() {
      this.m_readOnly = true;
   }

   protected final void checkWriteable() throws IllegalStateException {
      if (this.m_readOnly) {
         throw new IllegalStateException("ComponentSelector is read only and can not be modified");
      }
   }
}

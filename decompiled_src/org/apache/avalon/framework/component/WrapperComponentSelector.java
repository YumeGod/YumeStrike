package org.apache.avalon.framework.component;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceSelector;

public class WrapperComponentSelector implements ComponentSelector {
   private final ServiceSelector m_selector;
   private final String m_key;

   public WrapperComponentSelector(String key, ServiceSelector selector) {
      if (null == key) {
         throw new NullPointerException("key");
      } else if (null == selector) {
         throw new NullPointerException("selector");
      } else {
         this.m_key = key + "/";
         this.m_selector = selector;
      }
   }

   public Component select(Object policy) throws ComponentException {
      try {
         Object object = this.m_selector.select(policy);
         if (object instanceof Component) {
            return (Component)object;
         }
      } catch (ServiceException var3) {
         throw new ComponentException(this.m_key + policy, var3.getMessage(), var3);
      }

      String message = "Role does not implement the Component interface and thus can not be accessed via ComponentSelector";
      throw new ComponentException(this.m_key + policy, "Role does not implement the Component interface and thus can not be accessed via ComponentSelector");
   }

   public boolean hasComponent(Object policy) {
      return this.m_selector.isSelectable(policy);
   }

   public void release(Component object) {
      this.m_selector.release(object);
   }

   ServiceSelector getWrappedSelector() {
      return this.m_selector;
   }
}

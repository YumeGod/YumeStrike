package org.apache.avalon.framework.service;

import org.apache.avalon.framework.component.Component;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentSelector;

public class WrapperServiceSelector implements ServiceSelector {
   private final ComponentSelector m_selector;
   private final String m_key;

   public WrapperServiceSelector(String key, ComponentSelector selector) {
      if (null == key) {
         throw new NullPointerException("key");
      } else if (null == selector) {
         throw new NullPointerException("selector");
      } else {
         this.m_key = key + "/";
         this.m_selector = selector;
      }
   }

   public Object select(Object policy) throws ServiceException {
      try {
         return this.m_selector.select(policy);
      } catch (ComponentException var3) {
         throw new ServiceException(this.m_key + policy, var3.getMessage(), var3);
      }
   }

   public boolean isSelectable(Object policy) {
      return this.m_selector.hasComponent(policy);
   }

   public void release(Object object) {
      this.m_selector.release((Component)object);
   }

   ComponentSelector getWrappedSelector() {
      return this.m_selector;
   }
}

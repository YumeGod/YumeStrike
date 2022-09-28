package org.apache.avalon.framework.service;

import org.apache.avalon.framework.component.Component;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentSelector;

public class WrapperServiceManager implements ServiceManager {
   private final ComponentManager m_componentManager;

   public WrapperServiceManager(ComponentManager componentManager) {
      if (null == componentManager) {
         throw new NullPointerException("componentManager");
      } else {
         this.m_componentManager = componentManager;
      }
   }

   public Object lookup(String key) throws ServiceException {
      try {
         Object service = this.m_componentManager.lookup(key);
         return service instanceof ComponentSelector ? new WrapperServiceSelector(key, (ComponentSelector)service) : service;
      } catch (ComponentException var3) {
         throw new ServiceException(key, var3.getMessage(), var3);
      }
   }

   public boolean hasService(String key) {
      return this.m_componentManager.hasComponent(key);
   }

   public void release(Object service) {
      if (service instanceof WrapperServiceSelector) {
         this.m_componentManager.release(((WrapperServiceSelector)service).getWrappedSelector());
      } else {
         this.m_componentManager.release((Component)service);
      }

   }
}

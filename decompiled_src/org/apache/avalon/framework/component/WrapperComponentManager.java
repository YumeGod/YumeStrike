package org.apache.avalon.framework.component;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceSelector;

public class WrapperComponentManager implements ComponentManager {
   private final ServiceManager m_manager;

   public WrapperComponentManager(ServiceManager manager) {
      if (null == manager) {
         throw new NullPointerException("manager");
      } else {
         this.m_manager = manager;
      }
   }

   public Component lookup(String key) throws ComponentException {
      try {
         Object object = this.m_manager.lookup(key);
         if (object instanceof ServiceSelector) {
            return new WrapperComponentSelector(key, (ServiceSelector)object);
         }

         if (object instanceof Component) {
            return (Component)object;
         }
      } catch (ServiceException var3) {
         throw new ComponentException(var3.getKey(), var3.getMessage(), var3.getCause());
      }

      String message = "Role does not implement the Component interface and thus can not be accessed via ComponentManager";
      throw new ComponentException(key, "Role does not implement the Component interface and thus can not be accessed via ComponentManager");
   }

   public boolean hasComponent(String key) {
      return this.m_manager.hasService(key);
   }

   public void release(Component component) {
      if (component instanceof WrapperComponentSelector) {
         WrapperComponentSelector selector = (WrapperComponentSelector)component;
         this.m_manager.release(selector.getWrappedSelector());
      } else {
         this.m_manager.release(component);
      }

   }
}

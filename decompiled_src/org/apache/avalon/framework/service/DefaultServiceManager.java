package org.apache.avalon.framework.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultServiceManager implements ServiceManager {
   private final HashMap m_objects;
   private final ServiceManager m_parent;
   private boolean m_readOnly;

   public DefaultServiceManager() {
      this((ServiceManager)null);
   }

   public DefaultServiceManager(ServiceManager parent) {
      this.m_objects = new HashMap();
      this.m_parent = parent;
   }

   public Object lookup(String key) throws ServiceException {
      Object object = this.m_objects.get(key);
      if (null != object) {
         return object;
      } else if (null != this.m_parent) {
         return this.m_parent.lookup(key);
      } else {
         String message = "Unable to provide implementation for " + key;
         throw new ServiceException(key, message, (Throwable)null);
      }
   }

   public boolean hasService(String key) {
      try {
         this.lookup(key);
         return true;
      } catch (Throwable var3) {
         return false;
      }
   }

   public void put(String key, Object object) {
      this.checkWriteable();
      this.m_objects.put(key, object);
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      Iterator objects = this.m_objects.keySet().iterator();
      buffer.append("Services:");

      while(objects.hasNext()) {
         buffer.append("[");
         buffer.append(objects.next());
         buffer.append("]");
      }

      return buffer.toString();
   }

   protected final ServiceManager getParent() {
      return this.m_parent;
   }

   protected final Map getObjectMap() {
      return this.m_objects;
   }

   public void makeReadOnly() {
      this.m_readOnly = true;
   }

   protected final void checkWriteable() throws IllegalStateException {
      if (this.m_readOnly) {
         String message = "ServiceManager is read only and can not be modified";
         throw new IllegalStateException("ServiceManager is read only and can not be modified");
      }
   }

   public void release(Object object) {
   }
}

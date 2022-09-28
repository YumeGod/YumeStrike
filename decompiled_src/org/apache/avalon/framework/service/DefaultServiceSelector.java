package org.apache.avalon.framework.service;

import java.util.HashMap;
import java.util.Map;

public class DefaultServiceSelector implements ServiceSelector {
   private final HashMap m_objects;
   private boolean m_readOnly;
   private final String m_role;

   public DefaultServiceSelector() {
      this("");
   }

   public DefaultServiceSelector(String role) {
      this.m_objects = new HashMap();
      if (null == role) {
         throw new NullPointerException(role);
      } else {
         this.m_role = role;
      }
   }

   public Object select(Object hint) throws ServiceException {
      Object object = this.m_objects.get(hint);
      if (null != object) {
         return object;
      } else {
         throw new ServiceException(this.m_role + "/" + hint.toString(), "Unable to provide implementation");
      }
   }

   public boolean isSelectable(Object hint) {
      boolean objectExists = false;

      try {
         this.release(this.select(hint));
         objectExists = true;
      } catch (Throwable var4) {
      }

      return objectExists;
   }

   public void release(Object object) {
   }

   public void put(Object hint, Object object) {
      this.checkWriteable();
      this.m_objects.put(hint, object);
   }

   protected final Map getObjectMap() {
      return this.m_objects;
   }

   public void makeReadOnly() {
      this.m_readOnly = true;
   }

   protected final void checkWriteable() throws IllegalStateException {
      if (this.m_readOnly) {
         throw new IllegalStateException("ServiceSelector is read only and can not be modified");
      }
   }
}

package org.apache.avalon.framework.service;

public interface ServiceSelector {
   Object select(Object var1) throws ServiceException;

   boolean isSelectable(Object var1);

   void release(Object var1);
}

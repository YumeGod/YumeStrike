package org.apache.avalon.framework.service;

public interface ServiceManager {
   Object lookup(String var1) throws ServiceException;

   boolean hasService(String var1);

   void release(Object var1);
}

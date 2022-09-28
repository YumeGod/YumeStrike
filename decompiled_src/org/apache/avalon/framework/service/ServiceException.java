package org.apache.avalon.framework.service;

import org.apache.avalon.framework.CascadingException;

public class ServiceException extends CascadingException {
   private final String m_key;

   /** @deprecated */
   public ServiceException(String message, Throwable throwable) {
      this((String)null, message, throwable);
   }

   public ServiceException(String key, String message, Throwable throwable) {
      super(message, throwable);
      this.m_key = key;
   }

   /** @deprecated */
   public ServiceException(String message) {
      this((String)null, message, (Throwable)null);
   }

   public ServiceException(String key, String message) {
      this(key, message, (Throwable)null);
   }

   public String getKey() {
      return this.m_key;
   }

   /** @deprecated */
   public String getRole() {
      return this.getKey();
   }

   public String getMessage() {
      return this.m_key == null ? super.getMessage() : super.getMessage() + " (Key='" + this.m_key + "')";
   }
}

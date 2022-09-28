package org.apache.avalon.framework.component;

import org.apache.avalon.framework.CascadingException;

public class ComponentException extends CascadingException {
   private final String m_key;

   public ComponentException(String key, String message, Throwable throwable) {
      super(message, throwable);
      this.m_key = key;
   }

   /** @deprecated */
   public ComponentException(String message, Throwable throwable) {
      this((String)null, message, throwable);
   }

   /** @deprecated */
   public ComponentException(String message) {
      this((String)null, message, (Throwable)null);
   }

   public ComponentException(String key, String message) {
      this(key, message, (Throwable)null);
   }

   public final String getKey() {
      return this.m_key;
   }

   /** @deprecated */
   public final String getRole() {
      return this.getKey();
   }

   public String getMessage() {
      return this.m_key == null ? super.getMessage() : super.getMessage() + " (key [" + this.m_key + "])";
   }
}

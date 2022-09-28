package org.apache.avalon.framework.configuration;

import org.apache.avalon.framework.CascadingException;

public class ConfigurationException extends CascadingException {
   private final Configuration m_config;

   public ConfigurationException(Configuration config) {
      this("Bad configuration: " + config.toString(), config);
   }

   public ConfigurationException(String message) {
      this(message, (Configuration)null);
   }

   public ConfigurationException(String message, Throwable throwable) {
      this(message, (Configuration)null, throwable);
   }

   public ConfigurationException(String message, Configuration config) {
      this(message, config, (Throwable)null);
   }

   public ConfigurationException(String message, Configuration config, Throwable throwable) {
      super(message, throwable);
      this.m_config = config;
   }

   public Configuration getOffendingConfiguration() {
      return this.m_config;
   }

   public String getMessage() {
      StringBuffer message = new StringBuffer(super.getMessage());
      if (null != this.m_config) {
         message.append("@");
         message.append(this.m_config.getLocation());
      }

      return message.toString();
   }
}

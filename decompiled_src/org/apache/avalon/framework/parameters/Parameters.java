package org.apache.avalon.framework.parameters;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

public class Parameters implements Serializable {
   public static final Parameters EMPTY_PARAMETERS = new Parameters();
   private Map m_parameters = new HashMap();
   private boolean m_readOnly;

   public String setParameter(String name, String value) throws IllegalStateException {
      this.checkWriteable();
      if (null == name) {
         return null;
      } else {
         return null == value ? (String)this.m_parameters.remove(name) : (String)this.m_parameters.put(name, value);
      }
   }

   public void removeParameter(String name) {
      this.setParameter(name, (String)null);
   }

   /** @deprecated */
   public Iterator getParameterNames() {
      return this.m_parameters.keySet().iterator();
   }

   public String[] getNames() {
      return (String[])this.m_parameters.keySet().toArray(new String[0]);
   }

   public boolean isParameter(String name) {
      return this.m_parameters.containsKey(name);
   }

   public String getParameter(String name) throws ParameterException {
      if (null == name) {
         throw new ParameterException("You cannot lookup a null parameter");
      } else {
         String test = (String)this.m_parameters.get(name);
         if (null == test) {
            throw new ParameterException("The parameter '" + name + "' does not contain a value");
         } else {
            return test;
         }
      }
   }

   public String getParameter(String name, String defaultValue) {
      if (name == null) {
         return defaultValue;
      } else {
         String test = (String)this.m_parameters.get(name);
         return test == null ? defaultValue : test;
      }
   }

   private int parseInt(String value) throws NumberFormatException {
      if (value.startsWith("0x")) {
         return Integer.parseInt(value.substring(2), 16);
      } else if (value.startsWith("0o")) {
         return Integer.parseInt(value.substring(2), 8);
      } else {
         return value.startsWith("0b") ? Integer.parseInt(value.substring(2), 2) : Integer.parseInt(value);
      }
   }

   public int getParameterAsInteger(String name) throws ParameterException {
      try {
         return this.parseInt(this.getParameter(name));
      } catch (NumberFormatException var3) {
         throw new ParameterException("Could not return an integer value", var3);
      }
   }

   public int getParameterAsInteger(String name, int defaultValue) {
      try {
         String value = this.getParameter(name, (String)null);
         return value == null ? defaultValue : this.parseInt(value);
      } catch (NumberFormatException var4) {
         return defaultValue;
      }
   }

   private long parseLong(String value) throws NumberFormatException {
      if (value.startsWith("0x")) {
         return Long.parseLong(value.substring(2), 16);
      } else if (value.startsWith("0o")) {
         return Long.parseLong(value.substring(2), 8);
      } else {
         return value.startsWith("0b") ? Long.parseLong(value.substring(2), 2) : Long.parseLong(value);
      }
   }

   public long getParameterAsLong(String name) throws ParameterException {
      try {
         return this.parseLong(this.getParameter(name));
      } catch (NumberFormatException var3) {
         throw new ParameterException("Could not return a long value", var3);
      }
   }

   public long getParameterAsLong(String name, long defaultValue) {
      try {
         String value = this.getParameter(name, (String)null);
         return value == null ? defaultValue : this.parseLong(value);
      } catch (NumberFormatException var5) {
         return defaultValue;
      }
   }

   public float getParameterAsFloat(String name) throws ParameterException {
      try {
         return Float.parseFloat(this.getParameter(name));
      } catch (NumberFormatException var3) {
         throw new ParameterException("Could not return a float value", var3);
      }
   }

   public float getParameterAsFloat(String name, float defaultValue) {
      try {
         String value = this.getParameter(name, (String)null);
         return value == null ? defaultValue : Float.parseFloat(value);
      } catch (NumberFormatException var4) {
         return defaultValue;
      }
   }

   public boolean getParameterAsBoolean(String name) throws ParameterException {
      String value = this.getParameter(name);
      if (value.equalsIgnoreCase("true")) {
         return true;
      } else if (value.equalsIgnoreCase("false")) {
         return false;
      } else {
         throw new ParameterException("Could not return a boolean value");
      }
   }

   public boolean getParameterAsBoolean(String name, boolean defaultValue) {
      String value = this.getParameter(name, (String)null);
      if (value == null) {
         return defaultValue;
      } else if (value.equalsIgnoreCase("true")) {
         return true;
      } else {
         return value.equalsIgnoreCase("false") ? false : defaultValue;
      }
   }

   public Parameters merge(Parameters other) {
      this.checkWriteable();
      String[] names = other.getNames();

      for(int i = 0; i < names.length; ++i) {
         String name = names[i];
         String value = null;

         try {
            value = other.getParameter(name);
         } catch (ParameterException var7) {
            value = null;
         }

         this.setParameter(name, value);
      }

      return this;
   }

   public void makeReadOnly() {
      this.m_readOnly = true;
   }

   protected final void checkWriteable() throws IllegalStateException {
      if (this.m_readOnly) {
         throw new IllegalStateException("Context is read only and can not be modified");
      }
   }

   public static Parameters fromConfiguration(Configuration configuration) throws ConfigurationException {
      return fromConfiguration(configuration, "parameter");
   }

   public static Parameters fromConfiguration(Configuration configuration, String elementName) throws ConfigurationException {
      if (null == configuration) {
         throw new ConfigurationException("You cannot convert to parameters with a null Configuration");
      } else {
         Configuration[] parameters = configuration.getChildren(elementName);
         Parameters params = new Parameters();

         for(int i = 0; i < parameters.length; ++i) {
            try {
               String name = parameters[i].getAttribute("name");
               String value = parameters[i].getAttribute("value");
               params.setParameter(name, value);
            } catch (Exception var7) {
               throw new ConfigurationException("Cannot process Configurable", var7);
            }
         }

         return params;
      }
   }

   public static Parameters fromProperties(Properties properties) {
      Parameters parameters = new Parameters();
      Enumeration names = properties.propertyNames();

      while(names.hasMoreElements()) {
         String key = names.nextElement().toString();
         String value = properties.getProperty(key);
         parameters.setParameter(key, value);
      }

      return parameters;
   }

   public static Properties toProperties(Parameters params) {
      Properties properties = new Properties();
      String[] names = params.getNames();

      for(int i = 0; i < names.length; ++i) {
         properties.setProperty(names[i], params.getParameter(names[i], ""));
      }

      return properties;
   }

   static {
      EMPTY_PARAMETERS.makeReadOnly();
   }
}

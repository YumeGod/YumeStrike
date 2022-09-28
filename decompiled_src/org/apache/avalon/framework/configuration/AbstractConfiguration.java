package org.apache.avalon.framework.configuration;

public abstract class AbstractConfiguration implements Configuration {
   protected abstract String getPrefix() throws ConfigurationException;

   public int getValueAsInteger() throws ConfigurationException {
      String value = this.getValue().trim();

      try {
         if (value.startsWith("0x")) {
            return Integer.parseInt(value.substring(2), 16);
         } else if (value.startsWith("0o")) {
            return Integer.parseInt(value.substring(2), 8);
         } else {
            return value.startsWith("0b") ? Integer.parseInt(value.substring(2), 2) : Integer.parseInt(value);
         }
      } catch (Exception var4) {
         String message = "Cannot parse the value \"" + value + "\" as an integer in the configuration element \"" + this.getName() + "\" at " + this.getLocation();
         throw new ConfigurationException(message);
      }
   }

   public int getValueAsInteger(int defaultValue) {
      try {
         return this.getValueAsInteger();
      } catch (ConfigurationException var3) {
         return defaultValue;
      }
   }

   public long getValueAsLong() throws ConfigurationException {
      String value = this.getValue().trim();

      try {
         if (value.startsWith("0x")) {
            return Long.parseLong(value.substring(2), 16);
         } else if (value.startsWith("0o")) {
            return Long.parseLong(value.substring(2), 8);
         } else {
            return value.startsWith("0b") ? Long.parseLong(value.substring(2), 2) : Long.parseLong(value);
         }
      } catch (Exception var4) {
         String message = "Cannot parse the value \"" + value + "\" as a long in the configuration element \"" + this.getName() + "\" at " + this.getLocation();
         throw new ConfigurationException(message);
      }
   }

   public long getValueAsLong(long defaultValue) {
      try {
         return this.getValueAsLong();
      } catch (ConfigurationException var4) {
         return defaultValue;
      }
   }

   public float getValueAsFloat() throws ConfigurationException {
      String value = this.getValue().trim();

      try {
         return Float.parseFloat(value);
      } catch (Exception var4) {
         String message = "Cannot parse the value \"" + value + "\" as a float in the configuration element \"" + this.getName() + "\" at " + this.getLocation();
         throw new ConfigurationException(message);
      }
   }

   public float getValueAsFloat(float defaultValue) {
      try {
         return this.getValueAsFloat();
      } catch (ConfigurationException var3) {
         return defaultValue;
      }
   }

   public boolean getValueAsBoolean() throws ConfigurationException {
      String value = this.getValue().trim();
      if (this.isTrue(value)) {
         return true;
      } else if (this.isFalse(value)) {
         return false;
      } else {
         String message = "Cannot parse the value \"" + value + "\" as a boolean in the configuration element \"" + this.getName() + "\" at " + this.getLocation();
         throw new ConfigurationException(message);
      }
   }

   public boolean getValueAsBoolean(boolean defaultValue) {
      try {
         return this.getValueAsBoolean();
      } catch (ConfigurationException var3) {
         return defaultValue;
      }
   }

   public String getValue(String defaultValue) {
      try {
         return this.getValue();
      } catch (ConfigurationException var3) {
         return defaultValue;
      }
   }

   public int getAttributeAsInteger(String name) throws ConfigurationException {
      String value = this.getAttribute(name).trim();

      try {
         if (value.startsWith("0x")) {
            return Integer.parseInt(value.substring(2), 16);
         } else if (value.startsWith("0o")) {
            return Integer.parseInt(value.substring(2), 8);
         } else {
            return value.startsWith("0b") ? Integer.parseInt(value.substring(2), 2) : Integer.parseInt(value);
         }
      } catch (Exception var5) {
         String message = "Cannot parse the value \"" + value + "\" as an integer in the attribute \"" + name + "\" at " + this.getLocation();
         throw new ConfigurationException(message);
      }
   }

   public int getAttributeAsInteger(String name, int defaultValue) {
      try {
         return this.getAttributeAsInteger(name);
      } catch (ConfigurationException var4) {
         return defaultValue;
      }
   }

   public long getAttributeAsLong(String name) throws ConfigurationException {
      String value = this.getAttribute(name);

      try {
         if (value.startsWith("0x")) {
            return Long.parseLong(value.substring(2), 16);
         } else if (value.startsWith("0o")) {
            return Long.parseLong(value.substring(2), 8);
         } else {
            return value.startsWith("0b") ? Long.parseLong(value.substring(2), 2) : Long.parseLong(value);
         }
      } catch (Exception var5) {
         String message = "Cannot parse the value \"" + value + "\" as a long in the attribute \"" + name + "\" at " + this.getLocation();
         throw new ConfigurationException(message);
      }
   }

   public long getAttributeAsLong(String name, long defaultValue) {
      try {
         return this.getAttributeAsLong(name);
      } catch (ConfigurationException var5) {
         return defaultValue;
      }
   }

   public float getAttributeAsFloat(String name) throws ConfigurationException {
      String value = this.getAttribute(name);

      try {
         return Float.parseFloat(value);
      } catch (Exception var5) {
         String message = "Cannot parse the value \"" + value + "\" as a float in the attribute \"" + name + "\" at " + this.getLocation();
         throw new ConfigurationException(message);
      }
   }

   public float getAttributeAsFloat(String name, float defaultValue) {
      try {
         return this.getAttributeAsFloat(name);
      } catch (ConfigurationException var4) {
         return defaultValue;
      }
   }

   public boolean getAttributeAsBoolean(String name) throws ConfigurationException {
      String value = this.getAttribute(name);
      if (this.isTrue(value)) {
         return true;
      } else if (this.isFalse(value)) {
         return false;
      } else {
         String message = "Cannot parse the value \"" + value + "\" as a boolean in the attribute \"" + name + "\" at " + this.getLocation();
         throw new ConfigurationException(message);
      }
   }

   private boolean isTrue(String value) {
      return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("on") || value.equalsIgnoreCase("1");
   }

   private boolean isFalse(String value) {
      return value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no") || value.equalsIgnoreCase("off") || value.equalsIgnoreCase("0");
   }

   public boolean getAttributeAsBoolean(String name, boolean defaultValue) {
      try {
         return this.getAttributeAsBoolean(name);
      } catch (ConfigurationException var4) {
         return defaultValue;
      }
   }

   public String getAttribute(String name, String defaultValue) {
      try {
         return this.getAttribute(name);
      } catch (ConfigurationException var4) {
         return defaultValue;
      }
   }

   public Configuration getChild(String name) {
      return this.getChild(name, true);
   }

   public Configuration getChild(String name, boolean createNew) {
      Configuration[] children = this.getChildren(name);
      if (children.length > 0) {
         return children[0];
      } else {
         return createNew ? new DefaultConfiguration(name, "-") : null;
      }
   }

   public String toString() {
      return this.getName() + "::" + this.getValue("<no value>") + ":@" + this.getLocation();
   }

   // $FF: synthetic method
   public abstract String getValue() throws ConfigurationException;

   // $FF: synthetic method
   public abstract String getAttribute(String var1) throws ConfigurationException;

   // $FF: synthetic method
   public abstract String[] getAttributeNames();

   // $FF: synthetic method
   public abstract Configuration[] getChildren(String var1);

   // $FF: synthetic method
   public abstract Configuration[] getChildren();

   // $FF: synthetic method
   public abstract String getNamespace() throws ConfigurationException;

   // $FF: synthetic method
   public abstract String getLocation();

   // $FF: synthetic method
   public abstract String getName();
}

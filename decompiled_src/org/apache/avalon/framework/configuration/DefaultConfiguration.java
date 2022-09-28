package org.apache.avalon.framework.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class DefaultConfiguration extends AbstractConfiguration implements MutableConfiguration, Serializable {
   protected static final Configuration[] EMPTY_ARRAY = new Configuration[0];
   private final String m_name;
   private final String m_location;
   private final String m_namespace;
   private final String m_prefix;
   private HashMap m_attributes;
   private ArrayList m_children;
   private String m_value;
   private boolean m_readOnly;

   public DefaultConfiguration(Configuration config) throws ConfigurationException {
      this(config.getName(), config.getLocation(), config.getNamespace(), config instanceof AbstractConfiguration ? ((AbstractConfiguration)config).getPrefix() : "");
      this.addAll(config);
   }

   public DefaultConfiguration(String name) {
      this(name, (String)null, "", "");
   }

   public DefaultConfiguration(String name, String location) {
      this(name, location, "", "");
   }

   public DefaultConfiguration(String name, String location, String ns, String prefix) {
      this.m_name = name;
      this.m_location = location;
      this.m_namespace = ns;
      this.m_prefix = prefix;
   }

   public String getName() {
      return this.m_name;
   }

   public String getNamespace() throws ConfigurationException {
      if (null != this.m_namespace) {
         return this.m_namespace;
      } else {
         throw new ConfigurationException("No namespace (not even default \"\") is associated with the configuration element \"" + this.getName() + "\" at " + this.getLocation());
      }
   }

   protected String getPrefix() throws ConfigurationException {
      if (null != this.m_prefix) {
         return this.m_prefix;
      } else {
         throw new ConfigurationException("No prefix (not even default \"\") is associated with the configuration element \"" + this.getName() + "\" at " + this.getLocation());
      }
   }

   public String getLocation() {
      return this.m_location;
   }

   public String getValue(String defaultValue) {
      return null != this.m_value ? this.m_value : defaultValue;
   }

   public String getValue() throws ConfigurationException {
      if (null != this.m_value) {
         return this.m_value;
      } else {
         throw new ConfigurationException("No value is associated with the configuration element \"" + this.getName() + "\" at " + this.getLocation());
      }
   }

   public String[] getAttributeNames() {
      return null == this.m_attributes ? new String[0] : (String[])this.m_attributes.keySet().toArray(new String[0]);
   }

   public Configuration[] getChildren() {
      return null == this.m_children ? new Configuration[0] : (Configuration[])this.m_children.toArray(new Configuration[0]);
   }

   public String getAttribute(String name) throws ConfigurationException {
      String value = null != this.m_attributes ? (String)this.m_attributes.get(name) : null;
      if (null != value) {
         return value;
      } else {
         throw new ConfigurationException("No attribute named \"" + name + "\" is " + "associated with the configuration element \"" + this.getName() + "\" at " + this.getLocation());
      }
   }

   public Configuration getChild(String name, boolean createNew) {
      if (null != this.m_children) {
         int size = this.m_children.size();

         for(int i = 0; i < size; ++i) {
            Configuration configuration = (Configuration)this.m_children.get(i);
            if (name.equals(configuration.getName())) {
               return configuration;
            }
         }
      }

      return createNew ? new DefaultConfiguration(name, "<generated>" + this.getLocation(), this.m_namespace, this.m_prefix) : null;
   }

   public Configuration[] getChildren(String name) {
      if (null == this.m_children) {
         return new Configuration[0];
      } else {
         ArrayList children = new ArrayList();
         int size = this.m_children.size();

         for(int i = 0; i < size; ++i) {
            Configuration configuration = (Configuration)this.m_children.get(i);
            if (name.equals(configuration.getName())) {
               children.add(configuration);
            }
         }

         return (Configuration[])children.toArray(new Configuration[0]);
      }
   }

   /** @deprecated */
   public void appendValueData(String value) {
      this.checkWriteable();
      if (null == this.m_value) {
         this.m_value = value;
      } else {
         this.m_value = this.m_value + value;
      }

   }

   public void setValue(String value) {
      this.checkWriteable();
      this.m_value = value;
   }

   public void setValue(int value) {
      this.setValue(String.valueOf(value));
   }

   public void setValue(long value) {
      this.setValue(String.valueOf(value));
   }

   public void setValue(boolean value) {
      this.setValue(String.valueOf(value));
   }

   public void setValue(float value) {
      this.setValue(String.valueOf(value));
   }

   public void setAttribute(String name, String value) {
      this.checkWriteable();
      if (null != value) {
         if (null == this.m_attributes) {
            this.m_attributes = new HashMap();
         }

         this.m_attributes.put(name, value);
      } else if (null != this.m_attributes) {
         this.m_attributes.remove(name);
      }

   }

   public void setAttribute(String name, int value) {
      this.setAttribute(name, String.valueOf(value));
   }

   public void setAttribute(String name, long value) {
      this.setAttribute(name, String.valueOf(value));
   }

   public void setAttribute(String name, boolean value) {
      this.setAttribute(name, String.valueOf(value));
   }

   public void setAttribute(String name, float value) {
      this.setAttribute(name, String.valueOf(value));
   }

   /** @deprecated */
   public String addAttribute(String name, String value) {
      this.checkWriteable();
      if (null == this.m_attributes) {
         this.m_attributes = new HashMap();
      }

      return (String)this.m_attributes.put(name, value);
   }

   public void addChild(Configuration configuration) {
      this.checkWriteable();
      if (null == this.m_children) {
         this.m_children = new ArrayList();
      }

      this.m_children.add(configuration);
   }

   public void addAll(Configuration other) {
      this.checkWriteable();
      this.setValue(other.getValue((String)null));
      this.addAllAttributes(other);
      this.addAllChildren(other);
   }

   public void addAllAttributes(Configuration other) {
      this.checkWriteable();
      String[] attributes = other.getAttributeNames();

      for(int i = 0; i < attributes.length; ++i) {
         String name = attributes[i];
         String value = other.getAttribute(name, (String)null);
         this.setAttribute(name, value);
      }

   }

   public void addAllChildren(Configuration other) {
      this.checkWriteable();
      Configuration[] children = other.getChildren();

      for(int i = 0; i < children.length; ++i) {
         this.addChild(children[i]);
      }

   }

   public void removeChild(Configuration configuration) {
      this.checkWriteable();
      if (null != this.m_children) {
         this.m_children.remove(configuration);
      }
   }

   public int getChildCount() {
      return null == this.m_children ? 0 : this.m_children.size();
   }

   public void makeReadOnly() {
      this.m_readOnly = true;
   }

   protected final void checkWriteable() throws IllegalStateException {
      if (this.m_readOnly) {
         throw new IllegalStateException("Configuration is read only and can not be modified");
      }
   }

   protected final boolean isReadOnly() {
      return this.m_readOnly;
   }

   private MutableConfiguration toMutable(Configuration child) throws ConfigurationException {
      if (!(child instanceof MutableConfiguration) || child instanceof DefaultConfiguration && ((DefaultConfiguration)child).isReadOnly()) {
         this.checkWriteable();
         DefaultConfiguration config = new DefaultConfiguration(child);

         for(int i = 0; i < this.m_children.size(); ++i) {
            if (this.m_children.get(i) == child) {
               this.m_children.set(i, config);
               break;
            }
         }

         return config;
      } else {
         return (MutableConfiguration)child;
      }
   }

   public MutableConfiguration getMutableChild(String name) throws ConfigurationException {
      return this.getMutableChild(name, true);
   }

   public MutableConfiguration getMutableChild(String name, boolean autoCreate) throws ConfigurationException {
      Configuration child = this.getChild(name, false);
      if (child == null) {
         if (autoCreate) {
            DefaultConfiguration config = new DefaultConfiguration(name, "-");
            this.addChild(config);
            return config;
         } else {
            return null;
         }
      } else {
         return this.toMutable(child);
      }
   }

   public MutableConfiguration[] getMutableChildren() throws ConfigurationException {
      if (null == this.m_children) {
         return new MutableConfiguration[0];
      } else {
         ArrayList children = new ArrayList();
         int size = this.m_children.size();

         for(int i = 0; i < size; ++i) {
            Configuration configuration = (Configuration)this.m_children.get(i);
            children.add(this.toMutable(configuration));
         }

         return (MutableConfiguration[])children.toArray(new MutableConfiguration[0]);
      }
   }

   public MutableConfiguration[] getMutableChildren(String name) throws ConfigurationException {
      if (null == this.m_children) {
         return new MutableConfiguration[0];
      } else {
         ArrayList children = new ArrayList();
         int size = this.m_children.size();

         for(int i = 0; i < size; ++i) {
            Configuration configuration = (Configuration)this.m_children.get(i);
            if (name.equals(configuration.getName())) {
               children.add(this.toMutable(configuration));
            }
         }

         return (MutableConfiguration[])children.toArray(new MutableConfiguration[0]);
      }
   }

   public boolean equals(Object other) {
      if (other == null) {
         return false;
      } else {
         return !(other instanceof Configuration) ? false : ConfigurationUtil.equals(this, (Configuration)other);
      }
   }

   public int hashCode() {
      int hash = this.m_prefix.hashCode();
      if (this.m_name != null) {
         hash ^= this.m_name.hashCode();
      }

      hash >>>= 7;
      if (this.m_location != null) {
         hash ^= this.m_location.hashCode();
      }

      hash >>>= 7;
      if (this.m_namespace != null) {
         hash ^= this.m_namespace.hashCode();
      }

      hash >>>= 7;
      if (this.m_attributes != null) {
         hash ^= this.m_attributes.hashCode();
      }

      hash >>>= 7;
      if (this.m_children != null) {
         hash ^= this.m_children.hashCode();
      }

      hash >>>= 7;
      if (this.m_value != null) {
         hash ^= this.m_value.hashCode();
      }

      hash >>>= 7;
      hash ^= this.m_readOnly ? 1 : 3;
      return hash;
   }
}

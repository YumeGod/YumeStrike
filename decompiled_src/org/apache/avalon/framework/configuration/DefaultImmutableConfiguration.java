package org.apache.avalon.framework.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class DefaultImmutableConfiguration extends AbstractConfiguration implements Serializable {
   protected static final Configuration[] EMPTY_ARRAY = new Configuration[0];
   private final String m_name;
   private final String m_location;
   private final String m_namespace;
   private final String m_prefix;
   private final HashMap m_attributes;
   private final ArrayList m_children;
   private final String m_value;

   public DefaultImmutableConfiguration(Configuration config) throws ConfigurationException {
      this.m_name = config.getName();
      this.m_location = config.getLocation();
      this.m_namespace = config.getNamespace();
      this.m_prefix = config instanceof AbstractConfiguration ? ((AbstractConfiguration)config).getPrefix() : "";
      this.m_value = config.getValue((String)null);
      String[] attributes = config.getAttributeNames();
      if (attributes.length > 0) {
         this.m_attributes = new HashMap();

         for(int i = 0; i < attributes.length; ++i) {
            String name = attributes[i];
            String value = config.getAttribute(name, (String)null);
            this.m_attributes.put(name, value);
         }
      } else {
         this.m_attributes = null;
      }

      Configuration[] children = config.getChildren();
      if (children.length > 0) {
         this.m_children = new ArrayList();

         for(int i = 0; i < children.length; ++i) {
            this.m_children.add(new DefaultImmutableConfiguration(children[i]));
         }
      } else {
         this.m_children = null;
      }

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

   public int getChildCount() {
      return null == this.m_children ? 0 : this.m_children.size();
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
      return hash;
   }
}

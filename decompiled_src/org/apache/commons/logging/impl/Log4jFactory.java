package org.apache.commons.logging.impl;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

/** @deprecated */
public final class Log4jFactory extends LogFactory {
   private Hashtable attributes = new Hashtable();
   private Hashtable instances = new Hashtable();

   public Object getAttribute(String name) {
      return this.attributes.get(name);
   }

   public String[] getAttributeNames() {
      Vector names = new Vector();
      Enumeration keys = this.attributes.keys();

      while(keys.hasMoreElements()) {
         names.addElement((String)keys.nextElement());
      }

      String[] results = new String[names.size()];

      for(int i = 0; i < results.length; ++i) {
         results[i] = (String)names.elementAt(i);
      }

      return results;
   }

   public Log getInstance(Class clazz) throws LogConfigurationException {
      Log instance = (Log)this.instances.get(clazz);
      if (instance != null) {
         return instance;
      } else {
         Log instance = new Log4JLogger(Logger.getLogger(clazz));
         this.instances.put(clazz, instance);
         return instance;
      }
   }

   public Log getInstance(String name) throws LogConfigurationException {
      Log instance = (Log)this.instances.get(name);
      if (instance != null) {
         return instance;
      } else {
         Log instance = new Log4JLogger(Logger.getLogger(name));
         this.instances.put(name, instance);
         return instance;
      }
   }

   public void release() {
      this.instances.clear();
   }

   public void removeAttribute(String name) {
      this.attributes.remove(name);
   }

   public void setAttribute(String name, Object value) {
      if (value == null) {
         this.attributes.remove(name);
      } else {
         this.attributes.put(name, value);
      }

   }
}

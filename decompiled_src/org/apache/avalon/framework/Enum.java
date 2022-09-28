package org.apache.avalon.framework;

import java.util.Map;

public abstract class Enum {
   private final String m_name;

   protected Enum(String name) {
      this(name, (Map)null);
   }

   protected Enum(String name, Map map) {
      this.m_name = name;
      if (null != map) {
         map.put(name, this);
      }

   }

   public final boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof Enum)) {
         return false;
      } else {
         Enum enum = (Enum)o;
         if (!this.getClass().equals(enum.getClass())) {
            return false;
         } else {
            if (this.m_name != null) {
               if (!this.m_name.equals(enum.m_name)) {
                  return false;
               }
            } else if (enum.m_name != null) {
               return false;
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int result = this.m_name != null ? this.m_name.hashCode() : 0;
      result = 29 * result + this.getClass().hashCode();
      return result;
   }

   public final String getName() {
      return this.m_name;
   }

   public String toString() {
      return this.getClass().getName() + "[" + this.m_name + "]";
   }
}

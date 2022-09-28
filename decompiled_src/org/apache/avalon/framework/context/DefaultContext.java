package org.apache.avalon.framework.context;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class DefaultContext implements Context {
   private static final Hidden HIDDEN_MAKER = new Hidden();
   private final Map m_contextData;
   private final Context m_parent;
   private boolean m_readOnly;

   public DefaultContext(Map contextData, Context parent) {
      this.m_parent = parent;
      this.m_contextData = contextData;
   }

   public DefaultContext(Map contextData) {
      this(contextData, (Context)null);
   }

   public DefaultContext(Context parent) {
      this(new Hashtable(), parent);
   }

   public DefaultContext() {
      this((Context)null);
   }

   public Object get(Object key) throws ContextException {
      Object data = this.m_contextData.get(key);
      String message;
      if (null != data) {
         if (data instanceof Hidden) {
            message = "Unable to locate " + key;
            throw new ContextException(message);
         } else {
            return data instanceof Resolvable ? ((Resolvable)data).resolve(this) : data;
         }
      } else if (null == this.m_parent) {
         message = "Unable to resolve context key: " + key;
         throw new ContextException(message);
      } else {
         return this.m_parent.get(key);
      }
   }

   public void put(Object key, Object value) throws IllegalStateException {
      this.checkWriteable();
      if (null == value) {
         this.m_contextData.remove(key);
      } else {
         this.m_contextData.put(key, value);
      }

   }

   public void hide(Object key) throws IllegalStateException {
      this.checkWriteable();
      this.m_contextData.put(key, HIDDEN_MAKER);
   }

   protected final Map getContextData() {
      return this.m_contextData;
   }

   protected final Context getParent() {
      return this.m_parent;
   }

   public void makeReadOnly() {
      this.m_readOnly = true;
   }

   protected final void checkWriteable() throws IllegalStateException {
      if (this.m_readOnly) {
         String message = "Context is read only and can not be modified";
         throw new IllegalStateException("Context is read only and can not be modified");
      }
   }

   private static final class Hidden implements Serializable {
      private Hidden() {
      }

      // $FF: synthetic method
      Hidden(Object x0) {
         this();
      }
   }
}

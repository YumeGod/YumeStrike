package org.apache.xalan.trace;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.apache.xalan.transformer.TransformerImpl;

public class ExtensionEvent {
   public static final int DEFAULT_CONSTRUCTOR = 0;
   public static final int METHOD = 1;
   public static final int CONSTRUCTOR = 2;
   public final int m_callType;
   public final TransformerImpl m_transformer;
   public final Object m_method;
   public final Object m_instance;
   public final Object[] m_arguments;

   public ExtensionEvent(TransformerImpl transformer, Method method, Object instance, Object[] arguments) {
      this.m_transformer = transformer;
      this.m_method = method;
      this.m_instance = instance;
      this.m_arguments = arguments;
      this.m_callType = 1;
   }

   public ExtensionEvent(TransformerImpl transformer, Constructor constructor, Object[] arguments) {
      this.m_transformer = transformer;
      this.m_instance = null;
      this.m_arguments = arguments;
      this.m_method = constructor;
      this.m_callType = 2;
   }

   public ExtensionEvent(TransformerImpl transformer, Class clazz) {
      this.m_transformer = transformer;
      this.m_instance = null;
      this.m_arguments = null;
      this.m_method = clazz;
      this.m_callType = 0;
   }
}

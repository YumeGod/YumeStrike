package org.apache.xml.utils;

public class StringBufferPool {
   private static ObjectPool m_stringBufPool;
   // $FF: synthetic field
   static Class class$org$apache$xml$utils$FastStringBuffer;

   public static synchronized FastStringBuffer get() {
      return (FastStringBuffer)m_stringBufPool.getInstance();
   }

   public static synchronized void free(FastStringBuffer sb) {
      sb.setLength(0);
      m_stringBufPool.freeInstance(sb);
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      m_stringBufPool = new ObjectPool(class$org$apache$xml$utils$FastStringBuffer == null ? (class$org$apache$xml$utils$FastStringBuffer = class$("org.apache.xml.utils.FastStringBuffer")) : class$org$apache$xml$utils$FastStringBuffer);
   }
}

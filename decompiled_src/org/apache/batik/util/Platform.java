package org.apache.batik.util;

import java.awt.Frame;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Platform {
   public static boolean isOSX = System.getProperty("os.name").equals("Mac OS X");
   public static boolean isJRE13 = System.getProperty("java.version").startsWith("1.3");
   // $FF: synthetic field
   static Class class$java$awt$Frame;

   public static void unmaximize(Frame var0) {
      if (!isJRE13) {
         try {
            Method var1 = (class$java$awt$Frame == null ? (class$java$awt$Frame = class$("java.awt.Frame")) : class$java$awt$Frame).getMethod("getExtendedState", (Class[])null);
            Method var2 = (class$java$awt$Frame == null ? (class$java$awt$Frame = class$("java.awt.Frame")) : class$java$awt$Frame).getMethod("setExtendedState", Integer.TYPE);
            int var3 = (Integer)var1.invoke(var0, (Object[])null);
            var2.invoke(var0, new Integer(var3 & -7));
         } catch (InvocationTargetException var4) {
         } catch (NoSuchMethodException var5) {
         } catch (IllegalAccessException var6) {
         }
      }

   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}

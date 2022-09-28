package org.xml.sax.helpers;

class NewInstance {
   private static final boolean DO_FALLBACK = true;
   // $FF: synthetic field
   static Class class$org$xml$sax$helpers$NewInstance;

   static Object newInstance(ClassLoader var0, String var1) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
      Class var2;
      if (var0 == null) {
         var2 = Class.forName(var1);
      } else {
         try {
            var2 = var0.loadClass(var1);
         } catch (ClassNotFoundException var4) {
            var0 = (class$org$xml$sax$helpers$NewInstance == null ? (class$org$xml$sax$helpers$NewInstance = class$("org.xml.sax.helpers.NewInstance")) : class$org$xml$sax$helpers$NewInstance).getClassLoader();
            if (var0 != null) {
               var2 = var0.loadClass(var1);
            } else {
               var2 = Class.forName(var1);
            }
         }
      }

      Object var3 = var2.newInstance();
      return var3;
   }

   static ClassLoader getClassLoader() {
      SecuritySupport var0 = SecuritySupport.getInstance();
      ClassLoader var1 = var0.getContextClassLoader();
      if (var1 == null) {
         var1 = (class$org$xml$sax$helpers$NewInstance == null ? (class$org$xml$sax$helpers$NewInstance = class$("org.xml.sax.helpers.NewInstance")) : class$org$xml$sax$helpers$NewInstance).getClassLoader();
      }

      return var1;
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

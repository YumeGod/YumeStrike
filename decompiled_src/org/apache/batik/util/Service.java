package org.apache.batik.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Service {
   static HashMap providerMap = new HashMap();
   // $FF: synthetic field
   static Class class$org$apache$batik$util$Service;

   public static synchronized Iterator providers(Class var0) {
      String var1 = "META-INF/services/" + var0.getName();
      List var2 = (List)providerMap.get(var1);
      if (var2 != null) {
         return var2.iterator();
      } else {
         ArrayList var42 = new ArrayList();
         providerMap.put(var1, var42);
         ClassLoader var3 = null;

         try {
            var3 = var0.getClassLoader();
         } catch (SecurityException var37) {
         }

         if (var3 == null) {
            var3 = (class$org$apache$batik$util$Service == null ? (class$org$apache$batik$util$Service = class$("org.apache.batik.util.Service")) : class$org$apache$batik$util$Service).getClassLoader();
         }

         if (var3 == null) {
            return var42.iterator();
         } else {
            Enumeration var4;
            try {
               var4 = var3.getResources(var1);
            } catch (IOException var36) {
               return var42.iterator();
            }

            label310:
            while(var4.hasMoreElements()) {
               InputStream var5 = null;
               InputStreamReader var6 = null;
               BufferedReader var7 = null;

               try {
                  URL var8 = (URL)var4.nextElement();
                  var5 = var8.openStream();
                  var6 = new InputStreamReader(var5, "UTF-8");
                  var7 = new BufferedReader(var6);
                  String var9 = var7.readLine();

                  while(true) {
                     while(true) {
                        if (var9 == null) {
                           continue label310;
                        }

                        try {
                           int var10 = var9.indexOf(35);
                           if (var10 != -1) {
                              var9 = var9.substring(0, var10);
                           }

                           var9 = var9.trim();
                           if (var9.length() != 0) {
                              Object var11 = var3.loadClass(var9).newInstance();
                              var42.add(var11);
                              break;
                           }

                           var9 = var7.readLine();
                        } catch (Exception var38) {
                           break;
                        }
                     }

                     var9 = var7.readLine();
                  }
               } catch (Exception var39) {
               } catch (LinkageError var40) {
               } finally {
                  if (var5 != null) {
                     try {
                        var5.close();
                     } catch (IOException var35) {
                     }

                     var5 = null;
                  }

                  if (var6 != null) {
                     try {
                        var6.close();
                     } catch (IOException var34) {
                     }

                     var6 = null;
                  }

                  if (var7 == null) {
                     try {
                        var7.close();
                     } catch (IOException var33) {
                     }

                     var7 = null;
                  }

               }
            }

            return var42.iterator();
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

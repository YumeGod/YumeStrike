package org.apache.xmlgraphics.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;

public class Service {
   static Map classMap = new HashMap();
   static Map instanceMap = new HashMap();

   public static synchronized Iterator providers(Class cls) {
      return providers(cls, true);
   }

   public static synchronized Iterator providers(Class cls, boolean returnInstances) {
      String serviceFile = "META-INF/services/" + cls.getName();
      Map cacheMap = returnInstances ? instanceMap : classMap;
      List l = (List)cacheMap.get(serviceFile);
      if (l != null) {
         return l.iterator();
      } else {
         List l = new ArrayList();
         cacheMap.put(serviceFile, l);
         ClassLoader cl = null;

         try {
            cl = cls.getClassLoader();
         } catch (SecurityException var22) {
         }

         if (cl == null) {
            cl = Service.class.getClassLoader();
         }

         if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
         }

         if (cl == null) {
            return l.iterator();
         } else {
            Enumeration var6;
            try {
               var6 = cl.getResources(serviceFile);
            } catch (IOException var21) {
               return l.iterator();
            }

            label172:
            while(var6.hasMoreElements()) {
               try {
                  URL u = (URL)var6.nextElement();
                  InputStream is = u.openStream();
                  Reader r = new InputStreamReader(is, "UTF-8");
                  BufferedReader br = new BufferedReader(r);

                  try {
                     String line = br.readLine();

                     while(true) {
                        while(true) {
                           if (line == null) {
                              continue label172;
                           }

                           try {
                              int idx = line.indexOf(35);
                              if (idx != -1) {
                                 line = line.substring(0, idx);
                              }

                              line = line.trim();
                              if (line.length() != 0) {
                                 if (returnInstances) {
                                    Object obj = cl.loadClass(line).newInstance();
                                    l.add(obj);
                                 } else {
                                    l.add(line);
                                 }
                                 break;
                              }

                              line = br.readLine();
                           } catch (Exception var23) {
                              break;
                           }
                        }

                        line = br.readLine();
                     }
                  } finally {
                     IOUtils.closeQuietly((Reader)br);
                     IOUtils.closeQuietly(is);
                  }
               } catch (Exception var25) {
               } catch (LinkageError var26) {
               }
            }

            return l.iterator();
         }
      }
   }
}

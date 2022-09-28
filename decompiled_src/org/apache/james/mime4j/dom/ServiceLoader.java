package org.apache.james.mime4j.dom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;

class ServiceLoader {
   private ServiceLoader() {
   }

   static Object load(Class spiClass) {
      String spiResURI = "META-INF/services/" + spiClass.getName();
      ClassLoader classLoader = spiClass.getClassLoader();

      try {
         Enumeration resources = classLoader.getResources(spiResURI);

         label109:
         while(true) {
            if (resources.hasMoreElements()) {
               URL resource = (URL)resources.nextElement();
               InputStream instream = resource.openStream();

               try {
                  BufferedReader reader = new BufferedReader(new InputStreamReader(instream));

                  while(true) {
                     String line;
                     if ((line = reader.readLine()) != null) {
                        line = line.trim();
                        int cmtIdx = line.indexOf(35);
                        if (cmtIdx != -1) {
                           line = line.substring(0, cmtIdx);
                           line = line.trim();
                        }

                        if (line.length() == 0) {
                           continue;
                        }

                        Class implClass = classLoader.loadClass(line);
                        if (!spiClass.isAssignableFrom(implClass)) {
                           continue;
                        }

                        Object impl = implClass.newInstance();
                        Object var11 = spiClass.cast(impl);
                        return var11;
                     }

                     reader.close();
                     continue label109;
                  }
               } finally {
                  instream.close();
               }
            }

            return null;
         }
      } catch (IOException var19) {
         throw new ServiceLoaderException(var19);
      } catch (ClassNotFoundException var20) {
         throw new ServiceLoaderException("Unknown SPI class '" + spiClass.getName() + "'", var20);
      } catch (IllegalAccessException var21) {
         return null;
      } catch (InstantiationException var22) {
         throw new ServiceLoaderException("SPI class '" + spiClass.getName() + "' cannot be instantiated", var22);
      }
   }
}

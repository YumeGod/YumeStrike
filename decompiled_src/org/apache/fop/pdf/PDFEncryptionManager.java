package org.apache.fop.pdf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Provider;
import java.security.Security;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PDFEncryptionManager {
   protected static Log log;

   public static boolean isJCEAvailable() {
      try {
         Class.forName("javax.crypto.Cipher");
         return true;
      } catch (ClassNotFoundException var1) {
         return false;
      }
   }

   public static boolean checkAvailableAlgorithms() {
      if (!isJCEAvailable()) {
         return false;
      } else {
         Provider[] providers = Security.getProviders("Cipher.RC4");
         if (providers == null) {
            log.warn("Cipher provider for RC4 not available.");
            return false;
         } else {
            providers = Security.getProviders("MessageDigest.MD5");
            if (providers == null) {
               log.warn("MessageDigest provider for MD5 not available.");
               return false;
            } else {
               return true;
            }
         }
      }
   }

   public static void setupPDFEncryption(PDFEncryptionParams params, PDFDocument pdf) {
      if (pdf == null) {
         throw new NullPointerException("PDF document must not be null");
      } else {
         if (params != null) {
            if (!checkAvailableAlgorithms()) {
               if (isJCEAvailable()) {
                  log.warn("PDF encryption has been requested, JCE is available but there's no JCE provider available that provides the necessary algorithms. The PDF won't be encrypted.");
               } else {
                  log.warn("PDF encryption has been requested but JCE is unavailable! The PDF won't be encrypted.");
               }
            }

            pdf.setEncryption(params);
         }

      }
   }

   public static PDFEncryption newInstance(int objnum, PDFEncryptionParams params) {
      try {
         Class clazz = Class.forName("org.apache.fop.pdf.PDFEncryptionJCE");
         Method makeMethod = clazz.getMethod("make", Integer.TYPE, PDFEncryptionParams.class);
         Object obj = makeMethod.invoke((Object)null, new Integer(objnum), params);
         return (PDFEncryption)obj;
      } catch (ClassNotFoundException var5) {
         if (checkAvailableAlgorithms()) {
            log.warn("JCE and algorithms available, but the implementation class unavailable. Please do a full rebuild.");
         }

         return null;
      } catch (NoSuchMethodException var6) {
         log.error(var6);
         return null;
      } catch (IllegalAccessException var7) {
         log.error(var7);
         return null;
      } catch (InvocationTargetException var8) {
         log.error(var8);
         return null;
      }
   }

   static {
      log = LogFactory.getLog(PDFEncryptionManager.class);
   }
}

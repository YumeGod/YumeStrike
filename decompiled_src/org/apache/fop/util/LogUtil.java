package org.apache.fop.util;

import org.apache.commons.logging.Log;
import org.apache.fop.apps.FOPException;

public class LogUtil {
   public static void handleError(Log log, String errorStr, boolean strict) throws FOPException {
      handleException(log, new FOPException(errorStr), strict);
   }

   public static void handleException(Log log, Exception e, boolean strict) throws FOPException {
      if (strict) {
         if (e instanceof FOPException) {
            throw (FOPException)e;
         } else {
            throw new FOPException(e);
         }
      } else {
         log.error(e.getMessage());
      }
   }
}

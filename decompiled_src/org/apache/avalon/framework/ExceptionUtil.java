package org.apache.avalon.framework;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

public final class ExceptionUtil {
   private static final String LINE_SEPARATOR = System.getProperty("line.separator");
   private static final String GET_CAUSE_NAME = "getCause";
   private static final Class[] GET_CAUSE_PARAMTYPES = new Class[0];

   private ExceptionUtil() {
   }

   public static String printStackTrace(Throwable throwable) {
      return printStackTrace(throwable, 0, true);
   }

   public static String printStackTrace(Throwable throwable, boolean printCascading) {
      return printStackTrace(throwable, 0, printCascading);
   }

   public static String printStackTrace(Throwable throwable, int depth) {
      int dp = depth;
      String[] lines = captureStackTrace(throwable);
      if (0 == depth || depth > lines.length) {
         dp = lines.length;
      }

      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < dp; ++i) {
         sb.append(lines[i]);
         sb.append(LINE_SEPARATOR);
      }

      return sb.toString();
   }

   public static String printStackTrace(Throwable throwable, int depth, boolean printCascading) {
      return printStackTrace(throwable, depth, printCascading, true);
   }

   public static String printStackTrace(Throwable throwable, int depth, boolean printCascading, boolean useReflection) {
      String result = printStackTrace(throwable, depth);
      if (!printCascading) {
         return result;
      } else {
         StringBuffer sb = new StringBuffer();
         sb.append(result);

         for(Throwable cause = getCause(throwable, useReflection); null != cause; cause = getCause(cause, useReflection)) {
            sb.append("rethrown from");
            sb.append(LINE_SEPARATOR);
            sb.append(printStackTrace(cause, depth));
         }

         return sb.toString();
      }
   }

   public static Throwable getCause(Throwable throwable, boolean useReflection) {
      if (throwable instanceof CascadingThrowable) {
         CascadingThrowable cascadingThrowable = (CascadingThrowable)throwable;
         return cascadingThrowable.getCause();
      } else if (useReflection) {
         try {
            Class clazz = throwable.getClass();
            Method method = clazz.getMethod("getCause", GET_CAUSE_PARAMTYPES);
            return (Throwable)method.invoke(throwable, (Object[])null);
         } catch (Throwable var4) {
            return null;
         }
      } else {
         return null;
      }
   }

   public static String[] captureStackTrace(Throwable throwable) {
      StringWriter sw = new StringWriter();
      throwable.printStackTrace(new PrintWriter(sw, true));
      return splitStringInternal(sw.toString(), LINE_SEPARATOR);
   }

   /** @deprecated */
   public static String[] splitString(String string, String onToken) {
      return splitStringInternal(string, onToken);
   }

   private static String[] splitStringInternal(String string, String onToken) {
      StringTokenizer tokenizer = new StringTokenizer(string, onToken);
      String[] result = new String[tokenizer.countTokens()];

      for(int i = 0; i < result.length; ++i) {
         result[i] = tokenizer.nextToken();
      }

      return result;
   }
}

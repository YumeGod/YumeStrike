package org.apache.xml.serializer;

import java.util.Properties;

public final class OutputPropertyUtils {
   public static boolean getBooleanProperty(String key, Properties props) {
      String s = props.getProperty(key);
      return null != s && s.equals("yes");
   }

   public static int getIntProperty(String key, Properties props) {
      String s = props.getProperty(key);
      return null == s ? 0 : Integer.parseInt(s);
   }
}

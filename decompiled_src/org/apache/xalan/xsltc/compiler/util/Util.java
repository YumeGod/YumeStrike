package org.apache.xalan.xsltc.compiler.util;

import java.util.StringTokenizer;
import org.apache.xml.utils.XML11Char;

public final class Util {
   private static char filesep;

   public static String noExtName(String name) {
      int index = name.lastIndexOf(46);
      return name.substring(0, index >= 0 ? index : name.length());
   }

   public static String baseName(String name) {
      int index = name.lastIndexOf(92);
      if (index < 0) {
         index = name.lastIndexOf(47);
      }

      if (index >= 0) {
         return name.substring(index + 1);
      } else {
         int lastColonIndex = name.lastIndexOf(58);
         return lastColonIndex > 0 ? name.substring(lastColonIndex + 1) : name;
      }
   }

   public static String pathName(String name) {
      int index = name.lastIndexOf(47);
      if (index < 0) {
         index = name.lastIndexOf(92);
      }

      return name.substring(0, index + 1);
   }

   public static String toJavaName(String name) {
      if (name.length() > 0) {
         StringBuffer result = new StringBuffer();
         char ch = name.charAt(0);
         result.append(Character.isJavaIdentifierStart(ch) ? ch : '_');
         int n = name.length();

         for(int i = 1; i < n; ++i) {
            ch = name.charAt(i);
            result.append(Character.isJavaIdentifierPart(ch) ? ch : '_');
         }

         return result.toString();
      } else {
         return name;
      }
   }

   public static org.apache.bcel.generic.Type getJCRefType(String signature) {
      return org.apache.bcel.generic.Type.getType(signature);
   }

   public static String internalName(String cname) {
      return cname.replace('.', filesep);
   }

   public static void println(String s) {
      System.out.println(s);
   }

   public static void println(char ch) {
      System.out.println(ch);
   }

   public static void TRACE1() {
      System.out.println("TRACE1");
   }

   public static void TRACE2() {
      System.out.println("TRACE2");
   }

   public static void TRACE3() {
      System.out.println("TRACE3");
   }

   public static String replace(String base, char ch, String str) {
      return base.indexOf(ch) < 0 ? base : replace(base, String.valueOf(ch), new String[]{str});
   }

   public static String replace(String base, String delim, String[] str) {
      int len = base.length();
      StringBuffer result = new StringBuffer();

      for(int i = 0; i < len; ++i) {
         char ch = base.charAt(i);
         int k = delim.indexOf(ch);
         if (k >= 0) {
            result.append(str[k]);
         } else {
            result.append(ch);
         }
      }

      return result.toString();
   }

   public static String escape(String input) {
      return replace(input, ".-/:", new String[]{"$dot$", "$dash$", "$slash$", "$colon$"});
   }

   public static String getLocalName(String qname) {
      int index = qname.lastIndexOf(":");
      return index > 0 ? qname.substring(index + 1) : qname;
   }

   public static String getPrefix(String qname) {
      int index = qname.lastIndexOf(":");
      return index > 0 ? qname.substring(0, index) : "";
   }

   public static boolean isLiteral(String str) {
      int length = str.length();

      for(int i = 0; i < length - 1; ++i) {
         if (str.charAt(i) == '{' && str.charAt(i + 1) != '{') {
            return false;
         }
      }

      return true;
   }

   public static boolean isValidQNames(String str) {
      if (str != null && !str.equals("")) {
         StringTokenizer tokens = new StringTokenizer(str);

         while(tokens.hasMoreTokens()) {
            if (!XML11Char.isXML11ValidQName(tokens.nextToken())) {
               return false;
            }
         }
      }

      return true;
   }

   static {
      String temp = System.getProperty("file.separator", "/");
      filesep = temp.charAt(0);
   }
}

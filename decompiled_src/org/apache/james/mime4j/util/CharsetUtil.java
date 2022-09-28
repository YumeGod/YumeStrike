package org.apache.james.mime4j.util;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

public class CharsetUtil {
   public static final String CRLF = "\r\n";
   public static final int CR = 13;
   public static final int LF = 10;
   public static final int SP = 32;
   public static final int HT = 9;
   public static final Charset US_ASCII = Charset.forName("US-ASCII");
   public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
   public static final Charset UTF_8 = Charset.forName("UTF-8");
   public static final Charset DEFAULT_CHARSET;

   public static boolean isASCII(char ch) {
      return ('ﾀ' & ch) == 0;
   }

   public static boolean isASCII(String s) {
      if (s == null) {
         throw new IllegalArgumentException("String may not be null");
      } else {
         int len = s.length();

         for(int i = 0; i < len; ++i) {
            if (!isASCII(s.charAt(i))) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isWhitespace(char ch) {
      return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
   }

   public static boolean isWhitespace(String s) {
      if (s == null) {
         throw new IllegalArgumentException("String may not be null");
      } else {
         int len = s.length();

         for(int i = 0; i < len; ++i) {
            if (!isWhitespace(s.charAt(i))) {
               return false;
            }
         }

         return true;
      }
   }

   public static Charset lookup(String name) {
      if (name == null) {
         return null;
      } else {
         try {
            return Charset.forName(name);
         } catch (IllegalCharsetNameException var2) {
            return null;
         } catch (UnsupportedCharsetException var3) {
            return null;
         }
      }
   }

   static {
      DEFAULT_CHARSET = US_ASCII;
   }
}

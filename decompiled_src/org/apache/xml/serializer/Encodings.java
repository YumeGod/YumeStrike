package org.apache.xml.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.xml.serializer.utils.WrappedRuntimeException;

public final class Encodings {
   private static final String ENCODINGS_FILE = "org/apache/xml/serializer/Encodings.properties";
   private static final String ENCODINGS_PROP = "org.apache.xalan.serialize.encodings";
   static final String DEFAULT_MIME_ENCODING = "UTF-8";
   private static final Hashtable _encodingTableKeyJava = new Hashtable();
   private static final Hashtable _encodingTableKeyMime = new Hashtable();
   private static final EncodingInfo[] _encodings = loadEncodingInfo();

   static Writer getWriter(OutputStream output, String encoding) throws UnsupportedEncodingException {
      for(int i = 0; i < _encodings.length; ++i) {
         if (_encodings[i].name.equalsIgnoreCase(encoding)) {
            try {
               return new OutputStreamWriter(output, _encodings[i].javaName);
            } catch (IllegalArgumentException var6) {
            } catch (UnsupportedEncodingException var7) {
            }
         }
      }

      try {
         return new OutputStreamWriter(output, encoding);
      } catch (IllegalArgumentException var5) {
         throw new UnsupportedEncodingException(encoding);
      }
   }

   static EncodingInfo getEncodingInfo(String encoding) {
      String normalizedEncoding = toUpperCaseFast(encoding);
      EncodingInfo ei = (EncodingInfo)_encodingTableKeyJava.get(normalizedEncoding);
      if (ei == null) {
         ei = (EncodingInfo)_encodingTableKeyMime.get(normalizedEncoding);
      }

      if (ei == null) {
         ei = new EncodingInfo((String)null, (String)null);
      }

      return ei;
   }

   private static String toUpperCaseFast(String s) {
      boolean different = false;
      int mx = s.length();
      char[] chars = new char[mx];

      for(int i = 0; i < mx; ++i) {
         char ch = s.charAt(i);
         if ('a' <= ch && ch <= 'z') {
            ch = (char)(ch + -32);
            different = true;
         }

         chars[i] = ch;
      }

      String upper;
      if (different) {
         upper = String.valueOf(chars);
      } else {
         upper = s;
      }

      return upper;
   }

   static String getMimeEncoding(String encoding) {
      if (null == encoding) {
         try {
            encoding = System.getProperty("file.encoding", "UTF8");
            if (null != encoding) {
               String jencoding = !encoding.equalsIgnoreCase("Cp1252") && !encoding.equalsIgnoreCase("ISO8859_1") && !encoding.equalsIgnoreCase("8859_1") && !encoding.equalsIgnoreCase("UTF8") ? convertJava2MimeEncoding(encoding) : "UTF-8";
               encoding = null != jencoding ? jencoding : "UTF-8";
            } else {
               encoding = "UTF-8";
            }
         } catch (SecurityException var2) {
            encoding = "UTF-8";
         }
      } else {
         encoding = convertJava2MimeEncoding(encoding);
      }

      return encoding;
   }

   private static String convertJava2MimeEncoding(String encoding) {
      EncodingInfo enc = (EncodingInfo)_encodingTableKeyJava.get(toUpperCaseFast(encoding));
      return null != enc ? enc.name : encoding;
   }

   public static String convertMime2JavaEncoding(String encoding) {
      for(int i = 0; i < _encodings.length; ++i) {
         if (_encodings[i].name.equalsIgnoreCase(encoding)) {
            return _encodings[i].javaName;
         }
      }

      return encoding;
   }

   private static EncodingInfo[] loadEncodingInfo() {
      URL url = null;

      try {
         String urlString = null;
         InputStream is = null;

         try {
            urlString = System.getProperty("org.apache.xalan.serialize.encodings", "");
         } catch (SecurityException var17) {
         }

         if (urlString != null && urlString.length() > 0) {
            url = new URL(urlString);
            is = url.openStream();
         }

         if (is == null) {
            SecuritySupport ss = SecuritySupport.getInstance();
            is = ss.getResourceAsStream(ObjectFactory.findClassLoader(), "org/apache/xml/serializer/Encodings.properties");
         }

         Properties props = new Properties();
         if (is != null) {
            props.load(is);
            is.close();
         }

         int totalEntries = props.size();
         int totalMimeNames = 0;
         Enumeration keys = props.keys();

         int i;
         for(int i = 0; i < totalEntries; ++i) {
            String javaName = (String)keys.nextElement();
            String val = props.getProperty(javaName);
            ++totalMimeNames;
            i = val.indexOf(32);

            for(int j = 0; j < i; ++j) {
               if (val.charAt(j) == ',') {
                  ++totalMimeNames;
               }
            }
         }

         EncodingInfo[] ret = new EncodingInfo[totalMimeNames];
         int j = 0;
         keys = props.keys();

         for(i = 0; i < totalEntries; ++i) {
            String javaName = (String)keys.nextElement();
            String val = props.getProperty(javaName);
            int pos = val.indexOf(32);
            if (pos >= 0) {
               StringTokenizer st = new StringTokenizer(val.substring(0, pos), ",");

               for(boolean first = true; st.hasMoreTokens(); first = false) {
                  String mimeName = st.nextToken();
                  ret[j] = new EncodingInfo(mimeName, javaName);
                  _encodingTableKeyMime.put(mimeName.toUpperCase(), ret[j]);
                  if (first) {
                     _encodingTableKeyJava.put(javaName.toUpperCase(), ret[j]);
                  }

                  ++j;
               }
            }
         }

         return ret;
      } catch (MalformedURLException var18) {
         throw new WrappedRuntimeException(var18);
      } catch (IOException var19) {
         throw new WrappedRuntimeException(var19);
      }
   }

   static boolean isHighUTF16Surrogate(char ch) {
      return '\ud800' <= ch && ch <= '\udbff';
   }

   static boolean isLowUTF16Surrogate(char ch) {
      return '\udc00' <= ch && ch <= '\udfff';
   }

   static int toCodePoint(char highSurrogate, char lowSurrogate) {
      int codePoint = (highSurrogate - '\ud800' << 10) + (lowSurrogate - '\udc00') + 65536;
      return codePoint;
   }

   static int toCodePoint(char ch) {
      return ch;
   }
}

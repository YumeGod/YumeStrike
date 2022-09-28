package org.apache.xml.serializer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import javax.xml.transform.TransformerException;
import org.apache.xml.serializer.utils.SystemIDResolver;
import org.apache.xml.serializer.utils.Utils;
import org.apache.xml.serializer.utils.WrappedRuntimeException;

final class CharInfo {
   private Hashtable m_charToString;
   public static final String HTML_ENTITIES_RESOURCE = "org.apache.xml.serializer.HTMLEntities";
   public static final String XML_ENTITIES_RESOURCE = "org.apache.xml.serializer.XMLEntities";
   public static final char S_HORIZONAL_TAB = '\t';
   public static final char S_LINEFEED = '\n';
   public static final char S_CARRIAGERETURN = '\r';
   final boolean onlyQuotAmpLtGt;
   private static final int ASCII_MAX = 128;
   private boolean[] isSpecialAttrASCII;
   private boolean[] isSpecialTextASCII;
   private boolean[] isCleanTextASCII;
   private int[] array_of_bits;
   private static final int SHIFT_PER_WORD = 5;
   private static final int LOW_ORDER_BITMASK = 31;
   private int firstWordNotUsed;
   private CharKey m_charKey;
   private static Hashtable m_getCharInfoCache = new Hashtable();
   // $FF: synthetic field
   static Class class$org$apache$xml$serializer$CharInfo;

   private CharInfo(String entitiesResource, String method) {
      this(entitiesResource, method, false);
   }

   private CharInfo(String entitiesResource, String method, boolean internal) {
      this.m_charToString = new Hashtable();
      this.isSpecialAttrASCII = new boolean[128];
      this.isSpecialTextASCII = new boolean[128];
      this.isCleanTextASCII = new boolean[128];
      this.array_of_bits = this.createEmptySetOfIntegers(65535);
      this.m_charKey = new CharKey();
      ResourceBundle entities = null;
      boolean noExtraEntities = true;
      if (internal) {
         try {
            entities = ResourceBundle.getBundle(entitiesResource);
         } catch (Exception var26) {
         }
      }

      String line;
      int index;
      if (entities != null) {
         Enumeration keys = entities.getKeys();

         while(keys.hasMoreElements()) {
            String name = (String)keys.nextElement();
            line = entities.getString(name);
            index = Integer.parseInt(line);
            this.defineEntity(name, (char)index);
            if (this.extraEntity(index)) {
               noExtraEntities = false;
            }
         }

         this.set(10);
         this.set(13);
      } else {
         InputStream is = null;

         try {
            if (internal) {
               is = (class$org$apache$xml$serializer$CharInfo == null ? (class$org$apache$xml$serializer$CharInfo = class$("org.apache.xml.serializer.CharInfo")) : class$org$apache$xml$serializer$CharInfo).getResourceAsStream(entitiesResource);
            } else {
               ClassLoader cl = ObjectFactory.findClassLoader();
               if (cl == null) {
                  is = ClassLoader.getSystemResourceAsStream(entitiesResource);
               } else {
                  is = cl.getResourceAsStream(entitiesResource);
               }

               if (is == null) {
                  try {
                     URL url = new URL(entitiesResource);
                     is = url.openStream();
                  } catch (Exception var25) {
                  }
               }
            }

            if (is == null) {
               throw new RuntimeException(Utils.messages.createMessage("ER_RESOURCE_COULD_NOT_FIND", new Object[]{entitiesResource, entitiesResource}));
            }

            BufferedReader reader;
            try {
               reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            } catch (UnsupportedEncodingException var24) {
               reader = new BufferedReader(new InputStreamReader(is));
            }

            line = reader.readLine();

            while(true) {
               while(line != null) {
                  if (line.length() != 0 && line.charAt(0) != '#') {
                     index = line.indexOf(32);
                     if (index > 1) {
                        String name = line.substring(0, index);
                        ++index;
                        if (index < line.length()) {
                           String value = line.substring(index);
                           index = value.indexOf(32);
                           if (index > 0) {
                              value = value.substring(0, index);
                           }

                           int code = Integer.parseInt(value);
                           this.defineEntity(name, (char)code);
                           if (this.extraEntity(code)) {
                              noExtraEntities = false;
                           }
                        }
                     }

                     line = reader.readLine();
                  } else {
                     line = reader.readLine();
                  }
               }

               is.close();
               this.set(10);
               this.set(13);
               break;
            }
         } catch (Exception var27) {
            throw new RuntimeException(Utils.messages.createMessage("ER_RESOURCE_COULD_NOT_LOAD", new Object[]{entitiesResource, var27.toString(), entitiesResource, var27.toString()}));
         } finally {
            if (is != null) {
               try {
                  is.close();
               } catch (Exception var23) {
               }
            }

         }
      }

      for(int ch = 0; ch < 128; ++ch) {
         if ((32 > ch && 10 != ch && 13 != ch && 9 != ch || this.get(ch)) && 34 != ch) {
            this.isCleanTextASCII[ch] = false;
            this.isSpecialTextASCII[ch] = true;
         } else {
            this.isCleanTextASCII[ch] = true;
            this.isSpecialTextASCII[ch] = false;
         }
      }

      this.onlyQuotAmpLtGt = noExtraEntities;

      for(int i = 0; i < 128; ++i) {
         this.isSpecialAttrASCII[i] = this.get(i);
      }

      if ("xml".equals(method)) {
         this.isSpecialAttrASCII[9] = true;
      }

   }

   private void defineEntity(String name, char value) {
      StringBuffer sb = new StringBuffer("&");
      sb.append(name);
      sb.append(';');
      String entityString = sb.toString();
      this.defineChar2StringMapping(entityString, value);
   }

   synchronized String getOutputStringForChar(char value) {
      this.m_charKey.setChar(value);
      return (String)this.m_charToString.get(this.m_charKey);
   }

   final boolean isSpecialAttrChar(int value) {
      return value < 128 ? this.isSpecialAttrASCII[value] : this.get(value);
   }

   final boolean isSpecialTextChar(int value) {
      return value < 128 ? this.isSpecialTextASCII[value] : this.get(value);
   }

   final boolean isTextASCIIClean(int value) {
      return this.isCleanTextASCII[value];
   }

   private static CharInfo getCharInfoBasedOnPrivilege(final String entitiesFileName, final String method, final boolean internal) {
      return (CharInfo)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return new CharInfo(entitiesFileName, method, internal);
         }
      });
   }

   static CharInfo getCharInfo(String entitiesFileName, String method) {
      CharInfo charInfo = (CharInfo)m_getCharInfoCache.get(entitiesFileName);
      if (charInfo != null) {
         return charInfo;
      } else {
         try {
            charInfo = getCharInfoBasedOnPrivilege(entitiesFileName, method, true);
            m_getCharInfoCache.put(entitiesFileName, charInfo);
            return charInfo;
         } catch (Exception var7) {
            try {
               return getCharInfoBasedOnPrivilege(entitiesFileName, method, false);
            } catch (Exception var6) {
               String absoluteEntitiesFileName;
               if (entitiesFileName.indexOf(58) < 0) {
                  absoluteEntitiesFileName = SystemIDResolver.getAbsoluteURIFromRelative(entitiesFileName);
               } else {
                  try {
                     absoluteEntitiesFileName = SystemIDResolver.getAbsoluteURI(entitiesFileName, (String)null);
                  } catch (TransformerException var5) {
                     throw new WrappedRuntimeException(var5);
                  }
               }

               return getCharInfoBasedOnPrivilege(entitiesFileName, method, false);
            }
         }
      }
   }

   private static int arrayIndex(int i) {
      return i >> 5;
   }

   private static int bit(int i) {
      int ret = 1 << (i & 31);
      return ret;
   }

   private int[] createEmptySetOfIntegers(int max) {
      this.firstWordNotUsed = 0;
      int[] arr = new int[arrayIndex(max - 1) + 1];
      return arr;
   }

   private final void set(int i) {
      this.setASCIIdirty(i);
      int j = i >> 5;
      int k = j + 1;
      if (this.firstWordNotUsed < k) {
         this.firstWordNotUsed = k;
      }

      int[] var10000 = this.array_of_bits;
      var10000[j] |= 1 << (i & 31);
   }

   private final boolean get(int i) {
      boolean in_the_set = false;
      int j = i >> 5;
      if (j < this.firstWordNotUsed) {
         in_the_set = (this.array_of_bits[j] & 1 << (i & 31)) != 0;
      }

      return in_the_set;
   }

   private boolean extraEntity(int entityValue) {
      boolean extra = false;
      if (entityValue < 128) {
         switch (entityValue) {
            case 34:
            case 38:
            case 60:
            case 62:
               break;
            default:
               extra = true;
         }
      }

      return extra;
   }

   private void setASCIIdirty(int j) {
      if (0 <= j && j < 128) {
         this.isCleanTextASCII[j] = false;
         this.isSpecialTextASCII[j] = true;
      }

   }

   private void setASCIIclean(int j) {
      if (0 <= j && j < 128) {
         this.isCleanTextASCII[j] = true;
         this.isSpecialTextASCII[j] = false;
      }

   }

   private void defineChar2StringMapping(String outputString, char inputChar) {
      CharKey character = new CharKey(inputChar);
      this.m_charToString.put(character, outputString);
      this.set(inputChar);
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   // $FF: synthetic method
   CharInfo(String x0, String x1, boolean x2, Object x3) {
      this(x0, x1, x2);
   }

   private static class CharKey {
      private char m_char;

      public CharKey(char key) {
         this.m_char = key;
      }

      public CharKey() {
      }

      public final void setChar(char c) {
         this.m_char = c;
      }

      public final int hashCode() {
         return this.m_char;
      }

      public final boolean equals(Object obj) {
         return ((CharKey)obj).m_char == this.m_char;
      }
   }
}

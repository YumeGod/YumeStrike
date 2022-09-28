package org.apache.james.mime4j.codec;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.Locale;
import org.apache.james.mime4j.util.CharsetUtil;

public class EncoderUtil {
   private static final byte[] BASE64_TABLE;
   private static final char BASE64_PAD = '=';
   private static final BitSet Q_REGULAR_CHARS;
   private static final BitSet Q_RESTRICTED_CHARS;
   private static final int MAX_USED_CHARACTERS = 50;
   private static final String ENC_WORD_PREFIX = "=?";
   private static final String ENC_WORD_SUFFIX = "?=";
   private static final int ENCODED_WORD_MAX_LENGTH = 75;
   private static final BitSet TOKEN_CHARS;
   private static final BitSet ATEXT_CHARS;

   private static BitSet initChars(String specials) {
      BitSet bs = new BitSet(128);

      for(char ch = '!'; ch < 127; ++ch) {
         if (specials.indexOf(ch) == -1) {
            bs.set(ch);
         }
      }

      return bs;
   }

   private EncoderUtil() {
   }

   public static String encodeAddressDisplayName(String displayName) {
      if (isAtomPhrase(displayName)) {
         return displayName;
      } else {
         return hasToBeEncoded(displayName, 0) ? encodeEncodedWord(displayName, EncoderUtil.Usage.WORD_ENTITY) : quote(displayName);
      }
   }

   public static String encodeAddressLocalPart(String localPart) {
      return isDotAtomText(localPart) ? localPart : quote(localPart);
   }

   public static String encodeHeaderParameter(String name, String value) {
      name = name.toLowerCase(Locale.US);
      return isToken(value) ? name + "=" + value : name + "=" + quote(value);
   }

   public static String encodeIfNecessary(String text, Usage usage, int usedCharacters) {
      return hasToBeEncoded(text, usedCharacters) ? encodeEncodedWord(text, usage, usedCharacters) : text;
   }

   public static boolean hasToBeEncoded(String text, int usedCharacters) {
      if (text == null) {
         throw new IllegalArgumentException();
      } else if (usedCharacters >= 0 && usedCharacters <= 50) {
         int nonWhiteSpaceCount = usedCharacters;

         for(int idx = 0; idx < text.length(); ++idx) {
            char ch = text.charAt(idx);
            if (ch != '\t' && ch != ' ') {
               ++nonWhiteSpaceCount;
               if (nonWhiteSpaceCount > 77) {
                  return true;
               }

               if (ch < ' ' || ch >= 127) {
                  return true;
               }
            } else {
               nonWhiteSpaceCount = 0;
            }
         }

         return false;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public static String encodeEncodedWord(String text, Usage usage) {
      return encodeEncodedWord(text, usage, 0, (Charset)null, (Encoding)null);
   }

   public static String encodeEncodedWord(String text, Usage usage, int usedCharacters) {
      return encodeEncodedWord(text, usage, usedCharacters, (Charset)null, (Encoding)null);
   }

   public static String encodeEncodedWord(String text, Usage usage, int usedCharacters, Charset charset, Encoding encoding) {
      if (text == null) {
         throw new IllegalArgumentException();
      } else if (usedCharacters >= 0 && usedCharacters <= 50) {
         if (charset == null) {
            charset = determineCharset(text);
         }

         byte[] bytes = encode(text, charset);
         if (encoding == null) {
            encoding = determineEncoding(bytes, usage);
         }

         String prefix;
         if (encoding == EncoderUtil.Encoding.B) {
            prefix = "=?" + charset.name() + "?B?";
            return encodeB(prefix, text, usedCharacters, charset, bytes);
         } else {
            prefix = "=?" + charset.name() + "?Q?";
            return encodeQ(prefix, text, usage, usedCharacters, charset, bytes);
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   public static String encodeB(byte[] bytes) {
      StringBuilder sb = new StringBuilder();
      int idx = 0;

      int end;
      int data;
      for(end = bytes.length; idx < end - 2; idx += 3) {
         data = (bytes[idx] & 255) << 16 | (bytes[idx + 1] & 255) << 8 | bytes[idx + 2] & 255;
         sb.append((char)BASE64_TABLE[data >> 18 & 63]);
         sb.append((char)BASE64_TABLE[data >> 12 & 63]);
         sb.append((char)BASE64_TABLE[data >> 6 & 63]);
         sb.append((char)BASE64_TABLE[data & 63]);
      }

      if (idx == end - 2) {
         data = (bytes[idx] & 255) << 16 | (bytes[idx + 1] & 255) << 8;
         sb.append((char)BASE64_TABLE[data >> 18 & 63]);
         sb.append((char)BASE64_TABLE[data >> 12 & 63]);
         sb.append((char)BASE64_TABLE[data >> 6 & 63]);
         sb.append('=');
      } else if (idx == end - 1) {
         data = (bytes[idx] & 255) << 16;
         sb.append((char)BASE64_TABLE[data >> 18 & 63]);
         sb.append((char)BASE64_TABLE[data >> 12 & 63]);
         sb.append('=');
         sb.append('=');
      }

      return sb.toString();
   }

   public static String encodeQ(byte[] bytes, Usage usage) {
      BitSet qChars = usage == EncoderUtil.Usage.TEXT_TOKEN ? Q_REGULAR_CHARS : Q_RESTRICTED_CHARS;
      StringBuilder sb = new StringBuilder();
      int end = bytes.length;

      for(int idx = 0; idx < end; ++idx) {
         int v = bytes[idx] & 255;
         if (v == 32) {
            sb.append('_');
         } else if (!qChars.get(v)) {
            sb.append('=');
            sb.append(hexDigit(v >>> 4));
            sb.append(hexDigit(v & 15));
         } else {
            sb.append((char)v);
         }
      }

      return sb.toString();
   }

   public static boolean isToken(String str) {
      int length = str.length();
      if (length == 0) {
         return false;
      } else {
         for(int idx = 0; idx < length; ++idx) {
            char ch = str.charAt(idx);
            if (!TOKEN_CHARS.get(ch)) {
               return false;
            }
         }

         return true;
      }
   }

   private static boolean isAtomPhrase(String str) {
      boolean containsAText = false;
      int length = str.length();

      for(int idx = 0; idx < length; ++idx) {
         char ch = str.charAt(idx);
         if (ATEXT_CHARS.get(ch)) {
            containsAText = true;
         } else if (!CharsetUtil.isWhitespace(ch)) {
            return false;
         }
      }

      return containsAText;
   }

   private static boolean isDotAtomText(String str) {
      char prev = '.';
      int length = str.length();
      if (length == 0) {
         return false;
      } else {
         for(int idx = 0; idx < length; ++idx) {
            char ch = str.charAt(idx);
            if (ch == '.') {
               if (prev == '.' || idx == length - 1) {
                  return false;
               }
            } else if (!ATEXT_CHARS.get(ch)) {
               return false;
            }

            prev = ch;
         }

         return true;
      }
   }

   private static String quote(String str) {
      String escaped = str.replaceAll("[\\\\\"]", "\\\\$0");
      return "\"" + escaped + "\"";
   }

   private static String encodeB(String prefix, String text, int usedCharacters, Charset charset, byte[] bytes) {
      int encodedLength = bEncodedLength(bytes);
      int totalLength = prefix.length() + encodedLength + "?=".length();
      if (totalLength <= 75 - usedCharacters) {
         return prefix + encodeB(bytes) + "?=";
      } else {
         String part1 = text.substring(0, text.length() / 2);
         byte[] bytes1 = encode(part1, charset);
         String word1 = encodeB(prefix, part1, usedCharacters, charset, bytes1);
         String part2 = text.substring(text.length() / 2);
         byte[] bytes2 = encode(part2, charset);
         String word2 = encodeB(prefix, part2, 0, charset, bytes2);
         return word1 + " " + word2;
      }
   }

   private static int bEncodedLength(byte[] bytes) {
      return (bytes.length + 2) / 3 * 4;
   }

   private static String encodeQ(String prefix, String text, Usage usage, int usedCharacters, Charset charset, byte[] bytes) {
      int encodedLength = qEncodedLength(bytes, usage);
      int totalLength = prefix.length() + encodedLength + "?=".length();
      if (totalLength <= 75 - usedCharacters) {
         return prefix + encodeQ(bytes, usage) + "?=";
      } else {
         String part1 = text.substring(0, text.length() / 2);
         byte[] bytes1 = encode(part1, charset);
         String word1 = encodeQ(prefix, part1, usage, usedCharacters, charset, bytes1);
         String part2 = text.substring(text.length() / 2);
         byte[] bytes2 = encode(part2, charset);
         String word2 = encodeQ(prefix, part2, usage, 0, charset, bytes2);
         return word1 + " " + word2;
      }
   }

   private static int qEncodedLength(byte[] bytes, Usage usage) {
      BitSet qChars = usage == EncoderUtil.Usage.TEXT_TOKEN ? Q_REGULAR_CHARS : Q_RESTRICTED_CHARS;
      int count = 0;

      for(int idx = 0; idx < bytes.length; ++idx) {
         int v = bytes[idx] & 255;
         if (v == 32) {
            ++count;
         } else if (!qChars.get(v)) {
            count += 3;
         } else {
            ++count;
         }
      }

      return count;
   }

   private static byte[] encode(String text, Charset charset) {
      ByteBuffer buffer = charset.encode(text);
      byte[] bytes = new byte[buffer.limit()];
      buffer.get(bytes);
      return bytes;
   }

   private static Charset determineCharset(String text) {
      boolean ascii = true;
      int len = text.length();

      for(int index = 0; index < len; ++index) {
         char ch = text.charAt(index);
         if (ch > 255) {
            return CharsetUtil.UTF_8;
         }

         if (ch > 127) {
            ascii = false;
         }
      }

      return ascii ? CharsetUtil.US_ASCII : CharsetUtil.ISO_8859_1;
   }

   private static Encoding determineEncoding(byte[] bytes, Usage usage) {
      if (bytes.length == 0) {
         return EncoderUtil.Encoding.Q;
      } else {
         BitSet qChars = usage == EncoderUtil.Usage.TEXT_TOKEN ? Q_REGULAR_CHARS : Q_RESTRICTED_CHARS;
         int qEncoded = 0;

         int percentage;
         for(percentage = 0; percentage < bytes.length; ++percentage) {
            int v = bytes[percentage] & 255;
            if (v != 32 && !qChars.get(v)) {
               ++qEncoded;
            }
         }

         percentage = qEncoded * 100 / bytes.length;
         return percentage > 30 ? EncoderUtil.Encoding.B : EncoderUtil.Encoding.Q;
      }
   }

   private static char hexDigit(int i) {
      return i < 10 ? (char)(i + 48) : (char)(i - 10 + 65);
   }

   static {
      BASE64_TABLE = Base64OutputStream.BASE64_TABLE;
      Q_REGULAR_CHARS = initChars("=_?");
      Q_RESTRICTED_CHARS = initChars("=_?\"#$%&'(),.:;<>@[\\]^`{|}~");
      TOKEN_CHARS = initChars("()<>@,;:\\\"/[]?=");
      ATEXT_CHARS = initChars("()<>@.,;:\\\"[]");
   }

   public static enum Usage {
      TEXT_TOKEN,
      WORD_ENTITY;
   }

   public static enum Encoding {
      B,
      Q;
   }
}

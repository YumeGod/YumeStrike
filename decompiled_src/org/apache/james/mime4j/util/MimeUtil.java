package org.apache.james.mime4j.util;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public final class MimeUtil {
   public static final String ENC_QUOTED_PRINTABLE = "quoted-printable";
   public static final String ENC_BINARY = "binary";
   public static final String ENC_BASE64 = "base64";
   public static final String ENC_8BIT = "8bit";
   public static final String ENC_7BIT = "7bit";
   private static final Random random = new Random();
   private static int counter = 0;
   private static final ThreadLocal RFC822_DATE_FORMAT = new ThreadLocal() {
      protected DateFormat initialValue() {
         return new Rfc822DateFormat();
      }
   };

   private MimeUtil() {
   }

   public static boolean isSameMimeType(String pType1, String pType2) {
      return pType1 != null && pType2 != null && pType1.equalsIgnoreCase(pType2);
   }

   public static boolean isMessage(String pMimeType) {
      return pMimeType != null && pMimeType.equalsIgnoreCase("message/rfc822");
   }

   public static boolean isMultipart(String pMimeType) {
      return pMimeType != null && pMimeType.toLowerCase().startsWith("multipart/");
   }

   public static boolean isBase64Encoding(String pTransferEncoding) {
      return "base64".equalsIgnoreCase(pTransferEncoding);
   }

   public static boolean isQuotedPrintableEncoded(String pTransferEncoding) {
      return "quoted-printable".equalsIgnoreCase(pTransferEncoding);
   }

   public static String createUniqueBoundary() {
      StringBuilder sb = new StringBuilder();
      sb.append("-=Part.");
      sb.append(Integer.toHexString(nextCounterValue()));
      sb.append('.');
      sb.append(Long.toHexString(random.nextLong()));
      sb.append('.');
      sb.append(Long.toHexString(System.currentTimeMillis()));
      sb.append('.');
      sb.append(Long.toHexString(random.nextLong()));
      sb.append("=-");
      return sb.toString();
   }

   public static String createUniqueMessageId(String hostName) {
      StringBuilder sb = new StringBuilder("<Mime4j.");
      sb.append(Integer.toHexString(nextCounterValue()));
      sb.append('.');
      sb.append(Long.toHexString(random.nextLong()));
      sb.append('.');
      sb.append(Long.toHexString(System.currentTimeMillis()));
      if (hostName != null) {
         sb.append('@');
         sb.append(hostName);
      }

      sb.append('>');
      return sb.toString();
   }

   public static String formatDate(Date date, TimeZone zone) {
      DateFormat df = (DateFormat)RFC822_DATE_FORMAT.get();
      if (zone == null) {
         df.setTimeZone(TimeZone.getDefault());
      } else {
         df.setTimeZone(zone);
      }

      return df.format(date);
   }

   public static String fold(String s, int usedCharacters) {
      int maxCharacters = true;
      int length = s.length();
      if (usedCharacters + length <= 76) {
         return s;
      } else {
         StringBuilder sb = new StringBuilder();
         int lastLineBreak = -usedCharacters;

         int nextWspIdx;
         for(int wspIdx = indexOfWsp(s, 0); wspIdx != length; wspIdx = nextWspIdx) {
            nextWspIdx = indexOfWsp(s, wspIdx + 1);
            if (nextWspIdx - lastLineBreak > 76) {
               sb.append(s.substring(Math.max(0, lastLineBreak), wspIdx));
               sb.append("\r\n");
               lastLineBreak = wspIdx;
            }
         }

         sb.append(s.substring(Math.max(0, lastLineBreak)));
         return sb.toString();
      }
   }

   public static String unfold(String s) {
      int length = s.length();

      for(int idx = 0; idx < length; ++idx) {
         char c = s.charAt(idx);
         if (c == '\r' || c == '\n') {
            return unfold0(s, idx);
         }
      }

      return s;
   }

   private static String unfold0(String s, int crlfIdx) {
      int length = s.length();
      StringBuilder sb = new StringBuilder(length);
      if (crlfIdx > 0) {
         sb.append(s.substring(0, crlfIdx));
      }

      for(int idx = crlfIdx + 1; idx < length; ++idx) {
         char c = s.charAt(idx);
         if (c != '\r' && c != '\n') {
            sb.append(c);
         }
      }

      return sb.toString();
   }

   private static int indexOfWsp(String s, int fromIndex) {
      int len = s.length();

      for(int index = fromIndex; index < len; ++index) {
         char c = s.charAt(index);
         if (c == ' ' || c == '\t') {
            return index;
         }
      }

      return len;
   }

   private static synchronized int nextCounterValue() {
      return counter++;
   }

   private static final class Rfc822DateFormat extends SimpleDateFormat {
      private static final long serialVersionUID = 1L;

      public Rfc822DateFormat() {
         super("EEE, d MMM yyyy HH:mm:ss ", Locale.US);
      }

      public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
         StringBuffer sb = super.format(date, toAppendTo, pos);
         int zoneMillis = this.calendar.get(15);
         int dstMillis = this.calendar.get(16);
         int minutes = (zoneMillis + dstMillis) / 1000 / 60;
         if (minutes < 0) {
            sb.append('-');
            minutes = -minutes;
         } else {
            sb.append('+');
         }

         sb.append(String.format("%02d%02d", minutes / 60, minutes % 60));
         return sb;
      }
   }
}

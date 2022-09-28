package org.apache.xml.serialize;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.apache.xerces.util.EncodingMap;

public class EncodingInfo {
   private Object[] fArgsForMethod = null;
   String ianaName;
   String javaName;
   int lastPrintable;
   Object fCharsetEncoder = null;
   Object fCharToByteConverter = null;
   boolean fHaveTriedCToB = false;
   boolean fHaveTriedCharsetEncoder = false;

   public EncodingInfo(String var1, String var2, int var3) {
      this.ianaName = var1;
      this.javaName = EncodingMap.getIANA2JavaMapping(var1);
      this.lastPrintable = var3;
   }

   public String getIANAName() {
      return this.ianaName;
   }

   public Writer getWriter(OutputStream var1) throws UnsupportedEncodingException {
      if (this.javaName != null) {
         return new OutputStreamWriter(var1, this.javaName);
      } else {
         this.javaName = EncodingMap.getIANA2JavaMapping(this.ianaName);
         return this.javaName == null ? new OutputStreamWriter(var1, "UTF8") : new OutputStreamWriter(var1, this.javaName);
      }
   }

   public boolean isPrintable(char var1) {
      return var1 <= this.lastPrintable ? true : this.isPrintable0(var1);
   }

   private boolean isPrintable0(char var1) {
      if (this.fCharsetEncoder == null && EncodingInfo.CharsetMethods.fgNIOCharsetAvailable && !this.fHaveTriedCharsetEncoder) {
         if (this.fArgsForMethod == null) {
            this.fArgsForMethod = new Object[1];
         }

         try {
            this.fArgsForMethod[0] = this.javaName;
            Object var2 = EncodingInfo.CharsetMethods.fgCharsetForNameMethod.invoke((Object)null, this.fArgsForMethod);
            if ((Boolean)EncodingInfo.CharsetMethods.fgCharsetCanEncodeMethod.invoke(var2, (Object[])null)) {
               this.fCharsetEncoder = EncodingInfo.CharsetMethods.fgCharsetNewEncoderMethod.invoke(var2, (Object[])null);
            } else {
               this.fHaveTriedCharsetEncoder = true;
            }
         } catch (Exception var5) {
            this.fHaveTriedCharsetEncoder = true;
         }
      }

      if (this.fCharsetEncoder != null) {
         try {
            this.fArgsForMethod[0] = new Character(var1);
            return (Boolean)EncodingInfo.CharsetMethods.fgCharsetEncoderCanEncodeMethod.invoke(this.fCharsetEncoder, this.fArgsForMethod);
         } catch (Exception var6) {
            this.fCharsetEncoder = null;
            this.fHaveTriedCharsetEncoder = false;
         }
      }

      if (this.fCharToByteConverter == null) {
         if (this.fHaveTriedCToB || !EncodingInfo.CharToByteConverterMethods.fgConvertersAvailable) {
            return false;
         }

         if (this.fArgsForMethod == null) {
            this.fArgsForMethod = new Object[1];
         }

         try {
            this.fArgsForMethod[0] = this.javaName;
            this.fCharToByteConverter = EncodingInfo.CharToByteConverterMethods.fgGetConverterMethod.invoke((Object)null, this.fArgsForMethod);
         } catch (Exception var4) {
            this.fHaveTriedCToB = true;
            return false;
         }
      }

      try {
         this.fArgsForMethod[0] = new Character(var1);
         return (Boolean)EncodingInfo.CharToByteConverterMethods.fgCanConvertMethod.invoke(this.fCharToByteConverter, this.fArgsForMethod);
      } catch (Exception var3) {
         this.fCharToByteConverter = null;
         this.fHaveTriedCToB = false;
         return false;
      }
   }

   public static void testJavaEncodingName(String var0) throws UnsupportedEncodingException {
      byte[] var1 = new byte[]{118, 97, 108, 105, 100};
      new String(var1, var0);
   }

   static class CharToByteConverterMethods {
      private static java.lang.reflect.Method fgGetConverterMethod = null;
      private static java.lang.reflect.Method fgCanConvertMethod = null;
      private static boolean fgConvertersAvailable = false;
      // $FF: synthetic field
      static Class class$java$lang$String;

      private CharToByteConverterMethods() {
      }

      // $FF: synthetic method
      static Class class$(String var0) {
         try {
            return Class.forName(var0);
         } catch (ClassNotFoundException var2) {
            throw new NoClassDefFoundError(var2.getMessage());
         }
      }

      static {
         try {
            Class var0 = Class.forName("sun.io.CharToByteConverter");
            fgGetConverterMethod = var0.getMethod("getConverter", class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            fgCanConvertMethod = var0.getMethod("canConvert", Character.TYPE);
            fgConvertersAvailable = true;
         } catch (Exception var1) {
            fgGetConverterMethod = null;
            fgCanConvertMethod = null;
            fgConvertersAvailable = false;
         }

      }
   }

   static class CharsetMethods {
      private static java.lang.reflect.Method fgCharsetForNameMethod = null;
      private static java.lang.reflect.Method fgCharsetCanEncodeMethod = null;
      private static java.lang.reflect.Method fgCharsetNewEncoderMethod = null;
      private static java.lang.reflect.Method fgCharsetEncoderCanEncodeMethod = null;
      private static boolean fgNIOCharsetAvailable = false;
      // $FF: synthetic field
      static Class class$java$lang$String;

      private CharsetMethods() {
      }

      // $FF: synthetic method
      static Class class$(String var0) {
         try {
            return Class.forName(var0);
         } catch (ClassNotFoundException var2) {
            throw new NoClassDefFoundError(var2.getMessage());
         }
      }

      static {
         try {
            Class var0 = Class.forName("java.nio.charset.Charset");
            Class var1 = Class.forName("java.nio.charset.CharsetEncoder");
            fgCharsetForNameMethod = var0.getMethod("forName", class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            fgCharsetCanEncodeMethod = var0.getMethod("canEncode");
            fgCharsetNewEncoderMethod = var0.getMethod("newEncoder");
            fgCharsetEncoderCanEncodeMethod = var1.getMethod("canEncode", Character.TYPE);
            fgNIOCharsetAvailable = true;
         } catch (Exception var2) {
            fgCharsetForNameMethod = null;
            fgCharsetCanEncodeMethod = null;
            fgCharsetEncoderCanEncodeMethod = null;
            fgCharsetNewEncoderMethod = null;
            fgNIOCharsetAvailable = false;
         }

      }
   }
}

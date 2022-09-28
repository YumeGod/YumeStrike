package org.apache.xerces.impl.dv.xs;

import java.io.UnsupportedEncodingException;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.util.URI;

public class AnyURIDV extends TypeValidator {
   private static final URI BASE_URI;
   private static boolean[] gNeedEscaping;
   private static char[] gAfterEscaping1;
   private static char[] gAfterEscaping2;
   private static char[] gHexChs;

   public short getAllowedFacets() {
      return 2079;
   }

   public Object getActualValue(String var1, ValidationContext var2) throws InvalidDatatypeValueException {
      try {
         if (var1.length() != 0) {
            String var3 = encode(var1);
            new URI(BASE_URI, var3);
         }

         return var1;
      } catch (URI.MalformedURIException var4) {
         throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{var1, "anyURI"});
      }
   }

   private static String encode(String var0) {
      int var1 = var0.length();
      StringBuffer var3 = new StringBuffer(var1 * 3);

      int var2;
      int var4;
      for(var4 = 0; var4 < var1; ++var4) {
         var2 = var0.charAt(var4);
         if (var2 >= 128) {
            break;
         }

         if (gNeedEscaping[var2]) {
            var3.append('%');
            var3.append(gAfterEscaping1[var2]);
            var3.append(gAfterEscaping2[var2]);
         } else {
            var3.append((char)var2);
         }
      }

      if (var4 < var1) {
         Object var5 = null;

         byte[] var9;
         try {
            var9 = var0.substring(var4).getBytes("UTF-8");
         } catch (UnsupportedEncodingException var8) {
            return var0;
         }

         var1 = var9.length;

         for(var4 = 0; var4 < var1; ++var4) {
            byte var6 = var9[var4];
            if (var6 < 0) {
               var2 = var6 + 256;
               var3.append('%');
               var3.append(gHexChs[var2 >> 4]);
               var3.append(gHexChs[var2 & 15]);
            } else if (gNeedEscaping[var6]) {
               var3.append('%');
               var3.append(gAfterEscaping1[var6]);
               var3.append(gAfterEscaping2[var6]);
            } else {
               var3.append((char)var6);
            }
         }
      }

      return var3.length() != var1 ? var3.toString() : var0;
   }

   static {
      URI var0 = null;

      try {
         var0 = new URI("abc://def.ghi.jkl");
      } catch (URI.MalformedURIException var5) {
      }

      BASE_URI = var0;
      gNeedEscaping = new boolean[128];
      gAfterEscaping1 = new char[128];
      gAfterEscaping2 = new char[128];
      gHexChs = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

      for(int var6 = 0; var6 <= 31; ++var6) {
         gNeedEscaping[var6] = true;
         gAfterEscaping1[var6] = gHexChs[var6 >> 4];
         gAfterEscaping2[var6] = gHexChs[var6 & 15];
      }

      gNeedEscaping[127] = true;
      gAfterEscaping1[127] = '7';
      gAfterEscaping2[127] = 'F';
      char[] var1 = new char[]{' ', '<', '>', '"', '{', '}', '|', '\\', '^', '~', '`'};
      int var2 = var1.length;

      for(int var4 = 0; var4 < var2; ++var4) {
         char var3 = var1[var4];
         gNeedEscaping[var3] = true;
         gAfterEscaping1[var3] = gHexChs[var3 >> 4];
         gAfterEscaping2[var3] = gHexChs[var3 & 15];
      }

   }
}

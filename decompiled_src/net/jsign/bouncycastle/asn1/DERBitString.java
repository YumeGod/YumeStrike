package net.jsign.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import net.jsign.bouncycastle.util.Arrays;
import net.jsign.bouncycastle.util.io.Streams;

public class DERBitString extends ASN1Primitive implements ASN1String {
   private static final char[] table = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
   protected final byte[] data;
   protected final int padBits;

   protected static int getPadBits(int var0) {
      int var1 = 0;

      int var2;
      for(var2 = 3; var2 >= 0; --var2) {
         if (var2 != 0) {
            if (var0 >> var2 * 8 != 0) {
               var1 = var0 >> var2 * 8 & 255;
               break;
            }
         } else if (var0 != 0) {
            var1 = var0 & 255;
            break;
         }
      }

      if (var1 == 0) {
         return 7;
      } else {
         for(var2 = 1; ((var1 <<= 1) & 255) != 0; ++var2) {
         }

         return 8 - var2;
      }
   }

   protected static byte[] getBytes(int var0) {
      int var1 = 4;

      for(int var2 = 3; var2 >= 1 && (var0 & 255 << var2 * 8) == 0; --var2) {
         --var1;
      }

      byte[] var4 = new byte[var1];

      for(int var3 = 0; var3 < var1; ++var3) {
         var4[var3] = (byte)(var0 >> var3 * 8 & 255);
      }

      return var4;
   }

   public static DERBitString getInstance(Object var0) {
      if (var0 != null && !(var0 instanceof DERBitString)) {
         throw new IllegalArgumentException("illegal object in getInstance: " + var0.getClass().getName());
      } else {
         return (DERBitString)var0;
      }
   }

   public static DERBitString getInstance(ASN1TaggedObject var0, boolean var1) {
      ASN1Primitive var2 = var0.getObject();
      return !var1 && !(var2 instanceof DERBitString) ? fromOctetString(((ASN1OctetString)var2).getOctets()) : getInstance(var2);
   }

   protected DERBitString(byte var1, int var2) {
      this.data = new byte[1];
      this.data[0] = var1;
      this.padBits = var2;
   }

   public DERBitString(byte[] var1, int var2) {
      this.data = var1;
      this.padBits = var2;
   }

   public DERBitString(byte[] var1) {
      this(var1, 0);
   }

   public DERBitString(int var1) {
      this.data = getBytes(var1);
      this.padBits = getPadBits(var1);
   }

   public DERBitString(ASN1Encodable var1) throws IOException {
      this.data = var1.toASN1Primitive().getEncoded("DER");
      this.padBits = 0;
   }

   public byte[] getBytes() {
      return this.data;
   }

   public int getPadBits() {
      return this.padBits;
   }

   public int intValue() {
      int var1 = 0;

      for(int var2 = 0; var2 != this.data.length && var2 != 4; ++var2) {
         var1 |= (this.data[var2] & 255) << 8 * var2;
      }

      return var1;
   }

   boolean isConstructed() {
      return false;
   }

   int encodedLength() {
      return 1 + StreamUtil.calculateBodyLength(this.data.length + 1) + this.data.length + 1;
   }

   void encode(ASN1OutputStream var1) throws IOException {
      byte[] var2 = new byte[this.getBytes().length + 1];
      var2[0] = (byte)this.getPadBits();
      System.arraycopy(this.getBytes(), 0, var2, 1, var2.length - 1);
      var1.writeEncoded(3, var2);
   }

   public int hashCode() {
      return this.padBits ^ Arrays.hashCode(this.data);
   }

   protected boolean asn1Equals(ASN1Primitive var1) {
      if (!(var1 instanceof DERBitString)) {
         return false;
      } else {
         DERBitString var2 = (DERBitString)var1;
         return this.padBits == var2.padBits && Arrays.areEqual(this.data, var2.data);
      }
   }

   public String getString() {
      StringBuffer var1 = new StringBuffer("#");
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      ASN1OutputStream var3 = new ASN1OutputStream(var2);

      try {
         var3.writeObject(this);
      } catch (IOException var6) {
         throw new ASN1ParsingException("Internal error encoding BitString: " + var6.getMessage(), var6);
      }

      byte[] var4 = var2.toByteArray();

      for(int var5 = 0; var5 != var4.length; ++var5) {
         var1.append(table[var4[var5] >>> 4 & 15]);
         var1.append(table[var4[var5] & 15]);
      }

      return var1.toString();
   }

   public String toString() {
      return this.getString();
   }

   static DERBitString fromOctetString(byte[] var0) {
      if (var0.length < 1) {
         throw new IllegalArgumentException("truncated BIT STRING detected");
      } else {
         byte var1 = var0[0];
         byte[] var2 = new byte[var0.length - 1];
         if (var2.length != 0) {
            System.arraycopy(var0, 1, var2, 0, var0.length - 1);
         }

         return new DERBitString(var2, var1);
      }
   }

   static DERBitString fromInputStream(int var0, InputStream var1) throws IOException {
      if (var0 < 1) {
         throw new IllegalArgumentException("truncated BIT STRING detected");
      } else {
         int var2 = var1.read();
         byte[] var3 = new byte[var0 - 1];
         if (var3.length != 0 && Streams.readFully(var1, var3) != var3.length) {
            throw new EOFException("EOF encountered in middle of BIT STRING");
         } else {
            return new DERBitString(var3, var2);
         }
      }
   }
}

package net.jsign;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PVK {
   private static final long PVK_MAGIC = 2964713758L;

   private PVK() {
   }

   public static PrivateKey parse(File file, String password) throws GeneralSecurityException, IOException {
      ByteBuffer buffer = ByteBuffer.allocate((int)file.length());
      FileInputStream in = new FileInputStream(file);

      PrivateKey var4;
      try {
         in.getChannel().read(buffer);
         var4 = parse(buffer, password);
      } finally {
         in.close();
      }

      return var4;
   }

   public static PrivateKey parse(ByteBuffer buffer, String password) throws GeneralSecurityException {
      buffer.order(ByteOrder.LITTLE_ENDIAN);
      buffer.rewind();
      long magic = (long)buffer.getInt() & 4294967295L;
      if (2964713758L != magic) {
         throw new IllegalArgumentException("PVK header signature not found");
      } else {
         int res = buffer.getInt();
         int keyType = buffer.getInt();
         int encrypted = buffer.getInt();
         int saltLength = buffer.getInt();
         int keyLength = buffer.getInt();
         byte[] salt = new byte[saltLength];
         buffer.get(salt);
         byte btype = buffer.get();
         byte version = buffer.get();
         short reserved = buffer.getShort();
         int keyalg = buffer.getInt();
         byte[] key = new byte[keyLength - 8];
         buffer.get(key);
         if (encrypted == 0) {
            return parseKey(key);
         } else {
            try {
               return parseKey(decrypt(key, salt, password, false));
            } catch (IllegalArgumentException var16) {
               return parseKey(decrypt(key, salt, password, true));
            }
         }
      }
   }

   private static byte[] decrypt(byte[] encoded, byte[] salt, String password, boolean weak) throws GeneralSecurityException {
      MessageDigest digest = MessageDigest.getInstance("SHA1");
      digest.update(salt);
      digest.update(password.getBytes());
      byte[] hash = digest.digest();
      if (weak) {
         Arrays.fill(hash, 5, hash.length, (byte)0);
      }

      Cipher rc4 = Cipher.getInstance("RC4");
      rc4.init(2, new SecretKeySpec(hash, 0, 16, "RC4"));
      return rc4.doFinal(encoded);
   }

   private static PrivateKey parseKey(byte[] key) throws GeneralSecurityException {
      ByteBuffer buffer = ByteBuffer.wrap(key);
      buffer.order(ByteOrder.LITTLE_ENDIAN);
      byte[] keymagic = new byte[4];
      buffer.get(keymagic);
      if (!"RSA2".equals(new String(keymagic))) {
         throw new IllegalArgumentException("Unsupported key format: " + new String(keymagic));
      } else {
         int bitlength = buffer.getInt();
         BigInteger publicExponent = new BigInteger(String.valueOf(buffer.getInt()));
         int l = bitlength / 8;
         BigInteger modulus = getBigInteger(buffer, l);
         BigInteger primeP = getBigInteger(buffer, l / 2);
         BigInteger primeQ = getBigInteger(buffer, l / 2);
         BigInteger primeExponentP = getBigInteger(buffer, l / 2);
         BigInteger primeExponentQ = getBigInteger(buffer, l / 2);
         BigInteger crtCoefficient = getBigInteger(buffer, l / 2);
         BigInteger privateExponent = getBigInteger(buffer, l);
         RSAPrivateCrtKeySpec spec = new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient);
         KeyFactory factory = KeyFactory.getInstance("RSA");
         return factory.generatePrivate(spec);
      }
   }

   private static BigInteger getBigInteger(ByteBuffer buffer, int length) {
      byte[] array = new byte[length];
      buffer.get(array);
      byte[] bigintBytes = new byte[length + 1];

      for(int i = 0; i < array.length; ++i) {
         bigintBytes[i + 1] = array[array.length - 1 - i];
      }

      return new BigInteger(bigintBytes);
   }
}

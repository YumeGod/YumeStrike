package org.apache.fop.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class PDFEncryptionJCE extends PDFObject implements PDFEncryption {
   private static final char[] PAD = new char[]{'(', '¿', 'N', '^', 'N', 'u', '\u008a', 'A', 'd', '\u0000', 'N', 'V', 'ÿ', 'ú', '\u0001', '\b', '.', '.', '\u0000', '¶', 'Ð', 'h', '>', '\u0080', '/', '\f', '©', 'þ', 'd', 'S', 'i', 'z'};
   public static final int PERMISSION_PRINT = 4;
   public static final int PERMISSION_EDIT_CONTENT = 8;
   public static final int PERMISSION_COPY_CONTENT = 16;
   public static final int PERMISSION_EDIT_ANNOTATIONS = 32;
   private MessageDigest digest = null;
   private Random random = new Random();
   private PDFEncryptionParams params;
   private byte[] fileID = null;
   private byte[] encryptionKey = null;
   private String dictionary = null;

   public PDFEncryptionJCE(int objnum) {
      this.setObjectNumber(objnum);

      try {
         this.digest = MessageDigest.getInstance("MD5");
      } catch (NoSuchAlgorithmException var3) {
         throw new UnsupportedOperationException(var3.getMessage());
      }
   }

   public static PDFEncryption make(int objnum, PDFEncryptionParams params) {
      PDFEncryptionJCE impl = new PDFEncryptionJCE(objnum);
      impl.setParams(params);
      impl.init();
      return impl;
   }

   public PDFEncryptionParams getParams() {
      return this.params;
   }

   public void setParams(PDFEncryptionParams params) {
      this.params = params;
   }

   private byte[] prepPassword(String password) {
      byte[] obuffer = new byte[32];
      byte[] pbuffer = password.getBytes();
      int i = 0;

      int j;
      for(j = 0; i < obuffer.length && i < pbuffer.length; ++i) {
         obuffer[i] = pbuffer[i];
      }

      while(i < obuffer.length) {
         obuffer[i++] = (byte)PAD[j++];
      }

      return obuffer;
   }

   public byte[] getFileID() {
      if (this.fileID == null) {
         this.fileID = new byte[16];
         this.random.nextBytes(this.fileID);
      }

      return this.fileID;
   }

   public String getFileID(int index) {
      if (index == 1) {
         return PDFText.toHex(this.getFileID());
      } else {
         byte[] id = new byte[16];
         this.random.nextBytes(id);
         return PDFText.toHex(id);
      }
   }

   private byte[] encryptWithKey(byte[] data, byte[] key) {
      try {
         Cipher c = this.initCipher(key);
         return c.doFinal(data);
      } catch (IllegalBlockSizeException var4) {
         throw new IllegalStateException(var4.getMessage());
      } catch (BadPaddingException var5) {
         throw new IllegalStateException(var5.getMessage());
      }
   }

   private Cipher initCipher(byte[] key) {
      try {
         Cipher c = Cipher.getInstance("RC4");
         SecretKeySpec keyspec = new SecretKeySpec(key, "RC4");
         c.init(1, keyspec);
         return c;
      } catch (InvalidKeyException var4) {
         throw new IllegalStateException(var4.getMessage());
      } catch (NoSuchAlgorithmException var5) {
         throw new UnsupportedOperationException(var5.getMessage());
      } catch (NoSuchPaddingException var6) {
         throw new UnsupportedOperationException(var6.getMessage());
      }
   }

   private Cipher initCipher(int number, int generation) {
      byte[] hash = this.calcHash(number, generation);
      int size = hash.length;
      hash = this.digest.digest(hash);
      byte[] key = this.calcKey(hash, size);
      return this.initCipher(key);
   }

   private byte[] encryptWithHash(byte[] data, byte[] hash, int size) {
      hash = this.digest.digest(hash);
      byte[] key = this.calcKey(hash, size);
      return this.encryptWithKey(data, key);
   }

   private byte[] calcKey(byte[] hash, int size) {
      byte[] key = new byte[size];

      for(int i = 0; i < size; ++i) {
         key[i] = hash[i];
      }

      return key;
   }

   public void init() {
      byte[] oValue;
      if (this.params.getOwnerPassword().length() > 0) {
         oValue = this.encryptWithHash(this.prepPassword(this.params.getUserPassword()), this.prepPassword(this.params.getOwnerPassword()), 5);
      } else {
         oValue = this.encryptWithHash(this.prepPassword(this.params.getUserPassword()), this.prepPassword(this.params.getUserPassword()), 5);
      }

      int permissions = -4;
      if (!this.params.isAllowPrint()) {
         permissions -= 4;
      }

      if (!this.params.isAllowCopyContent()) {
         permissions -= 16;
      }

      if (!this.params.isAllowEditContent()) {
         permissions -= 8;
      }

      if (!this.params.isAllowEditAnnotations()) {
         permissions -= 32;
      }

      this.digest.update(this.prepPassword(this.params.getUserPassword()));
      this.digest.update(oValue);
      this.digest.update((byte)(permissions >>> 0));
      this.digest.update((byte)(permissions >>> 8));
      this.digest.update((byte)(permissions >>> 16));
      this.digest.update((byte)(permissions >>> 24));
      this.digest.update(this.getFileID());
      byte[] hash = this.digest.digest();
      this.encryptionKey = new byte[5];

      for(int i = 0; i < 5; ++i) {
         this.encryptionKey[i] = hash[i];
      }

      byte[] uValue = this.encryptWithKey(this.prepPassword(""), this.encryptionKey);
      this.dictionary = this.getObjectID() + "<< /Filter /Standard\n" + "/V 1\n" + "/R 2\n" + "/Length 40\n" + "/P " + permissions + "\n" + "/O " + PDFText.toHex(oValue) + "\n" + "/U " + PDFText.toHex(uValue) + "\n" + ">>\n" + "endobj\n";
   }

   public byte[] encryptData(byte[] data, int number, int generation) {
      if (this.encryptionKey == null) {
         throw new IllegalStateException("PDF Encryption has not been initialized");
      } else {
         byte[] hash = this.calcHash(number, generation);
         return this.encryptWithHash(data, hash, hash.length);
      }
   }

   public byte[] encrypt(byte[] data, PDFObject refObj) {
      PDFObject o;
      for(o = refObj; o != null && !o.hasObjectNumber(); o = o.getParent()) {
      }

      if (o == null) {
         throw new IllegalStateException("No object number could be obtained for a PDF object");
      } else {
         return this.encryptData(data, o.getObjectNumber(), o.getGeneration());
      }
   }

   private byte[] calcHash(int number, int generation) {
      byte[] hash = new byte[this.encryptionKey.length + 5];

      int i;
      for(i = 0; i < this.encryptionKey.length; ++i) {
         hash[i] = this.encryptionKey[i];
      }

      hash[i++] = (byte)(number >>> 0);
      hash[i++] = (byte)(number >>> 8);
      hash[i++] = (byte)(number >>> 16);
      hash[i++] = (byte)(generation >>> 0);
      hash[i++] = (byte)(generation >>> 8);
      return hash;
   }

   public PDFFilter makeFilter(int number, int generation) {
      return new EncryptionFilter(this, number, generation);
   }

   public void applyFilter(AbstractPDFStream stream) {
      stream.getFilterList().addFilter(this.makeFilter(stream.getObjectNumber(), stream.getGeneration()));
   }

   public byte[] toPDF() {
      if (this.dictionary == null) {
         throw new IllegalStateException("PDF Encryption has not been initialized");
      } else {
         return encode(this.dictionary);
      }
   }

   public String getTrailerEntry() {
      return "/Encrypt " + this.getObjectNumber() + " " + this.getGeneration() + " R\n" + "/ID[" + this.getFileID(1) + this.getFileID(2) + "]\n";
   }

   private class EncryptionFilter extends PDFFilter {
      private PDFEncryptionJCE encryption;
      private int number;
      private int generation;

      public EncryptionFilter(PDFEncryptionJCE encryption, int number, int generation) {
         this.encryption = encryption;
         this.number = number;
         this.generation = generation;
         PDFObject.log.debug("new encryption filter for number " + number + " and generation " + generation);
      }

      public String getName() {
         return "";
      }

      public PDFObject getDecodeParms() {
         return null;
      }

      public byte[] encode(byte[] data) {
         return this.encryption.encryptData(data, this.number, this.generation);
      }

      public void encode(InputStream in, OutputStream out, int length) throws IOException {
         byte[] buffer = new byte[length];
         in.read(buffer);
         buffer = this.encode(buffer);
         out.write(buffer);
      }

      public OutputStream applyFilter(OutputStream out) throws IOException {
         return new CipherOutputStream(out, this.encryption.initCipher(this.number, this.generation));
      }
   }
}

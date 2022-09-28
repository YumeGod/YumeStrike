package org.apache.james.mime4j.storage;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class CipherStorageProvider extends AbstractStorageProvider {
   private final StorageProvider backend;
   private final String algorithm;
   private final KeyGenerator keygen;

   public CipherStorageProvider(StorageProvider backend) {
      this(backend, "Blowfish");
   }

   public CipherStorageProvider(StorageProvider backend, String algorithm) {
      if (backend == null) {
         throw new IllegalArgumentException();
      } else {
         try {
            this.backend = backend;
            this.algorithm = algorithm;
            this.keygen = KeyGenerator.getInstance(algorithm);
         } catch (NoSuchAlgorithmException var4) {
            throw new IllegalArgumentException(var4);
         }
      }
   }

   public StorageOutputStream createStorageOutputStream() throws IOException {
      SecretKeySpec skeySpec = this.getSecretKeySpec();
      return new CipherStorageOutputStream(this.backend.createStorageOutputStream(), this.algorithm, skeySpec);
   }

   private SecretKeySpec getSecretKeySpec() {
      byte[] raw = this.keygen.generateKey().getEncoded();
      return new SecretKeySpec(raw, this.algorithm);
   }

   private static final class CipherStorage implements Storage {
      private Storage encrypted;
      private final String algorithm;
      private final SecretKeySpec skeySpec;

      public CipherStorage(Storage encrypted, String algorithm, SecretKeySpec skeySpec) {
         this.encrypted = encrypted;
         this.algorithm = algorithm;
         this.skeySpec = skeySpec;
      }

      public void delete() {
         if (this.encrypted != null) {
            this.encrypted.delete();
            this.encrypted = null;
         }

      }

      public InputStream getInputStream() throws IOException {
         if (this.encrypted == null) {
            throw new IllegalStateException("storage has been deleted");
         } else {
            try {
               Cipher cipher = Cipher.getInstance(this.algorithm);
               cipher.init(2, this.skeySpec);
               InputStream in = this.encrypted.getInputStream();
               return new CipherInputStream(in, cipher);
            } catch (GeneralSecurityException var3) {
               throw (IOException)(new IOException()).initCause(var3);
            }
         }
      }
   }

   private static final class CipherStorageOutputStream extends StorageOutputStream {
      private final StorageOutputStream storageOut;
      private final String algorithm;
      private final SecretKeySpec skeySpec;
      private final CipherOutputStream cipherOut;

      public CipherStorageOutputStream(StorageOutputStream out, String algorithm, SecretKeySpec skeySpec) throws IOException {
         try {
            this.storageOut = out;
            this.algorithm = algorithm;
            this.skeySpec = skeySpec;
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(1, skeySpec);
            this.cipherOut = new CipherOutputStream(out, cipher);
         } catch (GeneralSecurityException var5) {
            throw (IOException)(new IOException()).initCause(var5);
         }
      }

      public void close() throws IOException {
         super.close();
         this.cipherOut.close();
      }

      protected void write0(byte[] buffer, int offset, int length) throws IOException {
         this.cipherOut.write(buffer, offset, length);
      }

      protected Storage toStorage0() throws IOException {
         Storage encrypted = this.storageOut.toStorage();
         return new CipherStorage(encrypted, this.algorithm, this.skeySpec);
      }
   }
}

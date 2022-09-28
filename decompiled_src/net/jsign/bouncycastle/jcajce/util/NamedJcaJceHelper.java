package net.jsign.bouncycastle.jcajce.util;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;

public class NamedJcaJceHelper implements JcaJceHelper {
   protected final String providerName;

   public NamedJcaJceHelper(String var1) {
      this.providerName = var1;
   }

   public Cipher createCipher(String var1) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
      return Cipher.getInstance(var1, this.providerName);
   }

   public Mac createMac(String var1) throws NoSuchAlgorithmException, NoSuchProviderException {
      return Mac.getInstance(var1, this.providerName);
   }

   public KeyAgreement createKeyAgreement(String var1) throws NoSuchAlgorithmException, NoSuchProviderException {
      return KeyAgreement.getInstance(var1, this.providerName);
   }

   public AlgorithmParameterGenerator createAlgorithmParameterGenerator(String var1) throws NoSuchAlgorithmException, NoSuchProviderException {
      return AlgorithmParameterGenerator.getInstance(var1, this.providerName);
   }

   public AlgorithmParameters createAlgorithmParameters(String var1) throws NoSuchAlgorithmException, NoSuchProviderException {
      return AlgorithmParameters.getInstance(var1, this.providerName);
   }

   public KeyGenerator createKeyGenerator(String var1) throws NoSuchAlgorithmException, NoSuchProviderException {
      return KeyGenerator.getInstance(var1, this.providerName);
   }

   public KeyFactory createKeyFactory(String var1) throws NoSuchAlgorithmException, NoSuchProviderException {
      return KeyFactory.getInstance(var1, this.providerName);
   }

   public SecretKeyFactory createSecretKeyFactory(String var1) throws NoSuchAlgorithmException, NoSuchProviderException {
      return SecretKeyFactory.getInstance(var1, this.providerName);
   }

   public KeyPairGenerator createKeyPairGenerator(String var1) throws NoSuchAlgorithmException, NoSuchProviderException {
      return KeyPairGenerator.getInstance(var1, this.providerName);
   }

   public MessageDigest createDigest(String var1) throws NoSuchAlgorithmException, NoSuchProviderException {
      return MessageDigest.getInstance(var1, this.providerName);
   }

   public Signature createSignature(String var1) throws NoSuchAlgorithmException, NoSuchProviderException {
      return Signature.getInstance(var1, this.providerName);
   }

   public CertificateFactory createCertificateFactory(String var1) throws CertificateException, NoSuchProviderException {
      return CertificateFactory.getInstance(var1, this.providerName);
   }
}

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

public interface JcaJceHelper {
   Cipher createCipher(String var1) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException;

   Mac createMac(String var1) throws NoSuchAlgorithmException, NoSuchProviderException;

   KeyAgreement createKeyAgreement(String var1) throws NoSuchAlgorithmException, NoSuchProviderException;

   AlgorithmParameterGenerator createAlgorithmParameterGenerator(String var1) throws NoSuchAlgorithmException, NoSuchProviderException;

   AlgorithmParameters createAlgorithmParameters(String var1) throws NoSuchAlgorithmException, NoSuchProviderException;

   KeyGenerator createKeyGenerator(String var1) throws NoSuchAlgorithmException, NoSuchProviderException;

   KeyFactory createKeyFactory(String var1) throws NoSuchAlgorithmException, NoSuchProviderException;

   SecretKeyFactory createSecretKeyFactory(String var1) throws NoSuchAlgorithmException, NoSuchProviderException;

   KeyPairGenerator createKeyPairGenerator(String var1) throws NoSuchAlgorithmException, NoSuchProviderException;

   MessageDigest createDigest(String var1) throws NoSuchAlgorithmException, NoSuchProviderException;

   Signature createSignature(String var1) throws NoSuchAlgorithmException, NoSuchProviderException;

   CertificateFactory createCertificateFactory(String var1) throws NoSuchProviderException, CertificateException;
}

package net.jsign.bouncycastle.cert.selector;

import java.math.BigInteger;
import net.jsign.bouncycastle.asn1.ASN1OctetString;
import net.jsign.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import net.jsign.bouncycastle.asn1.x500.X500Name;
import net.jsign.bouncycastle.asn1.x509.Extension;
import net.jsign.bouncycastle.cert.X509CertificateHolder;
import net.jsign.bouncycastle.util.Arrays;
import net.jsign.bouncycastle.util.Selector;

public class X509CertificateHolderSelector implements Selector {
   private byte[] subjectKeyId;
   private X500Name issuer;
   private BigInteger serialNumber;

   public X509CertificateHolderSelector(byte[] var1) {
      this((X500Name)null, (BigInteger)null, var1);
   }

   public X509CertificateHolderSelector(X500Name var1, BigInteger var2) {
      this(var1, var2, (byte[])null);
   }

   public X509CertificateHolderSelector(X500Name var1, BigInteger var2, byte[] var3) {
      this.issuer = var1;
      this.serialNumber = var2;
      this.subjectKeyId = var3;
   }

   public X500Name getIssuer() {
      return this.issuer;
   }

   public BigInteger getSerialNumber() {
      return this.serialNumber;
   }

   public byte[] getSubjectKeyIdentifier() {
      return Arrays.clone(this.subjectKeyId);
   }

   public int hashCode() {
      int var1 = Arrays.hashCode(this.subjectKeyId);
      if (this.serialNumber != null) {
         var1 ^= this.serialNumber.hashCode();
      }

      if (this.issuer != null) {
         var1 ^= this.issuer.hashCode();
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof X509CertificateHolderSelector)) {
         return false;
      } else {
         X509CertificateHolderSelector var2 = (X509CertificateHolderSelector)var1;
         return Arrays.areEqual(this.subjectKeyId, var2.subjectKeyId) && this.equalsObj(this.serialNumber, var2.serialNumber) && this.equalsObj(this.issuer, var2.issuer);
      }
   }

   private boolean equalsObj(Object var1, Object var2) {
      return var1 != null ? var1.equals(var2) : var2 == null;
   }

   public boolean match(Object var1) {
      if (var1 instanceof X509CertificateHolder) {
         X509CertificateHolder var2 = (X509CertificateHolder)var1;
         if (this.getSerialNumber() != null) {
            IssuerAndSerialNumber var5 = new IssuerAndSerialNumber(var2.toASN1Structure());
            return var5.getName().equals(this.issuer) && var5.getSerialNumber().getValue().equals(this.serialNumber);
         }

         if (this.subjectKeyId != null) {
            Extension var3 = var2.getExtension(Extension.subjectKeyIdentifier);
            if (var3 == null) {
               return Arrays.areEqual(this.subjectKeyId, MSOutlookKeyIdCalculator.calculateKeyId(var2.getSubjectPublicKeyInfo()));
            }

            byte[] var4 = ASN1OctetString.getInstance(var3.getParsedValue()).getOctets();
            return Arrays.areEqual(this.subjectKeyId, var4);
         }
      } else if (var1 instanceof byte[]) {
         return Arrays.areEqual(this.subjectKeyId, (byte[])((byte[])var1));
      }

      return false;
   }

   public Object clone() {
      return new X509CertificateHolderSelector(this.issuer, this.serialNumber, this.subjectKeyId);
   }
}

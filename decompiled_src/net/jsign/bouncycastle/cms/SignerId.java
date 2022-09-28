package net.jsign.bouncycastle.cms;

import java.math.BigInteger;
import net.jsign.bouncycastle.asn1.x500.X500Name;
import net.jsign.bouncycastle.cert.selector.X509CertificateHolderSelector;
import net.jsign.bouncycastle.util.Selector;

public class SignerId implements Selector {
   private X509CertificateHolderSelector baseSelector;

   private SignerId(X509CertificateHolderSelector var1) {
      this.baseSelector = var1;
   }

   public SignerId(byte[] var1) {
      this((X500Name)null, (BigInteger)null, var1);
   }

   public SignerId(X500Name var1, BigInteger var2) {
      this(var1, var2, (byte[])null);
   }

   public SignerId(X500Name var1, BigInteger var2, byte[] var3) {
      this(new X509CertificateHolderSelector(var1, var2, var3));
   }

   public X500Name getIssuer() {
      return this.baseSelector.getIssuer();
   }

   public BigInteger getSerialNumber() {
      return this.baseSelector.getSerialNumber();
   }

   public byte[] getSubjectKeyIdentifier() {
      return this.baseSelector.getSubjectKeyIdentifier();
   }

   public int hashCode() {
      return this.baseSelector.hashCode();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof SignerId)) {
         return false;
      } else {
         SignerId var2 = (SignerId)var1;
         return this.baseSelector.equals(var2.baseSelector);
      }
   }

   public boolean match(Object var1) {
      return var1 instanceof SignerInformation ? ((SignerInformation)var1).getSID().equals(this) : this.baseSelector.match(var1);
   }

   public Object clone() {
      return new SignerId(this.baseSelector);
   }
}

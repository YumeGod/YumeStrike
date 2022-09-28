package net.jsign.bouncycastle.asn1.ess;

import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1OctetString;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.ASN1Sequence;
import net.jsign.bouncycastle.asn1.DEROctetString;
import net.jsign.bouncycastle.asn1.DERSequence;
import net.jsign.bouncycastle.asn1.x509.IssuerSerial;

public class ESSCertID extends ASN1Object {
   private ASN1OctetString certHash;
   private IssuerSerial issuerSerial;

   public static ESSCertID getInstance(Object var0) {
      if (var0 instanceof ESSCertID) {
         return (ESSCertID)var0;
      } else {
         return var0 != null ? new ESSCertID(ASN1Sequence.getInstance(var0)) : null;
      }
   }

   private ESSCertID(ASN1Sequence var1) {
      if (var1.size() >= 1 && var1.size() <= 2) {
         this.certHash = ASN1OctetString.getInstance(var1.getObjectAt(0));
         if (var1.size() > 1) {
            this.issuerSerial = IssuerSerial.getInstance(var1.getObjectAt(1));
         }

      } else {
         throw new IllegalArgumentException("Bad sequence size: " + var1.size());
      }
   }

   public ESSCertID(byte[] var1) {
      this.certHash = new DEROctetString(var1);
   }

   public ESSCertID(byte[] var1, IssuerSerial var2) {
      this.certHash = new DEROctetString(var1);
      this.issuerSerial = var2;
   }

   public byte[] getCertHash() {
      return this.certHash.getOctets();
   }

   public IssuerSerial getIssuerSerial() {
      return this.issuerSerial;
   }

   public ASN1Primitive toASN1Primitive() {
      ASN1EncodableVector var1 = new ASN1EncodableVector();
      var1.add(this.certHash);
      if (this.issuerSerial != null) {
         var1.add(this.issuerSerial);
      }

      return new DERSequence(var1);
   }
}

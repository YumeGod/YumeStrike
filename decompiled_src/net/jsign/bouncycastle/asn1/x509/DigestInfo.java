package net.jsign.bouncycastle.asn1.x509;

import java.util.Enumeration;
import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1OctetString;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.ASN1Sequence;
import net.jsign.bouncycastle.asn1.ASN1TaggedObject;
import net.jsign.bouncycastle.asn1.DEROctetString;
import net.jsign.bouncycastle.asn1.DERSequence;

public class DigestInfo extends ASN1Object {
   private byte[] digest;
   private AlgorithmIdentifier algId;

   public static DigestInfo getInstance(ASN1TaggedObject var0, boolean var1) {
      return getInstance(ASN1Sequence.getInstance(var0, var1));
   }

   public static DigestInfo getInstance(Object var0) {
      if (var0 instanceof DigestInfo) {
         return (DigestInfo)var0;
      } else {
         return var0 != null ? new DigestInfo(ASN1Sequence.getInstance(var0)) : null;
      }
   }

   public DigestInfo(AlgorithmIdentifier var1, byte[] var2) {
      this.digest = var2;
      this.algId = var1;
   }

   public DigestInfo(ASN1Sequence var1) {
      Enumeration var2 = var1.getObjects();
      this.algId = AlgorithmIdentifier.getInstance(var2.nextElement());
      this.digest = ASN1OctetString.getInstance(var2.nextElement()).getOctets();
   }

   public AlgorithmIdentifier getAlgorithmId() {
      return this.algId;
   }

   public byte[] getDigest() {
      return this.digest;
   }

   public ASN1Primitive toASN1Primitive() {
      ASN1EncodableVector var1 = new ASN1EncodableVector();
      var1.add(this.algId);
      var1.add(new DEROctetString(this.digest));
      return new DERSequence(var1);
   }
}

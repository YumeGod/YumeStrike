package net.jsign.bouncycastle.asn1.tsp;

import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1OctetString;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.ASN1Sequence;
import net.jsign.bouncycastle.asn1.DEROctetString;
import net.jsign.bouncycastle.asn1.DERSequence;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class MessageImprint extends ASN1Object {
   AlgorithmIdentifier hashAlgorithm;
   byte[] hashedMessage;

   public static MessageImprint getInstance(Object var0) {
      if (var0 instanceof MessageImprint) {
         return (MessageImprint)var0;
      } else {
         return var0 != null ? new MessageImprint(ASN1Sequence.getInstance(var0)) : null;
      }
   }

   private MessageImprint(ASN1Sequence var1) {
      this.hashAlgorithm = AlgorithmIdentifier.getInstance(var1.getObjectAt(0));
      this.hashedMessage = ASN1OctetString.getInstance(var1.getObjectAt(1)).getOctets();
   }

   public MessageImprint(AlgorithmIdentifier var1, byte[] var2) {
      this.hashAlgorithm = var1;
      this.hashedMessage = var2;
   }

   public AlgorithmIdentifier getHashAlgorithm() {
      return this.hashAlgorithm;
   }

   public byte[] getHashedMessage() {
      return this.hashedMessage;
   }

   public ASN1Primitive toASN1Primitive() {
      ASN1EncodableVector var1 = new ASN1EncodableVector();
      var1.add(this.hashAlgorithm);
      var1.add(new DEROctetString(this.hashedMessage));
      return new DERSequence(var1);
   }
}

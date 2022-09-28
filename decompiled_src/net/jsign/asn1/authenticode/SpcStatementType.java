package net.jsign.asn1.authenticode;

import net.jsign.bouncycastle.asn1.ASN1Encodable;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.DERSequence;

public class SpcStatementType extends ASN1Object {
   private ASN1ObjectIdentifier identifier;

   public SpcStatementType(ASN1ObjectIdentifier identifier) {
      if (!AuthenticodeObjectIdentifiers.SPC_INDIVIDUAL_SP_KEY_PURPOSE_OBJID.equals(identifier) && !AuthenticodeObjectIdentifiers.SPC_COMMERCIAL_SP_KEY_PURPOSE_OBJID.equals(identifier)) {
         throw new IllegalArgumentException("Invalid id for SpcStatementType : " + identifier);
      } else {
         this.identifier = identifier;
      }
   }

   public ASN1Primitive toASN1Primitive() {
      return new DERSequence(new ASN1Encodable[]{this.identifier});
   }
}

package net.jsign.bouncycastle.asn1.x500;

import net.jsign.bouncycastle.asn1.ASN1Encodable;
import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.ASN1Set;
import net.jsign.bouncycastle.asn1.DERSequence;
import net.jsign.bouncycastle.asn1.DERSet;

public class RDN extends ASN1Object {
   private ASN1Set values;

   private RDN(ASN1Set var1) {
      this.values = var1;
   }

   public static RDN getInstance(Object var0) {
      if (var0 instanceof RDN) {
         return (RDN)var0;
      } else {
         return var0 != null ? new RDN(ASN1Set.getInstance(var0)) : null;
      }
   }

   public RDN(ASN1ObjectIdentifier var1, ASN1Encodable var2) {
      ASN1EncodableVector var3 = new ASN1EncodableVector();
      var3.add(var1);
      var3.add(var2);
      this.values = new DERSet(new DERSequence(var3));
   }

   public RDN(AttributeTypeAndValue var1) {
      this.values = new DERSet(var1);
   }

   public RDN(AttributeTypeAndValue[] var1) {
      this.values = new DERSet(var1);
   }

   public boolean isMultiValued() {
      return this.values.size() > 1;
   }

   public int size() {
      return this.values.size();
   }

   public AttributeTypeAndValue getFirst() {
      return this.values.size() == 0 ? null : AttributeTypeAndValue.getInstance(this.values.getObjectAt(0));
   }

   public AttributeTypeAndValue[] getTypesAndValues() {
      AttributeTypeAndValue[] var1 = new AttributeTypeAndValue[this.values.size()];

      for(int var2 = 0; var2 != var1.length; ++var2) {
         var1[var2] = AttributeTypeAndValue.getInstance(this.values.getObjectAt(var2));
      }

      return var1;
   }

   public ASN1Primitive toASN1Primitive() {
      return this.values;
   }
}

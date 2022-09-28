package net.jsign.bouncycastle.asn1.ocsp;

import java.math.BigInteger;
import net.jsign.bouncycastle.asn1.ASN1Enumerated;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1Primitive;

public class OCSPResponseStatus extends ASN1Object {
   public static final int SUCCESSFUL = 0;
   public static final int MALFORMED_REQUEST = 1;
   public static final int INTERNAL_ERROR = 2;
   public static final int TRY_LATER = 3;
   public static final int SIG_REQUIRED = 5;
   public static final int UNAUTHORIZED = 6;
   private ASN1Enumerated value;

   public OCSPResponseStatus(int var1) {
      this(new ASN1Enumerated(var1));
   }

   private OCSPResponseStatus(ASN1Enumerated var1) {
      this.value = var1;
   }

   public static OCSPResponseStatus getInstance(Object var0) {
      if (var0 instanceof OCSPResponseStatus) {
         return (OCSPResponseStatus)var0;
      } else {
         return var0 != null ? new OCSPResponseStatus(ASN1Enumerated.getInstance(var0)) : null;
      }
   }

   public BigInteger getValue() {
      return this.value.getValue();
   }

   public ASN1Primitive toASN1Primitive() {
      return this.value;
   }
}

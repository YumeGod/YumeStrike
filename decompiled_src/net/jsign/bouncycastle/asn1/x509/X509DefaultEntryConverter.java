package net.jsign.bouncycastle.asn1.x509;

import java.io.IOException;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.DERGeneralizedTime;
import net.jsign.bouncycastle.asn1.DERIA5String;
import net.jsign.bouncycastle.asn1.DERPrintableString;
import net.jsign.bouncycastle.asn1.DERUTF8String;

public class X509DefaultEntryConverter extends X509NameEntryConverter {
   public ASN1Primitive getConvertedValue(ASN1ObjectIdentifier var1, String var2) {
      if (var2.length() != 0 && var2.charAt(0) == '#') {
         try {
            return this.convertHexEncoded(var2, 1);
         } catch (IOException var4) {
            throw new RuntimeException("can't recode value for oid " + var1.getId());
         }
      } else {
         if (var2.length() != 0 && var2.charAt(0) == '\\') {
            var2 = var2.substring(1);
         }

         if (!var1.equals(X509Name.EmailAddress) && !var1.equals(X509Name.DC)) {
            if (var1.equals(X509Name.DATE_OF_BIRTH)) {
               return new DERGeneralizedTime(var2);
            } else {
               return (ASN1Primitive)(!var1.equals(X509Name.C) && !var1.equals(X509Name.SN) && !var1.equals(X509Name.DN_QUALIFIER) && !var1.equals(X509Name.TELEPHONE_NUMBER) ? new DERUTF8String(var2) : new DERPrintableString(var2));
            }
         } else {
            return new DERIA5String(var2);
         }
      }
   }
}

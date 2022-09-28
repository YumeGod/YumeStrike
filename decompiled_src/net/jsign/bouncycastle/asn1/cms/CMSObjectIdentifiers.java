package net.jsign.bouncycastle.asn1.cms;

import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

public interface CMSObjectIdentifiers {
   ASN1ObjectIdentifier data = PKCSObjectIdentifiers.data;
   ASN1ObjectIdentifier signedData = PKCSObjectIdentifiers.signedData;
   ASN1ObjectIdentifier envelopedData = PKCSObjectIdentifiers.envelopedData;
   ASN1ObjectIdentifier signedAndEnvelopedData = PKCSObjectIdentifiers.signedAndEnvelopedData;
   ASN1ObjectIdentifier digestedData = PKCSObjectIdentifiers.digestedData;
   ASN1ObjectIdentifier encryptedData = PKCSObjectIdentifiers.encryptedData;
   ASN1ObjectIdentifier authenticatedData = PKCSObjectIdentifiers.id_ct_authData;
   ASN1ObjectIdentifier compressedData = PKCSObjectIdentifiers.id_ct_compressedData;
   ASN1ObjectIdentifier authEnvelopedData = PKCSObjectIdentifiers.id_ct_authEnvelopedData;
   ASN1ObjectIdentifier timestampedData = PKCSObjectIdentifiers.id_ct_timestampedData;
   ASN1ObjectIdentifier id_ri = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.16");
   ASN1ObjectIdentifier id_ri_ocsp_response = id_ri.branch("2");
   ASN1ObjectIdentifier id_ri_scvp = id_ri.branch("4");
}

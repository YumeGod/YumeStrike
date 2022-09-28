package net.jsign.bouncycastle.cms;

import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface CMSTypedData extends CMSProcessable {
   ASN1ObjectIdentifier getContentType();
}

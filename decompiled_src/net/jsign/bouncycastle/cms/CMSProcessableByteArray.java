package net.jsign.bouncycastle.cms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.cms.CMSObjectIdentifiers;
import net.jsign.bouncycastle.util.Arrays;

public class CMSProcessableByteArray implements CMSTypedData, CMSReadable {
   private final ASN1ObjectIdentifier type;
   private final byte[] bytes;

   public CMSProcessableByteArray(byte[] var1) {
      this(new ASN1ObjectIdentifier(CMSObjectIdentifiers.data.getId()), var1);
   }

   public CMSProcessableByteArray(ASN1ObjectIdentifier var1, byte[] var2) {
      this.type = var1;
      this.bytes = var2;
   }

   public InputStream getInputStream() {
      return new ByteArrayInputStream(this.bytes);
   }

   public void write(OutputStream var1) throws IOException, CMSException {
      var1.write(this.bytes);
   }

   public Object getContent() {
      return Arrays.clone(this.bytes);
   }

   public ASN1ObjectIdentifier getContentType() {
      return this.type;
   }
}

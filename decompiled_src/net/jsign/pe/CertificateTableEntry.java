package net.jsign.pe;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import net.jsign.bouncycastle.asn1.cms.ContentInfo;
import net.jsign.bouncycastle.cms.CMSException;
import net.jsign.bouncycastle.cms.CMSProcessable;
import net.jsign.bouncycastle.cms.CMSSignedData;

public class CertificateTableEntry {
   private int size;
   private int revision;
   private int type;
   private byte[] content;
   private CMSSignedData signature;

   CertificateTableEntry(PEFile peFile, long index) {
      this.size = (int)peFile.readDWord(index, 0);
      this.revision = peFile.readWord(index, 4);
      this.type = peFile.readWord(index, 6);
      this.content = new byte[this.size - 8];
      peFile.read(this.content, index, 8);
   }

   public CertificateTableEntry(CMSSignedData signature) throws IOException {
      this.setSignature(signature);
   }

   public int getSize() {
      return this.size;
   }

   public CMSSignedData getSignature() throws CMSException {
      if (this.type != CertificateType.PKCS_SIGNED_DATA.getValue()) {
         throw new UnsupportedOperationException("Unsupported certificate type: " + this.type);
      } else if (this.revision != 512) {
         throw new UnsupportedOperationException("Unsupported certificate revision: " + this.revision);
      } else {
         if (this.signature == null) {
            this.signature = new CMSSignedData((CMSProcessable)null, ContentInfo.getInstance(this.content));
         }

         return this.signature;
      }
   }

   public void setSignature(CMSSignedData signature) throws IOException {
      this.signature = signature;
      byte[] content = signature.toASN1Structure().getEncoded("DER");
      this.content = this.pad(content, 8);
      this.size = this.content.length + 8;
      this.type = CertificateType.PKCS_SIGNED_DATA.getValue();
   }

   private byte[] pad(byte[] data, int multiple) {
      if (data.length % multiple == 0) {
         return data;
      } else {
         byte[] copy = new byte[data.length + (multiple - data.length % multiple)];
         System.arraycopy(data, 0, copy, 0, data.length);
         return copy;
      }
   }

   public byte[] toBytes() {
      ByteBuffer buffer = ByteBuffer.allocate(this.size);
      buffer.order(ByteOrder.LITTLE_ENDIAN);
      buffer.putInt(buffer.limit());
      buffer.putShort((short)512);
      buffer.putShort(CertificateType.PKCS_SIGNED_DATA.getValue());
      buffer.put(this.content);
      return buffer.array();
   }
}

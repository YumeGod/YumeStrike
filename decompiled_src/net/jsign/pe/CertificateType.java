package net.jsign.pe;

public enum CertificateType {
   X509(1),
   PKCS_SIGNED_DATA(2),
   RESERVED_1(3),
   TS_STACK_SIGNED(4);

   private short value;

   private CertificateType(int value) {
      this.value = (short)value;
   }

   public short getValue() {
      return this.value;
   }
}

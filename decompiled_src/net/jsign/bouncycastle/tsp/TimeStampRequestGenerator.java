package net.jsign.bouncycastle.tsp;

import java.io.IOException;
import java.math.BigInteger;
import net.jsign.bouncycastle.asn1.ASN1Boolean;
import net.jsign.bouncycastle.asn1.ASN1Encodable;
import net.jsign.bouncycastle.asn1.ASN1Integer;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.DERNull;
import net.jsign.bouncycastle.asn1.tsp.MessageImprint;
import net.jsign.bouncycastle.asn1.tsp.TimeStampReq;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;
import net.jsign.bouncycastle.asn1.x509.Extensions;
import net.jsign.bouncycastle.asn1.x509.ExtensionsGenerator;

public class TimeStampRequestGenerator {
   private ASN1ObjectIdentifier reqPolicy;
   private ASN1Boolean certReq;
   private ExtensionsGenerator extGenerator = new ExtensionsGenerator();

   /** @deprecated */
   public void setReqPolicy(String var1) {
      this.reqPolicy = new ASN1ObjectIdentifier(var1);
   }

   public void setReqPolicy(ASN1ObjectIdentifier var1) {
      this.reqPolicy = var1;
   }

   public void setCertReq(boolean var1) {
      this.certReq = ASN1Boolean.getInstance(var1);
   }

   /** @deprecated */
   public void addExtension(String var1, boolean var2, ASN1Encodable var3) throws IOException {
      this.addExtension(var1, var2, var3.toASN1Primitive().getEncoded());
   }

   /** @deprecated */
   public void addExtension(String var1, boolean var2, byte[] var3) {
      this.extGenerator.addExtension(new ASN1ObjectIdentifier(var1), var2, var3);
   }

   public void addExtension(ASN1ObjectIdentifier var1, boolean var2, ASN1Encodable var3) throws TSPIOException {
      TSPUtil.addExtension(this.extGenerator, var1, var2, var3);
   }

   public void addExtension(ASN1ObjectIdentifier var1, boolean var2, byte[] var3) {
      this.extGenerator.addExtension(var1, var2, var3);
   }

   /** @deprecated */
   public TimeStampRequest generate(String var1, byte[] var2) {
      return this.generate((String)var1, var2, (BigInteger)null);
   }

   /** @deprecated */
   public TimeStampRequest generate(String var1, byte[] var2, BigInteger var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("No digest algorithm specified");
      } else {
         ASN1ObjectIdentifier var4 = new ASN1ObjectIdentifier(var1);
         AlgorithmIdentifier var5 = new AlgorithmIdentifier(var4, DERNull.INSTANCE);
         MessageImprint var6 = new MessageImprint(var5, var2);
         Extensions var7 = null;
         if (!this.extGenerator.isEmpty()) {
            var7 = this.extGenerator.generate();
         }

         return var3 != null ? new TimeStampRequest(new TimeStampReq(var6, this.reqPolicy, new ASN1Integer(var3), this.certReq, var7)) : new TimeStampRequest(new TimeStampReq(var6, this.reqPolicy, (ASN1Integer)null, this.certReq, var7));
      }
   }

   public TimeStampRequest generate(ASN1ObjectIdentifier var1, byte[] var2) {
      return this.generate(var1.getId(), var2);
   }

   public TimeStampRequest generate(ASN1ObjectIdentifier var1, byte[] var2, BigInteger var3) {
      return this.generate(var1.getId(), var2, var3);
   }
}

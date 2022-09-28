package net.jsign.bouncycastle.tsp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.jsign.bouncycastle.asn1.ASN1InputStream;
import net.jsign.bouncycastle.asn1.cmp.PKIFailureInfo;
import net.jsign.bouncycastle.asn1.cmp.PKIFreeText;
import net.jsign.bouncycastle.asn1.cms.Attribute;
import net.jsign.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import net.jsign.bouncycastle.asn1.tsp.TimeStampResp;
import net.jsign.bouncycastle.util.Arrays;

public class TimeStampResponse {
   TimeStampResp resp;
   TimeStampToken timeStampToken;

   public TimeStampResponse(TimeStampResp var1) throws TSPException, IOException {
      this.resp = var1;
      if (var1.getTimeStampToken() != null) {
         this.timeStampToken = new TimeStampToken(var1.getTimeStampToken());
      }

   }

   public TimeStampResponse(byte[] var1) throws TSPException, IOException {
      this((InputStream)(new ByteArrayInputStream(var1)));
   }

   public TimeStampResponse(InputStream var1) throws TSPException, IOException {
      this(readTimeStampResp(var1));
   }

   private static TimeStampResp readTimeStampResp(InputStream var0) throws IOException, TSPException {
      try {
         return TimeStampResp.getInstance((new ASN1InputStream(var0)).readObject());
      } catch (IllegalArgumentException var2) {
         throw new TSPException("malformed timestamp response: " + var2, var2);
      } catch (ClassCastException var3) {
         throw new TSPException("malformed timestamp response: " + var3, var3);
      }
   }

   public int getStatus() {
      return this.resp.getStatus().getStatus().intValue();
   }

   public String getStatusString() {
      if (this.resp.getStatus().getStatusString() == null) {
         return null;
      } else {
         StringBuffer var1 = new StringBuffer();
         PKIFreeText var2 = this.resp.getStatus().getStatusString();

         for(int var3 = 0; var3 != var2.size(); ++var3) {
            var1.append(var2.getStringAt(var3).getString());
         }

         return var1.toString();
      }
   }

   public PKIFailureInfo getFailInfo() {
      return this.resp.getStatus().getFailInfo() != null ? new PKIFailureInfo(this.resp.getStatus().getFailInfo()) : null;
   }

   public TimeStampToken getTimeStampToken() {
      return this.timeStampToken;
   }

   public void validate(TimeStampRequest var1) throws TSPException {
      TimeStampToken var2 = this.getTimeStampToken();
      if (var2 != null) {
         TimeStampTokenInfo var3 = var2.getTimeStampInfo();
         if (var1.getNonce() != null && !var1.getNonce().equals(var3.getNonce())) {
            throw new TSPValidationException("response contains wrong nonce value.");
         }

         if (this.getStatus() != 0 && this.getStatus() != 1) {
            throw new TSPValidationException("time stamp token found in failed request.");
         }

         if (!Arrays.constantTimeAreEqual(var1.getMessageImprintDigest(), var3.getMessageImprintDigest())) {
            throw new TSPValidationException("response for different message imprint digest.");
         }

         if (!var3.getMessageImprintAlgOID().equals(var1.getMessageImprintAlgOID())) {
            throw new TSPValidationException("response for different message imprint algorithm.");
         }

         Attribute var4 = var2.getSignedAttributes().get(PKCSObjectIdentifiers.id_aa_signingCertificate);
         Attribute var5 = var2.getSignedAttributes().get(PKCSObjectIdentifiers.id_aa_signingCertificateV2);
         if (var4 == null && var5 == null) {
            throw new TSPValidationException("no signing certificate attribute present.");
         }

         if (var4 != null && var5 != null) {
         }

         if (var1.getReqPolicy() != null && !var1.getReqPolicy().equals(var3.getPolicy())) {
            throw new TSPValidationException("TSA policy wrong for request.");
         }
      } else if (this.getStatus() == 0 || this.getStatus() == 1) {
         throw new TSPValidationException("no time stamp token found and one expected.");
      }

   }

   public byte[] getEncoded() throws IOException {
      return this.resp.getEncoded();
   }
}

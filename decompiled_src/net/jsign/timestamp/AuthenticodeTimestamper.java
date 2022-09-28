package net.jsign.timestamp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Collection;
import net.jsign.DigestAlgorithm;
import net.jsign.asn1.authenticode.AuthenticodeTimeStampRequest;
import net.jsign.bouncycastle.asn1.DERSet;
import net.jsign.bouncycastle.asn1.cms.Attribute;
import net.jsign.bouncycastle.asn1.cms.AttributeTable;
import net.jsign.bouncycastle.asn1.cms.CMSAttributes;
import net.jsign.bouncycastle.cms.CMSSignedData;
import net.jsign.bouncycastle.cms.SignerInformation;
import net.jsign.bouncycastle.util.Selector;
import net.jsign.bouncycastle.util.encoders.Base64;

public class AuthenticodeTimestamper extends Timestamper {
   public AuthenticodeTimestamper() {
      this.setURL("http://timestamp.comodoca.com/authenticode");
   }

   protected CMSSignedData timestamp(DigestAlgorithm algo, byte[] encryptedDigest) throws IOException, TimestampingException {
      AuthenticodeTimeStampRequest timestampRequest = new AuthenticodeTimeStampRequest(encryptedDigest);
      byte[] request = Base64.encode(timestampRequest.getEncoded("DER"));
      HttpURLConnection conn = (HttpURLConnection)this.tsaurl.openConnection();
      conn.setConnectTimeout(10000);
      conn.setReadTimeout(10000);
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setUseCaches(false);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-type", "application/octet-stream");
      conn.setRequestProperty("Content-length", String.valueOf(request.length));
      conn.setRequestProperty("Accept", "application/octet-stream");
      conn.setRequestProperty("User-Agent", "Transport");
      conn.getOutputStream().write(request);
      conn.getOutputStream().flush();
      if (conn.getResponseCode() >= 400) {
         throw new IOException("Unable to complete the timestamping due to HTTP error: " + conn.getResponseCode() + " - " + conn.getResponseMessage());
      } else {
         try {
            byte[] response = Base64.decode(this.toBytes(conn.getInputStream()));
            return new CMSSignedData(response);
         } catch (Exception var7) {
            throw new TimestampingException("Unable to complete the timestamping", var7);
         }
      }
   }

   protected Collection getExtraCertificates(CMSSignedData token) {
      return token.getCertificates().getMatches((Selector)null);
   }

   protected AttributeTable getUnsignedAttributes(CMSSignedData token) {
      SignerInformation timestampSignerInformation = (SignerInformation)token.getSignerInfos().getSigners().iterator().next();
      Attribute counterSignature = new Attribute(CMSAttributes.counterSignature, new DERSet(timestampSignerInformation.toASN1Structure()));
      return new AttributeTable(counterSignature);
   }

   private byte[] toBytes(InputStream in) throws IOException {
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      byte[] buffer = new byte[4096];

      int n;
      while((n = in.read(buffer)) != -1) {
         bout.write(buffer, 0, n);
      }

      return bout.toByteArray();
   }
}

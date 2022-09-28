package net.jsign.timestamp;

import java.io.IOException;
import java.net.HttpURLConnection;
import net.jsign.DigestAlgorithm;
import net.jsign.asn1.authenticode.AuthenticodeObjectIdentifiers;
import net.jsign.bouncycastle.asn1.ASN1InputStream;
import net.jsign.bouncycastle.asn1.DERSet;
import net.jsign.bouncycastle.asn1.cms.Attribute;
import net.jsign.bouncycastle.asn1.cms.AttributeTable;
import net.jsign.bouncycastle.asn1.tsp.TimeStampResp;
import net.jsign.bouncycastle.cms.CMSSignedData;
import net.jsign.bouncycastle.tsp.TimeStampRequest;
import net.jsign.bouncycastle.tsp.TimeStampRequestGenerator;
import net.jsign.bouncycastle.tsp.TimeStampResponse;

public class RFC3161Timestamper extends Timestamper {
   public RFC3161Timestamper() {
      this.setURL("http://timestamp.comodoca.com/rfc3161");
   }

   protected CMSSignedData timestamp(DigestAlgorithm algo, byte[] encryptedDigest) throws IOException, TimestampingException {
      TimeStampRequestGenerator reqgen = new TimeStampRequestGenerator();
      reqgen.setCertReq(true);
      TimeStampRequest req = reqgen.generate(algo.oid, algo.getMessageDigest().digest(encryptedDigest));
      byte[] request = req.getEncoded();
      HttpURLConnection conn = (HttpURLConnection)this.tsaurl.openConnection();
      conn.setConnectTimeout(10000);
      conn.setReadTimeout(10000);
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setUseCaches(false);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-type", "application/timestamp-query");
      conn.setRequestProperty("Content-length", String.valueOf(request.length));
      conn.setRequestProperty("Accept", "application/timestamp-query");
      conn.setRequestProperty("User-Agent", "Transport");
      conn.getOutputStream().write(request);
      conn.getOutputStream().flush();
      if (conn.getResponseCode() >= 400) {
         throw new IOException("Unable to complete the timestamping due to HTTP error: " + conn.getResponseCode() + " - " + conn.getResponseMessage());
      } else {
         try {
            TimeStampResp resp = TimeStampResp.getInstance((new ASN1InputStream(conn.getInputStream())).readObject());
            TimeStampResponse response = new TimeStampResponse(resp);
            response.validate(req);
            if (response.getStatus() != 0) {
               throw new IOException("Unable to complete the timestamping due to an invalid response (" + response.getStatusString() + ")");
            } else {
               return response.getTimeStampToken().toCMSSignedData();
            }
         } catch (Exception var9) {
            throw new TimestampingException("Unable to complete the timestamping", var9);
         }
      }
   }

   protected AttributeTable getUnsignedAttributes(CMSSignedData token) {
      Attribute rfc3161CounterSignature = new Attribute(AuthenticodeObjectIdentifiers.SPC_RFC3161_OBJID, new DERSet(token.toASN1Structure()));
      return new AttributeTable(rfc3161CounterSignature);
   }
}

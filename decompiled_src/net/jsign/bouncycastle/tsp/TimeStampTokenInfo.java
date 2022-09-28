package net.jsign.bouncycastle.tsp;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.tsp.Accuracy;
import net.jsign.bouncycastle.asn1.tsp.TSTInfo;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;
import net.jsign.bouncycastle.asn1.x509.GeneralName;

public class TimeStampTokenInfo {
   TSTInfo tstInfo;
   Date genTime;

   TimeStampTokenInfo(TSTInfo var1) throws TSPException, IOException {
      this.tstInfo = var1;

      try {
         this.genTime = var1.getGenTime().getDate();
      } catch (ParseException var3) {
         throw new TSPException("unable to parse genTime field");
      }
   }

   public boolean isOrdered() {
      return this.tstInfo.getOrdering().isTrue();
   }

   public Accuracy getAccuracy() {
      return this.tstInfo.getAccuracy();
   }

   public Date getGenTime() {
      return this.genTime;
   }

   public GenTimeAccuracy getGenTimeAccuracy() {
      return this.getAccuracy() != null ? new GenTimeAccuracy(this.getAccuracy()) : null;
   }

   public ASN1ObjectIdentifier getPolicy() {
      return this.tstInfo.getPolicy();
   }

   public BigInteger getSerialNumber() {
      return this.tstInfo.getSerialNumber().getValue();
   }

   public GeneralName getTsa() {
      return this.tstInfo.getTsa();
   }

   public BigInteger getNonce() {
      return this.tstInfo.getNonce() != null ? this.tstInfo.getNonce().getValue() : null;
   }

   public AlgorithmIdentifier getHashAlgorithm() {
      return this.tstInfo.getMessageImprint().getHashAlgorithm();
   }

   public ASN1ObjectIdentifier getMessageImprintAlgOID() {
      return this.tstInfo.getMessageImprint().getHashAlgorithm().getAlgorithm();
   }

   public byte[] getMessageImprintDigest() {
      return this.tstInfo.getMessageImprint().getHashedMessage();
   }

   public byte[] getEncoded() throws IOException {
      return this.tstInfo.getEncoded();
   }

   /** @deprecated */
   public TSTInfo toTSTInfo() {
      return this.tstInfo;
   }

   public TSTInfo toASN1Structure() {
      return this.tstInfo;
   }
}

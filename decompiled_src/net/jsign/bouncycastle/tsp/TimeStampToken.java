package net.jsign.bouncycastle.tsp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import net.jsign.bouncycastle.asn1.ASN1InputStream;
import net.jsign.bouncycastle.asn1.cms.Attribute;
import net.jsign.bouncycastle.asn1.cms.AttributeTable;
import net.jsign.bouncycastle.asn1.cms.ContentInfo;
import net.jsign.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import net.jsign.bouncycastle.asn1.ess.ESSCertID;
import net.jsign.bouncycastle.asn1.ess.ESSCertIDv2;
import net.jsign.bouncycastle.asn1.ess.SigningCertificate;
import net.jsign.bouncycastle.asn1.ess.SigningCertificateV2;
import net.jsign.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import net.jsign.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import net.jsign.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import net.jsign.bouncycastle.asn1.tsp.TSTInfo;
import net.jsign.bouncycastle.asn1.x500.X500Name;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;
import net.jsign.bouncycastle.asn1.x509.GeneralName;
import net.jsign.bouncycastle.asn1.x509.IssuerSerial;
import net.jsign.bouncycastle.cert.X509CertificateHolder;
import net.jsign.bouncycastle.cms.CMSException;
import net.jsign.bouncycastle.cms.CMSSignedData;
import net.jsign.bouncycastle.cms.CMSTypedData;
import net.jsign.bouncycastle.cms.SignerId;
import net.jsign.bouncycastle.cms.SignerInformation;
import net.jsign.bouncycastle.cms.SignerInformationVerifier;
import net.jsign.bouncycastle.operator.DigestCalculator;
import net.jsign.bouncycastle.operator.OperatorCreationException;
import net.jsign.bouncycastle.util.Arrays;
import net.jsign.bouncycastle.util.Store;

public class TimeStampToken {
   CMSSignedData tsToken;
   SignerInformation tsaSignerInfo;
   Date genTime;
   TimeStampTokenInfo tstInfo;
   CertID certID;

   public TimeStampToken(ContentInfo var1) throws TSPException, IOException {
      this(getSignedData(var1));
   }

   private static CMSSignedData getSignedData(ContentInfo var0) throws TSPException {
      try {
         return new CMSSignedData(var0);
      } catch (CMSException var2) {
         throw new TSPException("TSP parsing error: " + var2.getMessage(), var2.getCause());
      }
   }

   public TimeStampToken(CMSSignedData var1) throws TSPException, IOException {
      this.tsToken = var1;
      if (!this.tsToken.getSignedContentTypeOID().equals(PKCSObjectIdentifiers.id_ct_TSTInfo.getId())) {
         throw new TSPValidationException("ContentInfo object not for a time stamp.");
      } else {
         Collection var2 = this.tsToken.getSignerInfos().getSigners();
         if (var2.size() != 1) {
            throw new IllegalArgumentException("Time-stamp token signed by " + var2.size() + " signers, but it must contain just the TSA signature.");
         } else {
            this.tsaSignerInfo = (SignerInformation)var2.iterator().next();

            try {
               CMSTypedData var3 = this.tsToken.getSignedContent();
               ByteArrayOutputStream var4 = new ByteArrayOutputStream();
               var3.write(var4);
               ASN1InputStream var5 = new ASN1InputStream(new ByteArrayInputStream(var4.toByteArray()));
               this.tstInfo = new TimeStampTokenInfo(TSTInfo.getInstance(var5.readObject()));
               Attribute var6 = this.tsaSignerInfo.getSignedAttributes().get(PKCSObjectIdentifiers.id_aa_signingCertificate);
               if (var6 != null) {
                  SigningCertificate var7 = SigningCertificate.getInstance(var6.getAttrValues().getObjectAt(0));
                  this.certID = new CertID(ESSCertID.getInstance(var7.getCerts()[0]));
               } else {
                  var6 = this.tsaSignerInfo.getSignedAttributes().get(PKCSObjectIdentifiers.id_aa_signingCertificateV2);
                  if (var6 == null) {
                     throw new TSPValidationException("no signing certificate attribute found, time stamp invalid.");
                  }

                  SigningCertificateV2 var9 = SigningCertificateV2.getInstance(var6.getAttrValues().getObjectAt(0));
                  this.certID = new CertID(ESSCertIDv2.getInstance(var9.getCerts()[0]));
               }

            } catch (CMSException var8) {
               throw new TSPException(var8.getMessage(), var8.getUnderlyingException());
            }
         }
      }
   }

   public TimeStampTokenInfo getTimeStampInfo() {
      return this.tstInfo;
   }

   public SignerId getSID() {
      return this.tsaSignerInfo.getSID();
   }

   public AttributeTable getSignedAttributes() {
      return this.tsaSignerInfo.getSignedAttributes();
   }

   public AttributeTable getUnsignedAttributes() {
      return this.tsaSignerInfo.getUnsignedAttributes();
   }

   public Store getCertificates() {
      return this.tsToken.getCertificates();
   }

   public Store getCRLs() {
      return this.tsToken.getCRLs();
   }

   public Store getAttributeCertificates() {
      return this.tsToken.getAttributeCertificates();
   }

   public void validate(SignerInformationVerifier var1) throws TSPException, TSPValidationException {
      if (!var1.hasAssociatedCertificate()) {
         throw new IllegalArgumentException("verifier provider needs an associated certificate");
      } else {
         try {
            X509CertificateHolder var2 = var1.getAssociatedCertificate();
            DigestCalculator var3 = var1.getDigestCalculator(this.certID.getHashAlgorithm());
            OutputStream var4 = var3.getOutputStream();
            var4.write(var2.getEncoded());
            var4.close();
            if (!Arrays.constantTimeAreEqual(this.certID.getCertHash(), var3.getDigest())) {
               throw new TSPValidationException("certificate hash does not match certID hash.");
            } else {
               if (this.certID.getIssuerSerial() != null) {
                  IssuerAndSerialNumber var5 = new IssuerAndSerialNumber(var2.toASN1Structure());
                  if (!this.certID.getIssuerSerial().getSerial().equals(var5.getSerialNumber())) {
                     throw new TSPValidationException("certificate serial number does not match certID for signature.");
                  }

                  GeneralName[] var6 = this.certID.getIssuerSerial().getIssuer().getNames();
                  boolean var7 = false;

                  for(int var8 = 0; var8 != var6.length; ++var8) {
                     if (var6[var8].getTagNo() == 4 && X500Name.getInstance(var6[var8].getName()).equals(X500Name.getInstance(var5.getName()))) {
                        var7 = true;
                        break;
                     }
                  }

                  if (!var7) {
                     throw new TSPValidationException("certificate name does not match certID for signature. ");
                  }
               }

               TSPUtil.validateCertificate(var2);
               if (!var2.isValidOn(this.tstInfo.getGenTime())) {
                  throw new TSPValidationException("certificate not valid when time stamp created.");
               } else if (!this.tsaSignerInfo.verify(var1)) {
                  throw new TSPValidationException("signature not created by certificate.");
               }
            }
         } catch (CMSException var9) {
            if (var9.getUnderlyingException() != null) {
               throw new TSPException(var9.getMessage(), var9.getUnderlyingException());
            } else {
               throw new TSPException("CMS exception: " + var9, var9);
            }
         } catch (IOException var10) {
            throw new TSPException("problem processing certificate: " + var10, var10);
         } catch (OperatorCreationException var11) {
            throw new TSPException("unable to create digest: " + var11.getMessage(), var11);
         }
      }
   }

   public boolean isSignatureValid(SignerInformationVerifier var1) throws TSPException {
      try {
         return this.tsaSignerInfo.verify(var1);
      } catch (CMSException var3) {
         if (var3.getUnderlyingException() != null) {
            throw new TSPException(var3.getMessage(), var3.getUnderlyingException());
         } else {
            throw new TSPException("CMS exception: " + var3, var3);
         }
      }
   }

   public CMSSignedData toCMSSignedData() {
      return this.tsToken;
   }

   public byte[] getEncoded() throws IOException {
      return this.tsToken.getEncoded();
   }

   private class CertID {
      private ESSCertID certID;
      private ESSCertIDv2 certIDv2;

      CertID(ESSCertID var2) {
         this.certID = var2;
         this.certIDv2 = null;
      }

      CertID(ESSCertIDv2 var2) {
         this.certIDv2 = var2;
         this.certID = null;
      }

      public String getHashAlgorithmName() {
         if (this.certID != null) {
            return "SHA-1";
         } else {
            return NISTObjectIdentifiers.id_sha256.equals(this.certIDv2.getHashAlgorithm().getAlgorithm()) ? "SHA-256" : this.certIDv2.getHashAlgorithm().getAlgorithm().getId();
         }
      }

      public AlgorithmIdentifier getHashAlgorithm() {
         return this.certID != null ? new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1) : this.certIDv2.getHashAlgorithm();
      }

      public byte[] getCertHash() {
         return this.certID != null ? this.certID.getCertHash() : this.certIDv2.getCertHash();
      }

      public IssuerSerial getIssuerSerial() {
         return this.certID != null ? this.certID.getIssuerSerial() : this.certIDv2.getIssuerSerial();
      }
   }
}

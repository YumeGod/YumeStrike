package net.jsign;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import net.jsign.asn1.authenticode.AuthenticodeObjectIdentifiers;
import net.jsign.asn1.authenticode.AuthenticodeSignedDataGenerator;
import net.jsign.asn1.authenticode.SpcAttributeTypeAndOptionalValue;
import net.jsign.asn1.authenticode.SpcIndirectDataContent;
import net.jsign.asn1.authenticode.SpcPeImageData;
import net.jsign.asn1.authenticode.SpcSpOpusInfo;
import net.jsign.asn1.authenticode.SpcStatementType;
import net.jsign.bouncycastle.asn1.ASN1Encodable;
import net.jsign.bouncycastle.asn1.DERNull;
import net.jsign.bouncycastle.asn1.DERSet;
import net.jsign.bouncycastle.asn1.cms.Attribute;
import net.jsign.bouncycastle.asn1.cms.AttributeTable;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;
import net.jsign.bouncycastle.asn1.x509.DigestInfo;
import net.jsign.bouncycastle.cert.X509CertificateHolder;
import net.jsign.bouncycastle.cert.jcajce.JcaCertStore;
import net.jsign.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import net.jsign.bouncycastle.cms.CMSAttributeTableGenerator;
import net.jsign.bouncycastle.cms.CMSException;
import net.jsign.bouncycastle.cms.CMSSignedData;
import net.jsign.bouncycastle.cms.DefaultSignedAttributeTableGenerator;
import net.jsign.bouncycastle.cms.SignerInfoGenerator;
import net.jsign.bouncycastle.cms.SignerInfoGeneratorBuilder;
import net.jsign.bouncycastle.operator.ContentSigner;
import net.jsign.bouncycastle.operator.DigestCalculatorProvider;
import net.jsign.bouncycastle.operator.OperatorCreationException;
import net.jsign.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import net.jsign.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import net.jsign.pe.CertificateTableEntry;
import net.jsign.pe.DataDirectoryType;
import net.jsign.pe.PEFile;
import net.jsign.timestamp.Timestamper;
import net.jsign.timestamp.TimestampingMode;

public class PESigner {
   private Certificate[] chain;
   private PrivateKey privateKey;
   private DigestAlgorithm algo;
   private String programName;
   private String programURL;
   private boolean timestamping;
   private TimestampingMode tsmode;
   private String tsaurlOverride;
   private Timestamper timestamper;

   public PESigner(Certificate[] chain, PrivateKey privateKey) {
      this.algo = DigestAlgorithm.getDefault();
      this.timestamping = true;
      this.tsmode = TimestampingMode.AUTHENTICODE;
      this.chain = chain;
      this.privateKey = privateKey;
   }

   public PESigner(KeyStore keystore, String alias, String password) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
      this(keystore.getCertificateChain(alias), (PrivateKey)keystore.getKey(alias, password.toCharArray()));
   }

   public PESigner withProgramName(String programName) {
      this.programName = programName;
      return this;
   }

   public PESigner withProgramURL(String programURL) {
      this.programURL = programURL;
      return this;
   }

   public PESigner withTimestamping(boolean timestamping) {
      this.timestamping = timestamping;
      return this;
   }

   public PESigner withTimestampingMode(TimestampingMode tsmode) {
      this.tsmode = tsmode;
      return this;
   }

   public PESigner withTimestampingAutority(String url) {
      this.tsaurlOverride = url;
      return this;
   }

   public PESigner withTimestamper(Timestamper timestamper) {
      this.timestamper = timestamper;
      return this;
   }

   public PESigner withDigestAlgorithm(DigestAlgorithm algorithm) {
      if (algorithm != null) {
         this.algo = algorithm;
      }

      return this;
   }

   public void sign(PEFile file) throws Exception {
      file.pad(8);
      CertificateTableEntry entry = this.createCertificateTableEntry(file);
      file.writeDataDirectory(DataDirectoryType.CERTIFICATE_TABLE, entry.toBytes());
      file.close();
   }

   private CertificateTableEntry createCertificateTableEntry(PEFile file) throws IOException, CMSException, OperatorCreationException, CertificateEncodingException {
      CMSSignedData sigData = this.createSignature(file);
      if (this.timestamping) {
         Timestamper ts = this.timestamper;
         if (ts == null) {
            ts = Timestamper.create(this.tsmode);
         }

         if (this.tsaurlOverride != null) {
            ts.setURL(this.tsaurlOverride);
         }

         sigData = ts.timestamp(this.algo, sigData);
      }

      return new CertificateTableEntry(sigData);
   }

   private CMSSignedData createSignature(PEFile file) throws IOException, CMSException, OperatorCreationException, CertificateEncodingException {
      byte[] sha = file.computeDigest(this.algo);
      AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(this.algo.oid, DERNull.INSTANCE);
      DigestInfo digestInfo = new DigestInfo(algorithmIdentifier, sha);
      SpcAttributeTypeAndOptionalValue data = new SpcAttributeTypeAndOptionalValue(AuthenticodeObjectIdentifiers.SPC_PE_IMAGE_DATA_OBJID, new SpcPeImageData());
      SpcIndirectDataContent spcIndirectDataContent = new SpcIndirectDataContent(data, digestInfo);
      ContentSigner shaSigner = (new JcaContentSignerBuilder(this.algo + "with" + this.privateKey.getAlgorithm())).build(this.privateKey);
      DigestCalculatorProvider digestCalculatorProvider = (new JcaDigestCalculatorProviderBuilder()).build();
      CMSAttributeTableGenerator attributeTableGenerator = new DefaultSignedAttributeTableGenerator(this.createAuthenticatedAttributes());
      X509CertificateHolder certificate = new JcaX509CertificateHolder((X509Certificate)this.chain[0]);
      SignerInfoGeneratorBuilder signerInfoGeneratorBuilder = new SignerInfoGeneratorBuilder(digestCalculatorProvider);
      signerInfoGeneratorBuilder.setSignedAttributeGenerator(attributeTableGenerator);
      SignerInfoGenerator signerInfoGenerator = signerInfoGeneratorBuilder.build(shaSigner, (X509CertificateHolder)certificate);
      AuthenticodeSignedDataGenerator generator = new AuthenticodeSignedDataGenerator();
      generator.addCertificates(new JcaCertStore(this.removeRoot(this.chain)));
      generator.addSignerInfoGenerator(signerInfoGenerator);
      return generator.generate(AuthenticodeObjectIdentifiers.SPC_INDIRECT_DATA_OBJID, spcIndirectDataContent);
   }

   private List removeRoot(Certificate[] certificates) {
      List list = new ArrayList();
      if (certificates.length == 1) {
         list.add(certificates[0]);
      } else {
         Certificate[] arr$ = certificates;
         int len$ = certificates.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Certificate certificate = arr$[i$];
            if (!this.isSelfSigned((X509Certificate)certificate)) {
               list.add(certificate);
            }
         }
      }

      return list;
   }

   private boolean isSelfSigned(X509Certificate certificate) {
      return certificate.getSubjectDN().equals(certificate.getIssuerDN());
   }

   private AttributeTable createAuthenticatedAttributes() {
      List attributes = new ArrayList();
      SpcStatementType spcStatementType = new SpcStatementType(AuthenticodeObjectIdentifiers.SPC_INDIVIDUAL_SP_KEY_PURPOSE_OBJID);
      attributes.add(new Attribute(AuthenticodeObjectIdentifiers.SPC_STATEMENT_TYPE_OBJID, new DERSet(spcStatementType)));
      if (this.programName != null || this.programURL != null) {
         SpcSpOpusInfo spcSpOpusInfo = new SpcSpOpusInfo(this.programName, this.programURL);
         attributes.add(new Attribute(AuthenticodeObjectIdentifiers.SPC_SP_OPUS_INFO_OBJID, new DERSet(spcSpOpusInfo)));
      }

      return new AttributeTable(new DERSet((ASN1Encodable[])attributes.toArray(new ASN1Encodable[attributes.size()])));
   }
}

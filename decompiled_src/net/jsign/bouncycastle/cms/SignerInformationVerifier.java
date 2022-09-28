package net.jsign.bouncycastle.cms;

import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;
import net.jsign.bouncycastle.cert.X509CertificateHolder;
import net.jsign.bouncycastle.operator.ContentVerifier;
import net.jsign.bouncycastle.operator.ContentVerifierProvider;
import net.jsign.bouncycastle.operator.DigestCalculator;
import net.jsign.bouncycastle.operator.DigestCalculatorProvider;
import net.jsign.bouncycastle.operator.OperatorCreationException;
import net.jsign.bouncycastle.operator.SignatureAlgorithmIdentifierFinder;

public class SignerInformationVerifier {
   private ContentVerifierProvider verifierProvider;
   private DigestCalculatorProvider digestProvider;
   private SignatureAlgorithmIdentifierFinder sigAlgorithmFinder;
   private CMSSignatureAlgorithmNameGenerator sigNameGenerator;

   public SignerInformationVerifier(CMSSignatureAlgorithmNameGenerator var1, SignatureAlgorithmIdentifierFinder var2, ContentVerifierProvider var3, DigestCalculatorProvider var4) {
      this.sigNameGenerator = var1;
      this.sigAlgorithmFinder = var2;
      this.verifierProvider = var3;
      this.digestProvider = var4;
   }

   public boolean hasAssociatedCertificate() {
      return this.verifierProvider.hasAssociatedCertificate();
   }

   public X509CertificateHolder getAssociatedCertificate() {
      return this.verifierProvider.getAssociatedCertificate();
   }

   public ContentVerifier getContentVerifier(AlgorithmIdentifier var1, AlgorithmIdentifier var2) throws OperatorCreationException {
      String var3 = this.sigNameGenerator.getSignatureName(var2, var1);
      return this.verifierProvider.get(this.sigAlgorithmFinder.find(var3));
   }

   public DigestCalculator getDigestCalculator(AlgorithmIdentifier var1) throws OperatorCreationException {
      return this.digestProvider.get(var1);
   }
}

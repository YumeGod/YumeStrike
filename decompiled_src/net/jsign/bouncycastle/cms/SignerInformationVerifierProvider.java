package net.jsign.bouncycastle.cms;

import net.jsign.bouncycastle.operator.OperatorCreationException;

public interface SignerInformationVerifierProvider {
   SignerInformationVerifier get(SignerId var1) throws OperatorCreationException;
}

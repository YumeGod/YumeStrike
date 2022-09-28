package org.apache.bcel.verifier.statics;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.verifier.PassVerifier;
import org.apache.bcel.verifier.VerificationResult;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.exc.LoadingException;
import org.apache.bcel.verifier.exc.Utility;

public final class Pass1Verifier extends PassVerifier {
   private JavaClass jc;
   private Verifier myOwner;

   private JavaClass getJavaClass() {
      if (this.jc == null) {
         this.jc = Repository.lookupClass(this.myOwner.getClassName());
      }

      return this.jc;
   }

   public Pass1Verifier(Verifier owner) {
      this.myOwner = owner;
   }

   public VerificationResult do_verify() {
      JavaClass jc;
      try {
         jc = this.getJavaClass();
         if (jc != null && !this.myOwner.getClassName().equals(jc.getClassName())) {
            throw new LoadingException("Wrong name: the internal name of the .class file '" + jc.getClassName() + "' does not match the file's name '" + this.myOwner.getClassName() + "'.");
         }
      } catch (LoadingException var5) {
         return new VerificationResult(2, var5.getMessage());
      } catch (ClassFormatError var6) {
         return new VerificationResult(2, var6.getMessage());
      } catch (RuntimeException var7) {
         return new VerificationResult(2, "Parsing via BCEL did not succeed. " + var7.getClass().getName() + " occured:\n" + Utility.getStackTrace(var7));
      }

      return jc != null ? VerificationResult.VR_OK : new VerificationResult(2, "Repository.lookup() failed. FILE NOT FOUND?");
   }

   public String[] getMessages() {
      return super.getMessages();
   }
}

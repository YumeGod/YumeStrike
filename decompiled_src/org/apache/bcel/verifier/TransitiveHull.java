package org.apache.bcel.verifier;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;

public class TransitiveHull implements VerifierFactoryObserver {
   private int indent = 0;

   private TransitiveHull() {
   }

   public void update(String classname) {
      System.gc();

      for(int i = 0; i < this.indent; ++i) {
         System.out.print(" ");
      }

      System.out.println(classname);
      ++this.indent;
      Verifier v = VerifierFactory.getVerifier(classname);
      VerificationResult vr = v.doPass1();
      if (vr != VerificationResult.VR_OK) {
         System.out.println("Pass 1:\n" + vr);
      }

      vr = v.doPass2();
      if (vr != VerificationResult.VR_OK) {
         System.out.println("Pass 2:\n" + vr);
      }

      if (vr == VerificationResult.VR_OK) {
         JavaClass jc = Repository.lookupClass(v.getClassName());

         for(int i = 0; i < jc.getMethods().length; ++i) {
            vr = v.doPass3a(i);
            if (vr != VerificationResult.VR_OK) {
               System.out.println(v.getClassName() + ", Pass 3a, method " + i + " ['" + jc.getMethods()[i] + "']:\n" + vr);
            }

            vr = v.doPass3b(i);
            if (vr != VerificationResult.VR_OK) {
               System.out.println(v.getClassName() + ", Pass 3b, method " + i + " ['" + jc.getMethods()[i] + "']:\n" + vr);
            }
         }
      }

      --this.indent;
   }

   public static void main(String[] args) {
      if (args.length != 1) {
         System.out.println("Need exactly one argument: The root class to verify.");
         System.exit(1);
      }

      int dotclasspos = args[0].lastIndexOf(".class");
      if (dotclasspos != -1) {
         args[0] = args[0].substring(0, dotclasspos);
      }

      args[0] = args[0].replace('/', '.');
      TransitiveHull th = new TransitiveHull();
      VerifierFactory.attach(th);
      VerifierFactory.getVerifier(args[0]);
      VerifierFactory.detach(th);
   }
}

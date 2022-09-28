package org.apache.bcel.verifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.verifier.statics.Pass1Verifier;
import org.apache.bcel.verifier.statics.Pass2Verifier;
import org.apache.bcel.verifier.statics.Pass3aVerifier;
import org.apache.bcel.verifier.structurals.Pass3bVerifier;

public class Verifier {
   private final String classname;
   private Pass1Verifier p1v;
   private Pass2Verifier p2v;
   private HashMap p3avs = new HashMap();
   private HashMap p3bvs = new HashMap();

   public VerificationResult doPass1() {
      if (this.p1v == null) {
         this.p1v = new Pass1Verifier(this);
      }

      return this.p1v.verify();
   }

   public VerificationResult doPass2() {
      if (this.p2v == null) {
         this.p2v = new Pass2Verifier(this);
      }

      return this.p2v.verify();
   }

   public VerificationResult doPass3a(int method_no) {
      String key = Integer.toString(method_no);
      Pass3aVerifier p3av = (Pass3aVerifier)this.p3avs.get(key);
      if (this.p3avs.get(key) == null) {
         p3av = new Pass3aVerifier(this, method_no);
         this.p3avs.put(key, p3av);
      }

      return p3av.verify();
   }

   public VerificationResult doPass3b(int method_no) {
      String key = Integer.toString(method_no);
      Pass3bVerifier p3bv = (Pass3bVerifier)this.p3bvs.get(key);
      if (this.p3bvs.get(key) == null) {
         p3bv = new Pass3bVerifier(this, method_no);
         this.p3bvs.put(key, p3bv);
      }

      return p3bv.verify();
   }

   private Verifier() {
      this.classname = "";
   }

   Verifier(String fully_qualified_classname) {
      this.classname = fully_qualified_classname;
      this.flush();
   }

   public final String getClassName() {
      return this.classname;
   }

   public void flush() {
      this.p1v = null;
      this.p2v = null;
      this.p3avs.clear();
      this.p3bvs.clear();
   }

   public String[] getMessages() {
      ArrayList messages = new ArrayList();
      String[] p2m;
      int i;
      if (this.p1v != null) {
         p2m = this.p1v.getMessages();

         for(i = 0; i < p2m.length; ++i) {
            messages.add("Pass 1: " + p2m[i]);
         }
      }

      if (this.p2v != null) {
         p2m = this.p2v.getMessages();

         for(i = 0; i < p2m.length; ++i) {
            messages.add("Pass 2: " + p2m[i]);
         }
      }

      Iterator p3as = this.p3avs.values().iterator();

      String[] ret;
      int i;
      int meth;
      while(p3as.hasNext()) {
         Pass3aVerifier pv = (Pass3aVerifier)p3as.next();
         ret = pv.getMessages();
         i = pv.getMethodNo();

         for(meth = 0; meth < ret.length; ++meth) {
            messages.add("Pass 3a, method " + i + " ('" + Repository.lookupClass(this.classname).getMethods()[i] + "'): " + ret[meth]);
         }
      }

      Iterator p3bs = this.p3bvs.values().iterator();

      while(p3bs.hasNext()) {
         Pass3bVerifier pv = (Pass3bVerifier)p3bs.next();
         String[] p3bm = pv.getMessages();
         meth = pv.getMethodNo();

         for(int i = 0; i < p3bm.length; ++i) {
            messages.add("Pass 3b, method " + meth + " ('" + Repository.lookupClass(this.classname).getMethods()[meth] + "'): " + p3bm[i]);
         }
      }

      ret = new String[messages.size()];

      for(i = 0; i < messages.size(); ++i) {
         ret[i] = (String)messages.get(i);
      }

      return ret;
   }

   public static void main(String[] args) {
      System.out.println("JustIce by Enver Haase, (C) 2001. http://bcel.sourceforge.net\n");

      for(int k = 0; k < args.length; ++k) {
         if (args[k].endsWith(".class")) {
            int dotclasspos = args[k].lastIndexOf(".class");
            if (dotclasspos != -1) {
               args[k] = args[k].substring(0, dotclasspos);
            }
         }

         args[k] = args[k].replace('/', '.');
         System.out.println("Now verifiying: " + args[k] + "\n");
         Verifier v = VerifierFactory.getVerifier(args[k]);
         VerificationResult vr = v.doPass1();
         System.out.println("Pass 1:\n" + vr);
         vr = v.doPass2();
         System.out.println("Pass 2:\n" + vr);
         int j;
         if (vr == VerificationResult.VR_OK) {
            JavaClass jc = Repository.lookupClass(args[k]);

            for(j = 0; j < jc.getMethods().length; ++j) {
               vr = v.doPass3a(j);
               System.out.println("Pass 3a, method " + j + " ['" + jc.getMethods()[j] + "']:\n" + vr);
               vr = v.doPass3b(j);
               System.out.println("Pass 3b, method number " + j + " ['" + jc.getMethods()[j] + "']:\n" + vr);
            }
         }

         System.out.println("Warnings:");
         String[] warnings = v.getMessages();
         if (warnings.length == 0) {
            System.out.println("<none>");
         }

         for(j = 0; j < warnings.length; ++j) {
            System.out.println(warnings[j]);
         }

         System.out.println("\n");
         v.flush();
         Repository.clearCache();
         System.gc();
      }

   }
}

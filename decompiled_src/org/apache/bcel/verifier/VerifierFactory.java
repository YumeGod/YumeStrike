package org.apache.bcel.verifier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class VerifierFactory {
   private static HashMap hashMap = new HashMap();
   private static Vector observers = new Vector();

   private VerifierFactory() {
   }

   public static Verifier getVerifier(String fully_qualified_classname) {
      Verifier v = (Verifier)hashMap.get(fully_qualified_classname);
      if (v == null) {
         v = new Verifier(fully_qualified_classname);
         hashMap.put(fully_qualified_classname, v);
         notify(fully_qualified_classname);
      }

      return v;
   }

   private static void notify(String fully_qualified_classname) {
      Iterator i = observers.iterator();

      while(i.hasNext()) {
         VerifierFactoryObserver vfo = (VerifierFactoryObserver)i.next();
         vfo.update(fully_qualified_classname);
      }

   }

   public static Verifier[] getVerifiers() {
      Verifier[] vs = new Verifier[hashMap.values().size()];
      return (Verifier[])hashMap.values().toArray(vs);
   }

   public static void attach(VerifierFactoryObserver o) {
      observers.addElement(o);
   }

   public static void detach(VerifierFactoryObserver o) {
      observers.removeElement(o);
   }
}

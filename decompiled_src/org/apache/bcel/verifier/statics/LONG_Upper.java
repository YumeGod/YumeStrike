package org.apache.bcel.verifier.statics;

import org.apache.bcel.generic.Type;

public final class LONG_Upper extends Type {
   private static LONG_Upper singleInstance = new LONG_Upper();

   private LONG_Upper() {
      super((byte)15, "Long_Upper");
   }

   public static LONG_Upper theInstance() {
      return singleInstance;
   }
}

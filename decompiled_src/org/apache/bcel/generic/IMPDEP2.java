package org.apache.bcel.generic;

public class IMPDEP2 extends Instruction {
   public IMPDEP2() {
      super((short)255, (short)1);
   }

   public void accept(Visitor v) {
      v.visitIMPDEP2(this);
   }
}

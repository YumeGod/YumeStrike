package org.apache.bcel.generic;

public class IMPDEP1 extends Instruction {
   public IMPDEP1() {
      super((short)254, (short)1);
   }

   public void accept(Visitor v) {
      v.visitIMPDEP1(this);
   }
}

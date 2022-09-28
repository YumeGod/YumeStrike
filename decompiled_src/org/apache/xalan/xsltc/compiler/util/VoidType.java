package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;

public final class VoidType extends Type {
   protected VoidType() {
   }

   public String toString() {
      return "void";
   }

   public boolean identicalTo(Type other) {
      return this == other;
   }

   public String toSignature() {
      return "V";
   }

   public org.apache.bcel.generic.Type toJCType() {
      return null;
   }

   public Instruction POP() {
      return InstructionConstants.NOP;
   }

   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type) {
      if (type == Type.String) {
         this.translateTo(classGen, methodGen, (StringType)type);
      } else {
         ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", this.toString(), type.toString());
         classGen.getParser().reportError(2, err);
      }

   }

   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type) {
      InstructionList il = methodGen.getInstructionList();
      il.append((CompoundInstruction)(new PUSH(classGen.getConstantPool(), "")));
   }

   public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
      if (!clazz.getName().equals("void")) {
         ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", this.toString(), clazz.getName());
         classGen.getParser().reportError(2, err);
      }

   }
}

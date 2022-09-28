package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class BooleanExpr extends Expression {
   private boolean _value;

   public BooleanExpr(boolean value) {
      this._value = value;
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      super._type = Type.Boolean;
      return super._type;
   }

   public String toString() {
      return this._value ? "true()" : "false()";
   }

   public boolean getValue() {
      return this._value;
   }

   public boolean contextDependent() {
      return false;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      il.append((CompoundInstruction)(new PUSH(cpg, this._value)));
   }

   public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen) {
      InstructionList il = methodGen.getInstructionList();
      if (this._value) {
         il.append(InstructionConstants.NOP);
      } else {
         super._falseList.add(il.append((BranchInstruction)(new GOTO((InstructionHandle)null))));
      }

   }
}

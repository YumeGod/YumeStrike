package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;

final class NameCall extends NameBase {
   public NameCall(QName fname) {
      super(fname);
   }

   public NameCall(QName fname, Vector arguments) {
      super(fname, arguments);
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      int getName = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNodeNameX", "(I)Ljava/lang/String;");
      super.translate(classGen, methodGen);
      il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(getName, 2)));
   }
}

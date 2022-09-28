package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;

final class LocalNameCall extends NameBase {
   public LocalNameCall(QName fname) {
      super(fname);
   }

   public LocalNameCall(QName fname, Vector arguments) {
      super(fname, arguments);
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      int getNodeName = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNodeName", "(I)Ljava/lang/String;");
      int getLocalName = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "getLocalName", "(Ljava/lang/String;)Ljava/lang/String;");
      super.translate(classGen, methodGen);
      il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(getNodeName, 2)));
      il.append((org.apache.bcel.generic.Instruction)(new INVOKESTATIC(getLocalName)));
   }
}

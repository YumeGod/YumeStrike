package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.runtime.BasisLibrary;

final class ParameterRef extends VariableRefBase {
   QName _name = null;

   public ParameterRef(Param param) {
      super(param);
      this._name = param._name;
   }

   public String toString() {
      return "parameter-ref(" + super._variable.getName() + '/' + super._variable.getType() + ')';
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      String name = BasisLibrary.mapQNameToJavaName(this._name.toString());
      String signature = super._type.toSignature();
      if (super._variable.isLocal()) {
         if (classGen.isExternal()) {
            Closure variableClosure;
            for(variableClosure = super._closure; variableClosure != null && !variableClosure.inInnerClass(); variableClosure = variableClosure.getParentClosure()) {
            }

            if (variableClosure != null) {
               il.append((org.apache.bcel.generic.Instruction)InstructionConstants.ALOAD_0);
               il.append((org.apache.bcel.generic.Instruction)(new GETFIELD(cpg.addFieldref(variableClosure.getInnerClassName(), name, signature))));
            } else {
               il.append(super._variable.loadInstruction());
               super._variable.removeReference(this);
            }
         } else {
            il.append(super._variable.loadInstruction());
            super._variable.removeReference(this);
         }
      } else {
         String className = classGen.getClassName();
         il.append(classGen.loadTranslet());
         if (classGen.isExternal()) {
            il.append((org.apache.bcel.generic.Instruction)(new CHECKCAST(cpg.addClass(className))));
         }

         il.append((org.apache.bcel.generic.Instruction)(new GETFIELD(cpg.addFieldref(className, name, signature))));
      }

      if (super._variable.getType() instanceof NodeSetType) {
         int clone = cpg.addInterfaceMethodref("org.apache.xml.dtm.DTMAxisIterator", "cloneIterator", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(clone, 1)));
      }

   }
}

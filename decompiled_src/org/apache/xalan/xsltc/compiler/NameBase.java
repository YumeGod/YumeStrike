package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

class NameBase extends FunctionCall {
   private Expression _param = null;
   private Type _paramType;

   public NameBase(QName fname) {
      super(fname);
      this._paramType = Type.Node;
   }

   public NameBase(QName fname, Vector arguments) {
      super(fname, arguments);
      this._paramType = Type.Node;
      this._param = this.argument(0);
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      switch (this.argumentCount()) {
         case 0:
            this._paramType = Type.Node;
            break;
         case 1:
            this._paramType = this._param.typeCheck(stable);
            break;
         default:
            throw new TypeCheckError(this);
      }

      if (this._paramType != Type.NodeSet && this._paramType != Type.Node && this._paramType != Type.Reference) {
         throw new TypeCheckError(this);
      } else {
         return super._type = Type.String;
      }
   }

   public Type getType() {
      return super._type;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      il.append(methodGen.loadDOM());
      if (this.argumentCount() == 0) {
         il.append(methodGen.loadContextNode());
      } else if (this._paramType == Type.Node) {
         this._param.translate(classGen, methodGen);
      } else if (this._paramType == Type.Reference) {
         this._param.translate(classGen, methodGen);
         il.append((org.apache.bcel.generic.Instruction)(new INVOKESTATIC(cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "referenceToNodeSet", "(Ljava/lang/Object;)Lorg/apache/xml/dtm/DTMAxisIterator;"))));
         il.append(methodGen.nextNode());
      } else {
         this._param.translate(classGen, methodGen);
         this._param.startIterator(classGen, methodGen);
         il.append(methodGen.nextNode());
      }

   }
}

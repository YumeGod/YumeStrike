package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFNULL;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class Copy extends Instruction {
   private UseAttributeSets _useSets;

   public void parseContents(Parser parser) {
      String useSets = this.getAttribute("use-attribute-sets");
      if (useSets.length() > 0) {
         if (!Util.isValidQNames(useSets)) {
            ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", useSets, this);
            parser.reportError(3, err);
         }

         this._useSets = new UseAttributeSets(useSets, parser);
      }

      this.parseChildren(parser);
   }

   public void display(int indent) {
      this.indent(indent);
      Util.println("Copy");
      this.indent(indent + 4);
      this.displayContents(indent + 4);
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      if (this._useSets != null) {
         this._useSets.typeCheck(stable);
      }

      this.typeCheckContents(stable);
      return Type.Void;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      LocalVariableGen name = methodGen.addLocalVariable2("name", Util.getJCRefType("Ljava/lang/String;"), il.getEnd());
      LocalVariableGen length = methodGen.addLocalVariable2("length", Util.getJCRefType("I"), il.getEnd());
      il.append(methodGen.loadDOM());
      il.append(methodGen.loadCurrentNode());
      il.append(methodGen.loadHandler());
      int cpy = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "shallowCopy", "(ILorg/apache/xml/serializer/SerializationHandler;)Ljava/lang/String;");
      il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(cpy, 3)));
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
      il.append((org.apache.bcel.generic.Instruction)(new ASTORE(name.getIndex())));
      BranchHandle ifBlock1 = il.append((BranchInstruction)(new IFNULL((InstructionHandle)null)));
      il.append((org.apache.bcel.generic.Instruction)(new ALOAD(name.getIndex())));
      int lengthMethod = cpg.addMethodref("java.lang.String", "length", "()I");
      il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(lengthMethod)));
      il.append((org.apache.bcel.generic.Instruction)(new ISTORE(length.getIndex())));
      if (this._useSets != null) {
         SyntaxTreeNode parent = this.getParent();
         if (!(parent instanceof LiteralElement) && !(parent instanceof LiteralElement)) {
            il.append((org.apache.bcel.generic.Instruction)(new ILOAD(length.getIndex())));
            BranchHandle ifBlock2 = il.append((BranchInstruction)(new IFEQ((InstructionHandle)null)));
            this._useSets.translate(classGen, methodGen);
            ifBlock2.setTarget(il.append(InstructionConstants.NOP));
         } else {
            this._useSets.translate(classGen, methodGen);
         }
      }

      this.translateContents(classGen, methodGen);
      il.append((org.apache.bcel.generic.Instruction)(new ILOAD(length.getIndex())));
      BranchHandle ifBlock3 = il.append((BranchInstruction)(new IFEQ((InstructionHandle)null)));
      il.append(methodGen.loadHandler());
      il.append((org.apache.bcel.generic.Instruction)(new ALOAD(name.getIndex())));
      il.append(methodGen.endElement());
      InstructionHandle end = il.append(InstructionConstants.NOP);
      ifBlock1.setTarget(end);
      ifBlock3.setTarget(end);
      methodGen.removeLocalVariable(name);
      methodGen.removeLocalVariable(length);
   }
}

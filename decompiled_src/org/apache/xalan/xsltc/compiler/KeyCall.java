package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFGT;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class KeyCall extends FunctionCall {
   private Expression _name;
   private Expression _value;
   private Type _valueType;
   private QName _resolvedQName = null;

   public KeyCall(QName fname, Vector arguments) {
      super(fname, arguments);
      switch (this.argumentCount()) {
         case 1:
            this._name = null;
            this._value = this.argument(0);
            break;
         case 2:
            this._name = this.argument(0);
            this._value = this.argument(1);
            break;
         default:
            this._name = this._value = null;
      }

   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      Type returnType = super.typeCheck(stable);
      if (this._name != null) {
         Type nameType = this._name.typeCheck(stable);
         if (this._name instanceof LiteralExpr) {
            LiteralExpr literal = (LiteralExpr)this._name;
            this._resolvedQName = this.getParser().getQNameIgnoreDefaultNs(literal.getValue());
         } else if (!(nameType instanceof StringType)) {
            this._name = new CastExpr(this._name, Type.String);
         }
      }

      this._valueType = this._value.typeCheck(stable);
      if (this._valueType != Type.NodeSet && this._valueType != Type.String) {
         this._value = new CastExpr(this._value, Type.String);
      }

      return returnType;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      int getNodeHandle = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNodeHandle", "(I)I");
      int dupInit = cpg.addMethodref("org.apache.xalan.xsltc.dom.DupFilterIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;)V");
      LocalVariableGen keyIterator = this.translateCall(classGen, methodGen);
      il.append((org.apache.bcel.generic.Instruction)(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.DupFilterIterator"))));
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
      il.append((org.apache.bcel.generic.Instruction)(new ALOAD(keyIterator.getIndex())));
      il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(dupInit)));
   }

   private LocalVariableGen translateCall(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      int getNodeValue = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getStringValueX", "(I)Ljava/lang/String;");
      int getKeyIndex = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "getKeyIndex", "(Ljava/lang/String;)Lorg/apache/xalan/xsltc/dom/KeyIndex;");
      int lookupId = cpg.addMethodref("org/apache/xalan/xsltc/dom/KeyIndex", "lookupId", "(Ljava/lang/Object;)V");
      int lookupKey = cpg.addMethodref("org/apache/xalan/xsltc/dom/KeyIndex", "lookupKey", "(Ljava/lang/Object;)V");
      int merge = cpg.addMethodref("org/apache/xalan/xsltc/dom/KeyIndex", "merge", "(Lorg/apache/xalan/xsltc/dom/KeyIndex;)V");
      int indexConstructor = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "createKeyIndex", "()Lorg/apache/xalan/xsltc/dom/KeyIndex;");
      int keyDom = cpg.addMethodref("org.apache.xalan.xsltc.dom.KeyIndex", "setDom", "(Lorg/apache/xalan/xsltc/DOM;)V");
      LocalVariableGen returnIndex = methodGen.addLocalVariable("returnIndex", Util.getJCRefType("Lorg/apache/xalan/xsltc/dom/KeyIndex;"), il.getEnd(), (InstructionHandle)null);
      LocalVariableGen searchIndex = methodGen.addLocalVariable("searchIndex", Util.getJCRefType("Lorg/apache/xalan/xsltc/dom/KeyIndex;"), il.getEnd(), (InstructionHandle)null);
      if (this._valueType == Type.NodeSet) {
         il.append(methodGen.loadCurrentNode());
         il.append(methodGen.loadIterator());
         this._value.translate(classGen, methodGen);
         this._value.startIterator(classGen, methodGen);
         il.append(methodGen.storeIterator());
         il.append(classGen.loadTranslet());
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(indexConstructor)));
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append(methodGen.loadDOM());
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(keyDom)));
         il.append((org.apache.bcel.generic.Instruction)(new ASTORE(returnIndex.getIndex())));
         il.append(classGen.loadTranslet());
         if (this._name == null) {
            il.append((CompoundInstruction)(new PUSH(cpg, "##id")));
         } else if (this._resolvedQName != null) {
            il.append((CompoundInstruction)(new PUSH(cpg, this._resolvedQName.toString())));
         } else {
            this._name.translate(classGen, methodGen);
         }

         il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(getKeyIndex)));
         il.append((org.apache.bcel.generic.Instruction)(new ASTORE(searchIndex.getIndex())));
         BranchHandle nextNode = il.append((BranchInstruction)(new GOTO((InstructionHandle)null)));
         InstructionHandle loop = il.append(InstructionConstants.NOP);
         il.append((org.apache.bcel.generic.Instruction)(new ALOAD(returnIndex.getIndex())));
         il.append((org.apache.bcel.generic.Instruction)(new ALOAD(searchIndex.getIndex())));
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append(methodGen.loadDOM());
         il.append(methodGen.loadCurrentNode());
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(getNodeValue, 2)));
         if (this._name == null) {
            il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(lookupId)));
         } else {
            il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(lookupKey)));
         }

         il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(merge)));
         nextNode.setTarget(il.append(methodGen.loadIterator()));
         il.append(methodGen.nextNode());
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append(methodGen.storeCurrentNode());
         il.append((BranchInstruction)(new IFGT(loop)));
         il.append(methodGen.storeIterator());
         il.append(methodGen.storeCurrentNode());
      } else {
         il.append(classGen.loadTranslet());
         if (this._name == null) {
            il.append((CompoundInstruction)(new PUSH(cpg, "##id")));
         } else if (this._resolvedQName != null) {
            il.append((CompoundInstruction)(new PUSH(cpg, this._resolvedQName.toString())));
         } else {
            this._name.translate(classGen, methodGen);
         }

         il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(getKeyIndex)));
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         this._value.translate(classGen, methodGen);
         if (this._name == null) {
            il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(lookupId)));
         } else {
            il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(lookupKey)));
         }

         il.append((org.apache.bcel.generic.Instruction)(new ASTORE(returnIndex.getIndex())));
      }

      return returnIndex;
   }
}

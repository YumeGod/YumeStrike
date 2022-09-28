package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFGE;
import org.apache.bcel.generic.IFGT;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XML11Char;

final class Key extends TopLevelElement {
   private QName _name;
   private Pattern _match;
   private Expression _use;
   private Type _useType;

   public void parseContents(Parser parser) {
      String name = this.getAttribute("name");
      if (!XML11Char.isXML11ValidQName(name)) {
         ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", name, this);
         parser.reportError(3, err);
      }

      this._name = parser.getQNameIgnoreDefaultNs(name);
      this._match = parser.parsePattern(this, "match", (String)null);
      this._use = parser.parseExpression(this, "use", (String)null);
      if (this._name == null) {
         this.reportError(this, parser, "REQUIRED_ATTR_ERR", "name");
      } else if (this._match.isDummy()) {
         this.reportError(this, parser, "REQUIRED_ATTR_ERR", "match");
      } else if (this._use.isDummy()) {
         this.reportError(this, parser, "REQUIRED_ATTR_ERR", "use");
      }
   }

   public String getName() {
      return this._name.toString();
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      this._match.typeCheck(stable);
      this._useType = this._use.typeCheck(stable);
      if (!(this._useType instanceof StringType) && !(this._useType instanceof NodeSetType)) {
         this._use = new CastExpr(this._use, Type.String);
      }

      return Type.Void;
   }

   public void traverseNodeSet(ClassGenerator classGen, MethodGenerator methodGen, int buildKeyIndex) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      int getNodeValue = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getStringValueX", "(I)Ljava/lang/String;");
      int getNodeIdent = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNodeIdent", "(I)I");
      int keyDom = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "setKeyIndexDom", "(Ljava/lang/String;Lorg/apache/xalan/xsltc/DOM;)V");
      LocalVariableGen parentNode = methodGen.addLocalVariable("parentNode", Util.getJCRefType("I"), il.getEnd(), (InstructionHandle)null);
      il.append((org.apache.bcel.generic.Instruction)(new ISTORE(parentNode.getIndex())));
      il.append(methodGen.loadDOM());
      il.append((org.apache.bcel.generic.Instruction)(new ILOAD(parentNode.getIndex())));
      il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(getNodeIdent, 2)));
      il.append((org.apache.bcel.generic.Instruction)(new ISTORE(parentNode.getIndex())));
      il.append(methodGen.loadCurrentNode());
      il.append(methodGen.loadIterator());
      this._use.translate(classGen, methodGen);
      this._use.startIterator(classGen, methodGen);
      il.append(methodGen.storeIterator());
      BranchHandle nextNode = il.append((BranchInstruction)(new GOTO((InstructionHandle)null)));
      InstructionHandle loop = il.append(InstructionConstants.NOP);
      il.append(classGen.loadTranslet());
      il.append((CompoundInstruction)(new PUSH(cpg, this._name.toString())));
      il.append((org.apache.bcel.generic.Instruction)(new ILOAD(parentNode.getIndex())));
      il.append(methodGen.loadDOM());
      il.append(methodGen.loadCurrentNode());
      il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(getNodeValue, 2)));
      il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(buildKeyIndex)));
      il.append(classGen.loadTranslet());
      il.append((CompoundInstruction)(new PUSH(cpg, this.getName())));
      il.append(methodGen.loadDOM());
      il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(keyDom)));
      nextNode.setTarget(il.append(methodGen.loadIterator()));
      il.append(methodGen.nextNode());
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
      il.append(methodGen.storeCurrentNode());
      il.append((BranchInstruction)(new IFGE(loop)));
      il.append(methodGen.storeIterator());
      il.append(methodGen.storeCurrentNode());
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      int current = methodGen.getLocalIndex("current");
      int key = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "buildKeyIndex", "(Ljava/lang/String;ILjava/lang/Object;)V");
      int keyDom = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "setKeyIndexDom", "(Ljava/lang/String;Lorg/apache/xalan/xsltc/DOM;)V");
      int getNodeIdent = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNodeIdent", "(I)I");
      int git = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getAxisIterator", "(I)Lorg/apache/xml/dtm/DTMAxisIterator;");
      il.append(methodGen.loadCurrentNode());
      il.append(methodGen.loadIterator());
      il.append(methodGen.loadDOM());
      il.append((CompoundInstruction)(new PUSH(cpg, 4)));
      il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(git, 2)));
      il.append(methodGen.loadCurrentNode());
      il.append(methodGen.setStartNode());
      il.append(methodGen.storeIterator());
      BranchHandle nextNode = il.append((BranchInstruction)(new GOTO((InstructionHandle)null)));
      InstructionHandle loop = il.append(InstructionConstants.NOP);
      il.append(methodGen.loadCurrentNode());
      this._match.translate(classGen, methodGen);
      this._match.synthesize(classGen, methodGen);
      BranchHandle skipNode = il.append((BranchInstruction)(new IFEQ((InstructionHandle)null)));
      if (this._useType instanceof NodeSetType) {
         il.append(methodGen.loadCurrentNode());
         this.traverseNodeSet(classGen, methodGen, key);
      } else {
         il.append(classGen.loadTranslet());
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append((CompoundInstruction)(new PUSH(cpg, this._name.toString())));
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP_X1);
         il.append(methodGen.loadCurrentNode());
         this._use.translate(classGen, methodGen);
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.SWAP);
         il.append(methodGen.loadDOM());
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.SWAP);
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(getNodeIdent, 2)));
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.SWAP);
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(key)));
         il.append(methodGen.loadDOM());
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(keyDom)));
      }

      InstructionHandle skip = il.append(InstructionConstants.NOP);
      il.append(methodGen.loadIterator());
      il.append(methodGen.nextNode());
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
      il.append(methodGen.storeCurrentNode());
      il.append((BranchInstruction)(new IFGT(loop)));
      il.append(methodGen.storeIterator());
      il.append(methodGen.storeCurrentNode());
      nextNode.setTarget(skip);
      skipNode.setTarget(skip);
   }
}

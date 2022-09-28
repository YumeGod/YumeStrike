package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.FlowList;

public final class ResultTreeType extends Type {
   private final String _methodName;

   protected ResultTreeType() {
      this._methodName = null;
   }

   public ResultTreeType(String methodName) {
      this._methodName = methodName;
   }

   public String toString() {
      return "result-tree";
   }

   public boolean identicalTo(Type other) {
      return other instanceof ResultTreeType;
   }

   public String toSignature() {
      return "Lorg/apache/xalan/xsltc/DOM;";
   }

   public org.apache.bcel.generic.Type toJCType() {
      return Util.getJCRefType(this.toSignature());
   }

   public String getMethodName() {
      return this._methodName;
   }

   public boolean implementedAsMethod() {
      return this._methodName != null;
   }

   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type) {
      if (type == Type.String) {
         this.translateTo(classGen, methodGen, (StringType)type);
      } else if (type == Type.Boolean) {
         this.translateTo(classGen, methodGen, (BooleanType)type);
      } else if (type == Type.Real) {
         this.translateTo(classGen, methodGen, (RealType)type);
      } else if (type == Type.NodeSet) {
         this.translateTo(classGen, methodGen, (NodeSetType)type);
      } else if (type == Type.Reference) {
         this.translateTo(classGen, methodGen, (ReferenceType)type);
      } else if (type == Type.Object) {
         this.translateTo(classGen, methodGen, (ObjectType)type);
      } else {
         ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", this.toString(), type.toString());
         classGen.getParser().reportError(2, err);
      }

   }

   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      il.append((Instruction)InstructionConstants.POP);
      il.append(InstructionConstants.ICONST_1);
   }

   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      if (this._methodName == null) {
         int index = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getStringValue", "()Ljava/lang/String;");
         il.append((Instruction)(new INVOKEINTERFACE(index, 1)));
      } else {
         String className = classGen.getClassName();
         int current = methodGen.getLocalIndex("current");
         il.append(classGen.loadTranslet());
         if (classGen.isExternal()) {
            il.append((Instruction)(new CHECKCAST(cpg.addClass(className))));
         }

         il.append((Instruction)InstructionConstants.DUP);
         il.append((Instruction)(new GETFIELD(cpg.addFieldref(className, "_dom", "Lorg/apache/xalan/xsltc/DOM;"))));
         int index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.StringValueHandler", "<init>", "()V");
         il.append((Instruction)(new NEW(cpg.addClass("org.apache.xalan.xsltc.runtime.StringValueHandler"))));
         il.append((Instruction)InstructionConstants.DUP);
         il.append((Instruction)InstructionConstants.DUP);
         il.append((Instruction)(new INVOKESPECIAL(index)));
         LocalVariableGen handler = methodGen.addLocalVariable("rt_to_string_handler", Util.getJCRefType("Lorg/apache/xalan/xsltc/runtime/StringValueHandler;"), (InstructionHandle)null, (InstructionHandle)null);
         il.append((Instruction)(new ASTORE(handler.getIndex())));
         index = cpg.addMethodref(className, this._methodName, "(Lorg/apache/xalan/xsltc/DOM;Lorg/apache/xml/serializer/SerializationHandler;)V");
         il.append((Instruction)(new INVOKEVIRTUAL(index)));
         il.append((Instruction)(new ALOAD(handler.getIndex())));
         index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.StringValueHandler", "getValue", "()Ljava/lang/String;");
         il.append((Instruction)(new INVOKEVIRTUAL(index)));
      }

   }

   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, RealType type) {
      this.translateTo(classGen, methodGen, Type.String);
      Type.String.translateTo(classGen, methodGen, Type.Real);
   }

   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ReferenceType type) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      if (this._methodName == null) {
         il.append(InstructionConstants.NOP);
      } else {
         String className = classGen.getClassName();
         int current = methodGen.getLocalIndex("current");
         il.append(classGen.loadTranslet());
         if (classGen.isExternal()) {
            il.append((Instruction)(new CHECKCAST(cpg.addClass(className))));
         }

         il.append(methodGen.loadDOM());
         il.append(methodGen.loadDOM());
         int index = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getResultTreeFrag", "(IZ)Lorg/apache/xalan/xsltc/DOM;");
         il.append((CompoundInstruction)(new PUSH(cpg, 32)));
         il.append((CompoundInstruction)(new PUSH(cpg, false)));
         il.append((Instruction)(new INVOKEINTERFACE(index, 3)));
         il.append((Instruction)InstructionConstants.DUP);
         LocalVariableGen newDom = methodGen.addLocalVariable("rt_to_reference_dom", Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), (InstructionHandle)null, (InstructionHandle)null);
         il.append((Instruction)(new CHECKCAST(cpg.addClass("Lorg/apache/xalan/xsltc/DOM;"))));
         il.append((Instruction)(new ASTORE(newDom.getIndex())));
         index = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getOutputDomBuilder", "()Lorg/apache/xml/serializer/SerializationHandler;");
         il.append((Instruction)(new INVOKEINTERFACE(index, 1)));
         il.append((Instruction)InstructionConstants.DUP);
         il.append((Instruction)InstructionConstants.DUP);
         LocalVariableGen domBuilder = methodGen.addLocalVariable("rt_to_reference_handler", Util.getJCRefType("Lorg/apache/xml/serializer/SerializationHandler;"), (InstructionHandle)null, (InstructionHandle)null);
         il.append((Instruction)(new ASTORE(domBuilder.getIndex())));
         index = cpg.addInterfaceMethodref("org.apache.xml.serializer.SerializationHandler", "startDocument", "()V");
         il.append((Instruction)(new INVOKEINTERFACE(index, 1)));
         index = cpg.addMethodref(className, this._methodName, "(Lorg/apache/xalan/xsltc/DOM;Lorg/apache/xml/serializer/SerializationHandler;)V");
         il.append((Instruction)(new INVOKEVIRTUAL(index)));
         il.append((Instruction)(new ALOAD(domBuilder.getIndex())));
         index = cpg.addInterfaceMethodref("org.apache.xml.serializer.SerializationHandler", "endDocument", "()V");
         il.append((Instruction)(new INVOKEINTERFACE(index, 1)));
         il.append((Instruction)(new ALOAD(newDom.getIndex())));
      }

   }

   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, NodeSetType type) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      il.append((Instruction)InstructionConstants.DUP);
      il.append(classGen.loadTranslet());
      il.append((Instruction)(new GETFIELD(cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "namesArray", "[Ljava/lang/String;"))));
      il.append(classGen.loadTranslet());
      il.append((Instruction)(new GETFIELD(cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "urisArray", "[Ljava/lang/String;"))));
      il.append(classGen.loadTranslet());
      il.append((Instruction)(new GETFIELD(cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "typesArray", "[I"))));
      il.append(classGen.loadTranslet());
      il.append((Instruction)(new GETFIELD(cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "namespaceArray", "[Ljava/lang/String;"))));
      int mapping = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "setupMapping", "([Ljava/lang/String;[Ljava/lang/String;[I[Ljava/lang/String;)V");
      il.append((Instruction)(new INVOKEINTERFACE(mapping, 5)));
      il.append((Instruction)InstructionConstants.DUP);
      int iter = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getIterator", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
      il.append((Instruction)(new INVOKEINTERFACE(iter, 1)));
   }

   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ObjectType type) {
      methodGen.getInstructionList().append(InstructionConstants.NOP);
   }

   public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type) {
      InstructionList il = methodGen.getInstructionList();
      this.translateTo(classGen, methodGen, Type.Boolean);
      return new FlowList(il.append((BranchInstruction)(new IFEQ((InstructionHandle)null))));
   }

   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
      String className = clazz.getName();
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      int index;
      if (className.equals("org.w3c.dom.Node")) {
         this.translateTo(classGen, methodGen, Type.NodeSet);
         index = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "makeNode", "(Lorg/apache/xml/dtm/DTMAxisIterator;)Lorg/w3c/dom/Node;");
         il.append((Instruction)(new INVOKEINTERFACE(index, 2)));
      } else if (className.equals("org.w3c.dom.NodeList")) {
         this.translateTo(classGen, methodGen, Type.NodeSet);
         index = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "makeNodeList", "(Lorg/apache/xml/dtm/DTMAxisIterator;)Lorg/w3c/dom/NodeList;");
         il.append((Instruction)(new INVOKEINTERFACE(index, 2)));
      } else if (className.equals("java.lang.Object")) {
         il.append(InstructionConstants.NOP);
      } else if (className.equals("java.lang.String")) {
         this.translateTo(classGen, methodGen, Type.String);
      } else {
         ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", this.toString(), className);
         classGen.getParser().reportError(2, err);
      }

   }

   public void translateBox(ClassGenerator classGen, MethodGenerator methodGen) {
      this.translateTo(classGen, methodGen, Type.Reference);
   }

   public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen) {
      methodGen.getInstructionList().append(InstructionConstants.NOP);
   }

   public String getClassName() {
      return "org.apache.xalan.xsltc.DOM";
   }

   public Instruction LOAD(int slot) {
      return new ALOAD(slot);
   }

   public Instruction STORE(int slot) {
      return new ASTORE(slot);
   }
}

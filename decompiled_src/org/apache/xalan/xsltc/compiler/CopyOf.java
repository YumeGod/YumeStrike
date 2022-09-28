package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.ResultTreeType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class CopyOf extends Instruction {
   private Expression _select;

   public void display(int indent) {
      this.indent(indent);
      Util.println("CopyOf");
      this.indent(indent + 4);
      Util.println("select " + this._select.toString());
   }

   public void parseContents(Parser parser) {
      this._select = parser.parseExpression(this, "select", (String)null);
      if (this._select.isDummy()) {
         this.reportError(this, parser, "REQUIRED_ATTR_ERR", "select");
      }
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      Type tselect = this._select.typeCheck(stable);
      if (!(tselect instanceof NodeType) && !(tselect instanceof NodeSetType) && !(tselect instanceof ReferenceType) && !(tselect instanceof ResultTreeType)) {
         this._select = new CastExpr(this._select, Type.String);
      }

      return Type.Void;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      Type tselect = this._select.getType();
      String CPY1_SIG = "(Lorg/apache/xml/dtm/DTMAxisIterator;Lorg/apache/xml/serializer/SerializationHandler;)V";
      int cpy1 = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "copy", "(Lorg/apache/xml/dtm/DTMAxisIterator;Lorg/apache/xml/serializer/SerializationHandler;)V");
      String CPY2_SIG = "(ILorg/apache/xml/serializer/SerializationHandler;)V";
      int cpy2 = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "copy", "(ILorg/apache/xml/serializer/SerializationHandler;)V");
      String getDoc_SIG = "()I";
      int getDoc = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getDocument", "()I");
      if (tselect instanceof NodeSetType) {
         il.append(methodGen.loadDOM());
         this._select.translate(classGen, methodGen);
         this._select.startIterator(classGen, methodGen);
         il.append(methodGen.loadHandler());
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(cpy1, 3)));
      } else if (tselect instanceof NodeType) {
         il.append(methodGen.loadDOM());
         this._select.translate(classGen, methodGen);
         il.append(methodGen.loadHandler());
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(cpy2, 3)));
      } else if (tselect instanceof ResultTreeType) {
         this._select.translate(classGen, methodGen);
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(getDoc, 1)));
         il.append(methodGen.loadHandler());
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(cpy2, 3)));
      } else if (tselect instanceof ReferenceType) {
         this._select.translate(classGen, methodGen);
         il.append(methodGen.loadHandler());
         il.append(methodGen.loadCurrentNode());
         il.append(methodGen.loadDOM());
         int copy = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "copy", "(Ljava/lang/Object;Lorg/apache/xml/serializer/SerializationHandler;ILorg/apache/xalan/xsltc/DOM;)V");
         il.append((org.apache.bcel.generic.Instruction)(new INVOKESTATIC(copy)));
      } else {
         il.append(classGen.loadTranslet());
         this._select.translate(classGen, methodGen);
         il.append(methodGen.loadHandler());
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "characters", "(Ljava/lang/String;Lorg/apache/xml/serializer/SerializationHandler;)V"))));
      }

   }
}
